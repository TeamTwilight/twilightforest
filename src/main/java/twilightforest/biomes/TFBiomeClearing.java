package twilightforest.biomes;

import java.util.Random;

public class TFBiomeClearing extends TFBiomeBase {

	public TFBiomeClearing(Builder props) {
		super(props);

		getTFBiomeDecorator().hasCanopy = false;
		getTFBiomeDecorator().setTreesPerChunk(-999);

		getTFBiomeDecorator().setFlowersPerChunk(4);
		getTFBiomeDecorator().setGrassPerChunk(10);
	}

	//TODO: Move to feature decoration
	@Override
	public WorldGenerator getRandomWorldGenForGrass(Random random) {
		return new WorldGenTallGrass(BlockTallGrass.EnumType.GRASS);
	}
}
