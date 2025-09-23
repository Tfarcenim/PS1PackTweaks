package tfar.ps1packtweaks;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;

public class ModTags {

    public static final TagKey<Block> HEROBRINE_SPAWNS_BEHIND = BlockTags.create(PS1PackTweaks.id("herobrine_spawns_behind"));
    public static final TagKey<Block> CAN_TUNNEL_THROUGH = BlockTags.create(PS1PackTweaks.id("can_tunnel_through"));
    public static final TagKey<EntityType<?>> MIDNIGHT_LURKERS = create("midnight_lurkers");

    public static final TagKey<Biome> HAS_CAMP = biome("has_structure/camp");
    public static final TagKey<Biome> HAS_RITUAL_SITE = biome("has_structure/ritual_site");
    public static final TagKey<Biome> HAS_PUMPKIN_FARM = biome("has_structure/pumpkin_farm");

    public static final TagKey<ConfiguredStructureFeature<?, ?>> END_CITY = TagKey.create(Registry.CONFIGURED_STRUCTURE_FEATURE_REGISTRY,
            PS1PackTweaks.id("end_city"));


    private static TagKey<Biome> biome(String pName) {
        return TagKey.create(Registry.BIOME_REGISTRY, PS1PackTweaks.id(pName));
    }

    private static TagKey<EntityType<?>> create(String pName) {
        return TagKey.create(Registry.ENTITY_TYPE_REGISTRY, PS1PackTweaks.id(pName));
    }
}
