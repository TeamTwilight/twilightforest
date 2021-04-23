package twilightforest.data;

import net.minecraft.entity.EntityType;
import net.minecraft.item.Items;
import net.minecraft.loot.*;
import net.minecraft.loot.conditions.EntityHasProperty;
import net.minecraft.loot.conditions.KilledByPlayer;
import net.minecraft.loot.conditions.RandomChance;
import net.minecraft.loot.functions.LootingEnchantBonus;
import net.minecraft.loot.functions.SetCount;
import net.minecraft.loot.functions.SetName;
import net.minecraft.loot.functions.Smelt;
import twilightforest.block.TFBlocks;
import twilightforest.entity.TFEntities;
import twilightforest.item.TFItems;
import twilightforest.loot.LootConditionIsMinion;

import java.util.HashSet;
import java.util.Set;

public class EntityLootTables extends net.minecraft.data.loot.EntityLootTables {

	private final Set<EntityType<?>> knownEntities = new HashSet<>();

	@Override
	protected void registerLootTable(EntityType<?> entity, LootTable.Builder builder) {
		super.registerLootTable(entity, builder);
		knownEntities.add(entity);
	}

	@Override
	protected void addTables() {
		registerLootTable(TFEntities.adherent, emptyLootTable());
		registerLootTable(TFEntities.harbinger_cube, emptyLootTable());
		//registerLootTable(TFEntities.firefly, emptyLootTable());
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

	@Override
	public Set<EntityType<?>> getKnownEntities() {
		return knownEntities;
	}
}
