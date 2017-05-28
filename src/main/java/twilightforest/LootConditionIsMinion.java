package twilightforest;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import twilightforest.entity.EntityTFMiniGhast;

import java.util.Random;

public class LootConditionIsMinion implements LootCondition {
	private final boolean inverse;

	public LootConditionIsMinion(boolean inverse) {
		this.inverse = inverse;
	}

	@Override
	public boolean testCondition(Random rand, LootContext context) {
		return context.getLootedEntity() instanceof EntityTFMiniGhast && ((EntityTFMiniGhast) context.getLootedEntity()).isMinion() == !inverse;
	}

	public static class Serializer extends LootCondition.Serializer<LootConditionIsMinion> {
		protected Serializer() {
			super(new ResourceLocation(TwilightForestMod.ID, "is_minion"), LootConditionIsMinion.class);
		}

		@Override
		public void serialize(JsonObject json, LootConditionIsMinion value, JsonSerializationContext context) {
			json.addProperty("inverse", value.inverse);
		}

		@Override
		public LootConditionIsMinion deserialize(JsonObject json, JsonDeserializationContext context) {
			return new LootConditionIsMinion(JsonUtils.getBoolean(json, "inverse", false));
		}
	}
}
