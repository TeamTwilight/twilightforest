package twilightforest.entity.ai;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import twilightforest.TwilightForestMod;
import twilightforest.entity.boss.EntityTFSnowQueen;
import twilightforest.entity.boss.EntityTFSnowQueen.Phase;

public class EntityAITFHoverSummon extends EntityAIBase {

	private static final float HOVER_HEIGHT = 6F;
	private static final float HOVER_RADIUS = 6F;
	private static final int MAX_MINIONS_AT_ONCE = 4;


	private Class<? extends EntityLivingBase> classTarget;
	private EntityTFSnowQueen attacker;

	private double hoverPosX;
	private double hoverPosY;
	private double hoverPosZ;
	private int seekTimer;
	private int maxSeekTime;

	public EntityAITFHoverSummon(EntityTFSnowQueen entityTFSnowQueen, Class<EntityPlayer> class1, double speed) {
		this.attacker = entityTFSnowQueen;
		this.classTarget = class1;
		this.setMutexBits(3);
		this.maxSeekTime = 80;

	}

	@Override
	public boolean shouldExecute() {
		EntityLivingBase target = this.attacker.getAttackTarget();

		if (target == null) {
			return false;
		} else if (!target.isEntityAlive()) {
			return false;
		} else if (this.classTarget != null && !this.classTarget.isAssignableFrom(target.getClass())) {
			return false;
		} else if (this.attacker.getCurrentPhase() != Phase.SUMMON) {
			return false;
		} else {
			//System.out.println("We can hover at target!");

			return attacker.getEntitySenses().canSee(target);
		}

	}

	@Override
	public boolean shouldContinueExecuting() {
		EntityLivingBase target = this.attacker.getAttackTarget();

		if (target == null || !target.isEntityAlive()) {
			return false;
		} else if (this.attacker.getCurrentPhase() != Phase.SUMMON) {
			return false;
		} else if (this.seekTimer > this.maxSeekTime) {
			return false;
		} else {
			// check visibility
			boolean isVisible = this.canEntitySee(this.attacker, hoverPosX, hoverPosY, hoverPosZ);

			if (!isVisible) {
				//System.out.println("Hover spot is no longer visible");
			}

			return isVisible;
		}
	}

	@Override
	public void startExecuting() {
		EntityLivingBase target = this.attacker.getAttackTarget();

		if (target != null) {
			// find a spot above the player
			makeNewHoverSpot(target);
		}
	}

	@Override
	public void resetTask() {
	}

	@Override
	public void updateTask() {
		this.seekTimer++;
		EntityLivingBase target = this.attacker.getAttackTarget();

		// are we there yet?
		if (this.attacker.getDistanceSq(hoverPosX, hoverPosY, hoverPosZ) <= 1.0F) {
			//System.out.println("Successful hovering, making new spot");

			this.checkAndSummon();

			this.makeNewHoverSpot(target);
		}

		// check if we are at our waypoint target
		double offsetX = this.hoverPosX - this.attacker.posX;
		double offsetY = this.hoverPosY - this.attacker.posY;
		double offsetZ = this.hoverPosZ - this.attacker.posZ;

		double distanceDesired = offsetX * offsetX + offsetY * offsetY + offsetZ * offsetZ;

		distanceDesired = (double) MathHelper.sqrt(distanceDesired);


		// add velocity
		double velX = offsetX / distanceDesired * 0.05D;
		double velY = offsetY / distanceDesired * 0.1D;
		double velZ = offsetZ / distanceDesired * 0.05D;

		// gravity offset
		velY += 0.05F;

		this.attacker.addVelocity(velX, velY, velZ);

		// look at target
		if (target != null) {
			this.attacker.faceEntity(target, 30.0F, 30.0F);
			this.attacker.getLookHelper().setLookPositionWithEntity(target, 30.0F, 30.0F);
		}

		//System.out.println("Hovering!");
	}


	/**
	 * Make a new spot to hover at!
	 */
	private void makeNewHoverSpot(EntityLivingBase target) {
		double hx = 0, hy = 0, hz = 0;

		int tries = 100;

		for (int i = 0; i < tries; i++) {
			hx = target.posX + (this.attacker.getRNG().nextFloat() - this.attacker.getRNG().nextFloat()) * HOVER_RADIUS;
			hy = target.posY + HOVER_HEIGHT;
			hz = target.posZ + (this.attacker.getRNG().nextFloat() - this.attacker.getRNG().nextFloat()) * HOVER_RADIUS;

			if (!isPositionOccupied(hx, hy, hz) && this.canEntitySee(this.attacker, hx, hy, hz) && this.canEntitySee(target, hx, hy, hz)) {
				break;
			}
		}

		if (tries == 99) {
			TwilightForestMod.LOGGER.debug("Found no spots, giving up");
		}

		this.hoverPosX = hx;
		this.hoverPosY = hy;
		this.hoverPosZ = hz;

		this.seekTimer = 0;

	}

	private boolean isPositionOccupied(double hx, double hy, double hz) {
		float radius = this.attacker.width / 2F;
		AxisAlignedBB aabb = new AxisAlignedBB(hx - radius, hy, hz - radius, hx + radius, hy + this.attacker.height, hz + radius);

		boolean isOccupied = this.attacker.world.getCollisionBoxes(attacker, aabb).isEmpty();

		return isOccupied;
	}

	/**
	 * Can the specified entity see the specified location?
	 */
	protected boolean canEntitySee(Entity entity, double dx, double dy, double dz) {
		return entity.world.rayTraceBlocks(new Vec3d(entity.posX, entity.posY + (double) entity.getEyeHeight(), entity.posZ), new Vec3d(dx, dy, dz)) == null;

	}

	private void checkAndSummon() {
		if (this.attacker.getSummonsRemaining() > 0 && this.attacker.countMyMinions() < MAX_MINIONS_AT_ONCE) {
			this.attacker.summonMinionAt(this.attacker.getAttackTarget());

			//System.out.println("Summoning minion");
		} else {
			//System.out.println("Summons full");
		}
	}
}
