package twilightforest.block;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;

public abstract class SpecialMagicLogBlock extends RotatedPillarBlock {

	public static final BooleanProperty ACTIVE = BooleanProperty.create("active");

	protected SpecialMagicLogBlock(BlockBehaviour.Properties properties) {
		super(properties.strength(2.0F).sound(SoundType.WOOD).lightLevel((state) -> state.getValue(ACTIVE) ? 15 : 0));

		this.registerDefaultState(this.getStateDefinition().any().setValue(ACTIVE, false));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(ACTIVE);
	}

	//No longer an override, but keep here for sanity
	public int tickRate() {
		return 20;
	}

	@Override
	@Deprecated
	public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
		level.scheduleTick(pos, this, this.tickRate());
	}

	@Override
	@Deprecated
	public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource rand) {
		if (level.isClientSide() || !state.getValue(ACTIVE) || !this.doesCoreFunction()) return;

		this.playSound(level, pos, rand);
		this.performTreeEffect(level, pos, rand);

		level.scheduleTick(pos, this, this.tickRate());
	}

	@Override
	@Deprecated
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
		if (!this.doesCoreFunction()) {
			state.setValue(ACTIVE, false);
			player.displayClientMessage(Component.translatable("misc.twilightforest.core_disabled", this.getName()).withStyle(ChatFormatting.RED), true);
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

	abstract void performTreeEffect(Level level, BlockPos pos, RandomSource rand);

	public abstract boolean doesCoreFunction();

	protected void playSound(Level level, BlockPos pos, RandomSource rand) {
	}
}
