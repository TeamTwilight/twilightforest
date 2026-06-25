package twilightforest.world.components.structures.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.MobSpawnSettings;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public interface ControlledSpawns {

	String CODEC_NAME = "controlled_spawns";

	/**
	 * Returns a list of hostile monsters.  Are we ever going to need passive or water creatures?
	 */
	WeightedList<MobSpawnSettings.SpawnerData> getSpawnableList(MobCategory creatureType);

	/**
	 * Returns a list of hostile monsters in the specified indexed category
	 */
	WeightedList<MobSpawnSettings.SpawnerData> getSpawnableMonsterList(int index);

	record ControlledSpawningConfig(Map<String, WeightedList<MobSpawnSettings.SpawnerData>> spawnableMonsterLists, WeightedList<MobSpawnSettings.SpawnerData> ambientCreatureList, WeightedList<MobSpawnSettings.SpawnerData> waterCreatureList, WeightedList<MobSpawnSettings.SpawnerData> combinedMonsterSpawnableCache, WeightedList<MobSpawnSettings.SpawnerData> combinedCreatureSpawnableCache) {
		public static final Codec<ControlledSpawningConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Codec.unboundedMap(Codec.STRING, WeightedList.codec(MobSpawnSettings.SpawnerData.CODEC)).fieldOf("labelled_monster_spawns").forGetter(ControlledSpawningConfig::spawnableMonsterLists),
			WeightedList.codec(MobSpawnSettings.SpawnerData.CODEC).fieldOf("ambient_spawns").forGetter(ControlledSpawningConfig::ambientCreatureList),
			WeightedList.codec(MobSpawnSettings.SpawnerData.CODEC).fieldOf("water_spawns").forGetter(ControlledSpawningConfig::waterCreatureList)
		).apply(instance, ControlledSpawningConfig::create));

		public static final ControlledSpawningConfig EMPTY = create(Map.of(), WeightedList.of(), WeightedList.of());

		@SuppressWarnings("unchecked")
		public static ControlledSpawningConfig firstIndexMonsters(WeightedList<MobSpawnSettings.SpawnerData> spawnableMonsterList) {
			return justMonsters(spawnableMonsterList);
		}

		@SuppressWarnings("unchecked")
		public static ControlledSpawningConfig justMonsters(WeightedList<MobSpawnSettings.SpawnerData>... spawnableMonsterLists) {
			return create(convertMonsterList(Arrays.asList(spawnableMonsterLists)), WeightedList.of(), WeightedList.of());
		}

		public static ControlledSpawningConfig create(List<WeightedList<MobSpawnSettings.SpawnerData>> spawnableMonsterLists, WeightedList<MobSpawnSettings.SpawnerData> ambientCreatureList, WeightedList<MobSpawnSettings.SpawnerData> waterCreatureList) {
			return create(convertMonsterList(spawnableMonsterLists), ambientCreatureList, waterCreatureList);
		}

		public static ControlledSpawningConfig create(Map<String, WeightedList<MobSpawnSettings.SpawnerData>> spawnableMonsterLists, WeightedList<MobSpawnSettings.SpawnerData> ambientCreatureList, WeightedList<MobSpawnSettings.SpawnerData> waterCreatureList) {
			WeightedList.Builder<MobSpawnSettings.SpawnerData> combinedMonsters = WeightedList.builder();
			spawnableMonsterLists.values().forEach(combinedMonsters::addAll);

			WeightedList.Builder<MobSpawnSettings.SpawnerData> combinedCreatures = WeightedList.builder();
			combinedCreatures.addAll(ambientCreatureList);
			combinedCreatures.addAll(waterCreatureList);

			return new ControlledSpawningConfig(
				spawnableMonsterLists,
				ambientCreatureList,
				waterCreatureList,
				combinedMonsters.build(),
				combinedCreatures.build()
			);
		}

		private static Map<String, WeightedList<MobSpawnSettings.SpawnerData>> convertMonsterList(List<WeightedList<MobSpawnSettings.SpawnerData>> lists) {
			int i = 0;
			Map<String, WeightedList<MobSpawnSettings.SpawnerData>> map = new LinkedHashMap<>();

			for (WeightedList<MobSpawnSettings.SpawnerData> list : lists) {
				map.put(String.valueOf(i), list);
				i++;
			}

			return map;
		}

		public WeightedList<MobSpawnSettings.SpawnerData> getForLabel(String index) {
			return this.spawnableMonsterLists().getOrDefault(index, WeightedList.of());
		}
	}
}
