package tfar.ps1packtweaks.mixin;

import net.minecraft.client.renderer.entity.layers.SheepFurLayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(SheepFurLayer.class)
public class SheepFurLayerMixin {

	@Redirect(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/entity/animal/Sheep;FFFFFF)V"
	,at = @At(value = "INVOKE", target = "Ljava/lang/String;equals(Ljava/lang/Object;)Z"))
	private boolean alwaysFalse(String instance, Object o) {
		return false;
	}
}
