package twilightforest.entity;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.Pose;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import twilightforest.TFSounds;
import twilightforest.entity.ai.EntityAITFChargeAttack;
import twilightforest.util.TFDamageSources;

public class EntityTFPinchBeetle extends MonsterEntity implements IHostileMount {

	public EntityTFPinchBeetle(EntityType<? extends EntityTFPinchBeetle> type, World world) {
		super(type, world);
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(0, new SwimGoal(this));
		this.goalSelector.addGoal(2, new EntityAITFChargeAttack(this, 1.5F, false));
		this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.0D, false));
		this.goalSelector.addGoal(6, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
		this.goalSelector.addGoal(7, new LookAtGoal(this, PlayerEntity.class, 8.0F));
		this.goalSelector.addGoal(8, new LookRandomlyGoal(this));
		this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
	}

	public static AttributeModifierMap.MutableAttribute registerAttributes() {
		return MonsterEntity.func_234295_eP_()
				.createMutableAttribute(Attributes.MAX_HEALTH, 40.0D)
				.createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.23D)
				.createMutableAttribute(Attributes.ATTACK_DAMAGE, 4.0D)
				.createMutableAttribute(Attributes.ARMOR, 2.0D);
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return TFSounds.PINCH_BEETLE_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return TFSounds.PINCH_BEETLE_DEATH;
	}

	@Override
	protected void playStepSound(BlockPos pos, BlockState block) {
		playSound(TFSounds.PINCH_BEETLE_STEP, 0.15F, 1.0F);
	}

	@Override
	public void livingTick() {
		if (!this.getPassengers().isEmpty()) {
			if (this.getPassengers().get(0).isSneaking()) {
				this.getPassengers().get(0).setSneaking(false);
			}
		}

		super.livingTick();

		if (!this.getPassengers().isEmpty()) {
			this.getLookController().setLookPositionWithEntity(getPassengers().get(0), 100F, 100F);

			// push out of user in wall
			Vector3d riderPos = this.getRiderPosition();
			this.pushOutOfBlocks(riderPos.x, riderPos.y, riderPos.z);
		}
	}

	@Override
	public boolean attackEntityAsMob(Entity entity) {
		if (this.getPassengers().isEmpty() && !entity.isPassenger()) {
			entity.startRiding(this);
		}
		entity.attackEntityFrom(TFDamageSources.CLAMPED(this), (float)this.getAttributeValue(Attributes.ATTACK_DAMAGE));
		return super.attackEntityAsMob(entity);
	}

	@Override
	public float getEyeHeight(Pose pose) {
		return 0.25F;
	}

	@Override
	public void updatePassenger(Entity passenger) {
		if (!this.getPassengers().isEmpty()) {
			Vector3d riderPos = this.getRiderPosition();

			this.getPassengers().get(0).setPosition(riderPos.x, riderPos.y, riderPos.z);
		}
	}

	@Override
	public double getMountedYOffset() {
		return 0.75D;
	}

	private Vector3d getRiderPosition() {
		if (!this.getPassengers().isEmpty()) {
			float distance = 0.9F;

			double dx = Math.cos((this.rotationYaw + 90) * Math.PI / 180.0D) * distance;
			double dz = Math.sin((this.rotationYaw + 90) * Math.PI / 180.0D) * distance;

			return new Vector3d(this.getPosX() + dx, this.getPosY() + this.getMountedYOffset() + this.getPassengers().get(0).getYOffset(), this.getPosZ() + dz);
		} else {
			return new Vector3d(this.getPosX(), this.getPosY(), this.getPosZ());
		}
	}

	@Override
	public boolean canRiderInteract() {
		return true;
	}

	@Override
	public EntitySize getSize(Pose pose) {

		if (!this.getPassengers().isEmpty()) {
			return EntitySize.flexible(1.9F, 2.0F);
		} else {
			return super.getSize(pose);
		}
	}
}
