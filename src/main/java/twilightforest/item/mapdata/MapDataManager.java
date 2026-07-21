package twilightforest.item.mapdata;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.maps.MapId;
import org.jspecify.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/*
* Most of the content in this class is adapted from implementations in ServerLevel and ClientLevel.
*/
public final class MapDataManager {
	private static final Map<MapId, TFMagicMapData> MAGIC_MAP_CLIENT_DATA = new HashMap<>();
	private static final Map<MapId, TFMazeMapData> MAZE_MAP_CLIENT_DATA = new HashMap<>();

	public static @Nullable TFMagicMapData getServerMagicMapData(ServerLevel serverLevel, MapId id) {
		return serverLevel.getServer().getDataStorage().get(TFMagicMapData.magicMapType(id));
	}

	public static @Nullable TFMazeMapData getServerMazeMapData(ServerLevel serverLevel, MapId id) {
		return serverLevel.getServer().getDataStorage().get(TFMazeMapData.mazeMapType(id));
	}

	public static @Nullable TFMagicMapData getClientMagicMapData(MapId id) {
		return MAGIC_MAP_CLIENT_DATA.get(id);
	}

	public static @Nullable TFMazeMapData getClientMazeMapData(MapId id) {
		return MAZE_MAP_CLIENT_DATA.get(id);
	}

	public static void saveServerMagicMapData(ServerLevel serverLevel, MapId id, TFMagicMapData data) {
		serverLevel.getServer().getDataStorage().set(TFMagicMapData.magicMapType(id), data);
	}

	public static void saveServerMazeMapData(ServerLevel serverLevel, MapId id, TFMazeMapData data) {
		serverLevel.getServer().getDataStorage().set(TFMazeMapData.mazeMapType(id), data);
	}

	public static void saveClientMagicMapData(MapId id, TFMagicMapData data) {
		MAGIC_MAP_CLIENT_DATA.put(id, data);
	}

	public static void saveClientMazeMapData(MapId id, TFMazeMapData data) {
		MAZE_MAP_CLIENT_DATA.put(id, data);
	}
}