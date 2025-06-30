package tfar.ps1packtweaks.mixin;

import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(PoiType.class)
public interface PoiAccess {
    @Accessor
    static Map<BlockState,PoiType> getTYPE_BY_STATE() {
        throw new AssertionError();
    }
}
