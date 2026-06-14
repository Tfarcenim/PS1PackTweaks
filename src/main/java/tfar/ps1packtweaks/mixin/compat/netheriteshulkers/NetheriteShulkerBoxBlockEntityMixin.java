package tfar.ps1packtweaks.mixin.compat.netheriteshulkers;

import be.ephys.netherite_shulkers.NetheriteShulkerBoxBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.stream.IntStream;

@Mixin(NetheriteShulkerBoxBlockEntity.class)
public class NetheriteShulkerBoxBlockEntityMixin {

    @Shadow private NonNullList<ItemStack> itemStacks;

    @Mutable
    @Shadow
    @Final
    private static int[] SLOTS;

    @Inject(method = "<init>",at = @At("RETURN"))
    private void forceSize(BlockPos pos, BlockState blockState, CallbackInfo ci) {
        this.itemStacks = NonNullList.withSize(27, ItemStack.EMPTY);
    }

    @Inject(method = "<clinit>",at = @At("RETURN"))
    private static void onInitialize(CallbackInfo ci) {
        SLOTS = IntStream.range(0, 27).toArray();
    }


    @Overwrite
    protected AbstractContainerMenu createMenu(int menuId, Inventory playerInventory) {
        return ChestMenu.threeRows(menuId, playerInventory, (Container) this);
    }
}
