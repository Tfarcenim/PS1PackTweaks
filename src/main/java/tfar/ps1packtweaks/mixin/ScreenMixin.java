package tfar.ps1packtweaks.mixin;

import net.minecraft.client.gui.screens.OnlineOptionsScreen;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tfar.ps1packtweaks.client.PS1PackTweaksClient;

@Mixin(Screen.class)
public class ScreenMixin {
    @Inject(method = "shouldCloseOnEsc",at = @At("HEAD"),cancellable = true)
    private void preventClose(CallbackInfoReturnable<Boolean> cir) {
        if ((Object)this instanceof OnlineOptionsScreen && PS1PackTweaksClient.lockScreen) {
            cir.setReturnValue(false);
        }
    }
}
