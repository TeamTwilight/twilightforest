package twilightforest.world.components.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.BlockStateConfiguration;
import twilightforest.block.TFBlocks;
import twilightforest.block.TFPlantBlock;
import twilightforest.block.TrollRootBlock;

import java.util.Random;

public class UndergroundPlantFeature extends Feature<BlockStateConfiguration> {

	private final boolean isTrollber;

	public UndergroundPlantFeature(Codec<BlockStateConfiguration> config, boolean trollber) {
		super(config);
		this.isTrollber = trollber;
	}

	@Override
	public boolean place(FeaturePlaceContext<BlockStateConfiguration> ctx) {
		WorldGenLevel world = ctx.level();
		BlockPos pos = ctx.origin();
		Random random = ctx.random();

		int copyX = pos.getX();
		int copyZ = pos.getZ();

		for (; pos.getY() > world.getMinBuildHeight(); pos = pos.below()) {
			if (world.isEmptyBlock(pos) && random.nextInt(6) > 0) {
				if (!isTrollber && TFPlantBlock.canPlaceRootAt(world, pos)) {
					world.setBlock(pos, ctx.config().state, 16 | 2);
				} else if (isTrollber && TrollRootBlock.canPlaceRootBelow(world, pos.above())) {
					if (random.nextInt(10) == 0) {
						world.setBlock(pos, TFBlocks.UNRIPE_TROLLBER.get().defaultBlockState(), 16 | 2);
					} else {
						world.setBlock(pos, ctx.config().state, 16 | 2);
					}
				}
			} else {
				pos = new BlockPos(
						copyX + random.nextInt(4) - random.nextInt(4),
						pos.getY(),
						copyZ + random.nextInt(4) - random.nextInt(4)
				);
			}
		}
		return true;
	}
}
