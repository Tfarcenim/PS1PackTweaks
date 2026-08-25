package tfar.ps1packtweaks.mixin.compat;

import net.enderitemc.enderitemod.materials.EnderiteMaterial;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EnderiteMaterial.class)
public class EnderiteMaterialMixin {
    @Shadow @Mutable
    @Final
    private float miningSpeed;

    @Inject(method = "<init>",at = @At("RETURN"))
    private void onInit(CallbackInfo ci) {
        miningSpeed = 18;//required to instamine deepslate with Haste III 31
    }
}
