package twilightforest.biomes;

import net.minecraft.util.math.BlockPos;

public class TFBiomeDarkForestCenter extends TFBiomeDarkForest {

	public TFBiomeDarkForestCenter(BiomeProperties props) {
		super(props);
	}

	@Override
	public int getGrassColorAtPos(BlockPos pos) {
		double d0 = GRASS_COLOR_NOISE.getValue(pos.getX() * 0.0225D, pos.getZ() * 0.0225D);
		return d0 < -0.2D ? 0x667540 : 0x554114;
	}

	@Override
	public int getFoliageColorAtPos(BlockPos pos) {
		double d0 = GRASS_COLOR_NOISE.getValue(pos.getX() * 0.0225D, pos.getZ() * 0.0225D);
		return d0 < -0.1D ? 0xf9821e : 0xe94e14;
	}
}
