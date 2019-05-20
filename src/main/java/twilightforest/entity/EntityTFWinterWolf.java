package twilightforest.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import twilightforest.TwilightForestMod;
import twilightforest.biomes.TFBiomes;
import twilightforest.client.particle.TFParticleType;
import twilightforest.entity.ai.EntityAITFBreathAttack;

public class EntityTFWinterWolf extends EntityTFHostileWolf implements IBreathAttacker {

	public static final ResourceLocation LOOT_TABLE = TwilightForestMod.prefix("entities/winter_wolf");
	private static final DataParameter<Boolean> BREATH_FLAG = EntityDataManager.createKey(EntityTFWinterWolf.class, DataSerializers.BOOLEAN);
	private static final float BREATH_DAMAGE = 2.0F;

	public EntityTFWinterWolf(World world) {
		super(world);
		this.setSize(1.4F, 1.9F);
		setCollarColor(EnumDyeColor.LIGHT_BLUE);
	}

	@Override
	protected void initEntityAI() {
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(2, new EntityAITFBreathAttack(this, 1.0F, 5F, 30, 0.1F));
		this.tasks.addTask(3, new EntityAIAttackMelee(this, 1.0F, false));
		this.tasks.addTask(6, new EntityAIWander(this, 1.0F));

		this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<>(this, EntityPlayer.class, true));
	}

	@Override
	protected void setAttributes() {
		super.setAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(30.0D);
		this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(6);
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		dataManager.register(BREATH_FLAG, false);
	}

	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();

		if (isBreathing()) {
			if (this.world.isRemote) {
				spawnBreathParticles();
			}
			playBreathSound();
		}
	}

	private void spawnBreathParticles() {

		Vec3d look = this.getLookVec();

		final double dist = 0.5;
		double px = this.posX + look.x * dist;
		double py = this.posY + 1.25 + look.y * dist;
		double pz = this.posZ + look.z * dist;

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

			TwilightForestMod.proxy.spawnParticle(TFParticleType.SNOW, px, py, pz, dx, dy, dz);
		}
	}

	private void playBreathSound() {
		playSound(SoundEvents.ENTITY_GHAST_SHOOT, rand.nextFloat() * 0.5F, rand.nextFloat() * 0.5F);
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

	@Override
	protected boolean isValidLightLevel() {
		return world.getBiome(new BlockPos(this)) == TFBiomes.snowy_forest
				|| super.isValidLightLevel();
	}

	@Override
	public ResourceLocation getLootTable() {
		return LOOT_TABLE;
	}

}
