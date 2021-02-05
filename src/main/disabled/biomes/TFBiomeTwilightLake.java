package twilightforest.worldgen.biomes;

import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;

public class TFBiomeTwilightLake extends TFBiomeBase {

	public TFBiomeTwilightLake(Builder props) {
		super(props);
	}

	@Override
	public void addFeatures() {
		super.addFeatures();

		TFBiomeDecorator.addOres(this);
		TFBiomeDecorator.addLakes(this);
		TFBiomeDecorator.addSprings(this);
		TFBiomeDecorator.addTorchberries(this);
		TFBiomeDecorator.addMushrooms(this);
		TFBiomeDecorator.addReeds(this, 1);
	}

	@Override
	public void addSpawns() {
		super.addSpawns();

		addSpawn(EntityClassification.WATER_CREATURE, new SpawnListEntry(EntityType.SQUID, 10, 4, 4));
	}

	//	@Override
//	protected TFFeature getContainedFeature() {
//		return TFFeature.QUEST_ISLAND;
//	}
}
