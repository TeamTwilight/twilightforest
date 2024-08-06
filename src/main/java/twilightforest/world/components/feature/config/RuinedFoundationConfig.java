package twilightforest.world.components.feature.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.BlockFamilies;
import net.minecraft.data.BlockFamily;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.util.valueproviders.*;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
import net.minecraft.world.level.storage.loot.LootTable;
import twilightforest.data.TFBlockFamilies;
import twilightforest.loot.TFLootTables;

@SuppressWarnings("ConstantValue")
public record RuinedFoundationConfig(RuinedFoundationDimensions dimensions, RuinedFoundationBlocks blocks, ResourceKey<LootTable> lootTable) implements FeatureConfiguration {
	public static final Codec<RuinedFoundationConfig> CODEC = RecordCodecBuilder.create(inst -> inst.group(
		RuinedFoundationDimensions.CODEC.forGetter(RuinedFoundationConfig::dimensions),
		RuinedFoundationBlocks.CODEC.forGetter(RuinedFoundationConfig::blocks),
		ResourceKey.codec(Registries.LOOT_TABLE).fieldOf("loot_table").forGetter(RuinedFoundationConfig::lootTable)
	).apply(inst, RuinedFoundationConfig::new));

	public static RuinedFoundationConfig withDefaultBlocks(boolean floorWaterlogged) {
		RuinedFoundationBlocks foundationBlocks = RuinedFoundationBlocks.blocksFromFamilies(floorWaterlogged, BlockFamilies.OAK_PLANKS, BlockFamilies.COBBLESTONE, BlockFamilies.MOSSY_COBBLESTONE, BlockStateProvider.simple(Blocks.CHEST));

		return new RuinedFoundationConfig(RuinedFoundationDimensions.makeDefault(), foundationBlocks, TFLootTables.FOUNDATION_BASEMENT);
	}

	public record RuinedFoundationDimensions(IntProvider wallWidth, IntProvider wallHeights, IntProvider basementHeight, FloatProvider placeFloorTest) {
		public static final MapCodec<RuinedFoundationDimensions> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
			IntProvider.codec(1, 16).fieldOf("wall_width").forGetter(RuinedFoundationDimensions::wallWidth),
			IntProvider.codec(1, 32).fieldOf("wall_heights").forGetter(RuinedFoundationDimensions::wallHeights),
			IntProvider.codec(0, 16).fieldOf("basement_height").forGetter(RuinedFoundationDimensions::basementHeight),
			FloatProvider.codec(-8, 8).fieldOf("random_floor_chance").forGetter(RuinedFoundationDimensions::placeFloorTest)
		).apply(inst, RuinedFoundationDimensions::new));

		public static RuinedFoundationDimensions makeDefault() {
			WeightedListInt basementHeights = new WeightedListInt(SimpleWeightedRandomList.<IntProvider>builder()
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
			BlockStateProvider.CODEC.fieldOf("floor").forGetter(RuinedFoundationBlocks::floor),
			BlockStateProvider.CODEC.fieldOf("basement_posts").forGetter(RuinedFoundationBlocks::basementPosts),
			BlockStateProvider.CODEC.fieldOf("loot_container").forGetter(RuinedFoundationBlocks::lootContainer),
			BlockStateProvider.CODEC.fieldOf("wall_block").forGetter(RuinedFoundationBlocks::wallBlock),
			BlockStateProvider.CODEC.fieldOf("wall_top_block").forGetter(RuinedFoundationBlocks::wallTop),
			BlockStateProvider.CODEC.fieldOf("decayed_wall_block").forGetter(RuinedFoundationBlocks::decayedWall),
			BlockStateProvider.CODEC.fieldOf("decayed_wall_top_block").forGetter(RuinedFoundationBlocks::decayedTop)
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

			final WeightedStateProvider floor = new WeightedStateProvider(SimpleWeightedRandomList.<BlockState>builder()
				.add(floorMaterial.getBaseBlock().defaultBlockState(), 39)
				.add(floorMaterial.get(BlockFamily.Variant.SLAB).defaultBlockState().setValue(BlockStateProperties.WATERLOGGED, floorWaterlogged), 1)
				.add(floorStairs.setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH).setValue(BlockStateProperties.WATERLOGGED, floorWaterlogged), 6)
				.add(floorStairs.setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.EAST).setValue(BlockStateProperties.WATERLOGGED, floorWaterlogged), 2)
				.add(floorStairs.setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.SOUTH).setValue(BlockStateProperties.WATERLOGGED, floorWaterlogged), 6)
				.add(floorStairs.setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.WEST).setValue(BlockStateProperties.WATERLOGGED, floorWaterlogged), 2)
				.build()
			);

			final WeightedStateProvider wallTop = new WeightedStateProvider(SimpleWeightedRandomList.<BlockState>builder()
				.add(wallBlock, 5)
				.add(wallMaterial.get(BlockFamily.Variant.SLAB).defaultBlockState(), 1)
				.add(wallStairs.setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.EAST), 2)
				.add(wallStairs.setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.WEST), 2)
				.build()
			);

			final WeightedStateProvider decayedTop = new WeightedStateProvider(SimpleWeightedRandomList.<BlockState>builder()
				.add(decayedWall, 5)
				.add(decayedMaterial.get(BlockFamily.Variant.SLAB).defaultBlockState(), 1)
				.add(decayedStairs.setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.EAST), 2)
				.add(decayedStairs.setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.WEST), 2)
				.build()
			);

			return new RuinedFoundationBlocks(floor, BlockStateProvider.simple(floorMaterial.get(basementSupports).defaultBlockState()), chestBlockProvider, BlockStateProvider.simple(wallBlock), wallTop, BlockStateProvider.simple(decayedWall), decayedTop);
		}
	}
}
