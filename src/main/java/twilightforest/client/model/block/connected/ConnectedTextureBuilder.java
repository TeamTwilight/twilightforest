package twilightforest.client.model.block.connected;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.CustomLoaderBuilder;
import net.neoforged.neoforge.client.model.generators.ModelBuilder;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import twilightforest.TwilightForestMod;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class ConnectedTextureBuilder<T extends ModelBuilder<T>> extends CustomLoaderBuilder<T> {

	private final List<Direction> enabledFaces = new ArrayList<>();
	private final List<Block> connectableBlocks = new ArrayList<>();
	private final List<TagKey<Block>> connectableTags = new ArrayList<>();
	private boolean renderOnDisabledFaces = true;

	private int baseTintIndex = -1;
	private int baseEmissivity = 0;
	private int tintIndex = -1;
	private int emissivity = 0;

	protected ConnectedTextureBuilder(T parent, ExistingFileHelper existingFileHelper) {
		super(TwilightForestMod.prefix("connected_texture_block"), parent, existingFileHelper, false);
	}

	public static <T extends ModelBuilder<T>> ConnectedTextureBuilder<T> begin(T parent, ExistingFileHelper helper) {
		return new ConnectedTextureBuilder<>(parent, helper);
	}

	public ConnectedTextureBuilder<T> addConnectionFaces(Direction... faces) {
		this.enabledFaces.addAll(List.of(faces));
		return this;
	}

	public ConnectedTextureBuilder<T> disableRenderingOnDisabledFaces() {
		this.renderOnDisabledFaces = false;
		return this;
	}

	public ConnectedTextureBuilder<T> setBaseTintIndex(int index) {
		this.baseTintIndex = index;
		return this;
	}

	public ConnectedTextureBuilder<T> setOverlayTintIndex(int index) {
		this.tintIndex = index;
		return this;
	}

	public ConnectedTextureBuilder<T> setBaseEmissivity(int value) {
		this.baseEmissivity = value;
		return this;
	}

	public ConnectedTextureBuilder<T> setOverlayEmissivity(int value) {
		this.emissivity = value;
		return this;
	}

	public final ConnectedTextureBuilder<T> connectsTo(Block... blocks) {
		this.connectableBlocks.addAll(List.of(blocks));
		return this;
	}

	@SuppressWarnings("varargs")
	@SafeVarargs
	public final ConnectedTextureBuilder<T> connectsTo(TagKey<Block>... blocks) {
		this.connectableTags.addAll(List.of(blocks));
		return this;
	}

	@Override
	public JsonObject toJson(JsonObject json) {
		json = super.toJson(json);
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
		if (!this.enabledFaces.isEmpty() || this.tintIndex > -1 || this.emissivity != 0) {
			JsonObject overlayInfo = new JsonObject();
			if (!this.enabledFaces.isEmpty()) {
				JsonArray array = new JsonArray();
				this.enabledFaces.forEach(face -> array.add(face.getName()));
				overlayInfo.add("faces", array);
			}
			if (this.tintIndex > -1) {
				overlayInfo.addProperty("tint_index", this.tintIndex);
			}
			if (this.emissivity != 0) {
				overlayInfo.addProperty("emissivity", this.emissivity);
			}
			overlayInfo.addProperty("render_disabled_faces", this.renderOnDisabledFaces);
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

		return json;
	}
}