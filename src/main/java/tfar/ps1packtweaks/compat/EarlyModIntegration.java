package tfar.ps1packtweaks.compat;

import net.minecraftforge.fml.loading.LoadingModList;

public enum EarlyModIntegration {
    shinyhorses;
    public final boolean loaded;
    EarlyModIntegration() {
        loaded = LoadingModList.get().getModFileById(name()) != null;
    }
}
