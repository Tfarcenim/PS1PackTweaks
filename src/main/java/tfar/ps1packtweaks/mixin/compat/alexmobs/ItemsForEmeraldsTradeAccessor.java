package tfar.ps1packtweaks.mixin.compat.alexmobs;

import com.github.alexthe666.alexsmobs.misc.ItemsForEmeraldsTrade;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ItemsForEmeraldsTrade.class)
public interface ItemsForEmeraldsTradeAccessor {
    @Accessor
    ItemStack getSellingItem();
    @Accessor
    int getEmeraldCount();
    @Accessor
    int getSellingItemCount();
    @Accessor
    int getMaxUses();
    @Accessor
    int getXpValue();
    @Accessor
    float getPriceMultiplier();
}
