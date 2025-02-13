package tfar.ps1packtweaks.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

public class DataGenerators {
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        final ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        if (event.includeServer()) {
       //     generator.addProvider(new Recipes(generator));
       //     generator.addProvider(new LootTables(generator));
        //    BlockTags blockTags = new BlockTags(generator, existingFileHelper);
        //    generator.addProvider(blockTags);
          //  generator.addProvider(new ItemTags(generator, blockTags, existingFileHelper));
           // generator.addProvider(new ConfiguredStructureTags(generator, existingFileHelper));
        }
        if (event.includeClient()) {
           // generator.addProvider(new BlockStates(generator, existingFileHelper));
           // generator.addProvider(new BlockModels(generator, existingFileHelper));
          //  generator.addProvider(new ItemModels(generator, existingFileHelper));
        }
    }
}
