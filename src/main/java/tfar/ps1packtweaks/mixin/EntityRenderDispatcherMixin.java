package tfar.ps1packtweaks.mixin;

import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(EntityRenderDispatcher.class)
@Debug(export = true)
public class EntityRenderDispatcherMixin {
}
