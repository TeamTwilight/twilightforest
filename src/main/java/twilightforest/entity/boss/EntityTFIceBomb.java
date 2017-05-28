package twilightforest.entity.boss;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import twilightforest.TwilightForestMod;
import twilightforest.client.particle.TFParticleType;
import twilightforest.entity.EntityTFYeti;

public class EntityTFIceBomb extends EntityThrowable {
	
	private int zoneTimer = 80;
	private boolean hasHit;

	public EntityTFIceBomb(World par1World) {
		super(par1World);
	}

	public EntityTFIceBomb(World par1World, EntityLivingBase thrower) {
		super(par1World, thrower);
	}

	@Override
	protected void onImpact(RayTraceResult mop) {
		this.motionY = 0;
		this.hasHit = true;
		
		if (!world.isRemote) {
			if (this.getThrower() instanceof EntityTFYetiAlpha && getDistanceSqToEntity(this.getThrower()) <= 100) {
				this.setDead();
			}

			this.doTerrainEffects();
		}
	}
	
    private void doTerrainEffects() {
    	int range = 3;
		int ix = MathHelper.floor(this.lastTickPosX);
		int iy = MathHelper.floor(this.lastTickPosY);
		int iz = MathHelper.floor(this.lastTickPosZ);
		
		for (int x = -range; x <= range; x++) {
			for (int y = -range; y <= range; y++) {
				for (int z = -range; z <= range; z++) {
					this.doTerrainEffect(new BlockPos(ix + x, iy + y, iz + z));
				}
			}
		}
	}

    /**
     * Freeze water, put snow on snowable surfaces
     */
	private void doTerrainEffect(BlockPos pos) {
		IBlockState state = world.getBlockState(pos);
		if (state.getMaterial() == Material.WATER) {
			this.world.setBlockState(pos, Blocks.ICE.getDefaultState());
		}
		if (state.getMaterial() == Material.LAVA) {
			this.world.setBlockState(pos, Blocks.OBSIDIAN.getDefaultState());
		}
		if (this.world.isAirBlock(pos) && Blocks.SNOW_LAYER.canPlaceBlockAt(this.world, pos)) {
			this.world.setBlockState(pos, Blocks.SNOW_LAYER.getDefaultState());
		}
	}

    @Override
	public void onUpdate()
    {
    	super.onUpdate();
    	
        //this.func_145771_j(this.posX, (this.boundingBox.minY + this.boundingBox.maxY) / 2.0D, this.posZ); // push out of block

    	if (this.hasHit)
    	{
			this.motionX *= 0.1D;
			this.motionY *= 0.1D;
			this.motionZ *= 0.1D;

    		this.zoneTimer--;
    		makeIceZone();

    		if (!world.isRemote && this.zoneTimer <= 0) {
    			setDead();
    		}
    	} else {
    		makeTrail();
    	}
    }
    
	public void makeTrail() {
		for (int i = 0; i < 10; i++) {
			double dx = posX + 0.75F * (rand.nextFloat() - 0.5F); 
			double dy = posY + 0.75F * (rand.nextFloat() - 0.5F); 
			double dz = posZ + 0.75F * (rand.nextFloat() - 0.5F);

			world.spawnParticle(EnumParticleTypes.FALLING_DUST, dx, dy, dz, -motionX, -motionY, -motionZ, Block.getStateId(Blocks.SNOW.getDefaultState()));
		}
	}

	private void makeIceZone() {
		if (this.world.isRemote) {
			// sparkles
			for (int i = 0; i < 20; i++) {
				double dx = this.posX + (rand.nextFloat() - rand.nextFloat()) * 3.0F;
				double dy = this.posY + (rand.nextFloat() - rand.nextFloat()) * 3.0F;
				double dz = this.posZ + (rand.nextFloat() - rand.nextFloat()) * 3.0F;
				
				world.spawnParticle(EnumParticleTypes.FALLING_DUST, dx, dy, dz, 0, 0, 0, Block.getStateId(Blocks.SNOW.getDefaultState()));
			}
		} else {
			if (this.zoneTimer % 10 == 0) {
	    		hitNearbyEntities();
			}
		}
	}

	private void hitNearbyEntities() {
		List<EntityLivingBase> nearby = this.world.getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox().expand(3, 2, 3));
		
		for (EntityLivingBase entity : nearby) {
			if (entity != this.getThrower()) {
				if (entity instanceof EntityTFYeti) {
					// TODO: make "frozen yeti" entity?
					BlockPos pos = new BlockPos(entity.lastTickPosX, entity.lastTickPosY, entity.lastTickPosZ);
					world.setBlockState(pos, Blocks.ICE.getDefaultState());
					world.setBlockState(pos.up(), Blocks.ICE.getDefaultState());

					entity.setDead();
				} else {
					entity.attackEntityFrom(DamageSource.magic, 1);
					entity.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 20 * 5, 2, false, true));
				}
			}
		}
	}

	public IBlockState getBlock() {
		return Blocks.PACKED_ICE.getDefaultState();
	}
	
    @Override
	protected float getGravityVelocity()
    {
        return this.hasHit ? 0F : 0.025F;
    }
}
