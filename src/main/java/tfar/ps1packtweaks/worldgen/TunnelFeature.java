package tfar.ps1packtweaks.worldgen;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import tfar.ps1packtweaks.PS1PackTweaks;

import java.util.Random;
import java.util.function.Predicate;

public class TunnelFeature extends Feature<TunnelConfiguration> {

    public TunnelFeature(Codec<TunnelConfiguration> pCodec) {
        super(pCodec);
    }

    @Override
    public boolean place(FeaturePlaceContext<TunnelConfiguration> pContext) {
        WorldGenLevel level = pContext.level();
        Random random = pContext.random();
        BlockPos blockpos = pContext.origin();
        blockpos = blockpos.offset(7,0,7);

        TunnelConfiguration config = pContext.config();
        Predicate<BlockState> predicate = isReplaceable(BlockTags.FEATURES_CANNOT_REPLACE);
        int blockCount = 0;
        int length = config.minLength() + random.nextInt(config.maxLength() - config.minLength() +1);
        Direction direction = Direction.Plane.HORIZONTAL.getRandomDirection(random);
        boolean placeTorch = random.nextDouble() < config.torchChance();
        for (int i = 0; i < length;i++) {
            BlockPos pos = blockpos.relative(direction,i);
            if (!level.getBlockState(pos).isAir() && !level.getBlockState(pos).getMaterial().isLiquid()) {
                blockCount++;
            } else {
                continue;
            }
            safeSetBlock(level,pos, Blocks.CAVE_AIR.defaultBlockState(), predicate);
            safeSetBlock(level,pos.above(), Blocks.CAVE_AIR.defaultBlockState(), predicate);

            safeSetBlock(level,pos.relative(direction.getClockWise()), Blocks.CAVE_AIR.defaultBlockState(), predicate);
            safeSetBlock(level,pos.above().relative(direction.getClockWise()), Blocks.CAVE_AIR.defaultBlockState(), predicate);
            if (placeTorch) {
                safeSetBlock(level,pos, Blocks.REDSTONE_TORCH.defaultBlockState(), predicate);
                placeTorch = false;
            }
        }
        return blockCount>0;
    }
}
