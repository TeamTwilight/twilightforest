package twilightforest.world;

import com.google.common.collect.Maps;
import it.unimi.dsi.fastutil.objects.Object2LongMap;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.portal.PortalInfo;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.server.level.ColumnPos;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.TicketType;
import net.minecraftforge.common.util.ITeleporter;
import twilightforest.TFConfig;
import twilightforest.TwilightForestMod;
import twilightforest.block.TFPortalBlock;
import twilightforest.block.TFBlocks;
import twilightforest.util.WorldUtil;
import twilightforest.world.components.chunkgenerators.ChunkGeneratorTwilight;
import twilightforest.world.registration.TFGenerationSettings;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

public class TFTeleporter implements ITeleporter {

	private static final Map<ResourceLocation, Map<ColumnPos, PortalPosition>> destinationCoordinateCache = new HashMap<>();
	private static final Object2LongMap<ColumnPos> columnMap = new Object2LongOpenHashMap<>();

	private static boolean locked;

	public TFTeleporter(boolean locked) {
		TFTeleporter.locked = locked;
	}

	@Nullable
	@Override
	public PortalInfo getPortalInfo(Entity entity, ServerLevel dest, Function<ServerLevel, PortalInfo> defaultPortalInfo) {
		PortalInfo pos;
		if ((pos = placeInExistingPortal(dest, entity, entity.blockPosition(), entity instanceof Player)) == null) {
			pos = moveToSafeCoords(dest, entity);
			makePortal(entity, dest, pos.pos);
			pos = placeInExistingPortal(dest, entity, new BlockPos(pos.pos), entity instanceof Player);
		}
		return pos;
	}

	@Nullable
	private static PortalInfo placeInExistingPortal(ServerLevel world, Entity entity, BlockPos pos, boolean isPlayer) {
		int i = 200; // scan radius up to 200, and also un-inline this variable back into below
		boolean flag = true;
		BlockPos blockpos = BlockPos.ZERO;
		ColumnPos columnPos = new ColumnPos(pos);

		if (!isPlayer && columnMap.containsKey(columnPos)) {
			return null;
		} else {
			PortalPosition portalPosition = destinationCoordinateCache.containsKey(world.dimension().location()) ? destinationCoordinateCache.get(world.dimension().location()).get(columnPos) : null;
			if (portalPosition != null) {
				blockpos = portalPosition.pos;
				portalPosition.lastUpdateTime = world.getGameTime();
				flag = false;
			} else {
				//BlockPos blockpos3 = new BlockPos(entity);
				double d0 = Double.MAX_VALUE;

				for (int i1 = -i; i1 <= i; ++i1) {
					BlockPos blockpos2;

					for (int j1 = -i; j1 <= i; ++j1) {

						// skip positions outside current world border (MC-114796)
						if (!world.getWorldBorder().isWithinBounds(pos.offset(i1, 0, j1))) {
							continue;
						}

						// skip chunks that aren't generated
						ChunkPos chunkPos = new ChunkPos(pos.offset(i1, 0, j1));
						if (!world.getChunkSource().chunkMap.isExistingChunkFull(chunkPos)) {
							continue;
						}

						// explicitly fetch chunk so it can be unloaded if needed
						LevelChunk chunk = world.getChunk(chunkPos.x, chunkPos.z);

						for (BlockPos blockpos1 = pos.offset(i1, getScanHeight(world, pos) - pos.getY(), j1); blockpos1.getY() >= 0; blockpos1 = blockpos2) {
							blockpos2 = blockpos1.below();

							// don't lookup state if inner condition would fail
							if (d0 >= 0.0D && blockpos1.distSqr(pos) >= d0) {
								continue;
							}

							// use our portal block
							if (isPortal(chunk.getBlockState(blockpos1))) {
								for (blockpos2 = blockpos1.below(); isPortal(chunk.getBlockState(blockpos2)); blockpos2 = blockpos2.below()) {
									blockpos1 = blockpos2;
								}

								float d1 = (float) blockpos1.distSqr(pos);
								if (d0 < 0.0D || d1 < d0) {
									d0 = d1;
									blockpos = blockpos1;
									// restrict search radius to new distance
									i = Mth.ceil(Mth.sqrt(d1));
								}
							}
						}

						// mark unwatched chunks for unload
						//						if (!this.world.getPlayerChunkMap().contains(chunkPos.x, chunkPos.z)) {
						//							this.world.getChunkProvider().queueUnload(chunk);
						//						}
					}
				}
			}
		}

		if (blockpos.equals(BlockPos.ZERO)) {
			long factor = world.getGameTime() + 300L;
			columnMap.put(columnPos, factor);
			return null;
		} else {
			if (flag) {
				destinationCoordinateCache.putIfAbsent(world.dimension().location(), Maps.newHashMapWithExpectedSize(4096));
				destinationCoordinateCache.get(world.dimension().location()).put(columnPos, new PortalPosition(blockpos, world.getGameTime()));
				world.getChunkSource().addRegionTicket(TicketType.PORTAL, new ChunkPos(blockpos), 3, new BlockPos(columnPos.x, blockpos.getY(), columnPos.z));
			}

			// replace with our own placement logic
			BlockPos[] portalBorder = getBoundaryPositions(world, blockpos).toArray(new BlockPos[0]);
			BlockPos borderPos = portalBorder[0/*random.nextInt(portalBorder.length)*/];

			double portalX = borderPos.getX() + 0.5;
			double portalY = borderPos.getY() + 1.0;
			double portalZ = borderPos.getZ() + 0.5;

			return makePortalInfo(entity, portalX, portalY, portalZ);
		}
	}

