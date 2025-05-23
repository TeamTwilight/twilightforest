package twilightforest.block;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import twilightforest.enums.BossVariant;

import java.util.Map;

public class TrophyWallBlock extends AbstractTrophyBlock {

	public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;
	public static final MapCodec<TrophyWallBlock> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
			BossVariant.CODEC.fieldOf("variant").forGetter(AbstractTrophyBlock::getVariant),
			propertiesCodec())
		.apply(instance, TrophyWallBlock::new));
	private static final Map<Direction, VoxelShape> SHAPES = Maps
		.newEnumMap(ImmutableMap.of(Direction.NORTH, Block.box(4.0D, 4.0D, 8.0D, 12.0D, 12.0D, 16.0D), Direction.SOUTH, Block.box(4.0D, 4.0D, 0.0D, 12.0D, 12.0D, 8.0D), Direction.EAST, Block.box(0.0D, 4.0D, 4.0D, 8.0D, 12.0D, 12.0D), Direction.WEST, Block.box(8.0D, 4.0D, 4.0D, 16.0D, 12.0D, 12.0D)));
	private static final Map<Direction, VoxelShape> YETI_SHAPES = Maps
		.newEnumMap(ImmutableMap.of(Direction.NORTH, Block.box(3.25D, 4.0D, 8.5D, 12.75D, 14.5D, 16.0D), Direction.SOUTH, Block.box(3.25D, 4.0D, 0.0D, 12.75D, 14.5D, 7.5D), Direction.EAST, Block.box(0.0D, 4.0D, 3.25D, 7.5D, 14.5D, 12.75D), Direction.WEST, Block.box(8.5D, 4.0D, 3.25D, 16.0D, 14.5D, 12.75D)));

	public TrophyWallBlock(BossVariant variant, BlockBehaviour.Properties properties) {
		super(variant, 0, properties);
		this.registerDefaultState(this.getStateDefinition().any().setValue(FACING, Direction.NORTH));
	}

	@Override
	protected MapCodec<? extends BaseEntityBlock> codec() {
		return CODEC;
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
		if (((AbstractTrophyBlock) state.getBlock()).getVariant() == BossVariant.UR_GHAST) {
			return TrophyBlock.GHAST_SHAPE;
		} else if (((AbstractTrophyBlock) state.getBlock()).getVariant() == BossVariant.ALPHA_YETI) {
			return YETI_SHAPES.get(state.getValue(FACING));
		}
		return SHAPES.get(state.getValue(FACING));
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		BlockState blockstate = this.defaultBlockState();
		BlockGetter getter = context.getLevel();
		BlockPos blockpos = context.getClickedPos();
		Direction[] adirection = context.getNearestLookingDirections();

		for (Direction direction : adirection) {
			if (direction.getAxis().isHorizontal()) {
				Direction direction1 = direction.getOpposite();
				blockstate = blockstate.setValue(FACING, direction1);
				if (!getter.getBlockState(blockpos.relative(direction)).canBeReplaced(context)) {
					return blockstate;
				}
			}
		}

		return null;
	}

	@Override
	public BlockState rotate(BlockState state, Rotation rot) {
		return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
	}

	@Override
	@SuppressWarnings("deprecation")
	public BlockState mirror(BlockState state, Mirror mirror) {
		return state.rotate(mirror.getRotation(state.getValue(FACING)));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING, POWERED);
	}
}
