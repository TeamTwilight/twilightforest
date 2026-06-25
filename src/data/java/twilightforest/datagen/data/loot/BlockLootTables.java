package twilightforest.datagen.data.loot;

import net.minecraft.advancements.criterion.StatePropertiesPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.AlternativesEntry;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.CopyBlockState;
import net.minecraft.world.level.storage.loot.functions.CopyComponentsFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.BonusLevelTableCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.loot.CanItemPerformAbility;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import twilightforest.block.*;
import twilightforest.enums.HollowLogVariants;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFDataComponents;
import twilightforest.init.TFItems;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class BlockLootTables extends BlockLootSubProvider {
	// [VanillaCopy] of BlockLoot fields, just changed shears to work with modded ones
	private static final float[] DEFAULT_SAPLING_DROP_RATES = new float[]{0.05F, 0.0625F, 0.083333336F, 0.1F};
	private static final float[] RARE_SAPLING_DROP_RATES = new float[]{0.025F, 0.027777778F, 0.03125F, 0.041666668F, 0.1F};
	private static final LootItemCondition.Builder HAS_SHEARS = CanItemPerformAbility.canItemPerformAbility(ItemAbilities.SHEARS_DIG);

	public BlockLootTables(HolderLookup.Provider provider) {
		super(Set.of(), FeatureFlags.REGISTRY.allFlags(), provider);
	}

	@Override
	protected void generate() {
		HolderLookup.RegistryLookup<Enchantment> registrylookup = this.registries.lookupOrThrow(Registries.ENCHANTMENT);

		dropSelf(TFBlocks.TOWERWOOD.get());
		dropSelf(TFBlocks.ENCASED_TOWERWOOD.get());
		dropSelf(TFBlocks.CRACKED_TOWERWOOD.get());
		dropSelf(TFBlocks.MOSSY_TOWERWOOD.get());
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
		dropSelf(TFBlocks.RAINY_CLOUD.get());
		dropSelf(TFBlocks.SNOWY_CLOUD.get());
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
		add(TFBlocks.TROLLBER.get(), createShearsDispatchTable(TFBlocks.TROLLBER.get(), LootItem.lootTableItem(TFItems.TORCHBERRIES.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0F, 8.0F))).apply(ApplyBonusCount.addOreBonusCount(registrylookup.getOrThrow(Enchantments.FORTUNE)))));
		add(TFBlocks.HUGE_LILY_PAD.get(), createSingleItemTable(TFItems.HUGE_LILY_PAD.get()));
		add(TFBlocks.HUGE_WATER_LILY.get(), createSingleItemTable(TFItems.HUGE_WATER_LILY.get()));
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
		add(TFBlocks.MINOTAUR_LABYRINTH_MINIATURE_STRUCTURE.get(), noDrop());
		add(TFBlocks.DARK_TOWER_MINIATURE_STRUCTURE.get(), noDrop());
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
		dropSelf(TFBlocks.MAZE_SLIME_BLOCK.get());
		add(TFBlocks.RED_THREAD.get(), redThread());
		dropWhenSilkTouch(TFBlocks.HEDGE.get());
		add(TFBlocks.ROOT_BLOCK.get(), createSingleItemTableWithSilkTouch(TFBlocks.ROOT_BLOCK.get(), Items.STICK, UniformGenerator.between(3, 5)));
		add(TFBlocks.LIVEROOT_BLOCK.get(), createSilkTouchDispatchTable(TFBlocks.LIVEROOT_BLOCK.get(), applyExplosionCondition(TFBlocks.LIVEROOT_BLOCK.get(), LootItem.lootTableItem(TFItems.LIVEROOT.get()).apply(ApplyBonusCount.addOreBonusCount(registrylookup.getOrThrow(Enchantments.FORTUNE))))));
		add(TFBlocks.MANGROVE_ROOT.get(), createSingleItemTableWithSilkTouch(TFBlocks.MANGROVE_ROOT.get(), Items.STICK, UniformGenerator.between(3, 5)));
		dropSelf(TFBlocks.UNCRAFTING_TABLE.get());
		add(TFBlocks.BRAZIER.get(), block -> this.createSinglePropConditionTable(block, BrazierBlock.HALF, DoubleBlockHalf.LOWER));

		this.add(TFBlocks.MASON_JAR.get(), LootTable.lootTable().withPool(
			this.applyExplosionCondition(
				TFBlocks.MASON_JAR.get(),
				LootPool.lootPool()
					.setRolls(ConstantValue.exactly(1.0F))
					.add(
						LootItem.lootTableItem(TFItems.MASON_JAR.get())
							.apply(
								CopyComponentsFunction.copyComponentsFromBlockEntity(LootContextParams.BLOCK_ENTITY)
									.include(DataComponents.CUSTOM_NAME)
									.include(DataComponents.CONTAINER)
									.include(DataComponents.LOCK)
									.include(DataComponents.CONTAINER_LOOT)
									.include(TFDataComponents.JAR_LID.get())
							)
					)
			)
		));

		this.add(TFBlocks.FIREFLY_JAR.get(), LootTable.lootTable().withPool(
			this.applyExplosionCondition(
				TFBlocks.FIREFLY_JAR.get(),
				LootPool.lootPool()
					.setRolls(ConstantValue.exactly(1.0F))
					.add(
						LootItem.lootTableItem(TFItems.FIREFLY_JAR.get())
							.apply(
								CopyComponentsFunction.copyComponentsFromBlockEntity(LootContextParams.BLOCK_ENTITY)
									.include(TFDataComponents.JAR_LID.get())
							)
					)
			)
		));

		this.add(TFBlocks.CICADA_JAR.get(), LootTable.lootTable().withPool(
			this.applyExplosionCondition(
				TFBlocks.CICADA_JAR.get(),
				LootPool.lootPool()
					.setRolls(ConstantValue.exactly(1.0F))
					.add(
						LootItem.lootTableItem(TFItems.CICADA_JAR.get())
							.apply(
								CopyComponentsFunction.copyComponentsFromBlockEntity(LootContextParams.BLOCK_ENTITY)
									.include(TFDataComponents.JAR_LID.get())
							)
					)
			)
		));

		add(TFBlocks.FIREFLY_SPAWNER.get(), particleSpawner());
		add(TFBlocks.MOSS_PATCH.get(), createShearsOnlyDrop(TFBlocks.MOSS_PATCH.get()));
		add(TFBlocks.MAYAPPLE.get(), createShearsOnlyDrop(TFBlocks.MAYAPPLE.get()));
		add(TFBlocks.CLOVER_PATCH.get(), createShearsOnlyDrop(TFBlocks.CLOVER_PATCH.get()));
		add(TFBlocks.FIDDLEHEAD.get(), createShearsOnlyDrop(TFBlocks.FIDDLEHEAD.get()));
		dropSelf(TFBlocks.MUSHGLOOM.get());
		add(TFBlocks.IRON_OREBERRY_BUSH.get(), noDrop());
		add(TFBlocks.GOLD_OREBERRY_BUSH.get(), noDrop());
		add(TFBlocks.COPPER_OREBERRY_BUSH.get(), noDrop());
		add(TFBlocks.ESSENCE_OREBERRY_BUSH.get(), noDrop());
		add(TFBlocks.RASPBERRY_BUSH.get(), noDrop());
		add(TFBlocks.BLUEBERRY_BUSH.get(), noDrop());
		add(TFBlocks.BLACKBERRY_BUSH.get(), noDrop());
		add(TFBlocks.MALOBERRY_BUSH.get(), noDrop());
		add(TFBlocks.BLIGHTBERRY_BUSH.get(), noDrop());
		add(TFBlocks.DUSKBERRY_BUSH.get(), noDrop());
		add(TFBlocks.SKYBERRY_BUSH.get(), noDrop());
		add(TFBlocks.STINGBERRY_BUSH.get(), noDrop());
		add(TFBlocks.TORCHBERRY_PLANT.get(), torchberryPlant(TFBlocks.TORCHBERRY_PLANT.get()));
		add(TFBlocks.ROOT_STRAND.get(), block -> createShearsDispatchTable(block, applyExplosionDecay(block, LootItem.lootTableItem(Items.STICK).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))));
		add(TFBlocks.FALLEN_LEAVES.get(), this.fallenLeaves());
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
		add(TFBlocks.NAGA_TROPHY.get(), createSingleItemTable(TFItems.NAGA_TROPHY.get()));
		add(TFBlocks.NAGA_WALL_TROPHY.get(), createSingleItemTable(TFItems.NAGA_TROPHY.get()));
		add(TFBlocks.LICH_TROPHY.get(), createSingleItemTable(TFItems.LICH_TROPHY.get()));
		add(TFBlocks.LICH_WALL_TROPHY.get(), createSingleItemTable(TFItems.LICH_TROPHY.get()));
		add(TFBlocks.MINOSHROOM_TROPHY.get(), createSingleItemTable(TFItems.MINOSHROOM_TROPHY.get()));
		add(TFBlocks.MINOSHROOM_WALL_TROPHY.get(), createSingleItemTable(TFItems.MINOSHROOM_TROPHY.get()));
		add(TFBlocks.HYDRA_TROPHY.get(), createSingleItemTable(TFItems.HYDRA_TROPHY.get()));
		add(TFBlocks.HYDRA_WALL_TROPHY.get(), createSingleItemTable(TFItems.HYDRA_TROPHY.get()));
		add(TFBlocks.KNIGHT_PHANTOM_TROPHY.get(), createSingleItemTable(TFItems.KNIGHT_PHANTOM_TROPHY.get()));
		add(TFBlocks.KNIGHT_PHANTOM_WALL_TROPHY.get(), createSingleItemTable(TFItems.KNIGHT_PHANTOM_TROPHY.get()));
		add(TFBlocks.UR_GHAST_TROPHY.get(), createSingleItemTable(TFItems.UR_GHAST_TROPHY.get()));
		add(TFBlocks.UR_GHAST_WALL_TROPHY.get(), createSingleItemTable(TFItems.UR_GHAST_TROPHY.get()));
		add(TFBlocks.ALPHA_YETI_TROPHY.get(), createSingleItemTable(TFItems.ALPHA_YETI_TROPHY.get()));
		add(TFBlocks.ALPHA_YETI_WALL_TROPHY.get(), createSingleItemTable(TFItems.ALPHA_YETI_TROPHY.get()));
		add(TFBlocks.SNOW_QUEEN_TROPHY.get(), createSingleItemTable(TFItems.SNOW_QUEEN_TROPHY.get()));
		add(TFBlocks.SNOW_QUEEN_WALL_TROPHY.get(), createSingleItemTable(TFItems.SNOW_QUEEN_TROPHY.get()));
		add(TFBlocks.QUEST_RAM_TROPHY.get(), createSingleItemTable(TFItems.QUEST_RAM_TROPHY.get()));
		add(TFBlocks.QUEST_RAM_WALL_TROPHY.get(), createSingleItemTable(TFItems.QUEST_RAM_TROPHY.get()));

		add(TFBlocks.ZOMBIE_SKULL_CANDLE.get(), createSingleItemTable(Blocks.ZOMBIE_HEAD));
		add(TFBlocks.ZOMBIE_WALL_SKULL_CANDLE.get(), createSingleItemTable(Blocks.ZOMBIE_HEAD));
		add(TFBlocks.SKELETON_SKULL_CANDLE.get(), createSingleItemTable(Blocks.SKELETON_SKULL));
		add(TFBlocks.SKELETON_WALL_SKULL_CANDLE.get(), createSingleItemTable(Blocks.SKELETON_SKULL));
		add(TFBlocks.WITHER_SKELE_SKULL_CANDLE.get(), createSingleItemTable(Blocks.WITHER_SKELETON_SKULL));
		add(TFBlocks.WITHER_SKELE_WALL_SKULL_CANDLE.get(), createSingleItemTable(Blocks.WITHER_SKELETON_SKULL));
		add(TFBlocks.CREEPER_SKULL_CANDLE.get(), createSingleItemTable(Blocks.CREEPER_HEAD));
		add(TFBlocks.CREEPER_WALL_SKULL_CANDLE.get(), createSingleItemTable(Blocks.CREEPER_HEAD));
		add(TFBlocks.PLAYER_SKULL_CANDLE.get(), createSingleItemTable(Blocks.PLAYER_HEAD).apply(
			CopyComponentsFunction.copyComponentsFromBlockEntity(LootContextParams.BLOCK_ENTITY)
				.include(DataComponents.PROFILE)
				.include(DataComponents.NOTE_BLOCK_SOUND)
				.include(DataComponents.CUSTOM_NAME)
		));
		add(TFBlocks.PLAYER_WALL_SKULL_CANDLE.get(), createSingleItemTable(Blocks.PLAYER_HEAD).apply(
			CopyComponentsFunction.copyComponentsFromBlockEntity(LootContextParams.BLOCK_ENTITY)
				.include(DataComponents.PROFILE)
				.include(DataComponents.NOTE_BLOCK_SOUND)
				.include(DataComponents.CUSTOM_NAME)
		));
		add(TFBlocks.PIGLIN_SKULL_CANDLE.get(), createSingleItemTable(Blocks.PIGLIN_HEAD));
		add(TFBlocks.PIGLIN_WALL_SKULL_CANDLE.get(), createSingleItemTable(Blocks.PIGLIN_HEAD));

		dropSelf(TFBlocks.IRON_LADDER.get());
		add(TFBlocks.ROPE.get(), this.rope());
		dropWhenSilkTouch(TFBlocks.CANOPY_WINDOW.value());
		dropWhenSilkTouch(TFBlocks.CANOPY_WINDOW_PANE.value());
		dropSelf(TFBlocks.TWISTED_STONE.get());
		dropSelf(TFBlocks.TWISTED_STONE_PILLAR.get());
		dropSelf(TFBlocks.BOLD_STONE_PILLAR.get());
		add(TFBlocks.SKULL_CHEST.get(), skullChestInfo(TFBlocks.SKULL_CHEST.get()));
		add(TFBlocks.KEEPSAKE_CASKET.get(), casketInfo(TFBlocks.KEEPSAKE_CASKET.get(), TFItems.KEEPSAKE_CASKET.get()));
		dropSelf(TFBlocks.CANDELABRA.get());
		add(TFBlocks.WROUGHT_IRON_FENCE.get(), createSingleItemTable(TFItems.WROUGHT_IRON_FENCE.get()));
		dropSelf(TFBlocks.TERRORCOTTA_ARCS.value());
		dropSelf(TFBlocks.TERRORCOTTA_CURVES.value());
		dropSelf(TFBlocks.TERRORCOTTA_LINES.value());
		dropSelf(TFBlocks.CORONATION_CARPET.value());

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
		add(TFBlocks.POTTED_THORN.get(), createSingleItemTable(Items.FLOWER_POT));
		add(TFBlocks.POTTED_GREEN_THORN.get(), createSingleItemTable(Items.FLOWER_POT));
		add(TFBlocks.POTTED_DEAD_THORN.get(), createSingleItemTable(Items.FLOWER_POT));

		dropSelf(TFBlocks.OAK_BANISTER.get());
		dropSelf(TFBlocks.SPRUCE_BANISTER.get());
		dropSelf(TFBlocks.BIRCH_BANISTER.get());
		dropSelf(TFBlocks.JUNGLE_BANISTER.get());
		dropSelf(TFBlocks.ACACIA_BANISTER.get());
		dropSelf(TFBlocks.DARK_OAK_BANISTER.get());
		dropSelf(TFBlocks.CRIMSON_BANISTER.get());
		dropSelf(TFBlocks.WARPED_BANISTER.get());
		dropSelf(TFBlocks.VANGROVE_BANISTER.get());
		dropSelf(TFBlocks.BAMBOO_BANISTER.get());
		dropSelf(TFBlocks.CHERRY_BANISTER.get());
		dropSelf(TFBlocks.PALE_OAK_BANISTER.get());

		add(TFBlocks.OAK_DRYING_RACK.get(), noDrop());
		add(TFBlocks.SPRUCE_DRYING_RACK.get(), noDrop());
		add(TFBlocks.BIRCH_DRYING_RACK.get(), noDrop());
		add(TFBlocks.JUNGLE_DRYING_RACK.get(), noDrop());
		add(TFBlocks.ACACIA_DRYING_RACK.get(), noDrop());
		add(TFBlocks.DARK_OAK_DRYING_RACK.get(), noDrop());
		add(TFBlocks.CRIMSON_DRYING_RACK.get(), noDrop());
		add(TFBlocks.WARPED_DRYING_RACK.get(), noDrop());
		add(TFBlocks.VANGROVE_DRYING_RACK.get(), noDrop());
		add(TFBlocks.BAMBOO_DRYING_RACK.get(), noDrop());
		add(TFBlocks.CHERRY_DRYING_RACK.get(), noDrop());
		add(TFBlocks.PALE_OAK_DRYING_RACK.get(), noDrop());
		add(TFBlocks.TWILIGHT_OAK_DRYING_RACK.get(), noDrop());
		add(TFBlocks.CANOPY_DRYING_RACK.get(), noDrop());
		add(TFBlocks.MANGROVE_DRYING_RACK.get(), noDrop());
		add(TFBlocks.DARK_DRYING_RACK.get(), noDrop());
		add(TFBlocks.TIME_DRYING_RACK.get(), noDrop());
		add(TFBlocks.TRANSFORMATION_DRYING_RACK.get(), noDrop());
		add(TFBlocks.MINING_DRYING_RACK.get(), noDrop());
		add(TFBlocks.SORTING_DRYING_RACK.get(), noDrop());

		add(TFBlocks.HOLLOW_OAK_LOG_HORIZONTAL.get(), hollowLog(TFBlocks.HOLLOW_OAK_LOG_HORIZONTAL.get()));
		add(TFBlocks.HOLLOW_SPRUCE_LOG_HORIZONTAL.get(), hollowLog(TFBlocks.HOLLOW_SPRUCE_LOG_HORIZONTAL.get()));
		add(TFBlocks.HOLLOW_BIRCH_LOG_HORIZONTAL.get(), hollowLog(TFBlocks.HOLLOW_BIRCH_LOG_HORIZONTAL.get()));
		add(TFBlocks.HOLLOW_JUNGLE_LOG_HORIZONTAL.get(), hollowLog(TFBlocks.HOLLOW_JUNGLE_LOG_HORIZONTAL.get()));
		add(TFBlocks.HOLLOW_ACACIA_LOG_HORIZONTAL.get(), hollowLog(TFBlocks.HOLLOW_ACACIA_LOG_HORIZONTAL.get()));
		add(TFBlocks.HOLLOW_DARK_OAK_LOG_HORIZONTAL.get(), hollowLog(TFBlocks.HOLLOW_DARK_OAK_LOG_HORIZONTAL.get()));
		add(TFBlocks.HOLLOW_CRIMSON_STEM_HORIZONTAL.get(), hollowLog(TFBlocks.HOLLOW_CRIMSON_STEM_HORIZONTAL.get()));
		add(TFBlocks.HOLLOW_WARPED_STEM_HORIZONTAL.get(), hollowLog(TFBlocks.HOLLOW_WARPED_STEM_HORIZONTAL.get()));
		add(TFBlocks.HOLLOW_VANGROVE_LOG_HORIZONTAL.get(), hollowLog(TFBlocks.HOLLOW_VANGROVE_LOG_HORIZONTAL.get()));
		add(TFBlocks.HOLLOW_CHERRY_LOG_HORIZONTAL.get(), hollowLog(TFBlocks.HOLLOW_CHERRY_LOG_HORIZONTAL.get()));
		add(TFBlocks.HOLLOW_PALE_OAK_LOG_HORIZONTAL.get(), hollowLog(TFBlocks.HOLLOW_PALE_OAK_LOG_HORIZONTAL.get()));
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
		add(TFBlocks.HOLLOW_VANGROVE_LOG_VERTICAL.get(), verticalHollowLog(TFBlocks.HOLLOW_VANGROVE_LOG_VERTICAL.get()));
		add(TFBlocks.HOLLOW_CHERRY_LOG_VERTICAL.get(), verticalHollowLog(TFBlocks.HOLLOW_CHERRY_LOG_VERTICAL.get()));
		add(TFBlocks.HOLLOW_PALE_OAK_LOG_VERTICAL.get(), verticalHollowLog(TFBlocks.HOLLOW_PALE_OAK_LOG_VERTICAL.get()));
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
		add(TFBlocks.HOLLOW_VANGROVE_LOG_CLIMBABLE.get(), hollowLog(TFBlocks.HOLLOW_VANGROVE_LOG_CLIMBABLE.get()));
		add(TFBlocks.HOLLOW_CHERRY_LOG_CLIMBABLE.get(), hollowLog(TFBlocks.HOLLOW_CHERRY_LOG_CLIMBABLE.get()));
		add(TFBlocks.HOLLOW_PALE_OAK_LOG_CLIMBABLE.get(), hollowLog(TFBlocks.HOLLOW_PALE_OAK_LOG_CLIMBABLE.get()));
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
		add(TFBlocks.TWILIGHT_OAK_LEAVES.get(), silkAndStick(TFBlocks.TWILIGHT_OAK_LEAVES.get(), TFBlocks.TWILIGHT_OAK_SAPLING.get(), DEFAULT_SAPLING_DROP_RATES));
		dropSelf(TFBlocks.RAINBOW_OAK_SAPLING.get());
		add(TFBlocks.RAINBOW_OAK_LEAVES.get(), silkAndStick(TFBlocks.RAINBOW_OAK_LEAVES.get(), TFBlocks.RAINBOW_OAK_SAPLING.get(), RARE_SAPLING_DROP_RATES));
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
		add(TFBlocks.TWILIGHT_OAK_SIGN.get(), createSingleItemTable(TFItems.TWILIGHT_OAK_SIGN.get()));
		add(TFBlocks.TWILIGHT_WALL_SIGN.get(), createSingleItemTable(TFItems.TWILIGHT_OAK_SIGN.get()));
		add(TFBlocks.TWILIGHT_OAK_HANGING_SIGN.get(), createSingleItemTable(TFItems.TWILIGHT_OAK_HANGING_SIGN.get()));
		add(TFBlocks.TWILIGHT_OAK_WALL_HANGING_SIGN.get(), createSingleItemTable(TFItems.TWILIGHT_OAK_HANGING_SIGN.get()));
		dropSelf(TFBlocks.TWILIGHT_OAK_BANISTER.get());
		dropSelf(TFBlocks.TWILIGHT_OAK_CHEST.get());
		dropSelf(TFBlocks.TWILIGHT_OAK_TRAPPED_CHEST.get());

		dropSelf(TFBlocks.CANOPY_LOG.get());
		dropSelf(TFBlocks.STRIPPED_CANOPY_LOG.get());
		dropSelf(TFBlocks.CANOPY_WOOD.get());
		dropSelf(TFBlocks.STRIPPED_CANOPY_WOOD.get());
		dropSelf(TFBlocks.CANOPY_SAPLING.get());
		add(TFBlocks.CANOPY_LEAVES.get(), silkAndStick(TFBlocks.CANOPY_LEAVES.get(), TFBlocks.CANOPY_SAPLING.get(), DEFAULT_SAPLING_DROP_RATES));
		dropSelf(TFBlocks.CANOPY_PLANKS.get());
		dropSelf(TFBlocks.CANOPY_STAIRS.get());
		add(TFBlocks.CANOPY_SLAB.get(), createSlabItemTable(TFBlocks.CANOPY_SLAB.get()));
		dropSelf(TFBlocks.CANOPY_BUTTON.get());
		dropSelf(TFBlocks.CANOPY_FENCE.get());
		dropSelf(TFBlocks.CANOPY_GATE.get());
		dropSelf(TFBlocks.CANOPY_PLATE.get());
		add(TFBlocks.CANOPY_DOOR.get(), createSinglePropConditionTable(TFBlocks.CANOPY_DOOR.get(), DoorBlock.HALF, DoubleBlockHalf.LOWER));
		dropSelf(TFBlocks.CANOPY_TRAPDOOR.get());
		add(TFBlocks.CANOPY_SIGN.get(), createSingleItemTable(TFItems.CANOPY_SIGN.get()));
		add(TFBlocks.CANOPY_WALL_SIGN.get(), createSingleItemTable(TFItems.CANOPY_SIGN.get()));
		add(TFBlocks.CANOPY_HANGING_SIGN.get(), createSingleItemTable(TFItems.CANOPY_HANGING_SIGN.get()));
		add(TFBlocks.CANOPY_WALL_HANGING_SIGN.get(), createSingleItemTable(TFItems.CANOPY_HANGING_SIGN.get()));
		add(TFBlocks.CANOPY_BOOKSHELF.get(), createSingleItemTableWithSilkTouch(TFBlocks.CANOPY_BOOKSHELF.get(), Items.BOOK, ConstantValue.exactly(2.0F)));
		dropSelf(TFBlocks.CHISELED_CANOPY_BOOKSHELF.get());
		dropSelf(TFBlocks.CANOPY_BANISTER.get());
		dropSelf(TFBlocks.CANOPY_CHEST.get());
		dropSelf(TFBlocks.CANOPY_TRAPPED_CHEST.get());

		dropSelf(TFBlocks.MANGROVE_LOG.get());
		dropSelf(TFBlocks.STRIPPED_MANGROVE_LOG.get());
		dropSelf(TFBlocks.MANGROVE_WOOD.get());
		dropSelf(TFBlocks.STRIPPED_MANGROVE_WOOD.get());
		dropSelf(TFBlocks.MANGROVE_SAPLING.get());
		add(TFBlocks.MANGROVE_LEAVES.get(), silkAndStick(TFBlocks.MANGROVE_LEAVES.get(), TFBlocks.MANGROVE_SAPLING.get(), DEFAULT_SAPLING_DROP_RATES));
		dropSelf(TFBlocks.MANGROVE_PLANKS.get());
		dropSelf(TFBlocks.MANGROVE_STAIRS.get());
		add(TFBlocks.MANGROVE_SLAB.get(), createSlabItemTable(TFBlocks.MANGROVE_SLAB.get()));
		dropSelf(TFBlocks.MANGROVE_BUTTON.get());
		dropSelf(TFBlocks.MANGROVE_FENCE.get());
		dropSelf(TFBlocks.MANGROVE_GATE.get());
		dropSelf(TFBlocks.MANGROVE_PLATE.get());
		add(TFBlocks.MANGROVE_DOOR.get(), createSinglePropConditionTable(TFBlocks.MANGROVE_DOOR.get(), DoorBlock.HALF, DoubleBlockHalf.LOWER));
		dropSelf(TFBlocks.MANGROVE_TRAPDOOR.get());
		add(TFBlocks.MANGROVE_SIGN.get(), createSingleItemTable(TFItems.MANGROVE_SIGN.get()));
		add(TFBlocks.MANGROVE_WALL_SIGN.get(), createSingleItemTable(TFItems.MANGROVE_SIGN.get()));
		add(TFBlocks.MANGROVE_HANGING_SIGN.get(), createSingleItemTable(TFItems.MANGROVE_HANGING_SIGN.get()));
		add(TFBlocks.MANGROVE_WALL_HANGING_SIGN.get(), createSingleItemTable(TFItems.MANGROVE_HANGING_SIGN.get()));
		dropSelf(TFBlocks.MANGROVE_BANISTER.get());
		dropSelf(TFBlocks.MANGROVE_CHEST.get());
		dropSelf(TFBlocks.MANGROVE_TRAPPED_CHEST.get());

		dropSelf(TFBlocks.DARK_LOG.get());
		dropSelf(TFBlocks.STRIPPED_DARK_LOG.get());
		dropSelf(TFBlocks.DARK_WOOD.get());
		dropSelf(TFBlocks.STRIPPED_DARK_WOOD.get());
		dropSelf(TFBlocks.DARKWOOD_SAPLING.get());
		add(TFBlocks.DARK_LEAVES.get(), silkAndStick(TFBlocks.DARK_LEAVES.get(), TFBlocks.DARKWOOD_SAPLING.get(), RARE_SAPLING_DROP_RATES));
		add(TFBlocks.HARDENED_DARK_LEAVES.get(), silkAndStick(TFBlocks.DARK_LEAVES.get(), TFBlocks.DARKWOOD_SAPLING.get(), RARE_SAPLING_DROP_RATES));
		dropSelf(TFBlocks.DARK_PLANKS.get());
		dropSelf(TFBlocks.DARK_STAIRS.get());
		add(TFBlocks.DARK_SLAB.get(), createSlabItemTable(TFBlocks.DARK_SLAB.get()));
		dropSelf(TFBlocks.DARK_BUTTON.get());
		dropSelf(TFBlocks.DARK_FENCE.get());
		dropSelf(TFBlocks.DARK_GATE.get());
		dropSelf(TFBlocks.DARK_PLATE.get());
		add(TFBlocks.DARK_DOOR.get(), createSinglePropConditionTable(TFBlocks.DARK_DOOR.get(), DoorBlock.HALF, DoubleBlockHalf.LOWER));
		dropSelf(TFBlocks.DARK_TRAPDOOR.get());
		add(TFBlocks.DARK_SIGN.get(), createSingleItemTable(TFItems.DARK_SIGN.get()));
		add(TFBlocks.DARK_WALL_SIGN.get(), createSingleItemTable(TFItems.DARK_SIGN.get()));
		add(TFBlocks.DARK_HANGING_SIGN.get(), createSingleItemTable(TFItems.DARK_HANGING_SIGN.get()));
		add(TFBlocks.DARK_WALL_HANGING_SIGN.get(), createSingleItemTable(TFItems.DARK_HANGING_SIGN.get()));
		dropSelf(TFBlocks.DARK_BANISTER.get());
		dropSelf(TFBlocks.DARK_CHEST.get());
		dropSelf(TFBlocks.DARK_TRAPPED_CHEST.get());

		dropSelf(TFBlocks.TIME_LOG.get());
		dropSelf(TFBlocks.STRIPPED_TIME_LOG.get());
		dropSelf(TFBlocks.TIME_WOOD.get());
		dropSelf(TFBlocks.STRIPPED_TIME_WOOD.get());
		dropOther(TFBlocks.TIME_LOG_CORE.get(), TFBlocks.TIME_LOG.get());
		dropSelf(TFBlocks.TIME_SAPLING.get());
		registerLeavesNoSapling(TFBlocks.TIME_LEAVES.get(), registrylookup);
		dropSelf(TFBlocks.TIME_PLANKS.get());
		dropSelf(TFBlocks.TIME_STAIRS.get());
		add(TFBlocks.TIME_SLAB.get(), createSlabItemTable(TFBlocks.TIME_SLAB.get()));
		dropSelf(TFBlocks.TIME_BUTTON.get());
		dropSelf(TFBlocks.TIME_FENCE.get());
		dropSelf(TFBlocks.TIME_GATE.get());
		dropSelf(TFBlocks.TIME_PLATE.get());
		add(TFBlocks.TIME_DOOR.get(), createSinglePropConditionTable(TFBlocks.TIME_DOOR.get(), DoorBlock.HALF, DoubleBlockHalf.LOWER));
		dropSelf(TFBlocks.TIME_TRAPDOOR.get());
		add(TFBlocks.TIME_SIGN.get(), createSingleItemTable(TFItems.TIME_SIGN.get()));
		add(TFBlocks.TIME_WALL_SIGN.get(), createSingleItemTable(TFItems.TIME_SIGN.get()));
		add(TFBlocks.TIME_HANGING_SIGN.get(), createSingleItemTable(TFItems.TIME_HANGING_SIGN.get()));
		add(TFBlocks.TIME_WALL_HANGING_SIGN.get(), createSingleItemTable(TFItems.TIME_HANGING_SIGN.get()));
		dropSelf(TFBlocks.TIME_BANISTER.get());
		dropSelf(TFBlocks.TIME_CHEST.get());
		dropSelf(TFBlocks.TIME_TRAPPED_CHEST.get());

		dropSelf(TFBlocks.TRANSFORMATION_LOG.get());
		dropSelf(TFBlocks.STRIPPED_TRANSFORMATION_LOG.get());
		dropSelf(TFBlocks.TRANSFORMATION_WOOD.get());
		dropSelf(TFBlocks.STRIPPED_TRANSFORMATION_WOOD.get());
		dropOther(TFBlocks.TRANSFORMATION_LOG_CORE.get(), TFBlocks.TRANSFORMATION_LOG.get());
		dropSelf(TFBlocks.TRANSFORMATION_SAPLING.get());
		registerLeavesNoSapling(TFBlocks.TRANSFORMATION_LEAVES.get(), registrylookup);
		dropSelf(TFBlocks.TRANSFORMATION_PLANKS.get());
		dropSelf(TFBlocks.TRANSFORMATION_STAIRS.get());
		add(TFBlocks.TRANSFORMATION_SLAB.get(), createSlabItemTable(TFBlocks.TRANSFORMATION_SLAB.get()));
		dropSelf(TFBlocks.TRANSFORMATION_BUTTON.get());
		dropSelf(TFBlocks.TRANSFORMATION_FENCE.get());
		dropSelf(TFBlocks.TRANSFORMATION_GATE.get());
		dropSelf(TFBlocks.TRANSFORMATION_PLATE.get());
		add(TFBlocks.TRANSFORMATION_DOOR.get(), createSinglePropConditionTable(TFBlocks.TRANSFORMATION_DOOR.get(), DoorBlock.HALF, DoubleBlockHalf.LOWER));
		dropSelf(TFBlocks.TRANSFORMATION_TRAPDOOR.get());
		add(TFBlocks.TRANSFORMATION_SIGN.get(), createSingleItemTable(TFItems.TRANSFORMATION_SIGN.get()));
		add(TFBlocks.TRANSFORMATION_WALL_SIGN.get(), createSingleItemTable(TFItems.TRANSFORMATION_SIGN.get()));
		add(TFBlocks.TRANSFORMATION_HANGING_SIGN.get(), createSingleItemTable(TFItems.TRANSFORMATION_HANGING_SIGN.get()));
		add(TFBlocks.TRANSFORMATION_WALL_HANGING_SIGN.get(), createSingleItemTable(TFItems.TRANSFORMATION_HANGING_SIGN.get()));
		dropSelf(TFBlocks.TRANSFORMATION_BANISTER.get());
		dropSelf(TFBlocks.TRANSFORMATION_CHEST.get());
		dropSelf(TFBlocks.TRANSFORMATION_TRAPPED_CHEST.get());

		dropSelf(TFBlocks.MINING_LOG.get());
		dropSelf(TFBlocks.STRIPPED_MINING_LOG.get());
		dropSelf(TFBlocks.MINING_WOOD.get());
		dropSelf(TFBlocks.STRIPPED_MINING_WOOD.get());
		dropOther(TFBlocks.MINING_LOG_CORE.get(), TFBlocks.MINING_LOG.get());
		dropSelf(TFBlocks.MINING_SAPLING.get());
		registerLeavesNoSapling(TFBlocks.MINING_LEAVES.get(), registrylookup);
		dropSelf(TFBlocks.MINING_PLANKS.get());
		dropSelf(TFBlocks.MINING_STAIRS.get());
		add(TFBlocks.MINING_SLAB.get(), createSlabItemTable(TFBlocks.MINING_SLAB.get()));
		dropSelf(TFBlocks.MINING_BUTTON.get());
		dropSelf(TFBlocks.MINING_FENCE.get());
		dropSelf(TFBlocks.MINING_GATE.get());
		dropSelf(TFBlocks.MINING_PLATE.get());
		add(TFBlocks.MINING_DOOR.get(), createSinglePropConditionTable(TFBlocks.MINING_DOOR.get(), DoorBlock.HALF, DoubleBlockHalf.LOWER));
		dropSelf(TFBlocks.MINING_TRAPDOOR.get());
		add(TFBlocks.MINING_SIGN.get(), createSingleItemTable(TFItems.MINING_SIGN.get()));
		add(TFBlocks.MINING_WALL_SIGN.get(), createSingleItemTable(TFItems.MINING_SIGN.get()));
		add(TFBlocks.MINING_HANGING_SIGN.get(), createSingleItemTable(TFItems.MINING_HANGING_SIGN.get()));
		add(TFBlocks.MINING_WALL_HANGING_SIGN.get(), createSingleItemTable(TFItems.MINING_HANGING_SIGN.get()));
		dropSelf(TFBlocks.MINING_BANISTER.get());
		dropSelf(TFBlocks.MINING_CHEST.get());
		dropSelf(TFBlocks.MINING_TRAPPED_CHEST.get());

		dropSelf(TFBlocks.SORTING_LOG.get());
		dropSelf(TFBlocks.STRIPPED_SORTING_LOG.get());
		dropSelf(TFBlocks.SORTING_WOOD.get());
		dropSelf(TFBlocks.STRIPPED_SORTING_WOOD.get());
		dropOther(TFBlocks.SORTING_LOG_CORE.get(), TFBlocks.SORTING_LOG.get());
		dropSelf(TFBlocks.SORTING_SAPLING.get());
		registerLeavesNoSapling(TFBlocks.SORTING_LEAVES.get(), registrylookup);
		dropSelf(TFBlocks.SORTING_PLANKS.get());
		dropSelf(TFBlocks.SORTING_STAIRS.get());
		add(TFBlocks.SORTING_SLAB.get(), createSlabItemTable(TFBlocks.SORTING_SLAB.get()));
		dropSelf(TFBlocks.SORTING_BUTTON.get());
		dropSelf(TFBlocks.SORTING_FENCE.get());
		dropSelf(TFBlocks.SORTING_GATE.get());
		dropSelf(TFBlocks.SORTING_PLATE.get());
		add(TFBlocks.SORTING_DOOR.get(), createSinglePropConditionTable(TFBlocks.SORTING_DOOR.get(), DoorBlock.HALF, DoubleBlockHalf.LOWER));
		dropSelf(TFBlocks.SORTING_TRAPDOOR.get());
		add(TFBlocks.SORTING_SIGN.get(), createSingleItemTable(TFItems.SORTING_SIGN.get()));
		add(TFBlocks.SORTING_WALL_SIGN.get(), createSingleItemTable(TFItems.SORTING_SIGN.get()));
		add(TFBlocks.SORTING_HANGING_SIGN.get(), createSingleItemTable(TFItems.SORTING_HANGING_SIGN.get()));
		add(TFBlocks.SORTING_WALL_HANGING_SIGN.get(), createSingleItemTable(TFItems.SORTING_HANGING_SIGN.get()));
		dropSelf(TFBlocks.SORTING_BANISTER.get());
		dropSelf(TFBlocks.SORTING_CHEST.get());
		dropSelf(TFBlocks.SORTING_TRAPPED_CHEST.get());
		add(TFBlocks.OMINOUS_FIRE.get(), noDrop());
		ominousCandle(TFBlocks.OMINOUS_CANDLE);
		ominousCandle(TFBlocks.OMINOUS_WHITE_CANDLE);
		ominousCandle(TFBlocks.OMINOUS_ORANGE_CANDLE);
		ominousCandle(TFBlocks.OMINOUS_MAGENTA_CANDLE);
		ominousCandle(TFBlocks.OMINOUS_LIGHT_BLUE_CANDLE);
		ominousCandle(TFBlocks.OMINOUS_YELLOW_CANDLE);
		ominousCandle(TFBlocks.OMINOUS_LIME_CANDLE);
		ominousCandle(TFBlocks.OMINOUS_PINK_CANDLE);
		ominousCandle(TFBlocks.OMINOUS_GRAY_CANDLE);
		ominousCandle(TFBlocks.OMINOUS_LIGHT_GRAY_CANDLE);
		ominousCandle(TFBlocks.OMINOUS_CYAN_CANDLE);
		ominousCandle(TFBlocks.OMINOUS_PURPLE_CANDLE);
		ominousCandle(TFBlocks.OMINOUS_BLUE_CANDLE);
		ominousCandle(TFBlocks.OMINOUS_BROWN_CANDLE);
		ominousCandle(TFBlocks.OMINOUS_GREEN_CANDLE);
		ominousCandle(TFBlocks.OMINOUS_RED_CANDLE);
		ominousCandle(TFBlocks.OMINOUS_BLACK_CANDLE);
	}

	private void registerLeavesNoSapling(Block leaves, HolderLookup.RegistryLookup<Enchantment> registrylookup) {
		LootPoolEntryContainer.Builder<?> sticks = applyExplosionDecay(leaves, LootItem.lootTableItem(Items.STICK)
			.apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F)))
			.when(BonusLevelTableCondition.bonusLevelFlatChance(registrylookup.getOrThrow(Enchantments.FORTUNE), 0.02F, 0.022222223F, 0.025F, 0.033333335F, 0.1F)));
		add(leaves, createSilkTouchOrShearsDispatchTable(leaves, sticks));
	}

	private LootTable.Builder hollowLog(Block log) {
		HolderLookup.RegistryLookup<Enchantment> registrylookup = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
		return LootTable.lootTable()
			.withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
				.add(LootItem.lootTableItem(log.asItem()).when(this.hasSilkTouch()).otherwise(LootItem.lootTableItem(Items.STICK).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 4.0F))).apply(ApplyBonusCount.addUniformBonusCount(registrylookup.getOrThrow(Enchantments.FORTUNE))))))
			.withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
				.add(LootItem.lootTableItem(Blocks.SHORT_GRASS).when(this.hasSilkTouch()).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(log).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(HorizontalHollowLogBlock.VARIANT, HollowLogVariants.Horizontal.MOSS_AND_GRASS)))))
			.withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
				.add(LootItem.lootTableItem(TFBlocks.MOSS_PATCH.get()).when(this.hasSilkTouch()).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(log).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(HorizontalHollowLogBlock.VARIANT, HollowLogVariants.Horizontal.MOSS_AND_GRASS)))))
			.withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
				.add(LootItem.lootTableItem(TFBlocks.MOSS_PATCH.get()).when(this.hasSilkTouch()).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(log).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(HorizontalHollowLogBlock.VARIANT, HollowLogVariants.Horizontal.MOSS)))))
			.withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
				.add(LootItem.lootTableItem(Items.SNOWBALL).when(this.hasSilkTouch()).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(log).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(HorizontalHollowLogBlock.VARIANT, HollowLogVariants.Horizontal.SNOW)))))
			.withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
				.add(LootItem.lootTableItem(Blocks.VINE).when(this.hasSilkTouch()).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(log).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(ClimbableHollowLogBlock.VARIANT, HollowLogVariants.Climbable.VINE)))))
			.withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
				.add(LootItem.lootTableItem(Blocks.LADDER).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(log).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(ClimbableHollowLogBlock.VARIANT, HollowLogVariants.Climbable.LADDER)))))
			.withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
				.add(LootItem.lootTableItem(Blocks.LADDER).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(log).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(ClimbableHollowLogBlock.VARIANT, HollowLogVariants.Climbable.LADDER_WATERLOGGED)))));
	}

	private LootTable.Builder verticalHollowLog(Block log) {
		HolderLookup.RegistryLookup<Enchantment> registrylookup = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
		return LootTable.lootTable()
			.withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
				.add(LootItem.lootTableItem(log.asItem()).when(this.hasSilkTouch()).otherwise(LootItem.lootTableItem(Items.STICK).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 4.0F))).apply(ApplyBonusCount.addUniformBonusCount(registrylookup.getOrThrow(Enchantments.FORTUNE))))));
	}

	// [VanillaCopy] super.droppingWithChancesAndSticks, but non-silk touch parameter can be an item instead of a block
	private LootTable.Builder silkAndStick(Block block, ItemLike nonSilk, float... nonSilkFortune) {
		HolderLookup.RegistryLookup<Enchantment> registrylookup = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
		return createSilkTouchOrShearsDispatchTable(block, this.applyExplosionCondition(block, LootItem.lootTableItem(nonSilk.asItem())).when(BonusLevelTableCondition.bonusLevelFlatChance(registrylookup.getOrThrow(Enchantments.FORTUNE), nonSilkFortune))).withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).when((HAS_SHEARS.or(this.hasSilkTouch())).invert()).add(applyExplosionDecay(block, LootItem.lootTableItem(Items.STICK).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F)))).when(BonusLevelTableCondition.bonusLevelFlatChance(registrylookup.getOrThrow(Enchantments.FORTUNE), 0.02F, 0.022222223F, 0.025F, 0.033333335F, 0.1F))));
	}

	private static LootTable.Builder skullChestInfo(Block block) {
		return LootTable.lootTable()
			.withPool(LootPool.lootPool()
				.setRolls(ConstantValue.exactly(1))
				.add(LootItem.lootTableItem(block)
					.apply(CopyComponentsFunction.copyComponentsFromBlockEntity(LootContextParams.BLOCK_ENTITY).include(DataComponents.CUSTOM_NAME))));
	}

	private static LootTable.Builder casketInfo(Block block, ItemLike item) {
		return LootTable.lootTable()
			.withPool(LootPool.lootPool()
				.setRolls(ConstantValue.exactly(1))
				.add(LootItem.lootTableItem(item)
					.apply(CopyComponentsFunction.copyComponentsFromBlockEntity(LootContextParams.BLOCK_ENTITY).include(DataComponents.CUSTOM_NAME))
					.apply(CopyBlockState.copyState(block).copy(KeepsakeCasketBlock.BREAKAGE))));
	}

	private LootTable.Builder particleSpawner() {
		return LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
				.add(this.applyExplosionDecay(TFBlocks.FIREFLY_SPAWNER.get(), LootItem.lootTableItem(TFBlocks.FIREFLY_SPAWNER.get()))))
			.withPool(LootPool.lootPool()
				.add(AlternativesEntry.alternatives(AlternativesEntry.alternatives(FireflySpawnerBlock.RADIUS.getPossibleValues(), layer ->
					LootItem.lootTableItem(TFBlocks.FIREFLY.get())
						.when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(TFBlocks.FIREFLY_SPAWNER.get())
							.setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(FireflySpawnerBlock.RADIUS, layer)))
						.apply(SetItemCountFunction.setCount(ConstantValue.exactly(layer - 1)))))));
	}

	protected LootTable.Builder torchberryPlant(Block pBlock) {
		return LootTable.lootTable()
			.withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
				.add(LootItem.lootTableItem(pBlock).when(HAS_SHEARS)))
			.withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
				.add(LootItem.lootTableItem(TFItems.TORCHBERRIES.get())
					.when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(pBlock).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(TorchberryPlantBlock.HAS_BERRIES, true)))));
	}

	protected LootTable.Builder redThread() {
		return LootTable.lootTable()
			.withPool(LootPool.lootPool()
				.add(this.applyExplosionDecay(TFBlocks.RED_THREAD.get(), LootItem.lootTableItem(TFBlocks.RED_THREAD.get())
					.apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F), true)
						.when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(TFBlocks.RED_THREAD.get())
							.setProperties(StatePropertiesPredicate.Builder.properties()
								.hasProperty(PipeBlock.EAST, true))))
					.apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F), true)
						.when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(TFBlocks.RED_THREAD.get())
							.setProperties(StatePropertiesPredicate.Builder.properties()
								.hasProperty(PipeBlock.WEST, true))))
					.apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F), true)
						.when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(TFBlocks.RED_THREAD.get())
							.setProperties(StatePropertiesPredicate.Builder.properties()
								.hasProperty(PipeBlock.NORTH, true))))
					.apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F), true)
						.when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(TFBlocks.RED_THREAD.get())
							.setProperties(StatePropertiesPredicate.Builder.properties()
								.hasProperty(PipeBlock.SOUTH, true))))
					.apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F), true)
						.when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(TFBlocks.RED_THREAD.get())
							.setProperties(StatePropertiesPredicate.Builder.properties()
								.hasProperty(PipeBlock.UP, true))))
					.apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F), true)
						.when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(TFBlocks.RED_THREAD.get())
							.setProperties(StatePropertiesPredicate.Builder.properties()
								.hasProperty(PipeBlock.DOWN, true))))
					.apply(SetItemCountFunction.setCount(ConstantValue.exactly(-1.0F), true)))));
	}

	protected LootTable.Builder rope() {
		return LootTable.lootTable()
			.withPool(LootPool.lootPool()
				.add(this.applyExplosionDecay(TFBlocks.ROPE.get(), LootItem.lootTableItem(TFItems.ROPE.get())
					.apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F), true)
						.when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(TFBlocks.ROPE.get())
							.setProperties(StatePropertiesPredicate.Builder.properties()
								.hasProperty(RopeBlock.X, true))))
					.apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F), true)
						.when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(TFBlocks.ROPE.get())
							.setProperties(StatePropertiesPredicate.Builder.properties()
								.hasProperty(RopeBlock.Y, true))))
					.apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F), true)
						.when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(TFBlocks.ROPE.get())
							.setProperties(StatePropertiesPredicate.Builder.properties()
								.hasProperty(RopeBlock.Z, true))))
					.apply(SetItemCountFunction.setCount(ConstantValue.exactly(-1.0F), true)))));
	}

	protected LootTable.Builder fallenLeaves() {
		return LootTable.lootTable().withPool(LootPool.lootPool()
			.add(AlternativesEntry.alternatives(AlternativesEntry.alternatives(FallenLeavesBlock.LAYERS.getPossibleValues(), layer ->
				LootItem.lootTableItem(TFItems.FALLEN_LEAVES.get())
					.when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(TFBlocks.FALLEN_LEAVES.get())
						.setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(FallenLeavesBlock.LAYERS, layer)))
					.apply(SetItemCountFunction.setCount(ConstantValue.exactly(layer)))).when(HAS_SHEARS))));
	}

	protected void ominousCandle(DeferredBlock<OminousCandleBlock> block) {
		this.add(block.get(), LootTable.lootTable()
			.withPool(
				LootPool.lootPool()
					.setRolls(ConstantValue.exactly(1.0F))
					.add(
						this.applyExplosionDecay(
							block.get(),
							LootItem.lootTableItem(block.get().candle)
								.apply(
									List.of(2, 3, 4),
									value -> SetItemCountFunction.setCount(ConstantValue.exactly((float) value))
										.when(
											LootItemBlockStatePropertyCondition.hasBlockStateProperties(block.get())
												.setProperties(
													StatePropertiesPredicate.Builder.properties().hasProperty(OminousCandleBlock.CANDLES, value)
												)
										)
								)
						)
					)
			));
	}

	//[VanillaCopy] of a few different methods from BlockLoot. These are here just so we can use the modded shears thing
	@Override
	protected LootTable.Builder createShearsDispatchTable(Block block, LootPoolEntryContainer.Builder<?> builder) {
		return createSelfDropDispatchTable(block, HAS_SHEARS, builder);
	}

	@Override
	protected LootTable.Builder createSilkTouchOrShearsDispatchTable(Block block, LootPoolEntryContainer.Builder<?> builder) {
		return createSelfDropDispatchTable(block, HAS_SHEARS.or(this.hasSilkTouch()), builder);
	}

	protected LootTable.Builder createShearsOnlyDrop(ItemLike p_124287_) {
		return LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).when(HAS_SHEARS).add(LootItem.lootTableItem(p_124287_)));
	}

	@Override
	protected Iterable<Block> getKnownBlocks() {
		return TFBlocks.BLOCKS.getEntries().stream().map(DeferredHolder::value).collect(Collectors.toList());
	}
}
