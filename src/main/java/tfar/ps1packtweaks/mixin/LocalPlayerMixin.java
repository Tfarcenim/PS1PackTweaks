package tfar.ps1packtweaks.mixin;

import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tfar.ps1packtweaks.client.PS1PackTweaksClient;

@Mixin(LocalPlayer.class)
public class LocalPlayerMixin {

    @Inject(method = "hasEnoughImpulseToStartSprinting",at = @At("RETURN"),cancellable = true)
    private void blockSprint(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(PS1PackTweaksClient.canStartSprinting((LocalPlayer) (Object)this,cir.getReturnValue()));
    }
}
