package tfar.ps1packtweaks.mixin.compat.guiclock;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.vertex.PoseStack;
import com.natamus.collective_common_forge.functions.StringFunctions;
import com.natamus.guiclock_common_forge.config.ConfigHandler;
import com.natamus.guiclock_common_forge.events.GUIEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Collection;

@Mixin(GUIEvent.class)
public class GUIEventMixin {

    @Shadow(remap = false)
    private static String getGameTime() {
        throw new UnsupportedOperationException("Implemented via mixin");
    }

    @Shadow(remap = false)
    private static void drawText(Font fontRenderer, PoseStack poseStack, String content, float x, float y, int rgb, boolean drawShadow) {
        throw new UnsupportedOperationException("Implemented via mixin");
    }

    @Shadow
    @Final
    private static Minecraft mc;

    @Shadow
    private static String daystring;

    /**
     * @author
     * @reason
     */
    @Overwrite(remap = false)
    public static void renderOverlay(PoseStack poseStack, float tickDelta) {
        if (!mc.options.renderDebug) {
            boolean gametimeb = ConfigHandler.mustHaveClockInInventoryForGameTime;
            boolean realtimeb = ConfigHandler.mustHaveClockInInventoryForRealTime;
            boolean found = true;
            if (gametimeb || realtimeb) {
                found = mc.player.getOffhandItem().getItem().equals(Items.CLOCK);
                if (!found) {
                    Inventory inv = mc.player.getInventory();

                    for (int n = 0; n <= 35; ++n) {
                        if (inv.getItem(n).getItem().equals(Items.CLOCK)) {
                            found = true;
                            break;
                        }
                    }
                }
            }

            poseStack.pushPose();
            Font fontRenderer = mc.font;
            Window scaled = mc.getWindow();
            int width = scaled.getGuiScaledWidth();
            int heightoffset = ConfigHandler.clockHeightOffset;
            if (heightoffset < 5) {
                heightoffset = 5;
            }

            if (ConfigHandler.lowerClockWhenPlayerHasEffects) {
                Collection<MobEffectInstance> activeeffects = mc.player.getActiveEffects();
                if (!activeeffects.isEmpty()) {
                    boolean haspositive = false;
                    boolean hasnegative = false;

                    for (MobEffectInstance effect : activeeffects) {
                        if (effect.isVisible()) {
                            if (effect.getEffect().getCategory().equals(MobEffectCategory.BENEFICIAL)) {
                                haspositive = true;
                            } else {
                                hasnegative = true;
                            }

                            if (haspositive && hasnegative) {
                                break;
                            }
                        }
                    }

                    if (hasnegative && haspositive) {
                        heightoffset += 50;
                    } else if (haspositive && !hasnegative) {
                        heightoffset += 25;
                    }
                }
            }

            if (ConfigHandler.showOnlyMinecraftClockIcon) {
                if (gametimeb && !found) {
                    return;
                }

                int xcoord;
                if (ConfigHandler.clockPositionIsLeft) {
                    xcoord = 20;
                } else if (ConfigHandler.clockPositionIsCenter) {
                    xcoord = width / 2 - 8;
                } else {
                    xcoord = width - 20;
                }

                xcoord += ConfigHandler.clockWidthOffset;
                ItemRenderer itemrenderer = mc.getItemRenderer();
                itemrenderer.renderAndDecorateItem(new ItemStack(Items.CLOCK), xcoord, heightoffset);
            } else {
                String realtime = StringFunctions.getPCLocalTime(ConfigHandler._24hourformat, ConfigHandler.showRealTimeSeconds);
                String time;
                if (ConfigHandler.showBothTimes) {
                    if (gametimeb && realtimeb) {
                        if (!found) {
                            return;
                        }

                        String var29 = getGameTime();
                        time = var29 + " | " + realtime;
                    } else if (!found && gametimeb) {
                        time = realtime;
                    } else if (!found && realtimeb) {
                        time = getGameTime();
                    } else {
                        String var10000 = getGameTime();
                        time = var10000 + " | " + realtime;
                    }
                } else if (ConfigHandler.showRealTime) {
                    if (realtimeb && !found) {
                        return;
                    }

                    time = realtime;
                } else {
                    if (gametimeb && !found) {
                        return;
                    }

                    time = getGameTime();
                }

                if (time.equals("")) {
                    return;
                }

                int stringWidth = fontRenderer.width(time);
                int daystringWidth = fontRenderer.width(daystring);
                //don't use java.awt in mods
                //Color colour = new Color(ConfigHandler.RGB_R, ConfigHandler.RGB_G, ConfigHandler.RGB_B, 255);

                int xcoord;
                int daycoord;
                if (ConfigHandler.clockPositionIsLeft) {
                    xcoord = 5;
                    daycoord = 5;
                } else if (ConfigHandler.clockPositionIsCenter) {
                    xcoord = width / 2 - stringWidth / 2;
                    daycoord = width / 2 - daystringWidth / 2;
                } else {
                    xcoord = width - stringWidth - 5;
                    daycoord = width - daystringWidth - 5;
                }

                xcoord += ConfigHandler.clockWidthOffset;
                daycoord += ConfigHandler.clockWidthOffset;
                int a = 255;//R is 252, G is 252, B is 84
                int r = 252;
                int g = 252;
                int b = 84;
                int rgb = ((a & 0xFF) << 24) |
                        ((r & 0xFF) << 16) |
                        ((g & 0xFF) << 8)  |
                        ((b & 0xFF) << 0);
                drawText(fontRenderer, poseStack, time, (float) xcoord, (float) heightoffset, rgb, ConfigHandler.drawTextShadow);
                if (!daystring.equals("")) {
                    drawText(fontRenderer, poseStack, daystring, (float) daycoord, (float) (heightoffset + 10), rgb, ConfigHandler.drawTextShadow);
                }
            }

            poseStack.popPose();
        }
    }
}
