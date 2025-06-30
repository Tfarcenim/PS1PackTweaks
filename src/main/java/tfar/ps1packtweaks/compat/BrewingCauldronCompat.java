package tfar.ps1packtweaks.compat;

import net.minecraft.world.entity.ai.village.poi.PoiType;
import tfar.ps1packtweaks.PS1PackTweaks;

import java.util.Set;

public class BrewingCauldronCompat {

    public static void setup() {
        PS1PackTweaks.changePOI(PoiType.CLERIC, Set.of(tfar.brewingcauldron.Init.ModBlocks.BREWING_CAULDRON,
                tfar.brewingcauldron.Init.ModBlocks.WATER_BREWING_CAULDRON,
                tfar.brewingcauldron.Init.ModBlocks.LAVA_BREWING_CAULDRON,
                tfar.brewingcauldron.Init.ModBlocks.POWDER_SNOW_BREWING_CAULDRON));
    }
}
