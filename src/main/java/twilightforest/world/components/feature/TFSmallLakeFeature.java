package twilightforest.world.components.feature;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import org.jspecify.annotations.Nullable;
import twilightforest.tags.TFBlockTags;

import java.util.Optional;

@SuppressWarnings("deprecation")
public record TFSmallLakeFeature(BlockStateProvider fluid, @Nullable BlockStateProvider barrier, @Nullable BlockStateProvider ice) implements Feature {

	private static final BlockState AIR = Blocks.CAVE_AIR.defaultBlockState();

	public static final MapCodec<TFSmallLakeFeature> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
		BlockStateProvider.DIRECT_CODEC
			.fieldOf("fluid")
			.forGetter(TFSmallLakeFeature::fluid),
		BlockStateProvider.DIRECT_CODEC
			.optionalFieldOf("barrier")
			.xmap(optional -> optional.orElse(null), Optional::ofNullable)
			.forGetter(TFSmallLakeFeature::barrier),
		BlockStateProvider.DIRECT_CODEC
			.optionalFieldOf("ice")
			.xmap(optional -> optional.orElse(null), Optional::ofNullable)
			.forGetter(TFSmallLakeFeature::ice)
	).apply(instance, TFSmallLakeFeature::new));

	@Override
	public MapCodec<? extends Feature> codec() {
		return MAP_CODEC;
	}

	@Override
	public boolean place(WorldGenLevel worldGenLevel, ChunkGenerator chunkGenerator, RandomSource randomSource, BlockPos blockPos) {
		if (blockPos.getY() <= worldGenLevel.getMinY() + 4) {
			return false;
		} else {
			blockPos = blockPos.below(4);
			boolean[] booleans = new boolean[2048];
			int i = randomSource.nextInt(4) + 4;

			for (int j = 0; j < i; j++) {
				double d0 = randomSource.nextDouble() * 6.0 + 3.0;
				double d1 = randomSource.nextDouble() * 4.0 + 2.0;
				double d2 = randomSource.nextDouble() * 6.0 + 3.0;
				double d3 = randomSource.nextDouble() * (16.0 - d0 - 2.0) + 1.0 + d0 / 2.0;
				double d4 = randomSource.nextDouble() * (8.0 - d1 - 4.0) + 2.0 + d1 / 2.0;
				double d5 = randomSource.nextDouble() * (16.0 - d2 - 2.0) + 1.0 + d2 / 2.0;

				for (int l = 1; l < 15; l++) {
					for (int i1 = 1; i1 < 15; i1++) {
						for (int j1 = 1; j1 < 7; j1++) {
							double d6 = ((double)l - d3) / (d0 / 2.0);
							double d7 = ((double)j1 - d4) / (d1 / 2.0);
							double d8 = ((double)i1 - d5) / (d2 / 2.0);
							double d9 = d6 * d6 + d7 * d7 + d8 * d8;
							if (d9 < 1.0) {
								booleans[(l * 16 + i1) * 8 + j1] = true;
							}
						}
					}
				}
			}

			BlockState fluidState = fluid().getState(worldGenLevel, randomSource, blockPos);

			for (int x = 0; x < 16; x++) {
				for (int z = 0; z < 16; z++) {
					for (int y = 0; y < 8; y++) {
						boolean flag = !booleans[(x * 16 + z) * 8 + y]
							&& (
							x < 15 && booleans[((x + 1) * 16 + z) * 8 + y]
								|| x > 0 && booleans[((x - 1) * 16 + z) * 8 + y]
								|| z < 15 && booleans[(x * 16 + z + 1) * 8 + y]
								|| z > 0 && booleans[(x * 16 + (z - 1)) * 8 + y]
								|| y < 7 && booleans[(x * 16 + z) * 8 + y + 1]
								|| y > 0 && booleans[(x * 16 + z) * 8 + (y - 1)]
						);
						if (flag) {
							BlockState blockstate3 = worldGenLevel.getBlockState(blockPos.offset(x, y, z));
							if (y >= 4 && blockstate3.liquid()) {
								return false;
							}

							if (y < 4 && !blockstate3.isSolid() && worldGenLevel.getBlockState(blockPos.offset(x, y, z)) != fluidState) {
								return false;
							}
						}
					}
				}
			}

			BlockState iceState = ice() != null ? ice().getState(worldGenLevel, randomSource, blockPos) : null;

			for (int x = 0; x < 16; x++) {
				for (int z = 0; z < 16; z++) {
					for (int y = 0; y < 8; y++) {
						if (booleans[(x * 16 + z) * 8 + y]) {
							BlockPos offset = blockPos.offset(x, y, z);
							if (!(worldGenLevel.getBlockState(offset).is(TFBlockTags.SMALL_LAKES_DONT_REPLACE) ||
								worldGenLevel.getBlockState(offset.above()).is(TFBlockTags.SMALL_LAKES_DONT_REPLACE))) {
								if (y >= 4) {
									worldGenLevel.setBlock(offset, AIR, Block.UPDATE_CLIENTS);
									worldGenLevel.scheduleTick(offset, AIR.getBlock(), 0);
									this.markAboveForPostProcessing(worldGenLevel, offset);
									continue;
								}

								if (y == 3 && iceState != null) {
									worldGenLevel.setBlock(offset, iceState, Block.UPDATE_CLIENTS);
									continue;
								}

								worldGenLevel.setBlock(offset, fluidState, Block.UPDATE_CLIENTS);
							}
						}
					}
				}
			}

			if (barrier() != null) {
				BlockState barrierState = barrier().getState(worldGenLevel, randomSource, blockPos);
				if (!barrierState.isAir()) {
					for (int x = 0; x < 16; x++) {
						for (int z = 0; z < 16; z++) {
							for (int y = 0; y < 8; y++) {
								boolean flag2 = !booleans[(x * 16 + z) * 8 + y]
									&& (
									x < 15 && booleans[((x + 1) * 16 + z) * 8 + y]
										|| x > 0 && booleans[((x - 1) * 16 + z) * 8 + y]
										|| z < 15 && booleans[(x * 16 + z + 1) * 8 + y]
										|| z > 0 && booleans[(x * 16 + (z - 1)) * 8 + y]
										|| y < 7 && booleans[(x * 16 + z) * 8 + y + 1]
										|| y > 0 && booleans[(x * 16 + z) * 8 + (y - 1)]
								);
								if (flag2 && (y < 4 || randomSource.nextInt(2) != 0)) {
									BlockState blockstate = worldGenLevel.getBlockState(blockPos.offset(x, y, z));
									if (blockstate.isSolid() && !blockstate.is(BlockTags.LAVA_POOL_STONE_CANNOT_REPLACE)) {
										BlockPos blockpos3 = blockPos.offset(x, y, z);
										worldGenLevel.setBlock(blockpos3, barrierState, Block.UPDATE_CLIENTS);
										this.markAboveForPostProcessing(worldGenLevel, blockpos3);
									}
								}
							}
						}
					}
				}
			}

			return true;
		}
	}
}
