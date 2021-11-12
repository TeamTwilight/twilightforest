package twilightforest.data;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import twilightforest.TwilightForestMod;
import twilightforest.advancements.ArmorInventoryChangedTrigger;
import twilightforest.advancements.HasAdvancementTrigger;
import twilightforest.block.TFBlocks;
import twilightforest.entity.TFEntities;
import twilightforest.item.TFItems;
import twilightforest.world.registration.TFStructures;
import twilightforest.world.registration.biomes.BiomeKeys;

import java.util.Locale;
import java.util.function.Consumer;

public class PatchouliAdvancementGenerator implements Consumer<Consumer<Advancement>> {
	@Override
	public void accept(Consumer<Advancement> consumer) {
		Advancement root = Advancement.Builder.advancement()
				.addCriterion("hidden", new ImpossibleTrigger.TriggerInstance())
				.save(consumer, "twilightforest:alt/root");

		//biomes
		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_forest", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/biomes/twilight_forest")))
				.addCriterion("has_firefly", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/biomes/firefly_forest")))
				.addCriterion("has_clearing", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/biomes/twilight_clearing")))
				.addCriterion("has_savannah", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/biomes/oak_savannah")))
				.addCriterion("has_dense", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/biomes/dense_twilight_forest")))
				.addCriterion("has_mush", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/biomes/mushroom_forest")))
				.addCriterion("has_dense_mush", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/biomes/dense_mushroom_forest")))
				.addCriterion("has_lake", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/biomes/twilight_lake")))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/biomes/general_biomes");

		minorKeyBiomes(consumer, root);
		makeBiomeAdvancement("dark_forest_center", BiomeKeys.DARK_FOREST_CENTER, consumer, root);
		makeBiomeAdvancement("dense_mushroom_forest", BiomeKeys.DENSE_MUSHROOM_FOREST, consumer, root);
		makeBiomeAdvancement("dense_twilight_forest", BiomeKeys.DENSE_FOREST, consumer, root);
		makeBiomeAdvancement("enchanted_forest", BiomeKeys.ENCHANTED_FOREST, consumer, root);
		makeBiomeAdvancement("final_plateau", BiomeKeys.FINAL_PLATEAU, consumer, root);
		makeBiomeAdvancement("fire_swamp", BiomeKeys.FIRE_SWAMP, consumer, root);
		makeBiomeAdvancement("firefly_forest", BiomeKeys.FIREFLY_FOREST, consumer, root);
		makeBiomeAdvancement("mushroom_forest", BiomeKeys.MUSHROOM_FOREST, consumer, root);
		makeBiomeAdvancement("oak_savannah", BiomeKeys.OAK_SAVANNAH, consumer, root);
		makeBiomeAdvancement("spooky_forest", BiomeKeys.SPOOKY_FOREST, consumer, root);
		makeBiomeAdvancement("thornlands", BiomeKeys.THORNLANDS, consumer, root);
		makeBiomeAdvancement("twilight_clearing", BiomeKeys.CLEARING, consumer, root);
		makeBiomeAdvancement("twilight_forest", BiomeKeys.FOREST, consumer, root);
		makeBiomeAdvancement("twilight_glacier", BiomeKeys.GLACIER, consumer, root);
		makeBiomeAdvancement("twilight_highlands", BiomeKeys.HIGHLANDS, consumer, root);
		makeBiomeAdvancement("twilight_lake", BiomeKeys.LAKE, consumer, root);

		//entities
		entityAdvancement(TFEntities.ADHERENT, consumer, root);
		entityAdvancement(TFEntities.ARMORED_GIANT, consumer, root);
		entityAdvancement(TFEntities.BIGHORN_SHEEP, consumer, root);
		entityAdvancement(TFEntities.BLOCKCHAIN_GOBLIN, consumer, root);
		entityAdvancement(TFEntities.DWARF_RABBIT, consumer, root);
		entityAdvancement(TFEntities.DEATH_TOME, consumer, root);
		entityAdvancement(TFEntities.DEER, consumer, root);
		entityAdvancement(TFEntities.FIRE_BEETLE, consumer, root);
		entityAdvancement(TFEntities.GIANT_MINER, consumer, root);
		entityAdvancement(TFEntities.HARBINGER_CUBE, consumer, root);
		entityAdvancement(TFEntities.HEDGE_SPIDER, consumer, root);
		entityAdvancement(TFEntities.HELMET_CRAB, consumer, root);
		entityAdvancement(TFEntities.HOSTILE_WOLF, consumer, root);
		entityAdvancement(TFEntities.HYDRA, consumer, root);
		entityAdvancement(TFEntities.ICE_CRYSTAL, consumer, root);
		entityAdvancement(TFEntities.KING_SPIDER, consumer, root);
		entityAdvancement(TFEntities.KNIGHT_PHANTOM, consumer, root);
		entityAdvancement(TFEntities.KOBOLD, consumer, root);
		entityAdvancement(TFEntities.LICH, consumer, root);
		entityAdvancement(TFEntities.LICH_MINION, consumer, root);
		entityAdvancement(TFEntities.MAZE_SLIME, consumer, root);
		entityAdvancement(TFEntities.CARMINITE_GHASTLING, consumer, root);
		entityAdvancement(TFEntities.MINOSHROOM, consumer, root);
		entityAdvancement(TFEntities.MINOTAUR, consumer, root);
		entityAdvancement(TFEntities.MIST_WOLF, consumer, root);
		entityAdvancement(TFEntities.MOSQUITO_SWARM, consumer, root);
		entityAdvancement(TFEntities.NAGA, consumer, root);
		entityAdvancement(TFEntities.PENGUIN, consumer, root);
		entityAdvancement(TFEntities.PINCH_BEETLE, consumer, root);
		entityAdvancement(TFEntities.RAVEN, consumer, root);
		entityAdvancement(TFEntities.REDCAP, consumer, root);
		entityAdvancement(TFEntities.REDCAP_SAPPER, consumer, root);
		entityAdvancement(TFEntities.SKELETON_DRUID, consumer, root);
		entityAdvancement(TFEntities.SLIME_BEETLE, consumer, root);
		entityAdvancement(TFEntities.SNOW_GUARDIAN, consumer, root);
		entityAdvancement(TFEntities.SNOW_QUEEN, consumer, root);
		entityAdvancement(TFEntities.SQUIRREL, consumer, root);
		entityAdvancement(TFEntities.STABLE_ICE_CORE, consumer, root);
		entityAdvancement(TFEntities.SWARM_SPIDER, consumer, root);
		entityAdvancement(TFEntities.TINY_BIRD, consumer, root);
		entityAdvancement(TFEntities.CARMINITE_BROODLING, consumer, root);
		entityAdvancement(TFEntities.CARMINITE_GHASTGUARD, consumer, root);
		entityAdvancement(TFEntities.CARMINITE_GOLEM, consumer, root);
		entityAdvancement(TFEntities.TOWERWOOD_BORER, consumer, root);
		entityAdvancement(TFEntities.TROLL, consumer, root);
		entityAdvancement(TFEntities.UNSTABLE_ICE_CORE, consumer, root);
		entityAdvancement(TFEntities.UR_GHAST, consumer, root);
		entityAdvancement(TFEntities.BOAR, consumer, root);
		entityAdvancement(TFEntities.WINTER_WOLF, consumer, root);
		entityAdvancement(TFEntities.WRAITH, consumer, root);
		entityAdvancement(TFEntities.YETI, consumer, root);
		entityAdvancement(TFEntities.ALPHA_YETI, consumer, root);

		Advancement.Builder.advancement().parent(root)
				.addCriterion("hurt_by_indirect", EntityHurtPlayerTrigger.TriggerInstance.entityHurtPlayer(DamagePredicate.Builder.damageInstance().sourceEntity(EntityPredicate.Builder.entity().of(TFEntities.LOWER_GOBLIN_KNIGHT).build())))
				.addCriterion("slain_by", KilledTrigger.TriggerInstance.entityKilledPlayer(EntityPredicate.Builder.entity().of(TFEntities.LOWER_GOBLIN_KNIGHT)))
				.addCriterion("hurt", PlayerHurtEntityTrigger.TriggerInstance.playerHurtEntity(EntityPredicate.Builder.entity().of(TFEntities.LOWER_GOBLIN_KNIGHT).build()))
				.addCriterion("slay", KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of(TFEntities.LOWER_GOBLIN_KNIGHT)))
				.addCriterion("summon", SummonedEntityTrigger.TriggerInstance.summonedEntity(EntityPredicate.Builder.entity().of(TFEntities.LOWER_GOBLIN_KNIGHT)))
				.addCriterion("tame", TameAnimalTrigger.TriggerInstance.tamedAnimal(EntityPredicate.Builder.entity().of(TFEntities.LOWER_GOBLIN_KNIGHT).build()))
				.addCriterion("hurt_by_indirect_alt", EntityHurtPlayerTrigger.TriggerInstance.entityHurtPlayer(DamagePredicate.Builder.damageInstance().sourceEntity(EntityPredicate.Builder.entity().of(TFEntities.UPPER_GOBLIN_KNIGHT).build())))
				.addCriterion("slain_by_alt", KilledTrigger.TriggerInstance.entityKilledPlayer(EntityPredicate.Builder.entity().of(TFEntities.UPPER_GOBLIN_KNIGHT)))
				.addCriterion("hurt_alt", PlayerHurtEntityTrigger.TriggerInstance.playerHurtEntity(EntityPredicate.Builder.entity().of(TFEntities.UPPER_GOBLIN_KNIGHT).build()))
				.addCriterion("slay_alt", KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of(TFEntities.UPPER_GOBLIN_KNIGHT)))
				.addCriterion("summon_alt", SummonedEntityTrigger.TriggerInstance.summonedEntity(EntityPredicate.Builder.entity().of(TFEntities.UPPER_GOBLIN_KNIGHT)))
				.addCriterion("tame_alt", TameAnimalTrigger.TriggerInstance.tamedAnimal(EntityPredicate.Builder.entity().of(TFEntities.UPPER_GOBLIN_KNIGHT).build()))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/entities/goblin_knight");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_bighorn", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/entities/bighorn_sheep")))
				.addCriterion("has_bunny", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/entities/bunny")))
				.addCriterion("has_deer", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/entities/deer")))
				.addCriterion("has_raven", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/entities/raven")))
				.addCriterion("has_squirrel", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/entities/squirrel")))
				.addCriterion("has_boar", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/entities/wild_boar")))
				.addCriterion("has_penguin", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/entities/penguin")))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/entities/animals");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_hedge", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/entities/hedge_spider")))
				.addCriterion("has_swarm", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/entities/swarm_spider")))
				.addCriterion("has_slime", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/entities/slime_beetle")))
				.addCriterion("has_fire", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/entities/fire_beetle")))
				.addCriterion("has_pinch", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/entities/pinch_beetle")))
				.addCriterion("has_mosquito", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/entities/mosquito_swarm")))
				.addCriterion("has_king", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/entities/king_spider")))
				.addCriterion("has_termite", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/entities/tower_termite")))
				.addCriterion("has_broodling", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/entities/tower_broodling")))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/entities/arthopods");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_ghastling", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/entities/mini_ghast")))
				.addCriterion("has_ghastguard", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/entities/tower_ghast")))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/entities/ghasts");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_armored", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/entities/armored_giant")))
				.addCriterion("has_miner", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/entities/giant_miner")))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/entities/giants");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_redcap", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/entities/redcap")))
				.addCriterion("has_sapper", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/entities/redcap_sapper")))
				.addCriterion("has_blockchain", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/entities/blockchain_goblin")))
				.addCriterion("has_knight", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/entities/goblin_knight")))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/entities/goblins");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_stable", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/entities/stable_ice_core")))
				.addCriterion("has_unstable", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/entities/unstable_ice_core")))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/entities/ice_cores");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_druid", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/entities/skeleton_druid")))
				.addCriterion("has_wraith", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/entities/wraith")))
				.addCriterion("has_guardian", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/entities/snow_gurdian")))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/entities/undead");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_hostile", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/entities/hostile_wolf")))
				.addCriterion("has_mist", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/entities/mist_wolf")))
				.addCriterion("has_winter", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/entities/winter_wolf")))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/entities/wolves");

		//landmarks
		landmarkAdvancement(TFStructures.DARK_TOWER, consumer, root);
		landmarkAdvancement(TFStructures.FINAL_CASTLE, consumer, root);
		landmarkAdvancement(TFStructures.HEDGE_MAZE, consumer, root);
		landmarkAdvancement(TFStructures.HYDRA_LAIR, consumer, root);
		landmarkAdvancement(TFStructures.AURORA_PALACE, consumer, root);
		landmarkAdvancement(TFStructures.KNIGHT_STRONGHOLD, consumer, root);
		landmarkAdvancement(TFStructures.LABYRINTH, consumer, root);
		landmarkAdvancement(TFStructures.HOLLOW_HILL_LARGE, consumer, root);
		landmarkAdvancement(TFStructures.LICH_TOWER, consumer, root);
		landmarkAdvancement(TFStructures.HOLLOW_HILL_MEDIUM, consumer, root);
		landmarkAdvancement(TFStructures.MUSHROOM_TOWER, consumer, root);
		landmarkAdvancement(TFStructures.NAGA_COURTYARD, consumer, root);
		landmarkAdvancement(TFStructures.QUEST_GROVE, consumer, root);
		landmarkAdvancement(TFStructures.HOLLOW_HILL_SMALL, consumer, root);
		landmarkAdvancement(TFStructures.YETI_CAVE, consumer, root);

		Advancement.Builder.advancement().parent(root)
				.addCriterion("found_structure", LocationTrigger.TriggerInstance.located(LocationPredicate.Builder.location().setFeature(TFStructures.TROLL_CAVE).setY(MinMaxBounds.Doubles.atLeast(150)).build()))
				.save(consumer, "twilightforest:alt/major_landmarks/giant_cloud");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("found_structure", LocationTrigger.TriggerInstance.located(LocationPredicate.Builder.location().setFeature(TFStructures.TROLL_CAVE).setY(MinMaxBounds.Doubles.atMost(50)).build()))
				.save(consumer, "twilightforest:alt/major_landmarks/troll_cave");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_smol", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/major_landmarks/hollow_hill_small")))
				.addCriterion("has_med", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/major_landmarks/hollow_hill_medium")))
				.addCriterion("has_large", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/major_landmarks/hollow_hill_large")))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/major_landmarks/hollow_hills");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_ingredient", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.ARCTIC_FUR.get()))
				.addCriterion("has_helmet", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.ARCTIC_HELMET.get()))
				.addCriterion("has_chestplate", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.ARCTIC_CHESTPLATE.get()))
				.addCriterion("has_leggings", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.ARCTIC_LEGGINGS.get()))
				.addCriterion("has_boots", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.ARCTIC_BOOTS.get()))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/arctic_armor_set");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_ironwood", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/treasures/ironwood_armor_set")))
				.addCriterion("has_naga", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/treasures/naga_armor_set")))
				.addCriterion("has_steeleaf", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/treasures/steeleaf_armor_set")))
				.addCriterion("has_fiery", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/treasures/fiery_armor_set")))
				.addCriterion("has_knightmetal", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/treasures/knightmetal_armor_set")))
				.addCriterion("has_arctic", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/treasures/arctic_armor_set")))
				.addCriterion("has_yeti", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/treasures/yeti_armor_set")))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/armors");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_triple", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/treasures/triple_bow")))
				.addCriterion("has_ice", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/treasures/ice_bow")))
				.addCriterion("has_seeker", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/treasures/seeker_bow")))
				.addCriterion("has_ender", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/treasures/ender_bow")))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/bows");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("carminite", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.CARMINITE.get()))
				.addCriterion("carminite_block", InventoryChangeTrigger.TriggerInstance.hasItems(TFBlocks.CARMINITE_BLOCK.get()))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/carminite");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_charm1", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.CHARM_OF_KEEPING_1.get()))
				.addCriterion("has_charm2", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.CHARM_OF_KEEPING_2.get()))
				.addCriterion("has_charm3", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.CHARM_OF_KEEPING_3.get()))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/charm_of_keeping");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_charm1", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.CHARM_OF_LIFE_1.get()))
				.addCriterion("has_charm2", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.CHARM_OF_LIFE_2.get()))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/charm_of_life");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_keeping", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/treasures/charm_of_keeping")))
				.addCriterion("has_life", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/treasures/charm_of_life")))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/charms");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.CRUMBLE_HORN.get()))
				.addCriterion("used", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/treasures/crumble_horn_used")))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/crumble_horn");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("used", ItemUsedOnBlockTrigger.TriggerInstance.itemUsedOnBlock(LocationPredicate.Builder.location(), ItemPredicate.Builder.item().of(TFItems.CRUMBLE_HORN.get())))
				.save(consumer, "twilightforest:alt/treasures/crumble_horn_used");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_jet", InventoryChangeTrigger.TriggerInstance.hasItems(TFBlocks.FIRE_JET.get()))
				.addCriterion("has_towerwood", InventoryChangeTrigger.TriggerInstance.hasItems(TFBlocks.ENCASED_TOWERWOOD.get()))
				.addCriterion("has_encased_ver", InventoryChangeTrigger.TriggerInstance.hasItems(TFBlocks.ENCASED_FIRE_JET.get()))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/encased_fire_jet");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_smoker", InventoryChangeTrigger.TriggerInstance.hasItems(TFBlocks.SMOKER.get()))
				.addCriterion("has_towerwood", InventoryChangeTrigger.TriggerInstance.hasItems(TFBlocks.ENCASED_TOWERWOOD.get()))
				.addCriterion("has_encased_ver", InventoryChangeTrigger.TriggerInstance.hasItems(TFBlocks.ENCASED_SMOKER.get()))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/encased_smoker");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.ENDER_BOW.get()))
				.save(consumer, "twilightforest:alt/treasures/ender_bow");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_blood", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.FIERY_BLOOD.get()))
				.addCriterion("has_tears", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.FIERY_TEARS.get()))
				.addCriterion("has_ingot", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.FIERY_INGOT.get()))
				.addCriterion("has_helmet", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.FIERY_HELMET.get()))
				.addCriterion("has_chestplate", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.FIERY_CHESTPLATE.get()))
				.addCriterion("has_leggings", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.FIERY_LEGGINGS.get()))
				.addCriterion("has_boots", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.FIERY_BOOTS.get()))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/fiery_armor_set");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_blood", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.FIERY_BLOOD.get()))
				.addCriterion("has_tears", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.FIERY_TEARS.get()))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/fiery_blood_tears");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_blood", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.FIERY_BLOOD.get()))
				.addCriterion("has_tears", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.FIERY_TEARS.get()))
				.addCriterion("has_ingot", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.FIERY_INGOT.get()))
				.addCriterion("has_sword", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.FIERY_SWORD.get()))
				.addCriterion("has_pick", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.FIERY_PICKAXE.get()))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/fiery_tool_set");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(TFBlocks.FIRE_JET.get()))
				.addCriterion("in_swamp", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/biomes/fire_swamp")))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/fire_jet");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_cobble", InventoryChangeTrigger.TriggerInstance.hasItems(TFBlocks.GIANT_COBBLESTONE.get()))
				.addCriterion("has_wood", InventoryChangeTrigger.TriggerInstance.hasItems(TFBlocks.GIANT_LOG.get()))
				.addCriterion("has_leaves", InventoryChangeTrigger.TriggerInstance.hasItems(TFBlocks.GIANT_LEAVES.get()))
				.addCriterion("has_obby", InventoryChangeTrigger.TriggerInstance.hasItems(TFBlocks.GIANT_OBSIDIAN.get()))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/giant_blocks");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_cobble", InventoryChangeTrigger.TriggerInstance.hasItems(TFBlocks.GIANT_COBBLESTONE.get()))
				.addCriterion("has_wood", InventoryChangeTrigger.TriggerInstance.hasItems(TFBlocks.GIANT_LOG.get()))
				.addCriterion("has_pick", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.GIANT_PICKAXE.get()))
				.addCriterion("has_sword", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.GIANT_SWORD.get()))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/giant_tool_set");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.GLASS_SWORD.get()))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/glass_sword");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.ICE_BOMB.get()))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/ice_bomb");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.ICE_BOW.get()))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/ice_bow");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.ICE_SWORD.get()))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/ice_sword");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_raw", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.RAW_IRONWOOD.get()))
				.addCriterion("has_ingot", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.IRONWOOD_INGOT.get()))
				.addCriterion("has_helmet", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.IRONWOOD_HELMET.get()))
				.addCriterion("has_chestplate", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.IRONWOOD_CHESTPLATE.get()))
				.addCriterion("has_leggings", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.IRONWOOD_LEGGINGS.get()))
				.addCriterion("has_boots", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.IRONWOOD_BOOTS.get()))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/ironwood_armor_set");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_raw", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.RAW_IRONWOOD.get()))
				.addCriterion("has_ingot", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.IRONWOOD_INGOT.get()))
				.addCriterion("has_sword", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.IRONWOOD_SWORD.get()))
				.addCriterion("has_pick", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.IRONWOOD_PICKAXE.get()))
				.addCriterion("has_axe", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.IRONWOOD_AXE.get()))
				.addCriterion("has_shovel", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.IRONWOOD_SHOVEL.get()))
				.addCriterion("has_hoe", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.IRONWOOD_HOE.get()))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/ironwood_tool_set");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_shard", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.ARMOR_SHARD.get()))
				.addCriterion("has_raw", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.ARMOR_SHARD_CLUSTER.get()))
				.addCriterion("has_ingot", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.KNIGHTMETAL_INGOT.get()))
				.addCriterion("has_helmet", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.KNIGHTMETAL_HELMET.get()))
				.addCriterion("has_chestplate", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.KNIGHTMETAL_CHESTPLATE.get()))
				.addCriterion("has_leggings", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.KNIGHTMETAL_LEGGINGS.get()))
				.addCriterion("has_boots", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.KNIGHTMETAL_BOOTS.get()))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/knightmetal_armor_set");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_shard", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.ARMOR_SHARD.get()))
				.addCriterion("has_raw", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.ARMOR_SHARD_CLUSTER.get()))
				.addCriterion("has_ingot", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.KNIGHTMETAL_INGOT.get()))
				.addCriterion("has_sword", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.KNIGHTMETAL_SWORD.get()))
				.addCriterion("has_pick", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.KNIGHTMETAL_PICKAXE.get()))
				.addCriterion("has_axe", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.KNIGHTMETAL_AXE.get()))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/knightmetal_tool_set");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.LIFEDRAIN_SCEPTER.get()))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/lifedrain_scepter");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.MAGIC_BEANS.get()))
				.addCriterion("used", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/treaures/magic_beanstalk")))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/magic_beans");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_item", ItemUsedOnBlockTrigger.TriggerInstance.itemUsedOnBlock(LocationPredicate.Builder.location().setBlock(BlockPredicate.Builder.block().of(TFBlocks.UBEROUS_SOIL.get()).build()), ItemPredicate.Builder.item().of(TFItems.MAGIC_BEANS.get())))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/magic_beanstalk");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_focus", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.MAGIC_MAP_FOCUS.get()))
				.addCriterion("has_empty", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.MAGIC_MAP.get()))
				.addCriterion("has_filled", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.FILLED_MAGIC_MAP.get()))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/magic_map");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_time", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/treasures/tree_of_time")))
				.addCriterion("has_trans", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/treasures/tree_of_transformation")))
				.addCriterion("has_mine", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/treasures/tree_of_mining")))
				.addCriterion("has_sort", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/treasures/tree_of_sorting")))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/magic_trees");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_magic", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/treasures/magic_map")))
				.addCriterion("has_maze", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/treasures/maze_map")))
				.addCriterion("has_ore", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/treasures/ore_map")))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/maps");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_focus", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.MAZE_MAP_FOCUS.get()))
				.addCriterion("has_empty", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.MAZE_MAP.get()))
				.addCriterion("has_filled", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.FILLED_MAZE_MAP.get()))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/maze_map");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.MAZEBREAKER_PICKAXE.get()))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/mazebreaker_pickaxe");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.DIAMOND_MINOTAUR_AXE.get()))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/minotaur_axe_diamond");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.GOLDEN_MINOTAUR_AXE.get()))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/minotaur_axe_gold");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_diamond", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/treasures/minotaur_axe_diamond")))
				.addCriterion("has_gold", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/treasures/minotaur_axe_gold")))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/minotaur_axes");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.MOON_DIAL.get()))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/moon_dial");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_scale", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.NAGA_SCALE.get()))
				.addCriterion("has_chestplate", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.NAGA_CHESTPLATE.get()))
				.addCriterion("has_leggings", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.NAGA_LEGGINGS.get()))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/naga_armor_set");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_empty", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.ORE_MAP.get()))
				.addCriterion("has_filled", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.FILLED_ORE_MAP.get()))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/ore_map");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_helm", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.PHANTOM_HELMET.get()))
				.addCriterion("has_chestplate", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.PHANTOM_CHESTPLATE.get()))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/phantom_armor_set");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_twilight", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/treasures/twilight_scepter")))
				.addCriterion("has_lifedrain", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/treasures/lifedrain_scepter")))
				.addCriterion("has_zombie", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/treasures/zombie_scepter")))
				.addCriterion("has_shield", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/treasures/shield_scepter")))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/scepters");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.SEEKER_BOW.get()))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/seeker_bow");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.FORTIFICATION_SCEPTER.get()))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/shield_scepter");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(TFBlocks.SMOKER.get()))
				.addCriterion("in_swamp", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/biomes/fire_swamp")))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/smoking_block");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_ingot", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.STEELEAF_INGOT.get()))
				.addCriterion("has_helmet", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.STEELEAF_HELMET.get()))
				.addCriterion("has_chestplate", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.STEELEAF_CHESTPLATE.get()))
				.addCriterion("has_leggings", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.STEELEAF_LEGGINGS.get()))
				.addCriterion("has_boots", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.STEELEAF_BOOTS.get()))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/steeleaf_armor_set");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_ingot", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.STEELEAF_INGOT.get()))
				.addCriterion("has_sword", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.STEELEAF_SWORD.get()))
				.addCriterion("has_pick", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.STEELEAF_PICKAXE.get()))
				.addCriterion("has_axe", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.STEELEAF_AXE.get()))
				.addCriterion("has_shovel", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.STEELEAF_SHOVEL.get()))
				.addCriterion("has_hoe", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.STEELEAF_HOE.get()))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/steeleaf_tool_set");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_ironwood", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/treasures/ironwood_tool_set")))
				.addCriterion("has_steeleaf", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/treasures/steeleaf_tool_set")))
				.addCriterion("has_fiery", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/treasures/fiery_tool_set")))
				.addCriterion("has_knightmetal", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/treasures/knightmetal_tool_set")))
				.addCriterion("has_ice", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/treasures/ice_sword")))
				.addCriterion("has_glass", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/treasures/glass_sword")))
				.addCriterion("has_giant", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/treasures/giant_tool_set")))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/tools");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_sapling", InventoryChangeTrigger.TriggerInstance.hasItems(TFBlocks.TIME_SAPLING.get()))
				.addCriterion("has_log", InventoryChangeTrigger.TriggerInstance.hasItems(TFBlocks.TIME_LOG.get()))
				.addCriterion("has_wood", InventoryChangeTrigger.TriggerInstance.hasItems(TFBlocks.TIME_WOOD.get()))
				.addCriterion("has_leaves", InventoryChangeTrigger.TriggerInstance.hasItems(TFBlocks.TIME_LEAVES.get()))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/tree_of_time");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_sapling", InventoryChangeTrigger.TriggerInstance.hasItems(TFBlocks.SORTING_SAPLING.get()))
				.addCriterion("has_log", InventoryChangeTrigger.TriggerInstance.hasItems(TFBlocks.SORTING_LOG.get()))
				.addCriterion("has_leaves", InventoryChangeTrigger.TriggerInstance.hasItems(TFBlocks.SORTING_LEAVES.get()))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/tree_of_sorting");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_sapling", InventoryChangeTrigger.TriggerInstance.hasItems(TFBlocks.MINING_SAPLING.get()))
				.addCriterion("has_log", InventoryChangeTrigger.TriggerInstance.hasItems(TFBlocks.MINING_LOG.get()))
				.addCriterion("has_wood", InventoryChangeTrigger.TriggerInstance.hasItems(TFBlocks.MINING_WOOD.get()))
				.addCriterion("has_leaves", InventoryChangeTrigger.TriggerInstance.hasItems(TFBlocks.MINING_LEAVES.get()))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/tree_of_mining");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_sapling", InventoryChangeTrigger.TriggerInstance.hasItems(TFBlocks.TRANSFORMATION_SAPLING.get()))
				.addCriterion("has_log", InventoryChangeTrigger.TriggerInstance.hasItems(TFBlocks.TRANSFORMATION_LOG.get()))
				.addCriterion("has_wood", InventoryChangeTrigger.TriggerInstance.hasItems(TFBlocks.TRANSFORMATION_WOOD.get()))
				.addCriterion("has_leaves", InventoryChangeTrigger.TriggerInstance.hasItems(TFBlocks.TRANSFORMATION_LEAVES.get()))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/tree_of_transformation");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.TRIPLE_BOW.get()))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/triple_bow");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("pedestal", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("progress_trophy_pedestal")))
				.addCriterion("naga", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("progress_naga")))
				.addCriterion("lich", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("progress_lich")))
				.addCriterion("minoshroom", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("progress_labyrinth")))
				.addCriterion("hydra", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("progress_hydra")))
				.addCriterion("knights", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("progress_knights")))
				.addCriterion("ghast", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("progress_ur_ghast")))
				.addCriterion("yeti", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("progress_yeti")))
				.addCriterion("queen", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("progress_glacier")))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/trophies");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(TFBlocks.TROPHY_PEDESTAL.get()))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/trophy_pedestal");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.TWILIGHT_SCEPTER.get()))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/twilight_scepter");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(TFBlocks.UNCRAFTING_TABLE.get()))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/uncrafting_table");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_ingredient", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.ALPHA_YETI_FUR.get()))
				.addCriterion("has_helmet", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.YETI_HELMET.get()))
				.addCriterion("has_chestplate", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.YETI_CHESTPLATE.get()))
				.addCriterion("has_leggings", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.YETI_LEGGINGS.get()))
				.addCriterion("has_boots", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.YETI_BOOTS.get()))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/yeti_armor_set");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.ZOMBIE_SCEPTER.get()))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/zombie_scepter");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("hydra_chop", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/treasures/foods/hydra_chop")))
				.addCriterion("maze_wafer", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/treasures/foods/maze_wafer")))
				.addCriterion("meef", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/treasures/foods/meef")))
				.addCriterion("venison", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/treasures/foods/venison")))
				.addCriterion("e115", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/treasures/foods/experiment_115")))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/foods/any");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.EXPERIMENT_115.get()))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/foods/experiment_115");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.HYDRA_CHOP.get()))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/foods/hydra_chop");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.MAZE_WAFER.get()))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/foods/maze_wafer");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_raw", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.RAW_MEEF.get()))
				.addCriterion("has_cooked", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.COOKED_MEEF.get()))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/foods/meef");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_raw", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.RAW_VENISON.get()))
				.addCriterion("has_cooked", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.COOKED_VENISON.get()))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/foods/venison");
	}

	private void makeBiomeAdvancement(String name, ResourceKey<Biome> key, Consumer<Advancement> consumer, Advancement root) {
		Advancement.Builder.advancement().parent(root)
				.addCriterion("in_biome", LocationTrigger.TriggerInstance.located(LocationPredicate.inBiome(key)))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/biomes/" + name);
	}

	private void entityAdvancement(EntityType<?> entity, Consumer<Advancement> consumer, Advancement root) {
		Advancement.Builder.advancement().parent(root)
				.addCriterion("hurt_by_indirect", EntityHurtPlayerTrigger.TriggerInstance.entityHurtPlayer(DamagePredicate.Builder.damageInstance().sourceEntity(EntityPredicate.Builder.entity().of(entity).build())))
				.addCriterion("slain_by", KilledTrigger.TriggerInstance.entityKilledPlayer(EntityPredicate.Builder.entity().of(entity)))
				.addCriterion("hurt", PlayerHurtEntityTrigger.TriggerInstance.playerHurtEntity(EntityPredicate.Builder.entity().of(entity).build()))
				.addCriterion("slay", KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of(entity)))
				.addCriterion("summon", SummonedEntityTrigger.TriggerInstance.summonedEntity(EntityPredicate.Builder.entity().of(entity)))
				.addCriterion("tame", TameAnimalTrigger.TriggerInstance.tamedAnimal(EntityPredicate.Builder.entity().of(entity).build()))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/entities/" + entity.getRegistryName().getPath());
	}

	private void landmarkAdvancement(StructureFeature<?> structure, Consumer<Advancement> consumer, Advancement root) {
		Advancement.Builder.advancement().parent(root)
				.addCriterion("found_structure", LocationTrigger.TriggerInstance.located(LocationPredicate.inFeature(structure)))
				.save(consumer, "twilightforest:alt/major_landmarks/" + structure.getRegistryName().getPath());
	}

	private void minorKeyBiomes(Consumer<Advancement> consumer, Advancement root) {
		Advancement.Builder.advancement().parent(root)
				.addCriterion("in_biome", LocationTrigger.TriggerInstance.located(LocationPredicate.inBiome(BiomeKeys.DARK_FOREST)))
				.addCriterion("has_other", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/biomes/dark_forest_center")))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/biomes/dark_forest");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("in_biome", LocationTrigger.TriggerInstance.located(LocationPredicate.inBiome(BiomeKeys.SWAMP)))
				.addCriterion("has_other", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/biomes/fire_swamp")))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/biomes/swamp");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("in_biome", LocationTrigger.TriggerInstance.located(LocationPredicate.inBiome(BiomeKeys.SNOWY_FOREST)))
				.addCriterion("has_other", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/biomes/twilight_glacier")))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/biomes/snowy_forest");
	}
}
