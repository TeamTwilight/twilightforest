package twilightforest.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import twilightforest.util.EntityUtil;

import java.util.List;

public class HedgeBlock extends Block {

	private static final VoxelShape HEDGE_BB = Shapes.create(new AABB(0, 0, 0, 1, 0.9375, 1));

	private static final int DAMAGE = 3;

	public HedgeBlock(BlockBehaviour.Properties properties) {
		super(properties);
	}

	@Override
	@Deprecated
	public VoxelShape getCollisionShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
		return HEDGE_BB;
	}

	@Nullable
	@Override
	public BlockPathTypes getBlockPathType(BlockState state, BlockGetter getter, BlockPos pos, @Nullable Mob mob) {
		return mob != null && this.shouldDamage(mob) ? BlockPathTypes.DANGER_OTHER : null;
	}

	@Override
	@Deprecated
	public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
		if (this.shouldDamage(entity)) {
			entity.hurt(level.damageSources().cactus(), DAMAGE);
		}
	}

	@Override
	public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
		if (this.shouldDamage(entity)) {
			entity.hurt(level.damageSources().cactus(), DAMAGE);
		}
	}

	@Override
	public void attack(BlockState state, Level level, BlockPos pos, Player player) {
		if (!level.isClientSide()) {
			player.hurt(level.damageSources().cactus(), DAMAGE);
			level.scheduleTick(pos, this, 10);
		}
	}

	@Override
	public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity te, ItemStack stack) {
		super.playerDestroy(level, player, pos, state, te, stack);
		player.hurt(level.damageSources().cactus(), DAMAGE);
	}

	@Override
	@Deprecated
	public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		// find players within range
		List<Player> nearbyPlayers = level.getEntitiesOfClass(Player.class, new AABB(pos).inflate(8.0));

		for (Player player : nearbyPlayers) {
			// are they swinging?
			if (player.swinging) {
				BlockHitResult ray = EntityUtil.rayTrace(player);
				// are they pointing at this block?
				if (ray.getType() == HitResult.Type.BLOCK && pos.equals(ray.getBlockPos())) {
					// prick them!  prick them hard!
					player.hurt(level.damageSources().cactus(), DAMAGE);

					// trigger this again!
					level.scheduleTick(pos, this, 10);
				}
			}
		}
	}

	private boolean shouldDamage(Entity entity) {
		return !(entity instanceof Spider || entity instanceof ItemEntity || entity.isIgnoringBlockTriggers());
	}

	@Override
	public int getFlammability(BlockState state, BlockGetter getter, BlockPos pos, Direction face) {
		return 0;
	}

	@Override
	public int getFireSpreadSpeed(BlockState state, BlockGetter getter, BlockPos pos, Direction face) {
		return 0;
	}
}
