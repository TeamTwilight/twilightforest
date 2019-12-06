package twilightforest.entity;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.MeleeAttackGoal;
import net.minecraft.entity.ai.HurtByTargetGoal;
import net.minecraft.entity.ai.EntityAILeapAtTarget;
import net.minecraft.entity.ai.LookRandomlyGoal;
import net.minecraft.entity.ai.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.SwimGoal;
import net.minecraft.entity.ai.WaterAvoidingRandomWalkingGoal;
import net.minecraft.entity.ai.LookAtGoal;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import twilightforest.TFSounds;
import twilightforest.TwilightForestMod;
import twilightforest.entity.ai.EntityAITFFlockToSameKind;
import twilightforest.entity.ai.EntityAITFPanicOnFlockDeath;

public class EntityTFKobold extends EntityMob {

	public static final ResourceLocation LOOT_TABLE = TwilightForestMod.prefix("entities/kobold");
	private static final DataParameter<Boolean> PANICKED = EntityDataManager.createKey(EntityTFKobold.class, DataSerializers.BOOLEAN);

	public EntityTFKobold(World world) {
		super(world);
		setSize(0.8F, 1.1F);
	}

	@Override
	protected void registerGoals() {
		this.tasks.addTask(0, new SwimGoal(this));
		this.tasks.addTask(1, new EntityAITFPanicOnFlockDeath(this, 2.0F));
		this.tasks.addTask(2, new EntityAILeapAtTarget(this, 0.3F));
		this.tasks.addTask(3, new MeleeAttackGoal(this, 1.0D, false));
		this.tasks.addTask(4, new EntityAITFFlockToSameKind(this, 1.0D));
		this.tasks.addTask(6, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
		this.tasks.addTask(7, new LookAtGoal(this, EntityPlayer.class, 8.0F));
		this.tasks.addTask(7, new LookRandomlyGoal(this));
		this.targetTasks.addTask(1, new HurtByTargetGoal(this, true));
		this.targetTasks.addTask(2, new NearestAttackableTargetGoal<>(this, EntityPlayer.class, true));
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		dataManager.register(PANICKED, false);
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(13.0D);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.28D);
		this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(4.0D);
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

	@Override
	public ResourceLocation getLootTable() {
		return LOOT_TABLE;
	}

	public boolean isPanicked() {
		return dataManager.get(PANICKED);
	}

	public void setPanicked(boolean flag) {
		dataManager.set(PANICKED, flag);
	}

	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();

		if (world.isRemote && isPanicked()) {
			for (int i = 0; i < 2; i++) {
				this.world.spawnParticle(EnumParticleTypes.WATER_SPLASH, this.posX + (this.rand.nextDouble() - 0.5D) * this.width * 0.5, this.posY + this.getEyeHeight(), this.posZ + (this.rand.nextDouble() - 0.5D) * this.width * 0.5, 0, 0, 0);
			}
		}

	}

	@Override
	public int getMaxSpawnedInChunk() {
		return 8;
	}
}
