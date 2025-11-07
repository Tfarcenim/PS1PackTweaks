package tfar.ps1packtweaks.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.DeathScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(DeathScreen.class)
public class DeathScreenMixin {
    @Redirect(method = "render",at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/gui/screens/DeathScreen;drawCenteredString(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/gui/Font;Lnet/minecraft/network/chat/Component;III)V",
            ordinal = 1))
    private void noDeathString(PoseStack poseStack, Font font, Component component, int i1, int i2, int i) {

    }
}
