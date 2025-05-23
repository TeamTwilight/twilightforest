package twilightforest.world.components.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.PipeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import twilightforest.block.HangingWebBlock;
import twilightforest.block.WebwormBlock;
import twilightforest.enums.WebShape;
import twilightforest.init.TFBlocks;
import twilightforest.tags.TFBlockTags;

public class WebFeature extends Feature<NoneFeatureConfiguration> {

	public WebFeature(Codec<NoneFeatureConfiguration> config) {
		super(config);
	}

	private static boolean isValid(WorldGenLevel level, BlockPos pos, RandomSource random) {
		BlockState state = level.getBlockState(pos);
		if (state.is(TFBlockTags.WEBS_GENERATE_ON_TOP_OF)) return true;
		return state.is(TFBlockTags.WEBS_GENERATE_HANGING_FROM) && (level.isEmptyBlock(pos.below()) || (level.isEmptyBlock(pos.below(2)) && random.nextFloat() <= 0.25F));
	}

	private static boolean placeAndUpdate(WorldGenLevel level, BlockPos pos, Direction direction, RandomSource random, boolean canBeWorm) {
		boolean flag = placeOrAdd(level, pos, direction, random, canBeWorm);
		if (flag) level.getChunk(pos).markPosForPostprocessing(pos);
		return flag;
	}

	private static boolean placeOrAdd(WorldGenLevel level, BlockPos pos, Direction direction, RandomSource random, boolean canBeWorm) {
		if (level.isEmptyBlock(pos)) {
			if (canBeWorm && random.nextFloat() <= 0.25F) {
				return level.setBlock(pos, TFBlocks.WEBWORM.get().defaultBlockState().setValue(PipeBlock.PROPERTY_BY_DIRECTION.get(direction.getOpposite()), true).setValue(WebwormBlock.FACING, direction), Block.UPDATE_CLIENTS);
			}
			if (direction.getAxis().isHorizontal()) {
				return level.setBlock(pos, TFBlocks.HANGING_WEB.get().defaultBlockState().setValue(HangingWebBlock.getPropertyForFace(direction), WebShape.TALL), Block.UPDATE_CLIENTS);
			} else {
				return level.setBlock(pos, TFBlocks.HANGING_WEB.get().defaultBlockState().setValue(PipeBlock.PROPERTY_BY_DIRECTION.get(direction), true), Block.UPDATE_CLIENTS);
			}
		}
		BlockState state = level.getBlockState(pos);
		if (state.is(TFBlocks.HANGING_WEB)) {
			if (canBeWorm && random.nextFloat() <= 0.25F) {
				BlockState defaultState = TFBlocks.WEBWORM.get().defaultBlockState().setValue(WebwormBlock.FACING, direction.getOpposite());

				for (Direction face : Direction.values()) {
					defaultState = defaultState.setValue(PipeBlock.PROPERTY_BY_DIRECTION.get(face), HangingWebBlock.checkFace(state, face));
				}

				return level.setBlock(pos, defaultState, HangingWebBlock.UPDATE_CLIENTS);
			}

			if (direction.getAxis().isHorizontal()) {
				return level.setBlock(pos, state.setValue(HangingWebBlock.getPropertyForFace(direction), WebShape.TALL), Block.UPDATE_CLIENTS);
			} else {
				return level.setBlock(pos, state.setValue(PipeBlock.PROPERTY_BY_DIRECTION.get(direction), true), Block.UPDATE_CLIENTS);
			}
		}

		if (state.is(TFBlocks.WEBWORM)) {
			return level.setBlock(pos, state.setValue(PipeBlock.PROPERTY_BY_DIRECTION.get(direction), true), Block.UPDATE_CLIENTS);
		}
		return false;
	}

	@Override
	public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> config) {
		WorldGenLevel level = config.level();
		RandomSource random = config.random();
		BlockPos pos = config.origin().above(random.nextInt(level.getMaxY() - config.origin().getY()));
		while (pos.getY() > config.origin().getY()) {
			pos = pos.below();
			if (isValid(level, pos, random)) {
				int count = 0;

				for (Direction direction : Direction.Plane.HORIZONTAL.shuffledCopy(random)) {
					BlockPos.MutableBlockPos relative = pos.relative(direction).mutable();
					Direction opposite = direction.getOpposite();
					if (random.nextInt(4) + 1 > count && HangingWebBlock.isAcceptableNeighbour(level, pos, opposite) && placeAndUpdate(level, relative, opposite, random, true)) {
						count++;
						int length = WebwormBlock.getWebLengthForPos(relative, opposite) - 1;
						for (int i = 0; i < length; i++) {
							relative.move(Direction.DOWN);
							if (!placeAndUpdate(level, relative, opposite, random, false)) break;
						}
					}
				}

				if (count > 1 && random.nextFloat() <= 0.33F && HangingWebBlock.isAcceptableNeighbour(level, pos, Direction.DOWN)) placeAndUpdate(level, pos.above(), Direction.DOWN, random, true);
				return count > 0;
			}
		}

		return false;
	}
}
