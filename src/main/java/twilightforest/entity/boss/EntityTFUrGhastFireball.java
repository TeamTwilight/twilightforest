package twilightforest.entity.boss;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;
import twilightforest.entity.ITFProjectile;

public class EntityTFUrGhastFireball extends EntityLargeFireball implements ITFProjectile {

	public EntityTFUrGhastFireball(World world, EntityTFUrGhast entityTFTowerBoss, double x, double y, double z) {
		super(world, entityTFTowerBoss, x, y, z);
	}

	// [VanillaCopy] super, edits noted
	@Override
	protected void onImpact(RayTraceResult result) {
		// TF - don't collide with other fireballs
		if (!this.world.isRemote && !(result.entityHit instanceof EntityFireball)) {
			if (result.entityHit != null) {
				// TF - up damage by 10
				result.entityHit.attackEntityFrom(DamageSource.causeFireballDamage(this, this.shootingEntity), 16.0F);
				this.applyEnchantments(this.shootingEntity, result.entityHit);
			}

			boolean flag = ForgeEventFactory.getMobGriefingEvent(this.world, this.shootingEntity);
			this.world.newExplosion(null, this.posX, this.posY, this.posZ, (float) this.explosionPower, flag, flag);
			this.setDead();
		}
	}

	@Override
	public Entity getThrower() {
		return this.shootingEntity;
	}

	@Override
	public void setThrower(Entity entity) {
		if (entity instanceof EntityLivingBase) {
			this.shootingEntity = (EntityLivingBase) entity;
		}
	}
}
