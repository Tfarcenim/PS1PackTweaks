package tfar.ps1packtweaks.mixin.compat;

import com.natamus.zombiehorsespawn_common_forge.events.ZombieHorseEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.animal.horse.ZombieHorse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ZombieHorseEvent.class)
public class ZombieHorseEventMixin {
    @Redirect(method = "onWorldTick",at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;isInWaterRainOrBubble()Z"))
    private static boolean blockBurning(Entity instance) {
        if (!(((ZombieHorse)instance).getItemBySlot(EquipmentSlot.CHEST).isEmpty())) {
            return true;
        }
        return instance.isInWaterRainOrBubble();
    }
}