	private static int getScanHeight(ServerLevel world, BlockPos pos) {
		return getScanHeight(world, pos.getX(), pos.getZ());
	}

	private static int getScanHeight(ServerLevel world, int x, int z) {
		int worldHeight = world.getMaxBuildHeight() - 1;
		int chunkHeight = world.getChunk(x >> 4, z >> 4).getHighestSectionPosition() + 15;
		return Math.min(worldHeight, chunkHeight);
	}

	private static boolean isPortal(BlockState state) {
		return state.getBlock() == TFBlocks.TWILIGHT_PORTAL.get();
	}

	// from the start point, builds a set of all directly adjacent non-portal blocks
	private static Set<BlockPos> getBoundaryPositions(ServerLevel world, BlockPos start) {
		Set<BlockPos> result = new HashSet<>(), checked = new HashSet<>();
		checked.add(start);
		checkAdjacent(world, start, checked, result);
		return result;
	}

	private static void checkAdjacent(ServerLevel world, BlockPos pos, Set<BlockPos> checked, Set<BlockPos> result) {
		for (Direction facing : Direction.Plane.HORIZONTAL) {
			BlockPos offset = pos.relative(facing);
			if (!checked.add(offset))
				continue;
			if (isPortalAt(world, offset)) {
				checkAdjacent(world, offset, checked, result);
			} else {
				result.add(offset);
			}
		}
	}

	private static boolean isPortalAt(ServerLevel world, BlockPos pos) {
		return isPortal(world.getBlockState(pos));
	}

	private static PortalInfo moveToSafeCoords(ServerLevel world, Entity entity) {
		// if we're in enforced progression mode, check the biomes for safety
		boolean checkProgression = TFGenerationSettings.isProgressionEnforced(world);

		BlockPos pos = entity.blockPosition();
		if (isSafeAround(world, pos, entity, checkProgression)) {
			TwilightForestMod.LOGGER.debug("Portal destination looks safe!");
			return makePortalInfo(entity, entity.position());
		}

		TwilightForestMod.LOGGER.debug("Portal destination looks unsafe, rerouting!");

		BlockPos safeCoords = findSafeCoords(world, 200, pos, entity, checkProgression);
		if (safeCoords != null) {
			TwilightForestMod.LOGGER.debug("Safely rerouted!");
			return makePortalInfo(entity, safeCoords.getX(), entity.getY(), safeCoords.getZ());
		}

		TwilightForestMod.LOGGER.info("Did not find a safe portal spot at first try, trying again with longer range.");
		safeCoords = findSafeCoords(world, 400, pos, entity, checkProgression);

		if (safeCoords != null) {
			TwilightForestMod.LOGGER.info("Safely rerouted to long range portal.  Return trip not guaranteed.");
			return makePortalInfo(entity, safeCoords.getX(), entity.getY(), safeCoords.getZ());
		}

		TwilightForestMod.LOGGER.warn("Still did not find a safe portal spot.");

		return makePortalInfo(entity, entity.position());
	}

