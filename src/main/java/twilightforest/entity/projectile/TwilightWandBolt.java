package twilightforest.entity.projectile;

import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import twilightforest.init.TFDamageTypes;
import twilightforest.init.TFEntities;
import twilightforest.init.TFParticleType;
import twilightforest.init.TFSounds;

public class TwilightWandBolt extends TFThrowable {

	public TwilightWandBolt(EntityType<? extends TwilightWandBolt> type, Level world) {
		super(type, world);
	}

	public TwilightWandBolt(Level world, LivingEntity thrower) {
		super(TFEntities.WAND_BOLT.get(), world, thrower, new ItemStack(Items.ENDER_PEARL));
		this.shootFromRotation(thrower, thrower.getXRot(), thrower.getYRot(), 0, 1.5F, 1.0F);
	}

	public TwilightWandBolt(Level worldIn, double x, double y, double z) {
		super(TFEntities.WAND_BOLT.get(), worldIn, x, y, z, new ItemStack(Items.ENDER_PEARL));
	}

	@Override
	public void tick() {
		super.tick();
		this.makeTrail();
	}

	private void makeTrail() {
		for (int i = 0; i < 5; i++) {
			double dx = this.getX() + 0.25D * (this.random.nextDouble() - this.random.nextDouble());
			double dy = this.getY() + 0.25D * (this.random.nextDouble() - this.random.nextDouble());
			double dz = this.getZ() + 0.25D * (this.random.nextDouble() - this.random.nextDouble());

			float s1 = ((this.random.nextFloat() * 0.5F) + 0.5F) * 0.17F;  // color
			float s2 = ((this.random.nextFloat() * 0.5F) + 0.5F) * 0.80F;  // color
			float s3 = ((this.random.nextFloat() * 0.5F) + 0.5F) * 0.69F;  // color

			this.level().addParticle(ColorParticleOption.create(TFParticleType.MAGIC_EFFECT.get(), s1, s2, s3), dx, dy, dz, 0.0D, 0.0D, 0.0D);
		}
	}

	@Override
	protected double getDefaultGravity() {
		return 0.003F;
	}

	@Override
	protected boolean canHitEntity(Entity target) {
		return super.canHitEntity(target) && !(target instanceof LichBolt) && !(target instanceof LichBomb);
	}

	@Override
	public void handleEntityEvent(byte id) {
		if (id == EntityEvent.DEATH) {
			for (int i = 0; i < 8; i++) {
				this.level().addParticle(TFParticleType.TWILIGHT_ORB.get(), this.getX(), this.getY(), this.getZ(), this.random.nextGaussian() * 0.05D, random.nextDouble() * 0.2D, random.nextGaussian() * 0.05D);
			}
		} else {
			super.handleEntityEvent(id);
		}
	}

	@Override
	protected void onHitEntity(EntityHitResult result) {
		super.onHitEntity(result);
		if (!this.level().isClientSide()) {
			Entity hit = result.getEntity();
			if (hit instanceof LivingEntity) {
				hit.hurt(TFDamageTypes.getIndirectEntityDamageSource(this.level(), TFDamageTypes.TWILIGHT_SCEPTER, this, this.getOwner()), 6);
			}
			this.level().playSound(null, hit.blockPosition(), TFSounds.TWILIGHT_SCEPTER_HIT.get(), this.getOwner() != null ? this.getOwner().getSoundSource() : SoundSource.PLAYERS);
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
	public boolean hurtServer(ServerLevel server, DamageSource source, float amount) {
		super.hurtServer(server, source, amount);

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

	//required method
	@Override
	protected Item getDefaultItem() {
		return Items.ENDER_PEARL;
	}
}
