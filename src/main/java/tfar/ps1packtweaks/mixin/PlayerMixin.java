package tfar.ps1packtweaks.mixin;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tfar.ps1packtweaks.PS1PackTweaks;

@Mixin(Player.class)
public class PlayerMixin {
    @Inject(method = "awardStat(Lnet/minecraft/resources/ResourceLocation;I)V",at = @At("RETURN"))
    private void onStatAwarded(ResourceLocation pStat, int pIncrement, CallbackInfo ci) {
        PS1PackTweaks.onStatAwarded((Player)(Object)this,pStat,pIncrement);
    }
}
