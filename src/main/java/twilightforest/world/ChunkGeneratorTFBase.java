package twilightforest.world;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityClassification;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.SpawnListEntry;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.chunk.ChunkSection;
import net.minecraft.world.gen.NoiseChunkGenerator;
import net.minecraft.world.gen.OctavesNoiseGenerator;
import net.minecraft.world.gen.WorldGenRegion;
import net.minecraft.world.spawner.WorldEntitySpawner;
import twilightforest.TFFeature;
import twilightforest.biomes.TFBiomeBase;
import twilightforest.block.TFBlocks;
import twilightforest.util.IntPair;

import javax.annotation.Nullable;
import java.util.List;

// TODO: doc out all the vanilla copying
public abstract class ChunkGeneratorTFBase extends NoiseChunkGenerator<TFWorld> {

	private static final float[] field_222576_h = Util.make(new float[25], (afloat) -> {
		for(int i = -2; i <= 2; ++i) {
			for(int j = -2; j <= 2; ++j) {
				float f = 10.0F / MathHelper.sqrt((float)(i * i + j * j) + 0.2F);
				afloat[i + 2 + (j + 2) * 5] = f;
			}
		}

	});
	private final OctavesNoiseGenerator depthNoise;
	private final boolean isAmplified;
	private boolean shouldGenerateBedrock = true;
//	protected final Map<TFFeature, MapGenTFMajorFeature> featureGenerators = new EnumMap<>(TFFeature.class);
//	protected final MapGenTFMajorFeature nothingGenerator = new MapGenTFMajorFeature();

	public ChunkGeneratorTFBase(IWorld world, BiomeProvider provider, TFWorld settings, boolean shouldGenerateBedrock) {
		this(world, provider, settings);

		this.shouldGenerateBedrock = shouldGenerateBedrock;
	}

	public ChunkGeneratorTFBase(IWorld world, BiomeProvider provider, TFWorld settings) {
		super(world, provider, 4, 8, 256, settings, true);
		this.randomSeed.skip(2620);
		this.depthNoise = new OctavesNoiseGenerator(this.randomSeed, 15, 0);
		this.isAmplified = world.getWorldInfo().getGenerator() == WorldType.AMPLIFIED;
	}

	@Override
	public void spawnMobs(WorldGenRegion region) {
		int i = region.getMainChunkX();
		int j = region.getMainChunkZ();
		Biome biome = region.getBiome((new ChunkPos(i, j)).asBlockPos());
		SharedSeedRandom sharedseedrandom = new SharedSeedRandom();
		sharedseedrandom.setDecorationSeed(region.getSeed(), i << 4, j << 4);
		WorldEntitySpawner.performWorldGenSpawning(region, biome, i, j, sharedseedrandom);
	}

	@Override
	protected void fillNoiseColumn(double[] noiseY, int noiseX, int noiseZ) {
		double d0 = (double)684.412F;
		double d1 = (double)684.412F;
		double d2 = 8.555149841308594D;
		double d3 = 4.277574920654297D;
		int i = -10;
		int j = 3;
		this.func_222546_a(noiseY, noiseX, noiseZ, d0, d1, d2, d3, j, i);
	}

	@Override
	protected double func_222545_a(double depth, double noise, int y) {
		double d1 = ((double)y - (8.5D + depth * 8.5D / 8.0D * 4.0D)) * 12.0D * 128.0D / 256.0D / noise;
		if (d1 < 0.0D) {
			d1 *= 4.0D;
		}

		return d1;
	}

	@Override
	protected double[] getBiomeNoiseColumn(int x, int z) {
		double[] adouble = new double[2];
		float f = 0.0F;
		float f1 = 0.0F;
		float f2 = 0.0F;
		int j = this.getSeaLevel();
		float f3 = this.biomeProvider.getBiomeForNoiseGen(x, j, z).getDepth();

		for(int k = -2; k <= 2; ++k) {
			for(int l = -2; l <= 2; ++l) {
				Biome biome = this.biomeProvider.getBiomeForNoiseGen(x + k, j, z + l);
				float f4 = biome.getDepth();
				float f5 = biome.getScale();
				if (this.isAmplified && f4 > 0.0F) {
					f4 = 1.0F + f4 * 2.0F;
					f5 = 1.0F + f5 * 4.0F;
				}

				float f6 = field_222576_h[k + 2 + (l + 2) * 5] / (f4 + 2.0F);
				if (biome.getDepth() > f3) {
					f6 /= 2.0F;
				}

				f += f5 * f6;
				f1 += f4 * f6;
				f2 += f6;
			}
		}

		f = f / f2;
		f1 = f1 / f2;
		f = f * 0.9F + 0.1F;
		f1 = (f1 * 4.0F - 1.0F) / 8.0F;
		adouble[0] = (double)f1 + this.getNoiseDepthAt(x, z);
		adouble[1] = (double)f;
		return adouble;
	}

