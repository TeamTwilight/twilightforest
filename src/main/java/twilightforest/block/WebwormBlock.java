package twilightforest.block;

import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import twilightforest.block.entity.WebwormBlockEntity;
import twilightforest.init.*;
import twilightforest.loot.TFLootTables;
import twilightforest.tags.TFEntityTypeTags;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class WebwormBlock extends CritterBlock {
	public static final MapCodec<WebwormBlock> CODEC = simpleCodec(WebwormBlock::new);
	public static final BooleanProperty UP = PipeBlock.UP;
	public static final BooleanProperty DOWN = PipeBlock.DOWN;
	public static final BooleanProperty NORTH = PipeBlock.NORTH;
	public static final BooleanProperty EAST = PipeBlock.EAST;
	public static final BooleanProperty SOUTH = PipeBlock.SOUTH;
	public static final BooleanProperty WEST = PipeBlock.WEST;
	private final Map<BlockState, VoxelShape> shapesCache;

	public WebwormBlock(Properties properties) {
		super(properties);
		this.shapesCache = ImmutableMap.copyOf(this.stateDefinition.getPossibleStates().stream().collect(Collectors.toMap(Function.identity(), this::calculateShape)));
	}

	private VoxelShape calculateShape(BlockState state) {
		VoxelShape voxelshape = Shapes.empty();
		if (state.getValue(UP)) {
			voxelshape = HangingWebBlock.UP_AABB;
		}

		if (state.getValue(NORTH)) {
			voxelshape = Shapes.or(voxelshape, HangingWebBlock.NORTH_AABB);
		}

		if (state.getValue(SOUTH)) {
			voxelshape = Shapes.or(voxelshape, HangingWebBlock.SOUTH_AABB);
		}

		if (state.getValue(EAST)) {
			voxelshape = Shapes.or(voxelshape, HangingWebBlock.EAST_AABB);
		}

		if (state.getValue(WEST)) {
			voxelshape = Shapes.or(voxelshape, HangingWebBlock.WEST_AABB);
		}

		switch (state.getValue(FACING)) {
			case DOWN -> voxelshape = Shapes.or(voxelshape, this.DOWN_BB);
			case NORTH -> voxelshape = Shapes.or(voxelshape, this.NORTH_BB);
			case SOUTH -> voxelshape = Shapes.or(voxelshape, this.SOUTH_BB);
			case WEST -> voxelshape = Shapes.or(voxelshape, this.WEST_BB);
			case EAST -> voxelshape = Shapes.or(voxelshape, this.EAST_BB);
			default -> voxelshape = Shapes.or(voxelshape, this.UP_BB);
		};

		return voxelshape.isEmpty() ? Shapes.block() : voxelshape;
	}

	@Override
	protected void initDefaultState() {
		this.registerDefaultState(this.stateDefinition.any()
			.setValue(FACING, Direction.UP)
			.setValue(UP, false)
			.setValue(DOWN, false)
			.setValue(NORTH, false)
			.setValue(EAST, false)
			.setValue(SOUTH, false)
			.setValue(WEST, false)
		);
	}

	@Override
	@SuppressWarnings("deprecation")
	protected RenderShape getRenderShape(BlockState state) {
		return RenderShape.MODEL;
	}

	@Override
	protected MapCodec<? extends BaseEntityBlock> codec() {
		return CODEC;
	}

	@Override
	protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		if (level.random.nextInt(4) != 0) return;

		Direction face = state.getValue(FACING).getOpposite();
		BooleanProperty faceProperty = PipeBlock.PROPERTY_BY_DIRECTION.get(face);
        if (!state.getValue(faceProperty)) {
			level.setBlockAndUpdate(pos, state.setValue(faceProperty, true));
        } else if (face.getAxis().isHorizontal()) {
			for (int y = 1; y < getWebLengthForPos(pos, face); y++) {
				BlockPos down = pos.below(y);
				if (level.isAreaLoaded(down, 4)) {
					BlockState downState = level.getBlockState(down);
					if (downState.is(this)) {
                        if (!downState.getValue(faceProperty)) {
                            level.setBlockAndUpdate(down, downState.setValue(faceProperty, true));
							return;
                        }
                    } else if (downState.is(TFBlocks.HANGING_WEB)) {
						if (!HangingWebBlock.checkFace(downState, face)) {
                            level.setBlockAndUpdate(down, downState.setValue(HangingWebBlock.getPropertyForFace(face), HangingWebBlock.getPropertyForPlacement(level, down, face)));
							return;
                        }
					} else if (downState.isAir()) {
						level.setBlockAndUpdate(down, TFBlocks.HANGING_WEB.get().defaultBlockState().setValue(HangingWebBlock.getPropertyForFace(face), HangingWebBlock.getPropertyForPlacement(level, down, face)));
						return;
					} else break;
				} else return;
			}
		}
	}

	public static int getWebLengthForPos(BlockPos pos, Direction face) {
		return RandomSource.create(pos.asLong() + face.ordinal()).nextInt(4) + 3;
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new WebwormBlockEntity(pos, state);
	}

	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		return createTickerHelper(type, TFBlockEntities.WEBWORM.get(), WebwormBlockEntity::tick);
	}

	@Override
	public ResourceKey<LootTable> getSquishLootTable() {
		return TFLootTables.WEBWORM_SQUISH_DROPS;
	}

	@Override
	public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
		if ((entity instanceof Projectile && !entity.getType().is(TFEntityTypeTags.DONT_KILL_BUGS)) || entity instanceof FallingBlockEntity) {
			BlockState web = TFBlocks.HANGING_WEB.get().defaultBlockState();
			boolean any = false;
			for (Direction direction : Direction.values()) {
				if (state.getValue(PipeBlock.PROPERTY_BY_DIRECTION.get(direction))) {
					if (direction.getAxis().isHorizontal()) {
						web = web.setValue(HangingWebBlock.getPropertyForFace(direction), HangingWebBlock.getPropertyForPlacement(level, pos, direction));
					} else {
						web = web.setValue(PipeBlock.PROPERTY_BY_DIRECTION.get(direction), true);
					}
					any = true;
				}
			}
			level.setBlockAndUpdate(pos, any ? web : Blocks.AIR.defaultBlockState());

			level.playSound(null, pos, TFSounds.BUG_SQUISH.get(), SoundSource.BLOCKS, 1.0F, 1.0F);

			if (level instanceof ServerLevel serverLevel && this.getSquishLootTable() != null) {
				LootParams ctx = new LootParams.Builder(serverLevel).withParameter(LootContextParams.BLOCK_STATE, state).withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(pos)).withParameter(LootContextParams.TOOL, ItemStack.EMPTY).create(LootContextParamSets.BLOCK);
				serverLevel.getServer().reloadableRegistries().getLootTable(this.getSquishLootTable()).getRandomItems(ctx).forEach((stack) -> popResource(serverLevel, pos, stack));
			}

			for (int i = 0; i < 50; i++) {
				boolean wallBug = state.getValue(FACING).getAxis() != Direction.Axis.Y;
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
			return;
		}

		if (entity instanceof Spider) return;
		if (entity instanceof LivingEntity livingentity && (livingentity.hasEffect(MobEffects.WEAVING) || livingentity.isShiftKeyDown())) return;
		List<Direction> propertyList = new ArrayList<>();
		for (Direction direction : Direction.values()) {
			if (state.getValue(PipeBlock.PROPERTY_BY_DIRECTION.get(direction))) propertyList.add(direction);
		}
		if (!propertyList.isEmpty()) {
			entity.makeStuckInBlock(state, new Vec3(0.95, 0.95, 0.95));
			// dissolve
			if (!level.isClientSide() && entity instanceof LivingEntity living && living.getRandom().nextInt(20) == 1) {
				BlockState newState = this.defaultBlockState().setValue(FACING, state.getValue(FACING));
				level.setBlockAndUpdate(pos, newState);

				BlockState web = TFBlocks.HANGING_WEB.get().defaultBlockState();
				for (Direction property : propertyList) {
					if (property.getAxis().isHorizontal()) {
						web = web.setValue(HangingWebBlock.getPropertyForFace(property), HangingWebBlock.getPropertyForPlacement(level, pos, property));
					} else {
						web = web.setValue(PipeBlock.PROPERTY_BY_DIRECTION.get(property), true);
					}
				}

				level.levelEvent(2001, pos, Block.getId(web));

				Block.dropResources(web, level, pos, null, entity, ItemStack.EMPTY);

				level.gameEvent(GameEvent.BLOCK_DESTROY, pos, GameEvent.Context.of(entity, web));
			}
		}
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		Direction clicked = context.getClickedFace();
		BlockState defaultState = this.defaultBlockState();

		BlockState blockState = context.getLevel().getBlockState(context.getClickedPos());

		if (blockState.is(TFBlocks.HANGING_WEB)) {
			for (Direction direction : Direction.values()) {
				defaultState = defaultState.setValue(PipeBlock.PROPERTY_BY_DIRECTION.get(direction), HangingWebBlock.checkFace(blockState, direction));
			}
        }

		BlockState state = defaultState.setValue(FACING, clicked);

		if (this.canSurvive(state, context.getLevel(), context.getClickedPos())) {
			return state;
		}

		for (Direction dir : context.getNearestLookingDirections()) {
			state = defaultState.setValue(FACING, dir.getOpposite());
			if (this.canSurvive(state, context.getLevel(), context.getClickedPos())) return state;
		}
		return null;
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return this.shapesCache.get(state);
	}

	@Override
	protected InteractionResult onJarAttempt(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
		stack.consume(1, player);
		player.getInventory().add(new ItemStack(TFBlocks.WEBWORM_JAR.get()));
		level.setBlockAndUpdate(pos, state.getFluidState().createLegacyBlock());
		return InteractionResult.SUCCESS;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING, UP, DOWN, NORTH, EAST, SOUTH, WEST);
	}
}
