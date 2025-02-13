package tfar.ps1packtweaks.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HorseModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.item.DyeableHorseArmorItem;
import net.minecraft.world.item.HorseArmorItem;
import net.minecraft.world.item.ItemStack;

public class UndeadHorseArmorLayer extends RenderLayer<AbstractHorse, HorseModel<AbstractHorse>> {
    private final HorseModel<AbstractHorse> model;

    public UndeadHorseArmorLayer(RenderLayerParent<AbstractHorse, HorseModel<AbstractHorse>> pRenderer, EntityModelSet p_174497_) {
        super(pRenderer);
        this.model = new HorseModel<>(p_174497_.bakeLayer(ModelLayers.HORSE_ARMOR));
    }

    public void render(PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight, AbstractHorse pLivingEntity, float pLimbSwing, float pLimbSwingAmount, float pPartialTicks, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        ItemStack itemstack = pLivingEntity.getItemBySlot(EquipmentSlot.CHEST);
        if (itemstack.getItem() instanceof HorseArmorItem horsearmoritem) {
            this.getParentModel().copyPropertiesTo(this.model);
            this.model.prepareMobModel(pLivingEntity, pLimbSwing, pLimbSwingAmount, pPartialTicks);
            this.model.setupAnim(pLivingEntity, pLimbSwing, pLimbSwingAmount, pAgeInTicks, pNetHeadYaw, pHeadPitch);
            float f;
            float f1;
            float f2;
            if (horsearmoritem instanceof DyeableHorseArmorItem) {
                int i = ((DyeableHorseArmorItem)horsearmoritem).getColor(itemstack);
                f = (float)(i >> 16 & 255) / 255.0F;
                f1 = (float)(i >> 8 & 255) / 255.0F;
                f2 = (float)(i & 255) / 255.0F;
            } else {
                f = 1.0F;
                f1 = 1.0F;
                f2 = 1.0F;
            }

            VertexConsumer vertexconsumer = pBuffer.getBuffer(RenderType.entityCutoutNoCull(horsearmoritem.getTexture()));
            this.model.renderToBuffer(pMatrixStack, vertexconsumer, pPackedLight, OverlayTexture.NO_OVERLAY, f, f1, f2, 1.0F);
        }
    }
}