package twilightforest.client.model.block.connected;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.client.model.geometry.IGeometryLoader;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class ConnectedTextureModelLoader implements IGeometryLoader<UnbakedConnectedTextureModel> {
	public static final ConnectedTextureModelLoader INSTANCE = new ConnectedTextureModelLoader();

	public ConnectedTextureModelLoader() {
	}

	@Override
	public UnbakedConnectedTextureModel read(JsonObject jsonObject, JsonDeserializationContext deserializationContext) throws JsonParseException {
		JsonObject baseTextureInfo = GsonHelper.getAsJsonObject(jsonObject, "base", new JsonObject());
		int baseTintIndex = GsonHelper.getAsInt(baseTextureInfo, "tint_index", -1);
		int baseEmissivity = GsonHelper.getAsInt(baseTextureInfo, "emissivity", 0);

		JsonObject overlayInfo = GsonHelper.getAsJsonObject(jsonObject, "connected_texture", new JsonObject());
		int tintIndex = GsonHelper.getAsInt(overlayInfo, "tint_index", -1);
		int emissivity = GsonHelper.getAsInt(overlayInfo, "emissivity", 0);
		boolean renderDisabled = GsonHelper.getAsBoolean(overlayInfo, "render_disabled_faces", true);
		EnumSet<Direction> faces = this.parseEnabledFaces(overlayInfo);

		List<Block> connectables = this.parseConnnectableBlocks(jsonObject);
		return new UnbakedConnectedTextureModel(faces, renderDisabled, connectables, baseTintIndex, baseEmissivity, tintIndex, emissivity);
	}

	private EnumSet<Direction> parseEnabledFaces(JsonObject object) {
		if (!object.has("faces")) {
			return EnumSet.allOf(Direction.class);
		} else {
			EnumSet<Direction> faces = EnumSet.noneOf(Direction.class);

			for (JsonElement element : object.getAsJsonArray("faces")) {
				Direction face = Direction.byName(element.getAsString());
				if (face == null) {
					throw new JsonParseException("Invalid face: " + element.getAsString());
				}

				faces.add(face);
			}

			return faces;
		}
	}

	private List<Block> parseConnnectableBlocks(JsonObject object) {
		if (!object.has("connectable_blocks")) {
			return List.of();
		} else {
			List<Block> blocks = new ArrayList<>();

			for (JsonElement element : object.getAsJsonArray("connectable_blocks")) {
				if (element.getAsString().startsWith("#")) {
					ResourceLocation tag = ResourceLocation.tryParse(element.getAsString().substring(1));
					if (tag != null) {
						BuiltInRegistries.BLOCK.getTagOrEmpty(TagKey.create(Registries.BLOCK, tag)).forEach(blockHolder -> blocks.add(blockHolder.value()));
					} else {
						throw new JsonParseException("Invalid block tag: " + element.getAsString());
					}
				} else {
					Block block = BuiltInRegistries.BLOCK.get(ResourceLocation.tryParse(element.getAsString()));
					if (block == Blocks.AIR) {
						throw new JsonParseException("Invalid block: " + element.getAsString());
					}
					blocks.add(block);
				}
			}

			return blocks;
		}
	}
}
