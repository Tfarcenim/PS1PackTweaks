package tfar.ps1packtweaks.datagen;

import com.Apothic0n.StarryEnd.core.objects.StarryEndBlocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.item.crafting.Ingredient;
import tfar.ps1packtweaks.Init;

import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider {
    public ModRecipeProvider(DataGenerator pGenerator) {
        super(pGenerator);
    }

    @Override
    protected void buildCraftingRecipes(Consumer<FinishedRecipe> consumer) {
        woodenBoat(consumer, Init.ModItems.EBONY_BOAT, StarryEndBlocks.EBONY_PLANKS.get());
        woodenBoat(consumer, Init.ModItems.ENDERVIOLET_BOAT, StarryEndBlocks.ENDERVIOLET_PLANKS.get());

        signBuilder(Init.ModItems.EBONY_SIGN, Ingredient.of(StarryEndBlocks.EBONY_PLANKS.get()))
                .unlockedBy(getHasName(StarryEndBlocks.EBONY_PLANKS.get()),has(StarryEndBlocks.EBONY_PLANKS.get()))
                .save(consumer);
        signBuilder(Init.ModItems.ENDERVIOLET_SIGN, Ingredient.of(StarryEndBlocks.ENDERVIOLET_PLANKS.get()))
                .unlockedBy(getHasName(StarryEndBlocks.ENDERVIOLET_PLANKS.get()),has(StarryEndBlocks.ENDERVIOLET_PLANKS.get()))
                .save(consumer);
    }
}
