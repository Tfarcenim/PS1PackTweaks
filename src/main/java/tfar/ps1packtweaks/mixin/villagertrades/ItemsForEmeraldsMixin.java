package tfar.ps1packtweaks.mixin.villagertrades;

import net.minecraft.world.entity.npc.VillagerTrades;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(VillagerTrades.ItemsForEmeralds.class)
//@Debug(export = true)
public class ItemsForEmeraldsMixin {

    @Mutable
    @Shadow @Final private int emeraldCost;

    @Inject(method = "<init>(Lnet/minecraft/world/item/ItemStack;IIIIF)V",at = @At(value = "RETURN"))
    private void limitTo32(CallbackInfo info) {
       this.emeraldCost = Math.min(this.emeraldCost,32);
    }
}
