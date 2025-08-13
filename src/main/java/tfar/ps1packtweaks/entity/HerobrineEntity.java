package tfar.ps1packtweaks.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.random.Weight;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeConfigSpec;
import org.jetbrains.annotations.Nullable;
import tfar.ps1packtweaks.CustomEvents;
import tfar.ps1packtweaks.Init;
import tfar.ps1packtweaks.PS1PackTweaksConfig;
import tfar.ps1packtweaks.entity.goals.BeingLookedAtGoal;
import tfar.ps1packtweaks.entity.goals.LookAtPlayerGoal;
import tfar.ps1packtweaks.entity.goals.LookforPlayerGoal;

import java.util.List;

public class HerobrineEntity extends PathfinderMob implements CanLookAt {
    public HerobrineEntity(EntityType<? extends PathfinderMob> $$0, Level level) {
        super($$0, level);
    }


    public static final int CHASE_DELAY = 50;
    public static final int DESPAWN_TIME = 1000;

    public static final int DISTANCE = 12;

    protected boolean staredAt;

    public Event event;

    long age;
    long lifespan = 1200;
    int ticksLookedAt;

    @Nullable Mob lurker;

    @Nullable
    public Mob getLurker() {
        return lurker;
    }


    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.FOLLOW_RANGE, 96).add(Attributes.MOVEMENT_SPEED, 0.3)
                .add(Attributes.ATTACK_DAMAGE, 3.0)
                .add(Attributes.ARMOR, 2.0);
    }

    /**
     * Checks to see if this player is looking at herobrine
     */
    @Override
    public boolean isLookingAtMe(Player pPlayer) {
        Vec3 lookVec = pPlayer.getViewVector(1.0F).normalize();
        Vec3 vec31 = new Vec3(this.getX() - pPlayer.getX(), this.getEyeY() - pPlayer.getEyeY(), this.getZ() - pPlayer.getZ());
        double d0 = vec31.length();
        vec31 = vec31.normalize();
        double d1 = lookVec.dot(vec31);
        return (d1 > (1.0D - (0.5D / d0))) && pPlayer.hasLineOfSight(this);
    }


    @Override
    public void setStaredAt(boolean staredAt) {
        this.staredAt = staredAt;
    }

    @Override
    public boolean isStaredAt() {
        return staredAt;
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new HerobrineEntity.HerobrineLookatPlayerGoal());
        this.targetSelector.addGoal(1, new BeingLookedAtGoal<>(this, e -> true));
        this.targetSelector.addGoal(2, new LookforPlayerGoal<>(this, e -> true));
    }

    @Override
    public void setTarget(@Nullable LivingEntity living) {
        if (living == null) {
            setStaredAt(false);
        }
        super.setTarget(living);
    }

    @Override
    public void tick() {
        super.tick();
        age++;
        if (age >= lifespan) {
            despawn();
        }
        if (staredAt) {
            ticksLookedAt++;
        }

        LivingEntity target = getTarget();

        if (target != null) {
            if (ticksLookedAt >= 2) {
                if (distanceTo(target) < 24) {
                    performLookedAtCondition(target);
                }
            }

            if (distanceTo(target) < 10) {
                despawn();
            }
        }
    }

    boolean isPerformingEvent;

    void performLookedAtCondition(LivingEntity target) {
        if (event == null || isPerformingEvent) return;
        isPerformingEvent = true;
        switch (event) {
            case VANISH_ON_SEEN -> {
                despawn();
            }
            case RUN_ON_SEEN -> {
                if (target != null) {
                    runAwayFrom(target);
                }
            }
            case TELEPORT_ON_SEEN -> {
                Vec3 p = target.position().add(target.getLookAngle().scale(2));
                teleportTo(p.x, p.y,p.z);
                target.addEffect(new MobEffectInstance(MobEffects.BLINDNESS,200));
            }
            case RUN_FROM_LURKER -> {

                Vec2 lurkerPos = CustomEvents.addPolar(position(),DISTANCE,random.nextInt(360));

                int y = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, (int) lurkerPos.x, (int) lurkerPos.y);

                //playSound(WBSoundEvents.WARDEN_ROAR,4,1);
                this.lurker = Init.ModEntityTypes.SCRIPTED_MIDNIGHT_LURKER.spawn((ServerLevel) level,null,null,null,
                        new BlockPos(lurkerPos.x,y,lurkerPos.y)
                        ,MobSpawnType.EVENT,false,false);

                lurker.setTarget(this);

                //player sees herobrine -> herobrine turns towards entity a few blocks away -> sound plays -> herobrine starts running

            }
        }
    }

    int chaseTime;

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        if (lurker!=null) {

            if (lurker.getDeltaMovement().length() > .15) {
                chaseTime++;
                runAwayFrom(lurker);
                if (chaseTime > DESPAWN_TIME) {
                    discard();
                }
            } else {
                getLookControl().setLookAt(lurker.getX(), lurker.getEyeY(), lurker.getZ());
            }
        }
    }

    public void runAwayFrom(Entity entity) {
        runAwayFrom(entity.position());
    }

    public void runAwayFrom(Vec3 pos) {
        Vec3 diff = position().subtract(pos).normalize().scale(64);
        Vec3 away = position().add(diff.x,diff.y,diff.z);
        getNavigation().moveTo(away.x,away.y,away.z,1.5);
    }

    BlockPos findSafeSignPos(BlockState state) {
        BlockPos pos = blockPosition();
        if (state.canSurvive(level,pos.below())) {
            return pos;
        }

        pos = level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING,pos);

        if (state.canSurvive(level,pos.below())) {
            return pos;
        }
        return null;
    }


    void despawn() {
        if (!level.isClientSide) {
            discard();
        }
    }

    ///note, most messages can only be 10 characters per line
    public static final List<String[]> messages = List.of(
            new String[]{"I'm always",
                    "watching"},
            new String[]{"I know your","secrets"},
            new String[]{"I'm closer","than you","think"},
            new String[]{"I'm behind","every corner"}
    );

    void placeSign() {
        BlockState state = Blocks.BIRCH_SIGN.defaultBlockState();
        BlockPos pos = findSafeSignPos(state);
        if (pos != null && !level.isOutsideBuildHeight(pos)) {
            level.setBlock(pos, state, 3);
            String[] message = messages.get(random.nextInt(messages.size()));
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof SignBlockEntity signBlockEntity) {
                for (int i = 0; i < message.length;i++) {
                    signBlockEntity.setMessage(i, new TextComponent(message[i]));
                }
                signBlockEntity.setChanged();
            }
            level.setBlock(pos.relative(Direction.NORTH), Blocks.REDSTONE_TORCH.defaultBlockState(), 3);
        }
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData, @Nullable CompoundTag pDataTag) {
        event = Event.list.getRandom(random).get();
        return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putLong("age", age);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        age = tag.getLong("age");
    }

    public class HerobrineLookatPlayerGoal extends LookAtPlayerGoal<HerobrineEntity> {

        public HerobrineLookatPlayerGoal() {
            super(HerobrineEntity.this);
        }

        @Override
        public boolean canUse() {
            return super.canUse() && !HerobrineEntity.this.isPerformingEvent;
        }
    }

    public enum Event implements WeightedEntry {
        VANISH_ON_SEEN(PS1PackTweaksConfig.SERVER.herobrineEvent0Weight),
        RUN_ON_SEEN(PS1PackTweaksConfig.SERVER.herobrineEvent1Weight),
        TELEPORT_ON_SEEN(PS1PackTweaksConfig.SERVER.herobrineEvent2Weight),
        RUN_FROM_LURKER(PS1PackTweaksConfig.SERVER.herobrineEvent3Weight);

        public static final WeightedRandomList<Event> list = WeightedRandomList.create(values());

        private final ForgeConfigSpec.IntValue config;

        Event(ForgeConfigSpec.IntValue config) {
            this.config = config;
        }

        @Override
        public Weight getWeight() {
            return Weight.of(config.get());
        }
    }

}
