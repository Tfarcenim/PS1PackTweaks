package tfar.ps1packtweaks.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Matrix4f;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.RecordItem;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import tfar.ps1packtweaks.PS1PackTweaksConfig;
import tfar.ps1packtweaks.PaintingEntityDuck;

import java.util.Iterator;

@Mixin(LevelRenderer.class)
public class LevelRendererMixin {

    @Shadow @Final private Minecraft minecraft;

    @Inject(method = "playStreamingMusic(Lnet/minecraft/sounds/SoundEvent;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/item/RecordItem;)V"
    ,at = @At("RETURN"),remap = false)
    private void stopGameMusic(SoundEvent pSoundEvent, BlockPos pPos, RecordItem musicDiscItem, CallbackInfo ci) {
        if (PS1PackTweaksConfig.CLIENT.stop_music_when_record_plays.get()) {
            minecraft.getMusicManager().stopPlaying();
        }
    }

    @Inject(method = "renderLevel",at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/renderer/entity/EntityRenderDispatcher;shouldRender(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/client/renderer/culling/Frustum;DDD)Z")
            ,locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void hookRender(PoseStack pPoseStack, float pPartialTick, long pFinishNanoTime, boolean pRenderBlockOutline,
                            Camera pCamera, GameRenderer pGameRenderer, LightTexture pLightTexture, Matrix4f pProjectionMatrix, CallbackInfo ci,
                            ProfilerFiller profilerfiller, boolean flag, Vec3 vec3, double d0, double d1, double d2, Matrix4f matrix4f, boolean flag1,
                            Frustum frustum, float f, boolean flag2, boolean flag3, MultiBufferSource.BufferSource multibuffersource$buffersource, Iterator var26, Entity entity
) {
        if (entity instanceof PaintingEntityDuck duck) {
            duck.setRendering(false);
        }
    }
}
