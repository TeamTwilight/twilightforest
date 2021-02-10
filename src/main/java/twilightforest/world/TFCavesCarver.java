package twilightforest.world;

import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.carver.WorldCarver;
import net.minecraft.world.gen.feature.ProbabilityConfig;
import org.apache.commons.lang3.mutable.MutableBoolean;

import java.util.BitSet;
import java.util.Random;
import java.util.function.Function;

//TODO: There are better parameter mappings. Retranslate some of these?
public class TFCavesCarver extends WorldCarver<ProbabilityConfig> {

	public TFCavesCarver(Codec<ProbabilityConfig> codec, int maxheight) {
		super(codec, maxheight);
		this.carvableFluids = ImmutableSet.of();
	}

	@Override
	public boolean shouldCarve(Random rand, int chunkX, int chunkZ, ProbabilityConfig config) {
		return rand.nextFloat() <= config.probability;
	}

	//VanillaCopy of CaveWorldCarver.carveRegion, modified for Highlands biome
	@Override
	public boolean carveRegion(IChunk chunkIn, Function<BlockPos, Biome> biomePos, Random rand, int seaLevel, int offsetX, int offsetZ, int chunkX, int chunkZ, BitSet mask, ProbabilityConfig config) {
		int size = (this.func_222704_c() * 2 - 1) * 16;
		int maxcaves = rand.nextInt(rand.nextInt(rand.nextInt(40) + 1) + 1);
		boolean isHighlands = false; //FIXME biomePos.apply(new BlockPos(offsetX * 16, 0, offsetZ * 16)) instanceof TFBiomeHighlands;

		for(int k = 0; k < maxcaves; ++k) {
			double randX = offsetX * 16 + rand.nextInt(16);
			double randY = (rand.nextInt(rand.nextInt(120) + 8));
			double randZ = offsetZ * 16 + rand.nextInt(16);
			int maxnodes = 1;
			if (rand.nextInt(4) == 0) {
				float cavesize = 1.0F + rand.nextFloat() * 6.0F;
				this.generateLargeCaveNode(chunkIn, biomePos, rand.nextLong(), seaLevel, chunkX, chunkZ, randX, randY, randZ, cavesize, 0.5D, mask);
				maxnodes += rand.nextInt(4);
			}

			for(int k1 = 0; k1 < maxnodes; ++k1) {
				float pitch = rand.nextFloat() * ((float)Math.PI * 2F);
				float angle = (rand.nextFloat() - 0.5F) / 4.0F;
				float cavesize = this.getCaveSize(rand);
				int loopsize = size - rand.nextInt(size / 4);
				this.generateCaveNode(chunkIn, biomePos, rand.nextLong(), seaLevel, chunkX, chunkZ, randX, randY, randZ, cavesize, pitch, angle, 0, loopsize, 1.0D, mask, isHighlands);
			}
		}

		return true;
	}

	/**
	 * Generates a larger initial cave node than usual. Called 25% of the time.
	 * VanillaCopy of CaveWorldCarver.func_227205_a_
	 */
	protected void generateLargeCaveNode(IChunk chunk, Function<BlockPos, Biome> biomePos, long seed, int seaLevel, int chunkX, int chunkZ, double offsetX, double startY, double offsetZ, float caveSize, double yScale, BitSet mask) {
		double sizeVar = 1.5D + MathHelper.sin(((float)Math.PI / 2F)) * caveSize;
		double scaledSize = sizeVar * yScale;
		this.func_227208_a_(chunk, biomePos, seed, seaLevel, chunkX, chunkZ, offsetX + 1.0D, startY, offsetZ, sizeVar, scaledSize, mask);
	}

