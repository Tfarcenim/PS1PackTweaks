package tfar.ps1packtweaks.mixin;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.entity.monster.EnderMan;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import tfar.ps1packtweaks.Init;

@Mixin(EnderMan.class)
public class EnderManMixin {
    @ModifyArg(method = "aiStep",at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;addParticle(Lnet/minecraft/core/particles/ParticleOptions;DDDDDD)V"))
    private ParticleOptions changeType(ParticleOptions pParticleData) {
        return Init.ModParticleTypes.ENDERMAN;
    }
}
