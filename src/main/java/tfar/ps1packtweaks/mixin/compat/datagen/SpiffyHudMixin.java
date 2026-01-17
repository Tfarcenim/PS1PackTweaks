package tfar.ps1packtweaks.mixin.compat.datagen;

import de.keksuccino.spiffyhud.SpiffyHud;
import de.keksuccino.spiffyhud.customization.CustomizationHandler;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(SpiffyHud.class)
public class SpiffyHudMixin {
    @Redirect(method = "<init>",at = @At(value = "INVOKE", target = "Lde/keksuccino/spiffyhud/customization/CustomizationHandler;init()V"))
    private void skipInDatagen() {
        if (Minecraft.getInstance()  != null) {
            CustomizationHandler.init();
        }
    }
}
