package tfar.ps1packtweaks.mixin.villagertrades;

import net.minecraft.world.entity.npc.VillagerTrades;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(VillagerTrades.EnchantBookForEmeralds.class)
@Debug(export = true)
public class EnchantBookForEmeraldsMixin {

    @ModifyArg(method = "getOffer",at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;<init>(Lnet/minecraft/world/level/ItemLike;I)V",ordinal = 0))

    private int limitTo32(int original) {
        return Math.min(32,original);
    }
}
