package tfar.ps1packtweaks.entity;

import com.cursedcauldron.wildbackport.client.registry.WBSoundEvents;
import net.mcreator.midnightlurker.procedures.MidnightLurkerAggressiveLoopExternalAnimationsProcedure;
import net.mcreator.midnightlurker.procedures.MidnightLurkerAggressivePlayReturnedAnimationProcedure;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import software.bernie.geckolib3.core.AnimationState;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

public class ScriptedMidnightLurker extends Monster implements IAnimatable {

    private AnimationFactory factory  = GeckoLibUtil.createFactory(this);


    public String animationprocedure = "empty";
    private boolean lastloop;


    public ScriptedMidnightLurker(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        LivingEntity target = getTarget();
        if (target != null) {
            getLookControl().setLookAt(target);
            if (tickCount == HerobrineEntity.CHASE_DELAY) {
                playSound(WBSoundEvents.WARDEN_ROAR,4,1);
            }
            if (tickCount > HerobrineEntity.CHASE_DELAY) {
                getNavigation().moveTo(target,1);
                if (!target.isAlive() || tickCount > HerobrineEntity.DESPAWN_TIME) {
                    discard();
                }
            }
        }
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "movement", 4.0F, this::movementPredicate));
        data.addAnimationController(new AnimationController<>(this, "procedure", 4.0F, this::procedurePredicate));
    }

    private <E extends IAnimatable> PlayState movementPredicate(AnimationEvent<E> event) {
        if (!this.animationprocedure.equals("empty")) {
            return PlayState.STOP;
        } else if ((event.isMoving() || !(event.getLimbSwingAmount() > -0.15F) || !(event.getLimbSwingAmount() < 0.15F)) && !this.isAggressive()) {
            event.getController().setAnimation((new AnimationBuilder()).addAnimation("stalking1", ILoopType.EDefaultLoopTypes.LOOP));
            return PlayState.CONTINUE;
        } else if (this.isInWaterOrBubble()) {
            event.getController().setAnimation((new AnimationBuilder()).addAnimation("swim1", ILoopType.EDefaultLoopTypes.LOOP));
            return PlayState.CONTINUE;
        } else if (this.isAggressive() && event.isMoving()) {
            event.getController().setAnimation((new AnimationBuilder()).addAnimation("running1", ILoopType.EDefaultLoopTypes.LOOP));
            return PlayState.CONTINUE;
        } else {
            event.getController().setAnimation((new AnimationBuilder()).addAnimation("idle1", ILoopType.EDefaultLoopTypes.LOOP));
            return PlayState.CONTINUE;
        }
    }

    private <E extends IAnimatable> PlayState procedurePredicate(AnimationEvent<E> event) {
        Entity entity = this;
        Level world = entity.level;
        double x = entity.getX();
        double y = entity.getY();
        double z = entity.getZ();
        String condition = MidnightLurkerAggressivePlayReturnedAnimationProcedure.execute(world, x, y, z, entity);
        if (!condition.equals("empty")) {
            this.animationprocedure = condition;
        }

        boolean loop = MidnightLurkerAggressiveLoopExternalAnimationsProcedure.execute(world, x, y, z, entity);
        if (!loop && this.lastloop) {
            this.lastloop = false;
            event.getController().setAnimation((new AnimationBuilder()).addAnimation(this.animationprocedure, ILoopType.EDefaultLoopTypes.PLAY_ONCE));
            event.getController().clearAnimationCache();
            return PlayState.STOP;
        } else {
            if (!this.animationprocedure.equals("empty") && event.getController().getAnimationState().equals(AnimationState.Stopped)) {
                if (!loop) {
                    event.getController().setAnimation((new AnimationBuilder()).addAnimation(this.animationprocedure, ILoopType.EDefaultLoopTypes.PLAY_ONCE));
                    if (event.getController().getAnimationState().equals(AnimationState.Stopped)) {
                        this.animationprocedure = "empty";
                        event.getController().markNeedsReload();
                    }
                } else {
                    event.getController().setAnimation((new AnimationBuilder()).addAnimation(this.animationprocedure, ILoopType.EDefaultLoopTypes.LOOP));
                    this.lastloop = true;
                }
            }

            return PlayState.CONTINUE;
        }
    }

    public static AttributeSupplier.Builder createAttributes() {
        AttributeSupplier.Builder builder = Mob.createMobAttributes().add(Attributes.MOVEMENT_SPEED, 0.42).add(Attributes.MAX_HEALTH, 120.0)
                .add(Attributes.ARMOR, 0).add(Attributes.ATTACK_DAMAGE, 6).add(Attributes.FOLLOW_RANGE, 100).add(Attributes.KNOCKBACK_RESISTANCE, 0.7);
        return builder;
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }
}
