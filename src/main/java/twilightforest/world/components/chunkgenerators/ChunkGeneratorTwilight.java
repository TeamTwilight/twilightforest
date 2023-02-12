package twilightforest.world.components.chunkgenerators;

import com.google.common.collect.*;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import net.minecraft.Util;
import net.minecraft.core.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
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
import net.minecraft.world.level.chunk.*;
import net.minecraft.world.level.levelgen.*;
import net.minecraft.world.level.levelgen.blending.Blender;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import org.jetbrains.annotations.Nullable;
import twilightforest.init.TFBiomes;
import twilightforest.init.TFBlocks;
import twilightforest.util.Vec2i;
import twilightforest.util.LegacyLandmarkPlacements;
import twilightforest.world.components.biomesources.TFBiomeProvider;
import twilightforest.world.components.chunkgenerators.warp.*;
import twilightforest.world.components.structures.TFStructureComponent;
import twilightforest.world.components.structures.placements.BiomeForcedLandmarkPlacement;
import twilightforest.world.components.structures.start.LegacyLandmark;
import twilightforest.world.components.structures.start.TFStructureStart;
import twilightforest.init.TFLandmark;
import twilightforest.world.registration.TFGenerationSettings;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Predicate;

// TODO override getBaseHeight and getBaseColumn for our advanced structure terraforming
@SuppressWarnings({"OptionalUsedAsFieldOrParameterType", "SuspiciousNameCombination", "deprecation"})
public class ChunkGeneratorTwilight extends ChunkGeneratorWrapper {
	public static final Codec<ChunkGeneratorTwilight> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
			ChunkGenerator.CODEC.fieldOf("wrapped_generator").forGetter(o -> o.delegate),
			NoiseGeneratorSettings.CODEC.fieldOf("noise_generation_settings").forGetter(o -> o.noiseGeneratorSettings),
			Codec.BOOL.fieldOf("generate_dark_forest_canopy").forGetter(o -> o.genDarkForestCanopy),
			Codec.INT.optionalFieldOf("dark_forest_canopy_height").forGetter(o -> o.darkForestCanopyHeight),
			Codec.unboundedMap(ResourceKey.codec(Registries.BIOME), TFLandmark.CODEC.listOf().xmap(ImmutableSet::copyOf, ImmutableList::copyOf)).fieldOf("landmark_placement_allowed_biomes").forGetter(o -> o.biomeLandmarkOverrides)
	).apply(instance, ChunkGeneratorTwilight::new));

	private final Map<ResourceKey<Biome>, ImmutableSet<TFLandmark>> biomeLandmarkOverrides;

	private final Holder<NoiseGeneratorSettings> noiseGeneratorSettings;
	private final boolean genDarkForestCanopy;
	private final Optional<Integer> darkForestCanopyHeight;

	private final BlockState defaultBlock;
	private final BlockState defaultFluid;
	private final Optional<Climate.Sampler> surfaceNoiseGetter;
	private final Optional<TFTerrainWarp> warper;

	private static final BlockState[] EMPTY_COLUMN = new BlockState[0];

	public ChunkGeneratorTwilight(ChunkGenerator delegate, Holder<NoiseGeneratorSettings> noiseGenSettings, boolean genDarkForestCanopy, Optional<Integer> darkForestCanopyHeight, Map<ResourceKey<Biome>, ImmutableSet<TFLandmark>> biomeLandmarkOverrides) {
		super(delegate);

		this.biomeLandmarkOverrides = biomeLandmarkOverrides;
		this.noiseGeneratorSettings = noiseGenSettings;
		this.genDarkForestCanopy = genDarkForestCanopy;
		this.darkForestCanopyHeight = darkForestCanopyHeight;

		if (delegate instanceof NoiseBasedChunkGenerator noiseGen && noiseGen.generatorSettings().isBound()) {
			this.defaultBlock = noiseGen.generatorSettings().get().defaultBlock();
			this.defaultFluid = noiseGen.generatorSettings().get().defaultFluid();
			this.surfaceNoiseGetter = Optional.empty();//Optional.of(noiseGen.sampler);
		} else {
			this.defaultBlock = Blocks.STONE.defaultBlockState();
			this.defaultFluid = Blocks.WATER.defaultBlockState();
			this.surfaceNoiseGetter = Optional.empty();
		}

		//BIOME_FEATURES = new ImmutableMap.Builder<ResourceLocation, TFLandmark>()
		//		.put(TFBiomes.DARK_FOREST.location(), TFLandmark.KNIGHT_STRONGHOLD)
		//		.put(TFBiomes.DARK_FOREST_CENTER.location(), TFLandmark.DARK_TOWER)
		//		//.put(TFBiomes.DENSE_MUSHROOM_FOREST.location(), MUSHROOM_TOWER)
		//		.put(TFBiomes.ENCHANTED_FOREST.location(), TFLandmark.QUEST_GROVE)
		//		.put(TFBiomes.FINAL_PLATEAU.location(), TFLandmark.FINAL_CASTLE)
		//		.put(TFBiomes.FIRE_SWAMP.location(), TFLandmark.HYDRA_LAIR)
		//		.put(TFBiomes.GLACIER.location(), TFLandmark.ICE_TOWER)
		//		.put(TFBiomes.HIGHLANDS.location(), TFLandmark.TROLL_CAVE)
		//		.put(TFBiomes.SNOWY_FOREST.location(), TFLandmark.YETI_CAVE)
		//		.put(TFBiomes.SWAMP.location(), TFLandmark.LABYRINTH)
		//		.put(TFBiomes.LAKE.location(), TFLandmark.QUEST_ISLAND)
		//		.build();

		//FIXME Watch this space, make sure it worked
		if (noiseGenSettings.isBound()) {
			NoiseSettings settings = noiseGenSettings.value().noiseSettings();
			if (delegate.getBiomeSource() instanceof TFBiomeProvider source) {
				WorldgenRandom random = new WorldgenRandom(new LegacyRandomSource(0L));
				TFBlendedNoise blendedNoise = new TFBlendedNoise(random);
				NoiseModifier modifier = NoiseModifier.PASS;
				this.warper = Optional.of(new TFTerrainWarp(settings.getCellWidth(), settings.getCellHeight(), settings.height() / settings.getCellHeight(), source, new NoiseSlider(-10.0D, 3, 0), new NoiseSlider(15.0D, 3, 0), settings, blendedNoise, modifier));
			} else {
				this.warper = Optional.empty();
			}
		} else {
			this.warper = Optional.empty();
		}
	}

	@Override
	protected Codec<? extends ChunkGenerator> codec() {
		return CODEC;
	}

	@Override
	public int getBaseHeight(int x, int z, Heightmap.Types heightMap, LevelHeightAccessor level, RandomState random) {
		if (warper.isEmpty()) {
			return super.getBaseHeight(x, z, heightMap, level, random);
		} else {
			NoiseSettings settings = this.noiseGeneratorSettings.value().noiseSettings();
			int minY = Math.max(settings.minY(), level.getMinBuildHeight());
			int maxY = Math.min(settings.minY() + settings.height(), level.getMaxBuildHeight());
			int minCell = Mth.intFloorDiv(minY, settings.getCellHeight());
			int maxCell = Mth.intFloorDiv(maxY - minY, settings.getCellHeight());
			return maxCell <= 0 ? level.getMinBuildHeight() : this.iterateNoiseColumn(random, x, z, null, heightMap.isOpaque(), minCell, maxCell).orElse(level.getMinBuildHeight());
		}
	}

	@Override
	public NoiseColumn getBaseColumn(int x, int z, LevelHeightAccessor level, RandomState random) {
		if (warper.isEmpty()) {
			return super.getBaseColumn(x, z, level, random);
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
				this.iterateNoiseColumn(random, x, z, ablockstate, null, minCell, maxCell);
				return new NoiseColumn(minY, ablockstate);
			}
		}
	}

	//This logic only seems to concern very specific features, but it does need the Warp
	protected OptionalInt iterateNoiseColumn(RandomState random, int x, int z, BlockState[] states, @Nullable Predicate<BlockState> predicate, int min, int max) {
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
				this.makeAndFillNoiseColumn(random, xDiv, zDiv, min, max),
				this.makeAndFillNoiseColumn(random, xDiv, zDiv + 1, min, max),
				this.makeAndFillNoiseColumn(random, xDiv + 1, zDiv, min, max),
				this.makeAndFillNoiseColumn(random, xDiv + 1, zDiv + 1, min, max)
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
	public CompletableFuture<ChunkAccess> createBiomes(Executor executor, RandomState random, Blender blender, StructureManager manager, ChunkAccess chunkAccess) {
		//Mimic behaviour of ChunkGenerator, NoiseBasedChunkGenerator does weird things
		return CompletableFuture.supplyAsync(Util.wrapThreadWithTaskName("init_biomes", () -> {
			chunkAccess.fillBiomesFromNoise(this.getBiomeSource(), Climate.empty());
			return chunkAccess;
		}), Util.backgroundExecutor());
	}

	//VanillaCopy of NoiseBasedChunkGenerator#fillFromNoise, only so doFill can be ours
	@Override
	public CompletableFuture<ChunkAccess> fillFromNoise(Executor executor, Blender blender, RandomState random, StructureManager structureManager, ChunkAccess chunkAccess) {
		if (warper.isEmpty()) {
			return super.fillFromNoise(executor, blender, random, structureManager, chunkAccess);
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

				return CompletableFuture.supplyAsync(() -> this.doFill(random, chunkAccess, mincell, maxcell), Util.backgroundExecutor()).whenCompleteAsync((chunk, throwable) -> {
					for (LevelChunkSection section : sections) {
						section.release();
					}
				}, executor);
			}
		}
	}

	private ChunkAccess doFill(RandomState random, ChunkAccess access, int min, int max) {
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
		TFNoiseInterpolator interpolator = new TFNoiseInterpolator(cellCountX, max, cellCountZ, chunkpos, min, (columns, x, z, min1, max1, max12) -> fillNoiseColumn(x, z, min1, max1, max12));
		List<TFNoiseInterpolator> list = Lists.newArrayList(interpolator);
		list.forEach(noiseint -> noiseint.initialiseFirstX(random));
		BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();

		for (int cellX = 0; cellX < cellCountX; cellX++) {
			int advX = cellX;
			list.forEach((noiseint) -> noiseint.advanceX(random, advX));

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

	private double[] makeAndFillNoiseColumn(RandomState state, int x, int z, int min, int max) {
		double[] columns = new double[max + 1];
		this.fillNoiseColumn(columns, x, z, min, max);
		return columns;
	}

	private void fillNoiseColumn(double[] columns, int x, int z, int min, int max) {
		this.warper.get().fillNoiseColumn(columns, x, z, min, max);
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
	public void buildSurface(WorldGenRegion world, StructureManager manager, RandomState random, ChunkAccess chunk) {
		this.deformTerrainForFeature(world, chunk);

		super.buildSurface(world, manager, random, chunk);

		this.darkForestCanopyHeight.ifPresent(integer -> this.addDarkForestCanopy(world, chunk, integer));

		addGlaciers(world, chunk);
	}

	private void addGlaciers(WorldGenRegion primer, ChunkAccess chunk) {

		BlockState glacierBase = Blocks.GRAVEL.defaultBlockState();
		BlockState glacierMain = Blocks.PACKED_ICE.defaultBlockState();
		BlockState glacierTop = Blocks.ICE.defaultBlockState();

		for (int z = 0; z < 16; z++) {
			for (int x = 0; x < 16; x++) {
				Optional<ResourceKey<Biome>> biome = primer.getBiome(primer.getCenter().getWorldPosition().offset(x, 0, z)).unwrapKey();
				if (biome.isEmpty() || !TFBiomes.GLACIER.location().equals(biome.get().location())) continue;

				// find the (current) top block
				int gBase = -1;
				for (int y = 127; y >= 0; y--) {
					Block currentBlock = primer.getBlockState(withY(primer.getCenter().getWorldPosition().offset(x, 0, z), y)).getBlock();
					if (currentBlock == Blocks.STONE) {
						gBase = y;
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
	public void addDebugScreenInfo(List<String> p_223175_, RandomState p_223176_, BlockPos p_223177_) {
		//do we do anything with this? we need to implement it for some reason
	}

	// TODO Is there a way we can make a beard instead of making hard terrain shapes?
	protected final void deformTerrainForFeature(WorldGenRegion primer, ChunkAccess chunk) {
		Vec2i featureRelativePos = new Vec2i();
		TFLandmark nearFeature = LegacyLandmarkPlacements.getNearestLandmark(primer.getCenter().x, primer.getCenter().z, primer, featureRelativePos);

		//Optional<StructureStart<?>> structureStart = TFGenerationSettings.locateTFStructureInRange(primer.getLevel(), nearFeature, chunk.getPos().getWorldPosition(), nearFeature.size + 1);

		if (!nearFeature.requiresTerraforming) {
			return;
		}

		final int relativeFeatureX = featureRelativePos.x;
		final int relativeFeatureZ = featureRelativePos.z;

		if (LegacyLandmarkPlacements.isTheseFeatures(nearFeature, TFLandmark.SMALL_HILL, TFLandmark.MEDIUM_HILL, TFLandmark.LARGE_HILL, TFLandmark.HYDRA_LAIR)) {
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
		} else if (nearFeature == TFLandmark.HEDGE_MAZE || nearFeature == TFLandmark.NAGA_COURTYARD || nearFeature == TFLandmark.QUEST_GROVE) {
			for (int xInChunk = 0; xInChunk < 16; xInChunk++) {
				for (int zInChunk = 0; zInChunk < 16; zInChunk++) {
					int featureDX = xInChunk - relativeFeatureX;
					int featureDZ = zInChunk - relativeFeatureZ;
					flattenTerrainForFeature(primer, nearFeature, xInChunk, zInChunk, featureDX, featureDZ);
				}
			}
		} else if (nearFeature == TFLandmark.YETI_CAVE) {
			for (int xInChunk = 0; xInChunk < 16; xInChunk++) {
				for (int zInChunk = 0; zInChunk < 16; zInChunk++) {
					int featureDX = xInChunk - relativeFeatureX;
					int featureDZ = zInChunk - relativeFeatureZ;

					this.deformTerrainForYetiLair(primer, nearFeature, xInChunk, zInChunk, featureDX, featureDZ);
				}
			}
		} else if (nearFeature == TFLandmark.TROLL_CAVE) {
			// troll cloud, more like
			this.deformTerrainForTrollCloud2(primer, chunk, relativeFeatureX, relativeFeatureZ);
		}

		// done!
	}

	private void flattenTerrainForFeature(WorldGenRegion primer, TFLandmark nearFeature, int x, int z, int dx, int dz) {

		float squishFactor = 0f;
		int mazeHeight = TFGenerationSettings.SEALEVEL + 5;
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

		// sets the ground level to the maze height, but dont move anything in rivers
		for (int y = 0; y < mazeHeight; y++) {
			BlockState b = primer.getBlockState(withY(primer.getCenter().getWorldPosition().offset(x, 0, z), y));
			if(!primer.getBiome(withY(primer.getCenter().getWorldPosition().offset(x, 0, z), y)).is(TFBiomes.STREAM)) {
				if (b.isAir() || b.getMaterial().isLiquid()) {
					primer.setBlock(withY(primer.getCenter().getWorldPosition().offset(x, 0, z), y), Blocks.STONE.defaultBlockState(), 3);
				}
			}
		}

		for (int y = mazeHeight; y <= 127; y++) {
			BlockState b = primer.getBlockState(withY(primer.getCenter().getWorldPosition().offset(x, 0, z), y));
			if(!primer.getBiome(withY(primer.getCenter().getWorldPosition().offset(x, 0, z), y)).is(TFBiomes.STREAM)) {
				if (!b.isAir() && !b.getMaterial().isLiquid()) {
					primer.setBlock(withY(primer.getCenter().getWorldPosition().offset(x, 0, z), y), Blocks.AIR.defaultBlockState(), 3);
				}
			}
		}
	}

	protected final BlockPos withY(BlockPos old, int y) {
		return new BlockPos(old.getX(), y, old.getZ());
	}

	private void deformTerrainForTrollCloud2(WorldGenRegion primer, ChunkAccess chunkAccess, int hx, int hz) {
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
				int num7 = (int) (seed & 3L);

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
	private void raiseHills(WorldGenRegion world, ChunkAccess chunk, TFLandmark nearFeature, int hdiam, int xInChunk, int zInChunk, int featureDX, int featureDZ, float hillHeight) {
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
		if (nearFeature == TFLandmark.HYDRA_LAIR) {
			int mx = featureDX + 16;
			int mz = featureDZ + 16;
			int mdist = (int) Mth.sqrt(mx * mx + mz * mz);
			int mheight = (int) (Mth.cos(mdist / (hdiam / 1.5f) * Mth.PI) * (hdiam / 1.5f));

			hollow = Math.max(mheight - 4, hollow);
		}

		// hollow out the hollow parts
		int hollowFloor = nearFeature == TFLandmark.HYDRA_LAIR ? this.getSeaLevel() : this.getSeaLevel() - 5 - (hollow >> 3);

		for (int y = hollowFloor + 1; y < hollowFloor + hollow; y++) {
			world.setBlock(movingPos.setY(y), Blocks.AIR.defaultBlockState(), 3);
		}
	}

	private void deformTerrainForYetiLair(WorldGenRegion primer, TFLandmark nearFeature, int xInChunk, int zInChunk, int featureDX, int featureDZ) {
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
	private void addDarkForestCanopy(WorldGenRegion primer, ChunkAccess chunk, int height) {
		BlockPos blockpos = primer.getCenter().getWorldPosition();
		int[] thicks = new int[5 * 5];
		boolean biomeFound = false;

		for (int dZ = 0; dZ < 5; dZ++) {
			for (int dX = 0; dX < 5; dX++) {
				for (int bx = -1; bx <= 1; bx++) {
					for (int bz = -1; bz <= 1; bz++) {
						BlockPos p = blockpos.offset((dX + bx) << 2, 0, (dZ + bz) << 2);
						Biome biome = biomeSource.getNoiseBiome(p.getX() >> 2, 256, p.getZ() >> 2, null).value();
						if (TFBiomes.DARK_FOREST.location().equals(primer.registryAccess().registryOrThrow(Registries.BIOME).getKey(biome)) || TFBiomes.DARK_FOREST_CENTER.location().equals(primer.registryAccess().registryOrThrow(Registries.BIOME).getKey(biome))) {
							thicks[dX + dZ * 5]++;
							biomeFound = true;
						}
					}
				}
			}
		}

		if (!biomeFound) return;

		Vec2i nearCenter = new Vec2i();
		TFLandmark nearFeature = LegacyLandmarkPlacements.getNearestLandmark(primer.getCenter().x, primer.getCenter().z, primer, nearCenter);

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
				if (nearFeature == TFLandmark.DARK_TOWER) {
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

	private static int getSpawnListIndexAt(StructureStart start, BlockPos pos) {
		int highestFoundIndex = -1;
		for (StructurePiece component : start.getPieces()) {
			if (component.getBoundingBox().isInside(pos)) {
				if (component instanceof TFStructureComponent tfComponent) {
					if (tfComponent.spawnListIndex > highestFoundIndex)
						highestFoundIndex = tfComponent.spawnListIndex;
				} else
					return 0;
			}
		}
		return highestFoundIndex;
	}

	@Nullable
	public static List<MobSpawnSettings.SpawnerData> gatherPotentialSpawns(StructureManager structureManager, MobCategory classification, BlockPos pos) {
		for (Structure structure : structureManager.registryAccess().registryOrThrow(Registries.STRUCTURE)) {
			if (structure instanceof LegacyLandmark landmark) {
				StructureStart start = structureManager.getStructureAt(pos, landmark);
				if (!start.isValid())
					continue;

				if (classification != MobCategory.MONSTER)
					return landmark.getSpawnableList(classification);
				if ((start instanceof TFStructureStart s && s.isConquered()))
					return null;
				final int index = getSpawnListIndexAt(start, pos);
				if (index < 0)
					return null;
				return landmark.getSpawnableMonsterList(index);
			}
		}
		return null;
	}

	@Override
	public WeightedRandomList<MobSpawnSettings.SpawnerData> getMobsAt(Holder<Biome> biome, StructureManager structureManager, MobCategory mobCategory, BlockPos pos) {
		List<MobSpawnSettings.SpawnerData> potentialStructureSpawns = gatherPotentialSpawns(structureManager, mobCategory, pos);
		if (potentialStructureSpawns != null)
			return WeightedRandomList.create(potentialStructureSpawns);

		return super.getMobsAt(biome, structureManager, mobCategory, pos);
	}

	public TFLandmark pickLandmarkForChunk(final ChunkPos chunk, final WorldGenLevel world) {
		return this.pickLandmarkForChunk(chunk.x, chunk.z, world);
	}

	public TFLandmark pickLandmarkForChunk(int x, int z, final WorldGenLevel world) {
		return LegacyLandmarkPlacements.pickLandmarkForChunk(x, z, world);
	}

	public boolean isLandmarkPickedForChunk(TFLandmark landmark, Holder<Biome> biome, int chunkX, int chunkZ, long seed) {
		if (!LegacyLandmarkPlacements.chunkHasLandmarkCenter(chunkX, chunkZ)) return false;

		var biomeKey = biome.unwrapKey();
		if (biomeKey.isEmpty()) return false;

		return this.biomeLandmarkOverrides.containsKey(biomeKey.get())
				? this.biomeGuaranteedLandmark(biomeKey.get(), landmark)
				: landmark == LegacyLandmarkPlacements.pickVarietyLandmark(chunkX, chunkZ, seed);
	}

	public boolean biomeGuaranteedLandmark(ResourceKey<Biome> biome, TFLandmark landmark) {
		if (!this.biomeLandmarkOverrides.containsKey(biome)) return false;
		return this.biomeLandmarkOverrides.getOrDefault(biome, ImmutableSet.of()).contains(landmark);
	}

	//copy of ChunkGenerator.createStructures, we just need to allow TF structures to correctly spawn
	@Override
	public void createStructures(RegistryAccess access, ChunkGeneratorStructureState state, StructureManager manager, ChunkAccess chunk, StructureTemplateManager templateManager) {
		ChunkPos chunkpos = chunk.getPos();
		SectionPos sectionpos = SectionPos.bottomOf(chunk);
		RandomState randomstate = state.randomState();
		state.possibleStructureSets().forEach((p_255564_) -> {
			StructurePlacement structureplacement = p_255564_.value().placement();
			List<StructureSet.StructureSelectionEntry> list = p_255564_.value().structures();

			for(StructureSet.StructureSelectionEntry structureset$structureselectionentry : list) {
				StructureStart structurestart = manager.getStartForStructure(sectionpos, structureset$structureselectionentry.structure().value(), chunk);
				if (structurestart != null && structurestart.isValid()) {
					return;
				}
			}

			//TF: check if we're trying to add a TF structure and handle that accordingly. We need to still feed the chunk generator into the placement method, so we absolutely have to do this
			//stupid mojang always changing everything
			if ((structureplacement instanceof BiomeForcedLandmarkPlacement forced && forced.isTFPlacementChunk(this, state, chunkpos.x, chunkpos.z)) || structureplacement.isStructureChunk(state, chunkpos.x, chunkpos.z)) {
				if (list.size() == 1) {
					this.tryGenerateStructure(list.get(0), manager, access, randomstate, templateManager, state.getLevelSeed(), chunk, chunkpos, sectionpos);
				} else {
					ArrayList<StructureSet.StructureSelectionEntry> arraylist = new ArrayList<>(list.size());
					arraylist.addAll(list);
					WorldgenRandom worldgenrandom = new WorldgenRandom(new LegacyRandomSource(0L));
					worldgenrandom.setLargeFeatureSeed(state.getLevelSeed(), chunkpos.x, chunkpos.z);
					int i = 0;

					for(StructureSet.StructureSelectionEntry structureset$structureselectionentry1 : arraylist) {
						i += structureset$structureselectionentry1.weight();
					}

					while(!arraylist.isEmpty()) {
						int j = worldgenrandom.nextInt(i);
						int k = 0;

						for(StructureSet.StructureSelectionEntry structureset$structureselectionentry2 : arraylist) {
							j -= structureset$structureselectionentry2.weight();
							if (j < 0) {
								break;
							}

							++k;
						}

						StructureSet.StructureSelectionEntry structureset$structureselectionentry3 = arraylist.get(k);
						if (this.tryGenerateStructure(structureset$structureselectionentry3, manager, access, randomstate, templateManager, state.getLevelSeed(), chunk, chunkpos, sectionpos)) {
							return;
						}

						arraylist.remove(k);
						i -= structureset$structureselectionentry3.weight();
					}

				}
			}
		});
	}

	@Nullable
	@Override
	public Pair<BlockPos, Holder<Structure>> findNearestMapStructure(ServerLevel level, HolderSet<Structure> targetStructures, BlockPos pos, int searchRadius, boolean skipKnownStructures) {
		ChunkGeneratorStructureState state = level.getChunkSource().getGeneratorState();

		@Nullable
		Pair<BlockPos, Holder<Structure>> nearest = super.findNearestMapStructure(level, targetStructures, pos, searchRadius, skipKnownStructures);

		Map<BiomeForcedLandmarkPlacement, Set<Holder<Structure>>> placementSetMap = new Object2ObjectArrayMap<>();
		for (Holder<Structure> holder : targetStructures) {
			for (StructurePlacement structureplacement : state.getPlacementsForStructure(holder)) {
				if (structureplacement instanceof BiomeForcedLandmarkPlacement landmarkPlacement) {
					placementSetMap.computeIfAbsent(landmarkPlacement, v -> new ObjectArraySet<>()).add(holder);
				}
			}
		}

		if (placementSetMap.isEmpty()) return nearest;

		double distance = nearest == null ? Double.MAX_VALUE : nearest.getFirst().distSqr(pos);

		for (BlockPos landmarkCenterPosition : LegacyLandmarkPlacements.landmarkCenterScanner(pos, Mth.ceil(Mth.sqrt(searchRadius)))) {
			for (Map.Entry<BiomeForcedLandmarkPlacement, Set<Holder<Structure>>> landmarkPlacement : placementSetMap.entrySet()) {
				if (!landmarkPlacement.getKey().isTFPlacementChunk(this, state, landmarkCenterPosition.getX() >> 4, landmarkCenterPosition.getZ() >> 4)) continue;

				for (Holder<Structure> targetStructure : targetStructures) {
					if (landmarkPlacement.getValue().contains(targetStructure)) {
						final double newDistance = landmarkCenterPosition.distToLowCornerSqr(pos.getX(), landmarkCenterPosition.getY(), pos.getZ());

						if (newDistance < distance) {
							nearest = new Pair<>(landmarkCenterPosition, targetStructure);
							distance = newDistance;
						}
					}
				}
			}
		}

		return nearest;
	}
}
