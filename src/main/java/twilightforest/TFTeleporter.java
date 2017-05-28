package twilightforest;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockPattern;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Teleporter;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.Biome;
import twilightforest.biomes.TFBiomeBase;
import twilightforest.block.TFBlocks;
import twilightforest.world.TFWorld;

public class TFTeleporter extends Teleporter
{
	public TFTeleporter(WorldServer par1WorldServer) {
		super(par1WorldServer);
	}

	@Override
	public void placeInPortal(Entity par1Entity, float facing)
	{
		if (!this.placeInExistingPortal(par1Entity, facing))
		{
			// if we're in enforced progression mode, check the biomes for safety
			if (par1Entity.world.getGameRules().getBoolean(TwilightForestMod.ENFORCED_PROGRESSION_RULE)) {
				BlockPos pos = new BlockPos(par1Entity);
				if (!isSafeBiomeAt(pos, par1Entity)) {
					TwilightForestMod.LOGGER.debug("Portal destination looks unsafe, rerouting!");
					
					BlockPos safeCoords = findSafeCoords(200, pos, par1Entity);
					
					if (safeCoords != null) {
			            par1Entity.setLocationAndAngles(safeCoords.getX(), par1Entity.posY, safeCoords.getZ(), 90.0F, 0.0F);

						TwilightForestMod.LOGGER.debug("Safely rerouted!");
					} else {
						TwilightForestMod.LOGGER.debug("Did not find a safe spot at first try, trying again with longer range.");
						safeCoords = findSafeCoords(400, pos, par1Entity);
						if (safeCoords != null) {
				            par1Entity.setLocationAndAngles(safeCoords.getX(), par1Entity.posY, safeCoords.getZ(), 90.0F, 0.0F);

							TwilightForestMod.LOGGER.debug("Safely rerouted to long range portal.  Return trip not guaranteed.");
						} else {
							TwilightForestMod.LOGGER.debug("Did not find a safe spot.");
						}
					}
				}
				
			}
			
			
			this.makePortal(par1Entity);
			this.placeInExistingPortal(par1Entity, facing);
		}
	}
	
	private BlockPos findSafeCoords(int range, BlockPos pos, Entity par1Entity) {
		for (int i = 0; i < 25; i++) {
			BlockPos dPos = new BlockPos(
					pos.getX() + random.nextInt(range) - random.nextInt(range),
					100,
					pos.getZ() + random.nextInt(range) - random.nextInt(range)
			);

			if (isSafeBiomeAt(dPos, par1Entity)) {
				return dPos;
			}
		}
		return null;
	}

	private boolean isSafeBiomeAt(BlockPos pos, Entity par1Entity) {
		Biome biomeAt = worldServerInstance.getBiome(pos);

		if (biomeAt instanceof TFBiomeBase && par1Entity instanceof EntityPlayerMP) {
			TFBiomeBase tfBiome = (TFBiomeBase)biomeAt;
			EntityPlayerMP player = (EntityPlayerMP)par1Entity;
			
			return tfBiome.doesPlayerHaveRequiredAchievement(player);
		} else {
			return true;
		}
	}

	// [VanillaCopy] copy of super, edits noted
	@Override
	public boolean placeInExistingPortal(Entity entityIn, float rotationYaw)
	{
		int i = 200; // TF - scan radius up to 200, and also un-inline this variable back into below
		double d0 = -1.0D;
		int j = MathHelper.floor(entityIn.posX);
		int k = MathHelper.floor(entityIn.posZ);
		boolean flag = true;
		BlockPos blockpos = BlockPos.ORIGIN;
		long l = ChunkPos.asLong(j, k);

		if (this.destinationCoordinateCache.containsKey(l))
		{
			Teleporter.PortalPosition teleporter$portalposition = (Teleporter.PortalPosition)this.destinationCoordinateCache.get(l);
			d0 = 0.0D;
			blockpos = teleporter$portalposition;
			teleporter$portalposition.lastUpdateTime = this.worldServerInstance.getTotalWorldTime();
			flag = false;
		}
		else
		{
			BlockPos blockpos3 = new BlockPos(entityIn);

			for (int i1 = -i; i1 <= i; ++i1)
			{
				BlockPos blockpos2;

				for (int j1 = -i; j1 <= i; ++j1)
				{
					for (BlockPos blockpos1 = blockpos3.add(i1, this.worldServerInstance.getActualHeight() - 1 - blockpos3.getY(), j1); blockpos1.getY() >= 0; blockpos1 = blockpos2)
					{
						blockpos2 = blockpos1.down();

						// TF - use our portal block
						if (this.worldServerInstance.getBlockState(blockpos1).getBlock() == TFBlocks.portal)
						{
							for (blockpos2 = blockpos1.down(); this.worldServerInstance.getBlockState(blockpos2).getBlock() == TFBlocks.portal; blockpos2 = blockpos2.down())
							{
								blockpos1 = blockpos2;
							}

							double d1 = blockpos1.distanceSq(blockpos3);

							if (d0 < 0.0D || d1 < d0)
							{
								d0 = d1;
								blockpos = blockpos1;
							}
						}
					}
				}
			}
		}

		if (d0 >= 0.0D)
		{
			if (flag)
			{
				this.destinationCoordinateCache.put(l, new Teleporter.PortalPosition(blockpos, this.worldServerInstance.getTotalWorldTime()));
			}

			// TF - replace with our own placement logic
			double portalX = blockpos.getX() + 0.5D;
			double portalY = blockpos.getY() + 0.5D;
			double portalZ = blockpos.getZ() + 0.5D;
			if(isBlockPortal(worldServerInstance, blockpos.west()))
			{
				portalX -= 0.5D;
			}
			if(isBlockPortal(worldServerInstance, blockpos.east()))
			{
				portalX += 0.5D;
			}
			if(isBlockPortal(worldServerInstance, blockpos.north()))
			{
				portalZ -= 0.5D;
			}
			if(isBlockPortal(worldServerInstance, blockpos.south()))
			{
				portalZ += 0.5D;
			}
			int xOffset = 0;
			int zOffset = 0;
			while(xOffset == zOffset && xOffset == 0)
			{
				xOffset = random.nextInt(3) - random.nextInt(3);
				zOffset = random.nextInt(3) - random.nextInt(3);
			}

			entityIn.motionX = entityIn.motionY = entityIn.motionZ = 0.0D;

			if (entityIn instanceof EntityPlayerMP)
			{
				((EntityPlayerMP)entityIn).connection.setPlayerLocation(portalX + xOffset, portalY + 1, portalZ + zOffset, entityIn.rotationYaw, entityIn.rotationPitch);
			}
			else
			{
				entityIn.setLocationAndAngles(portalX + xOffset, portalY + 1, portalZ + zOffset, entityIn.rotationYaw, entityIn.rotationPitch);
			}

			return true;
		}
		else
		{
			return false;
		}
	}
	
