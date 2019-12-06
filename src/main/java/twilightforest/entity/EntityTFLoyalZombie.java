package twilightforest.entity;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.MeleeAttackGoal;
import net.minecraft.entity.ai.EntityAIFollowOwner;
import net.minecraft.entity.ai.HurtByTargetGoal;
import net.minecraft.entity.ai.LookRandomlyGoal;
import net.minecraft.entity.ai.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.EntityAIOwnerHurtByTarget;
import net.minecraft.entity.ai.EntityAIOwnerHurtTarget;
import net.minecraft.entity.ai.SwimGoal;
import net.minecraft.entity.ai.WaterAvoidingRandomWalkingGoal;
import net.minecraft.entity.ai.LookAtGoal;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntityTFLoyalZombie extends EntityTameable {

	public EntityTFLoyalZombie(World world) {
		super(world);
		this.setSize(0.6F, 1.8F);
	}

	@Override
	protected void registerGoals() {
		this.tasks.addTask(1, new SwimGoal(this));
		this.tasks.addTask(4, new MeleeAttackGoal(this, 1.0D, true));
		this.tasks.addTask(5, new EntityAIFollowOwner(this, 1.0D, 10.0F, 2.0F));
		this.tasks.addTask(7, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
		this.tasks.addTask(9, new LookAtGoal(this, EntityPlayer.class, 8.0F));
		this.tasks.addTask(9, new LookRandomlyGoal(this));
		this.targetTasks.addTask(1, new EntityAIOwnerHurtByTarget(this));
		this.targetTasks.addTask(2, new EntityAIOwnerHurtTarget(this));
		this.targetTasks.addTask(3, new HurtByTargetGoal(this, true));
		this.targetTasks.addTask(4, new NearestAttackableTargetGoal<>(this, EntityMob.class, true));
	}

	@Override
	public EntityAnimal createChild(EntityAgeable entityanimal) {
		return null;
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(40.0D);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.3D);
		this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(3.0D);
	}

	@Override
	public boolean attackEntityAsMob(Entity entity) {
		boolean success = entity.attackEntityFrom(DamageSource.causeMobDamage(this), 7);

		if (success) {
			entity.motionY += 0.2;
		}

		return success;
	}

	@Override
	public void onLivingUpdate() {
		// once our damage boost effect wears out, start to burn
		// the effect here is that we die shortly after our 60 second lifespan
		if (!this.world.isRemote && this.getActivePotionEffect(MobEffects.STRENGTH) == null) {
			this.setFire(100);
		}

		super.onLivingUpdate();
	}

	// [VanillaCopy] EntityWolf.shouldAttackEntity, substituting with our class
	@Override
	public boolean shouldAttackEntity(EntityLivingBase target, EntityLivingBase owner) {
		if (!(target instanceof EntityCreeper) && !(target instanceof EntityGhast)) {
			if (target instanceof EntityTFLoyalZombie) {
				EntityTFLoyalZombie zombie = (EntityTFLoyalZombie) target;

				if (zombie.isTamed() && zombie.getOwner() == owner) {
					return false;
				}
			}

			return target instanceof EntityPlayer && owner instanceof EntityPlayer && !((EntityPlayer) owner).canAttackPlayer((EntityPlayer) target) ? false : !(target instanceof AbstractHorse) || !((AbstractHorse) target).isTame();
		} else {
			return false;
		}
	}

	@Override
	protected boolean canDespawn() {
		return !this.isTamed();
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return SoundEvents.ENTITY_ZOMBIE_AMBIENT;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return SoundEvents.ENTITY_ZOMBIE_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.ENTITY_ZOMBIE_DEATH;
	}

	@Override
	protected void playStepSound(BlockPos pos, Block block) {
		playSound(SoundEvents.ENTITY_ZOMBIE_STEP, 0.15F, 1.0F);
	}

	@Override
	public EnumCreatureAttribute getCreatureAttribute() {
		return EnumCreatureAttribute.UNDEAD;
	}
}
