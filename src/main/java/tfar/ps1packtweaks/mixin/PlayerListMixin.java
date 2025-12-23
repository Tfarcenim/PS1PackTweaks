package tfar.ps1packtweaks.mixin;

import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.mojang.authlib.GameProfile;

import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import tfar.ps1packtweaks.PS1PackTweaks;

@Mixin(PlayerList.class)
public class PlayerListMixin {
    @Redirect(method = "placeNewPlayer", allow = 2, require = 2, at = @At(value = "FIELD", target = "Lnet/minecraft/world/level/Level;OVERWORLD:Lnet/minecraft/resources/ResourceKey;"))
    private ResourceKey<Level> redirectPlayerListPlaceNewPlayerGetOverworld() {
        return PS1PackTweaks.redirectPlayerListPlaceNewPlayerGetOverworld((PlayerList) (Object) this);
    }

    @Inject(method = "getPlayerForLogin", at = @At("TAIL"), cancellable = true)
    private void onPlayerListGetPlayerForLogin(GameProfile profile, CallbackInfoReturnable<ServerPlayer> cir) {
        PS1PackTweaks.onPlayerListGetPlayerForLogin((PlayerList) (Object) this, profile, cir);
    }

    @Redirect(method = "respawn", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;overworld()Lnet/minecraft/server/level/ServerLevel;"))
    private ServerLevel redirectPlayerListRespawnGetServerOverworld(MinecraftServer server) {
        return PS1PackTweaks.redirectPlayerListRespawnGetServerOverworld(server);
    }
}
