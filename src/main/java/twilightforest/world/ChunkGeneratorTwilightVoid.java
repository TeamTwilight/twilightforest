package twilightforest.world;

import net.minecraft.block.BlockFalling;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.event.terraingen.TerrainGen;
import twilightforest.TFConfig;
import twilightforest.TFFeature;
import twilightforest.biomes.TFBiomes;

import java.util.BitSet;

public class ChunkGeneratorTwilightVoid extends ChunkGeneratorTFBase {

	private final boolean generateHollowTrees = TFConfig.dimension.skylightOaks;

	public ChunkGeneratorTwilightVoid(World world, long seed, boolean enableFeatures) {
		super(world, seed, enableFeatures, false);
	}

	@Override
	public Chunk generateChunk(int x, int z) {
		rand.setSeed(getSeed(x, z));

		BitSet data = new BitSet(65536);
		setBlocksInChunk(x, z, data);
		squishTerrain(data);

		// now we reload the biome array so that it's scaled 1:1 with blocks on the ground
		this.biomesForGeneration = world.getBiomeProvider().getBiomes(biomesForGeneration, x * 16, z * 16, 16, 16);

		ChunkPrimer primer = new ChunkPrimer();
		initPrimer(primer, data);

		deformTerrainForFeature(x, z, primer);
		replaceBiomeBlocks(x, z, primer, biomesForGeneration);

		generateFeatures(x, z, primer);
		if (generateHollowTrees) {
			hollowTreeGenerator.generate(world, x, z, primer);
		}

		return makeChunk(x, z, primer);
	}

	@Override
	protected void initPrimer(ChunkPrimer primer, BitSet data) {

		IBlockState stone = Blocks.STONE.getDefaultState();

		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {

				Biome biome = biomesForGeneration[x & 15 | (z & 15) << 4];
				if (biome != TFBiomes.highlandsCenter) continue;

				for (int y = 0; y < 256; y++) {
					if (data.get(getIndex(x, y, z))) {
						primer.setBlockState(x, y, z, stone);
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
		this.rand.setSeed(this.world.getSeed());
		long k = this.rand.nextLong() / 2L * 2L + 1L;
		long l = this.rand.nextLong() / 2L * 2L + 1L;
		this.rand.setSeed((long)x * k + (long)z * l ^ this.world.getSeed());
		boolean flag = false;
		ChunkPos chunkpos = new ChunkPos(x, z);

		ForgeEventFactory.onChunkPopulate(true, this, this.world, this.rand, x, z, flag);

		for (MapGenTFMajorFeature generator : featureGenerators.values()) {
			generator.generateStructure(world, rand, chunkpos);
		}

		if (generateHollowTrees) {
			hollowTreeGenerator.generateStructure(world, rand, chunkpos);
		}

		blockpos = blockpos.add(8, 0, 8);

		if (TerrainGen.populate(this, this.world, this.rand, x, z, flag, PopulateChunkEvent.Populate.EventType.ICE)) {
			for (int k2 = 0; k2 < 16; ++k2) {
				for (int j3 = 0; j3 < 16; ++j3) {

					BlockPos blockpos1 = this.world.getPrecipitationHeight(blockpos.add(k2, 0, j3));
					BlockPos blockpos2 = blockpos1.down();

					if (this.world.canBlockFreezeWater(blockpos2)) {
						this.world.setBlockState(blockpos2, Blocks.ICE.getDefaultState(), 16 | 2);
					}

					if (this.world.canSnowAt(blockpos1, true)) {
						this.world.setBlockState(blockpos1, Blocks.SNOW_LAYER.getDefaultState(), 16 | 2);
					}
				}
			}
		}//Forge: End ICE

		ForgeEventFactory.onChunkPopulate(false, this, this.world, this.rand, x, z, flag);

		BlockFalling.fallInstantly = false;
	}

	@Override
	protected void deformTerrainForTrollCaves(ChunkPrimer primer, TFFeature nearFeature, int x, int z, int dx, int dz) {

		int radius = (nearFeature.size * 2 + 1) * 8;
		int dist = (int) Math.sqrt(dx * dx + dz * dz);
		if (dist > radius) return;

		Biome biome = biomesForGeneration[x & 15 | (z & 15) << 4];
		if (biome != TFBiomes.highlands) return;

		for (int y = 0; y < 60; y++) {
			if (primer.getBlockState(x, y, z).getBlock() != Blocks.STONE) {
				primer.setBlockState(x, y, z, Blocks.STONE.getDefaultState());
			}
		}
	}

	@Override
	public void recreateStructures(Chunk chunk, int x, int z) {
		super.recreateStructures(chunk, x, z);
		if (generateHollowTrees) {
			hollowTreeGenerator.generate(world, x, z, null);
		}
	}
}
