package tfar.ps1packtweaks.mixin;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Material;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(Blocks.class)
public class BlocksMixin {
    @ModifyArg(method = "<clinit>",at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;of(Lnet/minecraft/world/level/material/Material;)Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;")
            ,slice = @Slice(
                    from = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/SnowLayerBlock;<init>(Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)V"),
            to = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/IceBlock;<init>(Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)V")
    )
    )
    private static Material changeMat(Material original) {
        return Material.ICE_SOLID;
    }



}
