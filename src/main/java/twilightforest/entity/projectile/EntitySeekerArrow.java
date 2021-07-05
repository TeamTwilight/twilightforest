package twilightforest.entity.projectile;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class EntitySeekerArrow extends EntityTFArrow {

	private static final DataParameter<Integer> TARGET = EntityDataManager.createKey(EntitySeekerArrow.class, DataSerializers.VARINT);

	private static final double seekDistance = 5.0;
	private static final double seekFactor = 0.8;
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
					this.world.addParticle(ParticleTypes.WITCH, this.getPosX() + this.getMotion().getX() * i / 4.0D, this.getPosY() + this.getMotion().getY() * i / 4.0D, this.getPosZ() + this.getMotion().getZ() * i / 4.0D, -this.getMotion().getX(), -this.getMotion().getY() + 0.2D, -this.getMotion().getZ());
				}
			}

			Entity target = getTarget();
			if (target != null) {

				Vector3d targetVec = getVectorToTarget(target).scale(seekFactor);
				Vector3d courseVec = getMotionVec();

				// vector lengths
				double courseLen = courseVec.length();
				double targetLen = targetVec.length();
				double totalLen = Math.sqrt(courseLen*courseLen + targetLen*targetLen);

				double dotProduct = courseVec.dotProduct(targetVec) / (courseLen * targetLen); // cosine similarity

				if (dotProduct > seekThreshold) {

					// add vector to target, scale to match current velocity
					Vector3d newMotion = courseVec.scale(courseLen / totalLen).add(targetVec.scale(courseLen / totalLen));

					this.setMotion(newMotion.add(0, 0.045F, 0));

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
			target = null;
			setTarget(null);
		}

		if (target == null) {
			AxisAlignedBB positionBB = new AxisAlignedBB(getPosX(), getPosY(), getPosZ(), getPosX(), getPosY(), getPosZ());
			AxisAlignedBB targetBB = positionBB;

			// add two possible courses to our selection box
			Vector3d courseVec = getMotionVec().scale(seekDistance).rotateYaw((float) seekAngle);
			targetBB = targetBB.union(positionBB.offset(courseVec));

			courseVec = getMotionVec().scale(seekDistance).rotateYaw((float) -seekAngle);
			targetBB = targetBB.union(positionBB.offset(courseVec));

			targetBB = targetBB.grow(0, seekDistance * 0.5, 0);

			double closestDot = -1.0;
			Entity closestTarget = null;

			List<MonsterEntity> monsters = this.world.getEntitiesWithinAABB(MonsterEntity.class, targetBB);

			for (LivingEntity living : this.world.getEntitiesWithinAABB(LivingEntity.class, targetBB)) {

				if (!monsters.isEmpty()) {
					for (MonsterEntity targets : monsters) {
						Vector3d motionVec = getMotionVec().normalize();
						Vector3d targetVec = getVectorToTarget(living).normalize();
						double dot = motionVec.dotProduct(targetVec);
						if (dot > Math.max(closestDot, seekThreshold)) {
							closestDot = dot;
							closestTarget = targets;
							break;
						}
					}
				}

				if (living instanceof PlayerEntity) {
					continue;
				}

				if (getShooter() != null && living instanceof TameableEntity && ((TameableEntity) living).getOwner() == getShooter()) {
					continue;
				}

				Vector3d motionVec = getMotionVec().normalize();
				Vector3d targetVec = getVectorToTarget(living).normalize();

				double dot = motionVec.dotProduct(targetVec);

				if (dot > Math.max(closestDot, seekThreshold)) {
					closestDot = dot;
					if (monsters.isEmpty()) closestTarget = living;
				}
			}

			if (closestTarget != null) {
				setTarget(closestTarget);
			}
		}
	}

	private Vector3d getMotionVec() {
		return new Vector3d(this.getMotion().getX(), this.getMotion().getY(), this.getMotion().getZ());
	}

	private Vector3d getVectorToTarget(Entity target) {
		return new Vector3d(target.getPosX() - this.getPosX(), (target.getPosY() + target.getEyeHeight()) - this.getPosY(), target.getPosZ() - this.getPosZ());
	}

	@Nullable
	private Entity getTarget() {
		return world.getEntityByID(dataManager.get(TARGET));
	}

	private void setTarget(@Nullable Entity e) {
		dataManager.set(TARGET, e == null ? -1 : e.getEntityId());
	}

	private boolean isThisArrowFlying() {
		return !inGround && getMotion().lengthSquared() > 1.0;
	}
}
