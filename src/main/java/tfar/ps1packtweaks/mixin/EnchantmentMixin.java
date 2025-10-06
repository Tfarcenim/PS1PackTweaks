package tfar.ps1packtweaks.mixin;

import net.minecraft.world.item.enchantment.Enchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;
import tfar.ps1packtweaks.duck.EnchantmentDuck;

@Mixin(Enchantment.class)
public class EnchantmentMixin implements EnchantmentDuck {

    @Unique
    boolean discoverable = true;
    @Unique
    boolean tradeable = true;

    @Override
    public void setDiscoverable(boolean discoverable) {
        this.discoverable = discoverable;
    }

    @Override
    public void setTradeable(boolean tradeable) {
        this.tradeable = tradeable;
    }

    /**
     * @author Tfar
     * @reason configurable enchants
     */
    @Overwrite
    public boolean isDiscoverable() {
        return discoverable;
    }
    /**
     * @author Tfar
     * @reason configurable enchants
     */
    @Overwrite
    public boolean isTradeable() {
        return tradeable;
    }
}
