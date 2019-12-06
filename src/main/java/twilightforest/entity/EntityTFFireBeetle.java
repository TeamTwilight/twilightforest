package twilightforest.entity;

import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import twilightforest.TwilightForestMod;
import twilightforest.entity.ai.EntityAITFBreathAttack;

public class EntityTFFireBeetle extends MobEntity implements IBreathAttacker {

	public static final ResourceLocation LOOT_TABLE = TwilightForestMod.prefix("entities/fire_beetle");
	private static final DataParameter<Boolean> BREATHING = EntityDataManager.createKey(EntityTFFireBeetle.class, DataSerializers.BOOLEAN);
	private static final int BREATH_DURATION = 10;
	private static final int BREATH_DAMAGE = 2;

	public EntityTFFireBeetle(World world) {
		super(world);
		this.isImmuneToFire();
		setSize(1.1F, .75F);
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(0, new SwimGoal(this));
		this.goalSelector.addGoal(2, new EntityAITFBreathAttack<>(this, 1.0F, 5F, 30, 0.1F));
		this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 1.0F, false));
		this.goalSelector.addGoal(6, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
		//this.goalSelector.addGoal(7, new LookAtGoal(this, EntityPlayer.class, 8.0F));
		//this.goalSelector.addGoal(7, new LookRandomlyGoal(this));
		this.targetSelector.addGoal(1, new HurtByTargetGoal(this, false));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
	}

	@Override
	protected void registerData() {
		super.registerData();
		dataManager.register(BREATHING, false);
	}

	@Override
	protected void registerAttributes() {
		super.registerAttributes();
		this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(25.0D);
		this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.23D);
		this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(4.0D);
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return SoundEvents.ENTITY_SPIDER_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.ENTITY_SPIDER_DEATH;
	}

	@Override
	protected void playStepSound(BlockPos pos, BlockState block) {
		playSound(SoundEvents.ENTITY_SPIDER_STEP, 0.15F, 1.0F);
	}

	@Override
	public ResourceLocation getLootTable() {
		return LOOT_TABLE;
	}

	@Override
	public boolean isBreathing() {
		return dataManager.get(BREATHING);
	}

	@Override
	public void setBreathing(boolean flag) {
		dataManager.set(BREATHING, flag);
	}

	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();

		// when breathing fire, spew particles
		if (isBreathing()) {
			Vec3d look = this.getLookVec();

			double dist = 0.9;
			double px = this.posX + look.x * dist;
			double py = this.posY + 0.25 + look.y * dist;
			double pz = this.posZ + look.z * dist;

			for (int i = 0; i < 2; i++) {
				double dx = look.x;
				double dy = look.y;
				double dz = look.z;

				double spread = 5 + this.getRNG().nextDouble() * 2.5;
				double velocity = 0.15 + this.getRNG().nextDouble() * 0.15;

				// spread flame
				dx += this.getRNG().nextGaussian() * 0.007499999832361937D * spread;
				dy += this.getRNG().nextGaussian() * 0.007499999832361937D * spread;
				dz += this.getRNG().nextGaussian() * 0.007499999832361937D * spread;
				dx *= velocity;
				dy *= velocity;
				dz *= velocity;

				world.addParticle(ParticleTypes.FLAME, px, py, pz, dx, dy, dz);
			}

			playSound(SoundEvents.ENTITY_GHAST_SHOOT, rand.nextFloat() * 0.5F, rand.nextFloat() * 0.5F);
		}
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public int getBrightnessForRender() {
		if (isBreathing()) {
			return 15728880;
		} else {
			return super.getBrightnessForRender();
		}
	}

	@Override
	public int getVerticalFaceSpeed() {
		return 500;
	}

	@Override
	public float getEyeHeight(Pose pose) {
		return this.getHeight() * 0.6F;
	}

	@Override
	public CreatureAttribute getCreatureAttribute() {
		return CreatureAttribute.ARTHROPOD;
	}

	@Override
	public void doBreathAttack(Entity target) {
		if (!target.isImmuneToFire() && target.attackEntityFrom(DamageSource.IN_FIRE, BREATH_DAMAGE)) {
			target.setFire(BREATH_DURATION);
		}
	}

}
