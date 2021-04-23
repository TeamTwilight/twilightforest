package twilightforest.entity.ai;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.math.vector.Vector3d;
import twilightforest.TwilightForestMod;

public abstract class EntityAITFHoverBase<T extends MobEntity> extends Goal {

	protected final T attacker;

	protected final float hoverHeight;
	protected final float hoverRadius;

	protected double hoverPosX;
	protected double hoverPosY;
	protected double hoverPosZ;

	protected EntityAITFHoverBase(T snowQueen, float hoverHeight, float hoverRadius) {
		this.attacker = snowQueen;
		this.hoverHeight = hoverHeight;
		this.hoverRadius = hoverRadius;
	}

	@Override
	public void startExecuting() {
		LivingEntity target = this.attacker.getAttackTarget();
		if (target != null) {
			// find a spot above the player
			makeNewHoverSpot(target);
		}
	}

	/**
	 * Make a new spot to hover at!
	 */
	protected void makeNewHoverSpot(LivingEntity target) {
		double hx = 0, hy = 0, hz = 0;

		boolean found = false;

		for (int i = 0; i < 100; i++) {
			hx = target.getPosX() + (this.attacker.getRNG().nextFloat() - this.attacker.getRNG().nextFloat()) * hoverRadius;
			hy = target.getPosY() + hoverHeight;
			hz = target.getPosZ() + (this.attacker.getRNG().nextFloat() - this.attacker.getRNG().nextFloat()) * hoverRadius;

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
		float radius = this.attacker.getWidth() / 2F;
		AxisAlignedBB aabb = new AxisAlignedBB(hx - radius, hy, hz - radius, hx + radius, hy + this.attacker.getHeight(), hz + radius);
		return !this.attacker.world.checkNoEntityCollision(attacker, VoxelShapes.create(aabb)) || this.attacker.world.getCollisionShapes(attacker, aabb).count() > 0;
	}

	/**
	 * Can the specified entity see the specified location?
	 */
	protected boolean canEntitySee(Entity entity, double dx, double dy, double dz) {
		return entity.world.rayTraceBlocks(new RayTraceContext(new Vector3d(entity.getPosX(), entity.getPosY() + entity.getEyeHeight(), entity.getPosZ()), new Vector3d(dx, dy, dz), RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, entity)) == null;
	}
}
