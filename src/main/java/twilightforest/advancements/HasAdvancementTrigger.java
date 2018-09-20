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

public class HasAdvancementTrigger extends TriggerTF<HasAdvancementTrigger.Instance> {

    private static final ResourceLocation ID = new ResourceLocation(TwilightForestMod.ID, "has_advancement");

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    @Override
    public Instance deserializeInstance(JsonObject json, JsonDeserializationContext context) {
        ResourceLocation advancementId = new ResourceLocation(JsonUtils.getString(json, "advancement"));
        return new HasAdvancementTrigger.Instance(advancementId);
    }

    public void trigger(EntityPlayerMP player) {
        Listeners l = (Listeners) listeners.get(player.getAdvancements());
        if (l != null) {
            l.trigger(player);
        }
    }

    static class Instance extends AbstractCriterionInstance {

        private final ResourceLocation advancementLocation;

        Instance(ResourceLocation advancementLocation) {
            super(HasAdvancementTrigger.ID);
            this.advancementLocation = advancementLocation;
        }

        boolean test(EntityPlayerMP player) {
            return TwilightForestMod.proxy.doesPlayerHaveAdvancement(player, advancementLocation);
        }
    }

    private static class Listeners extends TriggerTF.Listeners<HasAdvancementTrigger.Instance> {

        Listeners(PlayerAdvancements playerAdvancements) {
            super(playerAdvancements);
        }

        public void trigger(EntityPlayerMP player) {
            List<Listener<HasAdvancementTrigger.Instance>> list = new ArrayList<>();

            for (ICriterionTrigger.Listener<HasAdvancementTrigger.Instance> listener : this.listeners) {
                if (listener.getCriterionInstance().test(player)) {
                    list.add(listener);
                }
            }

            for (ICriterionTrigger.Listener<HasAdvancementTrigger.Instance> listener : list) {
                listener.grantCriterion(this.playerAdvancements);
            }
        }
    }
}
