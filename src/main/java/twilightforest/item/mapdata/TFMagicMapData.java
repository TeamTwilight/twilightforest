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
import net.minecraft.util.context.ContextKey;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.maps.*;
import org.jetbrains.annotations.Nullable;
import twilightforest.TwilightForestMod;
import twilightforest.item.MagicMapItem;
import twilightforest.network.MagicMapPacket;
import twilightforest.util.Codecs;

import java.util.*;

public class TFMagicMapData extends MapItemSavedData {

	public static ContextKey<Boolean> MAGIC_MAP_KEY = new ContextKey<>(TwilightForestMod.prefix("is_magic_map"));
	public static ContextKey<List<String>> CONQUERED_STRUCTURES_KEY = new ContextKey<>(TwilightForestMod.prefix("conquered_structures"));

	private static final Map<String, TFMagicMapData> CLIENT_DATA = new HashMap<>();
	public final List<String> conqueredStructures = new ArrayList<>();

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

		if (nbt.contains("conquered_structures", Tag.TAG_LIST)) {
			tfdata.conqueredStructures.clear();
			ListTag tag = nbt.getList("conquered_structures", Tag.TAG_STRING);
			tag.forEach(tag1 -> tfdata.conqueredStructures.add(tag1.getAsString()));
		}

		return tfdata;
	}

	@Override
	public CompoundTag save(CompoundTag tag, HolderLookup.Provider provider) {
		tag = super.save(tag, provider);

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
