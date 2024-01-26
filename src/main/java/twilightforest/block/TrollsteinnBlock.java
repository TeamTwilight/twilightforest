package twilightforest.block;

import com.google.common.collect.ImmutableMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.joml.Vector3f;

import java.util.Map;

public class TrollsteinnBlock extends Block {
	private static final BooleanProperty DOWN_LIT = BooleanProperty.create("down");
	private static final BooleanProperty UP_LIT = BooleanProperty.create("up");
	private static final BooleanProperty NORTH_LIT = BooleanProperty.create("north");
	private static final BooleanProperty SOUTH_LIT = BooleanProperty.create("south");
	private static final BooleanProperty WEST_LIT = BooleanProperty.create("west");
	private static final BooleanProperty EAST_LIT = BooleanProperty.create("east");
	private static final Map<Direction, BooleanProperty> PROPERTY_MAP = ImmutableMap.<Direction, BooleanProperty>builder()
			.put(Direction.DOWN, DOWN_LIT)
			.put(Direction.UP, UP_LIT)
			.put(Direction.NORTH, NORTH_LIT)
			.put(Direction.SOUTH, SOUTH_LIT)
			.put(Direction.WEST, WEST_LIT)
			.put(Direction.EAST, EAST_LIT).build();

	private static final int LIGHT_THRESHOLD = 7;

	public TrollsteinnBlock(Properties properties) {
		super(properties);

		this.registerDefaultState(this.getStateDefinition().any()
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
	public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		BlockState newState = state;
		for (Direction direction : Direction.values())
			newState = newState.setValue(PROPERTY_MAP.get(direction), level.getMaxLocalRawBrightness(pos.relative(direction)) > LIGHT_THRESHOLD);
		if (!newState.equals(state)) level.setBlockAndUpdate(pos, newState);
	}

	@Override
	public boolean hasAnalogOutputSignal(BlockState state) {
		return true;
	}

	@Override
	public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
		int peak = 0;
		for (Direction direction : Direction.values())
			peak = Math.max(level.getMaxLocalRawBrightness(pos.relative(direction)), peak);
		return peak;
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		BlockState ret = defaultBlockState();
		for (Map.Entry<Direction, BooleanProperty> e : PROPERTY_MAP.entrySet()) {
			int light = ctx.getLevel().getMaxLocalRawBrightness(ctx.getClickedPos().relative(e.getKey()));
			ret = ret.setValue(e.getValue(), light > LIGHT_THRESHOLD);
		}
		return ret;
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource rand) {
		if (rand.nextInt(2) == 0) this.sparkle(level, pos);
	}

	// [VanillaCopy] Based on BlockRedstoneOre.spawnParticles
	private void sparkle(Level level, BlockPos pos) {
		RandomSource random = level.getRandom();
		int threshold = LIGHT_THRESHOLD;

		for (Direction side : Direction.values()) {
			double rx = pos.getX() + random.nextFloat();
			double ry = pos.getY() + random.nextFloat();
			double rz = pos.getZ() + random.nextFloat();

			if (side == Direction.DOWN && !level.getBlockState(pos.below()).isSolidRender(level, pos) && level.getMaxLocalRawBrightness(pos.below()) <= threshold) {
				ry = pos.getY() - 0.0625D;
			}

			if (side == Direction.UP && !level.getBlockState(pos.above()).isSolidRender(level, pos) && level.getMaxLocalRawBrightness(pos.above()) <= threshold) {
				ry = pos.getY() + 0.0625D + 1.0D;
			}

			if (side == Direction.NORTH && !level.getBlockState(pos.north()).isSolidRender(level, pos) && level.getMaxLocalRawBrightness(pos.north()) <= threshold) {
				rz = pos.getZ() - 0.0625D;
			}

			if (side == Direction.SOUTH && !level.getBlockState(pos.south()).isSolidRender(level, pos) && level.getMaxLocalRawBrightness(pos.south()) <= threshold) {
				rz = pos.getZ() + 0.0625D + 1.0D;
			}

			if (side == Direction.WEST && !level.getBlockState(pos.west()).isSolidRender(level, pos) && level.getMaxLocalRawBrightness(pos.west()) <= threshold) {
				rx = pos.getX() - 0.0625D;
			}

			if (side == Direction.EAST && !level.getBlockState(pos.east()).isSolidRender(level, pos) && level.getMaxLocalRawBrightness(pos.east()) <= threshold) {
				rx = pos.getX() + 0.0625D + 1.0D;
			}

			if (rx < pos.getX() || rx > pos.getX() + 1 || ry < 0.0D || ry > pos.getY() + 1 || rz < pos.getZ() || rz > pos.getZ() + 1) {
				level.addParticle(new DustParticleOptions(new Vector3f(0.5F, 0.0F, 0.5F), 1.0F), rx, ry, rz, 0.25D, -1.0D, 0.5D);
			}
		}
	}
}
