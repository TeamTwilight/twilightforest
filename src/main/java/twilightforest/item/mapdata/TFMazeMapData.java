package twilightforest.item.mapdata;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundMapItemDataPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedDataType;
import net.minecraft.world.level.saveddata.maps.MapBanner;
import net.minecraft.world.level.saveddata.maps.MapFrame;
import net.minecraft.world.level.saveddata.maps.MapId;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.jetbrains.annotations.Nullable;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFStructures;
import twilightforest.item.MazeMapItem;
import twilightforest.network.MazeMapPacket;
import twilightforest.util.landmarks.LegacyLandmarkPlacements;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TFMazeMapData extends MapItemSavedData {
	private static final Map<String, TFMazeMapData> CLIENT_DATA = new HashMap<>();

	public int yCenter;
	public boolean ore;
	// redundant copies of parent's private fields, needed for CODEC serialization
	private boolean trackingPosition;
	private boolean unlimitedTracking;

	public TFMazeMapData(int x, int z, byte scale, boolean trackpos, boolean unlimited, boolean locked, ResourceKey<Level> dim) {
		super(x, z, scale, trackpos, unlimited, locked, dim);
		this.trackingPosition = trackpos;
		this.unlimitedTracking = unlimited;
	}

	public static TFMazeMapData load(CompoundTag nbt, HolderLookup.Provider provider) {
		MapItemSavedData data = MapItemSavedData.CODEC.parse(provider.createSerializationContext(NbtOps.INSTANCE), nbt).getOrThrow();
		final boolean trackingPosition = !nbt.contains("trackingPosition") || nbt.getBoolean("trackingPosition").orElse(false);
		final boolean unlimitedTracking = nbt.getBoolean("unlimitedTracking").orElse(false);
		final boolean locked = nbt.getBoolean("locked").orElse(false);
		TFMazeMapData tfdata = new TFMazeMapData(data.centerX, data.centerZ, data.scale, trackingPosition, unlimitedTracking, locked, data.dimension);

		tfdata.colors = data.colors;
		tfdata.bannerMarkers.putAll(data.bannerMarkers);
		tfdata.decorations.putAll(data.decorations);
		tfdata.frameMarkers.putAll(data.frameMarkers);
		tfdata.trackedDecorationCount = data.trackedDecorationCount;

		tfdata.yCenter = nbt.getIntOr("yCenter", 0);
		tfdata.ore = nbt.getBooleanOr("mapOres", false);

		return tfdata;
	}

	public CompoundTag save(CompoundTag nbt, HolderLookup.Provider provider) {
		nbt.putInt("yCenter", this.yCenter);
		nbt.putBoolean("mapOres", this.ore);
		return nbt;
	}

	public void calculateMapCenter(Level world, int x, int y, int z) {
		this.yCenter = y;

		// when we are in a labyrinth, snap to the LABYRINTH
		if (world instanceof ServerLevel level) {
			if (LegacyLandmarkPlacements.pickLandmarkForChunk(x >> 4, z >> 4, level) == TFStructures.LABYRINTH) {
				BlockPos mc = LegacyLandmarkPlacements.getNearestCenterXZ(x >> 4, z >> 4);
				this.centerX = mc.getX();
				this.centerZ = mc.getZ();
			}
		}
	}

	public static final Codec<TFMazeMapData> TF_CODEC = RecordCodecBuilder.create(
		instance -> instance.group(
			Level.RESOURCE_KEY_CODEC.fieldOf("dimension").forGetter(m -> m.dimension),
			Codec.INT.fieldOf("xCenter").forGetter(m -> m.centerX),
			Codec.INT.fieldOf("zCenter").forGetter(m -> m.centerZ),
			Codec.BYTE.optionalFieldOf("scale", (byte)0).forGetter(m -> m.scale),
			Codec.BYTE_BUFFER.fieldOf("colors").forGetter(m -> ByteBuffer.wrap(m.colors)),
			Codec.BOOL.optionalFieldOf("trackingPosition", true).forGetter(m -> m.trackingPosition),
			Codec.BOOL.optionalFieldOf("unlimitedTracking", false).forGetter(m -> m.unlimitedTracking),
			Codec.BOOL.optionalFieldOf("locked", false).forGetter(m -> m.locked),
			MapBanner.CODEC.listOf().optionalFieldOf("banners", List.of()).forGetter(m -> List.copyOf(m.bannerMarkers.values())),
			MapFrame.CODEC.listOf().optionalFieldOf("frames", List.of()).forGetter(m -> List.copyOf(m.frameMarkers.values())),
			Codec.INT.optionalFieldOf("yCenter", 0).forGetter(m -> m.yCenter),
			Codec.BOOL.optionalFieldOf("mapOres", false).forGetter(m -> m.ore)
		).apply(instance, (dim, x, z, scale, colors, tracking, unlimited, locked, banners, frames, yCenter, ore) -> {
			TFMazeMapData data = new TFMazeMapData(x, z, (byte) Mth.clamp(scale, 0, 4), tracking, unlimited, locked, dim);
			if (colors.array().length == 16384) data.colors = colors.array();
			for (MapBanner b : banners) data.bannerMarkers.put(b.getId(), b);
			for (MapFrame f : frames) data.frameMarkers.put(f.getId(), f);
			data.yCenter = yCenter;
			data.ore = ore;
			return data;
		})
	);

	public static SavedDataType<TFMazeMapData> mapDataType(MapId mapId) {
		return new SavedDataType<>(
			TwilightForestMod.prefix(MazeMapItem.STR_ID + "_" + mapId.id()),
			() -> { throw new IllegalStateException("Should never create an empty map saved data"); },
			TF_CODEC,
			DataFixTypes.SAVED_DATA_MAP_DATA
		);
	}

	// [VanillaCopy] Adapted from World.getMapData
	@Nullable
	public static TFMazeMapData getMazeMapData(Level level, MapId mapId) {
		if (level.isClientSide()) return CLIENT_DATA.get(MazeMapItem.getMapName(mapId.id()));
		else return level.getServer().getDataStorage().get(mapDataType(mapId));
	}

	// Like the method above, but if we know we're on client
	@Nullable
	public static TFMazeMapData getClientMagicMapData(String name) {
		return CLIENT_DATA.get(name);
	}

	// [VanillaCopy] Adapted from World.registerMapData
	public static void registerMazeMapData(Level level, TFMazeMapData data, MapId mapId) {
		if (level.isClientSide()) CLIENT_DATA.put(MazeMapItem.getMapName(mapId.id()), data);
		else level.getServer().getDataStorage().set(mapDataType(mapId), data);
	}

	@Nullable
	@Override
	public Packet<?> getUpdatePacket(MapId mapId, Player player) {
		Packet<?> packet = super.getUpdatePacket(mapId, player);
		return packet instanceof ClientboundMapItemDataPacket mapItemDataPacket ? new MazeMapPacket(mapItemDataPacket, this.ore, this.yCenter).toVanillaClientbound() : packet;
	}
}