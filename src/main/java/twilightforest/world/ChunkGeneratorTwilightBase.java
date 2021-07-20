package twilightforest.world;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityClassification;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.gen.DimensionSettings;
import net.minecraft.world.gen.NoiseChunkGenerator;
import net.minecraft.world.gen.WorldGenRegion;
import net.minecraft.world.gen.feature.structure.StructureManager;
import twilightforest.TFFeature;
import twilightforest.block.TFBlocks;
import twilightforest.structures.start.TFStructure;
import twilightforest.util.IntPair;

import java.util.List;
import java.util.function.Supplier;

// TODO: doc out all the vanilla copying
// Actually, figure out how to get this back up again
public abstract class ChunkGeneratorTwilightBase extends NoiseChunkGenerator {
	protected final long seed;
	protected final Supplier<DimensionSettings> dimensionSettings;
	private final boolean shouldGenerateBedrock;

	public ChunkGeneratorTwilightBase(BiomeProvider provider, long seed, Supplier<DimensionSettings> settings, boolean shouldGenerateBedrock) {
		super(provider, seed, settings);
		this.seed = seed;
		this.dimensionSettings = settings;
		this.shouldGenerateBedrock = shouldGenerateBedrock;
	}

	public ChunkGeneratorTwilightBase(BiomeProvider provider, long seed, Supplier<DimensionSettings> settings) {
		this(provider, seed, settings, true);
	}

	@Override
	public int getGroundHeight() {
		return 32;
	}

	protected static int getIndex(int x, int y, int z) {
		return x << 12 | z << 8 | y;
	}

