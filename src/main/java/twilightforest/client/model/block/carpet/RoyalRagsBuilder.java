package twilightforest.client.model.block.carpet;

import com.google.gson.JsonObject;
import net.neoforged.neoforge.client.model.generators.template.CustomLoaderBuilder;
import twilightforest.TwilightForestMod;

public class RoyalRagsBuilder extends CustomLoaderBuilder {

	protected RoyalRagsBuilder() {
		super(TwilightForestMod.prefix("royal_rags"), false);
	}

	public static RoyalRagsBuilder begin() {
		return new RoyalRagsBuilder();
	}

	@Override
	public CustomLoaderBuilder copyInternal() {
		return new RoyalRagsBuilder();
	}
}
