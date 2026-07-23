package twilightforest.item.mapdata;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundMapItemDataPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedDataType;
import net.minecraft.world.level.saveddata.maps.*;
import org.jspecify.annotations.Nullable;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFStructures;
import twilightforest.network.MazeMapPacket;
import twilightforest.util.landmarks.LegacyLandmarkPlacements;

import java.nio.ByteBuffer;
import java.util.List;

public class TFMazeMapData extends MapItemSavedData {
	public int yCenter;
	public boolean ore;

	// [VanillaCopy] from MapItemSavedData but with our own fields and constructor
	public static final Codec<TFMazeMapData> CODEC = RecordCodecBuilder.create(i ->
		i.group(
			Level.RESOURCE_KEY_CODEC
				.fieldOf("dimension")
				.forGetter(m -> m.dimension),
			Codec.INT
				.fieldOf("xCenter")
				.forGetter(m -> m.centerX),
			Codec.INT
				.fieldOf("zCenter")
				.forGetter(m -> m.centerZ),
			Codec.BYTE
				.optionalFieldOf("scale", (byte) 0)
				.forGetter(m -> m.scale),
			Codec.BYTE_BUFFER
				.fieldOf("colors")
				.forGetter(m -> ByteBuffer.wrap(m.colors)),
			Codec.BOOL
				.optionalFieldOf("trackingPosition", true)
				.forGetter(m -> m.trackingPosition),
			Codec.BOOL
				.optionalFieldOf("unlimitedTracking", false)
				.forGetter(m -> m.unlimitedTracking),
			Codec.BOOL
				.optionalFieldOf("locked", false)
				.forGetter(m -> m.locked),
			MapBanner.CODEC
				.listOf()
				.optionalFieldOf("banners", List.of())
				.forGetter(m -> List.copyOf(m.bannerMarkers.values())),
			MapFrame.CODEC
				.listOf()
				.optionalFieldOf("frames", List.of())
				.forGetter(m -> List.copyOf(m.frameMarkers.values())),
			Codec.INT
				.optionalFieldOf("yCenter", 0)
				.forGetter(m -> m.yCenter),
			Codec.BOOL
				.optionalFieldOf("mapOres", false)
				.forGetter(m -> m.ore)
		).apply(i, TFMazeMapData::new)
	);

	// [VanillaCopy] from MapItemSavedData but changed to use our Codec and namespace
	public static SavedDataType<TFMazeMapData> mazeMapType(MapId id) {
		return new SavedDataType<>(TwilightForestMod.prefix(id.key()), () -> {
			throw new IllegalStateException("Should never create an empty map saved data");
		}, CODEC, DataFixTypes.SAVED_DATA_MAP_DATA);
	}

	// [VanillaCopy] from MapItemSavedData
	public TFMazeMapData(int centerX, int centerZ, byte scale, boolean trackingPosition, boolean unlimitedTracking, boolean locked, ResourceKey<Level> dimension) {
		super(centerX, centerZ, scale, trackingPosition, unlimitedTracking, locked, dimension);
	}

	// [VanillaCopy] from MapItemSavedData but with our own fields
	private TFMazeMapData(ResourceKey<Level> dimension, int centerX, int centerZ, byte scale, ByteBuffer colors, boolean trackingPosition, boolean unlimitedTracking, boolean locked, List<MapBanner> banners, List<MapFrame> frames, int yCenter, boolean ore) {
		this(centerX, centerZ, (byte) Mth.clamp(scale, 0, 4), trackingPosition, unlimitedTracking, locked, dimension);

		if (colors.array().length == 16384) {
			this.colors = colors.array();
		}

		for(MapBanner banner : banners) {
			this.bannerMarkers.put(banner.getId(), banner);
			this.addDecoration(banner.getDecoration(), null, banner.getId(), banner.pos().getX(), banner.pos().getZ(), 180.0F, banner.name().orElse(null));
		}

		for(MapFrame frame : frames) {
			this.frameMarkers.put(frame.getId(), frame);
			this.addDecoration(MapDecorationTypes.FRAME, null, getFrameKey(frame.entityId()), frame.pos().getX(), frame.pos().getZ(), frame.rotation(), null); // getFrameKey(...) needs an AT entry or VanillaCopy
		}

		this.yCenter = yCenter;
		this.ore = ore;
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

	@Nullable
	@Override
	public Packet<?> getUpdatePacket(MapId mapId, Player player) {
		Packet<?> packet = super.getUpdatePacket(mapId, player);
		return packet instanceof ClientboundMapItemDataPacket mapItemDataPacket ? new MazeMapPacket(mapItemDataPacket, this.ore, this.yCenter).toVanillaClientbound() : packet;
	}
}