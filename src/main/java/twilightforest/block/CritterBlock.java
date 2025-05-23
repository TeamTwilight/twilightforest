package twilightforest.block;

import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import twilightforest.tags.TFEntityTypeTags;
import twilightforest.init.*;

public abstract class CritterBlock extends BaseEntityBlock implements SimpleWaterloggedBlock {

	public static final EnumProperty<Direction> FACING = DirectionalBlock.FACING;
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	protected final VoxelShape DOWN_BB = Shapes.create(new AABB(0.2F, 0.85F, 0.2F, 0.8F, 1.0F, 0.8F));
	protected final VoxelShape UP_BB = Shapes.create(new AABB(0.2F, 0.0F, 0.2F, 0.8F, 0.15F, 0.8F));
	protected final VoxelShape NORTH_BB = Shapes.create(new AABB(0.2F, 0.2F, 0.85F, 0.8F, 0.8F, 1.0F));
	protected final VoxelShape SOUTH_BB = Shapes.create(new AABB(0.2F, 0.2F, 0.0F, 0.8F, 0.8F, 0.15F));
	protected final VoxelShape WEST_BB = Shapes.create(new AABB(0.85F, 0.2F, 0.2F, 1.0F, 0.8F, 0.8F));
	protected final VoxelShape EAST_BB = Shapes.create(new AABB(0.0F, 0.2F, 0.2F, 0.15F, 0.8F, 0.8F));

	protected CritterBlock(Properties properties) {
		super(properties);
		this.initDefaultState();
	}

	protected void initDefaultState() {
		this.registerDefaultState(this.getStateDefinition().any().setValue(FACING, Direction.UP));
	}

	public static boolean canSurvive(LevelReader reader, BlockPos pos, Direction facing) {
		BlockPos restingPos = pos.relative(facing.getOpposite());
		return canSupportCenter(reader, restingPos, facing) || reader.getBlockState(restingPos).getBlock() instanceof LeavesBlock;
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
		return switch (state.getValue(FACING)) {
			case DOWN -> DOWN_BB;
			case NORTH -> NORTH_BB;
			case SOUTH -> SOUTH_BB;
			case WEST -> WEST_BB;
			case EAST -> EAST_BB;
			default -> UP_BB;
		};
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		Direction clicked = context.getClickedFace();
		BlockState state = defaultBlockState().setValue(FACING, clicked);

		if (canSurvive(state, context.getLevel(), context.getClickedPos())) {
			return state;
		}

		for (Direction dir : context.getNearestLookingDirections()) {
			state = defaultBlockState().setValue(FACING, dir.getOpposite());
			if (canSurvive(state, context.getLevel(), context.getClickedPos())) {
				return state;
			}
		}
		return null;
	}

	@Override
	protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess scheduledTickAccess, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, RandomSource random) {
		if (state.getValue(FACING).getOpposite() == direction && !state.canSurvive(level, pos)) {
			return Blocks.AIR.defaultBlockState();
		} else {
			return super.updateShape(state, level, scheduledTickAccess, pos, direction, neighborPos, neighborState, random);
		}
	}

	@Override
	public boolean canSurvive(BlockState state, LevelReader reader, BlockPos pos) {
		Direction facing = state.getValue(DirectionalBlock.FACING);
		return canSurvive(reader, pos, facing);
	}

	@Override
	@SuppressWarnings("deprecation")
	public BlockState rotate(BlockState state, Rotation rotation) {
		return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
	}

	@Override
	protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
		if (stack.getItem() == TFItems.MASON_JAR.asItem()) {
			ItemContainerContents contents = stack.getComponents().get(DataComponents.CONTAINER);
			if (contents == null || contents.copyOne().isEmpty()) {
				return this.onJarAttempt(stack, state, level, pos, player, hand, result);
			}
		}
		return InteractionResult.PASS;
	}

	protected InteractionResult onJarAttempt(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
		return InteractionResult.PASS;
	}

	@Override
	public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
		if ((entity instanceof Projectile && !entity.getType().is(TFEntityTypeTags.DONT_KILL_BUGS)) || entity instanceof FallingBlockEntity) {
			level.setBlockAndUpdate(pos, state.getFluidState().createLegacyBlock());
			if (level.isClientSide())
				Minecraft.getInstance().getSoundManager().stop(TFSounds.CICADA.get().location(), SoundSource.NEUTRAL);

			level.playSound(null, pos, TFSounds.BUG_SQUISH.get(), SoundSource.BLOCKS, 1.0F, 1.0F);

			if (level instanceof ServerLevel serverLevel && this.getSquishLootTable() != null) {
				LootParams ctx = new LootParams.Builder(serverLevel).withParameter(LootContextParams.BLOCK_STATE, state).withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(pos)).withParameter(LootContextParams.TOOL, ItemStack.EMPTY).create(LootContextParamSets.BLOCK);
				serverLevel.getServer().reloadableRegistries().getLootTable(this.getSquishLootTable()).getRandomItems(ctx).forEach((stack) -> popResource(serverLevel, pos, stack));
			}

			for (int i = 0; i < 50; i++) {
				boolean wallBug = state.getValue(FACING) != Direction.UP;
				level.addParticle(new BlockParticleOption(ParticleTypes.BLOCK, Blocks.SLIME_BLOCK.defaultBlockState()),
					pos.getX() + Mth.nextFloat(level.getRandom(), 0.25F, 0.75F),
					pos.getY() + (wallBug ? 0.5F : 0.0F),
					pos.getZ() + Mth.nextFloat(level.getRandom(), 0.25F, 0.75F),
					0.0D, 0.0D, 0.0D);
			}
			if (entity instanceof Projectile projectile && projectile.getOwner() instanceof ServerPlayer player) {
				player.awardStat(TFStats.BUGS_SQUISHED.get());
				TFAdvancements.KILL_BUG.get().trigger(player, state);
			}
		}
	}

	@Nullable
	public abstract ResourceKey<LootTable> getSquishLootTable(); // Oh, no!

	@Override
	public abstract BlockEntity newBlockEntity(BlockPos pos, BlockState state);

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}

	public abstract static class WaterLoggable extends CritterBlock implements SimpleWaterloggedBlock {
		public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

		protected WaterLoggable(Properties properties) {
			super(properties);
		}

		@Override
		protected void initDefaultState() {
			this.registerDefaultState(this.getStateDefinition().any().setValue(FACING, Direction.UP).setValue(WATERLOGGED, Boolean.FALSE));
		}


		@Override
		public BlockState getStateForPlacement(BlockPlaceContext context) {
			BlockState state = super.getStateForPlacement(context);
			if (state != null) {
				FluidState fluidstate = context.getLevel().getFluidState(context.getClickedPos());
				state = state.setValue(WATERLOGGED, fluidstate.getType() == Fluids.WATER);
			}
			return state;
		}

		@Override
		public FluidState getFluidState(BlockState state) {
			return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
		}

		@Override
		protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess scheduledTickAccess, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, RandomSource random) {
			if (state.getValue(WATERLOGGED)) {
				scheduledTickAccess.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
			}
			return super.updateShape(state, level, scheduledTickAccess, pos, direction, neighborPos, neighborState, random);
		}

		@Override
		protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
			super.createBlockStateDefinition(builder);
			builder.add(WATERLOGGED);
		}
	}
}
