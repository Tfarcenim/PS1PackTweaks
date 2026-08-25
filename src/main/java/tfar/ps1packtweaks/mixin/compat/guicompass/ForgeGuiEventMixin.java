package tfar.ps1packtweaks.mixin.compat.guicompass;

import com.natamus.guicompass.forge.events.ForgeGUIEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(ForgeGUIEvent.class)
public class ForgeGuiEventMixin {
    /**
     * @author tfarcenim
     * @reason poorly written code
     */
    @Overwrite(remap = false)
    public void renderOverlay(RenderGameOverlayEvent.Pre e) {
        //NO
    }
}
