package twilightforest.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import twilightforest.enums.BossVariant;

import javax.annotation.Nullable;

public class BlockTFBossSpawner extends Block {

	private final BossVariant boss;

	protected BlockTFBossSpawner(Block.Properties props, BossVariant variant) {
		super(props);
		boss = variant;
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return boss.hasSpawner();
	}

	@Override
	@Nullable
	public TileEntity createTileEntity(BlockState state, IBlockReader reader) {
		return boss.getSpawner();
	}

	@Override
	public boolean canEntityDestroy(BlockState state, IBlockReader world, BlockPos pos, Entity entity) {
		return state.getBlockHardness(world, pos) >= 0f;
	}
}
