package twilightforest.advancements;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonObject;
import net.minecraft.advancements.ICriterionTrigger;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.advancements.criterion.CriterionInstance;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.loot.ConditionArrayParser;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import twilightforest.TwilightForestMod;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class StructureClearedTrigger implements ICriterionTrigger<StructureClearedTrigger.Instance> {

	public static final ResourceLocation ID = TwilightForestMod.prefix("structure_cleared");
	private final Map<PlayerAdvancements, StructureClearedTrigger.Listeners> listeners = Maps.newHashMap();

	@Override
	public ResourceLocation getId() {
		return ID;
	}

	@Override
	public void addListener(PlayerAdvancements playerAdvancementsIn, Listener<StructureClearedTrigger.Instance> listener) {
		StructureClearedTrigger.Listeners listeners = this.listeners.computeIfAbsent(playerAdvancementsIn, Listeners::new);
		listeners.add(listener);
	}

	@Override
	public void removeListener(PlayerAdvancements playerAdvancementsIn, Listener<StructureClearedTrigger.Instance> listener) {
		StructureClearedTrigger.Listeners listeners = this.listeners.get(playerAdvancementsIn);
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
		String structureName = JSONUtils.getString(json, "structure");
		return new StructureClearedTrigger.Instance(player, structureName);
	}

	public void trigger(ServerPlayerEntity player, String structureName) {
		StructureClearedTrigger.Listeners listeners = this.listeners.get(player.getAdvancements());
		if (listeners != null) {
			listeners.trigger(structureName);
		}
	}

	public static class Instance extends CriterionInstance {

		private final String structureName;

		public Instance(EntityPredicate.AndPredicate player, String structureName) {
			super(StructureClearedTrigger.ID, player);
			this.structureName = structureName;
		}

		boolean test(String structureName) {
			return this.structureName.equals(structureName);
		}
	}

	static class Listeners {

		private final PlayerAdvancements playerAdvancements;
		private final Set<Listener<StructureClearedTrigger.Instance>> listeners = Sets.newHashSet();

		public Listeners(PlayerAdvancements playerAdvancementsIn) {
			this.playerAdvancements = playerAdvancementsIn;
		}

		public boolean isEmpty() {
			return this.listeners.isEmpty();
		}

		public void add(Listener<StructureClearedTrigger.Instance> listener) {
			this.listeners.add(listener);
		}

		public void remove(Listener<StructureClearedTrigger.Instance> listener) {
			this.listeners.remove(listener);
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
