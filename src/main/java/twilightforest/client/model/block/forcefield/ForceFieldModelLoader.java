package twilightforest.client.model.block.forcefield;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.client.resources.model.cuboid.CuboidModelElement;
import net.minecraft.util.GsonHelper;
import net.neoforged.neoforge.client.model.NeoForgeModelProperties;
import net.neoforged.neoforge.client.model.StandardModelParameters;
import net.neoforged.neoforge.client.model.UnbakedModelLoader;
import org.jetbrains.annotations.Nullable;
import twilightforest.client.model.block.forcefield.ForceFieldModel.ExtraDirection;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

public class ForceFieldModelLoader implements UnbakedModelLoader<UnbakedForceFieldModel> {
	public static final ForceFieldModelLoader INSTANCE = new ForceFieldModelLoader();

	@Override
	@SuppressWarnings("ConstantConditions")
	public UnbakedForceFieldModel read(JsonObject json, JsonDeserializationContext context) throws JsonParseException {

		Map<CuboidModelElement, Condition> elementsAndConditions = new IdentityHashMap<>();

		if (json.has("elements")) {
			for (JsonElement jsonElement : GsonHelper.getAsJsonArray(json, "elements")) {
				ExtraDirection direction = null;
				boolean b = false;
				List<ExtraDirection> parents = new ArrayList<>();

				if (jsonElement instanceof JsonObject element) {
					if (element.get("condition") instanceof JsonObject condition) {
						direction = ForceFieldModel.ExtraDirection.byName(GsonHelper.getAsString(condition, "direction", "up"));
						b = GsonHelper.getAsBoolean(condition, "if", true);
						for (JsonElement parentElement : GsonHelper.getAsJsonArray(condition, "parents")) {
							parents.add(ForceFieldModel.ExtraDirection.byName(parentElement.getAsString()));
						}
					}
				}
				elementsAndConditions.put(context.deserialize(jsonElement, CuboidModelElement.class), new Condition(direction, b, parents));
			}
		}

		return new UnbakedForceFieldModel(elementsAndConditions, StandardModelParameters.parse(json, context));
	}

	public record Condition(@Nullable ExtraDirection direction, boolean b, List<ExtraDirection> parents) {

	}
}
