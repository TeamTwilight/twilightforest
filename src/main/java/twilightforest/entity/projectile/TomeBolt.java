package twilightforest.entity.projectile;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.Difficulty;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import twilightforest.util.TFDamageSources;

@OnlyIn(value = Dist.CLIENT, _interface = ItemSupplier.class)
public class TomeBolt extends TFThrowable implements ItemSupplier {

	public TomeBolt(EntityType<? extends TomeBolt> type, Level world, LivingEntity thrower) {
		super(type, world, thrower);
	}

	public TomeBolt(EntityType<? extends TomeBolt> type, Level world) {
		super(type, world);
	}

	@Override
	public void tick() {
		super.tick();
		makeTrail();
	}

	@Override
	protected float getGravity() {
		return 0.003F;
	}

	private void makeTrail() {
		for (int i = 0; i < 5; i++) {
			double dx = getX() + 0.5 * (random.nextDouble() - random.nextDouble());
			double dy = getY() + 0.5 * (random.nextDouble() - random.nextDouble());
			double dz = getZ() + 0.5 * (random.nextDouble() - random.nextDouble());
			level.addParticle(ParticleTypes.CRIT, dx, dy, dz, 0.0D, 0.0D, 0.0D);
		}
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void handleEntityEvent(byte id) {
		if (id == 3) {
			ParticleOptions particle = new ItemParticleOption(ParticleTypes.ITEM, new ItemStack(Items.PAPER));
			for (int i = 0; i < 8; ++i) {
				this.level.addParticle(particle, false, this.getX(), this.getY(), this.getZ(), random.nextGaussian() * 0.05D, random.nextDouble() * 0.2D, random.nextGaussian() * 0.05D);
			}
		} else {
			super.handleEntityEvent(id);
		}
	}

	@Override
	protected void onHit(HitResult result) {
		if (!this.level.isClientSide) {
			if (result instanceof EntityHitResult) {
				EntityHitResult entityRay = ((EntityHitResult) result);
				if (entityRay.getEntity() instanceof LivingEntity) {
					if (entityRay.getEntity().hurt(random.nextBoolean() ? TFDamageSources.lostWords(this, (LivingEntity)this.getOwner()) : TFDamageSources.schooled(this, (LivingEntity)this.getOwner()), 3)) {
						// inflict move slowdown
						int duration = level.getDifficulty() == Difficulty.PEACEFUL ? 2 : level.getDifficulty() == Difficulty.NORMAL ? 6 : 8;
						((LivingEntity) entityRay.getEntity()).addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, duration * 20, 1));
					}
				}
			}

			this.level.broadcastEntityEvent(this, (byte) 3);
			this.discard();
		}
	}

	@Override
	public ItemStack getItem() {
		return new ItemStack(Items.PAPER);
	}
}
