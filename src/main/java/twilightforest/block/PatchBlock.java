package twilightforest.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.PipeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.Map;
import java.util.Random;

public class PatchBlock extends TFPlantBlock {

	public static final MapCodec<PatchBlock> CODEC = simpleCodec(PatchBlock::new);
	public static final BooleanProperty NORTH = BlockStateProperties.NORTH;
	public static final BooleanProperty EAST = BlockStateProperties.EAST;
	public static final BooleanProperty SOUTH = BlockStateProperties.SOUTH;
	public static final BooleanProperty WEST = BlockStateProperties.WEST;
	private static final Map<Direction, BooleanProperty> PROPERTY_BY_DIRECTION = PipeBlock.PROPERTY_BY_DIRECTION.entrySet().stream().filter(directionPropertyPair -> directionPropertyPair.getKey().getAxis().isHorizontal()).collect(Util.toMap());

	public PatchBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.getStateDefinition().any().setValue(NORTH, false).setValue(EAST, false).setValue(SOUTH, false).setValue(WEST, false));
	}

	@Override
	protected MapCodec<? extends BushBlock> codec() {
		return CODEC;
	}

	@Override
	public BlockState updateShape(BlockState state, Direction dir, BlockState neighborUpdated, LevelAccessor accessor, BlockPos pos, BlockPos posNeighbor) {
		return dir.getAxis().isHorizontal() ? state.setValue(PROPERTY_BY_DIRECTION.get(dir), neighborUpdated.getBlock() == this) : super.updateShape(state, dir, neighborUpdated, accessor, pos, posNeighbor);
	}

	@Override
	@Deprecated
	public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
		return shapeFromPos(state, pos);
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

	public static AABB AABBFromLong(long seed) {
		int xOff0 = (int) (seed >> 12 & 3L);
		int xOff1 = (int) (seed >> 15 & 3L);
		int zOff0 = (int) (seed >> 18 & 3L);
		int zOff1 = (int) (seed >> 21 & 3L);

		return new AABB(
				1F + xOff1,
				0.0F,
				1F + zOff1,
				15F - xOff0,
				1F,
				15F - zOff0
		);
	}

	public static AABB AABBFromRandom(RandomSource random) {
		return AABBFromLong(random.nextLong());
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(NORTH, EAST, SOUTH, WEST);
	}
}
