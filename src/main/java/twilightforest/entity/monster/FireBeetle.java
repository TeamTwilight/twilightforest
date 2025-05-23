package twilightforest.entity.monster;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import twilightforest.entity.IBreathAttacker;
import twilightforest.entity.ai.goal.BreathAttackGoal;
import twilightforest.init.TFDamageTypes;
import twilightforest.init.TFSounds;

public class FireBeetle extends Monster implements IBreathAttacker {

	private static final EntityDataAccessor<Boolean> BREATHING = SynchedEntityData.defineId(FireBeetle.class, EntityDataSerializers.BOOLEAN);
	private static final int BREATH_DURATION = 10;
	private static final int BREATH_DAMAGE = 2;

	public FireBeetle(EntityType<? extends FireBeetle> type, Level world) {
		super(type, world);
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(0, new FloatGoal(this));
		this.goalSelector.addGoal(2, new BreathAttackGoal<>(this, 5F, 30, 0.1F));
		this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 1.0F, false));
		this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 1.0D));
		this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		builder.define(BREATHING, false);
	}

	public static AttributeSupplier.Builder registerAttributes() {
		return Monster.createMonsterAttributes()
			.add(Attributes.MAX_HEALTH, 25.0D)
			.add(Attributes.MOVEMENT_SPEED, 0.23D)
			.add(Attributes.ATTACK_DAMAGE, 4.0D);
	}

	@Nullable
	@Override
	protected SoundEvent getAmbientSound() {
		return TFSounds.FIRE_BEETLE_AMBIENT.get();
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return TFSounds.FIRE_BEETLE_HURT.get();
	}

	@Override
	protected SoundEvent getDeathSound() {
		return TFSounds.FIRE_BEETLE_DEATH.get();
	}

	@Override
	protected void playStepSound(BlockPos pos, BlockState block) {
		playSound(TFSounds.FIRE_BEETLE_STEP.get(), 0.15F, 1.0F);
	}

	@Override
	public boolean isBreathing() {
		return this.getEntityData().get(BREATHING);
	}

	@Override
	public void setBreathing(boolean flag) {
		this.getEntityData().set(BREATHING, flag);
	}

	@Override
	public void aiStep() {
		super.aiStep();

		// when breathing fire, spew particles
		if (this.isBreathing()) {
			Vec3 look = this.getLookAngle();

			double dist = 0.9;
			double px = this.getX() + look.x() * dist;
			double py = this.getY() + 0.25D + look.y() * dist;
			double pz = this.getZ() + look.z() * dist;

			for (int i = 0; i < 2; i++) {
				double dx = look.x();
				double dy = look.y();
				double dz = look.z();

				double spread = 5 + this.getRandom().nextDouble() * 2.5;
				double velocity = 0.15 + this.getRandom().nextDouble() * 0.15;

				// spread flame
				dx += this.getRandom().nextGaussian() * 0.0075D * spread;
				dy += this.getRandom().nextGaussian() * 0.0075D * spread;
				dz += this.getRandom().nextGaussian() * 0.0075D * spread;
				dx *= velocity;
				dy *= velocity;
				dz *= velocity;

				this.level().addParticle(ParticleTypes.FLAME, px, py, pz, dx, dy, dz);
			}

			playSound(TFSounds.FIRE_BEETLE_SHOOT.get(), this.getRandom().nextFloat() * 0.5F, this.getRandom().nextFloat() * 0.5F);
			this.gameEvent(GameEvent.PROJECTILE_SHOOT);
		}
	}

	@Override
	public int getMaxHeadXRot() {
		return 500;
	}

	@Override
	public void doBreathAttack(ServerLevel level, Entity target) {
		if (!target.fireImmune() && target.hurtServer(level, TFDamageTypes.getEntityDamageSource(this.level(), TFDamageTypes.SCORCHED, this), BREATH_DAMAGE)) {
			target.igniteForSeconds(BREATH_DURATION);
		}
	}

	@Override
	public boolean doHurtTarget(ServerLevel level, Entity entity) {
		if (this.isBreathing()) {
			return entity.hurtServer(level, TFDamageTypes.getEntityDamageSource(this.level(), TFDamageTypes.SCORCHED, this), BREATH_DAMAGE);
		}
		return super.doHurtTarget(level, entity);
	}
}
