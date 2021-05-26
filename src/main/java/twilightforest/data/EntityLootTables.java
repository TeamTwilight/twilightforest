package twilightforest.data;

import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Items;
import net.minecraft.loot.*;
import net.minecraft.loot.conditions.EntityHasProperty;
import net.minecraft.loot.conditions.KilledByPlayer;
import net.minecraft.loot.conditions.RandomChance;
import net.minecraft.loot.conditions.RandomChanceWithLooting;
import net.minecraft.loot.functions.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraftforge.registries.ForgeRegistries;
import twilightforest.TwilightForestMod;
import twilightforest.block.TFBlocks;
import twilightforest.entity.TFEntities;
import twilightforest.item.TFItems;
import twilightforest.loot.*;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class EntityLootTables extends net.minecraft.data.loot.EntityLootTables {

	private final Set<EntityType<?>> knownEntities = new HashSet<>();

	@Override
	protected void registerLootTable(EntityType<?> entity, LootTable.Builder builder) {
		super.registerLootTable(entity, builder);
		knownEntities.add(entity);
	}

	@Override
	protected void registerLootTable(ResourceLocation id, LootTable.Builder table) {
		super.registerLootTable(id, table);
	}

	@Override
	protected void addTables() {
		registerLootTable(TFEntities.adherent, emptyLootTable());
		registerLootTable(TFEntities.harbinger_cube, emptyLootTable());
		registerLootTable(TFEntities.mosquito_swarm, emptyLootTable());
		registerLootTable(TFEntities.pinch_beetle, emptyLootTable());
		registerLootTable(TFEntities.quest_ram, emptyLootTable());
		registerLootTable(TFEntities.roving_cube, emptyLootTable());
		registerLootTable(TFEntities.squirrel, emptyLootTable());
		registerLootTable(TFEntities.bunny, fromEntityLootTable(EntityType.RABBIT));
		registerLootTable(TFEntities.hedge_spider, fromEntityLootTable(EntityType.SPIDER));
		registerLootTable(TFEntities.fire_beetle, fromEntityLootTable(EntityType.CREEPER));
		registerLootTable(TFEntities.hostile_wolf, fromEntityLootTable(EntityType.WOLF));
		registerLootTable(TFEntities.king_spider, fromEntityLootTable(EntityType.SPIDER));
		registerLootTable(TFEntities.mist_wolf, fromEntityLootTable(EntityType.WOLF));
		registerLootTable(TFEntities.redcap_sapper, fromEntityLootTable(TFEntities.redcap));
		registerLootTable(TFEntities.slime_beetle, fromEntityLootTable(EntityType.SLIME));
		registerLootTable(TFEntities.swarm_spider, fromEntityLootTable(EntityType.SPIDER));
		registerLootTable(TFEntities.tower_broodling, fromEntityLootTable(EntityType.SPIDER));
		registerLootTable(TFEntities.tower_ghast, fromEntityLootTable(EntityType.GHAST));
		registerLootTable(TFEntities.bighorn_sheep, fromEntityLootTable(EntityType.SHEEP));
		registerLootTable(TFTreasure.BIGHORN_SHEEP_BLACK, sheepLootTableBuilderWithDrop(Blocks.BLACK_WOOL));
		registerLootTable(TFTreasure.BIGHORN_SHEEP_BLUE, sheepLootTableBuilderWithDrop(Blocks.BLUE_WOOL));
		registerLootTable(TFTreasure.BIGHORN_SHEEP_BROWN, sheepLootTableBuilderWithDrop(Blocks.BROWN_WOOL));
		registerLootTable(TFTreasure.BIGHORN_SHEEP_CYAN, sheepLootTableBuilderWithDrop(Blocks.CYAN_WOOL));
		registerLootTable(TFTreasure.BIGHORN_SHEEP_GRAY, sheepLootTableBuilderWithDrop(Blocks.GRAY_WOOL));
		registerLootTable(TFTreasure.BIGHORN_SHEEP_GREEN, sheepLootTableBuilderWithDrop(Blocks.GREEN_WOOL));
		registerLootTable(TFTreasure.BIGHORN_SHEEP_LIGHT_BLUE, sheepLootTableBuilderWithDrop(Blocks.LIGHT_BLUE_WOOL));
		registerLootTable(TFTreasure.BIGHORN_SHEEP_LIGHT_GRAY, sheepLootTableBuilderWithDrop(Blocks.LIGHT_GRAY_WOOL));
		registerLootTable(TFTreasure.BIGHORN_SHEEP_LIME, sheepLootTableBuilderWithDrop(Blocks.LIME_WOOL));
		registerLootTable(TFTreasure.BIGHORN_SHEEP_MAGENTA, sheepLootTableBuilderWithDrop(Blocks.MAGENTA_WOOL));
		registerLootTable(TFTreasure.BIGHORN_SHEEP_ORANGE, sheepLootTableBuilderWithDrop(Blocks.ORANGE_WOOL));
		registerLootTable(TFTreasure.BIGHORN_SHEEP_PINK, sheepLootTableBuilderWithDrop(Blocks.PINK_WOOL));
		registerLootTable(TFTreasure.BIGHORN_SHEEP_PURPLE, sheepLootTableBuilderWithDrop(Blocks.PURPLE_WOOL));
		registerLootTable(TFTreasure.BIGHORN_SHEEP_RED, sheepLootTableBuilderWithDrop(Blocks.RED_WOOL));
		registerLootTable(TFTreasure.BIGHORN_SHEEP_WHITE, sheepLootTableBuilderWithDrop(Blocks.WHITE_WOOL));
		registerLootTable(TFTreasure.BIGHORN_SHEEP_YELLOW, sheepLootTableBuilderWithDrop(Blocks.YELLOW_WOOL));

		registerLootTable(TFEntities.armored_giant,
				LootTable.builder()
						.addLootPool(LootPool.builder()
								.rolls(ConstantRange.of(1))
								.addEntry(ItemLootEntry.builder(TFItems.giant_sword.get()))
								.acceptCondition(KilledByPlayer.builder())));

		registerLootTable(TFEntities.giant_miner,
				LootTable.builder()
						.addLootPool(LootPool.builder()
								.rolls(ConstantRange.of(1))
								.addEntry(ItemLootEntry.builder(TFItems.giant_pickaxe.get()))
								.acceptCondition(KilledByPlayer.builder())));

		registerLootTable(TFEntities.blockchain_goblin,
				LootTable.builder()
						.addLootPool(LootPool.builder()
								.rolls(ConstantRange.of(1))
								.addEntry(ItemLootEntry.builder(TFItems.armor_shard.get())
										.acceptFunction(SetCount.builder(RandomValueRange.of(0.0F, 2.0F)))
										.acceptFunction(LootingEnchantBonus.builder(RandomValueRange.of(0.0F, 1.0F))))));

		registerLootTable(TFEntities.mini_ghast,
				LootTable.builder()
						.addLootPool(LootPool.builder()
								.rolls(ConstantRange.of(1))
								.addEntry(TableLootEntry.builder(EntityType.GHAST.getLootTable()))
								.acceptCondition(LootConditionIsMinion.builder(true))));

		/*registerLootTable(TFEntities.boggard,
				LootTable.builder()
						.addLootPool(LootPool.builder()
								.rolls(ConstantRange.of(1))
								.addEntry(ItemLootEntry.builder(TFItems.maze_map_focus.get())
										.acceptFunction(LootingEnchantBonus.builder(RandomValueRange.of(1.0F, 1.0F))))
								.acceptCondition(RandomChance.builder(0.2F)))
						.addLootPool(LootPool.builder()
								.rolls(ConstantRange.of(1))
								.addEntry(ItemLootEntry.builder(Items.IRON_BOOTS)
										.acceptFunction(LootingEnchantBonus.builder(RandomValueRange.of(1.0F, 1.0F))))
								.acceptCondition(RandomChance.builder(0.1666F)))
						.addLootPool(LootPool.builder()
								.rolls(ConstantRange.of(1))
								.addEntry(ItemLootEntry.builder(Items.IRON_PICKAXE)
										.acceptFunction(LootingEnchantBonus.builder(RandomValueRange.of(1.0F, 1.0F))))
								.acceptCondition(RandomChance.builder(0.1111F))));*/

		registerLootTable(TFEntities.wild_boar,
				LootTable.builder()
						.addLootPool(LootPool.builder()
								.rolls(ConstantRange.of(1))
								.addEntry(ItemLootEntry.builder(Items.PORKCHOP)
										.acceptFunction(SetCount.builder(RandomValueRange.of(1.0F, 3.0F)))
										.acceptFunction(Smelt.func_215953_b()
												.acceptCondition(EntityHasProperty.builder(LootContext.EntityTarget.THIS, ON_FIRE)))
										.acceptFunction(LootingEnchantBonus.builder(RandomValueRange.of(0.0F, 1.0F))))));

		registerLootTable(TFEntities.helmet_crab,
				LootTable.builder()
						.addLootPool(LootPool.builder()
								.rolls(ConstantRange.of(1))
								.addEntry(ItemLootEntry.builder(TFItems.armor_shard.get())
										.acceptFunction(SetCount.builder(RandomValueRange.of(0.0F, 2.0F)))
										.acceptFunction(LootingEnchantBonus.builder(RandomValueRange.of(0.0F, 1.0F)))))
						.addLootPool(LootPool.builder()
								.rolls(ConstantRange.of(1))
								.addEntry(ItemLootEntry.builder(Items.COD)
										.acceptFunction(SetCount.builder(RandomValueRange.of(1.0F, 1.0F)))
										.acceptFunction(LootingEnchantBonus.builder(RandomValueRange.of(0.0F, 1.0F))))
								.acceptCondition(RandomChance.builder(0.5F))));

		registerLootTable(TFEntities.goblin_knight_upper,
				LootTable.builder()
						.addLootPool(LootPool.builder()
								.rolls(ConstantRange.of(1))
								.addEntry(ItemLootEntry.builder(TFItems.armor_shard.get())
										.acceptFunction(SetCount.builder(RandomValueRange.of(0.0F, 2.0F)))
										.acceptFunction(LootingEnchantBonus.builder(RandomValueRange.of(0.0F, 1.0F))))));

		registerLootTable(TFEntities.goblin_knight_lower,
				LootTable.builder()
						.addLootPool(LootPool.builder()
								.rolls(ConstantRange.of(1))
								.addEntry(ItemLootEntry.builder(TFItems.armor_shard.get())
										.acceptFunction(SetCount.builder(RandomValueRange.of(0.0F, 2.0F)))
										.acceptFunction(LootingEnchantBonus.builder(RandomValueRange.of(0.0F, 1.0F))))));

		registerLootTable(TFEntities.wraith,
				LootTable.builder()
						.addLootPool(LootPool.builder()
								.rolls(ConstantRange.of(1))
								.addEntry(ItemLootEntry.builder(Items.GLOWSTONE_DUST)
										.acceptFunction(SetCount.builder(RandomValueRange.of(0.0F, 2.0F)))
										.acceptFunction(LootingEnchantBonus.builder(RandomValueRange.of(0.0F, 1.0F))))));

		registerLootTable(TFEntities.redcap,
				LootTable.builder()
						.addLootPool(LootPool.builder()
								.rolls(ConstantRange.of(1))
								.addEntry(ItemLootEntry.builder(Items.COAL)
										.acceptFunction(SetCount.builder(RandomValueRange.of(0.0F, 1.0F)))
										.acceptFunction(LootingEnchantBonus.builder(RandomValueRange.of(0.0F, 1.0F))))));

		registerLootTable(TFEntities.yeti,
				LootTable.builder()
						.addLootPool(LootPool.builder()
								.rolls(ConstantRange.of(1))
								.addEntry(ItemLootEntry.builder(TFItems.arctic_fur.get())
										.acceptFunction(SetCount.builder(RandomValueRange.of(0.0F, 2.0F)))
										.acceptFunction(LootingEnchantBonus.builder(RandomValueRange.of(0.0F, 1.0F))))));

		registerLootTable(TFEntities.winter_wolf,
				LootTable.builder()
						.addLootPool(LootPool.builder()
								.rolls(ConstantRange.of(1))
								.addEntry(ItemLootEntry.builder(TFItems.arctic_fur.get())
										.acceptFunction(SetCount.builder(RandomValueRange.of(0.0F, 2.0F)))
										.acceptFunction(LootingEnchantBonus.builder(RandomValueRange.of(0.0F, 1.0F))))));

		registerLootTable(TFEntities.tiny_bird,
				LootTable.builder()
						.addLootPool(LootPool.builder()
								.rolls(ConstantRange.of(1))
								.addEntry(ItemLootEntry.builder(Items.FEATHER)
										.acceptFunction(SetCount.builder(RandomValueRange.of(0.0F, 2.0F)))
										.acceptFunction(LootingEnchantBonus.builder(RandomValueRange.of(0.0F, 1.0F))))));

		registerLootTable(TFEntities.penguin,
				LootTable.builder()
						.addLootPool(LootPool.builder()
								.rolls(ConstantRange.of(1))
								.addEntry(ItemLootEntry.builder(Items.FEATHER)
										.acceptFunction(SetCount.builder(RandomValueRange.of(0.0F, 2.0F)))
										.acceptFunction(LootingEnchantBonus.builder(RandomValueRange.of(0.0F, 1.0F))))));

		registerLootTable(TFEntities.ice_crystal,
				LootTable.builder()
						.addLootPool(LootPool.builder()
								.rolls(ConstantRange.of(1))
								.addEntry(ItemLootEntry.builder(Items.SNOWBALL)
										.acceptFunction(SetCount.builder(RandomValueRange.of(0.0F, 2.0F)))
										.acceptFunction(LootingEnchantBonus.builder(RandomValueRange.of(0.0F, 1.0F))))));

		registerLootTable(TFEntities.unstable_ice_core,
				LootTable.builder()
						.addLootPool(LootPool.builder()
								.rolls(ConstantRange.of(1))
								.addEntry(ItemLootEntry.builder(Items.SNOWBALL)
										.acceptFunction(SetCount.builder(RandomValueRange.of(0.0F, 2.0F)))
										.acceptFunction(LootingEnchantBonus.builder(RandomValueRange.of(0.0F, 1.0F))))));

		registerLootTable(TFEntities.stable_ice_core,
				LootTable.builder()
						.addLootPool(LootPool.builder()
								.rolls(ConstantRange.of(1))
								.addEntry(ItemLootEntry.builder(Items.SNOWBALL)
										.acceptFunction(SetCount.builder(RandomValueRange.of(0.0F, 2.0F)))
										.acceptFunction(LootingEnchantBonus.builder(RandomValueRange.of(0.0F, 1.0F))))));

		registerLootTable(TFEntities.snow_guardian,
				LootTable.builder()
						.addLootPool(LootPool.builder()
								.rolls(ConstantRange.of(1))
								.addEntry(ItemLootEntry.builder(Items.SNOWBALL)
										.acceptFunction(SetCount.builder(RandomValueRange.of(0.0F, 2.0F)))
										.acceptFunction(LootingEnchantBonus.builder(RandomValueRange.of(0.0F, 1.0F))))));

		registerLootTable(TFEntities.raven,
				LootTable.builder()
						.addLootPool(LootPool.builder()
								.rolls(ConstantRange.of(1))
								.addEntry(ItemLootEntry.builder(TFItems.raven_feather.get())
										.acceptFunction(SetCount.builder(RandomValueRange.of(0.0F, 2.0F)))
										.acceptFunction(LootingEnchantBonus.builder(RandomValueRange.of(0.0F, 1.0F))))));

		registerLootTable(TFEntities.tower_termite,
				LootTable.builder()
						.addLootPool(LootPool.builder()
								.rolls(ConstantRange.of(1))
								.addEntry(ItemLootEntry.builder(TFItems.borer_essence.get())
										.acceptFunction(SetCount.builder(RandomValueRange.of(0.0F, 2.0F)))
										.acceptFunction(LootingEnchantBonus.builder(RandomValueRange.of(0.0F, 1.0F))))));

		registerLootTable(TFEntities.skeleton_druid,
				LootTable.builder()
						.addLootPool(LootPool.builder()
								.rolls(ConstantRange.of(1))
								.addEntry(ItemLootEntry.builder(Items.BONE)
										.acceptFunction(SetCount.builder(RandomValueRange.of(0.0F, 2.0F)))
										.acceptFunction(LootingEnchantBonus.builder(RandomValueRange.of(0.0F, 1.0F)))))
								.addLootPool(LootPool.builder()
										.rolls(ConstantRange.of(1))
										.addEntry(ItemLootEntry.builder(TFItems.torchberries.get())
												.acceptFunction(SetCount.builder(RandomValueRange.of(0.0F, 2.0F)))
												.acceptFunction(LootingEnchantBonus.builder(RandomValueRange.of(0.0F, 1.0F))))));

		registerLootTable(TFEntities.deer,
				LootTable.builder()
						.addLootPool(LootPool.builder()
								.rolls(ConstantRange.of(1))
								.addEntry(ItemLootEntry.builder(Items.LEATHER)
										.acceptFunction(SetCount.builder(RandomValueRange.of(0.0F, 2.0F)))
										.acceptFunction(LootingEnchantBonus.builder(RandomValueRange.of(0.0F, 1.0F)))))
						.addLootPool(LootPool.builder()
								.rolls(ConstantRange.of(1))
								.addEntry(ItemLootEntry.builder(TFItems.raw_venison.get())
										.acceptFunction(SetCount.builder(RandomValueRange.of(1.0F, 3.0F)))
										.acceptFunction(Smelt.func_215953_b()
												.acceptCondition(EntityHasProperty.builder(LootContext.EntityTarget.THIS, ON_FIRE)))
										.acceptFunction(LootingEnchantBonus.builder(RandomValueRange.of(0.0F, 1.0F))))));

		registerLootTable(TFEntities.kobold,
				LootTable.builder()
						.addLootPool(LootPool.builder()
								.rolls(ConstantRange.of(1))
								.addEntry(ItemLootEntry.builder(Items.WHEAT)
										.acceptFunction(SetCount.builder(RandomValueRange.of(0.0F, 2.0F)))
										.acceptFunction(LootingEnchantBonus.builder(RandomValueRange.of(0.0F, 1.0F)))))
						.addLootPool(LootPool.builder()
								.rolls(ConstantRange.of(1))
								.addEntry(ItemLootEntry.builder(Items.GOLD_NUGGET)
										.acceptFunction(SetCount.builder(RandomValueRange.of(-1.0F, 1.0F)))
										.acceptFunction(LootingEnchantBonus.builder(RandomValueRange.of(0.0F, 1.0F))))
								.acceptCondition(KilledByPlayer.builder())));

		registerLootTable(TFEntities.maze_slime,
				LootTable.builder()
						.addLootPool(LootPool.builder()
								.rolls(ConstantRange.of(1))
								.addEntry(ItemLootEntry.builder(Items.SLIME_BALL)
										.acceptFunction(SetCount.builder(ConstantRange.of(1)))))
						.addLootPool(LootPool.builder()
								.rolls(ConstantRange.of(1))
								.addEntry(ItemLootEntry.builder(TFItems.charm_of_keeping_1.get()))
								.acceptCondition(RandomChance.builder(0.025F))));

		registerLootTable(TFEntities.minotaur,
				LootTable.builder()
						.addLootPool(LootPool.builder()
								.rolls(ConstantRange.of(1))
								.addEntry(ItemLootEntry.builder(TFItems.raw_meef.get())
										.acceptFunction(SetCount.builder(ConstantRange.of(1)))
										.acceptFunction(Smelt.func_215953_b()
												.acceptCondition(EntityHasProperty.builder(LootContext.EntityTarget.THIS, ON_FIRE)))))
						.addLootPool(LootPool.builder()
								.rolls(ConstantRange.of(1))
								.addEntry(ItemLootEntry.builder(TFItems.maze_map_focus.get()))
								.acceptCondition(RandomChance.builder(0.025F))));

		registerLootTable(TFEntities.tower_golem,
				LootTable.builder()
						.addLootPool(LootPool.builder()
								.rolls(ConstantRange.of(1))
								.addEntry(ItemLootEntry.builder(Items.IRON_INGOT)
										.acceptFunction(SetCount.builder(RandomValueRange.of(0.0F, 2.0F)))
										.acceptFunction(LootingEnchantBonus.builder(RandomValueRange.of(0.0F, 1.0F)))))
						.addLootPool(LootPool.builder()
								.rolls(ConstantRange.of(1))
								.addEntry(ItemLootEntry.builder(TFBlocks.tower_wood.get()))
									.acceptFunction(SetCount.builder(RandomValueRange.of(0.0F, 2.0F)))
									.acceptFunction(LootingEnchantBonus.builder(RandomValueRange.of(0.0F, 1.0F)))));

		registerLootTable(TFEntities.troll,
				LootTable.builder()
						.addLootPool(LootPool.builder()
								.rolls(ConstantRange.of(1))
								.addEntry(ItemLootEntry.builder(TFItems.magic_beans.get()))
								.acceptCondition(RandomChance.builder(0.025F))));

		registerLootTable(TFEntities.death_tome,
				LootTable.builder()
						.addLootPool(LootPool.builder()
								.rolls(ConstantRange.of(1))
								.addEntry(ItemLootEntry.builder(Items.PAPER))
								.acceptFunction(SetCount.builder(ConstantRange.of(3)))
								.acceptFunction(LootingEnchantBonus.builder(RandomValueRange.of(1, 1))))
						.addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
								.addEntry(ItemLootEntry.builder(Items.WRITABLE_BOOK).weight(2).quality(3))
								.addEntry(ItemLootEntry.builder(Items.BOOK).weight(19))
								.addEntry(TableLootEntry.builder(TFTreasure.DEATH_TOME_BOOKS).weight(1)))
						.addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
								.acceptCondition(KilledByPlayer.builder())
								.acceptCondition(RandomChanceWithLooting.builder(0.025F, 0.005F))
								.addEntry(ItemLootEntry.builder(TFItems.magic_map_focus.get()))));

		registerLootTable(TFTreasure.DEATH_TOME_HURT,
				LootTable.builder()
						.addLootPool(LootPool.builder()
								.rolls(ConstantRange.of(1))
								.addEntry(TableLootEntry.builder(LootTables.EMPTY))
								.addEntry(ItemLootEntry.builder(Items.PAPER))));

		registerLootTable(TFTreasure.DEATH_TOME_BOOKS,
				LootTable.builder()
						.addLootPool(LootPool.builder()
								.rolls(ConstantRange.of(1))
								.addEntry(ItemLootEntry.builder(Items.ENCHANTED_BOOK)
										.acceptFunction(EnchantWithLevels.func_215895_a(RandomValueRange.of(1, 10))).weight(32))
								.addEntry(ItemLootEntry.builder(Items.ENCHANTED_BOOK)
										.acceptFunction(EnchantWithLevels.func_215895_a(RandomValueRange.of(11, 20))).weight(8))
								.addEntry(ItemLootEntry.builder(Items.ENCHANTED_BOOK)
										.acceptFunction(EnchantWithLevels.func_215895_a(RandomValueRange.of(21, 30))).weight(4))
								.addEntry(ItemLootEntry.builder(Items.ENCHANTED_BOOK)
										.acceptFunction(EnchantWithLevels.func_215895_a(RandomValueRange.of(31, 40))).weight(1))));

		//FIXME add shaders for the bosses
		registerLootTable(TFEntities.naga,
				LootTable.builder()
						.addLootPool(LootPool.builder()
								.rolls(ConstantRange.of(1))
								.addEntry(ItemLootEntry.builder(TFItems.naga_scale.get())
								.acceptFunction(SetCount.builder(RandomValueRange.of(6, 11)))))
						.addLootPool(LootPool.builder()
								.rolls(ConstantRange.of(1))
								.addEntry(ItemLootEntry.builder(TFBlocks.naga_trophy.get().asItem())))
						.addLootPool(LootPool.builder()
								.rolls(ConstantRange.of(1))
								.addEntry(ItemLootEntry.builder(TFItems.naga_scale.get())
										.acceptCondition(LootConditionModExists.builder("immersiveengineering"))
										.acceptFunction(LootFunctionModItemSwap.builder().apply("immersiveengineering", ForgeRegistries.ITEMS.getValue(TwilightForestMod.prefix("shader")), TFItems.naga_scale.get()))
										.acceptFunction(SetNBT.builder(Util.make(new CompoundNBT(), (nbt) -> {
											nbt.putString("shader_name", "twilightforest:naga");
										})))))
						.addLootPool(LootPool.builder()
								.rolls(ConstantRange.of(1))
								.addEntry(ItemLootEntry.builder(TFItems.naga_scale.get())
										.acceptCondition(LootConditionModExists.builder("immersiveengineering"))
										.acceptFunction(LootFunctionModItemSwap.builder().apply("immersiveengineering", ForgeRegistries.ITEMS.getValue(TwilightForestMod.prefix("shader_bag_twilight")), TFItems.naga_scale.get())))));

		registerLootTable(TFEntities.lich,
				LootTable.builder()
						.addLootPool(LootPool.builder()
								.rolls(ConstantRange.of(1))
								.addEntry(ItemLootEntry.builder(TFItems.twilight_scepter.get()))
								.addEntry(ItemLootEntry.builder(TFItems.lifedrain_scepter.get()))
								.addEntry(ItemLootEntry.builder(TFItems.zombie_scepter.get()))
								.addEntry(ItemLootEntry.builder(TFItems.shield_scepter.get())))
						.addLootPool(LootPool.builder()
								.rolls(RandomValueRange.of(2, 4))
								.addEntry(ItemLootEntry.builder(Items.GOLDEN_SWORD)
										.acceptFunction(EnchantWithLevels.func_215895_a(RandomValueRange.of(10, 40))))
								.addEntry(ItemLootEntry.builder(Items.GOLDEN_HELMET)
										.acceptFunction(EnchantWithLevels.func_215895_a(RandomValueRange.of(10, 40))))
								.addEntry(ItemLootEntry.builder(Items.GOLDEN_CHESTPLATE)
										.acceptFunction(EnchantWithLevels.func_215895_a(RandomValueRange.of(10, 40))))
								.addEntry(ItemLootEntry.builder(Items.GOLDEN_LEGGINGS)
										.acceptFunction(EnchantWithLevels.func_215895_a(RandomValueRange.of(10, 40))))
								.addEntry(ItemLootEntry.builder(Items.GOLDEN_BOOTS)
										.acceptFunction(EnchantWithLevels.func_215895_a(RandomValueRange.of(10, 40)))))
						.addLootPool(LootPool.builder()
								.rolls(ConstantRange.of(1))
								.addEntry(ItemLootEntry.builder(Items.ENDER_PEARL)
										.acceptFunction(SetCount.builder(RandomValueRange.of(1, 4)))
										.acceptFunction(LootingEnchantBonus.builder(RandomValueRange.of(0, 1)))))
						.addLootPool(LootPool.builder()
								.rolls(ConstantRange.of(1))
								.addEntry(ItemLootEntry.builder(Items.BONE)
										.acceptFunction(SetCount.builder(RandomValueRange.of(5, 9)))
										.acceptFunction(LootingEnchantBonus.builder(RandomValueRange.of(0, 1)))))
						.addLootPool(LootPool.builder()
								.rolls(ConstantRange.of(1))
								.addEntry(ItemLootEntry.builder(TFBlocks.lich_trophy.get().asItem())))
						.addLootPool(LootPool.builder()
								.rolls(ConstantRange.of(1))
								.addEntry(ItemLootEntry.builder(Items.GOLD_NUGGET)
										.acceptCondition(LootConditionModExists.builder("immersiveengineering"))
										.acceptFunction(LootFunctionModItemSwap.builder().apply("immersiveengineering", ForgeRegistries.ITEMS.getValue(TwilightForestMod.prefix("shader")), Items.GOLD_NUGGET))
										.acceptFunction(SetNBT.builder(Util.make(new CompoundNBT(), (nbt) -> {
											nbt.putString("shader_name", "twilightforest:lich");
										})))))
						.addLootPool(LootPool.builder()
								.rolls(ConstantRange.of(1))
								.addEntry(ItemLootEntry.builder(Items.GOLD_NUGGET)
										.acceptCondition(LootConditionModExists.builder("immersiveengineering"))
										.acceptFunction(LootFunctionModItemSwap.builder().apply("immersiveengineering", ForgeRegistries.ITEMS.getValue(TwilightForestMod.prefix("shader_bag_twilight")), Items.GOLD_NUGGET)))));

		registerLootTable(TFEntities.minoshroom,
				LootTable.builder()
						.addLootPool(LootPool.builder()
								.rolls(ConstantRange.of(1))
								.addEntry(ItemLootEntry.builder(TFItems.meef_stroganoff.get())
										.acceptFunction(SetCount.builder(RandomValueRange.of(2, 5)))
										.acceptFunction(LootingEnchantBonus.builder(RandomValueRange.of(0, 1)))))
						.addLootPool(LootPool.builder()
								.rolls(ConstantRange.of(1))
								.addEntry(ItemLootEntry.builder(TFBlocks.minoshroom_trophy.get().asItem())))
						.addLootPool(LootPool.builder()
								.rolls(ConstantRange.of(1))
								.addEntry(ItemLootEntry.builder(TFItems.meef_stroganoff.get())
										.acceptCondition(LootConditionModExists.builder("immersiveengineering"))
										.acceptFunction(LootFunctionModItemSwap.builder().apply("immersiveengineering", ForgeRegistries.ITEMS.getValue(TwilightForestMod.prefix("shader")), TFItems.meef_stroganoff.get()))
										.acceptFunction(SetNBT.builder(Util.make(new CompoundNBT(), (nbt) -> {
											nbt.putString("shader_name", "twilightforest:minoshroom");
										})))))
						.addLootPool(LootPool.builder()
								.rolls(ConstantRange.of(1))
								.addEntry(ItemLootEntry.builder(TFItems.meef_stroganoff.get())
										.acceptCondition(LootConditionModExists.builder("immersiveengineering"))
										.acceptFunction(LootFunctionModItemSwap.builder().apply("immersiveengineering", ForgeRegistries.ITEMS.getValue(TwilightForestMod.prefix("shader_bag_twilight")), TFItems.meef_stroganoff.get())))));

		registerLootTable(TFEntities.hydra,
				LootTable.builder()
						.addLootPool(LootPool.builder()
								.rolls(ConstantRange.of(1))
								.addEntry(ItemLootEntry.builder(TFItems.hydra_chop.get())
										.acceptFunction(SetCount.builder(RandomValueRange.of(5, 35)))
										.acceptFunction(LootingEnchantBonus.builder(RandomValueRange.of(0, 2)))))
						.addLootPool(LootPool.builder()
								.rolls(ConstantRange.of(1))
								.addEntry(ItemLootEntry.builder(TFItems.fiery_blood.get())
										.acceptFunction(SetCount.builder(RandomValueRange.of(7, 10)))
										.acceptFunction(LootingEnchantBonus.builder(RandomValueRange.of(0, 1)))))
						.addLootPool(LootPool.builder()
								.rolls(ConstantRange.of(1))
								.addEntry(ItemLootEntry.builder(TFBlocks.hydra_trophy.get().asItem())))
						.addLootPool(LootPool.builder()
								.rolls(ConstantRange.of(1))
								.addEntry(ItemLootEntry.builder(TFItems.fiery_blood.get())
										.acceptCondition(LootConditionModExists.builder("immersiveengineering"))
										.acceptFunction(LootFunctionModItemSwap.builder().apply("immersiveengineering", ForgeRegistries.ITEMS.getValue(TwilightForestMod.prefix("shader")), TFItems.fiery_blood.get()))
										.acceptFunction(SetNBT.builder(Util.make(new CompoundNBT(), (nbt) -> {
											nbt.putString("shader_name", "twilightforest:hydra");
										})))))
						.addLootPool(LootPool.builder()
								.rolls(ConstantRange.of(1))
								.addEntry(ItemLootEntry.builder(TFItems.fiery_blood.get())
										.acceptCondition(LootConditionModExists.builder("immersiveengineering"))
										.acceptFunction(LootFunctionModItemSwap.builder().apply("immersiveengineering", ForgeRegistries.ITEMS.getValue(TwilightForestMod.prefix("shader_bag_twilight")), TFItems.fiery_blood.get())))));

		registerLootTable(TFEntities.yeti_alpha,
				LootTable.builder()
						.addLootPool(LootPool.builder()
								.rolls(ConstantRange.of(1))
								.addEntry(ItemLootEntry.builder(TFItems.alpha_fur.get())
										.acceptFunction(SetCount.builder(ConstantRange.of(6)))
										.acceptFunction(LootingEnchantBonus.builder(RandomValueRange.of(0, 1)))))
						.addLootPool(LootPool.builder()
								.rolls(ConstantRange.of(1))
								.addEntry(ItemLootEntry.builder(TFItems.ice_bomb.get())
										.acceptFunction(SetCount.builder(ConstantRange.of(6)))
										.acceptFunction(LootingEnchantBonus.builder(RandomValueRange.of(0, 1)))))
						.addLootPool(LootPool.builder()
								.rolls(ConstantRange.of(1))
								.addEntry(ItemLootEntry.builder(TFItems.ice_bomb.get())
										.acceptCondition(LootConditionModExists.builder("immersiveengineering"))
										.acceptFunction(LootFunctionModItemSwap.builder().apply("immersiveengineering", ForgeRegistries.ITEMS.getValue(TwilightForestMod.prefix("shader")), TFItems.ice_bomb.get()))
										.acceptFunction(SetNBT.builder(Util.make(new CompoundNBT(), (nbt) -> {
											nbt.putString("shader_name", "twilightforest:alpha_yeti");
										})))))
						.addLootPool(LootPool.builder()
								.rolls(ConstantRange.of(1))
								.addEntry(ItemLootEntry.builder(TFItems.ice_bomb.get())
										.acceptCondition(LootConditionModExists.builder("immersiveengineering"))
										.acceptFunction(LootFunctionModItemSwap.builder().apply("immersiveengineering", ForgeRegistries.ITEMS.getValue(TwilightForestMod.prefix("shader_bag_twilight")), TFItems.ice_bomb.get())))));

		registerLootTable(TFEntities.snow_queen,
				LootTable.builder()
						.addLootPool(LootPool.builder()
								.rolls(ConstantRange.of(1))
								.addEntry(ItemLootEntry.builder(TFItems.triple_bow.get()))
								.addEntry(ItemLootEntry.builder(TFItems.seeker_bow.get())))
						.addLootPool(LootPool.builder()
								.rolls(RandomValueRange.of(1, 4))
								.addEntry(ItemLootEntry.builder(Blocks.PACKED_ICE.asItem())
										.acceptFunction(SetCount.builder(ConstantRange.of(7)))
										.acceptFunction(LootingEnchantBonus.builder(RandomValueRange.of(0, 1)))))
						.addLootPool(LootPool.builder()
								.rolls(RandomValueRange.of(2, 5))
								.addEntry(ItemLootEntry.builder(Items.SNOWBALL)
										.acceptFunction(SetCount.builder(ConstantRange.of(16)))))
						.addLootPool(LootPool.builder()
								.rolls(ConstantRange.of(1))
								.addEntry(ItemLootEntry.builder(TFBlocks.snow_queen_trophy.get().asItem())))
						.addLootPool(LootPool.builder()
								.rolls(ConstantRange.of(1))
								.addEntry(ItemLootEntry.builder(TFItems.ice_bomb.get())
										.acceptCondition(LootConditionModExists.builder("immersiveengineering"))
										.acceptFunction(LootFunctionModItemSwap.builder().apply("immersiveengineering", ForgeRegistries.ITEMS.getValue(TwilightForestMod.prefix("shader")), TFItems.ice_bomb.get()))
										.acceptFunction(SetNBT.builder(Util.make(new CompoundNBT(), (nbt) -> {
											nbt.putString("shader_name", "twilightforest:snow_queen");
										})))))
						.addLootPool(LootPool.builder()
								.rolls(ConstantRange.of(1))
								.addEntry(ItemLootEntry.builder(TFItems.ice_bomb.get())
										.acceptCondition(LootConditionModExists.builder("immersiveengineering"))
										.acceptFunction(LootFunctionModItemSwap.builder().apply("immersiveengineering", ForgeRegistries.ITEMS.getValue(TwilightForestMod.prefix("shader_bag_twilight")), TFItems.ice_bomb.get())))));

		registerLootTable(TFTreasure.QUESTING_RAM_REWARDS,
				LootTable.builder()
						.addLootPool(LootPool.builder()
								.rolls(ConstantRange.of(1))
								.addEntry(ItemLootEntry.builder(Blocks.DIAMOND_BLOCK.asItem())))
						.addLootPool(LootPool.builder()
								.rolls(ConstantRange.of(1))
								.addEntry(ItemLootEntry.builder(Blocks.IRON_BLOCK.asItem())))
						.addLootPool(LootPool.builder()
								.rolls(ConstantRange.of(1))
								.addEntry(ItemLootEntry.builder(Blocks.EMERALD_BLOCK.asItem())))
						.addLootPool(LootPool.builder()
								.rolls(ConstantRange.of(1))
								.addEntry(ItemLootEntry.builder(Blocks.LAPIS_BLOCK.asItem())))
						.addLootPool(LootPool.builder()
								.rolls(ConstantRange.of(1))
								.addEntry(ItemLootEntry.builder(Blocks.GOLD_BLOCK.asItem())))
						.addLootPool(LootPool.builder()
								.rolls(ConstantRange.of(1))
								.addEntry(ItemLootEntry.builder(TFBlocks.quest_ram_trophy.get().asItem())))
						.addLootPool(LootPool.builder()
								.rolls(ConstantRange.of(1))
								.addEntry(ItemLootEntry.builder(TFItems.crumble_horn.get())))
						.addLootPool(LootPool.builder()
								.rolls(ConstantRange.of(1))
								.addEntry(ItemLootEntry.builder(Blocks.COAL_BLOCK.asItem())
										.acceptCondition(LootConditionModExists.builder("immersiveengineering"))
										.acceptFunction(LootFunctionModItemSwap.builder().apply("immersiveengineering", ForgeRegistries.ITEMS.getValue(TwilightForestMod.prefix("shader")), Blocks.COAL_BLOCK.asItem()))
										.acceptFunction(SetNBT.builder(Util.make(new CompoundNBT(), (nbt) -> {
											nbt.putString("shader_name", "twilightforest:questing_ram");
										})))))
						.addLootPool(LootPool.builder()
								.rolls(ConstantRange.of(1))
								.addEntry(ItemLootEntry.builder(Blocks.COAL_BLOCK.asItem())
										.acceptCondition(LootConditionModExists.builder("immersiveengineering"))
										.acceptFunction(LootFunctionModItemSwap.builder().apply("immersiveengineering", ForgeRegistries.ITEMS.getValue(TwilightForestMod.prefix("shader_bag_twilight")), Blocks.COAL_BLOCK.asItem())))));
	}

	public LootTable.Builder emptyLootTable() {
		return LootTable.builder();
	}

	public LootTable.Builder fromEntityLootTable(EntityType<?> parent) {
		return LootTable.builder()
				.addLootPool(LootPool.builder()
						.rolls(ConstantRange.of(1))
						.addEntry(TableLootEntry.builder(parent.getLootTable())));
	}

	private static LootTable.Builder sheepLootTableBuilderWithDrop(IItemProvider wool) {
		return LootTable.builder().addLootPool(LootPool.builder().rolls(ConstantRange.of(1)).addEntry(ItemLootEntry.builder(wool))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1)).addEntry(TableLootEntry.builder(EntityType.SHEEP.getLootTable())));
	}

	@Override
	public Set<EntityType<?>> getKnownEntities() {
		return knownEntities;
	}
}
