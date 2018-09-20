package twilightforest.advancements;

import com.google.common.collect.Lists;
import net.minecraft.advancements.ICriterionInstance;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.entity.player.EntityPlayerMP;

public abstract class TriggerTFSimple<T extends ICriterionInstance> extends TriggerTF<T> {

    public void trigger(EntityPlayerMP player) {
        Listeners<T> listeners = (Listeners<T>) this.listeners.get(player.getAdvancements());
        if (listeners != null) {
            listeners.trigger();
        }
    }

    static class Listeners<T extends ICriterionInstance> extends TriggerTF.Listeners<T> {

        public Listeners(PlayerAdvancements playerAdvancementsIn) {
            super(playerAdvancementsIn);
        }

        public void trigger() {
            for (Listener<T> listener : Lists.newArrayList(this.listeners)) {
                listener.grantCriterion(this.playerAdvancements);
            }
        }
    }
}
