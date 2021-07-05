package twilightforest.loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.loot.ILootSerializer;
import net.minecraft.loot.LootConditionType;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.util.JSONUtils;
import net.minecraftforge.fml.ModList;

// Loot condition for checking if a mod exists.
public class LootConditionModExists implements ILootCondition {

    private final boolean exists;
    private final String modID;

    public LootConditionModExists(String modID) {
        this.exists = ModList.get().isLoaded(modID);
        this.modID = modID;
    }

    @Override
    public LootConditionType getConditionType() {
        return TFTreasure.MOD_EXISTS;
    }

    @Override
    public boolean test(LootContext context) {
        return exists;
    }

    public static ILootCondition.IBuilder builder(String modid) {
        return () -> new LootConditionModExists(modid);
    }

    public static class Serializer implements ILootSerializer<LootConditionModExists> {

		@Override
		public void serialize(JsonObject json, LootConditionModExists value, JsonSerializationContext context) {
			json.addProperty("mod_id", value.modID);
		}

		@Override
		public LootConditionModExists deserialize(JsonObject json, JsonDeserializationContext context) {
			return new LootConditionModExists(JSONUtils.getString(json, "mod_id"));
		}
	}
}
