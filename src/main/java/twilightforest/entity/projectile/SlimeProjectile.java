package twilightforest.entity.projectile;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class SlimeProjectile extends TFThrowable implements ItemSupplier {

	public SlimeProjectile(EntityType<? extends SlimeProjectile> type, Level world) {
		super(type, world);
	}

	public SlimeProjectile(EntityType<? extends SlimeProjectile> type, Level world, LivingEntity thrower) {
		super(type, world, thrower);
	}

	@Override
	public void tick() {
		super.tick();
		this.makeTrail(ParticleTypes.ITEM_SLIME, 2);
	}

	@Override
	protected double getDefaultGravity() {
		return 0.006F;
	}

	@Override
	public boolean hurtServer(ServerLevel level, DamageSource source, float amount) {
		this.die();
		return super.hurtServer(level, source, amount);
	}

	@Override
	public void handleEntityEvent(byte id) {
		if (id == EntityEvent.DEATH) {
			for (int i = 0; i < 8; ++i) {
				this.level().addParticle(ParticleTypes.ITEM_SLIME, this.getX(), this.getY(), this.getZ(), this.random.nextGaussian() * 0.05D, random.nextDouble() * 0.2D, random.nextGaussian() * 0.05D);
			}
		} else {
			super.handleEntityEvent(id);
		}
	}

	@Override
	protected void onHitEntity(EntityHitResult result) {
		super.onHitEntity(result);
		Entity target = result.getEntity();
		if (this.level() instanceof ServerLevel level && target instanceof LivingEntity)
			target.hurtServer(level, this.damageSources().thrown(this, this.getOwner()), 4);
	}

	@Override
	protected void onHit(HitResult result) {
		super.onHit(result);
		this.die();
	}

	private void die() {
		if (!this.level().isClientSide()) {
			this.playSound(SoundEvents.SLIME_SQUISH, 1.0F, 1.0F / (this.random.nextFloat() * 0.4F + 0.8F));
			this.discard();
			this.level().broadcastEntityEvent(this, (byte) 3);
		}
	}

	@Override
	public ItemStack getItem() {
		return new ItemStack(Items.SLIME_BALL);
	}
}
