package twilightforest.world;

import net.minecraft.block.Block;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.SpawnListEntry;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.NoiseGeneratorPerlin;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraftforge.event.ForgeEventFactory;
import twilightforest.TFFeature;
import twilightforest.biomes.TFBiomeBase;
import twilightforest.biomes.TFBiomeDecorator;
import twilightforest.block.TFBlocks;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

// TODO: doc out all the vanilla copying
public abstract class ChunkGeneratorTFBase implements IChunkGenerator {

	protected final Random rand;

	protected final NoiseGeneratorPerlin surfaceNoise;

	protected final World world;
	protected final WorldType terrainType;

	protected double[] depthBuffer = new double[256];
	protected Biome[] biomesForGeneration;

	protected final MapGenTFMajorFeature majorFeatureGenerator = new MapGenTFMajorFeature();

	private final boolean shouldGenerateBedrock;

	protected static long getSeed(int x, int z) {
		return x * 0x4f9939f508L + z * 0x1ef1565bd5L;
	}

	public ChunkGeneratorTFBase(World world, long seed, boolean enableFeatures, boolean shouldGenerateBedrock) {
		this.world = world;
		this.terrainType = world.getWorldInfo().getTerrainType();
		this.rand = new Random(seed);
		this.surfaceNoise = new NoiseGeneratorPerlin(this.rand, 4);
		this.shouldGenerateBedrock = shouldGenerateBedrock;
	}

	protected final Chunk makeChunk(int x, int z, ChunkPrimer primer) {

		Chunk chunk = new Chunk(world, primer, x, z);

		// load in biomes, to prevent striping?!
		byte[] chunkBiomes = chunk.getBiomeArray();
		for (int i = 0; i < chunkBiomes.length; ++i) {
			chunkBiomes[i] = (byte) Biome.getIdForBiome(this.biomesForGeneration[i]);
		}

		chunk.generateSkylightMap();

		return chunk;
	}

	// [VanillaCopy] Exact, ChunkGeneratorOverworld.replaceBiomeBlocks
	public void replaceBiomeBlocks(int x, int z, ChunkPrimer primer, Biome[] biomesIn)
	{
		if (!ForgeEventFactory.onReplaceBiomeBlocks(this, x, z, primer, this.world)) return;
		double d0 = 0.03125D;
		this.depthBuffer = this.surfaceNoise.getRegion(this.depthBuffer, (double)(x * 16), (double)(z * 16), 16, 16, 0.0625D, 0.0625D, 1.0D);

		for (int i = 0; i < 16; ++i)
		{
			for (int j = 0; j < 16; ++j)
			{
				Biome biome = biomesIn[j + i * 16];
				biome.genTerrainBlocks(this.world, this.rand, primer, x * 16 + i, z * 16 + j, this.depthBuffer[j + i * 16]);
			}
		}
	}

	/**
	 * Raises up and hollows out the hollow hills.
	 */
	protected final void deformTerrainForFeature(int cx, int cz, ChunkPrimer primer) {
		TFFeature nearFeature = TFFeature.getNearestFeature(cx, cz, world);
		if (!nearFeature.isTerrainAltered) {
			return;
		}

		int[] nearCenter = TFFeature.getNearestCenter(cx, cz, world);

		int hx = nearCenter[0];
		int hz = nearCenter[1];

		if (nearFeature == TFFeature.trollCave) {
			// troll cloud, more like
			deformTerrainForTrollCloud2(primer, nearFeature, cx, cz, hx, hz);
		}

		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {
				int dx = x - hx;
				int dz = z - hz;

				if (nearFeature == TFFeature.hill1 || nearFeature == TFFeature.hill2 || nearFeature == TFFeature.hill3 || nearFeature == TFFeature.hydraLair) {
					// hollow hills
					int hdiam = ((nearFeature.size * 2 + 1) * 16);
					int dist = (int) Math.sqrt(dx * dx + dz * dz);
					int hheight = (int) (Math.cos((float) dist / (float) hdiam * Math.PI) * ((float) hdiam / 3F));

					raiseHills(primer, nearFeature, hdiam, x, z, dx, dz, hheight);
				} else if (nearFeature == TFFeature.hedgeMaze || nearFeature == TFFeature.nagaCourtyard || nearFeature == TFFeature.questGrove) {
					// hedge mazes, naga arena
					flattenTerrainForFeature(primer, nearFeature, x, z, dx, dz);
				} else if (nearFeature == TFFeature.yetiCave) {
					// yeti lairs are square
					deformTerrainForYetiLair(primer, nearFeature, x, z, dx, dz);
				}
			}
		}

