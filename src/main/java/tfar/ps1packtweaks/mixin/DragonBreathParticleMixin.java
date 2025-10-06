package tfar.ps1packtweaks.mixin;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.DragonBreathParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DragonBreathParticle.class)
public abstract class DragonBreathParticleMixin extends TextureSheetParticle {
    protected DragonBreathParticleMixin(ClientLevel pLevel, double pX, double pY, double pZ) {
        super(pLevel, pX, pY, pZ);
    }

    @Inject(method = "<init>",at = @At("RETURN"))
    private void fixColors(ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed, SpriteSet pSprites, CallbackInfo ci) {
        this.rCol = 0;
        this.gCol = Mth.nextFloat(this.random, 0.8235294F, 0.9764706F);
        this.bCol = 0;
    }
}
