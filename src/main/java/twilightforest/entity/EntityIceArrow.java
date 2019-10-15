package twilightforest.entity;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import twilightforest.potions.TFPotions;

public class EntityIceArrow extends EntityTFArrow {

	public EntityIceArrow(World world) {
		super(world);
	}

	public EntityIceArrow(World world, EntityLivingBase shooter) {
		super(world, shooter);
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		if (world.isRemote && !inGround) {
			int stateId = Block.getStateId(Blocks.SNOW.getDefaultState());
			for (int i = 0; i < 4; ++i) {
				this.world.spawnParticle(EnumParticleTypes.FALLING_DUST, this.posX + this.motionX * (double) i / 4.0D, this.posY + this.motionY * (double) i / 4.0D, this.posZ + this.motionZ * (double) i / 4.0D, -this.motionX, -this.motionY + 0.2D, -this.motionZ, stateId);
			}
		}
	}

	@Override
	protected void onHit(RayTraceResult ray) {
		super.onHit(ray);
		if (!world.isRemote && ray.entityHit instanceof EntityLivingBase) {
			int chillLevel = 2;
			((EntityLivingBase) ray.entityHit).addPotionEffect(new PotionEffect(TFPotions.frosty, 20 * 10, chillLevel));
		}
	}
}
