package twilightforest.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.InteractionResult;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.AABB;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import twilightforest.TFSounds;
import twilightforest.client.particle.TFParticleType;
import twilightforest.util.WorldUtil;
import twilightforest.world.components.chunkgenerators.ChunkGeneratorTwilight;

import java.util.Random;

public class CastleDoorBlock extends Block {

	public static final BooleanProperty ACTIVE = BooleanProperty.create("active");
	public static final BooleanProperty VANISHED = BooleanProperty.create("vanish");

	private static final VoxelShape REAPPEARING_BB = Shapes.create(new AABB(0.375F, 0.375F, 0.375F, 0.625F, 0.625F, 0.625F));

	public CastleDoorBlock(Properties props) {
		super(props);
		this.registerDefaultState(stateDefinition.any().setValue(ACTIVE, false).setValue(VANISHED, false));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(ACTIVE, VANISHED);
	}

	@Override
	@Deprecated
	public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return state.getValue(VANISHED) ? Shapes.empty() : super.getCollisionShape(state, world, pos, context);
	}

	@Override
	@Deprecated
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return state.getValue(VANISHED) ? REAPPEARING_BB : super.getShape(state, world, pos, context);
	}

	@Override
	@Deprecated
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
		return onActivation(world, pos, state);
	}

	@Override
	@Deprecated
	public void neighborChanged(BlockState state, Level world, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
		if (!(block instanceof CastleDoorBlock) && world.hasNeighborSignal(pos)) {
			onActivation(world, pos, state);
		}
	}

	@Override
	public PushReaction getPistonPushReaction(BlockState pState) {
		return PushReaction.BLOCK;
	}

	private InteractionResult onActivation(Level world, BlockPos pos, BlockState state) {

		if (state.getValue(VANISHED) || state.getValue(ACTIVE)) return InteractionResult.FAIL;

		if (isBlockLocked(world, pos)) {
			world.playSound(null, pos, TFSounds.DOOR_ACTIVATED, SoundSource.BLOCKS, 1.0F, 0.3F);
			return InteractionResult.PASS;
		} else {
			changeToActiveBlock(world, pos, state);
			return InteractionResult.SUCCESS;
		}
	}

	private static void changeToActiveBlock(Level world, BlockPos pos, BlockState originState) {
		if (originState.getBlock() instanceof CastleDoorBlock) {
			world.setBlockAndUpdate(pos, originState.setValue(ACTIVE, true));
		}
		world.getBlockTicks().scheduleTick(pos, originState.getBlock(), 2 + world.random.nextInt(5));
	}

	private static boolean isBlockLocked(Level world, BlockPos pos) {
		// check if we are in a structure, and if that structure says that we are locked
		if (!world.isClientSide) {
			ChunkGeneratorTwilight generator = WorldUtil.getChunkGenerator(world);
			//return generator != null && generator.isStructureLocked(pos, lockIndex);
		}
		return false;
	}

	@Override
	@Deprecated
	public void tick(BlockState state, ServerLevel world, BlockPos pos, Random random) {
		if (state.getValue(VANISHED)) {
			if (state.getValue(ACTIVE)) {
				world.setBlockAndUpdate(pos, state.setValue(VANISHED, false).setValue(ACTIVE, false));
			} else {
				changeToActiveBlock(world, pos, state);
			}
			playReappearSound(world, pos);
		} else {
			if (state.getValue(ACTIVE)) {
				world.setBlockAndUpdate(pos, state.setValue(VANISHED, true).setValue(ACTIVE, false));
				world.getBlockTicks().scheduleTick(pos, this, 80);

				playVanishSound(world, pos);

				vanishParticles(world, pos);

				// activate all adjacent inactive doors
				for (Direction e : Direction.values()) {
					checkAndActivateCastleDoor(world, pos.relative(e));
				}
			}
		}
	}

	private static void playVanishSound(Level world, BlockPos pos) {
		world.playSound(null, pos, TFSounds.DOOR_VANISH, SoundSource.BLOCKS, 0.125f, world.random.nextFloat() * 0.25F + 1.75F);
	}

	private static void playReappearSound(Level world, BlockPos pos) {
		world.playSound(null, pos, TFSounds.DOOR_REAPPEAR, SoundSource.BLOCKS, 0.125f, world.random.nextFloat() * 0.25F + 1.25F);
	}

	/**
	 * If the targeted block is a vanishing block, activate it
	 */
	public static void checkAndActivateCastleDoor(Level world, BlockPos pos) {
		BlockState state = world.getBlockState(pos);

		if (state.getBlock() instanceof CastleDoorBlock && !state.getValue(VANISHED) && !state.getValue(ACTIVE) && !isBlockLocked(world, pos)) {
			changeToActiveBlock(world, pos, state);
		}
	}

	private static void vanishParticles(Level world, BlockPos pos) {
		Random rand = world.getRandom();
		if(world instanceof ServerLevel) {
			for (int dx = 0; dx < 4; ++dx) {
				for (int dy = 0; dy < 4; ++dy) {
					for (int dz = 0; dz < 4; ++dz) {

						double x = pos.getX() + (dx + 0.5D) / 4;
						double y = pos.getY() + (dy + 0.5D) / 4;
						double z = pos.getZ() + (dz + 0.5D) / 4;

						double speed = rand.nextGaussian() * 0.2D;

						((ServerLevel)world).sendParticles(TFParticleType.ANNIHILATE.get(), x, y, z, 1, 0, 0, 0, speed);
					}
				}
			}
		}
	}
}
