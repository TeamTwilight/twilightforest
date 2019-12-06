package twilightforest.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.RangedAttackGoal;
import net.minecraft.entity.ai.HurtByTargetGoal;
import net.minecraft.entity.ai.LookRandomlyGoal;
import net.minecraft.entity.ai.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.SwimGoal;
import net.minecraft.entity.ai.WaterAvoidingRandomWalkingGoal;
import net.minecraft.entity.ai.LookAtGoal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import twilightforest.TFSounds;
import twilightforest.TwilightForestMod;

public class EntityTFIceShooter extends EntityTFIceMob implements IRangedAttackMob {

	public static final ResourceLocation LOOT_TABLE = TwilightForestMod.prefix("entities/ice_shooter");

	public EntityTFIceShooter(World world) {
		super(world);
		this.setSize(0.8F, 1.8F);
	}

	@Override
	protected void registerGoals() {
		this.tasks.addTask(0, new SwimGoal(this));
		this.tasks.addTask(1, new RangedAttackGoal(this, 1.25D, 20, 10.0F));
		this.tasks.addTask(2, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
		this.tasks.addTask(3, new LookAtGoal(this, EntityPlayer.class, 8.0F));
		this.tasks.addTask(3, new LookRandomlyGoal(this));
		this.targetTasks.addTask(1, new HurtByTargetGoal(this, true));
		this.targetTasks.addTask(2, new NearestAttackableTargetGoal<>(this, EntityPlayer.class, true));
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.23000000417232513D);
		this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(3.0D);
	}

	@Override
	public float getEyeHeight() {
		return this.height * 0.6F;
	}

	@Override
	public ResourceLocation getLootTable() {
		return LOOT_TABLE;
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return TFSounds.ICE_AMBIENT;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return TFSounds.ICE_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return TFSounds.ICE_DEATH;
	}

	@Override
	public int getMaxSpawnedInChunk() {
		return 8;
	}

	@Override
	public void attackEntityWithRangedAttack(EntityLivingBase target, float distanceFactor) {
		EntityTFIceSnowball snowball = new EntityTFIceSnowball(this.world, this);

		// [VanillaCopy] Adapted from EntitySnowman
		double d0 = target.posY + (double) target.getEyeHeight() - 1.4;
		double d1 = target.posX - this.posX;
		double d2 = d0 - snowball.posY;
		double d3 = target.posZ - this.posZ;
		float f = MathHelper.sqrt(d1 * d1 + d3 * d3) * 0.2F;
		snowball.shoot(d1, d2 + (double) f, d3, 1.6F, 0.0F);

		this.playSound(SoundEvents.ENTITY_SNOWBALL_THROW, 1.0F, 1.0F / (this.getRNG().nextFloat() * 0.4F + 0.8F));
		this.world.spawnEntity(snowball);
	}

	@Override
	public void setSwingingArms(boolean swingingArms) {
	}
}
