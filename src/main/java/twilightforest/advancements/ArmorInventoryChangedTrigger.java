package twilightforest.advancements;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.advancements.ICriterionTrigger;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.advancements.critereon.AbstractCriterionInstance;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import twilightforest.TwilightForestMod;

import java.util.Map;
import java.util.Set;

public class ArmorInventoryChangedTrigger implements ICriterionTrigger<ArmorInventoryChangedTrigger.Instance> {

    public static final ResourceLocation ID = new ResourceLocation(TwilightForestMod.ID, "armor_changed");
    private final Map<PlayerAdvancements, ArmorInventoryChangedTrigger.Listeners> listeners = Maps.newHashMap();

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    @Override
    public void addListener(PlayerAdvancements playerAdvancementsIn, Listener<ArmorInventoryChangedTrigger.Instance> listener) {
        ArmorInventoryChangedTrigger.Listeners listeners = this.listeners.computeIfAbsent(playerAdvancementsIn, Listeners::new);
        listeners.add(listener);
    }

    @Override
    public void removeListener(PlayerAdvancements playerAdvancementsIn, Listener<ArmorInventoryChangedTrigger.Instance> listener) {
        ArmorInventoryChangedTrigger.Listeners listeners = this.listeners.get(playerAdvancementsIn);
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
    public Instance deserializeInstance(JsonObject json, JsonDeserializationContext context) {
        ItemPredicate from = ItemPredicate.deserialize(json.get("from"));
        ItemPredicate to = ItemPredicate.deserialize(json.get("to"));
        return new ArmorInventoryChangedTrigger.Instance(from, to);
    }

    public void trigger(EntityPlayerMP player, ItemStack from, ItemStack to) {
        ArmorInventoryChangedTrigger.Listeners listeners = this.listeners.get(player.getAdvancements());

        if (listeners != null)
            listeners.trigger(from, to);
    }

    public class Instance extends AbstractCriterionInstance {
        private final ItemPredicate from;
        private final ItemPredicate to;

        public Instance(ItemPredicate from, ItemPredicate to) {
            super(ArmorInventoryChangedTrigger.ID);
            this.from = from;
            this.to = to;
        }

        public boolean test(ItemStack from, ItemStack to) {
            return this.from.test(from) && this.to.test(to);
        }

        @Override
        public ResourceLocation getId() {
            return ArmorInventoryChangedTrigger.ID;
        }
    }

    static class Listeners {
        private final PlayerAdvancements playerAdvancements;
        private final Set<Listener<ArmorInventoryChangedTrigger.Instance>> listeners = Sets.newHashSet();

        public Listeners(PlayerAdvancements playerAdvancementsIn) {
            this.playerAdvancements = playerAdvancementsIn;
        }

        public boolean isEmpty() {
            return this.listeners.isEmpty();
        }

        public void add(Listener<ArmorInventoryChangedTrigger.Instance> listener) {
            this.listeners.add(listener);
        }

        public void remove(Listener<ArmorInventoryChangedTrigger.Instance> listener) {
            this.listeners.remove(listener);
        }

        public void trigger(ItemStack from, ItemStack to) {
            for (ICriterionTrigger.Listener<ArmorInventoryChangedTrigger.Instance> listener : this.listeners)
                if (listener.getCriterionInstance().test(from, to))
                    listener.grantCriterion(this.playerAdvancements);
        }
    }
}
