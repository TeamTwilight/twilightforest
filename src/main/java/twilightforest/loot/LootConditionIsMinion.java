package twilightforest.loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.loot.ILootSerializer;
import net.minecraft.loot.LootConditionType;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.util.JSONUtils;
import twilightforest.entity.EntityTFMiniGhast;

import javax.annotation.Nonnull;

public class LootConditionIsMinion implements ILootCondition {

	private final boolean inverse;

	public LootConditionIsMinion(boolean inverse) {
		this.inverse = inverse;
	}

	@Override
	public LootConditionType func_230419_b_() {
		return TFTreasure.IS_MINION;
	}

	@Override
	public boolean test(@Nonnull LootContext context) {
		return context.get(LootParameters.THIS_ENTITY) instanceof EntityTFMiniGhast && ((EntityTFMiniGhast) context.get(LootParameters.THIS_ENTITY)).isMinion() == !inverse;
	}

	public static IBuilder builder(boolean inverse) {
		return () -> new LootConditionIsMinion(inverse);
	}

	public static class Serializer implements ILootSerializer<LootConditionIsMinion> {

		@Override
		public void func_230424_a_(JsonObject json, LootConditionIsMinion value, JsonSerializationContext context) {
			json.addProperty("inverse", value.inverse);
		}

		@Override
		public LootConditionIsMinion func_230423_a_(JsonObject json, JsonDeserializationContext context) {
			return new LootConditionIsMinion(JSONUtils.getBoolean(json, "inverse", false));
		}
	}
}
