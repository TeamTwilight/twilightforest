package twilightforest.block;

import com.google.common.collect.ImmutableMap;
import com.mojang.math.Vector3f;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Map;
import java.util.Random;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class TrollsteinnBlock extends Block {

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

	public TrollsteinnBlock(Properties props) {
		super(props);

		this.registerDefaultState(stateDefinition.any()
				.setValue(DOWN_LIT, false).setValue(UP_LIT, false)
				.setValue(NORTH_LIT, false).setValue(SOUTH_LIT, false)
				.setValue(WEST_LIT, false).setValue(EAST_LIT, false));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(DOWN_LIT, UP_LIT, NORTH_LIT, SOUTH_LIT, WEST_LIT, EAST_LIT);
	}

	@Override
	public BlockState updateShape(BlockState state, Direction dirToNeighbor, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
		boolean lit = world.getMaxLocalRawBrightness(neighborPos) > LIGHT_THRESHHOLD;
		return state.setValue(PROPERTY_MAP.get(dirToNeighbor), lit);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		BlockState ret = defaultBlockState();
		for (Map.Entry<Direction, BooleanProperty> e : PROPERTY_MAP.entrySet()) {
			int light = ctx.getLevel().getMaxLocalRawBrightness(ctx.getClickedPos().relative(e.getKey()));
			ret = ret.setValue(e.getValue(), light > LIGHT_THRESHHOLD);
		}
		return ret;
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void animateTick(BlockState state, Level world, BlockPos pos, Random rand) {
		if (rand.nextInt(2) == 0) {
			this.sparkle(world, pos);
		}
	}

	// [VanillaCopy] Based on BlockRedstoneOre.spawnParticles
	private void sparkle(Level world, BlockPos pos) {
		Random random = world.random;
		int threshhold = LIGHT_THRESHHOLD;

		for (Direction side : Direction.values()) {
			double rx = pos.getX() + random.nextFloat();
			double ry = pos.getY() + random.nextFloat();
			double rz = pos.getZ() + random.nextFloat();

			if (side == Direction.DOWN && !world.getBlockState(pos.below()).isSolidRender(world, pos) && world.getMaxLocalRawBrightness(pos.below()) <= threshhold) {
				ry = pos.getY() - 0.0625D;
			}

			if (side == Direction.UP && !world.getBlockState(pos.above()).isSolidRender(world, pos) && world.getMaxLocalRawBrightness(pos.above()) <= threshhold) {
				ry = pos.getY() + 0.0625D + 1.0D;
			}

			if (side == Direction.NORTH && !world.getBlockState(pos.north()).isSolidRender(world, pos) && world.getMaxLocalRawBrightness(pos.north()) <= threshhold) {
				rz = pos.getZ() - 0.0625D;
			}

			if (side == Direction.SOUTH && !world.getBlockState(pos.south()).isSolidRender(world, pos) && world.getMaxLocalRawBrightness(pos.south()) <= threshhold) {
				rz = pos.getZ() + 0.0625D + 1.0D;
			}

			if (side == Direction.WEST && !world.getBlockState(pos.west()).isSolidRender(world, pos) && world.getMaxLocalRawBrightness(pos.west()) <= threshhold) {
				rx = pos.getX() - 0.0625D;
			}

			if (side == Direction.EAST && !world.getBlockState(pos.east()).isSolidRender(world, pos) && world.getMaxLocalRawBrightness(pos.east()) <= threshhold) {
				rx = pos.getX() + 0.0625D + 1.0D;
			}

			if (rx < pos.getX() || rx > pos.getX() + 1 || ry < 0.0D || ry > pos.getY() + 1 || rz < pos.getZ() || rz > pos.getZ() + 1) {
				world.addParticle(new DustParticleOptions(new Vector3f(0.5F, 0.0F, 0.5F), 1.0F), rx, ry, rz, 0.25D, -1.0D, 0.5D);
			}
		}
	}
}
