package tfar.ps1packtweaks.compat;

import net.minecraftforge.fml.ModList;

public enum ModIntegration {
    guicompass,
    morehorsearmor,
    netherite_shulkers,
    shinyhorses,
    enderitemod;
    public final boolean loaded;
    ModIntegration() {
        loaded = ModList.get().isLoaded(name());
    }
}