	/**
	 * Crush the terrain to half the height
	 */
	protected final void squishTerrain(ChunkBitArray data) {
		int squishHeight = TFGenerationSettings.MAXHEIGHT / 2;
		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {
				for (int y = 0; y < squishHeight; y++) {
					data.set(getIndex(x, y, z), data.get(getIndex(x, y * 2 + 1, z)));
				}
				for (int y = squishHeight; y < TFGenerationSettings.CHUNKHEIGHT; y++) {
					data.clear(getIndex(x, y, z));
				}
			}
		}
	}

	protected final void deformTerrainForFeature(WorldGenRegion primer) {
		IntPair nearCenter = new IntPair();
		TFFeature nearFeature = TFFeature.getNearestFeature(getPos(primer).x, getPos(primer).z, primer, nearCenter);

		if (!nearFeature.isTerrainAltered) {
			return;
		}

		int hx = nearCenter.x;
		int hz = nearCenter.z;

		if (nearFeature == TFFeature.TROLL_CAVE) {
			// troll cloud, more like
			deformTerrainForTrollCloud2(primer, nearFeature, hx, hz);
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
			}
		}

		// done!
	}

	protected void deformTerrainForTrollCaves(WorldGenRegion primer, TFFeature nearFeature, int x, int z, int dx, int dz) {}

	//TODO: Parameter "nearFeature" is unused. Remove?
	private void deformTerrainForTrollCloud2(WorldGenRegion primer, TFFeature nearFeature, int hx, int hz) {
		for (int bx = 0; bx < 4; bx++) {
			for (int bz = 0; bz < 4; bz++) {
				int dx = (bx * 4) - hx - 2;
				int dz = (bz * 4) - hz - 2;

				// generate several centers for other clouds
				int regionX = (getPos(primer).x + 8) >> 4;
				int regionZ = (getPos(primer).z + 8) >> 4;

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

				float pr = primer.getRandom().nextFloat();
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

							primer.setBlockState(withY(getPos(primer).asBlockPos().add(lx, 0, lz), y), TFBlocks.wispy_cloud.get().getDefaultState(), 3);
							for (int d = 1; d < depth; d++) {
								primer.setBlockState(withY(getPos(primer).asBlockPos().add(lx, 0, lz), y - d), TFBlocks.fluffy_cloud.get().getDefaultState(), 3);
							}
							primer.setBlockState(withY(getPos(primer).asBlockPos().add(lx, 0, lz), y - depth), TFBlocks.wispy_cloud.get().getDefaultState(), 3);
						} else if (dist < 8 || cv < 1F) {
							for (int d = 1; d < depth; d++) {
								primer.setBlockState(withY(getPos(primer).asBlockPos().add(lx, 0, lz), y - d), TFBlocks.fluffy_cloud.get().getDefaultState(), 3);
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
		for (int y = TFGenerationSettings.SEALEVEL; y < TFGenerationSettings.CHUNKHEIGHT; y++) {
			Block currentTerrain = primer.getBlockState(withY(getPos(primer).asBlockPos().add(x, 0, z), y)).getBlock();
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
				primer.setBlockState(withY(getPos(primer).asBlockPos().add(x, 0, z), y), Blocks.STONE.getDefaultState(), 3);
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
		int hollowFloor = TFGenerationSettings.SEALEVEL - 5 - (hollow / 8);
		if (nearFeature == TFFeature.HYDRA_LAIR) {
			// different floor
			hollowFloor = TFGenerationSettings.SEALEVEL;
		}

		if (hillHeight > 0) {
			// put a base on hills that go over open space or water
			for (int y = 0; y < TFGenerationSettings.SEALEVEL; y++) {
				if (primer.getBlockState(withY(getPos(primer).asBlockPos().add(x, 0, z), y)).getBlock() != Blocks.STONE) {
					primer.setBlockState(withY(getPos(primer).asBlockPos().add(x, 0, z), y), Blocks.STONE.getDefaultState(), 3);
				}
			}
		}

		for (int y = hollowFloor + 1; y < hollowFloor + hollow; y++) {
			primer.setBlockState(withY(getPos(primer).asBlockPos().add(x, 0, z), y), Blocks.AIR.getDefaultState(), 3);
		}
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
				Block currentTerrain = primer.getBlockState(withY(getPos(primer).asBlockPos().add(x, 0, z), y)).getBlock();
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
			Block b = primer.getBlockState(withY(getPos(primer).asBlockPos().add(x, 0, z), y)).getBlock();
			if (b == Blocks.AIR || b == Blocks.WATER) {
				primer.setBlockState(withY(getPos(primer).asBlockPos().add(x, 0, z), y), Blocks.STONE.getDefaultState(), 3);
			}
		}
		for (int y = mazeHeight; y <= 127; y++) {
			Block b = primer.getBlockState(withY(getPos(primer).asBlockPos().add(x, 0, z), y)).getBlock();
			if (b != Blocks.AIR && b != Blocks.WATER) {
				primer.setBlockState(withY(getPos(primer).asBlockPos().add(x, 0, z), y), Blocks.AIR.getDefaultState(), 3);
			}
		}
	}

	private void deformTerrainForYetiLair(WorldGenRegion primer, TFFeature nearFeature, int x, int z, int dx, int dz) {

		float squishFactor = 0f;
		int topHeight = TFGenerationSettings.SEALEVEL + 24;
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
		int hollowCeiling;

		int offset = Math.min(Math.abs(dx), Math.abs(dz));
		hollowCeiling = (TFGenerationSettings.SEALEVEL + 40) - (offset * 4);

		// center square cave
		if (dx >= -caveBoundary && dz >= -caveBoundary && dx <= caveBoundary && dz <= caveBoundary) {
			hollowCeiling = TFGenerationSettings.SEALEVEL + 16;
		}

		// slope ceiling slightly
		hollowCeiling -= (offset / 6);

		// max out ceiling 8 blocks from roof
		hollowCeiling = Math.min(hollowCeiling, TFGenerationSettings.SEALEVEL + 16);

		// floor, also with slight slope
		int hollowFloor = TFGenerationSettings.SEALEVEL - 4 + (offset / 6);

		if (squishFactor > 0f) {
			// blend the old terrain height to arena height
			for (int y = 0; y <= 127; y++) {
				Block currentTerrain = primer.getBlockState(withY(getPos(primer).asBlockPos().add(x, 0, z), y)).getBlock();
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
			Block b = primer.getBlockState(withY(getPos(primer).asBlockPos().add(x, 0, z), y)).getBlock();
			if (b == Blocks.AIR || b == Blocks.WATER) {
				primer.setBlockState(withY(getPos(primer).asBlockPos().add(x, 0, z), y), Blocks.STONE.getDefaultState(), 3);
			}
		}

		// hollow out inside
		for (int y = hollowFloor + 1; y < hollowCeiling; ++y) {
			primer.setBlockState(withY(getPos(primer).asBlockPos().add(x, 0, z), y), Blocks.AIR.getDefaultState(), 3);
		}

		// ice floor
		if (hollowFloor < hollowCeiling && hollowFloor < TFGenerationSettings.SEALEVEL + 3) {
			primer.setBlockState(withY(getPos(primer).asBlockPos().add(x, 0, z), hollowFloor), Blocks.PACKED_ICE.getDefaultState(), 3);
		}
	}

	public final boolean shouldGenerateBedrock() {
		return shouldGenerateBedrock;
	}
	
	protected final ChunkPos getPos(WorldGenRegion primer) {
		return new ChunkPos(primer.getMainChunkX(), primer.getMainChunkZ());
	}

	protected final BlockPos withY(BlockPos old, int y) {
		return new BlockPos(old.getX(), y, old.getZ());
	}

	@Override
	public List<MobSpawnInfo.Spawners> func_230353_a_(Biome biome, StructureManager structureManager, EntityClassification classification, BlockPos pos) {
		List<MobSpawnInfo.Spawners> potentialStructureSpawns = TFStructure.gatherPotentialSpawns(structureManager, classification, pos);
		if (potentialStructureSpawns != null)
			return potentialStructureSpawns;
		List<MobSpawnInfo.Spawners> spawns = net.minecraftforge.common.world.StructureSpawnManager.getStructureSpawns(structureManager, classification, pos);
		if (spawns != null)
			return spawns;
		return classification == EntityClassification.MONSTER && pos.getY() >= TFGenerationSettings.SEALEVEL ? ImmutableList.of() : super.func_230353_a_(biome, structureManager, classification, pos);
	}

}
