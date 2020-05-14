package twilightforest.world;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.OctavesNoiseGenerator;
import net.minecraft.world.gen.WorldGenRegion;
import twilightforest.TFConfig;
import twilightforest.biomes.TFBiomes;

// TODO: doc out all the vanilla copying
public class ChunkGeneratorTwilightForest extends ChunkGeneratorTFBase {

	private final OctavesNoiseGenerator noiseGen4;
	//private final OctavesNoiseGenerator scaleNoise;
	//private final OctavesNoiseGenerator forestNoise;

//	private final TFGenCaves caveGenerator = new TFGenCaves();
//	private final TFGenRavine ravineGenerator = new TFGenRavine();

	public ChunkGeneratorTwilightForest(IWorld world, BiomeProvider seed, TFWorld settings) {
		super(world, seed, settings, true);
		this.noiseGen4 = new OctavesNoiseGenerator(this.randomSeed, 4, 0);
		//this.scaleNoise = new OctavesNoiseGenerator(rand, 10);
		//this.forestNoise = new OctavesNoiseGenerator(rand, 8);
	}

	@Override
	public void decorate(WorldGenRegion region) {
		super.decorate(region);
		int x = region.getMainChunkX();
		int z = region.getMainChunkZ();

		randomSeed.setSeed(getSeed());

		ChunkBitArray data = new ChunkBitArray();
		//func_222529_a(x, z, data);
		squishTerrain(data);

		ChunkPrimer primer = new DirectChunkPrimer();
		initPrimer(primer, data);

		// Dark Forest canopy uses the different scaled biomesForGeneration value already set in setBlocksInChunk
		//addDarkForestCanopy2(x, z, primer); TODO: Should be moved to Biome Decorator or...?

		// now we reload the biome array so that it's scaled 1:1 with blocks on the ground
		//this.biomesForGeneration = world.getDimension().getBiomeProvider().getBiomes(biomesForGeneration, x * 16, z * 16, 16, 16);

		//addGlaciers(x, z, primer, biomesForGeneration);
		deformTerrainForFeature(x, z, primer);
		//replaceBiomeBlocks(x, z, primer, biomesForGeneration);

//		caveGenerator.generate(world, x, z, primer); TODO: Handled in Biome carvers
//		ravineGenerator.generate(world, x, z, primer); TODO: Handled in Biome carvers
//		generateFeatures(x, z, primer); TODO: Should be moved to Biome Decorator
//		hollowTreeGenerator.generate(world, x, z, primer); TODO: Should be handled via Biome Decorator

		makeChunk(x, z, primer);
	}

