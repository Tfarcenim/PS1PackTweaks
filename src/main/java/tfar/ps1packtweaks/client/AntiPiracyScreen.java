package tfar.ps1packtweaks.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import tfar.ps1packtweaks.PS1PackTweaks;

public class AntiPiracyScreen extends Screen {
    protected AntiPiracyScreen() {
        super(new TextComponent(""));
    }

    public static final ResourceLocation JUMP_SCARE = PS1PackTweaks.id("textures/gui/antipiracy.png");

    @Override
    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        //super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
        //Minecraft.getInstance().gui.renderTextureOverlay(JUMP_SCARE, 1);

        int screenHeight = Minecraft.getInstance().getWindow().getGuiScaledHeight();
        int screenWidth = Minecraft.getInstance().getWindow().getGuiScaledWidth();

        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1);
        RenderSystem.setShaderTexture(0, JUMP_SCARE);
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder bufferbuilder = tesselator.getBuilder();
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        bufferbuilder.vertex(0.0D, screenHeight, -90.0D).uv(0.0F, 1.0F).endVertex();
        bufferbuilder.vertex(screenWidth, screenHeight, -90.0D).uv(1.0F, 1.0F).endVertex();
        bufferbuilder.vertex(screenWidth, 0.0D, -90.0D).uv(1.0F, 0.0F).endVertex();
        bufferbuilder.vertex(0.0D, 0.0D, -90.0D).uv(0.0F, 0.0F).endVertex();
        tesselator.end();
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }
}
