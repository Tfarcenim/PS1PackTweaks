package tfar.ps1packtweaks.compat;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexMultiConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.item.HorseArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public class ShinyHorsesCompat {

    public static VertexConsumer renderHorseArmorGlintHook(VertexConsumer old, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, AbstractHorse horse, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        ItemStack stack = horse.getItemBySlot(EquipmentSlot.CHEST);
        HorseArmorItem horseArmorItem = (HorseArmorItem)stack.getItem();
        boolean glint = horseArmorItem.isFoil(stack);
        if (glint) {
            ResourceLocation texture = horseArmorItem.getTexture();
            return VertexMultiConsumer.create(bufferIn.getBuffer(RenderType.entityGlint()), bufferIn.getBuffer(RenderType.entityCutoutNoCull(texture)));
        } else {
            return old;
        }
    }

    public static void checkHorseHook(Enchantment enchantmentIn, LivingEntity entityIn, CallbackInfoReturnable<Integer> cir) {
        if (entityIn instanceof AbstractHorse abstractHorse) {
            ItemStack armor = abstractHorse.getItemBySlot(EquipmentSlot.CHEST);
            if (armor.getItem() instanceof HorseArmorItem) {
                int level = EnchantmentHelper.getItemEnchantmentLevel(enchantmentIn, armor);
                cir.setReturnValue(level);
            }
        }
    }
}
