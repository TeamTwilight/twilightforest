package twilightforest.client.model.block.giantblock;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.geometry.IGeometryLoader;

public class GiantBlockModelLoader implements IGeometryLoader<UnbakedGiantBlockModel> {

	public static final GiantBlockModelLoader INSTANCE = new GiantBlockModelLoader();

	@Override
	public UnbakedGiantBlockModel read(JsonObject object, JsonDeserializationContext deserializationContext) throws JsonParseException {
		ResourceLocation parent = ResourceLocation.tryParse(object.get("parent_block").getAsString());

		assert parent != null;
		return new UnbakedGiantBlockModel(parent);
	}
}
