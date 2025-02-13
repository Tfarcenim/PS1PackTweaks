package tfar.ps1packtweaks.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;
import tfar.ps1packtweaks.PS1PackTweaks;
import tfar.ps1packtweaks.entity.Barnacle;

public class BarnacleRenderer extends GeoEntityRenderer<Barnacle> {
    public BarnacleRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new BarnacleModel());
    }

    @Override
    public RenderType getRenderType(Barnacle barnacle, float partialTicks, PoseStack stack, MultiBufferSource renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn, ResourceLocation textureLocation) {
        return RenderType.entityCutoutNoCull(this.getTextureLocation(barnacle));
    }

    public static final ResourceLocation TEXTURE = PS1PackTweaks.id("textures/entity/barnacle.png");

    @Override
    public ResourceLocation getTextureLocation(Barnacle entity) {
        return TEXTURE;
    }
}