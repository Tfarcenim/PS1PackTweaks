package tfar.ps1packtweaks.worldgen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import tfar.ps1packtweaks.util.MiscCodecs;

import java.util.List;

public record SignFeatureConfig(List<List<Component>> text, Block block) implements FeatureConfiguration {
    public static final Codec<SignFeatureConfig> CODEC = RecordCodecBuilder.create(
            instance->
                    instance.group(MiscCodecs.COMPONENT_CODEC.listOf().listOf().fieldOf("messages").forGetter(SignFeatureConfig::text),
                                    Registry.BLOCK.byNameCodec().fieldOf("block").forGetter(SignFeatureConfig::block)
                            )
                            .apply(instance, SignFeatureConfig::new));
}
////	- STOP
////	- LEAVE
////	- Can you see me?
////	- I see you
////	- RUN
////	- You shouldn't be here
////	- Do you hear it?
////	- I'm not dead
////	- Se upp på ryggen
////	- Bakom dig
////	- Du är inte säker
////	- Titta inte