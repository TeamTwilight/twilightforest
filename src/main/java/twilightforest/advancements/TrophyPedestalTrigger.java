package twilightforest.advancements;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.advancements.critereon.AbstractCriterionInstance;
import net.minecraft.util.ResourceLocation;
import twilightforest.TwilightForestMod;

public class TrophyPedestalTrigger extends TriggerTFSimple<TrophyPedestalTrigger.Instance> {

    public static final ResourceLocation ID = new ResourceLocation(TwilightForestMod.ID, "placed_on_trophy_pedestal");

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    @Override
    public TrophyPedestalTrigger.Instance deserializeInstance(JsonObject json, JsonDeserializationContext context) {
        return new TrophyPedestalTrigger.Instance();
    }

    public static class Instance extends AbstractCriterionInstance {

        public Instance() {
            super(TrophyPedestalTrigger.ID);
        }
    }
}
