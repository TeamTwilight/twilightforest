package twilightforest.client.model.block.aurorablock;

import com.google.common.base.Preconditions;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.generators.CustomLoaderBuilder;
import net.neoforged.neoforge.client.model.generators.ModelBuilder;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import twilightforest.TwilightForestMod;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NoiseVaryingModelBuilder<T extends ModelBuilder<T>> extends CustomLoaderBuilder<T> {
	private final List<T> variants = new ArrayList<>();

	public NoiseVaryingModelBuilder(T parent, ExistingFileHelper existingFileHelper) {
		super(TwilightForestMod.prefix("noise_varying"), parent, existingFileHelper, false);
	}

	public NoiseVaryingModelBuilder<T> add(T builder) {
		builder.assertExistence();

		this.variants.add(builder);

		return this;
	}

	public NoiseVaryingModelBuilder<T> addAll(T[] builders) {
		Arrays.stream(builders).forEach(this::add);

		return this;
	}

	@Override
	public T end() {
		Preconditions.checkArgument(!this.variants.isEmpty(), "Noise Varying builder cannot have zero variants.");

		return super.end();
	}

	@Override
	public JsonObject toJson(JsonObject json) {
		JsonObject mainJson = super.toJson(json);

		JsonArray variants = new JsonArray();
		this.variants.stream().map(ModelFile::getLocation).map(ResourceLocation::toString).forEach(variants::add);
		mainJson.add("variants", variants);

		return mainJson;
	}
}
