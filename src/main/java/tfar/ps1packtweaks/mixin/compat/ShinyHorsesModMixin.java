package tfar.ps1packtweaks.mixin.compat;

import com.kuraion.shinyhorses.ShinyHorsesMod;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.animal.horse.SkeletonHorse;
import net.minecraft.world.entity.animal.horse.ZombieHorse;
import net.minecraft.world.item.enchantment.Enchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tfar.ps1packtweaks.compat.ShinyHorsesCompat;

@Mixin(ShinyHorsesMod.class)
public class ShinyHorsesModMixin {
    @Inject(method = "checkHorseHook",at = @At("HEAD"),cancellable = true,remap = false)
    private static void checkUndeadHook(Enchantment enchantmentIn, LivingEntity entityIn, CallbackInfoReturnable<Integer> cir, CallbackInfo ci) {
        if (entityIn instanceof AbstractHorse abstractHorse && (abstractHorse instanceof ZombieHorse || abstractHorse instanceof SkeletonHorse)) {
            ShinyHorsesCompat.checkHorseHook(enchantmentIn, entityIn, cir);
        }
    }
}
