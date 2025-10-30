package tfar.ps1packtweaks.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import tfar.ps1packtweaks.PS1PackTweaksConfig;

public class JumpscareScreen extends Screen {

    int timer = 30;

    protected JumpscareScreen(Component pTitle) {
        super(pTitle);
    }

    @Override
    public void tick() {
        super.tick();
        timer--;
        if (timer <= 0) {
            minecraft.popGuiLayer();
        }
    }

    @Override
    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
        Minecraft.getInstance().gui.renderTextureOverlay(PS1PackTweaksClient.JUMP_SCARE, 1);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }
}
