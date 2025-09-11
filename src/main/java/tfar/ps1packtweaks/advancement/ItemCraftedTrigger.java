package tfar.ps1packtweaks.advancement;

import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonObject;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.level.ItemLike;
import tfar.ps1packtweaks.PS1PackTweaks;

public class ItemCraftedTrigger extends SimpleCriterionTrigger<ItemCraftedTrigger.TriggerInstance> {
    static final ResourceLocation ID = PS1PackTweaks.id("craft_item");


    public ItemCraftedTrigger() {
    }

    public ResourceLocation getId() {
        return ID;
    }

    public TriggerInstance createInstance(JsonObject pJson, EntityPredicate.Composite pPredicate, DeserializationContext pDeserializationContext) {
        return new TriggerInstance(pPredicate, ItemPredicate.fromJson(pJson));
    }

    public void trigger(ServerPlayer pPlayer, ItemStack pItem) {
        this.trigger(pPlayer, instance -> instance.matches(pItem));
    }

    public static class TriggerInstance extends AbstractCriterionTriggerInstance {

        private final ItemPredicate item;


        public TriggerInstance(EntityPredicate.Composite pPlayer, ItemPredicate item) {
            super(ID, pPlayer);
            this.item = item;
        }

        public static TriggerInstance crafted() {
            return new TriggerInstance(EntityPredicate.Composite.ANY, ItemPredicate.ANY);
        }

        public static TriggerInstance crafted(ItemPredicate pItem) {
            return new TriggerInstance(EntityPredicate.Composite.ANY, pItem);
        }

        public static TriggerInstance crafted(ItemLike pItem) {
            return new TriggerInstance(EntityPredicate.Composite.ANY, new ItemPredicate(null, ImmutableSet.of(pItem.asItem()), MinMaxBounds.Ints.ANY, MinMaxBounds.Ints.ANY, EnchantmentPredicate.NONE, EnchantmentPredicate.NONE, null, NbtPredicate.ANY));
        }

        public boolean matches(ItemStack pItem) {
            return this.item.matches(pItem);
        }

        public JsonObject serializeToJson(SerializationContext pConditions) {
            JsonObject jsonobject = super.serializeToJson(pConditions);
            jsonobject.add("item", this.item.serializeToJson());
            return jsonobject;
        }
    }
}
