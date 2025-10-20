package tfar.ps1packtweaks.mixin.compat;

import com.natamus.giantspawn.ModForge;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tfar.ps1packtweaks.client.WorldLocker;

@Mixin(ModForge.class)
public class MainMixin {
    @Inject(method = "loadComplete",at = @At("HEAD"),cancellable = true,remap = false)
    private void preventSpawn(FMLLoadCompleteEvent event, CallbackInfo ci) {
        if (WorldLocker.isPure()) {
            ci.cancel();
        }
    }
}
