package twilightforest.block;

import net.minecraft.block.BlockState;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import twilightforest.TFConfig;

import javax.annotation.Nullable;

public class BlockTFGiantLeaves extends BlockTFGiantBlock {

	public BlockTFGiantLeaves(Properties props) {
		super(props);
	}

	@Override
	public int getOpacity(BlockState state, IBlockReader worldIn, BlockPos pos) {
		return TFConfig.COMMON_CONFIG.PERFORMANCE.leavesLightOpacity.get();
	}

	@Override
	public boolean canCreatureSpawn(BlockState state, IBlockReader world, BlockPos pos, EntitySpawnPlacementRegistry.PlacementType type, @Nullable EntityType<?> entityType) {
		return false;
	}
}
