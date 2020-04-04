package twilightforest.entity.boss;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.DamagingProjectileEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;
import twilightforest.entity.projectile.ITFProjectile;

public class EntityTFUrGhastFireball extends FireballEntity implements ITFProjectile {

	public EntityTFUrGhastFireball(World world, EntityTFUrGhast entityTFTowerBoss, double x, double y, double z) {
		super(world, entityTFTowerBoss, x, y, z);
	}

	// [VanillaCopy] super, edits noted
	@Override
	protected void onImpact(RayTraceResult result) {
		// TF - don't collide with other fireballs
		if (result instanceof EntityRayTraceResult) {
			if (!this.world.isRemote && !(((EntityRayTraceResult)result).getEntity() instanceof DamagingProjectileEntity)) {
				if (((EntityRayTraceResult)result).getEntity() != null) {
					// TF - up damage by 10
					((EntityRayTraceResult)result).getEntity().attackEntityFrom(DamageSource.causeFireballDamage(this, this.shootingEntity), 16.0F);
					this.applyEnchantments(this.shootingEntity, ((EntityRayTraceResult)result).getEntity());
				}

				boolean flag = ForgeEventFactory.getMobGriefingEvent(this.world, this.shootingEntity);
				this.world.newExplosion(null, this.getX(), this.getY(), this.getZ(), (float) this.explosionPower, flag, flag);
				this.remove();
			}
		}
	}

	@Override
	public Entity getThrower() {
		return this.shootingEntity;
	}

	@Override
	public void setThrower(Entity entity) {
		if (entity instanceof LivingEntity) {
			this.shootingEntity = (LivingEntity) entity;
		}
	}
}
