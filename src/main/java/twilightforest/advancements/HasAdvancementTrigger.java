package twilightforest.advancements;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.ICriterionTrigger;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.advancements.criterion.CriterionInstance;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import twilightforest.TwilightForestMod;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class HasAdvancementTrigger implements ICriterionTrigger<HasAdvancementTrigger.Instance> {

	private static final ResourceLocation ID = TwilightForestMod.prefix("has_advancement");
	private final Map<PlayerAdvancements, HasAdvancementTrigger.Listeners> listeners = Maps.newHashMap();

	@Override
	public ResourceLocation getId() {
		return ID;
	}

	@Override
	public void addListener(PlayerAdvancements playerAdvancementsIn, Listener<Instance> listener) {
		HasAdvancementTrigger.Listeners listeners = this.listeners.computeIfAbsent(playerAdvancementsIn, Listeners::new);
		listeners.add(listener);
	}

	@Override
	public void removeListener(PlayerAdvancements playerAdvancementsIn, ICriterionTrigger.Listener<HasAdvancementTrigger.Instance> listener) {
		HasAdvancementTrigger.Listeners listeners = this.listeners.get(playerAdvancementsIn);
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
		ResourceLocation advancementId = new ResourceLocation(JSONUtils.getString(json, "advancement"));
		return new HasAdvancementTrigger.Instance(advancementId);
	}

	public void trigger(ServerPlayerEntity player, Advancement advancement) {
		Listeners listeners = this.listeners.get(player.getAdvancements());
		if (listeners != null) {
			listeners.trigger(advancement);
		}
	}

	static class Instance extends CriterionInstance {

		private final ResourceLocation advancementLocation;

		Instance(ResourceLocation advancementLocation) {
			super(HasAdvancementTrigger.ID);
			this.advancementLocation = advancementLocation;
		}

		boolean test(Advancement advancement) {
			return advancementLocation.equals(advancement.getId());
		}
	}

	private static class Listeners {

		private final PlayerAdvancements playerAdvancements;
		private final Set<Listener<HasAdvancementTrigger.Instance>> listeners = Sets.newHashSet();

		Listeners(PlayerAdvancements playerAdvancements) {
			this.playerAdvancements = playerAdvancements;
		}

		public boolean isEmpty() {
			return this.listeners.isEmpty();
		}

		public void add(ICriterionTrigger.Listener<HasAdvancementTrigger.Instance> listener) {
			this.listeners.add(listener);
		}

		public void remove(ICriterionTrigger.Listener<HasAdvancementTrigger.Instance> listener) {
			this.listeners.remove(listener);
		}

		public void trigger(Advancement advancement) {
			List<Listener<HasAdvancementTrigger.Instance>> list = new ArrayList<>();

			for (ICriterionTrigger.Listener<HasAdvancementTrigger.Instance> listener : this.listeners) {
				if (listener.getCriterionInstance().test(advancement)) {
					list.add(listener);
				}
			}

			for (ICriterionTrigger.Listener<HasAdvancementTrigger.Instance> listener : list) {
				listener.grantCriterion(this.playerAdvancements);
			}
		}
	}
}
