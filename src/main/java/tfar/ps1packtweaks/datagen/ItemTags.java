package tfar.ps1packtweaks.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import tfar.ps1packtweaks.PS1PackTweaks;

import javax.annotation.Nullable;

public class ItemTags extends ItemTagsProvider {
    public ItemTags(final DataGenerator generatorIn, ModBlockTagProvider blockTagProvider, @Nullable final ExistingFileHelper existingFileHelper) {
        super(generatorIn, blockTagProvider, PS1PackTweaks.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags() {
    }
}
