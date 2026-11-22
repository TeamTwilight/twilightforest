package twilightforest.world.components.feature;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.Feature;
import twilightforest.util.features.FeatureUtil;

public record FireJetFeature(BlockState state) implements Feature {

	public static final MapCodec<FireJetFeature> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
		BlockState.CODEC.fieldOf("state").forGetter(FireJetFeature::state)
	).apply(instance, FireJetFeature::new));

	@Override
	public MapCodec<? extends Feature> codec() {
		return MAP_CODEC;
	}

	@Override
	public boolean place(WorldGenLevel worldGenLevel, ChunkGenerator chunkGenerator, RandomSource randomSource, BlockPos blockPos) {
		if (!FeatureUtil.isAreaSuitable(worldGenLevel, blockPos, 5, 2, 5))
			return false;

		for (int i = 0; i < 4; ++i) {
			BlockPos dPos = blockPos.offset(
				randomSource.nextInt(8) - randomSource.nextInt(8),
				randomSource.nextInt(4) - randomSource.nextInt(4),
				randomSource.nextInt(8) - randomSource.nextInt(8)
			);

			if (worldGenLevel.isEmptyBlock(dPos) && worldGenLevel.canSeeSkyFromBelowWater(dPos) && worldGenLevel.getBlockState(dPos.below()).is(BlockTags.SUBSTRATE_OVERWORLD)
				&& worldGenLevel.getBlockState(dPos.east().below()).is(BlockTags.SUBSTRATE_OVERWORLD) && worldGenLevel.getBlockState(dPos.west().below()).is(BlockTags.SUBSTRATE_OVERWORLD)
				&& worldGenLevel.getBlockState(dPos.south().below()).is(BlockTags.SUBSTRATE_OVERWORLD) && worldGenLevel.getBlockState(dPos.north().below()).is(BlockTags.SUBSTRATE_OVERWORLD)) {

				//create blocks around the jet/smoker, just in case
				for (int gx = -2; gx <= 2; gx++) {
					for (int gz = -2; gz <= 2; gz++) {
						BlockPos grassPos = dPos.offset(gx, -1, gz);
						worldGenLevel.setBlock(grassPos, Blocks.GRASS_BLOCK.defaultBlockState(), 0);
					}
				}

				// jet
				worldGenLevel.setBlock(dPos.below(), state(), 0);

				// create reservoir with stone walls
				for (int rx = -2; rx <= 2; rx++) {
					for (int rz = -2; rz <= 2; rz++) {
						BlockPos dPos2 = dPos.offset(rx, -2, rz);
						if ((rx == 1 || rx == 0 || rx == -1) && (rz == 1 || rz == 0 || rz == -1)) {
							// lava reservoir
							worldGenLevel.setBlock(dPos2, Blocks.LAVA.defaultBlockState(), 0);
						} else if (!worldGenLevel.getBlockState(dPos2).is(Blocks.LAVA)) {
							// only stone where there is no lava
							worldGenLevel.setBlock(dPos2, Blocks.STONE.defaultBlockState(), 0);
						}
						worldGenLevel.setBlock(dPos2.below(), Blocks.STONE.defaultBlockState(), 0);
					}
				}
			}
		}

		return true;
	}
}
