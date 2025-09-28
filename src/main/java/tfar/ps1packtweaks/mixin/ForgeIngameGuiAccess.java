package tfar.ps1packtweaks.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.client.gui.IIngameOverlay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ForgeIngameGui.class)
public interface ForgeIngameGuiAccess {

    @Accessor @Mutable
    static void setEXPERIENCE_BAR_ELEMENT(IIngameOverlay overlay){
        throw new AssertionError();
    }

    @Invoker("renderExperience")
    void $renderExperience(int x, PoseStack poseStack);

    @Invoker("renderChat")
    void $renderChat(int width,int height, PoseStack poseStack);

}
