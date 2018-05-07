// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package twilightforest.world;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldEntitySpawner;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.SpawnListEntry;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.NoiseGeneratorOctaves;
import net.minecraft.world.gen.NoiseGeneratorPerlin;
import net.minecraft.world.gen.feature.WorldGenDungeons;
import net.minecraft.world.gen.feature.WorldGenLakes;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import twilightforest.TFConfig;
import twilightforest.TFFeature;
import twilightforest.biomes.TFBiomeBase;
import twilightforest.biomes.TFBiomes;
import twilightforest.block.TFBlocks;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

// Referenced classes of package net.minecraft.src:
//            IChunkProvider, MapGenCaves, MapGenStronghold, MapGenVillage, 
//            MapGenMineshaft, MapGenRavine, NoiseGeneratorOctaves, World, 
//            WorldChunkManager, Block, Biome, Chunk,
//            MapGenBase, MathHelper, BlockSand, WorldGenLakes, 
//            WorldGenDungeons, SpawnerAnimals, IProgressUpdate
// todo 1.10 doc out all the vanilla copying
public class ChunkGeneratorTwilightForest implements IChunkGenerator {
	private final Random rand;
	private NoiseGeneratorOctaves minLimitPerlinNoise;
	private NoiseGeneratorOctaves maxLimitPerlinNoise;
	private NoiseGeneratorOctaves mainPerlinNoise;
	private NoiseGeneratorPerlin surfaceNoise;
	private NoiseGeneratorOctaves noiseGen4;
	//private NoiseGeneratorOctaves scaleNoise;
	private NoiseGeneratorOctaves depthNoise;
	//private NoiseGeneratorOctaves forestNoise;
	private final World world;
	private WorldType terrainType;
	private final double[] heightMap;
	private final float[] biomeWeights;
	private double[] depthBuffer = new double[256];
	private Biome biomesForGeneration[];
	private double[] mainNoiseRegion;
	private double[] minLimitRegion;
	private double[] maxLimitRegion;
	private double[] depthRegion;

	private final TFGenCaves caveGenerator = new TFGenCaves();
	private final TFGenRavine ravineGenerator = new TFGenRavine();
	private final MapGenTFMajorFeature majorFeatureGenerator = new MapGenTFMajorFeature();
	private final MapGenTFHollowTree hollowTreeGenerator = new MapGenTFHollowTree();

