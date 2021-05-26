package twilightforest.world.feature;

import com.mojang.serialization.Codec;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import twilightforest.util.FeatureUtil;
import twilightforest.world.feature.config.CaveStalactiteConfig;

import java.util.Random;

/**
 * Makes a Stalagmite suitable for outside appearances.
 *
 * @author Ben
 */
public class TFGenOutsideStalagmite extends TFGenCaveStalactite {

	public TFGenOutsideStalagmite(Codec<CaveStalactiteConfig> configIn) {
		super(configIn);
	}

	@Override
	public boolean generate(ISeedReader world, ChunkGenerator generator, Random rand, BlockPos pos, CaveStalactiteConfig config) {
		int length = rand.nextInt(10) + 5;

		if (!FeatureUtil.isAreaSuitable(world, pos, 1, length, 1)) {
			return false;
		}

		// I think we already have code for this! :D
		return makeSpike(world, rand, pos.down(), length, config);
	}
}
