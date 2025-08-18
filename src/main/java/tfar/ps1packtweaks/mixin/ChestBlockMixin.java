package tfar.ps1packtweaks.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ChestBlock.class)
public class ChestBlockMixin {
    @Inject(method = "setPlacedBy",at = @At(value = "RETURN"),
            locals = LocalCapture.CAPTURE_FAILHARD)
    private void onSetPlacedBy(Level pLevel, BlockPos pPos, BlockState pState, LivingEntity pPlacer, ItemStack pStack, CallbackInfo ci) {
        BlockEntity blockentity = pLevel.getBlockEntity(pPos);
        if (blockentity != null) {
            blockentity.getTileData().putBoolean("ps1packtweaks:player_placed", true);
        }
    }
}
