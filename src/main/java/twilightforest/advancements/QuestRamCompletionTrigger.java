package twilightforest.advancements;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.advancements.critereon.AbstractCriterionInstance;
import net.minecraft.util.ResourceLocation;
import twilightforest.TwilightForestMod;

public class QuestRamCompletionTrigger extends TriggerTFSimple<QuestRamCompletionTrigger.Instance> {

    public static final ResourceLocation ID = new ResourceLocation(TwilightForestMod.ID, "complete_quest_ram");

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    @Override
    public QuestRamCompletionTrigger.Instance deserializeInstance(JsonObject json, JsonDeserializationContext context) {
        return new QuestRamCompletionTrigger.Instance();
    }

    public static class Instance extends AbstractCriterionInstance {

        public Instance() {
            super(QuestRamCompletionTrigger.ID);
        }
    }
}
