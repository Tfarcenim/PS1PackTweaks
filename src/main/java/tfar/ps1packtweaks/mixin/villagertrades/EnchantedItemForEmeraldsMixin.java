package tfar.ps1packtweaks.mixin.villagertrades;

import net.minecraft.world.entity.npc.VillagerTrades;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(VillagerTrades.EnchantedItemForEmeralds.class)
public class EnchantedItemForEmeraldsMixin {
    @ModifyConstant(method = "getOffer",constant = @Constant(intValue = 64))
    private int limit32(int original) {
        return 32;
    }
}