	private boolean isBlockPortal(World world, BlockPos pos)
	{
		return world.getBlockState(pos).getBlock() == TFBlocks.portal;
	}

	@Override
	public boolean makePortal(Entity entity) {
		BlockPos spot = findPortalCoords(entity, true);
        
        if (spot != null)
        {
			TwilightForestMod.LOGGER.debug("Found ideal portal spot");
        	makePortalAt(worldServerInstance, spot);
        	return true;
        }
        else
        {
			TwilightForestMod.LOGGER.debug("Did not find ideal portal spot, shooting for okay one");
        	spot = findPortalCoords(entity, false);
            if (spot != null)
            {
				TwilightForestMod.LOGGER.debug("Found okay portal spot");
            	makePortalAt(worldServerInstance, spot);
            	return true;
            }
        }

        // well I don't think we can actally just return false and fail here
		TwilightForestMod.LOGGER.debug("Did not even find an okay portal spot, just making a random one");
    	
		// adjust the portal height based on what world we're traveling to
		double yFactor = worldServerInstance.provider.getDimension() == 0 ? 2 : 0.5;
		// modified copy of base Teleporter method:
        makePortalAt(worldServerInstance, new BlockPos(entity.posX, entity.posY * yFactor, entity.posZ));

        return false;
	}

	private BlockPos findPortalCoords(Entity entity, boolean ideal) {
		// adjust the portal height based on what world we're traveling to
		double yFactor = worldServerInstance.provider.getDimension() == 0 ? 2 : 0.5;
		// modified copy of base Teleporter method:
        int entityX = MathHelper.floor(entity.posX);
        int entityZ = MathHelper.floor(entity.posZ);

        double spotWeight = -1D;
        
        BlockPos spot = null;
        
        byte range = 16;
        for(int rx = entityX - range; rx <= entityX + range; rx++)
        {
        	double xWeight = (rx + 0.5D) - entity.posX;
        	for(int rz = entityZ - range; rz <= entityZ + range; rz++)
        	{
        		double zWeight = (rz + 0.5D) - entity.posZ;

        		for(int ry = 128 - 1; ry >= 0; ry--)
        		{
        			BlockPos pos = new BlockPos(rx, ry, rz);

        			if(!worldServerInstance.isAirBlock(pos))
        			{
        				continue;
        			}

        			while (pos.getY() > 0 && worldServerInstance.isAirBlock(pos.down())) pos = pos.down();

        			if (ideal ? isIdealPortal(pos) : isOkayPortal(pos))
        			{
        				double yWeight = (pos.getY() + 0.5D) - entity.posY * yFactor;
        				double rPosWeight = xWeight * xWeight + yWeight * yWeight + zWeight * zWeight;
        				
        				
        				if(spotWeight < 0.0D || rPosWeight < spotWeight)
        				{
        					spotWeight = rPosWeight;
        					spot = pos;
        				}
        			}
        		}
        	}
        }
        
		return spot;
	}

