package tfar.ps1packtweaks.compat;

import net.minecraftforge.fml.ModList;

public enum ModIntegration {
    morehorsearmor,
    shinyhorses;
    public final boolean loaded;
    ModIntegration() {
        loaded = ModList.get().isLoaded(name());
    }
}
