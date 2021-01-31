package twilightforest.world;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.math.SectionPos;
import net.minecraft.world.Blockreader;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.chunk.ChunkSection;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.*;
import net.minecraft.world.gen.feature.jigsaw.JigsawJunction;
import net.minecraft.world.gen.feature.jigsaw.JigsawPattern;
import net.minecraft.world.gen.feature.structure.AbstractVillagePiece;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import twilightforest.TFConfig;
import twilightforest.block.TFBlocks;
import twilightforest.util.IntPair;

import javax.annotation.Nullable;
import java.util.Random;
import java.util.function.Predicate;
import java.util.function.Supplier;

// TODO: doc out all the vanilla copying
public class ChunkGeneratorTwilightForest extends ChunkGeneratorTFBase {

	public static final Codec<ChunkGeneratorTwilightForest> codecTFChunk = RecordCodecBuilder.create((instance) ->
			instance.group(
					BiomeProvider.CODEC.fieldOf("biome_source").forGetter(ChunkGenerator::getBiomeProvider),
					Codec.LONG.fieldOf("seed").stable().orElseGet(() -> TFDimensions.seed).forGetter((obj) -> obj.seed),
					DimensionSettings.field_236098_b_.fieldOf("settings").forGetter(ChunkGeneratorTwilightForest::getDimensionSettings))
					.apply(instance, instance.stable(ChunkGeneratorTwilightForest::new)));

	private long seed;

	public ChunkGeneratorTwilightForest(BiomeProvider provider, long seed, Supplier<DimensionSettings> settings) {
		super(provider, seed, settings, true);
		this.seed = seed;
	}

	@Override
	protected Codec<? extends ChunkGenerator> func_230347_a_() {
		return codecTFChunk;
	}

	@Override
	public ChunkGenerator func_230349_a_(long l) {
		return new ChunkGeneratorTwilightForest(this.biomeProvider.getBiomeProvider(l), l, () -> this.dimensionSettings);
	}

	private Supplier<DimensionSettings> getDimensionSettings() {
		return () -> this.dimensionSettings;
	}

	@Override
	public void generateSurface(WorldGenRegion worldGenRegion, IChunk iChunk) {
		ChunkPos chunkpos = iChunk.getPos();
		int i = chunkpos.x;
		int j = chunkpos.z;
		SharedSeedRandom sharedseedrandom = new SharedSeedRandom();
		sharedseedrandom.setBaseChunkSeed(i, j);
		ChunkPos chunkpos1 = iChunk.getPos();
		int k = chunkpos1.getXStart();
		int l = chunkpos1.getZStart();
		double d0 = 0.0625D;
		BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable();

		for(int i1 = 0; i1 < 16; ++i1) {
			for(int j1 = 0; j1 < 16; ++j1) {
				int k1 = k + i1;
				int l1 = l + j1;
				int i2 = iChunk.getTopBlockY(Heightmap.Type.WORLD_SURFACE_WG, i1, j1) + 1;
				double d1 = this.surfaceDepthNoise.noiseAt((double)k1 * 0.0625D, (double)l1 * 0.0625D, 0.0625D, (double)i1 * 0.0625D) * 15.0D;
				worldGenRegion.getBiome(blockpos$mutable.setPos(k + i1, i2, l + j1)).buildSurface(sharedseedrandom, iChunk, k1, l1, i2, d1, this.defaultBlock, this.defaultFluid, this.getSeaLevel(), worldGenRegion.getSeed());
			}
		}

		this.makeBedrock(iChunk, sharedseedrandom);
	}

	private void makeBedrock(IChunk chunkIn, Random rand) {
		BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable();
		int i = chunkIn.getPos().getXStart();
		int j = chunkIn.getPos().getZStart();
		int k = this.dimensionSettings.func_236118_f_();
		int l = this.field_236085_x_ - 1 - this.dimensionSettings.func_236117_e_();
		int i1 = 5;
		boolean flag = l + 4 >= 0 && l < this.field_236085_x_;
		boolean flag1 = k + 4 >= 0 && k < this.field_236085_x_;
		if (flag || flag1) {
			for(BlockPos blockpos : BlockPos.getAllInBoxMutable(i, 0, j, i + 15, 0, j + 15)) {
				if (flag) {
					for(int j1 = 0; j1 < 5; ++j1) {
						if (j1 <= rand.nextInt(5)) {
							chunkIn.setBlockState(blockpos$mutable.setPos(blockpos.getX(), l - j1, blockpos.getZ()), Blocks.BEDROCK.getDefaultState(), false);
						}
					}
				}

				if (flag1) {
					for(int k1 = 4; k1 >= 0; --k1) {
						if (k1 <= rand.nextInt(5)) {
							chunkIn.setBlockState(blockpos$mutable.setPos(blockpos.getX(), k + k1, blockpos.getZ()), Blocks.BEDROCK.getDefaultState(), false);
						}
					}
				}
			}

		}
	}

