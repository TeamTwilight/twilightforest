package twilightforest.entity.projectile;

import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import twilightforest.entity.boss.Lich;
import twilightforest.util.TFDamageSources;

public class LichBolt extends TFThrowable {

	@SuppressWarnings("unused")
	public LichBolt(EntityType<? extends LichBolt> type, Level world) {
		super(type, world);
	}

	public LichBolt(EntityType<? extends LichBolt> type, Level world, LivingEntity owner) {
		super(type, world, owner);
	}

	@Override
	public void tick() {
		super.tick();
		makeTrail();
	}

	private void makeTrail() {
		for (int i = 0; i < 5; i++) {
			double dx = getX() + 0.5 * (random.nextDouble() - random.nextDouble());
			double dy = getY() + 0.5 * (random.nextDouble() - random.nextDouble());
			double dz = getZ() + 0.5 * (random.nextDouble() - random.nextDouble());

			double s1 = ((random.nextFloat() * 0.5F) + 0.5F) * 0.17F;
			double s2 = ((random.nextFloat() * 0.5F) + 0.5F) * 0.80F;
			double s3 = ((random.nextFloat() * 0.5F) + 0.5F) * 0.69F;

			level.addParticle(ParticleTypes.ENTITY_EFFECT, dx, dy, dz, s1, s2, s3);
		}
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
	public boolean hurt(DamageSource damagesource, float amount) {
		super.hurt(damagesource, amount);

		if (!this.level.isClientSide && damagesource.getEntity() != null) {
			Vec3 vec3d = damagesource.getEntity().getLookAngle();
			// reflect faster and more accurately
			this.shoot(vec3d.x, vec3d.y, vec3d.z, 1.5F, 0.1F);  // reflect faster and more accurately

			if (damagesource.getDirectEntity() instanceof LivingEntity)
				this.setOwner(damagesource.getDirectEntity());

			return true;
		}

		return false;
	}

	@Override
	protected float getGravity() {
		return 0.001F;
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void handleEntityEvent(byte id) {
		if (id == 3) {
			ItemStack itemId = new ItemStack(Items.ENDER_PEARL);
			for (int i = 0; i < 8; ++i) {
				this.level.addParticle(new ItemParticleOption(ParticleTypes.ITEM, itemId), this.getX(), this.getY(), this.getZ(), random.nextGaussian() * 0.05D, random.nextDouble() * 0.2D, random.nextGaussian() * 0.05D);
			}
		} else {
			super.handleEntityEvent(id);
		}
	}

	@Override
	protected void onHit(HitResult result) {
		if (result instanceof EntityHitResult hit) {
			Entity entityHit = hit.getEntity();
			if (entityHit instanceof LichBolt
					|| entityHit instanceof LichBomb
					|| (entityHit instanceof Lich && ((Lich) entityHit).isShadowClone())) {
				return;
			}

			if (!this.level.isClientSide) {
				if (entityHit instanceof LivingEntity) {
					entityHit.hurt(TFDamageSources.LICH_BOLT, 6);
				}
				this.level.broadcastEntityEvent(this, (byte) 3);
				this.discard();
			}
		}
	}
}
