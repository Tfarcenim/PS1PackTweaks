package tfar.ps1packtweaks.mixin;

import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(LivingEntityRenderer.class)
public class LivingEntityRenderMixin {
    /**
     * @author
     * @reason
     */
    @Overwrite
    public static boolean isEntityUpsideDown(LivingEntity entity) {
        return false;
    }
}
