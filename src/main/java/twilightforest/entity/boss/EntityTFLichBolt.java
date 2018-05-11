package twilightforest.entity.boss;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


public class EntityTFLichBolt extends EntityThrowable {
	@SuppressWarnings("unused")
	public EntityTFLichBolt(World par1World) {
		super(par1World);
	}

	public EntityTFLichBolt(World par1World, EntityLivingBase par2EntityLiving) {
		super(par1World, par2EntityLiving);
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		makeTrail();
	}

	private void makeTrail() {
		for (int i = 0; i < 5; i++) {
			double dx = posX + 0.5 * (rand.nextDouble() - rand.nextDouble());
			double dy = posY + 0.5 * (rand.nextDouble() - rand.nextDouble());
			double dz = posZ + 0.5 * (rand.nextDouble() - rand.nextDouble());

			double s1 = ((rand.nextFloat() * 0.5F) + 0.5F) * 0.17F;
			double s2 = ((rand.nextFloat() * 0.5F) + 0.5F) * 0.80F;
			double s3 = ((rand.nextFloat() * 0.5F) + 0.5F) * 0.69F;

			world.spawnParticle(EnumParticleTypes.SPELL_MOB, dx, dy, dz, s1, s2, s3);
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
			Vec3d vec3d = damagesource.getTrueSource().getLookVec();
			// reflect faster and more accurately
			this.setThrowableHeading(vec3d.x, vec3d.y, vec3d.z, 1.5F, 0.1F);  // reflect faster and more accurately

			if (damagesource.getImmediateSource() instanceof EntityLivingBase)
				this.thrower = (EntityLivingBase) damagesource.getImmediateSource();

			return true;
		}

		return false;
	}

	@Override
	protected float getGravityVelocity() {
		return 0.001F;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void handleStatusUpdate(byte id) {
		if (id == 3) {
			for (int i = 0; i < 8; ++i) {
				this.world.spawnParticle(EnumParticleTypes.ITEM_CRACK, this.posX, this.posY, this.posZ, rand.nextGaussian() * 0.05D, rand.nextDouble() * 0.2D, rand.nextGaussian() * 0.05D, Item.getIdFromItem(Items.ENDER_PEARL));
			}
		} else {
			super.handleStatusUpdate(id);
		}
	}

	@Override
	protected void onImpact(RayTraceResult result) {
		if (result.entityHit instanceof EntityTFLichBolt
				|| result.entityHit instanceof EntityTFLichBomb
				|| (result.entityHit instanceof EntityTFLich && ((EntityTFLich) result.entityHit).isShadowClone())) {
			return;
		}

		if (!this.world.isRemote) {
			if (result.entityHit instanceof EntityLivingBase) {
				result.entityHit.attackEntityFrom(DamageSource.causeIndirectMagicDamage(this, this.getThrower()), 6);
			}
			this.world.setEntityState(this, (byte) 3);
			this.setDead();
		}
	}
}
