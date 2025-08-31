package tfar.ps1packtweaks;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DuplicateOutputsLootModifier extends LootModifier {

    /**
     * Constructs a LootModifier.
     *
     * @param conditionsIn the ILootConditions that need to be matched before the loot is modified.
     */
    public DuplicateOutputsLootModifier(LootItemCondition[] conditionsIn) {
        super(conditionsIn);
    }

    final Random random = new Random();
    final double chance = .05;

    @NotNull
    @Override
    protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
        Entity entity = context.getParamOrNull(LootContextParams.THIS_ENTITY);
        if (entity instanceof LivingEntity living) {
            MobEffectInstance mobEffectInstance = living.getEffect(MobEffects.LUCK);
            if (mobEffectInstance != null) {
                double chanceDouble = chance * (mobEffectInstance.getAmplifier()+1);
                if (random.nextDouble() < chanceDouble) {
                    List<ItemStack> copy = new ArrayList<>(generatedLoot);
                    generatedLoot.addAll(copy);
                }
            }
        }
        return generatedLoot;
    }

    public static class Serializer extends GlobalLootModifierSerializer<DuplicateOutputsLootModifier> {
        public Serializer() {
        }

        public DuplicateOutputsLootModifier read(ResourceLocation name, JsonObject object, LootItemCondition[] conditionsIn) {
            return new DuplicateOutputsLootModifier(conditionsIn);
        }

        public JsonObject write(DuplicateOutputsLootModifier instance) {
            return makeConditions(instance.conditions);
        }
    }
}
