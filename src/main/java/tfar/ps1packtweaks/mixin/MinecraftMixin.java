package tfar.ps1packtweaks.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.server.IntegratedServer;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.server.WorldStem;
import net.minecraft.world.level.storage.LevelStorageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tfar.ps1packtweaks.client.PS1PackTweaksClient;

import javax.annotation.Nullable;
import java.util.function.Function;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin {
    @Shadow public abstract boolean hasSingleplayerServer();

    @Shadow @Nullable public LocalPlayer player;

    @Inject(method = "doLoadLevel",at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiling/ProfilerFiller;push(Ljava/lang/String;)V"))
    private void onLevelLoad(String pLevelName, Function<LevelStorageSource.LevelStorageAccess, WorldStem.DataPackConfigSupplier> pLevelSaveToDatapackFunction,
                             Function<LevelStorageSource.LevelStorageAccess, WorldStem.WorldDataSupplier> p_205208_,
                             boolean pVanillaOnly, @Coerce Object pSelectionType, boolean creating, CallbackInfo ci) {
        PS1PackTweaksClient.changeDisc(pLevelName);

    }

    @Inject(method = "clearLevel(Lnet/minecraft/client/gui/screens/Screen;)V",at = @At("HEAD"))
    private void onLevelExit(Screen pScreen, CallbackInfo ci) {

    }

    @Shadow public abstract SoundManager getSoundManager();
    @Shadow public abstract boolean isLocalServer();
    @Shadow private @Nullable IntegratedServer singleplayerServer;

    @Inject(at = @At("TAIL"), method = "setScreen")
    public void openScreen(@Nullable Screen screen, CallbackInfo ci) {
        if (PS1PackTweaksClient.CLIENT.inventorypause_enabled.get() && PS1PackTweaksClient.CLIENT.inventorypause_pause_sounds.get() && PS1PackTweaksClient.isPauseScreen(screen)) {
            boolean canPauseGame = isLocalServer() && !this.singleplayerServer.isPublished();
            if(canPauseGame) {
                this.getSoundManager().pause();
            }
        }
    }

    @WrapOperation(method = "runTick(Z)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/Screen;isPauseScreen()Z"))
    private boolean pauseGame(Screen instance, Operation<Boolean> original) {
        if (PS1PackTweaksClient.CLIENT.inventorypause_enabled.get() && PS1PackTweaksClient.isPauseScreen(instance)) {
            return true;
        }
        return original.call(instance);
    }
}
