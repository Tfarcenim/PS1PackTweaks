package tfar.ps1packtweaks.mixin.compat.guiclock;

import com.natamus.guiclock.forge.events.ForgeGUIEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(ForgeGUIEvent.class)
public class ForgeGuiEventMixin {
    /**
     * @author tfarcenim
     * @reason also poorly written code
     */
    @Overwrite(remap = false)
    public void renderOverlay(RenderGameOverlayEvent.Post e) {
        //STOP IT
    }
}
