package twilightforest.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.RangedAttackGoal;
import net.minecraft.entity.monster.SkeletonEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.HoeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.*;
import twilightforest.TFSounds;
import twilightforest.entity.projectile.EntityTFNatureBolt;

import java.util.Random;

public class EntityTFSkeletonDruid extends SkeletonEntity {

	public EntityTFSkeletonDruid(EntityType<? extends EntityTFSkeletonDruid> type, World world) {
		super(type, world);
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(4, new RangedAttackGoal(this, 1.25D, 60, 5.0F));
	}
	
	@Override
	protected SoundEvent getAmbientSound() {
	      return TFSounds.SKELETON_DRUID_AMBIENT;
	   }

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
	      return TFSounds.SKELETON_DRUID_HURT;
	   }

	@Override
	protected SoundEvent getDeathSound() {
	      return TFSounds.SKELETON_DRUID_DEATH;
	   }

	@Override
	protected SoundEvent getStepSound() {
	      return TFSounds.SKELETON_DRUID_STEP;
	   }


	@Override
	public void setCombatTask() {
		if (!(this.getHeldItem(Hand.MAIN_HAND).getItem() instanceof HoeItem)) {
			super.setCombatTask();
		}
	}

	@Override
	protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
		this.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.GOLDEN_HOE));
	}

	@Override
	public void attackEntityWithRangedAttack(LivingEntity attackTarget, float extraDamage) {
		if (this.getHeldItem(Hand.MAIN_HAND).getItem() instanceof HoeItem) {
			EntityTFNatureBolt natureBolt = new EntityTFNatureBolt(this.world, this);
			playSound(TFSounds.SKELETON_DRUID_SHOOT, 1.0F, 1.0F / (rand.nextFloat() * 0.4F + 0.8F));

			double tx = attackTarget.getPosX() - this.getPosX();
			double ty = attackTarget.getPosY() + attackTarget.getEyeHeight() - 2.699999988079071D - this.getPosY();
			double tz = attackTarget.getPosZ() - this.getPosZ();
			float heightOffset = MathHelper.sqrt(tx * tx + tz * tz) * 0.2F;
			natureBolt.shoot(tx, ty + heightOffset, tz, 0.6F, 6.0F);
			this.world.addEntity(natureBolt);
		} else {
			super.attackEntityWithRangedAttack(attackTarget, extraDamage);
		}
	}

	public static boolean skeletonDruidSpawnHandler(EntityType<? extends EntityTFSkeletonDruid> entity, IWorld world, SpawnReason reason, BlockPos pos, Random random) {
		return world.getDifficulty() != Difficulty.PEACEFUL && isValidLightLevel(world, pos, random) && canSpawnOn(entity, world, reason, pos, random);
	}

	// [VanillaCopy] of super. Edits noted.
	public static boolean isValidLightLevel(IWorld world, BlockPos pos, Random random) {
		if (world.getLightFor(LightType.SKY, pos) > random.nextInt(32)) {
			return false;
		} else {
			int i = world.getLight(pos);

			// TF - no thunder check
			/*if (this.world.isThundering())
            {
                int j = this.world.getSkylightSubtracted();
                this.world.setSkylightSubtracted(10);
                i = this.world.getLightFromNeighbors(blockpos);
                this.world.setSkylightSubtracted(j);
            }*/

			return i <= random.nextInt(12); // TF - rand(8) -> rand(12)
		}
	}
}
