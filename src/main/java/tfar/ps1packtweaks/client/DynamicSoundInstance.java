package tfar.ps1packtweaks.client;

import net.minecraft.Util;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.levelgen.RandomSource;
import net.minecraft.world.level.levelgen.SingleThreadedRandomSource;
import org.apache.commons.lang3.RandomUtils;

public class DynamicSoundInstance extends AbstractTickableSoundInstance {

    RandomSource randomSource;

    /**
     * @see net.minecraft.client.resources.sounds.SimpleSoundInstance#forMusic(SoundEvent)
     * @param p_119606_
     * @param p_119607_
     */

    public DynamicSoundInstance(SoundEvent p_119606_, SoundSource p_119607_) {
        super(p_119606_, p_119607_);
        relative = true;
        attenuation = Attenuation.NONE;
        randomSource = new SingleThreadedRandomSource(Util.getNanos());
    }

    float originalPitch;
    int elapsed;
    boolean modified;

    //Music changing pitch
    @Override
    public void tick() {
        if (!modified && randomSource.nextDouble() < PS1PackTweaksClient.CLIENT.randomPitchChance.get()) {
            modified = true;
            originalPitch = pitch;
            pitch *= (float) RandomUtils.nextDouble(PS1PackTweaksClient.CLIENT.minMusicPitch.get(), PS1PackTweaksClient.CLIENT.maxMusicPitch.get());
        }
        if (modified) {
            elapsed++;
            if (elapsed > PS1PackTweaksClient.CLIENT.ticksMusicPitch.get()) {
                modified = false;
                elapsed = 0;
                pitch = originalPitch;
            }
        }
    }
}
