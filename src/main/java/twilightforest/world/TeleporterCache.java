package twilightforest.world;

import com.google.common.collect.Maps;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ColumnPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;
import org.jetbrains.annotations.Nullable;
import twilightforest.TwilightForestMod;

import java.util.HashMap;
import java.util.Map;

public class TeleporterCache extends SavedData {

	// destinationCoordinateCache is (src -> dest) [DestWorld, [SrcPos, DestPos]]
	private final Map<ResourceKey<Level>, Map<ColumnPos, TFTeleporter.PortalPosition>> destinationCoordinateCache = new HashMap<>();

	private TeleporterCache() {
	}

	public static TeleporterCache get(ServerLevel level) {
		ServerLevel server = level.getServer().overworld();
		return server.getDataStorage().computeIfAbsent(TeleporterCache.factory());
	}

	public static SavedDataType<TeleporterCache> factory() {
		return new SavedDataType<>(TwilightForestMod.prefix("teleporter_cache"), TeleporterCache::new, TeleporterCache.CODEC, DataFixTypes.SAVED_DATA_MAP_DATA);
	}

	public void addBlockToCache(ResourceKey<Level> dimension, ColumnPos columnPos, TFTeleporter.PortalPosition position) {
		this.destinationCoordinateCache.putIfAbsent(dimension, Maps.newHashMapWithExpectedSize(4096));
		this.destinationCoordinateCache.get(dimension).put(columnPos, position);
		this.setDirty();
	}

	@Nullable
	public TFTeleporter.PortalPosition getPortalPosition(Identifier dimension, ColumnPos pos) {
		if (this.destinationCoordinateCache.containsKey(dimension)) {
			return this.destinationCoordinateCache.get(dimension).get(pos);
		}
		return null;
	}

	public void removeInvalidPos(Identifier dimension, ColumnPos pos) {
		this.destinationCoordinateCache.get(dimension).remove(pos);
		this.setDirty();
	}

	private CompoundTag saveData() {
		CompoundTag tag = new CompoundTag();
		ListTag dcc = new ListTag();
		this.destinationCoordinateCache.forEach((rl, map) -> {
			CompoundTag ct = new CompoundTag();
			ListTag links = new ListTag();
			map.forEach((columnPos, portalPos) -> {
				CompoundTag link = new CompoundTag();
				CompoundTag column = new CompoundTag();
				column.putInt("x", columnPos.x());
				column.putInt("z", columnPos.z());
				link.put("column", column);
				CompoundTag portal = new CompoundTag();
				portal.putLong("time", portalPos.lastUpdateTime);
				portal.putLong("pos", portalPos.pos.asLong());
				link.put("portal", portal);
				links.add(link);
			});
			ct.put("links", links);
			ct.putString("name", rl.identifier().toString());
			dcc.add(ct);
		});
		tag.put("dest", dcc);
		return tag;
	}

	private static TeleporterCache loadData(CompoundTag tag) {
		TeleporterCache cache = new TeleporterCache();
		ListTag destList = tag.getList("dest").orElse(new ListTag());
		for (int i = 0; i < destList.size(); i++) {
			CompoundTag dest = destList.getCompound(i).orElse(new CompoundTag());
			String nameStr = dest.getString("name").orElse("");
			Identifier name = Identifier.parse(nameStr);
			ResourceKey<Level> dimensionKey = ResourceKey.create(Registries.DIMENSION, name);
			cache.destinationCoordinateCache.putIfAbsent(dimensionKey, Maps.newHashMapWithExpectedSize(4096));
			ListTag links = dest.getList("links").orElse(new ListTag());
			for (int j = 0; j < links.size(); j++) {
				CompoundTag link = links.getCompound(j).orElse(new CompoundTag());
				CompoundTag column = link.getCompoundOrEmpty("column");
				CompoundTag portal = link.getCompoundOrEmpty("portal");
				int x = column.getInt("x").orElse(0);
				int z = column.getInt("z").orElse(0);
				long pos = portal.getLong("pos").orElse(0L);
				long time = portal.getLong("time").orElse(0L);
				cache.destinationCoordinateCache.get(dimensionKey).put(new ColumnPos(x, z), new TFTeleporter.PortalPosition(BlockPos.of(pos), time));
			}
		}
		return cache;
	}

	public static final Codec<TeleporterCache> CODEC = new Codec<>() {
		@Override
		public <T> DataResult<T> encode(TeleporterCache cache, DynamicOps<T> ops, T prefix) {
			return CompoundTag.CODEC.encode(cache.saveData(), ops, prefix);
		}

		@Override
		public <T> DataResult<Pair<TeleporterCache, T>> decode(DynamicOps<T> ops, T input) {
			return CompoundTag.CODEC.decode(ops, input).map(pair -> Pair.of(loadData(pair.getFirst()), pair.getSecond()));
		}
	};
}
