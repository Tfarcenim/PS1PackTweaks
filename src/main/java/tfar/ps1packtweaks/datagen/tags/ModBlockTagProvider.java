package tfar.ps1packtweaks.datagen.tags;

import net.enderitemc.enderitemod.init.Registration;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import tfar.ps1packtweaks.Init;
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
        tag(BlockTags.MINEABLE_WITH_AXE).add(Init.ModBlocks.ENDERSHROOM_BLOCK);
        tag(ModTags.HEROBRINE_SPAWNS_BEHIND).addTags(Tags.Blocks.GLASS,Tags.Blocks.GLASS_PANES);
        tag(ModTags.CAN_TUNNEL_THROUGH).addTags(BlockTags.BASE_STONE_OVERWORLD,BlockTags.DIRT,Tags.Blocks.ORES);
        this.tag(BlockTags.SHULKER_BOXES).add(Blocks.SHULKER_BOX, Blocks.BLACK_SHULKER_BOX, Blocks.BLUE_SHULKER_BOX, Blocks.BROWN_SHULKER_BOX, Blocks.CYAN_SHULKER_BOX, Blocks.GRAY_SHULKER_BOX, Blocks.GREEN_SHULKER_BOX, Blocks.LIGHT_BLUE_SHULKER_BOX, Blocks.LIGHT_GRAY_SHULKER_BOX, Blocks.LIME_SHULKER_BOX, Blocks.MAGENTA_SHULKER_BOX, Blocks.ORANGE_SHULKER_BOX, Blocks.PINK_SHULKER_BOX, Blocks.PURPLE_SHULKER_BOX, Blocks.RED_SHULKER_BOX, Blocks.WHITE_SHULKER_BOX, Blocks.YELLOW_SHULKER_BOX);
    }
}
