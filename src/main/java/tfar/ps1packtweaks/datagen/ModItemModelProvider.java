package tfar.ps1packtweaks.datagen;

import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;
import tfar.ps1packtweaks.Init;
import tfar.ps1packtweaks.PS1PackTweaks;


public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, PS1PackTweaks.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        generatedItem(Init.ModItems.BARNACLE_TOOTH.getRegistryName().getPath());
        generatedItem(Init.ModItems.PRISMARINE_ROD.getRegistryName().getPath());
    }

    private void generatedItem(String path) {
        singleTexture(path, new ResourceLocation("item/generated"),
                "layer0", PS1PackTweaks.id("item/" + path));
    }

    protected void makeSimpleBlockItem(Item item, ResourceLocation loc) {
        String s = Registry.ITEM.getKey(item).toString();
        getBuilder(s)
                .parent(getExistingFile(loc));
    }

    protected void makeSimpleBlockItem(Item item) {
        makeSimpleBlockItem(item, PS1PackTweaks.id("block/" + Registry.ITEM.getKey(item).getPath()));
    }

    private void generatedItem(RegistryObject<? extends Item> item) {
        generatedItem(item.getId().getPath());
    }

}
