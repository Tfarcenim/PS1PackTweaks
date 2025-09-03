package tfar.ps1packtweaks.mixin.compat.embeddium;

import me.jellysquid.mods.sodium.client.gl.compile.ChunkBuildContext;
import me.jellysquid.mods.sodium.client.render.chunk.compile.ChunkBuildResult;
import me.jellysquid.mods.sodium.client.render.chunk.tasks.ChunkRenderRebuildTask;
import me.jellysquid.mods.sodium.client.util.task.CancellationSource;
import net.minecraft.client.Minecraft;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tfar.ps1packtweaks.PS1PackTweaksConfig;
import tfar.ps1packtweaks.client.PS1PackTweaksClient;

@Mixin(ChunkRenderRebuildTask.class)
public class ChunkRenderRebuildTaskMixin {

    @Inject(method = "performBuild",at = @At("HEAD"),remap = false)
    private void trackReplace(ChunkBuildContext buildContext, CancellationSource cancellationSource, CallbackInfoReturnable<ChunkBuildResult> cir) {
        if (PS1PackTweaksClient.ticksSinceJoined<PS1PackTweaksClient.DIRT_TIME &&
                Minecraft.getInstance().level.getRandom().nextDouble() > PS1PackTweaksConfig.CLIENT.replaceBlocksChance.get()) {
                   PS1PackTweaksClient.ticksSinceJoined = PS1PackTweaksClient.DIRT_TIME+1;
        }
    }

    @ModifyVariable(method = "performBuild", at = @At(
            value = "INVOKE_ASSIGN", target = "Lnet/minecraft/core/BlockPos$MutableBlockPos;set(III)Lnet/minecraft/core/BlockPos$MutableBlockPos;",ordinal = 0
    ,shift = At.Shift.AFTER))
    private BlockState hackEmbeddiumRender(BlockState original) {
        return PS1PackTweaksClient.replaceBlockRender(original);
    }
}
