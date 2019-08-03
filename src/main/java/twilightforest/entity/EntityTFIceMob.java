package twilightforest.entity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import twilightforest.TwilightForestMod;
import twilightforest.client.particle.TFParticleType;

public abstract class EntityTFIceMob extends EntityMob {
	public EntityTFIceMob(World worldIn) {
		super(worldIn);
	}

	@Override
	public void onLivingUpdate() {
		if (!this.onGround && this.motionY < 0.0D) {
			this.motionY *= 0.6D;
		}
		super.onLivingUpdate();
		if (this.world.isRemote) {
			// make snow particles
			for (int i = 0; i < 3; i++) {
				float px = (this.rand.nextFloat() - this.rand.nextFloat()) * 0.3F;
				float py = this.getEyeHeight() + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.5F;
				float pz = (this.rand.nextFloat() - this.rand.nextFloat()) * 0.3F;

				TwilightForestMod.proxy.spawnParticle(TFParticleType.SNOW_GUARDIAN, this.lastTickPosX + px, this.lastTickPosY + py, this.lastTickPosZ + pz, 0, 0, 0);
				if (this.world.getBiome(this.getPosition()).getTemperature(this.getPosition()) > 1.0F || this.isBurning()) {
					this.world.spawnParticle(EnumParticleTypes.CLOUD, this.lastTickPosX + px, this.lastTickPosY + py, this.lastTickPosZ + pz, 0, 0.1F, 0);
					this.world.spawnParticle(EnumParticleTypes.WATER_DROP, this.lastTickPosX + px, this.lastTickPosY + py, this.lastTickPosZ + pz, 0, 0, 0);
				}
			}
		}
		if (this.world.getBiome(this.getPosition()).getTemperature(this.getPosition()) > 1.0F && this.ticksExisted % 20 == 0) {
			//BURN!!!
			this.attackEntityFrom(DamageSource.ON_FIRE, 1.0F);
		}
	}

	@Override
	public void fall(float distance, float damageMultiplier) {
	}

	@Override
	protected void updateFallState(double y, boolean onGroundIn, IBlockState state, BlockPos pos) {
	}
}
