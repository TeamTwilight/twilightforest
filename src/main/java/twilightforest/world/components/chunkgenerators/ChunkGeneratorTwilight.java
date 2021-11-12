package twilightforest.world.components.chunkgenerators;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.util.Mth;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.*;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.synth.SurfaceNoise;
import net.minecraftforge.common.world.StructureSpawnManager;
import twilightforest.block.TFBlocks;
import twilightforest.util.IntPair;
import twilightforest.world.components.structures.start.TFStructureStart;
import twilightforest.world.registration.TFDimensions;
import twilightforest.world.registration.TFFeature;
import twilightforest.world.registration.biomes.BiomeKeys;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

// TODO override getBaseHeight and getBaseColumn for our advanced structure terraforming
public class ChunkGeneratorTwilight extends ChunkGeneratorWrapper {
	public static final Codec<ChunkGeneratorTwilight> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
			ChunkGenerator.CODEC.fieldOf("wrapped_generator").forGetter(o -> o.delegate),
			Codec.BOOL.fieldOf("generate_dark_forest_canopy").forGetter(o -> o.genDarkForestCanopy),
			Codec.BOOL.fieldOf("monster_spawns_below_sealevel").forGetter(o -> o.monsterSpawnsBelowSeaLevel),
			Codec.INT.optionalFieldOf("dark_forest_canopy_height").forGetter(o -> o.darkForestCanopyHeight),
			Codec.BOOL.fieldOf("use_overworld_seed").forGetter(o -> o.useOverworldSeed)
	).apply(instance, instance.stable(ChunkGeneratorTwilight::new)));

	private final boolean genDarkForestCanopy;
	private final boolean monsterSpawnsBelowSeaLevel;
	private final Optional<Integer> darkForestCanopyHeight;
	private final boolean useOverworldSeed;

	private final BlockState defaultBlock;
	private final SurfaceNoise surfaceNoiseGetter;

	public final ConcurrentHashMap<ChunkPos, TFFeature> featureCache = new ConcurrentHashMap<>();

	public ChunkGeneratorTwilight(ChunkGenerator delegate, boolean genDarkForestCanopy, boolean monsterSpawnsBelowSeaLevel, Optional<Integer> darkForestCanopyHeight, boolean owSeed) {
		//super(delegate.getBiomeSource(), delegate.getBiomeSource(), delegate.getSettings(), delegate instanceof NoiseBasedChunkGenerator noiseGen ? noiseGen.seed : delegate.strongholdSeed);
		super(owSeed ? delegate = delegate.withSeed(TFDimensions.seed) : delegate);
		this.genDarkForestCanopy = genDarkForestCanopy;
		this.monsterSpawnsBelowSeaLevel = monsterSpawnsBelowSeaLevel;
		this.darkForestCanopyHeight = darkForestCanopyHeight;
		this.useOverworldSeed = owSeed;

		if (delegate instanceof NoiseBasedChunkGenerator noiseGen) {
			this.defaultBlock = noiseGen.defaultBlock;
			this.surfaceNoiseGetter = noiseGen.surfaceNoise;
		} else {
			this.defaultBlock = Blocks.STONE.defaultBlockState();
			this.surfaceNoiseGetter = (x, y, yScale, yMax) -> ChunkGeneratorTwilight.this.getSeaLevel();
		}
	}

	@Override
	protected Codec<? extends ChunkGenerator> codec() {
		return CODEC;
	}

	@Override
	public ChunkGenerator withSeed(long newSeed) {
		return new ChunkGeneratorTwilight(this.delegate.withSeed(newSeed), this.genDarkForestCanopy, this.monsterSpawnsBelowSeaLevel, this.darkForestCanopyHeight, false);
	}

	@Override
	public void buildSurfaceAndBedrock(WorldGenRegion world, ChunkAccess chunk) {
		this.deformTerrainForFeature(world, chunk);

		super.buildSurfaceAndBedrock(world, chunk);

		if (this.darkForestCanopyHeight.isPresent())
			this.addDarkForestCanopy(world, chunk, this.darkForestCanopyHeight.get());
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
	private void raiseHills(WorldGenRegion world, ChunkAccess chunk, TFFeature nearFeature, int hdiam, int xInChunk, int zInChunk, int featureDX, int featureDZ, float hillHeight) {
		BlockPos.MutableBlockPos movingPos = world.getCenter().getWorldPosition().offset(xInChunk, 0, zInChunk).mutable();

		// raise the hill
		int groundHeight = chunk.getHeight(Heightmap.Types.OCEAN_FLOOR_WG, movingPos.getX(), movingPos.getZ());
		float noiseRaw = (float) (this.surfaceNoiseGetter.getSurfaceNoiseValue(movingPos.getX() / 64f, movingPos.getZ() / 64f, 1.0f, 256) * 32f);
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
						Biome biome = biomeSource.getNoiseBiome(p.getX() >> 2, 0, p.getZ() >> 2);
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
					int noise = Math.min(3, (int) (this.surfaceNoiseGetter.getSurfaceNoiseValue((blockpos.getX() + dX) * 0.0625D, (blockpos.getZ() + dZ) * 0.0625D, 0.0625D, dX * 0.0625D) * 15F / 1.25F));

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
	public WeightedRandomList<MobSpawnSettings.SpawnerData> getMobsAt(Biome biome, StructureFeatureManager structureManager, MobCategory mobCategory, BlockPos pos) {
		if (!this.monsterSpawnsBelowSeaLevel) return super.getMobsAt(biome, structureManager, mobCategory, pos);

		List<MobSpawnSettings.SpawnerData> potentialStructureSpawns = TFStructureStart.gatherPotentialSpawns(structureManager, mobCategory, pos);
		if (potentialStructureSpawns != null)
			return WeightedRandomList.create(potentialStructureSpawns);
		WeightedRandomList<MobSpawnSettings.SpawnerData> spawns = StructureSpawnManager.getStructureSpawns(structureManager, mobCategory, pos);
		if (spawns != null)
			return spawns;
		return mobCategory == MobCategory.MONSTER && pos.getY() >= this.getSeaLevel() ? WeightedRandomList.create() : super.getMobsAt(biome, structureManager, mobCategory, pos);
	}

	public TFFeature getFeatureCached(final ChunkPos chunk, final WorldGenLevel world) {
		return this.featureCache.computeIfAbsent(chunk, chunkPos -> TFFeature.generateFeature(chunkPos.x, chunkPos.z, world));
	}
}
