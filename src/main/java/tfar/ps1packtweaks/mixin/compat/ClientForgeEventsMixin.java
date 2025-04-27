package tfar.ps1packtweaks.mixin.compat;

import com.nyfaria.nightmare.event.ClientForgeEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(ClientForgeEvents.class)
public class ClientForgeEventsMixin {
    @ModifyArg(method = "onPlayerTick",at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;pauseGame(Z)V"))
    private static boolean fixPause(boolean pPauseOnly) {
        return false;
    }
}
