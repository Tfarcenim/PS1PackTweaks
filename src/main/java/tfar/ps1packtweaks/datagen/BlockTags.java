package tfar.ps1packtweaks.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import tfar.ps1packtweaks.PS1PackTweaks;

import javax.annotation.Nullable;

public class BlockTags extends BlockTagsProvider {
    public BlockTags(final DataGenerator generatorIn, @Nullable final ExistingFileHelper existingFileHelper) {
        super(generatorIn, PS1PackTweaks.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags() {

    }
}
