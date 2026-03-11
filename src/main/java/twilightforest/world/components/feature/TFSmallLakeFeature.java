package twilightforest.world.components.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import org.jetbrains.annotations.Nullable;
import twilightforest.data.tags.BlockTagGenerator;

import java.util.Optional;

@SuppressWarnings("deprecation")
public class TFSmallLakeFeature extends Feature<TFSmallLakeFeature.Configuration> {
	private static final BlockState AIR = Blocks.CAVE_AIR.defaultBlockState();

	public TFSmallLakeFeature(Codec<TFSmallLakeFeature.Configuration> codec) {
		super(codec);
	}

	@Override
	public boolean place(FeaturePlaceContext<TFSmallLakeFeature.Configuration> context) {
		BlockPos blockpos = context.origin();
		WorldGenLevel worldgenlevel = context.level();
		RandomSource randomsource = context.random();
		TFSmallLakeFeature.Configuration config = context.config();

		if (blockpos.getY() <= worldgenlevel.getMinBuildHeight() + 4) {
			return false;
		} else {
			blockpos = blockpos.below(4);
			boolean[] booleans = new boolean[2048];
			int i = randomsource.nextInt(4) + 4;

			for (int j = 0; j < i; j++) {
				double d0 = randomsource.nextDouble() * 6.0 + 3.0;
				double d1 = randomsource.nextDouble() * 4.0 + 2.0;
				double d2 = randomsource.nextDouble() * 6.0 + 3.0;
				double d3 = randomsource.nextDouble() * (16.0 - d0 - 2.0) + 1.0 + d0 / 2.0;
				double d4 = randomsource.nextDouble() * (8.0 - d1 - 4.0) + 2.0 + d1 / 2.0;
				double d5 = randomsource.nextDouble() * (16.0 - d2 - 2.0) + 1.0 + d2 / 2.0;

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

			BlockState fluidState = config.fluid().getState(randomsource, blockpos);

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
							BlockState blockstate3 = worldgenlevel.getBlockState(blockpos.offset(x, y, z));
							if (y >= 4 && blockstate3.liquid()) {
								return false;
							}

							if (y < 4 && !blockstate3.isSolid() && worldgenlevel.getBlockState(blockpos.offset(x, y, z)) != fluidState) {
								return false;
							}
						}
					}
				}
			}

			BlockState iceState = config.ice != null ? config.ice.getState(randomsource, blockpos) : null;

			for (int x = 0; x < 16; x++) {
				for (int z = 0; z < 16; z++) {
					for (int y = 0; y < 8; y++) {
						if (booleans[(x * 16 + z) * 8 + y]) {
							BlockPos offset = blockpos.offset(x, y, z);
							if (!(worldgenlevel.getBlockState(offset).is(BlockTagGenerator.SMALL_LAKES_DONT_REPLACE) ||
								worldgenlevel.getBlockState(offset.above()).is(BlockTagGenerator.SMALL_LAKES_DONT_REPLACE))) {
								if (y >= 4) {
									worldgenlevel.setBlock(offset, AIR, Block.UPDATE_CLIENTS);
                                    worldgenlevel.scheduleTick(offset, AIR.getBlock(), 0);
                                    this.markAboveForPostProcessing(worldgenlevel, offset);
									continue;
                                }

								if (y == 3 && iceState != null) {
									worldgenlevel.setBlock(offset, iceState, Block.UPDATE_CLIENTS);
									continue;
								}

								worldgenlevel.setBlock(offset, fluidState, Block.UPDATE_CLIENTS);
                            }
						}
					}
				}
			}

			if (config.barrier() != null) {
				BlockState barrierState = config.barrier().getState(randomsource, blockpos);
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
								if (flag2 && (y < 4 || randomsource.nextInt(2) != 0)) {
									BlockState blockstate = worldgenlevel.getBlockState(blockpos.offset(x, y, z));
									if (blockstate.isSolid() && !blockstate.is(BlockTags.LAVA_POOL_STONE_CANNOT_REPLACE)) {
										BlockPos blockpos3 = blockpos.offset(x, y, z);
										worldgenlevel.setBlock(blockpos3, barrierState, Block.UPDATE_CLIENTS);
										this.markAboveForPostProcessing(worldgenlevel, blockpos3);
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

	public record Configuration(BlockStateProvider fluid, @Nullable BlockStateProvider barrier, @Nullable BlockStateProvider ice) implements FeatureConfiguration {
		public static final Codec<TFSmallLakeFeature.Configuration> CODEC = RecordCodecBuilder.create(
			instance -> instance.group(
					BlockStateProvider.CODEC.fieldOf("fluid").forGetter(Configuration::fluid),
					BlockStateProvider.CODEC.optionalFieldOf("barrier").forGetter(configuration -> Optional.ofNullable(configuration.barrier())),
					BlockStateProvider.CODEC.optionalFieldOf("ice").forGetter(configuration -> Optional.ofNullable(configuration.ice()))
				)
				.apply(instance, TFSmallLakeFeature.Configuration::new)
		);

		@SuppressWarnings("OptionalUsedAsFieldOrParameterType") // Vanilla does this shit too
		private Configuration(BlockStateProvider fluid, Optional<BlockStateProvider> barrier, Optional<BlockStateProvider> ice) {
			this(fluid, barrier.orElse(null), ice.orElse(null));
		}
	}
}
