package tfar.ps1packtweaks.mixin;

import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import tfar.ps1packtweaks.PS1PackTweaks;

import java.util.Set;

@Mixin(PoiType.class)
//@Debug(export = true)
public class PoiTypeMixin {

    @ModifyArg(method = "<clinit>",at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ai/village/poi/PoiType;register(Ljava/lang/String;Ljava/util/Set;II)Lnet/minecraft/world/entity/ai/village/poi/PoiType;",ordinal = 12))
    private static Set<BlockState> modifyBlockStates(Set<BlockState> original) {
        return PS1PackTweaks.ANVILS;
    }

}
