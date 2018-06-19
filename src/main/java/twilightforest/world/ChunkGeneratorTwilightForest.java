package twilightforest.world;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldEntitySpawner;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.NoiseGeneratorOctaves;
import net.minecraft.world.gen.feature.WorldGenDungeons;
import net.minecraft.world.gen.feature.WorldGenLakes;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.event.terraingen.TerrainGen;
import twilightforest.TFConfig;
import twilightforest.TFFeature;
import twilightforest.biomes.TFBiomes;
import twilightforest.block.TFBlocks;

import javax.annotation.Nullable;

// TODO: doc out all the vanilla copying
public class ChunkGeneratorTwilightForest extends ChunkGeneratorTFBase {

	private final NoiseGeneratorOctaves minLimitPerlinNoise;
	private final NoiseGeneratorOctaves maxLimitPerlinNoise;
	private final NoiseGeneratorOctaves mainPerlinNoise;
	private final NoiseGeneratorOctaves noiseGen4;
	//private final NoiseGeneratorOctaves scaleNoise;
	private final NoiseGeneratorOctaves depthNoise;
	//private final NoiseGeneratorOctaves forestNoise;

	private final double[] heightMap;
	private final float[] biomeWeights;

	private double[] mainNoiseRegion;
	private double[] minLimitRegion;
	private double[] maxLimitRegion;
	private double[] depthRegion;

	private final TFGenCaves caveGenerator = new TFGenCaves();
	private final TFGenRavine ravineGenerator = new TFGenRavine();
	private final MapGenTFHollowTree hollowTreeGenerator = new MapGenTFHollowTree();

	public ChunkGeneratorTwilightForest(World world, long seed, boolean enableFeatures) {
		super(world, seed, enableFeatures, true);
		this.minLimitPerlinNoise = new NoiseGeneratorOctaves(this.rand, 16);
		this.maxLimitPerlinNoise = new NoiseGeneratorOctaves(this.rand, 16);
		this.mainPerlinNoise = new NoiseGeneratorOctaves(this.rand, 8);
		this.noiseGen4 = new NoiseGeneratorOctaves(rand, 4);
		//this.scaleNoise = new NoiseGeneratorOctaves(rand, 10);
		this.depthNoise = new NoiseGeneratorOctaves(rand, 16);
		//this.forestNoise = new NoiseGeneratorOctaves(rand, 8);
		this.heightMap = new double[825];
		this.biomeWeights = new float[25];

		for (int j = -2; j <= 2; ++j) {
			for (int k = -2; k <= 2; ++k) {
				float f = 10.0F / MathHelper.sqrt((float) (j * j + k * k) + 0.2F);
				this.biomeWeights[j + 2 + (k + 2) * 5] = f;
			}
		}
	}

	@Override
	public Chunk generateChunk(int x, int z) {
		rand.setSeed(getSeed(x, z));
		ChunkPrimer primer = new ChunkPrimer();
		setBlocksInChunk(x, z, primer);

		squishTerrain(primer);

		// Dark Forest canopy uses the different scaled biomesForGeneration value already set in setBlocksInChunk
		addDarkForestCanopy2(x, z, primer);

		// now we reload the biome array so that it's scaled 1:1 with blocks on the ground
		this.biomesForGeneration = world.getBiomeProvider().getBiomes(biomesForGeneration, x * 16, z * 16, 16, 16);
		addGlaciers(x, z, primer, biomesForGeneration);
		deformTerrainForFeature(x, z, primer);
		replaceBiomeBlocks(x, z, primer, biomesForGeneration);
		caveGenerator.generate(world, x, z, primer);
		ravineGenerator.generate(world, x, z, primer);
		majorFeatureGenerator.generate(world, x, z, primer);
		hollowTreeGenerator.generate(world, x, z, primer);

		return makeChunk(x, z, primer);
	}

