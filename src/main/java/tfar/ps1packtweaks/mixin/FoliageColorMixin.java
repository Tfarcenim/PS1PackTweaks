package tfar.ps1packtweaks.mixin;

import net.minecraft.world.level.FoliageColor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(FoliageColor.class)
public class FoliageColorMixin {
    /**
     * @author Tfar
     * @reason patch birch color
     */
    @Overwrite
    public static int getBirchColor() {
        return 0xffffffff;
    }

    /**
     * @author Tfar
     * @reason patch spruce color
     */
    @Overwrite
    public static int getEvergreenColor() {
        return 0xffffffff;
    }
}
