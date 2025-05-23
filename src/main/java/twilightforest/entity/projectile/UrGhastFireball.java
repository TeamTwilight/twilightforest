package twilightforest.entity.projectile;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.event.EventHooks;
import twilightforest.entity.boss.UrGhast;

public class UrGhastFireball extends LargeFireball implements ITFProjectile {

	private final int power;

	public UrGhastFireball(Level world, UrGhast entityTFTowerBoss, double x, double y, double z, int power) {
		super(world, entityTFTowerBoss, new Vec3(x, y, z), power);
		this.power = power;
	}

	//[VanillaCopy] of Projectile.onHit. We don't want fireballs to explode no matter what they hit, which is what LargeFireball.onHit does
	@Override
	protected void onHit(HitResult pResult) {
		HitResult.Type hitresult$type = pResult.getType();
		if (hitresult$type == HitResult.Type.ENTITY) {
			this.onHitEntity((EntityHitResult) pResult);
			this.level().gameEvent(GameEvent.PROJECTILE_LAND, pResult.getLocation(), GameEvent.Context.of(this, null));
		} else if (hitresult$type == HitResult.Type.BLOCK) {
			BlockHitResult blockhitresult = (BlockHitResult) pResult;
			this.onHitBlock(blockhitresult);
			BlockPos blockpos = blockhitresult.getBlockPos();
			this.level().gameEvent(GameEvent.PROJECTILE_LAND, blockpos, GameEvent.Context.of(this, this.level().getBlockState(blockpos)));
		}
	}

	@Override
	protected void onHitEntity(EntityHitResult result) {
		if (this.level() instanceof ServerLevel serverlevel && !(result.getEntity() instanceof AbstractHurtingProjectile)) {
			Entity entity1 = result.getEntity();
			Entity owner = this.getOwner();
			DamageSource source = this.damageSources().fireball(this, owner);
			// TF - up damage by 10
			entity1.hurtServer(serverlevel, source, 16.0F);
			EnchantmentHelper.doPostAttackEffects(serverlevel, entity1, source);

			boolean flag = EventHooks.canEntityGrief(serverlevel, this.getOwner());
			this.level().explode(null, this.getX(), this.getY(), this.getZ(), this.power, flag, Level.ExplosionInteraction.NONE);
			this.discard();
		}
	}

	@Override
	protected void onHitBlock(BlockHitResult result) {
		super.onHitBlock(result);
		if (this.level() instanceof ServerLevel level) {
			//explode and leave fire when hitting a block, but dont destroy them
			boolean flag = EventHooks.canEntityGrief(level, this.getOwner());
			this.level().explode(null, this.getX(), this.getY(), this.getZ(), (float) this.power, flag, Level.ExplosionInteraction.NONE);
			this.discard();
		}
	}

	@Override
	public void shoot(double x, double y, double z, float scale, float dist) {
		Vec3 vec3d = new Vec3(x, y, z).normalize().add(this.random.nextGaussian() * 0.0075F * dist, this.random.nextGaussian() * 0.0075F * dist, this.random.nextGaussian() * 0.0075F * dist).scale(scale);
		this.setDeltaMovement(vec3d);
		float f = Mth.sqrt((float) distanceToSqr(vec3d));
		this.setYRot((float) (Mth.atan2(vec3d.x(), z) * (180F / Mth.PI)));
		this.setXRot((float) (Mth.atan2(vec3d.y(), f) * (180F / Mth.PI)));
		this.yRotO = this.getYRot();
		this.xRotO = this.getXRot();
	}
}
