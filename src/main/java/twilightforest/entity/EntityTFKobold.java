package twilightforest.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import twilightforest.TFSounds;
import twilightforest.entity.ai.EntityAITFFlockToSameKind;
import twilightforest.entity.ai.EntityAITFPanicOnFlockDeath;
import twilightforest.util.TFDamageSources;

public class EntityTFKobold extends MonsterEntity {

	private static final DataParameter<Boolean> PANICKED = EntityDataManager.createKey(EntityTFKobold.class, DataSerializers.BOOLEAN);

	public EntityTFKobold(EntityType<? extends EntityTFKobold> type, World world) {
		super(type, world);
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(0, new SwimGoal(this));
		this.goalSelector.addGoal(1, new EntityAITFPanicOnFlockDeath(this, 2.0F));
		this.goalSelector.addGoal(2, new LeapAtTargetGoal(this, 0.3F));
		this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 1.0D, false));
		this.goalSelector.addGoal(4, new EntityAITFFlockToSameKind(this, 1.0D));
		this.goalSelector.addGoal(6, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
		this.goalSelector.addGoal(7, new LookAtGoal(this, PlayerEntity.class, 8.0F));
		this.goalSelector.addGoal(7, new LookRandomlyGoal(this));
		this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
	}

	@Override
	protected void registerData() {
		super.registerData();
		dataManager.register(PANICKED, false);
	}

	public static AttributeModifierMap.MutableAttribute registerAttributes() {
		return MonsterEntity.func_234295_eP_()
				.createMutableAttribute(Attributes.MAX_HEALTH, 13.0D)
				.createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.28D)
				.createMutableAttribute(Attributes.ATTACK_DAMAGE, 4.0D);
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return TFSounds.KOBOLD_AMBIENT;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return TFSounds.KOBOLD_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return TFSounds.KOBOLD_DEATH;
	}

	public boolean isPanicked() {
		return dataManager.get(PANICKED);
	}

	public void setPanicked(boolean flag) {
		dataManager.set(PANICKED, flag);
	}

	@Override
	public void livingTick() {
		super.livingTick();

		if (world.isRemote && isPanicked()) {
			for (int i = 0; i < 2; i++) {
				this.world.addParticle(ParticleTypes.SPLASH, this.getPosX() + (this.rand.nextDouble() - 0.5D) * this.getWidth() * 0.5, this.getPosY() + this.getEyeHeight(), this.getPosZ() + (this.rand.nextDouble() - 0.5D) * this.getWidth() * 0.5, 0, 0, 0);
			}
		}
	}

	@Override
	public int getMaxSpawnedInChunk() {
		return 8;
	}

	@Override
	public boolean attackEntityAsMob(Entity entityIn) {
		return determineDeathMessage(entityIn, this.getAttribute(Attributes.MAX_HEALTH).getBaseValue() > 50);
	}

	public boolean determineDeathMessage(Entity entityIn, boolean isFinalBoss) {
		if (isFinalBoss) {
			entityIn.attackEntityFrom(TFDamageSources.FINAL_BOSS, (float)this.getAttributeValue(Attributes.ATTACK_DAMAGE));
		}
		return super.attackEntityAsMob(entityIn);
	}
}
