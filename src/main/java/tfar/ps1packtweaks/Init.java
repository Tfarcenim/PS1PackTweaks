package tfar.ps1packtweaks;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.decoration.Motive;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraftforge.common.data.GlobalLootModifierProvider;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import tfar.ps1packtweaks.advancement.ItemCraftedTrigger;
import tfar.ps1packtweaks.advancement.PetKilledTrigger;
import tfar.ps1packtweaks.advancement.PlayerFoundEntityTrigger;
import tfar.ps1packtweaks.entity.Barnacle;
import tfar.ps1packtweaks.entity.HerobrineEntity;
import tfar.ps1packtweaks.entity.InvisibleEntity;
import tfar.ps1packtweaks.entity.ScriptedMidnightLurker;
import tfar.ps1packtweaks.worldgen.*;

public class Init {

    public static final PlayerFoundEntityTrigger PLAYER_FOUND_ENTITY = CriteriaTriggers.register(new PlayerFoundEntityTrigger());
    public static final ItemCraftedTrigger ITEM_CRAFTED = CriteriaTriggers.register(new ItemCraftedTrigger());
    public static final PetKilledTrigger PLAYER_PET_KILLED = CriteriaTriggers.register(new PetKilledTrigger());

    public static void init() {

    }

    public static class ModItems{
        public static final Item BARNACLE_TOOTH = new Item(new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS));
        public static final Item PRISMARINE_ROD = new Item(new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS));
    }

    public static class ModEntityTypes {
        public static final EntityType<Barnacle> BARNACLE = EntityType.Builder
                .of(Barnacle::new, MobCategory.MONSTER)
                .sized(1.2F, 1.2F)
                .build(new ResourceLocation(PS1PackTweaks.MOD_ID, "barnacle").toString());

        public static final EntityType<HerobrineEntity> HEROBRINE = EntityType.Builder.of(HerobrineEntity::new, MobCategory.MONSTER)
                .sized(0.6F, 1.95F).clientTrackingRange(8).build("");

        public static final EntityType<ScriptedMidnightLurker> SCRIPTED_MIDNIGHT_LURKER =  EntityType.Builder.of(ScriptedMidnightLurker::new, MobCategory.MONSTER)
                .setTrackingRange(8)
                .setUpdateInterval(3).fireImmune().sized(0.7F, 2.5F).build("");

        public static final EntityType<InvisibleEntity> INVISIBLE_ENTITY = EntityType.Builder.of(InvisibleEntity::new, MobCategory.MONSTER)
                .sized(0.6F, 1.95F).clientTrackingRange(8).build("");
    }

    public static class ModSounds {
        public static final SoundEvent BARNACLE_AMBIENT = new SoundEvent(PS1PackTweaks.id( "barnacle_ambient"));
        public static final SoundEvent BARNACLE_HURT = new SoundEvent(PS1PackTweaks.id("barnacle_hurt"));
        public static final SoundEvent BARNACLE_DEATH = new SoundEvent(PS1PackTweaks.id( "barnacle_death"));
        public static final SoundEvent BARNACLE_FLOP = new SoundEvent(PS1PackTweaks.id( "barnacle_flop"));
        public static final SoundEvent SCREEN = new SoundEvent(PS1PackTweaks.id("screen"));
    }

    public static class ModBlocks {
    }

    public static class ModPaintings {
        public static final Motive CURSED_COURBET = new Motive(32,16);
    }

    public static class ModFeatures {
        public static final Feature<TunnelConfiguration> TUNNEL = new TunnelFeature(TunnelConfiguration.CODEC);
        public static final Feature<SignFeatureConfig> SIGN = new SignFeature(SignFeatureConfig.CODEC);
        public static final Feature<PyramidConfig> PYRAMID = new SandPyramidFeature(PyramidConfig.CODEC);
    }

    public static class GlobalLootModifiers {
        public static final DuplicateOutputsLootModifier.Serializer DUPLICATE_OUTPUTS = new DuplicateOutputsLootModifier.Serializer();
    }

}
