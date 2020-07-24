package twilightforest.advancements;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonObject;
import net.minecraft.advancements.ICriterionTrigger;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.advancements.criterion.CriterionInstance;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.loot.ConditionArrayParser;
import net.minecraft.util.ResourceLocation;
import twilightforest.TwilightForestMod;

import java.util.Map;
import java.util.Set;

public class QuestRamCompletionTrigger implements ICriterionTrigger<QuestRamCompletionTrigger.Instance> {

    public static final ResourceLocation ID = TwilightForestMod.prefix("complete_quest_ram");
    private final Map<PlayerAdvancements, QuestRamCompletionTrigger.Listeners> listeners = Maps.newHashMap();

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    @Override
    public void addListener(PlayerAdvancements playerAdvancementsIn, Listener<QuestRamCompletionTrigger.Instance> listener) {
        QuestRamCompletionTrigger.Listeners listeners = this.listeners.computeIfAbsent(playerAdvancementsIn, Listeners::new);
        listeners.add(listener);
    }

    @Override
    public void removeListener(PlayerAdvancements playerAdvancementsIn, Listener<QuestRamCompletionTrigger.Instance> listener) {
        QuestRamCompletionTrigger.Listeners listeners = this.listeners.get(playerAdvancementsIn);
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
    public Instance func_230307_a_(JsonObject json, ConditionArrayParser condition) {
		EntityPredicate.AndPredicate player = EntityPredicate.AndPredicate.deserializeJSONObject(json, "player", condition);
		return new QuestRamCompletionTrigger.Instance(player);
    }

    public void trigger(ServerPlayerEntity player) {
        QuestRamCompletionTrigger.Listeners listeners = this.listeners.get(player.getAdvancements());
        if (listeners != null) {
            listeners.trigger();
        }
    }

    public static class Instance extends CriterionInstance {
        public Instance(EntityPredicate.AndPredicate player) {
            super(QuestRamCompletionTrigger.ID, player);
        }
    }

    static class Listeners {

        private final PlayerAdvancements playerAdvancements;
        private final Set<Listener<QuestRamCompletionTrigger.Instance>> listeners = Sets.newHashSet();

        public Listeners(PlayerAdvancements playerAdvancementsIn) {
            this.playerAdvancements = playerAdvancementsIn;
        }

        public boolean isEmpty() {
            return this.listeners.isEmpty();
        }

        public void add(Listener<QuestRamCompletionTrigger.Instance> listener) {
            this.listeners.add(listener);
        }

        public void remove(Listener<QuestRamCompletionTrigger.Instance> listener) {
            this.listeners.remove(listener);
        }

        public void trigger() {
            for (Listener<QuestRamCompletionTrigger.Instance> listener : Lists.newArrayList(this.listeners)) {
                listener.grantCriterion(this.playerAdvancements);
            }
        }
    }
}
