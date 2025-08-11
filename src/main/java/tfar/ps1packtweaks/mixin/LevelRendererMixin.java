package tfar.ps1packtweaks.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.RecordItem;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tfar.ps1packtweaks.PS1PackTweaksConfig;

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
}
