package tfar.ps1packtweaks.mixin;

import net.minecraft.world.effect.MobEffect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(MobEffect.class)
public interface MobEffectAccess {

    @Accessor @Mutable
    void setColor(int color);
}
