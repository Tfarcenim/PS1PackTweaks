package tfar.ps1packtweaks.datagen;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import tfar.ps1packtweaks.Init;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class NBTCopyShapedRecipeBuilder extends ShapedRecipeBuilder {
    public NBTCopyShapedRecipeBuilder(ItemLike pResult, int pCount) {
        super(pResult, pCount);
    }

    public static ShapedRecipeBuilder nbtCopyShaped(ItemLike pResult) {
        return nbtCopyShaped(pResult, 1);
    }

    public static NBTCopyShapedRecipeBuilder nbtCopyShaped(ItemLike pResult, int pCount) {
        return new NBTCopyShapedRecipeBuilder(pResult, pCount);
    }

    @Override
    public void save(Consumer<FinishedRecipe> pFinishedRecipeConsumer, ResourceLocation pRecipeId) {
        //this.ensureValid(pRecipeId);
        this.advancement.parent(new ResourceLocation("recipes/root")).addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(pRecipeId)).rewards(AdvancementRewards.Builder.recipe(pRecipeId)).requirements(RequirementsStrategy.OR);
        pFinishedRecipeConsumer.accept(new Result(pRecipeId, this.result, this.count, this.group == null ? "" : this.group, this.rows, this.key, this.advancement, new ResourceLocation(pRecipeId.getNamespace(), "recipes/" + this.result.getItemCategory().getRecipeFolderName() + "/" + pRecipeId.getPath())));
    }

    public static class Result extends ShapedRecipeBuilder.Result {
        public Result(ResourceLocation pId, Item pResult, int pCount, String pGroup, List<String> pPattern, Map<Character, Ingredient> pKey, Advancement.Builder pAdvancement, ResourceLocation pAdvancementId) {
            super(pId, pResult, pCount, pGroup, pPattern, pKey, pAdvancement, pAdvancementId);
        }

        @Override
        public RecipeSerializer<?> getType() {
            return Init.ModRecipeSerializers.NBT_COPY_CRAFTING_SHAPED;
        }
    }
}
