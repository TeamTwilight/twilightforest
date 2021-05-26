package twilightforest.block;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.MobEntity;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import twilightforest.data.BlockTagGenerator;
import twilightforest.data.FluidTagGenerator;
import twilightforest.enums.FireJetVariant;
import twilightforest.tileentity.TileEntityTFFlameJet;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockTFFireJet extends Block {

	public static final EnumProperty<FireJetVariant> STATE = EnumProperty.create("state", FireJetVariant.class);

	protected BlockTFFireJet(Properties props) {
		super(props);
		setDefaultState(getDefaultState().with(STATE, FireJetVariant.IDLE));
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		super.fillStateContainer(builder);
		builder.add(STATE);
	}

	@Nullable
	@Override
	public PathNodeType getAiPathNodeType(BlockState state, IBlockReader world, BlockPos pos, @Nullable MobEntity entity) {
		return state.get(STATE) == FireJetVariant.IDLE ? null : PathNodeType.DAMAGE_FIRE;
	}

	@Override
	@Deprecated
	public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		if (!world.isRemote && state.get(STATE) == FireJetVariant.IDLE) {
			BlockPos lavaPos = findLavaAround(world, pos.down());

			if (isLava(world, lavaPos)) {
				world.setBlockState(lavaPos, Blocks.AIR.getDefaultState());
				world.setBlockState(pos, state.with(STATE, FireJetVariant.POPPING));
			}
		}
	}

	/**
	 * Find a full block of lava near the designated block.  This is intentionally not really that reliable.
	 */
	private BlockPos findLavaAround(World world, BlockPos pos) {
		if (isLava(world, pos)) {
			return pos;
		}

		for (int i = 0; i < 3; i++) {
			BlockPos randPos = pos.add(world.rand.nextInt(3) - 1, 0, world.rand.nextInt(3) - 1);
			if (isLava(world, randPos)) {
				return randPos;
			}
		}

		return pos;
	}

	private boolean isLava(World world, BlockPos pos) {
		BlockState state = world.getBlockState(pos);
		Block b = state.getBlock();
		return b.isIn(BlockTagGenerator.FIRE_JET_FUEL) || b.getFluidState(state).isTagged(FluidTagGenerator.FIRE_JET_FUEL);
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return state.get(STATE) == FireJetVariant.POPPING || state.get(STATE) == FireJetVariant.FLAME;
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return hasTileEntity(state) ? new TileEntityTFFlameJet() : null;
	}
}
