package tfar.ps1packtweaks.worldgen;

import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.*;
import tfar.ps1packtweaks.Init;
import tfar.ps1packtweaks.PS1PackTweaks;

import java.util.ArrayList;
import java.util.List;

public class ModConfiguredFeatures {

    public static final Holder<ConfiguredFeature<TunnelConfiguration, ?>> CONFIGURED_TUNNEL = FeatureUtils.register(PS1PackTweaks.MOD_ID+":tunnel",
            Init.ModFeatures.TUNNEL,
            new TunnelConfiguration(16,24,.25f));

    public static final Holder<ConfiguredFeature<PyramidConfig, ?>> CONFIGURED_PYRAMID = FeatureUtils.register(PS1PackTweaks.MOD_ID+":pyramid",
            Init.ModFeatures.PYRAMID,
            new PyramidConfig(1));

    public static final Holder<ConfiguredFeature<SignFeatureConfig, ?>> CONFIGURED_OAK_SIGN = FeatureUtils.register(PS1PackTweaks.MOD_ID+":oak_sign",
            Init.ModFeatures.SIGN,
            new SignFeatureConfig(getTexts(), Blocks.OAK_SIGN));

    public static final Holder<ConfiguredFeature<SignFeatureConfig, ?>> CONFIGURED_BIRCH_SIGN = FeatureUtils.register(PS1PackTweaks.MOD_ID+":birch_sign",
            Init.ModFeatures.SIGN,
            new SignFeatureConfig(getTexts(), Blocks.BIRCH_SIGN));

    public static final Holder<ConfiguredFeature<SignFeatureConfig, ?>> CONFIGURED_JUNGLE_SIGN = FeatureUtils.register(PS1PackTweaks.MOD_ID+":jungle_sign",
            Init.ModFeatures.SIGN,
            new SignFeatureConfig(getTexts(), Blocks.JUNGLE_SIGN));

    public static final Holder<ConfiguredFeature<SignFeatureConfig, ?>> CONFIGURED_SPRUCE_SIGN = FeatureUtils.register(PS1PackTweaks.MOD_ID+":spruce_sign",
            Init.ModFeatures.SIGN,
            new SignFeatureConfig(getTexts(), Blocks.SPRUCE_SIGN));

    public static final Holder<ConfiguredFeature<SignFeatureConfig, ?>> CONFIGURED_ACACIA_SIGN = FeatureUtils.register(PS1PackTweaks.MOD_ID+":acacia_sign",
            Init.ModFeatures.SIGN,
            new SignFeatureConfig(getTexts(), Blocks.ACACIA_SIGN));

    public static final Holder<ConfiguredFeature<SignFeatureConfig, ?>> CONFIGURED_DARK_OAK_SIGN = FeatureUtils.register(PS1PackTweaks.MOD_ID+":dark_oak_sign",
            Init.ModFeatures.SIGN,
            new SignFeatureConfig(getTexts(), Blocks.DARK_OAK_SIGN));


    static List<PlacementModifier> commonPlacement(int pCount, PlacementModifier pHeightRange) {
        return placement(CountPlacement.of(pCount), pHeightRange);
    }

    private static List<PlacementModifier> placement(PlacementModifier p_195347_, PlacementModifier p_195348_) {
        return List.of(p_195347_, InSquarePlacement.spread(), p_195348_, BiomeFilter.biome());
    }

    static List<List<Component>> getTexts() {
        List<List<Component>> lists = new ArrayList<>();
        lists.add(List.of(new TextComponent("STOP")));
        lists.add(List.of(new TextComponent("LEAVE")));
        lists.add(List.of(new TextComponent("Can you see me?")));
        lists.add(List.of(new TextComponent("I see you")));
        lists.add(List.of(new TextComponent("RUN")));
        lists.add(List.of(new TextComponent("You shouldn't"),new TextComponent("be here")));
        lists.add(List.of(new TextComponent("I'm not dead")));
        lists.add(List.of(new TextComponent("Se upp på ryggen")));
        lists.add(List.of(new TextComponent("Bakom dig")));
        lists.add(List.of(new TextComponent("Du är inte säker")));
        lists.add(List.of(new TextComponent("Titta inte")));
        return lists;
    }

    //	- STOP
//	- LEAVE
//	- Can you see me?
//	- I see you
//	- RUN
//	- You shouldn't be here
//	- Do you hear it?
//	- I'm not dead
//	- Se upp på ryggen
//	- Bakom dig
//	- Du är inte säker
//	- Titta inte

    public static void init() {

    }

}
