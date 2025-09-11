package tfar.ps1packtweaks;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;

public class ModTags {

    public static final TagKey<Block> HEROBRINE_SPAWNS_BEHIND = BlockTags.create(PS1PackTweaks.id("herobrine_spawns_behind"));
    public static final TagKey<Block> CAN_TUNNEL_THROUGH = BlockTags.create(PS1PackTweaks.id("can_tunnel_through"));
    public static final TagKey<EntityType<?>> MIDNIGHT_LURKERS = create("midnight_lurkers");

    private static TagKey<EntityType<?>> create(String pName) {
        return TagKey.create(Registry.ENTITY_TYPE_REGISTRY, PS1PackTweaks.id(pName));
    }
}