	private double getNoiseDepthAt(int p_222574_1_, int p_222574_2_) {
		double d0 = this.depthNoise.getValue((double)(p_222574_1_ * 200), 10.0D, (double)(p_222574_2_ * 200), 1.0D, 0.0D, true) * 65535.0D / 8000.0D;
		if (d0 < 0.0D) {
			d0 = -d0 * 0.3D;
		}

		d0 = d0 * 3.0D - 2.0D;
		if (d0 < 0.0D) {
			d0 = d0 / 28.0D;
		} else {
			if (d0 > 1.0D) {
				d0 = 1.0D;
			}

			d0 = d0 / 40.0D;
		}

		return d0;
	}

	//TODO: Biome Decorator
//	protected final void generateFeatures(int x, int z, ChunkPrimer primer) {
//		for (MapGenTFMajorFeature generator : featureGenerators.values()) {
//			generator.place(world, settings, x, z, primer);
//		}
//	}

	protected final Chunk makeChunk(int x, int z, ChunkPrimer primer) {

		Chunk chunk = new Chunk(world.getWorld(), primer);

		fillChunk(chunk, primer);

		/*// load in biomes, to prevent striping?!
		byte[] chunkBiomes = chunk.getBiomeArray();
		for (int i = 0; i < chunkBiomes.length; ++i) {
			chunkBiomes[i] = (byte) Biome.getIdForBiome(this.biomesForGeneration[i]);
		}

		chunk.generateSkylightMap();*/

		return chunk;
	}

	// [VanillaCopy] Extended Chunk constructor, material check replaced with block check
	private void fillChunk(Chunk chunk, ChunkPrimer primer) {

//		int i = 256;
//		boolean flag = world.getDimension().hasSkyLight();
		ChunkSection[] storageArrays = chunk.getSections();

		for (int j = 0; j < 16; ++j) {
			for (int k = 0; k < 16; ++k) {
				for (int l = 0; l < 256; ++l) {

					BlockState iblockstate = primer.getBlockState(new BlockPos(j, l, k));

					if (iblockstate.getBlock() != Blocks.AIR) {

						int i1 = l >> 4;

						if (storageArrays[i1] == Chunk.EMPTY_SECTION) {
							storageArrays[i1] = new ChunkSection(i1 << 4/*, flag*/);
						}

						storageArrays[i1].setBlockState(j, l & 15, k, iblockstate);
					}
				}
			}
		}
	}

	/**
	 * Returns a list of creatures of the specified type that can spawn at the
	 * given location.
	 * <p>
	 * Twilight Forest variant! First check features, then only if we're not in
	 * a feature, check the biome.
	 */
	@Override
	public List<SpawnListEntry> getPossibleCreatures(EntityClassification creatureType, BlockPos pos) {
		// are the specified coordinates precisely in a feature?
		TFFeature nearestFeature = TFFeature.getFeatureForRegionPos(pos.getX(), pos.getZ(), world.getWorld());

//		List<SpawnListEntry> featureList = getFeatureGenerator(nearestFeature).getPossibleCreatures(creatureType, pos);
//		if (featureList != null) {
//			return featureList;
//		}

		Biome biome = world.getBiome(pos);

		if (pos.getY() < TFWorld.SEALEVEL && biome instanceof TFBiomeBase) {
			// cave monsters!
			return ((TFBiomeBase) biome).getUndergroundSpawnableList(creatureType);
		} else {
			return biome.getSpawns(creatureType);
		}
	}

//	protected final MapGenTFMajorFeature getFeatureGenerator(TFFeature feature) {
//		return featureGenerators.getOrDefault(feature, nothingGenerator);
//	}

	@Override
	public int getGroundHeight() {
		return this.world.getSeaLevel() + 1;
	}

	protected static int getIndex(int x, int y, int z) {
		return x << 12 | z << 8 | y;
	}

