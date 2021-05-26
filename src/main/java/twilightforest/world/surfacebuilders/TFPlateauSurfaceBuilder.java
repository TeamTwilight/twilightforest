package twilightforest.world.surfacebuilders;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;
import twilightforest.block.TFBlocks;
import twilightforest.worldgen.BlockConstants;

import java.util.Random;

public class TFPlateauSurfaceBuilder extends SurfaceBuilder<SurfaceBuilderConfig> {

	public TFPlateauSurfaceBuilder(Codec<SurfaceBuilderConfig> config) {
		super(config);
	}

	@Override
	public void buildSurface(Random rand, IChunk primer, Biome biome, int x, int z, int startheight, double noiseVal, BlockState defaultBlock, BlockState defaultFluid, int sealevel, long seed, SurfaceBuilderConfig config) {
		this.genTwilightBiomeTerrain(rand, primer, biome, x, z, startheight, noiseVal, defaultBlock, defaultFluid, config.getTop(), config.getUnder(), config.getUnderWaterMaterial(), sealevel);
	}

	//[VanillaCopy] of DefaultrfaceBuilder.buildSurface, but we fill everything with deadrock
	protected void genTwilightBiomeTerrain(Random random, IChunk chunkIn, Biome biomeIn, int x, int z, int startHeight, double noise, BlockState defaultBlock, BlockState defaultFluid, BlockState top, BlockState middle, BlockState bottom, int sealevel) {
		BlockState blockstate = top;
		BlockState blockstate1 = middle;
		BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable();
		int i = -1;
		int j = (int)(noise / 3.0D + 3.0D + random.nextDouble() * 0.25D);
		int k = x & 15;
		int l = z & 15;

		for(int i1 = startHeight; i1 >= 0; --i1) {
			blockpos$mutable.setPos(k, i1, l);
			BlockState blockstate2 = chunkIn.getBlockState(blockpos$mutable);
			if (blockstate2.isAir()) {
				i = -1;
			} else if (blockstate2.isIn(defaultBlock.getBlock())) {
				if (!chunkIn.getBlockState(blockpos$mutable).isIn(TFBlocks.deadrock_weathered.get()) || !chunkIn.getBlockState(blockpos$mutable).isIn(TFBlocks.deadrock_cracked.get())) {
					chunkIn.setBlockState(blockpos$mutable, BlockConstants.DEADROCK, false);
				}
				if (i == -1) {
					if (j <= 0) {
						blockstate = Blocks.AIR.getDefaultState();
						blockstate1 = defaultBlock;
					} else if (i1 >= sealevel - 4 && i1 <= sealevel + 1) {
						blockstate = top;
						blockstate1 = middle;
					}

					if (i1 < sealevel && (blockstate == null || blockstate.isAir())) {
						if (biomeIn.getTemperature(blockpos$mutable.setPos(x, i1, z)) < 0.15F) {
							blockstate = Blocks.ICE.getDefaultState();
						} else {
							blockstate = defaultFluid;
						}

						blockpos$mutable.setPos(k, i1, l);
					}

					i = j;
					if (i1 >= sealevel - 1) {
						chunkIn.setBlockState(blockpos$mutable, blockstate, false);
					} else if (i1 < sealevel - 7 - j) {
						blockstate = Blocks.AIR.getDefaultState();
						blockstate1 = defaultBlock;
						chunkIn.setBlockState(blockpos$mutable, bottom, false);
					} else {
						chunkIn.setBlockState(blockpos$mutable, blockstate1, false);
					}
				} else if (i > 0) {
					--i;
					chunkIn.setBlockState(blockpos$mutable, blockstate1, false);
					if (i == 0 && blockstate1.isIn(Blocks.SAND) && j > 1) {
						i = random.nextInt(4) + Math.max(0, i1 - 63);
						blockstate1 = blockstate1.isIn(Blocks.RED_SAND) ? Blocks.RED_SANDSTONE.getDefaultState() : Blocks.SANDSTONE.getDefaultState();
					}
				}
			}
		}
	}
}
