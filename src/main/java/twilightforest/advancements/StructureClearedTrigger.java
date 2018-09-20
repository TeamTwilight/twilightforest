package twilightforest.advancements;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.advancements.ICriterionTrigger;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.advancements.critereon.AbstractCriterionInstance;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import twilightforest.TwilightForestMod;

import java.util.ArrayList;
import java.util.List;

public class StructureClearedTrigger extends TriggerTF<StructureClearedTrigger.Instance> {

    public static final ResourceLocation ID = new ResourceLocation(TwilightForestMod.ID, "structure_cleared");

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    @Override
    public StructureClearedTrigger.Instance deserializeInstance(JsonObject json, JsonDeserializationContext context) {
        String structureName = JsonUtils.getString(json, "structure");
        return new StructureClearedTrigger.Instance(structureName);
    }

    public void trigger(EntityPlayerMP player, String structureName) {
        StructureClearedTrigger.Listeners listeners = (StructureClearedTrigger.Listeners) this.listeners.get(player.getAdvancements());
        if (listeners != null) {
            listeners.trigger(structureName);
        }
    }

    public static class Instance extends AbstractCriterionInstance {

        private final String structureName;

        public Instance(String structureName) {
            super(StructureClearedTrigger.ID);
            this.structureName = structureName;
        }

        boolean test(String structureName) {
            return this.structureName.equals(structureName);
        }
    }

    static class Listeners extends TriggerTF.Listeners<StructureClearedTrigger.Instance> {

        public Listeners(PlayerAdvancements playerAdvancementsIn) {
            super(playerAdvancementsIn);
        }

        public void trigger(String structureName) {

            List<Listener<StructureClearedTrigger.Instance>> list = new ArrayList<>();

            for (ICriterionTrigger.Listener<StructureClearedTrigger.Instance> listener : this.listeners) {
                if (listener.getCriterionInstance().test(structureName)) {
                    list.add(listener);
                }
            }

            for (ICriterionTrigger.Listener<StructureClearedTrigger.Instance> listener : list) {
                listener.grantCriterion(this.playerAdvancements);
            }
        }
    }
}