	//VanillaCopy of CaveWorldCarver.func_227206_a_, modified for Highlands biome
	protected void generateCaveNode(IChunk chunk, Function<BlockPos, Biome> biomePos, long seed, int seaLevel, int chunkX, int chunkZ, double offsetX, double startY, double offsetZ, float caveRadius, float pitch, float yaw, int loopStart, int loopEnd, double yScale, BitSet mask, boolean isHighlands) {
		Random random = new Random(seed);
		int range = random.nextInt(loopEnd / 2) + loopEnd / 4;
		boolean flag = random.nextInt(6) == 0;
		float pitchoffset = 0.0F;
		float yawoffset = 0.0F;

		if (isHighlands && caveRadius < 6F) {
			caveRadius *= 2.5F;
		}

		for(int loop = loopStart; loop < loopEnd; ++loop) {
			double cavesize = 1.5D + MathHelper.sin((float)Math.PI * loop / loopEnd) * caveRadius;
			double scale = cavesize * yScale;
			float f2 = MathHelper.cos(yaw);
			offsetX += MathHelper.cos(pitch) * f2;
			startY += MathHelper.sin(yaw);
			offsetZ += MathHelper.sin(pitch) * f2;
			yaw = yaw * (flag ? 0.92F : 0.7F);
			yaw = yaw + yawoffset * 0.1F;
			pitch += pitchoffset * 0.1F;
			yawoffset = yawoffset * 0.9F;
			pitchoffset = pitchoffset * 0.75F;
			yawoffset = yawoffset + (random.nextFloat() - random.nextFloat()) * random.nextFloat() * 2.0F;
			pitchoffset = pitchoffset + (random.nextFloat() - random.nextFloat()) * random.nextFloat() * 4.0F;
			if (loop == range && caveRadius > 1.0F) {
				this.generateCaveNode(chunk, biomePos, random.nextLong(), seaLevel, chunkX, chunkZ, offsetX, startY, offsetZ, random.nextFloat() * 0.5F + 0.5F, pitch - ((float)Math.PI / 2F), yaw / 3.0F, loop, loopEnd, 1.0D, mask, isHighlands);
				this.generateCaveNode(chunk, biomePos, random.nextLong(), seaLevel, chunkX, chunkZ, offsetX, startY, offsetZ, random.nextFloat() * 0.5F + 0.5F, pitch + ((float)Math.PI / 2F), yaw / 3.0F, loop, loopEnd, 1.0D, mask, isHighlands);
				return;
			}

			if (random.nextInt(4) != 0) {
				if (!this.func_222702_a(chunkX, chunkZ, offsetX, offsetZ, loop, loopEnd, caveRadius)) {
					return;
				}

				this.func_227208_a_(chunk, biomePos, seed, seaLevel, chunkX, chunkZ, offsetX, startY, offsetZ, cavesize, scale, mask);
			}
		}
	}

	//VanillaCopy of WorldCarver.func_227208_a_, modified to call TFCavesCarver.canCarveArea
	@Override
	protected boolean func_227208_a_(IChunk chunk, Function<BlockPos, Biome> biomePos, long seed, int seaLevel, int chunkX, int chunkZ, double offsetX, double startY, double offsetZ, double sizeVar, double scaledSize, BitSet mask) {
		Random random = new Random(seed + chunkX + chunkZ);
		double d0 = chunkX * 16 + 8;
		double d1 = chunkZ * 16 + 8;

		if (!(offsetX < d0 - 16.0D - sizeVar * 2.0D) && !(offsetZ < d1 - 16.0D - sizeVar * 2.0D) && !(offsetX > d0 + 16.0D + sizeVar * 2.0D) && !(offsetZ > d1 + 16.0D + sizeVar * 2.0D)) {
			int minX = Math.max(MathHelper.floor(offsetX - sizeVar) - chunkX * 16 - 1, 0);
			int maxX = Math.min(MathHelper.floor(offsetX + sizeVar) - chunkX * 16 + 1, 16);
			int minY = Math.max(MathHelper.floor(startY - scaledSize) - 1, 1);
			int maxY = Math.min(MathHelper.floor(startY + scaledSize) + 1, this.maxHeight - 8);
			int minZ = Math.max(MathHelper.floor(offsetZ - sizeVar) - chunkZ * 16 - 1, 0);
			int maxZ = Math.min(MathHelper.floor(offsetZ + sizeVar) - chunkZ * 16 + 1, 16);
			if (this.func_222700_a(chunk, chunkX, chunkZ, minX, maxX, minY, maxY, minZ, maxZ)) {
				return false;
			} else {
				boolean hasHitWater = false;
				BlockPos.Mutable mutable = new BlockPos.Mutable();
				BlockPos.Mutable mutable1 = new BlockPos.Mutable();
				BlockPos.Mutable mutable2 = new BlockPos.Mutable();

				for(int posX = minX; posX < maxX; ++posX) {
					int rX = posX + chunkX * 16;
					double sizeX = (rX + 0.5D - offsetX) / sizeVar;

					for(int posZ = minZ; posZ < maxZ; ++posZ) {
						int rZ = posZ + chunkZ * 16;
						double sizeZ = (rZ + 0.5D - offsetZ) / sizeVar;
						if (!(sizeX * sizeX + sizeZ * sizeZ >= 1.0D)) {
							MutableBoolean mutableboolean = new MutableBoolean(false);

							for(int posY = maxY; posY > minY; --posY) {
								double sizeY = (posY - 0.5D - startY) / scaledSize;
								if (!this.func_222708_a(sizeX, sizeY, sizeZ, posY)) {
									hasHitWater |= this.canCarveArea(chunk, biomePos, mask, random, mutable, mutable1, mutable2, sizeX, sizeY, sizeZ, rX, rZ, posX, posY, posZ, mutableboolean);
								}
							}
						}
					}
				}

				return hasHitWater;
			}
		} else {
			return false;
		}
	}

