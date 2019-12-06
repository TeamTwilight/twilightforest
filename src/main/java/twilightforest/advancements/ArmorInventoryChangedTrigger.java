package twilightforest.advancements;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.advancements.ICriterionTrigger;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.advancements.criterion.CriterionInstance;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import twilightforest.TwilightForestMod;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ArmorInventoryChangedTrigger implements ICriterionTrigger<ArmorInventoryChangedTrigger.Instance> {

	public static final ResourceLocation ID = TwilightForestMod.prefix("armor_changed");
	private final Map<PlayerAdvancements, Listeners> listeners = Maps.newHashMap();

	@Override
	public ResourceLocation getId() {
		return ID;
	}

	@Override
	public void addListener(PlayerAdvancements playerAdvancements, Listener<Instance> listener) {
		Listeners listeners = this.listeners.computeIfAbsent(playerAdvancements, Listeners::new);
		listeners.add(listener);
	}

	@Override
	public void removeListener(PlayerAdvancements playerAdvancements, Listener<Instance> listener) {
		Listeners listeners = this.listeners.get(playerAdvancements);
		if (listeners != null) {
			listeners.remove(listener);
			if (listeners.isEmpty()) {
				this.listeners.remove(playerAdvancements);
			}
		}
	}

	@Override
	public void removeAllListeners(PlayerAdvancements playerAdvancements) {
		this.listeners.remove(playerAdvancements);
	}

	@Override
	public Instance deserializeInstance(JsonObject json, JsonDeserializationContext context) {
		ItemPredicate from = ItemPredicate.deserialize(json.get("from"));
		ItemPredicate to = ItemPredicate.deserialize(json.get("to"));
		return new Instance(from, to);
	}

	public void trigger(ServerPlayerEntity player, ItemStack from, ItemStack to) {
		Listeners listeners = this.listeners.get(player.getAdvancements());
		if (listeners != null) {
			listeners.trigger(from, to);
		}
	}

	public static class Instance extends CriterionInstance {

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
		private final Set<Listener<Instance>> listeners = Sets.newHashSet();

		public Listeners(PlayerAdvancements playerAdvancementsIn) {
			this.playerAdvancements = playerAdvancementsIn;
		}

		public boolean isEmpty() {
			return this.listeners.isEmpty();
		}

		public void add(Listener<Instance> listener) {
			this.listeners.add(listener);
		}

		public void remove(Listener<Instance> listener) {
			this.listeners.remove(listener);
		}

		public void trigger(ItemStack from, ItemStack to) {

			List<Listener<Instance>> list = new ArrayList<>();

			for (Listener<Instance> listener : this.listeners) {
				if (listener.getCriterionInstance().test(from, to)) {
					list.add(listener);
				}
			}

			for (Listener<Instance> listener : list) {
				listener.grantCriterion(this.playerAdvancements);
			}
		}
	}
}
