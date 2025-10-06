package tfar.ps1packtweaks.mixin;

import net.minecraftforge.client.gui.OverlayRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(OverlayRegistry.class)
public interface OverlayRegistryAccess {
    @Accessor
    static List<OverlayRegistry.OverlayEntry> getOverlaysOrdered() {
        throw new AssertionError();
    }
}
