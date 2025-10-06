package tfar.ps1packtweaks.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.AttackIndicatorStatus;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import static net.minecraft.client.gui.components.AbstractWidget.WIDGETS_LOCATION;

@Mixin(Gui.class)
public abstract class GuiMixin extends GuiComponent{
    @Shadow protected abstract Player getCameraPlayer();

    @Shadow protected int screenWidth;

    @Shadow protected int screenHeight;

    @Shadow protected abstract void renderSlot(int pX, int pY, float pPartialTick, Player pPlayer, ItemStack pStack, int p_168683_);

    @Shadow @Final protected Minecraft minecraft;

    /**
     * @author
     * @reason
     */
    @Overwrite
    protected void renderHotbar(float pPartialTick, PoseStack pPoseStack) {
        Player player = this.getCameraPlayer();
        if (player != null) {
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderTexture(0, WIDGETS_LOCATION);
            ItemStack itemstack = player.getOffhandItem();
            HumanoidArm humanoidarm = player.getMainArm().getOpposite();
            int i = this.screenWidth / 2;
            int j = this.getBlitOffset();
            int k = 182;
            int l = 91;
            this.setBlitOffset(-90);
            this.blit(pPoseStack, i - 91, this.screenHeight - 22, 0, 0, 182, 22);
            this.blit(pPoseStack, i - 91 - 1 + player.getInventory().selected * 20, this.screenHeight - 22 - 1, 0, 22, 24, 24);
            if (!itemstack.isEmpty()) {
                if (humanoidarm == HumanoidArm.LEFT) {
                    this.blit(pPoseStack, i - 91 - 29, this.screenHeight - 23, 24, 22, 29, 24);
                } else {
                    this.blit(pPoseStack, i + 91, this.screenHeight - 23, 53, 22, 29, 24);
                }
            }

            this.setBlitOffset(j);
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            int i1 = 1;

            for(int j1 = 0; j1 < 9; ++j1) {
                int k1 = i - 90 + j1 * 20 + 2;
                int l1 = this.screenHeight - 16 - 3;
                this.renderSlot(k1, l1, pPartialTick, player, player.getInventory().items.get(j1), i1++);
            }

            if (!itemstack.isEmpty()) {
                int j2 = this.screenHeight - 16 - 3;
                if (humanoidarm == HumanoidArm.LEFT) {
                    this.renderSlot(i - 91 - 26, j2, pPartialTick, player, itemstack, i1++);
                } else {
                    this.renderSlot(i + 91 + 10, j2, pPartialTick, player, itemstack, i1++);
                }
            }

            if (this.minecraft.options.attackIndicator == AttackIndicatorStatus.HOTBAR) {
                float f = this.minecraft.player.getAttackStrengthScale(0.0F);
                if (f < 1.0F) {
                    int k2 = this.screenHeight - 20;
                    int l2 = i + 91 + 6;
                    if (humanoidarm == HumanoidArm.RIGHT) {
                        l2 = i - 91 - 22;
                    }

                    RenderSystem.setShaderTexture(0, GuiComponent.GUI_ICONS_LOCATION);
                    int i2 = (int)(f * 19.0F);
                    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                    this.blit(pPoseStack, l2, k2, 0, 94, 18, 18);
                    this.blit(pPoseStack, l2, k2 + 18 - i2, 18, 112 - i2, 18, i2);
                }
            }

            RenderSystem.disableBlend();
        }
    }
}
