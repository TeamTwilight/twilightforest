package twilightforest.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
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
import org.jetbrains.annotations.Nullable;
import twilightforest.block.entity.FireJetBlockEntity;
import twilightforest.data.tags.BlockTagGenerator;
import twilightforest.data.tags.FluidTagGenerator;
import twilightforest.enums.FireJetVariant;
import twilightforest.init.TFBlockEntities;

public class FireJetBlock extends BaseEntityBlock {

	public static final MapCodec<FireJetBlock> CODEC = simpleCodec(FireJetBlock::new);
	public static final EnumProperty<FireJetVariant> STATE = EnumProperty.create("state", FireJetVariant.class);

	public FireJetBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.getStateDefinition().any().setValue(STATE, FireJetVariant.IDLE));
	}

	@Override
	protected MapCodec<? extends BaseEntityBlock> codec() {
		return CODEC;
	}

	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.MODEL;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(STATE);
	}

	@Nullable
	@Override
	public BlockPathTypes getBlockPathType(BlockState state, BlockGetter getter, BlockPos pos, @Nullable Mob entity) {
		return state.getValue(STATE) == FireJetVariant.IDLE ? null : BlockPathTypes.DAMAGE_FIRE;
	}

	@Override
	@Deprecated
	public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		if (!level.isClientSide() && state.getValue(STATE) == FireJetVariant.IDLE) {
			BlockPos lavaPos = findLavaAround(level, pos.below());

			if (this.isLava(level, lavaPos)) {
				level.setBlockAndUpdate(lavaPos, Blocks.AIR.defaultBlockState());
				level.setBlockAndUpdate(pos, state.setValue(STATE, FireJetVariant.POPPING));
			}
		}
	}

	/**
	 * Find a full block of lava near the designated block.  This is intentionally not really that reliable.
	 */
	private BlockPos findLavaAround(Level level, BlockPos pos) {
		if (this.isLava(level, pos)) {
			return pos;
		}

		for (int i = 0; i < 3; i++) {
			BlockPos randPos = pos.offset(level.getRandom().nextInt(3) - 1, 0, level.getRandom().nextInt(3) - 1);
			if (this.isLava(level, randPos)) {
				return randPos;
			}
		}

		return pos;
	}

	private boolean isLava(Level level, BlockPos pos) {
		BlockState state = level.getBlockState(pos);
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
