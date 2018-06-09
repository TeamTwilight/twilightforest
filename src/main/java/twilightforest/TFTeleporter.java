package twilightforest;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Teleporter;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.Biome;
import twilightforest.biomes.TFBiomeBase;
import twilightforest.block.BlockTFPortal;
import twilightforest.block.TFBlocks;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class TFTeleporter extends Teleporter {
	public static TFTeleporter getTeleporterForDim(MinecraftServer server, int dim) {
		WorldServer ws = server.getWorld(dim);

		for (Teleporter t : ws.customTeleporters) {
			if (t instanceof TFTeleporter) {
				return (TFTeleporter) t;
			}
		}

		TFTeleporter tp = new TFTeleporter(ws);
		ws.customTeleporters.add(tp);
		return tp;
	}

	private TFTeleporter(WorldServer dest) {
		super(dest);
	}

	@Override
	public void placeInPortal(Entity par1Entity, float facing) {
		if (!this.placeInExistingPortal(par1Entity, facing)) {
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

	@Nullable
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
		Biome biomeAt = world.getBiome(pos);

		if (biomeAt instanceof TFBiomeBase && par1Entity instanceof EntityPlayerMP) {
			TFBiomeBase tfBiome = (TFBiomeBase) biomeAt;
			EntityPlayerMP player = (EntityPlayerMP) par1Entity;

			return tfBiome.doesPlayerHaveRequiredAchievement(player);
		} else {
			return true;
		}
	}

	// [VanillaCopy] copy of super, edits noted
	@Override
	public boolean placeInExistingPortal(Entity entityIn, float rotationYaw) {
		int i = 200; // TF - scan radius up to 200, and also un-inline this variable back into below
		double d0 = -1.0D;
		int j = MathHelper.floor(entityIn.posX);
		int k = MathHelper.floor(entityIn.posZ);
		boolean flag = true;
		BlockPos blockpos = BlockPos.ORIGIN;
		long l = ChunkPos.asLong(j, k);

		if (this.destinationCoordinateCache.containsKey(l)) {
			Teleporter.PortalPosition portalPosition = this.destinationCoordinateCache.get(l);
			d0 = 0.0D;
			blockpos = portalPosition;
			portalPosition.lastUpdateTime = this.world.getTotalWorldTime();
			flag = false;
		} else {
			BlockPos blockpos3 = new BlockPos(entityIn);

			for (int i1 = -i; i1 <= i; ++i1) {
				BlockPos blockpos2;

				for (int j1 = -i; j1 <= i; ++j1) {

					// TF - skip chunks that aren't generated
					ChunkPos chunkPos = new ChunkPos(blockpos3.add(i1, 0, j1));
					if (!this.world.isChunkGeneratedAt(chunkPos.x, chunkPos.z)) {
						continue;
					}

					for (BlockPos blockpos1 = blockpos3.add(i1, getScanHeight(blockpos3) - blockpos3.getY(), j1); blockpos1.getY() >= 0; blockpos1 = blockpos2) {
						blockpos2 = blockpos1.down();

						// TF - don't lookup state if inner condition would fail
						if (d0 >= 0.0D && blockpos1.distanceSq(blockpos3) >= d0) {
							continue;
						}

						// TF - use our portal block
						if (this.world.getBlockState(blockpos1).getBlock() == TFBlocks.portal) {
							for (blockpos2 = blockpos1.down(); this.world.getBlockState(blockpos2).getBlock() == TFBlocks.portal; blockpos2 = blockpos2.down()) {
								blockpos1 = blockpos2;
							}

							double d1 = blockpos1.distanceSq(blockpos3);

							if (d0 < 0.0D || d1 < d0) {
								d0 = d1;
								blockpos = blockpos1;
								// TF - restrict search radius to new distance
								i = MathHelper.ceil(MathHelper.sqrt(d1));
							}
						}
					}
				}
			}
		}

		if (d0 >= 0.0D) {
			if (flag) {
				this.destinationCoordinateCache.put(l, new Teleporter.PortalPosition(blockpos, this.world.getTotalWorldTime()));
			}

			// TF - replace with our own placement logic
			BlockPos[] portalBorder = getBoundaryPositions(blockpos).toArray(new BlockPos[0]);
			BlockPos borderPos = portalBorder[random.nextInt(portalBorder.length)];

			double portalX = borderPos.getX() + 0.5;
			double portalY = borderPos.getY() + 1.0;
			double portalZ = borderPos.getZ() + 0.5;

			entityIn.motionX = entityIn.motionY = entityIn.motionZ = 0.0D;

			if (entityIn instanceof EntityPlayerMP) {
				((EntityPlayerMP) entityIn).connection.setPlayerLocation(portalX, portalY, portalZ, entityIn.rotationYaw, entityIn.rotationPitch);
			} else {
				entityIn.setLocationAndAngles(portalX, portalY, portalZ, entityIn.rotationYaw, entityIn.rotationPitch);
			}

			return true;
		} else {
			return false;
		}
	}

	private int getScanHeight(BlockPos pos) {
		return getScanHeight(pos.getX(), pos.getZ());
	}

	private int getScanHeight(int x, int z) {
		int worldHeight = world.getActualHeight() - 1;
		int chunkHeight = world.getChunkFromChunkCoords(x >> 4, z >> 4).getTopFilledSegment() + 15;
		return Math.min(worldHeight, chunkHeight);
	}

	// from the start point, builds a set of all directly adjacent non-portal blocks
	private Set<BlockPos> getBoundaryPositions(BlockPos start) {
		Set<BlockPos> result = new HashSet<>(), checked = new HashSet<>();
		checked.add(start);
		checkAdjacent(start, checked, result);
		return result;
	}

	private void checkAdjacent(BlockPos pos, Set<BlockPos> checked, Set<BlockPos> result) {
		for (EnumFacing facing : EnumFacing.Plane.HORIZONTAL) {
			BlockPos offset = pos.offset(facing);
			if (isBlockPortal(world, offset)) {
				if (checked.add(offset)) {
					checkAdjacent(offset, checked, result);
				}
			} else {
				result.add(offset);
			}
		}
	}

	private boolean isBlockPortal(World world, BlockPos pos) {
		return world.getBlockState(pos).getBlock() == TFBlocks.portal;
	}

	@Override
	public boolean makePortal(Entity entity) {
		BlockPos spot = findPortalCoords(entity, true);
		String name = entity.getCustomNameTag();

		if (spot != null) {
			TwilightForestMod.LOGGER.debug("Found ideal portal spot for " + name);
			makePortalAt(world, spot);
			return true;
		} else {
			TwilightForestMod.LOGGER.debug("Did not find ideal portal spot, shooting for okay one for " + name);
			spot = findPortalCoords(entity, false);
			if (spot != null) {
				TwilightForestMod.LOGGER.debug("Found okay portal spot for " + name);
				makePortalAt(world, spot);
				return true;
			}
		}

		// well I don't think we can actually just return false and fail here
		TwilightForestMod.LOGGER.debug("Did not even find an okay portal spot, just making a random one for " + name);

		// adjust the portal height based on what world we're traveling to
		double yFactor = world.provider.getDimension() == 0 ? 2 : 0.5;
		// modified copy of base Teleporter method:
		makePortalAt(world, new BlockPos(entity.posX, entity.posY * yFactor, entity.posZ));

		return false;
	}

	@Nullable
	private BlockPos findPortalCoords(Entity entity, boolean ideal) {
		// adjust the portal height based on what world we're traveling to
		double yFactor = world.provider.getDimension() == 0 ? 2 : 0.5;
		// modified copy of base Teleporter method:
		int entityX = MathHelper.floor(entity.posX);
		int entityZ = MathHelper.floor(entity.posZ);

		double spotWeight = -1D;

		BlockPos spot = null;

		byte range = 16;
		for (int rx = entityX - range; rx <= entityX + range; rx++) {
			double xWeight = (rx + 0.5D) - entity.posX;
			for (int rz = entityZ - range; rz <= entityZ + range; rz++) {
				double zWeight = (rz + 0.5D) - entity.posZ;

				for (int ry = getScanHeight(rx, rz); ry >= 0; ry--) {
					BlockPos pos = new BlockPos(rx, ry, rz);

					if (!world.isAirBlock(pos)) {
						continue;
					}

					while (pos.getY() > 0 && world.isAirBlock(pos.down())) pos = pos.down();

					double yWeight = (pos.getY() + 0.5D) - entity.posY * yFactor;
					double rPosWeight = xWeight * xWeight + yWeight * yWeight + zWeight * zWeight;

					if (spotWeight < 0.0D || rPosWeight < spotWeight) {
						if (ideal ? isIdealPortal(pos) : isOkayPortal(pos)) {
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
		for (int potentialZ = 0; potentialZ < 4; potentialZ++) {
			for (int potentialX = 0; potentialX < 4; potentialX++) {
				for (int potentialY = -1; potentialY < 3; potentialY++) {
					BlockPos tPos = pos.add(potentialX - 1, potentialY, potentialZ - 1);
					Material material = world.getBlockState(tPos).getMaterial();
					if (potentialY == -1 && material != Material.GRASS
							|| potentialY >= 0 && !material.isReplaceable()) {
						return false;
					}
				}
			}
		}
		return true;
	}

	private boolean isOkayPortal(BlockPos pos) {
		for (int potentialZ = 0; potentialZ < 4; potentialZ++) {
			for (int potentialX = 0; potentialX < 4; potentialX++) {
				for (int potentialY = -1; potentialY < 3; potentialY++) {
					BlockPos tPos = pos.add(potentialX - 1, potentialY, potentialZ - 1);
                    Material material = world.getBlockState(tPos).getMaterial();
					if (potentialY == -1 && !material.isSolid() && !material.isLiquid()
							|| potentialY >= 0 && !material.isReplaceable()) {
						return false;
					}
				}
			}
		}
		return true;
	}

	private void makePortalAt(World world, BlockPos pos) {

		if (pos.getY() < 30) {
			pos = new BlockPos(pos.getX(), 30, pos.getZ());
		} else if (pos.getY() > 128 - 10) {
			pos = new BlockPos(pos.getX(), 128 - 10, pos.getZ());
		}

		// sink the portal 1 into the ground
		pos = pos.down();

		// grass all around it
		IBlockState grass = Blocks.GRASS.getDefaultState();

		world.setBlockState(pos.west().north(), grass);
		world.setBlockState(pos.north(), grass);
		world.setBlockState(pos.east().north(), grass);
		world.setBlockState(pos.east(2).north(), grass);

		world.setBlockState(pos.west(), grass);
		world.setBlockState(pos.east(2), grass);

		world.setBlockState(pos.west().south(), grass);
		world.setBlockState(pos.east(2).south(), grass);

		world.setBlockState(pos.west().south(2), grass);
		world.setBlockState(pos.south(2), grass);
		world.setBlockState(pos.east().south(2), grass);
		world.setBlockState(pos.east(2).south(2), grass);

		// dirt under it
		IBlockState dirt = Blocks.DIRT.getDefaultState();

		world.setBlockState(pos.down(), dirt);
		world.setBlockState(pos.east().down(), dirt);
		world.setBlockState(pos.south().down(), dirt);
		world.setBlockState(pos.east().south().down(), dirt);

		// portal in it
		IBlockState portal = TFBlocks.portal.getDefaultState().withProperty(BlockTFPortal.DISALLOW_RETURN, !TFConfig.shouldReturnPortalBeUsable);

		world.setBlockState(pos, portal, 2);
		world.setBlockState(pos.east(), portal, 2);
		world.setBlockState(pos.south(), portal, 2);
		world.setBlockState(pos.east().south(), portal, 2);

		// meh, let's just make a bunch of air over it for 4 squares
		for (int dx = -1; dx <= 2; dx++) {
			for (int dz = -1; dz <= 2; dz++) {
				for (int dy = 1; dy <= 5; dy++) {
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