	@Override
	public void func_230352_b_(IWorld iWorld, StructureManager structureManager, IChunk iChunk) {
		ObjectList<StructurePiece> objectlist = new ObjectArrayList<>(10);
		ObjectList<JigsawJunction> objectlist1 = new ObjectArrayList<>(32);
		ChunkPos chunkpos = iChunk.getPos();
		int i = chunkpos.x;
		int j = chunkpos.z;
		int k = i << 4;
		int l = j << 4;

		for(Structure<?> structure : Structure.field_236384_t_) {
			structureManager.func_235011_a_(SectionPos.from(chunkpos, 0), structure).forEach((p_236089_5_) -> {
				for(StructurePiece structurepiece1 : p_236089_5_.getComponents()) {
					if (structurepiece1.func_214810_a(chunkpos, 12)) {
						if (structurepiece1 instanceof AbstractVillagePiece) {
							AbstractVillagePiece abstractvillagepiece = (AbstractVillagePiece)structurepiece1;
							JigsawPattern.PlacementBehaviour jigsawpattern$placementbehaviour = abstractvillagepiece.getJigsawPiece().getPlacementBehaviour();
							if (jigsawpattern$placementbehaviour == JigsawPattern.PlacementBehaviour.RIGID) {
								objectlist.add(abstractvillagepiece);
							}

							for(JigsawJunction jigsawjunction1 : abstractvillagepiece.getJunctions()) {
								int l5 = jigsawjunction1.getSourceX();
								int i6 = jigsawjunction1.getSourceZ();
								if (l5 > k - 12 && i6 > l - 12 && l5 < k + 15 + 12 && i6 < l + 15 + 12) {
									objectlist1.add(jigsawjunction1);
								}
							}
						} else {
							objectlist.add(structurepiece1);
						}
					}
				}

			});
		}

		double[][][] adouble = new double[2][this.noiseSizeZ + 1][this.noiseSizeY + 1];

		for(int i5 = 0; i5 < this.noiseSizeZ + 1; ++i5) {
			adouble[0][i5] = new double[this.noiseSizeY + 1];
			this.fillNoiseColumn(adouble[0][i5], i * this.noiseSizeX, j * this.noiseSizeZ + i5);
			adouble[1][i5] = new double[this.noiseSizeY + 1];
		}

		ChunkPrimer chunkprimer = (ChunkPrimer)iChunk;
		Heightmap heightmap = chunkprimer.getHeightmap(Heightmap.Type.OCEAN_FLOOR_WG);
		Heightmap heightmap1 = chunkprimer.getHeightmap(Heightmap.Type.WORLD_SURFACE_WG);
		BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable();
		ObjectListIterator<StructurePiece> objectlistiterator = objectlist.iterator();
		ObjectListIterator<JigsawJunction> objectlistiterator1 = objectlist1.iterator();

		for(int i1 = 0; i1 < this.noiseSizeX; ++i1) {
			for(int j1 = 0; j1 < this.noiseSizeZ + 1; ++j1) {
				this.fillNoiseColumn(adouble[1][j1], i * this.noiseSizeX + i1 + 1, j * this.noiseSizeZ + j1);
			}

			for(int j5 = 0; j5 < this.noiseSizeZ; ++j5) {
				ChunkSection chunksection = chunkprimer.getSection(15);
				chunksection.lock();

				for(int k1 = this.noiseSizeY - 1; k1 >= 0; --k1) {
					double d0 = adouble[0][j5][k1];
					double d1 = adouble[0][j5 + 1][k1];
					double d2 = adouble[1][j5][k1];
					double d3 = adouble[1][j5 + 1][k1];
					double d4 = adouble[0][j5][k1 + 1];
					double d5 = adouble[0][j5 + 1][k1 + 1];
					double d6 = adouble[1][j5][k1 + 1];
					double d7 = adouble[1][j5 + 1][k1 + 1];

					for(int l1 = this.verticalNoiseGranularity - 1; l1 >= 0; --l1) {
						int i2 = k1 * this.verticalNoiseGranularity + l1;
						int j2 = i2 & 15;
						int k2 = i2 >> 4;
						if (chunksection.getYLocation() >> 4 != k2) {
							chunksection.unlock();
							chunksection = chunkprimer.getSection(k2);
							chunksection.lock();
						}

						double d8 = (double)l1 / (double)this.verticalNoiseGranularity;
						double d9 = MathHelper.lerp(d8, d0, d4);
						double d10 = MathHelper.lerp(d8, d2, d6);
						double d11 = MathHelper.lerp(d8, d1, d5);
						double d12 = MathHelper.lerp(d8, d3, d7);

						for(int l2 = 0; l2 < this.horizontalNoiseGranularity; ++l2) {
							int i3 = k + i1 * this.horizontalNoiseGranularity + l2;
							int j3 = i3 & 15;
							double d13 = (double)l2 / (double)this.horizontalNoiseGranularity;
							double d14 = MathHelper.lerp(d13, d9, d10);
							double d15 = MathHelper.lerp(d13, d11, d12);

							for(int k3 = 0; k3 < this.horizontalNoiseGranularity; ++k3) {
								int l3 = l + j5 * this.horizontalNoiseGranularity + k3;
								int i4 = l3 & 15;
								double d16 = (double)k3 / (double)this.horizontalNoiseGranularity;
								double d17 = MathHelper.lerp(d16, d14, d15);
								double d18 = MathHelper.clamp(d17 / 200.0D, -1.0D, 1.0D);

								int j4;
								int k4;
								int l4;
								for(d18 = d18 / 2.0D - d18 * d18 * d18 / 24.0D; objectlistiterator.hasNext(); d18 += func_222556_a(j4, k4, l4) * 0.8D) {
									StructurePiece structurepiece = objectlistiterator.next();
									MutableBoundingBox mutableboundingbox = structurepiece.getBoundingBox();
									j4 = Math.max(0, Math.max(mutableboundingbox.minX - i3, i3 - mutableboundingbox.maxX));
									k4 = i2 - (mutableboundingbox.minY + (structurepiece instanceof AbstractVillagePiece ? ((AbstractVillagePiece)structurepiece).getGroundLevelDelta() : 0));
									l4 = Math.max(0, Math.max(mutableboundingbox.minZ - l3, l3 - mutableboundingbox.maxZ));
								}

								objectlistiterator.back(objectlist.size());

								while(objectlistiterator1.hasNext()) {
									JigsawJunction jigsawjunction = objectlistiterator1.next();
									int k5 = i3 - jigsawjunction.getSourceX();
									j4 = i2 - jigsawjunction.getSourceGroundY();
									k4 = l3 - jigsawjunction.getSourceZ();
									d18 += func_222556_a(k5, j4, k4) * 0.4D;
								}

								objectlistiterator1.back(objectlist1.size());
								BlockState blockstate = this.func_236086_a_(d18, i2);
								if (blockstate != Blocks.AIR.getDefaultState()) {
									blockpos$mutable.setPos(i3, i2, l3);
									if (blockstate.getLightValue(chunkprimer, blockpos$mutable) != 0) {
										chunkprimer.addLightPosition(blockpos$mutable);
									}

									chunksection.setBlockState(j3, j2, i4, blockstate, false);
									heightmap.update(j3, i2, i4, blockstate);
									heightmap1.update(j3, i2, i4, blockstate);
								}
							}
						}
					}
				}

				chunksection.unlock();
			}

			double[][] adouble1 = adouble[0];
			adouble[0] = adouble[1];
			adouble[1] = adouble1;
		}
	}

