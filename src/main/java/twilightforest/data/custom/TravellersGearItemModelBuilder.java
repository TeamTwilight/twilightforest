package twilightforest.data.custom;

import net.neoforged.neoforge.client.model.generators.CustomLoaderBuilder;
import net.neoforged.neoforge.client.model.generators.ModelBuilder;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import twilightforest.TwilightForestMod;

public class TravellersGearItemModelBuilder<T extends ModelBuilder<T>> extends CustomLoaderBuilder<T> {

	public static <T extends ModelBuilder<T>> TravellersGearItemModelBuilder<T> begin(T parent, ExistingFileHelper existingFileHelper) {
		return new TravellersGearItemModelBuilder<>(parent, existingFileHelper);
	}

	protected TravellersGearItemModelBuilder(T parent, ExistingFileHelper existingFileHelper) {
		super(TwilightForestMod.prefix("travellers_gear"), parent, existingFileHelper, false);
	}
}
