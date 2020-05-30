package twilightforest.entity.projectile;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.IRendersAsItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.Item;
import net.minecraft.network.IPacket;
import net.minecraft.particles.ItemParticleData;
import net.minecraft.util.DamageSource;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;

public class EntityTFTwilightWandBolt extends EntityTFThrowable implements IRendersAsItem {

	@SuppressWarnings("unused")
	public EntityTFTwilightWandBolt(EntityType<? extends EntityTFTwilightWandBolt> type, World world) {
		super(type, world);
	}

	@SuppressWarnings("unused")
	public EntityTFTwilightWandBolt(EntityType<? extends EntityTFTwilightWandBolt> type, World world, double x, double y, double z) {
		super(type, world, x, y, z);
	}

	public EntityTFTwilightWandBolt(EntityType<? extends EntityTFTwilightWandBolt> type, World world, LivingEntity thrower) {
		super(type, world, thrower);
		shoot(thrower, thrower.rotationPitch, thrower.rotationYaw, 0, 1.5F, 1.0F);
	}

	@Override
	public void tick() {
		super.tick();
		makeTrail();
	}

	private void makeTrail() {
		for (int i = 0; i < 5; i++) {
			double dx = getX() + 0.5 * (rand.nextDouble() - rand.nextDouble());
			double dy = getY() + 0.5 * (rand.nextDouble() - rand.nextDouble());
			double dz = getZ() + 0.5 * (rand.nextDouble() - rand.nextDouble());

			double s1 = ((rand.nextFloat() * 0.5F) + 0.5F) * 0.17F;  // color
			double s2 = ((rand.nextFloat() * 0.5F) + 0.5F) * 0.80F;  // color
			double s3 = ((rand.nextFloat() * 0.5F) + 0.5F) * 0.69F;  // color

			world.addParticle(ParticleTypes.ENTITY_EFFECT, dx, dy, dz, s1, s2, s3);
		}
	}

	@Override
	protected float getGravityVelocity() {
		return 0.003F;
	}

	@OnlyIn(Dist.CLIENT)
	private final static ItemStack particleItem = new ItemStack(Items.ENDER_PEARL);

	@OnlyIn(Dist.CLIENT)
	@Override
	public void handleStatusUpdate(byte id) {
		if (id == 3) {
			for (int i = 0; i < 8; ++i) {
				this.world.addParticle(new ItemParticleData(ParticleTypes.ITEM, particleItem), false, this.getX(), this.getY(), this.getZ(), rand.nextGaussian() * 0.05D, rand.nextDouble() * 0.2D, rand.nextGaussian() * 0.05D);
			}
		} else {
			super.handleStatusUpdate(id);
		}
	}

	@Override
	protected void onImpact(RayTraceResult result) {
		if (!this.world.isRemote) {
			if (result instanceof EntityRayTraceResult) {
				if (((EntityRayTraceResult)result).getEntity() instanceof LivingEntity) {
					((EntityRayTraceResult)result).getEntity().attackEntityFrom(DamageSource.causeIndirectMagicDamage(this, this.getThrower()), 6);
				}
			}

			this.world.setEntityState(this, (byte) 3);
			this.remove();
		}
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		super.attackEntityFrom(source, amount);

		if (!this.world.isRemote && source.getTrueSource() != null) {
			Vec3d vec3d = source.getTrueSource().getLookVec();
			// reflect faster and more accurately
			this.shoot(vec3d.x, vec3d.y, vec3d.z, 1.5F, 0.1F);  // reflect faster and more accurately

			if (source.getImmediateSource() instanceof LivingEntity) {
				this.owner = (LivingEntity) source.getImmediateSource();
			}
			return true;
		}

		return false;
	}

	@Override
	public ItemStack getItem() {
		return new ItemStack(Items.ENDER_PEARL);
	}

	@Override
	public IPacket<?> createSpawnPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}
}