	public void setBlocksInChunk(int x, int z, ChunkPrimer primer) {
		byte seaLevel = 63;
		this.biomesForGeneration = this.world.getBiomeProvider().getBiomesForGeneration(this.biomesForGeneration, x * 4 - 2, z * 4 - 2, 10, 10);
		this.generateHeightmap(x * 4, 0, z * 4);

		for (int k = 0; k < 4; ++k) {
			int l = k * 5;
			int i1 = (k + 1) * 5;

			for (int j1 = 0; j1 < 4; ++j1) {
				int k1 = (l + j1) * 33;
				int l1 = (l + j1 + 1) * 33;
				int i2 = (i1 + j1) * 33;
				int j2 = (i1 + j1 + 1) * 33;

				for (int k2 = 0; k2 < 32; ++k2) {
					double d0 = 0.125D;
					double d1 = this.heightMap[k1 + k2];
					double d2 = this.heightMap[l1 + k2];
					double d3 = this.heightMap[i2 + k2];
					double d4 = this.heightMap[j2 + k2];
					double d5 = (this.heightMap[k1 + k2 + 1] - d1) * d0;
					double d6 = (this.heightMap[l1 + k2 + 1] - d2) * d0;
					double d7 = (this.heightMap[i2 + k2 + 1] - d3) * d0;
					double d8 = (this.heightMap[j2 + k2 + 1] - d4) * d0;

					for (int l2 = 0; l2 < 8; ++l2) {
						double d9 = 0.25D;
						double d10 = d1;
						double d11 = d2;
						double d12 = (d3 - d1) * d9;
						double d13 = (d4 - d2) * d9;

						for (int i3 = 0; i3 < 4; ++i3) {
							double d14 = 0.25D;
							double d16 = (d11 - d10) * d14;
							double d15 = d10 - d16;

							for (int k3 = 0; k3 < 4; ++k3) {
								if ((d15 += d16) > 0.0D) {
									primer.setBlockState(k * 4 + i3, k2 * 8 + l2, j1 * 4 + k3, Blocks.STONE.getDefaultState());
								} else if (k2 * 8 + l2 < seaLevel) {
									primer.setBlockState(k * 4 + i3, k2 * 8 + l2, j1 * 4 + k3, Blocks.WATER.getDefaultState());
								}
							}

							d10 += d12;
							d11 += d13;
						}

						d1 += d5;
						d2 += d6;
						d3 += d7;
						d4 += d8;
					}
				}
			}
		}
	}

	private void generateHeightmap(int x, int zero, int z) {

		this.depthRegion = this.depthNoise.generateNoiseOctaves(this.depthRegion, x, z, 5, 5, 200.0D, 200.0D, 0.5D);
		this.mainNoiseRegion = this.mainPerlinNoise.generateNoiseOctaves(this.mainNoiseRegion, x, zero, z, 5, 33, 5, 8.555150000000001D, 4.277575000000001D, 8.555150000000001D);
		this.minLimitRegion = this.minLimitPerlinNoise.generateNoiseOctaves(this.minLimitRegion, x, zero, z, 5, 33, 5, 684.412D, 684.412D, 684.412D);
		this.maxLimitRegion = this.maxLimitPerlinNoise.generateNoiseOctaves(this.maxLimitRegion, x, zero, z, 5, 33, 5, 684.412D, 684.412D, 684.412D);
		int terrainIndex = 0;
		int noiseIndex = 0;

		for (int ax = 0; ax < 5; ++ax) {
			for (int az = 0; az < 5; ++az) {
				float totalVariation = 0.0F;
				float totalHeight = 0.0F;
				float totalFactor = 0.0F;
				byte two = 2;
				Biome biome = this.biomesForGeneration[ax + 2 + (az + 2) * 10];

				for (int ox = -two; ox <= two; ++ox) {
					for (int oz = -two; oz <= two; ++oz) {
						Biome biome1 = this.biomesForGeneration[ax + ox + 2 + (az + oz + 2) * 10];
						float rootHeight = biome1.getBaseHeight();
						float heightVariation = biome1.getHeightVariation();

						if (this.terrainType == WorldType.AMPLIFIED && rootHeight > 0.0F) {
							rootHeight = 1.0F + rootHeight * 2.0F;
							heightVariation = 1.0F + heightVariation * 4.0F;
						}

						float heightFactor = this.biomeWeights[ox + 2 + (oz + 2) * 5] / (rootHeight + 2.0F);

						if (biome1.getBaseHeight() > biome.getBaseHeight()) {
							heightFactor /= 2.0F;
						}

						totalVariation += heightVariation * heightFactor;
						totalHeight += rootHeight * heightFactor;
						totalFactor += heightFactor;
					}
				}

				totalVariation /= totalFactor;
				totalHeight /= totalFactor;
				totalVariation = totalVariation * 0.9F + 0.1F;
				totalHeight = (totalHeight * 4.0F - 1.0F) / 8.0F;
				double terrainNoise = this.depthRegion[noiseIndex] / 8000.0D;

				if (terrainNoise < 0.0D) {
					terrainNoise = -terrainNoise * 0.3D;
				}

				terrainNoise = terrainNoise * 3.0D - 2.0D;

				if (terrainNoise < 0.0D) {
					terrainNoise /= 2.0D;

					if (terrainNoise < -1.0D) {
						terrainNoise = -1.0D;
					}

					terrainNoise /= 1.4D;
					terrainNoise /= 2.0D;
				} else {
					if (terrainNoise > 1.0D) {
						terrainNoise = 1.0D;
					}

					terrainNoise /= 8.0D;
				}

				++noiseIndex;
				double heightCalc = (double) totalHeight;
				double variationCalc = (double) totalVariation;
				heightCalc += terrainNoise * 0.2D;
				heightCalc = heightCalc * 8.5D / 8.0D;
				double d5 = 8.5D + heightCalc * 4.0D;

				for (int ay = 0; ay < 33; ++ay) {
					double d6 = ((double) ay - d5) * 12.0D * 128.0D / 256.0D / variationCalc;

					if (d6 < 0.0D) {
						d6 *= 4.0D;
					}

					double d7 = this.minLimitRegion[terrainIndex] / 512.0D;
					double d8 = this.maxLimitRegion[terrainIndex] / 512.0D;
					double d9 = (this.mainNoiseRegion[terrainIndex] / 10.0D + 1.0D) / 2.0D;
					double terrainCalc = MathHelper.clampedLerp(d7, d8, d9) - d6;

					if (ay > 29) {
						double d11 = (double) ((float) (ay - 29) / 3.0F);
						terrainCalc = terrainCalc * (1.0D - d11) + -10.0D * d11;
					}

					this.heightMap[terrainIndex] = terrainCalc;
					++terrainIndex;
				}
			}
		}
	}

