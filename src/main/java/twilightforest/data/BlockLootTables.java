package twilightforest.data;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.DoorBlock;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Items;
import net.minecraft.loot.*;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.loot.conditions.TableBonus;
import net.minecraft.loot.functions.ApplyBonus;
import net.minecraft.loot.functions.SetCount;
import net.minecraft.state.properties.DoubleBlockHalf;
import net.minecraft.util.IItemProvider;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import twilightforest.block.TFBlocks;
import twilightforest.item.TFItems;

import java.util.HashSet;
import java.util.Set;

public class BlockLootTables extends net.minecraft.data.loot.BlockLootTables {
	private final Set<Block> knownBlocks = new HashSet<>();
	// [VanillaCopy] super
	private static final float[] DEFAULT_SAPLING_DROP_RATES = new float[]{0.05F, 0.0625F, 0.083333336F, 0.1F};
	private static final float[] RARE_SAPLING_DROP_RATES = new float[]{0.025F, 0.027777778F, 0.03125F, 0.041666668F, 0.1F};

	@Override
	protected void registerLootTable(Block block, LootTable.Builder builder) {
		super.registerLootTable(block, builder);
		knownBlocks.add(block);
	}

	@Override
	protected void addTables() {
		registerEmpty(TFBlocks.experiment_115.get());
		registerDropSelfLootTable(TFBlocks.tower_wood.get());
		registerDropSelfLootTable(TFBlocks.tower_wood_encased.get());
		registerDropSelfLootTable(TFBlocks.tower_wood_cracked.get());
		registerDropSelfLootTable(TFBlocks.tower_wood_mossy.get());
		registerSilkTouch(TFBlocks.tower_wood_infested.get());
		registerEmpty(TFBlocks.antibuilder.get());
		registerDropSelfLootTable(TFBlocks.carminite_builder.get());
		registerDropSelfLootTable(TFBlocks.ghast_trap.get());
		registerDropSelfLootTable(TFBlocks.carminite_reactor.get());
		registerDropSelfLootTable(TFBlocks.reappearing_block.get());
		registerDropSelfLootTable(TFBlocks.vanishing_block.get());
		registerDropSelfLootTable(TFBlocks.locked_vanishing_block.get());
		registerDropSelfLootTable(TFBlocks.firefly.get());
		registerDropSelfLootTable(TFBlocks.cicada.get());
		registerDropSelfLootTable(TFBlocks.moonworm.get());
		registerDropSelfLootTable(TFBlocks.trophy_pedestal.get());
		//registerDropSelfLootTable(TFBlocks.terrorcotta_circle.get());
		//registerDropSelfLootTable(TFBlocks.terrorcotta_diagonal.get());
		registerDropSelfLootTable(TFBlocks.aurora_block.get());
		registerDropSelfLootTable(TFBlocks.aurora_pillar.get());
		registerLootTable(TFBlocks.aurora_slab.get(), droppingSlab(TFBlocks.aurora_slab.get()));
		registerSilkTouch(TFBlocks.auroralized_glass.get());
		registerDropSelfLootTable(TFBlocks.underbrick.get());
		registerDropSelfLootTable(TFBlocks.underbrick_cracked.get());
		registerDropSelfLootTable(TFBlocks.underbrick_mossy.get());
		registerDropSelfLootTable(TFBlocks.underbrick_floor.get());
		registerDropSelfLootTable(TFBlocks.thorn_rose.get());
		registerLootTable(TFBlocks.thorn_leaves.get(), silkAndStick(TFBlocks.thorn_leaves.get(), TFItems.magic_beans.get(), RARE_SAPLING_DROP_RATES));
		registerLootTable(TFBlocks.beanstalk_leaves.get(), silkAndStick(TFBlocks.beanstalk_leaves.get(), TFItems.magic_beans.get(), RARE_SAPLING_DROP_RATES));
		registerDropSelfLootTable(TFBlocks.deadrock.get());
		registerDropSelfLootTable(TFBlocks.deadrock_cracked.get());
		registerDropSelfLootTable(TFBlocks.deadrock_weathered.get());
		registerDropSelfLootTable(TFBlocks.trollsteinn.get());
		registerSilkTouch(TFBlocks.wispy_cloud.get());
		registerDropSelfLootTable(TFBlocks.fluffy_cloud.get());
		registerDropSelfLootTable(TFBlocks.giant_cobblestone.get());
		registerDropSelfLootTable(TFBlocks.giant_log.get());
		registerDropSelfLootTable(TFBlocks.giant_leaves.get());
		registerDropSelfLootTable(TFBlocks.giant_obsidian.get());
		registerLootTable(TFBlocks.uberous_soil.get(), dropping(Blocks.DIRT));
		registerDropSelfLootTable(TFBlocks.huge_stalk.get());
		registerLootTable(TFBlocks.huge_mushgloom.get(), b -> droppingItemRarely(b, TFBlocks.mushgloom.get()));
		registerLootTable(TFBlocks.huge_mushgloom_stem.get(), b -> droppingItemRarely(b, TFBlocks.mushgloom.get()));
		registerLootTable(TFBlocks.trollvidr.get(), onlyWithShears(TFBlocks.trollvidr.get()));
		registerLootTable(TFBlocks.unripe_trollber.get(), onlyWithShears(TFBlocks.unripe_trollber.get()));
		LootTable.Builder trollber = LootTable.builder().addLootPool(LootPool.builder().rolls(ConstantRange.of(1)).addEntry(
						withExplosionDecay(TFItems.torchberries.get(), ItemLootEntry.builder(TFItems.torchberries.get())
										.acceptFunction(SetCount.builder(new RandomValueRange(4, 8)))
										.acceptFunction(ApplyBonus.oreDrops(Enchantments.FORTUNE)))));
		registerLootTable(TFBlocks.trollber.get(), trollber);
		registerDropSelfLootTable(TFBlocks.huge_lilypad.get());
		registerDropSelfLootTable(TFBlocks.huge_waterlily.get());
		registerDropSelfLootTable(TFBlocks.castle_brick.get());
		registerDropSelfLootTable(TFBlocks.castle_brick_worn.get());
		registerDropSelfLootTable(TFBlocks.castle_brick_cracked.get());
		registerDropSelfLootTable(TFBlocks.castle_brick_mossy.get());
		registerDropSelfLootTable(TFBlocks.castle_brick_frame.get());
		registerDropSelfLootTable(TFBlocks.castle_pillar_encased.get());
		registerDropSelfLootTable(TFBlocks.castle_pillar_encased_tile.get());
		registerDropSelfLootTable(TFBlocks.castle_pillar_bold.get());
		registerDropSelfLootTable(TFBlocks.castle_pillar_bold_tile.get());
		registerDropSelfLootTable(TFBlocks.castle_stairs_brick.get());
		registerDropSelfLootTable(TFBlocks.castle_stairs_worn.get());
		registerDropSelfLootTable(TFBlocks.castle_stairs_cracked.get());
		registerDropSelfLootTable(TFBlocks.castle_stairs_mossy.get());
		registerDropSelfLootTable(TFBlocks.castle_stairs_encased.get());
		registerDropSelfLootTable(TFBlocks.castle_stairs_bold.get());
		registerDropSelfLootTable(TFBlocks.castle_rune_brick_purple.get());
		registerDropSelfLootTable(TFBlocks.castle_rune_brick_yellow.get());
		registerDropSelfLootTable(TFBlocks.castle_rune_brick_pink.get());
		registerDropSelfLootTable(TFBlocks.castle_rune_brick_blue.get());
		registerDropSelfLootTable(TFBlocks.cinder_furnace.get());
		registerDropSelfLootTable(TFBlocks.cinder_log.get());
		registerDropSelfLootTable(TFBlocks.cinder_wood.get());
		registerDropSelfLootTable(TFBlocks.castle_door_purple.get());
		registerDropSelfLootTable(TFBlocks.castle_door_yellow.get());
		registerDropSelfLootTable(TFBlocks.castle_door_pink.get());
		registerDropSelfLootTable(TFBlocks.castle_door_blue.get());
		registerDropSelfLootTable(TFBlocks.twilight_portal_miniature_structure.get());
		registerDropSelfLootTable(TFBlocks.naga_courtyard_miniature_structure.get());
		registerDropSelfLootTable(TFBlocks.lich_tower_miniature_structure.get());
		registerDropSelfLootTable(TFBlocks.knightmetal_block.get());
		registerDropSelfLootTable(TFBlocks.ironwood_block.get());
		registerDropSelfLootTable(TFBlocks.fiery_block.get());
		registerDropSelfLootTable(TFBlocks.steeleaf_block.get());
		registerDropSelfLootTable(TFBlocks.arctic_fur_block.get());
		registerDropSelfLootTable(TFBlocks.carminite_block.get());
		registerDropSelfLootTable(TFBlocks.maze_stone.get());
		registerDropSelfLootTable(TFBlocks.maze_stone_brick.get());
		registerDropSelfLootTable(TFBlocks.maze_stone_chiseled.get());
		registerDropSelfLootTable(TFBlocks.maze_stone_decorative.get());
		registerDropSelfLootTable(TFBlocks.maze_stone_cracked.get());
		registerDropSelfLootTable(TFBlocks.maze_stone_mossy.get());
		registerDropSelfLootTable(TFBlocks.maze_stone_mosaic.get());
		registerDropSelfLootTable(TFBlocks.maze_stone_border.get());
		registerEmpty(TFBlocks.hedge.get());
		registerLootTable(TFBlocks.root.get(), droppingRandomly(Items.STICK, new RandomValueRange(3, 5)));
		registerLootTable(TFBlocks.liveroot_block.get(), dropping(TFItems.liveroot.get()));
		registerDropSelfLootTable(TFBlocks.uncrafting_table.get());
		registerDropSelfLootTable(TFBlocks.firefly_jar.get());
		registerLootTable(TFBlocks.moss_patch.get(), BlockLootTables::onlyWithShears);
		registerLootTable(TFBlocks.mayapple.get(), BlockLootTables::onlyWithShears);
		registerLootTable(TFBlocks.clover_patch.get(), BlockLootTables::onlyWithShears);
		registerLootTable(TFBlocks.fiddlehead.get(), BlockLootTables::onlyWithShears);
		registerDropSelfLootTable(TFBlocks.mushgloom.get());
		registerDropping(TFBlocks.torchberry_plant.get(), TFItems.torchberries.get());
		registerLootTable(TFBlocks.root_strand.get(), BlockLootTables::onlyWithShears);
		registerLootTable(TFBlocks.fallen_leaves.get(), BlockLootTables::onlyWithShears);
		registerDropSelfLootTable(TFBlocks.smoker.get());
		registerDropSelfLootTable(TFBlocks.encased_smoker.get());
		registerDropSelfLootTable(TFBlocks.fire_jet.get());
		registerDropSelfLootTable(TFBlocks.encased_fire_jet.get());
		registerDropSelfLootTable(TFBlocks.naga_stone_head.get());
		registerDropSelfLootTable(TFBlocks.naga_stone.get());
		registerDropSelfLootTable(TFBlocks.nagastone_pillar.get());
		registerDropSelfLootTable(TFBlocks.nagastone_pillar_mossy.get());
		registerDropSelfLootTable(TFBlocks.nagastone_pillar_weathered.get());
		registerDropSelfLootTable(TFBlocks.etched_nagastone.get());
		registerDropSelfLootTable(TFBlocks.etched_nagastone_mossy.get());
		registerDropSelfLootTable(TFBlocks.etched_nagastone_weathered.get());
		registerDropSelfLootTable(TFBlocks.nagastone_stairs_left.get());
		registerDropSelfLootTable(TFBlocks.nagastone_stairs_right.get());
		registerDropSelfLootTable(TFBlocks.nagastone_stairs_mossy_left.get());
		registerDropSelfLootTable(TFBlocks.nagastone_stairs_mossy_right.get());
		registerDropSelfLootTable(TFBlocks.nagastone_stairs_weathered_left.get());
		registerDropSelfLootTable(TFBlocks.nagastone_stairs_weathered_right.get());
		registerLootTable(TFBlocks.naga_trophy.get(), dropping(TFItems.naga_trophy.get()));
		registerLootTable(TFBlocks.naga_wall_trophy.get(), dropping(TFItems.naga_trophy.get()));
		registerLootTable(TFBlocks.lich_trophy.get(), dropping(TFItems.lich_trophy.get()));
		registerLootTable(TFBlocks.lich_wall_trophy.get(), dropping(TFItems.lich_trophy.get()));
		registerLootTable(TFBlocks.minoshroom_trophy.get(), dropping(TFItems.minoshroom_trophy.get()));
		registerLootTable(TFBlocks.minoshroom_wall_trophy.get(), dropping(TFItems.minoshroom_trophy.get()));
		registerLootTable(TFBlocks.hydra_trophy.get(), dropping(TFItems.hydra_trophy.get()));
		registerLootTable(TFBlocks.hydra_wall_trophy.get(), dropping(TFItems.hydra_trophy.get()));
		registerLootTable(TFBlocks.knight_phantom_trophy.get(), dropping(TFItems.knight_phantom_trophy.get()));
		registerLootTable(TFBlocks.knight_phantom_wall_trophy.get(), dropping(TFItems.knight_phantom_trophy.get()));
		registerLootTable(TFBlocks.ur_ghast_trophy.get(), dropping(TFItems.ur_ghast_trophy.get()));
		registerLootTable(TFBlocks.ur_ghast_wall_trophy.get(), dropping(TFItems.ur_ghast_trophy.get()));
		registerLootTable(TFBlocks.snow_queen_trophy.get(), dropping(TFItems.snow_queen_trophy.get()));
		registerLootTable(TFBlocks.snow_queen_wall_trophy.get(), dropping(TFItems.snow_queen_trophy.get()));
		registerLootTable(TFBlocks.quest_ram_trophy.get(), dropping(TFItems.quest_ram_trophy.get()));
		registerLootTable(TFBlocks.quest_ram_wall_trophy.get(), dropping(TFItems.quest_ram_trophy.get()));
		registerDropSelfLootTable(TFBlocks.iron_ladder.get());
		registerDropSelfLootTable(TFBlocks.stone_twist.get());
		//registerDropSelfLootTable(TFBlocks.lapis_block.get());
		registerLootTable(TFBlocks.keepsake_casket.get(), droppingWithName(TFBlocks.keepsake_casket.get()));

		registerDropSelfLootTable(TFBlocks.oak_log.get());
		registerDropSelfLootTable(TFBlocks.oak_wood.get());
		registerDropSelfLootTable(TFBlocks.oak_sapling.get());
		registerLootTable(TFBlocks.oak_leaves.get(), droppingWithChancesAndSticks(TFBlocks.oak_leaves.get(), TFBlocks.oak_sapling.get(), DEFAULT_SAPLING_DROP_RATES));
		registerDropSelfLootTable(TFBlocks.rainboak_sapling.get());
		registerLootTable(TFBlocks.rainboak_leaves.get(), droppingWithChancesAndSticks(TFBlocks.rainboak_leaves.get(), TFBlocks.rainboak_sapling.get(), RARE_SAPLING_DROP_RATES));
		registerDropSelfLootTable(TFBlocks.twilight_oak_planks.get());
		registerDropSelfLootTable(TFBlocks.twilight_oak_stairs.get());
		registerLootTable(TFBlocks.twilight_oak_slab.get(), droppingSlab(TFBlocks.twilight_oak_slab.get()));
		registerDropSelfLootTable(TFBlocks.twilight_oak_button.get());
		registerDropSelfLootTable(TFBlocks.twilight_oak_fence.get());
		registerDropSelfLootTable(TFBlocks.twilight_oak_gate.get());
		registerDropSelfLootTable(TFBlocks.twilight_oak_plate.get());
		registerLootTable(TFBlocks.twilight_oak_door.get(), (b) -> droppingWhen(b, DoorBlock.HALF, DoubleBlockHalf.LOWER));
		registerDropSelfLootTable(TFBlocks.twilight_oak_trapdoor.get());

		registerDropSelfLootTable(TFBlocks.canopy_log.get());
		registerDropSelfLootTable(TFBlocks.canopy_wood.get());
		registerDropSelfLootTable(TFBlocks.canopy_sapling.get());
		registerLootTable(TFBlocks.canopy_leaves.get(), droppingWithChancesAndSticks(TFBlocks.canopy_leaves.get(), TFBlocks.canopy_sapling.get(), DEFAULT_SAPLING_DROP_RATES));
		registerDropSelfLootTable(TFBlocks.canopy_planks.get());
		registerDropSelfLootTable(TFBlocks.canopy_stairs.get());
		registerLootTable(TFBlocks.canopy_slab.get(), droppingSlab(TFBlocks.canopy_slab.get()));
		registerDropSelfLootTable(TFBlocks.canopy_button.get());
		registerDropSelfLootTable(TFBlocks.canopy_fence.get());
		registerDropSelfLootTable(TFBlocks.canopy_gate.get());
		registerDropSelfLootTable(TFBlocks.canopy_plate.get());
		registerLootTable(TFBlocks.canopy_door.get(), (b) -> droppingWhen(b, DoorBlock.HALF, DoubleBlockHalf.LOWER));
		registerDropSelfLootTable(TFBlocks.canopy_trapdoor.get());

		registerDropSelfLootTable(TFBlocks.mangrove_log.get());
		registerDropSelfLootTable(TFBlocks.mangrove_wood.get());
		registerDropSelfLootTable(TFBlocks.mangrove_sapling.get());
		registerLootTable(TFBlocks.mangrove_leaves.get(), droppingWithChancesAndSticks(TFBlocks.mangrove_leaves.get(), TFBlocks.mangrove_sapling.get(), DEFAULT_SAPLING_DROP_RATES));
		registerDropSelfLootTable(TFBlocks.mangrove_planks.get());
		registerDropSelfLootTable(TFBlocks.mangrove_stairs.get());
		registerLootTable(TFBlocks.mangrove_slab.get(), droppingSlab(TFBlocks.mangrove_slab.get()));
		registerDropSelfLootTable(TFBlocks.mangrove_button.get());
		registerDropSelfLootTable(TFBlocks.mangrove_fence.get());
		registerDropSelfLootTable(TFBlocks.mangrove_gate.get());
		registerDropSelfLootTable(TFBlocks.mangrove_plate.get());
		registerLootTable(TFBlocks.mangrove_door.get(), (b) -> droppingWhen(b, DoorBlock.HALF, DoubleBlockHalf.LOWER));
		registerDropSelfLootTable(TFBlocks.mangrove_trapdoor.get());

		registerDropSelfLootTable(TFBlocks.dark_log.get());
		registerDropSelfLootTable(TFBlocks.dark_wood.get());
		registerDropSelfLootTable(TFBlocks.darkwood_sapling.get());
		registerLootTable(TFBlocks.dark_leaves.get(), droppingWithChancesAndSticks(TFBlocks.dark_leaves.get(), TFBlocks.darkwood_sapling.get(), RARE_SAPLING_DROP_RATES));
		registerDropSelfLootTable(TFBlocks.dark_planks.get());
		registerDropSelfLootTable(TFBlocks.dark_stairs.get());
		registerLootTable(TFBlocks.dark_slab.get(), droppingSlab(TFBlocks.dark_slab.get()));
		registerDropSelfLootTable(TFBlocks.dark_button.get());
		registerDropSelfLootTable(TFBlocks.dark_fence.get());
		registerDropSelfLootTable(TFBlocks.dark_gate.get());
		registerDropSelfLootTable(TFBlocks.dark_plate.get());
		registerLootTable(TFBlocks.dark_door.get(), (b) -> droppingWhen(b, DoorBlock.HALF, DoubleBlockHalf.LOWER));
		registerDropSelfLootTable(TFBlocks.dark_trapdoor.get());

		registerDropSelfLootTable(TFBlocks.time_log.get());
		registerDropSelfLootTable(TFBlocks.time_wood.get());
		registerDropping(TFBlocks.time_log_core.get(), TFBlocks.time_log.get());
		registerDropSelfLootTable(TFBlocks.time_sapling.get());
		registerLeavesNoSapling(TFBlocks.time_leaves.get());
		registerDropSelfLootTable(TFBlocks.time_planks.get());
		registerDropSelfLootTable(TFBlocks.time_stairs.get());
		registerLootTable(TFBlocks.time_slab.get(), droppingSlab(TFBlocks.time_slab.get()));
		registerDropSelfLootTable(TFBlocks.time_button.get());
		registerDropSelfLootTable(TFBlocks.time_fence.get());
		registerDropSelfLootTable(TFBlocks.time_gate.get());
		registerDropSelfLootTable(TFBlocks.time_plate.get());
		registerLootTable(TFBlocks.time_door.get(), (b) -> droppingWhen(b, DoorBlock.HALF, DoubleBlockHalf.LOWER));
		registerDropSelfLootTable(TFBlocks.time_trapdoor.get());

		registerDropSelfLootTable(TFBlocks.transformation_log.get());
		registerDropSelfLootTable(TFBlocks.transformation_wood.get());
		registerDropping(TFBlocks.transformation_log_core.get(), TFBlocks.transformation_log.get());
		registerDropSelfLootTable(TFBlocks.transformation_sapling.get());
		registerLeavesNoSapling(TFBlocks.transformation_leaves.get());
		registerDropSelfLootTable(TFBlocks.trans_planks.get());
		registerDropSelfLootTable(TFBlocks.trans_stairs.get());
		registerLootTable(TFBlocks.trans_slab.get(), droppingSlab(TFBlocks.trans_slab.get()));
		registerDropSelfLootTable(TFBlocks.trans_button.get());
		registerDropSelfLootTable(TFBlocks.trans_fence.get());
		registerDropSelfLootTable(TFBlocks.trans_gate.get());
		registerDropSelfLootTable(TFBlocks.trans_plate.get());
		registerLootTable(TFBlocks.trans_door.get(), (b) -> droppingWhen(b, DoorBlock.HALF, DoubleBlockHalf.LOWER));
		registerDropSelfLootTable(TFBlocks.trans_trapdoor.get());

		registerDropSelfLootTable(TFBlocks.mining_log.get());
		registerDropSelfLootTable(TFBlocks.mining_wood.get());
		registerDropping(TFBlocks.mining_log_core.get(), TFBlocks.mining_log.get());
		registerDropSelfLootTable(TFBlocks.mining_sapling.get());
		registerLeavesNoSapling(TFBlocks.mining_leaves.get());
		registerDropSelfLootTable(TFBlocks.mine_planks.get());
		registerDropSelfLootTable(TFBlocks.mine_stairs.get());
		registerLootTable(TFBlocks.mine_slab.get(), droppingSlab(TFBlocks.mine_slab.get()));
		registerDropSelfLootTable(TFBlocks.mine_button.get());
		registerDropSelfLootTable(TFBlocks.mine_fence.get());
		registerDropSelfLootTable(TFBlocks.mine_gate.get());
		registerDropSelfLootTable(TFBlocks.mine_plate.get());
		registerLootTable(TFBlocks.mine_door.get(), (b) -> droppingWhen(b, DoorBlock.HALF, DoubleBlockHalf.LOWER));
		registerDropSelfLootTable(TFBlocks.mine_trapdoor.get());

		registerDropSelfLootTable(TFBlocks.sorting_log.get());
		registerDropSelfLootTable(TFBlocks.sorting_wood.get());
		registerDropping(TFBlocks.sorting_log_core.get(), TFBlocks.sorting_log.get());
		registerDropSelfLootTable(TFBlocks.sorting_sapling.get());
		registerLeavesNoSapling(TFBlocks.sorting_leaves.get());
		registerDropSelfLootTable(TFBlocks.sort_planks.get());
		registerDropSelfLootTable(TFBlocks.sort_stairs.get());
		registerLootTable(TFBlocks.sort_slab.get(), droppingSlab(TFBlocks.sort_slab.get()));
		registerDropSelfLootTable(TFBlocks.sort_button.get());
		registerDropSelfLootTable(TFBlocks.sort_fence.get());
		registerDropSelfLootTable(TFBlocks.sort_gate.get());
		registerDropSelfLootTable(TFBlocks.sort_plate.get());
		registerLootTable(TFBlocks.sort_door.get(), (b) -> droppingWhen(b, DoorBlock.HALF, DoubleBlockHalf.LOWER));
		registerDropSelfLootTable(TFBlocks.sort_trapdoor.get());


	}