	private static double func_222556_a(int p_222556_0_, int p_222556_1_, int p_222556_2_) {
		int i = p_222556_0_ + 12;
		int j = p_222556_1_ + 12;
		int k = p_222556_2_ + 12;
		if (i >= 0 && i < 24) {
			if (j >= 0 && j < 24) {
				return k >= 0 && k < 24 ? (double)field_222561_h[k * 24 * 24 + i * 24 + j] : 0.0D;
			} else {
				return 0.0D;
			}
		} else {
			return 0.0D;
		}
	}

	@Override
	public int getHeight(int i, int i1, Heightmap.Type type) {
		return this.func_236087_a_(i, i1, null, type.getHeightLimitPredicate());
	}

	@Override
	public IBlockReader func_230348_a_(int i, int i1) {
		BlockState[] ablockstate = new BlockState[this.noiseSizeY * this.verticalNoiseGranularity];
		this.func_236087_a_(i, i1, ablockstate, null);
		return new Blockreader(ablockstate);
	}

	private int func_236087_a_(int p_236087_1_, int p_236087_2_, @Nullable BlockState[] p_236087_3_, @Nullable Predicate<BlockState> p_236087_4_) {
		int i = Math.floorDiv(p_236087_1_, this.horizontalNoiseGranularity);
		int j = Math.floorDiv(p_236087_2_, this.horizontalNoiseGranularity);
		int k = Math.floorMod(p_236087_1_, this.horizontalNoiseGranularity);
		int l = Math.floorMod(p_236087_2_, this.horizontalNoiseGranularity);
		double d0 = (double)k / (double)this.horizontalNoiseGranularity;
		double d1 = (double)l / (double)this.horizontalNoiseGranularity;
		double[][] adouble = new double[][]{this.func_222547_b(i, j), this.func_222547_b(i, j + 1), this.func_222547_b(i + 1, j), this.func_222547_b(i + 1, j + 1)};

		for(int i1 = this.noiseSizeY - 1; i1 >= 0; --i1) {
			double d2 = adouble[0][i1];
			double d3 = adouble[1][i1];
			double d4 = adouble[2][i1];
			double d5 = adouble[3][i1];
			double d6 = adouble[0][i1 + 1];
			double d7 = adouble[1][i1 + 1];
			double d8 = adouble[2][i1 + 1];
			double d9 = adouble[3][i1 + 1];

			for(int j1 = this.verticalNoiseGranularity - 1; j1 >= 0; --j1) {
				double d10 = (double)j1 / (double)this.verticalNoiseGranularity;
				double d11 = MathHelper.lerp3(d10, d0, d1, d2, d6, d4, d8, d3, d7, d5, d9);
				int k1 = i1 * this.verticalNoiseGranularity + j1;
				BlockState blockstate = this.func_236086_a_(d11, k1);
				if (p_236087_3_ != null) {
					p_236087_3_[k1] = blockstate;
				}

				if (p_236087_4_ != null && p_236087_4_.test(blockstate)) {
					return k1 + 1;
				}
			}
		}

		return 0;
	}

