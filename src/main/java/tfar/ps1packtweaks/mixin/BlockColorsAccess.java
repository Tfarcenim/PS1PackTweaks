package tfar.ps1packtweaks.mixin;

import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BlockColors.class)
public interface BlockColorsAccess {

    @Accessor
    java.util.Map<net.minecraftforge.registries.IRegistryDelegate<Block>, BlockColor> getBlockColors();


}
