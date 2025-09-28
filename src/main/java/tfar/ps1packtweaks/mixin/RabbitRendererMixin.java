package tfar.ps1packtweaks.mixin;

import net.minecraft.client.renderer.entity.RabbitRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(RabbitRenderer.class)
public class RabbitRendererMixin {
    @Redirect(method = "getTextureLocation(Lnet/minecraft/world/entity/animal/Rabbit;)Lnet/minecraft/resources/ResourceLocation;"
    ,at = @At(value = "INVOKE", target = "Ljava/lang/String;equals(Ljava/lang/Object;)Z"))
    private boolean never(String instance, Object object) {
        return false;
    }
}
