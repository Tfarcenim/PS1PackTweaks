package tfar.ps1packtweaks.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import tfar.ps1packtweaks.ChatSettings;
import tfar.ps1packtweaks.Init;
import tfar.ps1packtweaks.PS1TweaksConfig;
import tfar.ps1packtweaks.compat.BetterGuiCompassHUD;
import tfar.ps1packtweaks.compat.ModIntegration;

public class PS1PackTweaksClient {

    public static void init(IEventBus bus) {
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT,PS1TweaksConfig.CLIENT_SPEC);
        bus.addListener(PS1PackTweaksClient::setup);
        MinecraftForge.EVENT_BUS.addListener(PS1PackTweaksClient::joinServer);
        MinecraftForge.EVENT_BUS.addListener(MouseHider::startupScreen);
        MinecraftForge.EVENT_BUS.addListener(MouseHider::clientTick);
    }

    static void setup(FMLClientSetupEvent event) {
        EntityRenderers.register(Init.ModEntityTypes.BARNACLE,BarnacleRenderer::new);
        if (ModIntegration.guicompass.loaded) {
            BetterGuiCompassHUD.setup();
        }
    }

    static void joinServer(ClientPlayerNetworkEvent.LoggedInEvent event) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.hasSingleplayerServer()) {
            ChatSettings chatSettings = PS1TweaksConfig.CLIENT.singleplayer_chat_settings.get();
            minecraft.options.chatVisibility = chatSettings.chatVisiblity();
        } else {
            ChatSettings chatSettings = PS1TweaksConfig.CLIENT.multiplayer_chat_settings.get();
            minecraft.options.chatVisibility = chatSettings.chatVisiblity();
        }
    }
}
