package twilightforest.world.components.feature;

import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import twilightforest.block.TFBlocks;

import java.util.Random;

import static twilightforest.block.HugeLilyPadBlock.FACING;
import static twilightforest.block.HugeLilyPadBlock.PIECE;
import static twilightforest.enums.HugeLilypadPiece.NE;
import static twilightforest.enums.HugeLilypadPiece.NW;
import static twilightforest.enums.HugeLilypadPiece.SE;
import static twilightforest.enums.HugeLilypadPiece.SW;

/**
 * Generate huge lily pads
 *
 * @author Ben
 */
public class TFGenHugeLilyPad extends Feature<NoneFeatureConfiguration> {

	public TFGenHugeLilyPad(Codec<NoneFeatureConfiguration> config) {
		super(config);
	}

	@Override
	public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> ctx) {
		WorldGenLevel world = ctx.level();
		BlockPos pos = ctx.origin();
		Random random = ctx.random();

		for (int i = 0; i < 10; i++) {
			BlockPos dPos = pos.offset(
					random.nextInt(8) - random.nextInt(8),
					random.nextInt(4) - random.nextInt(4),
					random.nextInt(8) - random.nextInt(8)
			);

			if (shouldPlacePadAt(world, dPos) && world.isAreaLoaded(dPos, 1)) {
				final Direction horizontal = Direction.from2DDataValue(random.nextInt(4));
				final BlockState lilypad = TFBlocks.HUGE_LILY_PAD.get().defaultBlockState().setValue(FACING, horizontal);

				world.setBlock(dPos, lilypad.setValue(PIECE, NW), 16 | 2);
				world.setBlock(dPos.east(), lilypad.setValue(PIECE, NE), 16 | 2);
				world.setBlock(dPos.east().south(), lilypad.setValue(PIECE, SE), 16 | 2);
				world.setBlock(dPos.south(), lilypad.setValue(PIECE, SW), 16 | 2);
			}
		}

		return true;
	}

	private boolean shouldPlacePadAt(LevelAccessor world, BlockPos pos) {
		return world.isEmptyBlock(pos) && world.getBlockState(pos.below()).getMaterial() == Material.WATER
				&& world.isEmptyBlock(pos.east()) && world.getBlockState(pos.east().below()).getMaterial() == Material.WATER
				&& world.isEmptyBlock(pos.south()) && world.getBlockState(pos.south().below()).getMaterial() == Material.WATER
				&& world.isEmptyBlock(pos.east().south()) && world.getBlockState(pos.east().south().below()).getMaterial() == Material.WATER;
	}
}