	private double[] func_222547_b(int p_222547_1_, int p_222547_2_) {
		double[] adouble = new double[this.noiseSizeY + 1];
		this.fillNoiseColumn(adouble, p_222547_1_, p_222547_2_);
		return adouble;
	}

	protected BlockState func_236086_a_(double p_236086_1_, int p_236086_3_) {
		BlockState blockstate;
		if (p_236086_1_ > 0.0D) {
			blockstate = this.defaultBlock;
		} else if (p_236086_3_ < this.getSeaLevel()) {
			blockstate = this.defaultFluid;
		} else {
			blockstate = Blocks.AIR.getDefaultState();
		}

		return blockstate;
	}

//	@Override
//	public void decorate(WorldGenRegion region) {
//		super.decorate(region);
//		int x = region.getMainChunkX();
//		int z = region.getMainChunkZ();
//
//		randomSeed.setSeed(getSeed());
//
//		ChunkBitArray data = new ChunkBitArray();
//		//func_222529_a(x, z, data);
//		squishTerrain(data);
//
//		ChunkPrimer primer = new DirectChunkPrimer(new ChunkPos(x, z));
//		initPrimer(region, data);
//
//		// Dark Forest canopy uses the different scaled biomesForGeneration value already set in setBlocksInChunk
//		addDarkForestCanopy2(x, z, region);
//
//		// now we reload the biome array so that it's scaled 1:1 with blocks on the ground
//		//this.biomesForGeneration = world.getDimension().getBiomeProvider().getBiomes(biomesForGeneration, x * 16, z * 16, 16, 16);
//
//		addGlaciers(region, this.biomeProvider.getBiomeForNoiseGen(x, getSeaLevel(), z));
//		deformTerrainForFeature(x, z, region);
//		//replaceBiomeBlocks(x, z, primer, biomesForGeneration);
//
////		generateFeatures(x, z, primer); TODO: Should be moved to Biome Decorator
////		hollowTreeGenerator.generate(world, x, z, primer); TODO: Should be handled via Biome Decorator
//
//		makeChunk(x, z, primer);
//	}

