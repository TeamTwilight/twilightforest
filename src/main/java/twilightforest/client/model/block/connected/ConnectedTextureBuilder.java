package twilightforest.client.model.block.connected;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.template.CustomLoaderBuilder;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import twilightforest.TwilightForestMod;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class ConnectedTextureBuilder extends CustomLoaderBuilder {

	private List<Direction> connectedFaces = new ArrayList<>();
	private List<Block> connectableBlocks = new ArrayList<>();
	private List<TagKey<Block>> connectableTags = new ArrayList<>();
	@Nullable
	private Pair<Vector3f, Vector3f> element;
	private boolean renderOverlayOnAllFaces = true;

	private int baseTintIndex = -1;
	private int baseEmissivity = 0;
	private int tintIndex = -1;
	private int emissivity = 0;
	private boolean translucent;

	public ConnectedTextureBuilder() {
		super(TwilightForestMod.prefix("connected_texture_block"), false);
	}

	public static ConnectedTextureBuilder begin() {
		return new ConnectedTextureBuilder();
	}

	public ConnectedTextureBuilder addConnectionFaces(Direction... faces) {
		this.connectedFaces.addAll(List.of(faces));
		return this;
	}

	public ConnectedTextureBuilder createElement(Vector3f from, Vector3f to) {
		this.element = Pair.of(from, to);
		return this;
	}

	public ConnectedTextureBuilder disableOverlayRenderingOnAllFaces() {
		this.renderOverlayOnAllFaces = false;
		return this;
	}

	public ConnectedTextureBuilder setBaseTintIndex(int index) {
		this.baseTintIndex = index;
		return this;
	}

	public ConnectedTextureBuilder setOverlayTintIndex(int index) {
		this.tintIndex = index;
		return this;
	}

	public ConnectedTextureBuilder setBaseEmissivity(int value) {
		this.baseEmissivity = value;
		return this;
	}

	public ConnectedTextureBuilder setOverlayEmissivity(int value) {
		this.emissivity = value;
		return this;
	}

	public ConnectedTextureBuilder setTranslucent() {
		this.translucent = true;
		return this;
	}

	public final ConnectedTextureBuilder connectsTo(Block... blocks) {
		this.connectableBlocks.addAll(List.of(blocks));
		return this;
	}

	@SuppressWarnings("varargs")
	@SafeVarargs
	public final ConnectedTextureBuilder connectsTo(TagKey<Block>... blocks) {
		this.connectableTags.addAll(List.of(blocks));
		return this;
	}

	@Override
	protected ConnectedTextureBuilder copyInternal() {
		ConnectedTextureBuilder builder = new ConnectedTextureBuilder();
		builder.connectedFaces = List.copyOf(this.connectedFaces);
		builder.connectableBlocks = List.copyOf(this.connectableBlocks);
		builder.connectableTags = List.copyOf(this.connectableTags);
		builder.element = this.element;
		builder.renderOverlayOnAllFaces = this.renderOverlayOnAllFaces;
		builder.baseTintIndex = this.baseTintIndex;
		builder.baseEmissivity = this.baseEmissivity;
		builder.tintIndex = this.tintIndex;
		builder.emissivity = this.emissivity;
		builder.translucent = this.translucent;
		return builder;
	}

	@Override
	public JsonObject toJson(JsonObject json) {
		json = super.toJson(json);
		if (this.translucent) {
			json.addProperty("translucent", true);
		}
		if (this.baseTintIndex > -1 || this.baseEmissivity != 0) {
			JsonObject baseInfo = new JsonObject();
			if (this.baseTintIndex > -1) {
				baseInfo.addProperty("tint_index", this.baseTintIndex);
			}
			if (this.baseEmissivity != 0) {
				baseInfo.addProperty("emissivity", this.baseEmissivity);
			}
			json.add("base", baseInfo);
		}
		if (!this.connectedFaces.isEmpty() || this.tintIndex > -1 || this.emissivity != 0) {
			JsonObject overlayInfo = new JsonObject();
			if (!this.connectedFaces.isEmpty()) {
				JsonArray array = new JsonArray();
				this.connectedFaces.forEach(face -> array.add(face.getName()));
				overlayInfo.add("faces", array);
			}
			if (this.tintIndex > -1) {
				overlayInfo.addProperty("tint_index", this.tintIndex);
			}
			if (this.emissivity != 0) {
				overlayInfo.addProperty("emissivity", this.emissivity);
			}
			if (!this.renderOverlayOnAllFaces) {
				overlayInfo.addProperty("always_render_overlay", false);
			}
			json.add("connected_texture", overlayInfo);
		}

		if (!this.connectableTags.isEmpty() || !this.connectableBlocks.isEmpty()) {
			JsonArray connectables = new JsonArray();
			if (!this.connectableBlocks.isEmpty()) {
				this.connectableBlocks.forEach(block -> connectables.add(BuiltInRegistries.BLOCK.getKey(block).toString()));
			}

			if (!this.connectableTags.isEmpty()) {
				this.connectableTags.forEach(tag -> connectables.add("#" + tag.location()));
			}

			json.add("connectable_blocks", connectables);
		}

		if (this.element != null) {
			JsonObject serializedElement = new JsonObject();
			serializedElement.add("from", serializeVector3f(this.element.getFirst()));
			serializedElement.add("to", serializeVector3f(this.element.getSecond()));
			json.add("element", serializedElement);
		}

		return json;
	}

	private static JsonArray serializeVector3f(Vector3f vec) {
		JsonArray ret = new JsonArray();
		ret.add(serializeFloat(vec.x()));
		ret.add(serializeFloat(vec.y()));
		ret.add(serializeFloat(vec.z()));
		return ret;
	}

	private static Number serializeFloat(float f) {
		if ((int) f == f) {
			return (int) f;
		}
		return f;
	}
}