package tfar.ps1packtweaks.mixin;

import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundRespawnPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tfar.ps1packtweaks.client.PS1PackTweaksClient;

@Mixin(ClientPacketListener.class)
public class ClientPacketListenerMixin {
    @Inject(method = "handleRespawn",at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;getId()I"))
    private void changeDiscTest(ClientboundRespawnPacket pPacket, CallbackInfo ci) {
        PS1PackTweaksClient.handle(pPacket.getDimension());
    }
}
