package tfar.ps1packtweaks.mixin.compat;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import de.keksuccino.konkrete.rendering.RenderUtils;
import de.keksuccino.spiffyhud.customization.rendering.ingamehud.CustomizableIngameGui;
import de.keksuccino.spiffyhud.customization.rendering.ingamehud.hudelements.HotbarHudElement;
import de.keksuccino.spiffyhud.customization.rendering.ingamehud.hudelements.IngameHudElement;
import net.minecraft.client.AttackIndicatorStatus;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(HotbarHudElement.class)
public abstract class HotbarMixin extends IngameHudElement {
    public HotbarMixin(CustomizableIngameGui handler) {
        super(handler);
    }

    @Shadow protected abstract Player getRenderViewPlayer();

    @Shadow @Final protected static ResourceLocation WIDGETS_TEX_PATH;

    @Shadow protected abstract void renderSlot(int x, int y, float partial, Player player, ItemStack stack, int slot);

    /**
     * @author
     * @reason
     */
    @Overwrite(remap = false)
    protected void renderHotbarRaw(PoseStack matrix, float partialTicks) {
        Player playerentity = this.getRenderViewPlayer();
        if (playerentity != null) {
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderUtils.bindTexture(WIDGETS_TEX_PATH);
            ItemStack itemstack = playerentity.getOffhandItem();
            HumanoidArm handside = playerentity.getMainArm().getOpposite();
            this.blit(matrix, this.x, this.y, 0, 0, 182, 22);
            int currentItem = 4;
            if (!this.handler.isEditor()) {
                currentItem = playerentity.getInventory().selected;
            }

            this.blit(matrix, this.x - 1 + currentItem * 20, this.y - 1, 0, 22, 24, 24);
            if (!itemstack.isEmpty() || this.handler.isEditor()) {
                if (handside == HumanoidArm.LEFT) {
                    this.blit(matrix, this.x - 29, this.y - 1, 24, 22, 29, 24);
                } else {
                    this.blit(matrix, this.x + 182, this.y - 1, 53, 22, 29, 24);
                }
            }

            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            int i2;
            int k2;
            int l1;
            if (!this.handler.isEditor()) {
                int i1 = 1;

                for(i2 = 0; i2 < 9; ++i2) {
                    k2 = this.x + 1 + i2 * 20 + 2;
                    l1 = this.y + 3;
                    this.renderSlot(k2, l1, partialTicks, playerentity, playerentity.getInventory().items.get(i2), i1++);
                }

                if (!itemstack.isEmpty()) {
                    i2 = this.y + 3;
                    if (handside == HumanoidArm.LEFT) {
                        this.renderSlot(this.x - 26, i2, partialTicks, playerentity, itemstack, i1++);
                    } else {
                        this.renderSlot(this.x + 182 + 10, i2, partialTicks, playerentity, itemstack, i1++);
                    }
                }
            }

            if (this.mc.options.attackIndicator == AttackIndicatorStatus.HOTBAR) {
                float f = this.mc.player.getAttackStrengthScale(0.0F);
                if (f < 1.0F) {
                    i2 = this.y + 2;
                    k2 = this.x + 182 + 6;
                    if (handside == HumanoidArm.RIGHT) {
                        k2 = this.x - 22;
                    }

                    RenderUtils.bindTexture(GUI_ICONS_LOCATION);
                    l1 = (int)(f * 19.0F);
                    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                    this.blit(matrix, k2, i2, 0, 94, 18, 18);
                    this.blit(matrix, k2, i2 + 18 - l1, 18, 112 - l1, 18, l1);
                }
            }

            RenderSystem.disableBlend();
        }

    }

}
