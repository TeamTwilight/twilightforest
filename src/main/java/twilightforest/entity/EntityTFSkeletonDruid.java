package twilightforest.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIAttackRanged;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import twilightforest.TwilightForestMod;

public class EntityTFSkeletonDruid extends EntitySkeleton {
	public static final ResourceLocation LOOT_TABLE = new ResourceLocation(TwilightForestMod.ID, "entities/skeleton_druid");

	public EntityTFSkeletonDruid(World world) {
		super(world);
	}

	@Override
	protected void initEntityAI() {
		super.initEntityAI();
		this.tasks.addTask(4, new EntityAIAttackRanged(this, 1.25D, 20, 10.0F) {
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
		// Don't mess with tasks when switching items
	}

	@Override
	protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
		this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.GOLDEN_HOE));
	}

	@Override
	public ResourceLocation getLootTable() {
		return LOOT_TABLE;
	}

	@Override
	public void attackEntityWithRangedAttack(EntityLivingBase attackTarget, float extraDamage) {
		EntityTFNatureBolt natureBolt = new EntityTFNatureBolt(this.world, this);
		playSound(SoundEvents.ENTITY_GHAST_SHOOT, 1.0F, 1.0F / (rand.nextFloat() * 0.4F + 0.8F));

		double tx = attackTarget.posX - this.posX;
		double ty = attackTarget.posY + attackTarget.getEyeHeight() - 2.699999988079071D - this.posY;
		double tz = attackTarget.posZ - this.posZ;
		float heightOffset = MathHelper.sqrt(tx * tx + tz * tz) * 0.2F;
		natureBolt.shoot(tx, ty + heightOffset, tz, 0.6F, 6.0F);
		this.world.spawnEntity(natureBolt);
	}

	// [VanillaCopy] of super. Edits noted.
	@Override
	protected boolean isValidLightLevel() {
		BlockPos blockpos = new BlockPos(this.posX, this.getEntityBoundingBox().minY, this.posZ);

		if (this.world.getLightFor(EnumSkyBlock.SKY, blockpos) > this.rand.nextInt(32)) {
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
