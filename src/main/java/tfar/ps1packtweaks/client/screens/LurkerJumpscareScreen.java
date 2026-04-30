package tfar.ps1packtweaks.client.screens;

import com.mojang.blaze3d.vertex.PoseStack;
import net.mcreator.midnightlurker.init.MidnightlurkerModSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import tfar.ps1packtweaks.Init;
import tfar.ps1packtweaks.PS1PackTweaks;
import tfar.ps1packtweaks.client.PS1PackTweaksClient;

public class LurkerJumpscareScreen extends Screen {

    int timer;

    int frame = 1;
    public LurkerJumpscareScreen(Component pTitle) {
        super(pTitle);
        Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(Init.ModSounds.LURKER_JUMPSCARE, 1.0F));

    }

    @Override
    public void tick() {
        super.tick();
        timer++;
        if (timer >= 10) {
            minecraft.popGuiLayer();
        }
        frame = Mth.clamp(timer,1, 9);

    }

    @Override
    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
        Minecraft.getInstance().gui.renderTextureOverlay(PS1PackTweaks.id("textures/gui/lurker/"+frame+".png"), 1);
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
