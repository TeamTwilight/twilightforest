package twilightforest.advancements;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.advancements.ICriterionInstance;
import net.minecraft.advancements.ICriterionTrigger;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.util.ResourceLocation;

import java.util.Map;
import java.util.Set;

public abstract class TriggerTF<T extends ICriterionInstance> implements ICriterionTrigger<T> {

    final Map<PlayerAdvancements, Listeners<T>> listeners = Maps.newHashMap();

    @Override
    public abstract ResourceLocation getId();

    @Override
    public void addListener(PlayerAdvancements playerAdvancementsIn, Listener<T> listener) {
        TriggerTF.Listeners<T> listeners = this.listeners.computeIfAbsent(playerAdvancementsIn, Listeners::new);
        listeners.add(listener);
    }

    @Override
    public void removeListener(PlayerAdvancements playerAdvancementsIn, Listener<T> listener) {
        TriggerTF.Listeners<T> listeners = this.listeners.get(playerAdvancementsIn);
        if (listeners != null) {
            listeners.remove(listener);
            if (listeners.isEmpty()) {
                this.listeners.remove(playerAdvancementsIn);
            }
        }
    }

    @Override
    public void removeAllListeners(PlayerAdvancements playerAdvancementsIn) {
        this.listeners.remove(playerAdvancementsIn);
    }

    @Override
    public abstract T deserializeInstance(JsonObject json, JsonDeserializationContext context);

    static class Listeners<T extends ICriterionInstance> {

        final PlayerAdvancements playerAdvancements;
        final Set<Listener<T>> listeners = Sets.newHashSet();

        public Listeners(PlayerAdvancements playerAdvancementsIn) {
            this.playerAdvancements = playerAdvancementsIn;
        }

        public boolean isEmpty() {
            return this.listeners.isEmpty();
        }

        public void add(Listener<T> listener) {
            this.listeners.add(listener);
        }

        public void remove(Listener<T> listener) {
            this.listeners.remove(listener);
        }
    }
}
