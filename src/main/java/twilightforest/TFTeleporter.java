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
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.IChunkGenerator;
import twilightforest.biomes.TFBiomeBase;
import twilightforest.block.BlockTFPortal;
import twilightforest.block.TFBlocks;
import twilightforest.world.ChunkGeneratorTFBase;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.function.Predicate;

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
	public void placeInPortal(Entity entity, float facing) {
		if (!this.placeInExistingPortal(entity, facing)) {
			this.moveToSafeCoords(entity);
			this.makePortal(entity);
			this.placeInExistingPortal(entity, facing);
		}
	}

	private void moveToSafeCoords(Entity entity) {
		// if we're in enforced progression mode, check the biomes for safety
		boolean checkProgression = world.getGameRules().getBoolean(TwilightForestMod.ENFORCED_PROGRESSION_RULE);

		BlockPos pos = new BlockPos(entity);
		if (isSafeAround(pos, entity, checkProgression)) {
			return;
		}

		TwilightForestMod.LOGGER.debug("Portal destination looks unsafe, rerouting!");

		BlockPos safeCoords = findSafeCoords(200, pos, entity, checkProgression);
		if (safeCoords != null) {
			entity.setLocationAndAngles(safeCoords.getX(), entity.posY, safeCoords.getZ(), 90.0F, 0.0F);
			TwilightForestMod.LOGGER.debug("Safely rerouted!");
			return;
		}

		TwilightForestMod.LOGGER.info("Did not find a safe portal spot at first try, trying again with longer range.");
		safeCoords = findSafeCoords(400, pos, entity, checkProgression);

		if (safeCoords != null) {
			entity.setLocationAndAngles(safeCoords.getX(), entity.posY, safeCoords.getZ(), 90.0F, 0.0F);
			TwilightForestMod.LOGGER.info("Safely rerouted to long range portal.  Return trip not guaranteed.");
			return;
		}

		TwilightForestMod.LOGGER.warn("Still did not find a safe portal spot.");
	}

	@Nullable
	private BlockPos findSafeCoords(int range, BlockPos pos, Entity entity, boolean checkProgression) {
		int attempts = range / 8;
		for (int i = 0; i < attempts; i++) {
			BlockPos dPos = new BlockPos(
					pos.getX() + random.nextInt(range) - random.nextInt(range),
					100,
					pos.getZ() + random.nextInt(range) - random.nextInt(range)
			);

			if (isSafeAround(dPos, entity, checkProgression)) {
				return dPos;
			}
		}
		return null;
	}

	public final boolean isSafeAround(BlockPos pos, Entity entity, boolean checkProgression) {

		if (!isSafe(pos, entity, checkProgression)) {
			return false;
		}

		for (EnumFacing facing : EnumFacing.Plane.HORIZONTAL) {
			if (!isSafe(pos.offset(facing, 16), entity, checkProgression)) {
				return false;
			}
		}

		return true;
	}

	private boolean isSafe(BlockPos pos, Entity entity, boolean checkProgression) {
		return checkPos(pos) && (!checkProgression || checkBiome(pos, entity)) && checkStructure(pos);
	}

	private boolean checkPos(BlockPos pos) {
		return world.getWorldBorder().contains(pos);
	}

	private boolean checkStructure(BlockPos pos) {
		IChunkGenerator generator = world.getChunkProvider().chunkGenerator;
		if (generator instanceof ChunkGeneratorTFBase) {
			if (!world.isBlockLoaded(pos)) {
				generator.recreateStructures(null, pos.getX() >> 4, pos.getZ() >> 4);
			}
			return !((ChunkGeneratorTFBase) generator).isBlockInFullStructure(pos.getX(), pos.getZ());
		}
		return true;
	}

	private boolean checkBiome(BlockPos pos, Entity entity) {
		Biome biome = world.getBiome(pos);
		if (biome instanceof TFBiomeBase && entity instanceof EntityPlayerMP) {
			TFBiomeBase tfBiome = (TFBiomeBase) biome;
			EntityPlayerMP player = (EntityPlayerMP) entity;
			return tfBiome.doesPlayerHaveRequiredAchievement(player);
		}
		return true;
	}

	// [VanillaCopy] copy of super, edits noted
	@Override
	public boolean placeInExistingPortal(Entity entity, float rotationYaw) {
		int i = 200; // TF - scan radius up to 200, and also un-inline this variable back into below
		double d0 = -1.0D;
		int j = MathHelper.floor(entity.posX);
		int k = MathHelper.floor(entity.posZ);
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
			BlockPos blockpos3 = new BlockPos(entity);

			for (int i1 = -i; i1 <= i; ++i1) {
				BlockPos blockpos2;

				for (int j1 = -i; j1 <= i; ++j1) {

					// TF - skip positions outside current world border (MC-114796)
					if (!this.world.getWorldBorder().contains(blockpos3.add(i1, 0, j1))) {
						continue;
					}

					// TF - skip chunks that aren't generated
					ChunkPos chunkPos = new ChunkPos(blockpos3.add(i1, 0, j1));
					if (!this.world.isChunkGeneratedAt(chunkPos.x, chunkPos.z)) {
						continue;
					}

					// TF - explicitly fetch chunk so it can be unloaded if needed
					Chunk chunk = this.world.getChunk(chunkPos.x, chunkPos.z);

					for (BlockPos blockpos1 = blockpos3.add(i1, getScanHeight(blockpos3) - blockpos3.getY(), j1); blockpos1.getY() >= 0; blockpos1 = blockpos2) {
						blockpos2 = blockpos1.down();

						// TF - don't lookup state if inner condition would fail
						if (d0 >= 0.0D && blockpos1.distanceSq(blockpos3) >= d0) {
							continue;
						}

						// TF - use our portal block
						if (isPortal(chunk.getBlockState(blockpos1))) {
							for (blockpos2 = blockpos1.down(); isPortal(chunk.getBlockState(blockpos2)); blockpos2 = blockpos2.down()) {
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

					// TF - mark unwatched chunks for unload
					if (!this.world.getPlayerChunkMap().contains(chunkPos.x, chunkPos.z)) {
						this.world.getChunkProvider().queueUnload(chunk);
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

			entity.motionX = entity.motionY = entity.motionZ = 0.0D;

			if (entity instanceof EntityPlayerMP) {
				((EntityPlayerMP) entity).connection.setPlayerLocation(portalX, portalY, portalZ, entity.rotationYaw, entity.rotationPitch);
			} else {
				entity.setLocationAndAngles(portalX, portalY, portalZ, entity.rotationYaw, entity.rotationPitch);
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
		int chunkHeight = world.getChunk(x >> 4, z >> 4).getTopFilledSegment() + 15;
		return Math.min(worldHeight, chunkHeight);
	}

	private static boolean isPortal(IBlockState state) {
		return state.getBlock() == TFBlocks.twilight_portal;
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
			if (!checked.add(offset)) continue;
			if (isPortalAt(offset)) {
				checkAdjacent(offset, checked, result);
			} else {
				result.add(offset);
			}
		}
	}

	private boolean isPortalAt(BlockPos pos) {
		return isPortal(world.getBlockState(pos));
	}

	@Override
	public boolean makePortal(Entity entity) {

		// ensure area is populated first
		loadSurroundingArea(entity);

		BlockPos spot = findPortalCoords(entity, this::isPortalAt);
		String name = entity.getName();

		if (spot != null) {
			TwilightForestMod.LOGGER.debug("Found existing portal for {} at {}", name, spot);
			cachePortalCoords(entity, spot);
			return true;
		}

		spot = findPortalCoords(entity, this::isIdealForPortal);

		if (spot != null) {
			TwilightForestMod.LOGGER.debug("Found ideal portal spot for {} at {}", name, spot);
			cachePortalCoords(entity, makePortalAt(world, spot));
			return true;
		}

		TwilightForestMod.LOGGER.debug("Did not find ideal portal spot, shooting for okay one for {}", name);
		spot = findPortalCoords(entity, this::isOkayForPortal);

		if (spot != null) {
			TwilightForestMod.LOGGER.debug("Found okay portal spot for {} at {}", name, spot);
			cachePortalCoords(entity, makePortalAt(world, spot));
			return true;
		}

		// well I don't think we can actually just return false and fail here
		TwilightForestMod.LOGGER.debug("Did not even find an okay portal spot, just making a random one for {}", name);

		// adjust the portal height based on what world we're traveling to
		double yFactor = getYFactor();
		// modified copy of base Teleporter method:
		cachePortalCoords(entity, makePortalAt(world, new BlockPos(entity.posX, (entity.posY * yFactor) - 1.0, entity.posZ)));

		return false;
	}

	private void loadSurroundingArea(Entity entity) {

		int x = MathHelper.floor(entity.posX) >> 4;
		int z = MathHelper.floor(entity.posZ) >> 4;

		for (int dx = -2; dx <= 2; dx++) {
			for (int dz = -2; dz <= 2; dz++) {
				world.getChunk(x + dx, z + dz);
			}
		}
	}

	private double getYFactor() {
		return world.provider.getDimension() == TFConfig.originDimension ? 2.0 : 0.5;
	}

	@Nullable
	private BlockPos findPortalCoords(Entity entity, Predicate<BlockPos> predicate) {
		// adjust the height based on what world we're traveling to
		double yFactor = getYFactor();
		// modified copy of base Teleporter method:
		int entityX = MathHelper.floor(entity.posX);
		int entityZ = MathHelper.floor(entity.posZ);

		BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

		double spotWeight = -1D;
		BlockPos spot = null;

		int range = 16;
		for (int rx = entityX - range; rx <= entityX + range; rx++) {
			double xWeight = (rx + 0.5D) - entity.posX;
			for (int rz = entityZ - range; rz <= entityZ + range; rz++) {
				double zWeight = (rz + 0.5D) - entity.posZ;

				for (int ry = getScanHeight(rx, rz); ry >= 0; ry--) {

					if (!world.isAirBlock(pos.setPos(rx, ry, rz))) {
						continue;
					}

					while (ry > 0 && world.isAirBlock(pos.setPos(rx, ry - 1, rz))) {
						ry--;
					}

					double yWeight = (ry + 0.5D) - entity.posY * yFactor;
					double rPosWeight = xWeight * xWeight + yWeight * yWeight + zWeight * zWeight;

					if (spotWeight < 0.0D || rPosWeight < spotWeight) {
						// check from the "in ground" pos
						if (predicate.test(pos)) {
							spotWeight = rPosWeight;
							spot = pos.toImmutable();
						}
					}
				}
			}
		}

		return spot;
	}

	private boolean isIdealForPortal(BlockPos pos) {
		for (int potentialZ = 0; potentialZ < 4; potentialZ++) {
			for (int potentialX = 0; potentialX < 4; potentialX++) {
				for (int potentialY = 0; potentialY < 4; potentialY++) {
					BlockPos tPos = pos.add(potentialX - 1, potentialY, potentialZ - 1);
					Material material = world.getBlockState(tPos).getMaterial();
					if (potentialY == 0 && material != Material.GRASS
							|| potentialY >= 1 && !material.isReplaceable()) {
						return false;
					}
				}
			}
		}
		return true;
	}

	private boolean isOkayForPortal(BlockPos pos) {
		for (int potentialZ = 0; potentialZ < 4; potentialZ++) {
			for (int potentialX = 0; potentialX < 4; potentialX++) {
				for (int potentialY = 0; potentialY < 4; potentialY++) {
					BlockPos tPos = pos.add(potentialX - 1, potentialY, potentialZ - 1);
					Material material = world.getBlockState(tPos).getMaterial();
					if (potentialY == 0 && !material.isSolid() && !material.isLiquid()
							|| potentialY >= 1 && !material.isReplaceable()) {
						return false;
					}
				}
			}
		}
		return true;
	}

	private void cachePortalCoords(Entity entity, BlockPos pos) {
		int x = MathHelper.floor(entity.posX), z = MathHelper.floor(entity.posZ);
		destinationCoordinateCache.put(ChunkPos.asLong(x, z), new PortalPosition(pos, world.getTotalWorldTime()));
	}

	private BlockPos makePortalAt(World world, BlockPos pos) {

		if (pos.getY() < 30) {
			pos = new BlockPos(pos.getX(), 30, pos.getZ());
		} else if (pos.getY() > 128 - 10) {
			pos = new BlockPos(pos.getX(), 128 - 10, pos.getZ());
		}

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
		IBlockState portal = TFBlocks.twilight_portal.getDefaultState().withProperty(BlockTFPortal.DISALLOW_RETURN, !TFConfig.shouldReturnPortalBeUsable);

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

		return pos;
	}

	private IBlockState randNatureBlock(Random random) {
		Block[] blocks = {Blocks.BROWN_MUSHROOM, Blocks.RED_MUSHROOM, Blocks.TALLGRASS, Blocks.RED_FLOWER, Blocks.YELLOW_FLOWER};
		return blocks[random.nextInt(blocks.length)].getDefaultState();
	}
}
