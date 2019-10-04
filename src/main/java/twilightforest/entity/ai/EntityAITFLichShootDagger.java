package twilightforest.entity.ai;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import twilightforest.entity.boss.EntityTFLich;
import twilightforest.entity.boss.EntityTFThrownWep;

public class EntityAITFLichShootDagger extends EntityAIBase {
	private EntityTFLich lich;
	private int attackTick;
	private EntityLivingBase attackTarget;
	public EntityAITFLichShootDagger(EntityTFLich entityTFLich) {
		this.lich = entityTFLich;
		this.setMutexBits(3);
	}


	@Override
	public boolean shouldExecute() {
		this.attackTarget = this.lich.getAttackTarget();

		if (this.attackTarget == null) {
			return false;
		} else {
			return this.lich.getPhase() == 3 && this.lich.getHealth() > this.lich.getMaxHealth() / 3 && this.lich.getRNG().nextInt(50) == 0 && this.lich.canEntityBeSeen(this.attackTarget);
		}
	}

	@Override
	public boolean shouldContinueExecuting() {
		return this.attackTick >= 0;
	}

	@Override
	public void startExecuting() {
		this.lich.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, ItemStack.EMPTY);
		attackTick = 30 + this.lich.getRNG().nextInt(20);
		this.lich.setChargeDagger(true);
		this.lich.playSound(SoundEvents.ITEM_ARMOR_EQUIP_GOLD, 2, 1.1F);

		double d0 = this.attackTarget.posX - this.lich.posX;
		double d1 = this.attackTarget.posZ - this.lich.posZ;
		float f = MathHelper.sqrt(d0 * d0 + d1 * d1);

		if(this.lich.onGround) {
			if ((double) f >= 1.0E-4D) {
				this.lich.motionX -= d0 / (double) f * 0.8D * 0.7D + this.lich.motionX * 0.3D;
				this.lich.motionZ -= d1 / (double) f * 0.8D * 0.7D + this.lich.motionZ * 0.3D;
			}

			this.lich.motionY = 0.3F;
		}
	}

	@Override
	public void updateTask() {
		this.lich.getLookHelper().setLookPositionWithEntity(attackTarget, 30.0F, 30.0F);

		if (this.attackTick-- == 0) {
			this.lich.setChargeDagger(false);
			this.lich.playSound(SoundEvents.ENTITY_ARROW_SHOOT, 2, 1F + this.lich.getRNG().nextFloat() * 0.1F);
			this.shootDagger(this.attackTarget);
		}
	}

	private void shootDagger(Entity targetedEntity) {
		float bodyFacingAngle = ((this.lich.renderYawOffset * 3.141593F) / 180F);
		double sx = this.lich.posX + (MathHelper.cos(bodyFacingAngle) * 1);
		double sy = this.lich.posY + (this.lich.height * 0.82);
		double sz = this.lich.posZ + (MathHelper.sin(bodyFacingAngle) * 1);

		double tx = targetedEntity.posX - sx;
		double ty = (targetedEntity.getEntityBoundingBox().minY + (double) (targetedEntity.height / 2.0F)) - (this.lich.posY + this.lich.height / 2.0F);
		double tz = targetedEntity.posZ - sz;

		this.lich.playSound(SoundEvents.ENTITY_SNOWBALL_THROW, 1.0F, (this.lich.getRNG().nextFloat() - this.lich.getRNG().nextFloat()) * 0.2F + 0.4F);
		EntityTFThrownWep projectile = new EntityTFThrownWep(this.lich.world, this.lich).setItem(new ItemStack(Items.GOLDEN_SWORD));

		float speed = 0.8F;

		projectile.shoot(tx, ty, tz, speed, 1.0F);

		projectile.setLocationAndAngles(sx, sy, sz, this.lich.rotationYaw, this.lich.rotationPitch);

		this.lich.world.spawnEntity(projectile);
	}
}
