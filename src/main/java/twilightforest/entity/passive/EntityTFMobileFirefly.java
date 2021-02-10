package twilightforest.entity.passive;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.passive.AmbientEntity;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import twilightforest.client.particle.PinnedFireflyData;

import java.util.Random;

public class EntityTFMobileFirefly extends AmbientEntity {
	private BlockPos spawnPosition;

	public EntityTFMobileFirefly(EntityType<? extends EntityTFMobileFirefly> type, World world) {
		super(type, world);
	}

	@Override
	protected float getSoundVolume() {
		return 0.1F;
	}

	@Override
	protected float getSoundPitch() {
		return super.getSoundPitch() * 0.95F;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return SoundEvents.ENTITY_BAT_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.ENTITY_BAT_DEATH;
	}

	@Override
	public boolean canBePushed() {
		return false;
	}

	@Override
	protected void collideWithEntity(Entity entity) {
	}

	public static AttributeModifierMap.MutableAttribute registerAttributes() {
		return MobEntity.func_233666_p_()
				.createMutableAttribute(Attributes.MAX_HEALTH, 6.0D);
	}

	@Override
	public void tick() {
		super.tick();

		Vector3d motion = getMotion();
		this.setMotion(motion.x, motion.y * 0.6000000238418579D, motion.z);

		if (world.isRemote && ticksExisted % 30 == 0) {
			world.addParticle(new PinnedFireflyData(getEntityId()), getPosX(), getPosY(), getPosZ(), 0, 0, 0);
		}
	}

	@Override
	protected void updateAITasks() {
		super.updateAITasks();

		// [VanillaCopy] direct from last half of EntityBat.updateAITasks
		if (this.spawnPosition != null && (!this.world.isAirBlock(this.spawnPosition) || this.spawnPosition.getY() < 1)) {
			this.spawnPosition = null;
		}

		// TODO: True adds 0.5
		if (this.spawnPosition == null || this.rand.nextInt(30) == 0 || this.spawnPosition.distanceSq(((int) this.getPosX()), ((int) this.getPosY()), ((int) this.getPosZ()), false) < 4.0D) {
			this.spawnPosition = new BlockPos((int) this.getPosX() + this.rand.nextInt(7) - this.rand.nextInt(7), (int) this.getPosY() + this.rand.nextInt(6) - 2, (int) this.getPosZ() + this.rand.nextInt(7) - this.rand.nextInt(7));
		}

		double d0 = this.spawnPosition.getX() + 0.5D - this.getPosX();
		double d1 = this.spawnPosition.getY() + 0.1D - this.getPosY();
		double d2 = this.spawnPosition.getZ() + 0.5D - this.getPosZ();
		this.setMotion(this.getMotion().add(new Vector3d(
				(Math.signum(d0) * 0.5D - this.getMotion().getX()) * 0.10000000149011612D,
				(Math.signum(d1) * 0.699999988079071D - this.getMotion().getY()) * 0.10000000149011612D,
				(Math.signum(d2) * 0.5D - this.getMotion().getZ()) * 0.10000000149011612D
		)));
		float f = (float) (MathHelper.atan2(this.getMotion().getZ(), this.getMotion().getX()) * (180D / Math.PI)) - 90.0F;
		float f1 = MathHelper.wrapDegrees(f - this.rotationYaw);
		this.moveForward = 0.5F;
		this.rotationYaw += f1;
		// End copy
	}

	@Override
	public boolean isSteppingCarefully() {
		return false;
	}

	@Override
	public boolean onLivingFall(float dist, float mult) {
		return false;
	}

	@Override
	protected void updateFallState(double y, boolean onGround, BlockState state, BlockPos pos) {
	}

	@Override
	public boolean doesEntityNotTriggerPressurePlate() {
		return true;
	}

	// [VanillaCopy] EntityBat.getCanSpawnHere. Edits noted.
	public static boolean getCanSpawnHere(EntityType<EntityTFMobileFirefly> entity, IWorld world, SpawnReason reason, BlockPos pos, Random random) {
		return pos.getY() < world.getSeaLevel()
				&& ! random.nextBoolean()
				&& world.getLight(pos) <= random.nextInt(4)
				&& canSpawnOn(entity, world, reason, pos, random);
	}
}
