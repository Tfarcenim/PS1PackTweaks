package tfar.ps1packtweaks.datagen;

import com.google.gson.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

import java.util.ArrayList;
import java.util.List;

public class OrLootTableCondition implements LootItemCondition {

    public static final LootItemConditionType OR_LOOT_TABLE_ID = new LootItemConditionType(new Serializer());

    private final List<ResourceLocation> targetLootTableIds;

    OrLootTableCondition(final List<ResourceLocation> targetLootTableIds) {
        this.targetLootTableIds = targetLootTableIds;
    }

    @Override
    public LootItemConditionType getType() {
        return OR_LOOT_TABLE_ID;
    }

    @Override
    public boolean test(LootContext lootContext) {
        return targetLootTableIds.contains(lootContext.getQueriedLootTableId());
    }

    public static class Serializer implements net.minecraft.world.level.storage.loot.Serializer<OrLootTableCondition> {
        @Override
        public void serialize(JsonObject object, OrLootTableCondition instance, JsonSerializationContext ctx) {
            JsonArray jsonArray = new JsonArray();
            for (ResourceLocation resourceLocation : instance.targetLootTableIds) {
                jsonArray.add(resourceLocation.toString());
            }
            object.add("loot_table_ids", jsonArray);
        }

        @Override
        public OrLootTableCondition deserialize(JsonObject object, JsonDeserializationContext ctx) {
            JsonArray jsonArray = object.getAsJsonArray("loot_table_ids");
            List<ResourceLocation> list = new ArrayList<>();
            for (JsonElement element : jsonArray) {
                list.add(new ResourceLocation(element.getAsString()));
            }
            return new OrLootTableCondition(list);
        }
    }
}
