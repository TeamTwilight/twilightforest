package twilightforest.world.components.structures.util;

import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.util.random.WeightedList;

public interface ConfigurableSpawns extends ControlledSpawns {
	ControlledSpawningConfig getConfig();

	@Override
	default WeightedList<MobSpawnSettings.SpawnerData> getSpawnableList(MobCategory creatureType) {
		return switch (creatureType) {
			case MONSTER -> this.getSpawnableMonsterList(0);
			case AMBIENT -> this.getConfig().ambientCreatureList();
			case WATER_CREATURE -> this.getConfig().waterCreatureList();
			default -> WeightedList.of();
		};
	}

	@Override
	default WeightedList<MobSpawnSettings.SpawnerData> getSpawnableMonsterList(int index) {
		return this.getConfig().getForLabel(String.valueOf(index));
	}
}
