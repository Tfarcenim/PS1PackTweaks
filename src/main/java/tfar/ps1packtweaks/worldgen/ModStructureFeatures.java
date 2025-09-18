package tfar.ps1packtweaks.worldgen;

import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;

public class ModStructureFeatures {

    public static final StructureFeature<JigsawConfiguration> END_VILLAGE = new EndVillageFeature(JigsawConfiguration.CODEC);

    static {
        StructureFeature.STEP.put(END_VILLAGE, GenerationStep.Decoration.SURFACE_STRUCTURES);
    }

}
