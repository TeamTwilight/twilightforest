package twilightforest.world.components.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import twilightforest.init.TFBlocks;

import static twilightforest.block.HugeLilyPadBlock.FACING;
import static twilightforest.block.HugeLilyPadBlock.PIECE;
import static twilightforest.enums.HugeLilypadPiece.*;

/**
 * Generate huge lily pads
 *
 * @author Ben
 */
public class HugeLilypadFeature extends Feature<NoneFeatureConfiguration> {

	public HugeLilypadFeature(Codec<NoneFeatureConfiguration> config) {
		super(config);
	}

	@Override
	public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> ctx) {
		WorldGenLevel world = ctx.level();
		BlockPos pos = ctx.origin();
		RandomSource random = ctx.random();

		for (int i = 0; i < 10; i++) {
			BlockPos dPos = pos.offset(
				random.nextInt(8) - random.nextInt(8),
				random.nextInt(4) - random.nextInt(4),
				random.nextInt(8) - random.nextInt(8)
			);

			if (shouldPlacePadAt(world, dPos) && world.isAreaLoaded(dPos, 1)) {
				final Direction horizontal = Direction.from2DDataValue(random.nextInt(4));
				final BlockState lilypad = TFBlocks.HUGE_LILY_PAD.get().defaultBlockState().setValue(FACING, horizontal);

				world.setBlock(dPos, lilypad.setValue(PIECE, NW), Block.UPDATE_KNOWN_SHAPE | Block.UPDATE_CLIENTS);
				world.setBlock(dPos.east(), lilypad.setValue(PIECE, NE), Block.UPDATE_KNOWN_SHAPE | Block.UPDATE_CLIENTS);
				world.setBlock(dPos.east().south(), lilypad.setValue(PIECE, SE), Block.UPDATE_KNOWN_SHAPE | Block.UPDATE_CLIENTS);
				world.setBlock(dPos.south(), lilypad.setValue(PIECE, SW), Block.UPDATE_KNOWN_SHAPE | Block.UPDATE_CLIENTS);
			}
		}

		return true;
	}

	private boolean shouldPlacePadAt(LevelAccessor world, BlockPos pos) {
		return world.isEmptyBlock(pos) && world.getBlockState(pos.below()).is(Blocks.WATER)
			&& world.isEmptyBlock(pos.east()) && world.getBlockState(pos.east().below()).is(Blocks.WATER)
			&& world.isEmptyBlock(pos.south()) && world.getBlockState(pos.south().below()).is(Blocks.WATER)
			&& world.isEmptyBlock(pos.east().south()) && world.getBlockState(pos.east().south().below()).is(Blocks.WATER);
	}
}
