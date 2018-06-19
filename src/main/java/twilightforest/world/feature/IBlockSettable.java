package twilightforest.world.feature;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

// Shim interface to expose the WorldGenerator's setBlock method
// Mainly so TFGenerator's util methods don't have to be duplicated between the TFGenerator subclasses and the
// tree generators
// todo 1.9 improve this?
public interface IBlockSettable {
	// [VanillaCopy] pin to signature of WorldGenerator.setBlockAndNotifyAdequately
	// But cannot have exact same name as the subclass methods will get reobf-ed but the interface one won't
	void setBlockAndNotify(World world, BlockPos pos, IBlockState state);
}
