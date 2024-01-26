package twilightforest.item.mapdata;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
import net.minecraft.network.protocol.game.ClientboundMapItemDataPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.jetbrains.annotations.Nullable;
import twilightforest.init.TFStructures;
import twilightforest.network.MazeMapPacket;
import twilightforest.util.LegacyLandmarkPlacements;

import java.util.HashMap;
import java.util.Map;

public class TFMazeMapData extends MapItemSavedData {
	private static final Map<String, TFMazeMapData> CLIENT_DATA = new HashMap<>();

	public int yCenter;
	public boolean ore;

	public TFMazeMapData(int x, int z, byte scale, boolean trackpos, boolean unlimited, boolean locked, ResourceKey<Level> dim) {
		super(x, z, scale, trackpos, unlimited, locked, dim);
	}

	public static TFMazeMapData load(CompoundTag nbt) {
		MapItemSavedData data = MapItemSavedData.load(nbt);
		final boolean trackingPosition = !nbt.contains("trackingPosition", 1) || nbt.getBoolean("trackingPosition");
		final boolean unlimitedTracking = nbt.getBoolean("unlimitedTracking");
		final boolean locked = nbt.getBoolean("locked");
		TFMazeMapData tfdata = new TFMazeMapData(data.centerX, data.centerZ, data.scale, trackingPosition, unlimitedTracking, locked, data.dimension);

		tfdata.colors = data.colors;
		tfdata.bannerMarkers.putAll(data.bannerMarkers);
		tfdata.decorations.putAll(data.decorations);
		tfdata.frameMarkers.putAll(data.frameMarkers);
		tfdata.trackedDecorationCount = data.trackedDecorationCount;

		tfdata.yCenter = nbt.getInt("yCenter");
		tfdata.ore = nbt.getBoolean("mapOres");

		return tfdata;
	}

	@Override
	public CompoundTag save(CompoundTag nbt) {
		CompoundTag ret = super.save(nbt);
		ret.putInt("yCenter", this.yCenter);
		ret.putBoolean("mapOres", this.ore);
		return ret;
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

	// [VanillaCopy] Adapted from World.getMapData
	@Nullable
	public static TFMazeMapData getMazeMapData(Level level, String name) {
		if (level.isClientSide()) return CLIENT_DATA.get(name);
		else return (TFMazeMapData) ((ServerLevel)level).getServer().overworld().getDataStorage().get(TFMazeMapData.factory(), name);
	}

	public static SavedData.Factory<MapItemSavedData> factory() {
		return new SavedData.Factory<>(() -> {
			throw new IllegalStateException("Should never create an empty map saved data");
		}, TFMazeMapData::load, DataFixTypes.SAVED_DATA_MAP_DATA);
	}

	// [VanillaCopy] Adapted from World.registerMapData
	public static void registerMazeMapData(Level level, TFMazeMapData data, String id) {
		if (level.isClientSide()) CLIENT_DATA.put(id, data);
		else ((ServerLevel) level).getServer().overworld().getDataStorage().set(id, data);
	}

	@Nullable
	@Override
	public Packet<?> getUpdatePacket(int mapId, Player player) {
		Packet<?> packet = super.getUpdatePacket(mapId, player);
		return packet instanceof ClientboundMapItemDataPacket mapItemDataPacket ? new ClientboundCustomPayloadPacket(new MazeMapPacket(mapItemDataPacket, this.ore, this.yCenter)) : packet;
	}
}
