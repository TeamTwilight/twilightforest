package twilightforest.item.mapdata;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import it.unimi.dsi.fastutil.ints.Int2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
import net.minecraft.network.protocol.game.ClientboundMapItemDataPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.maps.MapDecorationTypes;
import net.minecraft.world.level.saveddata.maps.MapId;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFStructures;
import twilightforest.network.MagicMapPacket;
import twilightforest.util.LandmarkUtil;
import twilightforest.util.LegacyLandmarkPlacements;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class TFMagicMapData extends MapItemSavedData {
	private static final Map<String, TFMagicMapData> CLIENT_DATA = new HashMap<>();

	public final Int2ObjectMap<TFMapDecoration> tfDecorations = new Int2ObjectLinkedOpenHashMap<>();

	public TFMagicMapData(int x, int z, byte scale, boolean trackpos, boolean unlimited, boolean locked, ResourceKey<Level> dim) {
		super(x, z, scale, trackpos, unlimited, locked, dim);
	}

	public static TFMagicMapData load(CompoundTag nbt, HolderLookup.Provider provider) {
		MapItemSavedData data = MapItemSavedData.load(nbt, provider);
		final boolean trackingPosition = !nbt.contains("trackingPosition", 1) || nbt.getBoolean("trackingPosition");
		final boolean unlimitedTracking = nbt.getBoolean("unlimitedTracking");
		final boolean locked = nbt.getBoolean("locked");
		TFMagicMapData tfdata = new TFMagicMapData(data.centerX, data.centerZ, data.scale, trackingPosition, unlimitedTracking, locked, data.dimension);

		tfdata.colors = data.colors;
		tfdata.bannerMarkers.putAll(data.bannerMarkers);
		tfdata.decorations.putAll(data.decorations);
		tfdata.frameMarkers.putAll(data.frameMarkers);
		tfdata.trackedDecorationCount = data.trackedDecorationCount;

		byte[] featureStorage = nbt.getByteArray("features");
		if (featureStorage.length > 0) {
			tfdata.deserializeFeatures(featureStorage);
		}

		return tfdata;
	}

	@Override
	public CompoundTag save(CompoundTag cmp, HolderLookup.Provider provider) {
		cmp = super.save(cmp, provider);

		if (!this.tfDecorations.isEmpty()) {
			cmp.putByteArray("features", serializeFeatures());
		}

		return cmp;
	}

	/**
	 * Checks existing features against the feature cache changes wrong ones
	 */
	public void checkExistingFeatures(Level world) {
		IntArrayList toRemove = new IntArrayList();
		Int2ObjectLinkedOpenHashMap<TFMapDecoration> toAdd = new Int2ObjectLinkedOpenHashMap<>();

		for (var entry : tfDecorations.int2ObjectEntrySet()) {
			TFMapDecoration coord = entry.getValue();
			int worldX = (coord.x << this.scale - 1) + this.centerX;
			int worldZ = (coord.y << this.scale - 1) + this.centerZ;

			int trueId = TFMapDecoration.ICONS.getInt(LegacyLandmarkPlacements.pickLandmarkAtBlock(worldX, worldZ, world));
			if (coord.featureId != trueId) {
				toRemove.add(entry.getIntKey());
				if (trueId != 0) {
					toAdd.put(entry.getIntKey(), new TFMapDecoration(trueId, coord.x, coord.y, coord.rot, LandmarkUtil.isConquered(world, worldX, worldZ)));
				}
			}
		}

		for (int packedCoords : toRemove)
			tfDecorations.remove(packedCoords);
		tfDecorations.putAll(toAdd);
	}

	public void deserializeFeatures(byte[] arr) {
		this.tfDecorations.clear();

		for (int i = 0; i < arr.length / 3; ++i) {
			byte featureInfo = arr[i * 3];
			byte mapX = arr[i * 3 + 1];
			byte mapZ = arr[i * 3 + 2];
			byte mapRotation = 8;

			if ((featureInfo & 0b111_1111) != 0)
				this.tfDecorations.put(packCoordBytes(mapX, mapZ), new TFMapDecoration(featureInfo & 0b111_1111, mapX, mapZ, mapRotation, (featureInfo & 0b1000_0000) != 0));
		}
	}

	public byte[] serializeFeatures() {
		byte[] storage = new byte[this.tfDecorations.size() * 3];

		int i = 0;
		for (TFMapDecoration featureCoord : tfDecorations.values()) {
			byte featureInfo = (featureCoord.conquered ? (byte) (featureCoord.featureId | 0b1000_0000) : (byte) featureCoord.featureId);
			storage[i * 3] = featureInfo;
			storage[i * 3 + 1] = featureCoord.x;
			storage[i * 3 + 2] = featureCoord.y;
			i++;
		}

		return storage;
	}

	// [VanillaCopy] Adapted from World.getMapData
	@Nullable
	public static TFMagicMapData getMagicMapData(Level level, String name) {
		if (level instanceof ServerLevel serverLevel) return (TFMagicMapData) serverLevel.getServer().overworld().getDataStorage().get(TFMagicMapData.factory(), name);
		else return CLIENT_DATA.get(name);
	}

	// Like the method above, but if we know we're on client
	@Nullable
	public static TFMagicMapData getClientMagicMapData(String name) {
		return CLIENT_DATA.get(name);
	}

	// [VanillaCopy] Adapted from World.registerMapData
	public static void registerMagicMapData(Level level, TFMagicMapData data, String id) {
		if (level instanceof ServerLevel serverLevel) serverLevel.getServer().overworld().getDataStorage().set(id, data);
		else CLIENT_DATA.put(id, data);
	}

	public static Factory<MapItemSavedData> factory() {
		return new SavedData.Factory<>(() -> {
			throw new IllegalStateException("Should never create an empty map saved data");
		}, TFMagicMapData::load, DataFixTypes.SAVED_DATA_MAP_DATA);
	}

	@Nullable
	@Override
	public Packet<?> getUpdatePacket(MapId mapId, Player player) {
		Packet<?> packet = super.getUpdatePacket(mapId, player);
		return packet instanceof ClientboundMapItemDataPacket mapItemDataPacket ? new ClientboundCustomPayloadPacket(new MagicMapPacket(this, mapItemDataPacket)) : packet;
	}

	public void putMapData(TFMapDecoration info) {
		this.tfDecorations.put(packCoordBytes(info.x, info.y), info);
	}

	private static int packCoordBytes(byte x, byte y) {
		return (x & 0xFF) | ((y & 0xFF) << 8);
	}

	public static class TFMapDecoration {

		private static final Object2IntArrayMap<ResourceKey<Structure>> ICONS = new Object2IntArrayMap<>() {{
			defaultReturnValue(0); // Empty space on icons texture, renders nothing if added anyway
			put(TFStructures.HOLLOW_HILL_SMALL, 1);
			put(TFStructures.HOLLOW_HILL_MEDIUM, 2);
			put(TFStructures.HOLLOW_HILL_LARGE, 3);
			put(TFStructures.HEDGE_MAZE, 4);
			put(TFStructures.NAGA_COURTYARD, 5);
			put(TFStructures.LICH_TOWER, 6);
			put(TFStructures.AURORA_PALACE, 7);
			put(TFStructures.QUEST_GROVE, 9);
			put(TFStructures.HYDRA_LAIR, 12);
			put(TFStructures.LABYRINTH, 13);
			put(TFStructures.DARK_TOWER, 14);
			put(TFStructures.KNIGHT_STRONGHOLD, 15);
			put(TFStructures.YETI_CAVE, 17);
			put(TFStructures.TROLL_CAVE, 18);
			put(TFStructures.FINAL_CASTLE, 19);
		}};

		final int featureId;
		final byte x;
		final byte y;
		final byte rot;
		final boolean conquered;

		public TFMapDecoration(ResourceKey<Structure> featureId, byte xIn, byte yIn, boolean conquered) {
			this(ICONS.getInt(featureId), xIn, yIn, (byte) 8, conquered);
		}

		public TFMapDecoration(int featureId, byte xIn, byte yIn, byte rotationIn, boolean conquered) {
			this.featureId = featureId;
			this.x = xIn;
			this.y = yIn;
			this.rot = rotationIn;
			this.conquered = conquered;
		}

		public boolean render(int idx, PoseStack stack, MultiBufferSource buffer, int light) {
			if (featureId > 0) {
				stack.pushPose();
				stack.translate(0.0F + this.x / 2.0F + 64.0F, 0.0F + this.y / 2.0F + 64.0F, -0.02F);
				stack.mulPose(Axis.ZP.rotationDegrees(this.rot * 360 / 16.0F));
				stack.scale(4.0F, 4.0F, 3.0F);
				stack.translate(-0.125D, 0.125D, 0.0D);
				float uMin = featureId % 8.0F / 8.0F;
				float vMin = featureId / 8 / 8.0F;
				float uMax = (featureId % 8 + 1) / 8.0F;
				float vMax = (featureId / 8 + 1) / 8.0F;
				Matrix4f matrix4f = stack.last().pose();
				float depth = idx * -0.001F;
				VertexConsumer mapIconVertices = buffer.getBuffer(DecorationRenderTypes.MAP_ICONS);
				mapIconVertices.addVertex(matrix4f, -1.0F, 1.0F, depth).setColor(255, 255, 255, 255).setUv(uMin, vMin).setLight(light);
				mapIconVertices.addVertex(matrix4f, 1.0F, 1.0F, depth).setColor(255, 255, 255, 255).setUv(uMax, vMin).setLight(light);
				mapIconVertices.addVertex(matrix4f, 1.0F, -1.0F, depth).setColor(255, 255, 255, 255).setUv(uMax, vMax).setLight(light);
				mapIconVertices.addVertex(matrix4f, -1.0F, -1.0F, depth).setColor(255, 255, 255, 255).setUv(uMin, vMax).setLight(light);

				if (this.conquered) {
					depth -= 0.002f;
					TextureAtlasSprite sprite = Minecraft.getInstance().getMapDecorationTextures().getSprite(MapDecorationTypes.RED_X.value().assetId());
					float f2 = sprite.getU0();
					float f3 = sprite.getV0();
					float f4 = sprite.getU1();
					float f5 = sprite.getV1();
					VertexConsumer vertexconsumer1 = buffer.getBuffer(RenderType.text(sprite.atlasLocation()));
					vertexconsumer1.addVertex(matrix4f, -1.0F, 1.0F, depth).setColor(255, 255, 255, 255).setUv(f2, f3).setLight(light);
					vertexconsumer1.addVertex(matrix4f, 1.0F, 1.0F, depth).setColor(255, 255, 255, 255).setUv(f4, f3).setLight(light);
					vertexconsumer1.addVertex(matrix4f, 1.0F, -1.0F, depth).setColor(255, 255, 255, 255).setUv(f4, f5).setLight(light);
					vertexconsumer1.addVertex(matrix4f, -1.0F, -1.0F, depth).setColor(255, 255, 255, 255).setUv(f2, f5).setLight(light);
				}
				stack.popPose();
			}
			return true;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;
			TFMapDecoration that = (TFMapDecoration) o;
			return featureId == that.featureId && x == that.x && y == that.y && rot == that.rot && conquered == that.conquered;
		}

		@Override
		public int hashCode() {
			return Objects.hash(featureId, x, y, rot, conquered);
		}

		private static class DecorationRenderTypes {
			private static final RenderType MAP_ICONS = RenderType.text(TwilightForestMod.prefix("textures/gui/mapicons.png"));
		}
	}
}
