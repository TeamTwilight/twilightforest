package twilightforest.client.model.block.patch;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.util.GsonHelper;
import net.neoforged.neoforge.client.model.StandardModelParameters;
import net.neoforged.neoforge.client.model.UnbakedModelLoader;

public final class PatchModelLoader implements UnbakedModelLoader<UnbakedPatchModel> {
        public static final PatchModelLoader INSTANCE = new PatchModelLoader();

        private PatchModelLoader() {
        }

        @Override
        public UnbakedPatchModel read(JsonObject object, JsonDeserializationContext deserializationContext) throws JsonParseException {
                boolean shaggify = GsonHelper.getAsBoolean(object, "shaggify", false);
                return new UnbakedPatchModel(shaggify, StandardModelParameters.parse(object, deserializationContext));
        }
}
