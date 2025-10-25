package tfar.ps1packtweaks.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.DeathScreen;
import net.minecraft.client.gui.screens.OnlineOptionsScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.server.IntegratedServer;
import net.minecraft.client.sounds.SoundManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tfar.ps1packtweaks.client.PS1PackTweaksClient;

import javax.annotation.Nullable;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin {

    @Shadow @Nullable public LocalPlayer player;

    @Inject(method = "clearLevel(Lnet/minecraft/client/gui/screens/Screen;)V",at = @At("HEAD"))
    private void onLevelExit(Screen pScreen, CallbackInfo ci) {

    }

    @Shadow public abstract SoundManager getSoundManager();
    @Shadow public abstract boolean isLocalServer();
    @Shadow private @Nullable IntegratedServer singleplayerServer;

    @Shadow @Nullable public ClientLevel level;

    @Shadow private static Minecraft instance;

    @Inject(at = @At("TAIL"), method = "setScreen")
    public void openScreen(@Nullable Screen screen, CallbackInfo ci) {
        boolean canPauseGame = isLocalServer() && this.singleplayerServer!= null && !this.singleplayerServer.isPublished();
        if (PS1PackTweaksClient.CLIENT.inventorypause_enabled.get() && PS1PackTweaksClient.CLIENT.inventorypause_pause_sounds.get() &&
                PS1PackTweaksClient.isPauseScreen(screen)) {
            if(canPauseGame) {
                this.getSoundManager().pause();
            }
        } else if (screen instanceof DeathScreen) {
            if (canPauseGame) {
                this.getSoundManager().pause();
            }
        }
    }

    @Inject(
            method = {"setLevel"},
            at = {@At("HEAD")}
    )
    private void onSetLevel(ClientLevel levelClient, CallbackInfo callback) {
        PS1PackTweaksClient.onChangeLevel(levelClient);
    }

    @WrapOperation(method = "runTick(Z)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/Screen;isPauseScreen()Z"))
    private boolean pauseGame(Screen instance, Operation<Boolean> original) {
        if (PS1PackTweaksClient.CLIENT.inventorypause_enabled.get() && PS1PackTweaksClient.isPauseScreen(instance)) {
            return true;
        } else if (instance instanceof OnlineOptionsScreen) {
            return false;
        }
        return original.call(instance);
    }
}
