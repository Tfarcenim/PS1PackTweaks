package tfar.ps1packtweaks.mixin;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.entity.projectile.EyeOfEnder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import tfar.ps1packtweaks.Init;

@Mixin(EyeOfEnder.class)
public class EyeOfEnderMixin {
    @ModifyArg(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;addParticle(Lnet/minecraft/core/particles/ParticleOptions;DDDDDD)V", ordinal = 1))
    private ParticleOptions change(ParticleOptions pParticleData) {
        return Init.ModParticleTypes.BLUE_ENDERMAN;
    }
}