	public static boolean isSafeAround(Level world, BlockPos pos, Entity entity, boolean checkProgression) {

		if (!isSafe(world, pos, entity, checkProgression)) {
			return false;
		}

		for (Direction facing : Direction.Plane.HORIZONTAL) {
			if (!isSafe(world, pos.relative(facing, 16), entity, checkProgression)) {
				return false;
			}
		}

		return true;
	}

	private static boolean isSafe(Level world, BlockPos pos, Entity entity, boolean checkProgression) {
		return checkPos(world, pos) && (!checkProgression || checkBiome(world, pos, entity)) && checkStructure(world, pos);
	}

	private static boolean checkPos(Level world, BlockPos pos) {
		return world.getWorldBorder().isWithinBounds(pos);
	}

	private static boolean checkStructure(Level world, BlockPos pos) {
		ChunkGeneratorTwilight generator = WorldUtil.getChunkGenerator(world);
		if (generator != null)
			return !TFGenerationSettings.locateTFStructureInRange((ServerLevel) world, pos, 0).isPresent();
		return true;
	}

	private static boolean checkBiome(Level world, BlockPos pos, Entity entity) {
		return TFGenerationSettings.isBiomeSafeFor(world.getBiome(pos), entity);
	}

	@Nullable
	private static BlockPos findSafeCoords(ServerLevel world, int range, BlockPos pos, Entity entity, boolean checkProgression) {
		int attempts = range / 8;
		for (int x = 0; x < attempts; x++) {
			for (int z = 0; z < attempts; z++) {
				BlockPos dPos = new BlockPos(pos.getX() + (x * attempts) - (range / 2), 100, pos.getZ() + (z * attempts) - (range / 2));

				if (isSafeAround(world, dPos, entity, checkProgression)) {
					return dPos;
				}
			}
		}
		return null;
	}

	private void makePortal(Entity entity, ServerLevel world, Vec3 pos) {
		// ensure area is populated first
		loadSurroundingArea(world, pos);

		BlockPos spot = findPortalCoords(world, pos, blockPos -> isPortalAt(world, blockPos));
		String name = entity.getName().getString();

		if (spot != null) {
			TwilightForestMod.LOGGER.debug("Found existing portal for {} at {}", name, spot);
			cachePortalCoords(world, pos, spot);
			return;
		}

		spot = findPortalCoords(world, pos, blockpos -> isIdealForPortal(world, blockpos));

		if (spot != null) {
			TwilightForestMod.LOGGER.debug("Found ideal portal spot for {} at {}", name, spot);
			cachePortalCoords(world, pos, makePortalAt(world, spot));
			return;
		}

		TwilightForestMod.LOGGER.debug("Did not find ideal portal spot, shooting for okay one for {}", name);
		spot = findPortalCoords(world, pos, blockPos -> isOkayForPortal(world, blockPos));

		if (spot != null) {
			TwilightForestMod.LOGGER.debug("Found okay portal spot for {} at {}", name, spot);
			cachePortalCoords(world, pos, makePortalAt(world, spot));
			return;
		}

		// well I don't think we can actually just return and fail here
		TwilightForestMod.LOGGER.debug("Did not even find an okay portal spot, just making a random one for {}", name);

		// adjust the portal height based on what world we're traveling to
		double yFactor = getYFactor(world);
		// modified copy of base Teleporter method:
		cachePortalCoords(world, pos, makePortalAt(world, new BlockPos(entity.getX(), (entity.getY() * yFactor) - 1.0, entity.getZ())));
	}

