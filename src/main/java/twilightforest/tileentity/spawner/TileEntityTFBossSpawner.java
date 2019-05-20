package twilightforest.tileentity.spawner;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.EnumDifficulty;

public abstract class TileEntityTFBossSpawner extends TileEntity implements ITickable {

	protected static final int SHORT_RANGE = 9, LONG_RANGE = 50;

	protected final ResourceLocation mobID;
	protected Entity displayCreature = null;
	protected boolean spawnedBoss = false;

	protected TileEntityTFBossSpawner(ResourceLocation mobID) {
		this.mobID = mobID;
	}

	public boolean anyPlayerInRange() {
		return world.isAnyPlayerWithinRangeAt(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, getRange());
	}

	@Override
	public void update() {
		if (spawnedBoss || !anyPlayerInRange()) {
			return;
		}
		if (world.isRemote) {
			// particles
			double rx = pos.getX() + world.rand.nextFloat();
			double ry = pos.getY() + world.rand.nextFloat();
			double rz = pos.getZ() + world.rand.nextFloat();
			world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, rx, ry, rz, 0.0D, 0.0D, 0.0D);
			world.spawnParticle(EnumParticleTypes.FLAME, rx, ry, rz, 0.0D, 0.0D, 0.0D);
		} else {
			if (world.getDifficulty() != EnumDifficulty.PEACEFUL) {
				if (spawnMyBoss()) {
					world.destroyBlock(pos, false);
					spawnedBoss = true;
				}
			}
		}
	}

	/**
	 * Spawn the boss
	 */
	protected boolean spawnMyBoss() {
		// create creature
		EntityLiving myCreature = makeMyCreature();

		myCreature.moveToBlockPosAndAngles(pos, world.rand.nextFloat() * 360F, 0.0F);
		myCreature.onInitialSpawn(world.getDifficultyForLocation(pos), null);

		// set creature's home to this
		initializeCreature(myCreature);

		// spawn it
		return world.spawnEntity(myCreature);
	}

	/**
	 * Get a temporary copy of the creature we're going to summon for display purposes
	 */
	public Entity getDisplayEntity() {
		if (this.displayCreature == null) {
			this.displayCreature = makeMyCreature();
		}
		return this.displayCreature;
	}

	/**
	 * Any post-creation initialization goes here
	 */
	protected void initializeCreature(EntityLiving myCreature) {
		if (myCreature instanceof EntityCreature) {
			((EntityCreature) myCreature).setHomePosAndDistance(pos, 46);
		}
	}

	protected int getRange() {
		return SHORT_RANGE;
	}

	protected EntityLiving makeMyCreature() {
		return (EntityLiving) EntityList.createEntityByIDFromName(mobID, world);
	}
}
