package tfar.ps1packtweaks.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.ConfiguredStructureTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;
import tfar.ps1packtweaks.PS1PackTweaks;

public class ConfiguredStructureTags extends ConfiguredStructureTagsProvider {
    public ConfiguredStructureTags(DataGenerator arg, @Nullable ExistingFileHelper existingFileHelper) {
        super(arg, PS1PackTweaks.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags() {
    }

    public String getName() {
        return "Configured Structure Feature Tags";
    }
}
