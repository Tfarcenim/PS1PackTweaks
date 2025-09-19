package tfar.ps1packtweaks.worldgen;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.data.worldgen.Pools;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGeneratorSupplier;
import net.minecraft.world.level.levelgen.structure.pools.JigsawPlacement;

import java.util.Optional;
import java.util.Random;
import java.util.function.Predicate;

public class EndVillageFeature extends StructureFeature<JigsawConfiguration> {
   public EndVillageFeature(Codec<JigsawConfiguration> pCodec, int pStartY, boolean pDoExpansionHack, boolean pProjectStartToHeightmap,
                            Predicate<PieceGeneratorSupplier.Context<JigsawConfiguration>> pPredicate) {
        super(pCodec, (context) -> {
            if (!pPredicate.test(context)) {
                return Optional.empty();
            } else {
                BlockPos blockpos = new BlockPos(context.chunkPos().getMinBlockX(), pStartY, context.chunkPos().getMinBlockZ());
                Pools.bootstrap();
                return TweakedJigsawPlacement.addPieces(context, PoolElementStructurePiece::new, blockpos,
                        pDoExpansionHack, pProjectStartToHeightmap);
            }
        });
    }

    public EndVillageFeature(Codec<JigsawConfiguration> pCodec) {
        this(pCodec, 0, true, true, EndVillageFeature::test);
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