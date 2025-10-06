package tfar.ps1packtweaks;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.ItemCombinerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.state.BlockState;

public class FletchingTableMenu extends ItemCombinerMenu {
    public FletchingTableMenu(int pContainerId, Inventory pPlayerInventory, ContainerLevelAccess pAccess) {
        super(Init.ModMenus.FLETCHING_TABLE, pContainerId, pPlayerInventory, pAccess);

    }

    public FletchingTableMenu(int pType, Inventory pContainerId) {
        this(pType, pContainerId,ContainerLevelAccess.NULL);
    }

    @Override
    protected boolean mayPickup(Player pPlayer, boolean pHasStack) {
        return true;
    }

    @Override
    protected void onTake(Player player, ItemStack stack) {
        stack.onCraftedBy(player.level, player, stack.getCount());
        this.resultSlots.awardUsedRecipes(player);
        inputSlots.setItem(0,ItemStack.EMPTY);
        inputSlots.setItem(1,Items.GLASS_BOTTLE.getDefaultInstance());
        this.access.execute((p_40263_, p_40264_) -> {
            p_40263_.levelEvent(LevelEvent.SOUND_SMITHING_TABLE_USED, p_40264_, 0);
        });
    }

    private void shrinkStackInSlot(int pIndex) {
        ItemStack itemstack = this.inputSlots.getItem(pIndex);
        itemstack.shrink(1);
        this.inputSlots.setItem(pIndex, itemstack);
    }

    @Override
    protected boolean isValidBlock(BlockState pState) {
        return pState.is(Blocks.FLETCHING_TABLE);
    }

    @Override
    public void createResult() {
        ItemStack stack = inputSlots.getItem(0);
        ItemStack potion = inputSlots.getItem(1);
        if (stack.is(Items.ARROW) && (potion.is(Items.POTION) || potion.is(Items.LINGERING_POTION))) {
            ItemStack itemstack1 = new ItemStack(Items.TIPPED_ARROW, stack.getCount());
            PotionUtils.setPotion(itemstack1, PotionUtils.getPotion(potion));
            PotionUtils.setCustomEffects(itemstack1, PotionUtils.getCustomEffects(potion));
            resultSlots.setItem(0,itemstack1);
        } else {
            resultSlots.setItem(0,ItemStack.EMPTY);
        }
    }
}
