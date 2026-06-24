package twilightforest.client.model.block.giantblock;

import net.neoforged.neoforge.client.model.generators.template.CustomLoaderBuilder;
import twilightforest.TwilightForestMod;

public class GiantBlockBuilder extends CustomLoaderBuilder {

	public static GiantBlockBuilder begin() {
		return new GiantBlockBuilder();
	}

	public GiantBlockBuilder() {
		super(TwilightForestMod.prefix("giant_block"), false);
	}

	@Override
	protected CustomLoaderBuilder copyInternal() {
		return new GiantBlockBuilder();
	}
}
