package twilightforest.advancements;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.advancements.critereon.AbstractCriterionInstance;
import net.minecraft.util.ResourceLocation;
import twilightforest.TwilightForestMod;

public class HydraChopTrigger extends TriggerTFSimple<HydraChopTrigger.Instance> {

    public static final ResourceLocation ID = new ResourceLocation(TwilightForestMod.ID, "consume_hydra_chop_on_low_hunger");

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    @Override
    public HydraChopTrigger.Instance deserializeInstance(JsonObject json, JsonDeserializationContext context) {
        return new HydraChopTrigger.Instance();
    }

    public static class Instance extends AbstractCriterionInstance {

        public Instance() {
            super(HydraChopTrigger.ID);
        }
    }
}
