package twilightforest.tileentity.spawner;

import net.minecraft.entity.*;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.world.Difficulty;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.server.ServerWorld;

public abstract class TileEntityTFBossSpawner<T extends MobEntity> extends TileEntity implements ITickableTileEntity {

	protected static final int SHORT_RANGE = 9, LONG_RANGE = 50;

	protected final EntityType<T> entityType;
	protected Entity displayCreature = null;
	protected boolean spawnedBoss = false;

	protected TileEntityTFBossSpawner(TileEntityType<?> type, EntityType<T> entityType) {
		super(type);
		this.entityType = entityType;
	}

	public boolean anyPlayerInRange() {
		return world.isPlayerWithin(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, getRange());
	}

	@Override
	public void tick() {
		if (spawnedBoss || !anyPlayerInRange()) {
			return;
		}
		if (world.isRemote) {
			// particles
			double rx = pos.getX() + world.rand.nextFloat();
			double ry = pos.getY() + world.rand.nextFloat();
			double rz = pos.getZ() + world.rand.nextFloat();
			world.addParticle(ParticleTypes.SMOKE, rx, ry, rz, 0.0D, 0.0D, 0.0D);
			world.addParticle(ParticleTypes.FLAME, rx, ry, rz, 0.0D, 0.0D, 0.0D);
		} else {
			if (world.getDifficulty() != Difficulty.PEACEFUL) {
				if (spawnMyBoss((ServerWorld)world)) {
					world.destroyBlock(pos, false);
					spawnedBoss = true;
				}
			}
		}
	}

	protected boolean spawnMyBoss(IServerWorld world) {
		// create creature
		T myCreature = makeMyCreature();

		myCreature.moveToBlockPosAndAngles(pos, world.getWorld().rand.nextFloat() * 360F, 0.0F);
		myCreature.onInitialSpawn(world, world.getDifficultyForLocation(pos), SpawnReason.SPAWNER, null, null);

		// set creature's home to this
		initializeCreature(myCreature);

		// spawn it
		return world.addEntity(myCreature);
	}

	protected void initializeCreature(T myCreature) {
		myCreature.setHomePosAndDistance(pos, 46);
	}

	protected int getRange() {
		return SHORT_RANGE;
	}

	protected T makeMyCreature() {
		return entityType.create(world);
	}
}
