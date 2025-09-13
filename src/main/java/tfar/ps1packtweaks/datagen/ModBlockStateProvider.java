package tfar.ps1packtweaks.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import tfar.ps1packtweaks.Init;
import tfar.ps1packtweaks.PS1PackTweaks;
import tfar.ps1packtweaks.compat.ModIntegration;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, PS1PackTweaks.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        signBlock(Init.ModBlocks.EBONY_SIGN,Init.ModBlocks.EBONY_WALL_SIGN,models().sign("ebony_sign",
                ModIntegration.kazsend.id("block/ebony_planks")));

        signBlock(Init.ModBlocks.ENDERVIOLET_SIGN,Init.ModBlocks.ENDERVIOLET_WALL_SIGN,models().sign("enderviolet_sign",
                ModIntegration.kazsend.id("block/enderviolet_planks")));
    }
}
