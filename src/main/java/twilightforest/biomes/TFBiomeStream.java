package twilightforest.biomes;

public class TFBiomeStream extends TFBiomeBase {

	public TFBiomeStream(Builder props) {
		super(props);
		getTFBiomeDecorator().setWaterlilyPerChunk(2);
		spawnableCreatureList.clear();
	}
}
