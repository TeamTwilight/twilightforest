package twilightforest.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;

public class BlockTFReappearTranslucent extends BlockTFTowerTranslucent {

	public static final BooleanProperty ACTIVE = BooleanProperty.create("active");
	private static final VoxelShape REAPPEARING_BB = VoxelShapes.create(new AxisAlignedBB(0.375F, 0.375F, 0.375F, 0.625F, 0.625F, 0.625F));

	public BlockTFReappearTranslucent() {
		super(Properties.create(Material.GLASS).hardnessAndResistance(50.0F, 2000.0F).sound(SoundType.METAL).tickRandomly().doesNotBlockMovement());
		this.setDefaultState(stateContainer.getBaseState().with(ACTIVE, false));
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		super.fillStateContainer(builder);
		builder.add(ACTIVE);
	}

	@Override
	@Deprecated
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return REAPPEARING_BB;
	}

	@Override
	@Deprecated
	public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		if (world.isRemote) return;

		if (state.get(ACTIVE)) {
			world.setBlockState(pos, TFBlocks.reappearing_translucent.get().getDefaultState().with(ACTIVE, false));
			//world.notifyNeighborsRespectDebug(pos, this, false);
			world.playSound(null, pos, SoundEvents.BLOCK_WOODEN_BUTTON_CLICK_OFF, SoundCategory.BLOCKS, 0.3F, 0.5F);
			//world.markBlockRangeForRenderUpdate(x, y, z, x, y, z);

		} else if (!state.get(ACTIVE)) {
			BlockTFReappearingBlock.changeToActiveVanishBlock(world, pos, TFBlocks.reappearing_translucent.get().getDefaultState().with(ACTIVE, true));
		}
	}
}
