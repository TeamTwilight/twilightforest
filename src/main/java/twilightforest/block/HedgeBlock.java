package twilightforest.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.AABB;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import twilightforest.util.EntityUtil;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

import net.minecraft.world.level.block.state.BlockBehaviour;

public class HedgeBlock extends Block {

	private static final VoxelShape HEDGE_BB = Shapes.create(new AABB(0, 0, 0, 1, 0.9375, 1));

	private static final int DAMAGE = 3;

	protected HedgeBlock(BlockBehaviour.Properties props) {
		super(props);
	}

	@Override
	@Deprecated
	public VoxelShape getCollisionShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
		return HEDGE_BB;
	}

	@Nullable
	@Override
	public BlockPathTypes getAiPathNodeType(BlockState state, BlockGetter world, BlockPos pos, @Nullable Mob entity) {
		return entity != null && shouldDamage(entity) ? BlockPathTypes.DANGER_CACTUS : null;
	}

	@Override
	@Deprecated
	public void entityInside(BlockState state, Level worldIn, BlockPos pos, Entity entity) {
		if (shouldDamage(entity)) {
			entity.hurt(DamageSource.CACTUS, DAMAGE);
		}
	}

	@Override
	public void stepOn(Level world, BlockPos pos, BlockState state, Entity entity) {
		if (shouldDamage(entity)) {
			entity.hurt(DamageSource.CACTUS, DAMAGE);
		}
	}

	@Override
	public void attack(BlockState state, Level world, BlockPos pos, Player player) {
		if (!world.isClientSide) {
			world.getBlockTicks().scheduleTick(pos, this, 10);
		}
	}

	@Override
	public void playerDestroy(Level world, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity te, ItemStack stack) {
		super.playerDestroy(world, player, pos, state, te, stack);
		player.hurt(DamageSource.CACTUS, DAMAGE);
	}

	@Override
	@Deprecated
	public void tick(BlockState state, ServerLevel world, BlockPos pos, Random random) {
		// find players within range
		List<Player> nearbyPlayers = world.getEntitiesOfClass(Player.class, new AABB(pos).inflate(8.0));

		for (Player player : nearbyPlayers) {
			// are they swinging?
			if (player.swinging) {
				BlockHitResult ray = EntityUtil.rayTrace(player);
				// are they pointing at this block?
				if (ray.getType() == HitResult.Type.BLOCK && pos.equals(ray.getBlockPos())) {
					// prick them!  prick them hard!
					player.hurt(DamageSource.CACTUS, DAMAGE);

					// trigger this again!
					world.getBlockTicks().scheduleTick(pos, this, 10);
				}
			}
		}
	}

	private boolean shouldDamage(Entity entity) {
		return !(entity instanceof Spider || entity instanceof ItemEntity || entity.isIgnoringBlockTriggers());
	}

	@Override
	public int getFlammability(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
		return 0;
	}

	@Override
	public int getFireSpreadSpeed(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
		return 0;
	}
}
