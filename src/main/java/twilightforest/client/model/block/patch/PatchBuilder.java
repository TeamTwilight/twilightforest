package twilightforest.client.model.block.patch;

import com.google.gson.JsonObject;
import net.neoforged.neoforge.client.model.generators.template.CustomLoaderBuilder;
import twilightforest.TwilightForestMod;

public class PatchBuilder extends CustomLoaderBuilder {

	private boolean shaggify = false;

	public PatchBuilder() {
		super(TwilightForestMod.prefix("patch"), false);
	}

	public PatchBuilder shaggify() {
		this.shaggify = true;
		return this;
	}

	@Override
	protected CustomLoaderBuilder copyInternal() {
		PatchBuilder builder = new PatchBuilder();
		builder.shaggify = this.shaggify;
		return builder;
	}

	@Override
	public JsonObject toJson(JsonObject json) {
		JsonObject mainJson = super.toJson(json);

		mainJson.addProperty("shaggify", this.shaggify);

		return mainJson;
	}
}
