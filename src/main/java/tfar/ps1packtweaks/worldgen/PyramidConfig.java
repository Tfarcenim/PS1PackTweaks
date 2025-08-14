package tfar.ps1packtweaks.worldgen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public record PyramidConfig(int height) implements FeatureConfiguration {

    public static final Codec<PyramidConfig> CODEC = RecordCodecBuilder.create(
            instance->
                    instance.group(Codec.intRange(1,20).fieldOf("height").forGetter(PyramidConfig::height)
                    ).apply(instance, PyramidConfig::new));

}
