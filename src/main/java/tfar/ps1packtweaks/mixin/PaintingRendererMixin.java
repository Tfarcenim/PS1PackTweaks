package tfar.ps1packtweaks.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.entity.PaintingRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.entity.decoration.Painting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tfar.ps1packtweaks.PaintingEntityDuck;

@Mixin(PaintingRenderer.class)
public class PaintingRendererMixin {
    @Inject(method = "renderPainting",at = @At("HEAD"))
    private void onRender(PoseStack pPoseStack, VertexConsumer p_115560_, Painting pPainting, int p_115562_, int p_115563_,
                          TextureAtlasSprite p_115564_, TextureAtlasSprite p_115565_, CallbackInfo ci) {
        ((PaintingEntityDuck)pPainting).setRendering(true);
    }
}
