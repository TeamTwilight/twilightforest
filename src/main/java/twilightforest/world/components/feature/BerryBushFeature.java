package twilightforest.world.components.feature;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.BlockStateConfiguration;
import twilightforest.block.BerryBushBlock;
import twilightforest.block.SnowLoggable;
import twilightforest.data.tags.BlockTagGenerator;
import twilightforest.init.TFBiomes;
import twilightforest.util.TFMathUtil;
import twilightforest.util.WorldUtil;

import java.util.List;

public class BerryBushFeature extends Feature<BlockStateConfiguration> {
	private static final float DEFAULT_RIPE_PROBABILITY = 0.2F;

	public BerryBushFeature(Codec<BlockStateConfiguration> codec) {
		super(codec);
	}

	@Override
	public boolean place(FeaturePlaceContext<BlockStateConfiguration> context) {
		WorldGenLevel level = context.level();
		BlockPos pos = context.origin();
		BlockState stateToPlace = context.config().state;
		RandomSource random = context.random();

		if (!(stateToPlace.getBlock() instanceof BerryBushBlock berryBushBlock))
			return false;

		if (!level.getBlockState(pos.below()).is(berryBushBlock.getSurviveBlockTag()))
			return false;

		boolean isInSnowyBiome = level.getBiome(pos).is(TFBiomes.SNOWY_FOREST);
		switch (chooseSize(random)) {
			case LARGE -> generateLargeNode(level, pos, stateToPlace, random, isInSnowyBiome);
			case MEDIUM -> generateMediumNode(level, pos, stateToPlace, random, isInSnowyBiome);
			case SMALL -> generateSmallNode(level, pos, stateToPlace, random, isInSnowyBiome);
			default -> setBush(level, pos, stateToPlace, random.nextInt(4), isInSnowyBiome);
		}
		return true;
	}

	protected void generateLargeNode(WorldGenLevel level, BlockPos pos, BlockState state, RandomSource random, boolean isInSnowyBiome) {
		for (int dx = -1; dx <= 1; dx++) {
			for (int dz = -1; dz <= 1; dz++) {
				setBush(level, pos.offset(dx, -2, dz), state, random, isInSnowyBiome);
			}
		}

		for (int dx = -2; dx <= 2; dx++) {
			for (int dy = -1; dy <= 0; dy++) {
				for (int dz = -2; dz <= 2; dz++) {
					if (TFMathUtil.taxicabGeometryDistance(dx, dz) < 4)
						setBush(level, pos.offset(dx, dy, dz), state, random, isInSnowyBiome);
				}
			}
		}

		for (int dx = -1; dx <= 1; dx++) {
			for (int dz = -1; dz <= 1; dz++) {
				setBush(level, pos.offset(dx, 1, dz), state, random, isInSnowyBiome);
			}
		}
	}

	protected void generateMediumNode(WorldGenLevel level, BlockPos pos, BlockState state, RandomSource random, boolean isInSnowyBiome) {
		for (int dy = 0; dy <= 2; dy++) {
			int maxChebyshevDistance = 2 - dy;
			for (int dx = -maxChebyshevDistance; dx <= maxChebyshevDistance; dx++) {
				for (int dz = -maxChebyshevDistance; dz <= maxChebyshevDistance; dz++) {
					if (TFMathUtil.chebyshevGeometryDistance(dx, dz) < maxChebyshevDistance || random.nextBoolean())
						setBush(level, pos.offset(dx, dy, dz), state, random, isInSnowyBiome);
				}
			}
		}
	}

	protected void generateSmallNode(WorldGenLevel level, BlockPos pos, BlockState state, RandomSource random, boolean isInSnowyBiome) {
		setBush(level, pos.offset(0, 0, 0), state, random, isInSnowyBiome);
		for (int dx = -1; dx <= 1; dx++) {
			for (int dz = -1; dz <= 1; dz++) {
				if (TFMathUtil.taxicabGeometryDistance(dx, dz) == 1 && random.nextBoolean())
					setBush(level, pos.offset(dx, 0, dz), state, random.nextInt(4), isInSnowyBiome);
			}
		}
	}

	protected void setBush(WorldGenLevel level, BlockPos pos, BlockState state, RandomSource random, boolean isInSnowyBiome) {
		setBush(level, pos, state, random.nextFloat() < DEFAULT_RIPE_PROBABILITY ? 3 : 2, isInSnowyBiome);
	}

	protected void setBush(WorldGenLevel level, BlockPos pos, BlockState state, int age, boolean isInSnowyBiome) {
		BlockState stateToReplace = level.getBlockState(pos);
		if (!stateToReplace.is(BlockTagGenerator.TF_BERRY_BUSHES_SURVIVE) || stateToReplace.is(BlockTags.FEATURES_CANNOT_REPLACE) || !stateToReplace.getFluidState().isEmpty())
			return;
		if (!(state.getBlock() instanceof BerryBushBlock berryBushBlock))
			return;

		if (!level.getBlockState(pos.below()).is(berryBushBlock.getSurviveBlockTag()) && age < 2)
			return;
		BlockState stateToPlace = state.setValue(BlockStateProperties.AGE_3, age);
		if (isInSnowyBiome && !level.getBlockState(pos.below()).is(state.getBlock()))
			stateToPlace = stateToPlace.setValue(SnowLoggable.SNOW_LAYERS, 1);
		level.setBlock(pos, stateToPlace, Block.UPDATE_ALL);
		this.markAboveForPostProcessing(level, pos);

		if (isInSnowyBiome && age >= 2)
			level.setBlock(pos.above(), Blocks.SNOW.defaultBlockState(), Block.UPDATE_ALL);
	}

	protected BushNodeSizes chooseSize(RandomSource random) {
		List<Pair<BushNodeSizes, Float>> weights = List.of(
			Pair.of(BushNodeSizes.LARGE, 1F),
			Pair.of(BushNodeSizes.MEDIUM, 2F),
			Pair.of(BushNodeSizes.SMALL, 4F),
			Pair.of(BushNodeSizes.TINY, 3F)
		);
		return WorldUtil.getRandomElementWithWeights(weights, random);
	}

	protected enum BushNodeSizes {
		TINY,
		SMALL,
		MEDIUM,
		LARGE,
	}
}
