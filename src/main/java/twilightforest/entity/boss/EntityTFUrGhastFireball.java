package twilightforest.entity.boss;

import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import twilightforest.entity.ITFProjectile;

public class EntityTFUrGhastFireball extends EntityLargeFireball implements ITFProjectile {

	public EntityTFUrGhastFireball(World world, EntityTFUrGhast entityTFTowerBoss, double x, double y, double z) {
		super(world, entityTFTowerBoss, x, y, z);
	}

	// [VanillaCopy] super, edits noted
	@Override
	protected void onImpact(RayTraceResult result) {
		if (!this.world.isRemote && !(result.entityHit instanceof EntityFireball)) // TF - don't collide with other fireballs
		{
			if (result.entityHit != null) {
				result.entityHit.attackEntityFrom(DamageSource.causeFireballDamage(this, this.shootingEntity), 16.0F); // TF - up damage by 10
				this.applyEnchantments(this.shootingEntity, result.entityHit);
			}

			boolean flag = this.world.getGameRules().getBoolean("mobGriefing");
			this.world.newExplosion(null, this.posX, this.posY, this.posZ, (float) this.explosionPower, flag, flag);
			this.setDead();
		}
	}
}
