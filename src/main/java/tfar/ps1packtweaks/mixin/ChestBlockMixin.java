package tfar.ps1packtweaks.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractChestBlock;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.function.Supplier;

@Mixin(ChestBlock.class)
public abstract class ChestBlockMixin extends AbstractChestBlock {
    @Inject(method = "setPlacedBy",at = @At(value = "RETURN"),
            locals = LocalCapture.CAPTURE_FAILHARD)
    private void onSetPlacedBy(Level pLevel, BlockPos pPos, BlockState pState, LivingEntity pPlacer, ItemStack pStack, CallbackInfo ci) {
        BlockEntity blockentity = pLevel.getBlockEntity(pPos);
        if (blockentity != null) {
            blockentity.getTileData().putBoolean("ps1packtweaks:player_placed", true);
        }
    }


    protected ChestBlockMixin(BlockBehaviour.Properties $$0, Supplier $$1) {
        super($$0, $$1);
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public VoxelShape getShape(BlockState $$0, BlockGetter $$1, BlockPos $$2, CollisionContext $$3) {
        return Shapes.block();
    }

    @Override
    public float getShadeBrightness(BlockState $$0, BlockGetter $$1, BlockPos $$2) {
        return 1;
    }

    @Override
    public boolean propagatesSkylightDown(BlockState $$0, BlockGetter $$1, BlockPos $$2) {
        return true;
    }

}
