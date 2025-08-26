package tfar.ps1packtweaks.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

public class InvisibleEntityRenderer<T extends LivingEntity> extends LivingEntityRenderer<T, PlayerModel<T>> {
    private final ResourceLocation texture;

    public InvisibleEntityRenderer(EntityRendererProvider.Context context, boolean slim, ResourceLocation texture) {
        super(context, new PlayerModel<>(context.bakeLayer(slim ? ModelLayers.PLAYER_SLIM : ModelLayers.PLAYER), slim), 0.5F);
        this.texture = texture;
    }

    @Override
    public void render(T pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight) {
    }

    @Override
    public ResourceLocation getTextureLocation(T pEntity) {
        return texture;
    }
}
