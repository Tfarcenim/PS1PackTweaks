package tfar.ps1packtweaks.mixin;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.animal.horse.ZombieHorse;
import net.minecraft.world.item.HorseArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ZombieHorse.class)
public abstract class ZombieHorseMixin extends AbstractHorse {

    @Override
    public boolean canWearArmor() {
        return true;
    }

    public boolean isArmor(ItemStack pStack) {
        return pStack.getItem() instanceof HorseArmorItem;
    }

    protected ZombieHorseMixin(EntityType<? extends AbstractHorse> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }
}
