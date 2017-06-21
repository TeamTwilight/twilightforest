package twilightforest.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBreakable;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.Teleporter;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import twilightforest.TFAchievementPage;
import twilightforest.TFConfig;
import twilightforest.TFTeleporter;
import twilightforest.TwilightForestMod;

import java.util.Arrays;
import java.util.Random;

public class BlockTFPortal extends BlockBreakable {
	private static final AxisAlignedBB AABB = new AxisAlignedBB(0.0F, 0.0F, 0.0F, 1.0F, 0.75F, 1.0F);

	public BlockTFPortal() {
		super(Material.PORTAL, false);
		this.setHardness(-1F);
		this.setSoundType(SoundType.GLASS);
		this.setLightLevel(0.75F);
	}

	@Override
	@Deprecated
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
		return AABB;
	}

	@Override
	@Deprecated
	public AxisAlignedBB getCollisionBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
		return NULL_AABB;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	/**
	 * The function name says it all.  Tries to create a portal at the specified location.
	 * In this case, the location is the location of a pool with very specific parameters.
	 */
	public boolean tryToCreatePortal(World world, BlockPos pos) {
		if (isGoodPortalPool(world, pos)) {
			world.addWeatherEffect(new EntityLightningBolt(world, pos.getX(), pos.getY(), pos.getZ(), false));

			transmuteWaterToPortal(world, pos);

			return true;
		} else {
			return false;
		}
	}

	/**
	 * Changes the pool it's been given to all portal.  No checks done, only does 4 squares.
	 */
	private void transmuteWaterToPortal(World world, BlockPos pos) {
		// adjust so that the other 3 water squares are in the +x, +z directions.
		if (world.getBlockState(pos.west()).getMaterial() == Material.WATER) {
			pos = pos.west();
		}
		if (world.getBlockState(pos.north()).getMaterial() == Material.WATER) {
			pos = pos.north();
		}

		world.setBlockState(pos, TFBlocks.portal.getDefaultState(), 2);
		world.setBlockState(pos.east(), TFBlocks.portal.getDefaultState(), 2);
		world.setBlockState(pos.east().south(), TFBlocks.portal.getDefaultState(), 2);
		world.setBlockState(pos.south(), TFBlocks.portal.getDefaultState(), 2);

		//System.out.println("Transmuting water to portal");
	}

	/**
	 * If this spot, or a spot in any one of the 8 directions around me is good, we're good.
	 */
	private boolean isGoodPortalPool(World world, BlockPos pos) {
		return isGoodPortalPoolStrict(world, pos)
				|| isGoodPortalPoolStrict(world, pos.north())
				|| isGoodPortalPoolStrict(world, pos.south())
				|| isGoodPortalPoolStrict(world, pos.west())
				|| isGoodPortalPoolStrict(world, pos.east())
				|| isGoodPortalPoolStrict(world, pos.west().north())
				|| isGoodPortalPoolStrict(world, pos.west().south())
				|| isGoodPortalPoolStrict(world, pos.east().north())
				|| isGoodPortalPoolStrict(world, pos.east().south());
	}

	/**
	 * Returns true only if there is water here, and at dx + 1, dy + 1, grass surrounding it, and solid beneath.
	 * <p>
	 * <p>
	 * GGGG
	 * G+wG
	 * GwwG
	 * GGGG
	 */
	public boolean isGoodPortalPoolStrict(World world, BlockPos pos) {
		// 4 squares of water
		return world.getBlockState(pos).getMaterial() == Material.WATER
				&& world.getBlockState(pos.east()).getMaterial() == Material.WATER
				&& world.getBlockState(pos.east().south()).getMaterial() == Material.WATER
				&& world.getBlockState(pos.south()).getMaterial() == Material.WATER

				// grass in the 12 squares surrounding
				&& isGrassOrDirt(world, pos.west().north())
				&& isGrassOrDirt(world, pos.west())
				&& isGrassOrDirt(world, pos.west().south())
				&& isGrassOrDirt(world, pos.west().south(2))

				&& isGrassOrDirt(world, pos.north())
				&& isGrassOrDirt(world, pos.east().north())

				&& isGrassOrDirt(world, pos.south(2))
				&& isGrassOrDirt(world, pos.east().south(2))

				&& isGrassOrDirt(world, pos.east(2).north())
				&& isGrassOrDirt(world, pos.east(2))
				&& isGrassOrDirt(world, pos.east(2).south())
				&& isGrassOrDirt(world, pos.east(2).south(2))

				// solid underneath
				&& world.getBlockState(pos.down()).getMaterial().isSolid()
				&& world.getBlockState(pos.down().east()).getMaterial().isSolid()
				&& world.getBlockState(pos.down().east().south()).getMaterial().isSolid()
				&& world.getBlockState(pos.down().south()).getMaterial().isSolid()

				// 12 nature blocks above the grass?
				&& isNatureBlock(world, pos.up().west().north())
				&& isNatureBlock(world, pos.up().west())
				&& isNatureBlock(world, pos.up().west().south())
				&& isNatureBlock(world, pos.up().west().south(2))

				&& isNatureBlock(world, pos.up().north())
				&& isNatureBlock(world, pos.up().east().north())

				&& isNatureBlock(world, pos.up().south(2))
				&& isNatureBlock(world, pos.up().south(2).east())

				&& isNatureBlock(world, pos.up().east(2).north())
				&& isNatureBlock(world, pos.up().east(2))
				&& isNatureBlock(world, pos.up().east(2).south())
				&& isNatureBlock(world, pos.up().east(2).south(2));
	}

	/**
	 * Does the block at this location count as a "nature" block for portal purposes?
	 */
	private boolean isNatureBlock(World world, BlockPos pos) {
		Material mat = world.getBlockState(pos).getMaterial();
		return mat == Material.PLANTS || mat == Material.VINE || mat == Material.LEAVES;
	}

	/**
	 * Each twilight portal pool block should have grass or dirt on one side and a portal on the other.  If this is not true, delete this block, presumably causing a chain reaction.
	 */
	@Override
	@Deprecated
	public void neighborChanged(IBlockState state, World world, BlockPos pos, Block notUsed, BlockPos fromPos) {
		boolean good = Arrays.stream(EnumFacing.HORIZONTALS)
				.filter(e -> world.getBlockState(pos.offset(e)).getBlock() == this
						&& isGrassOrDirt(world, pos.offset(e.getOpposite())))
				.count() >= 2;

		if (!good) {
			world.playEvent(2001, pos, Block.getStateId(state));
			world.setBlockState(pos, Blocks.WATER.getDefaultState());
		}
	}

	private boolean isGrassOrDirt(World world, BlockPos pos) {
		return world.getBlockState(pos).getMaterial() == Material.GRASS
				|| world.getBlockState(pos).getMaterial() == Material.GROUND;
	}

	@Override
	public int quantityDropped(Random random) {
		return 0;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.TRANSLUCENT;
	}

	@Override
	public void onEntityCollidedWithBlock(World world, BlockPos pos, IBlockState state, Entity entity) {
		if (!entity.isRiding() && entity.getPassengers().isEmpty() && entity.timeUntilPortal <= 0) {
			if (entity instanceof EntityPlayerMP) {
				EntityPlayerMP playerMP = (EntityPlayerMP) entity;

				if (playerMP.timeUntilPortal > 0) {
					// do not switch dimensions if the player has any time on this thinger
					playerMP.timeUntilPortal = 10;
				} else {

					// send to twilight
					if (playerMP.dimension != TFConfig.dimension.dimensionID) {
						playerMP.addStat(TFAchievementPage.twilightPortal);
						playerMP.addStat(TFAchievementPage.twilightArrival);
						TwilightForestMod.LOGGER.debug("Player touched the portal block.  Sending the player to dimension {}", TFConfig.dimension.dimensionID);

						playerMP.mcServer.getPlayerList().transferPlayerToDimension(playerMP, TFConfig.dimension.dimensionID, TFTeleporter.getTeleporterForDim(playerMP.mcServer, TFConfig.dimension.dimensionID));

						// set respawn point for TF dimension to near the arrival portal
						playerMP.setSpawnChunk(new BlockPos(playerMP), true, TFConfig.dimension.dimensionID);
					} else {
						playerMP.mcServer.getPlayerList().transferPlayerToDimension(playerMP, 0, TFTeleporter.getTeleporterForDim(playerMP.mcServer, 0));
					}
				}
			} else {
				if (entity.dimension != TFConfig.dimension.dimensionID) {
					changeDimension(entity, TFConfig.dimension.dimensionID);
				} else {
					changeDimension(entity, 0);
				}
			}
		}
	}

	/**
	 * [VanillaCopy] Entity.changeDimension. Relevant edits noted.
	 * `this` -> `toTeleport`
	 * return value Entity -> void
	 */
	private void changeDimension(Entity toTeleport, int dimensionIn) {
		if (!toTeleport.world.isRemote && !toTeleport.isDead) {
			if (!net.minecraftforge.common.ForgeHooks.onTravelToDimension(toTeleport, dimensionIn)) return;
			toTeleport.world.profiler.startSection("changeDimension");
			MinecraftServer minecraftserver = toTeleport.getServer();
			int i = toTeleport.dimension;
			WorldServer worldserver = minecraftserver.worldServerForDimension(i);
			WorldServer worldserver1 = minecraftserver.worldServerForDimension(dimensionIn);
			toTeleport.dimension = dimensionIn;

			if (i == 1 && dimensionIn == 1) {
				worldserver1 = minecraftserver.worldServerForDimension(0);
				toTeleport.dimension = 0;
			}

			toTeleport.world.removeEntity(toTeleport);
			toTeleport.isDead = false;
			toTeleport.world.profiler.startSection("reposition");
			BlockPos blockpos;

			if (dimensionIn == 1) {
				blockpos = worldserver1.getSpawnCoordinate();
			} else {
				double d0 = toTeleport.posX;
				double d1 = toTeleport.posZ;
				double d2 = 8.0D;

				// Tf - remove 8x scaling for nether
				d0 = MathHelper.clamp(d0, worldserver1.getWorldBorder().minX() + 16.0D, worldserver1.getWorldBorder().maxX() - 16.0D);
				d1 = MathHelper.clamp(d1, worldserver1.getWorldBorder().minZ() + 16.0D, worldserver1.getWorldBorder().maxZ() - 16.0D);

				d0 = (double) MathHelper.clamp((int) d0, -29999872, 29999872);
				d1 = (double) MathHelper.clamp((int) d1, -29999872, 29999872);
				float f = toTeleport.rotationYaw;
				toTeleport.setLocationAndAngles(d0, toTeleport.posY, d1, 90.0F, 0.0F);
				Teleporter teleporter = TFTeleporter.getTeleporterForDim(minecraftserver, dimensionIn); // TF - custom teleporter
				teleporter.placeInExistingPortal(toTeleport, f);
				blockpos = new BlockPos(toTeleport);
			}

			worldserver.updateEntityWithOptionalForce(toTeleport, false);
			toTeleport.world.profiler.endStartSection("reloading");
			Entity entity = EntityList.newEntity(toTeleport.getClass(), worldserver1);

			if (entity != null) {
				entity.copyDataFromOld(toTeleport);

				if (i == 1 && dimensionIn == 1) {
					BlockPos blockpos1 = worldserver1.getTopSolidOrLiquidBlock(worldserver1.getSpawnPoint());
					entity.moveToBlockPosAndAngles(blockpos1, entity.rotationYaw, entity.rotationPitch);
				} else {
					// TF - inline moveToBlockPosAndAngles without +0.5 offsets, since teleporter already took care of it
					entity.setLocationAndAngles((double) blockpos.getX(), (double) blockpos.getY(), (double) blockpos.getZ(), entity.rotationYaw, entity.rotationPitch);
				}

				boolean flag = entity.forceSpawn;
				entity.forceSpawn = true;
				worldserver1.spawnEntity(entity);
				entity.forceSpawn = flag;
				worldserver1.updateEntityWithOptionalForce(entity, false);
			}

			toTeleport.isDead = true;
			toTeleport.world.profiler.endSection();
			worldserver.resetUpdateEntityTick();
			worldserver1.resetUpdateEntityTick();
			toTeleport.world.profiler.endSection();
		}
	}

	// Full [VanillaCopy] of BlockPortal.randomDisplayTick
	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
		if (rand.nextInt(100) == 0) {
			worldIn.playSound((double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D, (double) pos.getZ() + 0.5D, SoundEvents.BLOCK_PORTAL_AMBIENT, SoundCategory.BLOCKS, 0.5F, rand.nextFloat() * 0.4F + 0.8F, false);
		}

		for (int i = 0; i < 4; ++i) {
			double d0 = (double) ((float) pos.getX() + rand.nextFloat());
			double d1 = (double) ((float) pos.getY() + rand.nextFloat());
			double d2 = (double) ((float) pos.getZ() + rand.nextFloat());
			double d3 = ((double) rand.nextFloat() - 0.5D) * 0.5D;
			double d4 = ((double) rand.nextFloat() - 0.5D) * 0.5D;
			double d5 = ((double) rand.nextFloat() - 0.5D) * 0.5D;
			int j = rand.nextInt(2) * 2 - 1;

			if (worldIn.getBlockState(pos.west()).getBlock() != this && worldIn.getBlockState(pos.east()).getBlock() != this) {
				d0 = (double) pos.getX() + 0.5D + 0.25D * (double) j;
				d3 = (double) (rand.nextFloat() * 2.0F * (float) j);
			} else {
				d2 = (double) pos.getZ() + 0.5D + 0.25D * (double) j;
				d5 = (double) (rand.nextFloat() * 2.0F * (float) j);
			}

			worldIn.spawnParticle(EnumParticleTypes.PORTAL, d0, d1, d2, d3, d4, d5, new int[0]);
		}
	}
}
