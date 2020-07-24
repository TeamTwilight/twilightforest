package twilightforest.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particles.RedstoneParticleData;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.*;

/**
 * Block that disappears then reappears after a short delay.
 * Blockstate lifecycle: [active=false, vanished=false] -> right click or redstone
 * -> [active=true, vanished=false] -> delay -> [active=false, vanished=true]
 * -> delay -> [active=true, vanished=true] -> delay -> initial state
 *
 * If the block has no "vanished" state property registered, it simply deletes itself after the first delay.
 * @see BlockReappearing, It is only separated from this class because vanilla does
 * not like having blockstate properties be conditionally registered.
 */
public class BlockTFVanishingBlock extends Block {
	public static final BooleanProperty ACTIVE = BooleanProperty.create("active");
	public static final BooleanProperty VANISHED = BooleanProperty.create("vanished");
	private static final VoxelShape VANISHED_SHAPE = makeCuboidShape(6, 6, 6, 10, 10, 10);

	public BlockTFVanishingBlock(Properties props) {
		super(props);
		this.setDefaultState(stateContainer.getBaseState().with(ACTIVE, false));
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		super.fillStateContainer(builder);
		builder.add(ACTIVE);
	}

	private boolean isVanished(BlockState state) {
		return state.hasProperty(VANISHED) && state.get(VANISHED);
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext ctx) {
		return isVanished(state) ? VANISHED_SHAPE : super.getShape(state, world, pos, ctx);
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext ctx) {
		return isVanished(state) ? VoxelShapes.empty() : super.getCollisionShape(state, world, pos, ctx);
	}

	@Override
	@Deprecated
	public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
		if (!isVanished(state) && !state.get(ACTIVE)) {
			if (areBlocksLocked(world, pos)) {
				world.playSound(null, pos, SoundEvents.BLOCK_WOODEN_BUTTON_CLICK_OFF, SoundCategory.BLOCKS, 1.0F, 0.3F);
			} else {
				activate(world, pos);
			}
			return ActionResultType.SUCCESS;
		}

		return ActionResultType.PASS;
	}

	@Override
	public float getExplosionResistance(BlockState state, IBlockReader world, BlockPos pos, Explosion explosion) {
		return !state.get(ACTIVE) ? 6000F : super.getExplosionResistance(state, world, pos, explosion);
	}

	@Override
	public boolean canEntityDestroy(BlockState state, IBlockReader world, BlockPos pos, Entity entity) {
		return !state.get(ACTIVE) ? !areBlocksLocked(world, pos) : super.canEntityDestroy(state, world, pos, entity);
	}

	private static boolean areBlocksLocked(IBlockReader world, BlockPos start) {
		int limit = 512;
		Deque<BlockPos> queue = new ArrayDeque<>();
		Set<BlockPos> checked = new HashSet<>();
		queue.offer(start);

		for (int iter = 0; !queue.isEmpty() && iter < limit; iter++) {
			BlockPos cur = queue.pop();
			BlockState state = world.getBlockState(cur);
			if (state.getBlock() == TFBlocks.locked_vanishing_block.get() && state.get(BlockTFLockedVanishing.LOCKED)) {
				return true;
			}

			checked.add(cur);

			if (state.getBlock() instanceof BlockTFVanishingBlock) {
				for (Direction facing : Direction.values()) {
					BlockPos neighbor = cur.offset(facing);
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
	public void neighborChanged(BlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
		if (world.isRemote) {
			return;
		}

		if (!isVanished(state) && !state.get(ACTIVE) && world.isBlockPowered(pos) && !areBlocksLocked(world, pos)) {
			activate(world, pos);
		}
	}

	@Override
	@Deprecated
	public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		if (world.isRemote) {
			return;
		}

		if (isVanished(state)) {
			if (state.get(ACTIVE)) {
				world.setBlockState(pos, state.with(VANISHED, false).with(ACTIVE, false));
			} else {
				world.setBlockState(pos, state.with(ACTIVE, true));
				world.getPendingBlockTicks().scheduleTick(pos, this, 15);
			}
			world.playSound(null, pos, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, 0.3F, 0.6F);
		} else {
			if (state.get(ACTIVE)) {
				if (state.hasProperty(VANISHED)) {
					world.setBlockState(pos, state.with(ACTIVE, false).with(VANISHED, true));
					world.getPendingBlockTicks().scheduleTick(pos, this, 80);
				} else {
					world.removeBlock(pos, false);
				}

				world.playSound(null, pos, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, 0.3F, 0.5F);

				for (Direction e : Direction.values()) {
					activate(world, pos.offset(e));
				}
			}
		}
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState state, World world, BlockPos pos, Random random) {
		if (state.get(ACTIVE)) {
			this.sparkle(world, pos);
		}
	}

	// [VanillaCopy] BlockRedstoneOre.spawnParticles. Unchanged.
	public void sparkle(World worldIn, BlockPos pos) {
		Random random = worldIn.rand;
		double d0 = 0.0625D;

		for (int i = 0; i < 6; ++i) {
			double d1 = (double) ((float) pos.getX() + random.nextFloat());
			double d2 = (double) ((float) pos.getY() + random.nextFloat());
			double d3 = (double) ((float) pos.getZ() + random.nextFloat());

			if (i == 0 && !worldIn.getBlockState(pos.up()).isOpaqueCube(worldIn, pos)) {
				d2 = (double) pos.getY() + d0 + 1.0D;
			}

			if (i == 1 && !worldIn.getBlockState(pos.down()).isOpaqueCube(worldIn, pos)) {
				d2 = (double) pos.getY() - d0;
			}

			if (i == 2 && !worldIn.getBlockState(pos.south()).isOpaqueCube(worldIn, pos)) {
				d3 = (double) pos.getZ() + d0 + 1.0D;
			}

			if (i == 3 && !worldIn.getBlockState(pos.north()).isOpaqueCube(worldIn, pos)) {
				d3 = (double) pos.getZ() - d0;
			}

			if (i == 4 && !worldIn.getBlockState(pos.east()).isOpaqueCube(worldIn, pos)) {
				d1 = (double) pos.getX() + d0 + 1.0D;
			}

			if (i == 5 && !worldIn.getBlockState(pos.west()).isOpaqueCube(worldIn, pos)) {
				d1 = (double) pos.getX() - d0;
			}

			float f1 = 1.0F * 0.6F + 0.4F;
			float f2 = Math.max(0.0F, 1.0F * 1.0F * 0.7F - 0.5F);
			float f3 = Math.max(0.0F, 1.0F * 1.0F * 0.6F - 0.7F);
			if (d1 < (double) pos.getX() || d1 > (double) (pos.getX() + 1) || d2 < 0.0D || d2 > (double) (pos.getY() + 1) || d3 < (double) pos.getZ() || d3 > (double) (pos.getZ() + 1)) {
				worldIn.addParticle(new RedstoneParticleData(f1, f2, f3, 1.0F), d1, d2, d3, 0.0D, 0.0D, 0.0D);
			}
		}
	}

	private void activate(World world, BlockPos pos) {
		BlockState state = world.getBlockState(pos);
		if (state.getBlock() instanceof BlockTFVanishingBlock && !isVanished(state) && !state.get(ACTIVE)) {
			world.setBlockState(pos, state.with(ACTIVE, true));
			world.getPendingBlockTicks().scheduleTick(pos, state.getBlock(), 2 + world.rand.nextInt(5));
		}
	}
}
