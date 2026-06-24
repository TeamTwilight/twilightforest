package twilightforest.item.mapdata;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundMapItemDataPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.saveddata.maps.MapDecoration;
import net.minecraft.world.level.saveddata.maps.MapDecorationType;
import net.minecraft.world.level.saveddata.maps.MapId;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.jetbrains.annotations.Nullable;
import twilightforest.item.MagicMapItem;
import twilightforest.network.MagicMapPacket;
import twilightforest.util.Codecs;

import java.util.*;

public class TFMagicMapData extends MapItemSavedData {
	private static final Codec<byte[]> COLORS_CODEC = Codec.BYTE.listOf()
		.xmap(list -> {
			byte[] arr = new byte[list.size()];
			for (int i = 0; i < list.size(); i++) arr[i] = list.get(i);
			return arr;
		}, array -> {
			List<Byte> list = new ArrayList<>(array.length);
			for (byte b : array) list.add(b);
			return list;
		});

	public static final Codec<TFMagicMapData> CODEC = RecordCodecBuilder.create(instance ->
		instance.group(
			Codec.INT.fieldOf("xCenter").forGetter(data -> data.centerX),
			Codec.INT.fieldOf("zCenter").forGetter(data -> data.centerZ),
			Codec.BYTE.fieldOf("scale").forGetter(data -> data.scale),
			Codec.BOOL.optionalFieldOf("trackingPosition", true).forGetter(data -> data.trackingPosition),
			Codec.BOOL.optionalFieldOf("unlimitedTracking", false).forGetter(data -> data.unlimitedTracking),
			Codec.BOOL.optionalFieldOf("locked", false).forGetter(data -> data.locked),
			ResourceKey.codec(Registries.DIMENSION).fieldOf("dimension").forGetter(data -> data.dimension),
			COLORS_CODEC.fieldOf("colors").forGetter(data -> data.colors),
			DecorationHolder.CODEC.listOf().optionalFieldOf("decorations", List.of()).forGetter(data -> {
				List<DecorationHolder> holders = new ArrayList<>();
				data.decorations.forEach((s, decoration) -> {
					if (decoration.type().value().showOnItemFrame()) {
						holders.add(new DecorationHolder(s, decoration));
					}
				});
				return holders;
			}),
			Codec.STRING.listOf().optionalFieldOf("conquered_structures", List.of()).forGetter(data -> data.conqueredStructures)
		).apply(instance, (centerX, centerZ, scale, trackingPosition, unlimitedTracking, locked, dimension, colorsArray, decorationsList, conqueredList) -> {
			TFMagicMapData tfdata = new TFMagicMapData(centerX, centerZ, scale, trackingPosition, unlimitedTracking, locked, dimension);
			if (colorsArray.length == tfdata.colors.length) {
				System.arraycopy(colorsArray, 0, tfdata.colors, 0, colorsArray.length);
			}
			for (DecorationHolder decoration : decorationsList) {
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
			tfdata.conqueredStructures.clear();
			tfdata.conqueredStructures.addAll(conqueredList);
			return tfdata;
		})
	);

	private static final Map<MapId, TFMagicMapData> CLIENT_DATA = new HashMap<>();
	public final List<String> conqueredStructures = new ArrayList<>();

	public TFMagicMapData(int x, int z, byte scale, boolean trackpos, boolean unlimited, boolean locked, ResourceKey<Level> dim) {
		super(x, z, scale, trackpos, unlimited, locked, dim);
	}

	// [VanillaCopy] Adapted from World.getMapData
	@Nullable
	public static TFMagicMapData getMagicMapData(Level level, MapId mapId) {
		if (level instanceof ServerLevel serverLevel) {
			MapItemSavedData baseData = serverLevel.getServer().overworld().getMapData(mapId);
			if (baseData instanceof TFMagicMapData tfData) {
				return tfData;
			}
			return null;
		} else {
			return CLIENT_DATA.get(mapId);
		}
	}


	// [VanillaCopy] Adapted from World.registerMapData
	public static void registerMagicMapData(Level level, TFMagicMapData data, MapId id) {
		if (level instanceof ServerLevel serverLevel) serverLevel.getServer().overworld().getDataStorage().set(type(id), data);
		else CLIENT_DATA.put(id, data);
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
