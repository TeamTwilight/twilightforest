package twilightforest.world.components.feature;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.Feature;
import twilightforest.block.SnowLoggable;
import twilightforest.tags.TFBlockTags;
import twilightforest.util.TFMathUtil;
import twilightforest.util.WorldUtil;

import java.util.List;

public record BerryBushFeature(BlockState bushState, TagKey<Block> placesOn, boolean canBeSnowy) implements Feature {

	private static final float DEFAULT_RIPE_PROBABILITY = 0.2F;

	public static final MapCodec<BerryBushFeature> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
		BlockState.CODEC
			.fieldOf("bush")
			.forGetter(BerryBushFeature::bushState),
		TagKey.codec(Registries.BLOCK)
			.fieldOf("generates_on")
			.forGetter(BerryBushFeature::placesOn),
		Codec.BOOL
			.fieldOf("can_generate_snowy")
			.forGetter(BerryBushFeature::canBeSnowy)
	).apply(instance, BerryBushFeature::new));

	@Override
	public MapCodec<? extends Feature> codec() {
		return MAP_CODEC;
	}

	@Override
	public boolean place(WorldGenLevel worldGenLevel, ChunkGenerator chunkGenerator, RandomSource randomSource, BlockPos blockPos) {
		if (!worldGenLevel.getBlockState(blockPos.below()).is(placesOn()))
			return false;

		boolean isInSnowyBiome = canBeSnowy() && worldGenLevel.getBiome(blockPos).value().shouldSnow(worldGenLevel, blockPos);
		return switch (this.chooseSize(randomSource)) {
			case LARGE -> this.generateLargeNode(worldGenLevel, blockPos, bushState(), placesOn(), randomSource, isInSnowyBiome);
			case MEDIUM -> this.generateMediumNode(worldGenLevel, blockPos, bushState(), placesOn(), randomSource, isInSnowyBiome);
			case SMALL -> this.generateSmallNode(worldGenLevel, blockPos, bushState(), placesOn(), randomSource, isInSnowyBiome);
			default -> this.setBush(worldGenLevel, blockPos, bushState(), placesOn(), randomSource.nextInt(4), isInSnowyBiome);
		};
	}

	private boolean generateLargeNode(WorldGenLevel level, BlockPos pos, BlockState state, TagKey<Block> generatesOn, RandomSource random, boolean isInSnowyBiome) {
		boolean placed = false;
		for (int dx = -1; dx <= 1; dx++) {
			for (int dz = -1; dz <= 1; dz++) {
				placed |= this.setBush(level, pos.offset(dx, -2, dz), state, generatesOn, random, isInSnowyBiome);
			}
		}

		for (int dx = -2; dx <= 2; dx++) {
			for (int dy = -1; dy <= 0; dy++) {
				for (int dz = -2; dz <= 2; dz++) {
					if (TFMathUtil.taxicabGeometryDistance(dx, dz) < 4)
						placed |= this.setBush(level, pos.offset(dx, dy, dz), state, generatesOn, random, isInSnowyBiome);
				}
			}
		}

		for (int dx = -1; dx <= 1; dx++) {
			for (int dz = -1; dz <= 1; dz++) {
				placed |= this.setBush(level, pos.offset(dx, 1, dz), state, generatesOn, random, isInSnowyBiome);
			}
		}
		return placed;
	}

	private boolean generateMediumNode(WorldGenLevel level, BlockPos pos, BlockState state, TagKey<Block> generatesOn, RandomSource random, boolean isInSnowyBiome) {
		boolean placed = false;
		for (int dy = -1; dy <= 2; dy++) {
			int maxTaxicabDistance = Math.min(2 - dy, 2);
			for (int dx = -maxTaxicabDistance; dx <= maxTaxicabDistance; dx++) {
				for (int dz = -maxTaxicabDistance; dz <= maxTaxicabDistance; dz++) {
					if (TFMathUtil.taxicabGeometryDistance(dx, dz) < 2 * maxTaxicabDistance || random.nextBoolean())
						placed |= this.setBush(level, pos.offset(dx, dy, dz), state, generatesOn, random, isInSnowyBiome);
				}
			}
		}
		return placed;
	}

	private boolean generateSmallNode(WorldGenLevel level, BlockPos pos, BlockState state, TagKey<Block> generatesOn, RandomSource random, boolean isInSnowyBiome) {
		boolean placed = this.setBush(level, pos.offset(0, 0, 0), state, generatesOn, random, isInSnowyBiome);
		for (int dx = -1; dx <= 1; dx++) {
			for (int dy = -1; dy <= 0; dy++) {
				for (int dz = -1; dz <= 1; dz++) {
					if (TFMathUtil.taxicabGeometryDistance(dx, dz) == 1 && random.nextBoolean())
						placed |= this.setBush(level, pos.offset(dx, dy, dz), state, generatesOn, random.nextInt(4), isInSnowyBiome);
				}
			}
		}
		return placed;
	}

	private boolean setBush(WorldGenLevel level, BlockPos pos, BlockState state, TagKey<Block> generatesOn, RandomSource random, boolean isInSnowyBiome) {
		return this.setBush(level, pos, state, generatesOn, random.nextFloat() < DEFAULT_RIPE_PROBABILITY ? 3 : 2, isInSnowyBiome);
	}

	private boolean setBush(WorldGenLevel level, BlockPos pos, BlockState state, TagKey<Block> generatesOn, int age, boolean isInSnowyBiome) {
		BlockState stateToReplace = level.getBlockState(pos);
		if (!stateToReplace.is(TFBlockTags.TF_BERRY_BUSHES_REPLACE) || stateToReplace.is(BlockTags.FEATURES_CANNOT_REPLACE) || !stateToReplace.getFluidState().isEmpty())
			return false;

		if (!level.getBlockState(pos.below()).is(generatesOn) && age < 2)
			return false;

		BlockState stateToPlace = state.trySetValue(BlockStateProperties.AGE_3, age);
		if (isInSnowyBiome && !level.getBlockState(pos.below()).is(state.getBlock()))
			stateToPlace = stateToPlace.trySetValue(SnowLoggable.SNOW_LAYERS, 1);
		level.setBlock(pos, stateToPlace, Block.UPDATE_ALL);
		this.markAboveForPostProcessing(level, pos);

		if (isInSnowyBiome && age >= 2)
			level.setBlock(pos.above(), Blocks.SNOW.defaultBlockState(), Block.UPDATE_ALL);

		return true;
	}

	private BushNodeSizes chooseSize(RandomSource random) {
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
		LARGE
	}
}
