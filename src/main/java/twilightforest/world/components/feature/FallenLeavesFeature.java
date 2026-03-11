package twilightforest.world.components.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.material.Fluids;
import twilightforest.block.FallenLeavesBlock;
import twilightforest.init.TFBlocks;

public class FallenLeavesFeature extends Feature<NoneFeatureConfiguration> {

	public FallenLeavesFeature(Codec<NoneFeatureConfiguration> config) {
		super(config);
	}

	private final BlockState state = TFBlocks.FALLEN_LEAVES.get().defaultBlockState();

	@Override
	public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> ctx) {
		WorldGenLevel level = ctx.level();
		BlockPos position = level.getHeightmapPos(Heightmap.Types.WORLD_SURFACE_WG, ctx.origin());
		RandomSource rand = ctx.random();

		if (this.canPlace(position, level)) {
			if (!level.getFluidState(position.below()).isEmpty()) {
				return this.generateFlatPileOnWater(level, position, rand);
			} else {
				int startHeight = rand.nextInt(6) + 1;
				level.setBlock(position, this.state.setValue(FallenLeavesBlock.LAYERS, startHeight), Block.UPDATE_KNOWN_SHAPE | Block.UPDATE_CLIENTS);
				for (int i = 0; i < startHeight; i++) {
					this.generateCircleOfLeaves(level, position, rand, i, startHeight - i - 1);
					if (rand.nextInt(3) == 0) i++;
				}
			}
			return true;
		}
		return false;
	}

	private boolean generateFlatPileOnWater(WorldGenLevel level, BlockPos pos, RandomSource random) {
		for (int x = 0; x < 5; x++) {
			for (int z = 0; z < 5; z++) {
				if (random.nextInt(3) != 0)
					continue;
				boolean flag = false;
				int y = 2;
				do {
					if (this.canPlace(pos.offset(x, y, z), level)) {
						flag = true;
						break;
					}
					y--;
				} while (y >= -2);
				if (!flag)
					continue;
				BlockPos finalPos = pos.offset(x, y, z);
				if (this.state.canSurvive(level, finalPos))
					level.setBlock(finalPos, this.state, Block.UPDATE_KNOWN_SHAPE | Block.UPDATE_CLIENTS);
			}
		}
		return true;
	}

	private void generateCircleOfLeaves(WorldGenLevel level, BlockPos origin, RandomSource random, int radius, int height) {
		for (int i1 = origin.getX() - radius; i1 <= origin.getX() + radius; ++i1) {
			for (int j1 = origin.getZ() - radius; j1 <= origin.getZ() + radius; ++j1) {
				int k1 = i1 - origin.getX();
				int l1 = j1 - origin.getZ();
				if (k1 * k1 + l1 * l1 <= radius * radius) {
					BlockPos newPos = new BlockPos(i1, origin.getY(), j1);
					int trueHeight = height - random.nextInt(3);
					if (trueHeight > 0) {
						this.checkAndGenerateLeafPile(level, newPos, trueHeight);
					}
				}
			}
		}
	}

	private void checkAndGenerateLeafPile(WorldGenLevel level, BlockPos pos, int pileLayer) {
		boolean flag = false;
		int y = 0;
		do {
			if (this.canPlace(pos.offset(0, y, 0), level)) {
				flag = true;
				break;
			}
			y--;
		} while (y >= -2);
		if (!flag)
			return;
		BlockPos finalPos = pos.offset(0, y, 0);
		if (this.state.canSurvive(level, finalPos))
			level.setBlock(finalPos, this.state.setValue(FallenLeavesBlock.LAYERS, pileLayer), Block.UPDATE_KNOWN_SHAPE | Block.UPDATE_CLIENTS);
	}

	private boolean canPlace(BlockPos pos, WorldGenLevel level) {
		BlockState state = level.getBlockState(pos.below());
		return !level.getBlockState(pos).is(this.state.getBlock()) && (level.isEmptyBlock(pos) || level.getBlockState(pos).is(TFBlocks.MAYAPPLE) || level.getBlockState(pos).canBeReplaced()) && (state.is(BlockTags.DIRT) || level.getFluidState(pos.below()).is(Fluids.WATER));
	}
}
