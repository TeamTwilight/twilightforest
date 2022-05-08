package twilightforest.world.components.chunkgenerators;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.util.Mth;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.*;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.chunk.ProtoChunk;
import net.minecraft.world.level.levelgen.*;
import net.minecraft.world.level.levelgen.blending.Blender;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import twilightforest.block.TFBlocks;
import twilightforest.util.IntPair;
import twilightforest.world.components.biomesources.TFBiomeProvider;
import twilightforest.world.components.chunkgenerators.warp.NoiseModifier;
import twilightforest.world.components.chunkgenerators.warp.TFBlendedNoise;
import twilightforest.world.components.chunkgenerators.warp.TFNoiseInterpolator;
import twilightforest.world.components.chunkgenerators.warp.TFTerrainWarp;
import twilightforest.world.components.structures.start.TFStructureStart;
import twilightforest.world.registration.TFFeature;
import twilightforest.world.registration.TFGenerationSettings;
import twilightforest.world.registration.TwilightFeatures;
import twilightforest.world.registration.biomes.BiomeKeys;

import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.function.Predicate;

// TODO override getBaseHeight and getBaseColumn for our advanced structure terraforming
public class ChunkGeneratorTwilight extends ChunkGeneratorWrapper {
	public static final Codec<ChunkGeneratorTwilight> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
			ChunkGenerator.CODEC.fieldOf("wrapped_generator").forGetter(o -> o.delegate),
			RegistryOps.retrieveRegistry(Registry.STRUCTURE_SET_REGISTRY).forGetter(o -> o.structureSets),
			NoiseGeneratorSettings.CODEC.fieldOf("noise_generation_settings").forGetter(o -> o.noiseGeneratorSettings),
			Codec.BOOL.fieldOf("generate_dark_forest_canopy").forGetter(o -> o.genDarkForestCanopy),
			Codec.BOOL.fieldOf("monster_spawns_below_sealevel").forGetter(o -> o.monsterSpawnsBelowSeaLevel),
			Codec.INT.optionalFieldOf("dark_forest_canopy_height").forGetter(o -> o.darkForestCanopyHeight),
			Codec.BOOL.fieldOf("use_overworld_seed").forGetter(o -> false) // Don't make this persistent, we want to load the stored seed on existing worlds! This is purely used on world creation ONLY!!
	).apply(instance, instance.stable(ChunkGeneratorTwilight::new)));

	private final Holder<NoiseGeneratorSettings> noiseGeneratorSettings;
	private final boolean genDarkForestCanopy;
	private final boolean monsterSpawnsBelowSeaLevel;
	private final Optional<Integer> darkForestCanopyHeight;

	private final BlockState defaultBlock;
	private final BlockState defaultFluid;
	private final Optional<Climate.Sampler> surfaceNoiseGetter;
	private final Optional<TFTerrainWarp> warper;

	public final ConcurrentHashMap<ChunkPos, TFFeature> featureCache = new ConcurrentHashMap<>();
	private static final BlockState[] EMPTY_COLUMN = new BlockState[0];

	public ChunkGeneratorTwilight(ChunkGenerator delegate, Registry<StructureSet> structures, Holder<NoiseGeneratorSettings> noiseGenSettings, boolean genDarkForestCanopy, boolean monsterSpawnsBelowSeaLevel, Optional<Integer> darkForestCanopyHeight, boolean owSeed) {
		//super(delegate.getBiomeSource(), delegate.getBiomeSource(), delegate.getSettings(), delegate instanceof NoiseBasedChunkGenerator noiseGen ? noiseGen.seed : delegate.strongholdSeed);
		super(structures, owSeed ? delegate = delegate.withSeed(TwilightFeatures.seed) : delegate);
		this.noiseGeneratorSettings = noiseGenSettings;
		this.genDarkForestCanopy = genDarkForestCanopy;
		this.monsterSpawnsBelowSeaLevel = monsterSpawnsBelowSeaLevel;
		this.darkForestCanopyHeight = darkForestCanopyHeight;

		if (delegate instanceof NoiseBasedChunkGenerator noiseGen) {
			this.defaultBlock = noiseGen.defaultBlock;
			this.defaultFluid = noiseGenSettings.value().defaultFluid();
			this.surfaceNoiseGetter = Optional.of(noiseGen.sampler);
		} else {
			this.defaultBlock = Blocks.STONE.defaultBlockState();
			this.defaultFluid = Blocks.WATER.defaultBlockState();
			this.surfaceNoiseGetter = Optional.empty();
		}

		NoiseSettings settings = noiseGenSettings.value().noiseSettings();
		if (delegate.getBiomeSource() instanceof TFBiomeProvider source) {
			WorldgenRandom random = new WorldgenRandom(new LegacyRandomSource(delegate.ringPlacementSeed));
			TFBlendedNoise blendedNoise = new TFBlendedNoise(random, settings.noiseSamplingSettings(), settings.getCellWidth(), settings.getCellHeight());
			NoiseModifier modifier = NoiseModifier.PASS;
			this.warper = Optional.of(new TFTerrainWarp(settings.getCellWidth(), settings.getCellHeight(), settings.getCellCountY(), source, settings, blendedNoise, modifier));
		} else {
			this.warper = Optional.empty();
		}
	}

	@Override
	protected Codec<? extends ChunkGenerator> codec() {
		return CODEC;
	}

	@Override
	public ChunkGenerator withSeed(long newSeed) {
		return new ChunkGeneratorTwilight(this.delegate.withSeed(newSeed), this.structureSets, this.noiseGeneratorSettings, this.genDarkForestCanopy, this.monsterSpawnsBelowSeaLevel, this.darkForestCanopyHeight, false);
	}

	@Override
	public int getBaseHeight(int x, int z, Heightmap.Types heightMap, LevelHeightAccessor level) {
		if (warper.isEmpty()) {
			return super.getBaseHeight(x, z, heightMap, level);
		} else {
			NoiseSettings settings = this.noiseGeneratorSettings.value().noiseSettings();
			int minY = Math.max(settings.minY(), level.getMinBuildHeight());
			int maxY = Math.min(settings.minY() + settings.height(), level.getMaxBuildHeight());
			int minCell = Mth.intFloorDiv(minY, settings.getCellHeight());
			int maxCell = Mth.intFloorDiv(maxY - minY, settings.getCellHeight());
			return maxCell <= 0 ? level.getMinBuildHeight() : this.iterateNoiseColumn(x, z, null, heightMap.isOpaque(), minCell, maxCell).orElse(level.getMinBuildHeight());
		}
	}

	@Override
	public NoiseColumn getBaseColumn(int x, int z, LevelHeightAccessor level) {
		if (warper.isEmpty()) {
			return super.getBaseColumn(x, z, level);
		} else {
			NoiseSettings settings = this.noiseGeneratorSettings.value().noiseSettings();
			int minY = Math.max(settings.minY(), level.getMinBuildHeight());
			int maxY = Math.min(settings.minY() + settings.height(), level.getMaxBuildHeight());
			int minCell = Mth.intFloorDiv(minY, settings.getCellHeight());
			int maxCell = Mth.intFloorDiv(maxY - minY, settings.getCellHeight());
			if (maxCell <= 0) {
				return new NoiseColumn(minY, EMPTY_COLUMN);
			} else {
				BlockState[] ablockstate = new BlockState[maxCell * settings.getCellHeight()];
				this.iterateNoiseColumn(x, z, ablockstate, null, minCell, maxCell);
				return new NoiseColumn(minY, ablockstate);
			}
		}
	}

	//This logic only seems to concern very specific features, but it does need the Warp
	protected OptionalInt iterateNoiseColumn(int x, int z, BlockState[] states, Predicate<BlockState> predicate, int min, int max) {
		NoiseSettings settings = this.noiseGeneratorSettings.value().noiseSettings();
		int cellWidth = settings.getCellWidth();
		int cellHeight = settings.getCellHeight();
		int xDiv = Math.floorDiv(x, cellWidth);
		int zDiv = Math.floorDiv(z, cellWidth);
		int xMod = Math.floorMod(x, cellWidth);
		int zMod = Math.floorMod(z, cellWidth);
		int xMin = xMod / cellWidth;
		int zMin = zMod / cellWidth;
		double[][] columns = new double[][] {
				this.makeAndFillNoiseColumn(xDiv, zDiv, min, max),
				this.makeAndFillNoiseColumn(xDiv, zDiv + 1, min, max),
				this.makeAndFillNoiseColumn(xDiv + 1, zDiv, min, max),
				this.makeAndFillNoiseColumn(xDiv + 1, zDiv + 1, min, max)
		};

		for (int cell = max - 1; cell >= 0; cell--) {
			double d00 = columns[0][cell];
			double d10 = columns[1][cell];
			double d20 = columns[2][cell];
			double d30 = columns[3][cell];
			double d01 = columns[0][cell + 1];
			double d11 = columns[1][cell + 1];
			double d21 = columns[2][cell + 1];
			double d31 = columns[3][cell + 1];

			for (int height = cellHeight - 1; height >= 0; height--) {
				double dcell = height / (double)cellHeight;
				double lcell = Mth.lerp3(dcell, xMin, zMin, d00, d01, d20, d21, d10, d11, d30, d31);
				int layer = cell * cellHeight + height;
				int maxlayer = layer + min * cellHeight;
				BlockState state = this.generateBaseState(lcell, layer);

				if (states != null) {
					states[layer] = state;
				}

				if (predicate != null && predicate.test(state)) {
					return OptionalInt.of(maxlayer + 1);
				}
			}
		}

		return OptionalInt.empty();
	}

	@Override
	public CompletableFuture<ChunkAccess> createBiomes(Registry<Biome> biomes, Executor executor, Blender blender, StructureFeatureManager manager, ChunkAccess chunkAccess) {
		//Mimic behaviour of ChunkGenerator, NoiseBasedChunkGenerator does weird things
		return CompletableFuture.supplyAsync(Util.wrapThreadWithTaskName("init_biomes", () -> {
			chunkAccess.fillBiomesFromNoise(this.getBiomeSource(), this.climateSampler());
			return chunkAccess;
		}), Util.backgroundExecutor());
	}

	//VanillaCopy of NoiseBasedChunkGenerator#fillFromNoise, only so doFill can be ours
	@Override
	public CompletableFuture<ChunkAccess> fillFromNoise(Executor executor, Blender blender, StructureFeatureManager structureManager, ChunkAccess chunkAccess) {
		if (warper.isEmpty()) {
			return super.fillFromNoise(executor, blender, structureManager, chunkAccess);
		} else {
			NoiseSettings settings = this.noiseGeneratorSettings.value().noiseSettings();
			int cellHeight = settings.getCellHeight();
			int minY = Math.max(settings.minY(), chunkAccess.getMinBuildHeight());
			int maxY = Math.min(settings.minY() + settings.height(), chunkAccess.getMaxBuildHeight());
			int mincell = Mth.intFloorDiv(minY, cellHeight);
			int maxcell = Mth.intFloorDiv(maxY - minY, cellHeight);

			if (maxcell <= 0) {
				return CompletableFuture.completedFuture(chunkAccess);
			} else {
				int maxIndex = chunkAccess.getSectionIndex(maxcell * cellHeight - 1 + minY);
				int minIndex = chunkAccess.getSectionIndex(minY);
				Set<LevelChunkSection> sections = Sets.newHashSet();

				for (int index = maxIndex; index >= minIndex; index--) {
					LevelChunkSection section = chunkAccess.getSection(index);
					section.acquire();
					sections.add(section);
				}

				return CompletableFuture.supplyAsync(() -> this.doFill(chunkAccess, mincell, maxcell), Util.backgroundExecutor()).whenCompleteAsync((chunk, throwable) -> {
					for (LevelChunkSection section : sections) {
						section.release();
					}
				}, executor);
			}
		}
	}

	private ChunkAccess doFill(ChunkAccess access, int min, int max) {
		NoiseSettings settings = noiseGeneratorSettings.value().noiseSettings();
		int cellWidth = settings.getCellWidth();
		int cellHeight = settings.getCellHeight();
		int cellCountX = 16 / cellWidth;
		int cellCountZ = 16 / cellWidth;
		Heightmap oceanfloor = access.getOrCreateHeightmapUnprimed(Heightmap.Types.OCEAN_FLOOR_WG);
		Heightmap surface = access.getOrCreateHeightmapUnprimed(Heightmap.Types.WORLD_SURFACE_WG);
		ChunkPos chunkpos = access.getPos();
		int minX = chunkpos.getMinBlockX();
		int minZ = chunkpos.getMinBlockZ();
		TFNoiseInterpolator interpolator = new TFNoiseInterpolator(cellCountX, max, cellCountZ, chunkpos, min, this::fillNoiseColumn);
		List<TFNoiseInterpolator> list = Lists.newArrayList(interpolator);
		list.forEach(TFNoiseInterpolator::initialiseFirstX);
		BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();

		for (int cellX = 0; cellX < cellCountX; cellX++) {
			int advX = cellX;
			list.forEach((noiseint) -> noiseint.advanceX(advX));

			for (int cellZ = 0; cellZ < cellCountZ; cellZ++) {
				LevelChunkSection section = access.getSection(access.getSectionsCount() - 1);

				for (int cellY = max - 1; cellY >= 0; cellY--) {
					int advY = cellY;
					int advZ = cellZ;
					list.forEach((noiseint) -> noiseint.selectYZ(advY, advZ));

					for(int height = cellHeight - 1; height >= 0; height--) {
						int minheight = (min + cellY) * cellHeight + height;
						int mincellY = minheight & 15;
						int minindexY = access.getSectionIndex(minheight);

						if (access.getSectionIndex(section.bottomBlockY()) != minindexY) {
							section = access.getSection(minindexY);
						}

						double heightdiv = (double)height / (double)cellHeight;
						list.forEach((noiseint) -> noiseint.updateY(heightdiv));

						for (int widthX = 0; widthX < cellWidth; widthX++) {
							int minwidthX = minX + cellX * cellWidth + widthX;
							int mincellX = minwidthX & 15;
							double widthdivX = (double)widthX / (double)cellWidth;
							list.forEach((noiseint) -> noiseint.updateX(widthdivX));

							for (int widthZ = 0; widthZ < cellWidth; widthZ++) {
								int minwidthZ = minZ + cellZ * cellWidth + widthZ;
								int mincellZ = minwidthZ & 15;
								double widthdivZ = (double)widthZ / (double)cellWidth;
								double noiseval = interpolator.updateZ(widthdivZ);
								//BlockState state = this.updateNoiseAndGenerateBaseState(beardifier, this.emptyAquifier, NoiseModifier.PASS, minwidthX, minheight, minwidthZ, noiseval); //TODO
								BlockState state = this.generateBaseState(noiseval, minheight);

								if (state != Blocks.AIR.defaultBlockState()) {
									if (state.getLightEmission() != 0 && access instanceof ProtoChunk proto) {
										mutable.set(minwidthX, minheight, minwidthZ);
										proto.addLight(mutable);
									}

									section.setBlockState(mincellX, mincellY, mincellZ, state, false);
									oceanfloor.update(mincellX, minheight, mincellZ, state);
									surface.update(mincellX, minheight, mincellZ, state);

									//Probably not necessary?
//									if (emptyAquifier.shouldScheduleFluidUpdate() && !state.getFluidState().isEmpty()) {
//										mutable.set(minwidthX, minheight, minwidthZ);
//										access.markPosForPostprocessing(mutable);
//									}
								}
							}
						}
					}
				}
			}

			list.forEach(TFNoiseInterpolator::swapSlices);
		}

		return access;
	}

	private double[] makeAndFillNoiseColumn(int x, int z, int min, int max) {
		double[] columns = new double[max + 1];
		this.fillNoiseColumn(columns, x, z, min, max);
		return columns;
	}

	private void fillNoiseColumn(double[] columns, int x, int z, int min, int max) {
		NoiseSettings settings = this.noiseGeneratorSettings.value().noiseSettings();
		this.warper.get().fillNoiseColumn(this, columns, x, z, settings, this.getSeaLevel(), min, max);
	}

	//Logic based on 1.16. Will only ever get the default Block, Fluid, or Air
	private BlockState generateBaseState(double noiseVal, double level) {
		BlockState state;

		if (noiseVal > 0.0D) {
			state = this.defaultBlock;
		} else if (level < this.getSeaLevel()) {
			state = this.defaultFluid;
		} else {
			state = Blocks.AIR.defaultBlockState();
		}

		return state;
	}

	@Override
	public void buildSurface(WorldGenRegion world, StructureFeatureManager manager, ChunkAccess chunk) {
		this.deformTerrainForFeature(world, chunk);

		super.buildSurface(world, manager, chunk);

		//noinspection OptionalIsPresent
		if (this.darkForestCanopyHeight.isPresent())
			this.addDarkForestCanopy(world, chunk, this.darkForestCanopyHeight.get());

		addGlaciers(world, chunk);
	}

	private void addGlaciers(WorldGenRegion primer, ChunkAccess chunk) {

		BlockState glacierBase = Blocks.GRAVEL.defaultBlockState();
		BlockState glacierMain = Blocks.PACKED_ICE.defaultBlockState();
		BlockState glacierTop = Blocks.ICE.defaultBlockState();

		for (int z = 0; z < 16; z++) {
			for (int x = 0; x < 16; x++) {
				Optional<ResourceKey<Biome>> biome = primer.getBiome(primer.getCenter().getWorldPosition().offset(x, 0, z)).unwrapKey();
				if (biome.isEmpty() || !BiomeKeys.GLACIER.location().equals(biome.get().location())) continue;

				// find the (current) top block
				int gBase = -1;
				for (int y = 127; y >= 0; y--) {
					Block currentBlock = primer.getBlockState(withY(primer.getCenter().getWorldPosition().offset(x, 0, z), y)).getBlock();
					if (currentBlock == Blocks.STONE) {
						gBase = y + 1;
						primer.setBlock(withY(primer.getCenter().getWorldPosition().offset(x, 0, z), y), glacierBase, 3);
						break;
					}
				}

				// raise the glacier from that top block
				int gHeight = 32;
				int gTop = Math.min(gBase + gHeight, 127);

				for (int y = gBase; y < gTop; y++) {
					primer.setBlock(withY(primer.getCenter().getWorldPosition().offset(x, 0, z), y), glacierMain, 3);
				}
				primer.setBlock(withY(primer.getCenter().getWorldPosition().offset(x, 0, z), gTop), glacierTop, 3);
			}
		}
	}

	@Override
	public void addDebugScreenInfo(List<String> p_208054_, BlockPos p_208055_) {
		//do we do anything with this? we need to implement it for some reason
	}

	// TODO Is there a way we can make a beard instead of making hard terrain shapes?
	protected final void deformTerrainForFeature(WorldGenRegion primer, ChunkAccess chunk) {
		IntPair featureRelativePos = new IntPair();
		TFFeature nearFeature = TFFeature.getNearestFeature(primer.getCenter().x, primer.getCenter().z, primer, featureRelativePos);

		//Optional<StructureStart<?>> structureStart = TFGenerationSettings.locateTFStructureInRange(primer.getLevel(), nearFeature, chunk.getPos().getWorldPosition(), nearFeature.size + 1);

		if (!nearFeature.requiresTerraforming) {
			return;
		}

		final int relativeFeatureX = featureRelativePos.x;
		final int relativeFeatureZ = featureRelativePos.z;

		if (TFFeature.isTheseFeatures(nearFeature, TFFeature.SMALL_HILL, TFFeature.MEDIUM_HILL, TFFeature.LARGE_HILL, TFFeature.HYDRA_LAIR)) {
			int hdiam = (nearFeature.size * 2 + 1) * 16;

			for (int xInChunk = 0; xInChunk < 16; xInChunk++) {
				for (int zInChunk = 0; zInChunk < 16; zInChunk++) {
					int featureDX = xInChunk - relativeFeatureX;
					int featureDZ = zInChunk - relativeFeatureZ;

					float dist = (int) Mth.sqrt(featureDX * featureDX + featureDZ * featureDZ);
					float hheight = (int) (Mth.cos(dist / hdiam * Mth.PI) * (hdiam / 3F));
					this.raiseHills(primer, chunk, nearFeature, hdiam, xInChunk, zInChunk, featureDX, featureDZ, hheight);
				}
			}
		} else if (nearFeature == TFFeature.HEDGE_MAZE || nearFeature == TFFeature.NAGA_COURTYARD || nearFeature == TFFeature.QUEST_GROVE) {
			for (int xInChunk = 0; xInChunk < 16; xInChunk++) {
				for (int zInChunk = 0; zInChunk < 16; zInChunk++) {
					int featureDX = xInChunk - relativeFeatureX;
					int featureDZ = zInChunk - relativeFeatureZ;
					flattenTerrainForFeature(primer, nearFeature, xInChunk, zInChunk, featureDX, featureDZ);
				}
			}
		} else if (nearFeature == TFFeature.YETI_CAVE) {
			for (int xInChunk = 0; xInChunk < 16; xInChunk++) {
				for (int zInChunk = 0; zInChunk < 16; zInChunk++) {
					int featureDX = xInChunk - relativeFeatureX;
					int featureDZ = zInChunk - relativeFeatureZ;

					this.deformTerrainForYetiLair(primer, nearFeature, xInChunk, zInChunk, featureDX, featureDZ);
				}
			}
		} else if (nearFeature == TFFeature.TROLL_CAVE) {
			// troll cloud, more like
			this.deformTerrainForTrollCloud2(primer, chunk, nearFeature, relativeFeatureX, relativeFeatureZ);
		}

		// done!
	}

	private void flattenTerrainForFeature(WorldGenRegion primer, TFFeature nearFeature, int x, int z, int dx, int dz) {

		float squishFactor = 0f;
		int mazeHeight = TFGenerationSettings.SEALEVEL + 1;
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
				Block currentTerrain = primer.getBlockState(withY(primer.getCenter().getWorldPosition().offset(x, 0, z), y)).getBlock();
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
			Block b = primer.getBlockState(withY(primer.getCenter().getWorldPosition().offset(x, 0, z), y)).getBlock();
			if (b == Blocks.AIR || b == Blocks.WATER) {
				primer.setBlock(withY(primer.getCenter().getWorldPosition().offset(x, 0, z), y), Blocks.STONE.defaultBlockState(), 3);
			}
		}
		for (int y = mazeHeight; y <= 127; y++) {
			Block b = primer.getBlockState(withY(primer.getCenter().getWorldPosition().offset(x, 0, z), y)).getBlock();
			if (b != Blocks.AIR && b != Blocks.WATER) {
				primer.setBlock(withY(primer.getCenter().getWorldPosition().offset(x, 0, z), y), Blocks.AIR.defaultBlockState(), 3);
			}
		}
	}

	protected final BlockPos withY(BlockPos old, int y) {
		return new BlockPos(old.getX(), y, old.getZ());
	}

	//TODO: Parameter "nearFeature" is unused. Remove?
	private void deformTerrainForTrollCloud2(WorldGenRegion primer, ChunkAccess chunkAccess, TFFeature nearFeature, int hx, int hz) {
		for (int bx = 0; bx < 4; bx++) {
			for (int bz = 0; bz < 4; bz++) {
				int dx = bx * 4 - hx - 2;
				int dz = bz * 4 - hz - 2;

				// generate several centers for other clouds
				int regionX = primer.getCenter().x + 8 >> 4;
				int regionZ = primer.getCenter().z + 8 >> 4;

				long seed = regionX * 3129871L ^ regionZ * 116129781L;
				seed = seed * seed * 42317861L + seed * 7L;

				int num0 = (int) (seed >> 12 & 3L);
				int num1 = (int) (seed >> 15 & 3L);
				int num2 = (int) (seed >> 18 & 3L);
				int num3 = (int) (seed >> 21 & 3L);
				int num4 = (int) (seed >> 9 & 3L);
				int num5 = (int) (seed >> 6 & 3L);
				int num6 = (int) (seed >> 3 & 3L);
				int num7 = (int) (seed >> 0 & 3L);

				int dx2 = dx + num0 * 5 - num1 * 4;
				int dz2 = dz + num2 * 4 - num3 * 5;
				int dx3 = dx + num4 * 5 - num5 * 4;
				int dz3 = dz + num6 * 4 - num7 * 5;

				// take the minimum distance to any center
				float dist0 = Mth.sqrt(dx * dx + dz * dz) / 4.0f;
				float dist2 = Mth.sqrt(dx2 * dx2 + dz2 * dz2) / 3.5f;
				float dist3 = Mth.sqrt(dx3 * dx3 + dz3 * dz3) / 4.5f;

				double dist = Math.min(dist0, Math.min(dist2, dist3));

				float pr = primer.getRandom().nextFloat();
				double cv = dist - 7F - pr * 3.0F;

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

						BlockPos.MutableBlockPos movingPos = primer.getCenter().getWorldPosition().mutable().move(lx, 0, lz);

						final int dY = primer.getHeight(Heightmap.Types.WORLD_SURFACE_WG, movingPos.getX(), movingPos.getZ());
						final int oceanFloor = primer.getHeight(Heightmap.Types.OCEAN_FLOOR_WG, movingPos.getX(), movingPos.getZ());

						if (dist < 7 || cv < 0.05F) {
							primer.setBlock(movingPos.setY(y), TFBlocks.WISPY_CLOUD.get().defaultBlockState(), 3);
							for (int d = 1; d < depth; d++) {
								primer.setBlock(movingPos.setY(y - d), TFBlocks.FLUFFY_CLOUD.get().defaultBlockState(), 3);
							}
							primer.setBlock(movingPos.setY(y - depth), TFBlocks.WISPY_CLOUD.get().defaultBlockState(), 3);
						} else if (dist < 8 || cv < 1F) {
							for (int d = 1; d < depth; d++) {
								primer.setBlock(movingPos.setY(y - d), TFBlocks.FLUFFY_CLOUD.get().defaultBlockState(), 3);
							}
						}

						// What are you gonna do, call the cops?
						forceHeightMapLevel(chunkAccess, Heightmap.Types.WORLD_SURFACE_WG, movingPos, dY);
						forceHeightMapLevel(chunkAccess, Heightmap.Types.WORLD_SURFACE, movingPos, dY);
						forceHeightMapLevel(chunkAccess, Heightmap.Types.OCEAN_FLOOR_WG, movingPos, oceanFloor);
						forceHeightMapLevel(chunkAccess, Heightmap.Types.OCEAN_FLOOR, movingPos, oceanFloor);
					}
				}
			}
		}
	}

	/**
	 * Raises up and hollows out the hollow hills.
	 */ // TODO Add some surface noise
	// FIXME Make this method process whole chunks instead of columns only
	private void raiseHills(WorldGenRegion world, ChunkAccess chunk, TFFeature nearFeature, int hdiam, int xInChunk, int zInChunk, int featureDX, int featureDZ, float hillHeight) {
		BlockPos.MutableBlockPos movingPos = world.getCenter().getWorldPosition().offset(xInChunk, 0, zInChunk).mutable();

		// raise the hill
		int groundHeight = chunk.getHeight(Heightmap.Types.OCEAN_FLOOR_WG, movingPos.getX(), movingPos.getZ());
		float noiseRaw = this.surfaceNoiseGetter.map(ns -> {
			// FIXME Once the above FIXME is done, instantiate the noise chunk and build the hill from there

			//ns.baseNoise.instantiate()

			// (movingPos.getX() / 64f, movingPos.getZ() / 64f, 1.0f, 256) * 32f)

			return 0f;
		}).orElse(0f);
		float totalHeightRaw = groundHeight * 0.75f + this.getSeaLevel() * 0.25f + hillHeight + noiseRaw;
		int totalHeight = (int) (((int) totalHeightRaw >> 1) * 0.375f + totalHeightRaw * 0.625f);

		for (int y = groundHeight; y <= totalHeight; y++) {
			world.setBlock(movingPos.setY(y), this.defaultBlock, 3);
		}

		// add the hollow part. Also turn water into stone below that
		int hollow = Math.min((int) hillHeight - 4 - nearFeature.size, totalHeight - 3);

		// hydra lair has a piece missing
		if (nearFeature == TFFeature.HYDRA_LAIR) {
			int mx = featureDX + 16;
			int mz = featureDZ + 16;
			int mdist = (int) Mth.sqrt(mx * mx + mz * mz);
			int mheight = (int) (Mth.cos(mdist / (hdiam / 1.5f) * Mth.PI) * (hdiam / 1.5f));

			hollow = Math.max(mheight - 4, hollow);
		}

		// hollow out the hollow parts
		int hollowFloor = nearFeature == TFFeature.HYDRA_LAIR ? this.getSeaLevel() : this.getSeaLevel() - 5 - (hollow >> 3);

		for (int y = hollowFloor + 1; y < hollowFloor + hollow; y++) {
			world.setBlock(movingPos.setY(y), Blocks.AIR.defaultBlockState(), 3);
		}
	}

	private void deformTerrainForYetiLair(WorldGenRegion primer, TFFeature nearFeature, int xInChunk, int zInChunk, int featureDX, int featureDZ) {
		float squishFactor = 0f;
		int topHeight = this.getSeaLevel() + 24;
		int outerBoundary = (nearFeature.size * 2 + 1) * 8 - 8;

		// outer boundary
		if (featureDX <= -outerBoundary) {
			squishFactor = (-featureDX - outerBoundary) / 8.0f;
		} else if (featureDX >= outerBoundary) {
			squishFactor = (featureDX - outerBoundary) / 8.0f;
		}

		if (featureDZ <= -outerBoundary) {
			squishFactor = Math.max(squishFactor, (-featureDZ - outerBoundary) / 8.0f);
		} else if (featureDZ >= outerBoundary) {
			squishFactor = Math.max(squishFactor, (featureDZ - outerBoundary) / 8.0f);
		}

		// inner boundary
		int caveBoundary = nearFeature.size * 2 * 8 - 8;
		int hollowCeiling;

		int offset = Math.min(Math.abs(featureDX), Math.abs(featureDZ));
		hollowCeiling = this.getSeaLevel() + 40 - offset * 4;

		// center square cave
		if (featureDX >= -caveBoundary && featureDZ >= -caveBoundary && featureDX <= caveBoundary && featureDZ <= caveBoundary) {
			hollowCeiling = this.getSeaLevel() + 16;
		}

		// slope ceiling slightly
		hollowCeiling -= offset / 6;

		// max out ceiling 8 blocks from roof
		hollowCeiling = Math.min(hollowCeiling, this.getSeaLevel() + 16);

		// floor, also with slight slope
		int hollowFloor = this.getSeaLevel() - 4 + offset / 6;

		BlockPos.MutableBlockPos movingPos = primer.getCenter().getWorldPosition().offset(xInChunk, 0, zInChunk).mutable();

		if (squishFactor > 0f) {
			// blend the old terrain height to arena height
			for (int y = primer.getMinBuildHeight(); y <= primer.getMaxBuildHeight(); y++) {
				if (!this.defaultBlock.equals(primer.getBlockState(movingPos.setY(y)))) {
					// we found the lowest chunk of earth
					topHeight += (y - topHeight) * squishFactor;
					hollowFloor += (y - hollowFloor) * squishFactor;
					break;
				}
			}
		}

		// carve the cave into the stone

		// add stone
		for (int y = primer.getMinBuildHeight(); y < topHeight; y++) {
			Block b = primer.getBlockState(movingPos.setY(y)).getBlock();
			if (b == Blocks.AIR || b == Blocks.WATER) {
				primer.setBlock(movingPos.setY(y), this.defaultBlock, 3);
			}
		}

		// hollow out inside
		for (int y = hollowFloor + 1; y < hollowCeiling; ++y) {
			primer.setBlock(movingPos.setY(y), Blocks.AIR.defaultBlockState(), 3);
		}

		// ice floor
		if (hollowFloor < hollowCeiling && hollowFloor < this.getSeaLevel() + 3) {
			primer.setBlock(movingPos.setY(hollowFloor), Blocks.PACKED_ICE.defaultBlockState(), 3);
		}
	}

	/**
	 * Adds dark forest canopy.  This version uses the "unzoomed" array of biomes used in land generation to determine how many of the nearby blocks are dark forest
	 */
	// Currently this is too sophisicated to be made into a SurfaceBuilder, it looks like
	private void addDarkForestCanopy(WorldGenRegion primer, ChunkAccess chunk, int height) {
		BlockPos blockpos = primer.getCenter().getWorldPosition();
		int[] thicks = new int[5 * 5];
		boolean biomeFound = false;

		for (int dZ = 0; dZ < 5; dZ++) {
			for (int dX = 0; dX < 5; dX++) {
				for (int bx = -1; bx <= 1; bx++) {
					for (int bz = -1; bz <= 1; bz++) {
						BlockPos p = blockpos.offset((dX + bx) << 2, 0, (dZ + bz) << 2);
						Biome biome = biomeSource.getNoiseBiome(p.getX() >> 2, 0, p.getZ() >> 2, null).value();
						if (BiomeKeys.DARK_FOREST.location().equals(biome.getRegistryName()) || BiomeKeys.DARK_FOREST_CENTER.location().equals(biome.getRegistryName())) {
							thicks[dX + dZ * 5]++;
							biomeFound = true;
						}
					}
				}
			}
		}

		if (!biomeFound) return;

		IntPair nearCenter = new IntPair();
		TFFeature nearFeature = TFFeature.getNearestFeature(primer.getCenter().x, primer.getCenter().z, primer, nearCenter);

		double d = 0.03125D;
		//depthBuffer = noiseGen4.generateNoiseOctaves(depthBuffer, chunkX * 16, chunkZ * 16, 0, 16, 16, 1, d * 2D, d * 2D, d * 2D);

		for (int dZ = 0; dZ < 16; dZ++) {
			for (int dX = 0; dX < 16; dX++) {
				int qx = dX >> 2;
				int qz = dZ >> 2;

				float xweight = (dX % 4) * 0.25F + 0.125F;
				float zweight = (dZ % 4) * 0.25F + 0.125F;

				float thickness = thicks[qx + (qz) * 5] * (1F - xweight) * (1F - zweight)
						+ thicks[qx + 1 + (qz) * 5] * (xweight) * (1F - zweight)
						+ thicks[qx + (qz + 1) * 5] * (1F - xweight) * (zweight)
						+ thicks[qx + 1 + (qz + 1) * 5] * (xweight) * (zweight)
						- 4;

				// make sure we're not too close to the tower
				if (nearFeature == TFFeature.DARK_TOWER) {
					int hx = nearCenter.x;
					int hz = nearCenter.z;

					int rx = dX - hx;
					int rz = dZ - hz;
					int dist = (int) Mth.sqrt(rx * rx + rz * rz);

					if (dist < 24) {
						thickness -= (24 - dist);
					}
				}

				// TODO Clean up this math
				if (thickness > 1) {
					// We can use the Deltas here because the methods called will just
					final int dY = chunk.getHeight(Heightmap.Types.WORLD_SURFACE_WG, dX, dZ);
					final int oceanFloor = chunk.getHeight(Heightmap.Types.OCEAN_FLOOR_WG, dX, dZ);
					BlockPos pos = primer.getCenter().getWorldPosition().offset(dX, dY, dZ);

					// Skip any blocks over water
					if (chunk.getBlockState(pos).getMaterial().isLiquid())
						continue;

					// just use the same noise generator as the terrain uses for stones
					//int noise = Math.min(3, (int) (depthBuffer[dZ & 15 | (dX & 15) << 4] / 1.25f));
					int noise = 0;// FIXME [1.18] Math.min(3, (int) (this.surfaceNoiseGetter.getSurfaceNoiseValue((blockpos.getX() + dX) * 0.0625D, (blockpos.getZ() + dZ) * 0.0625D, 0.0625D, dX * 0.0625D) * 15F / 1.25F));

					// manipulate top and bottom
					int treeBottom = pos.getY() + height - (int) (thickness * 0.5F);
					int treeTop = treeBottom + (int) (thickness * 1.5F);

					treeBottom -= noise;

					BlockState darkLeaves = TFBlocks.HARDENED_DARK_LEAVES.get().defaultBlockState();

					for (int y = treeBottom; y < treeTop; y++) {
						primer.setBlock(pos.atY(y), darkLeaves, 3);
					}

					// What are you gonna do, call the cops?
					forceHeightMapLevel(chunk, Heightmap.Types.WORLD_SURFACE_WG, pos, dY);
					forceHeightMapLevel(chunk, Heightmap.Types.WORLD_SURFACE, pos, dY);
					forceHeightMapLevel(chunk, Heightmap.Types.OCEAN_FLOOR_WG, pos, oceanFloor);
					forceHeightMapLevel(chunk, Heightmap.Types.OCEAN_FLOOR, pos, oceanFloor);
				}
			}
		}
	}

	static void forceHeightMapLevel(ChunkAccess chunk, Heightmap.Types type, BlockPos pos, int dY) {
		chunk.getOrCreateHeightmapUnprimed(type).setHeight(pos.getX() & 15, pos.getZ() & 15, dY + 1);
	}

	@Override
	public WeightedRandomList<MobSpawnSettings.SpawnerData> getMobsAt(Holder<Biome> biome, StructureFeatureManager structureManager, MobCategory mobCategory, BlockPos pos) {
		if (!this.monsterSpawnsBelowSeaLevel) return super.getMobsAt(biome, structureManager, mobCategory, pos);

		List<MobSpawnSettings.SpawnerData> potentialStructureSpawns = TFStructureStart.gatherPotentialSpawns(structureManager, mobCategory, pos);
		if (potentialStructureSpawns != null)
			return WeightedRandomList.create(potentialStructureSpawns);
		//FIXME forge has StructureSpawnManager commented out, find out if theyre redoing this class or removing it entirely
//		WeightedRandomList<MobSpawnSettings.SpawnerData> spawns = StructureSpawnManager.getStructureSpawns(structureManager, mobCategory, pos);
//		if (spawns != null)
//			return spawns;
		return mobCategory == MobCategory.MONSTER && pos.getY() >= this.getSeaLevel() ? WeightedRandomList.create() : super.getMobsAt(biome, structureManager, mobCategory, pos);
	}

	public TFFeature getFeatureCached(final ChunkPos chunk, final WorldGenLevel world) {
		return this.featureCache.computeIfAbsent(chunk, chunkPos -> TFFeature.generateFeature(chunkPos.x, chunkPos.z, world));
	}
}
