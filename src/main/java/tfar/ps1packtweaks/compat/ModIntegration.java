package tfar.ps1packtweaks.compat;

import net.minecraftforge.fml.ModList;

public enum ModIntegration {
    shinyhorses;
    public final boolean loaded;
    ModIntegration() {
        loaded = ModList.get().isLoaded(name());
    }
}
