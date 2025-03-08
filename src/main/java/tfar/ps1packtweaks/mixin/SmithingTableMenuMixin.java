package tfar.ps1packtweaks.mixin;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.*;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tfar.ps1packtweaks.BlockItemSlot;

@Mixin(SmithingMenu.class)
public abstract class SmithingTableMenuMixin extends ItemCombinerMenu{


    public SmithingTableMenuMixin(@Nullable MenuType<?> pType, int pContainerId, Inventory pPlayerInventory, ContainerLevelAccess pAccess) {
        super(pType, pContainerId, pPlayerInventory, pAccess);
    }

    @Inject(method = "<init>(ILnet/minecraft/world/entity/player/Inventory;Lnet/minecraft/world/inventory/ContainerLevelAccess;)V",at = @At("RETURN"))
    private void blockSlots(int pContainerId, Inventory pPlayerInventory, ContainerLevelAccess pAccess, CallbackInfo ci) {
        pS1PackTweaks$replaceSlot(new BlockItemSlot(this.inputSlots, 0, 27, 47),0);
        pS1PackTweaks$replaceSlot(new BlockItemSlot(this.inputSlots, 1, 76, 47),1);
    }

    @Unique
    private void pS1PackTweaks$replaceSlot(Slot slot, int slotID) {
        slots.set(slotID,slot);
        slot.index = slotID;
    }
}
