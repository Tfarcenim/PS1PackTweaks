package tfar.ps1packtweaks.mixin.compat;

import com.natamus.skeletonhorsespawn_common_forge.events.SkeletonHorseEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.animal.horse.SkeletonHorse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(SkeletonHorseEvent.class)
public class SkeletonHorseEventMixin {
    @Redirect(method = "onWorldTick",at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;isInWaterRainOrBubble()Z"))
    private static boolean blockBurning(Entity instance) {
        if (!(((SkeletonHorse)instance).getItemBySlot(EquipmentSlot.CHEST).isEmpty())) {
            return true;
        }
        return instance.isInWaterRainOrBubble();
    }
}
