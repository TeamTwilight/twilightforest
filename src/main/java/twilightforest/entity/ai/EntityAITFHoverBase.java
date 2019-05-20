package twilightforest.entity.ai;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import twilightforest.TwilightForestMod;
import twilightforest.entity.boss.EntityTFSnowQueen;

public abstract class EntityAITFHoverBase extends EntityAIBase {

	protected final Class<? extends EntityLivingBase> classTarget;
	protected final EntityTFSnowQueen attacker;

	private final float hoverHeight;
	private final float hoverRadius;

	protected double hoverPosX;
	protected double hoverPosY;
	protected double hoverPosZ;

	protected EntityAITFHoverBase(EntityTFSnowQueen snowQueen, Class<? extends EntityLivingBase> targetClass, float hoverHeight, float hoverRadius) {
		this.classTarget = targetClass;
		this.attacker = snowQueen;
		this.hoverHeight = hoverHeight;
		this.hoverRadius = hoverRadius;
	}

	@Override
	public void startExecuting() {
		EntityLivingBase target = this.attacker.getAttackTarget();
		if (target != null) {
			// find a spot above the player
			makeNewHoverSpot(target);
		}
	}

	/**
	 * Make a new spot to hover at!
	 */
	protected void makeNewHoverSpot(EntityLivingBase target){
		double hx = 0, hy = 0, hz = 0;

		boolean found = false;

		for (int i = 0; i < 100; i++) {
			hx = target.posX + (this.attacker.getRNG().nextFloat() - this.attacker.getRNG().nextFloat()) * hoverRadius;
			hy = target.posY + hoverHeight;
			hz = target.posZ + (this.attacker.getRNG().nextFloat() - this.attacker.getRNG().nextFloat()) * hoverRadius;

			if (!isPositionOccupied(hx, hy, hz) && this.canEntitySee(this.attacker, hx, hy, hz) && this.canEntitySee(target, hx, hy, hz)) {
				found = true;
				break;
			}
		}

		if (!found) {
			TwilightForestMod.LOGGER.debug("Found no spots, giving up");
		}

		this.hoverPosX = hx;
		this.hoverPosY = hy;
		this.hoverPosZ = hz;
	}

	protected boolean isPositionOccupied(double hx, double hy, double hz) {
		float radius = this.attacker.width / 2F;
		AxisAlignedBB aabb = new AxisAlignedBB(hx - radius, hy, hz - radius, hx + radius, hy + this.attacker.height, hz + radius);
		return !this.attacker.world.checkNoEntityCollision(aabb, attacker) || !this.attacker.world.getCollisionBoxes(attacker, aabb).isEmpty();
	}

	/**
	 * Can the specified entity see the specified location?
	 */
	protected boolean canEntitySee(Entity entity, double dx, double dy, double dz) {
		return entity.world.rayTraceBlocks(new Vec3d(entity.posX, entity.posY + (double) entity.getEyeHeight(), entity.posZ), new Vec3d(dx, dy, dz)) == null;
	}
}
