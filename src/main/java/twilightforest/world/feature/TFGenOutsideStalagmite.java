package twilightforest.world.feature;

import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;


/**
 * Makes a Stalagmite suitable for outside appearances.
 *
 * @author Ben
 */
public class TFGenOutsideStalagmite extends TFGenCaveStalactite {

	public TFGenOutsideStalagmite() {
		super(Blocks.STONE, 1.0F, false);
	}

	@Override
	public boolean generate(World world, Random rand, BlockPos pos) {
		int length = rand.nextInt(10) + 5;

		if (!isAreaSuitable(world, rand, pos, 1, length, 1)) {
			return false;
		}

		// I think we already have code for this! :D
		return makeSpike(world, rand, pos.down(), length);
	}

}
