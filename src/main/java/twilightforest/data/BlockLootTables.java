package twilightforest.data;

import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.CopyBlockState;
import net.minecraft.world.level.storage.loot.functions.CopyNbtFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.*;
import net.minecraft.world.level.storage.loot.providers.nbt.ContextNbtProvider;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import twilightforest.block.*;
import twilightforest.enums.HollowLogVariants;
import twilightforest.item.TFItems;

import java.util.HashSet;
import java.util.Set;

public class BlockLootTables extends net.minecraft.data.loot.BlockLoot {
	private final Set<Block> knownBlocks = new HashSet<>();
	// [VanillaCopy] super
	private static final float[] DEFAULT_SAPLING_DROP_RATES = new float[]{0.05F, 0.0625F, 0.083333336F, 0.1F};
	private static final float[] RARE_SAPLING_DROP_RATES = new float[]{0.025F, 0.027777778F, 0.03125F, 0.041666668F, 0.1F};

	@Override
	protected void add(Block block, LootTable.Builder builder) {
		super.add(block, builder);
		knownBlocks.add(block);
	}

	@Override
	protected void addTables() {
		registerEmpty(TFBlocks.EXPERIMENT_115.get());
		dropSelf(TFBlocks.TOWERWOOD.get());
		dropSelf(TFBlocks.ENCASED_TOWERWOOD.get());
		dropSelf(TFBlocks.CRACKED_TOWERWOOD.get());
		dropSelf(TFBlocks.MOSSY_TOWERWOOD.get());
		registerEmpty(TFBlocks.ANTIBUILDER.get());
		dropSelf(TFBlocks.CARMINITE_BUILDER.get());
		dropSelf(TFBlocks.GHAST_TRAP.get());
		dropSelf(TFBlocks.CARMINITE_REACTOR.get());
		dropSelf(TFBlocks.REAPPEARING_BLOCK.get());
		dropSelf(TFBlocks.VANISHING_BLOCK.get());
		dropSelf(TFBlocks.LOCKED_VANISHING_BLOCK.get());
		dropSelf(TFBlocks.FIREFLY.get());
		dropSelf(TFBlocks.CICADA.get());
		dropSelf(TFBlocks.MOONWORM.get());
		dropSelf(TFBlocks.TROPHY_PEDESTAL.get());
		//registerDropSelfLootTable(TFBlocks.TERRORCOTTA_CIRCLE.get());
		//registerDropSelfLootTable(TFBlocks.TERRORCOTTA_DIAGONAL.get());
		dropSelf(TFBlocks.AURORA_BLOCK.get());
		dropSelf(TFBlocks.AURORA_PILLAR.get());
		add(TFBlocks.AURORA_SLAB.get(), createSlabItemTable(TFBlocks.AURORA_SLAB.get()));
		dropWhenSilkTouch(TFBlocks.AURORALIZED_GLASS.get());
		dropSelf(TFBlocks.UNDERBRICK.get());
		dropSelf(TFBlocks.CRACKED_UNDERBRICK.get());
		dropSelf(TFBlocks.MOSSY_UNDERBRICK.get());
		dropSelf(TFBlocks.UNDERBRICK_FLOOR.get());
		dropSelf(TFBlocks.THORN_ROSE.get());
		add(TFBlocks.THORN_LEAVES.get(), silkAndStick(TFBlocks.THORN_LEAVES.get(), Items.STICK, RARE_SAPLING_DROP_RATES));
		add(TFBlocks.BEANSTALK_LEAVES.get(), silkAndStick(TFBlocks.BEANSTALK_LEAVES.get(), TFItems.MAGIC_BEANS.get(), RARE_SAPLING_DROP_RATES));
		dropSelf(TFBlocks.DEADROCK.get());
		dropSelf(TFBlocks.CRACKED_DEADROCK.get());
		dropSelf(TFBlocks.WEATHERED_DEADROCK.get());
		dropSelf(TFBlocks.TROLLSTEINN.get());
		dropWhenSilkTouch(TFBlocks.WISPY_CLOUD.get());
		dropSelf(TFBlocks.FLUFFY_CLOUD.get());
		dropSelf(TFBlocks.GIANT_COBBLESTONE.get());
		dropSelf(TFBlocks.GIANT_LOG.get());
		dropSelf(TFBlocks.GIANT_LEAVES.get());
		dropSelf(TFBlocks.GIANT_OBSIDIAN.get());
		add(TFBlocks.UBEROUS_SOIL.get(), createSingleItemTable(Blocks.DIRT));
		dropSelf(TFBlocks.HUGE_STALK.get());
		add(TFBlocks.HUGE_MUSHGLOOM.get(), createMushroomBlockDrop(TFBlocks.HUGE_MUSHGLOOM.get(), TFBlocks.MUSHGLOOM.get()));
		add(TFBlocks.HUGE_MUSHGLOOM_STEM.get(), createMushroomBlockDrop(TFBlocks.HUGE_MUSHGLOOM_STEM.get(), TFBlocks.MUSHGLOOM.get()));
		add(TFBlocks.TROLLVIDR.get(), createShearsOnlyDrop(TFBlocks.TROLLVIDR.get()));
		add(TFBlocks.UNRIPE_TROLLBER.get(), createShearsOnlyDrop(TFBlocks.UNRIPE_TROLLBER.get()));
		add(TFBlocks.TROLLBER.get(), createShearsDispatchTable(TFBlocks.TROLLBER.get(), LootItem.lootTableItem(TFItems.TORCHBERRIES.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0F, 8.0F))).apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))));
		dropSelf(TFBlocks.HUGE_LILY_PAD.get());
		dropSelf(TFBlocks.HUGE_WATER_LILY.get());
		dropSelf(TFBlocks.CASTLE_BRICK.get());
		dropSelf(TFBlocks.WORN_CASTLE_BRICK.get());
		dropSelf(TFBlocks.CRACKED_CASTLE_BRICK.get());
		dropSelf(TFBlocks.MOSSY_CASTLE_BRICK.get());
		dropSelf(TFBlocks.THICK_CASTLE_BRICK.get());
		dropSelf(TFBlocks.CASTLE_ROOF_TILE.get());
		dropSelf(TFBlocks.ENCASED_CASTLE_BRICK_PILLAR.get());
		dropSelf(TFBlocks.ENCASED_CASTLE_BRICK_TILE.get());
		dropSelf(TFBlocks.BOLD_CASTLE_BRICK_PILLAR.get());
		dropSelf(TFBlocks.BOLD_CASTLE_BRICK_TILE.get());
		dropSelf(TFBlocks.CASTLE_BRICK_STAIRS.get());
		dropSelf(TFBlocks.WORN_CASTLE_BRICK_STAIRS.get());
		dropSelf(TFBlocks.CRACKED_CASTLE_BRICK_STAIRS.get());
		dropSelf(TFBlocks.MOSSY_CASTLE_BRICK_STAIRS.get());
		dropSelf(TFBlocks.ENCASED_CASTLE_BRICK_STAIRS.get());
		dropSelf(TFBlocks.BOLD_CASTLE_BRICK_STAIRS.get());
		dropSelf(TFBlocks.VIOLET_CASTLE_RUNE_BRICK.get());
		dropSelf(TFBlocks.YELLOW_CASTLE_RUNE_BRICK.get());
		dropSelf(TFBlocks.PINK_CASTLE_RUNE_BRICK.get());
		dropSelf(TFBlocks.BLUE_CASTLE_RUNE_BRICK.get());
		dropSelf(TFBlocks.CINDER_FURNACE.get());
		dropSelf(TFBlocks.CINDER_LOG.get());
		dropSelf(TFBlocks.CINDER_WOOD.get());
		dropSelf(TFBlocks.VIOLET_CASTLE_DOOR.get());
		dropSelf(TFBlocks.YELLOW_CASTLE_DOOR.get());
		dropSelf(TFBlocks.PINK_CASTLE_DOOR.get());
		dropSelf(TFBlocks.BLUE_CASTLE_DOOR.get());
		dropSelf(TFBlocks.TWILIGHT_PORTAL_MINIATURE_STRUCTURE.get());
		dropSelf(TFBlocks.NAGA_COURTYARD_MINIATURE_STRUCTURE.get());
		dropSelf(TFBlocks.LICH_TOWER_MINIATURE_STRUCTURE.get());
		dropSelf(TFBlocks.KNIGHTMETAL_BLOCK.get());
		dropSelf(TFBlocks.IRONWOOD_BLOCK.get());
		dropSelf(TFBlocks.FIERY_BLOCK.get());
		dropSelf(TFBlocks.STEELEAF_BLOCK.get());
		dropSelf(TFBlocks.ARCTIC_FUR_BLOCK.get());
		dropSelf(TFBlocks.CARMINITE_BLOCK.get());
		dropSelf(TFBlocks.MAZESTONE.get());
		dropSelf(TFBlocks.MAZESTONE_BRICK.get());
		dropSelf(TFBlocks.CUT_MAZESTONE.get());
		dropSelf(TFBlocks.DECORATIVE_MAZESTONE.get());
		dropSelf(TFBlocks.CRACKED_MAZESTONE.get());
		dropSelf(TFBlocks.MOSSY_MAZESTONE.get());
		dropSelf(TFBlocks.MAZESTONE_MOSAIC.get());
		dropSelf(TFBlocks.MAZESTONE_BORDER.get());
		dropWhenSilkTouch(TFBlocks.HEDGE.get());
		add(TFBlocks.ROOT_BLOCK.get(), createSingleItemTableWithSilkTouch(TFBlocks.ROOT_BLOCK.get(), Items.STICK, UniformGenerator.between(3, 5)));
		add(TFBlocks.LIVEROOT_BLOCK.get(), createSilkTouchDispatchTable(TFBlocks.LIVEROOT_BLOCK.get(), applyExplosionCondition(TFBlocks.LIVEROOT_BLOCK.get(), LootItem.lootTableItem(TFItems.LIVEROOT.get()).apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE)))));
		add(TFBlocks.MANGROVE_ROOT.get(), createSingleItemTableWithSilkTouch(TFBlocks.MANGROVE_ROOT.get(), Items.STICK, UniformGenerator.between(3, 5)));
		dropSelf(TFBlocks.UNCRAFTING_TABLE.get());
		dropSelf(TFBlocks.FIREFLY_JAR.get());
		add(TFBlocks.FIREFLY_SPAWNER.get(), particleSpawner());
		dropSelf(TFBlocks.CICADA_JAR.get());
		add(TFBlocks.MOSS_PATCH.get(), createShearsOnlyDrop(TFBlocks.MOSS_PATCH.get()));
		add(TFBlocks.MAYAPPLE.get(), createShearsOnlyDrop(TFBlocks.MAYAPPLE.get()));
		add(TFBlocks.CLOVER_PATCH.get(), createShearsOnlyDrop(TFBlocks.CLOVER_PATCH.get()));
		add(TFBlocks.FIDDLEHEAD.get(), createShearsOnlyDrop(TFBlocks.FIDDLEHEAD.get()));
		dropSelf(TFBlocks.MUSHGLOOM.get());
		add(TFBlocks.TORCHBERRY_PLANT.get(), torchberryPlant(TFBlocks.TORCHBERRY_PLANT.get()));
		add(TFBlocks.ROOT_STRAND.get(), createShearsOnlyDrop(TFBlocks.ROOT_STRAND.get()));
		add(TFBlocks.FALLEN_LEAVES.get(), createShearsOnlyDrop(TFBlocks.FALLEN_LEAVES.get()));
		dropSelf(TFBlocks.SMOKER.get());
		dropSelf(TFBlocks.ENCASED_SMOKER.get());
		dropSelf(TFBlocks.FIRE_JET.get());
		dropSelf(TFBlocks.ENCASED_FIRE_JET.get());
		dropSelf(TFBlocks.NAGASTONE_HEAD.get());
		dropSelf(TFBlocks.NAGASTONE.get());
		dropSelf(TFBlocks.SPIRAL_BRICKS.get());
		dropSelf(TFBlocks.NAGASTONE_PILLAR.get());
		dropSelf(TFBlocks.MOSSY_NAGASTONE_PILLAR.get());
		dropSelf(TFBlocks.CRACKED_NAGASTONE_PILLAR.get());
		dropSelf(TFBlocks.ETCHED_NAGASTONE.get());
		dropSelf(TFBlocks.MOSSY_ETCHED_NAGASTONE.get());
		dropSelf(TFBlocks.CRACKED_ETCHED_NAGASTONE.get());
		dropSelf(TFBlocks.NAGASTONE_STAIRS_LEFT.get());
		dropSelf(TFBlocks.NAGASTONE_STAIRS_RIGHT.get());
		dropSelf(TFBlocks.MOSSY_NAGASTONE_STAIRS_LEFT.get());
		dropSelf(TFBlocks.MOSSY_NAGASTONE_STAIRS_RIGHT.get());
		dropSelf(TFBlocks.CRACKED_NAGASTONE_STAIRS_LEFT.get());
		dropSelf(TFBlocks.CRACKED_NAGASTONE_STAIRS_RIGHT.get());
		add(TFBlocks.NAGA_TROPHY.get(), createSingleItemTable(TFBlocks.NAGA_TROPHY.get().asItem()));
		add(TFBlocks.NAGA_WALL_TROPHY.get(), createSingleItemTable(TFBlocks.NAGA_TROPHY.get().asItem()));
		add(TFBlocks.LICH_TROPHY.get(), createSingleItemTable(TFBlocks.LICH_TROPHY.get().asItem()));
		add(TFBlocks.LICH_WALL_TROPHY.get(), createSingleItemTable(TFBlocks.LICH_TROPHY.get().asItem()));
		add(TFBlocks.MINOSHROOM_TROPHY.get(), createSingleItemTable(TFBlocks.MINOSHROOM_TROPHY.get().asItem()));
		add(TFBlocks.MINOSHROOM_WALL_TROPHY.get(), createSingleItemTable(TFBlocks.MINOSHROOM_TROPHY.get().asItem()));
		add(TFBlocks.HYDRA_TROPHY.get(), createSingleItemTable(TFBlocks.HYDRA_TROPHY.get().asItem()));
		add(TFBlocks.HYDRA_WALL_TROPHY.get(), createSingleItemTable(TFBlocks.HYDRA_TROPHY.get().asItem()));
		add(TFBlocks.KNIGHT_PHANTOM_TROPHY.get(), createSingleItemTable(TFBlocks.KNIGHT_PHANTOM_TROPHY.get().asItem()));
		add(TFBlocks.KNIGHT_PHANTOM_WALL_TROPHY.get(), createSingleItemTable(TFBlocks.KNIGHT_PHANTOM_TROPHY.get().asItem()));
		add(TFBlocks.UR_GHAST_TROPHY.get(), createSingleItemTable(TFBlocks.UR_GHAST_TROPHY.get().asItem()));
		add(TFBlocks.UR_GHAST_WALL_TROPHY.get(), createSingleItemTable(TFBlocks.UR_GHAST_TROPHY.get().asItem()));
		add(TFBlocks.ALPHA_YETI_TROPHY.get(), createSingleItemTable(TFBlocks.ALPHA_YETI_TROPHY.get().asItem()));
		add(TFBlocks.ALPHA_YETI_WALL_TROPHY.get(), createSingleItemTable(TFBlocks.ALPHA_YETI_TROPHY.get().asItem()));
		add(TFBlocks.SNOW_QUEEN_TROPHY.get(), createSingleItemTable(TFBlocks.SNOW_QUEEN_TROPHY.get().asItem()));
		add(TFBlocks.SNOW_QUEEN_WALL_TROPHY.get(), createSingleItemTable(TFBlocks.SNOW_QUEEN_TROPHY.get().asItem()));
		add(TFBlocks.QUEST_RAM_TROPHY.get(), createSingleItemTable(TFBlocks.QUEST_RAM_TROPHY.get().asItem()));
		add(TFBlocks.QUEST_RAM_WALL_TROPHY.get(), createSingleItemTable(TFBlocks.QUEST_RAM_TROPHY.get().asItem()));

		add(TFBlocks.ZOMBIE_SKULL_CANDLE.get(), dropWithoutSilk(Blocks.ZOMBIE_HEAD));
		add(TFBlocks.ZOMBIE_WALL_SKULL_CANDLE.get(), dropWithoutSilk(Blocks.ZOMBIE_HEAD));
		add(TFBlocks.SKELETON_SKULL_CANDLE.get(), dropWithoutSilk(Blocks.SKELETON_SKULL));
		add(TFBlocks.SKELETON_WALL_SKULL_CANDLE.get(), dropWithoutSilk(Blocks.SKELETON_SKULL));
		add(TFBlocks.WITHER_SKELE_SKULL_CANDLE.get(), dropWithoutSilk(Blocks.WITHER_SKELETON_SKULL));
		add(TFBlocks.WITHER_SKELE_WALL_SKULL_CANDLE.get(), dropWithoutSilk(Blocks.WITHER_SKELETON_SKULL));
		add(TFBlocks.CREEPER_SKULL_CANDLE.get(), dropWithoutSilk(Blocks.CREEPER_HEAD));
		add(TFBlocks.CREEPER_WALL_SKULL_CANDLE.get(), dropWithoutSilk(Blocks.CREEPER_HEAD));
		add(TFBlocks.PLAYER_SKULL_CANDLE.get(), dropWithoutSilk(Blocks.PLAYER_HEAD).apply(CopyNbtFunction.copyData(ContextNbtProvider.BLOCK_ENTITY).copy("SkullOwner", "SkullOwner")));
		add(TFBlocks.PLAYER_WALL_SKULL_CANDLE.get(), dropWithoutSilk(Blocks.PLAYER_HEAD).apply(CopyNbtFunction.copyData(ContextNbtProvider.BLOCK_ENTITY).copy("SkullOwner", "SkullOwner")));

		dropSelf(TFBlocks.IRON_LADDER.get());
		dropSelf(TFBlocks.TWISTED_STONE.get());
		dropSelf(TFBlocks.TWISTED_STONE_PILLAR.get());
		dropSelf(TFBlocks.BOLD_STONE_PILLAR.get());
		registerEmpty(TFBlocks.DEATH_TOME_SPAWNER.get());
		dropWhenSilkTouch(TFBlocks.EMPTY_CANOPY_BOOKSHELF.get());
		//registerDropSelfLootTable(TFBlocks.LAPIS_BLOCK.get());
		add(TFBlocks.KEEPSAKE_CASKET.get(), casketInfo(TFBlocks.KEEPSAKE_CASKET.get()));
		dropSelf(TFBlocks.CANDELABRA.get());
		dropPottedContents(TFBlocks.POTTED_TWILIGHT_OAK_SAPLING.get());
		dropPottedContents(TFBlocks.POTTED_CANOPY_SAPLING.get());
		dropPottedContents(TFBlocks.POTTED_MANGROVE_SAPLING.get());
		dropPottedContents(TFBlocks.POTTED_DARKWOOD_SAPLING.get());
		dropPottedContents(TFBlocks.POTTED_HOLLOW_OAK_SAPLING.get());
		dropPottedContents(TFBlocks.POTTED_RAINBOW_OAK_SAPLING.get());
		dropPottedContents(TFBlocks.POTTED_TIME_SAPLING.get());
		dropPottedContents(TFBlocks.POTTED_TRANSFORMATION_SAPLING.get());
		dropPottedContents(TFBlocks.POTTED_MINING_SAPLING.get());
		dropPottedContents(TFBlocks.POTTED_SORTING_SAPLING.get());
		dropPottedContents(TFBlocks.POTTED_MAYAPPLE.get());
		dropPottedContents(TFBlocks.POTTED_FIDDLEHEAD.get());
		dropPottedContents(TFBlocks.POTTED_MUSHGLOOM.get());
		dropPottedContents(TFBlocks.POTTED_THORN.get());
		dropPottedContents(TFBlocks.POTTED_GREEN_THORN.get());
		dropPottedContents(TFBlocks.POTTED_DEAD_THORN.get());

		dropSelf(TFBlocks.OAK_BANISTER.get());
		dropSelf(TFBlocks.SPRUCE_BANISTER.get());
		dropSelf(TFBlocks.BIRCH_BANISTER.get());
		dropSelf(TFBlocks.JUNGLE_BANISTER.get());
		dropSelf(TFBlocks.ACACIA_BANISTER.get());
		dropSelf(TFBlocks.DARK_OAK_BANISTER.get());
		dropSelf(TFBlocks.CRIMSON_BANISTER.get());
		dropSelf(TFBlocks.WARPED_BANISTER.get());

		add(TFBlocks.HOLLOW_OAK_LOG_HORIZONTAL.get(), hollowLog(TFBlocks.HOLLOW_OAK_LOG_HORIZONTAL.get()));
		add(TFBlocks.HOLLOW_SPRUCE_LOG_HORIZONTAL.get(), hollowLog(TFBlocks.HOLLOW_SPRUCE_LOG_HORIZONTAL.get()));
		add(TFBlocks.HOLLOW_BIRCH_LOG_HORIZONTAL.get(), hollowLog(TFBlocks.HOLLOW_BIRCH_LOG_HORIZONTAL.get()));
		add(TFBlocks.HOLLOW_JUNGLE_LOG_HORIZONTAL.get(), hollowLog(TFBlocks.HOLLOW_JUNGLE_LOG_HORIZONTAL.get()));
		add(TFBlocks.HOLLOW_ACACIA_LOG_HORIZONTAL.get(), hollowLog(TFBlocks.HOLLOW_ACACIA_LOG_HORIZONTAL.get()));
		add(TFBlocks.HOLLOW_DARK_OAK_LOG_HORIZONTAL.get(), hollowLog(TFBlocks.HOLLOW_DARK_OAK_LOG_HORIZONTAL.get()));
		add(TFBlocks.HOLLOW_CRIMSON_STEM_HORIZONTAL.get(), hollowLog(TFBlocks.HOLLOW_CRIMSON_STEM_HORIZONTAL.get()));
		add(TFBlocks.HOLLOW_WARPED_STEM_HORIZONTAL.get(), hollowLog(TFBlocks.HOLLOW_WARPED_STEM_HORIZONTAL.get()));
		add(TFBlocks.HOLLOW_TWILIGHT_OAK_LOG_HORIZONTAL.get(), hollowLog(TFBlocks.HOLLOW_TWILIGHT_OAK_LOG_HORIZONTAL.get()));
		add(TFBlocks.HOLLOW_CANOPY_LOG_HORIZONTAL.get(), hollowLog(TFBlocks.HOLLOW_CANOPY_LOG_HORIZONTAL.get()));
		add(TFBlocks.HOLLOW_MANGROVE_LOG_HORIZONTAL.get(), hollowLog(TFBlocks.HOLLOW_MANGROVE_LOG_HORIZONTAL.get()));
		add(TFBlocks.HOLLOW_DARK_LOG_HORIZONTAL.get(), hollowLog(TFBlocks.HOLLOW_DARK_LOG_HORIZONTAL.get()));
		add(TFBlocks.HOLLOW_TIME_LOG_HORIZONTAL.get(), hollowLog(TFBlocks.HOLLOW_TIME_LOG_HORIZONTAL.get()));
		add(TFBlocks.HOLLOW_TRANSFORMATION_LOG_HORIZONTAL.get(), hollowLog(TFBlocks.HOLLOW_TRANSFORMATION_LOG_HORIZONTAL.get()));
		add(TFBlocks.HOLLOW_MINING_LOG_HORIZONTAL.get(), hollowLog(TFBlocks.HOLLOW_MINING_LOG_HORIZONTAL.get()));
		add(TFBlocks.HOLLOW_SORTING_LOG_HORIZONTAL.get(), hollowLog(TFBlocks.HOLLOW_SORTING_LOG_HORIZONTAL.get()));

		add(TFBlocks.HOLLOW_OAK_LOG_VERTICAL.get(), verticalHollowLog(TFBlocks.HOLLOW_OAK_LOG_VERTICAL.get()));
		add(TFBlocks.HOLLOW_SPRUCE_LOG_VERTICAL.get(), verticalHollowLog(TFBlocks.HOLLOW_SPRUCE_LOG_VERTICAL.get()));
		add(TFBlocks.HOLLOW_BIRCH_LOG_VERTICAL.get(), verticalHollowLog(TFBlocks.HOLLOW_BIRCH_LOG_VERTICAL.get()));
		add(TFBlocks.HOLLOW_JUNGLE_LOG_VERTICAL.get(), verticalHollowLog(TFBlocks.HOLLOW_JUNGLE_LOG_VERTICAL.get()));
		add(TFBlocks.HOLLOW_ACACIA_LOG_VERTICAL.get(), verticalHollowLog(TFBlocks.HOLLOW_ACACIA_LOG_VERTICAL.get()));
		add(TFBlocks.HOLLOW_DARK_OAK_LOG_VERTICAL.get(), verticalHollowLog(TFBlocks.HOLLOW_DARK_OAK_LOG_VERTICAL.get()));
		add(TFBlocks.HOLLOW_CRIMSON_STEM_VERTICAL.get(), verticalHollowLog(TFBlocks.HOLLOW_CRIMSON_STEM_VERTICAL.get()));
		add(TFBlocks.HOLLOW_WARPED_STEM_VERTICAL.get(), verticalHollowLog(TFBlocks.HOLLOW_WARPED_STEM_VERTICAL.get()));
		add(TFBlocks.HOLLOW_TWILIGHT_OAK_LOG_VERTICAL.get(), verticalHollowLog(TFBlocks.HOLLOW_TWILIGHT_OAK_LOG_VERTICAL.get()));
		add(TFBlocks.HOLLOW_CANOPY_LOG_VERTICAL.get(), verticalHollowLog(TFBlocks.HOLLOW_CANOPY_LOG_VERTICAL.get()));
		add(TFBlocks.HOLLOW_MANGROVE_LOG_VERTICAL.get(), verticalHollowLog(TFBlocks.HOLLOW_MANGROVE_LOG_VERTICAL.get()));
		add(TFBlocks.HOLLOW_DARK_LOG_VERTICAL.get(), verticalHollowLog(TFBlocks.HOLLOW_DARK_LOG_VERTICAL.get()));
		add(TFBlocks.HOLLOW_TIME_LOG_VERTICAL.get(), verticalHollowLog(TFBlocks.HOLLOW_TIME_LOG_VERTICAL.get()));
		add(TFBlocks.HOLLOW_TRANSFORMATION_LOG_VERTICAL.get(), verticalHollowLog(TFBlocks.HOLLOW_TRANSFORMATION_LOG_VERTICAL.get()));
		add(TFBlocks.HOLLOW_MINING_LOG_VERTICAL.get(), verticalHollowLog(TFBlocks.HOLLOW_MINING_LOG_VERTICAL.get()));
		add(TFBlocks.HOLLOW_SORTING_LOG_VERTICAL.get(), verticalHollowLog(TFBlocks.HOLLOW_SORTING_LOG_VERTICAL.get()));

		add(TFBlocks.HOLLOW_OAK_LOG_CLIMBABLE.get(), hollowLog(TFBlocks.HOLLOW_OAK_LOG_CLIMBABLE.get()));
		add(TFBlocks.HOLLOW_SPRUCE_LOG_CLIMBABLE.get(), hollowLog(TFBlocks.HOLLOW_SPRUCE_LOG_CLIMBABLE.get()));
		add(TFBlocks.HOLLOW_BIRCH_LOG_CLIMBABLE.get(), hollowLog(TFBlocks.HOLLOW_BIRCH_LOG_CLIMBABLE.get()));
		add(TFBlocks.HOLLOW_JUNGLE_LOG_CLIMBABLE.get(), hollowLog(TFBlocks.HOLLOW_JUNGLE_LOG_CLIMBABLE.get()));
		add(TFBlocks.HOLLOW_ACACIA_LOG_CLIMBABLE.get(), hollowLog(TFBlocks.HOLLOW_ACACIA_LOG_CLIMBABLE.get()));
		add(TFBlocks.HOLLOW_DARK_OAK_LOG_CLIMBABLE.get(), hollowLog(TFBlocks.HOLLOW_DARK_OAK_LOG_CLIMBABLE.get()));
		add(TFBlocks.HOLLOW_CRIMSON_STEM_CLIMBABLE.get(), hollowLog(TFBlocks.HOLLOW_CRIMSON_STEM_CLIMBABLE.get()));
		add(TFBlocks.HOLLOW_WARPED_STEM_CLIMBABLE.get(), hollowLog(TFBlocks.HOLLOW_WARPED_STEM_CLIMBABLE.get()));
		add(TFBlocks.HOLLOW_TWILIGHT_OAK_LOG_CLIMBABLE.get(), hollowLog(TFBlocks.HOLLOW_TWILIGHT_OAK_LOG_CLIMBABLE.get()));
		add(TFBlocks.HOLLOW_CANOPY_LOG_CLIMBABLE.get(), hollowLog(TFBlocks.HOLLOW_CANOPY_LOG_CLIMBABLE.get()));
		add(TFBlocks.HOLLOW_MANGROVE_LOG_CLIMBABLE.get(), hollowLog(TFBlocks.HOLLOW_MANGROVE_LOG_CLIMBABLE.get()));
		add(TFBlocks.HOLLOW_DARK_LOG_CLIMBABLE.get(), hollowLog(TFBlocks.HOLLOW_DARK_LOG_CLIMBABLE.get()));
		add(TFBlocks.HOLLOW_TIME_LOG_CLIMBABLE.get(), hollowLog(TFBlocks.HOLLOW_TIME_LOG_CLIMBABLE.get()));
		add(TFBlocks.HOLLOW_TRANSFORMATION_LOG_CLIMBABLE.get(), hollowLog(TFBlocks.HOLLOW_TRANSFORMATION_LOG_CLIMBABLE.get()));
		add(TFBlocks.HOLLOW_MINING_LOG_CLIMBABLE.get(), hollowLog(TFBlocks.HOLLOW_MINING_LOG_CLIMBABLE.get()));
		add(TFBlocks.HOLLOW_SORTING_LOG_CLIMBABLE.get(), hollowLog(TFBlocks.HOLLOW_SORTING_LOG_CLIMBABLE.get()));


		dropSelf(TFBlocks.TWILIGHT_OAK_LOG.get());
		dropSelf(TFBlocks.STRIPPED_TWILIGHT_OAK_LOG.get());
		dropSelf(TFBlocks.TWILIGHT_OAK_WOOD.get());
		dropSelf(TFBlocks.STRIPPED_TWILIGHT_OAK_WOOD.get());
		dropSelf(TFBlocks.TWILIGHT_OAK_SAPLING.get());
		add(TFBlocks.TWILIGHT_OAK_LEAVES.get(), createLeavesDrops(TFBlocks.TWILIGHT_OAK_LEAVES.get(), TFBlocks.TWILIGHT_OAK_SAPLING.get(), DEFAULT_SAPLING_DROP_RATES));
		dropSelf(TFBlocks.RAINBOW_OAK_SAPLING.get());
		add(TFBlocks.RAINBOW_OAK_LEAVES.get(), createLeavesDrops(TFBlocks.RAINBOW_OAK_LEAVES.get(), TFBlocks.RAINBOW_OAK_SAPLING.get(), RARE_SAPLING_DROP_RATES));
		dropSelf(TFBlocks.HOLLOW_OAK_SAPLING.get());
		dropSelf(TFBlocks.TWILIGHT_OAK_PLANKS.get());
		dropSelf(TFBlocks.TWILIGHT_OAK_STAIRS.get());
		add(TFBlocks.TWILIGHT_OAK_SLAB.get(), createSlabItemTable(TFBlocks.TWILIGHT_OAK_SLAB.get()));
		dropSelf(TFBlocks.TWILIGHT_OAK_BUTTON.get());
		dropSelf(TFBlocks.TWILIGHT_OAK_FENCE.get());
		dropSelf(TFBlocks.TWILIGHT_OAK_GATE.get());
		dropSelf(TFBlocks.TWILIGHT_OAK_PLATE.get());
		add(TFBlocks.TWILIGHT_OAK_DOOR.get(), createSinglePropConditionTable(TFBlocks.TWILIGHT_OAK_DOOR.get(), DoorBlock.HALF, DoubleBlockHalf.LOWER));
		dropSelf(TFBlocks.TWILIGHT_OAK_TRAPDOOR.get());
		add(TFBlocks.TWILIGHT_OAK_SIGN.get(), createSingleItemTable(TFBlocks.TWILIGHT_OAK_SIGN.get().asItem()));
		add(TFBlocks.TWILIGHT_WALL_SIGN.get(), createSingleItemTable(TFBlocks.TWILIGHT_OAK_SIGN.get().asItem()));
		dropSelf(TFBlocks.TWILIGHT_OAK_BANISTER.get());

		dropSelf(TFBlocks.CANOPY_LOG.get());
		dropSelf(TFBlocks.STRIPPED_CANOPY_LOG.get());
		dropSelf(TFBlocks.CANOPY_WOOD.get());
		dropSelf(TFBlocks.STRIPPED_CANOPY_WOOD.get());
		dropSelf(TFBlocks.CANOPY_SAPLING.get());
		add(TFBlocks.CANOPY_LEAVES.get(), createLeavesDrops(TFBlocks.CANOPY_LEAVES.get(), TFBlocks.CANOPY_SAPLING.get(), DEFAULT_SAPLING_DROP_RATES));
		dropSelf(TFBlocks.CANOPY_PLANKS.get());
		dropSelf(TFBlocks.CANOPY_STAIRS.get());
		add(TFBlocks.CANOPY_SLAB.get(), createSlabItemTable(TFBlocks.CANOPY_SLAB.get()));
		dropSelf(TFBlocks.CANOPY_BUTTON.get());
		dropSelf(TFBlocks.CANOPY_FENCE.get());
		dropSelf(TFBlocks.CANOPY_GATE.get());
		dropSelf(TFBlocks.CANOPY_PLATE.get());
		add(TFBlocks.CANOPY_DOOR.get(), createSinglePropConditionTable(TFBlocks.CANOPY_DOOR.get(), DoorBlock.HALF, DoubleBlockHalf.LOWER));
		dropSelf(TFBlocks.CANOPY_TRAPDOOR.get());
		add(TFBlocks.CANOPY_SIGN.get(), createSingleItemTable(TFBlocks.CANOPY_SIGN.get().asItem()));
		add(TFBlocks.CANOPY_WALL_SIGN.get(), createSingleItemTable(TFBlocks.CANOPY_SIGN.get().asItem()));
		add(TFBlocks.CANOPY_BOOKSHELF.get(), createSingleItemTableWithSilkTouch(TFBlocks.CANOPY_BOOKSHELF.get(), Items.BOOK, ConstantValue.exactly(3.0F)));
		dropSelf(TFBlocks.CANOPY_BANISTER.get());

		dropSelf(TFBlocks.MANGROVE_LOG.get());
		dropSelf(TFBlocks.STRIPPED_MANGROVE_LOG.get());
		dropSelf(TFBlocks.MANGROVE_WOOD.get());
		dropSelf(TFBlocks.STRIPPED_MANGROVE_WOOD.get());
		dropSelf(TFBlocks.MANGROVE_SAPLING.get());
		add(TFBlocks.MANGROVE_LEAVES.get(), createLeavesDrops(TFBlocks.MANGROVE_LEAVES.get(), TFBlocks.MANGROVE_SAPLING.get(), DEFAULT_SAPLING_DROP_RATES));
		dropSelf(TFBlocks.MANGROVE_PLANKS.get());
		dropSelf(TFBlocks.MANGROVE_STAIRS.get());
		add(TFBlocks.MANGROVE_SLAB.get(), createSlabItemTable(TFBlocks.MANGROVE_SLAB.get()));
		dropSelf(TFBlocks.MANGROVE_BUTTON.get());
		dropSelf(TFBlocks.MANGROVE_FENCE.get());
		dropSelf(TFBlocks.MANGROVE_GATE.get());
		dropSelf(TFBlocks.MANGROVE_PLATE.get());
		add(TFBlocks.MANGROVE_DOOR.get(), createSinglePropConditionTable(TFBlocks.MANGROVE_DOOR.get(), DoorBlock.HALF, DoubleBlockHalf.LOWER));
		dropSelf(TFBlocks.MANGROVE_TRAPDOOR.get());
		add(TFBlocks.MANGROVE_SIGN.get(), createSingleItemTable(TFBlocks.MANGROVE_SIGN.get().asItem()));
		add(TFBlocks.MANGROVE_WALL_SIGN.get(), createSingleItemTable(TFBlocks.MANGROVE_SIGN.get().asItem()));
		dropSelf(TFBlocks.MANGROVE_BANISTER.get());

		dropSelf(TFBlocks.DARK_LOG.get());
		dropSelf(TFBlocks.STRIPPED_DARK_LOG.get());
		dropSelf(TFBlocks.DARK_WOOD.get());
		dropSelf(TFBlocks.STRIPPED_DARK_WOOD.get());
		dropSelf(TFBlocks.DARKWOOD_SAPLING.get());
		add(TFBlocks.DARK_LEAVES.get(), createLeavesDrops(TFBlocks.DARK_LEAVES.get(), TFBlocks.DARKWOOD_SAPLING.get(), RARE_SAPLING_DROP_RATES));
		add(TFBlocks.HARDENED_DARK_LEAVES.get(), createLeavesDrops(TFBlocks.DARK_LEAVES.get(), TFBlocks.DARKWOOD_SAPLING.get(), RARE_SAPLING_DROP_RATES));
		dropSelf(TFBlocks.DARK_PLANKS.get());
		dropSelf(TFBlocks.DARK_STAIRS.get());
		add(TFBlocks.DARK_SLAB.get(), createSlabItemTable(TFBlocks.DARK_SLAB.get()));
		dropSelf(TFBlocks.DARK_BUTTON.get());
		dropSelf(TFBlocks.DARK_FENCE.get());
		dropSelf(TFBlocks.DARK_GATE.get());
		dropSelf(TFBlocks.DARK_PLATE.get());
		add(TFBlocks.DARK_DOOR.get(), createSinglePropConditionTable(TFBlocks.DARK_DOOR.get(), DoorBlock.HALF, DoubleBlockHalf.LOWER));
		dropSelf(TFBlocks.DARK_TRAPDOOR.get());
		add(TFBlocks.DARKWOOD_SIGN.get(), createSingleItemTable(TFBlocks.DARKWOOD_SIGN.get().asItem()));
		add(TFBlocks.DARKWOOD_WALL_SIGN.get(), createSingleItemTable(TFBlocks.DARKWOOD_SIGN.get().asItem()));
		dropSelf(TFBlocks.DARKWOOD_BANISTER.get());

		dropSelf(TFBlocks.TIME_LOG.get());
		dropSelf(TFBlocks.STRIPPED_TIME_LOG.get());
		dropSelf(TFBlocks.TIME_WOOD.get());
		dropSelf(TFBlocks.STRIPPED_TIME_WOOD.get());
		dropOther(TFBlocks.TIME_LOG_CORE.get(), TFBlocks.TIME_LOG.get());
		dropSelf(TFBlocks.TIME_SAPLING.get());
		registerLeavesNoSapling(TFBlocks.TIME_LEAVES.get());
		dropSelf(TFBlocks.TIME_PLANKS.get());
		dropSelf(TFBlocks.TIME_STAIRS.get());
		add(TFBlocks.TIME_SLAB.get(), createSlabItemTable(TFBlocks.TIME_SLAB.get()));
		dropSelf(TFBlocks.TIME_BUTTON.get());
		dropSelf(TFBlocks.TIME_FENCE.get());
		dropSelf(TFBlocks.TIME_GATE.get());
		dropSelf(TFBlocks.TIME_PLATE.get());
		add(TFBlocks.TIME_DOOR.get(), createSinglePropConditionTable(TFBlocks.TIME_DOOR.get(), DoorBlock.HALF, DoubleBlockHalf.LOWER));
		dropSelf(TFBlocks.TIME_TRAPDOOR.get());
		add(TFBlocks.TIME_SIGN.get(), createSingleItemTable(TFBlocks.TIME_SIGN.get().asItem()));
		add(TFBlocks.TIME_WALL_SIGN.get(), createSingleItemTable(TFBlocks.TIME_SIGN.get().asItem()));
		dropSelf(TFBlocks.TIME_BANISTER.get());

		dropSelf(TFBlocks.TRANSFORMATION_LOG.get());
		dropSelf(TFBlocks.STRIPPED_TRANSFORMATION_LOG.get());
		dropSelf(TFBlocks.TRANSFORMATION_WOOD.get());
		dropSelf(TFBlocks.STRIPPED_TRANSFORMATION_WOOD.get());
		dropOther(TFBlocks.TRANSFORMATION_LOG_CORE.get(), TFBlocks.TRANSFORMATION_LOG.get());
		dropSelf(TFBlocks.TRANSFORMATION_SAPLING.get());
		registerLeavesNoSapling(TFBlocks.TRANSFORMATION_LEAVES.get());
		dropSelf(TFBlocks.TRANSFORMATION_PLANKS.get());
		dropSelf(TFBlocks.TRANSFORMATION_STAIRS.get());
		add(TFBlocks.TRANSFORMATION_SLAB.get(), createSlabItemTable(TFBlocks.TRANSFORMATION_SLAB.get()));
		dropSelf(TFBlocks.TRANSFORMATION_BUTTON.get());
		dropSelf(TFBlocks.TRANSFORMATION_FENCE.get());
		dropSelf(TFBlocks.TRANSFORMATION_GATE.get());
		dropSelf(TFBlocks.TRANSFORMATION_PLATE.get());
		add(TFBlocks.TRANSFORMATION_DOOR.get(), createSinglePropConditionTable(TFBlocks.TRANSFORMATION_DOOR.get(), DoorBlock.HALF, DoubleBlockHalf.LOWER));
		dropSelf(TFBlocks.TRANSFORMATION_TRAPDOOR.get());
		add(TFBlocks.TRANSFORMATION_SIGN.get(), createSingleItemTable(TFBlocks.TRANSFORMATION_SIGN.get().asItem()));
		add(TFBlocks.TRANSFORMATION_WALL_SIGN.get(), createSingleItemTable(TFBlocks.TRANSFORMATION_SIGN.get().asItem()));
		dropSelf(TFBlocks.TRANSFORMATION_BANISTER.get());

		dropSelf(TFBlocks.MINING_LOG.get());
		dropSelf(TFBlocks.STRIPPED_MINING_LOG.get());
		dropSelf(TFBlocks.MINING_WOOD.get());
		dropSelf(TFBlocks.STRIPPED_MINING_WOOD.get());
		dropOther(TFBlocks.MINING_LOG_CORE.get(), TFBlocks.MINING_LOG.get());
		dropSelf(TFBlocks.MINING_SAPLING.get());
		registerLeavesNoSapling(TFBlocks.MINING_LEAVES.get());
		dropSelf(TFBlocks.MINING_PLANKS.get());
		dropSelf(TFBlocks.MINING_STAIRS.get());
		add(TFBlocks.MINING_SLAB.get(), createSlabItemTable(TFBlocks.MINING_SLAB.get()));
		dropSelf(TFBlocks.MINING_BUTTON.get());
		dropSelf(TFBlocks.MINING_FENCE.get());
		dropSelf(TFBlocks.MINING_GATE.get());
		dropSelf(TFBlocks.MINING_PLATE.get());
		add(TFBlocks.MINING_DOOR.get(), createSinglePropConditionTable(TFBlocks.MINING_DOOR.get(), DoorBlock.HALF, DoubleBlockHalf.LOWER));
		dropSelf(TFBlocks.MINING_TRAPDOOR.get());
		add(TFBlocks.MINING_SIGN.get(), createSingleItemTable(TFBlocks.MINING_SIGN.get().asItem()));
		add(TFBlocks.MINING_WALL_SIGN.get(), createSingleItemTable(TFBlocks.MINING_SIGN.get().asItem()));
		dropSelf(TFBlocks.MINING_BANISTER.get());

		dropSelf(TFBlocks.SORTING_LOG.get());
		dropSelf(TFBlocks.STRIPPED_SORTING_LOG.get());
		dropSelf(TFBlocks.SORTING_WOOD.get());
		dropSelf(TFBlocks.STRIPPED_SORTING_WOOD.get());
		dropOther(TFBlocks.SORTING_LOG_CORE.get(), TFBlocks.SORTING_LOG.get());
		dropSelf(TFBlocks.SORTING_SAPLING.get());
		registerLeavesNoSapling(TFBlocks.SORTING_LEAVES.get());
		dropSelf(TFBlocks.SORTING_PLANKS.get());
		dropSelf(TFBlocks.SORTING_STAIRS.get());
		add(TFBlocks.SORTING_SLAB.get(), createSlabItemTable(TFBlocks.SORTING_SLAB.get()));
		dropSelf(TFBlocks.SORTING_BUTTON.get());
		dropSelf(TFBlocks.SORTING_FENCE.get());
		dropSelf(TFBlocks.SORTING_GATE.get());
		dropSelf(TFBlocks.SORTING_PLATE.get());
		add(TFBlocks.SORTING_DOOR.get(), createSinglePropConditionTable(TFBlocks.SORTING_DOOR.get(), DoorBlock.HALF, DoubleBlockHalf.LOWER));
		dropSelf(TFBlocks.SORTING_TRAPDOOR.get());
		add(TFBlocks.SORTING_SIGN.get(), createSingleItemTable(TFBlocks.SORTING_SIGN.get().asItem()));
		add(TFBlocks.SORTING_WALL_SIGN.get(), createSingleItemTable(TFBlocks.SORTING_SIGN.get().asItem()));
		dropSelf(TFBlocks.SORTING_BANISTER.get());

	}

	private void registerLeavesNoSapling(Block leaves) {
		LootPoolEntryContainer.Builder<?> sticks = applyExplosionDecay(leaves, LootItem.lootTableItem(Items.STICK)
				.apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F)))
				.when(BonusLevelTableCondition.bonusLevelFlatChance(Enchantments.BLOCK_FORTUNE, 0.02F, 0.022222223F, 0.025F, 0.033333335F, 0.1F)));
		add(leaves, createSilkTouchOrShearsDispatchTable(leaves, sticks));
	}

	private LootTable.Builder hollowLog(Block log) {
		LootItemCondition.Builder HAS_SILK_TOUCH = ObfuscationReflectionHelper.getPrivateValue(net.minecraft.data.loot.BlockLoot.class, null, "f_124062_");
		return LootTable.lootTable()
				.withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
						.add(LootItem.lootTableItem(log.asItem()).when(HAS_SILK_TOUCH).otherwise(LootItem.lootTableItem(Items.STICK).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 4.0F))).apply(ApplyBonusCount.addUniformBonusCount(Enchantments.BLOCK_FORTUNE)))))
				.withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
						.add(LootItem.lootTableItem(Blocks.GRASS).when(HAS_SILK_TOUCH).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(log).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(HollowLogHorizontal.VARIANT, HollowLogVariants.Horizontal.MOSS_AND_GRASS)))))
				.withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
						.add(LootItem.lootTableItem(TFBlocks.MOSS_PATCH.get()).when(HAS_SILK_TOUCH).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(log).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(HollowLogHorizontal.VARIANT, HollowLogVariants.Horizontal.MOSS_AND_GRASS)))))
				.withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
						.add(LootItem.lootTableItem(TFBlocks.MOSS_PATCH.get()).when(HAS_SILK_TOUCH).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(log).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(HollowLogHorizontal.VARIANT, HollowLogVariants.Horizontal.MOSS)))))
				.withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
						.add(LootItem.lootTableItem(Items.SNOWBALL).when(HAS_SILK_TOUCH).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(log).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(HollowLogHorizontal.VARIANT, HollowLogVariants.Horizontal.SNOW)))))
				.withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
						.add(LootItem.lootTableItem(Blocks.VINE).when(HAS_SILK_TOUCH).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(log).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(HollowLogClimbable.VARIANT, HollowLogVariants.Climbable.VINE)))))
				.withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
						.add(LootItem.lootTableItem(Blocks.LADDER).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(log).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(HollowLogClimbable.VARIANT, HollowLogVariants.Climbable.LADDER)))))
				.withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
						.add(LootItem.lootTableItem(Blocks.LADDER).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(log).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(HollowLogClimbable.VARIANT, HollowLogVariants.Climbable.LADDER_WATERLOGGED)))));
	}

	private LootTable.Builder verticalHollowLog(Block log) {
		LootItemCondition.Builder HAS_SILK_TOUCH = ObfuscationReflectionHelper.getPrivateValue(net.minecraft.data.loot.BlockLoot.class, null, "f_124062_");
		return LootTable.lootTable()
				.withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
						.add(LootItem.lootTableItem(log.asItem()).when(HAS_SILK_TOUCH).otherwise(LootItem.lootTableItem(Items.STICK).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 4.0F))).apply(ApplyBonusCount.addUniformBonusCount(Enchantments.BLOCK_FORTUNE)))));
	}

	// [VanillaCopy] super.droppingWithChancesAndSticks, but non-silk touch parameter can be an item instead of a block
	private static LootTable.Builder silkAndStick(Block block, ItemLike nonSilk, float... nonSilkFortune) {
		LootItemCondition.Builder NOT_SILK_TOUCH_OR_SHEARS = ObfuscationReflectionHelper.getPrivateValue(net.minecraft.data.loot.BlockLoot.class, null, "f_124066_");
		return createSilkTouchOrShearsDispatchTable(block, applyExplosionCondition(block, LootItem.lootTableItem(nonSilk.asItem())).when(BonusLevelTableCondition.bonusLevelFlatChance(Enchantments.BLOCK_FORTUNE, nonSilkFortune))).withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).when(NOT_SILK_TOUCH_OR_SHEARS).add(applyExplosionDecay(block, LootItem.lootTableItem(Items.STICK).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F)))).when(BonusLevelTableCondition.bonusLevelFlatChance(Enchantments.BLOCK_FORTUNE, 0.02F, 0.022222223F, 0.025F, 0.033333335F, 0.1F))));
	}

	private static LootTable.Builder casketInfo(Block block) {
		return LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).apply(CopyBlockState.copyState(block).copy(KeepsakeCasketBlock.BREAKAGE)));
	}

	private static LootTable.Builder particleSpawner() {
		return LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
						.add(applyExplosionDecay(TFBlocks.FIREFLY_SPAWNER.get(), LootItem.lootTableItem(TFBlocks.FIREFLY_SPAWNER.get()))))
				.withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
						.add(LootItem.lootTableItem(TFBlocks.FIREFLY.get())
								.apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F)).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(TFBlocks.FIREFLY_SPAWNER.get()).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(AbstractParticleSpawnerBlock.RADIUS, 2))))
								.apply(SetItemCountFunction.setCount(ConstantValue.exactly(2.0F)).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(TFBlocks.FIREFLY_SPAWNER.get()).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(AbstractParticleSpawnerBlock.RADIUS, 3))))
								.apply(SetItemCountFunction.setCount(ConstantValue.exactly(3.0F)).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(TFBlocks.FIREFLY_SPAWNER.get()).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(AbstractParticleSpawnerBlock.RADIUS, 4))))
								.apply(SetItemCountFunction.setCount(ConstantValue.exactly(4.0F)).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(TFBlocks.FIREFLY_SPAWNER.get()).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(AbstractParticleSpawnerBlock.RADIUS, 5))))
								.apply(SetItemCountFunction.setCount(ConstantValue.exactly(5.0F)).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(TFBlocks.FIREFLY_SPAWNER.get()).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(AbstractParticleSpawnerBlock.RADIUS, 6))))
								.apply(SetItemCountFunction.setCount(ConstantValue.exactly(6.0F)).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(TFBlocks.FIREFLY_SPAWNER.get()).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(AbstractParticleSpawnerBlock.RADIUS, 7))))
								.apply(SetItemCountFunction.setCount(ConstantValue.exactly(7.0F)).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(TFBlocks.FIREFLY_SPAWNER.get()).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(AbstractParticleSpawnerBlock.RADIUS, 8))))
								.apply(SetItemCountFunction.setCount(ConstantValue.exactly(8.0F)).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(TFBlocks.FIREFLY_SPAWNER.get()).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(AbstractParticleSpawnerBlock.RADIUS, 9))))
								.apply(SetItemCountFunction.setCount(ConstantValue.exactly(9.0F)).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(TFBlocks.FIREFLY_SPAWNER.get()).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(AbstractParticleSpawnerBlock.RADIUS, 10))))));
	}

	protected static LootTable.Builder torchberryPlant(Block pBlock) {
		LootItemCondition.Builder HAS_SHEARS = ObfuscationReflectionHelper.getPrivateValue(net.minecraft.data.loot.BlockLoot.class, null, "f_124064_");
		return LootTable.lootTable()
				.withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
						.add(LootItem.lootTableItem(pBlock).when(HAS_SHEARS)))
				.withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
						.add(LootItem.lootTableItem(TFItems.TORCHBERRIES.get())
								.when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(pBlock).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(TorchberryPlantBlock.HAS_BERRIES, true)))));
	}

	private static LootTable.Builder dropWithoutSilk(Block block) {
		LootItemCondition.Builder HAS_SILK_TOUCH = ObfuscationReflectionHelper.getPrivateValue(net.minecraft.data.loot.BlockLoot.class, null, "f_124062_");
		return LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).when(HAS_SILK_TOUCH.invert()).add(LootItem.lootTableItem(block)));
	}

	private void registerEmpty(Block b) {
		add(b, LootTable.lootTable());
	}

	@Override
	protected Iterable<Block> getKnownBlocks() {
		// todo 1.15 once all blockitems are ported, change this to all TF blocks, so an error will be thrown if we're missing any tables
		return knownBlocks;
	}
}
