package twilightforest.biomes;

public class TFBiomeClearing extends TFBiomeBase {

	public TFBiomeClearing(Builder props) {
		super(props);
	}

	@Override
	public void addFeatures() {
		super.addFeatures();

		TFBiomeDecorator.addClayDisks(this, 1);
		TFBiomeDecorator.addWoodRoots(this);
		TFBiomeDecorator.addOres(this);
		TFBiomeDecorator.addPlantRoots(this);
		TFBiomeDecorator.addLakes(this);
		TFBiomeDecorator.addRuins(this);
		TFBiomeDecorator.addSprings(this);
		TFBiomeDecorator.addTorchberries(this);
		TFBiomeDecorator.addGrass(this, 10);
		TFBiomeDecorator.addFlowers(this, 4);
		TFBiomeDecorator.addMushrooms(this);
	}
}
