package tfar.ps1packtweaks.advancement;

import com.google.gson.JsonObject;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.storage.loot.LootContext;
import tfar.ps1packtweaks.PS1PackTweaks;

public class PetKilledTrigger extends SimpleCriterionTrigger<PetKilledTrigger.TriggerInstance> {

    static final ResourceLocation ID = PS1PackTweaks.id("pet_killed");

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    public void trigger(ServerPlayer pPlayer, Mob pEntity) {
        LootContext lootcontext = EntityPredicate.createContext(pPlayer, pEntity);
        this.trigger(pPlayer, instance -> instance.matches(lootcontext));
    }

    @Override
    protected TriggerInstance createInstance(JsonObject pJson, EntityPredicate.Composite pPlayer, DeserializationContext pContext) {
        EntityPredicate.Composite composite = EntityPredicate.Composite.fromJson(pJson, "entity", pContext);
        return new TriggerInstance(pPlayer, composite);
    }

    public static class TriggerInstance extends AbstractCriterionTriggerInstance {
        private final EntityPredicate.Composite entity;

        public TriggerInstance(EntityPredicate.Composite pPlayer,EntityPredicate.Composite entity) {
            super(ID, pPlayer);
            this.entity = entity;
        }

        public boolean matches(LootContext pLootContext) {
            return this.entity.matches(pLootContext);
        }


        public static TriggerInstance killedPet() {
            return new TriggerInstance(EntityPredicate.Composite.ANY, EntityPredicate.Composite.ANY);
        }

        public static TriggerInstance killedPet(EntityPredicate pEntityPredicate) {
            return new TriggerInstance(EntityPredicate.Composite.ANY, EntityPredicate.Composite.wrap(pEntityPredicate));
        }

        @Override
        public JsonObject serializeToJson(SerializationContext pConditions) {
            JsonObject jsonobject = super.serializeToJson(pConditions);
            jsonobject.add("entity", this.entity.toJson(pConditions));
            return jsonobject;
        }
    }
}
