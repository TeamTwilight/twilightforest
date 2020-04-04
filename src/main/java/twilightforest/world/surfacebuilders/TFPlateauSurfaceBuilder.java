package twilightforest.world.surfacebuilders;

import com.mojang.datafixers.Dynamic;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;
import twilightforest.block.TFBlocks;
import twilightforest.world.TFWorld;

import java.util.Random;
import java.util.function.Function;

public class TFPlateauSurfaceBuilder extends SurfaceBuilder<SurfaceBuilderConfig> {

	public TFPlateauSurfaceBuilder(Function<Dynamic<?>, ? extends SurfaceBuilderConfig> config) {
		super(config);
	}

	@Override
	public void buildSurface(Random rand, IChunk primer, Biome biome, int x, int z, int startheight, double noiseVal, BlockState defaultBlock, BlockState defaultFluid, int sealevel, long seed, SurfaceBuilderConfig config) {
		this.genTwilightBiomeTerrain(rand, primer, biome, x, z, startheight, noiseVal, defaultBlock, defaultFluid, config.getTop(), config.getUnder(), config.getUnderWaterMaterial(), sealevel);
	}

	// Copy of super's generateBiomeTerrain, relevant edits noted.
	//protected void genTwilightBiomeTerrain(World world, Random rand, ChunkPrimer primer, int x, int z, double noiseVal) {
	protected void genTwilightBiomeTerrain(Random rand, IChunk primer, Biome biome, int x, int z, int startHeight, double noiseVal, BlockState defaultBlock, BlockState defaultFluid, BlockState top, BlockState middle, BlockState bottom, int sealevel) {
		int i = TFWorld.SEALEVEL; // TF - set sea level to 31 //TODO: Sea level is now handled by seaLevel
		BlockState iblockstate = top;
		BlockState iblockstate1 = middle;
		BlockState stoneReplacement = TFBlocks.deadrock.get().getDefaultState();
		int j = -1;
		int k = (int) (noiseVal / 3.0D + 3.0D + rand.nextDouble() * 0.25D);
		int l = x & 15;
		int i1 = z & 15;
		BlockPos.Mutable blockpos$mutableblockpos = new BlockPos.Mutable();
		boolean generateBedrock = shouldGenerateBedrock(world); // TF - conditional bedrock gen //TODO 1.15: World is not a valid argument

		for (int j1 = startHeight; j1 >= 0; --j1) { //Author's note: beginning value was 255. It is now startHeight
			blockpos$mutableblockpos.setPos(i1, j1, l);
			// TF - conditional bedrock gen
			if (generateBedrock && j1 <= rand.nextInt(5)) {
				primer.setBlockState(blockpos$mutableblockpos, Blocks.BEDROCK.getDefaultState(), false);
			} else {
				BlockState iblockstate2 = primer.getBlockState(blockpos$mutableblockpos);

				// TF - use block check for air
				if (iblockstate2.getBlock() == Blocks.AIR) {
					// j = -1; TF - commented out? todo 1.9
				} else if (iblockstate2.getBlock() == Blocks.STONE) {
					if (j == -1) {
						if (k <= 0) {
							iblockstate = AIR;
							iblockstate1 = defaultBlock;
						} else if (j1 >= sealevel - 4 && j1 <= sealevel + 1) {
							iblockstate = top;
							iblockstate1 = middle;
						}

						// TF - use block check for air
						if (j1 < sealevel && (iblockstate == null || iblockstate.getBlock() == Blocks.AIR)) {
							if (biome.getTemperature(blockpos$mutableblockpos.setPos(x, j1, z)) < 0.15F) {
								iblockstate = Blocks.ICE.getDefaultState();
							} else {
								iblockstate = defaultFluid;
							}
							blockpos$mutableblockpos.setPos(i1, j1, l);
						}

						j = k;

						if (j1 >= sealevel - 1) {
							primer.setBlockState(blockpos$mutableblockpos, iblockstate, false);
						} else if (j1 < sealevel - 7 - k) {
							iblockstate = AIR;
							iblockstate1 = defaultBlock;
							primer.setBlockState(blockpos$mutableblockpos, bottom, false);
						} else {
							primer.setBlockState(blockpos$mutableblockpos, iblockstate1, false);
						}
					}
					primer.setBlockState(blockpos$mutableblockpos, stoneReplacement, false);
				}
			}
		}
	}

	//TODO: Re-evaluate
	private static boolean shouldGenerateBedrock(World world) {
		ChunkGeneratorTFBase generator = TFWorld.getChunkGenerator(world);
		return generator == null || generator.shouldGenerateBedrock();
	}
}
