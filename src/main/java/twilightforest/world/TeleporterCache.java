package twilightforest.world;

import com.google.common.collect.Maps;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ColumnPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;
import net.minecraft.world.level.storage.SavedDataStorage;
import org.jetbrains.annotations.Nullable;
import twilightforest.TwilightForestMod;

import java.util.HashMap;
import java.util.Map;

public class TeleporterCache extends SavedData {

	// destinationCoordinateCache is (src -> dest) [DestWorld, [SrcPos, DestPos]]
	private final Map<ResourceKey<Level>, Map<ColumnPos, TFTeleporter.PortalPosition>> destinationCoordinateCache = new HashMap<>();

	public static final Codec<TeleporterCache> CODEC = CompoundTag.CODEC.xmap(TeleporterCache::load, cache -> cache.save(new CompoundTag()));
	public static final SavedDataType<TeleporterCache> FACTORY = new SavedDataType<>(TwilightForestMod.prefix("teleporter_cache"), TeleporterCache::new, CODEC, null);

	private TeleporterCache() {
		this.setDirty();
	}

	public static TeleporterCache get(ServerLevel level) {
		ServerLevel server = level.getServer().overworld();
		SavedDataStorage storage = server.getDataStorage();
		return storage.computeIfAbsent(TeleporterCache.FACTORY);
	}

	void addBlockToCache(ResourceKey<Level> dimension, ColumnPos columnPos, TFTeleporter.PortalPosition position) {
		this.destinationCoordinateCache.putIfAbsent(dimension, Maps.newHashMapWithExpectedSize(4096));
		this.destinationCoordinateCache.get(dimension).put(columnPos, position);
		this.setDirty();
	}

	@Nullable
	TFTeleporter.PortalPosition getPortalPosition(Identifier dimension, ColumnPos pos) {
		ResourceKey<Level> levelKey = ResourceKey.create(Registries.DIMENSION, dimension);

		if (this.destinationCoordinateCache.containsKey(levelKey)) {
			return this.destinationCoordinateCache.get(levelKey).get(pos);
		}
		return null;
	}

	public void removeInvalidPos(Identifier dimension, ColumnPos pos) {
		this.destinationCoordinateCache.get(ResourceKey.create(Registries.DIMENSION, dimension)).remove(pos);
		this.setDirty();
	}

	public CompoundTag save(CompoundTag tag) {
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
			ct.putString("name", rl.toString());
			dcc.add(ct);
		});
		tag.put("dest", dcc);
		return tag;
	}

	public static TeleporterCache load(CompoundTag tag) {
		TeleporterCache cache = new TeleporterCache();
		ListTag destList = tag.getListOrEmpty("dest");

		for (int i = 0; i < destList.size(); i++) {
			CompoundTag dest = destList.getCompoundOrEmpty(i);
			Identifier name = Identifier.parse(dest.getString("name").get());
			ResourceKey<Level> levelKey = ResourceKey.create(Registries.DIMENSION, name);

			cache.destinationCoordinateCache.putIfAbsent(levelKey, Maps.newHashMapWithExpectedSize(4096));

			ListTag linksList = dest.getListOrEmpty("links");

			for (int j = 0; j < linksList.size(); j++) {
				CompoundTag link = linksList.getCompound(j).get();
				CompoundTag column = link.getCompound("column").get();
				CompoundTag portal = link.getCompound("portal").get();
				ColumnPos columnPos = new ColumnPos(column.getInt("x").get(), column.getInt("z").get());

				cache.destinationCoordinateCache.get(levelKey).put(columnPos, new TFTeleporter.PortalPosition(BlockPos.of(portal.getLong("pos").get()), portal.getLong("time").get()));
			}
		}
		return cache;
	}
}
