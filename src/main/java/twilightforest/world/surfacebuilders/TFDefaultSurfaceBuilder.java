package twilightforest.world.surfacebuilders;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;
import twilightforest.world.ChunkGeneratorTwilightBase;
import twilightforest.world.TFGenerationSettings;

import java.util.Random;

// TODO Do we even need this anymore? Json worldgen lets us redefine sealevel now
public class TFDefaultSurfaceBuilder extends SurfaceBuilder<SurfaceBuilderConfig> {
	public TFDefaultSurfaceBuilder(Codec<SurfaceBuilderConfig> config) {
		super(config);
	}

	@Override
	public void buildSurface(Random rand, IChunk primer, Biome biome, int x, int z, int startheight, double noiseVal, BlockState defaultBlock, BlockState defaultFluid, int sealevel, long seed, SurfaceBuilderConfig config) {
		this.genTwilightBiomeTerrain(rand, primer, biome, x, z, startheight, noiseVal, defaultBlock, defaultFluid, config.getTop(), config.getUnder(), config.getUnderWaterMaterial(), sealevel);
	}

	// Copy of super's generateBiomeTerrain, relevant edits noted.
	//protected void genTwilightBiomeTerrain(World world, Random rand, ChunkPrimer primer, int x, int z, double noiseVal) {
	protected void genTwilightBiomeTerrain(Random rand, IChunk primer, Biome biome, int x, int z, int startHeight, double noiseVal, BlockState defaultBlock, BlockState defaultFluid, BlockState top, BlockState middle, BlockState bottom, int sealevel) {
		BlockState topState = top;
		BlockState middleState = middle;
		int generatedDirtDepth = -1;
		int dirtDepth = (int) (noiseVal / 3.0D + 3.0D + rand.nextDouble() * 0.25D);
		int localX = x & 15;
		int localZ = z & 15;
		BlockPos.Mutable mutable = new BlockPos.Mutable();
		boolean generateBedrock = /*shouldGenerateBedrock(world)*/ true; // TF - conditional bedrock gen //TODO 1.15: World is not a valid argument. Defaulting to true

		for (int y = startHeight; y >= 0; --y) { //Author's note: beginning value was 255. It is now startHeight
			mutable.setPos(localX, y, localZ);
			// TF - conditional bedrock gen
			if (generateBedrock && y <= rand.nextInt(5)) {
				primer.setBlockState(mutable, Blocks.BEDROCK.getDefaultState(), false);
			} else {
				BlockState stateHere = primer.getBlockState(mutable);

				// TF - use block check for air
				if (stateHere.getBlock() == Blocks.AIR) {
					// generatedDirtDepth = -1; TF - commented out? todo 1.9
				} else if (stateHere.getBlock() == Blocks.STONE) {
					if (generatedDirtDepth == -1) {
						if (dirtDepth <= 0) {
							topState = Blocks.AIR.getDefaultState(); // FIXME Properly address if this is the right blockstate to equate to
							middleState = defaultBlock;
						} else if (y >= sealevel - 4 && y <= sealevel + 1) {
							topState = top;
							middleState = middle;
						}

						// TF - use block check for air
						if (y < sealevel && (topState == null || topState.getBlock() == Blocks.AIR)) {
							if (biome.getTemperature(mutable.setPos(x, y, z)) < 0.15F) {
								topState = Blocks.ICE.getDefaultState();
							} else {
								topState = defaultFluid;
							}
							mutable.setPos(localZ, y, localX);
						}

						generatedDirtDepth = dirtDepth;

						if (y >= sealevel - 1) {
							primer.setBlockState(mutable, topState, false);
						} else if (y < sealevel - 7 - dirtDepth) {
							topState = Blocks.AIR.getDefaultState(); // FIXME Properly address if this is the right blockstate to equate to
							middleState = defaultBlock;
							primer.setBlockState(mutable, bottom, false);
						} else {
							primer.setBlockState(mutable, middleState, false);
						}
					} else if (generatedDirtDepth > 0) {
						--generatedDirtDepth;
						primer.setBlockState(mutable, middleState, false);

						if (generatedDirtDepth == 0 && middleState.getBlock() == Blocks.SAND) {
							generatedDirtDepth = rand.nextInt(4) + Math.max(0, y - 63);
							middleState = middleState == Blocks.RED_SAND.getDefaultState() ? Blocks.RED_SANDSTONE.getDefaultState() : Blocks.SANDSTONE.getDefaultState();
						}
					}
				}
			}
		}
	}

	//TODO: Re-evaluate
	private static boolean shouldGenerateBedrock(World world) {
		ChunkGeneratorTwilightBase generator = TFGenerationSettings.getChunkGenerator(world);
		return generator == null || generator.shouldGenerateBedrock();
	}
}
