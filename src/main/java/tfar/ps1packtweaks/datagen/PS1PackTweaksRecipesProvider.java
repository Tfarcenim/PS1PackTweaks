package tfar.ps1packtweaks.datagen;

import be.ephys.netherite_shulkers.NetheriteShulkers;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import tfar.ps1packtweaks.Init;
import tfar.ps1packtweaks.ModTags;
import tfar.ps1packtweaks.PS1PackTweaks;

import java.util.function.Consumer;

public class PS1PackTweaksRecipesProvider extends RecipeProvider {
    public PS1PackTweaksRecipesProvider(DataGenerator generatorIn) {
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
                .save(consumer, PS1PackTweaks.id("prismarine_rod"));
        ShapedRecipeBuilder.shaped(Items.TRIDENT)
                .pattern(" TT")
                .pattern(" HT")
                .pattern("R  ")
                .define('T', Init.ModItems.BARNACLE_TOOTH)
                .define('H', Items.HEART_OF_THE_SEA)
                .define('R', Init.ModItems.PRISMARINE_ROD)
                .unlockedBy("has_tooth", has(Init.ModItems.BARNACLE_TOOTH))
                .save(consumer, PS1PackTweaks.id("trident"));

        NBTCopyShapedRecipeBuilder.nbtCopyShaped(NetheriteShulkers.NETHERITE_SHULKER_BOX_BLOCK.get())
                .define('n', Items.NETHERITE_INGOT)
                .define('s', ModTags.SHULKER_BOXES)
                .pattern("nnn")
                .pattern("nsn")
                .pattern("nnn")
                .unlockedBy("has_shulker", has(ModTags.SHULKER_BOXES))
                .save(consumer, PS1PackTweaks.id("netherite_shulker"));


    }
}
