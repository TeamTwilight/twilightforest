package twilightforest.util;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntityUtil {

	public static boolean canDestroyBlock(World world, BlockPos pos, Entity entity) {
		return canDestroyBlock(world, pos, world.getBlockState(pos), entity);
	}

	public static boolean canDestroyBlock(World world, BlockPos pos, IBlockState state, Entity entity) {
		float hardness = state.getBlockHardness(world, pos);
		return hardness >= 0f && hardness < 50f && !state.getBlock().isAir(state, world, pos)
				&& state.getBlock().canEntityDestroy(state, world, pos, entity);
	}
}