	public ChunkGeneratorTwilightForest(World world, long l, boolean flag) {
		this.world = world;
		this.terrainType = world.getWorldInfo().getTerrainType();
		this.rand = new Random(l);
		this.minLimitPerlinNoise = new NoiseGeneratorOctaves(this.rand, 16);
		this.maxLimitPerlinNoise = new NoiseGeneratorOctaves(this.rand, 16);
		this.mainPerlinNoise = new NoiseGeneratorOctaves(this.rand, 8);
		this.surfaceNoise = new NoiseGeneratorPerlin(this.rand, 4);
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
	public Chunk generateChunk(int cx, int cz) {
		rand.setSeed(cx * 0x4f9939f508L + cz * 0x1ef1565bd5L);
		ChunkPrimer primer = new ChunkPrimer();
		setBlocksInChunk(cx, cz, primer);

		squishTerrain(primer);

		// Dark Forest canopy uses the different scaled biomesForGeneration value already set in setBlocksInChunk
		addDarkForestCanopy2(cx, cz, primer);

		// now we reload the biome array so that it's scaled 1:1 with blocks on the ground
		this.biomesForGeneration = world.getBiomeProvider().getBiomes(biomesForGeneration, cx * 16, cz * 16, 16, 16);
		addGlaciers(cx, cz, primer, biomesForGeneration);
		deformTerrainForFeature(cx, cz, primer);
		replaceBiomeBlocks(cx, cz, primer, biomesForGeneration);
		caveGenerator.generate(world, cx, cz, primer);
		ravineGenerator.generate(world, cx, cz, primer);
		majorFeatureGenerator.generate(world, cx, cz, primer);
		hollowTreeGenerator.generate(world, cx, cz, primer);

		Chunk chunk = new Chunk(world, primer, cx, cz);

		// load in biomes, to prevent striping?!
		byte[] chunkBiomes = chunk.getBiomeArray();
		for (int i = 0; i < chunkBiomes.length; ++i) {
			chunkBiomes[i] = (byte) Biome.getIdForBiome(this.biomesForGeneration[i]);
		}

		chunk.generateSkylightMap();

		return chunk;
	}

	public void setBlocksInChunk(int chunkX, int chunkZ, ChunkPrimer primer) {
		byte seaLevel = 63;
		this.biomesForGeneration = this.world.getBiomeProvider().getBiomesForGeneration(this.biomesForGeneration, chunkX * 4 - 2, chunkZ * 4 - 2, 10, 10);
		this.generateHeightmap(chunkX * 4, 0, chunkZ * 4);

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
				Biome biomegenbase = this.biomesForGeneration[ax + 2 + (az + 2) * 10];

				for (int ox = -two; ox <= two; ++ox) {
					for (int oz = -two; oz <= two; ++oz) {
						Biome biomegenbase1 = this.biomesForGeneration[ax + ox + 2 + (az + oz + 2) * 10];
						float rootHeight = biomegenbase1.getBaseHeight();
						float heightVariation = biomegenbase1.getHeightVariation();

						if (this.terrainType == WorldType.AMPLIFIED && rootHeight > 0.0F) {
							rootHeight = 1.0F + rootHeight * 2.0F;
							heightVariation = 1.0F + heightVariation * 4.0F;
						}

						float heightFactor = this.biomeWeights[ox + 2 + (oz + 2) * 5] / (rootHeight + 2.0F);

						if (biomegenbase1.getBaseHeight() > biomegenbase.getBaseHeight()) {
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

	// [VanillaCopy] Exact, ChunkGeneratorOverworld.replaceBiomeBlocks
	public void replaceBiomeBlocks(int x, int z, ChunkPrimer primer, Biome[] biomesIn)
	{
		if (!net.minecraftforge.event.ForgeEventFactory.onReplaceBiomeBlocks(this, x, z, primer, this.world)) return;
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
	private void deformTerrainForFeature(int cx, int cz, ChunkPrimer primer) {
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
		for (int y = 0; y < TFWorld.CHUNKHEIGHT; y++) {
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

		for (int y = 0; y < TFWorld.CHUNKHEIGHT; y++) {
			// put a base on hills that go over open space or water
			if (hillHeight > 0 && y < TFWorld.SEALEVEL && primer.getBlockState(x, y, z).getBlock() != Blocks.STONE) {
				primer.setBlockState(x, y, z, Blocks.STONE.getDefaultState());
			}
			// hollow out the hollow parts
			int hollowFloor = TFWorld.SEALEVEL - 3 - (hollow / 8);
			if (nearFeature == TFFeature.hydraLair) {
				// different floor
				hollowFloor = TFWorld.SEALEVEL;
			}
			if (y > hollowFloor && y < hollowFloor + hollow) {
				primer.setBlockState(x, y, z, Blocks.AIR.getDefaultState());
			}
		}
	}

	private void flattenTerrainForFeature(ChunkPrimer primer, TFFeature nearFeature, int x, int z, int dx, int dz) {
		int oldGround;
		int newGround;
		float squishfactor = 0;
		int mazeheight = TFWorld.SEALEVEL + 1;
		final int FEATUREBOUNDRY = (nearFeature.size * 2 + 1) * 8 - 8;

		if (dx <= -FEATUREBOUNDRY) {
			squishfactor = (-dx - FEATUREBOUNDRY) / 8.0f;
		}

		if (dx >= FEATUREBOUNDRY) {
			squishfactor = (dx - FEATUREBOUNDRY) / 8.0f;
		}
		if (dz <= -FEATUREBOUNDRY) {
			squishfactor = Math.max(squishfactor, (-dz - FEATUREBOUNDRY) / 8.0f);
		}

		if (dz >= FEATUREBOUNDRY) {
			squishfactor = Math.max(squishfactor, (dz - FEATUREBOUNDRY) / 8.0f);
		}

		if (squishfactor > 0) {
			// blend the old terrain height to arena height
			newGround = -1;

			for (int y = 0; y <= 127; y++) {
				Block currentTerrain = primer.getBlockState(x, y, z).getBlock();
				// we're still in ground
				if (currentTerrain != Blocks.STONE) {
					if (newGround == -1) {
						// we found the lowest chunk of earth
						oldGround = y;
						mazeheight += ((oldGround - mazeheight) * squishfactor);

						newGround = oldGround;
					}
				}
			}
		}

		// sets the groundlevel to the mazeheight
		for (int y = 0; y <= 127; y++) {
			Block b = primer.getBlockState(x, y, z).getBlock();
			if (y < mazeheight && (b == Blocks.AIR || b == Blocks.WATER)) {
				primer.setBlockState(x, y, z, Blocks.STONE.getDefaultState());
			}
			if (y >= mazeheight && b != Blocks.WATER) {
				primer.setBlockState(x, y, z, Blocks.AIR.getDefaultState());
			}
		}
	}

	private void deformTerrainForYetiLair(ChunkPrimer primer, TFFeature nearFeature, int x, int z, int dx, int dz) {
		int oldGround;
		int newGround;
		float squishfactor = 0;
		int topHeight = TFWorld.SEALEVEL + 24;
		int outerBoundry = (nearFeature.size * 2 + 1) * 8 - 8;

		// outer boundry
		if (dx <= -outerBoundry) {
			squishfactor = (-dx - outerBoundry) / 8.0f;
		}

		if (dx >= outerBoundry) {
			squishfactor = (dx - outerBoundry) / 8.0f;
		}
		if (dz <= -outerBoundry) {
			squishfactor = Math.max(squishfactor, (-dz - outerBoundry) / 8.0f);
		}

		if (dz >= outerBoundry) {
			squishfactor = Math.max(squishfactor, (dz - outerBoundry) / 8.0f);
		}

		// inner boundry 
		int caveBoundry = (nearFeature.size * 2) * 8 - 8;
		int hollowCeiling = TFWorld.SEALEVEL + 16;

		int offset = Math.min(Math.abs(dx), Math.abs(dz));
		hollowCeiling = (TFWorld.SEALEVEL + 40) - (offset * 4);

		// center square cave
		if (dx >= -caveBoundry && dz >= -caveBoundry && dx <= caveBoundry && dz <= caveBoundry) {
			hollowCeiling = TFWorld.SEALEVEL + 16;
		}

		// slope ceiling slightly
		hollowCeiling -= (offset / 6);

		// max out ceiling 8 blocks from roof
		hollowCeiling = Math.min(hollowCeiling, TFWorld.SEALEVEL + 16);

		// floor, also with slight slope
		int hollowFloor = TFWorld.SEALEVEL - 1 + (offset / 6);

		if (squishfactor > 0) {
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
						topHeight += ((oldGround - topHeight) * squishfactor);

						hollowFloor += ((oldGround - hollowFloor) * squishfactor);

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

	private void addGlaciers(int chunkX, int chunkZ, ChunkPrimer primer, Biome biomes[]) {
		IBlockState ice = TFConfig.performance.glacierPackedIce ? Blocks.PACKED_ICE.getDefaultState() : Blocks.ICE.getDefaultState();

		for (int z = 0; z < 16; z++) {
			for (int x = 0; x < 16; x++) {
				Biome biome = biomes[x & 15 | (z & 15) << 4];
				if (biome == TFBiomes.glacier) {
					// find the (current) top block
					int topLevel = -1;
					for (int y = 127; y >= 0; y--) {
						Block currentBlock = primer.getBlockState(x, y, z).getBlock();
						if (currentBlock == Blocks.STONE) {
							topLevel = y;
							primer.setBlockState(x, y, z, Blocks.GRAVEL.getDefaultState());
							break;
						}
					}

					// raise the glacier from that top block
					int gHeight = 32;
					int gTop = topLevel + gHeight + 1;

					for (int y = topLevel + 1; y <= gTop && y < 128; y++) {
						primer.setBlockState(x, y, z, ice);
					}
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
	public void populate(int chunkX, int chunkZ) {
		BlockFalling.fallInstantly = true;
		int i = chunkX * 16;
		int j = chunkZ * 16;
		BlockPos blockpos = new BlockPos(i, 0, j);
		Biome biome = this.world.getBiome(blockpos.add(16, 0, 16));
		this.rand.setSeed(this.world.getSeed());
		long k = this.rand.nextLong() / 2L * 2L + 1L;
		long l = this.rand.nextLong() / 2L * 2L + 1L;
		this.rand.setSeed((long)chunkX * k + (long)chunkZ * l ^ this.world.getSeed());
		boolean flag = false;
		ChunkPos chunkpos = new ChunkPos(chunkX, chunkZ);

		net.minecraftforge.event.ForgeEventFactory.onChunkPopulate(true, this, this.world, this.rand, chunkX, chunkZ, flag);

		boolean disableFeatures = this.majorFeatureGenerator.generateStructure(world, rand, chunkpos)
				|| !TFFeature.getNearestFeature(chunkX, chunkZ, world).areChunkDecorationsEnabled;

		hollowTreeGenerator.generateStructure(world, rand, chunkpos);

		if (!disableFeatures && rand.nextInt(4) == 0 && biome.decorator.generateFalls) {
			int i1 = blockpos.getX() + rand.nextInt(16) + 8;
			int i2 = rand.nextInt(TFWorld.CHUNKHEIGHT);
			int i3 = blockpos.getZ() + rand.nextInt(16) + 8;
			(new WorldGenLakes(Blocks.WATER)).generate(world, rand, new BlockPos(i1, i2, i3));
		}

		if (!disableFeatures && rand.nextInt(32) == 0) // reduced from 8
		{
			int j1 = blockpos.getX() + rand.nextInt(16) + 8;
			int j2 = rand.nextInt(rand.nextInt(TFWorld.CHUNKHEIGHT - 8) + 8);
			int j3 = blockpos.getZ() + rand.nextInt(16) + 8;
			if (j2 < TFWorld.SEALEVEL || rand.nextInt(10) == 0) {
				(new WorldGenLakes(Blocks.LAVA)).generate(world, rand, new BlockPos(j1, j2, j3));
			}
		}
		for (int k1 = 0; k1 < 8; k1++) {
			int k2 = blockpos.getX() + rand.nextInt(16) + 8;
			int k3 = rand.nextInt(TFWorld.CHUNKHEIGHT);
			int l3 = blockpos.getZ() + rand.nextInt(16) + 8;
			(new WorldGenDungeons()).generate(world, rand, new BlockPos(k2, k3, l3));
		}

		biome.decorate(this.world, this.rand, new BlockPos(i, 0, j));
		if (net.minecraftforge.event.terraingen.TerrainGen.populate(this, this.world, this.rand, chunkX, chunkZ, flag, net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate.EventType.ANIMALS))
			WorldEntitySpawner.performWorldGenSpawning(this.world, biome, i + 8, j + 8, 16, 16, this.rand);
		blockpos = blockpos.add(8, 0, 8);

		if (net.minecraftforge.event.terraingen.TerrainGen.populate(this, this.world, this.rand, chunkX, chunkZ, flag, net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate.EventType.ICE))
		{
			for (int k2 = 0; k2 < 16; ++k2)
			{
				for (int j3 = 0; j3 < 16; ++j3)
				{
					BlockPos blockpos1 = this.world.getPrecipitationHeight(blockpos.add(k2, 0, j3));
					BlockPos blockpos2 = blockpos1.down();

					if (this.world.canBlockFreezeWater(blockpos2))
					{
						this.world.setBlockState(blockpos2, Blocks.ICE.getDefaultState(), 2);
					}

					if (this.world.canSnowAt(blockpos1, true))
					{
						this.world.setBlockState(blockpos1, Blocks.SNOW_LAYER.getDefaultState(), 2);
					}
				}
			}
		}//Forge: End ICE

		net.minecraftforge.event.ForgeEventFactory.onChunkPopulate(false, this, this.world, this.rand, chunkX, chunkZ, flag);

		BlockFalling.fallInstantly = false;
	}

	@Override
	public boolean generateStructures(Chunk chunkIn, int x, int z) {
		return false;
	}

	/**
	 * Returns a list of creatures of the specified type that can spawn at the
	 * given location.
	 * <p>
	 * Twilight Forest varient! First check features, then only if we're not in
	 * a feature, check the biome.
	 */
	@Override
	public List<SpawnListEntry> getPossibleCreatures(EnumCreatureType creatureType, BlockPos pos) {
		// are the specified coordinates precicely in a feature?
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
	public BlockPos getNearestStructurePos(World worldIn, String structureName, BlockPos position, boolean findUnexplored) {
		if(structureName.equalsIgnoreCase(hollowTreeGenerator.getStructureName()))
			return hollowTreeGenerator.getNearestStructurePos(worldIn, position, findUnexplored);
		TFFeature feature = TFFeature.getFeatureByName(new ResourceLocation(structureName));
		if (feature != TFFeature.nothing)
			return TFFeature.findNearestFeaturePosBySpacing(worldIn, feature, position, 20, 11, 10387313, true, 100, findUnexplored);
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
	public void recreateStructures(Chunk chunk, int var1, int var2) {
		majorFeatureGenerator.generate(world, var1, var2, null);
		hollowTreeGenerator.generate(world, var1, var2, null);
	}

	@Override
	public boolean isInsideStructure(World worldIn, String structureName, BlockPos pos) {
		TFFeature feature = TFFeature.getFeatureByName(new ResourceLocation(structureName));
		return structureName.equalsIgnoreCase(hollowTreeGenerator.getStructureName()) ? hollowTreeGenerator.isInsideStructure(pos) : feature != null && feature != TFFeature.nothing && getFeatureAt(pos) == feature;
	}
}
