package tfar.ps1packtweaks.mixin.compat;

import net.enderitemc.enderitemod.item.EnderiteSword;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.List;

@Mixin(EnderiteSword.class)
public class EnderiteSwordMixin {


    @Overwrite
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack itemstack = pPlayer.getItemInHand(pUsedHand);
        if (itemstack.isEdible()) {
            if (pPlayer.canEat(itemstack.getFoodProperties(pPlayer).canAlwaysEat())) {
                pPlayer.startUsingItem(pUsedHand);
                return InteractionResultHolder.consume(itemstack);
            } else {
                return InteractionResultHolder.fail(itemstack);
            }
        } else {
            return InteractionResultHolder.pass(pPlayer.getItemInHand(pUsedHand));
        }
    }

    @Overwrite
    public void appendHoverText(ItemStack itemStack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
    }
}
