package tfar.ps1packtweaks.mixin;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ApplyBonusCount.class)
public class ApplyBonusCountMixin {
    @Shadow @Final
    Enchantment enchantment;

    private final ThreadLocal<LootContext> threadLocal = ThreadLocal.withInitial(() -> null);

    @Inject(method = "run",at = @At("HEAD"))
    private void captureContext(ItemStack pStack, LootContext pContext, CallbackInfoReturnable<ItemStack> cir) {
        threadLocal.set(pContext);
    }

    @ModifyArg(method = "run",at = @At(
            value = "INVOKE", target = "Lnet/minecraft/world/level/storage/loot/functions/ApplyBonusCount$Formula;calculateNewCount(Ljava/util/Random;II)I"),
            index = 2)
    private int change(int original) {
        if (enchantment == Enchantments.BLOCK_FORTUNE) {
            LootContext lootContext = threadLocal.get();

            Entity entity = lootContext.getParamOrNull(LootContextParams.THIS_ENTITY);
            if (entity instanceof LivingEntity living) {
                MobEffectInstance instance = living.getEffect(MobEffects.LUCK);
                if (instance != null) {
                    original+=instance.getAmplifier()+1;
                }
            }
        }
        return original;
    }
}
