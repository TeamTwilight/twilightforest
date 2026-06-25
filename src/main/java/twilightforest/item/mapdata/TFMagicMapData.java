package twilightforest.item.mapdata;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.*;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundMapItemDataPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.saveddata.SavedDataType;
import net.minecraft.world.level.saveddata.maps.MapBanner;
import net.minecraft.world.level.saveddata.maps.MapDecoration;
import net.minecraft.world.level.saveddata.maps.MapDecorationType;
import net.minecraft.world.level.saveddata.maps.MapFrame;
import net.minecraft.world.level.saveddata.maps.MapId;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.jetbrains.annotations.Nullable;
import twilightforest.TwilightForestMod;
import twilightforest.item.MagicMapItem;
import twilightforest.network.MagicMapPacket;
import twilightforest.util.Codecs;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TFMagicMapData extends MapItemSavedData {
	private static final Map<String, TFMagicMapData> CLIENT_DATA = new HashMap<>();
	public final List<String> conqueredStructures = new ArrayList<>();
	// redundant copies of parent's private fields, needed for CODEC serialization
	private boolean trackingPosition;
	private boolean unlimitedTracking;

	public TFMagicMapData(int x, int z, byte scale, boolean trackpos, boolean unlimited, boolean locked, ResourceKey<Level> dim) {
		super(x, z, scale, trackpos, unlimited, locked, dim);
		this.trackingPosition = trackpos;
		this.unlimitedTracking = unlimited;
	}

	public static TFMagicMapData load(CompoundTag nbt, HolderLookup.Provider provider) {
		MapItemSavedData data = MapItemSavedData.CODEC.parse(provider.createSerializationContext(NbtOps.INSTANCE), nbt).getOrThrow();
		final boolean trackingPosition = !nbt.contains("trackingPosition") || nbt.getBooleanOr("trackingPosition", false);
		final boolean unlimitedTracking = nbt.getBooleanOr("unlimitedTracking", false);
		final boolean locked = nbt.getBooleanOr("locked", false);
		TFMagicMapData tfdata = new TFMagicMapData(data.centerX, data.centerZ, data.scale, trackingPosition, unlimitedTracking, locked, data.dimension);

		tfdata.colors = data.colors;
		tfdata.bannerMarkers.putAll(data.bannerMarkers);
		tfdata.frameMarkers.putAll(data.frameMarkers);

		for (DecorationHolder decoration : DecorationHolder.CODEC.listOf()
			.parse(provider.createSerializationContext(NbtOps.INSTANCE), nbt.get("decorations"))
			.resultOrPartial(error -> TwilightForestMod.LOGGER.warn("Failed to parse map decoration: '{}'", error))
			.orElse(List.of())) {
			MapDecoration mapdecoration1 = decoration.decoration();
			MapDecoration mapdecoration = tfdata.decorations.put(decoration.id(), mapdecoration1);
			if (!mapdecoration1.equals(mapdecoration)) {
				if (mapdecoration != null && mapdecoration.type().value().trackCount()) {
					tfdata.trackedDecorationCount--;
				}

				if (decoration.decoration().type().value().trackCount()) {
					tfdata.trackedDecorationCount++;
				}
				tfdata.setDecorationsDirty();
			}
		}

		if (nbt.contains("conquered_structures")) {
			tfdata.conqueredStructures.clear();
			ListTag tag = nbt.getList("conquered_structures").orElse(new ListTag());
			tag.forEach(tag1 -> tfdata.conqueredStructures.add(((StringTag) tag1).value()));
		}

		return tfdata;
	}

	public CompoundTag save(CompoundTag tag, HolderLookup.Provider provider) {
		List<DecorationHolder> holders = new ArrayList<>();
		this.decorations.forEach((s, decoration) -> {
			if (decoration.type().value().showOnItemFrame()) {
				holders.add(new DecorationHolder(s, decoration));
			}
		});
		tag.put("decorations", DecorationHolder.CODEC.listOf().encodeStart(NbtOps.INSTANCE, holders).getOrThrow());

		if (!this.conqueredStructures.isEmpty()) {
			ListTag conqueredTag = new ListTag();
			for (String structure : this.conqueredStructures) {
				conqueredTag.add(StringTag.valueOf(structure));
			}
			tag.put("conquered_structures", conqueredTag);
		}

		return tag;
	}

	public static final Codec<TFMagicMapData> TF_CODEC = RecordCodecBuilder.create(
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
			DecorationHolder.CODEC.listOf().optionalFieldOf("decorations", List.of()).forGetter(m -> {
				List<DecorationHolder> list = new ArrayList<>();
				m.decorations.forEach((s, deco) -> {
					if (deco.type().value().showOnItemFrame()) {
						list.add(new DecorationHolder(s, deco));
					}
				});
				return list;
			}),
			Codec.STRING.listOf().optionalFieldOf("conquered_structures", List.of()).forGetter(m -> List.copyOf(m.conqueredStructures))
		).apply(instance, (dim, x, z, scale, colors, tracking, unlimited, locked, banners, frames, decorations, conquered) -> {
			TFMagicMapData data = new TFMagicMapData(x, z, (byte) Mth.clamp(scale, 0, 4), tracking, unlimited, locked, dim);
			if (colors.array().length == 16384) data.colors = colors.array();
			for (MapBanner b : banners) data.bannerMarkers.put(b.getId(), b);
			for (MapFrame f : frames) data.frameMarkers.put(f.getId(), f);
			for (DecorationHolder dh : decorations) {
				data.decorations.put(dh.id(), dh.decoration());
				if (dh.decoration().type().value().trackCount()) data.trackedDecorationCount++;
			}
			data.conqueredStructures.addAll(conquered);
			return data;
		})
	);

	public static SavedDataType<TFMagicMapData> mapDataType(MapId mapId) {
		return new SavedDataType<>(
			TwilightForestMod.prefix(MagicMapItem.STR_ID + "_" + mapId.id()),
			() -> { throw new IllegalStateException("Should never create an empty map saved data"); },
			TF_CODEC,
			DataFixTypes.SAVED_DATA_MAP_DATA
		);
	}

	// [VanillaCopy] Adapted from World.getMapData
	@Nullable
	public static TFMagicMapData getMagicMapData(Level level, MapId mapId) {
		if (level instanceof ServerLevel serverLevel) return serverLevel.getServer().getDataStorage().get(mapDataType(mapId));
		else return CLIENT_DATA.get(MagicMapItem.getMapName(mapId.id()));
	}

	// Like the method above, but if we know we're on client
	@Nullable
	public static TFMagicMapData getClientMagicMapData(String name) {
		return CLIENT_DATA.get(name);
	}

	// [VanillaCopy] Adapted from World.registerMapData
	public static void registerMagicMapData(Level level, TFMagicMapData data, MapId mapId) {
		if (level instanceof ServerLevel serverLevel) serverLevel.getServer().getDataStorage().set(mapDataType(mapId), data);
		else CLIENT_DATA.put(MagicMapItem.getMapName(mapId.id()), data);
	}

	@Nullable
	@Override
	public Packet<?> getUpdatePacket(MapId mapId, Player player) {
		Packet<?> packet = super.getUpdatePacket(mapId, player);
		return packet instanceof ClientboundMapItemDataPacket mapItemDataPacket ? new MagicMapPacket(mapItemDataPacket, this.conqueredStructures).toVanillaClientbound() : packet;
	}

	public void addTFDecoration(Holder<MapDecorationType> decorationType, @Nullable LevelAccessor level, String id, double x, double z, double yRot, boolean conquered) {
		this.addDecoration(decorationType, level, id, x, z, yRot, null);
		MapDecoration deco = this.decorations.get(id);
		if (deco != null) {
			String conqueredID = MagicMapItem.makeName(decorationType, deco.x(), deco.y());
			if (conquered && !this.conqueredStructures.contains(conqueredID)) {
				this.conqueredStructures.add(conqueredID);
			} else if (!conquered) {
				this.conqueredStructures.remove(conqueredID);
			}
		}
	}

	public record DecorationHolder(String id, MapDecoration decoration) {
		public static final Codec<DecorationHolder> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Codec.STRING.fieldOf("id").forGetter(DecorationHolder::id),
			Codecs.DECORATION_CODEC.fieldOf("decoration").forGetter(DecorationHolder::decoration)
		).apply(instance, DecorationHolder::new));
	}
}