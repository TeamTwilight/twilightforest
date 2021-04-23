package twilightforest.world;

import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.carver.WorldCarver;
import net.minecraft.world.gen.feature.ProbabilityConfig;
import org.apache.commons.lang3.mutable.MutableBoolean;
import twilightforest.block.TFBlocks;

import java.util.BitSet;
import java.util.Random;
import java.util.function.Function;

//[VanillaCopy] of DefaultWorldCarver, but caves change size when in highlands, caves wont put air blocks under streams, and caves have dirt roofs
public class TFCavesCarver extends WorldCarver<ProbabilityConfig> {

	private final boolean isHighlands;

	public TFCavesCarver(Codec<ProbabilityConfig> codec, int maxheight, boolean isHighlands) {
		super(codec, maxheight);
		this.carvableFluids = ImmutableSet.of();
		this.isHighlands = isHighlands;
	}

	@Override
	public boolean shouldCarve(Random rand, int chunkX, int chunkZ, ProbabilityConfig config) {
		return rand.nextFloat() <= config.probability;
	}

	@Override
	public boolean carveRegion(IChunk chunk, Function<BlockPos, Biome> biomePos, Random rand, int seaLevel, int chunkXOffset, int chunkZOffset, int chunkX, int chunkZ, BitSet carvingMask, ProbabilityConfig config) {
		int size = (this.func_222704_c() * 2 - 1) * 16;
		int maxcaves = rand.nextInt(rand.nextInt(rand.nextInt(45) + 1) + 1);

		for (int k = 0; k < maxcaves; ++k) {
			double randx = (double) (chunkXOffset * 16 + rand.nextInt(16));
			double randy = (double) this.func_230361_b_(rand);
			double randz = (double) (chunkZOffset * 16 + rand.nextInt(16));
			int l = 1;
			if (rand.nextInt(4) == 0) {
				float cavesize = 1.0F + rand.nextFloat() * 6.0F;
				this.genLargeNode(chunk, biomePos, rand.nextLong(), seaLevel, chunkX, chunkZ, randx, randy, randz, cavesize, 0.5D, carvingMask);
				l += rand.nextInt(4);
			}

			for (int k1 = 0; k1 < l; ++k1) {
				float f = rand.nextFloat() * ((float) Math.PI * 2F);
				float f3 = (rand.nextFloat() - 0.5F) / 4.0F;
				float f2 = this.func_230359_a_(rand);
				int i1 = size - rand.nextInt(size / 4);
				this.genNode(chunk, biomePos, rand.nextLong(), seaLevel, chunkX, chunkZ, randx, randy, randz, f2, f, f3, 0, i1, this.func_230360_b_(), carvingMask, this.isHighlands);
			}
		}

		return true;
	}

	protected float func_230359_a_(Random rand) {
		float f = rand.nextFloat() * 2.0F + rand.nextFloat();
		if (rand.nextInt(10) == 0) {
			f *= rand.nextFloat() * rand.nextFloat() * 3.0F + 1.0F;
		}

		return f;
	}

	protected double func_230360_b_() {
		return 1.0D;
	}

	protected int func_230361_b_(Random rand) {
		return rand.nextInt(rand.nextInt(120) + 8);
	}

	protected void genLargeNode(IChunk chunk, Function<BlockPos, Biome> biomePos, long seed, int seaLevel, int chunkX, int chunkZ, double randOffsetXCoord, double startY, double randOffsetZCoord, float p_227205_14_, double p_227205_15_, BitSet carvingMask) {
		double d0 = 1.5D + (double) (MathHelper.sin(((float) Math.PI / 2F)) * p_227205_14_);
		double d1 = d0 * p_227205_15_;
		this.func_227208_a_(chunk, biomePos, seed, seaLevel, chunkX, chunkZ, randOffsetXCoord + 1.0D, startY, randOffsetZCoord, d0, d1, carvingMask);
	}

