package tfar.ps1packtweaks.advancement;

import com.google.gson.JsonObject;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.storage.loot.LootContext;
import tfar.ps1packtweaks.PS1PackTweaks;

public class PlayerFoundEntityTrigger extends SimpleCriterionTrigger<PlayerFoundEntityTrigger.TriggerInstance> {
    static final ResourceLocation ID = PS1PackTweaks.id("player_found_entity");


    public PlayerFoundEntityTrigger() {
    }

    public ResourceLocation getId() {
        return ID;
    }

    public TriggerInstance createInstance(JsonObject pJson, EntityPredicate.Composite pPredicate, DeserializationContext pDeserializationContext) {
        return new TriggerInstance(ID,pPredicate, EntityPredicate.Composite.fromJson(pJson, "entity",pDeserializationContext));
    }

    public void trigger(ServerPlayer pPlayer, LivingEntity entity) {
        LootContext lootcontext = EntityPredicate.createContext(pPlayer, entity);
        this.trigger(pPlayer, instance -> instance.matches(pPlayer,lootcontext));
    }

    public static class TriggerInstance extends AbstractCriterionTriggerInstance {

        private final EntityPredicate.Composite entity;


        public TriggerInstance(ResourceLocation pCriterion,EntityPredicate.Composite pPlayer, EntityPredicate.Composite entity) {
            super(pCriterion, pPlayer);
            this.entity = entity;
        }

        public boolean matches(ServerPlayer pPlayer, LootContext pContext) {
            boolean matches = this.entity.matches(pContext);
            return matches;
        }

        public static TriggerInstance located(EntityPredicate entityPredicate) {
            return new TriggerInstance(ID, EntityPredicate.Composite.ANY, EntityPredicate.Composite.wrap(entityPredicate));
        }
        public JsonObject serializeToJson(SerializationContext pConditions) {
            JsonObject jsonobject = super.serializeToJson(pConditions);
            jsonobject.add("entity", this.entity.toJson(pConditions));
            return jsonobject;
        }
    }
}