	protected void initPrimer(ChunkPrimer primer, ChunkBitArray data) {

		//TODO: Handled in TFWorld now
//		BlockState water = Blocks.WATER.getDefaultState();
//		BlockState stone = Blocks.STONE.getDefaultState();

		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {
				for (int y = 0; y < 256; y++) {
					boolean solid = data.get(getIndex(x, y, z));
					if (y < TFWorld.SEALEVEL && !solid) {
						primer.setBlockState(new BlockPos(x, y, z), settings.getDefaultBlock(), false);
					} else if (solid) {
						primer.setBlockState(new BlockPos(x, y, z), settings.getDefaultFluid(), false);
					}
				}
			}
		}
	}

	//TODO: Parameters "chunkX" and "chunkZ" are unused. Remove?
	private void addGlaciers(int chunkX, int chunkZ, ChunkPrimer primer, Biome[] biomes) {

		BlockState glacierBase = Blocks.GRAVEL.getDefaultState();
		BlockState glacierMain = TFConfig.COMMON_CONFIG.PERFORMANCE.glacierPackedIce.get() ? Blocks.PACKED_ICE.getDefaultState() : Blocks.ICE.getDefaultState();
		BlockState glacierTop = Blocks.ICE.getDefaultState();

		for (int z = 0; z < 16; z++) {
			for (int x = 0; x < 16; x++) {

				Biome biome = biomes[x & 15 | (z & 15) << 4];
				if (biome != TFBiomes.glacier.get()) continue;

				// find the (current) top block
				int gBase = -1;
				for (int y = 127; y >= 0; y--) {
					Block currentBlock = primer.getBlockState(new BlockPos(x, y, z)).getBlock();
					if (currentBlock == Blocks.STONE) {
						gBase = y + 1;
						primer.setBlockState(new BlockPos(x, y, z), glacierBase, false);
						break;
					}
				}

				// raise the glacier from that top block
				int gHeight = 32;
				int gTop = Math.min(gBase + gHeight, 127);

				for (int y = gBase; y < gTop; y++) {
					primer.setBlockState(new BlockPos(x, y, z), glacierMain, false);
				}
				primer.setBlockState(new BlockPos(x, gTop, z), glacierTop, false);
			}
		}
	}

	/**
	 * Adds dark forest canopy.  This version uses the "unzoomed" array of biomes used in land generation to determine how many of the nearby blocks are dark forest
	 */
	/*private void addDarkForestCanopy2(int chunkX, int chunkZ, ChunkPrimer primer) {

		int[] thicks = new int[5 * 5];
		boolean biomeFound = false;

		for (int z = 0; z < 5; z++) {
			for (int x = 0; x < 5; x++) {

				for (int bx = -1; bx <= 1; bx++) {
					for (int bz = -1; bz <= 1; bz++) {
						Biome biome = biomesForGeneration[x + bx + 2 + (z + bz + 2) * (10)];

						if (biome == TFBiomes.darkForest.get() || biome == TFBiomes.darkForestCenter.get()) {
							thicks[x + z * 5]++;
							biomeFound = true;
						}
					}
				}
			}
		}

		if (!biomeFound) return;

		IntPair nearCenter = new IntPair();
		TFFeature nearFeature = TFFeature.getNearestFeature(chunkX, chunkZ, world, nearCenter);

		double d = 0.03125D;
		depthBuffer = noiseGen4.generateNoiseOctaves(depthBuffer, chunkX * 16, chunkZ * 16, 0, 16, 16, 1, d * 2D, d * 2D, d * 2D);

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
				if (nearFeature == TFFeature.DARK_TOWER) {

					int hx = nearCenter.x;
					int hz = nearCenter.z;

					int dx = x - hx;
					int dz = z - hz;
					int dist = (int) Math.sqrt(dx * dx + dz * dz);

					if (dist < 24) {
						thickness -= (24 - dist);
					}
				}

				if (thickness > 1) {
					// find the (current) top block
					int topLevel = -1;
					for (int y = 127; y >= 0; y--) {
						Block currentBlock = primer.getBlockState(new BlockPos(x, y, z)).getBlock();
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
						// just use the same noise generator as the terrain uses for stones
						int noise = Math.min(3, (int) (depthBuffer[z & 15 | (x & 15) << 4] / 1.25f));

						// manipulate top and bottom
						int treeBottom = topLevel + 12 - (int) (thickness * 0.5F);
						int treeTop = treeBottom + (int) (thickness * 1.5F);

						treeBottom -= noise;

						BlockState darkLeaves = TFBlocks.dark_leaves.get().getDefaultState();
						for (int y = treeBottom; y < treeTop; y++) {
							primer.setBlockState(new BlockPos(x, y, z), darkLeaves, false);
						}
					}
				}
			}
		}
	}*/

//	@Override
//	public void decorate(WorldGenRegion region) {
	//BlockFalling.fallInstantly = true;

//		int i = x * 16;
//		int j = z * 16;
//		BlockPos blockpos = new BlockPos(i, 0, j);
//		Biome biome = this.world.getBiome(blockpos.add(16, 0, 16));
//		this.rand.setSeed(this.world.getSeed());
//		long k = this.rand.nextLong() / 2L * 2L + 1L;
//		long l = this.rand.nextLong() / 2L * 2L + 1L;
//		this.rand.setSeed((long)x * k + (long)z * l ^ this.world.getSeed());
//		boolean flag = false;
//		ChunkPos chunkpos = new ChunkPos(x, z);

//		ForgeEventFactory.onChunkPopulate(true, this, this.world, this.rand, x, z, flag);

//		boolean disableFeatures = false;
//
//		for (MapGenTFMajorFeature generator : featureGenerators.values()) {
//			if (generator.generateStructure(world, rand, chunkpos)) {
//				disableFeatures = true;
//			}
//		}

	//disableFeatures = disableFeatures || !TFFeature.getNearestFeature(x, z, world).areChunkDecorationsEnabled;

	//TODO: Move to Biome Decorator
	//hollowTreeGenerator.generateStructure(world, rand, chunkpos);

