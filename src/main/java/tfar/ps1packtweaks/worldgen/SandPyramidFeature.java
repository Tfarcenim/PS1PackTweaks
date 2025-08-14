package tfar.ps1packtweaks.worldgen;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

import java.util.function.Predicate;

public class SandPyramidFeature extends Feature<PyramidConfig> {
    public SandPyramidFeature(Codec<PyramidConfig> pCodec) {
        super(pCodec);
    }

    @Override
    public boolean place(FeaturePlaceContext<PyramidConfig> pContext) {
        BlockPos origin = pContext.origin();
        WorldGenLevel level = pContext.level();
        Predicate<BlockState> predicate = isReplaceable(BlockTags.FEATURES_CANNOT_REPLACE);

        safeSetBlock(level,origin, Blocks.SAND.defaultBlockState(),predicate);
        for (int z = -1; z <=1;z++) {
            for (int x = -1; x <=1;x++) {
                safeSetBlock(level,origin.offset(x,-1,z), Blocks.SAND.defaultBlockState(),predicate);
            }
        }

        for (int z = -2; z <=2;z++) {
            for (int x = -2; x <=2;x++) {
                safeSetBlock(level,origin.offset(x,-2,z), Blocks.SANDSTONE.defaultBlockState(),predicate);
            }
        }

        return true;
    }
}
