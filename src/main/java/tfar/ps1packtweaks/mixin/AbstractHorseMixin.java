package tfar.ps1packtweaks.mixin;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.animal.horse.SkeletonHorse;
import net.minecraft.world.entity.animal.horse.ZombieHorse;
import net.minecraft.world.item.HorseArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(AbstractHorse.class)
public abstract class AbstractHorseMixin extends Animal {
    private static final UUID ARMOR_MODIFIER_UUID = UUID.fromString("556E1665-8B10-40C8-8F9D-CF9B1667F295");

    protected AbstractHorseMixin(EntityType<? extends Animal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Shadow public abstract boolean isArmor(ItemStack pStack);

    @Shadow protected SimpleContainer inventory;

    @Inject(method = "addAdditionalSaveData",at = @At("RETURN"))
    private void addExtra(CompoundTag pCompound, CallbackInfo ci) {
        AbstractHorse abstractHorse = (AbstractHorse) (Object)this;
        if (abstractHorse instanceof ZombieHorse || abstractHorse instanceof SkeletonHorse) {
            if (!this.inventory.getItem(1).isEmpty()) {
                pCompound.put("ArmorItem", this.inventory.getItem(1).save(new CompoundTag()));
            }
        }
    }

    @Inject(method = "readAdditionalSaveData",at = @At("RETURN"))
    private void readExtra(CompoundTag pCompound, CallbackInfo ci) {
        AbstractHorse abstractHorse = (AbstractHorse) (Object)this;
        if (abstractHorse instanceof ZombieHorse || abstractHorse instanceof SkeletonHorse) {
            if (pCompound.contains("ArmorItem", 10)) {
                ItemStack itemstack = ItemStack.of(pCompound.getCompound("ArmorItem"));
                if (!itemstack.isEmpty() && this.isArmor(itemstack)) {
                    this.inventory.setItem(1, itemstack);
                }
            }
        }
    }

    @Inject(method = "updateContainerEquipment",at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/horse/AbstractHorse;setFlag(IZ)V"))
    private void onUpdateContainer(CallbackInfo ci) {
        AbstractHorse abstractHorse = (AbstractHorse) (Object)this;
        if (abstractHorse instanceof ZombieHorse || abstractHorse instanceof SkeletonHorse) {
            this.setArmorEquipment(this.inventory.getItem(1));
            this.setDropChance(EquipmentSlot.CHEST, 0.0F);
        }
    }

    private void setArmorEquipment(ItemStack pStack) {
        this.setArmor(pStack);
        if (!this.level.isClientSide) {
            this.getAttribute(Attributes.ARMOR).removeModifier(ARMOR_MODIFIER_UUID);
            if (this.isArmor(pStack)) {
                int i = ((HorseArmorItem)pStack.getItem()).getProtection();
                if (i != 0) {
                    this.getAttribute(Attributes.ARMOR).addTransientModifier(new AttributeModifier(ARMOR_MODIFIER_UUID, "Horse armor bonus", i, AttributeModifier.Operation.ADDITION));
                }
            }
        }
    }

    private void setArmor(ItemStack pStack) {
        this.setItemSlot(EquipmentSlot.CHEST, pStack);
        this.setDropChance(EquipmentSlot.CHEST, 0.0F);
    }
}
