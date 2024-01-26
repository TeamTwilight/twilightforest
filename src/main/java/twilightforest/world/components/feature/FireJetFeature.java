package twilightforest.world.components.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.BlockStateConfiguration;
import twilightforest.util.FeatureUtil;

public class FireJetFeature extends Feature<BlockStateConfiguration> {

	public FireJetFeature(Codec<BlockStateConfiguration> configIn) {
		super(configIn);
	}

	@Override
	public boolean place(FeaturePlaceContext<BlockStateConfiguration> ctx) {
		WorldGenLevel world = ctx.level();
		BlockPos pos = ctx.origin();
		RandomSource rand = ctx.random();
		BlockStateConfiguration config = ctx.config();

		if(!FeatureUtil.isAreaSuitable(world, pos, 5, 2, 5)) return false;

		for (int i = 0; i < 4; ++i) {
			BlockPos dPos = pos.offset(
					rand.nextInt(8) - rand.nextInt(8),
					rand.nextInt(4) - rand.nextInt(4),
					rand.nextInt(8) - rand.nextInt(8)
			);

			if (world.isEmptyBlock(dPos) && world.canSeeSkyFromBelowWater(dPos) && world.getBlockState(dPos.below()).is(BlockTags.DIRT)
					&& world.getBlockState(dPos.east().below()).is(BlockTags.DIRT) && world.getBlockState(dPos.west().below()).is(BlockTags.DIRT)
					&& world.getBlockState(dPos.south().below()).is(BlockTags.DIRT) && world.getBlockState(dPos.north().below()).is(BlockTags.DIRT)) {

				//create blocks around the jet/smoker, just in case
				for (int gx = -2; gx <= 2; gx++) {
					for (int gz = -2; gz <= 2; gz++) {
						BlockPos grassPos = dPos.offset(gx, -1, gz);
						world.setBlock(grassPos, Blocks.GRASS_BLOCK.defaultBlockState(), 0);
					}
				}

				// jet
				world.setBlock(dPos.below(), config.state, 0);

				// create reservoir with stone walls
				for (int rx = -2; rx <= 2; rx++) {
					for (int rz = -2; rz <= 2; rz++) {
						BlockPos dPos2 = dPos.offset(rx, -2, rz);
						if ((rx == 1 || rx == 0 || rx == -1) && (rz == 1 || rz == 0 || rz == -1)) {
							// lava reservoir
							world.setBlock(dPos2, Blocks.LAVA.defaultBlockState(), 0);
						} else if (!world.getBlockState(dPos2).is(Blocks.LAVA)) {
							// only stone where there is no lava
							world.setBlock(dPos2, Blocks.STONE.defaultBlockState(), 0);
						}
						world.setBlock(dPos2.below(), Blocks.STONE.defaultBlockState(), 0);
					}
				}
			}
		}

		return true;
	}
}
