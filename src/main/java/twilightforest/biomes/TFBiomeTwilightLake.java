package twilightforest.biomes;

import twilightforest.TFFeature;

public class TFBiomeTwilightLake extends TFBiomeBase {

	public TFBiomeTwilightLake(Builder props) {
		super(props);
		this.spawnableWaterCreatureList.add(new SpawnListEntry(EntitySquid.class, 10, 4, 4));
	}

	@Override
	protected TFFeature getContainedFeature() {
		return TFFeature.QUEST_ISLAND;
	}
}
