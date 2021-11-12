package twilightforest.block;

import com.mojang.math.Vector3f;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.util.*;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import twilightforest.TFSounds;

import java.util.*;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;

/**
 * Block that disappears then reappears after a short delay.
 * Blockstate lifecycle: [active=false, vanished=false] -> right click or redstone
 * -> [active=true, vanished=false] -> delay -> [active=false, vanished=true]
 * -> delay -> [active=true, vanished=true] -> delay -> initial state
 *
 * If the block has no "vanished" state property registered, it simply deletes itself after the first delay.
 * @see ReappearingBlock , It is only separated from this class because vanilla does
 * not like having blockstate properties be conditionally registered.
 */
public class VanishingBlock extends Block {
	public static final BooleanProperty ACTIVE = BooleanProperty.create("active");
	public static final BooleanProperty VANISHED = BooleanProperty.create("vanished");
	private static final VoxelShape VANISHED_SHAPE = box(6, 6, 6, 10, 10, 10);

	public VanishingBlock(Properties props) {
		super(props);
		this.registerDefaultState(stateDefinition.any().setValue(ACTIVE, false));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(ACTIVE);
	}

	private boolean isVanished(BlockState state) {
		return state.hasProperty(VANISHED) && state.getValue(VANISHED);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext ctx) {
		return isVanished(state) ? VANISHED_SHAPE : super.getShape(state, world, pos, ctx);
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext ctx) {
		return isVanished(state) ? Shapes.empty() : super.getCollisionShape(state, world, pos, ctx);
	}

	@Override
	@Deprecated
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
		if (!isVanished(state) && !state.getValue(ACTIVE)) {
			if (areBlocksLocked(world, pos)) {
				world.playSound(null, pos, TFSounds.LOCKED_VANISHING_BLOCK, SoundSource.BLOCKS, 1.0F, 0.3F);
			} else {
				activate(world, pos);
			}
			return InteractionResult.SUCCESS;
		}

		return InteractionResult.PASS;
	}

	@Override
	public float getExplosionResistance(BlockState state, BlockGetter world, BlockPos pos, Explosion explosion) {
		return !state.getValue(ACTIVE) ? 6000F : super.getExplosionResistance(state, world, pos, explosion);
	}

	@Override
	public boolean canEntityDestroy(BlockState state, BlockGetter world, BlockPos pos, Entity entity) {
		return !state.getValue(ACTIVE) ? !areBlocksLocked(world, pos) : super.canEntityDestroy(state, world, pos, entity);
	}

	private static boolean areBlocksLocked(BlockGetter world, BlockPos start) {
		int limit = 512;
		Deque<BlockPos> queue = new ArrayDeque<>();
		Set<BlockPos> checked = new HashSet<>();
		queue.offer(start);

		for (int iter = 0; !queue.isEmpty() && iter < limit; iter++) {
			BlockPos cur = queue.pop();
			BlockState state = world.getBlockState(cur);
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
	@Deprecated
	public void neighborChanged(BlockState state, Level world, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
		if (world.isClientSide) {
			return;
		}

		if (!isVanished(state) && !state.getValue(ACTIVE) && world.hasNeighborSignal(pos) && !areBlocksLocked(world, pos)) {
			activate(world, pos);
		}
	}

	@Override
	@Deprecated
	public void tick(BlockState state, ServerLevel world, BlockPos pos, Random random) {
		if (world.isClientSide) {
			return;
		}

		if (isVanished(state)) {
			if (state.getValue(ACTIVE)) {
				world.setBlockAndUpdate(pos, state.setValue(VANISHED, false).setValue(ACTIVE, false));
			} else {
				world.setBlockAndUpdate(pos, state.setValue(ACTIVE, true));
				world.getBlockTicks().scheduleTick(pos, this, 15);
			}
			world.playSound(null, pos, TFSounds.REAPPEAR_BLOCK, SoundSource.BLOCKS, 0.3F, 0.6F);
		} else {
			if (state.getValue(ACTIVE)) {
				if (state.hasProperty(VANISHED)) {
					world.setBlockAndUpdate(pos, state.setValue(ACTIVE, false).setValue(VANISHED, true));
					world.getBlockTicks().scheduleTick(pos, this, 80);
				} else {
					world.removeBlock(pos, false);
				}

				world.playSound(null, pos, state.getBlock() == TFBlocks.REAPPEARING_BLOCK.get() ? TFSounds.REAPPEAR_POOF : TFSounds.VANISHING_BLOCK, SoundSource.BLOCKS, 0.3F, 0.5F);

				for (Direction e : Direction.values()) {
					activate(world, pos.relative(e));
				}
			}
		}
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState state, Level world, BlockPos pos, Random random) {
		if (state.getValue(ACTIVE)) {
			this.sparkle(world, pos);
		}
	}

	// [VanillaCopy] BlockRedstoneOre.spawnParticles. Unchanged.
	public void sparkle(Level worldIn, BlockPos pos) {
		Random random = worldIn.random;
		double d0 = 0.0625D;

		for (int i = 0; i < 6; ++i) {
			double d1 = (float) pos.getX() + random.nextFloat();
			double d2 = (float) pos.getY() + random.nextFloat();
			double d3 = (float) pos.getZ() + random.nextFloat();

			if (i == 0 && !worldIn.getBlockState(pos.above()).isSolidRender(worldIn, pos)) {
				d2 = (double) pos.getY() + d0 + 1.0D;
			}

			if (i == 1 && !worldIn.getBlockState(pos.below()).isSolidRender(worldIn, pos)) {
				d2 = (double) pos.getY() - d0;
			}

			if (i == 2 && !worldIn.getBlockState(pos.south()).isSolidRender(worldIn, pos)) {
				d3 = (double) pos.getZ() + d0 + 1.0D;
			}

			if (i == 3 && !worldIn.getBlockState(pos.north()).isSolidRender(worldIn, pos)) {
				d3 = (double) pos.getZ() - d0;
			}

			if (i == 4 && !worldIn.getBlockState(pos.east()).isSolidRender(worldIn, pos)) {
				d1 = (double) pos.getX() + d0 + 1.0D;
			}

			if (i == 5 && !worldIn.getBlockState(pos.west()).isSolidRender(worldIn, pos)) {
				d1 = (double) pos.getX() - d0;
			}

			float f1 = 0.6F + 0.4F;
			float f2 = Math.max(0.0F, 1.0F * 1.0F * 0.7F - 0.5F);
			float f3 = Math.max(0.0F, 1.0F * 1.0F * 0.6F - 0.7F);
			if (d1 < (double) pos.getX() || d1 > (double) (pos.getX() + 1) || d2 < 0.0D || d2 > (double) (pos.getY() + 1) || d3 < (double) pos.getZ() || d3 > (double) (pos.getZ() + 1)) {
				worldIn.addParticle(new DustParticleOptions(new Vector3f(f1, f2, f3), 1.0F), d1, d2, d3, 0.0D, 0.0D, 0.0D);
			}
		}
	}

	private void activate(Level world, BlockPos pos) {
		BlockState state = world.getBlockState(pos);
		if (state.getBlock() instanceof VanishingBlock && !isVanished(state) && !state.getValue(ACTIVE)) {
			world.setBlockAndUpdate(pos, state.setValue(ACTIVE, true));
			world.getBlockTicks().scheduleTick(pos, state.getBlock(), 2 + world.random.nextInt(5));
		}
	}
}
