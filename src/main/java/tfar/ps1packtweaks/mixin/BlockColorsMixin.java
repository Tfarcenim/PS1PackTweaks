package tfar.ps1packtweaks.mixin;

import me.jellysquid.mods.sodium.mixin.core.model.MixinBlockColors;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tfar.ps1packtweaks.client.PS1PackTweaksClient;

/**
 * @see MixinBlockColors#preRegisterColor(BlockColor, Block[], CallbackInfo)
 */
@SuppressWarnings("JavadocReference")
@Mixin(value = BlockColors.class,priority = 999)
public class BlockColorsMixin {
    @Inject(
            method = {"register"},
            at = {@At("HEAD")}
            ,cancellable = true
    )
    private void preRegisterColor(BlockColor provider, Block[] blocks, CallbackInfo ci) {
        if (PS1PackTweaksClient.shouldRemoveColor(blocks)) {
            ci.cancel();
        }
    }
}
