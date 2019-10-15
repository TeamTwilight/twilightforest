package twilightforest.biomes;

import net.minecraft.entity.passive.EntitySquid;
import twilightforest.TFFeature;

public class TFBiomeTwilightLake extends TFBiomeBase {

	public TFBiomeTwilightLake(BiomeProperties props) {
		super(props);
		this.spawnableWaterCreatureList.add(new SpawnListEntry(EntitySquid.class, 10, 4, 4));
	}

	@Override
	protected TFFeature getContainedFeature() {
		return TFFeature.QUEST_ISLAND;
	}
}
