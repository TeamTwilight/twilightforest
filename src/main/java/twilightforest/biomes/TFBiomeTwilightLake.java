package twilightforest.biomes;

import net.minecraft.entity.passive.EntitySquid;

public class TFBiomeTwilightLake extends TFBiomeBase {

	public TFBiomeTwilightLake(BiomeProperties props) {
		super(props);

		this.spawnableWaterCreatureList.add(new SpawnListEntry(EntitySquid.class, 10, 4, 4));
	}

}
