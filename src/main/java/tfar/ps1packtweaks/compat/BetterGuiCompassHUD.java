package tfar.ps1packtweaks.compat;

import com.natamus.guicompass_common_forge.events.GUIEvent;
import net.minecraftforge.client.gui.IIngameOverlay;
import net.minecraftforge.client.gui.OverlayRegistry;
import tfar.ps1packtweaks.PS1PackTweaks;

public class BetterGuiCompassHUD {
    public static final IIngameOverlay OVERLAY = (gui, poseStack, partialTick, width, height) -> GUIEvent.renderOverlay(poseStack,partialTick);

    public static void setup() {
        OverlayRegistry.registerOverlayTop(PS1PackTweaks.MOD_ID+":gui_compass",OVERLAY);
    }
}
