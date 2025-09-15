package tfar.ps1packtweaks.worldgen;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.HugeRedMushroomFeature;
import net.minecraft.world.level.levelgen.feature.configurations.HugeMushroomFeatureConfiguration;

public class HugeEndershroomFeature extends HugeRedMushroomFeature {
    public HugeEndershroomFeature(Codec<HugeMushroomFeatureConfiguration> p_65975_) {
        super(p_65975_);
    }

    @Override
    protected boolean isValidPosition(LevelAccessor pLevel, BlockPos pPos, int pMaxHeight, BlockPos.MutableBlockPos pMutablePos, HugeMushroomFeatureConfiguration pConfig) {
         int i = pPos.getY();
        for (int j = 0; j <= pMaxHeight; ++j) {
            int k = this.getTreeRadiusForHeight(-1, -1, pConfig.foliageRadius, j);

            for (int l = -k; l <= k; ++l) {
                for (int i1 = -k; i1 <= k; ++i1) {
                    BlockState blockstate1 = pLevel.getBlockState(pMutablePos.setWithOffset(pPos, l, j, i1));
                    if (!blockstate1.isAir() && !blockstate1.is(BlockTags.LEAVES)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
