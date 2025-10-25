package tfar.ps1packtweaks.mixin;

import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.MusicManager;
import net.minecraft.sounds.Music;
import net.minecraft.sounds.SoundSource;
import org.checkerframework.checker.units.qual.A;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tfar.ps1packtweaks.PS1PackTweaksConfig;
import tfar.ps1packtweaks.client.DynamicSoundInstance;
import tfar.ps1packtweaks.client.PS1PackTweaksClient;
import tfar.ps1packtweaks.client.WorldLocker;
import tfar.ps1packtweaks.duck.MusicManagerDuck;

import javax.annotation.Nullable;

@Mixin(MusicManager.class)
//@Debug(export = true)
public abstract class MusicManagerMixin implements MusicManagerDuck {

    @Shadow @Nullable private SoundInstance currentMusic;

    @Shadow public abstract void stopPlaying();

    @Shadow private int nextSongDelay;
    boolean mute;

    @Inject(
            method = "startPlaying",
            at = {@At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/resources/sounds/SoundInstance;getSound()Lnet/minecraft/client/resources/sounds/Sound;",
                    shift = At.Shift.AFTER
            )}
    )
    public void replaceMusic(Music pSelector, CallbackInfo ci) {
        if (!WorldLocker.isPure()) {
            this.currentMusic = new DynamicSoundInstance(pSelector.getEvent(), SoundSource.MUSIC);
        }
    }

    @Unique
    int elapsed;

    @Inject(method = "tick",at = @At("HEAD"),cancellable = true)
    private void delaySong(CallbackInfo ci) {
        elapsed++;
        if (elapsed < PS1PackTweaksClient.CLIENT.delaySongTime.get() || mute) {
            ci.cancel();
        }
    }

    @Override
    public void setMute(boolean mute) {
        this.mute = mute;
        if (mute) {
            stopPlaying();
            nextSongDelay = Integer.MAX_VALUE;
        }
    }
}
