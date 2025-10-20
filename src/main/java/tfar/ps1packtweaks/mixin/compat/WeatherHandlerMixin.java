package tfar.ps1packtweaks.mixin.compat;

import com.weathersettings.weather.WeatherHandler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.GameRules;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.server.ServerLifecycleHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WeatherHandler.class)
public class WeatherHandlerMixin {
    @Inject(method = "onServerTick",at = @At("HEAD"),cancellable = true,remap = false)
    private static void respectCycleGamerule(TickEvent.ServerTickEvent event, CallbackInfo ci) {
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        if (!server.getGameRules().getBoolean(GameRules.RULE_WEATHER_CYCLE)) {
            ci.cancel();
        }
    }
}
