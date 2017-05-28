package twilightforest.entity;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.World;
import twilightforest.block.BlockTFMoonworm;
import twilightforest.block.TFBlockProperties;
import twilightforest.block.TFBlocks;
import twilightforest.item.TFItems;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityTFMoonwormShot extends EntityThrowable {
    public EntityTFMoonwormShot(World par1World) {
        super(par1World);
    }

	public EntityTFMoonwormShot(World world, EntityLivingBase thrower) {
		super(world, thrower);
		setHeadingFromThrower(thrower, thrower.rotationPitch, thrower.rotationYaw, 0F, 0.5F, 1.0F);
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
        makeTrail();
	}

	@Override
    public float getBrightness(float par1)
    {
        return 1.0F;
    }

	@Override
    @SideOnly(Side.CLIENT)
    public int getBrightnessForRender(float par1)
    {
        return 15728880;
    }

	
	private void makeTrail() {
//		for (int i = 0; i < 5; i++) {
//			double dx = posX + 0.5 * (rand.nextDouble() - rand.nextDouble()); 
//			double dy = posY + 0.5 * (rand.nextDouble() - rand.nextDouble()); 
//			double dz = posZ + 0.5 * (rand.nextDouble() - rand.nextDouble()); 
//			
//			double s1 = ((rand.nextFloat() * 0.5F) + 0.5F) * 0.17F;
//			double s2 = ((rand.nextFloat() * 0.5F) + 0.5F) * 0.80F;
//			double s3 = ((rand.nextFloat() * 0.5F) + 0.5F) * 0.69F;
//
//			world.spawnParticle("mobSpell", dx, dy, dz, s1, s2, s3);
//		}
	}

    @Override
	public boolean canBeCollidedWith()
    {
        return true;
    }

    @Override
	public float getCollisionBorderSize()
    {
        return 1.0F;
    }

	@Override
    protected float getGravityVelocity()
    {
        return 0.03F;
    }

	@Override
	protected void onImpact(RayTraceResult mop) {
        if (!world.isRemote) {
            if (mop.typeOfHit == Type.BLOCK)
            {
                IBlockState state = TFBlocks.moonworm.getDefaultState().withProperty(TFBlockProperties.FACING, mop.sideHit);
                world.setBlockState(mop.getBlockPos().offset(mop.sideHit), state);
                // todo sound
            }

            if (mop.entityHit != null)
            {
                mop.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), 0);
            }

            this.setDead();
        } else {
            for (int var3 = 0; var3 < 8; ++var3)
            {
                this.world.spawnParticle(EnumParticleTypes.BLOCK_CRACK, this.posX, this.posY, this.posZ, 0.0D, 0.0D, 0.0D, Block.getStateId(TFBlocks.moonworm.getDefaultState()));
            }
        }
	}
}
