package twilightforest.block;

import net.minecraft.block.material.PushReaction;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.IFluidState;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.state.IProperty;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockTFThorns extends BlockTFConnectableRotatedPillar {

	private static final float THORN_DAMAGE = 4.0F;

	BlockTFThorns(Properties props) {
		super(props, 10);
	}

//	@Override
//	protected boolean canConnectTo(BlockState state, BlockState otherState, IBlockAccess world, BlockPos pos, Direction connectTo) {
//		return (otherState.getBlock() instanceof BlockTFThorns
//				|| otherState.getBlock() == TFBlocks.thorn_rose
//				||(otherState.getBlock() == TFBlocks.twilight_leaves_3 && otherState.getValue(BlockTFLeaves3.VARIANT) == Leaves3Variant.THORN)
//				|| otherState.getMaterial() == Material.GRASS
//				|| otherState.getMaterial() == Material.GROUND)
//				|| super.canConnectTo(state, otherState, world, pos, connectTo);
//	}

	@Nullable
	@Override
	public PathNodeType getAiPathNodeType(BlockState state, IBlockReader world, BlockPos pos, @Nullable MobEntity entity) {
		return PathNodeType.DAMAGE_CACTUS;
	}

	@Override
	@Deprecated
	public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entity) {
		entity.attackEntityFrom(DamageSource.CACTUS, THORN_DAMAGE);
	}

	@Override
	public void onEntityWalk(World world, BlockPos pos, Entity entity) {
		BlockState state = world.getBlockState(pos);

		if (state.getBlock() instanceof BlockTFThorns && state.get(AXIS) == Direction.Axis.Y)
			onEntityCollision(state, world, pos, entity);

		super.onEntityWalk(world, pos, entity);
	}

	@Override
	public boolean removedByPlayer(BlockState state, World world, BlockPos pos, PlayerEntity player, boolean willHarvest, IFluidState fluid) {
		if (!player.abilities.isCreativeMode) {
			if (!world.isRemote) {
				// grow back
				world.setBlockState(pos, state, 2);
				// grow more
				this.doThornBurst(world, pos, state);
			}
		} else {
			world.removeBlock(pos, false);
		}

		return true;
	}

	@Override
	@Deprecated
	public PushReaction getPushReaction(BlockState state) {
		return PushReaction.BLOCK;
	}

	/**
	 * Grow thorns out of both the ends, then maybe in another direction too
	 */
	private void doThornBurst(World world, BlockPos pos, BlockState state) {
		switch (state.get(AXIS)) {
			case Y:
				growThorns(world, pos, Direction.UP);
				growThorns(world, pos, Direction.DOWN);
				break;
			case X:
				growThorns(world, pos, Direction.EAST);
				growThorns(world, pos, Direction.WEST);
				break;
			case Z:
				growThorns(world, pos, Direction.NORTH);
				growThorns(world, pos, Direction.SOUTH);
				break;
		}

		// also try three random directions
		growThorns(world, pos, Direction.random(world.rand));
		growThorns(world, pos, Direction.random(world.rand));
		growThorns(world, pos, Direction.random(world.rand));
	}

	/**
	 * grow several green thorns in the specified direction
	 */
	private void growThorns(World world, BlockPos pos, Direction dir) {
		int length = 1 + world.rand.nextInt(3);

		for (int i = 1; i < length; i++) {
			BlockPos dPos = pos.offset(dir, i);

			if (world.isAirBlock(dPos)) {
				world.setBlockState(dPos, TFBlocks.green_thorns.get().getDefaultState().with(AXIS, dir.getAxis()), 2);
			} else {
				break;
			}
		}
	}


//	@Override
//	public int quantityDropped(Random random) {
//		return 0;
//	}

	//TODO: Move to client
//	@OnlyIn(Dist.CLIENT)
//	@Override
//	public BlockRenderLayer getRenderLayer() {
//		return BlockRenderLayer.CUTOUT;
//	}
}
