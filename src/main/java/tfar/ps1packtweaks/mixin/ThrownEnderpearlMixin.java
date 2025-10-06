package tfar.ps1packtweaks.mixin;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.entity.projectile.ThrownEnderpearl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import tfar.ps1packtweaks.Init;

@Mixin(ThrownEnderpearl.class)
public class ThrownEnderpearlMixin {
    @ModifyArg(method = "onHit",at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;addParticle(Lnet/minecraft/core/particles/ParticleOptions;DDDDDD)V"))
    private ParticleOptions changeParticles(ParticleOptions pParticleData) {
        return Init.ModParticleTypes.ENDERMAN;
    }
}
