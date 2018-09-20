package twilightforest.advancements;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.advancements.critereon.AbstractCriterionInstance;
import net.minecraft.util.ResourceLocation;
import twilightforest.TwilightForestMod;

public class ActivateGhastTrapTrigger extends TriggerTFSimple<ActivateGhastTrapTrigger.Instance> {

    public static final ResourceLocation ID = new ResourceLocation(TwilightForestMod.ID, "activate_ghast_trap");

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    @Override
    public ActivateGhastTrapTrigger.Instance deserializeInstance(JsonObject json, JsonDeserializationContext context) {
        return new ActivateGhastTrapTrigger.Instance();
    }

    public static class Instance extends AbstractCriterionInstance {

        public Instance() {
            super(ActivateGhastTrapTrigger.ID);
        }
    }
}
