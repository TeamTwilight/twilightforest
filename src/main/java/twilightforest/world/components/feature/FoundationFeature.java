package twilightforest.world.components.feature;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.BlockFamilies;
import net.minecraft.data.BlockFamily;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.WeightedList;
import net.minecraft.util.valueproviders.*;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
import net.minecraft.world.level.storage.loot.LootTable;
import twilightforest.loot.TFLootTables;
import twilightforest.util.TFBlockFamilies;
import twilightforest.util.features.FeatureLogic;
import twilightforest.util.features.FeatureUtil;

public record FoundationFeature(RuinedFoundationDimensions dimensions, RuinedFoundationBlocks blocks, ResourceKey<LootTable> lootTable) implements Feature {

	public static final MapCodec<FoundationFeature> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
		RuinedFoundationDimensions.CODEC.forGetter(FoundationFeature::dimensions),
		RuinedFoundationBlocks.CODEC.forGetter(FoundationFeature::blocks),
		ResourceKey.codec(Registries.LOOT_TABLE)
			.fieldOf("loot_table")
			.forGetter(FoundationFeature::lootTable)
	).apply(instance, FoundationFeature::new));

	public static FoundationFeature withDefaultBlocks(boolean floorWaterlogged) {
		RuinedFoundationBlocks foundationBlocks = RuinedFoundationBlocks.blocksFromFamilies(floorWaterlogged, BlockFamilies.OAK_PLANKS, BlockFamilies.COBBLESTONE, BlockFamilies.MOSSY_COBBLESTONE, BlockStateProvider.of(Blocks.CHEST));
		return new FoundationFeature(RuinedFoundationDimensions.makeDefault(), foundationBlocks, TFLootTables.FOUNDATION_BASEMENT);
	}

	@Override
	public MapCodec<? extends Feature> codec() {
		return MAP_CODEC;
	}

	@Override
	public boolean place(WorldGenLevel worldGenLevel, ChunkGenerator chunkGenerator, RandomSource randomSource, BlockPos blockPos) {
		RuinedFoundationDimensions dimensions = dimensions();
		RuinedFoundationBlocks blocks = blocks();

		IntProvider wallWidths = dimensions.wallWidth();
		int xWidth = wallWidths.sample(randomSource);
		int zWidth = wallWidths.sample(randomSource);

		if (!FeatureUtil.isAreaSuitable(worldGenLevel, blockPos.offset(1, 0, 1), xWidth - 1, 4, zWidth - 1)) {
			return false;
		}

		//okay!
		generateFoundation(worldGenLevel, randomSource, blockPos, xWidth, zWidth, dimensions.wallHeights(), dimensions.placeFloorTest(), blocks.wallBlock(), blocks.wallTop(), blocks.decayedWall(), blocks.decayedTop(), blocks.floor());

		//TODO: chimney?

		int basementDepth = dimensions.basementHeight().sample(randomSource);
		if (basementDepth > 0) {
			BlockPos basementCeilingPos = blockPos.offset(1, -3, 1);
			generateBasement(xWidth - 2, zWidth - 2, basementDepth, worldGenLevel, basementCeilingPos, randomSource, dimensions.placeFloorTest(), blocks.floor(), blocks.basementPosts(), blocks.lootContainer(), lootTable());
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
		if (xWidth < 1 || zWidth < 1 || depth < 1)
			return;

		int chestX = rollChestCoord(xWidth, rand);
		int chestZ = rollChestCoord(zWidth, rand);

		// clear basement
		for (int dX = 0; dX <= xWidth; dX++) {
			for (int dZ = 0; dZ <= zWidth; dZ++) {
				int cornerOverlap = 0;
				if (dX == 0)
					cornerOverlap++;
				if (dZ == 0)
					cornerOverlap++;
				if (dX == xWidth)
					cornerOverlap++;
				if (dZ == zWidth)
					cornerOverlap++;

				boolean isInCorner = cornerOverlap > 1;

				for (int dY = 1 - depth; dY <= 0; dY++) {
					BlockPos placeAt = ceilingPos.offset(dX, dY, dZ);
					world.setBlock(placeAt, Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL);
					if (isInCorner)
						setAndUpdate(world, rand, basementPost, placeAt);
				}

				if ((dX == chestX && dZ == chestZ) || (cornerOverlap == 0 && placeFloorTest.sample(rand) <= 0)) {
					// destroyed wooden plank floor, placed by chance or because a chest is going to generate above it
					setAndUpdate(world, rand, floor, ceilingPos.offset(dX, -depth, dZ));
				}
			}
		}

		// make chest
		BlockPos lootPos = ceilingPos.offset(chestX, 1 - depth, chestZ);
		world.setBlock(lootPos, lootContainer.getState(world, rand, lootPos), Block.UPDATE_ALL);
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
		BlockState state = floor.getState(world, rand, placeAt).rotate(rotation);

		if (state.hasProperty(BlockStateProperties.WATERLOGGED)) {
			boolean hasWaterOrAbove = world.getFluidState(placeAt).is(FluidTags.WATER) || world.getFluidState(placeAt.above()).is(FluidTags.WATER);
			if (hasWaterOrAbove)
				state = state.setValue(BlockStateProperties.WATERLOGGED, true);
		}

		world.setBlock(placeAt, state, Block.UPDATE_ALL);

		world.getChunk(placeAt).markPosForPostProcessing(placeAt);
	}

	public record RuinedFoundationDimensions(IntProvider wallWidth, IntProvider wallHeights, IntProvider basementHeight, FloatProvider placeFloorTest) {
		public static final MapCodec<RuinedFoundationDimensions> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
			IntProviders.codec(1, 16).fieldOf("wall_width").forGetter(RuinedFoundationDimensions::wallWidth),
			IntProviders.codec(1, 32).fieldOf("wall_heights").forGetter(RuinedFoundationDimensions::wallHeights),
			IntProviders.codec(0, 16).fieldOf("basement_height").forGetter(RuinedFoundationDimensions::basementHeight),
			FloatProviders.codec(-8, 8).fieldOf("random_floor_chance").forGetter(RuinedFoundationDimensions::placeFloorTest)
		).apply(inst, RuinedFoundationDimensions::new));

		public static RuinedFoundationDimensions makeDefault() {
			WeightedListInt basementHeights = new WeightedListInt(WeightedList.<IntProvider>builder()
				// 50% basement chance!
				.add(ConstantInt.of(3), 1)
				.add(ConstantInt.of(0), 1)
				.build()
			);

			return new RuinedFoundationDimensions(UniformInt.of(5, 9), UniformInt.of(1, 5), basementHeights, UniformFloat.of(-2, 1));
		}
	}

	// Blockstates given are treated as the north orientation. Rotations apply for other-facing walls; corners resolving randomly.
	public record RuinedFoundationBlocks(BlockStateProvider floor, BlockStateProvider basementPosts, BlockStateProvider lootContainer, BlockStateProvider wallBlock, BlockStateProvider wallTop, BlockStateProvider decayedWall, BlockStateProvider decayedTop) {
		public static final MapCodec<RuinedFoundationBlocks> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
			BlockStateProvider.DIRECT_CODEC.fieldOf("floor").forGetter(RuinedFoundationBlocks::floor),
			BlockStateProvider.DIRECT_CODEC.fieldOf("basement_posts").forGetter(RuinedFoundationBlocks::basementPosts),
			BlockStateProvider.DIRECT_CODEC.fieldOf("loot_container").forGetter(RuinedFoundationBlocks::lootContainer),
			BlockStateProvider.DIRECT_CODEC.fieldOf("wall_block").forGetter(RuinedFoundationBlocks::wallBlock),
			BlockStateProvider.DIRECT_CODEC.fieldOf("wall_top_block").forGetter(RuinedFoundationBlocks::wallTop),
			BlockStateProvider.DIRECT_CODEC.fieldOf("decayed_wall_block").forGetter(RuinedFoundationBlocks::decayedWall),
			BlockStateProvider.DIRECT_CODEC.fieldOf("decayed_wall_top_block").forGetter(RuinedFoundationBlocks::decayedTop)
		).apply(inst, RuinedFoundationBlocks::new));

		public static RuinedFoundationBlocks blocksFromFamilies(boolean floorWaterlogged, BlockFamily floorMaterial, BlockFamily wallMaterial, BlockFamily decayedMaterial, BlockStateProvider chestBlockProvider) {
			boolean doFence = floorMaterial.get(BlockFamily.Variant.FENCE) != null;

			BlockFamily.Variant basementSupports = doFence ? BlockFamily.Variant.FENCE : BlockFamily.Variant.WALL;
			TFBlockFamilies.verifyFamilyShapes(floorMaterial, BlockFamily.Variant.SLAB, BlockFamily.Variant.STAIRS, basementSupports);

			TFBlockFamilies.verifyFamilyShapes(wallMaterial, BlockFamily.Variant.SLAB, BlockFamily.Variant.STAIRS);
			TFBlockFamilies.verifyFamilyShapes(decayedMaterial, BlockFamily.Variant.SLAB, BlockFamily.Variant.STAIRS);

			BlockState floorStairs = floorMaterial.get(BlockFamily.Variant.STAIRS).defaultBlockState();

			BlockState wallBlock = wallMaterial.getBaseBlock().defaultBlockState();
			BlockState wallStairs = wallMaterial.get(BlockFamily.Variant.STAIRS).defaultBlockState();

			BlockState decayedWall = decayedMaterial.getBaseBlock().defaultBlockState();
			BlockState decayedStairs = decayedMaterial.get(BlockFamily.Variant.STAIRS).defaultBlockState();

			final WeightedStateProvider floor = new WeightedStateProvider(WeightedList.<BlockState>builder()
				.add(floorMaterial.getBaseBlock().defaultBlockState(), 39)
				.add(floorMaterial.get(BlockFamily.Variant.SLAB).defaultBlockState().setValue(BlockStateProperties.WATERLOGGED, floorWaterlogged), 1)
				.add(floorStairs.setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH).setValue(BlockStateProperties.WATERLOGGED, floorWaterlogged), 6)
				.add(floorStairs.setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.EAST).setValue(BlockStateProperties.WATERLOGGED, floorWaterlogged), 2)
				.add(floorStairs.setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.SOUTH).setValue(BlockStateProperties.WATERLOGGED, floorWaterlogged), 6)
				.add(floorStairs.setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.WEST).setValue(BlockStateProperties.WATERLOGGED, floorWaterlogged), 2)
				.build()
			);

			final WeightedStateProvider wallTop = new WeightedStateProvider(WeightedList.<BlockState>builder()
				.add(wallBlock, 5)
				.add(wallMaterial.get(BlockFamily.Variant.SLAB).defaultBlockState(), 1)
				.add(wallStairs.setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.EAST), 2)
				.add(wallStairs.setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.WEST), 2)
				.build()
			);

			final WeightedStateProvider decayedTop = new WeightedStateProvider(WeightedList.<BlockState>builder()
				.add(decayedWall, 5)
				.add(decayedMaterial.get(BlockFamily.Variant.SLAB).defaultBlockState(), 1)
				.add(decayedStairs.setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.EAST), 2)
				.add(decayedStairs.setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.WEST), 2)
				.build()
			);

			return new RuinedFoundationBlocks(floor, BlockStateProvider.of(floorMaterial.get(basementSupports).defaultBlockState()), chestBlockProvider, BlockStateProvider.of(wallBlock), wallTop, BlockStateProvider.of(decayedWall), decayedTop);
		}
	}
}
