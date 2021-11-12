package twilightforest.advancements;

import com.google.gson.JsonObject;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.loot.LootContext;
import twilightforest.TwilightForestMod;

public class HurtBossTrigger extends SimpleCriterionTrigger<HurtBossTrigger.Instance> {

	public static final ResourceLocation ID = TwilightForestMod.prefix("hurt_boss");

	@Override
	protected Instance createInstance(JsonObject json, EntityPredicate.Composite player, DeserializationContext ctx) {
		EntityPredicate.Composite composite = EntityPredicate.Composite.fromJson(json, "hurt_entity", ctx);
		return new Instance(player, composite);
	}

	@Override
	public ResourceLocation getId() {
		return ID;
	}

	public void trigger(ServerPlayer player, Entity hurt) {
		LootContext entity = EntityPredicate.createContext(player, hurt);
		this.trigger(player, (instance) -> instance.matches(entity));
	}

	public static class Instance extends AbstractCriterionTriggerInstance {
		private final EntityPredicate.Composite hurt;

		public Instance(EntityPredicate.Composite player, EntityPredicate.Composite hurt) {
			super(ID, player);
			this.hurt = hurt;
		}

		public boolean matches(LootContext hurt) {
			return this.hurt.matches(hurt);
		}

		public static Instance hurtBoss(EntityPredicate.Builder hurt) {
			return new Instance(EntityPredicate.Composite.ANY, EntityPredicate.Composite.wrap(hurt.build()));
		}

		@Override
		public JsonObject serializeToJson(SerializationContext ctx) {
			JsonObject json = super.serializeToJson(ctx);
			json.add("hurt_entity", this.hurt.toJson(ctx));
			return json;
		}
	}
}
