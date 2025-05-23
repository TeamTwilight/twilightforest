package twilightforest.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;
import twilightforest.init.TFParticleType;
import twilightforest.init.TFSounds;
import twilightforest.network.ParticlePacket;

public class CastleDoorBlock extends Block {

	public static final BooleanProperty ACTIVE = BooleanProperty.create("active");
	public static final BooleanProperty VANISHED = BooleanProperty.create("vanish");

	private static final VoxelShape REAPPEARING_BB = Shapes.create(new AABB(0.375F, 0.375F, 0.375F, 0.625F, 0.625F, 0.625F));

	public CastleDoorBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.getStateDefinition().any().setValue(ACTIVE, false).setValue(VANISHED, false));
	}

	private static boolean isBlockLocked(Level level, BlockPos pos) {
		// check if we are in a structure, and if that structure says that we are locked
		// TODO is this method even needed any more? Might be needed in the future when the structure comes with internal gating progression - don't delete this method quite yet
		return false;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(ACTIVE, VANISHED);
	}

	@Override
	public boolean skipRendering(BlockState state, BlockState otherState, Direction direction) {
		return otherState.getBlock() instanceof CastleDoorBlock && otherState.getValue(VANISHED) == state.getValue(VANISHED);
	}

	@Override
	public VoxelShape getOcclusionShape(BlockState state) {
		return state.getValue(VANISHED) || !state.getValue(ACTIVE) ? Shapes.empty() : super.getOcclusionShape(state);
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
		return state.getValue(VANISHED) ? Shapes.empty() : super.getCollisionShape(state, getter, pos, context);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
		return state.getValue(VANISHED) ? REAPPEARING_BB : super.getShape(state, getter, pos, context);
	}

	@Override
	protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
		return this.onActivation(level, pos, state);
	}

	@Override
	public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, @Nullable Orientation orientation, boolean isMoving) {
		if (!(block instanceof CastleDoorBlock) && level.hasNeighborSignal(pos)) {
			this.onActivation(level, pos, state);
		}
	}

	private InteractionResult onActivation(Level level, BlockPos pos, BlockState state) {
		if (state.getValue(VANISHED) || state.getValue(ACTIVE)) return InteractionResult.FAIL;

		if (isBlockLocked(level, pos)) {
			level.playSound(null, pos, TFSounds.DOOR_ACTIVATED.get(), SoundSource.BLOCKS, 1.0F, 0.3F);
			return InteractionResult.PASS;
		} else {
			changeToActiveBlock(level, pos, state);
			return InteractionResult.SUCCESS;
		}
	}

	private void changeToActiveBlock(Level level, BlockPos pos, BlockState originState) {
		if (originState.getBlock() instanceof CastleDoorBlock) {
			level.setBlockAndUpdate(pos, originState.setValue(ACTIVE, true));
		}
		level.scheduleTick(pos, originState.getBlock(), 2 + level.getRandom().nextInt(5));
	}

	@Override
	public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		if (state.getValue(VANISHED)) {
			if (state.getValue(ACTIVE)) {
				level.setBlockAndUpdate(pos, state.setValue(VANISHED, false).setValue(ACTIVE, false));
			} else {
				this.changeToActiveBlock(level, pos, state);
			}
			this.playReappearSound(level, pos);
		} else {
			if (state.getValue(ACTIVE)) {
				level.setBlockAndUpdate(pos, state.setValue(VANISHED, true).setValue(ACTIVE, false));
				level.scheduleTick(pos, this, 80);

				this.playVanishSound(level, pos);

				this.vanishParticles(level, pos);

				// activate all adjacent inactive doors
				for (Direction e : Direction.values()) {
					this.checkAndActivateCastleDoor(level, pos.relative(e));
				}
			}
		}
	}

	private void playVanishSound(Level level, BlockPos pos) {
		level.playSound(null, pos, TFSounds.DOOR_VANISH.get(), SoundSource.BLOCKS, 0.125f, level.getRandom().nextFloat() * 0.25F + 1.75F);
	}

	private void playReappearSound(Level level, BlockPos pos) {
		level.playSound(null, pos, TFSounds.DOOR_REAPPEAR.get(), SoundSource.BLOCKS, 0.125f, level.getRandom().nextFloat() * 0.25F + 1.25F);
	}

	/**
	 * If the targeted block is a vanishing block, activate it
	 */
	public void checkAndActivateCastleDoor(Level level, BlockPos pos) {
		BlockState state = level.getBlockState(pos);

		if (state.getBlock() instanceof CastleDoorBlock && !state.getValue(VANISHED) && !state.getValue(ACTIVE) && !isBlockLocked(level, pos)) {
			this.changeToActiveBlock(level, pos, state);
		}
	}

	private void vanishParticles(Level level, BlockPos pos) {
		if (level instanceof ServerLevel serverLevel) {
			RandomSource rand = level.getRandom();
			ParticlePacket particlePacket = new ParticlePacket();
			for (int dx = 0; dx < 4; ++dx) {
				for (int dy = 0; dy < 4; ++dy) {
					for (int dz = 0; dz < 4; ++dz) {
						particlePacket.queueParticle(TFParticleType.ANNIHILATE.get(),
							pos.getX() + (dx + 0.5D) / 4,
							pos.getY() + (dy + 0.5D) / 4,
							pos.getZ() + (dz + 0.5D) / 4,
							rand.nextGaussian() * 0.2D, rand.nextGaussian() * 0.2D, rand.nextGaussian() * 0.2D);
					}
				}
			}
			PacketDistributor.sendToPlayersNear(serverLevel, null, pos.getX(), pos.getY(), pos.getZ(), 32.0, particlePacket);
		}
	}
}
