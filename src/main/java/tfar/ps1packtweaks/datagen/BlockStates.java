package tfar.ps1packtweaks.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import tfar.ps1packtweaks.PS1PackTweaks;

public class BlockStates extends BlockStateProvider {
    public BlockStates(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, PS1PackTweaks.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
    }
}
