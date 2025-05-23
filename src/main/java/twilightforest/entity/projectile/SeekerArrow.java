package twilightforest.entity.projectile;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import twilightforest.init.TFEntities;

import java.util.List;

public class SeekerArrow extends TFArrow {

	private static final EntityDataAccessor<Integer> TARGET = SynchedEntityData.defineId(SeekerArrow.class, EntityDataSerializers.INT);

	private static final double seekDistance = 5.0;
	private static final double seekFactor = 0.8;
	private static final double seekAngle = Math.PI / 6.0;
	private static final double seekThreshold = 0.5;

	public SeekerArrow(EntityType<? extends SeekerArrow> type, Level world) {
		super(type, world);
		this.setBaseDamage(1.0D);
	}

	public SeekerArrow(Level world, LivingEntity shooter, ItemStack stack, ItemStack weapon) {
		super(TFEntities.SEEKER_ARROW.get(), world, shooter, stack, weapon);
		this.setBaseDamage(1.0D);
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		builder.define(TARGET, -1);
	}

	@Override
	public void tick() {
		if (isThisArrowFlying()) {
			if (!this.level().isClientSide()) {
				this.updateTarget();
			}

			if (this.level().isClientSide() && !this.isInGround()) {
				for (int i = 0; i < 4; ++i) {
					this.level().addParticle(ParticleTypes.WITCH, this.getX() + this.getDeltaMovement().x() * i / 4.0D, this.getY() + this.getDeltaMovement().y() * i / 4.0D, this.getZ() + this.getDeltaMovement().z() * i / 4.0D, -this.getDeltaMovement().x(), -this.getDeltaMovement().y() + 0.2D, -this.getDeltaMovement().z());
				}
			}

			Entity target = getTarget();
			if (target != null) {

				Vec3 targetVec = getVectorToTarget(target).scale(seekFactor);
				Vec3 courseVec = getMotionVec();

				// vector lengths
				double courseLen = courseVec.length();
				double targetLen = targetVec.length();
				double totalLen = Math.sqrt(courseLen * courseLen + targetLen * targetLen);

				double dotProduct = courseVec.dot(targetVec) / (courseLen * targetLen); // cosine similarity

				if (dotProduct > seekThreshold) {

					// add vector to target, scale to match current velocity
					Vec3 newMotion = courseVec.scale(courseLen / totalLen).add(targetVec.scale(courseLen / totalLen));

					this.setDeltaMovement(newMotion.add(0, 0.045F, 0));

				} else if (!this.level().isClientSide()) {
					// too inaccurate for our intended target, give up on it
					this.setTarget(null);
				}
			}
		}

		super.tick();
	}

	private void updateTarget() {

		Entity target = getTarget();

		if (target != null && !target.isAlive()) {
			target = null;
			this.setTarget(null);
		}

		if (target == null) {
			AABB positionBB = new AABB(getX(), getY(), getZ(), getX(), getY(), getZ());
			AABB targetBB = positionBB;

			// add two possible courses to our selection box
			Vec3 courseVec = getMotionVec().scale(seekDistance).yRot((float) seekAngle);
			targetBB = targetBB.minmax(positionBB.move(courseVec));

			courseVec = getMotionVec().scale(seekDistance).yRot((float) -seekAngle);
			targetBB = targetBB.minmax(positionBB.move(courseVec));

			targetBB = targetBB.inflate(0, seekDistance * 0.5, 0);

			double closestDot = -1.0;
			Entity closestTarget = null;

			List<LivingEntity> entityList = this.level().getEntitiesOfClass(LivingEntity.class, targetBB);
			List<LivingEntity> monsters = entityList.stream().filter(l -> l instanceof Monster).toList();

			if (!monsters.isEmpty()) {
				for (LivingEntity monster : monsters) {
					if (((Monster) monster).getTarget() == this.getOwner()) {
						setTarget(monster);
						return;
					}
				}
				for (LivingEntity monster : monsters) {
					if (monster instanceof NeutralMob) continue;

					if (monster.hasLineOfSight(this)) {
						setTarget(monster);
						return;
					}
				}
			}

			for (LivingEntity living : entityList) {

				if (!living.hasLineOfSight(this)) continue;

				if (living == this.getOwner()) continue;

				if (getOwner() != null && living instanceof TamableAnimal animal && animal.getOwner() == this.getOwner())
					continue;

				Vec3 motionVec = getMotionVec().normalize();
				Vec3 targetVec = getVectorToTarget(living).normalize();

				double dot = motionVec.dot(targetVec);

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

	private Vec3 getMotionVec() {
		return new Vec3(this.getDeltaMovement().x(), this.getDeltaMovement().y(), this.getDeltaMovement().z());
	}

	private Vec3 getVectorToTarget(Entity target) {
		return new Vec3(target.getX() - this.getX(), (target.getY() + target.getEyeHeight()) - this.getY(), target.getZ() - this.getZ());
	}

	@Nullable
	private Entity getTarget() {
		return this.level().getEntity(this.getEntityData().get(TARGET));
	}

	private void setTarget(@Nullable Entity e) {
		this.getEntityData().set(TARGET, e == null ? -1 : e.getId());
	}

	private boolean isThisArrowFlying() {
		return !this.isInGround() && getDeltaMovement().lengthSqr() > 1.0;
	}

	@Override
	protected void onHitEntity(EntityHitResult result) {
		this.setCritArrow(false);
		super.onHitEntity(result);
	}
}
