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
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import twilightforest.entity.boss.Lich;
import twilightforest.init.TFDamageTypes;
import twilightforest.init.TFEntities;

public class LichBolt extends TFThrowable {

	public LichBolt(EntityType<? extends LichBolt> type, Level level) {
		super(type, level);
	}

	public LichBolt(Level level, LivingEntity owner) {
		super(TFEntities.LICH_BOLT.get(), level, owner);
	}

	@Override
	public void tick() {
		super.tick();
		this.makeTrail();
	}

	private void makeTrail() {
		double s1 = ((this.random.nextFloat() * 0.5F) + 0.5F) * 0.17F;
		double s2 = ((this.random.nextFloat() * 0.5F) + 0.5F) * 0.80F;
		double s3 = ((this.random.nextFloat() * 0.5F) + 0.5F) * 0.69F;

		this.makeTrail(ParticleTypes.ENTITY_EFFECT, s1, s2, s3, 5);
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

		if (!this.level().isClientSide() && damagesource.getEntity() != null) {
			Vec3 vec3d = damagesource.getEntity().getLookAngle();
			// reflect faster and more accurately
			this.shoot(vec3d.x(), vec3d.y(), vec3d.z(), 1.5F, 0.1F);  // reflect faster and more accurately

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
				this.level().addParticle(new ItemParticleOption(ParticleTypes.ITEM, itemId), this.getX(), this.getY(), this.getZ(), random.nextGaussian() * 0.05D, random.nextDouble() * 0.2D, random.nextGaussian() * 0.05D);
			}
		} else {
			super.handleEntityEvent(id);
		}
	}

	@Override
	protected void onHitBlock(BlockHitResult result) {
		super.onHitBlock(result);
		this.level().broadcastEntityEvent(this, (byte) 3);
		this.discard();
	}

	@Override
	protected void onHitEntity(EntityHitResult result) {
		Entity hit = result.getEntity();
		if (hit instanceof LichBolt || hit instanceof LichBomb || (hit instanceof Lich lich && lich.isShadowClone())) {
			return;
		}

		if (!this.level().isClientSide()) {
			if (hit instanceof LivingEntity) {
				hit.hurt(TFDamageTypes.getDamageSource(this.level(), TFDamageTypes.LICH_BOLT, TFEntities.LICH.get()), 6);
			}
			this.level().broadcastEntityEvent(this, (byte) 3);
			this.discard();
		}
	}
}
