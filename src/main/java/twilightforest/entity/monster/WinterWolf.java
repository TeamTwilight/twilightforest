package twilightforest.entity.monster;

import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
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
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.phys.Vec3;
import twilightforest.entity.IBreathAttacker;
import twilightforest.entity.ai.goal.BreathAttackGoal;
import twilightforest.init.TFBiomes;
import twilightforest.init.TFParticleType;
import twilightforest.init.TFSounds;

import java.util.Objects;
import java.util.Optional;

public class WinterWolf extends HostileWolf implements IBreathAttacker {

	private static final EntityDataAccessor<Boolean> BREATH_FLAG = SynchedEntityData.defineId(WinterWolf.class, EntityDataSerializers.BOOLEAN);
	private static final float BREATH_DAMAGE = 2.0F;

	public WinterWolf(EntityType<? extends WinterWolf> type, Level world) {
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

	public static AttributeSupplier.Builder registerAttributes() {
		return HostileWolf.registerAttributes()
			.add(Attributes.MAX_HEALTH, 30.0D)
			.add(Attributes.ATTACK_DAMAGE, 6);
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		builder.define(BREATH_FLAG, false);
	}

	@Override
	public void aiStep() {
		super.aiStep();

		if (this.isBreathing()) {
			if (this.level().isClientSide()) {
				this.spawnBreathParticles();
			}
			this.playBreathSound();
		}
	}

	private void spawnBreathParticles() {

		Vec3 look = this.getLookAngle();

		final double dist = 0.5;
		double px = this.getX() + look.x() * dist;
		double py = this.getY() + 1.25 + look.y() * dist;
		double pz = this.getZ() + look.z() * dist;

		for (int i = 0; i < 10; i++) {
			double dx = look.x();
			double dy = look.y();
			double dz = look.z();

			double spread = 5.0 + this.getRandom().nextDouble() * 2.5;
			double velocity = 3.0 + this.getRandom().nextDouble() * 0.15;

			// spread flame
			dx += this.getRandom().nextGaussian() * 0.0075D * spread;
			dy += this.getRandom().nextGaussian() * 0.0075D * spread;
			dz += this.getRandom().nextGaussian() * 0.0075D * spread;
			dx *= velocity;
			dy *= velocity;
			dz *= velocity;

			this.level().addParticle(TFParticleType.SNOW.get(), px, py, pz, dx, dy, dz);
		}
	}

	@Override
	protected SoundEvent getTargetSound() {
		return TFSounds.WINTER_WOLF_TARGET.get();
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return TFSounds.WINTER_WOLF_AMBIENT.get();
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
		return TFSounds.WINTER_WOLF_HURT.get();
	}

	private void playBreathSound() {
		playSound(TFSounds.WINTER_WOLF_SHOOT.get(), this.getRandom().nextFloat() * 0.5F, this.getRandom().nextFloat() * 0.5F);
	}

	@Override
	protected SoundEvent getDeathSound() {
		return TFSounds.WINTER_WOLF_DEATH.get();
	}

	@Override
	public float getVoicePitch() {
		return (this.getRandom().nextFloat() - this.getRandom().nextFloat()) * 0.2F + 0.6F;
	}

	@Override
	public boolean isBreathing() {
		return this.getEntityData().get(BREATH_FLAG);
	}

	@Override
	public void setBreathing(boolean flag) {
		this.getEntityData().set(BREATH_FLAG, flag);
	}

	@Override
	public void doBreathAttack(ServerLevel level, Entity target) {
		target.hurtServer(level, this.damageSources().mobAttack(this), BREATH_DAMAGE);
	}

	public static boolean checkWinterSpawnRules(EntityType<? extends WinterWolf> entity, ServerLevelAccessor accessor, EntitySpawnReason reason, BlockPos pos, RandomSource random) {
		Optional<ResourceKey<Biome>> key = accessor.getBiome(pos).unwrapKey();
		return accessor.getDifficulty() != Difficulty.PEACEFUL && Objects.equals(key, Optional.of(TFBiomes.SNOWY_FOREST)) || Monster.isDarkEnoughToSpawn(accessor, pos, random);
	}
}
