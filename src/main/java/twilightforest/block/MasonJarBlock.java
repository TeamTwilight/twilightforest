package twilightforest.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.DecoratedPotBlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import twilightforest.block.entity.MasonJarBlockEntity;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFParticleType;

import java.util.List;

public class MasonJarBlock extends BaseEntityBlock implements SimpleWaterloggedBlock {
	public static final MapCodec<MasonJarBlock> CODEC = simpleCodec(MasonJarBlock::new);
	private static final VoxelShape JAR = Block.box(3.0D, 0.0D, 3.0D, 13.0D, 14.0D, 13.0D);
	private static final VoxelShape LID = Block.box(4.0D, 14.0D, 4.0D, 12.0D, 16.0D, 12.0D);
	private static final VoxelShape AABB = Shapes.or(JAR, LID);
	private static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

	@SuppressWarnings("this-escape")
	public MasonJarBlock(BlockBehaviour.Properties properties) {
		super(properties);
		this.registerDefaultState(this.getStateDefinition().any().setValue(WATERLOGGED, false));
	}

	@Override
	protected MapCodec<? extends BaseEntityBlock> codec() {
		return CODEC;
	}

	@Override
	@Deprecated
	public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
		return AABB;
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		BlockState superState = super.getStateForPlacement(context);
		return superState == null ? null : superState.setValue(WATERLOGGED, context.getLevel().getFluidState(context.getClickedPos()).is(Fluids.WATER));
	}

	@Override
	public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor accessor, BlockPos currentPos, BlockPos facingPos) {
		if (state.getValue(WATERLOGGED)) accessor.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(accessor));
		return super.updateShape(state, facing, facingState, accessor, currentPos, facingPos);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(WATERLOGGED);
	}

	@Override
	public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
		if (level.getBlockEntity(pos) instanceof MasonJarBlockEntity blockEntity && blockEntity.getItemHandler().getItem().is(TFBlocks.FIREFLY.asItem())) {//FIXME move to be
			for (int i = 0; i < 2; i++) {
				double dx = pos.getX() + ((random.nextFloat() - random.nextFloat()) * 0.2F + 0.5F);
				double dy = pos.getY() + 0.4F + ((random.nextFloat() - random.nextFloat()) * 0.3F);
				double dz = pos.getZ() + ((random.nextFloat() - random.nextFloat()) * 0.2F + 0.5F);
				level.addParticle(TFParticleType.FIREFLY.get(), dx, dy, dz, 0, 0, 0);
			}
		}
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new MasonJarBlockEntity(pos, state);
	}

	@Override
	protected ItemInteractionResult useItemOn(ItemStack heldStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
		if (level.getBlockEntity(pos) instanceof MasonJarBlockEntity blockEntity) {
			if (level instanceof ServerLevel serverLevel) {
				MasonJarBlockEntity.MasonJarItemStackHandler handler = blockEntity.getItemHandler();
				if (heldStack.isEmpty()) {
					if (!handler.extractItem(0, Integer.MAX_VALUE, true).isEmpty()) {
						ItemStack attainedStack = handler.extractItem(0, Integer.MAX_VALUE, false);
						player.setItemInHand(hand, attainedStack);
						serverLevel.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);
					} else {
						serverLevel.playSound(null, pos, SoundEvents.DECORATED_POT_INSERT_FAIL, SoundSource.BLOCKS, 1.0F, 1.0F);
						blockEntity.wobble(DecoratedPotBlockEntity.WobbleStyle.NEGATIVE);
					}
					return ItemInteractionResult.SUCCESS;
				} else if (handler.insertItem(0, heldStack, true).getCount() < heldStack.getCount()) {
					player.awardStat(Stats.ITEM_USED.get(heldStack.getItem()));
					ItemStack inserted = heldStack.copy();
					ItemStack returned = handler.insertItem(0, heldStack, false);

					player.setItemInHand(hand, returned);
					float pitch = (float) (inserted.getCount() - returned.getCount()) / (float) inserted.getMaxStackSize();
					serverLevel.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1.0F, 0.7F + 0.5F * pitch); // FIXME

					serverLevel.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);
					return ItemInteractionResult.SUCCESS;
				}
				return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
			} else {
				return ItemInteractionResult.CONSUME;
			}
		} else {
			return ItemInteractionResult.SKIP_DEFAULT_BLOCK_INTERACTION;
		}
	}

	@Override
	protected List<ItemStack> getDrops(BlockState state, LootParams.Builder params) {
		if (params.getOptionalParameter(LootContextParams.BLOCK_ENTITY) instanceof MasonJarBlockEntity jarBlockEntity) {
			params = params.withDynamicDrop(ShulkerBoxBlock.CONTENTS, stackConsumer ->
				stackConsumer.accept(jarBlockEntity.getItemHandler().getItem()));
		}

		return super.getDrops(state, params);
	}

	@Override
	protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
		Containers.dropContentsOnDestroy(state, newState, level, pos);
		super.onRemove(state, level, pos, newState, movedByPiston);
	}

	@Override
	public ItemStack getCloneItemStack(BlockState state, HitResult target, LevelReader level, BlockPos pos, Player player) {
		return level.getBlockEntity(pos) instanceof MasonJarBlockEntity jarBlockEntity
			? jarBlockEntity.getJarAsItem()
			: super.getCloneItemStack(state, target, level, pos, player);
	}

	@Override
	@SuppressWarnings("deprecation")
	protected boolean hasAnalogOutputSignal(BlockState state) {
		return true;
	}

	@Override
	@SuppressWarnings("deprecation")
	protected int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
		return AbstractContainerMenu.getRedstoneSignalFromBlockEntity(level.getBlockEntity(pos));
	}
}
