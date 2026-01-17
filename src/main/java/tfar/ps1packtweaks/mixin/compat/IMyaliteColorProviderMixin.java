package tfar.ps1packtweaks.mixin.compat;

import net.minecraft.core.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import vazkii.quark.content.world.block.IMyaliteColorProvider;

@Mixin(IMyaliteColorProvider.class)
public interface IMyaliteColorProviderMixin {
    /**
     * @author
     * @reason
     */
    @Overwrite(remap = false)
    static int getColor(BlockPos pos, float s, float b) {
        return 0xffffff;
    }
}
