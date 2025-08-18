package tfar.ps1packtweaks.datagen;

import net.enderitemc.enderitemod.init.Registration;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import tfar.ps1packtweaks.ModTags;
import tfar.ps1packtweaks.PS1PackTweaks;

import javax.annotation.Nullable;

public class ModBlockTagProvider extends BlockTagsProvider {
    public ModBlockTagProvider(final DataGenerator generatorIn, @Nullable final ExistingFileHelper existingFileHelper) {
        super(generatorIn, PS1PackTweaks.MOD_ID, existingFileHelper);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void addTags() {
        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(Registration.ENDERITE_ORE.get());
        tag(ModTags.HEROBRINE_SPAWNS_BEHIND).addTags(Tags.Blocks.GLASS,Tags.Blocks.GLASS_PANES);
        tag(ModTags.CAN_TUNNEL_THROUGH).addTags(BlockTags.BASE_STONE_OVERWORLD,BlockTags.DIRT,Tags.Blocks.ORES);
    }
}