	/**
	 * Crush the terrain to half the height
	 */
	protected final void squishTerrain(ChunkBitArray data) {
		int squishHeight = TFWorld.MAXHEIGHT / 2;
		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {
				for (int y = 0; y < squishHeight; y++) {
					data.set(getIndex(x, y, z), data.get(getIndex(x, y * 2 + 1, z)));
				}
				for (int y = squishHeight; y < TFWorld.CHUNKHEIGHT; y++) {
					data.clear(getIndex(x, y, z));
				}
			}
		}
	}

	protected final void deformTerrainForFeature(int cx, int cz, WorldGenRegion primer) {

		IntPair nearCenter = new IntPair();
		TFFeature nearFeature = TFFeature.getNearestFeature(cx, cz, world.getWorld(), nearCenter);

		if (!nearFeature.isTerrainAltered) {
			return;
		}

		int hx = nearCenter.x;
		int hz = nearCenter.z;

		if (nearFeature == TFFeature.TROLL_CAVE) {
			// troll cloud, more like
			deformTerrainForTrollCloud2(primer, nearFeature, cx, cz, hx, hz);
		}

		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {

				int dx = x - hx;
				int dz = z - hz;

				if (nearFeature == TFFeature.SMALL_HILL || nearFeature == TFFeature.MEDIUM_HILL || nearFeature == TFFeature.LARGE_HILL || nearFeature == TFFeature.HYDRA_LAIR) {
					// hollow hills
					int hdiam = ((nearFeature.size * 2 + 1) * 16);
					int dist = (int) Math.sqrt(dx * dx + dz * dz);
					int hheight = (int) (Math.cos((float) dist / (float) hdiam * Math.PI) * ((float) hdiam / 3F));

					raiseHills(primer, nearFeature, hdiam, x, z, dx, dz, hheight);

				} else if (nearFeature == TFFeature.HEDGE_MAZE || nearFeature == TFFeature.NAGA_COURTYARD || nearFeature == TFFeature.QUEST_GROVE) {
					// hedge mazes, naga arena
					flattenTerrainForFeature(primer, nearFeature, x, z, dx, dz);

				} else if (nearFeature == TFFeature.YETI_CAVE) {
					// yeti lairs are square
					deformTerrainForYetiLair(primer, nearFeature, x, z, dx, dz);

				} else if (nearFeature == TFFeature.TROLL_CAVE) {
					deformTerrainForTrollCaves(primer, nearFeature, x, z, dx, dz);
				}
				//else if (nearFeature != TFFeature.NOTHING) {
				//	// hedge mazes, naga arena
				//	flattenTerrainForFeature(primer, nearFeature, x, z, dx, dz);
				//}
			}
		}

		// done!
	}

	protected void deformTerrainForTrollCaves(WorldGenRegion primer, TFFeature nearFeature, int x, int z, int dx, int dz) {}

	//TODO: Parameter "nearFeature" is unused. Remove?
	private void deformTerrainForTrollCloud2(WorldGenRegion primer, TFFeature nearFeature, int cx, int cz, int hx, int hz) {
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

				float pr = world.getRandom().nextFloat();
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

							primer.setBlockState(new BlockPos(lx, y, lz), TFBlocks.wispy_cloud.get().getDefaultState(), 3);
							for (int d = 1; d < depth; d++) {
								primer.setBlockState(new BlockPos(lx, y - d, lz), TFBlocks.fluffy_cloud.get().getDefaultState(), 3);
							}
							primer.setBlockState(new BlockPos(lx, y - depth, lz), TFBlocks.wispy_cloud.get().getDefaultState(), 3);
						} else if (dist < 8 || cv < 1F) {
							for (int d = 1; d < depth; d++) {
								primer.setBlockState(new BlockPos(lx, y - d, lz), TFBlocks.fluffy_cloud.get().getDefaultState(), 3);
							}
						}
					}
				}
			}
		}
	}

	/**
	 * Raises up and hollows out the hollow hills.
	 */
	private void raiseHills(WorldGenRegion primer, TFFeature nearFeature, int hdiam, int x, int z, int dx, int dz, int hillHeight) {

		int oldGround = -1;
		int newGround = -1;
		boolean foundGroundLevel = false;

		// raise the hill
		for (int y = TFWorld.SEALEVEL; y < TFWorld.CHUNKHEIGHT; y++) {
			Block currentTerrain = primer.getBlockState(new BlockPos(x, y, z)).getBlock();
			if (currentTerrain != Blocks.STONE) {
				// we found the top of the stone layer
				oldGround = y;
				newGround = y + hillHeight;
				foundGroundLevel = true;
				break;
			}
		}

		if (foundGroundLevel) {
			for (int y = oldGround; y <= newGround; y++) {
				primer.setBlockState(new BlockPos(x, y, z), Blocks.STONE.getDefaultState(), 3);
			}
		}

		// add the hollow part. Also turn water into stone below that
		int hollow = hillHeight - 4 - nearFeature.size;

		// hydra lair has a piece missing
		if (nearFeature == TFFeature.HYDRA_LAIR) {
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
		if (nearFeature == TFFeature.HYDRA_LAIR) {
			// different floor
			hollowFloor = TFWorld.SEALEVEL;
		}

		if (hillHeight > 0) {
			// put a base on hills that go over open space or water
			for (int y = 0; y < TFWorld.SEALEVEL; y++) {
				if (primer.getBlockState(new BlockPos(x, y, z)).getBlock() != Blocks.STONE) {
					primer.setBlockState(new BlockPos(x, y, z), Blocks.STONE.getDefaultState(), 3);
				}
			}
		}

		for (int y = hollowFloor + 1; y < hollowFloor + hollow; y++) {
			primer.setBlockState(new BlockPos(x, y, z), Blocks.AIR.getDefaultState(), 3);
		}
	}

	private void flattenTerrainForFeature(WorldGenRegion primer, TFFeature nearFeature, int x, int z, int dx, int dz) {

		float squishFactor = 0f;
		int mazeHeight = TFWorld.SEALEVEL + 1;
		final int FEATURE_BOUNDARY = (nearFeature.size * 2 + 1) * 8 - 8;

		if (dx <= -FEATURE_BOUNDARY) {
			squishFactor = (-dx - FEATURE_BOUNDARY) / 8.0f;
		} else if (dx >= FEATURE_BOUNDARY) {
			squishFactor = (dx - FEATURE_BOUNDARY) / 8.0f;
		}

		if (dz <= -FEATURE_BOUNDARY) {
			squishFactor = Math.max(squishFactor, (-dz - FEATURE_BOUNDARY) / 8.0f);
		} else if (dz >= FEATURE_BOUNDARY) {
			squishFactor = Math.max(squishFactor, (dz - FEATURE_BOUNDARY) / 8.0f);
		}

		if (squishFactor > 0f) {
			// blend the old terrain height to arena height
			for (int y = 0; y <= 127; y++) {
				Block currentTerrain = primer.getBlockState(new BlockPos(x, y, z)).getBlock();
				// we're still in ground
				if (currentTerrain != Blocks.STONE) {
					// we found the lowest chunk of earth
					mazeHeight += ((y - mazeHeight) * squishFactor);
					break;
				}
			}
		}

		// sets the ground level to the maze height
		for (int y = 0; y < mazeHeight; y++) {
			Block b = primer.getBlockState(new BlockPos(x, y, z)).getBlock();
			if (b == Blocks.AIR || b == Blocks.WATER) {
				primer.setBlockState(new BlockPos(x, y, z), Blocks.STONE.getDefaultState(), 3);
			}
		}
		for (int y = mazeHeight; y <= 127; y++) {
			Block b = primer.getBlockState(new BlockPos(x, y, z)).getBlock();
			if (b != Blocks.AIR && b != Blocks.WATER) {
				primer.setBlockState(new BlockPos(x, y, z), Blocks.AIR.getDefaultState(), 3);
			}
		}
	}

	private void deformTerrainForYetiLair(WorldGenRegion primer, TFFeature nearFeature, int x, int z, int dx, int dz) {

		float squishFactor = 0f;
		int topHeight = TFWorld.SEALEVEL + 24;
		int outerBoundary = (nearFeature.size * 2 + 1) * 8 - 8;

		// outer boundary
		if (dx <= -outerBoundary) {
			squishFactor = (-dx - outerBoundary) / 8.0f;
		} else if (dx >= outerBoundary) {
			squishFactor = (dx - outerBoundary) / 8.0f;
		}

		if (dz <= -outerBoundary) {
			squishFactor = Math.max(squishFactor, (-dz - outerBoundary) / 8.0f);
		} else if (dz >= outerBoundary) {
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

		if (squishFactor > 0f) {
			// blend the old terrain height to arena height
			for (int y = 0; y <= 127; y++) {
				Block currentTerrain = primer.getBlockState(new BlockPos(x, y, z)).getBlock();
				if (currentTerrain != Blocks.STONE) {
					// we found the lowest chunk of earth
					topHeight += ((y - topHeight) * squishFactor);
					hollowFloor += ((y - hollowFloor) * squishFactor);
					break;
				}
			}
		}

		// carve the cave into the stone

		// add stone
		for (int y = 0; y < topHeight; y++) {
			Block b = primer.getBlockState(new BlockPos(x, y, z)).getBlock();
			if (b == Blocks.AIR || b == Blocks.WATER) {
				primer.setBlockState(new BlockPos(x, y, z), Blocks.STONE.getDefaultState(), 3);
			}
		}

		// hollow out inside
		for (int y = hollowFloor + 1; y < hollowCeiling; ++y) {
			primer.setBlockState(new BlockPos(x, y, z), Blocks.AIR.getDefaultState(), 3);
		}

		// ice floor
		if (hollowFloor < hollowCeiling && hollowFloor < TFWorld.SEALEVEL + 3) {
			primer.setBlockState(new BlockPos(x, hollowFloor, z), Blocks.PACKED_ICE.getDefaultState(), 3);
		}
	}

	public final boolean shouldGenerateBedrock() {
		return shouldGenerateBedrock;
	}

	@Nullable
	@Override
	public BlockPos findNearestStructure(World world, String structureName, BlockPos position, int range, boolean findUnexplored) {
        //TODO: we need to readd this next time
		/*if (structureName.equalsIgnoreCase(hollowTreeGenerator.getStructureName())) {
			return hollowTreeGenerator.findNearest(world, this, position, range, findUnexplored);
		}*/
		TFFeature feature = TFFeature.getFeatureByName(new ResourceLocation(structureName));
		if (feature != TFFeature.NOTHING) {
			return TFFeature.findNearestFeaturePosBySpacing(world, feature, position, 20, 11, 10387313, true, 100, findUnexplored);
		}
		return null;
	}

//	public void setStructureConquered(BlockPos pos, boolean flag) {
//		getFeatureGenerator(TFFeature.getFeatureForRegionPos(pos.getX(), pos.getZ(), world.getWorld())).setStructureConquered(pos, flag);
//	}
//
//	public boolean isStructureLocked(BlockPos pos, int lockIndex) {
//		return getFeatureGenerator(TFFeature.getFeatureForRegionPos(pos.getX(), pos.getZ(), world.getWorld())).isStructureLocked(pos, lockIndex);
//	}
//
//	//TODO: Verify replaced method
//	public boolean isBlockInStructureBB(BlockPos pos) {
//		return getFeatureGenerator(TFFeature.getFeatureForRegionPos(pos.getX(), pos.getZ(), world.getWorld())).isPositionInStructure(world, pos);
//	}
//
//	@Nullable
//	public MutableBoundingBox getSBBAt(BlockPos pos) {
//		return getFeatureGenerator(TFFeature.getFeatureForRegionPos(pos.getX(), pos.getZ(), world.getWorld())).getSBBAt(pos);
//	}
//
//	public boolean isBlockProtected(BlockPos pos) {
//		return getFeatureGenerator(TFFeature.getFeatureForRegionPos(pos.getX(), pos.getZ(), world.getWorld())).isBlockProtectedAt(pos);
//	}
//
//	public boolean isStructureConquered(BlockPos pos) {
//		return getFeatureGenerator(TFFeature.getFeatureForRegionPos(pos.getX(), pos.getZ(), world.getWorld())).isStructureConquered(pos);
//	}
//
//	public boolean isBlockInFullStructure(int x, int z) {
//		return getFeatureGenerator(TFFeature.getFeatureForRegionPos(x, z, world.getWorld())).isBlockInFullStructure(x, z);
//	}
//
//	@Nullable
//	public MutableBoundingBox getFullSBBNear(int mapX, int mapZ, int range) {
//		return getFeatureGenerator(TFFeature.getFeatureForRegionPos(mapX, mapZ, world.getWorld())).getFullSBBNear(mapX, mapZ, range);
//	}
}