	//Modified WorldCarver.func_230358_a_
	protected boolean canCarveArea(IChunk chunk, Function<BlockPos, Biome> biomePos, BitSet mask, Random random, BlockPos.Mutable mutable, BlockPos.Mutable mutableUp, BlockPos.Mutable mutableDown, double chunkX, double chunkY, double chunkZ, int randX, int randZ, int posX, int posY, int posZ, MutableBoolean bool) {
		int i = posX | posZ << 4 | posY << 8;
		if (mask.get(i)) {
			return false;
		} else {
			mask.set(i);
			mutable.setPos(randX, posY, randZ);
			BlockState state = chunk.getBlockState(mutable);
			BlockState stateUp = chunk.getBlockState(mutableUp.setAndMove(mutable, Direction.UP));
			if (state.isIn(Blocks.GRASS_BLOCK) || state.isIn(Blocks.MYCELIUM)) {
				bool.setTrue();
			}

			if (!this.canCarveBlock(state, stateUp)) {
				return false;
			} else {
				if (posY < 10) {
					chunk.setBlockState(mutable, WATER.getBlockState(), false);
				} else {
					chunk.setBlockState(mutable, CAVE_AIR, false);
					if (bool.isTrue()) {
						mutableDown.setAndMove(mutable, Direction.DOWN);
						if (chunk.getBlockState(mutableDown).isIn(Blocks.DIRT)) {
							// FIXME chunk.setBlockState(mutableDown, biomePos.apply(mutable).getSurfaceBuilderConfig().getTop(), false);
						}
					}
				}

				return true;
			}

//			if (!canCarveBlock(state, stateUp)) {
//				return false;
//			} else {
//				if (chunkX * chunkX + chunkY * chunkY + chunkZ * chunkZ < 2.85D) {
//					if (posY < 10) {
//						chunk.setBlockState(mutable, WATER.getBlockState(), false);
//					} else {
//						chunk.setBlockState(mutable, CAVE_AIR, false);
//					}
//				} else {
//					Block localBlock = random.nextInt(6) == 0 ? TFBlocks.trollsteinn.get() : Blocks.EMERALD_BLOCK;
//					boolean isHighlands = biomePos.apply(mutable) instanceof TFBiomeHighlands;
//					localBlock = isHighlands ? localBlock : Blocks.DIAMOND_BLOCK;
//					localBlock = bool.booleanValue() ? Blocks.GOLD_BLOCK : localBlock;
//					chunk.setBlockState(mutable, localBlock.getDefaultState(), false);
//					bool.setFalse();
//
//					if (bool.isTrue()) {
//						mutableDown.func_239622_a_(mutable, Direction.DOWN);
//						if (chunk.getBlockState(mutableDown).isIn(Blocks.DIRT)) {
//							chunk.setBlockState(mutableDown, biomePos.apply(mutable).getSurfaceBuilderConfig().getTop(), false);
//						}
//					}
//				}
//
//				return true;
//			}
		}
	}

//	private boolean canReplace(Material material) {
//		return material == Material.EARTH || material == Material.ORGANIC;
//	}

//	@Override
//	protected boolean isCarvable(BlockState state) {
//		return state.isIn(TFBlocks.trollsteinn.get()) || canReplace(state.getMaterial()) || super.isCarvable(state);
//	}

	@Override
	protected boolean func_222708_a(double randX, double randY, double randZ, int posY) {
		return randY > -0.7D && randX * randX + randY * randY + randZ * randZ < 20.0D;
	}

	protected float getCaveSize(Random random) {
		float size = random.nextFloat() * 2.0F + random.nextFloat();
		if (random.nextInt(10) == 0) {
			size *= random.nextFloat() * random.nextFloat() * 3.0F + 1.0F;
		}

		return size;
	}
}
