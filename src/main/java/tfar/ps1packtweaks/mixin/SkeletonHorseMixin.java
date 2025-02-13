package tfar.ps1packtweaks.mixin;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.animal.horse.SkeletonHorse;
import net.minecraft.world.item.HorseArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(SkeletonHorse.class)
public abstract class SkeletonHorseMixin extends AbstractHorse {

    @Override
    public boolean canWearArmor() {
        return true;
    }

    public boolean isArmor(ItemStack pStack) {
        return pStack.getItem() instanceof HorseArmorItem;
    }

    protected SkeletonHorseMixin(EntityType<? extends AbstractHorse> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

}
