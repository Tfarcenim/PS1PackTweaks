package tfar.ps1packtweaks.datagen.tags;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.ConfiguredStructureTagsProvider;
import net.minecraft.world.level.levelgen.structure.BuiltinStructures;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;
import tfar.ps1packtweaks.ModTags;
import tfar.ps1packtweaks.PS1PackTweaks;

public class ModStructureTagsProvider extends ConfiguredStructureTagsProvider {
    public ModStructureTagsProvider(DataGenerator pGenerator, @Nullable ExistingFileHelper existingFileHelper) {
        super(pGenerator, PS1PackTweaks.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        tag(ModTags.END_CITY).add(BuiltinStructures.END_CITY);
    }
}
