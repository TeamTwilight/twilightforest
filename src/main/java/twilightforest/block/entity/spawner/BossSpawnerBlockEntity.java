package twilightforest.block.entity.spawner;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.ForgeEventFactory;

public abstract class BossSpawnerBlockEntity<T extends Mob> extends BlockEntity {

	protected static final int SHORT_RANGE = 9, LONG_RANGE = 50;

	protected final EntityType<T> entityType;
	protected boolean spawnedBoss = false;

	protected BossSpawnerBlockEntity(BlockEntityType<?> type, EntityType<T> entityType, BlockPos pos, BlockState state) {
		super(type, pos, state);
		this.entityType = entityType;
	}

	public boolean anyPlayerInRange() {
		return this.getLevel().hasNearbyAlivePlayer(this.getBlockPos().getX() + 0.5D, this.getBlockPos().getY() + 0.5D, this.getBlockPos().getZ() + 0.5D, this.getRange());
	}

	public static void tick(Level level, BlockPos pos, BlockState state, BossSpawnerBlockEntity<?> te) {
		if (te.spawnedBoss || !te.anyPlayerInRange()) {
			return;
		}
		if (level.isClientSide()) {
			// particles
			double rx = pos.getX() + level.getRandom().nextFloat();
			double ry = pos.getY() + level.getRandom().nextFloat();
			double rz = pos.getZ() + level.getRandom().nextFloat();
			level.addParticle(te.getSpawnerParticle(), rx, ry, rz, 0.0D, 0.0D, 0.0D);
		} else {
			if (level.getDifficulty() != Difficulty.PEACEFUL) {
				if (te.spawnMyBoss((ServerLevel) level)) {
					level.destroyBlock(pos, false);
					te.spawnedBoss = true;
				}
			}
		}
	}

	protected boolean spawnMyBoss(ServerLevelAccessor accessor) {
		// create creature
		T myCreature = makeMyCreature();

		myCreature.moveTo(this.getBlockPos().below(), accessor.getLevel().getRandom().nextFloat() * 360F, 0.0F);
		ForgeEventFactory.onFinalizeSpawn(myCreature, accessor, accessor.getCurrentDifficultyAt(this.getBlockPos()), MobSpawnType.SPAWNER, null, null);

		// set creature's home to this
		initializeCreature(myCreature);

		// spawn it
		return accessor.addFreshEntity(myCreature);
	}

	public abstract ParticleOptions getSpawnerParticle();

	protected void initializeCreature(T myCreature) {
		myCreature.restrictTo(this.getBlockPos(), 40);
	}

	protected int getRange() {
		return SHORT_RANGE;
	}

	protected T makeMyCreature() {
		return entityType.create(this.getLevel());
	}
}
