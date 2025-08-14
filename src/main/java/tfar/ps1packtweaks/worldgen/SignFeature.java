package tfar.ps1packtweaks.worldgen;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SignBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

import java.util.List;
import java.util.Random;
import java.util.function.Predicate;

public class SignFeature extends Feature<SignFeatureConfig> {

    public SignFeature(Codec<SignFeatureConfig> pCodec) {
        super(pCodec);
    }

    @Override
    public boolean place(FeaturePlaceContext<SignFeatureConfig> pContext) {
        SignFeatureConfig config = pContext.config();
        WorldGenLevel level = pContext.level();
        BlockPos pos = pContext.origin();
        BlockState state = config.block().defaultBlockState();

        Random random = pContext.random();
        List<List<Component>> messages = pContext.config().text();
        List<Component> message = messages.get(random.nextInt(messages.size()));
        Predicate<BlockState> predicate = isReplaceable(BlockTags.FEATURES_CANNOT_REPLACE);
        boolean b = placeStableBlock(level, pos, state, predicate);
        if (b) {
        Component[] messageArray = message.toArray(Component[]::new);
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof SignBlockEntity signBlockEntity) {
                for (int i = 0; i < messageArray.length; i++) {
                    signBlockEntity.setMessage(i, messageArray[i]);
                }
                signBlockEntity.setChanged();
                System.out.println("Placed Sign At: " + pos);
                return true;
            }
        }
        return false;
    }

    protected boolean placeStableBlock(WorldGenLevel pLevel, BlockPos pPos, BlockState pState, Predicate<BlockState> pOldState) {
        if (pOldState.test(pLevel.getBlockState(pPos)) && pState.canSurvive(pLevel, pPos)) {
            pLevel.setBlock(pPos, pState, 2);
            return true;
        }
        return false;
    }
}
