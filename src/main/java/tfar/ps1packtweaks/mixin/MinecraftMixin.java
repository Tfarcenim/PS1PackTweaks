package tfar.ps1packtweaks.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.WorldStem;
import net.minecraft.world.level.Level;
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
                             boolean pVanillaOnly, @Coerce Object pSelectionType, boolean creating, CallbackInfo ci){
        PS1PackTweaksClient.changeDisc(pLevelName);
    }

    @Inject(method = "clearLevel(Lnet/minecraft/client/gui/screens/Screen;)V",at = @At("HEAD"))
    private void onLevelExit(Screen pScreen, CallbackInfo ci) {
        if (hasSingleplayerServer()) {
            String name = Minecraft.getInstance().getSingleplayerServer().getWorldData().getLevelName();
            ResourceKey<Level> lastSeen = player.level.dimension();
            PS1PackTweaksClient.map.put(name,lastSeen);
            PS1PackTweaksClient.write();
        }
    }
}
