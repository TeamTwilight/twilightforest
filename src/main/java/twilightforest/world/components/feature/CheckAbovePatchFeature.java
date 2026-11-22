package twilightforest.world.components.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.IntProviders;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import twilightforest.init.TFBlocks;

//[VanillaCopy] of BaseDiskFeature, but we add a check to make sure the block above is air
public record CheckAbovePatchFeature(BlockStateProvider stateProvider, BlockPredicate target, IntProvider radius, int halfHeight) implements Feature {

	public static final MapCodec<CheckAbovePatchFeature> MAP_CODEC = RecordCodecBuilder.mapCodec(
		instance -> instance.group(
				BlockStateProvider.DIRECT_CODEC
					.fieldOf("state_provider")
					.forGetter(CheckAbovePatchFeature::stateProvider),
				BlockPredicate.CODEC
					.fieldOf("target")
					.forGetter(CheckAbovePatchFeature::target),
				IntProviders.codec(0, 8)
					.fieldOf("radius")
					.forGetter(CheckAbovePatchFeature::radius),
				Codec.intRange(0, 4)
					.fieldOf("half_height")
					.forGetter(CheckAbovePatchFeature::halfHeight)
			).apply(instance, CheckAbovePatchFeature::new));

	@Override
	public MapCodec<? extends Feature> codec() {
		return MAP_CODEC;
	}

	@Override
	public boolean place(WorldGenLevel worldGenLevel, ChunkGenerator chunkGenerator, RandomSource randomSource, BlockPos blockPos) {
		boolean flag = false;
		int i = blockPos.getY();
		int j = i + halfHeight();
		int k = i - halfHeight() - 1;
		int l = radius().sample(randomSource);
		BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

		for (BlockPos blockpos1 : BlockPos.betweenClosed(blockPos.offset(-l, 0, -l), blockPos.offset(l, 0, l))) {
			int i1 = blockpos1.getX() - blockPos.getX();
			int j1 = blockpos1.getZ() - blockPos.getZ();
			if (i1 * i1 + j1 * j1 <= l * l) {
				flag |= this.placeColumn(stateProvider(), target(), worldGenLevel, randomSource, j, k, blockpos$mutableblockpos.set(blockpos1));
			}
		}

		return flag;
	}

	private boolean placeColumn(BlockStateProvider stateProvider, BlockPredicate target, WorldGenLevel level, RandomSource random, int start, int end, BlockPos.MutableBlockPos mutablePos) {
		boolean flag = false;

		for (int i = start; i > end; --i) {
			mutablePos.setY(i);
			BlockState mutablePosAbove = level.getBlockState(mutablePos.above());

			boolean canAboveReplace = mutablePosAbove.canBeReplaced() || mutablePosAbove.is(BlockTags.FLOWERS) || mutablePosAbove.is(TFBlocks.MAYAPPLE) ;
			boolean canAboveSurvive = mutablePosAbove.canSurvive(level, mutablePos);

			if (target.test(level, mutablePos) && (canAboveReplace || canAboveSurvive)) {

				BlockState blockstate1 = stateProvider.getState(level, random, mutablePos);
				level.setBlock(mutablePos, blockstate1, Block.UPDATE_ALL);
				if (canAboveReplace) {
					level.destroyBlock(mutablePos.above(), false);
					this.markAboveForPostProcessing(level, mutablePos.above());
				}
				this.markAboveForPostProcessing(level, mutablePos);
				flag = true;
			}
		}

		return flag;
	}
}
