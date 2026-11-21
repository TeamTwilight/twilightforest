package twilightforest.world.components.feature;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import twilightforest.util.features.FeatureLogic;
import twilightforest.util.features.FeaturePlacers;
import twilightforest.util.RootPlacer;
import twilightforest.util.iterators.VoxelBresenhamIterator;

public record WoodRootFeature(BlockStateProvider blockRoot, BlockStateProvider oreRoot) implements Feature {
	public static final MapCodec<WoodRootFeature> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
		BlockStateProvider.DIRECT_CODEC
			.fieldOf("root_block")
			.forGetter(WoodRootFeature::blockRoot),
		BlockStateProvider.DIRECT_CODEC
			.fieldOf("root_ore")
			.forGetter(WoodRootFeature::oreRoot)
	).apply(instance, WoodRootFeature::new));

	@Override
	public MapCodec<? extends Feature> codec() {
		return MAP_CODEC;
	}

	@Override
	public boolean place(WorldGenLevel worldGenLevel, ChunkGenerator chunkGenerator, RandomSource randomSource, BlockPos blockPos) {

		// start must be in stone
		if (worldGenLevel.getBlockState(blockPos).getBlock() != Blocks.STONE) {
			return false;
		}

		float length = randomSource.nextFloat() * 6.0F + randomSource.nextFloat() * 6.0F + 4.0F;
		if (length > blockPos.getY()) {
			length = blockPos.getY();
		}

		// tilt between 0.6 and 0.9
		float tilt = 0.6F + randomSource.nextFloat() * 0.3F;

		return drawRoot(worldGenLevel, randomSource, blockPos, blockPos, length, randomSource.nextFloat(), tilt, blockRoot(), oreRoot());
	}

	private boolean drawRoot(WorldGenLevel world, RandomSource rand, BlockPos oPos, BlockPos pos, float length, float angle, float tilt, BlockStateProvider rootBlock, BlockStateProvider oreBlock) {
		// generate a direction and a length
		BlockPos dest = FeatureLogic.translate(pos, length, angle, tilt);

		// restrict x and z to within 7
		int limit = 6;
		if (oPos.getX() + limit < dest.getX()) {
			dest = new BlockPos(oPos.getX() + limit, dest.getY(), dest.getZ());
		}
		if (oPos.getX() - limit > dest.getX()) {
			dest = new BlockPos(oPos.getX() - limit, dest.getY(), dest.getZ());
		}
		if (oPos.getZ() + limit < dest.getZ()) {
			dest = new BlockPos(dest.getX(), dest.getY(), oPos.getZ() + limit);
		}
		if (oPos.getZ() - limit > dest.getZ()) {
			dest = new BlockPos(dest.getX(), dest.getY(), oPos.getZ() - limit);
		}

		// end must be in stone
		if (world.getBlockState(dest).getBlock() != Blocks.STONE) {
			return false;
		}

		// if both the start and the end are in stone, put a root there
		FeaturePlacers.traceRoot(world, new RootPlacer((checkedPos, rootPlacement) -> world.setBlock(checkedPos, rootPlacement, Block.UPDATE_ALL), 1), rand, rootBlock, new VoxelBresenhamIterator(pos, dest));

		// if we are long enough, make either another root or an oreball
		if (length > 8) {
			if (rand.nextInt(3) > 0) {
				// length > 8, usually split off into another root half as long
				BlockPos nextSrc = FeatureLogic.translate(pos, length / 2, angle, tilt);
				float nextAngle = (angle + 0.25F + (rand.nextFloat() * 0.5F)) % 1.0F;
				float nextTilt = 0.6F + rand.nextFloat() * 0.3F;
				drawRoot(world, rand, oPos, nextSrc, length / 2.0F, nextAngle, nextTilt, rootBlock, oreBlock);
			}
		}

		if (length > 6) {
			if (rand.nextInt(4) == 0) {
				// length > 6, potentially make oreball
				BlockPos ballSrc = FeatureLogic.translate(pos, length / 2, angle, tilt);
				BlockPos ballDest = FeatureLogic.translate(ballSrc, 1.5, (angle + 0.5F) % 1.0F, 0.75);

				this.placeRootBlock(world, ballSrc, oreBlock, rand);
				this.placeRootBlock(world, new BlockPos(ballSrc.getX(), ballSrc.getY(), ballDest.getZ()), oreBlock, rand);
				this.placeRootBlock(world, new BlockPos(ballDest.getX(), ballSrc.getY(), ballSrc.getZ()), oreBlock, rand);
				this.placeRootBlock(world, new BlockPos(ballSrc.getX(), ballSrc.getY(), ballDest.getZ()), oreBlock, rand);
				this.placeRootBlock(world, new BlockPos(ballSrc.getX(), ballDest.getY(), ballSrc.getZ()), oreBlock, rand);
				this.placeRootBlock(world, new BlockPos(ballSrc.getX(), ballDest.getY(), ballDest.getZ()), oreBlock, rand);
				this.placeRootBlock(world, new BlockPos(ballDest.getX(), ballDest.getY(), ballSrc.getZ()), oreBlock, rand);
				this.placeRootBlock(world, ballDest, oreBlock, rand);
			}
		}

		return true;
	}

	/**
	 * Function used to actually place root blocks if they're not going to break anything important
	 */
	protected boolean placeRootBlock(WorldGenLevel world, BlockPos pos, BlockStateProvider state, RandomSource random) {
		return FeatureLogic.canRootGrowIn(world, pos) && world.setBlock(pos, state.getState(world, random, pos), Block.UPDATE_ALL);
	}
}
