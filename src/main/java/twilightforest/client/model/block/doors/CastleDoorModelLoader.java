package twilightforest.client.model.block.doors;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.neoforged.neoforge.client.model.geometry.IGeometryLoader;

public class CastleDoorModelLoader implements IGeometryLoader<UnbakedCastleDoorModel> {
	public static final CastleDoorModelLoader INSTANCE = new CastleDoorModelLoader();

	public CastleDoorModelLoader() {
	}

	public UnbakedCastleDoorModel read(JsonObject jsonObject, JsonDeserializationContext deserializationContext) throws JsonParseException {
		return new UnbakedCastleDoorModel();
	}
}
