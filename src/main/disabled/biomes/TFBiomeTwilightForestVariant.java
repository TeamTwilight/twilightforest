package twilightforest.worldgen.biomes;

public class TFBiomeTwilightForestVariant extends TFBiomeBase {

	public TFBiomeTwilightForestVariant(Builder props) {
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
		TFBiomeDecorator.addMultipleTrees(this, TFBiomeDecorator.VARIANT_TREES_CONFIG, 25);
		TFBiomeDecorator.addFlowers(this, 8);
		TFBiomeDecorator.addTwilightGrass(this, TFBiomeDecorator.TWILIGHT_VARIANT_GRASS_CONFIG, 15);
		TFBiomeDecorator.addTallFerns(this, 7);
		TFBiomeDecorator.addMushrooms(this);
	}
}
