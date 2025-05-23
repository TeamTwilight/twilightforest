package twilightforest.datagen.data;

import net.minecraft.advancements.*;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.advancements.AdvancementSubProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import net.neoforged.neoforge.common.util.Lazy;
import twilightforest.TwilightForestMod;
import twilightforest.advancements.*;
import twilightforest.advancements.predicate.ItemColorPredicate;
import tamaized.beanification.Autowired;
import twilightforest.block.Experiment115Block;
import twilightforest.components.item.PotionFlaskComponent;
import twilightforest.util.AdvancementDataMultiRequirements;
import twilightforest.init.*;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class TFAdvancementGenerator implements AdvancementSubProvider {

	private static final Supplier<EntityType<?>[]> TF_KILLABLE = Lazy.of(() -> new EntityType<?>[]{TFEntities.ADHERENT.get(), TFEntities.ARMORED_GIANT.get(), TFEntities.BIGHORN_SHEEP.get(), TFEntities.BLOCKCHAIN_GOBLIN.get(), TFEntities.DWARF_RABBIT.get(), TFEntities.DEATH_TOME.get(), TFEntities.DEER.get(), TFEntities.FIRE_BEETLE.get(), TFEntities.GIANT_MINER.get(), TFEntities.LOWER_GOBLIN_KNIGHT.get(), TFEntities.UPPER_GOBLIN_KNIGHT.get(), TFEntities.HARBINGER_CUBE.get(), TFEntities.HEDGE_SPIDER.get(), TFEntities.HELMET_CRAB.get(), TFEntities.HOSTILE_WOLF.get(), TFEntities.HYDRA.get(), TFEntities.KING_SPIDER.get(), TFEntities.KNIGHT_PHANTOM.get(), TFEntities.KOBOLD.get(), TFEntities.LICH.get(), TFEntities.LICH_MINION.get(), TFEntities.MAZE_SLIME.get(), TFEntities.CARMINITE_GHASTLING.get(), TFEntities.MINOSHROOM.get(), TFEntities.MINOTAUR.get(), TFEntities.MIST_WOLF.get(), TFEntities.MOSQUITO_SWARM.get(), TFEntities.NAGA.get(), TFEntities.PENGUIN.get(), TFEntities.PINCH_BEETLE.get(), TFEntities.PLATEAU_BOSS.get(), TFEntities.QUEST_RAM.get(), TFEntities.RAVEN.get(), TFEntities.REDCAP.get(), TFEntities.REDCAP_SAPPER.get(), TFEntities.SKELETON_DRUID.get(), TFEntities.SLIME_BEETLE.get(), TFEntities.SNOW_GUARDIAN.get(), TFEntities.SNOW_QUEEN.get(), TFEntities.SQUIRREL.get(), TFEntities.STABLE_ICE_CORE.get(), TFEntities.SWARM_SPIDER.get(), TFEntities.TINY_BIRD.get(), TFEntities.CARMINITE_BROODLING.get(), TFEntities.CARMINITE_GHASTGUARD.get(), TFEntities.CARMINITE_GOLEM.get(), TFEntities.TOWERWOOD_BORER.get(), TFEntities.TROLL.get(), TFEntities.UNSTABLE_ICE_CORE.get(), TFEntities.UR_GHAST.get(), TFEntities.BOAR.get(), TFEntities.WINTER_WOLF.get(), TFEntities.WRAITH.get(), TFEntities.YETI.get(), TFEntities.ALPHA_YETI.get()});

	private static final Supplier<ItemLike[]> DENDROLOGIST_BLOCKS = Lazy.of(() -> new ItemLike[]{
		TFBlocks.TWILIGHT_OAK_LOG, TFBlocks.TWILIGHT_OAK_WOOD, TFBlocks.STRIPPED_TWILIGHT_OAK_LOG, TFBlocks.STRIPPED_TWILIGHT_OAK_WOOD, TFBlocks.HOLLOW_TWILIGHT_OAK_LOG_HORIZONTAL, TFBlocks.TWILIGHT_OAK_LEAVES, TFBlocks.TWILIGHT_OAK_SAPLING, TFBlocks.TWILIGHT_OAK_PLANKS, TFBlocks.TWILIGHT_OAK_SLAB, TFBlocks.TWILIGHT_OAK_STAIRS, TFBlocks.TWILIGHT_OAK_BUTTON, TFBlocks.TWILIGHT_OAK_FENCE, TFBlocks.TWILIGHT_OAK_GATE, TFBlocks.TWILIGHT_OAK_PLATE, TFBlocks.TWILIGHT_OAK_DOOR, TFBlocks.TWILIGHT_OAK_TRAPDOOR, TFBlocks.TWILIGHT_OAK_SIGN, TFBlocks.TWILIGHT_OAK_HANGING_SIGN, TFBlocks.TWILIGHT_OAK_CHEST, TFBlocks.TWILIGHT_OAK_TRAPPED_CHEST, TFBlocks.TWILIGHT_OAK_BANISTER, TFItems.TWILIGHT_OAK_BOAT, TFItems.TWILIGHT_OAK_CHEST_BOAT,
		TFBlocks.CANOPY_LOG, TFBlocks.CANOPY_WOOD, TFBlocks.STRIPPED_CANOPY_LOG, TFBlocks.STRIPPED_CANOPY_WOOD, TFBlocks.HOLLOW_CANOPY_LOG_HORIZONTAL, TFBlocks.CANOPY_LEAVES, TFBlocks.CANOPY_SAPLING, TFBlocks.CANOPY_PLANKS, TFBlocks.CANOPY_SLAB, TFBlocks.CANOPY_STAIRS, TFBlocks.CANOPY_BUTTON, TFBlocks.CANOPY_FENCE, TFBlocks.CANOPY_GATE, TFBlocks.CANOPY_PLATE, TFBlocks.CANOPY_DOOR, TFBlocks.CANOPY_TRAPDOOR, TFBlocks.CANOPY_SIGN, TFBlocks.CANOPY_HANGING_SIGN, TFBlocks.CANOPY_CHEST, TFBlocks.CANOPY_TRAPPED_CHEST, TFBlocks.CANOPY_BANISTER, TFBlocks.CANOPY_BOOKSHELF, TFBlocks.CHISELED_CANOPY_BOOKSHELF, TFItems.CANOPY_BOAT, TFItems.CANOPY_CHEST_BOAT,
		TFBlocks.MANGROVE_LOG, TFBlocks.MANGROVE_WOOD, TFBlocks.STRIPPED_MANGROVE_LOG, TFBlocks.STRIPPED_MANGROVE_WOOD, TFBlocks.HOLLOW_MANGROVE_LOG_HORIZONTAL, TFBlocks.MANGROVE_LEAVES, TFBlocks.MANGROVE_SAPLING, TFBlocks.MANGROVE_PLANKS, TFBlocks.MANGROVE_SLAB, TFBlocks.MANGROVE_STAIRS, TFBlocks.MANGROVE_BUTTON, TFBlocks.MANGROVE_FENCE, TFBlocks.MANGROVE_GATE, TFBlocks.MANGROVE_PLATE, TFBlocks.MANGROVE_DOOR, TFBlocks.MANGROVE_TRAPDOOR, TFBlocks.MANGROVE_SIGN, TFBlocks.MANGROVE_HANGING_SIGN, TFBlocks.MANGROVE_CHEST, TFBlocks.MANGROVE_TRAPPED_CHEST, TFBlocks.MANGROVE_BANISTER, TFItems.MANGROVE_BOAT, TFItems.MANGROVE_CHEST_BOAT,
		TFBlocks.DARK_LOG, TFBlocks.DARK_WOOD, TFBlocks.STRIPPED_DARK_LOG, TFBlocks.STRIPPED_DARK_WOOD, TFBlocks.HOLLOW_DARK_LOG_HORIZONTAL, TFBlocks.DARK_LEAVES, TFBlocks.DARKWOOD_SAPLING, TFBlocks.DARK_PLANKS, TFBlocks.DARK_SLAB, TFBlocks.DARK_STAIRS, TFBlocks.DARK_BUTTON, TFBlocks.DARK_FENCE, TFBlocks.DARK_GATE, TFBlocks.DARK_PLATE, TFBlocks.DARK_DOOR, TFBlocks.DARK_TRAPDOOR, TFBlocks.DARK_SIGN, TFBlocks.DARK_HANGING_SIGN, TFBlocks.DARK_CHEST, TFBlocks.DARK_TRAPPED_CHEST, TFBlocks.DARK_BANISTER, TFItems.DARK_BOAT, TFItems.DARK_CHEST_BOAT,
		TFBlocks.TIME_LOG, TFBlocks.TIME_WOOD, TFBlocks.STRIPPED_TIME_LOG, TFBlocks.STRIPPED_TIME_WOOD, TFBlocks.HOLLOW_TIME_LOG_HORIZONTAL, TFBlocks.TIME_LEAVES, TFBlocks.TIME_SAPLING, TFBlocks.TIME_PLANKS, TFBlocks.TIME_SLAB, TFBlocks.TIME_STAIRS, TFBlocks.TIME_BUTTON, TFBlocks.TIME_FENCE, TFBlocks.TIME_GATE, TFBlocks.TIME_PLATE, TFBlocks.TIME_DOOR, TFBlocks.TIME_TRAPDOOR, TFBlocks.TIME_SIGN, TFBlocks.TIME_HANGING_SIGN, TFBlocks.TIME_CHEST, TFBlocks.TIME_TRAPPED_CHEST, TFBlocks.TIME_BANISTER, TFItems.TIME_BOAT, TFItems.TIME_CHEST_BOAT,
		TFBlocks.TRANSFORMATION_LOG, TFBlocks.TRANSFORMATION_WOOD, TFBlocks.STRIPPED_TRANSFORMATION_LOG, TFBlocks.STRIPPED_TRANSFORMATION_WOOD, TFBlocks.HOLLOW_TRANSFORMATION_LOG_HORIZONTAL, TFBlocks.TRANSFORMATION_LEAVES, TFBlocks.TRANSFORMATION_SAPLING, TFBlocks.TRANSFORMATION_PLANKS, TFBlocks.TRANSFORMATION_SLAB, TFBlocks.TRANSFORMATION_STAIRS, TFBlocks.TRANSFORMATION_BUTTON, TFBlocks.TRANSFORMATION_FENCE, TFBlocks.TRANSFORMATION_GATE, TFBlocks.TRANSFORMATION_PLATE, TFBlocks.TRANSFORMATION_DOOR, TFBlocks.TRANSFORMATION_TRAPDOOR, TFBlocks.TRANSFORMATION_SIGN, TFBlocks.TRANSFORMATION_HANGING_SIGN, TFBlocks.TRANSFORMATION_CHEST, TFBlocks.TRANSFORMATION_TRAPPED_CHEST, TFBlocks.TRANSFORMATION_BANISTER, TFItems.TRANSFORMATION_BOAT, TFItems.TRANSFORMATION_CHEST_BOAT,
		TFBlocks.MINING_LOG, TFBlocks.MINING_WOOD, TFBlocks.STRIPPED_MINING_LOG, TFBlocks.STRIPPED_MINING_WOOD, TFBlocks.HOLLOW_MINING_LOG_HORIZONTAL, TFBlocks.MINING_LEAVES, TFBlocks.MINING_SAPLING, TFBlocks.MINING_PLANKS, TFBlocks.MINING_SLAB, TFBlocks.MINING_STAIRS, TFBlocks.MINING_BUTTON, TFBlocks.MINING_FENCE, TFBlocks.MINING_GATE, TFBlocks.MINING_PLATE, TFBlocks.MINING_DOOR, TFBlocks.MINING_TRAPDOOR, TFBlocks.MINING_SIGN, TFBlocks.MINING_HANGING_SIGN, TFBlocks.MINING_CHEST, TFBlocks.MINING_TRAPPED_CHEST, TFBlocks.MINING_BANISTER, TFItems.MINING_BOAT, TFItems.MINING_CHEST_BOAT,
		TFBlocks.SORTING_LOG, TFBlocks.SORTING_WOOD, TFBlocks.STRIPPED_SORTING_LOG, TFBlocks.STRIPPED_SORTING_WOOD, TFBlocks.HOLLOW_SORTING_LOG_HORIZONTAL, TFBlocks.SORTING_LEAVES, TFBlocks.SORTING_SAPLING, TFBlocks.SORTING_PLANKS, TFBlocks.SORTING_SLAB, TFBlocks.SORTING_STAIRS, TFBlocks.SORTING_BUTTON, TFBlocks.SORTING_FENCE, TFBlocks.SORTING_GATE, TFBlocks.SORTING_PLATE, TFBlocks.SORTING_DOOR, TFBlocks.SORTING_TRAPDOOR, TFBlocks.SORTING_SIGN, TFBlocks.SORTING_HANGING_SIGN, TFBlocks.SORTING_CHEST, TFBlocks.SORTING_TRAPPED_CHEST, TFBlocks.SORTING_BANISTER, TFItems.SORTING_BOAT, TFItems.SORTING_CHEST_BOAT,
		TFBlocks.TOWERWOOD, TFBlocks.CRACKED_TOWERWOOD, TFBlocks.MOSSY_TOWERWOOD, TFBlocks.ENCASED_TOWERWOOD,
		TFBlocks.VEILWOOD_LOG.get(), TFBlocks.VEILWOOD_WOOD.get(), TFBlocks.STRIPPED_VEILWOOD_LOG.get(), TFBlocks.STRIPPED_VEILWOOD_WOOD.get(), TFBlocks.VEILWOOD_SAPLING.get(), TFBlocks.HOLLOW_VEILWOOD_LOG_HORIZONTAL.get(), TFBlocks.VEILWOOD_PLANKS.get(), TFBlocks.VEILWOOD_SLAB.get(), TFBlocks.VEILWOOD_STAIRS.get(), TFBlocks.VEILWOOD_BUTTON.get(), TFBlocks.VEILWOOD_FENCE.get(), TFBlocks.VEILWOOD_GATE.get(), TFBlocks.VEILWOOD_PLATE.get(), TFBlocks.VEILWOOD_DOOR.get(), TFBlocks.VEILWOOD_TRAPDOOR.get(), TFBlocks.VEILWOOD_SIGN.get(), TFBlocks.VEILWOOD_HANGING_SIGN.get(), TFBlocks.VEILWOOD_CHEST.get(), TFBlocks.VEILWOOD_TRAPPED_CHEST.get(), TFBlocks.VEILWOOD_BANISTER.get(), TFItems.VEILWOOD_BOAT.get(), TFItems.VEILWOOD_CHEST_BOAT.get(),
		TFBlocks.ROOT_BLOCK, TFBlocks.ROOT_STRAND, TFBlocks.LIVEROOT_BLOCK, TFItems.LIVEROOT, TFBlocks.HOLLOW_OAK_SAPLING, TFBlocks.RAINBOW_OAK_SAPLING, TFBlocks.RAINBOW_OAK_LEAVES, TFBlocks.GIANT_LOG, TFBlocks.GIANT_LEAVES, TFBlocks.HUGE_STALK, TFBlocks.BEANSTALK_LEAVES, TFBlocks.THORN_LEAVES, TFBlocks.THORN_ROSE, TFBlocks.HEDGE, TFBlocks.FALLEN_LEAVES, TFBlocks.MANGROVE_ROOT,
	});

	@Autowired
	private static AdvancementDataMultiRequirements advancementDataMultiRequirements;

	@Autowired
	private static DrinkFromFlaskTrigger.TriggerInstance.DrinkFromFlaskTriggerInstanceFactory drinkFromFlaskTriggerInstanceFactory;

	@Override
	public void generate(HolderLookup.Provider registries, Consumer<AdvancementHolder> consumer) {
		HolderLookup.RegistryLookup<Biome> biomes = registries.lookupOrThrow(Registries.BIOME);
		HolderLookup.RegistryLookup<Structure> structures = registries.lookupOrThrow(Registries.STRUCTURE);

		AdvancementHolder root = Advancement.Builder.advancement().display(
				TFBlocks.TWILIGHT_PORTAL_MINIATURE_STRUCTURE,
				Component.translatable("advancement.twilightforest.root"),
				Component.translatable("advancement.twilightforest.root.desc"),
				TwilightForestMod.prefix("textures/block/mazestone_large_brick.png"),
				AdvancementType.TASK,
				true, false, false)
			.requirements(AdvancementRequirements.Strategy.OR)
			.addCriterion("in_tf",
				PlayerTrigger.TriggerInstance.located(
					LocationPredicate.Builder.inDimension(TFDimension.DIMENSION_KEY)))
			.addCriterion("make_portal", SimpleAdvancementTrigger.TriggerInstance.makeTFPortal())
			.save(consumer, "twilightforest:root");

		AdvancementHolder silence = this.addTFKillable(registries, Advancement.Builder.advancement().parent(root).display(
					TFItems.RAVEN_FEATHER,
					Component.translatable("advancement.twilightforest.twilight_hunter"),
					Component.translatable("advancement.twilightforest.twilight_hunter.desc"),
					null, AdvancementType.TASK, true, true, false)
				.requirements(AdvancementRequirements.Strategy.OR))
			.save(consumer, "twilightforest:twilight_hunter");

		AdvancementHolder naga = Advancement.Builder.advancement().parent(root).display(
				TFBlocks.NAGA_COURTYARD_MINIATURE_STRUCTURE,
				Component.translatable("advancement.twilightforest.kill_naga"),
				Component.translatable("advancement.twilightforest.kill_naga.desc",
					Component.translatable(TFEntities.NAGA.get().getDescriptionId()),
					Component.translatable(TFItems.NAGA_SCALE.get().getDescriptionId())),
				null, AdvancementType.GOAL, true, true, false)
			.addCriterion("naga", KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of(registries.lookupOrThrow(Registries.ENTITY_TYPE), TFEntities.NAGA.get())))
			.addCriterion("trophy", InventoryChangeTrigger.TriggerInstance.hasItems(TFBlocks.NAGA_TROPHY))
			.addCriterion("scale", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.NAGA_SCALE))
			.addCriterion("was_in_fight", HurtBossTrigger.TriggerInstance.hurtBoss(EntityPredicate.Builder.entity().of(registries.lookupOrThrow(Registries.ENTITY_TYPE), TFEntities.NAGA.get())))
			.requirements(AdvancementRequirements.Strategy.OR)
			.save(consumer, "twilightforest:progress_naga");

		AdvancementHolder lich = advancementDataMultiRequirements.wrap(Advancement.Builder.advancement().parent(naga).display(
				TFBlocks.LICH_TOWER_MINIATURE_STRUCTURE,
				Component.translatable("advancement.twilightforest.kill_lich"),
				Component.translatable("advancement.twilightforest.kill_lich.desc",
					Component.translatable(TFEntities.LICH.get().getDescriptionId())),
				null, AdvancementType.GOAL, true, true, false))
			.addCriterion("kill_lich", KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of(registries.lookupOrThrow(Registries.ENTITY_TYPE), TFEntities.LICH.get())))
			.addCriterion("trophy", InventoryChangeTrigger.TriggerInstance.hasItems(TFBlocks.LICH_TROPHY))
			.addCriterion("lifedrain_scepter", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.LIFEDRAIN_SCEPTER))
			.addCriterion("twilight_scepter", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.TWILIGHT_SCEPTER))
			.addCriterion("zombie_scepter", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.ZOMBIE_SCEPTER))
			.addCriterion("shield_scepter", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.FORTIFICATION_SCEPTER))
			.addCriterion("was_in_fight", HurtBossTrigger.TriggerInstance.hurtBoss(EntityPredicate.Builder.entity().of(registries.lookupOrThrow(Registries.ENTITY_TYPE), TFEntities.LICH.get())))
			.and()
			.addCriterion("kill_naga", this.advancementTrigger(naga))
			.requirements()
			.save(consumer, "twilightforest:progress_lich");

		AdvancementHolder minoshroom = Advancement.Builder.advancement().parent(lich).display(
				TFItems.MEEF_STROGANOFF,
				Component.translatable("advancement.twilightforest.progress_labyrinth"),
				Component.translatable("advancement.twilightforest.progress_labyrinth.desc"),
				null, AdvancementType.GOAL, true, true, false)
			.addCriterion("meef", ConsumeItemTrigger.TriggerInstance.usedItem(registries.lookupOrThrow(Registries.ITEM), TFItems.MEEF_STROGANOFF))
			.addCriterion("kill_lich", this.advancementTrigger(lich))
			.requirements(AdvancementRequirements.Strategy.AND)
			.save(consumer, "twilightforest:progress_labyrinth");

		AdvancementHolder hydra = advancementDataMultiRequirements.wrap(Advancement.Builder.advancement().parent(minoshroom).display(
				TFBlocks.HYDRA_TROPHY,
				Component.translatable("advancement.twilightforest.kill_hydra"),
				Component.translatable("advancement.twilightforest.kill_hydra.desc",
					Component.translatable(TFEntities.HYDRA.get().getDescriptionId())),
				null, AdvancementType.GOAL, true, true, false))
			.addCriterion("kill_hydra", KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of(registries.lookupOrThrow(Registries.ENTITY_TYPE), TFEntities.HYDRA.get())))
			.addCriterion("trophy", InventoryChangeTrigger.TriggerInstance.hasItems(TFBlocks.HYDRA_TROPHY))
			.addCriterion("was_in_fight", HurtBossTrigger.TriggerInstance.hurtBoss(EntityPredicate.Builder.entity().of(registries.lookupOrThrow(Registries.ENTITY_TYPE), TFEntities.HYDRA.get())))
			.and()
			.addCriterion("stroganoff", this.advancementTrigger(minoshroom))
			.requirements()
			.save(consumer, "twilightforest:progress_hydra");

		AdvancementHolder trophy_pedestal = Advancement.Builder.advancement().parent(lich).display(
				TFBlocks.TROPHY_PEDESTAL,
				Component.translatable("advancement.twilightforest.progress_trophy_pedestal"),
				Component.translatable("advancement.twilightforest.progress_trophy_pedestal.desc"),
				null, AdvancementType.GOAL, true, true, false)
			.addCriterion("trophy_pedestal", SimpleAdvancementTrigger.TriggerInstance.activatedPedestal())
			.addCriterion("kill_lich", this.advancementTrigger(lich))
			.requirements(AdvancementRequirements.Strategy.AND)
			.save(consumer, "twilightforest:progress_trophy_pedestal");

		AdvancementHolder knights = advancementDataMultiRequirements.wrap(Advancement.Builder.advancement().parent(trophy_pedestal).display(
				TFBlocks.KNIGHT_PHANTOM_TROPHY,
				Component.translatable("advancement.twilightforest.progress_knights"),
				Component.translatable("advancement.twilightforest.progress_knights.desc"),
				null, AdvancementType.GOAL, true, true, false))
			.addCriterion("all_knights", SimpleAdvancementTrigger.TriggerInstance.killAllPhantoms())
			.addCriterion("trophy", InventoryChangeTrigger.TriggerInstance.hasItems(TFBlocks.KNIGHT_PHANTOM_TROPHY))
			.and()
			.addCriterion("previous_progression", this.advancementTrigger(trophy_pedestal))
			.requirements()
			.save(consumer, "twilightforest:progress_knights");

		AdvancementHolder trap = Advancement.Builder.advancement().parent(knights).display(
				TFBlocks.GHAST_TRAP,
				Component.translatable("advancement.twilightforest.ghast_trap"),
				Component.translatable("advancement.twilightforest.ghast_trap.desc",
					Component.translatable(TFEntities.CARMINITE_GHASTLING.get().getDescriptionId()),
					Component.translatable(TFBlocks.GHAST_TRAP.get().getDescriptionId()),
					Component.translatable(TFEntities.UR_GHAST.get().getDescriptionId())),
				null, AdvancementType.TASK, true, true, false)
			.addCriterion("activate_ghast_trap", SimpleAdvancementTrigger.TriggerInstance.activateGhastTrap())
			.save(consumer, "twilightforest:ghast_trap");

		AdvancementHolder ur_ghast = advancementDataMultiRequirements.wrap(Advancement.Builder.advancement().parent(trap).display(
				TFBlocks.UR_GHAST_TROPHY,
				Component.translatable("advancement.twilightforest.progress_ur_ghast"),
				Component.translatable("advancement.twilightforest.progress_ur_ghast.desc",
					Component.translatable(TFEntities.UR_GHAST.get().getDescriptionId())),
				null, AdvancementType.GOAL, true, true, false))
			.addCriterion("ghast", KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of(registries.lookupOrThrow(Registries.ENTITY_TYPE), TFEntities.UR_GHAST.get())))
			.addCriterion("trophy", InventoryChangeTrigger.TriggerInstance.hasItems(TFBlocks.UR_GHAST_TROPHY))
			.addCriterion("was_in_fight", HurtBossTrigger.TriggerInstance.hurtBoss(EntityPredicate.Builder.entity().of(registries.lookupOrThrow(Registries.ENTITY_TYPE), TFEntities.UR_GHAST.get())))
			.and()
			.addCriterion("previous_progression", this.advancementTrigger(knights))
			.requirements()
			.save(consumer, "twilightforest:progress_ur_ghast");

		AdvancementHolder yeti = advancementDataMultiRequirements.wrap(Advancement.Builder.advancement().parent(lich).display(
				TFItems.ALPHA_YETI_FUR,
				Component.translatable("advancement.twilightforest.progress_yeti"),
				Component.translatable("advancement.twilightforest.progress_yeti.desc",
					Component.translatable(TFEntities.ALPHA_YETI.get().getDescriptionId())),
				null, AdvancementType.GOAL, true, true, false))
			.addCriterion("yeti", KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of(registries.lookupOrThrow(Registries.ENTITY_TYPE), TFEntities.ALPHA_YETI.get())))
			.addCriterion("trophy", InventoryChangeTrigger.TriggerInstance.hasItems(TFBlocks.ALPHA_YETI_TROPHY))
			.addCriterion("fur", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.ALPHA_YETI_FUR))
			.addCriterion("was_in_fight", HurtBossTrigger.TriggerInstance.hurtBoss(EntityPredicate.Builder.entity().of(registries.lookupOrThrow(Registries.ENTITY_TYPE), TFEntities.ALPHA_YETI.get())))
			.and()
			.addCriterion("previous_progression", this.advancementTrigger(lich))
			.requirements()
			.save(consumer, "twilightforest:progress_yeti");

		AdvancementHolder snow_queen = advancementDataMultiRequirements.wrap(Advancement.Builder.advancement().parent(yeti).display(
				TFBlocks.SNOW_QUEEN_TROPHY,
				Component.translatable("advancement.twilightforest.progress_glacier"),
				Component.translatable("advancement.twilightforest.progress_glacier.desc",
					Component.translatable(TFEntities.SNOW_QUEEN.get().getDescriptionId())),
				null, AdvancementType.GOAL, true, true, false))
			.addCriterion("queen", KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of(registries.lookupOrThrow(Registries.ENTITY_TYPE), TFEntities.SNOW_QUEEN.get())))
			.addCriterion("trophy", InventoryChangeTrigger.TriggerInstance.hasItems(TFBlocks.SNOW_QUEEN_TROPHY))
			.addCriterion("was_in_fight", HurtBossTrigger.TriggerInstance.hurtBoss(EntityPredicate.Builder.entity().of(registries.lookupOrThrow(Registries.ENTITY_TYPE), TFEntities.SNOW_QUEEN.get())))
			.and()
			.addCriterion("previous_progression", this.advancementTrigger(yeti))
			.requirements()
			.save(consumer, "twilightforest:progress_glacier");

		AdvancementHolder merge = Advancement.Builder.advancement().parent(lich).display(
				TFBlocks.UBEROUS_SOIL,
				Component.translatable("advancement.twilightforest.progress_merge"),
				Component.translatable("advancement.twilightforest.progress_merge.desc",
					Component.translatable(TFEntities.HYDRA.get().getDescriptionId()),
					Component.translatable(TFEntities.UR_GHAST.get().getDescriptionId()),
					Component.translatable(TFEntities.SNOW_QUEEN.get().getDescriptionId())),
				null, AdvancementType.GOAL, true, true, false)
			.addCriterion("hydra", this.advancementTrigger(hydra))
			.addCriterion("ur_ghast", this.advancementTrigger(ur_ghast))
			.addCriterion("snow_queen", this.advancementTrigger(snow_queen))
			.save(consumer, "twilightforest:progress_merge");

		Advancement.Builder.advancement().parent(merge).display(
				TFItems.MAGIC_BEANS,
				Component.translatable("advancement.twilightforest.troll"),
				Component.translatable("advancement.twilightforest.troll.desc",
					Component.translatable(TFEntities.TROLL.get().getDescriptionId())),
				null, AdvancementType.TASK, true, true, false)
			.addCriterion("troll", KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of(registries.lookupOrThrow(Registries.ENTITY_TYPE), TFEntities.TROLL.get()).located(LocationPredicate.Builder.inStructure(structures.getOrThrow(TFStructures.TROLL_CAVE)))))
			.save(consumer, "twilightforest:troll");

		AdvancementHolder beanstalk = Advancement.Builder.advancement().parent(merge).display(
				TFBlocks.HUGE_STALK,
				Component.translatable("advancement.twilightforest.beanstalk"),
				Component.translatable("advancement.twilightforest.beanstalk.desc",
					Component.translatable(TFItems.MAGIC_BEANS.get().getDescriptionId())),
				null, AdvancementType.GOAL, true, true, false)
			.addCriterion("beans", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.MAGIC_BEANS))
			.addCriterion("use_beans", ItemUsedOnLocationTrigger.TriggerInstance.itemUsedOnBlock(LocationPredicate.Builder.location().setBlock(BlockPredicate.Builder.block().of(registries.lookupOrThrow(Registries.BLOCK), TFBlocks.UBEROUS_SOIL.get())), ItemPredicate.Builder.item().of(registries.lookupOrThrow(Registries.ITEM), TFItems.MAGIC_BEANS)))
			.save(consumer, "twilightforest:beanstalk");

		AdvancementHolder giants = Advancement.Builder.advancement().parent(beanstalk).display(
				TFItems.GIANT_PICKAXE,
				Component.translatable("advancement.twilightforest.giants"),
				Component.translatable("advancement.twilightforest.giants.desc",
					Component.translatable(TFEntities.GIANT_MINER.get().getDescriptionId()),
					Component.translatable(TFItems.GIANT_PICKAXE.get().getDescriptionId())),
				null, AdvancementType.GOAL, true, true, false)
			.addCriterion("giant", KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of(registries.lookupOrThrow(Registries.ENTITY_TYPE), TFEntities.GIANT_MINER.get())))
			.addCriterion("pickaxe", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.MAGIC_BEANS))
			.save(consumer, "twilightforest:giants");

		AdvancementHolder lamp = Advancement.Builder.advancement().parent(giants).display(
				TFItems.LAMP_OF_CINDERS,
				Component.translatable("advancement.twilightforest.progress_troll"),
				Component.translatable("advancement.twilightforest.progress_troll.desc",
					Component.translatable(TFItems.LAMP_OF_CINDERS.get().getDescriptionId())),
				null, AdvancementType.GOAL, true, true, false)
			.addCriterion("lamp", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.LAMP_OF_CINDERS))
			.addCriterion("previous_progression", this.advancementTrigger(merge))
			.save(consumer, "twilightforest:progress_troll");

		Advancement.Builder.advancement().parent(lamp).display(
				Items.STRUCTURE_VOID,
				Component.translatable("advancement.twilightforest.progression_end"),
				Component.translatable("advancement.twilightforest.progression_end.desc"),
				null, AdvancementType.GOAL, true, false, false)
			.addCriterion("previous_progression", this.advancementTrigger(lamp))
			.addCriterion("plateau", PlayerTrigger.TriggerInstance.located(LocationPredicate.Builder.inBiome(biomes.getOrThrow(TFBiomes.FINAL_PLATEAU))))
			.save(consumer, "twilightforest:progression_end");

