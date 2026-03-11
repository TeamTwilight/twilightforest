package twilightforest.client.model.block.aurorablock;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.neoforged.neoforge.client.model.geometry.IGeometryLoader;

import java.util.ArrayList;
import java.util.List;

public class NoiseVaryingModelLoader implements IGeometryLoader<UnbakedNoiseVaryingModel> {
	public static final NoiseVaryingModelLoader INSTANCE = new NoiseVaryingModelLoader();

	private NoiseVaryingModelLoader() {
	}

	@Override
	public UnbakedNoiseVaryingModel read(JsonObject json, JsonDeserializationContext context) throws JsonParseException {
		List<String> builder = new ArrayList<>();

		for (JsonElement entry : json.getAsJsonArray("variants")) {
			builder.add(entry.getAsString());
		}

		return new UnbakedNoiseVaryingModel(builder.toArray(String[]::new));
	}
}
