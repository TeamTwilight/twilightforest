package twilightforest.advancements;

import com.google.gson.JsonObject;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import twilightforest.TwilightForestMod;

public class ActivateGhastTrapTrigger extends SimpleCriterionTrigger<ActivateGhastTrapTrigger.Instance> {

    public static final ResourceLocation ID = TwilightForestMod.prefix("activate_ghast_trap");

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    @Override
    protected ActivateGhastTrapTrigger.Instance createInstance(JsonObject json, EntityPredicate.Composite player, DeserializationContext ctx) {
        return new ActivateGhastTrapTrigger.Instance(player);
    }

    public void trigger(ServerPlayer player) {
        this.trigger(player, (instance) -> true);
    }


    public static class Instance extends AbstractCriterionTriggerInstance {
        public Instance(EntityPredicate.Composite player) {
            super(ActivateGhastTrapTrigger.ID, player);
        }

        public static Instance activateTrap() {
            return new Instance(EntityPredicate.Composite.ANY);
        }
    }
}
