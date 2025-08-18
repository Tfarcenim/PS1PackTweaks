package tfar.ps1packtweaks.mixin;

import net.minecraft.client.renderer.block.LiquidBlockRenderer;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LiquidBlockRenderer.class)
@Debug(export = true)
public class LiquidBlockRendererMixin {
    @ModifyVariable(method = "tesselate",at = @At(value = "STORE",
            target = "Lnet/minecraftforge/fluids/FluidAttributes;getColor(Lnet/minecraft/world/level/BlockAndTintGetter;Lnet/minecraft/core/BlockPos;)I"))
  int replaceColor(int value) {
       return 0xffffffff;
    }
}
