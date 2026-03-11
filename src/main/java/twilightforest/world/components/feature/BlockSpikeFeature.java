package twilightforest.world.components.feature;

import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import twilightforest.data.custom.stalactites.entry.Stalactite;
import twilightforest.data.tags.BlockTagGenerator;
import twilightforest.util.features.FeatureLogic;

import java.util.List;

public class BlockSpikeFeature extends Feature<NoneFeatureConfiguration> {
	public static final Stalactite STONE_STALACTITE = new Stalactite(Either.right(Blocks.STONE), 0.25F, 11, 1);

	public BlockSpikeFeature(Codec<NoneFeatureConfiguration> codec) {
		super(codec);
	}

	@Override
	public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
		RandomSource random = context.random();
		return startSpike(context.level(), context.origin(), STONE_STALACTITE, random, false);
	}

	public static boolean startSpike(WorldGenLevel level, BlockPos startPos, Stalactite config, RandomSource random, boolean hanging) {
		return startSpike(level, startPos, config, random, hanging, Integer.MAX_VALUE);
	}

	public static boolean startSpike(WorldGenLevel level, BlockPos startPos, Stalactite config, RandomSource random, boolean hanging, int forcedMaxHeight) {
		int maxInclusive = config.maxLength();
		int minInclusive = (int) (maxInclusive * config.sizeVariation());

		int length = Mth.randomBetweenInclusive(random, minInclusive, maxInclusive);

		return startSpike(level, startPos, config.ores(), length, minInclusive, Math.min(maxInclusive, forcedMaxHeight), 4, hanging, random);
	}

	public static boolean startSpike(WorldGenLevel level, BlockPos startPos, Either<List<Pair<Block, Integer>>, Block> ore, int length, int lengthMinimum, int lengthMaximum, int clearance, boolean hang, RandomSource random) {
		// Uncomment for easy spectator-xray debugging in position generation
		//if (true) return level.setBlock(startPos, Blocks.BEACON.defaultBlockState(), 3);

		if (lengthMaximum < Math.max(lengthMinimum, 1)) return false;

		BlockPos.MutableBlockPos movingPos = startPos.mutable();
		int clearedLength = 0;
		int dY = hang ? -1 : 1;

		// First find an air block
		for (int i = 0; i < length; i++) {
			clearedLength = i;

			if (FeatureLogic.worldGenReplaceable(level.getBlockState(movingPos))) break;

			movingPos.move(0, dY, 0);
		}

		// Since this gets skipped from the previous line, we invoke it manually
		movingPos.move(0, dY, 0);

		// Then find a solid block
		int remainingScanLength = length - clearedLength + clearance;
		int finalLength = clearedLength - clearance;
		for (int i = 0; i < remainingScanLength; i++) {
			finalLength = clearedLength + i;

			if (!FeatureLogic.worldGenReplaceable(level.getBlockState(movingPos))) break;

			movingPos.move(0, dY, 0);
		}

		finalLength = Math.min(length, finalLength);

		if (finalLength < lengthMinimum || finalLength > lengthMaximum) return false;

		return makeSpike(level, startPos, ore, finalLength, dY, random, hang);
	}

	private static boolean makeSpike(WorldGenLevel level, BlockPos startPos, Either<List<Pair<Block, Integer>>, Block> ore, int length, int dY, RandomSource random, boolean hang) {
		int diameter = (int) (length / 4.5F); // diameter of the base

		//only place spikes on solid ground, not on the tops of trees
		if (!hang) {
			BlockPos below = startPos.below(2);
			BlockState belowState = level.getBlockState(below);
			if (!belowState.is(BlockTagGenerator.SUPPORTS_STALAGMITES) &&
				(!FeatureLogic.worldGenReplaceable(belowState) || !belowState.isFaceSturdy(level, below, Direction.UP) || FeatureLogic.isBlockNotOk(belowState))) return false;
		}

		// let's see...
		for (int dx = -diameter; dx <= diameter; dx++) {
			for (int dz = -diameter; dz <= diameter; dz++) {
				// determine how long this spike will be.
				int absx = Math.abs(dx);
				int absz = Math.abs(dz);
				int dist = (int) (Math.max(absx, absz) + Math.min(absx, absz) * 0.5F);
				int spikeLength;

				if (dist <= 0) spikeLength = length;
				else spikeLength = random.nextInt((int) (length / (dist + 0.25F)));

				for (int i = -1; i < spikeLength; i++) {
					BlockPos placement = startPos.offset(dx, i * dY, dz);

					if (FeatureLogic.worldGenReplaceable(level.getBlockState(placement)) && (dY > 0 || placement.getY() < level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, placement.getX(), placement.getZ()) - 2)) {
						if (ore.right().isPresent()) {
							level.setBlock(placement, ore.right().get().defaultBlockState(), Block.UPDATE_ALL);
						} else {
							// FIXME Deduplicate this construction of the weightedlist, tt is constructed many times per generation
							WeightedRandomList<WeightedEntry.Wrapper<Block>> entries = WeightedRandomList.create(ore.left().get().stream().map(pair -> WeightedEntry.wrap(pair.getFirst(), pair.getSecond())).toList());
							level.setBlock(placement, entries.getRandom(random).orElse(WeightedEntry.wrap(Blocks.STONE, 1)).data().defaultBlockState(), Block.UPDATE_ALL);
						}
					}
				}
			}
		}

		return true;
	}

	// For empty random lists
	public static Stalactite defaultRandom(@SuppressWarnings("unused") RandomSource rand) {
		return STONE_STALACTITE;
	}
}
