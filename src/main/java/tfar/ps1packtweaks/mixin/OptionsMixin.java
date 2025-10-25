package tfar.ps1packtweaks.mixin;

import net.minecraft.client.CameraType;
import net.minecraft.client.Options;
import net.minecraft.sounds.SoundSource;
import org.checkerframework.checker.units.qual.A;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tfar.ps1packtweaks.client.PS1PackTweaksClient;

@Mixin(Options.class)
public class OptionsMixin {

    @Shadow private CameraType cameraType;

    @Inject(method = "setCameraType",at = @At("HEAD"))
    private void onPerspectiveChange(CameraType pPointOfView, CallbackInfo ci) {
        PS1PackTweaksClient.onPerspectiveChange(this.cameraType,pPointOfView);
    }

    @Inject(method = "getSoundSourceVolume",at = @At("HEAD"),cancellable = true)
    private void muteNearlyEverything(SoundSource pCategory, CallbackInfoReturnable<Float> cir) {
        if (pCategory != SoundSource.MASTER && PS1PackTweaksClient.lockScreen) {
            cir.setReturnValue(0f);
        }
    }
}