		// done!
	}

	private void raiseHills(ChunkPrimer primer, TFFeature nearFeature, int hdiam, int x, int z, int dx, int dz, int hillHeight) {
		int newGround = -1;
		boolean foundGroundLevel = false;

		// raise the hill
		for (int y = TFWorld.SEALEVEL; y < TFWorld.CHUNKHEIGHT; y++) {
			Block currentTerrain = primer.getBlockState(x, y, z).getBlock();
			if (currentTerrain != Blocks.STONE && !foundGroundLevel) {
				// we found the top of the stone layer
				newGround = y + hillHeight;

				foundGroundLevel = true;
			}
			if (foundGroundLevel && y <= newGround) {
				primer.setBlockState(x, y, z, Blocks.STONE.getDefaultState());
			}
		}
		// add the hollow part. Also turn water into stone below that
		int hollow = hillHeight - 4 - nearFeature.size;

		// hydra lair has a piece missing
		if (nearFeature == TFFeature.hydraLair) {
			int mx = dx + 16;
			int mz = dz + 16;
			int mdist = (int) Math.sqrt(mx * mx + mz * mz);
			int mheight = (int) (Math.cos(mdist / (hdiam / 1.5) * Math.PI) * (hdiam / 1.5));

			hollow = Math.max(mheight - 4, hollow);
		}

		if (hollow < 0) {
			hollow = 0;
		}

		// hollow out the hollow parts
		int hollowFloor = TFWorld.SEALEVEL - 3 - (hollow / 8);
		if (nearFeature == TFFeature.hydraLair) {
			// different floor
			hollowFloor = TFWorld.SEALEVEL;
		}

		if (hillHeight > 0) {
			// put a base on hills that go over open space or water
			for (int y = 0; y < TFWorld.SEALEVEL; y++) {
				if (primer.getBlockState(x, y, z).getBlock() != Blocks.STONE) {
					primer.setBlockState(x, y, z, Blocks.STONE.getDefaultState());
				}
			}
		}

		for (int y = hollowFloor + 1; y < hollowFloor + hollow; y++) {
			primer.setBlockState(x, y, z, Blocks.AIR.getDefaultState());
		}
	}

	private void flattenTerrainForFeature(ChunkPrimer primer, TFFeature nearFeature, int x, int z, int dx, int dz) {
		int oldGround;
		int newGround;
		float squishFactor = 0;
		int mazeHeight = TFWorld.SEALEVEL + 1;
		final int FEATURE_BOUNDARY = (nearFeature.size * 2 + 1) * 8 - 8;

		if (dx <= -FEATURE_BOUNDARY) {
			squishFactor = (-dx - FEATURE_BOUNDARY) / 8.0f;
		}

		if (dx >= FEATURE_BOUNDARY) {
			squishFactor = (dx - FEATURE_BOUNDARY) / 8.0f;
		}
		if (dz <= -FEATURE_BOUNDARY) {
			squishFactor = Math.max(squishFactor, (-dz - FEATURE_BOUNDARY) / 8.0f);
		}

		if (dz >= FEATURE_BOUNDARY) {
			squishFactor = Math.max(squishFactor, (dz - FEATURE_BOUNDARY) / 8.0f);
		}

		if (squishFactor > 0) {
			// blend the old terrain height to arena height
			newGround = -1;

			for (int y = 0; y <= 127; y++) {
				Block currentTerrain = primer.getBlockState(x, y, z).getBlock();
				// we're still in ground
				if (currentTerrain != Blocks.STONE) {
					if (newGround == -1) {
						// we found the lowest chunk of earth
						oldGround = y;
						mazeHeight += ((oldGround - mazeHeight) * squishFactor);

						newGround = oldGround;
					}
				}
			}
		}

		// sets the ground level to the maze height
		for (int y = 0; y <= 127; y++) {
			Block b = primer.getBlockState(x, y, z).getBlock();
			if (y < mazeHeight && (b == Blocks.AIR || b == Blocks.WATER)) {
				primer.setBlockState(x, y, z, Blocks.STONE.getDefaultState());
			}
			if (y >= mazeHeight && b != Blocks.WATER) {
				primer.setBlockState(x, y, z, Blocks.AIR.getDefaultState());
			}
		}
	}

	private void deformTerrainForYetiLair(ChunkPrimer primer, TFFeature nearFeature, int x, int z, int dx, int dz) {
		int oldGround;
		int newGround;
		float squishFactor = 0;
		int topHeight = TFWorld.SEALEVEL + 24;
		int outerBoundary = (nearFeature.size * 2 + 1) * 8 - 8;

		// outer boundary
		if (dx <= -outerBoundary) {
			squishFactor = (-dx - outerBoundary) / 8.0f;
		}

		if (dx >= outerBoundary) {
			squishFactor = (dx - outerBoundary) / 8.0f;
		}
		if (dz <= -outerBoundary) {
			squishFactor = Math.max(squishFactor, (-dz - outerBoundary) / 8.0f);
		}

		if (dz >= outerBoundary) {
			squishFactor = Math.max(squishFactor, (dz - outerBoundary) / 8.0f);
		}

		// inner boundary
		int caveBoundary = (nearFeature.size * 2) * 8 - 8;
		int hollowCeiling = TFWorld.SEALEVEL + 16;

		int offset = Math.min(Math.abs(dx), Math.abs(dz));
		hollowCeiling = (TFWorld.SEALEVEL + 40) - (offset * 4);

		// center square cave
		if (dx >= -caveBoundary && dz >= -caveBoundary && dx <= caveBoundary && dz <= caveBoundary) {
			hollowCeiling = TFWorld.SEALEVEL + 16;
		}

		// slope ceiling slightly
		hollowCeiling -= (offset / 6);

		// max out ceiling 8 blocks from roof
		hollowCeiling = Math.min(hollowCeiling, TFWorld.SEALEVEL + 16);

		// floor, also with slight slope
		int hollowFloor = TFWorld.SEALEVEL - 1 + (offset / 6);

		if (squishFactor > 0) {
			// blend the old terrain height to arena height
			newGround = -1;

			for (int y = 0; y <= 127; y++) {
				Block currentTerrain = primer.getBlockState(x, y, z).getBlock();
				if (currentTerrain == Blocks.STONE) {
					// we're still in ground
					continue;
				} else {
					if (newGround == -1) {
						// we found the lowest chunk of earth
						oldGround = y;
						topHeight += ((oldGround - topHeight) * squishFactor);

						hollowFloor += ((oldGround - hollowFloor) * squishFactor);

						newGround = oldGround;
					}
				}
			}
		}

		// carve the cave into the stone
		for (int y = 0; y <= 127; y++) {
			Block b = primer.getBlockState(x, y, z).getBlock();

			// add stone
			if (y < topHeight && (b == Blocks.AIR || b == Blocks.WATER)) {
				primer.setBlockState(x, y, z, Blocks.STONE.getDefaultState());
			}

			// hollow out inside
			if (y > hollowFloor && y < hollowCeiling) {
				primer.setBlockState(x, y, z, Blocks.AIR.getDefaultState());
			}

			// ice floor
			if (y == hollowFloor && y < hollowCeiling && y < TFWorld.SEALEVEL + 3) {
				primer.setBlockState(x, y, z, Blocks.PACKED_ICE.getDefaultState());
			}
		}
	}

	private void deformTerrainForTrollCloud2(ChunkPrimer primer, TFFeature nearFeature, int cx, int cz, int hx, int hz) {
		for (int bx = 0; bx < 4; bx++) {
			for (int bz = 0; bz < 4; bz++) {
				int dx = (bx * 4) - hx - 2;
				int dz = (bz * 4) - hz - 2;

				// generate several centers for other clouds
				int regionX = (cx + 8) >> 4;
				int regionZ = (cz + 8) >> 4;

				long seed = (long) (regionX * 3129871) ^ (long) regionZ * 116129781L;
				seed = seed * seed * 42317861L + seed * 7L;

				int num0 = (int) (seed >> 12 & 3L);
				int num1 = (int) (seed >> 15 & 3L);
				int num2 = (int) (seed >> 18 & 3L);
				int num3 = (int) (seed >> 21 & 3L);
				int num4 = (int) (seed >> 9 & 3L);
				int num5 = (int) (seed >> 6 & 3L);
				int num6 = (int) (seed >> 3 & 3L);
				int num7 = (int) (seed >> 0 & 3L);

				int dx2 = dx + (num0 * 5) - (num1 * 4);
				int dz2 = dz + (num2 * 4) - (num3 * 5);
				int dx3 = dx + (num4 * 5) - (num5 * 4);
				int dz3 = dz + (num6 * 4) - (num7 * 5);

				// take the minimum distance to any center
				double dist0 = Math.sqrt(dx * dx + dz * dz) / 4.0;
				double dist2 = Math.sqrt(dx2 * dx2 + dz2 * dz2) / 3.5;
				double dist3 = Math.sqrt(dx3 * dx3 + dz3 * dz3) / 4.5;

				double dist = Math.min(dist0, Math.min(dist2, dist3));

				float pr = world.rand.nextFloat();
				double cv = (dist - 7F) - (pr * 3.0F);

				// randomize depth and height
				int y = 166;
				int depth = 4;

				if (pr < 0.1F) {
					y++;
				}
				if (pr > 0.6F) {
					depth++;
				}
				if (pr > 0.9F) {
					depth++;
				}

				// generate cloud
				for (int sx = 0; sx < 4; sx++) {
					for (int sz = 0; sz < 4; sz++) {
						int lx = bx * 4 + sx;
						int lz = bz * 4 + sz;

						if (dist < 7 || cv < 0.05F) {

							primer.setBlockState(lx, y, lz, TFBlocks.wispy_cloud.getDefaultState());
							for (int d = 1; d < depth; d++) {
								primer.setBlockState(lx, y - d, lz, TFBlocks.fluffy_cloud.getDefaultState());
							}
							primer.setBlockState(lx, y - depth, lz, TFBlocks.wispy_cloud.getDefaultState());
						} else if (dist < 8 || cv < 1F) {
							for (int d = 1; d < depth; d++) {
								primer.setBlockState(lx, y - d, lz, TFBlocks.fluffy_cloud.getDefaultState());
							}
						}
					}
				}
			}
		}
	}

	protected final boolean allowSurfaceLakes(Biome biome) {
		if (biome.decorator instanceof TFBiomeDecorator) {
			return !((TFBiomeDecorator) biome.decorator).hasCanopy;
		}
		return true;
	}

	public final boolean shouldGenerateBedrock() {
		return shouldGenerateBedrock;
	}

	@Override
	public boolean generateStructures(Chunk chunk, int x, int z) {
		return false;
	}

	/**
	 * Returns a list of creatures of the specified type that can spawn at the
	 * given location.
	 * <p>
	 * Twilight Forest variant! First check features, then only if we're not in
	 * a feature, check the biome.
	 */
	@Override
	public List<SpawnListEntry> getPossibleCreatures(EnumCreatureType creatureType, BlockPos pos) {
		// are the specified coordinates precisely in a feature?
		TFFeature nearestFeature = TFFeature.getFeatureForRegion(pos.getX() >> 4, pos.getZ() >> 4, world);

		if (nearestFeature != TFFeature.nothing) {
			// if the feature is already conquered, no spawns
			if (this.isStructureConquered(pos)) {
				return null;
			}

			// check the precise coords.
			int spawnListIndex = this.majorFeatureGenerator.getSpawnListIndexAt(pos);
			if (spawnListIndex >= 0) {
				return nearestFeature.getSpawnableList(creatureType, spawnListIndex);
			}
		}

		Biome biome = world.getBiome(pos);

		if (pos.getY() < TFWorld.SEALEVEL && creatureType == EnumCreatureType.MONSTER && biome instanceof TFBiomeBase) {
			// cave monsters!
			return ((TFBiomeBase) biome).getUndergroundSpawnableList();
		} else {
			return biome.getSpawnableList(creatureType);
		}
	}

	@Nullable
	@Override
	public BlockPos getNearestStructurePos(World world, String structureName, BlockPos position, boolean findUnexplored) {
		TFFeature feature = TFFeature.getFeatureByName(new ResourceLocation(structureName));
		if (feature != TFFeature.nothing) {
			return TFFeature.findNearestFeaturePosBySpacing(world, feature, position, 20, 11, 10387313, true, 100, findUnexplored);
		}
		return null;
	}

	public void setStructureConquered(int mapX, int mapY, int mapZ, boolean flag) {
		this.majorFeatureGenerator.setStructureConquered(mapX, mapY, mapZ, flag);
	}

	public boolean isStructureLocked(BlockPos pos, int lockIndex) {
		return this.majorFeatureGenerator.isStructureLocked(pos, lockIndex);
	}

	public boolean isBlockInStructureBB(BlockPos pos) {
		return this.majorFeatureGenerator.isInsideStructure(pos);
	}

	public StructureBoundingBox getSBBAt(BlockPos pos) {
		return this.majorFeatureGenerator.getSBBAt(pos);
	}

	public boolean isBlockProtected(BlockPos pos) {
		return this.majorFeatureGenerator.isBlockProtectedAt(pos);
	}

	public boolean isStructureConquered(BlockPos pos) {
		return this.majorFeatureGenerator.isStructureConquered(pos);
	}

	public boolean isBlockInFullStructure(int x, int z) {
		return this.majorFeatureGenerator.isBlockInFullStructure(x, z);
	}

	public boolean isBlockNearFullStructure(int x, int z, int range) {
		return this.majorFeatureGenerator.isBlockNearFullStructure(x, z, range);
	}

	public StructureBoundingBox getFullSBBAt(int mapX, int mapZ) {
		return this.majorFeatureGenerator.getFullSBBAt(mapX, mapZ);
	}

	public StructureBoundingBox getFullSBBNear(int mapX, int mapZ, int range) {
		return this.majorFeatureGenerator.getFullSBBNear(mapX, mapZ, range);
	}

	public TFFeature getFeatureAt(BlockPos pos) {
		return majorFeatureGenerator.getFeatureAt(pos);
	}

	@Override
	public void recreateStructures(Chunk chunk, int x, int z) {
		majorFeatureGenerator.generate(world, x, z, null);
	}

	@Override
	public boolean isInsideStructure(World world, String structureName, BlockPos pos) {
		TFFeature feature = TFFeature.getFeatureByName(new ResourceLocation(structureName));
		return feature != TFFeature.nothing && getFeatureAt(pos) == feature;
	}
}
