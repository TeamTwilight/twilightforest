package twilightforest.worldgen.biomes;

public class TFBiomeMushrooms extends TFBiomeBase {

	public TFBiomeMushrooms(Builder props) {
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
		TFBiomeDecorator.addCanopyAlt(this, 0.2F);
		TFBiomeDecorator.addHugeMushrooms(this, 2);
		TFBiomeDecorator.addMultipleTrees(this, TFBiomeDecorator.NORMAL_TREES_CONFIG, 8);
		TFBiomeDecorator.addGrassWithFern(this, 2);
		TFBiomeDecorator.addFlowers(this, 2);
		TFBiomeDecorator.addMushroomsSometimes(this);
	}
}
