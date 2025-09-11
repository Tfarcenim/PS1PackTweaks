package tfar.ps1packtweaks.datagen.tags;

import net.enderitemc.enderitemod.misc.EnderiteTag;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.onvoid.morehorsearmor.common.MoreHorseArmorItems;
import tfar.ps1packtweaks.PS1PackTweaks;

import javax.annotation.Nullable;

public class ModItemTagProvider extends ItemTagsProvider {
    public ModItemTagProvider(final DataGenerator generatorIn, ModBlockTagProvider blockTagProvider, @Nullable final ExistingFileHelper existingFileHelper) {
        super(generatorIn, blockTagProvider, PS1PackTweaks.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        tag(EnderiteTag.ENDERITE_ITEM).add(MoreHorseArmorItems.ENDERITE_HORSE_ARMOR.get());
    }
}
