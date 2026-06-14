package tfar.ps1packtweaks.mixin;

import com.google.common.collect.Multimap;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.audio.Channel;
import net.minecraft.client.Options;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.resources.sounds.TickableSoundInstance;
import net.minecraft.client.sounds.ChannelAccess;
import net.minecraft.client.sounds.SoundEngine;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tfar.ps1packtweaks.PS1PackTweaks;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Mixin(SoundEngine.class)
@Debug(export = true)
public abstract class SoundEngineMixin {
    @Shadow
    @Final
    private Map<SoundInstance, Integer> soundDeleteTime;

    @Shadow
    private int tickCount;

    @Shadow
    @Final
    private List<TickableSoundInstance> queuedTickableSounds;

    @Shadow
    @Final
    private List<TickableSoundInstance> tickingSounds;

    @Shadow
    public abstract void play(SoundInstance p_120313_);

    @Shadow
    public abstract void stop(SoundInstance pSound);

    @Shadow
    @Final
    private Options options;

    @Shadow
    @Final
    private Map<SoundInstance, ChannelAccess.ChannelHandle> instanceToChannel;

    @Shadow
    @Final
    private Multimap<SoundSource, SoundInstance> instanceBySource;

    @Shadow
    private static boolean shouldLoopManually(SoundInstance pSound) {
        throw new UnsupportedOperationException("Implemented via mixin");
    }

    @Shadow
    protected abstract float calculateVolume(SoundInstance pSound);

    @Shadow
    protected abstract float calculatePitch(SoundInstance pSound);

    @Shadow
    @Final
    private Map<SoundInstance, Integer> queuedSounds;

    /*@Inject(method = "tickNonPaused",at = @At(value = "INVOKE", target = "Ljava/util/Map;get(Ljava/lang/Object;)Ljava/lang/Object;",ordinal = 1))
    private void captureCrashingSound(CallbackInfo ci,@Local SoundInstance soundinstance) {
        Integer integer = soundDeleteTime.get(soundinstance);
        if (integer == null) {
            PS1PackTweaks.LOGGER.error("This sound is causing the crash! : "
                    +soundinstance +" : "+soundinstance.getLocation()+" : "+soundinstance.getSound());
        }
    }*/

    /**
     * @author
     * @reason
     */
    @Overwrite
    private void tickNonPaused() {
        ++this.tickCount;
        this.queuedTickableSounds.stream().filter(SoundInstance::canPlaySound).forEach(this::play);
        this.queuedTickableSounds.clear();

        for(TickableSoundInstance tickablesoundinstance : this.tickingSounds) {
            if (!tickablesoundinstance.canPlaySound()) {
                this.stop(tickablesoundinstance);
            }

            tickablesoundinstance.tick();
            if (tickablesoundinstance.isStopped()) {
                this.stop(tickablesoundinstance);
            } else {
                float f = this.calculateVolume(tickablesoundinstance);
                float f1 = this.calculatePitch(tickablesoundinstance);
                Vec3 vec3 = new Vec3(tickablesoundinstance.getX(), tickablesoundinstance.getY(), tickablesoundinstance.getZ());
                ChannelAccess.ChannelHandle channelaccess$channelhandle = this.instanceToChannel.get(tickablesoundinstance);
                if (channelaccess$channelhandle != null) {
                    channelaccess$channelhandle.execute((channel) -> {
                        channel.setVolume(f);
                        channel.setPitch(f1);
                        channel.setSelfPosition(vec3);
                    });
                }
            }
        }

        Iterator<Map.Entry<SoundInstance, ChannelAccess.ChannelHandle>> iterator = this.instanceToChannel.entrySet().iterator();

        while(iterator.hasNext()) {
            Map.Entry<SoundInstance, ChannelAccess.ChannelHandle> entry = iterator.next();
            ChannelAccess.ChannelHandle channelaccess$channelhandle1 = entry.getValue();
            SoundInstance soundinstance = entry.getKey();
            float f2 = this.options.getSoundSourceVolume(soundinstance.getSource());
            if (f2 <= 0.0F) {
                channelaccess$channelhandle1.execute(Channel::stop);
                iterator.remove();
            } else if (channelaccess$channelhandle1.isStopped()) {
                int i = this.soundDeleteTime.getOrDefault(soundinstance,0);
                if (i <= this.tickCount) {
                    if (shouldLoopManually(soundinstance)) {
                        this.queuedSounds.put(soundinstance, this.tickCount + soundinstance.getDelay());
                    }

                    iterator.remove();
                   // LOGGER.debug(MARKER, "Removed channel {} because it's not playing anymore", (Object)channelaccess$channelhandle1);
                    this.soundDeleteTime.remove(soundinstance);

                    try {
                        this.instanceBySource.remove(soundinstance.getSource(), soundinstance);
                    } catch (RuntimeException runtimeexception) {
                    }

                    if (soundinstance instanceof TickableSoundInstance) {
                        this.tickingSounds.remove(soundinstance);
                    }
                }
            }
        }

        Iterator<Map.Entry<SoundInstance, Integer>> iterator1 = this.queuedSounds.entrySet().iterator();

        while(iterator1.hasNext()) {
            Map.Entry<SoundInstance, Integer> entry1 = iterator1.next();
            if (this.tickCount >= entry1.getValue()) {
                SoundInstance soundinstance1 = entry1.getKey();
                if (soundinstance1 instanceof TickableSoundInstance) {
                    ((TickableSoundInstance)soundinstance1).tick();
                }
                this.play(soundinstance1);
                iterator1.remove();
            }
        }
    }
}
