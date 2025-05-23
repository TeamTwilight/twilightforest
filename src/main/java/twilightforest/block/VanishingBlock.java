package twilightforest.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFSounds;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;

/**
 * Block that disappears then reappears after a short delay.
 * Blockstate lifecycle: [active=false, vanished=false] -> right click or redstone
 * -> [active=true, vanished=false] -> delay -> [active=false, vanished=true]
 * -> delay -> [active=true, vanished=true] -> delay -> initial state
 * <p>
 * If the block has no "vanished" state property registered, it simply deletes itself after the first delay.
 *
 * @see ReappearingBlock, It is only separated from this class because vanilla does
 * not like having blockstate properties be conditionally registered.
 */
public class VanishingBlock extends Block {
	public static final BooleanProperty ACTIVE = BooleanProperty.create("active");
	public static final BooleanProperty VANISHED = BooleanProperty.create("vanished");
	private static final VoxelShape VANISHED_SHAPE = box(6, 6, 6, 10, 10, 10);

	public VanishingBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.getStateDefinition().any().setValue(ACTIVE, false));
	}

	private static boolean areBlocksLocked(BlockGetter getter, BlockPos start) {
		int limit = 512;
		Deque<BlockPos> queue = new ArrayDeque<>();
		Set<BlockPos> checked = new HashSet<>();
		queue.offer(start);

		for (int iter = 0; !queue.isEmpty() && iter < limit; iter++) {
			BlockPos cur = queue.pop();
			BlockState state = getter.getBlockState(cur);
			if (state.getBlock() == TFBlocks.LOCKED_VANISHING_BLOCK.get() && state.getValue(LockedVanishingBlock.LOCKED)) {
				return true;
			}

			checked.add(cur);

			if (state.getBlock() instanceof VanishingBlock) {
				for (Direction facing : Direction.values()) {
					BlockPos neighbor = cur.relative(facing);
					if (!checked.contains(neighbor)) {
						queue.offer(neighbor);
					}
				}
			}
		}

		return false;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(ACTIVE);
	}

	private boolean isVanished(BlockState state) {
		return state.hasProperty(VANISHED) && state.getValue(VANISHED);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext ctx) {
		return isVanished(state) ? VANISHED_SHAPE : super.getShape(state, getter, pos, ctx);
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext ctx) {
		return isVanished(state) ? Shapes.empty() : super.getCollisionShape(state, getter, pos, ctx);
	}

	@Override
	protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
		if (!this.isVanished(state) && !state.getValue(ACTIVE)) {
			if (areBlocksLocked(level, pos)) {
				level.playSound(null, pos, TFSounds.LOCKED_VANISHING_BLOCK.get(), SoundSource.BLOCKS, 1.0F, 0.3F);
			} else {
				this.activate(level, pos);
			}
			return InteractionResult.SUCCESS;
		}

		return InteractionResult.PASS;
	}

	@Override
	public float getExplosionResistance(BlockState state, BlockGetter getter, BlockPos pos, Explosion explosion) {
		return !state.getValue(ACTIVE) ? 6000F : super.getExplosionResistance(state, getter, pos, explosion);
	}

	@Override
	public boolean canEntityDestroy(BlockState state, BlockGetter getter, BlockPos pos, Entity entity) {
		return !state.getValue(ACTIVE) ? !areBlocksLocked(getter, pos) : super.canEntityDestroy(state, getter, pos, entity);
	}

	@Override
	public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, @Nullable Orientation orientation, boolean isMoving) {
		if (level.isClientSide()) {
			return;
		}

		if (!this.isVanished(state) && !state.getValue(ACTIVE) && level.hasNeighborSignal(pos) && !areBlocksLocked(level, pos)) {
			this.activate(level, pos);
		}
	}

	@Override
	public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		if (level.isClientSide()) {
			return;
		}

		if (this.isVanished(state)) {
			if (state.getValue(ACTIVE)) {
				level.setBlockAndUpdate(pos, state.setValue(VANISHED, false).setValue(ACTIVE, false));
			} else {
				level.setBlockAndUpdate(pos, state.setValue(ACTIVE, true));
				level.scheduleTick(pos, this, 15);
			}
			level.playSound(null, pos, TFSounds.REAPPEAR_BLOCK.get(), SoundSource.BLOCKS, 0.3F, 0.6F);
		} else {
			if (state.getValue(ACTIVE)) {
				if (state.hasProperty(VANISHED)) {
					level.setBlockAndUpdate(pos, state.setValue(ACTIVE, false).setValue(VANISHED, true));
					level.scheduleTick(pos, this, 80);
				} else {
					level.removeBlock(pos, false);
				}

				level.playSound(null, pos, state.getBlock() == TFBlocks.REAPPEARING_BLOCK.get() ? TFSounds.REAPPEAR_POOF.get() : TFSounds.VANISHING_BLOCK.get(), SoundSource.BLOCKS, 0.3F, 0.5F);

				for (Direction e : Direction.values()) {
					this.activate(level, pos.relative(e));
				}
			}
		}
	}

	@Override
	public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
		if (state.getValue(ACTIVE)) {
			this.sparkle(level, pos);
		}
	}

	// [VanillaCopy] RedstoneOreBlock.spawnParticles. Unchanged.
	public void sparkle(Level level, BlockPos pos) {
		RandomSource random = level.getRandom();

		for (Direction direction : Direction.values()) {
			BlockPos blockpos = pos.relative(direction);
			if (!level.getBlockState(blockpos).isSolidRender()) {
				Direction.Axis axis = direction.getAxis();
				double d1 = axis == Direction.Axis.X ? 0.5 + 0.5625 * (double) direction.getStepX() : (double) random.nextFloat();
				double d2 = axis == Direction.Axis.Y ? 0.5 + 0.5625 * (double) direction.getStepY() : (double) random.nextFloat();
				double d3 = axis == Direction.Axis.Z ? 0.5 + 0.5625 * (double) direction.getStepZ() : (double) random.nextFloat();
				level.addParticle(DustParticleOptions.REDSTONE, (double) pos.getX() + d1, (double) pos.getY() + d2, (double) pos.getZ() + d3, 0.0, 0.0, 0.0);
			}
		}
	}

	private void activate(Level level, BlockPos pos) {
		BlockState state = level.getBlockState(pos);
		if (state.getBlock() instanceof VanishingBlock && !isVanished(state) && !state.getValue(ACTIVE)) {
			level.setBlockAndUpdate(pos, state.setValue(ACTIVE, true));
			level.scheduleTick(pos, state.getBlock(), 2 + level.getRandom().nextInt(5));
		}
	}
}
