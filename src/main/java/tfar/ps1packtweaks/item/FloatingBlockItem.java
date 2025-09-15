package tfar.ps1packtweaks.item;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.BlockHitResult;

public class FloatingBlockItem extends BlockItem {
    public FloatingBlockItem(Block pBlock, Properties pProperties) {
        super(pBlock, pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack stack = pPlayer.getItemInHand(pUsedHand);
        BlockHitResult hitResult = (BlockHitResult) pPlayer.pick(pPlayer.getReachDistance(),0,false);
        BlockPos pos = hitResult.getBlockPos();
        BlockPlaceContext blockPlaceContext = new BlockPlaceContext(pLevel, pPlayer, pUsedHand,stack,hitResult);
        InteractionResult interactionresult = this.place(new BlockPlaceContext(blockPlaceContext));

        return new InteractionResultHolder<>(interactionresult,stack);
    }
}
