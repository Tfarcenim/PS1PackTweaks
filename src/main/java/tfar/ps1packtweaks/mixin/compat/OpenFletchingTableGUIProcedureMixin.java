package tfar.ps1packtweaks.mixin.compat;

import net.hangel.fletchingtablemod.procedures.OpenFletchingTableGUIProcedure;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(OpenFletchingTableGUIProcedure.class)
public class OpenFletchingTableGUIProcedureMixin {
    /**
     * @author Tfar
     * @reason replacing gui
     */
    @Overwrite(remap = false)
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {}
}
