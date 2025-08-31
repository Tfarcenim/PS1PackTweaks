package tfar.ps1packtweaks.mixin.compat.embeddium;

import me.jellysquid.mods.sodium.client.render.pipeline.FluidRenderer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import tfar.ps1packtweaks.client.PS1PackTweaksClient;

@Mixin(FluidRenderer.class)
//@Debug(export = true)
public class FluidRendererMixin {
    @ModifyVariable(method = {"render","isSideExposed","isFluidOccluded","fluidHeight"}, at = @At(value = "INVOKE_ASSIGN",
            target = "Lnet/minecraft/world/level/BlockAndTintGetter;getBlockState(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;", shift = At.Shift.AFTER))
    private BlockState hackRenderer(BlockState original) {
        return PS1PackTweaksClient.replaceBlockRender(original);
    }

    @ModifyVariable(method = "fluidHeight",at = @At(
            value = "INVOKE_ASSIGN", target = "Lnet/minecraft/world/level/BlockAndTintGetter;getFluidState(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/material/FluidState;", shift = At.Shift.AFTER)
            ,ordinal = 1
    )
    private FluidState fix(FluidState original) {
        return PS1PackTweaksClient.replaceFluidRender(original);
    }
}
