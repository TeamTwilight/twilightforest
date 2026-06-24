package twilightforest.item.mapdata;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundMapItemDataPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.maps.MapId;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.jetbrains.annotations.Nullable;
import twilightforest.init.TFStructures;
import twilightforest.network.MazeMapPacket;
import twilightforest.util.landmarks.LegacyLandmarkPlacements;

import java.util.HashMap;
import java.util.Map;

public class TFMazeMapData extends MapItemSavedData {
	private static final Map<MapId, TFMazeMapData> CLIENT_DATA = new HashMap<>();

	public static final Codec<TFMazeMapData> CODEC = RecordCodecBuilder.create(instance ->
		instance.group(
			Codec.INT.fieldOf("xCenter").forGetter(data -> data.centerX),
			Codec.INT.fieldOf("zCenter").forGetter(data -> data.centerZ),
			Codec.BYTE.fieldOf("scale").forGetter(data -> data.scale),
			Codec.BOOL.optionalFieldOf("trackingPosition", true).forGetter(data -> true),
			Codec.BOOL.optionalFieldOf("unlimitedTracking", false).forGetter(data -> false),
			Codec.BOOL.optionalFieldOf("locked", false).forGetter(data -> data.locked),
			ResourceKey.codec(Registries.DIMENSION).fieldOf("dimension").forGetter(data -> data.dimension)
		).apply(instance, (centerX, centerZ, scale, trackingPosition, unlimitedTracking, locked, dimension) -> {
			TFMazeMapData mazeData = new TFMazeMapData(centerX, centerZ, scale, trackingPosition, unlimitedTracking, locked, dimension);
			return mazeData;
		})
	);


	public int yCenter;
	public boolean ore;

	public TFMazeMapData(int x, int z, byte scale, boolean trackpos, boolean unlimited, boolean locked, ResourceKey<Level> dim) {
		super(x, z, scale, trackpos, unlimited, locked, dim);
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
	public static TFMazeMapData getMazeMapData(Level level, MapId mapId) {
		if (level instanceof ServerLevel serverLevel) {
			MapItemSavedData baseData = serverLevel.getServer().overworld().getMapData(mapId);
			if (baseData instanceof TFMazeMapData tfData) {
				return tfData;
			}
			return null;
		} else {
			return CLIENT_DATA.get(mapId);
		}
	}

	// [VanillaCopy] Adapted from World.registerMapData
	public static void registerMazeMapData(Level level, TFMazeMapData data, MapId id) {
		if (level.isClientSide()) CLIENT_DATA.put(id, data);
		else ((ServerLevel) level).getServer().overworld().getDataStorage().set(type(id), data);
	}

	@Nullable
	@Override
	public Packet<?> getUpdatePacket(MapId mapId, Player player) {
		Packet<?> packet = super.getUpdatePacket(mapId, player);
		return packet instanceof ClientboundMapItemDataPacket mapItemDataPacket ? new MazeMapPacket(mapItemDataPacket, this.ore, this.yCenter).toVanillaClientbound() : packet;
	}
}
