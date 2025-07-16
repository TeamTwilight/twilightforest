package twilightforest.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.util.RandomSource;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import net.neoforged.neoforge.registries.DeferredItem;
import org.jetbrains.annotations.Nullable;

public abstract class TFBushBlock extends Block implements SnowLoggable {
	public static final IntegerProperty AGE = BlockStateProperties.AGE_3;
	public static final int MAX_AGE = 3;

	public final DeferredItem<Item> harvestItem;
	public final int minNumberOfBerries;
	public final int maxNumberOfBerries;

	protected final TagKey<Block> surviveBlockTag;

	private final VoxelShape smallBushShape;
	private final VoxelShape mediumBushShape;
	private final VoxelShape largeBushShape;

	public TFBushBlock(DeferredItem<Item> harvestItem, Properties properties, TagKey<Block> surviveBlockTag,
					   VoxelShape smallBushShape, VoxelShape mediumBushShape, VoxelShape largeBushShape,
					   int minNumberOfBerries, int maxNumberOfBerries) {
		super(properties.destroyTime(0.3F).randomTicks().dynamicShape().noOcclusion());

		this.harvestItem = harvestItem;
		this.surviveBlockTag = surviveBlockTag;
		this.smallBushShape = smallBushShape;
		this.mediumBushShape = mediumBushShape;
		this.largeBushShape = largeBushShape;
		this.minNumberOfBerries = minNumberOfBerries;
		this.maxNumberOfBerries = maxNumberOfBerries;
		this.registerDefaultState(this.stateDefinition.any().setValue(AGE, 0).setValue(SNOW_LAYERS, 0));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(AGE).add(SNOW_LAYERS);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return mergeSnowCap(mergeWithSnowLayer(
			switch (state.getValue(AGE)) {
				case 1 -> mediumBushShape;
				case 2, 3 -> largeBushShape;
				default -> smallBushShape;
			}, state), state).optimize();
	}

	@Override
	protected float getShadeBrightness(BlockState state, BlockGetter level, BlockPos pos) {
		return 1.0F;
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

	@Override
	protected ItemInteractionResult useItemOn(
		ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult
	) {
		if (!stack.is(Items.SNOW) || state.getValue(SNOW_LAYERS) == MAX_SNOW_LAYERS)
			return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
		if (!player.hasInfiniteMaterials())
			stack.shrink(1);
		BlockState newState = state.setValue(SNOW_LAYERS, state.getValue(SNOW_LAYERS) + 1);
		level.setBlock(pos, newState, Block.UPDATE_ALL | Block.UPDATE_KNOWN_SHAPE);
		return ItemInteractionResult.SUCCESS;
	}

	@Override
	public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest, FluidState fluid) {
		if (!player.isSecondaryUseActive() && state.getValue(SNOW_LAYERS) > MIN_SNOW_LAYERS) {
			this.handleBreakingLogic(level, pos, state, player, null);
			return false;
		}
		return super.onDestroyedByPlayer(state, level, pos, player, willHarvest, fluid);
	}

	@Nullable
	@Override
	public PushReaction getPistonPushReaction(BlockState state) {
		return state.getValue(AGE) < 2 ? PushReaction.DESTROY : null;
	}

	private int getNumberOfBerries(RandomSource random) {
		return random.nextIntBetweenInclusive(minNumberOfBerries, maxNumberOfBerries);
	}

	@Override
	public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
		BlockState blockStateBelow = level.getBlockState(pos.below());
		boolean isSameMatureBush = blockStateBelow.is(state.getBlock()) && blockStateBelow.getValue(AGE) >= MAX_AGE - 1;
		return blockStateBelow.is(surviveBlockTag) || isSameMatureBush;
	}

	public TagKey<Block> getSurviveBlockTag() {
		return surviveBlockTag;
	}

	protected boolean canGrowAt(BlockState state, LevelReader level, BlockPos pos) {
		return canSurvive(state, level, pos);
	}

	private VoxelShape mergeSnowCap(VoxelShape shape, BlockState state) {
		int age = state.getValue(AGE);
		if (age > 1 || state.getValue(SNOW_LAYERS) == MIN_SNOW_LAYERS)
			return shape;
		return Shapes.or(switch (age) {
			case 0 -> Block.box(4, 8, 4, 12, 10, 12);
			case 1 -> Block.box(2, 12, 2, 14, 14, 14);
			default -> throw new IllegalStateException("Unexpected value: " + age);
		}, shape);
	}
}
