package tfar.ps1packtweaks.mixin;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix4f;
import net.minecraft.client.renderer.entity.EnderDragonRenderer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(EnderDragonRenderer.class)
public class DragonRendererMixin {

    @Shadow @Final private static float HALF_SQRT_3;

    /**
     * @author
     * @reason
     */
    @Overwrite
    private static void vertex2(VertexConsumer p_114215_, Matrix4f p_114216_, float p_114217_, float p_114218_) {
        p_114215_.vertex(p_114216_, -HALF_SQRT_3 * p_114218_, p_114217_, -0.5F * p_114218_).color(0, 255, 0, 0).endVertex();
    }
    /**
     * @author
     * @reason
     */
    @Overwrite

    private static void vertex3(VertexConsumer p_114224_, Matrix4f p_114225_, float p_114226_, float p_114227_) {
        p_114224_.vertex(p_114225_, HALF_SQRT_3 * p_114227_, p_114226_, -0.5F * p_114227_).color(0, 255, 0, 0).endVertex();
    }
    /**
     * @author
     * @reason
     */
    @Overwrite

    private static void vertex4(VertexConsumer p_114229_, Matrix4f p_114230_, float p_114231_, float p_114232_) {
        p_114229_.vertex(p_114230_, 0.0F, p_114231_, p_114232_).color(0, 255, 0, 0).endVertex();
    }
}
