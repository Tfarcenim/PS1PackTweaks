package tfar.ps1packtweaks.mixin.compat;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.vandendaelen.nicephore.client.gui.ScreenshotScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;

@Mixin(ScreenshotScreen.class)
public abstract class ScreenShotScreenMixin extends Screen {

    @Shadow(remap = false) protected abstract void modIndex(int value);

    @Shadow(remap = false) protected abstract void openSettingsScreen();

    @Shadow(remap = false) protected abstract void changeFilter();

    @Shadow(remap = false) private float aspectRatio;

    @Shadow(remap = false) private ArrayList<File> screenshots;

    @Shadow(remap = false) private int index;

    @Shadow(remap = false) private static DynamicTexture SCREENSHOT_TEXTURE;

    @Shadow(remap = false) protected abstract void deleteScreenshot(File file);

    @Shadow(remap = false)
    private static String getFileSizeMegaBytes(File file) {
        throw new AssertionError();
    }

    protected ScreenShotScreenMixin(Component pTitle) {
        super(pTitle);
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        int centerX = this.minecraft.getWindow().getGuiScaledWidth() / 2;
        int pictureMidWith = (int)(this.minecraft.getWindow().getGuiScaledWidth() * 0.5 * 1.2);
        int pictureHeight = (int)((float)pictureMidWith / this.aspectRatio);
        int bottomLine = this.minecraft.getWindow().getGuiScaledHeight() - 30;
        this.renderBackground(matrixStack);
        this.clearWidgets();
      //  this.addRenderableWidget(new Button(10, 10, 100, 20, new TranslatableComponent("nicephore.screenshot.filter", NicephoreConfig.Client.getScreenshotFilter().name()), (button) -> {
      //      this.changeFilter();
      //  }));
     //   this.addRenderableWidget(new Button(this.minecraft.getWindow().getGuiScaledWidth() - 60, 10, 50, 20, new TranslatableComponent("nicephore.screenshot.exit"), (button) -> {
      //      this.onClose();
      //  }));
     //   this.addRenderableWidget(new Button(this.width - 120, 10, 50, 20, new TranslatableComponent("nicephore.gui.settings"), (button) -> {
     //       this.openSettingsScreen();
     //   }));
        if (!this.screenshots.isEmpty()) {
            this.addRenderableWidget(new Button(this.minecraft.getWindow().getGuiScaledWidth() / 2 - 80, bottomLine, 20, 20, new TextComponent("<"), (button) -> {
                this.modIndex(-1);
            }));
            this.addRenderableWidget(new Button(this.minecraft.getWindow().getGuiScaledWidth() / 2 + 60, bottomLine, 20, 20, new TextComponent(">"), (button) -> {
                this.modIndex(1);
            }));
      //      Button copyButton = new Button(this.minecraft.getWindow().getGuiScaledWidth() / 2 - 52, bottomLine, 50, 20, new TranslatableComponent("nicephore.gui.screenshots.copy"), (button) -> {
      //          File screenshot = this.screenshots.get(this.index);
       //         if (CopyImageToClipBoard.getInstance().copyImage(screenshot)) {
      //              PlayerHelper.sendMessage(new TranslatableComponent("nicephore.clipboard.success"));
       //         } else {
      //              PlayerHelper.sendMessage(new TranslatableComponent("nicephore.clipboard.error"));
       //         }

    //        });
    //        copyButton.active = OperatingSystems.getOS().getManager() != null;
    //        if (!copyButton.isActive() && (double)mouseX >= (double)copyButton.x && (double)mouseY >= (double)copyButton.y && (double)mouseX < (double)(copyButton.x + copyButton.getWidth()) && (double)mouseY < (double)(copyButton.y + copyButton.getHeight())) {
    //            this.renderComponentTooltip(matrixStack, List.of((new TranslatableComponent("nicephore.gui.screenshots.copy.unable")).withStyle(ChatFormatting.RED)), mouseX, mouseY);
     //       }

     //       this.addRenderableWidget(copyButton);
            this.addRenderableWidget(new Button(this.minecraft.getWindow().getGuiScaledWidth() / 2 -25, bottomLine, 50, 20, new TranslatableComponent("nicephore.gui.screenshots.delete"), (button) -> {
                this.deleteScreenshot(this.screenshots.get(this.index));
            }));
        }

        if (this.screenshots.isEmpty()) {
            drawCenteredString(matrixStack, Minecraft.getInstance().font, new TranslatableComponent("nicephore.screenshots.empty"), centerX, 20, Color.RED.getRGB());
        } else {
            File currentScreenshot = this.screenshots.get(this.index);
            if (currentScreenshot.exists()) {
                RenderSystem.setShaderTexture(0, SCREENSHOT_TEXTURE.getId());
                RenderSystem.enableBlend();
                blit(matrixStack, centerX - pictureMidWith / 2, 50, 0.0F, 0.0F, pictureMidWith, pictureHeight, pictureMidWith, pictureHeight);
                RenderSystem.disableBlend();
                drawCenteredString(matrixStack, Minecraft.getInstance().font, new TranslatableComponent("nicephore.gui.screenshots.pages", this.index + 1, this.screenshots.size()), centerX, 20, Color.WHITE.getRGB());
             //   drawCenteredString(matrixStack, Minecraft.getInstance().font, (new TextComponent(MessageFormat.format("{0} ({1})", currentScreenshot.getName(), getFileSizeMegaBytes(currentScreenshot)))).getContents(), centerX, 35, Color.WHITE.getRGB());
            }
        }

        super.render(matrixStack, mouseX, mouseY, partialTicks);
    }

}
