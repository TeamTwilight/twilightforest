package twilightforest.block;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;

public abstract class SpecialMagicLogBlock extends DirectionalBlock {

	public static final BooleanProperty ACTIVE = BooleanProperty.create("active");

	protected SpecialMagicLogBlock(BlockBehaviour.Properties properties) {
		super(properties);

		this.registerDefaultState(this.getStateDefinition().any().setValue(FACING, Direction.UP).setValue(ACTIVE, false));
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return this.defaultBlockState().setValue(FACING, context.getClickedFace());
	}

	@Override
	protected BlockState rotate(BlockState state, Rotation rotation) {
		return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
	}

	@Override
	protected BlockState mirror(BlockState state, Mirror mirror) {
		return state.rotate(mirror.getRotation(state.getValue(FACING)));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING, ACTIVE);
	}

	//No longer an override, but keep here for sanity
	public int tickRate() {
		return 20;
	}

	@Override
	public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
		level.scheduleTick(pos, this, this.tickRate());
	}

	@Override
	public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource rand) {
		if (!state.getValue(ACTIVE) || !this.doesCoreFunction()) return;

		this.playSound(level, pos, rand);
		this.performTreeEffect(level, pos, rand);

		level.scheduleTick(pos, this, this.tickRate());
	}

	@Override
	protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult result) {
		if (!this.doesCoreFunction()) {
			level.setBlockAndUpdate(pos, state.setValue(ACTIVE, false));
			player.sendOverlayMessage(Component.translatable("misc.twilightforest.core_disabled", this.getName()).withStyle(ChatFormatting.RED));
			return InteractionResult.SUCCESS;
		}

		if (!state.getValue(ACTIVE)) {
			level.setBlockAndUpdate(pos, state.setValue(ACTIVE, true));
			level.scheduleTick(pos, this, this.tickRate());
			return InteractionResult.SUCCESS;
		} else if (state.getValue(ACTIVE)) {
			level.setBlockAndUpdate(pos, state.setValue(ACTIVE, false));
			return InteractionResult.SUCCESS;
		}

		return InteractionResult.PASS;
	}

	abstract void performTreeEffect(ServerLevel level, BlockPos pos, RandomSource rand);

	@SuppressWarnings("BooleanMethodIsAlwaysInverted")
	public abstract boolean doesCoreFunction();

	protected void playSound(Level level, BlockPos pos, RandomSource rand) {
	}
}
