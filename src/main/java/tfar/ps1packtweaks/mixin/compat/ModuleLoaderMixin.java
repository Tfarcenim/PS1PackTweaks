package tfar.ps1packtweaks.mixin.compat;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vazkii.quark.base.module.ModuleLoader;
import vazkii.quark.base.module.QuarkModule;
import vazkii.quark.content.building.module.MorePottedPlantsModule;

import java.util.Map;

@Mixin(ModuleLoader.class)
public class ModuleLoaderMixin {
    @Shadow private Map<Class<? extends QuarkModule>, QuarkModule> foundModules;

    @Inject(method = "findModules",at = @At("RETURN"),remap = false)
    private void removeModules(CallbackInfo ci) {
        this.foundModules.entrySet().removeIf(classQuarkModuleEntry -> classQuarkModuleEntry.getKey() == MorePottedPlantsModule.class);
    }
}
