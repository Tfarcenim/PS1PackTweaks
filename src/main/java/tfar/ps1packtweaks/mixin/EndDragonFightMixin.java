package tfar.ps1packtweaks.mixin;

import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.level.dimension.end.EndDragonFight;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tfar.ps1packtweaks.PS1PackTweaks;

@Mixin(EndDragonFight.class)
public class EndDragonFightMixin {
    @Inject(method = "setDragonKilled",at = @At("HEAD"))
    private void onDragonKilled(EnderDragon pDragon, CallbackInfo ci) {
        PS1PackTweaks.onDragonKilled(pDragon);
    }
}
