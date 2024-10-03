package twilightforest.world.components.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.VineBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import twilightforest.init.TFBlocks;

import java.util.*;

public class EnchantedForestVinesFeature extends Feature<NoneFeatureConfiguration> {
	public EnchantedForestVinesFeature(Codec<NoneFeatureConfiguration> codec) {super(codec);}

	@Override
	public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
		WorldGenLevel world = context.level();
		RandomSource random = context.random();
		BlockPos pos = context.origin();

		Optional<Set<Direction>> directionsOptional = placeVerticalVines(world, random, pos);
		if (directionsOptional.isEmpty()) {
			return false;
		}

		Set<Direction> directions = directionsOptional.get();
		for (Direction direction : directions) {
			tryPlaceAdditionalVines(world, random, pos, direction);
		}

		return true;
	}

	private void tryPlaceAdditionalVines(WorldGenLevel world, RandomSource random, BlockPos pos, Direction direction) {
		tryPlaceVines(world, random, pos.relative(direction.getClockWise()), direction.getClockWise());
		tryPlaceVines(world, random, pos.relative(direction.getCounterClockWise()), direction.getCounterClockWise());
	}

	private void tryPlaceVines(WorldGenLevel world, RandomSource random, BlockPos pos, Direction direction) {
		if (random.nextBoolean()) {
			Optional<Set<Direction>> directionsOptional = placeVerticalVines(world, random, pos);
			if (directionsOptional.isPresent()) {
				placeVerticalVines(world, random, pos.relative(direction));
			}
		}
	}

	private Optional<Set<Direction>> placeVerticalVines(WorldGenLevel world, RandomSource random, BlockPos pos) {
		if (!world.isEmptyBlock(pos)) return Optional.empty();

		BlockState state = Blocks.VINE.defaultBlockState();
		EnumSet<Direction> directions = EnumSet.noneOf(Direction.class);
		boolean isTree = true;

		for (Direction dir : Direction.values()) {
			BlockPos relativePos = pos.relative(dir);
			if (isValidDirection(world, relativePos, dir)) {
				state = state.setValue(VineBlock.getPropertyForFace(dir), true);
				isTree &= isTree(world.getBlockState(relativePos));
				if (dir.getAxis().isHorizontal()) directions.add(dir);
			}
		}

		if (directions.isEmpty()) return Optional.empty();

		world.setBlock(pos, state, Block.UPDATE_CLIENTS);
		int length = isTree ? 1 : random.nextInt(1, 6);
		placeVerticalVines(world, pos, state.setValue(VineBlock.getPropertyForFace(Direction.UP), false), length);
		return Optional.of(directions);
	}

	private boolean isValidDirection(WorldGenLevel world, BlockPos pos, Direction direction) {
		return direction != Direction.DOWN &&
			VineBlock.isAcceptableNeighbour(world, pos, direction) &&
			!world.getBlockState(pos).is(TFBlocks.RAINBOW_OAK_LEAVES.get());
	}

	private void placeVerticalVines(WorldGenLevel world, BlockPos pos, BlockState state, int length) {
		for (int i = 1; i < length; i++) {
			BlockPos belowPos = pos.below(i);
			if (!world.isEmptyBlock(belowPos) && !world.getBlockState(belowPos).is(Blocks.VINE)) break;
			world.setBlock(belowPos, state, Block.UPDATE_CLIENTS);
		}
	}

	private boolean isTree(BlockState state) {
		return state.is(BlockTags.LOGS) || state.is(BlockTags.LEAVES);
	}
}