	/**
	 * Crush the terrain to half the height
	 */
	private void squishTerrain(ChunkPrimer primer) {
		int squishHeight = TFWorld.MAXHEIGHT / 2;

		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {
				for (int y = 0; y < TFWorld.CHUNKHEIGHT; y++) {
					if (y < squishHeight) {
						primer.setBlockState(x, y, z, primer.getBlockState(x, y * 2 + 1, z));
					} else {
						primer.setBlockState(x, y, z, Blocks.AIR.getDefaultState());
					}
				}
			}
		}
	}

	private void addGlaciers(int chunkX, int chunkZ, ChunkPrimer primer, Biome[] biomes) {

		IBlockState glacierBase = Blocks.GRAVEL.getDefaultState();
		IBlockState glacierMain = TFConfig.performance.glacierPackedIce ? Blocks.PACKED_ICE.getDefaultState() : Blocks.ICE.getDefaultState();
		IBlockState glacierTop = Blocks.ICE.getDefaultState();

		for (int z = 0; z < 16; z++) {
			for (int x = 0; x < 16; x++) {
				Biome biome = biomes[x & 15 | (z & 15) << 4];
				if (biome == TFBiomes.glacier) {
					// find the (current) top block
					int gBase = -1;
					for (int y = 127; y >= 0; y--) {
						Block currentBlock = primer.getBlockState(x, y, z).getBlock();
						if (currentBlock == Blocks.STONE) {
							gBase = y + 1;
							primer.setBlockState(x, y, z, glacierBase);
							break;
						}
					}

					// raise the glacier from that top block
					int gHeight = 32;
					int gTop = Math.min(gBase + gHeight, 127);

					for (int y = gBase; y < gTop; y++) {
						primer.setBlockState(x, y, z, glacierMain);
					}
					primer.setBlockState(x, gTop, z, glacierTop);
				}
			}
		}
	}

	/**
	 * Adds dark forest canopy.  This version uses the "unzoomed" array of biomes used in land generation to determine how many of the nearby blocks are dark forest
	 */
	private void addDarkForestCanopy2(int chunkX, int chunkZ, ChunkPrimer primer) {
		int[] thicks = new int[5 * 5];

		for (int z = 0; z < 5; z++) {
			for (int x = 0; x < 5; x++) {

				for (int bx = -1; bx <= 1; bx++) {
					for (int bz = -1; bz <= 1; bz++) {
						Biome biome = biomesForGeneration[x + bx + 2 + (z + bz + 2) * (10)];

						if (biome == TFBiomes.darkForest || biome == TFBiomes.darkForestCenter) {
							thicks[x + z * 5]++;
						}
					}
				}
			}
		}

		for (int z = 0; z < 16; z++) {
			for (int x = 0; x < 16; x++) {

				int qx = x / 4;
				int qz = z / 4;

				float xweight = (x % 4) * 0.25F + 0.125F;
				float zweight = (z % 4) * 0.25F + 0.125F;

				float thickness = 0;

				thickness += thicks[qx + (qz) * 5] * (1F - xweight) * (1F - zweight);
				thickness += thicks[qx + 1 + (qz) * 5] * (xweight) * (1F - zweight);
				thickness += thicks[qx + (qz + 1) * 5] * (1F - xweight) * (zweight);
				thickness += thicks[qx + 1 + (qz + 1) * 5] * (xweight) * (zweight);

				thickness -= 4;

				//int thickness = thicks[qz + (qz) * 5];

				// make sure we're not too close to the tower
				TFFeature nearFeature = TFFeature.getNearestFeature(chunkX, chunkZ, world);
				if (nearFeature == TFFeature.darkTower) {
					// check for closeness
					int[] nearCenter = TFFeature.getNearestCenter(chunkX, chunkZ, world);
					int hx = nearCenter[0];
					int hz = nearCenter[1];

					int dx = x - hx;
					int dz = z - hz;
					int dist = (int) Math.sqrt(dx * dx + dz * dz);

					if (dist < 24) {

						thickness -= (24 - dist);
					}
				}

				boolean generateForest = thickness > 1;

				if (generateForest) {
					double d = 0.03125D;
					depthBuffer = noiseGen4.generateNoiseOctaves(depthBuffer, chunkX * 16, chunkZ * 16, 0, 16, 16, 1, d * 2D, d * 2D, d * 2D);

					// find the (current) top block
					int topLevel = -1;
					for (int y = 127; y >= 0; y--) {
						Block currentBlock = primer.getBlockState(x, y, z).getBlock();
						if (currentBlock == Blocks.WATER) {
							// don't generate over water
							break;
						}
						if (currentBlock == Blocks.STONE) {
							topLevel = y;
							break;
						}
					}

					if (topLevel != -1) {
						// just use the same noise generator as the terrain uses
						// for stones
						int noise = Math.min(3, (int) (depthBuffer[z & 15 | (x & 15) << 4] / 1.25f));

						// manipulate top and bottom
						int treeBottom = topLevel + 12 - (int) (thickness * 0.5F);
						int treeTop = treeBottom + (int) (thickness * 1.5F);

						treeBottom -= noise;

						for (int y = treeBottom; y < treeTop; y++) {
							primer.setBlockState(x, y, z, TFBlocks.dark_leaves.getDefaultState());
						}
					}
				}
			}
		}
	}

	@Override
	public void populate(int x, int z) {

		BlockFalling.fallInstantly = true;

		int i = x * 16;
		int j = z * 16;
		BlockPos blockpos = new BlockPos(i, 0, j);
		Biome biome = this.world.getBiome(blockpos.add(16, 0, 16));
		this.rand.setSeed(this.world.getSeed());
		long k = this.rand.nextLong() / 2L * 2L + 1L;
		long l = this.rand.nextLong() / 2L * 2L + 1L;
		this.rand.setSeed((long)x * k + (long)z * l ^ this.world.getSeed());
		boolean flag = false;
		ChunkPos chunkpos = new ChunkPos(x, z);

		ForgeEventFactory.onChunkPopulate(true, this, this.world, this.rand, x, z, flag);

		boolean disableFeatures = this.majorFeatureGenerator.generateStructure(world, rand, chunkpos)
				|| !TFFeature.getNearestFeature(x, z, world).areChunkDecorationsEnabled;

		hollowTreeGenerator.generateStructure(world, rand, chunkpos);

		if (!disableFeatures && rand.nextInt(4) == 0) {
			if (TerrainGen.populate(this, this.world, this.rand, x, x, flag, PopulateChunkEvent.Populate.EventType.LAKE)) {
				int i1 = blockpos.getX() + rand.nextInt(16) + 8;
				int i2 = rand.nextInt(TFWorld.CHUNKHEIGHT);
				int i3 = blockpos.getZ() + rand.nextInt(16) + 8;
				if (i2 < TFWorld.SEALEVEL || allowSurfaceLakes(biome)) {
					(new WorldGenLakes(Blocks.WATER)).generate(world, rand, new BlockPos(i1, i2, i3));
				}
			}
		}

		if (!disableFeatures && rand.nextInt(32) == 0) { // reduced from 8
			if (TerrainGen.populate(this, this.world, this.rand, x, z, flag, PopulateChunkEvent.Populate.EventType.LAVA)) {
				int j1 = blockpos.getX() + rand.nextInt(16) + 8;
				int j2 = rand.nextInt(rand.nextInt(TFWorld.CHUNKHEIGHT - 8) + 8);
				int j3 = blockpos.getZ() + rand.nextInt(16) + 8;
				if (j2 < TFWorld.SEALEVEL || allowSurfaceLakes(biome) && rand.nextInt(10) == 0) {
					(new WorldGenLakes(Blocks.LAVA)).generate(world, rand, new BlockPos(j1, j2, j3));
				}
			}
		}

		if (TerrainGen.populate(this, this.world, this.rand, x, z, flag, PopulateChunkEvent.Populate.EventType.DUNGEON)) {
			for (int k1 = 0; k1 < 8; k1++) {
				int k2 = blockpos.getX() + rand.nextInt(16) + 8;
				int k3 = rand.nextInt(TFWorld.CHUNKHEIGHT);
				int l3 = blockpos.getZ() + rand.nextInt(16) + 8;
				(new WorldGenDungeons()).generate(world, rand, new BlockPos(k2, k3, l3));
			}
		}

		biome.decorate(this.world, this.rand, new BlockPos(i, 0, j));

		if (TerrainGen.populate(this, this.world, this.rand, x, z, flag, PopulateChunkEvent.Populate.EventType.ANIMALS)) {
			WorldEntitySpawner.performWorldGenSpawning(this.world, biome, i + 8, j + 8, 16, 16, this.rand);
		}

		blockpos = blockpos.add(8, 0, 8);

		if (TerrainGen.populate(this, this.world, this.rand, x, z, flag, PopulateChunkEvent.Populate.EventType.ICE)) {
			for (int k2 = 0; k2 < 16; ++k2) {
				for (int j3 = 0; j3 < 16; ++j3) {

					BlockPos blockpos1 = this.world.getPrecipitationHeight(blockpos.add(k2, 0, j3));
					BlockPos blockpos2 = blockpos1.down();

					if (this.world.canBlockFreezeWater(blockpos2)) {
						this.world.setBlockState(blockpos2, Blocks.ICE.getDefaultState(), 2);
					}

					if (this.world.canSnowAt(blockpos1, true)) {
						this.world.setBlockState(blockpos1, Blocks.SNOW_LAYER.getDefaultState(), 2);
					}
				}
			}
		}//Forge: End ICE

		ForgeEventFactory.onChunkPopulate(false, this, this.world, this.rand, x, z, flag);

		BlockFalling.fallInstantly = false;
	}

	@Nullable
	@Override
	public BlockPos getNearestStructurePos(World world, String structureName, BlockPos position, boolean findUnexplored) {
		if (structureName.equalsIgnoreCase(hollowTreeGenerator.getStructureName())) {
			return hollowTreeGenerator.getNearestStructurePos(world, position, findUnexplored);
		}
		return super.getNearestStructurePos(world, structureName, position, findUnexplored);
	}

	@Override
	public void recreateStructures(Chunk chunk, int x, int z) {
		super.recreateStructures(chunk, x, z);
		hollowTreeGenerator.generate(world, x, z, null);
	}

	@Override
	public boolean isInsideStructure(World world, String structureName, BlockPos pos) {
		if (structureName.equalsIgnoreCase(hollowTreeGenerator.getStructureName())) {
			return hollowTreeGenerator.isInsideStructure(pos);
		}
		return super.isInsideStructure(world, structureName, pos);
	}
}
