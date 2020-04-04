package twilightforest.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.RangedAttackGoal;
import net.minecraft.entity.monster.SkeletonEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.HoeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import twilightforest.TwilightForestMod;
import twilightforest.entity.projectile.EntityTFNatureBolt;

//TODO: Extend AbstractSkeletonEntity?
public class EntityTFSkeletonDruid extends SkeletonEntity {

	public static final ResourceLocation LOOT_TABLE = TwilightForestMod.prefix("entities/skeleton_druid");

	public EntityTFSkeletonDruid(EntityType<? extends EntityTFSkeletonDruid> type, World world) {
		super(type, world);
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(4, new RangedAttackGoal(this, 1.25D, 20, 10.0F) {
			@Override
			public void startExecuting() {
				super.startExecuting();
				setSwingingArms(true);
			}

			@Override
			public void resetTask() {
				super.resetTask();
				setSwingingArms(false);
			}
		});
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
	public ResourceLocation getLootTable() {
		return LOOT_TABLE;
	}

	@Override
	public void attackEntityWithRangedAttack(LivingEntity attackTarget, float extraDamage) {
		if (this.getHeldItem(Hand.MAIN_HAND).getItem() instanceof HoeItem) {
			EntityTFNatureBolt natureBolt = new EntityTFNatureBolt(TFEntities.nature_bolt.get(), this.world, this);
			playSound(SoundEvents.ENTITY_GHAST_SHOOT, 1.0F, 1.0F / (rand.nextFloat() * 0.4F + 0.8F));

			double tx = attackTarget.getX() - this.getX();
			double ty = attackTarget.getY() + attackTarget.getEyeHeight() - 2.699999988079071D - this.getY();
			double tz = attackTarget.getZ() - this.getZ();
			float heightOffset = MathHelper.sqrt(tx * tx + tz * tz) * 0.2F;
			natureBolt.shoot(tx, ty + heightOffset, tz, 0.6F, 6.0F);
			this.world.addEntity(natureBolt);
		} else {
			super.attackEntityWithRangedAttack(attackTarget, extraDamage);
		}
	}

	// [VanillaCopy] of super. Edits noted.
	@Override
	protected boolean isValidLightLevel() {
		BlockPos blockpos = new BlockPos(this.getX(), this.getBoundingBox().minY, this.getZ());

		if (this.world.getLightFor(LightType.SKY, blockpos) > this.rand.nextInt(32)) {
			return false;
		} else {
			int i = this.world.getLightFromNeighbors(blockpos);

			// TF - no thunder check
			/*if (this.world.isThundering())
            {
                int j = this.world.getSkylightSubtracted();
                this.world.setSkylightSubtracted(10);
                i = this.world.getLightFromNeighbors(blockpos);
                this.world.setSkylightSubtracted(j);
            }*/

			return i <= this.rand.nextInt(12); // TF - rand(8) -> rand(12)
		}
	}
}
