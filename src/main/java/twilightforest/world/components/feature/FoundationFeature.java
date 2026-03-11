package twilightforest.world.components.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.FloatProvider;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.storage.loot.LootTable;
import twilightforest.util.features.FeatureLogic;
import twilightforest.util.features.FeatureUtil;
import twilightforest.world.components.feature.config.RuinedFoundationConfig;

public class FoundationFeature extends Feature<RuinedFoundationConfig> {

	public FoundationFeature(Codec<RuinedFoundationConfig> configIn) {
		super(configIn);
	}

	@Override
	public boolean place(FeaturePlaceContext<RuinedFoundationConfig> ctx) {
		WorldGenLevel world = ctx.level();
		BlockPos pos = ctx.origin();
		RandomSource rand = ctx.random();
		RuinedFoundationConfig config = ctx.config();
		RuinedFoundationConfig.RuinedFoundationDimensions dimensions = ctx.config().dimensions();
		RuinedFoundationConfig.RuinedFoundationBlocks blocks = ctx.config().blocks();

		IntProvider wallWidths = dimensions.wallWidth();
		int xWidth = wallWidths.sample(rand);
		int zWidth = wallWidths.sample(rand);

		if (!FeatureUtil.isAreaSuitable(world, pos.offset(1, 0, 1), xWidth - 1, 4, zWidth - 1)) {
			return false;
		}

		//okay!
		generateFoundation(world, rand, pos, xWidth, zWidth, dimensions.wallHeights(), dimensions.placeFloorTest(), blocks.wallBlock(), blocks.wallTop(), blocks.decayedWall(), blocks.decayedTop(), blocks.floor());

		//TODO: chimney?

		int basementDepth = dimensions.basementHeight().sample(rand);
		if (basementDepth > 0) {
			BlockPos basementCeilingPos = pos.offset(1, -3, 1);
			generateBasement(xWidth - 2, zWidth - 2, basementDepth, world, basementCeilingPos, rand, dimensions.placeFloorTest(), blocks.floor(), blocks.basementPosts(), blocks.lootContainer(), config.lootTable());
		}

		return true;
	}

	private static void generateFoundation(WorldGenLevel world, RandomSource rand, BlockPos origin, int xWidth, int zWidth, IntProvider wallHeights, FloatProvider placeFloorTest, BlockStateProvider wallBlock, BlockStateProvider wallTop, BlockStateProvider decayedWall, BlockStateProvider decayedTop, BlockStateProvider floor) {
		for (int dX = 0; dX <= xWidth; dX++) {
			for (int dZ = 0; dZ <= zWidth; dZ++) {
				// stone on the edges
				Rotation wallRotation = FeatureLogic.wallVolumeRotation(rand, dX, dZ, xWidth, zWidth);
				if (wallRotation != null) {
					int height = wallHeights.sample(rand);

					for (int yBlock = 0; yBlock < height; yBlock++) {
						BlockPos placeAt = origin.offset(dX, yBlock - 1, dZ);
						setWallBlock(world, rand, wallBlock, decayedWall, yBlock, placeAt, wallRotation);
					}

					setWallBlock(world, rand, wallTop, decayedTop, height, origin.offset(dX, height - 1, dZ), wallRotation);
				} else if (placeFloorTest.sample(rand) <= 0) {
					// destroyed wooden plank floor
					setAndUpdate(world, rand, floor, origin.offset(dX, -1, dZ));
				}
			}
		}
	}

	private static void setWallBlock(WorldGenLevel world, RandomSource rand, BlockStateProvider main, BlockStateProvider decay, int yBlock, BlockPos placeAt, Rotation rotation) {
		setAndUpdate(world, rand, rollDecay(rand, yBlock, main, decay), placeAt, rotation);
	}

	public static BlockStateProvider rollDecay(RandomSource rand, int decayRarity, BlockStateProvider main, BlockStateProvider decay) {
		return rand.nextInt(decayRarity + 1) >= 1 ? main : decay;
	}

	private static void generateBasement(int xWidth, int zWidth, int depth, WorldGenLevel world, BlockPos ceilingPos, RandomSource rand, FloatProvider placeFloorTest, BlockStateProvider floor, BlockStateProvider basementPost, BlockStateProvider lootContainer, ResourceKey<LootTable> lootTable) {
		if (xWidth < 1 || zWidth < 1 || depth < 1) return;

		int chestX = rollChestCoord(xWidth, rand);
		int chestZ = rollChestCoord(zWidth, rand);

		// clear basement
		for (int dX = 0; dX <= xWidth; dX++) {
			for (int dZ = 0; dZ <= zWidth; dZ++) {
				int cornerOverlap = 0;
				if (dX == 0) cornerOverlap++;
				if (dZ == 0) cornerOverlap++;
				if (dX == xWidth) cornerOverlap++;
				if (dZ == zWidth) cornerOverlap++;

				boolean isInCorner = cornerOverlap > 1;

				for (int dY = 1 - depth; dY <= 0; dY++) {
					BlockPos placeAt = ceilingPos.offset(dX, dY, dZ);
					world.setBlock(placeAt, Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL);
					if (isInCorner) setAndUpdate(world, rand, basementPost, placeAt);
				}

				if ((dX == chestX && dZ == chestZ) || (cornerOverlap == 0 && placeFloorTest.sample(rand) <= 0)) {
					// destroyed wooden plank floor, placed by chance or because a chest is going to generate above it
					setAndUpdate(world, rand, floor, ceilingPos.offset(dX, -depth, dZ));
				}
			}
		}

		// make chest
		BlockPos lootPos = ceilingPos.offset(chestX, 1 - depth, chestZ);
		world.setBlock(lootPos, lootContainer.getState(rand, lootPos), Block.UPDATE_ALL);
		if (world.getBlockEntity(lootPos) instanceof RandomizableContainerBlockEntity lootBE) {
			lootBE.setLootTable(lootTable, world.getSeed() * lootPos.getX() + lootPos.getY() ^ lootPos.getZ());
		}
	}

	private static int rollChestCoord(int width, RandomSource rand) {
		if (width < 3) // No room to not be on an edge
			return rand.nextInt(Math.max(0, width) + 1);

		return rand.nextInt(Math.max(0, width - 1) + 1) + 1;
	}

	private static void setAndUpdate(WorldGenLevel world, RandomSource rand, BlockStateProvider floor, BlockPos placeAt) {
		setAndUpdate(world, rand, floor, placeAt, Rotation.NONE);
	}

	private static void setAndUpdate(WorldGenLevel world, RandomSource rand, BlockStateProvider floor, BlockPos placeAt, Rotation rotation) {
		BlockState state = floor.getState(rand, placeAt).rotate(rotation);

		if (state.hasProperty(BlockStateProperties.WATERLOGGED)) {
			boolean hasWaterOrAbove = world.getFluidState(placeAt).is(FluidTags.WATER) || world.getFluidState(placeAt.above()).is(FluidTags.WATER);
			if (hasWaterOrAbove)
				state = state.setValue(BlockStateProperties.WATERLOGGED, true);
		}

		world.setBlock(placeAt, state, Block.UPDATE_ALL);

		world.getChunk(placeAt).markPosForPostprocessing(placeAt);
	}
}
