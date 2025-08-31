package tfar.ps1packtweaks.mixin;

import net.minecraftforge.event.world.BlockEvent;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(BlockEvent.BreakEvent.class)
//is this needed?
public class BlockEventMixin {
    @ModifyArg(method = "<init>",at = @At(
            value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;getExpDrop(Lnet/minecraft/world/level/LevelReader;Lnet/minecraft/core/BlockPos;II)I"),index = 2)
    private int modifyFortune(int original) {
        return 1000;
    }
}
