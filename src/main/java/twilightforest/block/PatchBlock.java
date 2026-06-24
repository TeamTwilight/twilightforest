package twilightforest.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Util;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.Map;
import java.util.Random;

public class PatchBlock extends TFPlantBlock {

	public static final BooleanProperty NORTH = BlockStateProperties.NORTH;
	public static final BooleanProperty EAST = BlockStateProperties.EAST;
	public static final BooleanProperty SOUTH = BlockStateProperties.SOUTH;
	public static final BooleanProperty WEST = BlockStateProperties.WEST;
	public static final MapCodec<PatchBlock> CODEC = simpleCodec(PatchBlock::new);
	private static final Map<Direction, BooleanProperty> PROPERTY_BY_DIRECTION = PipeBlock.PROPERTY_BY_DIRECTION.entrySet().stream().filter(directionPropertyPair -> directionPropertyPair.getKey().getAxis().isHorizontal()).collect(Util.toMap());

	public PatchBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.getStateDefinition().any().setValue(NORTH, false).setValue(EAST, false).setValue(SOUTH, false).setValue(WEST, false));
	}

	public static VoxelShape shapeFromLong(BlockState state, long seed) {
		boolean xConnect0 = state.getValue(EAST);
		boolean xConnect1 = state.getValue(WEST);
		boolean zConnect0 = state.getValue(SOUTH);
		boolean zConnect1 = state.getValue(NORTH);

		int xOff0 = (int) (seed >> 12 & 3L);
		int xOff1 = (int) (seed >> 15 & 3L);
		int zOff0 = (int) (seed >> 18 & 3L);
		int zOff1 = (int) (seed >> 21 & 3L);

		return Block.box(
			xConnect1 ? 0F : (1F + xOff1),
			0.0F,
			zConnect1 ? 0F : (1F + zOff1),
			xConnect0 ? 16F : (15F - xOff0),
			1F,
			zConnect0 ? 16F : (15F - zOff0)
		);
	}

	public static VoxelShape shapeFromRandom(BlockState state, Random random) {
		return shapeFromLong(state, random.nextLong());
	}

	public static VoxelShape shapeFromPos(BlockState state, BlockPos pos) {
		return shapeFromRandom(state, new Random(state.getSeed(pos)));
	}

	public static BoundingBox AABBFromLong(long seed) {
		int xOff0 = (int) (seed >> 12 & 3L);
		int xOff1 = (int) (seed >> 15 & 3L);
		int zOff0 = (int) (seed >> 18 & 3L);
		int zOff1 = (int) (seed >> 21 & 3L);

		int x0 = 1 + xOff1;
		int z0 = 1 + zOff1;
		int x1 = 15 - xOff0;
		int z1 = 15 - zOff0;

		return new BoundingBox(x0, 0, z0, x1, 1, z1);
	}

	public static BoundingBox AABBFromRandom(RandomSource random) {
		return AABBFromLong(random.nextLong());
	}

	@Override
	protected MapCodec<? extends VegetationBlock> codec() {
		return CODEC;
	}

	@Override
	protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbor, BlockPos neighborPos, BlockState neighborState, RandomSource random) {
		return directionToNeighbor.getAxis().isHorizontal() ? state.setValue(PROPERTY_BY_DIRECTION.get(directionToNeighbor), neighborState.getBlock() == this) : super.updateShape(state, level, ticks, pos, directionToNeighbor, neighborPos, neighborState, random);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
		return shapeFromPos(state, pos);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(NORTH, EAST, SOUTH, WEST);
	}

	@Override
	protected BlockState rotate(BlockState state, Rotation rot) {
		return switch (rot) {
			case CLOCKWISE_180 -> state.setValue(NORTH, state.getValue(SOUTH)).setValue(EAST, state.getValue(WEST)).setValue(SOUTH, state.getValue(NORTH)).setValue(WEST, state.getValue(EAST));
			case COUNTERCLOCKWISE_90 -> state.setValue(NORTH, state.getValue(EAST)).setValue(EAST, state.getValue(SOUTH)).setValue(SOUTH, state.getValue(WEST)).setValue(WEST, state.getValue(NORTH));
			case CLOCKWISE_90 -> state.setValue(NORTH, state.getValue(WEST)).setValue(EAST, state.getValue(NORTH)).setValue(SOUTH, state.getValue(EAST)).setValue(WEST, state.getValue(SOUTH));
			default -> state;
		};
	}

	@Override
	protected BlockState mirror(BlockState state, Mirror mirror) {
		return switch (mirror) {
			case LEFT_RIGHT -> state.setValue(NORTH, state.getValue(SOUTH)).setValue(SOUTH, state.getValue(NORTH));
			case FRONT_BACK -> state.setValue(EAST, state.getValue(WEST)).setValue(WEST, state.getValue(EAST));
			default -> super.mirror(state, mirror);
		};
	}
}
