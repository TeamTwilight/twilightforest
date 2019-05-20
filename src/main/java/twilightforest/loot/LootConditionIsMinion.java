package twilightforest.loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.util.JsonUtils;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import twilightforest.TwilightForestMod;
import twilightforest.entity.EntityTFMiniGhast;

import javax.annotation.Nonnull;
import java.util.Random;

public class LootConditionIsMinion implements LootCondition {

	private final boolean inverse;

	public LootConditionIsMinion(boolean inverse) {
		this.inverse = inverse;
	}

	@Override
	public boolean testCondition(@Nonnull Random rand, @Nonnull LootContext context) {
		return context.getLootedEntity() instanceof EntityTFMiniGhast && ((EntityTFMiniGhast) context.getLootedEntity()).isMinion() == !inverse;
	}

	public static class Serializer extends LootCondition.Serializer<LootConditionIsMinion> {

		protected Serializer() {
			super(TwilightForestMod.prefix("is_minion"), LootConditionIsMinion.class);
		}

		@Override
		public void serialize(@Nonnull JsonObject json, @Nonnull LootConditionIsMinion value, @Nonnull JsonSerializationContext context) {
			json.addProperty("inverse", value.inverse);
		}

		@Nonnull
		@Override
		public LootConditionIsMinion deserialize(@Nonnull JsonObject json, @Nonnull JsonDeserializationContext context) {
			return new LootConditionIsMinion(JsonUtils.getBoolean(json, "inverse", false));
		}
	}
}
