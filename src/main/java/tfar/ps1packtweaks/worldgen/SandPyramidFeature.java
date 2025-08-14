package tfar.ps1packtweaks.worldgen;

import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

public class SandPyramidFeature extends Feature<PyramidConfig> {
    public SandPyramidFeature(Codec<PyramidConfig> pCodec) {
        super(pCodec);
    }

    @Override
    public boolean place(FeaturePlaceContext<PyramidConfig> pContext) {
        return true;
    }
}
