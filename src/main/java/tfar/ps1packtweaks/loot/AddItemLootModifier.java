package tfar.ps1packtweaks.loot;

import com.google.gson.JsonObject;
import net.minecraft.core.BlockPos;
import net.minecraft.data.worldgen.StructureFeatures;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.saveddata.maps.MapDecoration;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.ExplorationMapFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;
import org.jetbrains.annotations.NotNull;
import tfar.ps1packtweaks.ModTags;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AddItemLootModifier extends LootModifier {

    /**
     * Constructs a LootModifier.
     *
     * @param conditionsIn the ILootConditions that need to be matched before the loot is modified.
     */
    public AddItemLootModifier(LootItemCondition[] conditionsIn) {
        super(conditionsIn);
    }

    @NotNull
    @Override
    protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
        Vec3 vec3 = context.getParamOrNull(LootContextParams.ORIGIN);
        if (vec3 != null) {
            ServerLevel serverlevel = context.getLevel();
            BlockPos blockpos = serverlevel.findNearestMapFeature(ModTags.END_CITY, new BlockPos(vec3), 128, true);
            if (blockpos != null) {
                ItemStack itemstack = MapItem.create(serverlevel, blockpos.getX(), blockpos.getZ(), (byte) 4, true, true);
                itemstack.setHoverName(new TextComponent("End City Map"));
                MapItem.renderBiomePreviewMap(serverlevel, itemstack);
                MapItemSavedData.addTargetDecoration(itemstack, blockpos, "+", MapDecoration.Type.RED_X);
                generatedLoot.add(itemstack);
            }
        }

        return generatedLoot;
    }

    public static class Serializer extends GlobalLootModifierSerializer<AddItemLootModifier> {
        public Serializer() {
        }

        public AddItemLootModifier read(ResourceLocation name, JsonObject object, LootItemCondition[] conditionsIn) {
            return new AddItemLootModifier(conditionsIn);
        }

        public JsonObject write(AddItemLootModifier instance) {
            return makeConditions(instance.conditions);
        }
    }
}
