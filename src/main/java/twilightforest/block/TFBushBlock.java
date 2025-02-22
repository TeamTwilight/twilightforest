package twilightforest.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.util.RandomSource;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import net.neoforged.neoforge.registries.DeferredItem;
import org.jetbrains.annotations.Nullable;

public abstract class TFBushBlock extends Block {
	public static final IntegerProperty AGE = BlockStateProperties.AGE_3;
	public static final int MAX_AGE = 3;

	protected final DeferredItem<Item> harvestItem;
	protected final TagKey<Block> surviveBlockTag;

	private static final VoxelShape SHAPE_0 = Block.box(4.0, 0.001, 4.0, 12.0, 8.0, 12.0);
	private static final VoxelShape SHAPE_1 = Block.box(2.0, 0.001, 2.0, 14.0, 12.0, 14.0);
	private static final VoxelShape SHAPE_2 = Block.box(0.001, 0.001, 0.001, 15.999, 15.999, 15.999);

	public TFBushBlock(DeferredItem<Item> harvestItem, BlockBehaviour.Properties properties, TagKey<Block> surviveBlockTag) {
		super(properties.destroyTime(0.3F).randomTicks().dynamicShape().noOcclusion());

		this.harvestItem = harvestItem;
		this.surviveBlockTag = surviveBlockTag;
		this.registerDefaultState(this.stateDefinition.any().setValue(AGE, 0));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(AGE);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return switch (state.getValue(AGE)) {
			case 1 -> SHAPE_1;
			case 2, 3 -> SHAPE_2;
			default -> SHAPE_0;
		};
	}

	@Override
	public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		if (state.getValue(AGE) < MAX_AGE && random.nextInt(20) == 0 && canGrowAt(state, level, pos))
			grow(state, level, pos);
	}

	private void grow(BlockState state, ServerLevel level, BlockPos pos) {
		level.setBlock(pos, state.setValue(AGE, state.getValue(AGE) + 1), Block.UPDATE_CLIENTS);
	}

	@Override
	protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
		if (state.getValue(AGE) == MAX_AGE) {
			if (!level.isClientSide) {
				int count = getNumberOfBerries(level.random);
				ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(harvestItem.get(), count));
				level.setBlock(pos, state.setValue(AGE, MAX_AGE - 1), Block.UPDATE_CLIENTS);
			}
			return InteractionResult.sidedSuccess(level.isClientSide);
		}
		return InteractionResult.PASS;
	}

	@Nullable
	@Override
	public PushReaction getPistonPushReaction(BlockState state) {
		return state.getValue(AGE) < 2 ? PushReaction.DESTROY : null;
	}

	protected abstract int getNumberOfBerries(RandomSource random);

	@Override
	protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
		BlockState blockStateBelow = level.getBlockState(pos.below());
		boolean isSameMatureBush = blockStateBelow.is(state.getBlock()) && blockStateBelow.getValue(AGE) >= MAX_AGE - 1;
		return blockStateBelow.is(surviveBlockTag) || isSameMatureBush;
	}

	protected boolean canGrowAt(BlockState state, LevelReader level, BlockPos pos) {
		return canSurvive(state, level, pos);
	}
}
