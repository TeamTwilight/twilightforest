package twilightforest.world.feature;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

/**
 * Used to generate no particular tree
 */
public class TFGenNoTree extends TFTreeGenerator {

	public TFGenNoTree() {
	}

	public TFGenNoTree(boolean notify) {
		super(notify);
	}

	@Override
	public boolean generate(World world, Random random, BlockPos pos) {
		return false;
	}

}
