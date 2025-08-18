package tfar.ps1packtweaks.worldgen;

import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.placement.*;
import tfar.ps1packtweaks.PS1PackTweaks;

import java.util.List;

public class ModPlacedFeatures {
    public static final Holder<PlacedFeature> PLACED_TUNNEL = PlacementUtils.register(PS1PackTweaks.MOD_ID+":tunnel", ModConfiguredFeatures.CONFIGURED_TUNNEL,
            tunnelPlacement(HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(128))));

    public static final Holder<PlacedFeature> PLACED_PYRAMID = PlacementUtils.register(PS1PackTweaks.MOD_ID+":pyramid", ModConfiguredFeatures.CONFIGURED_PYRAMID,
            pyramidPlacement());

    public static final Holder<PlacedFeature> PLACED_OAK_SIGN = PlacementUtils.register(PS1PackTweaks.MOD_ID+":oak_sign",
            ModConfiguredFeatures.CONFIGURED_OAK_SIGN,placeSigns());

    public static final Holder<PlacedFeature> PLACED_BIRCH_SIGN = PlacementUtils.register(PS1PackTweaks.MOD_ID+":birch_sign",
            ModConfiguredFeatures.CONFIGURED_BIRCH_SIGN,placeSigns());

    public static final Holder<PlacedFeature> PLACED_SPRUCE_SIGN = PlacementUtils.register(PS1PackTweaks.MOD_ID+":spruce_sign",
            ModConfiguredFeatures.CONFIGURED_SPRUCE_SIGN,placeSigns());

    public static final Holder<PlacedFeature> PLACED_JUNGLE_SIGN = PlacementUtils.register(PS1PackTweaks.MOD_ID+":jungle_sign",
            ModConfiguredFeatures.CONFIGURED_JUNGLE_SIGN,placeSigns());

    public static final Holder<PlacedFeature> PLACED_ACACIA_SIGN = PlacementUtils.register(PS1PackTweaks.MOD_ID+":acacia_sign",
            ModConfiguredFeatures.CONFIGURED_ACACIA_SIGN,placeSigns());

    public static final Holder<PlacedFeature> PLACED_DARK_OAK_SIGN = PlacementUtils.register(PS1PackTweaks.MOD_ID+":dark_oak_sign",
            ModConfiguredFeatures.CONFIGURED_DARK_OAK_SIGN,placeSigns());

    public static final Holder<PlacedFeature> COBBLE_ROCK = PlacementUtils.register(PS1PackTweaks.MOD_ID+":cobble_rock", ModConfiguredFeatures.COBBLE_ROCK, RarityFilter.onAverageOnceEvery(100), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome());



    private static List<PlacementModifier> tunnelPlacement(PlacementModifier heightPlacement) {
        return List.of(RarityFilter.onAverageOnceEvery(2), heightPlacement, BiomeFilter.biome());
    }

    public static List<PlacementModifier> pyramidPlacement() {
        return List.of(RarityFilter.onAverageOnceEvery(400), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BiomeFilter.biome());
    }

    static List<PlacementModifier> placeSigns() {
        return List.of(RarityFilter.onAverageOnceEvery(2048),InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BiomeFilter.biome());
    }
}
