package tfar.ps1packtweaks.compat;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.ModList;

public enum ModIntegration {
    alexsmobs,
    brewingcauldron,
    guicompass,
    morehorsearmor,
    netherite_shulkers,
    shinyhorses,
    enderitemod,
    kazsend,
    kazs_end_village;
    public final boolean loaded;

    public ResourceLocation id(String path) {
        return new ResourceLocation(name(),path);
    }

    ModIntegration() {
        loaded = ModList.get().isLoaded(name());
    }
}
