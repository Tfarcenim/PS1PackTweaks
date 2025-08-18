package tfar.ps1packtweaks.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tfar.ps1packtweaks.PS1PackTweaks;

import java.util.Random;

@Mixin(BlockBehaviour.class)
public class BlockBehaviourMixin {
    @Inject(method = "randomTick",at = @At("HEAD"))
    private void onRandomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, Random pRandom, CallbackInfo ci) {
        PS1PackTweaks.onRandomTick((BlockBehaviour)(Object)this,pState,pLevel,pPos,pRandom);
    }
}
