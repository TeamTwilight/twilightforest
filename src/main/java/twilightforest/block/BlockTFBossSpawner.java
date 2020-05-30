package twilightforest.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
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

	public static final EnumProperty<BossVariant> VARIANT = EnumProperty.create("boss", BossVariant.class);

	protected BlockTFBossSpawner(Block.Properties props) {
		super(props);
		this.setDefaultState(stateContainer.getBaseState().with(VARIANT, BossVariant.NAGA));
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		super.fillStateContainer(builder);
		builder.add(VARIANT);
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return state.get(VARIANT).hasSpawner();
	}

	@Override
	@Nullable
	public TileEntity createTileEntity(BlockState state, IBlockReader reader) {
		return state.get(VARIANT).getSpawner();
	}

	@Override
	public boolean canEntityDestroy(BlockState state, IBlockReader world, BlockPos pos, Entity entity) {
		return blockHardness >= 0f;
	}

	//TODO: Find out what this does now
//	@Override
//	@Deprecated
//	public boolean isSolid(BlockState state) {
//		return false;
//	}

	//TODO: Move to client
//	@OnlyIn(Dist.CLIENT)
//	@Override
//	public BlockRenderLayer getRenderLayer() {
//		return BlockRenderLayer.CUTOUT;
//	}
}
