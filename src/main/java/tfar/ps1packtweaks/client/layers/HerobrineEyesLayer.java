package tfar.ps1packtweaks.client.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import tfar.ps1packtweaks.FinalHerobrine;
import tfar.ps1packtweaks.entity.FinalHerobrineEntity;

public class HerobrineEyesLayer extends GenericEyesLayer<FinalHerobrineEntity, PlayerModel<FinalHerobrineEntity>>{
    public HerobrineEyesLayer(RenderLayerParent<FinalHerobrineEntity, PlayerModel<FinalHerobrineEntity>> $$0, ResourceLocation texture) {
        super($$0, texture);
    }

    @Override
    public void render(PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight, FinalHerobrineEntity pLivingEntity,
                       float pLimbSwing, float pLimbSwingAmount, float pPartialTicks, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        VertexConsumer vertexconsumer = pBuffer.getBuffer(this.renderType());
        float alpha = Mth.clamp((pAgeInTicks- pLivingEntity.start) / pLivingEntity.duration,0f,1f);//should increase over time
        this.getParentModel().renderToBuffer(pMatrixStack, vertexconsumer, 15728640, OverlayTexture.NO_OVERLAY, 1, 1, 1, alpha);
    }
}
