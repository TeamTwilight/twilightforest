package twilightforest.client.model.block.giantblock;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.NeoForgeModelProperties;
import net.neoforged.neoforge.client.model.StandardModelParameters;
import net.neoforged.neoforge.client.model.UnbakedModelLoader;

public class GiantBlockModelLoader implements UnbakedModelLoader<UnbakedGiantBlockModel> {

	public static final GiantBlockModelLoader INSTANCE = new GiantBlockModelLoader();

	@Override
	public UnbakedGiantBlockModel read(JsonObject object, JsonDeserializationContext deserializationContext) throws JsonParseException {
		return new UnbakedGiantBlockModel(StandardModelParameters.parse(object, deserializationContext));
	}
}