//		AdvancementHolder thornlands = Advancement.Builder.advancement().parent(lamp).display(
//						TFBlocks.BROWN_THORNS,
//						Component.translatable("advancement.twilightforest.progress_thorns"),
//						Component.translatable("advancement.twilightforest.progress_thorns.desc"),
//						null, AdvancementType.GOAL, true, true, false)
//				.addCriterion("castle", PlayerTrigger.TriggerInstance.located(LocationPredicate.inBiome(TFBiomes.FINAL_PLATEAU)))
//				.addCriterion("previous_progression", this.advancementTrigger(lamp))
//				.save(consumer, "twilightforest:progress_thorns");
//
//		Advancement.Builder.advancement().parent(thornlands).display(
//						TFBlocks.VIOLET_CASTLE_RUNE_BRICK,
//						Component.translatable("advancement.twilightforest.progress_castle"),
//						Component.translatable("advancement.twilightforest.progress_castle.desc"),
//						null, AdvancementType.GOAL, true, true, false)
//				.addCriterion("castle", PlayerTrigger.TriggerInstance.located(LocationPredicate.inStructure(TFStructures.FINAL_CASTLE)))
//				.addCriterion("previous_progression", this.advancementTrigger(thornlands))
//				.save(consumer, "twilightforest:progress_castle");

		Advancement.Builder.advancement().parent(root).display(
				TFBlocks.QUEST_RAM_TROPHY,
				Component.translatable("advancement.twilightforest.quest_ram"),
				Component.translatable("advancement.twilightforest.quest_ram.desc",
					Component.translatable(TFEntities.QUEST_RAM.get().getDescriptionId())),
				null, AdvancementType.GOAL, true, true, false)
			.addCriterion("quest_ram_complete", SimpleAdvancementTrigger.TriggerInstance.completeQuestRam())
			.rewards(AdvancementRewards.Builder.experience(100))
			.save(consumer, "twilightforest:quest_ram");

		Advancement.Builder.advancement().parent(root).display(
				TFBlocks.CICADA,
				Component.translatable("advancement.twilightforest.kill_cicada"),
				Component.translatable("advancement.twilightforest.kill_cicada.desc"),
				null, AdvancementType.TASK, true, false, true)
			.addCriterion("kill_cicada", KillBugTrigger.TriggerInstance.killBug(TFBlocks.CICADA.get()))
			.save(consumer, "twilightforest:kill_cicada");

		Advancement.Builder.advancement().parent(root).display(
				TFBlocks.UNCRAFTING_TABLE,
				Component.translatable("advancement.twilightforest.uncraft_uncrafting_table"),
				Component.translatable("advancement.twilightforest.uncraft_uncrafting_table.desc"),
				null, AdvancementType.TASK, true, true, true)
			.addCriterion("uncraft_table", UncraftItemTrigger.TriggerInstance.uncraftedItem(registries.lookupOrThrow(Registries.ITEM), TFBlocks.UNCRAFTING_TABLE))
			.save(consumer, "twilightforest:uncraft_uncrafting_table");

		AdvancementHolder focus = Advancement.Builder.advancement().parent(silence).display(
				TFItems.MAGIC_MAP_FOCUS,
				Component.translatable("advancement.twilightforest.magic_map_focus"),
				Component.translatable("advancement.twilightforest.magic_map_focus.desc",
					Component.translatable(TFItems.MAGIC_MAP_FOCUS.get().getDescriptionId()),
					Component.translatable(TFItems.RAVEN_FEATHER.get().getDescriptionId()),
					Component.translatable(Items.GLOWSTONE_DUST.getDescriptionId()),
					Component.translatable(TFItems.TORCHBERRIES.get().getDescriptionId())),
				null, AdvancementType.TASK, true, true, false)
			.addCriterion("focus", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.MAGIC_MAP_FOCUS))
			.save(consumer, "twilightforest:magic_map_focus");

		AdvancementHolder magic_map = Advancement.Builder.advancement().parent(focus).display(
				TFItems.FILLED_MAGIC_MAP,
				Component.translatable("advancement.twilightforest.magic_map"),
				Component.translatable("advancement.twilightforest.magic_map.desc",
					Component.translatable(TFItems.FILLED_MAGIC_MAP.get().getDescriptionId())),
				null, AdvancementType.TASK, true, true, false)
			.addCriterion("magic_map", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.FILLED_MAGIC_MAP))
			.save(consumer, "twilightforest:magic_map");

		AdvancementHolder maze_map = Advancement.Builder.advancement().parent(magic_map).display(
				TFItems.FILLED_MAZE_MAP,
				Component.translatable("advancement.twilightforest.maze_map"),
				Component.translatable("advancement.twilightforest.maze_map.desc",
					Component.translatable(TFItems.FILLED_MAZE_MAP.get().getDescriptionId())),
				null, AdvancementType.GOAL, true, true, false)
			.addCriterion("maze_map", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.FILLED_MAZE_MAP))
			.save(consumer, "twilightforest:maze_map");

		Advancement.Builder.advancement().parent(maze_map).display(
				TFItems.FILLED_ORE_MAP,
				Component.translatable("advancement.twilightforest.ore_map"),
				Component.translatable("advancement.twilightforest.ore_map.desc",
					Component.translatable(TFItems.FILLED_ORE_MAP.get().getDescriptionId())),
				null, AdvancementType.CHALLENGE, true, true, true)
			.addCriterion("ore_map", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.FILLED_ORE_MAP))
			.save(consumer, "twilightforest:ore_map");

		AdvancementHolder hill1 = Advancement.Builder.advancement().parent(root).display(
				Items.IRON_BOOTS,
				Component.translatable("advancement.twilightforest.hill1"),
				Component.translatable("advancement.twilightforest.hill1.desc",
					Component.translatable(TFEntities.REDCAP.get().getDescriptionId())),
				null, AdvancementType.TASK, true, true, false)
			.addCriterion("redcap", KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of(registries.lookupOrThrow(Registries.ENTITY_TYPE), TFEntities.REDCAP.get()).located(LocationPredicate.Builder.inStructure(structures.getOrThrow(TFStructures.HOLLOW_HILL_SMALL)))))
			.save(consumer, "twilightforest:hill1");

		AdvancementHolder hill2 = Advancement.Builder.advancement().parent(hill1).display(
				TFItems.IRONWOOD_PICKAXE,
				Component.translatable("advancement.twilightforest.hill2"),
				Component.translatable("advancement.twilightforest.hill2.desc",
					Component.translatable(TFEntities.REDCAP_SAPPER.get().getDescriptionId())),
				null, AdvancementType.TASK, true, true, false)
			.addCriterion("redcap", KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of(registries.lookupOrThrow(Registries.ENTITY_TYPE), TFEntities.REDCAP_SAPPER.get()).located(LocationPredicate.Builder.inStructure(structures.getOrThrow(TFStructures.HOLLOW_HILL_MEDIUM)))))
			.save(consumer, "twilightforest:hill2");

		Advancement.Builder.advancement().parent(hill2).display(
				Items.GLOWSTONE_DUST,
				Component.translatable("advancement.twilightforest.hill3"),
				Component.translatable("advancement.twilightforest.hill3.desc",
					Component.translatable(TFEntities.WRAITH.get().getDescriptionId())),
				null, AdvancementType.TASK, true, true, false)
			.addCriterion("wraith", KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of(registries.lookupOrThrow(Registries.ENTITY_TYPE), TFEntities.WRAITH.get()).located(LocationPredicate.Builder.inStructure(structures.getOrThrow(TFStructures.HOLLOW_HILL_LARGE)))))
			.save(consumer, "twilightforest:hill3");

		Advancement.Builder.advancement().parent(root).display(
				TFBlocks.HEDGE,
				Component.translatable("advancement.twilightforest.hedge"),
				Component.translatable("advancement.twilightforest.hedge.desc"),
				null, AdvancementType.TASK, true, true, false)
			.addCriterion("hedge_spider", KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of(registries.lookupOrThrow(Registries.ENTITY_TYPE), TFEntities.HEDGE_SPIDER.get()).located(LocationPredicate.Builder.inStructure(structures.getOrThrow(TFStructures.HEDGE_MAZE)))))
			.addCriterion("swarm_spider", KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of(registries.lookupOrThrow(Registries.ENTITY_TYPE), TFEntities.SWARM_SPIDER.get()).located(LocationPredicate.Builder.inStructure(structures.getOrThrow(TFStructures.HEDGE_MAZE)))))
			.requirements(AdvancementRequirements.Strategy.OR)
			.save(consumer, "twilightforest:hedge");

		advancementDataMultiRequirements.wrap(Advancement.Builder.advancement().parent(root).display(
				Items.BOWL,
				Component.translatable("advancement.twilightforest.twilight_dining"),
				Component.translatable("advancement.twilightforest.twilight_dining.desc"),
				null, AdvancementType.CHALLENGE, true, true, false))
			.addCriterion("raw_venison", ConsumeItemTrigger.TriggerInstance.usedItem(registries.lookupOrThrow(Registries.ITEM), TFItems.RAW_VENISON))
			.addCriterion("cooked_venison", ConsumeItemTrigger.TriggerInstance.usedItem(registries.lookupOrThrow(Registries.ITEM), TFItems.COOKED_VENISON))
			.and()
			.addCriterion("raw_meef", ConsumeItemTrigger.TriggerInstance.usedItem(registries.lookupOrThrow(Registries.ITEM), TFItems.RAW_MEEF))
			.addCriterion("cooked_meef", ConsumeItemTrigger.TriggerInstance.usedItem(registries.lookupOrThrow(Registries.ITEM), TFItems.COOKED_MEEF))
			.and()
			.addCriterion("meef_stroganoff", ConsumeItemTrigger.TriggerInstance.usedItem(registries.lookupOrThrow(Registries.ITEM), TFItems.MEEF_STROGANOFF))
			.and()
			.addCriterion("hydra_chop", ConsumeItemTrigger.TriggerInstance.usedItem(registries.lookupOrThrow(Registries.ITEM), TFItems.HYDRA_CHOP))
			.and()
			.addCriterion("maze_wafer", ConsumeItemTrigger.TriggerInstance.usedItem(registries.lookupOrThrow(Registries.ITEM), TFItems.MAZE_WAFER))
			.and()
			.addCriterion("experiment_115", ConsumeItemTrigger.TriggerInstance.usedItem(registries.lookupOrThrow(Registries.ITEM), TFItems.EXPERIMENT_115))
			.and()
			.addCriterion("torchberries", ConsumeItemTrigger.TriggerInstance.usedItem(registries.lookupOrThrow(Registries.ITEM), TFItems.TORCHBERRIES))
			.requirements()
			.rewards(AdvancementRewards.Builder.experience(75))
			.save(consumer, "twilightforest:twilight_dinner");

		Advancement.Builder.advancement().parent(naga).display(
				TFItems.NAGA_CHESTPLATE,
				Component.translatable("advancement.twilightforest.naga_armors"),
				Component.translatable("advancement.twilightforest.naga_armors.desc",
					Component.translatable(TFItems.NAGA_SCALE.get().getDescriptionId())),
				null, AdvancementType.CHALLENGE, true, true, false)
			.addCriterion("armor", InventoryChangeTrigger.TriggerInstance.hasItems(
				TFItems.NAGA_CHESTPLATE, TFItems.NAGA_LEGGINGS))
			.rewards(AdvancementRewards.Builder.experience(25))
			.save(consumer, "twilightforest:naga_armors");

		Advancement.Builder.advancement().parent(lich).display(
				TFItems.ZOMBIE_SCEPTER,
				Component.translatable("advancement.twilightforest.lich_scepters"),
				Component.translatable("advancement.twilightforest.lich_scepters.desc"),
				null, AdvancementType.CHALLENGE, true, true, true)
			.addCriterion("scepters", InventoryChangeTrigger.TriggerInstance.hasItems(
				TFItems.LIFEDRAIN_SCEPTER, TFItems.TWILIGHT_SCEPTER,
				TFItems.ZOMBIE_SCEPTER, TFItems.FORTIFICATION_SCEPTER))
			.rewards(AdvancementRewards.Builder.experience(100))
			.save(consumer, "twilightforest:lich_scepters");

		Advancement.Builder.advancement().parent(lich).display(
				this.flaskWithHarming(),
				Component.translatable("advancement.twilightforest.full_mettle_alchemist"),
				Component.translatable("advancement.twilightforest.full_mettle_alchemist.desc"),
				null, AdvancementType.CHALLENGE, true, true, true)
			.addCriterion("drink_4_harming", drinkFromFlaskTriggerInstanceFactory.drankPotion(3, MinMaxBounds.Ints.atMost(6), Potions.STRONG_HARMING))
			.rewards(AdvancementRewards.Builder.experience(100))
			.save(consumer, "twilightforest:full_mettle_alchemist");

		Advancement.Builder.advancement().parent(minoshroom).display(
				TFItems.MAZEBREAKER_PICKAXE,
				Component.translatable("advancement.twilightforest.mazebreaker"),
				Component.translatable("advancement.twilightforest.mazebreaker.desc",
					Component.translatable(TFItems.MAZEBREAKER_PICKAXE.get().getDescriptionId())),
				null, AdvancementType.CHALLENGE, true, true, false)
			.addCriterion("pick", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.MAZEBREAKER_PICKAXE))
			.rewards(AdvancementRewards.Builder.experience(50))
			.save(consumer, "twilightforest:mazebreaker");

		Advancement.Builder.advancement().parent(hydra).display(
				TFItems.HYDRA_CHOP,
				Component.translatable("advancement.twilightforest.hydra_chop"),
				Component.translatable("advancement.twilightforest.hydra_chop.desc",
					Component.translatable(TFEntities.HYDRA.get().getDescriptionId())),
				null, AdvancementType.TASK, true, true, false)
			.addCriterion("hydra_chop", SimpleAdvancementTrigger.TriggerInstance.eatHydraChop())
			.save(consumer, "twilightforest:hydra_chop");

		advancementDataMultiRequirements.wrap(Advancement.Builder.advancement().parent(hydra).display(
				TFItems.FIERY_SWORD,
				Component.translatable("advancement.twilightforest.fiery_set"),
				Component.translatable("advancement.twilightforest.fiery_set.desc"),
				null, AdvancementType.CHALLENGE, true, true, false))
			.addCriterion("fiery_pick", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.FIERY_PICKAXE))
			.addCriterion("fiery_sword", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.FIERY_SWORD))
			.and()
			.addCriterion("fiery_helmet", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.FIERY_HELMET))
			.addCriterion("fiery_chestplate", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.FIERY_CHESTPLATE))
			.addCriterion("fiery_leggings", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.FIERY_LEGGINGS))
			.addCriterion("fiery_boots", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.FIERY_BOOTS))
			.requirements()
			.rewards(AdvancementRewards.Builder.experience(75))
			.save(consumer, "twilightforest:fiery_set");

		AdvancementHolder e115 = Advancement.Builder.advancement().parent(knights).display(
				TFItems.EXPERIMENT_115,
				Component.translatable("advancement.twilightforest.experiment_115"),
				Component.translatable("advancement.twilightforest.experiment_115.desc"),
				null, AdvancementType.TASK, true, true, false)
			.addCriterion("eat_experiment_115", ConsumeItemTrigger.TriggerInstance.usedItem(registries.lookupOrThrow(Registries.ITEM), TFItems.EXPERIMENT_115))
			.save(consumer, "twilightforest:experiment_115");

		Advancement.Builder.advancement().parent(e115).display(
				e115Tag("think"),
				Component.translatable("advancement.twilightforest.experiment_115_3"),
				Component.translatable("advancement.twilightforest.experiment_115_3.desc"),
				null, AdvancementType.CHALLENGE, true, true, true)
			.addCriterion("eat_115_e115", PlayerTrigger.TriggerInstance.located(Optional.of(EntityPredicate.Builder.entity().subPredicate(PlayerPredicate.Builder.player().addStat(Stats.CUSTOM, registries.lookupOrThrow(Registries.CUSTOM_STAT).getOrThrow(TFStats.E115_SLICES_EATEN.getKey()), MinMaxBounds.Ints.atLeast(115)).build()).build())))
			.save(consumer, "twilightforest:experiment_115_115");

		Advancement.Builder.advancement().parent(e115).display(
				e115Tag("full"),
				Component.translatable("advancement.twilightforest.experiment_115_2"),
				Component.translatable("advancement.twilightforest.experiment_115_2.desc"),
				null, AdvancementType.CHALLENGE, true, true, true)
			.addCriterion("place_complete_e115", ItemUsedOnLocationTrigger.TriggerInstance.itemUsedOnBlock(LocationPredicate.Builder.location().setBlock(BlockPredicate.Builder.block().of(registries.lookupOrThrow(Registries.BLOCK), TFBlocks.EXPERIMENT_115.get()).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(Experiment115Block.REGENERATE, true))), ItemPredicate.Builder.item().of(registries.lookupOrThrow(Registries.ITEM), Items.REDSTONE)))
			.save(consumer, "twilightforest:experiment_115_self_replenishing");

		Advancement.Builder.advancement().parent(yeti).display(
						TFItems.ARCTIC_CHESTPLATE.get(),
						Component.translatable("advancement.twilightforest.arctic_dyed"),
						Component.translatable("advancement.twilightforest.arctic_dyed.desc"),
						null, AdvancementType.TASK, true, true, false)
				.addCriterion("helmet", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(registries.lookupOrThrow(Registries.ITEM), TFItems.ARCTIC_HELMET.get()).withSubPredicate(TFItemSubPredicates.COLOR.get(), ItemColorPredicate.anyColor()).build()))
				.addCriterion("chestplate", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(registries.lookupOrThrow(Registries.ITEM), TFItems.ARCTIC_CHESTPLATE.get()).withSubPredicate(TFItemSubPredicates.COLOR.get(), ItemColorPredicate.anyColor()).build()))
				.addCriterion("leggings", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(registries.lookupOrThrow(Registries.ITEM), TFItems.ARCTIC_LEGGINGS.get()).withSubPredicate(TFItemSubPredicates.COLOR.get(), ItemColorPredicate.anyColor()).build()))
				.addCriterion("boots", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(registries.lookupOrThrow(Registries.ITEM), TFItems.ARCTIC_BOOTS.get()).withSubPredicate(TFItemSubPredicates.COLOR.get(), ItemColorPredicate.anyColor()).build()))
				.rewards(AdvancementRewards.Builder.experience(25))
				.save(consumer, "twilightforest:arctic_armor_dyed");

		Advancement.Builder.advancement().parent(yeti).display(
				TFItems.GLASS_SWORD,
				Component.translatable("advancement.twilightforest.glass_sword"),
				Component.translatable("advancement.twilightforest.glass_sword.desc"),
				null, AdvancementType.CHALLENGE, true, true, true)
			.addCriterion("broken_sword", SimpleAdvancementTrigger.TriggerInstance.brokenSword())
			.rewards(AdvancementRewards.Builder.experience(42))
			.save(consumer, "twilightforest:break_glass_sword");

		this.addDendrologistBlock(Advancement.Builder.advancement().parent(root)
				.display(TFBlocks.TWILIGHT_OAK_FENCE,
					Component.translatable("advancement.twilightforest.arborist"),
					Component.translatable("advancement.twilightforest.arborist.desc"),
					null, AdvancementType.CHALLENGE, true, true, false)
				.requirements(AdvancementRequirements.Strategy.AND))
			.rewards(AdvancementRewards.Builder.experience(1000))
			.save(consumer, "twilightforest:arborist");

	}

	private ItemStack e115Tag(String key) {
		ItemStack itemstack = new ItemStack(TFItems.EXPERIMENT_115.get());
		itemstack.set(TFDataComponents.EXPERIMENT_115_VARIANTS, key);
		return itemstack;
	}

	private ItemStack flaskWithHarming() {
		ItemStack itemstack = new ItemStack(TFItems.BRITTLE_FLASK.get());
		itemstack.set(TFDataComponents.POTION_FLASK_CONTENTS, new PotionFlaskComponent(new PotionContents(Potions.STRONG_HARMING), 4, 0, false));
		return itemstack;
	}

	private Advancement.Builder addTFKillable(HolderLookup.Provider registries, Advancement.Builder builder) {
		for (EntityType<?> entity : TF_KILLABLE.get()) {
			builder.addCriterion(EntityType.getKey(entity).getPath(),
				KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of(registries.lookupOrThrow(Registries.ENTITY_TYPE), entity)
					.located(LocationPredicate.Builder.inDimension(TFDimension.DIMENSION_KEY))));
		}
		return builder;
	}

	private Advancement.Builder addDendrologistBlock(Advancement.Builder builder) {
		for (ItemLike dendrologistBlock : DENDROLOGIST_BLOCKS.get()) {
			builder.addCriterion(BuiltInRegistries.ITEM.getKey(dendrologistBlock.asItem()).getPath(), InventoryChangeTrigger.TriggerInstance.hasItems(dendrologistBlock));
		}
		return builder;
	}

	private Criterion<PlayerTrigger.TriggerInstance> advancementTrigger(AdvancementHolder advancement) {
		return this.advancementTrigger(advancement.id().getPath());
	}

	private Criterion<PlayerTrigger.TriggerInstance> advancementTrigger(String name) {
		return CriteriaTriggers.TICK.createCriterion(new PlayerTrigger.TriggerInstance(Optional.of(ContextAwarePredicate.create(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().subPredicate(PlayerPredicate.Builder.player().checkAdvancementDone(TwilightForestMod.prefix(name), true).build())).build()))));
	}
}
