package twilightforest.advancements;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.advancements.critereon.AbstractCriterionInstance;
import net.minecraft.util.ResourceLocation;
import twilightforest.TwilightForestMod;

public class MakePortalTrigger extends TriggerTFSimple<MakePortalTrigger.Instance> {

    public static final ResourceLocation ID = new ResourceLocation(TwilightForestMod.ID, "make_tf_portal");

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    @Override
    public MakePortalTrigger.Instance deserializeInstance(JsonObject json, JsonDeserializationContext context) {
        return new MakePortalTrigger.Instance();
    }

    public static class Instance extends AbstractCriterionInstance {

        public Instance() {
            super(MakePortalTrigger.ID);
        }
    }
}
