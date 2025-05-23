package twilightforest.world.components.structures.util;

import com.google.common.collect.Streams;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.MobSpawnSettings;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// FIXME Using IDs to enumerate lists of mob spawn tables is a bad idea... Using String for now in the config, will transition this implementation detail later
public interface ControlledSpawns {
	List<MobSpawnSettings.SpawnerData> getCombinedMonsterSpawnableList();

	List<MobSpawnSettings.SpawnerData> getCombinedCreatureSpawnableList();

	/**
	 * Returns a list of hostile monsters.  Are we ever going to need passive or water creatures?
	 */
	List<MobSpawnSettings.SpawnerData> getSpawnableList(MobCategory creatureType);

	/**
	 * Returns a list of hostile monsters in the specified indexed category
	 */
	List<MobSpawnSettings.SpawnerData> getSpawnableMonsterList(int index);

	record ControlledSpawningConfig(Map<String, List<MobSpawnSettings.SpawnerData>> spawnableMonsterLists, List<MobSpawnSettings.SpawnerData> ambientCreatureList, List<MobSpawnSettings.SpawnerData> waterCreatureList, List<MobSpawnSettings.SpawnerData> combinedMonsterSpawnableCache, List<MobSpawnSettings.SpawnerData> combinedCreatureSpawnableCache) {
		public static final MapCodec<ControlledSpawningConfig> FLAT_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
			Codec.unboundedMap(Codec.STRING, MobSpawnSettings.SpawnerData.CODEC.listOf()).fieldOf("labelled_monster_spawns").forGetter(ControlledSpawningConfig::spawnableMonsterLists),
			MobSpawnSettings.SpawnerData.CODEC.listOf().fieldOf("ambient_spawns").forGetter(ControlledSpawningConfig::ambientCreatureList),
			MobSpawnSettings.SpawnerData.CODEC.listOf().fieldOf("water_spawns").forGetter(ControlledSpawningConfig::waterCreatureList)
		).apply(instance, ControlledSpawningConfig::create));

		public static final ControlledSpawningConfig EMPTY = create(Map.of(), List.of(), List.of());

		@SuppressWarnings("unchecked")
		public static ControlledSpawningConfig firstIndexMonsters(MobSpawnSettings.SpawnerData... spawnableMonsterList) {
			return justMonsters(Arrays.asList(spawnableMonsterList));
		}

		@SuppressWarnings("unchecked")
		public static ControlledSpawningConfig justMonsters(List<MobSpawnSettings.SpawnerData>... spawnableMonsterLists) {
			return create(convertMonsterList(Arrays.asList(spawnableMonsterLists)), List.of(), List.of());
		}

		public static ControlledSpawningConfig create(List<List<MobSpawnSettings.SpawnerData>> spawnableMonsterLists, List<MobSpawnSettings.SpawnerData> ambientCreatureList, List<MobSpawnSettings.SpawnerData> waterCreatureList) {
			return create(convertMonsterList(spawnableMonsterLists), ambientCreatureList, waterCreatureList);
		}

		public static ControlledSpawningConfig create(Map<String, List<MobSpawnSettings.SpawnerData>> spawnableMonsterLists, List<MobSpawnSettings.SpawnerData> ambientCreatureList, List<MobSpawnSettings.SpawnerData> waterCreatureList) {
			return new ControlledSpawningConfig(
				spawnableMonsterLists,
				ambientCreatureList,
				waterCreatureList,
				spawnableMonsterLists.values().stream().flatMap(List::stream).toList(),
				Streams.concat(ambientCreatureList.stream(), waterCreatureList.stream()).toList()
			);
		}

		private static Map<String, List<MobSpawnSettings.SpawnerData>> convertMonsterList(List<List<MobSpawnSettings.SpawnerData>> lists) {
			int i = 0;
			Map<String, List<MobSpawnSettings.SpawnerData>> map = new HashMap<>();

			for (List<MobSpawnSettings.SpawnerData> list : lists) {
				map.put("" + i, list);
				i++;
			}

			return map;
		}

		public List<MobSpawnSettings.SpawnerData> getForLabel(String index) {
			return this.spawnableMonsterLists().getOrDefault(index, List.of());
		}
	}
}
