package twilightforest.block.entity.spawner;

import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.event.EventHooks;
import twilightforest.entity.EnforcedHomePoint;

import java.util.Objects;

public abstract class BossSpawnerBlockEntity<T extends Mob & EnforcedHomePoint> extends BlockEntity {

	protected static final int SHORT_RANGE = 9, LONG_RANGE = 50;

	protected final EntityType<T> entityType;
	protected boolean spawnedBoss = false;

	protected BossSpawnerBlockEntity(BlockEntityType<?> type, EntityType<T> entityType, BlockPos pos, BlockState state) {
		super(type, pos, state);
		this.entityType = entityType;
	}

	public static void tick(Level level, BlockPos pos, BlockState state, BossSpawnerBlockEntity<?> te) {
		if (te.spawnedBoss || !te.anyPlayerInRange(level)) {
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

	public boolean anyPlayerInRange(Level level) {
		return level.hasNearbyAlivePlayer(this.getBlockPos().getX() + 0.5D, this.getBlockPos().getY() + 0.5D, this.getBlockPos().getZ() + 0.5D, this.getRange());
	}

	protected boolean spawnMyBoss(ServerLevelAccessor accessor) {
		// create creature
		T myCreature = this.makeMyCreature();

		BlockPos spawnPos = accessor.getBlockState(this.getBlockPos().below()).getCollisionShape(accessor, this.getBlockPos().below()).isEmpty() ? this.getBlockPos().below() : this.getBlockPos();
		myCreature.moveTo(spawnPos, accessor.getLevel().getRandom().nextFloat() * 360F, 0.0F);
		EventHooks.finalizeMobSpawn(myCreature, accessor, accessor.getCurrentDifficultyAt(spawnPos), EntitySpawnReason.SPAWNER, null);

		// set creature's home to this
		this.initializeCreature(myCreature);

		// spawn it
		return accessor.addFreshEntity(myCreature);
	}

	public abstract ParticleOptions getSpawnerParticle();

	protected void initializeCreature(T myCreature) {
		myCreature.setRestrictionPoint(GlobalPos.of(myCreature.level().dimension(), this.getBlockPos()));
	}

	protected int getRange() {
		return SHORT_RANGE;
	}

	protected T makeMyCreature() {
		return Objects.requireNonNull(this.entityType.create(this.getLevel(), EntitySpawnReason.SPAWNER));
	}
}