	private boolean isIdealPortal(BlockPos pos) {
		for(int potentialZ = 0; potentialZ < 4; potentialZ++)
		{
			for(int potentialX = 0; potentialX < 4; potentialX++)
			{
				for(int potentialY = -1; potentialY < 3; potentialY++)
				{
					BlockPos tPos = pos.add(potentialX - 1, potentialY, potentialZ - 1);
					if (potentialY == -1 && worldServerInstance.getBlockState(tPos).getMaterial() != Material.GRASS || potentialY >= 0 && !worldServerInstance.getBlockState(tPos).getMaterial().isReplaceable())
					{
						return false;
					}
				}

			}

		}
		return true;
	}

	private boolean isOkayPortal(BlockPos pos) {
		for(int potentialZ = 0; potentialZ < 4; potentialZ++)
		{
			for(int potentialX = 0; potentialX < 4; potentialX++)
			{
				for(int potentialY = -1; potentialY < 3; potentialY++)
				{
					BlockPos tPos = pos.add(potentialX - 1, potentialY, potentialZ - 1);
					if (potentialY == -1 && !worldServerInstance.getBlockState(tPos).getMaterial().isSolid() || potentialY >= 0 && !worldServerInstance.getBlockState(tPos).getMaterial().isReplaceable())
					{
						return false;
					}
				}
			}
		}
		return true;
	}

	private void makePortalAt(World world, BlockPos pos) {

		if(pos.getY() < 30)
		{
			pos = new BlockPos(pos.getX(), 30, pos.getZ());
		}

		if(pos.getY() > 128 - 10)
		{
			pos = new BlockPos(pos.getX(), 128 - 10, pos.getZ());
		}
		
		// sink the portal 1 into the ground
		pos = pos.down();

		
		// grass all around it
		world.setBlockState(pos.west().north(), Blocks.GRASS.getDefaultState());
		world.setBlockState(pos.north(), Blocks.GRASS.getDefaultState());
		world.setBlockState(pos.east().north(), Blocks.GRASS.getDefaultState());
		world.setBlockState(pos.east(2).north(), Blocks.GRASS.getDefaultState());

		world.setBlockState(pos.west(), Blocks.GRASS.getDefaultState());
		world.setBlockState(pos.east(2), Blocks.GRASS.getDefaultState());

		world.setBlockState(pos.west().south(), Blocks.GRASS.getDefaultState());
		world.setBlockState(pos.east(2).south(), Blocks.GRASS.getDefaultState());

		world.setBlockState(pos.west().south(2), Blocks.GRASS.getDefaultState());
		world.setBlockState(pos.south(2), Blocks.GRASS.getDefaultState());
		world.setBlockState(pos.east().south(2), Blocks.GRASS.getDefaultState());
		world.setBlockState(pos.east(2).south(2), Blocks.GRASS.getDefaultState());

		// dirt under it
		world.setBlockState(pos.down(), Blocks.DIRT.getDefaultState());
		world.setBlockState(pos.east().down(), Blocks.DIRT.getDefaultState());
		world.setBlockState(pos.south().down(), Blocks.DIRT.getDefaultState());
		world.setBlockState(pos.east().south().down(), Blocks.DIRT.getDefaultState());
		
		// portal in it
		world.setBlockState(pos, TFBlocks.portal.getDefaultState(), 2);
		world.setBlockState(pos.east(), TFBlocks.portal.getDefaultState(), 2);
		world.setBlockState(pos.south(), TFBlocks.portal.getDefaultState(), 2);
		world.setBlockState(pos.east().south(), TFBlocks.portal.getDefaultState(), 2);
		
		// meh, let's just make a bunch of air over it for 4 squares
		for (int dx = -1; dx <= 2; dx++) {
			for (int dz = -1; dz <= 2; dz++) {
				for (int dy = 1; dy <=5; dy++) {
					world.setBlockToAir(pos.add(dx, dy, dz));
				}
			}
		}

		// finally, "nature decorations"!
		world.setBlockState(pos.west().north().up(), randNatureBlock(world.rand), 2);
		world.setBlockState(pos.north().up(), randNatureBlock(world.rand), 2);
		world.setBlockState(pos.east().north().up(), randNatureBlock(world.rand), 2);
		world.setBlockState(pos.east(2).north().up(), randNatureBlock(world.rand), 2);

		world.setBlockState(pos.west().up(), randNatureBlock(world.rand), 2);
		world.setBlockState(pos.east(2).up(), randNatureBlock(world.rand), 2);

		world.setBlockState(pos.west().south().up(), randNatureBlock(world.rand), 2);
		world.setBlockState(pos.east(2).south().up(), randNatureBlock(world.rand), 2);

		world.setBlockState(pos.west().south(2).up(), randNatureBlock(world.rand), 2);
		world.setBlockState(pos.south(2).up(), randNatureBlock(world.rand), 2);
		world.setBlockState(pos.east().south(2).up(), randNatureBlock(world.rand), 2);
		world.setBlockState(pos.east(2).south(2).up(), randNatureBlock(world.rand), 2);
	}
	
	private IBlockState randNatureBlock(Random random) {
		Block[] block = {Blocks.BROWN_MUSHROOM, Blocks.RED_MUSHROOM, Blocks.TALLGRASS, Blocks.RED_FLOWER, Blocks.YELLOW_FLOWER};
		
		return block[random.nextInt(block.length)].getDefaultState();
	}

}
