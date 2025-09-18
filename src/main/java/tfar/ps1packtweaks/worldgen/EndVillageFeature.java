package tfar.ps1packtweaks.worldgen;

import com.mojang.serialization.Codec;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.JigsawFeature;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGeneratorSupplier;

import java.util.Random;

public class EndVillageFeature extends JigsawFeature {
    public EndVillageFeature(Codec<JigsawConfiguration> pCodec) {
        super(pCodec, 0, true, true, EndVillageFeature::test);
    }

    private static boolean test(PieceGeneratorSupplier.Context<JigsawConfiguration> jigsawConfigurationContext) {
        int i = getYPositionForFeature(jigsawConfigurationContext.chunkPos(), jigsawConfigurationContext.chunkGenerator(), jigsawConfigurationContext.heightAccessor());
        if (i < 60) {
            return false;
        } else {
            return true;
        }
    }

    private static int getYPositionForFeature(ChunkPos pChunkPos, ChunkGenerator pChunkGenerator, LevelHeightAccessor pLevel) {
        Random random = new Random(pChunkPos.x + pChunkPos.z * 10387313L);
        Rotation rotation = Rotation.getRandom(random);
        int i = 5;
        int j = 5;
        if (rotation == Rotation.CLOCKWISE_90) {
            i = -5;
        } else if (rotation == Rotation.CLOCKWISE_180) {
            i = -5;
            j = -5;
        } else if (rotation == Rotation.COUNTERCLOCKWISE_90) {
            j = -5;
        }

        int k = pChunkPos.getBlockX(7);
        int l = pChunkPos.getBlockZ(7);
        int i1 = pChunkGenerator.getFirstOccupiedHeight(k, l, Heightmap.Types.WORLD_SURFACE_WG, pLevel);
        int j1 = pChunkGenerator.getFirstOccupiedHeight(k, l + j, Heightmap.Types.WORLD_SURFACE_WG, pLevel);
        int k1 = pChunkGenerator.getFirstOccupiedHeight(k + i, l, Heightmap.Types.WORLD_SURFACE_WG, pLevel);
        int l1 = pChunkGenerator.getFirstOccupiedHeight(k + i, l + j, Heightmap.Types.WORLD_SURFACE_WG, pLevel);
        return Math.min(Math.min(i1, j1), Math.min(k1, l1));
    }

}