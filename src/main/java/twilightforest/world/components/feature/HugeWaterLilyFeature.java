package twilightforest.world.components.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import twilightforest.init.TFBlocks;

/**
 * Generate huge lily pads
 *
 * @author Ben
 */
public class HugeWaterLilyFeature extends Feature<NoneFeatureConfiguration> {

	public HugeWaterLilyFeature(Codec<NoneFeatureConfiguration> config) {
		super(config);
	}

	@Override
	public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> ctx) {
		WorldGenLevel world = ctx.level();
		BlockPos pos = ctx.origin();
		RandomSource random = ctx.random();

		for (int i = 0; i < 4; i++) {
			BlockPos pos_ = pos.offset(
				random.nextInt(8) - random.nextInt(8),
				random.nextInt(4) - random.nextInt(4),
				random.nextInt(8) - random.nextInt(8)
			);

			if (shouldPlacePadAt(world, pos_)) {
				world.setBlock(pos_, TFBlocks.HUGE_WATER_LILY.get().defaultBlockState(), Block.UPDATE_KNOWN_SHAPE | Block.UPDATE_CLIENTS);
			}
		}

		return true;
	}

	private boolean shouldPlacePadAt(LevelAccessor world, BlockPos pos) {
		return world.isEmptyBlock(pos) && world.getBlockState(pos.below()).is(Blocks.WATER);
	}
}
