package twilightforest.entity.projectile;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import twilightforest.entity.boss.Lich;
import twilightforest.util.TFDamageSources;

@OnlyIn(value = Dist.CLIENT, _interface = ItemSupplier.class)
public class LichBomb extends TFThrowable implements ItemSupplier {

	public LichBomb(EntityType<? extends LichBomb> type, Level world) {
		super(type, world);
	}

	public LichBomb(EntityType<? extends LichBomb> type, Level world, LivingEntity thrower) {
		super(type, world, thrower);
	}

	@Override
	public void tick() {
		super.tick();
		makeTrail();
	}

	private void makeTrail() {
		for (int i = 0; i < 1; i++) {
			double sx = 0.5 * (random.nextDouble() - random.nextDouble()) + this.getDeltaMovement().x();
			double sy = 0.5 * (random.nextDouble() - random.nextDouble()) + this.getDeltaMovement().y();
			double sz = 0.5 * (random.nextDouble() - random.nextDouble()) + this.getDeltaMovement().z();

			double dx = getX() + sx;
			double dy = getY() + sy;
			double dz = getZ() + sz;

			level.addParticle(ParticleTypes.FLAME, dx, dy, dz, sx * -0.25, sy * -0.25, sz * -0.25);
		}
	}

	@Override
	public boolean isOnFire() {
		return true;
	}

	@Override
	public boolean isPickable() {
		return true;
	}

	@Override
	public float getPickRadius() {
		return 1.0F;
	}

	@Override
	public boolean hurt(DamageSource source, float amount) {
		super.hurt(source, amount);

		if (source.getDirectEntity() != null) {
			if (!source.isExplosion())
				explode();
			return true;
		} else {
			return false;
		}
	}

	private void explode() {
		if (!this.level.isClientSide) {
			this.level.explode(this, TFDamageSources.LICH_BOMB, null, this.getX(), this.getY(), this.getZ(), 2F, false, Explosion.BlockInteraction.NONE);
			this.discard();
		}
	}

	@Override
	protected float getGravity() {
		return 0.001F;
	}

	@Override
	protected void onHit(HitResult result) {
		if (result instanceof EntityHitResult) {
			if (((EntityHitResult)result).getEntity() instanceof LichBolt
					|| ((EntityHitResult)result).getEntity() instanceof LichBomb
					|| ((EntityHitResult)result).getEntity() instanceof Lich) {
				return;
			}
		}

		explode();
	}

	@Override
	public ItemStack getItem() {
		return new ItemStack(Items.MAGMA_CREAM);
	}
}
