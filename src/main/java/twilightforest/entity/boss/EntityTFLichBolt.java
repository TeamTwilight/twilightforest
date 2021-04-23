package twilightforest.entity.boss;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.IRendersAsItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particles.ItemParticleData;
import net.minecraft.util.DamageSource;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import twilightforest.entity.projectile.EntityTFThrowable;
import twilightforest.util.TFDamageSources;

@OnlyIn(value = Dist.CLIENT, _interface = IRendersAsItem.class)
public class EntityTFLichBolt extends EntityTFThrowable implements IRendersAsItem {

	@SuppressWarnings("unused")
	public EntityTFLichBolt(EntityType<? extends EntityTFLichBolt> type, World world) {
		super(type, world);
	}

	public EntityTFLichBolt(EntityType<? extends EntityTFLichBolt> type, World world, LivingEntity owner) {
		super(type, world, owner);
	}

	@Override
	public void tick() {
		super.tick();
		makeTrail();
	}

	private void makeTrail() {
		for (int i = 0; i < 5; i++) {
			double dx = getPosX() + 0.5 * (rand.nextDouble() - rand.nextDouble());
			double dy = getPosY() + 0.5 * (rand.nextDouble() - rand.nextDouble());
			double dz = getPosZ() + 0.5 * (rand.nextDouble() - rand.nextDouble());

			double s1 = ((rand.nextFloat() * 0.5F) + 0.5F) * 0.17F;
			double s2 = ((rand.nextFloat() * 0.5F) + 0.5F) * 0.80F;
			double s3 = ((rand.nextFloat() * 0.5F) + 0.5F) * 0.69F;

			world.addParticle(ParticleTypes.ENTITY_EFFECT, dx, dy, dz, s1, s2, s3);
		}
	}

	@Override
	public boolean canBeCollidedWith() {
		return true;
	}

	@Override
	public float getCollisionBorderSize() {
		return 1.0F;
	}

	@Override
	public boolean attackEntityFrom(DamageSource damagesource, float amount) {
		super.attackEntityFrom(damagesource, amount);

		if (!this.world.isRemote && damagesource.getTrueSource() != null) {
			Vector3d vec3d = damagesource.getTrueSource().getLookVec();
			// reflect faster and more accurately
			this.shoot(vec3d.x, vec3d.y, vec3d.z, 1.5F, 0.1F);  // reflect faster and more accurately

			if (damagesource.getImmediateSource() instanceof LivingEntity)
				this.setShooter(damagesource.getImmediateSource());

			return true;
		}

		return false;
	}

	@Override
	protected float getGravityVelocity() {
		return 0.001F;
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void handleStatusUpdate(byte id) {
		if (id == 3) {
			ItemStack itemId = new ItemStack(Items.ENDER_PEARL);
			for (int i = 0; i < 8; ++i) {
				this.world.addParticle(new ItemParticleData(ParticleTypes.ITEM, itemId), this.getPosX(), this.getPosY(), this.getPosZ(), rand.nextGaussian() * 0.05D, rand.nextDouble() * 0.2D, rand.nextGaussian() * 0.05D);
			}
		} else {
			super.handleStatusUpdate(id);
		}
	}

	@Override
	protected void onImpact(RayTraceResult result) {
		if (result instanceof EntityRayTraceResult) {
			Entity entityHit = ((EntityRayTraceResult)result).getEntity();
			if (entityHit instanceof EntityTFLichBolt
					|| entityHit instanceof EntityTFLichBomb
					|| (entityHit instanceof EntityTFLich && ((EntityTFLich) entityHit).isShadowClone())) {
				return;
			}

			if (!this.world.isRemote) {
				if (entityHit instanceof LivingEntity) {
					entityHit.attackEntityFrom(TFDamageSources.LICH_BOLT, 6);
				}
				this.world.setEntityState(this, (byte) 3);
				this.remove();
			}
		}
	}

	@Override
	public ItemStack getItem() {
		return new ItemStack(Items.ENDER_PEARL);
	}
}
