package tfar.ps1packtweaks.mixin;

import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(InventoryScreen.class)
public class InventoryScreenMixin {
    @ModifyVariable(method = "renderEntityInInventory", at = @At(value = "HEAD"), ordinal = 0, argsOnly = true)
    private static float fixMouseX(float original) {
        return 0;
    }

    @ModifyVariable(method = "renderEntityInInventory", at = @At(value = "HEAD"), ordinal = 1, argsOnly = true)
    private static float fixMouseY(float original) {
        return 0;
    }
}
