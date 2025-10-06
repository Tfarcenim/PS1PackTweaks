package tfar.ps1packtweaks.mixin.compat;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import uno.rebellious.lavasponge.blocks.HotLavaSpongeBlock;

import javax.annotation.Nullable;
import java.util.List;

@Mixin(HotLavaSpongeBlock.class)
public class LavaSpongeBlockMixin {

    /**
     * @author tfar
     * @reason no tooltips
     */
    @Overwrite
    public void appendHoverText(ItemStack stack, @Nullable BlockGetter blockGetter, List<Component> list, TooltipFlag tooltipFlag) {
    }
}
