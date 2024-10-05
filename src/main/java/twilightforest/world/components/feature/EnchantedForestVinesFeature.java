package twilightforest.world.components.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.VineBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import twilightforest.init.TFBlocks;

public class EnchantedForestVinesFeature extends Feature<NoneFeatureConfiguration> {
	public EnchantedForestVinesFeature(Codec<NoneFeatureConfiguration> codec) {super(codec);}

	@Override
	public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
		WorldGenLevel world = context.level();
		BlockPos pos = context.origin();

		if (!world.isEmptyBlock(pos))
			return false;

		BlockState state = Blocks.VINE.defaultBlockState();
		boolean empty = true;
		for (Direction dir : Direction.values()) {
			BlockPos relativePos = pos.relative(dir);
			if (dir != Direction.DOWN && VineBlock.isAcceptableNeighbour(world, relativePos, dir) && !world.getBlockState(relativePos).is(TFBlocks.RAINBOW_OAK_LEAVES.get())) {
				state = state.setValue(VineBlock.getPropertyForFace(dir), true);
				if (dir.getAxis() != Direction.Axis.Y) {
					empty = false;
				}
			}
		}
		if (empty)
			return false;

		world.setBlock(pos, state, Block.UPDATE_CLIENTS);
		placeVerticalVines(world, pos, state.setValue(VineBlock.getPropertyForFace(Direction.UP), false), context.random().nextInt(10));
		return true;
	}

	private void placeVerticalVines(WorldGenLevel world, BlockPos pos, BlockState blockState, int height) {
		for (int i = 1; i < height; i++) {
			BlockPos belowPos = pos.below(i);
			if (!world.isEmptyBlock(belowPos) && !world.getBlockState(belowPos).is(Blocks.VINE))
				break;

			world.setBlock(belowPos, blockState, Block.UPDATE_CLIENTS);
		}
	}
}
