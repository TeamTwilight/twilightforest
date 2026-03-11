package twilightforest.world.components.structures.util;

import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.MobSpawnSettings;

import java.util.List;

public interface ConfigurableSpawns extends ControlledSpawns {
	ControlledSpawningConfig getConfig();

	@Override
	default List<MobSpawnSettings.SpawnerData> getCombinedMonsterSpawnableList() {
		return this.getConfig().combinedMonsterSpawnableCache();
	}

	@Override
	default List<MobSpawnSettings.SpawnerData> getCombinedCreatureSpawnableList() {
		return this.getConfig().combinedCreatureSpawnableCache();
	}

	@Override
	default List<MobSpawnSettings.SpawnerData> getSpawnableList(MobCategory creatureType) {
		return switch (creatureType) {
			case MONSTER -> this.getSpawnableMonsterList(0);
			case AMBIENT -> this.getConfig().ambientCreatureList();
			case WATER_CREATURE -> this.getConfig().waterCreatureList();
			default -> List.of();
		};
	}

	@Override
	default List<MobSpawnSettings.SpawnerData> getSpawnableMonsterList(int index) {
		return this.getConfig().getForLabel("" + index);
	}
}
