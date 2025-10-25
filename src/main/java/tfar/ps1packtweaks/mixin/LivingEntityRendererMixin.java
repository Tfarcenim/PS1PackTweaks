package tfar.ps1packtweaks.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import crumbs.trueherobrine.entity.HerobrineEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tfar.ps1packtweaks.client.renderer.AltFinalHerobrineRenderer;
import tfar.ps1packtweaks.entity.FinalHerobrineEntity;

@Mixin(LivingEntityRenderer.class)
@Debug(export = true)
public class LivingEntityRendererMixin<T extends LivingEntity> {

    private static final ThreadLocal<LivingEntity> LOCAL = ThreadLocal.withInitial(() -> null);
    private static final ThreadLocal<Float> LOCAL_2 = ThreadLocal.withInitial(() -> 0f);

    @Inject(method = "render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
            at = @At("HEAD"))

    private void captureEntity(T pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight, CallbackInfo ci) {
        LOCAL.set(pEntity);
        LOCAL_2.set(pPartialTicks);
    }

    @ModifyArg(method = "render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V"
    ,at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/EntityModel;renderToBuffer(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;IIFFFF)V"),
            index = 7)
    private float changeAlpha(float original) {
        if ((Object)this instanceof AltFinalHerobrineRenderer altFinalHerobrineRenderer) {
            FinalHerobrineEntity entity = (FinalHerobrineEntity) LOCAL.get();
            if (altFinalHerobrineRenderer.pass == 1) {
                return entity.getLayer2Alpha(LOCAL_2.get());
            }
            return original;
        }
        return original;
    }
}