//		if (!disableFeatures && rand.nextInt(4) == 0) {
//			if (TerrainGen.populate(this, this.world, this.rand, x, x, flag, PopulateChunkEvent.Populate.EventType.LAKE)) {
//				int i1 = blockpos.getX() + rand.nextInt(16) + 8;
//				int i2 = rand.nextInt(TFWorld.CHUNKHEIGHT);
//				int i3 = blockpos.getZ() + rand.nextInt(16) + 8;
//				if (i2 < TFWorld.SEALEVEL || allowSurfaceLakes(biome)) {
//					(new WorldGenLakes(Blocks.WATER)).generate(world, rand, new BlockPos(i1, i2, i3));
//				}
//			}
//		}

//		if (!disableFeatures && rand.nextInt(32) == 0) { // reduced from 8
//			if (TerrainGen.populate(this, this.world, this.rand, x, z, flag, PopulateChunkEvent.Populate.EventType.LAVA)) {
//				int j1 = blockpos.getX() + rand.nextInt(16) + 8;
//				int j2 = rand.nextInt(rand.nextInt(TFWorld.CHUNKHEIGHT - 8) + 8);
//				int j3 = blockpos.getZ() + rand.nextInt(16) + 8;
//				if (j2 < TFWorld.SEALEVEL || allowSurfaceLakes(biome) && rand.nextInt(10) == 0) {
//					(new WorldGenLakes(Blocks.LAVA)).generate(world, rand, new BlockPos(j1, j2, j3));
//				}
//			}
//		}

	//TODO: Handled via BiomeDecorator
//		if (TerrainGen.populate(this, this.world, this.rand, x, z, flag, PopulateChunkEvent.Populate.EventType.DUNGEON)) {
//			for (int k1 = 0; k1 < 8; k1++) {
//				int k2 = blockpos.getX() + rand.nextInt(16) + 8;
//				int k3 = rand.nextInt(TFWorld.CHUNKHEIGHT);
//				int l3 = blockpos.getZ() + rand.nextInt(16) + 8;
//				(new WorldGenDungeons()).generate(world, rand, new BlockPos(k2, k3, l3));
//			}
//		}

	//TODO: Handled via BiomeDecorator
//		biome.decorate(this.world, this.rand, new BlockPos(i, 0, j));

	//TODO: Handled already
//		if (TerrainGen.populate(this, this.world, this.rand, x, z, flag, PopulateChunkEvent.Populate.EventType.ANIMALS)) {
//			WorldEntitySpawner.performWorldGenSpawning(this.world, biome, i + 8, j + 8, 16, 16, this.rand);
//		}

//		blockpos = blockpos.add(8, 0, 8);

//		if (TerrainGen.populate(this, this.world, this.rand, x, z, flag, PopulateChunkEvent.Populate.EventType.ICE)) {
//			for (int k2 = 0; k2 < 16; ++k2) {
//				for (int j3 = 0; j3 < 16; ++j3) {
//
//					BlockPos blockpos1 = this.world.getPrecipitationHeight(blockpos.add(k2, 0, j3));
//					BlockPos blockpos2 = blockpos1.down();
//
//					//TODO: Handled via SurfaceBuilder
////					if (this.world.canBlockFreezeWater(blockpos2)) {
////						this.world.setBlockState(blockpos2, Blocks.ICE.getDefaultState(), 16 | 2);
////					}
//
//					//TODO: Apparently this is a Feature now?
////					if (this.world.canSnowAt(blockpos1, true)) {
////						this.world.setBlockState(blockpos1, Blocks.SNOW_LAYER.getDefaultState(), 16 | 2);
////					}
//				}
//			}
//		}//Forge: End ICE

//		ForgeEventFactory.onChunkPopulate(false, this, this.world, this.rand, x, z, flag);

	//BlockFalling.fallInstantly = false;
//	}

	//TODO: See super
//	@Override
//	public void recreateStructures(Chunk chunk, int x, int z) {
//		super.recreateStructures(chunk, x, z);
//		hollowTreeGenerator.generate(world, x, z, null);
//	}
}
