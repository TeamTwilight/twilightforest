package twilightforest.world;

import net.minecraft.block.BlockFalling;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.event.terraingen.TerrainGen;
import twilightforest.TFFeature;

public class ChunkGeneratorTwilightVoid extends ChunkGeneratorTFBase {

	public ChunkGeneratorTwilightVoid(World world, long seed, boolean enableFeatures) {
		super(world, seed, enableFeatures, false);
	}

	@Override
	public Chunk generateChunk(int x, int z) {
		rand.setSeed(getSeed(x, z));
		ChunkPrimer primer = new ChunkPrimer();
		// now we reload the biome array so that it's scaled 1:1 with blocks on the ground
		this.biomesForGeneration = world.getBiomeProvider().getBiomes(biomesForGeneration, x * 16, z * 16, 16, 16);
		deformTerrainForFeature(x, z, primer);
		replaceBiomeBlocks(x, z, primer, biomesForGeneration);

		for (TFFeature feature : TFFeature.values()) {
			if (feature != TFFeature.NOTHING) {
				feature.getFeatureGenerator().generate(world, x, z, primer);
			}
		}

		return makeChunk(x, z, primer);
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

		for (TFFeature feature : TFFeature.values()) {
			if (feature != TFFeature.NOTHING) {
				feature.getFeatureGenerator().generateStructure(world, rand, chunkpos);
			}
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
}
