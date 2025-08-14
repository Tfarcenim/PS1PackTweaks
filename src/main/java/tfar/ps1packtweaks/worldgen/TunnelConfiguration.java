package tfar.ps1packtweaks.worldgen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public record TunnelConfiguration(int minLength,int maxLength,float torchChance) implements FeatureConfiguration {
    public static final Codec<TunnelConfiguration> CODEC = RecordCodecBuilder.create(
            instance->
                    instance.group(Codec.intRange(1,64).fieldOf("min_length").forGetter(TunnelConfiguration::minLength),
                            Codec.intRange(1, 64).fieldOf("max_length").forGetter(TunnelConfiguration::maxLength),
                            Codec.floatRange(0.0F, 1.0F).fieldOf("torch_chance").forGetter(TunnelConfiguration::torchChance)
                    ).apply(instance, TunnelConfiguration::new));
}
