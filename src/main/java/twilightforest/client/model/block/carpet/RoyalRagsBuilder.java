package twilightforest.client.model.block.carpet;

import net.neoforged.neoforge.client.model.generators.CustomLoaderBuilder;
import net.neoforged.neoforge.client.model.generators.ModelBuilder;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import twilightforest.TwilightForestMod;

public class RoyalRagsBuilder<T extends ModelBuilder<T>> extends CustomLoaderBuilder<T> {

	protected RoyalRagsBuilder(T parent, ExistingFileHelper existingFileHelper) {
		super(TwilightForestMod.prefix("royal_rags"), parent, existingFileHelper, false);
	}

	public static <T extends ModelBuilder<T>> RoyalRagsBuilder<T> begin(T parent, ExistingFileHelper helper) {
		return new RoyalRagsBuilder<>(parent, helper);
	}
}