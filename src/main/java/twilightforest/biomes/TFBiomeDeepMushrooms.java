package twilightforest.biomes;


public class TFBiomeDeepMushrooms extends TFBiomeBase {

	public TFBiomeDeepMushrooms(BiomeProperties props) {
		super(props);

		getTFBiomeDecorator().setTreesPerChunk(1);

		getTFBiomeDecorator().setMushroomsPerChunk(12);
		getTFBiomeDecorator().setBigMushroomsPerChunk(8);

		getTFBiomeDecorator().myceliumPerChunk = 3;
		getTFBiomeDecorator().alternateCanopyChance = 0.9F;

	}

}
