package twilightforest.block;

import com.google.common.collect.Lists;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WaterlilyBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import twilightforest.enums.HugeLilypadPiece;

import java.util.List;

public class HugeLilyPadBlock extends WaterlilyBlock {
	public static final EnumProperty<Direction> FACING = TFHorizontalBlock.FACING;
	public static final EnumProperty<HugeLilypadPiece> PIECE = EnumProperty.create("piece", HugeLilypadPiece.class);
	private static final VoxelShape SHAPE = Block.box(0, 0, 0, 16, 1, 16);

	private boolean isSelfDestructing = false;

	public HugeLilyPadBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.getStateDefinition().any().setValue(FACING, Direction.NORTH).setValue(PIECE, HugeLilypadPiece.NW));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING, PIECE);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
		return SHAPE;
	}

	@Override
	public VoxelShape getOcclusionShape(BlockState state) {
		return Shapes.empty();
	}

	@Override
	public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
		if (!this.isSelfDestructing) {
			this.setGiantBlockToAir(level, pos, state);
		}
	}

	private void setGiantBlockToAir(Level level, BlockPos pos, BlockState state) {
		// this flag is not threadsafe
		this.isSelfDestructing = true;

		for (BlockPos check : this.getAllMyBlocks(pos, state)) {
			BlockState stateThere = level.getBlockState(check);
			if (stateThere.getBlock() == this) {
				level.destroyBlock(check, false);
			}
		}

		this.isSelfDestructing = false;
	}

	/**
	 * Get all 4 coordinates for all parts of this lily pad.
	 */
	public List<BlockPos> getAllMyBlocks(BlockPos pos, BlockState state) {
		List<BlockPos> pieces = Lists.newArrayListWithCapacity(4);
		if (state.getBlock() == this) {
			// find NW corner
			BlockPos nwPos = switch (state.getValue(PIECE)) {
				case NE -> pos.west();
				case SE -> pos.north().west();
				case SW -> pos.north();
				default -> pos;
			};

			pieces.add(nwPos);
			pieces.add(nwPos.south());
			pieces.add(nwPos.east());
			pieces.add(nwPos.south().east());
		}

		return pieces;
	}
}
