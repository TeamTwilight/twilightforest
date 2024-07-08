package twilightforest.entity.monster;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import twilightforest.data.tags.EntityTagGenerator;
import twilightforest.entity.IHostileMount;
import twilightforest.entity.ai.goal.ChargeAttackGoal;
import twilightforest.init.TFDamageTypes;
import twilightforest.init.TFSounds;
import twilightforest.util.EntityUtil;

public class PinchBeetle extends Monster implements IHostileMount {

	public PinchBeetle(EntityType<? extends PinchBeetle> type, Level world) {
		super(type, world);
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(0, new FloatGoal(this));
		this.goalSelector.addGoal(2, new ChargeAttackGoal(this, 1.5F, false));
		this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.0D, false));
		this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 1.0D));
		this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 8.0F));
		this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
		this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
	}

	public static AttributeSupplier.Builder registerAttributes() {
		return Monster.createMonsterAttributes()
			.add(Attributes.MAX_HEALTH, 40.0D)
			.add(Attributes.MOVEMENT_SPEED, 0.23D)
			.add(Attributes.ATTACK_DAMAGE, 4.0D)
			.add(Attributes.ARMOR, 2.0D);
	}

	@Nullable
	@Override
	protected SoundEvent getAmbientSound() {
		return TFSounds.PINCH_BEETLE_AMBIENT.get();
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return TFSounds.PINCH_BEETLE_HURT.get();
	}

	@Override
	protected SoundEvent getDeathSound() {
		return TFSounds.PINCH_BEETLE_DEATH.get();
	}

	@Override
	protected void playStepSound(BlockPos pos, BlockState state) {
		playSound(TFSounds.PINCH_BEETLE_STEP.get(), 0.15F, 1.0F);
	}

	@Override
	public void aiStep() {
		super.aiStep();
		this.dimensions = this.getDimensions(this.getPose());

		if (!this.getPassengers().isEmpty()) {
			Entity passenger = this.getPassengers().getFirst();

			if (passenger.getVehicle() != this) {
				this.removePassenger(passenger);
				return;
			}

			this.getLookControl().setLookAt(passenger, 100.0F, 100.0F);
			//always set our passenger as our target
			if (passenger instanceof LivingEntity entity) {
				this.setTarget(entity);
			}

			//if our held player switches gamemodes let them go
			if (passenger instanceof Player player && player.getAbilities().invulnerable) {
				player.stopRiding();
				this.setTarget(null);
			}
		}
	}

	@Override
	public void die(DamageSource source) {
		if (!this.getPassengers().isEmpty()) {
			this.getPassengers().forEach(Entity::stopRiding);
		}
		super.die(source);
	}

	@Override
	public void knockback(double x, double y, double z) {
		//only take knockback if not holding something
		if (this.getPassengers().isEmpty()) {
			super.knockback(x, y, z);
		}
	}

	@Override
	public boolean doHurtTarget(Entity entity) {
		if (this.getPassengers().isEmpty()) {
			var v = entity.getVehicle();

			if (v == null || !v.getType().is(EntityTagGenerator.RIDES_OBSTRUCT_SNATCHING)) {
				// Pluck them from the boat, minecart, donkey, or whatever
				entity.stopRiding();

				entity.startRiding(this, true);
			}
		}
		return EntityUtil.properlyApplyCustomDamageSource(this, entity, TFDamageTypes.getEntityDamageSource(this.level(), TFDamageTypes.CLAMPED, this), null);
	}

	@Override
	public boolean startRiding(Entity entity, boolean force) {
		if (entity instanceof Boat boat) {
			boat.kill();
			if (this.level().getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
				for (int i = 0; i < 3; i++) {
					this.spawnAtLocation(boat.getVariant().getPlanks());
				}

				for (int j = 0; j < 2; j++) {
					this.spawnAtLocation(Items.STICK);
				}
			}
			this.playSound(SoundEvents.ZOMBIE_BREAK_WOODEN_DOOR);
			return false;
		}

		return super.startRiding(entity, force);
	}

	@Override
	protected Vec3 getPassengerAttachmentPoint(Entity entity, EntityDimensions dimensions, float yRot) {
		return new Vec3(0.0F, this.getEyeHeight(), 0.75F);
	}

	@Override
	public boolean canRiderInteract() {
		return true;
	}

	@Override
	public EntityDimensions getDefaultDimensions(Pose pose) {

		if (!this.getPassengers().isEmpty()) {
			return EntityDimensions.scalable(2.2F, 1.6F);
		} else {
			return super.getDefaultDimensions(pose);
		}
	}
}
