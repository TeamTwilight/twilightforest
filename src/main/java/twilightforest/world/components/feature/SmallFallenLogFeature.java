package twilightforest.world.components.feature;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.Feature;
import twilightforest.block.HorizontalHollowLogBlock;
import twilightforest.enums.HollowLogVariants;
import twilightforest.init.TFBlocks;
import twilightforest.util.features.FeatureUtil;

public record SmallFallenLogFeature(BlockState normal, BlockState hollow) implements Feature {

	public static final MapCodec<SmallFallenLogFeature> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
		BlockState.CODEC
			.fieldOf("normal")
			.forGetter(SmallFallenLogFeature::normal),
		BlockState.CODEC
			.fieldOf("hollow")
			.forGetter(SmallFallenLogFeature::hollow)
	).apply(instance, SmallFallenLogFeature::new));

	@Override
	public MapCodec<? extends Feature> codec() {
		return MAP_CODEC;
	}

	@Override
	public boolean place(WorldGenLevel worldGenLevel, ChunkGenerator chunkGenerator, RandomSource randomSource, BlockPos blockPos) {
		boolean shouldMakeAllHollow = randomSource.nextBoolean();

		// determine direction
		boolean goingX = randomSource.nextBoolean();

		// length
		int length = randomSource.nextInt(4) + 3;

		// check area clear
		if (goingX) {
			if (!FeatureUtil.isAreaSuitable(worldGenLevel, blockPos, length, 2, 2, true)) {
				return false;
			}
		} else {
			if (!FeatureUtil.isAreaSuitable(worldGenLevel, blockPos, 2, 2, length, true)) {
				return false;
			}
		}

		// determine wood type
		BlockState logState = normal();
		BlockState hollowLogState = hollow();
		BlockState branchState;

		if (hollow().isAir())
			hollowLogState = null;

		//sometimes make floating logs
		if (randomSource.nextInt(5) == 0 && worldGenLevel.getBlockState(blockPos).liquid()) {
			BlockPos.MutableBlockPos floatingPos = blockPos.mutable();
			for (int i = 0; i < 10; i++) {
				if (worldGenLevel.getBlockState(floatingPos.above()).isAir()) {
					blockPos = floatingPos.immutable();
					break;
				} else {
					floatingPos.move(0, 1, 0);
				}
			}
		}

		// make log
		if (goingX) {
			logState = logState.setValue(RotatedPillarBlock.AXIS, Direction.Axis.X);
			if (hollowLogState != null) {
				hollowLogState = hollowLogState.setValue(HorizontalHollowLogBlock.HORIZONTAL_AXIS, Direction.Axis.X)
					.setValue(HorizontalHollowLogBlock.VARIANT, determineHollowProperties(worldGenLevel, blockPos, randomSource));
			}
			branchState = logState.setValue(RotatedPillarBlock.AXIS, Direction.Axis.Z);

			for (int lx = 0; lx < length; lx++) {
				worldGenLevel.setBlock(blockPos.offset(lx, 0, 1), hollowOrNormal(worldGenLevel, shouldMakeAllHollow, hollowLogState, logState), Block.UPDATE_ALL);
				if (randomSource.nextInt(3) > 0) {
					worldGenLevel.setBlock(blockPos.offset(lx, 1, 1), mossOrSeagrass(worldGenLevel, blockPos.offset(lx, 1, 1)), Block.UPDATE_ALL);
					this.markAboveForPostProcessing(worldGenLevel, blockPos.offset(lx, 0, 1));
				}
			}
		} else {
			logState = logState.setValue(RotatedPillarBlock.AXIS, Direction.Axis.Z);
			if (hollowLogState != null) {
				hollowLogState = hollowLogState.setValue(HorizontalHollowLogBlock.HORIZONTAL_AXIS, Direction.Axis.Z)
					.setValue(HorizontalHollowLogBlock.VARIANT, determineHollowProperties(worldGenLevel, blockPos, randomSource));
			}
			branchState = logState.setValue(RotatedPillarBlock.AXIS, Direction.Axis.X);

			for (int lz = 0; lz < length; lz++) {
				worldGenLevel.setBlock(blockPos.offset(1, 0, lz), hollowOrNormal(worldGenLevel, shouldMakeAllHollow, hollowLogState, logState), Block.UPDATE_ALL);
				if (randomSource.nextInt(3) > 0) {
					worldGenLevel.setBlock(blockPos.offset(1, 1, lz), mossOrSeagrass(worldGenLevel, blockPos.offset(1, 1, lz)), Block.UPDATE_ALL);
					this.markAboveForPostProcessing(worldGenLevel, blockPos.offset(1, 0, lz));
				}
			}
		}

		// possibly make branch
		if (randomSource.nextInt(3) > 0) {
			int bx;
			int bz;
			if (goingX) {
				bx = randomSource.nextInt(length);
				bz = randomSource.nextBoolean() ? 2 : 0;
			} else {
				bx = randomSource.nextBoolean() ? 2 : 0;
				bz = randomSource.nextInt(length);

			}
			worldGenLevel.setBlock(blockPos.offset(bx, 0, bz), branchState, Block.UPDATE_ALL);
			if (randomSource.nextBoolean()) {
				worldGenLevel.setBlock(blockPos.offset(bx, 1, bz), mossOrSeagrass(worldGenLevel, blockPos.offset(bx, 1, bz)), Block.UPDATE_ALL);
				this.markAboveForPostProcessing(worldGenLevel, blockPos.offset(bx, 0, bz));
			}
		}

		return true;
	}

	private BlockState mossOrSeagrass(WorldGenLevel level, BlockPos pos) {
		//no moss if we're cold
		if (level.getBlockState(pos.below(2)).is(BlockTags.SNOW)) {
			return Blocks.AIR.defaultBlockState();
		}
		return level.getBlockState(pos).is(Blocks.WATER) ? Blocks.SEAGRASS.defaultBlockState() : TFBlocks.MOSS_PATCH.get().defaultBlockState();
	}

	private BlockState hollowOrNormal(WorldGenLevel level, boolean shouldBeHollow, BlockState hollow, BlockState normal) {
		return ((shouldBeHollow || level.getRandom().nextInt(3) == 0) && !hollow.isAir()) ? hollow : normal;
	}

	private HollowLogVariants.Horizontal determineHollowProperties(WorldGenLevel world, BlockPos pos, RandomSource rand) {
		return  //If we're underwater, submerge in water
			world.getBlockState(pos).is(Blocks.WATER) ? HollowLogVariants.Horizontal.WATERLOGGED :
				//if we're in snow, add some snow
				world.getBlockState(pos.below()).is(BlockTags.SNOW) ? HollowLogVariants.Horizontal.SNOW :
					//maybe add moss + grass
					rand.nextInt(5) == 0 ? HollowLogVariants.Horizontal.MOSS_AND_GRASS :
						//maybe some moss
						rand.nextInt(3) == 0 ? HollowLogVariants.Horizontal.MOSS :
							//or maybe nothing
							HollowLogVariants.Horizontal.EMPTY;
	}
}
