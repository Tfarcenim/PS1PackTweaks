package tfar.ps1packtweaks.datagen.tags;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.world.level.biome.Biomes;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;
import tfar.ps1packtweaks.ModTags;
import tfar.ps1packtweaks.PS1PackTweaks;

public class ModBiomeTagsProvider extends BiomeTagsProvider {
    public ModBiomeTagsProvider(DataGenerator p_211094_,@Nullable ExistingFileHelper existingFileHelper) {
        super(p_211094_, PS1PackTweaks.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        tag(ModTags.HAS_CAMP).add(Biomes.END_BARRENS);
        tag(ModTags.HAS_PUMPKIN_FARM).add(Biomes.END_BARRENS);
        tag(ModTags.HAS_RITUAL_SITE).add(Biomes.END_BARRENS);
    }
}
