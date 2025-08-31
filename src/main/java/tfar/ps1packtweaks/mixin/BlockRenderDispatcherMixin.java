package tfar.ps1packtweaks.mixin;

import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import tfar.ps1packtweaks.client.PS1PackTweaksClient;

@Mixin(BlockRenderDispatcher.class)
//only works with vanilla!
public abstract class BlockRenderDispatcherMixin {

    public BlockRenderDispatcherMixin() {
    }

    @ModifyVariable(
            method = "renderBatched(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/BlockAndTintGetter;Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;ZLjava/util/Random;Lnet/minecraftforge/client/model/data/IModelData;)Z",
            at = @At(value = "HEAD"),
            remap = false,
            argsOnly = true)
    public BlockState replaceBlock(BlockState original) {
        //   if (Minecraft.getInstance().level.getRandom().nextDouble() > PS1PackTweaksConfig.CLIENT.replaceBlocksChance.get()) {
        //       PS1PackTweaksClient.ticksSinceJoined = PS1PackTweaksClient.DIRT_TIME;
        //   }
        return Blocks.DIRT.defaultBlockState();
    }

    @ModifyVariable(
            method = "renderLiquid",
            at = @At("HEAD"),
            argsOnly = true)
    public BlockState replaceWater(BlockState original) {
        if (original.is(Blocks.WATER)) {
            return Blocks.LAVA.defaultBlockState();
        }
        return original;
    }

    @ModifyVariable(
            method = "renderLiquid",
            at = @At("HEAD"),
            argsOnly = true)
    public FluidState replaceWater1(FluidState original) {
        return PS1PackTweaksClient.replaceFluidRender(original);
    }
}
