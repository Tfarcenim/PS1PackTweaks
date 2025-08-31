package tfar.ps1packtweaks.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;
import tfar.ps1packtweaks.compat.EnderiteModCompat;

public class ModDataGenerator {
    public static void gatherData(GatherDataEvent event) {
        EnderiteModCompat.setup();


        DataGenerator generator = event.getGenerator();
        final ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        if (event.includeServer()) {
            generator.addProvider(new Recipes(generator));
            generator.addProvider(new LootTables(generator));
            ModBlockTagProvider blockTags = new ModBlockTagProvider(generator, existingFileHelper);
            generator.addProvider(blockTags);
            generator.addProvider(new ModItemTagProvider(generator, blockTags, existingFileHelper));
            generator.addProvider(new ConfiguredStructureTags(generator, existingFileHelper));
            generator.addProvider(new ModGlobalLootModifierProvider(generator));
        }
        if (event.includeClient()) {
            generator.addProvider(new ModBlockStateProvider(generator, existingFileHelper));
            generator.addProvider(new BlockModels(generator, existingFileHelper));
            generator.addProvider(new ModItemModelProvider(generator, existingFileHelper));
            generator.addProvider(new ModLangProvider(generator));
        }
    }
}
