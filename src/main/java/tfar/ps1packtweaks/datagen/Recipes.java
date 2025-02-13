package tfar.ps1packtweaks.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import tfar.ps1packtweaks.Init;

import java.util.function.Consumer;

public class Recipes extends RecipeProvider {
    public Recipes(DataGenerator generatorIn) {
        super(generatorIn);
    }

    @Override
    protected void buildCraftingRecipes(Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(Init.ModItems.PRISMARINE_ROD)
                .pattern("P")
                .pattern("P")
                .pattern("P")
                .define('P', Blocks.PRISMARINE_BRICKS)
                .unlockedBy("has_prismarine", has(Blocks.PRISMARINE_BRICKS))
                .save(consumer);
        ShapedRecipeBuilder.shaped(Items.TRIDENT)
                .pattern(" TT")
                .pattern(" HT")
                .pattern("R  ")
                .define('T', Init.ModItems.BARNACLE_TOOTH)
                .define('H', Items.HEART_OF_THE_SEA)
                .define('R', Init.ModItems.PRISMARINE_ROD)
                .unlockedBy("has_tooth", has(Init.ModItems.BARNACLE_TOOTH))
                .save(consumer);

    }
}
