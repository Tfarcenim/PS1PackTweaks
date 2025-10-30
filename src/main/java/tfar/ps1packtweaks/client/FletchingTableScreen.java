package tfar.ps1packtweaks.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.inventory.ItemCombinerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import tfar.ps1packtweaks.FletchingTableMenu;
import tfar.ps1packtweaks.PS1PackTweaks;

public class FletchingTableScreen extends ItemCombinerScreen<FletchingTableMenu>{
    public FletchingTableScreen(FletchingTableMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle, PS1PackTweaks.id("textures/gui/fletching_table.png"));
    }

    @Override
    protected void renderLabels(PoseStack pPoseStack, int pMouseX, int pMouseY) {
        int x = titleLabelX - font.width(title)/2+80;

        this.font.draw(pPoseStack, this.title, x, (float)this.titleLabelY, 4210752);
        this.font.draw(pPoseStack, this.playerInventoryTitle, (float)this.inventoryLabelX, (float)this.inventoryLabelY, 4210752);
    }
}
