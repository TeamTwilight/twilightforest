package twilightforest.biomes;

public class TFBiomeMushrooms extends TFBiomeBase {

	public TFBiomeMushrooms(BiomeProperties props) {
		super(props);

		getTFBiomeDecorator().setTreesPerChunk(8);
		getTFBiomeDecorator().setMushroomsPerChunk(8);
		getTFBiomeDecorator().setBigMushroomsPerChunk(2);
		getTFBiomeDecorator().alternateCanopyChance = 0.2F;
	}
}
