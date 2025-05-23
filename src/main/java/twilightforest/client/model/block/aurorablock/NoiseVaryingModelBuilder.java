package twilightforest.client.model.block.aurorablock;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.generators.template.CustomLoaderBuilder;
import twilightforest.TwilightForestMod;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NoiseVaryingModelBuilder extends CustomLoaderBuilder {
	private final List<ResourceLocation> variants = new ArrayList<>();

	public NoiseVaryingModelBuilder() {
		super(TwilightForestMod.prefix("noise_varying"), false);
	}

	public NoiseVaryingModelBuilder add(ResourceLocation builder) {
		this.variants.add(builder);
		return this;
	}

	public NoiseVaryingModelBuilder addAll(ResourceLocation[] builders) {
		Arrays.stream(builders).forEach(this::add);

		return this;
	}

	@Override
	protected NoiseVaryingModelBuilder copyInternal() {
		NoiseVaryingModelBuilder builder = new NoiseVaryingModelBuilder();
		builder.variants.addAll(this.variants);
		return builder;
	}

	@Override
	public JsonObject toJson(JsonObject json) {
		JsonObject mainJson = super.toJson(json);

		JsonArray variants = new JsonArray();
		this.variants.forEach(resourceLocation -> variants.add(resourceLocation.toString()));
		mainJson.add("variants", variants);

		return mainJson;
	}
}
