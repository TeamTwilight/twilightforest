package twilightforest.world.components.feature.trees;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.TreeFeature;
import twilightforest.util.features.FeaturePlacers;

/**
 * Makes large trees with flat leaf ovals that provide a canopy for the forest
 *
 * @author Ben
 */

//Lots of things from TreeFeature, but we're checking for dirt to place on
public record DarkCanopyTreeFeature(TreeFeature delegate) implements Feature {

	public static final MapCodec<DarkCanopyTreeFeature> MAP_CODEC = TreeFeature.CODEC.xmap(DarkCanopyTreeFeature::new, DarkCanopyTreeFeature::delegate);

	@Override
	public MapCodec<? extends Feature> codec() {
		return MAP_CODEC;
	}

	@Override
	public boolean place(WorldGenLevel level, ChunkGenerator chunkGenerator, RandomSource random, BlockPos origin) {
		// If we are given leaves as a starting position, seek dirt or grass underneath
		boolean foundDirt = false;
		for (int dy = origin.getY(); dy >= level.getMinY(); dy--) {
			BlockState state = level.getBlockState(new BlockPos(origin.getX(), dy - 1, origin.getZ()));
			if (state.is(BlockTags.SUBSTRATE_OVERWORLD)) {
				// Yes!
				foundDirt = true;
				origin = new BlockPos(origin.getX(), dy, origin.getZ());
				break;
			} else if (state.is(BlockTags.BASE_STONE_OVERWORLD) || state.is(BlockTags.SAND)) {
				// Nope
				break;
			}
		}

		if (!foundDirt) {
			return false;
		}

		for (int i = 0; i < 4; i++) {
			// We check against the TreeFeature's validTreePos method, to see if the tree can grow here, cuz the trunk placer uses this as well
			// If we don't, some trees end up growing only one or two blocks tall
			if (!FeaturePlacers.validTreePos(level, origin.relative(Direction.UP, i)))
				return false;
		}

		// Do not grow next to another tree
		for (Direction e : Direction.Plane.HORIZONTAL) {
			if (level.getBlockState(origin.relative(e)).is(BlockTags.LOGS))
				return false;
		}

		return delegate.place(level, chunkGenerator, random, origin);
	}
}
