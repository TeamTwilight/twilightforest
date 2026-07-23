package twilightforest.item.mapdata;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundMapItemDataPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Mth;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.saveddata.SavedDataType;
import net.minecraft.world.level.saveddata.maps.*;
import org.jspecify.annotations.Nullable;
import twilightforest.TwilightForestMod;
import twilightforest.item.MagicMapItem;
import twilightforest.network.MagicMapPacket;
import twilightforest.util.Codecs;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class TFMagicMapData extends MapItemSavedData {
	public final List<String> conqueredStructures = new ArrayList<>();

	// [VanillaCopy] from MapItemSavedData but with our own fields and constructor
	public static final Codec<TFMagicMapData> CODEC = RecordCodecBuilder.create(i ->
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
			DecorationHolder.CODEC
				.listOf()
				.optionalFieldOf("decorations", List.of())
				.forGetter(m -> {
					List<DecorationHolder> holders = new ArrayList<>();
					m.decorations.forEach((id, decoration) -> {
						if (decoration.type().value().showOnItemFrame()) {
							holders.add(new DecorationHolder(id, decoration));
						}
					});
					return holders;
				}),
			Codec.STRING
				.listOf()
				.optionalFieldOf("conquered_structures", List.of())
				.forGetter(m -> List.copyOf(m.conqueredStructures))
		).apply(i, TFMagicMapData::new)
	);

	// [VanillaCopy] from MapItemSavedData but changed to use our Codec and namespace
	public static SavedDataType<TFMagicMapData> magicMapType(MapId id) {
		return new SavedDataType<>(TwilightForestMod.prefix(id.key()), () -> {
			throw new IllegalStateException("Should never create an empty map saved data");
		}, CODEC, DataFixTypes.SAVED_DATA_MAP_DATA);
	}

	// [VanillaCopy] from MapItemSavedData
	public TFMagicMapData(int centerX, int centerZ, byte scale, boolean trackingPosition, boolean unlimitedTracking, boolean locked, ResourceKey<Level> dimension) {
		super(centerX, centerZ, scale, trackingPosition, unlimitedTracking, locked, dimension);
	}

	// [VanillaCopy] from MapItemSavedData but with our own fields
	private TFMagicMapData(ResourceKey<Level> dimension, int centerX, int centerZ, byte scale, ByteBuffer colors, boolean trackingPosition, boolean unlimitedTracking, boolean locked, List<MapBanner> banners, List<MapFrame> frames, List<DecorationHolder> decorations, List<String> conqueredStructures) {
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


		for (DecorationHolder holder : decorations) {
			MapDecoration newDecoration = holder.decoration();
			MapDecoration oldDecoration = this.decorations.put(holder.id(), newDecoration);

			if (!newDecoration.equals(oldDecoration)) {
				if (oldDecoration != null && oldDecoration.type().value().trackCount()) {
					this.trackedDecorationCount--;
				}

				if (newDecoration.type().value().trackCount()) {
					this.trackedDecorationCount++;
				}
			}
		}

		this.conqueredStructures.addAll(conqueredStructures);
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