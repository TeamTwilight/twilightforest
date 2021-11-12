package twilightforest.advancements;

import com.google.gson.JsonObject;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import twilightforest.TwilightForestMod;

public class ArmorInventoryChangedTrigger extends SimpleCriterionTrigger<ArmorInventoryChangedTrigger.Instance> {

	public static final ResourceLocation ID = TwilightForestMod.prefix("armor_changed");

	@Override
	public ResourceLocation getId() {
		return ID;
	}

	@Override
	public Instance createInstance(JsonObject json, EntityPredicate.Composite player, DeserializationContext condition) {
		ItemPredicate from = ItemPredicate.fromJson(json.get("from"));
		ItemPredicate to = ItemPredicate.fromJson(json.get("to"));
		return new Instance(player, from, to);
	}

	public void trigger(ServerPlayer player, ItemStack from, ItemStack to) {
		this.trigger(player, (instance) -> instance.test(from, to));
	}


	public static class Instance extends AbstractCriterionTriggerInstance {

		private final ItemPredicate from;
		private final ItemPredicate to;

		public Instance(EntityPredicate.Composite player, ItemPredicate from, ItemPredicate to) {
			super(ArmorInventoryChangedTrigger.ID, player);
			this.from = from;
			this.to = to;
		}

		public static Instance equipArmor(ItemPredicate.Builder from, ItemPredicate.Builder to) {
			return new Instance(EntityPredicate.Composite.ANY, from.build(), to.build());
		}

		public boolean test(ItemStack from, ItemStack to) {
			return this.from.matches(from) && this.to.matches(to);
		}
	}
}
