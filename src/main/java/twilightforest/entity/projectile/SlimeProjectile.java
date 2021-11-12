package twilightforest.entity.projectile;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(value = Dist.CLIENT, _interface = ItemSupplier.class)
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
		makeTrail();
	}

	@Override
	protected float getGravity() {
		return 0.006F;
	}

	private void makeTrail() {
		for (int i = 0; i < 2; i++) {
			double dx = getX() + 0.5 * (random.nextDouble() - random.nextDouble());
			double dy = getY() + 0.5 * (random.nextDouble() - random.nextDouble());
			double dz = getZ() + 0.5 * (random.nextDouble() - random.nextDouble());
			level.addParticle(ParticleTypes.ITEM_SLIME, dx, dy, dz, 0.0D, 0.0D, 0.0D);
		}
	}

	@Override
	public boolean hurt(DamageSource source, float amount) {
		super.hurt(source, amount);
		die();
		return true;
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void handleEntityEvent(byte id) {
		if (id == 3) {
			for (int i = 0; i < 8; ++i) {
				this.level.addParticle(ParticleTypes.ITEM_SLIME, this.getX(), this.getY(), this.getZ(), random.nextGaussian() * 0.05D, random.nextDouble() * 0.2D, random.nextGaussian() * 0.05D);
			}
		} else {
			super.handleEntityEvent(id);
		}
	}

	@Override
	protected void onHit(HitResult result) {
		// only damage living things
		if (result instanceof EntityHitResult) {
			Entity target = ((EntityHitResult)result).getEntity();
			if (!level.isClientSide && target instanceof LivingEntity) {
				target.hurt(DamageSource.thrown(this, this.getOwner()), 4);
				//damage armor pieces
				if(target instanceof Player) {
					for(ItemStack stack : target.getArmorSlots())
						stack.hurtAndBreak(random.nextInt(1), ((Player)target), (user) -> user.broadcastBreakEvent(stack.getEquipmentSlot()));
				}
			}
		}

		die();
	}

	private void die() {
		if (!this.level.isClientSide) {
			this.playSound(SoundEvents.SLIME_SQUISH, 1.0F, 1.0F / (random.nextFloat() * 0.4F + 0.8F));
			this.discard();
			this.level.broadcastEntityEvent(this, (byte) 3);
		}
	}

	@Override
	public ItemStack getItem() {
		return new ItemStack(Items.SLIME_BALL);
	}
}
