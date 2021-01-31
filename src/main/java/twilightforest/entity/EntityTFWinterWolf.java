package twilightforest.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import twilightforest.TFSounds;
import twilightforest.biomes.TFBiomes;
import twilightforest.client.particle.TFParticleType;
import twilightforest.entity.ai.EntityAITFBreathAttack;

import java.util.Objects;
import java.util.Optional;
import java.util.Random;

public class EntityTFWinterWolf extends EntityTFHostileWolf implements IBreathAttacker {

	private static final DataParameter<Boolean> BREATH_FLAG = EntityDataManager.createKey(EntityTFWinterWolf.class, DataSerializers.BOOLEAN);
	private static final float BREATH_DAMAGE = 2.0F;

	public EntityTFWinterWolf(EntityType<? extends EntityTFWinterWolf> type, World world) {
		super(type, world);
		setCollarColor(DyeColor.LIGHT_BLUE);
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(0, new SwimGoal(this));
		this.goalSelector.addGoal(2, new EntityAITFBreathAttack<>(this, 1.0F, 5F, 30, 0.1F));
		this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 1.0F, false));
		this.goalSelector.addGoal(6, new WaterAvoidingRandomWalkingGoal(this, 1.0D));

		this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
	}

	public static AttributeModifierMap.MutableAttribute registerAttributes() {
		return EntityTFHostileWolf.registerAttributes()
				.createMutableAttribute(Attributes.MAX_HEALTH, 30.0D)
				.createMutableAttribute(Attributes.ATTACK_DAMAGE, 6);
	}

	@Override
	protected void registerData() {
		super.registerData();
		dataManager.register(BREATH_FLAG, false);
	}

	@Override
	public void livingTick() {
		super.livingTick();

		if (isBreathing()) {
			if (this.world.isRemote) {
				spawnBreathParticles();
			}
			playBreathSound();
		}
	}

	private void spawnBreathParticles() {

		Vector3d look = this.getLookVec();

		final double dist = 0.5;
		double px = this.getPosX() + look.x * dist;
		double py = this.getPosY() + 1.25 + look.y * dist;
		double pz = this.getPosZ() + look.z * dist;

		for (int i = 0; i < 10; i++) {
			double dx = look.x;
			double dy = look.y;
			double dz = look.z;

			double spread   = 5.0 + this.getRNG().nextDouble() * 2.5;
			double velocity = 3.0 + this.getRNG().nextDouble() * 0.15;

			// spread flame
			dx += this.getRNG().nextGaussian() * 0.007499999832361937D * spread;
			dy += this.getRNG().nextGaussian() * 0.007499999832361937D * spread;
			dz += this.getRNG().nextGaussian() * 0.007499999832361937D * spread;
			dx *= velocity;
			dy *= velocity;
			dz *= velocity;

			world.addParticle(TFParticleType.SNOW.get(), px, py, pz, dx, dy, dz);
		}
	}
	
	@Override
	protected SoundEvent getAmbientSound() {
		return TFSounds.WINTER_WOLF_IDLE;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
		return TFSounds.WINTER_WOLF_HURT;
	}

	private void playBreathSound() {
		playSound(TFSounds.WINTER_WOLF_SHOOT, rand.nextFloat() * 0.5F, rand.nextFloat() * 0.5F);
	}

	@Override
	protected float getSoundPitch() {
		return (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 0.6F;
	}

	@Override
	public boolean isBreathing() {
		return dataManager.get(BREATH_FLAG);
	}

	@Override
	public void setBreathing(boolean flag) {
		dataManager.set(BREATH_FLAG, flag);
	}

	@Override
	public void doBreathAttack(Entity target) {
		target.attackEntityFrom(DamageSource.causeMobDamage(this), BREATH_DAMAGE);
	}

	public static boolean canSpawnHere(EntityType<? extends EntityTFWinterWolf> entity, IServerWorld world, SpawnReason reason, BlockPos pos, Random random) {
		Optional<RegistryKey<Biome>> key = world.func_242406_i(pos);
		return Objects.equals(key, Optional.of(TFBiomes.snowy_forest)) || MonsterEntity.isValidLightLevel(world, pos, random);
	}
}
