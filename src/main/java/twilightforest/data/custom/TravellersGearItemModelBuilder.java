package twilightforest.data.custom;

import com.google.gson.JsonObject;
import net.neoforged.neoforge.client.model.generators.CustomLoaderBuilder;
import net.neoforged.neoforge.client.model.generators.ModelBuilder;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import twilightforest.TwilightForestMod;

public class TravellersGearItemModelBuilder<T extends ModelBuilder<T>> extends CustomLoaderBuilder<T> {

	private final String directory;
	private final String brokenDirectory;

	public static <T extends ModelBuilder<T>> TravellersGearItemModelBuilder<T> begin(T parent, String directory, String brokenDirectory, ExistingFileHelper existingFileHelper) {
		return new TravellersGearItemModelBuilder<>(parent, directory, brokenDirectory, existingFileHelper);
	}

	protected TravellersGearItemModelBuilder(T parent, String directory, String brokenDirectory, ExistingFileHelper existingFileHelper) {
		super(TwilightForestMod.prefix("travellers_gear"), parent, existingFileHelper, false);
		this.directory = directory;
		this.brokenDirectory = brokenDirectory;
	}

	@Override
	public JsonObject toJson(JsonObject json) {
		json = super.toJson(json);
		json.addProperty("modifier_directory", this.directory);
		json.addProperty("broken_modifier_directory", this.brokenDirectory);
		return json;
	}
}