	private static void loadSurroundingArea(ServerLevel world, Vec3 pos) {

		int x = Mth.floor(pos.x) >> 4;
		int z = Mth.floor(pos.y) >> 4;

		for (int dx = -2; dx <= 2; dx++) {
			for (int dz = -2; dz <= 2; dz++) {
				world.getChunk(x + dx, z + dz);
			}
		}
	}

	@Nullable
	private static BlockPos findPortalCoords(ServerLevel world, Vec3 loc, Predicate<BlockPos> predicate) {
		// adjust the height based on what world we're traveling to
		double yFactor = getYFactor(world);
		// modified copy of base Teleporter method:
		int entityX = Mth.floor(loc.x);
		int entityZ = Mth.floor(loc.z);

		BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

		double spotWeight = -1D;
		BlockPos spot = null;

		int range = 16;
		for (int rx = entityX - range; rx <= entityX + range; rx++) {
			double xWeight = (rx + 0.5D) - loc.x;
			for (int rz = entityZ - range; rz <= entityZ + range; rz++) {
				double zWeight = (rz + 0.5D) - loc.z;

				for (int ry = getScanHeight(world, rx, rz); ry >= world.getMinBuildHeight(); ry--) {

					if (!world.isEmptyBlock(pos.set(rx, ry, rz))) {
						continue;
					}

					while (ry > world.getMinBuildHeight() && world.isEmptyBlock(pos.set(rx, ry - 1, rz))) {
						ry--;
					}

					double yWeight = (ry + 0.5D) - loc.y * yFactor;
					double rPosWeight = xWeight * xWeight + yWeight * yWeight + zWeight * zWeight;

					if (spotWeight < 0.0D || rPosWeight < spotWeight) {
						// check from the "in ground" pos
						if (predicate.test(pos)) {
							spotWeight = rPosWeight;
							spot = pos.immutable();
						}
					}
				}
			}
		}

		return spot;
	}

	private static double getYFactor(ServerLevel world) {
		return world.dimension().location().equals(Level.OVERWORLD.location()) ? 2.0 : 0.5;
	}

	private static void cachePortalCoords(ServerLevel world, Vec3 loc, BlockPos pos) {
		int x = Mth.floor(loc.x), z = Mth.floor(loc.z);
		destinationCoordinateCache.putIfAbsent(world.dimension().location(), Maps.newHashMapWithExpectedSize(4096));
		destinationCoordinateCache.get(world.dimension().location()).put(new ColumnPos(x, z), new PortalPosition(pos, world.getGameTime()));
	}

	private static boolean isIdealForPortal(ServerLevel world, BlockPos pos) {
		for (int potentialZ = 0; potentialZ < 4; potentialZ++) {
			for (int potentialX = 0; potentialX < 4; potentialX++) {
				for (int potentialY = 0; potentialY < 4; potentialY++) {
					BlockPos tPos = pos.offset(potentialX - 1, potentialY, potentialZ - 1);
					Material material = world.getBlockState(tPos).getMaterial();
					if (potentialY == 0 && material != Material.GRASS || potentialY >= 1 && !material.isReplaceable()) {
						return false;
					}
				}
			}
		}
		return true;
	}

