package twilightforest.block;

import com.google.common.collect.ImmutableMap;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.particles.RedstoneParticleData;
import net.minecraft.state.BooleanProperty;
import net.minecraft.block.BlockState;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Map;
import java.util.Random;

public class BlockTFTrollSteinn extends Block {

	private static final BooleanProperty DOWN_LIT  = BooleanProperty.create("down");
	private static final BooleanProperty UP_LIT    = BooleanProperty.create("up");
	private static final BooleanProperty NORTH_LIT = BooleanProperty.create("north");
	private static final BooleanProperty SOUTH_LIT = BooleanProperty.create("south");
	private static final BooleanProperty WEST_LIT  = BooleanProperty.create("west");
	private static final BooleanProperty EAST_LIT  = BooleanProperty.create("east");
	private static final Map<Direction, BooleanProperty> PROPERTY_MAP = ImmutableMap.<Direction, BooleanProperty>builder()
					.put(Direction.DOWN, DOWN_LIT)
					.put(Direction.UP, UP_LIT)
					.put(Direction.NORTH, NORTH_LIT)
					.put(Direction.SOUTH, SOUTH_LIT)
					.put(Direction.WEST, WEST_LIT)
					.put(Direction.EAST, EAST_LIT).build();

	private static final int LIGHT_THRESHHOLD = 7;

	BlockTFTrollSteinn(Properties props) {
		super(props);

		this.setDefaultState(stateContainer.getBaseState()
				.with(DOWN_LIT, false).with(UP_LIT, false)
				.with(NORTH_LIT, false).with(SOUTH_LIT, false)
				.with(WEST_LIT, false).with(EAST_LIT, false));
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		super.fillStateContainer(builder);
		builder.add(DOWN_LIT, UP_LIT, NORTH_LIT, SOUTH_LIT, WEST_LIT, EAST_LIT);
	}

	@Override
	public BlockState updatePostPlacement(BlockState state, Direction dirToNeighbor, BlockState neighborState, IWorld world, BlockPos pos, BlockPos neighborPos) {
		boolean lit = world.getLight(neighborPos) > LIGHT_THRESHHOLD;
		return state.with(PROPERTY_MAP.get(dirToNeighbor), lit);
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext ctx) {
		BlockState ret = getDefaultState();
		for (Map.Entry<Direction, BooleanProperty> e : PROPERTY_MAP.entrySet()) {
			int light = ctx.getWorld().getLight(ctx.getPos().offset(e.getKey()));
			ret = ret.with(e.getValue(), light > LIGHT_THRESHHOLD);
		}
		return ret;
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void animateTick(BlockState state, World world, BlockPos pos, Random rand) {
		if (rand.nextInt(2) == 0) {
			this.sparkle(world, pos);
		}
	}

	// [VanillaCopy] Based on BlockRedstoneOre.spawnParticles
	private void sparkle(World world, BlockPos pos) {
		Random random = world.rand;
		int threshhold = LIGHT_THRESHHOLD;

		for (Direction side : Direction.values()) {
			double rx = pos.getX() + random.nextFloat();
			double ry = pos.getY() + random.nextFloat();
			double rz = pos.getZ() + random.nextFloat();

			if (side == Direction.DOWN && !world.getBlockState(pos.down()).isOpaqueCube(world, pos) && world.getLight(pos.down()) <= threshhold) {
				ry = pos.getY() - 0.0625D;
			}

			if (side == Direction.UP && !world.getBlockState(pos.up()).isOpaqueCube(world, pos) && world.getLight(pos.up()) <= threshhold) {
				ry = pos.getY() + 0.0625D + 1.0D;
			}

			if (side == Direction.NORTH && !world.getBlockState(pos.north()).isOpaqueCube(world, pos) && world.getLight(pos.north()) <= threshhold) {
				rz = pos.getZ() - 0.0625D;
			}

			if (side == Direction.SOUTH && !world.getBlockState(pos.south()).isOpaqueCube(world, pos) && world.getLight(pos.south()) <= threshhold) {
				rz = pos.getZ() + 0.0625D + 1.0D;
			}

			if (side == Direction.WEST && !world.getBlockState(pos.west()).isOpaqueCube(world, pos) && world.getLight(pos.west()) <= threshhold) {
				rx = pos.getX() - 0.0625D;
			}

			if (side == Direction.EAST && !world.getBlockState(pos.east()).isOpaqueCube(world, pos) && world.getLight(pos.east()) <= threshhold) {
				rx = pos.getX() + 0.0625D + 1.0D;
			}

			if (rx < pos.getX() || rx > pos.getX() + 1 || ry < 0.0D || ry > pos.getY() + 1 || rz < pos.getZ() || rz > pos.getZ() + 1) {
				world.addParticle(new RedstoneParticleData(0.0F, random.nextFloat(), 1.0F, 1.0F), rx, ry, rz, 0.25D, -1.0D, 0.5D);
			}
		}
	}
}
