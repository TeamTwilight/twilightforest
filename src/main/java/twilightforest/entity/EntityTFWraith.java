package twilightforest.entity;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Difficulty;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import twilightforest.TFSounds;
import twilightforest.entity.ai.TFNearestPlayerGoal;

import java.util.EnumSet;
import java.util.Random;

public class EntityTFWraith extends FlyingEntity implements IMob {

	public EntityTFWraith(EntityType<? extends EntityTFWraith> type, World world) {
		super(type, world);
		moveController = new NoClipMoveHelper(this);
		noClip = true;
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(4, new AIAttack(this));
		this.goalSelector.addGoal(5, new AIFlyTowardsTarget(this));
		this.goalSelector.addGoal(6, new AIRandomFly(this));
		this.goalSelector.addGoal(7, new AILookAround(this));
		this.targetSelector.addGoal(1, new TFNearestPlayerGoal(this));
	}

	static class AIFlyTowardsTarget extends Goal {
		private final EntityTFWraith taskOwner;

		AIFlyTowardsTarget(EntityTFWraith wraith) {
			this.taskOwner = wraith;
			this.setMutexFlags(EnumSet.of(Flag.MOVE));
		}

		@Override
		public boolean shouldExecute() {
			return taskOwner.getAttackTarget() != null;
		}

		@Override
		public boolean shouldContinueExecuting() {
			return false;
		}

		@Override
		public void startExecuting() {
			LivingEntity target = taskOwner.getAttackTarget();
			if (target != null)
				taskOwner.getMoveHelper().setMoveTo(target.getPosX(), target.getPosY(), target.getPosZ(), 0.5F);
		}
	}

	// Similar to MeleeAttackGoal but simpler (no pathfinding)
	static class AIAttack extends Goal {
		private final EntityTFWraith taskOwner;
		private int attackTick = 20;

		AIAttack(EntityTFWraith taskOwner) {
			this.taskOwner = taskOwner;
		}

		@Override
		public boolean shouldExecute() {
			LivingEntity target = taskOwner.getAttackTarget();

			return target != null
					&& target.getBoundingBox().maxY > taskOwner.getBoundingBox().minY
					&& target.getBoundingBox().minY < taskOwner.getBoundingBox().maxY
					&& taskOwner.getDistanceSq(target) <= 4.0D;
		}

		@Override
		public void tick() {
			if (attackTick > 0) {
				attackTick--;
			}
		}

		@Override
		public void resetTask() {
			attackTick = 20;
		}

		@Override
		public void startExecuting() {
			if (taskOwner.getAttackTarget() != null)
				taskOwner.attackEntityAsMob(taskOwner.getAttackTarget());
			attackTick = 20;
		}
	}

	// [VanillaCopy] EntityGhast.AIRandomFly
	static class AIRandomFly extends Goal {
		private final EntityTFWraith parentEntity;

		public AIRandomFly(EntityTFWraith wraith) {
			this.parentEntity = wraith;
			this.setMutexFlags(EnumSet.of(Flag.MOVE));
		}

		@Override
		public boolean shouldExecute() {
			if (parentEntity.getAttackTarget() != null)
				return false;
			MovementController entitymovehelper = this.parentEntity.getMoveHelper();
			double d0 = entitymovehelper.getX() - this.parentEntity.getPosX();
			double d1 = entitymovehelper.getY() - this.parentEntity.getPosY();
			double d2 = entitymovehelper.getZ() - this.parentEntity.getPosZ();
			double d3 = d0 * d0 + d1 * d1 + d2 * d2;
			return d3 < 1.0D || d3 > 3600.0D;
		}

		@Override
		public boolean shouldContinueExecuting() {
			return false;
		}

		@Override
		public void startExecuting() {
			Random random = this.parentEntity.getRNG();
			double d0 = this.parentEntity.getPosX() + (random.nextFloat() * 2.0F - 1.0F) * 16.0F;
			double d1 = this.parentEntity.getPosY() + (random.nextFloat() * 2.0F - 1.0F) * 16.0F;
			double d2 = this.parentEntity.getPosZ() + (random.nextFloat() * 2.0F - 1.0F) * 16.0F;
			this.parentEntity.getMoveHelper().setMoveTo(d0, d1, d2, 0.5D);
		}
	}

	// [VanillaCopy] EntityGhast.AILookAround
	public static class AILookAround extends Goal {
		private final EntityTFWraith parentEntity;

		public AILookAround(EntityTFWraith wraith) {
			this.parentEntity = wraith;
			this.setMutexFlags(EnumSet.of(Flag.LOOK));
		}

