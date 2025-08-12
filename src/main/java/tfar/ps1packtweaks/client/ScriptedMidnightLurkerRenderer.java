package tfar.ps1packtweaks.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.mcreator.midnightlurker.entity.layer.MidnightLurkerAggressiveLayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;
import tfar.ps1packtweaks.entity.ScriptedMidnightLurker;

public class ScriptedMidnightLurkerRenderer extends GeoEntityRenderer<ScriptedMidnightLurker> {
    public ScriptedMidnightLurkerRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new BasicGeoModel<>(
            new ResourceLocation("midnightlurker", "animations/midnightlurkerrunning.animation.json"),
            new ResourceLocation("midnightlurker", "geo/midnightlurkerrunning.geo.json"),
            new ResourceLocation("midnightlurker", "textures/entities/midnightlurkervoidgateangry.png")));
        this.shadowRadius = 0.7F;
        this.addLayer(new MidnightLurkerAggressiveLayer(this));
    }

    public RenderType getRenderType(ScriptedMidnightLurker entity, float partialTicks, PoseStack stack, MultiBufferSource renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn, ResourceLocation textureLocation) {
        stack.scale(0.95F, 0.95F, 0.95F);
        return RenderType.entityTranslucent(this.getTextureLocation(entity));
    }
}
