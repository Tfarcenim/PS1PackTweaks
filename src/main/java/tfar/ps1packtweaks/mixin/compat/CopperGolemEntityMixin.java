package tfar.ps1packtweaks.mixin.compat;

import com.faboslav.friendsandfoes.init.ModBlocks;
import com.minecraftserverzone.coppergolem.CopperGolemEntity;
import com.minecraftserverzone.coppergolem.configs.CopperGolemModConfig;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.event.ForgeEventFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CopperGolemEntity.class)
public abstract class CopperGolemEntityMixin extends TamableAnimal {
    @Shadow
    Block[] buttonblocks;

    @Shadow public float rollHead;

    @Shadow public abstract float getRollHead();

    @Shadow public abstract Integer getCollarColor();

    @Shadow public abstract void setSpeedOfStage(int stage);

    @Shadow public abstract void setCollarColor(int p_30398_);

    @Shadow public abstract int getWaxed();

    @Shadow public abstract void setWaxed(int p_30398_);

    @Shadow public abstract boolean isAngry();

    protected CopperGolemEntityMixin(EntityType<? extends TamableAnimal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Inject(method = "<init>",at = @At("RETURN"))
    private void limitButtons(EntityType p_30369_, Level p_30370_, CallbackInfo ci) {
        this.buttonblocks = new Block[]{ModBlocks.COPPER_BUTTON.get(),
                ModBlocks.EXPOSED_COPPER_BUTTON.get(),
        ModBlocks.WEATHERED_COPPER_BUTTON.get(),
        ModBlocks.OXIDIZED_COPPER_BUTTON.get(),
        ModBlocks.WAXED_COPPER_BUTTON.get(),
        ModBlocks.WAXED_EXPOSED_COPPER_BUTTON.get(),
        ModBlocks.WAXED_WEATHERED_COPPER_BUTTON.get(),
        ModBlocks.WAXED_OXIDIZED_COPPER_BUTTON.get()};
    }

    /**
     * @author Tfar
     * @reason support other axes
     */
    @Overwrite
    public InteractionResult mobInteract(Player p_30412_, InteractionHand p_30413_) {
        ItemStack itemstack = p_30412_.getItemInHand(p_30413_);
        Item item = itemstack.getItem();
        if (this.level.isClientSide) {
            boolean flag;
            if (!itemstack.is(Items.EGG) && !itemstack.is(Items.HONEYCOMB) && !itemstack.canPerformAction(ToolActions.AXE_SCRAPE)) {
                flag = this.isOwnedBy(p_30412_) || this.isTame() || itemstack.is(Items.COPPER_INGOT) && !this.isTame() && !this.isAngry();
                return flag ? InteractionResult.CONSUME : InteractionResult.PASS;
            } else {
                flag = !this.isAngry();
                return flag ? InteractionResult.SUCCESS : InteractionResult.PASS;
            }
        } else if (!itemstack.canPerformAction(ToolActions.AXE_SCRAPE)) {
            if (itemstack.is(Items.HONEYCOMB)) {
                if (this.getWaxed() == 0) {
                    this.setWaxed(1);
                    if (!p_30412_.getAbilities().instabuild) {
                        itemstack.shrink(1);
                    }

                    return InteractionResult.SUCCESS;
                } else {
                    return InteractionResult.FAIL;
                }
            } else if (itemstack.is(Items.EGG)) {
                if (this.getCollarColor() > -1 && this.getCollarColor() < 4) {
                    this.setCollarColor(this.getCollarColor() + 1);
                    this.setSpeedOfStage(this.getCollarColor());
                    if (!p_30412_.getAbilities().instabuild) {
                        itemstack.shrink(1);
                    }

                    return InteractionResult.SUCCESS;
                } else {
                    return InteractionResult.FAIL;
                }
            } else {
                if (itemstack.is(Items.COPPER_INGOT) && this.getRollHead() == 0.0F) {
                    this.rollHead = 20.0F;
                    this.level.broadcastEntityEvent(this, (byte)9);
                }

                if (this.isTame()) {
                    if (this.isFood(itemstack) && this.getHealth() < this.getMaxHealth()) {
                        if (!p_30412_.getAbilities().instabuild) {
                            itemstack.shrink(1);
                        }

                        this.heal((float) CopperGolemModConfig.STATS[2].get());
                        this.gameEvent(GameEvent.MOB_INTERACT, this.eyeBlockPosition());
                        return InteractionResult.SUCCESS;
                    }

                    if (!(item instanceof DyeItem)) {
                        InteractionResult interactionresult = super.mobInteract(p_30412_, p_30413_);
                        if ((!interactionresult.consumesAction() || this.isBaby()) && this.isOwnedBy(p_30412_)) {
                            this.setOrderedToSit(!this.isOrderedToSit());
                            this.jumping = false;
                            this.navigation.stop();
                            this.setTarget(null);
                            return InteractionResult.SUCCESS;
                        }

                        return interactionresult;
                    }
                } else if (itemstack.is(Items.COPPER_INGOT) && !this.isAngry()) {
                    if (!p_30412_.getAbilities().instabuild) {
                        itemstack.shrink(1);
                    }

                    if (this.random.nextInt(3) == 0 && !ForgeEventFactory.onAnimalTame(this, p_30412_)) {
                        this.tame(p_30412_);
                        this.navigation.stop();
                        this.yHeadRot = 0.0F;
                        this.yBodyRot = 0.0F;
                        this.yHeadRotO = 0.0F;
                        this.yBodyRotO = 0.0F;
                        this.setYBodyRot(0.0F);
                        this.setYHeadRot(0.0F);
                        this.setTarget(null);
                        this.setOrderedToSit(true);
                        this.level.broadcastEntityEvent(this, (byte)7);
                    }

                    return InteractionResult.SUCCESS;
                }

                return super.mobInteract(p_30412_, p_30413_);
            }
        } else {
            if (this.getWaxed() == 1) {
                this.setWaxed(0);
            }

            if (this.getCollarColor() > 1 && this.getCollarColor() < 5) {
                this.setCollarColor(this.getCollarColor() - 1);
                this.setSpeedOfStage(this.getCollarColor());
                return InteractionResult.SUCCESS;
            } else {
                return InteractionResult.FAIL;
            }
        }
    }
}
