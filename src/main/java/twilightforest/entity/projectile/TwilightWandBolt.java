package twilightforest.entity.projectile;

import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import twilightforest.init.TFDamageTypes;
import twilightforest.init.TFEntities;

public class TwilightWandBolt extends TFThrowable {

	public TwilightWandBolt(EntityType<? extends TwilightWandBolt> type, Level world) {
		super(type, world);
	}

	public TwilightWandBolt(Level world, LivingEntity thrower) {
		super(TFEntities.WAND_BOLT.get(), world, thrower);
		this.shootFromRotation(thrower, thrower.getXRot(), thrower.getYRot(), 0, 1.5F, 1.0F);
	}

	public TwilightWandBolt(Level worldIn, double x, double y, double z) {
		super(TFEntities.WAND_BOLT.get(), worldIn, x, y, z);
	}

	@Override
	public void tick() {
		super.tick();
		this.makeTrail();
	}

	private void makeTrail() {
		for (int i = 0; i < 5; i++) {
			double dx = this.getX() + 0.5D * (this.random.nextDouble() - this.random.nextDouble());
			double dy = this.getY() + 0.5D * (this.random.nextDouble() - this.random.nextDouble());
			double dz = this.getZ() + 0.5D * (this.random.nextDouble() - this.random.nextDouble());

			double s1 = ((this.random.nextFloat() * 0.5F) + 0.5F) * 0.17F;  // color
			double s2 = ((this.random.nextFloat() * 0.5F) + 0.5F) * 0.80F;  // color
			double s3 = ((this.random.nextFloat() * 0.5F) + 0.5F) * 0.69F;  // color

			this.level().addParticle(ParticleTypes.ENTITY_EFFECT, dx, dy, dz, s1, s2, s3);
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
				this.level().addParticle(particle, false, this.getX(), this.getY(), this.getZ(), this.random.nextGaussian() * 0.05D, random.nextDouble() * 0.2D, random.nextGaussian() * 0.05D);
			}
		} else {
			super.handleEntityEvent(id);
		}
	}

	@Override
	protected void onHitEntity(EntityHitResult result) {
		super.onHitEntity(result);
		if (!this.level().isClientSide()) {
			result.getEntity().hurt(TFDamageTypes.getIndirectEntityDamageSource(this.level(), TFDamageTypes.TWILIGHT_SCEPTER, this, this.getOwner()), 6);

			this.level().broadcastEntityEvent(this, (byte) 3);
			this.discard();
		}
	}

	@Override
	protected void onHitBlock(BlockHitResult result) {
		super.onHitBlock(result);
		if (!this.level().isClientSide()) {

			this.level().broadcastEntityEvent(this, (byte) 3);
			this.discard();
		}
	}

	@Override
	public boolean hurt(DamageSource source, float amount) {
		super.hurt(source, amount);

		if (!this.level().isClientSide() && source.getEntity() != null) {
			Vec3 vec3d = source.getEntity().getLookAngle();
			// reflect faster and more accurately
			this.shoot(vec3d.x(), vec3d.y(), vec3d.z(), 1.5F, 0.1F);

			if (source.getDirectEntity() instanceof LivingEntity) {
				this.setOwner(source.getDirectEntity());
			}
			return true;
		}

		return false;
	}
}
