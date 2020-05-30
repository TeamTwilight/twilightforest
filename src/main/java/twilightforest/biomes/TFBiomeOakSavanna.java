package twilightforest.biomes;

public class TFBiomeOakSavanna extends TFBiomeBase {

	public TFBiomeOakSavanna(Builder props) {
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
		TFBiomeDecorator.addCanopySavannah(this);
		TFBiomeDecorator.addMultipleTrees(this, TFBiomeDecorator.SAVANNAH_TREES_CONFIG, 1);
		TFBiomeDecorator.addPlantRoots(this);
		TFBiomeDecorator.addTorchberries(this);
		TFBiomeDecorator.addFlowers(this, 4);
		TFBiomeDecorator.addTwilightGrass(this, TFBiomeDecorator.SAVANNAH_GRASS_CONFIG, 20);
		TFBiomeDecorator.addTallGrass(this, 7);
		TFBiomeDecorator.addMushrooms(this);
	}
}
