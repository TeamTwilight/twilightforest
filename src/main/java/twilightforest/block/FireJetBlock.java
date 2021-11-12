package twilightforest.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import twilightforest.data.BlockTagGenerator;
import twilightforest.data.FluidTagGenerator;
import twilightforest.enums.FireJetVariant;
import twilightforest.block.entity.FireJetBlockEntity;
import twilightforest.block.entity.TFBlockEntities;

import javax.annotation.Nullable;
import java.util.Random;

public class FireJetBlock extends BaseEntityBlock {

	public static final EnumProperty<FireJetVariant> STATE = EnumProperty.create("state", FireJetVariant.class);

	protected FireJetBlock(Properties props) {
		super(props);
		registerDefaultState(defaultBlockState().setValue(STATE, FireJetVariant.IDLE));
	}

	@Override
	public RenderShape getRenderShape(BlockState p_49232_) {
		return RenderShape.MODEL;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(STATE);
	}

	@Nullable
	@Override
	public BlockPathTypes getAiPathNodeType(BlockState state, BlockGetter world, BlockPos pos, @Nullable Mob entity) {
		return state.getValue(STATE) == FireJetVariant.IDLE ? null : BlockPathTypes.DAMAGE_FIRE;
	}

	@Override
	@Deprecated
	public void randomTick(BlockState state, ServerLevel world, BlockPos pos, Random random) {
		if (!world.isClientSide && state.getValue(STATE) == FireJetVariant.IDLE) {
			BlockPos lavaPos = findLavaAround(world, pos.below());

			if (isLava(world, lavaPos)) {
				world.setBlockAndUpdate(lavaPos, Blocks.AIR.defaultBlockState());
				world.setBlockAndUpdate(pos, state.setValue(STATE, FireJetVariant.POPPING));
			}
		}
	}

	/**
	 * Find a full block of lava near the designated block.  This is intentionally not really that reliable.
	 */
	private BlockPos findLavaAround(Level world, BlockPos pos) {
		if (isLava(world, pos)) {
			return pos;
		}

		for (int i = 0; i < 3; i++) {
			BlockPos randPos = pos.offset(world.random.nextInt(3) - 1, 0, world.random.nextInt(3) - 1);
			if (isLava(world, randPos)) {
				return randPos;
			}
		}

		return pos;
	}

	private boolean isLava(Level world, BlockPos pos) {
		BlockState state = world.getBlockState(pos);
		return state.is(BlockTagGenerator.FIRE_JET_FUEL) || state.getBlock().getFluidState(state).is(FluidTagGenerator.FIRE_JET_FUEL);
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new FireJetBlockEntity(pos, state);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		return createTickerHelper(type, TFBlockEntities.FLAME_JET.get(), FireJetBlockEntity::tick);
	}
}