		@Override
		public boolean shouldExecute() {
			return true;
		}

		@Override
		public void tick() {
			if (this.parentEntity.getAttackTarget() == null) {
				this.parentEntity.rotationYaw = -((float) MathHelper.atan2(this.parentEntity.getMotion().getX(), this.parentEntity.getMotion().getZ())) * (180F / (float) Math.PI);
				this.parentEntity.renderYawOffset = this.parentEntity.rotationYaw;
			} else {
				LivingEntity entitylivingbase = this.parentEntity.getAttackTarget();

				if (entitylivingbase.getDistanceSq(this.parentEntity) < 4096.0D) {
					double d1 = entitylivingbase.getPosX() - this.parentEntity.getPosX();
					double d2 = entitylivingbase.getPosZ() - this.parentEntity.getPosZ();
					this.parentEntity.rotationYaw = -((float) MathHelper.atan2(d1, d2)) * (180F / (float) Math.PI);
					this.parentEntity.renderYawOffset = this.parentEntity.rotationYaw;
				}
			}
		}
	}

	public static AttributeModifierMap.MutableAttribute registerAttributes() {
		return MobEntity.func_233666_p_()
				.createMutableAttribute(Attributes.MAX_HEALTH, 20.0D)
				.createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.5D)
				.createMutableAttribute(Attributes.ATTACK_DAMAGE, 5.0D);
	}

	@Override
	public boolean isSteppingCarefully() {
		return false;
	}

	// [VanillaCopy] EntityMob.attackEntityAsMob. This whole inheritance hierarchy makes me sad.
	@Override
	public boolean attackEntityAsMob(Entity entityIn) {
		float f = (float) this.getAttribute(Attributes.ATTACK_DAMAGE).getValue();
		int i = 0;

		if (entityIn instanceof LivingEntity) {
			f += EnchantmentHelper.getModifierForCreature(this.getHeldItemMainhand(), ((LivingEntity) entityIn).getCreatureAttribute());
			i += EnchantmentHelper.getKnockbackModifier(this);
		}

		boolean flag = entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), f);

		if (flag) {
			if (i > 0 && entityIn instanceof LivingEntity) {
				((LivingEntity) entityIn).applyKnockback(i * 0.5F, MathHelper.sin(this.rotationYaw * 0.017453292F), (-MathHelper.cos(this.rotationYaw * 0.017453292F)));
				this.setMotion(getMotion().getX() * 0.6D, getMotion().getY(), getMotion().getZ() * 0.6D);
			}

			int j = EnchantmentHelper.getFireAspectModifier(this);

			if (j > 0) {
				entityIn.setFire(j * 4);
			}

			if (entityIn instanceof PlayerEntity) {
				PlayerEntity entityplayer = (PlayerEntity) entityIn;
				ItemStack itemstack = this.getHeldItemMainhand();
				ItemStack itemstack1 = entityplayer.isHandActive() ? entityplayer.getActiveItemStack() : ItemStack.EMPTY;

				if (!itemstack.isEmpty() && !itemstack1.isEmpty() && itemstack.getItem() instanceof AxeItem && itemstack1.getItem() == Items.SHIELD) {
					float f1 = 0.25F + EnchantmentHelper.getEfficiencyModifier(this) * 0.05F;

					if (this.rand.nextFloat() < f1) {
						entityplayer.getCooldownTracker().setCooldown(Items.SHIELD, 100);
						this.world.setEntityState(entityplayer, (byte) 30);
					}
				}
			}

			this.applyEnchantments(this, entityIn);
		}

		return flag;
	}

	private void despawnIfPeaceful() {
		if (!world.isRemote && world.getDifficulty() == Difficulty.PEACEFUL)
			remove();
	}

	@Override
	public void livingTick() {
		super.livingTick();
		despawnIfPeaceful();
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		if (super.attackEntityFrom(source, amount)) {
			Entity entity = source.getTrueSource();
			if (getRidingEntity() == entity || getPassengers().contains(entity)) {
				return true;
			}
			if (entity != this && entity instanceof LivingEntity) {
				setAttackTarget((LivingEntity) entity);
			}
			return true;
		} else {
			return false;
		}
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return TFSounds.WRAITH;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return TFSounds.WRAITH;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return TFSounds.WRAITH;
	}

	public static boolean getCanSpawnHere(EntityType<? extends EntityTFWraith> entity, IServerWorld world, SpawnReason reason, BlockPos pos, Random random) {
		return world.getDifficulty() != Difficulty.PEACEFUL && MonsterEntity.isValidLightLevel(world, pos, random) && canSpawnOn(entity, world, reason, pos, random);
	}
}
