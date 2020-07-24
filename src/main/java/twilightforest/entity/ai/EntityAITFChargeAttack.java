package twilightforest.entity.ai;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.event.ForgeEventFactory;
import twilightforest.entity.ITFCharger;
import twilightforest.util.EntityUtil;

import java.util.EnumSet;

public class EntityAITFChargeAttack extends Goal {

	private static final double MIN_RANGE_SQ = 16.0D;
	private static final double MAX_RANGE_SQ = 64.0D;
	private static final int FREQ = 1;

	private CreatureEntity charger;
	private LivingEntity chargeTarget;
	private double chargeX;
	private double chargeY;
	private double chargeZ;

	protected float speed;

	private final boolean canBreak;

	private int windup;

	private boolean hasAttacked;

	public EntityAITFChargeAttack(CreatureEntity entityLiving, float f, boolean canBreak) {
		this.charger = entityLiving;
		this.speed = f;
		this.canBreak = canBreak;
		this.windup = 0;
		this.hasAttacked = false;
		this.setMutexFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
	}

	@Override
	public boolean shouldExecute() {
		this.chargeTarget = this.charger.getAttackTarget();

		if (this.chargeTarget == null) {
			return false;
		} else {
			double distance = this.charger.getDistanceSq(this.chargeTarget);
			if (distance < MIN_RANGE_SQ || distance > MAX_RANGE_SQ) {
				return false;
			} else if (!this.charger.isOnGround()) {
				return false;
			} else {
				Vector3d chargePos = findChargePoint(charger, chargeTarget, 2.1);
				boolean canSeeTargetFromDest = charger.getEntitySenses().canSee(chargeTarget);
				if (!canSeeTargetFromDest) {
					return false;
				} else {
					chargeX = chargePos.x;
					chargeY = chargePos.y;
					chargeZ = chargePos.z;

					return this.charger.getRNG().nextInt(FREQ) == 0;
				}
			}

		}
	}

	@Override
	public void startExecuting() {
		this.windup = 15 + this.charger.getRNG().nextInt(30);
		this.charger.setSprinting(true);
	}

	@Override
	public boolean shouldContinueExecuting() {
		return windup > 0 || !this.charger.getNavigator().noPath();
	}

	@Override
	public void tick() {
		// look where we're going
		this.charger.getLookController().setLookPosition(chargeX, chargeY - 1, chargeZ, 10.0F, this.charger.getVerticalFaceSpeed());

		if (windup > 0) {
			if (--windup == 0) {
				// actually charge!

				this.charger.getNavigator().tryMoveToXYZ(chargeX, chargeY, chargeZ, this.speed);
			} else {
				this.charger.limbSwingAmount += 0.8;

				if (this.charger instanceof ITFCharger) {
					((ITFCharger) charger).setCharging(true);
				}
			}
		} else if (canBreak) {
			if (!charger.world.isRemote && ForgeEventFactory.getMobGriefingEvent(charger.world, charger)) {

				AxisAlignedBB bb = charger.getBoundingBox();
				int minx = MathHelper.floor(bb.minX - 0.75D);
				int miny = MathHelper.floor(bb.minY + 0.0D);
				int minz = MathHelper.floor(bb.minZ - 0.75D);
				int maxx = MathHelper.floor(bb.maxX + 0.75D);
				int maxy = MathHelper.floor(bb.maxY + 0.15D);
				int maxz = MathHelper.floor(bb.maxZ + 0.75D);

				BlockPos min = new BlockPos(minx, miny, minz);
				BlockPos max = new BlockPos(maxx, maxy, maxz);

				if (charger.world.isAreaLoaded(min, max)) {
					for (BlockPos pos : BlockPos.getAllInBoxMutable(min, max)) {
						if (EntityUtil.canDestroyBlock(charger.world, pos, charger) && charger.world.getTileEntity(pos) == null) {
							charger.world.destroyBlock(pos, true);
						}
					}
				}
			}
		}

		// attack the target when we get in range
		double rangeSq = this.charger.getWidth() * 2.1F * this.charger.getWidth() * 2.1F;

		if (this.charger.getDistanceSq(this.chargeTarget.getPosX(), this.chargeTarget.getBoundingBox().minY, this.chargeTarget.getPosZ()) <= rangeSq) {
			if (!this.hasAttacked) {
				this.hasAttacked = true;
				this.charger.attackEntityAsMob(this.chargeTarget);
			}
		}

	}

	@Override
	public void resetTask() {
		this.windup = 0;
		this.chargeTarget = null;
		this.hasAttacked = false;
		this.charger.setSprinting(false);

		if (this.charger instanceof ITFCharger) {
			((ITFCharger) charger).setCharging(false);
		}
	}


	/**
	 * Finds a point that is overshoot blocks "beyond" the target from our position.
	 */
	protected Vector3d findChargePoint(Entity attacker, Entity target, double overshoot) {

		// compute angle
		double vecx = target.getPosX() - attacker.getPosX();
		double vecz = target.getPosZ() - attacker.getPosZ();
		float rangle = (float) (Math.atan2(vecz, vecx));

		double distance = MathHelper.sqrt(vecx * vecx + vecz * vecz);

		// figure out where we're headed from the target angle
		double dx = MathHelper.cos(rangle) * (distance + overshoot);
		double dz = MathHelper.sin(rangle) * (distance + overshoot);

		// add that to the target entity's position, and we have our destination
		return new Vector3d(attacker.getPosX() + dx, target.getPosY(), attacker.getPosZ() + dz);
	}


}
