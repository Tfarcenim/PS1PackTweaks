package tfar.ps1packtweaks.datagen;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.critereon.*;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.advancements.AdvancementProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.data.ExistingFileHelper;
import tfar.ps1packtweaks.ModTags;
import tfar.ps1packtweaks.advancement.PlayerFoundEntityTrigger;
import tfar.ps1packtweaks.client.WorldLocker;
import tfar.ps1packtweaks.util.TextComponents;
import vazkii.quark.content.tools.module.EndermoshMusicDiscModule;

import java.util.function.Consumer;

public class ModAdvancementProvider extends AdvancementProvider {
    public ModAdvancementProvider(DataGenerator generatorIn, ExistingFileHelper fileHelperIn) {
        super(generatorIn, fileHelperIn);
    }

    @Override
    protected void registerAdvancements(Consumer<Advancement> consumer, ExistingFileHelper fileHelper) {
        Advancement cookie = Advancement.Builder.advancement()
                .display(Items.COOKIE, TextComponents.ADVANCEMENT_EAT_COOKIE_TITLE, TextComponents.ADVANCEMENT_EAT_COOKIE_DESC,
                        new ResourceLocation("textures/gui/advancements/backgrounds/end.png"),
                        FrameType.TASK, false, false, true)
                .addCriterion("eat_cookie", ConsumeItemTrigger.TriggerInstance.usedItem(Items.COOKIE))
                .save(consumer, WorldLocker.EAT_COOKIE,fileHelper);

        Advancement cake = Advancement.Builder.advancement()
                .display(Blocks.CAKE, TextComponents.ADVANCEMENT_PLACE_CAKE_TITLE, TextComponents.ADVANCEMENT_PLACE_CAKE_DESC,
                        new ResourceLocation("textures/gui/advancements/backgrounds/end.png"),
                        FrameType.TASK, false, false, true)
                .addCriterion("place_cake", PlacedBlockTrigger.TriggerInstance.placedBlock(Blocks.CAKE))
                .save(consumer, WorldLocker.PLACE_CAKE,fileHelper);

        Advancement snowGolem = Advancement.Builder.advancement()
                .display(Blocks.CARVED_PUMPKIN, TextComponents.ADVANCEMENT_SUMMON_SNOW_GOLEM_TITLE, TextComponents.ADVANCEMENT_SUMMON_SNOW_GOLEM_DESC,
                        new ResourceLocation("textures/gui/advancements/backgrounds/end.png"),
                        FrameType.TASK, false, false, true)
                .addCriterion("summon_snow_golem", SummonedEntityTrigger.TriggerInstance.summonedEntity(EntityPredicate.Builder
                        .entity().entityType(EntityTypePredicate.of(EntityType.SNOW_GOLEM))))
                .save(consumer, WorldLocker.SUMMON_SNOW_GOLEM,fileHelper);

        Advancement playRecord = Advancement.Builder.advancement()
                .display(Items.MUSIC_DISC_11, TextComponents.ADVANCEMENT_PLAY_RECORD_TITLE, TextComponents.ADVANCEMENT_PLAY_RECORD_DESC,
                        new ResourceLocation("textures/gui/advancements/backgrounds/end.png"),
                        FrameType.TASK, false, false, true)
                .addCriterion("play_disc_11", ItemUsedOnBlockTrigger.TriggerInstance.itemUsedOnBlock(
                        LocationPredicate.Builder.location().setBlock(BlockPredicate.Builder.block().of(Blocks.JUKEBOX).build()),
                        ItemPredicate.Builder.item().of(Items.MUSIC_DISC_11)))
                .save(consumer, WorldLocker.PLAY_DISC_11,fileHelper);

        Advancement drinkPotion = Advancement.Builder.advancement()
                .display(Items.POTION, TextComponents.ADVANCEMENT_DRINK_POTION_TITLE, TextComponents.ADVANCEMENT_DRINK_POTION_DESC,
                        new ResourceLocation("textures/gui/advancements/backgrounds/end.png"),
                        FrameType.TASK, false, false, true)
                .addCriterion("drink_potion", ConsumeItemTrigger.TriggerInstance.usedItem(Items.POTION))
                .save(consumer, WorldLocker.DRINK_POTION,fileHelper);

        Advancement craftJackOLantern = Advancement.Builder.advancement()
                .display(Blocks.JACK_O_LANTERN, TextComponents.ADVANCEMENT_CRAFT_JACK_O_LANTERN_TITLE, TextComponents.ADVANCEMENT_CRAFT_JACK_O_LANTERN_DESC,
                        new ResourceLocation("textures/gui/advancements/backgrounds/end.png"),
                        FrameType.TASK, false, false, true)
                .addCriterion("jack_o_lantern", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.JACK_O_LANTERN))
                .save(consumer, WorldLocker.CRAFT_JACK_O_LANTERN,fileHelper);

