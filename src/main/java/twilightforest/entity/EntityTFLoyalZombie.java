package twilightforest.entity;

import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.monster.GhastEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.passive.horse.AbstractHorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import twilightforest.TFSounds;

public class EntityTFLoyalZombie extends TameableEntity {

	public EntityTFLoyalZombie(EntityType<? extends EntityTFLoyalZombie> type, World world) {
		super(type, world);
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(1, new SwimGoal(this));
		this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.0D, true));
		this.goalSelector.addGoal(5, new FollowOwnerGoal(this, 1.0D, 10.0F, 2.0F, true));
		this.goalSelector.addGoal(7, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
		this.goalSelector.addGoal(9, new LookAtGoal(this, PlayerEntity.class, 8.0F));
		this.goalSelector.addGoal(9, new LookRandomlyGoal(this));
		this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
		this.targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
		this.targetSelector.addGoal(3, new HurtByTargetGoal(this));
		this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, MonsterEntity.class, true));
	}

	@Override
	public AnimalEntity func_241840_a(ServerWorld world, AgeableEntity entityanimal) {
		return null;
	}

	public static AttributeModifierMap.MutableAttribute registerAttributes() {
		return MobEntity.func_233666_p_()
				.createMutableAttribute(Attributes.MAX_HEALTH, 40.0D)
				.createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.3D)
				.createMutableAttribute(Attributes.ARMOR, 3.0D);
	}

	@Override
	public boolean attackEntityAsMob(Entity entity) {
		boolean success = entity.attackEntityFrom(DamageSource.causeMobDamage(this), 7);

		if (success) {
			entity.addVelocity(0, 0.2, 0);
		}

		return success;
	}

	@Override
	public void livingTick() {
		// once our damage boost effect wears out, start to burn
		// the effect here is that we die shortly after our 60 second lifespan
		if (!this.world.isRemote && this.getActivePotionEffect(Effects.STRENGTH) == null) {
			this.setFire(100);
		}

		super.livingTick();
	}

	/**
	 * [VanillaCopy] {@link net.minecraft.entity.passive.WolfEntity#shouldAttackEntity}, substituting with our class
 	 */
	@Override
	public boolean shouldAttackEntity(LivingEntity target, LivingEntity owner) {
		if (!(target instanceof CreeperEntity) && !(target instanceof GhastEntity)) {
			if (target instanceof EntityTFLoyalZombie) {
				EntityTFLoyalZombie zombie = (EntityTFLoyalZombie) target;
				return !zombie.isTamed() || zombie.getOwner() != owner;
			} else if (target instanceof PlayerEntity && owner instanceof PlayerEntity && !((PlayerEntity)owner).canAttackPlayer((PlayerEntity)target)) {
				return false;
			} else if (target instanceof AbstractHorseEntity && ((AbstractHorseEntity)target).isTame()) {
				return false;
			} else {
				return !(target instanceof TameableEntity) || !((TameableEntity)target).isTamed();
			}
		} else {
			return false;
		}
	}

	@Override
	public boolean canDespawn(double distanceToClosestPlayer) {
		return !this.isTamed();
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return TFSounds.LOYAL_ZOMBIE_AMBIENT;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return TFSounds.LOYAL_ZOMBIE_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return TFSounds.LOYAL_ZOMBIE_DEATH;
	}

	@Override
	protected void playStepSound(BlockPos pos, BlockState block) {
		playSound(TFSounds.LOYAL_ZOMBIE_STEP, 0.15F, 1.0F);
	}

	@Override
	public CreatureAttribute getCreatureAttribute() {
		return CreatureAttribute.UNDEAD;
	}
}
