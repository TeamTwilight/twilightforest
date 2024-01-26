package twilightforest.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;
import twilightforest.util.EntityUtil;

public class StrongholdShieldBlock extends DirectionalBlock {

	public static final MapCodec<StrongholdShieldBlock> CODEC = simpleCodec(StrongholdShieldBlock::new);

	public StrongholdShieldBlock(BlockBehaviour.Properties properties) {
		super(properties);
		this.registerDefaultState(this.getStateDefinition().any().setValue(FACING, Direction.DOWN));
	}

	@Override
	protected MapCodec<? extends DirectionalBlock> codec() {
		return CODEC;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(FACING);
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		Direction facing = context.getNearestLookingDirection();
		if (facing.getAxis().equals(Direction.Axis.Y)) {
			facing = facing.getOpposite();
		}
		return defaultBlockState().setValue(FACING, facing);
	}

	@Override
	@Deprecated
	public float getDestroyProgress(BlockState state, Player player, BlockGetter getter, BlockPos pos) {
		BlockHitResult ray = EntityUtil.rayTrace(player, range -> range + 1.0);

		Direction hitFace = ray.getDirection();
		boolean upOrDown = state.getValue(DirectionalBlock.FACING) == Direction.UP || state.getValue(DirectionalBlock.FACING) == Direction.DOWN;
		Direction sideFace = state.getValue(DirectionalBlock.FACING).getOpposite();
		Direction upFace = state.getValue(DirectionalBlock.FACING);

		if (hitFace == (upOrDown ? upFace : sideFace)) {
			return player.getDigSpeed(Blocks.STONE.defaultBlockState(), pos) / 1.5F / 100F;
		} else {
			return super.getDestroyProgress(state, player, getter, pos);
		}
	}

	@Override
	public boolean canEntityDestroy(BlockState state, BlockGetter getter, BlockPos pos, Entity entity) {
		return false;
	}
}
