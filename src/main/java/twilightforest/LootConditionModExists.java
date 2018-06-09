package twilightforest;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraftforge.fml.common.Loader;

import javax.annotation.Nonnull;
import java.util.Random;

// Loot condition for checking if a mod exists.
public class LootConditionModExists implements LootCondition {
    private final boolean exists;
    private final String modID;

    public LootConditionModExists(String modID) {
        this.exists = Loader.isModLoaded(modID);
        this.modID = modID;
    }

    @Override
    public boolean testCondition(Random rand, LootContext context) {
        return exists;
    }

    public static class Serializer extends LootCondition.Serializer<LootConditionModExists> {
        protected Serializer() {
            super(new ResourceLocation(TwilightForestMod.ID, "mod_exists"), LootConditionModExists.class);
        }

        @Override
        public void serialize(@Nonnull JsonObject json, @Nonnull LootConditionModExists value, @Nonnull JsonSerializationContext context) {
            json.addProperty("mod_id", value.modID);
        }

        @Nonnull
        @Override
        public LootConditionModExists deserialize(@Nonnull JsonObject json, @Nonnull JsonDeserializationContext context) {
            return new LootConditionModExists(JsonUtils.getString(json, "mod_id"));
        }
    }
}
