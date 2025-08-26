package tfar.ps1packtweaks.client;

import com.mojang.blaze3d.platform.InputConstants;
import com.nyfaria.nightmare.config.CommonConfig;
import com.nyfaria.nightmare.init.SoundInit;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.levelgen.RandomSource;
import tfar.ps1packtweaks.AbstractClientPlayerDuck;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.InBedChatScreen;
import net.minecraft.client.gui.screens.ProgressScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.gui.screens.worldselection.SelectWorldScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraftforge.client.event.ScreenOpenEvent;
import net.minecraftforge.event.TickEvent;
import org.lwjgl.glfw.GLFW;
import tfar.ps1packtweaks.PS1PackTweaksConfig;

import java.util.Random;

public class MouseHider {


    public static int hideTimer = -1;
    public static boolean hidden;
    public static boolean first = true;


    static void startupScreen(ScreenOpenEvent event) {
        Screen screen = event.getScreen();
        Screen oldScreen = Minecraft.getInstance().screen;
        if (first && screen instanceof TitleScreen) {
            hide(PS1PackTweaksConfig.CLIENT.hideTitleMouseTimer.get());
            first = false;
        }
        if (screen instanceof SelectWorldScreen) {
            unhide();
        }
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            if (screen instanceof InventoryScreen || screen instanceof CreativeModeInventoryScreen) {
                if (PS1PackTweaksConfig.CLIENT.herobrine_skin_chance.get() > player.getRandom().nextDouble()) {
                    ((AbstractClientPlayerDuck) player).setHerobrine(true);
                }
            } else if (oldScreen instanceof InventoryScreen || oldScreen instanceof CreativeModeInventoryScreen) {
                ((AbstractClientPlayerDuck) player).setHerobrine(false);
            }
        }

        if (screen == null) PS1PackTweaksClient.showDisc = false;

    }

    static void clientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            if (hidden) {
                if (hideTimer > 0) {
                    hideTimer--;
                    if (hideTimer == 0) {
                        unhide();
                    }
                }
            }
        } else {
            LocalPlayer player = Minecraft.getInstance().player;
            if (player != null) {
                Random random = player.getRandom();
                if (player.tickCount % 20 == 0 && random.nextDouble() < PS1PackTweaksConfig.CLIENT.pauseChance.get()) {
                    Minecraft.getInstance().pauseGame(false);
                }
            }
        }
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

    public static void afterRelease() {
        Screen screen = Minecraft.getInstance().screen;
        if (screen instanceof InBedChatScreen || screen instanceof ProgressScreen) {
            hide(-1);
        }
    }
}
