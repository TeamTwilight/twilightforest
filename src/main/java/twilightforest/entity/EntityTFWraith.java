package twilightforest.entity;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.FlyingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Difficulty;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import twilightforest.TFSounds;
import twilightforest.TwilightForestMod;
import twilightforest.entity.ai.TFNearestPlayerGoal;

import java.util.EnumSet;
import java.util.Random;

public class EntityTFWraith extends FlyingEntity implements IMob {

	public static final ResourceLocation LOOT_TABLE = TwilightForestMod.prefix("entities/wraith");

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
				taskOwner.getMoveHelper().setMoveTo(target.getX(), target.getY(), target.getZ(), 0.5F);
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
			double d0 = entitymovehelper.getX() - this.parentEntity.getX();
			double d1 = entitymovehelper.getY() - this.parentEntity.getY();
			double d2 = entitymovehelper.getZ() - this.parentEntity.getZ();
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
			double d0 = this.parentEntity.getX() + (double) ((random.nextFloat() * 2.0F - 1.0F) * 16.0F);
			double d1 = this.parentEntity.getY() + (double) ((random.nextFloat() * 2.0F - 1.0F) * 16.0F);
			double d2 = this.parentEntity.getZ() + (double) ((random.nextFloat() * 2.0F - 1.0F) * 16.0F);
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
					double d1 = entitylivingbase.getX() - this.parentEntity.getX();
					double d2 = entitylivingbase.getZ() - this.parentEntity.getZ();
					this.parentEntity.rotationYaw = -((float) MathHelper.atan2(d1, d2)) * (180F / (float) Math.PI);
					this.parentEntity.renderYawOffset = this.parentEntity.rotationYaw;
				}
			}
		}
	}

	@Override
	protected void registerAttributes() {
		super.registerAttributes();
		this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20.0D);
		this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.5D);

		// need to initialize damage since we're not an EntityMob
		this.getAttributes().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
		this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(5.0D);
	}

	@Override
	public boolean canTriggerWalking() {
		return false;
	}

	// [VanillaCopy] EntityMob.attackEntityAsMob. This whole inheritance hierarchy makes me sad.
	@Override
	public boolean attackEntityAsMob(Entity entityIn) {
		float f = (float) this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getValue();
		int i = 0;

		if (entityIn instanceof LivingEntity) {
			f += EnchantmentHelper.getModifierForCreature(this.getHeldItemMainhand(), ((LivingEntity) entityIn).getCreatureAttribute());
			i += EnchantmentHelper.getKnockbackModifier(this);
		}

		boolean flag = entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), f);

		if (flag) {
			if (i > 0 && entityIn instanceof LivingEntity) {
				((LivingEntity) entityIn).knockBack(this, (float) i * 0.5F, (double) MathHelper.sin(this.rotationYaw * 0.017453292F), (double) (-MathHelper.cos(this.rotationYaw * 0.017453292F)));
				this.motionX *= 0.6D;
				this.motionZ *= 0.6D;
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
					float f1 = 0.25F + (float) EnchantmentHelper.getEfficiencyModifier(this) * 0.05F;

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

	@Override
	public ResourceLocation getLootTable() {
		return LOOT_TABLE;
	}

	// [VanillaCopy] Direct copy of EntityMob.isValidLightLevel
	protected boolean isValidLightLevel() {
		BlockPos blockpos = new BlockPos(this.getX(), this.getBoundingBox().minY, this.getZ());

		if (this.world.getLightLevel(LightType.SKY, blockpos) > this.rand.nextInt(32)) {
			return false;
		} else {
			int i = this.world.getLightFromNeighbors(blockpos);

			if (this.world.isThundering()) {
				int j = this.world.getSkylightSubtracted();
				this.world.setSkylightSubtracted(10);
				i = this.world.getLightFromNeighbors(blockpos);
				this.world.setSkylightSubtracted(j);
			}

			return i <= this.rand.nextInt(8);
		}
	}

	@Override
	public boolean getCanSpawnHere() {
		return this.world.getDifficulty() != Difficulty.PEACEFUL && this.isValidLightLevel() && super.getCanSpawnHere();
	}
}
