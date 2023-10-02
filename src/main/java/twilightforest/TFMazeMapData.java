package twilightforest;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundMapItemDataPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import net.minecraftforge.network.NetworkDirection;
import org.jetbrains.annotations.Nullable;
import twilightforest.init.TFLandmark;
import twilightforest.network.MazeMapPacket;
import twilightforest.network.TFPacketHandler;
import twilightforest.util.LegacyLandmarkPlacements;
import twilightforest.world.registration.TFGenerationSettings;

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
		if (world instanceof ServerLevel && TFGenerationSettings.usesTwilightChunkGenerator((ServerLevel) world)) {
			if (LegacyLandmarkPlacements.getFeatureForRegion(x >> 4, z >> 4, (ServerLevel) world) == TFLandmark.LABYRINTH) {
				BlockPos mc = LegacyLandmarkPlacements.getNearestCenterXZ(x >> 4, z >> 4);
				this.centerX = mc.getX();
				this.centerZ = mc.getZ();
			}
		}
	}

	// [VanillaCopy] Adapted from World.getMapData
	@Nullable
	public static TFMazeMapData getMazeMapData(Level world, String name) {
		if (world.isClientSide) return CLIENT_DATA.get(name);
		else return ((ServerLevel)world).getServer().overworld().getDataStorage().get(TFMazeMapData::load, name);
	}

	// [VanillaCopy] Adapted from World.registerMapData
	public static void registerMazeMapData(Level world, TFMazeMapData data, String id) {
		if (world.isClientSide) CLIENT_DATA.put(id, data);
		else ((ServerLevel) world).getServer().overworld().getDataStorage().set(id, data);
	}

	@Nullable
	@Override
	public Packet<?> getUpdatePacket(int mapId, Player player) {
		Packet<?> packet = super.getUpdatePacket(mapId, player);
		return packet instanceof ClientboundMapItemDataPacket mapItemDataPacket ? TFPacketHandler.CHANNEL.toVanillaPacket(new MazeMapPacket(mapItemDataPacket, ore, yCenter), NetworkDirection.PLAY_TO_CLIENT) : packet;
	}
}
