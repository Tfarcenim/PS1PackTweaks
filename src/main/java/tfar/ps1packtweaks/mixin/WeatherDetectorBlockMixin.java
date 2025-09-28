package tfar.ps1packtweaks.mixin;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import nl.qudical.weatherdetector.block.WeatherDetectorBlock;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.List;

@Mixin(WeatherDetectorBlock.class)
public class WeatherDetectorBlockMixin {

    /**
     * @author Tfar
     * @reason no tooltips
     */
    @Overwrite
    public void appendHoverText(ItemStack pStack, @Nullable BlockGetter pLevel, List<Component> pTooltip, TooltipFlag pFlag) {
    }
}
