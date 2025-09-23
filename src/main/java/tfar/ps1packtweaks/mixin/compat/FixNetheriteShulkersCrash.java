package tfar.ps1packtweaks.mixin.compat;

import be.ephys.netherite_shulkers.NetheriteShulkers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.function.Supplier;

@Mixin(NetheriteShulkers.class)
public class FixNetheriteShulkersCrash {

    @Redirect(method = "<init>",at = @At(value = "INVOKE", target = "Lnet/minecraftforge/fml/DistExecutor;safeRunWhenOn(Lnet/minecraftforge/api/distmarker/Dist;Ljava/util/function/Supplier;)V"))
    private void no(Dist dist, Supplier<DistExecutor.SafeRunnable> toRun) {

    }
}
