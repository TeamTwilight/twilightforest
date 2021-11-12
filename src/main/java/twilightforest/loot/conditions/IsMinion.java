package twilightforest.loot.conditions;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.world.level.storage.loot.Serializer;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.util.GsonHelper;
import twilightforest.entity.monster.CarminiteGhastling;
import twilightforest.loot.TFTreasure;

import javax.annotation.Nonnull;

public class IsMinion implements LootItemCondition {
	private final boolean inverse;

	public IsMinion(boolean inverse) {
		this.inverse = inverse;
	}

	@Override
	public LootItemConditionType getType() {
		return TFTreasure.IS_MINION;
	}

	@Override
	public boolean test(@Nonnull LootContext context) {
		return context.getParamOrNull(LootContextParams.THIS_ENTITY) instanceof CarminiteGhastling && ((CarminiteGhastling) context.getParamOrNull(LootContextParams.THIS_ENTITY)).isMinion() == !inverse;
	}

	public static Builder builder(boolean inverse) {
		return () -> new IsMinion(inverse);
	}

	public static class ConditionSerializer implements Serializer<IsMinion> {
		@Override
		public void serialize(JsonObject json, IsMinion value, JsonSerializationContext context) {
			json.addProperty("inverse", value.inverse);
		}

		@Override
		public IsMinion deserialize(JsonObject json, JsonDeserializationContext context) {
			return new IsMinion(GsonHelper.getAsBoolean(json, "inverse", false));
		}
	}
}
