package twilightforest.worldgen.biomes;

import twilightforest.block.TFBlocks;

public class TFBiomeFireflyForest extends TFBiomeBase {

	public TFBiomeFireflyForest(Builder props) {
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
		TFBiomeDecorator.addCanopy(this);
		TFBiomeDecorator.addMultipleTrees(this, TFBiomeDecorator.NORMAL_TREES_CONFIG, 2);
		TFBiomeDecorator.addGrassWithFern(this, 1);
		TFBiomeDecorator.addFlowers(this, 4);
		TFBiomeDecorator.addTallFlowers(this);
		TFBiomeDecorator.addMushgloom(this, 24);
		TFBiomeDecorator.addMushrooms(this);
		TFBiomeDecorator.addHangingLamps(this);
		TFBiomeDecorator.addLamppost(this, TFBlocks.firefly_jar.get().getDefaultState(), 4);
		TFBiomeDecorator.addPumpkins(this, 32);
	}
}
