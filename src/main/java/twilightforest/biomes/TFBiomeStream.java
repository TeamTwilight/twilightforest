package twilightforest.biomes;

public class TFBiomeStream extends TFBiomeBase {

	public TFBiomeStream(BiomeProperties props) {
		super(props);
		getTFBiomeDecorator().setWaterlilyPerChunk(2);
		spawnableCreatureList.clear();
	}

}
