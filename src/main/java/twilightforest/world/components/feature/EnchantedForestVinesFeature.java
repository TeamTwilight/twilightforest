package twilightforest.world.components.feature;

import com.llamalad7.mixinextras.lib.apache.commons.tuple.Pair;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.VineBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import twilightforest.init.TFBlocks;

import java.util.*;

public class EnchantedForestVinesFeature extends Feature<NoneFeatureConfiguration> {
	private static final int rarity = 7;
	private static final int extraRarityOnTrees = 8;

	public EnchantedForestVinesFeature(Codec<NoneFeatureConfiguration> codec) {super(codec);}

	@Override
	public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
		WorldGenLevel world = context.level();
		setAllPossibleStates(world, context.random(), context.origin());
		return true;
	}

	private void setAllPossibleStates(WorldGenLevel world, RandomSource random, BlockPos pos) {
		Map<Pair<Integer, Integer>, Pair<Integer, Integer>> columnsY = getAllColumnsY(world, pos.getX(), pos.getZ());
		for (Map.Entry<Pair<Integer, Integer>, Pair<Integer, Integer>> entry : columnsY.entrySet()) {
			Pair<Integer, Integer> horizontalCoordinates = entry.getKey();
			int x = horizontalCoordinates.getLeft();
			int z = horizontalCoordinates.getRight();
			int minY = entry.getValue().getLeft();
			int maxY = entry.getValue().getRight();

			setColumn(world, random, x, minY, maxY, z);
		}
	}

	private Map<Pair<Integer, Integer>, Pair<Integer, Integer>> getAllColumnsY(WorldGenLevel world, int originX, int originZ) {
		Map<Pair<Integer, Integer>, Pair<Integer, Integer>> columns = new HashMap<>();
		for (int x = originX; x < originX + 16; x++) {
			for (int z = originZ; z < originZ + 16; z++) {
				columns.put(Pair.of(x, z), getColumnY(world, x, z));
			}
		}
		return columns;
	}

	private Pair<Integer, Integer> getColumnY(WorldGenLevel world, int x, int z) {
		int height1 = world.getHeight(Heightmap.Types.MOTION_BLOCKING, x - 1, z);
		int height2 = world.getHeight(Heightmap.Types.MOTION_BLOCKING, x + 1, z);
		int height3 = world.getHeight(Heightmap.Types.MOTION_BLOCKING, x, z - 1);
		int height4 = world.getHeight(Heightmap.Types.MOTION_BLOCKING, x, z + 1);

		return Pair.of(-10, Math.max(Math.max(height1, height2), Math.max(height3, height4)));
	}

	private void setColumn(WorldGenLevel world, RandomSource random, int x, int minY, int maxY, int z) {
		for (int y = minY; y <= maxY; y++) {
			BlockPos pos = new BlockPos(x, y, z);
			setVine(world, random, pos);
		}
	}

	private void setVine(WorldGenLevel world, RandomSource random, BlockPos pos) {
		if (random.nextInt(rarity) > 0 || !world.getBlockState(pos).isEmpty() || !isSuitableBiome(world, pos))
			return;

		BlockState state = Blocks.VINE.defaultBlockState();
		boolean empty = true;
		boolean isTree = true;

		for (Direction dir : Direction.values()) {
			BlockPos relativePos = pos.relative(dir);
			if (dir != Direction.DOWN && VineBlock.isAcceptableNeighbour(world, relativePos, dir) && !world.getBlockState(relativePos).is(TFBlocks.RAINBOW_OAK_LEAVES.get())) {
				if (!isTree(world.getBlockState(relativePos)))
					isTree = false;

				state = state.setValue(VineBlock.getPropertyForFace(dir), true);
				if (dir.getAxis() != Direction.Axis.Y)
					empty = false;
			}
		}
		if (isTree && random.nextInt(extraRarityOnTrees) > 0)
			return;

		if (!empty)
			setBlock(world, pos, state);
	}

	private boolean isSuitableBiome(WorldGenLevel world, BlockPos pos) {
		return Objects.requireNonNull(world.getBiome(pos).getKey()).location().getPath().equals("enchanted_forest");
	}

	private boolean isTree(BlockState state) {
		return state.is(BlockTags.LOGS) || state.is(BlockTags.LEAVES);
	}
}
