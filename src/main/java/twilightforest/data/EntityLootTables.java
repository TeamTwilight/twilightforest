package twilightforest.data;

import net.minecraft.entity.EntityType;
import net.minecraft.item.Items;
import net.minecraft.loot.*;
import net.minecraft.loot.conditions.KilledByPlayer;
import net.minecraft.loot.conditions.RandomChance;
import net.minecraft.loot.functions.LootingEnchantBonus;
import net.minecraft.loot.functions.SetCount;
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
		registerLootTable(TFEntities.hostile_wolf, fromEntityLootTable(EntityType.WOLF));
		registerLootTable(TFEntities.king_spider, fromEntityLootTable(EntityType.SPIDER));
		registerLootTable(TFEntities.mist_wolf, fromEntityLootTable(EntityType.WOLF));
		registerLootTable(TFEntities.redcap_sapper, fromEntityLootTable(TFEntities.redcap));
		registerLootTable(TFEntities.swarm_spider, fromEntityLootTable(EntityType.SPIDER));
		registerLootTable(TFEntities.tower_broodling, fromEntityLootTable(EntityType.SPIDER));
		registerLootTable(TFEntities.tower_ghast, fromEntityLootTable(EntityType.GHAST));
		registerLootTable(TFEntities.armored_giant,
				LootTable.builder()
						.addLootPool(LootPool.builder()
								.rolls(ConstantRange.of(1))
								.addEntry(ItemLootEntry.builder(TFItems.giant_sword.get()))
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
