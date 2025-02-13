package tfar.ps1packtweaks.client;

import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import tfar.ps1packtweaks.Init;

public class PS1PackTweaksClient {

    public static void init(IEventBus bus) {
        bus.addListener(PS1PackTweaksClient::setup);
    }

    static void setup(FMLClientSetupEvent event) {
        EntityRenderers.register(Init.ModEntityTypes.BARNACLE,BarnacleRenderer::new);
    }

}
