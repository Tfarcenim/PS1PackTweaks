package tfar.ps1packtweaks.mixin.compat;

import com.weathersettings.event.EventHandler;
import com.weathersettings.weather.WeatherHandler;
import net.minecraftforge.event.server.ServerStartedEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(EventHandler.class)
public class EventHandlerMixin {
    /**
     * @author
     * @reason
     */
    @Overwrite(remap = false)
    public static void onStart(ServerStartedEvent event) {
        WeatherHandler.onServerStart();
    }
}