	protected void genNode(IChunk chunk, Function<BlockPos, Biome> biomePos, long seed, int seaLevel, int chunkX, int chunkZ, double randOffsetXCoord, double startY, double randOffsetZCoord, float caveRadius, float pitch, float p_227206_16_, int p_227206_17_, int p_227206_18_, double p_227206_19_, BitSet p_227206_21_, boolean isHighlands) {
		Random random = new Random(seed);
		int i = random.nextInt(p_227206_18_ / 2) + p_227206_18_ / 4;
		boolean flag = random.nextInt(6) == 0;
		float f = 0.0F;
		float f1 = 0.0F;

		if (isHighlands && caveRadius < 6F) {
			caveRadius *= 2F;
		}

		for (int j = p_227206_17_; j < p_227206_18_; ++j) {
			double d0 = 1.5D + (double) (MathHelper.sin((float) Math.PI * (float) j / (float) p_227206_18_) * caveRadius);
			double d1 = d0 * p_227206_19_;
			float f2 = MathHelper.cos(p_227206_16_);
			randOffsetXCoord += (double) (MathHelper.cos(pitch) * f2);
			startY += (double) MathHelper.sin(p_227206_16_);
			randOffsetZCoord += (double) (MathHelper.sin(pitch) * f2);
			p_227206_16_ = p_227206_16_ * (flag ? 0.92F : 0.7F);
			p_227206_16_ = p_227206_16_ + f1 * 0.1F;
			pitch += f * 0.1F;
			f1 = f1 * 0.9F;
			f = f * 0.75F;
			f1 = f1 + (random.nextFloat() - random.nextFloat()) * random.nextFloat() * 2.0F;
			f = f + (random.nextFloat() - random.nextFloat()) * random.nextFloat() * 4.0F;
			if (j == i && caveRadius > 1.0F) {
				this.genNode(chunk, biomePos, random.nextLong(), seaLevel, chunkX, chunkZ, randOffsetXCoord, startY, randOffsetZCoord, random.nextFloat() * 0.5F + 0.5F, pitch - ((float) Math.PI / 2F), p_227206_16_ / 3.0F, j, p_227206_18_, 1.0D, p_227206_21_, this.isHighlands);
				this.genNode(chunk, biomePos, random.nextLong(), seaLevel, chunkX, chunkZ, randOffsetXCoord, startY, randOffsetZCoord, random.nextFloat() * 0.5F + 0.5F, pitch + ((float) Math.PI / 2F), p_227206_16_ / 3.0F, j, p_227206_18_, 1.0D, p_227206_21_, this.isHighlands);
				return;
			}

			if (random.nextInt(4) != 0) {
				if (!this.func_222702_a(chunkX, chunkZ, randOffsetXCoord, randOffsetZCoord, j, p_227206_18_, caveRadius)) {
					return;
				}

				this.func_227208_a_(chunk, biomePos, seed, seaLevel, chunkX, chunkZ, randOffsetXCoord, startY, randOffsetZCoord, d0, d1, p_227206_21_);
			}
		}

	}

	@Override
	protected boolean func_222708_a(double p_222708_1_, double p_222708_3_, double p_222708_5_, int p_222708_7_) {
		return p_222708_3_ <= -0.7D || p_222708_1_ * p_222708_1_ + p_222708_3_ * p_222708_3_ + p_222708_5_ * p_222708_5_ >= 1.0D;
	}

	@Override
	protected boolean carveBlock(IChunk chunk, Function<BlockPos, Biome> biomePos, BitSet carvingMask, Random rand, BlockPos.Mutable pos, BlockPos.Mutable posUp, BlockPos.Mutable posDown, int p_230358_8_, int p_230358_9_, int p_230358_10_, int posX, int posZ, int p_230358_13_, int posY, int p_230358_15_, MutableBoolean isSurface) {
		int i = p_230358_13_ | p_230358_15_ << 4 | posY << 8;
		if (carvingMask.get(i)) {
			return false;
		} else {
			carvingMask.set(i);
			pos.setPos(posX, posY, posZ);
			BlockState blockstate = chunk.getBlockState(pos);
			BlockState blockstate1 = chunk.getBlockState(posUp.setAndMove(pos, Direction.UP));
			if (blockstate.isIn(Blocks.GRASS_BLOCK) || blockstate.isIn(Blocks.MYCELIUM)) {
				isSurface.setTrue();
			}

			if (!this.canCarveBlock(blockstate, blockstate1)) {
				return false;
			} else {
				if (posY < 6) {
					return false;
				} else {
					//here's the code for preventing floating water and making dirt roofs. Enjoy :)
					BlockState aboveSurface = chunk.getBlockState(posUp.setPos(pos.add(0, 1, 0)));
					for (Direction facing : Direction.values()) {
						BlockState areaAround = chunk.getBlockState(posUp.offset(facing));
						BlockState areaAboveAround = chunk.getBlockState(posUp.setPos(pos.add(0, 1, 0).offset(facing)));
						if(rand.nextInt(10) == 0 && chunk.getBlockState(pos).isIn(Blocks.CAVE_AIR) && chunk.getBlockState(pos.offset(facing)).isIn(BlockTags.BASE_STONE_OVERWORLD) && this.isHighlands) {
							chunk.setBlockState(pos.offset(facing), TFBlocks.trollsteinn.get().getDefaultState(), false);
						}
						if (!aboveSurface.isIn(Blocks.WATER) && !areaAround.isIn(Blocks.WATER) && !areaAboveAround.isIn(Blocks.WATER)) {
							chunk.setBlockState(pos, CAVE_AIR, false);
							if (chunk.getBlockState(pos.up()).isIn(BlockTags.BASE_STONE_OVERWORLD) && chunk.getBlockState(pos).isIn(Blocks.CAVE_AIR) && !this.isHighlands) {
								chunk.setBlockState(pos.up(), Blocks.DIRT.getDefaultState(), false);
							}
							if (isSurface.isTrue()) {
								posDown.setAndMove(pos, Direction.DOWN);
								if (chunk.getBlockState(posDown).isIn(Blocks.DIRT)) {
									chunk.setBlockState(posDown, biomePos.apply(pos).getGenerationSettings().getSurfaceBuilderConfig().getTop(), false);
								}
							}
						}
					}
				}
			}
		}
		return true;
	}

	@Override
	protected boolean canCarveBlock(BlockState state, BlockState aboveState) {
		return this.isCarvable(state) || (state.isIn(Blocks.SAND) || state.isIn(TFBlocks.trollsteinn.get()) || state.isIn(Blocks.GRAVEL)) && !aboveState.getFluidState().isTagged(FluidTags.WATER);
	}
}
