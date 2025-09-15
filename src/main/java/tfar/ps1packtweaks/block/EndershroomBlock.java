package tfar.ps1packtweaks.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.MushroomBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

import java.util.function.Supplier;

public class EndershroomBlock extends MushroomBlock {
    public EndershroomBlock(Properties pProperties, Supplier<Holder<? extends ConfiguredFeature<?, ?>>> pFeatureSupplier) {
        super(pProperties, pFeatureSupplier);
    }

    @Override
    public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
        return true;
    }

    @Override
    protected boolean mayPlaceOn(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
        return true;
    }
}
