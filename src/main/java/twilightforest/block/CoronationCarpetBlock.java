package twilightforest.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.WoolCarpetBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.Nullable;
import twilightforest.block.entity.CoronationCarpetBlockEntity;
import twilightforest.init.TFBlockEntities;

// [VanillaCopy] extended WoolCarpetBlock with BlockEntity
public class CoronationCarpetBlock extends BaseEntityBlock {
	public static final MapCodec<CoronationCarpetBlock> CODEC = RecordCodecBuilder.mapCodec((i) -> i.group(DyeColor.CODEC.fieldOf("color").forGetter(CoronationCarpetBlock::getColor), propertiesCodec()).apply(i, CoronationCarpetBlock::new));
	private static final VoxelShape SHAPE = Block.column(16.0D, 0.0D, 1.0D);

	private final DyeColor color;

	public CoronationCarpetBlock(DyeColor color, Properties properties) {
		super(properties);
		this.color = color;
	}

	@Override
	protected MapCodec<CoronationCarpetBlock> codec() {
		return CODEC;
	}

	@Override
	public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
		return new CoronationCarpetBlockEntity(blockPos, blockState);
	}

	@Override
	protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return SHAPE;
	}

	@Override
	protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
		if (!state.canSurvive(level, pos)) {
			return Blocks.AIR.defaultBlockState();
		}

		if (level instanceof Level world && world.isClientSide()) {
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if (blockEntity instanceof CoronationCarpetBlockEntity) {
				world.getModelDataManager().requestRefresh(blockEntity);
			}
		}

		return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
	}

	@Override
	protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
		return !level.isEmptyBlock(pos.below());
	}

	public DyeColor getColor() {
		return color;
	}
}