        Advancement craftAnything = Advancement.Builder.advancement()
                .display(Blocks.CRAFTING_TABLE, TextComponents.ADVANCEMENT_CRAFT_ANYTHING_TITLE, TextComponents.ADVANCEMENT_CRAFT_ANYTHING_DESC,
                        new ResourceLocation("textures/gui/advancements/backgrounds/end.png"),
                        FrameType.TASK, false, false, true)
                .addCriterion("craft_anything", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.JACK_O_LANTERN))
                .save(consumer, WorldLocker.CRAFT_ANYTHING,fileHelper);


        Advancement killSpider = Advancement.Builder.advancement()
                .display(Items.SPIDER_EYE, TextComponents.ADVANCEMENT_KILL_SPIDER_TITLE, TextComponents.ADVANCEMENT_KILL_SPIDER_DESC,
                        new ResourceLocation("textures/gui/advancements/backgrounds/end.png"),
                        FrameType.TASK, false, false, true)
                .addCriterion("kill_spider", KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder
                        .entity().entityType(EntityTypePredicate.of(EntityType.SPIDER))))
                .save(consumer, WorldLocker.KILL_SPIDER,fileHelper);

        Advancement findLurker = Advancement.Builder.advancement()
                .display(Items.SPIDER_EYE, TextComponents.ADVANCEMENT_FIND_LURKER_TITLE, TextComponents.ADVANCEMENT_FIND_LURKER_DESC,
                        new ResourceLocation("textures/gui/advancements/backgrounds/end.png"),
                        FrameType.TASK, false, false, true)
                .addCriterion("find_lurker", PlayerFoundEntityTrigger.TriggerInstance.located(EntityPredicate.Builder
                        .entity().entityType(EntityTypePredicate.of(ModTags.MIDNIGHT_LURKERS)).build()))
                .save(consumer, WorldLocker.ENCOUNTER_MIDNIGHT_LURKER,fileHelper);

        Advancement playEndermosh = Advancement.Builder.advancement()
                .display(EndermoshMusicDiscModule.endermosh, TextComponents.ADVANCEMENT_PLAY_ENDERMOSH_TITLE, TextComponents.ADVANCEMENT_PLAY_ENDERMOSH_DESC,
                        new ResourceLocation("textures/gui/advancements/backgrounds/end.png"),
                        FrameType.TASK, false, false, true)
                .addCriterion("play_endermosh", ItemUsedOnBlockTrigger.TriggerInstance.itemUsedOnBlock(
                        LocationPredicate.Builder.location().setBlock(BlockPredicate.Builder.block().of(Blocks.JUKEBOX).build()),
                        ItemPredicate.Builder.item().of(EndermoshMusicDiscModule.endermosh)))
                .save(consumer, WorldLocker.PLAY_ENDERMOSH,fileHelper);

        Advancement petDied = Advancement.Builder.advancement()
                .display(Items.SKELETON_SKULL, TextComponents.ADVANCEMENT_PLAYER_PET_DIED_TITLE, TextComponents.ADVANCEMENT_PLAYER_PET_DIED_DESC,
                        new ResourceLocation("textures/gui/advancements/backgrounds/end.png"),
                        FrameType.TASK, false, false, true)
                .addCriterion("kill_spider", KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder
                        .entity().entityType(EntityTypePredicate.of(EntityType.SPIDER))))
                .save(consumer, WorldLocker.PLAYER_PET_DIES,fileHelper);

    }
}
