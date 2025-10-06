package tfar.ps1packtweaks.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.HalfTransparentBlock;
import net.minecraft.world.level.block.IceBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Random;

@Mixin(IceBlock.class)
public abstract class IceBlockMixin extends HalfTransparentBlock {


    @Shadow protected abstract void melt(BlockState pState, Level pLevel, BlockPos pPos);

    public IceBlockMixin(Properties pProperties) {
        super(pProperties);
    }

    /**
     * @author
     * @reason
     */
    @Override
    public boolean skipRendering(BlockState pState, BlockState pAdjacentBlockState, Direction pDirection) {
        return false;
    }

    private static final Direction[] DIRECTIONS = Direction.values();

    @Override
    public void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, Random pRandom) {

        for (Direction direction : DIRECTIONS) {
            int blockLight = pLevel.getBrightness(LightLayer.BLOCK,pPos.relative(direction));
            if (blockLight > 12) {
                this.melt(pState, pLevel, pPos);
                break;
            }
        }
    }

    @Override
    public boolean propagatesSkylightDown(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
        return false;
    }

    /*@Override
    public float getShadeBrightness(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
        return 1;
    }*/
}
