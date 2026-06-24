package twilightforest.block;

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
import net.minecraft.util.Util;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.*;
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
import org.jspecify.annotations.Nullable;
import twilightforest.init.*;
import twilightforest.tags.TFEntityTypeTags;

public abstract class CritterBlock extends BaseEntityBlock {

	public static final EnumProperty<Direction> FACING = DirectionalBlock.FACING;
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	private final VoxelShape DOWN_BB = Shapes.create(new AABB(0.2F, 0.85F, 0.2F, 0.8F, 1.0F, 0.8F));
	private final VoxelShape UP_BB = Shapes.create(new AABB(0.2F, 0.0F, 0.2F, 0.8F, 0.15F, 0.8F));
	private final VoxelShape NORTH_BB = Shapes.create(new AABB(0.2F, 0.2F, 0.85F, 0.8F, 0.8F, 1.0F));
	private final VoxelShape SOUTH_BB = Shapes.create(new AABB(0.2F, 0.2F, 0.0F, 0.8F, 0.8F, 0.15F));
	private final VoxelShape WEST_BB = Shapes.create(new AABB(0.85F, 0.2F, 0.2F, 1.0F, 0.8F, 0.8F));
	private final VoxelShape EAST_BB = Shapes.create(new AABB(0.0F, 0.2F, 0.2F, 0.15F, 0.8F, 0.8F));

	protected CritterBlock(Properties properties) {
		super(properties);
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

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		Direction clicked = context.getClickedFace();
		FluidState fluidstate = context.getLevel().getFluidState(context.getClickedPos());
		BlockState state = this.defaultBlockState().setValue(FACING, clicked).trySetValue(WATERLOGGED, fluidstate.getType() == Fluids.WATER);

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
	protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbor, BlockPos neighborPos, BlockState neighborState, RandomSource random) {
		if (state.getValue(FACING).getOpposite() == directionToNeighbor && !state.canSurvive(level, pos)) {
			return Blocks.AIR.defaultBlockState();
		} else {
			return super.updateShape(state, level, ticks, pos, directionToNeighbor, neighborPos, neighborState, random);
		}
	}

	@Override
	public boolean canSurvive(BlockState state, LevelReader reader, BlockPos pos) {
		Direction facing = state.getValue(DirectionalBlock.FACING);
		return canSurvive(reader, pos, facing);
	}

	@Override
	public BlockState rotate(BlockState state, Rotation rotation) {
		return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
	}

	@Override
	protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
		if (stack.is(TFItems.MASON_JAR.asItem())) {
			ItemContainerContents contents = stack.getComponents().get(DataComponents.CONTAINER);
			if (contents == null || contents.copyOne().isEmpty()) {
				if (this == TFBlocks.FIREFLY.get()) {
					ItemStack newStack = Util.make(new ItemStack(TFBlocks.FIREFLY_JAR.get()), jar -> jar.set(TFDataComponents.JAR_LID.get(), stack.get(TFDataComponents.JAR_LID.get())));
					stack.consume(1, player);
					player.getInventory().add(newStack);
					level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
					return InteractionResult.SUCCESS;
				} else if (this == TFBlocks.CICADA.get()) {
					ItemStack newStack = Util.make(new ItemStack(TFBlocks.CICADA_JAR.get()), jar -> jar.set(TFDataComponents.JAR_LID.get(), stack.get(TFDataComponents.JAR_LID.get())));
					stack.consume(1, player);
					player.getInventory().add(newStack);
					if (level.isClientSide())
						Minecraft.getInstance().getSoundManager().stop(TFSounds.CICADA.get().location(), SoundSource.NEUTRAL);
					level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
					return InteractionResult.SUCCESS;
				}
			}
		}
		return InteractionResult.PASS;
	}

	@Override
	protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity, InsideBlockEffectApplier effectApplier, boolean isPrecise) {
		if ((entity instanceof Projectile && !entity.is(TFEntityTypeTags.DONT_KILL_BUGS)) || entity instanceof FallingBlockEntity) {
			level.setBlockAndUpdate(pos, state.hasProperty(WATERLOGGED) && state.getValue(WATERLOGGED) ? Blocks.WATER.defaultBlockState() : Blocks.AIR.defaultBlockState());
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
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}
}
