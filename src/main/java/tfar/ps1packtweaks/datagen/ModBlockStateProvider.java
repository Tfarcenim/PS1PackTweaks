package tfar.ps1packtweaks.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.HugeMushroomBlock;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ModelFile;
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

        ModelFile modelFile = models().singleTexture("endershroom_block",mcLoc("block/template_single_face"),modLoc("block/endershroom_block"));
        ModelFile mushroom = models().getExistingFile(mcLoc("block/mushroom_block_inside"));
        getMultipartBuilder(Init.ModBlocks.ENDERSHROOM_BLOCK)
                .part().modelFile(modelFile).addModel().condition(HugeMushroomBlock.NORTH,true).end()
                .part().modelFile(modelFile).rotationY(90).uvLock(true).addModel().condition(HugeMushroomBlock.EAST,true).end()
                .part().modelFile(modelFile).rotationY(180).uvLock(true).addModel().condition(HugeMushroomBlock.SOUTH,true).end()
                .part().modelFile(modelFile).rotationY(270).uvLock(true).addModel().condition(HugeMushroomBlock.WEST,true).end()
                .part().modelFile(modelFile).rotationX(270).uvLock(true).addModel().condition(HugeMushroomBlock.UP,true).end()
                .part().modelFile(modelFile).rotationX(90).uvLock(true).addModel().condition(HugeMushroomBlock.DOWN,true).end()

                .part().modelFile(mushroom).addModel().condition(HugeMushroomBlock.NORTH,false).end()
                .part().modelFile(mushroom).rotationY(90).uvLock(false).addModel().condition(HugeMushroomBlock.EAST,false).end()
                .part().modelFile(mushroom).rotationY(180).uvLock(false).addModel().condition(HugeMushroomBlock.SOUTH,false).end()
                .part().modelFile(mushroom).rotationY(270).uvLock(false).addModel().condition(HugeMushroomBlock.WEST,false).end()
                .part().modelFile(mushroom).rotationX(270).uvLock(false).addModel().condition(HugeMushroomBlock.UP,false).end()
                .part().modelFile(mushroom).rotationX(90).uvLock(false).addModel().condition(HugeMushroomBlock.DOWN,false).end()
        ;

        simpleBlockItem(Init.ModBlocks.ENDERSHROOM_BLOCK,models().cubeAll("endershroom_block_inventory",modLoc("block/endershroom_block")));
        simpleBlock(Init.ModBlocks.ENDERSHROOM,models().cross("endershroom",modLoc("block/endershroom")));
        itemModels().singleTexture("endershroom",mcLoc("item/generated"),
                "layer0",modLoc("block/endershroom"));
    }
}
