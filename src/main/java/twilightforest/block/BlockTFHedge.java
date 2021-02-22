package twilightforest.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.monster.SpiderEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import twilightforest.util.EntityUtil;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class BlockTFHedge extends Block {

	private static final VoxelShape HEDGE_BB = VoxelShapes.create(new AxisAlignedBB(0, 0, 0, 1, 0.9375, 1));

	private static final int DAMAGE = 3;

	protected BlockTFHedge(Block.Properties props) {
		super(props);
	}

	@Override
	@Deprecated
	public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return HEDGE_BB;
	}

	@Nullable
	@Override
	public PathNodeType getAiPathNodeType(BlockState state, IBlockReader world, BlockPos pos, @Nullable MobEntity entity) {
		return entity != null && shouldDamage(entity) ? PathNodeType.DAMAGE_CACTUS : null;
	}

	@Override
	@Deprecated
	public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entity) {
		if (shouldDamage(entity)) {
			entity.attackEntityFrom(DamageSource.CACTUS, DAMAGE);
		}
	}

	@Override
	public void onEntityWalk(World world, BlockPos pos, Entity entity) {
		if (shouldDamage(entity)) {
			entity.attackEntityFrom(DamageSource.CACTUS, DAMAGE);
		}
	}

	@Override
	public void onBlockClicked(BlockState state, World world, BlockPos pos, PlayerEntity player) {
		if (!world.isRemote) {
			world.getPendingBlockTicks().scheduleTick(pos, this, 10);
		}
	}

	@Override
	public void harvestBlock(World world, PlayerEntity player, BlockPos pos, BlockState state, @Nullable TileEntity te, ItemStack stack) {
		super.harvestBlock(world, player, pos, state, te, stack);
		player.attackEntityFrom(DamageSource.CACTUS, DAMAGE);
	}

	@Override
	@Deprecated
	public void tick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		// find players within range
		List<PlayerEntity> nearbyPlayers = world.getEntitiesWithinAABB(PlayerEntity.class, new AxisAlignedBB(pos).grow(8.0));

		for (PlayerEntity player : nearbyPlayers) {
			// are they swinging?
			if (player.isSwingInProgress) {
				BlockRayTraceResult ray = EntityUtil.rayTrace(player);
				// are they pointing at this block?
				if (ray.getType() == RayTraceResult.Type.BLOCK && pos.equals(ray.getPos())) {
					// prick them!  prick them hard!
					player.attackEntityFrom(DamageSource.CACTUS, DAMAGE);

					// trigger this again!
					world.getPendingBlockTicks().scheduleTick(pos, this, 10);
				}
			}
		}
	}

	private boolean shouldDamage(Entity entity) {
		return !(entity instanceof SpiderEntity || entity instanceof ItemEntity || entity.doesEntityNotTriggerPressurePlate());
	}

	@Override
	public int getFlammability(BlockState state, IBlockReader world, BlockPos pos, Direction face) {
		return 0;
	}

	@Override
	public int getFireSpreadSpeed(BlockState state, IBlockReader world, BlockPos pos, Direction face) {
		return 0;
	}
}
