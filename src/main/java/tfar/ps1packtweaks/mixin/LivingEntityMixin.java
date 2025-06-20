package tfar.ps1packtweaks.mixin;

import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;

@Debug(export = true)
@Mixin(LivingEntity.class)
public class LivingEntityMixin {
}
