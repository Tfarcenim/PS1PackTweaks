package tfar.ps1packtweaks.recipes;

import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import tfar.ps1packtweaks.Init;

public class NBTCopyShapedRecipe extends ShapedRecipe {
    public NBTCopyShapedRecipe(ResourceLocation pId, String pGroup, int pWidth, int pHeight, NonNullList<Ingredient> pRecipeItems, ItemStack pResult) {
        super(pId, pGroup, pWidth, pHeight, pRecipeItems, pResult);
    }

    public NBTCopyShapedRecipe(ShapedRecipe pRecipe) {
        this(pRecipe.getId(), pRecipe.getGroup(), pRecipe.getWidth(), pRecipe.getHeight(),pRecipe.getIngredients(),pRecipe.getResultItem());
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Init.ModRecipeSerializers.NBT_COPY_CRAFTING_SHAPED;
    }

    @Override
    public ItemStack assemble(CraftingContainer pInv) {
        ItemStack assemble = super.assemble(pInv);
        ItemStack middleStack = pInv.getItem(4);
        if (middleStack.hasTag()) {
            assemble.setTag(middleStack.getTag());
        }
        return assemble;
    }

    public static class Serializer extends ShapedRecipe.Serializer {
        @Override
        public ShapedRecipe fromJson(ResourceLocation pRecipeId, JsonObject pJson) {
            return new NBTCopyShapedRecipe(super.fromJson(pRecipeId, pJson));
        }

        @Override
        public ShapedRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
            return new NBTCopyShapedRecipe(super.fromNetwork(pRecipeId, pBuffer));
        }
    }
}
