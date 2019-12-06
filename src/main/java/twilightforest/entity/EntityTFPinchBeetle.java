package twilightforest.entity;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.MeleeAttackGoal;
import net.minecraft.entity.ai.HurtByTargetGoal;
import net.minecraft.entity.ai.LookRandomlyGoal;
import net.minecraft.entity.ai.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.SwimGoal;
import net.minecraft.entity.ai.WaterAvoidingRandomWalkingGoal;
import net.minecraft.entity.ai.LookAtGoal;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import twilightforest.TwilightForestMod;
import twilightforest.entity.ai.EntityAITFChargeAttack;

public class EntityTFPinchBeetle extends EntityMob implements IHostileMount {

	public static final ResourceLocation LOOT_TABLE = TwilightForestMod.prefix("entities/pinch_beetle");

	public EntityTFPinchBeetle(World world) {
		super(world);
		setSize(1.2F, 1.1F);
	}

	@Override
	protected void registerGoals() {
		this.tasks.addTask(0, new SwimGoal(this));
		this.tasks.addTask(2, new EntityAITFChargeAttack(this, 2.0F, false));
		this.tasks.addTask(4, new MeleeAttackGoal(this, 1.0D, false));
		this.tasks.addTask(6, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
		this.tasks.addTask(7, new LookAtGoal(this, EntityPlayer.class, 8.0F));
		this.tasks.addTask(8, new LookRandomlyGoal(this));
		this.targetTasks.addTask(1, new HurtByTargetGoal(this, false));
		this.targetTasks.addTask(2, new NearestAttackableTargetGoal<>(this, EntityPlayer.class, true));
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(40.0D);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.23D);
		this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(4.0D);
		this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(2.0D);
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
	protected void playStepSound(BlockPos pos, Block block) {
		playSound(SoundEvents.ENTITY_SPIDER_STEP, 0.15F, 1.0F);
	}

	@Override
	public void onLivingUpdate() {
		if (!this.getPassengers().isEmpty()) {
			this.setSize(1.9F, 2.0F);

			if (this.getPassengers().get(0).isSneaking()) {
				this.getPassengers().get(0).setSneaking(false);
			}
		} else {
			this.setSize(1.2F, 1.1F);

		}

		super.onLivingUpdate();

		if (!this.getPassengers().isEmpty()) {
			this.getLookHelper().setLookPositionWithEntity(getPassengers().get(0), 100F, 100F);

			// push out of user in wall
			Vec3d riderPos = this.getRiderPosition();
			this.pushOutOfBlocks(riderPos.x, riderPos.y, riderPos.z);
		}
	}

	@Override
	public boolean attackEntityAsMob(Entity entity) {
		if (this.getPassengers().isEmpty() && !entity.isRiding()) {
			entity.startRiding(this);
		}

		return super.attackEntityAsMob(entity);
	}

	@Override
	public float getEyeHeight() {
		return 0.25F;
	}

	@Override
	public void updatePassenger(Entity passenger) {
		if (!this.getPassengers().isEmpty()) {
			Vec3d riderPos = this.getRiderPosition();

			this.getPassengers().get(0).setPosition(riderPos.x, riderPos.y, riderPos.z);
		}
	}

	@Override
	public double getMountedYOffset() {
		return 0.75D;
	}

	private Vec3d getRiderPosition() {
		if (!this.getPassengers().isEmpty()) {
			float distance = 0.9F;

			double dx = Math.cos((this.rotationYaw + 90) * Math.PI / 180.0D) * distance;
			double dz = Math.sin((this.rotationYaw + 90) * Math.PI / 180.0D) * distance;

			return new Vec3d(this.posX + dx, this.posY + this.getMountedYOffset() + this.getPassengers().get(0).getYOffset(), this.posZ + dz);
		} else {
			return new Vec3d(this.posX, this.posY, this.posZ);
		}
	}

	@Override
	public boolean canRiderInteract() {
		return true;
	}

	@Override
	protected ResourceLocation getLootTable() {
		return LOOT_TABLE;
	}
}
