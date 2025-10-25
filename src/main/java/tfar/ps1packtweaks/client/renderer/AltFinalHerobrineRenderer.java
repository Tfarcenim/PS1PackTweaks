package tfar.ps1packtweaks.client.renderer;

import com.faboslav.friendsandfoes.init.ModRenderType;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import tfar.ps1packtweaks.PS1PackTweaks;
import tfar.ps1packtweaks.client.CustomRenderTypes;
import tfar.ps1packtweaks.client.layers.HerobrineEyesLayer;
import tfar.ps1packtweaks.entity.FinalHerobrineEntity;

public class AltFinalHerobrineRenderer extends SimplePlayerRenderer<FinalHerobrineEntity> {
    public AltFinalHerobrineRenderer(EntityRendererProvider.Context context, boolean slim) {
        super(context, slim);
    }

    int pass;
    @Override
    public void render(FinalHerobrineEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight) {
        pass = 0;
        super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);
        pass = 1;
        super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);

    }

    @Override
    public ResourceLocation getTextureLocation(FinalHerobrineEntity pEntity) {
        if (pass == 1) {
            return PS1PackTweaks.id("textures/entity/blank_eyes.png");
        }
        return super.getTextureLocation(pEntity);
    }
}
