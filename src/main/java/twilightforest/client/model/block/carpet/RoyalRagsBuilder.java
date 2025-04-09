package twilightforest.client.model.block.carpet;

import net.neoforged.neoforge.client.model.generators.template.CustomLoaderBuilder;
import twilightforest.TwilightForestMod;

public class RoyalRagsBuilder extends CustomLoaderBuilder {

	public RoyalRagsBuilder() {
		super(TwilightForestMod.prefix("royal_rags"), false);
	}

	public static RoyalRagsBuilder begin() {
		return new RoyalRagsBuilder();
	}

	@Override
	protected CustomLoaderBuilder copyInternal() {
		return new RoyalRagsBuilder();
	}
}