	protected BlockPos makePortalAt(Level world, BlockPos pos) {
		// grass all around it
		BlockState grass = Blocks.GRASS_BLOCK.defaultBlockState();

		world.setBlockAndUpdate(pos.west().north(), grass);
		world.setBlockAndUpdate(pos.north(), grass);
		world.setBlockAndUpdate(pos.east().north(), grass);
		world.setBlockAndUpdate(pos.east(2).north(), grass);

		world.setBlockAndUpdate(pos.west(), grass);
		world.setBlockAndUpdate(pos.east(2), grass);

		world.setBlockAndUpdate(pos.west().south(), grass);
		world.setBlockAndUpdate(pos.east(2).south(), grass);

		world.setBlockAndUpdate(pos.west().south(2), grass);
		world.setBlockAndUpdate(pos.south(2), grass);
		world.setBlockAndUpdate(pos.east().south(2), grass);
		world.setBlockAndUpdate(pos.east(2).south(2), grass);

		// dirt under it
		BlockState dirt = Blocks.DIRT.defaultBlockState();

		world.setBlockAndUpdate(pos.below(), dirt);
		world.setBlockAndUpdate(pos.east().below(), dirt);
		world.setBlockAndUpdate(pos.south().below(), dirt);
		world.setBlockAndUpdate(pos.east().south().below(), dirt);

		// portal in it
		BlockState portal = TFBlocks.TWILIGHT_PORTAL.get().defaultBlockState().setValue(TFPortalBlock.DISALLOW_RETURN, (locked || !TFConfig.COMMON_CONFIG.shouldReturnPortalBeUsable.get()));

		world.setBlock(pos, portal, 2);
		world.setBlock(pos.east(), portal, 2);
		world.setBlock(pos.south(), portal, 2);
		world.setBlock(pos.east().south(), portal, 2);

		// meh, let's just make a bunch of air over it for 4 squares
		for (int dx = -1; dx <= 2; dx++) {
			for (int dz = -1; dz <= 2; dz++) {
				for (int dy = 1; dy <= 5; dy++) {
					world.removeBlock(pos.offset(dx, dy, dz), false);
				}
			}
		}

		// finally, "nature decorations"!
		world.setBlock(pos.west().north().above(), randNatureBlock(world.random), 2);
		world.setBlock(pos.north().above(), randNatureBlock(world.random), 2);
		world.setBlock(pos.east().north().above(), randNatureBlock(world.random), 2);
		world.setBlock(pos.east(2).north().above(), randNatureBlock(world.random), 2);

		world.setBlock(pos.west().above(), randNatureBlock(world.random), 2);
		world.setBlock(pos.east(2).above(), randNatureBlock(world.random), 2);

		world.setBlock(pos.west().south().above(), randNatureBlock(world.random), 2);
		world.setBlock(pos.east(2).south().above(), randNatureBlock(world.random), 2);

		world.setBlock(pos.west().south(2).above(), randNatureBlock(world.random), 2);
		world.setBlock(pos.south(2).above(), randNatureBlock(world.random), 2);
		world.setBlock(pos.east().south(2).above(), randNatureBlock(world.random), 2);
		world.setBlock(pos.east(2).south(2).above(), randNatureBlock(world.random), 2);

		return pos;
	}

	private static BlockState randNatureBlock(Random random) {
		Block[] blocks = {Blocks.BROWN_MUSHROOM, Blocks.RED_MUSHROOM, Blocks.GRASS, Blocks.POPPY, Blocks.DANDELION};
		return blocks[random.nextInt(blocks.length)].defaultBlockState();
	}

	private static boolean isOkayForPortal(ServerLevel world, BlockPos pos) {
		for (int potentialZ = 0; potentialZ < 4; potentialZ++) {
			for (int potentialX = 0; potentialX < 4; potentialX++) {
				for (int potentialY = 0; potentialY < 4; potentialY++) {
					BlockPos tPos = pos.offset(potentialX - 1, potentialY, potentialZ - 1);
					Material material = world.getBlockState(tPos).getMaterial();
					if (potentialY == 0 && !material.isSolid() && !material.isLiquid() || potentialY >= 1 && !material.isReplaceable()) {
						return false;
					}
				}
			}
		}
		return true;
	}

	private static PortalInfo makePortalInfo(Entity entity, double x, double y, double z) {
		return makePortalInfo(entity, new Vec3(x, y, z));
	}

	private static PortalInfo makePortalInfo(Entity entity, Vec3 pos) {
		return new PortalInfo(pos, Vec3.ZERO, entity.getYRot(), entity.getXRot());
	}

	@Override
	public Entity placeEntity(Entity entity, ServerLevel currentWorld, ServerLevel destWorld, float yaw, Function<Boolean, Entity> repositionEntity) {
		entity.fallDistance = 0;
		return repositionEntity.apply(false);
	}

	static class PortalPosition {
		public final BlockPos pos;
		long lastUpdateTime;

		PortalPosition(BlockPos pos, long time) {
			this.pos = pos;
			this.lastUpdateTime = time;
		}
	}
}
