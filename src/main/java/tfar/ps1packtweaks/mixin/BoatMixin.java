package tfar.ps1packtweaks.mixin;

import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tfar.ps1packtweaks.Init;
import tfar.ps1packtweaks.block.CustomBoatTypes;

@Mixin(Boat.class)
public abstract class BoatMixin {
    @Shadow public abstract Boat.Type getBoatType();

    @Inject(method = "getDropItem",at = @At("HEAD"),cancellable = true)
    private void fixDrops(CallbackInfoReturnable<Item> cir) {
        Boat.Type type = this.getBoatType();
        if (type == CustomBoatTypes.EBONY) {
            cir.setReturnValue(Init.ModItems.EBONY_BOAT);
        } else if (type == CustomBoatTypes.ENDERVIOLET) {
            cir.setReturnValue(Init.ModItems.ENDERVIOLET_BOAT);
        }
    }
}
