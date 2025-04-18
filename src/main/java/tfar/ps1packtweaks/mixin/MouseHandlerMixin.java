package tfar.ps1packtweaks.mixin;

import net.minecraft.client.MouseHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tfar.ps1packtweaks.client.MouseHider;

@Mixin(MouseHandler.class)
public class MouseHandlerMixin {
    @Inject(method = "releaseMouse",at = @At("RETURN"))
    private void afterRelease(CallbackInfo ci) {
        MouseHider.afterRelease();
    }
}
