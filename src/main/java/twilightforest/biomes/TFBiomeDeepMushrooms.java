package twilightforest.biomes;

import twilightforest.TFFeature;

public class TFBiomeDeepMushrooms extends TFBiomeBase {

	public TFBiomeDeepMushrooms(Builder props) {
		super(props);
	}

	@Override
	public void addFeatures() {
		super.addFeatures();

		TFBiomeDecorator.addWoodRoots(this);
		TFBiomeDecorator.addOres(this);
		TFBiomeDecorator.addClayDisks(this, 1);
		TFBiomeDecorator.addLakes(this);
		TFBiomeDecorator.addRuins(this);
		TFBiomeDecorator.addSprings(this);
		TFBiomeDecorator.addPlantRoots(this);
		TFBiomeDecorator.addTorchberries(this);
		TFBiomeDecorator.addCanopyAlt(this, 0.9F);
		TFBiomeDecorator.addHugeMushrooms(this, 8);
		TFBiomeDecorator.addMultipleTrees(this, TFBiomeDecorator.NORMAL_TREES_CONFIG, 1);
		TFBiomeDecorator.addMyceliumBlobs(this, 3);
		TFBiomeDecorator.addGrassWithFern(this, 2);
		TFBiomeDecorator.addFlowers(this, 2);
		TFBiomeDecorator.addMushroomsCommon(this);
	}

	//	@Override
//	protected TFFeature getContainedFeature() {
//		return TFFeature.MUSHROOM_TOWER;
//	}
}