	private void registerLeavesNoSapling(Block leaves) {
		LootEntry.Builder<?> sticks = withExplosionDecay(leaves, ItemLootEntry.builder(Items.STICK)
						.acceptFunction(SetCount.builder(RandomValueRange.of(1.0F, 2.0F)))
						.acceptCondition(TableBonus.builder(Enchantments.FORTUNE, 0.02F, 0.022222223F, 0.025F, 0.033333335F, 0.1F)));
		registerLootTable(leaves, droppingWithSilkTouchOrShears(leaves, sticks));
	}


	// [VanillaCopy] super.droppingWithChancesAndSticks, but non-silk touch parameter can be an item instead of a block
	private static LootTable.Builder silkAndStick(Block block, IItemProvider nonSilk, float... nonSilkFortune) {
		ILootCondition.IBuilder NOT_SILK_TOUCH_OR_SHEARS = ObfuscationReflectionHelper.getPrivateValue(net.minecraft.data.loot.BlockLootTables.class, null, "field_218577_e");
		return droppingWithSilkTouchOrShears(block, withSurvivesExplosion(block, ItemLootEntry.builder(nonSilk.asItem())).acceptCondition(TableBonus.builder(Enchantments.FORTUNE, nonSilkFortune))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1)).acceptCondition(NOT_SILK_TOUCH_OR_SHEARS).addEntry(withExplosionDecay(block, ItemLootEntry.builder(Items.STICK).acceptFunction(SetCount.builder(RandomValueRange.of(1.0F, 2.0F)))).acceptCondition(TableBonus.builder(Enchantments.FORTUNE, 0.02F, 0.022222223F, 0.025F, 0.033333335F, 0.1F))));
	}

	private void registerEmpty(Block b) {
		registerLootTable(b, LootTable.builder());
	}

	@Override
	protected Iterable<Block> getKnownBlocks() {
		// todo 1.15 once all blockitems are ported, change this to all TF blocks, so an error will be thrown if we're missing any tables
		return knownBlocks;
	}
}