	protected void initPrimer(WorldGenRegion primer, ChunkBitArray data) {
		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {
				for (int y = 0; y < 256; y++) {
					boolean solid = data.get(getIndex(x, y, z));
					if (y < TFGenerationSettings.SEALEVEL && !solid) {
//						primer.setBlockState(new BlockPos(x, y, z), settings.getDefaultBlock(), 3);
					} else if (solid) {
//						primer.setBlockState(new BlockPos(x, y, z), settings.getDefaultFluid(), 3);
					}
				}
			}
		}
	}

	private void addGlaciers(WorldGenRegion world, Biome biome) {

		BlockState glacierBase = Blocks.GRAVEL.getDefaultState();
		BlockState glacierMain = TFConfig.COMMON_CONFIG.PERFORMANCE.glacierPackedIce.get() ? Blocks.PACKED_ICE.getDefaultState() : Blocks.ICE.getDefaultState();
		BlockState glacierTop = Blocks.ICE.getDefaultState();

		for (int z = 0; z < 16; z++) {
			for (int x = 0; x < 16; x++) {

				// FIXME if (biome != TFBiomes.glacier.get()) continue;

				// find the (current) top block
				int gBase = -1;
				for (int y = 127; y >= 0; y--) {
					Block currentBlock = world.getBlockState(new BlockPos(x, y, z)).getBlock();
					if (currentBlock == Blocks.STONE) {
						gBase = y + 1;
						world.setBlockState(new BlockPos(x, y, z), glacierBase, 3);
						break;
					}
				}

				// raise the glacier from that top block
				int gHeight = 32;
				int gTop = Math.min(gBase + gHeight, 127);

				for (int y = gBase; y < gTop; y++) {
					world.setBlockState(new BlockPos(x, y, z), glacierMain, 3);
				}
				world.setBlockState(new BlockPos(x, gTop, z), glacierTop, 3);
			}
		}
	}

	/**
	 * Adds dark forest canopy.  This version uses the "unzoomed" array of biomes used in land generation to determine how many of the nearby blocks are dark forest
	 */
	private void addDarkForestCanopy2(int chunkX, int chunkZ, WorldGenRegion primer) {
		int[] thicks = new int[5 * 5];
		boolean biomeFound = false;

		for (int z = 0; z < 5; z++) {
			for (int x = 0; x < 5; x++) {

				for (int bx = -1; bx <= 1; bx++) {
					for (int bz = -1; bz <= 1; bz++) {
//						Biome biome = getBiomeProvider().getBiomeForNoiseGen(chunkX, getSeaLevel(), chunkZ);
//
//						if (biome == TFBiomes.darkForest.get() || biome == TFBiomes.darkForestCenter.get()) {
//							thicks[x + z * 5]++;
//							biomeFound = true;
//						}
					}
				}
			}
		}

		if (!biomeFound) return;

		IntPair nearCenter = new IntPair();
//		TFFeature nearFeature = TFFeature.getNearestFeature(chunkX, chunkZ, world.getWorld(), nearCenter);

		double d = 0.03125D;
		//depthBuffer = noiseGen4.generateNoiseOctaves(depthBuffer, chunkX * 16, chunkZ * 16, 0, 16, 16, 1, d * 2D, d * 2D, d * 2D);

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
//				if (nearFeature == TFFeature.DARK_TOWER) {
//
//					int hx = nearCenter.x;
//					int hz = nearCenter.z;
//
//					int dx = x - hx;
//					int dz = z - hz;
//					int dist = (int) Math.sqrt(dx * dx + dz * dz);
//
//					if (dist < 24) {
//						thickness -= (24 - dist);
//					}
//				}

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
						//int noise = Math.min(3, (int) (depthBuffer[z & 15 | (x & 15) << 4] / 1.25f));

						// manipulate top and bottom
						int treeBottom = topLevel + 12 - (int) (thickness * 0.5F);
						int treeTop = treeBottom + (int) (thickness * 1.5F);

						//treeBottom -= noise;

						BlockState darkLeaves = TFBlocks.dark_leaves.get().getDefaultState();
						for (int y = treeBottom; y < treeTop; y++) {
							primer.setBlockState(new BlockPos(x, y, z), darkLeaves, 3);
						}
					}
				}
			}
		}
	}

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
