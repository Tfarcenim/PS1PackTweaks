package tfar.ps1packtweaks.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.client.event.ScreenOpenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.lwjgl.glfw.GLFW;
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
        MinecraftForge.EVENT_BUS.addListener(PS1PackTweaksClient::startupScreen);
        MinecraftForge.EVENT_BUS.addListener(PS1PackTweaksClient::clientTick);
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

        }
    }

    static boolean isFirst = true;

    static int ticksPassed;
    static boolean isHidden;

    static void startupScreen(ScreenOpenEvent event) {
        if (isFirst && event.getScreen() instanceof TitleScreen) {
            GLFW.glfwSetInputMode(Minecraft.getInstance().getWindow().getWindow(), InputConstants.CURSOR, GLFW.GLFW_CURSOR_HIDDEN);
           isHidden = true;
           isFirst = false;
        } //else if (isHidden) {
         //   unhide();
       // }
    }

    static void clientTick(TickEvent.ClientTickEvent event) {
        if (event.phase ==  TickEvent.Phase.START) {
            if (isHidden) {
                if (ticksPassed < PS1TweaksConfig.CLIENT.hideMouseTimer.get()) {
                    ticksPassed++;
                } else {
                    unhide();
                }
            }
        }
    }

    /**
     * Will set the focus to ingame if the Minecraft window is the active with focus. Also clears any GUI screen
     * currently displayed
     */
    public static void unhide() {
        isHidden = false;
        GLFW.glfwSetInputMode(Minecraft.getInstance().getWindow().getWindow(), InputConstants.CURSOR, InputConstants.CURSOR_NORMAL);
    }

}
