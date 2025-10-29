package tfar.ps1packtweaks.mixin.compat;

import com.github.NGoedix.watchvideo.client.gui.VideoScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tfar.ps1packtweaks.client.PS1PackTweaksClient;

@Mixin(VideoScreen.class)
public class VideoScreenMixin {
    @Inject(method = "keyPressed",at = @At("HEAD"),cancellable = true)
    private void noClose(int pKeyCode, int pScanCode, int pModifiers, CallbackInfoReturnable<Boolean> cir) {
        if (PS1PackTweaksClient.lockScreen) cir.setReturnValue(false);
    }
}
