package tfar.ps1packtweaks.compat;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.ModList;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public enum ModIntegration {
    alexsmobs(false),
    brewingcauldron(false),
    enderitemod(false),
    guicompass(false),
    morehorsearmor(false),
    netherite_shulkers(false),
    shinyhorses(false),
    kazsend(false),
    kazs_end_village(false),
    midnightlurker(true),
    true_herobrine(true),
    playeranimator(false),
    weird_and_wonderous(true);

    public static final ModIntegration[] values = values();

    public final boolean loaded;
    public final boolean removeMobsWhenPure;

    public static final Set<String> removeWhenPure = Arrays.stream(values).filter(modIntegration -> modIntegration.removeMobsWhenPure)
            .map(Enum::name).collect(Collectors.toSet());

    public static boolean shouldRemoveMobs(String modid) {
        return removeWhenPure.contains(modid);
    }

    public ResourceLocation id(String path) {
        return new ResourceLocation(name(), path);
    }

    ModIntegration(boolean removeMobsWhenPure) {
        this.removeMobsWhenPure = removeMobsWhenPure;
        loaded = ModList.get().isLoaded(name());
    }
}
