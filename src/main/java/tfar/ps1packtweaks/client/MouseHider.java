package tfar.ps1packtweaks.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.gui.screens.InBedChatScreen;
import net.minecraft.client.gui.screens.ProgressScreen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import tfar.ps1packtweaks.duck.AbstractClientPlayerDuck;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.gui.screens.worldselection.SelectWorldScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraftforge.client.event.ScreenOpenEvent;
import org.lwjgl.glfw.GLFW;

public class MouseHider {


    public static int hideTimer = -1;
    public static boolean hidden;
    public static boolean first = true;


    static void startupScreen(ScreenOpenEvent event) {
        Screen screen = event.getScreen();
        Screen oldScreen = Minecraft.getInstance().screen;
        if (first && screen instanceof TitleScreen) {
            PS1PackTweaksClient.DIRT_TIME = PS1PackTweaksClient.CLIENT.replaceBlocksTime.get();
            hide(PS1PackTweaksClient.CLIENT.hideTitleMouseTimer.get());
            first = false;
        }
        if (screen instanceof SelectWorldScreen || screen instanceof JoinMultiplayerScreen) {
            unhide();
            WorldLocker.updateLocks();
        }
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            if (screen instanceof InventoryScreen || screen instanceof CreativeModeInventoryScreen) {
                if (PS1PackTweaksClient.CLIENT.herobrine_skin_chance.get() > player.getRandom().nextDouble()) {
                    ((AbstractClientPlayerDuck) player).setHerobrine(true);
                }
            } else if (oldScreen instanceof InventoryScreen || oldScreen instanceof CreativeModeInventoryScreen) {
                ((AbstractClientPlayerDuck) player).setHerobrine(false);
            }
        }

        if (screen == null) PS1PackTweaksClient.showDisc = false;
    }

    public static void hide(int ticks) {
        hidden = true;
        hideTimer = ticks;
        GLFW.glfwSetInputMode(Minecraft.getInstance().getWindow().getWindow(), InputConstants.CURSOR, GLFW.GLFW_CURSOR_HIDDEN);
    }

    public static void unhide() {
        hidden = false;
        GLFW.glfwSetInputMode(Minecraft.getInstance().getWindow().getWindow(), InputConstants.CURSOR, InputConstants.CURSOR_NORMAL);
    }

    //progressscreen opens in multiplayer
    public static void afterMouseRelease() {
        Screen screen = Minecraft.getInstance().screen;
        if (screen instanceof InBedChatScreen || screen instanceof ProgressScreen) {
            hide(-1);
        }
    }
}
