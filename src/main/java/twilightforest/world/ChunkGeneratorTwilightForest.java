package twilightforest.world;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.DimensionSettings;
import net.minecraft.world.gen.WorldGenRegion;
import net.minecraft.world.gen.feature.structure.StructureManager;
import twilightforest.TFConfig;
import twilightforest.TFFeature;
import twilightforest.worldgen.biomes.BiomeKeys;
import twilightforest.block.TFBlocks;
import twilightforest.util.IntPair;

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
		return new ChunkGeneratorTwilightForest(this.biomeProvider.getBiomeProvider(l), l, this.dimensionSettings);
	}

	private Supplier<DimensionSettings> getDimensionSettings() {
		return this.dimensionSettings;
	}

	@Override
	public void func_230352_b_(IWorld world, StructureManager p_230352_2_, IChunk chunk) {
		super.func_230352_b_(world, p_230352_2_, chunk);

		if(!(world instanceof WorldGenRegion))
			return;

		WorldGenRegion primer = (WorldGenRegion) world;

		// Dark Forest canopy uses the different scaled biomesForGeneration value already set in setBlocksInChunk
		addDarkForestCanopy2(primer);

		addGlaciers(primer);
		deformTerrainForFeature(primer);
	}

	private void addGlaciers(WorldGenRegion primer) {

		BlockState glacierBase = Blocks.GRAVEL.getDefaultState();
		BlockState glacierMain = TFConfig.COMMON_CONFIG.PERFORMANCE.glacierPackedIce.get() ? Blocks.PACKED_ICE.getDefaultState() : Blocks.ICE.getDefaultState();
		BlockState glacierTop = Blocks.ICE.getDefaultState();

		for (int z = 0; z < 16; z++) {
			for (int x = 0; x < 16; x++) {
				Biome biome = primer.getBiome(getPos(primer).asBlockPos().add(x, 0, z));
				if (!BiomeKeys.GLACIER.getLocation().equals(biome.getRegistryName())) continue;

				// find the (current) top block
				int gBase = -1;
				for (int y = 127; y >= 0; y--) {
					Block currentBlock = primer.getBlockState(withY(getPos(primer).asBlockPos().add(x, 0, z), y)).getBlock();
					if (currentBlock == Blocks.STONE) {
						gBase = y + 1;
						primer.setBlockState(withY(getPos(primer).asBlockPos().add(x, 0, z), y), glacierBase, 3);
						break;
					}
				}

				// raise the glacier from that top block
				int gHeight = 32;
				int gTop = Math.min(gBase + gHeight, 127);

				for (int y = gBase; y < gTop; y++) {
					primer.setBlockState(withY(getPos(primer).asBlockPos().add(x, 0, z), y), glacierMain, 3);
				}
				primer.setBlockState(withY(getPos(primer).asBlockPos().add(x, 0, z), gTop), glacierTop, 3);
			}
		}
	}

	/**
	 * Adds dark forest canopy.  This version uses the "unzoomed" array of biomes used in land generation to determine how many of the nearby blocks are dark forest
	 */
	private void addDarkForestCanopy2(WorldGenRegion primer) {
		int[] thicks = new int[5 * 5];
		boolean biomeFound = false;

		for (int z = 0; z < 5; z++) {
			for (int x = 0; x < 5; x++) {

				for (int bx = -1; bx <= 1; bx++) {
					for (int bz = -1; bz <= 1; bz++) {
						Biome biome = primer.getBiome(getPos(primer).asBlockPos().add(x + bx + 2, 0, z + bz + 2));
						if (BiomeKeys.DARK_FOREST.getLocation().equals(biome.getRegistryName()) || BiomeKeys.DARK_FOREST_CENTER.getLocation().equals(biome.getRegistryName())) {
							thicks[x + z * 5]++;
							biomeFound = true;
						}
					}
				}
			}
		}

		if (!biomeFound) return;

		IntPair nearCenter = new IntPair();
		TFFeature nearFeature = TFFeature.getNearestFeature(getPos(primer).x, getPos(primer).z, primer, nearCenter);

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
				if (nearFeature == TFFeature.DARK_TOWER) {

					int hx = nearCenter.x;
					int hz = nearCenter.z;

					int rx = getPos(primer).x - hx;
					int rz = getPos(primer).z - hz;
					int dist = (int) Math.sqrt(rx * rx + rz * rz);

					if (dist < 24) {
						thickness -= (24 - dist);
					}
				}

				if (thickness > 1) {
					// find the (current) top block
					int topLevel = -1;
					for (int y = 127; y >= 0; y--) {
						Block currentBlock = primer.getBlockState(withY(getPos(primer).asBlockPos().add(x, 0, z), y)).getBlock();
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
							primer.setBlockState(withY(getPos(primer).asBlockPos().add(x, 0, z), y), darkLeaves, 3);
						}
					}
				}
			}
		}
	}
}
