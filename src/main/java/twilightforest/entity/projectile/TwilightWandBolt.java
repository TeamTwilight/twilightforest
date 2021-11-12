package twilightforest.entity.projectile;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import twilightforest.TwilightForestMod;
import twilightforest.entity.TFEntities;

import javax.annotation.Nullable;

public class TwilightWandBolt extends TFThrowable {

	public TwilightWandBolt(EntityType<? extends TwilightWandBolt> type, Level world) {
		super(type, world);
	}

	public TwilightWandBolt(Level world, LivingEntity thrower) {
		super(TFEntities.WAND_BOLT, world, thrower);
		shootFromRotation(thrower, thrower.getXRot(), thrower.getYRot(), 0, 1.5F, 1.0F);
	}

	public TwilightWandBolt(Level worldIn, double x, double y, double z) {
		super(TFEntities.WAND_BOLT, worldIn, x, y, z);
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

			double s1 = ((random.nextFloat() * 0.5F) + 0.5F) * 0.17F;  // color
			double s2 = ((random.nextFloat() * 0.5F) + 0.5F) * 0.80F;  // color
			double s3 = ((random.nextFloat() * 0.5F) + 0.5F) * 0.69F;  // color

			level.addParticle(ParticleTypes.ENTITY_EFFECT, dx, dy, dz, s1, s2, s3);
		}
	}

	@Override
	protected float getGravity() {
		return 0.003F;
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void handleEntityEvent(byte id) {
		if (id == 3) {
			ParticleOptions particle = new ItemParticleOption(ParticleTypes.ITEM, new ItemStack(Items.ENDER_PEARL));
			for (int i = 0; i < 8; i++) {
				this.level.addParticle(particle, false, this.getX(), this.getY(), this.getZ(), random.nextGaussian() * 0.05D, random.nextDouble() * 0.2D, random.nextGaussian() * 0.05D);
			}
		} else {
			super.handleEntityEvent(id);
		}
	}

	@Override
	protected void onHit(HitResult result) {
		if (!this.level.isClientSide) {
			if (result instanceof EntityHitResult hit) {
				if (hit.getEntity() instanceof LivingEntity entity) {
					entity.hurt(DamageSource.indirectMagic(this, this.getOwner()), 6);
				}
			}

			this.level.broadcastEntityEvent(this, (byte) 3);
			this.discard();
		}
	}

	@Override
	public boolean hurt(DamageSource source, float amount) {
		super.hurt(source, amount);

		if (!this.level.isClientSide && source.getEntity() != null) {
			Vec3 vec3d = source.getEntity().getLookAngle();
			// reflect faster and more accurately
			this.shoot(vec3d.x, vec3d.y, vec3d.z, 1.5F, 0.1F);

			if (source.getDirectEntity() instanceof LivingEntity) {
				this.setOwner(source.getDirectEntity());
			}
			return true;
		}

		return false;
	}
}
