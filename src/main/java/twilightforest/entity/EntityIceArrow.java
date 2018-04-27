package twilightforest.entity;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import twilightforest.potions.TFPotions;

public class EntityIceArrow extends EntityArrow {

	public EntityIceArrow(World par1World) {
		super(par1World);
	}

	public EntityIceArrow(World world, EntityPlayer player) {
		super(world, player);
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		if (world.isRemote && !inGround) {
			for (int k = 0; k < 4; ++k) {
				this.world.spawnParticle(EnumParticleTypes.FALLING_DUST, this.posX + this.motionX * (double) k / 4.0D, this.posY + this.motionY * (double) k / 4.0D, this.posZ + this.motionZ * (double) k / 4.0D, -this.motionX, -this.motionY + 0.2D, -this.motionZ, Block.getStateId(Blocks.SNOW.getDefaultState()));
			}
		}
	}

	@Override
	protected void onHit(RayTraceResult rtr) {
		super.onHit(rtr);
		if (!world.isRemote && rtr.entityHit instanceof EntityLivingBase) {
			int chillLevel = 2;
			((EntityLivingBase) rtr.entityHit).addPotionEffect(new PotionEffect(TFPotions.frosty, 20 * 10, chillLevel));
		}
	}

	@Override
	protected ItemStack getArrowStack() {
		return new ItemStack(Items.ARROW);
	}

}
