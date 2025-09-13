package tfar.ps1packtweaks.mixin;

import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tfar.ps1packtweaks.PS1PackTweaks;

import java.util.function.Predicate;

@Mixin(ConfiguredStructureFeature.class)
public class StructureMixin<C extends FeatureConfiguration> {
    @Inject(method = "generate",at = @At("RETURN"),cancellable = true)
    private void noVoidStructures(RegistryAccess pRegistryAcess, ChunkGenerator pChunkGenerator, BiomeSource pBiomeSource,
                                  StructureManager pStructureManager, long pSeed, ChunkPos pChunkPos, int pReferences,
                                  LevelHeightAccessor pLevel, Predicate<Holder<Biome>> pBiomePredicate,
                                  CallbackInfoReturnable<StructureStart> cir) {
        //PS1PackTweaks.preventStructures((ConfiguredStructureFeature<?,?>)(Object)this,pRegistryAcess,pChunkPos,pLevel,cir);
    }
}
