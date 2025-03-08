package tfar.ps1packtweaks;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import tfar.ps1packtweaks.compat.Holders;

public class BlockItemSlot extends Slot {
    public BlockItemSlot(Container pContainer, int pSlot, int pX, int pY) {
        super(pContainer, pSlot, pX, pY);
    }

    @Override
    public boolean mayPlace(ItemStack pStack) {
        return !pStack.is(Holders.ENDERITE_SWORD) && super.mayPlace(pStack);
    }
}
