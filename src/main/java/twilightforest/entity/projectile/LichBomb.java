package twilightforest.entity.projectile;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import twilightforest.entity.boss.Lich;
import twilightforest.init.TFDamageTypes;
import twilightforest.init.TFEntities;

@OnlyIn(value = Dist.CLIENT, _interface = ItemSupplier.class)
public class LichBomb extends TFThrowable implements ItemSupplier {

	public LichBomb(EntityType<? extends LichBomb> type, Level world) {
		super(type, world);
	}

	public LichBomb(Level world, LivingEntity thrower) {
		super(TFEntities.LICH_BOMB.get(), world, thrower);
	}

	@Override
	public void tick() {
		super.tick();
		this.makeTrail();
	}

	private void makeTrail() {
		for (int i = 0; i < 1; i++) {
			double sx = 0.5 * (this.random.nextDouble() - this.random.nextDouble()) + this.getDeltaMovement().x();
			double sy = 0.5 * (this.random.nextDouble() - this.random.nextDouble()) + this.getDeltaMovement().y();
			double sz = 0.5 * (this.random.nextDouble() - this.random.nextDouble()) + this.getDeltaMovement().z();

			double dx = this.getX() + sx;
			double dy = this.getY() + sy;
			double dz = this.getZ() + sz;

			this.level().addParticle(ParticleTypes.FLAME, dx, dy, dz, sx * -0.25, sy * -0.25, sz * -0.25);
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
			if (!source.is(DamageTypeTags.IS_EXPLOSION))
				this.explode();
			return true;
		} else {
			return false;
		}
	}

	private void explode() {
		if (!this.level().isClientSide()) {
			this.level().explode(this, TFDamageTypes.getDamageSource(this.level(), TFDamageTypes.LICH_BOMB, TFEntities.LICH.get()), null, this.getX(), this.getY(), this.getZ(), 2.0F, false, Level.ExplosionInteraction.NONE);
			this.discard();
		}
	}

	@Override
	protected float getGravity() {
		return 0.001F;
	}

	@Override
	protected void onHitBlock(BlockHitResult result) {
		super.onHitBlock(result);
		this.explode();
	}

	@Override
	protected void onHitEntity(EntityHitResult result) {
		super.onHitEntity(result);
		if (result.getEntity() instanceof LichBolt || result.getEntity() instanceof LichBomb || result.getEntity() instanceof Lich) {
			return;
		}
		this.explode();
	}

	@Override
	public ItemStack getItem() {
		return new ItemStack(Items.MAGMA_CREAM);
	}
}
