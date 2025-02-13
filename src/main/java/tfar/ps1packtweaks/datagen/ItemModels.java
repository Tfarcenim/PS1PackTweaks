package tfar.ps1packtweaks.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;
import tfar.ps1packtweaks.Init;
import tfar.ps1packtweaks.PS1PackTweaks;


public class ItemModels extends ItemModelProvider {
    public ItemModels(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, PS1PackTweaks.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {

        generatedItem(Init.ModItems.BARNACLE_TOOTH.getRegistryName().getPath());

    }

    private void generatedItem(String path) {
        singleTexture(path, new ResourceLocation("item/generated"),
                "layer0", PS1PackTweaks.id("item/" + path));
    }

    private void generatedBlock(String path) {
        singleTexture(path, new ResourceLocation("item/generated"),
                "layer0", PS1PackTweaks.id("block/" + path));
    }

    private void generatedItem(RegistryObject<? extends Item> item) {
        generatedItem(item.getId().getPath());
    }

    private void egg(RegistryObject<? extends SpawnEggItem> obj) {
        String path = obj.getId().getPath();
        withExistingParent(path, new ResourceLocation("item/template_spawn_egg"));
    }

    private void wood(String path) {
        tree(path);
        generatedItem(path + "_door");
        withExistingParent(path + "_button", PS1PackTweaks.id("block/" + path + "_button_inventory"));
        withExistingParent(path + "_fence", PS1PackTweaks.id("block/" + path + "_fence_inventory"));
        withExistingParent(path + "_fence_gate", PS1PackTweaks.id("block/" + path + "_fence_gate"));
        withExistingParent(path + "_planks", PS1PackTweaks.id("block/" + path + "_planks"));
        withExistingParent(path + "_pressure_plate", PS1PackTweaks.id("block/" + path + "_pressure_plate"));
        withExistingParent(path + "_slab", PS1PackTweaks.id("block/" + path + "_slab"));
        withExistingParent(path + "_stairs", PS1PackTweaks.id("block/" + path + "_stairs"));
        withExistingParent(path + "_trapdoor", PS1PackTweaks.id("block/" + path + "_trapdoor_bottom"));
        singleTexture(path + "_sign", new ResourceLocation("item/generated"),
                "layer0", new ResourceLocation("item/acacia_sign"));
//        generatedItem(path + "_sign");
    }

    private void tree(String path) {
        generatedBlock(path + "_sapling");
        withExistingParent(path + "_leaves", PS1PackTweaks.id("block/" + path + "_leaves"));
        withExistingParent(path + "_log", PS1PackTweaks.id("block/" + path + "_log"));
        withExistingParent(path + "_wood", PS1PackTweaks.id("block/" + path + "_wood"));
        withExistingParent("stripped_" + path + "_log", PS1PackTweaks.id("block/stripped_" + path + "_log"));
        withExistingParent("stripped_" + path + "_wood", PS1PackTweaks.id("block/stripped_" + path + "_wood"));
    }
}
