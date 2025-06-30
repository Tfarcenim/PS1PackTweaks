package tfar.ps1packtweaks.mixin;

import net.minecraft.client.renderer.block.LiquidBlockRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.HalfTransparentBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tfar.ps1packtweaks.PS1PackTweaks;

@Mixin(LiquidBlockRenderer.class)
public abstract class GlassyMixin  {

    @Inject(method = "shouldRenderFace",at = @At("RETURN"),cancellable = true)
    private static void skipGlassRendering(BlockAndTintGetter pLevel, BlockPos pPos, FluidState pFluidState, BlockState pBlockState, Direction pSide, FluidState pNeighborFluid, CallbackInfoReturnable<Boolean> cir) {
        PS1PackTweaks.skipGlassRendering(pLevel, pPos, pFluidState, pBlockState, pSide, pNeighborFluid, cir);
    }
}
