package twilightforest.client.model.block.carpet;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.neoforged.neoforge.client.model.geometry.IGeometryLoader;

public class RoyalRagsModelBuilder implements IGeometryLoader<UnbakedRoyalRagsModel> {
	@Deprecated // FIXME: Generalize alongside with CastleDoor models
	public static final RoyalRagsModelBuilder INSTANCE = new RoyalRagsModelBuilder();

	public RoyalRagsModelBuilder() {
	}

	public UnbakedRoyalRagsModel read(JsonObject jsonObject, JsonDeserializationContext deserializationContext) throws JsonParseException {
		return new UnbakedRoyalRagsModel();
	}
}
