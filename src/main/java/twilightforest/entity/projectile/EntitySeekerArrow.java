package twilightforest.entity.projectile;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class EntitySeekerArrow extends EntityTFArrow {

	private static final DataParameter<Integer> TARGET = EntityDataManager.createKey(EntitySeekerArrow.class, DataSerializers.VARINT);

	private static final double seekDistance = 5.0;
	private static final double seekFactor = 0.2;
	private static final double seekAngle = Math.PI / 6.0;
	private static final double seekThreshold = 0.5;

	public EntitySeekerArrow(EntityType<? extends EntitySeekerArrow> type, World world) {
		super(type, world);
	}

	public EntitySeekerArrow(EntityType<? extends EntitySeekerArrow> type, World world, LivingEntity shooter) {
		super(type, world, shooter);
	}

	@Override
	protected void registerData() {
		super.registerData();
		dataManager.register(TARGET, -1);
	}

	@Override
	public void tick() {
		if (isThisArrowFlying()) {
			if (!world.isRemote) {
				updateTarget();
			}

			if (world.isRemote && !inGround) {
				for (int i = 0; i < 4; ++i) {
					this.world.addParticle(ParticleTypes.WITCH, this.getX() + this.getMotion().getX() * (double) i / 4.0D, this.getY() + this.getMotion().getY() * (double) i / 4.0D, this.getZ() + this.getMotion().getZ() * (double) i / 4.0D, -this.getMotion().getX(), -this.getMotion().getY() + 0.2D, -this.getMotion().getZ());
				}
			}

			Entity target = getTarget();
			if (target != null) {

				Vec3d targetVec = getVectorToTarget(target).scale(seekFactor);
				Vec3d courseVec = getMotionVec();

				// vector lengths
				double courseLen = courseVec.length();
				double targetLen = targetVec.length();
				double totalLen = Math.sqrt(courseLen*courseLen + targetLen*targetLen);

				double dotProduct = courseVec.dotProduct(targetVec) / (courseLen * targetLen); // cosine similarity

				if (dotProduct > seekThreshold) {

					// add vector to target, scale to match current velocity
					Vec3d newMotion = courseVec.scale(courseLen / totalLen).add(targetVec.scale(targetLen / totalLen));

//					this.getMotion().getX() = newMotion.x;
//					this.getMotion().getY() = newMotion.y;
//					this.getMotion().getZ() = newMotion.z;
					this.setMotion(newMotion);

					// compensate (mostly) for gravity
//					this.getMotion().getY() += 0.045F;
					this.getMotion().add(0, 0.045F, 0);

				} else if (!world.isRemote) {
					// too inaccurate for our intended target, give up on it
					setTarget(null);
				}
			}
		}

		super.tick();
	}

	private void updateTarget() {

		Entity target = getTarget();

		if (target != null && !target.isAlive()) {
			setTarget(target = null);
		}

		if (target == null) {
			AxisAlignedBB positionBB = new AxisAlignedBB(getX(), getY(), getZ(), getX(), getY(), getZ());
			AxisAlignedBB targetBB = positionBB;

			// add two possible courses to our selection box
			Vec3d courseVec = getMotionVec().scale(seekDistance).rotateYaw((float) seekAngle);
			targetBB = targetBB.union(positionBB.offset(courseVec));

			courseVec = getMotionVec().scale(seekDistance).rotateYaw((float) -seekAngle);
			targetBB = targetBB.union(positionBB.offset(courseVec));

			targetBB = targetBB.grow(0, seekDistance * 0.5, 0);

			double closestDot = -1.0;
			Entity closestTarget = null;

			for (LivingEntity living : this.world.getEntitiesWithinAABB(LivingEntity.class, targetBB)) {

				if (living instanceof PlayerEntity) continue;

				Vec3d motionVec = getMotionVec().normalize();
				Vec3d targetVec = getVectorToTarget(living).normalize();

				double dot = motionVec.dotProduct(targetVec);

				if (dot > Math.max(closestDot, seekThreshold)) {
					closestDot = dot;
					closestTarget = living;
				}
			}

			if (closestTarget != null) {
				setTarget(closestTarget);
			}
		}
	}

	private Vec3d getMotionVec() {
		return new Vec3d(this.getMotion().getX(), this.getMotion().getY(), this.getMotion().getZ());
	}

	private Vec3d getVectorToTarget(Entity target) {
		return new Vec3d(target.getX() - this.getX(), (target.getY() + (double) target.getEyeHeight()) - this.getY(), target.getZ() - this.getZ());
	}

	@Nullable
	private Entity getTarget() {
		return world.getEntityByID(dataManager.get(TARGET));
	}

	private void setTarget(@Nullable Entity e) {
		dataManager.set(TARGET, e == null ? -1 : e.getEntityId());
	}

	private boolean isThisArrowFlying() {
		return !inGround && getMotion().getX() * getMotion().getX() + getMotion().getY() * getMotion().getY() + getMotion().getZ() * getMotion().getZ() > 1.0;
	}
}
