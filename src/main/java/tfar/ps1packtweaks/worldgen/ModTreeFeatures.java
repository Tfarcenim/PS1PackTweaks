package tfar.ps1packtweaks.worldgen;

import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HugeMushroomBlock;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.configurations.HugeMushroomFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import tfar.ps1packtweaks.Init;

public class ModTreeFeatures {

    public static void init(){}

    public static final Holder<ConfiguredFeature<HugeMushroomFeatureConfiguration, ?>> HUGE_ENDERSHROOM =
            FeatureUtils.register("ps1packtweaks:huge_endershroom", Init.ModFeatures.HUGE_ENDERSHROOM, new HugeMushroomFeatureConfiguration(BlockStateProvider
                    .simple(Init.ModBlocks.ENDERSHROOM_BLOCK.defaultBlockState().setValue(HugeMushroomBlock.DOWN, false)), BlockStateProvider
                    .simple(Blocks.MUSHROOM_STEM.defaultBlockState().setValue(HugeMushroomBlock.UP, false)
                            .setValue(HugeMushroomBlock.DOWN, false)), 2));

}
