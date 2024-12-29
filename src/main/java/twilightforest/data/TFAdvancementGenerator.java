package twilightforest.data;

import net.minecraft.advancements.*;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentPredicate;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
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
import net.neoforged.neoforge.common.data.AdvancementProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.util.Lazy;
import twilightforest.TwilightForestMod;
import twilightforest.advancements.*;
import twilightforest.advancements.predicate.ItemColorPredicate;
import twilightforest.beans.Autowired;
import twilightforest.block.Experiment115Block;
import twilightforest.components.item.PotionFlaskComponent;
import twilightforest.data.helpers.AdvancementDataMultiRequirements;
import twilightforest.init.*;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class TFAdvancementGenerator implements AdvancementProvider.AdvancementGenerator {

	private static final Supplier<EntityType<?>[]> TF_KILLABLE = Lazy.of(() -> new EntityType<?>[]{TFEntities.ADHERENT.get(), TFEntities.ARMORED_GIANT.get(), TFEntities.BIGHORN_SHEEP.get(), TFEntities.BLOCKCHAIN_GOBLIN.get(), TFEntities.DWARF_RABBIT.get(), TFEntities.DEATH_TOME.get(), TFEntities.DEER.get(), TFEntities.FIRE_BEETLE.get(), TFEntities.GIANT_MINER.get(), TFEntities.LOWER_GOBLIN_KNIGHT.get(), TFEntities.UPPER_GOBLIN_KNIGHT.get(), TFEntities.HARBINGER_CUBE.get(), TFEntities.HEDGE_SPIDER.get(), TFEntities.HELMET_CRAB.get(), TFEntities.HOSTILE_WOLF.get(), TFEntities.HYDRA.get(), TFEntities.KING_SPIDER.get(), TFEntities.KNIGHT_PHANTOM.get(), TFEntities.KOBOLD.get(), TFEntities.LICH.get(), TFEntities.LICH_MINION.get(), TFEntities.MAZE_SLIME.get(), TFEntities.CARMINITE_GHASTLING.get(), TFEntities.MINOSHROOM.get(), TFEntities.MINOTAUR.get(), TFEntities.MIST_WOLF.get(), TFEntities.MOSQUITO_SWARM.get(), TFEntities.NAGA.get(), TFEntities.PENGUIN.get(), TFEntities.PINCH_BEETLE.get(), TFEntities.PLATEAU_BOSS.get(), TFEntities.QUEST_RAM.get(), TFEntities.RAVEN.get(), TFEntities.REDCAP.get(), TFEntities.REDCAP_SAPPER.get(), TFEntities.SKELETON_DRUID.get(), TFEntities.SLIME_BEETLE.get(), TFEntities.SNOW_GUARDIAN.get(), TFEntities.SNOW_QUEEN.get(), TFEntities.SQUIRREL.get(), TFEntities.STABLE_ICE_CORE.get(), TFEntities.SWARM_SPIDER.get(), TFEntities.TINY_BIRD.get(), TFEntities.CARMINITE_BROODLING.get(), TFEntities.CARMINITE_GHASTGUARD.get(), TFEntities.CARMINITE_GOLEM.get(), TFEntities.TOWERWOOD_BORER.get(), TFEntities.TROLL.get(), TFEntities.UNSTABLE_ICE_CORE.get(), TFEntities.UR_GHAST.get(), TFEntities.BOAR.get(), TFEntities.WINTER_WOLF.get(), TFEntities.WRAITH.get(), TFEntities.YETI.get(), TFEntities.ALPHA_YETI.get()});

	private static final Supplier<ItemLike[]> DENDROLOGIST_BLOCKS = Lazy.of(() -> new ItemLike[]{
		TFBlocks.TWILIGHT_OAK_LOG.get(), TFBlocks.TWILIGHT_OAK_WOOD.get(), TFBlocks.STRIPPED_TWILIGHT_OAK_LOG.get(), TFBlocks.STRIPPED_TWILIGHT_OAK_WOOD.get(), TFBlocks.HOLLOW_TWILIGHT_OAK_LOG_HORIZONTAL.get(), TFBlocks.TWILIGHT_OAK_LEAVES.get(), TFBlocks.TWILIGHT_OAK_SAPLING.get(), TFBlocks.TWILIGHT_OAK_PLANKS.get(), TFBlocks.TWILIGHT_OAK_SLAB.get(), TFBlocks.TWILIGHT_OAK_STAIRS.get(), TFBlocks.TWILIGHT_OAK_BUTTON.get(), TFBlocks.TWILIGHT_OAK_FENCE.get(), TFBlocks.TWILIGHT_OAK_GATE.get(), TFBlocks.TWILIGHT_OAK_PLATE.get(), TFBlocks.TWILIGHT_OAK_DOOR.get(), TFBlocks.TWILIGHT_OAK_TRAPDOOR.get(), TFBlocks.TWILIGHT_OAK_SIGN.get(), TFBlocks.TWILIGHT_OAK_HANGING_SIGN.get(), TFBlocks.TWILIGHT_OAK_CHEST.get(), TFBlocks.TWILIGHT_OAK_TRAPPED_CHEST.get(), TFBlocks.TWILIGHT_OAK_BANISTER.get(), TFItems.TWILIGHT_OAK_BOAT.get(), TFItems.TWILIGHT_OAK_CHEST_BOAT.get(),
		TFBlocks.CANOPY_LOG.get(), TFBlocks.CANOPY_WOOD.get(), TFBlocks.STRIPPED_CANOPY_LOG.get(), TFBlocks.STRIPPED_CANOPY_WOOD.get(), TFBlocks.HOLLOW_CANOPY_LOG_HORIZONTAL.get(), TFBlocks.CANOPY_LEAVES.get(), TFBlocks.CANOPY_SAPLING.get(), TFBlocks.CANOPY_PLANKS.get(), TFBlocks.CANOPY_SLAB.get(), TFBlocks.CANOPY_STAIRS.get(), TFBlocks.CANOPY_BUTTON.get(), TFBlocks.CANOPY_FENCE.get(), TFBlocks.CANOPY_GATE.get(), TFBlocks.CANOPY_PLATE.get(), TFBlocks.CANOPY_DOOR.get(), TFBlocks.CANOPY_TRAPDOOR.get(), TFBlocks.CANOPY_SIGN.get(), TFBlocks.CANOPY_HANGING_SIGN.get(), TFBlocks.CANOPY_CHEST.get(), TFBlocks.CANOPY_TRAPPED_CHEST.get(), TFBlocks.CANOPY_BANISTER.get(), TFBlocks.CANOPY_BOOKSHELF.get(), TFBlocks.CHISELED_CANOPY_BOOKSHELF.get(), TFItems.CANOPY_BOAT.get(), TFItems.CANOPY_CHEST_BOAT.get(),
		TFBlocks.MANGROVE_LOG.get(), TFBlocks.MANGROVE_WOOD.get(), TFBlocks.STRIPPED_MANGROVE_LOG.get(), TFBlocks.STRIPPED_MANGROVE_WOOD.get(), TFBlocks.HOLLOW_MANGROVE_LOG_HORIZONTAL.get(), TFBlocks.MANGROVE_LEAVES.get(), TFBlocks.MANGROVE_SAPLING.get(), TFBlocks.MANGROVE_PLANKS.get(), TFBlocks.MANGROVE_SLAB.get(), TFBlocks.MANGROVE_STAIRS.get(), TFBlocks.MANGROVE_BUTTON.get(), TFBlocks.MANGROVE_FENCE.get(), TFBlocks.MANGROVE_GATE.get(), TFBlocks.MANGROVE_PLATE.get(), TFBlocks.MANGROVE_DOOR.get(), TFBlocks.MANGROVE_TRAPDOOR.get(), TFBlocks.MANGROVE_SIGN.get(), TFBlocks.MANGROVE_HANGING_SIGN.get(), TFBlocks.MANGROVE_CHEST.get(), TFBlocks.MANGROVE_TRAPPED_CHEST.get(), TFBlocks.MANGROVE_BANISTER.get(), TFItems.MANGROVE_BOAT.get(), TFItems.MANGROVE_CHEST_BOAT.get(),
		TFBlocks.DARK_LOG.get(), TFBlocks.DARK_WOOD.get(), TFBlocks.STRIPPED_DARK_LOG.get(), TFBlocks.STRIPPED_DARK_WOOD.get(), TFBlocks.HOLLOW_DARK_LOG_HORIZONTAL.get(), TFBlocks.DARK_LEAVES.get(), TFBlocks.DARKWOOD_SAPLING.get(), TFBlocks.DARK_PLANKS.get(), TFBlocks.DARK_SLAB.get(), TFBlocks.DARK_STAIRS.get(), TFBlocks.DARK_BUTTON.get(), TFBlocks.DARK_FENCE.get(), TFBlocks.DARK_GATE.get(), TFBlocks.DARK_PLATE.get(), TFBlocks.DARK_DOOR.get(), TFBlocks.DARK_TRAPDOOR.get(), TFBlocks.DARK_SIGN.get(), TFBlocks.DARK_HANGING_SIGN.get(), TFBlocks.DARK_CHEST.get(), TFBlocks.DARK_TRAPPED_CHEST.get(), TFBlocks.DARK_BANISTER.get(), TFItems.DARK_BOAT.get(), TFItems.DARK_CHEST_BOAT.get(),
		TFBlocks.TIME_LOG.get(), TFBlocks.TIME_WOOD.get(), TFBlocks.STRIPPED_TIME_LOG.get(), TFBlocks.STRIPPED_TIME_WOOD.get(), TFBlocks.HOLLOW_TIME_LOG_HORIZONTAL.get(), TFBlocks.TIME_LEAVES.get(), TFBlocks.TIME_SAPLING.get(), TFBlocks.TIME_PLANKS.get(), TFBlocks.TIME_SLAB.get(), TFBlocks.TIME_STAIRS.get(), TFBlocks.TIME_BUTTON.get(), TFBlocks.TIME_FENCE.get(), TFBlocks.TIME_GATE.get(), TFBlocks.TIME_PLATE.get(), TFBlocks.TIME_DOOR.get(), TFBlocks.TIME_TRAPDOOR.get(), TFBlocks.TIME_SIGN.get(), TFBlocks.TIME_HANGING_SIGN.get(), TFBlocks.TIME_CHEST.get(), TFBlocks.TIME_TRAPPED_CHEST.get(), TFBlocks.TIME_BANISTER.get(), TFItems.TIME_BOAT.get(), TFItems.TIME_CHEST_BOAT.get(),
		TFBlocks.TRANSFORMATION_LOG.get(), TFBlocks.TRANSFORMATION_WOOD.get(), TFBlocks.STRIPPED_TRANSFORMATION_LOG.get(), TFBlocks.STRIPPED_TRANSFORMATION_WOOD.get(), TFBlocks.HOLLOW_TRANSFORMATION_LOG_HORIZONTAL.get(), TFBlocks.TRANSFORMATION_LEAVES.get(), TFBlocks.TRANSFORMATION_SAPLING.get(), TFBlocks.TRANSFORMATION_PLANKS.get(), TFBlocks.TRANSFORMATION_SLAB.get(), TFBlocks.TRANSFORMATION_STAIRS.get(), TFBlocks.TRANSFORMATION_BUTTON.get(), TFBlocks.TRANSFORMATION_FENCE.get(), TFBlocks.TRANSFORMATION_GATE.get(), TFBlocks.TRANSFORMATION_PLATE.get(), TFBlocks.TRANSFORMATION_DOOR.get(), TFBlocks.TRANSFORMATION_TRAPDOOR.get(), TFBlocks.TRANSFORMATION_SIGN.get(), TFBlocks.TRANSFORMATION_HANGING_SIGN.get(), TFBlocks.TRANSFORMATION_CHEST.get(), TFBlocks.TRANSFORMATION_TRAPPED_CHEST.get(), TFBlocks.TRANSFORMATION_BANISTER.get(), TFItems.TRANSFORMATION_BOAT.get(), TFItems.TRANSFORMATION_CHEST_BOAT.get(),
		TFBlocks.MINING_LOG.get(), TFBlocks.MINING_WOOD.get(), TFBlocks.STRIPPED_MINING_LOG.get(), TFBlocks.STRIPPED_MINING_WOOD.get(), TFBlocks.HOLLOW_MINING_LOG_HORIZONTAL.get(), TFBlocks.MINING_LEAVES.get(), TFBlocks.MINING_SAPLING.get(), TFBlocks.MINING_PLANKS.get(), TFBlocks.MINING_SLAB.get(), TFBlocks.MINING_STAIRS.get(), TFBlocks.MINING_BUTTON.get(), TFBlocks.MINING_FENCE.get(), TFBlocks.MINING_GATE.get(), TFBlocks.MINING_PLATE.get(), TFBlocks.MINING_DOOR.get(), TFBlocks.MINING_TRAPDOOR.get(), TFBlocks.MINING_SIGN.get(), TFBlocks.MINING_HANGING_SIGN.get(), TFBlocks.MINING_CHEST.get(), TFBlocks.MINING_TRAPPED_CHEST.get(), TFBlocks.MINING_BANISTER.get(), TFItems.MINING_BOAT.get(), TFItems.MINING_CHEST_BOAT.get(),
		TFBlocks.SORTING_LOG.get(), TFBlocks.SORTING_WOOD.get(), TFBlocks.STRIPPED_SORTING_LOG.get(), TFBlocks.STRIPPED_SORTING_WOOD.get(), TFBlocks.HOLLOW_SORTING_LOG_HORIZONTAL.get(), TFBlocks.SORTING_LEAVES.get(), TFBlocks.SORTING_SAPLING.get(), TFBlocks.SORTING_PLANKS.get(), TFBlocks.SORTING_SLAB.get(), TFBlocks.SORTING_STAIRS.get(), TFBlocks.SORTING_BUTTON.get(), TFBlocks.SORTING_FENCE.get(), TFBlocks.SORTING_GATE.get(), TFBlocks.SORTING_PLATE.get(), TFBlocks.SORTING_DOOR.get(), TFBlocks.SORTING_TRAPDOOR.get(), TFBlocks.SORTING_SIGN.get(), TFBlocks.SORTING_HANGING_SIGN.get(), TFBlocks.SORTING_CHEST.get(), TFBlocks.SORTING_TRAPPED_CHEST.get(), TFBlocks.SORTING_BANISTER.get(), TFItems.SORTING_BOAT.get(), TFItems.SORTING_CHEST_BOAT.get(),
		TFBlocks.TOWERWOOD.get(), TFBlocks.CRACKED_TOWERWOOD.get(), TFBlocks.MOSSY_TOWERWOOD.get(), TFBlocks.ENCASED_TOWERWOOD.get(),
		TFBlocks.ROOT_BLOCK.get(), TFBlocks.ROOT_STRAND.get(), TFBlocks.LIVEROOT_BLOCK.get(), TFItems.LIVEROOT.get(), TFBlocks.HOLLOW_OAK_SAPLING.get(), TFBlocks.RAINBOW_OAK_SAPLING.get(), TFBlocks.RAINBOW_OAK_LEAVES.get(), TFBlocks.GIANT_LOG.get(), TFBlocks.GIANT_LEAVES.get(), TFBlocks.HUGE_STALK.get(), TFBlocks.BEANSTALK_LEAVES.get(), TFBlocks.THORN_LEAVES.get(), TFBlocks.THORN_ROSE.get(), TFBlocks.HEDGE.get(), TFBlocks.FALLEN_LEAVES.get(), TFBlocks.MANGROVE_ROOT.get(),
	});

	@Autowired
	private static AdvancementDataMultiRequirements advancementDataMultiRequirements;

	@Autowired
	private static DrinkFromFlaskTrigger.TriggerInstance.DrinkFromFlaskTriggerInstanceFactory drinkFromFlaskTriggerInstanceFactory;

	@Override
	public void generate(HolderLookup.Provider registries, Consumer<AdvancementHolder> consumer, ExistingFileHelper helper) {
		HolderLookup.RegistryLookup<Biome> biomes = registries.lookupOrThrow(Registries.BIOME);
		HolderLookup.RegistryLookup<Structure> structures = registries.lookupOrThrow(Registries.STRUCTURE);

		AdvancementHolder root = Advancement.Builder.advancement().display(
				TFBlocks.TWILIGHT_PORTAL_MINIATURE_STRUCTURE.get(),
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

		AdvancementHolder silence = this.addTFKillable(Advancement.Builder.advancement().parent(root).display(
					TFItems.RAVEN_FEATHER.get(),
					Component.translatable("advancement.twilightforest.twilight_hunter"),
					Component.translatable("advancement.twilightforest.twilight_hunter.desc"),
					null, AdvancementType.TASK, true, true, false)
				.requirements(AdvancementRequirements.Strategy.OR))
			.save(consumer, "twilightforest:twilight_hunter");

		AdvancementHolder naga = Advancement.Builder.advancement().parent(root).display(
				TFBlocks.NAGA_COURTYARD_MINIATURE_STRUCTURE.get(),
				Component.translatable("advancement.twilightforest.kill_naga"),
				Component.translatable("advancement.twilightforest.kill_naga.desc",
					Component.translatable(TFEntities.NAGA.get().getDescriptionId()),
					Component.translatable(TFItems.NAGA_SCALE.get().getDescriptionId())),
				null, AdvancementType.GOAL, true, true, false)
			.addCriterion("naga", KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of(TFEntities.NAGA.get())))
			.addCriterion("trophy", InventoryChangeTrigger.TriggerInstance.hasItems(TFBlocks.NAGA_TROPHY.get()))
			.addCriterion("scale", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.NAGA_SCALE.get()))
			.addCriterion("was_in_fight", HurtBossTrigger.TriggerInstance.hurtBoss(EntityPredicate.Builder.entity().of(TFEntities.NAGA.get())))
			.requirements(AdvancementRequirements.Strategy.OR)
			.save(consumer, "twilightforest:progress_naga");

		AdvancementHolder lich = advancementDataMultiRequirements.wrap(Advancement.Builder.advancement().parent(naga).display(
				TFBlocks.LICH_TOWER_MINIATURE_STRUCTURE.get(),
				Component.translatable("advancement.twilightforest.kill_lich"),
				Component.translatable("advancement.twilightforest.kill_lich.desc",
					Component.translatable(TFEntities.LICH.get().getDescriptionId())),
				null, AdvancementType.GOAL, true, true, false))
			.addCriterion("kill_lich", KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of(TFEntities.LICH.get())))
			.addCriterion("trophy", InventoryChangeTrigger.TriggerInstance.hasItems(TFBlocks.LICH_TROPHY.get()))
			.addCriterion("lifedrain_scepter", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.LIFEDRAIN_SCEPTER.get()))
			.addCriterion("twilight_scepter", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.TWILIGHT_SCEPTER.get()))
			.addCriterion("zombie_scepter", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.ZOMBIE_SCEPTER.get()))
			.addCriterion("shield_scepter", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.FORTIFICATION_SCEPTER.get()))
			.addCriterion("was_in_fight", HurtBossTrigger.TriggerInstance.hurtBoss(EntityPredicate.Builder.entity().of(TFEntities.LICH.get())))
			.and()
			.addCriterion("kill_naga", this.advancementTrigger(naga))
			.requirements()
			.save(consumer, "twilightforest:progress_lich");

		AdvancementHolder minoshroom = Advancement.Builder.advancement().parent(lich).display(
				TFItems.MEEF_STROGANOFF.get(),
				Component.translatable("advancement.twilightforest.progress_labyrinth"),
				Component.translatable("advancement.twilightforest.progress_labyrinth.desc"),
				null, AdvancementType.GOAL, true, true, false)
			.addCriterion("meef", ConsumeItemTrigger.TriggerInstance.usedItem(TFItems.MEEF_STROGANOFF.get()))
			.addCriterion("kill_lich", this.advancementTrigger(lich))
			.requirements(AdvancementRequirements.Strategy.AND)
			.save(consumer, "twilightforest:progress_labyrinth");

		AdvancementHolder hydra = advancementDataMultiRequirements.wrap(Advancement.Builder.advancement().parent(minoshroom).display(
				TFBlocks.HYDRA_TROPHY.get(),
				Component.translatable("advancement.twilightforest.kill_hydra"),
				Component.translatable("advancement.twilightforest.kill_hydra.desc",
					Component.translatable(TFEntities.HYDRA.get().getDescriptionId())),
				null, AdvancementType.GOAL, true, true, false))
			.addCriterion("kill_hydra", KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of(TFEntities.HYDRA.get())))
			.addCriterion("trophy", InventoryChangeTrigger.TriggerInstance.hasItems(TFBlocks.HYDRA_TROPHY.get()))
			.addCriterion("was_in_fight", HurtBossTrigger.TriggerInstance.hurtBoss(EntityPredicate.Builder.entity().of(TFEntities.HYDRA.get())))
			.and()
			.addCriterion("stroganoff", this.advancementTrigger(minoshroom))
			.requirements()
			.save(consumer, "twilightforest:progress_hydra");

		AdvancementHolder trophy_pedestal = Advancement.Builder.advancement().parent(lich).display(
				TFBlocks.TROPHY_PEDESTAL.get(),
				Component.translatable("advancement.twilightforest.progress_trophy_pedestal"),
				Component.translatable("advancement.twilightforest.progress_trophy_pedestal.desc"),
				null, AdvancementType.GOAL, true, true, false)
			.addCriterion("trophy_pedestal", SimpleAdvancementTrigger.TriggerInstance.activatedPedestal())
			.addCriterion("kill_lich", this.advancementTrigger(lich))
			.requirements(AdvancementRequirements.Strategy.AND)
			.save(consumer, "twilightforest:progress_trophy_pedestal");

		AdvancementHolder knights = advancementDataMultiRequirements.wrap(Advancement.Builder.advancement().parent(trophy_pedestal).display(
				TFBlocks.KNIGHT_PHANTOM_TROPHY.get(),
				Component.translatable("advancement.twilightforest.progress_knights"),
				Component.translatable("advancement.twilightforest.progress_knights.desc"),
				null, AdvancementType.GOAL, true, true, false))
			.addCriterion("all_knights", SimpleAdvancementTrigger.TriggerInstance.killAllPhantoms())
			.addCriterion("trophy", InventoryChangeTrigger.TriggerInstance.hasItems(TFBlocks.KNIGHT_PHANTOM_TROPHY.get()))
			.and()
			.addCriterion("previous_progression", this.advancementTrigger(trophy_pedestal))
			.requirements()
			.save(consumer, "twilightforest:progress_knights");

		AdvancementHolder trap = Advancement.Builder.advancement().parent(knights).display(
				TFBlocks.GHAST_TRAP.get(),
				Component.translatable("advancement.twilightforest.ghast_trap"),
				Component.translatable("advancement.twilightforest.ghast_trap.desc",
					Component.translatable(TFEntities.CARMINITE_GHASTLING.get().getDescriptionId()),
					Component.translatable(TFBlocks.GHAST_TRAP.get().getDescriptionId()),
					Component.translatable(TFEntities.UR_GHAST.get().getDescriptionId())),
				null, AdvancementType.TASK, true, true, false)
			.addCriterion("activate_ghast_trap", SimpleAdvancementTrigger.TriggerInstance.activateGhastTrap())
			.save(consumer, "twilightforest:ghast_trap");

		AdvancementHolder ur_ghast = advancementDataMultiRequirements.wrap(Advancement.Builder.advancement().parent(trap).display(
				TFBlocks.UR_GHAST_TROPHY.get(),
				Component.translatable("advancement.twilightforest.progress_ur_ghast"),
				Component.translatable("advancement.twilightforest.progress_ur_ghast.desc",
					Component.translatable(TFEntities.UR_GHAST.get().getDescriptionId())),
				null, AdvancementType.GOAL, true, true, false))
			.addCriterion("ghast", KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of(TFEntities.UR_GHAST.get())))
			.addCriterion("trophy", InventoryChangeTrigger.TriggerInstance.hasItems(TFBlocks.UR_GHAST_TROPHY.get()))
			.addCriterion("was_in_fight", HurtBossTrigger.TriggerInstance.hurtBoss(EntityPredicate.Builder.entity().of(TFEntities.UR_GHAST.get())))
			.and()
			.addCriterion("previous_progression", this.advancementTrigger(knights))
			.requirements()
			.save(consumer, "twilightforest:progress_ur_ghast");

		AdvancementHolder yeti = advancementDataMultiRequirements.wrap(Advancement.Builder.advancement().parent(lich).display(
				TFItems.ALPHA_YETI_FUR.get(),
				Component.translatable("advancement.twilightforest.progress_yeti"),
				Component.translatable("advancement.twilightforest.progress_yeti.desc",
					Component.translatable(TFEntities.ALPHA_YETI.get().getDescriptionId())),
				null, AdvancementType.GOAL, true, true, false))
			.addCriterion("yeti", KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of(TFEntities.ALPHA_YETI.get())))
			.addCriterion("trophy", InventoryChangeTrigger.TriggerInstance.hasItems(TFBlocks.ALPHA_YETI_TROPHY.get()))
			.addCriterion("fur", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.ALPHA_YETI_FUR.get()))
			.addCriterion("was_in_fight", HurtBossTrigger.TriggerInstance.hurtBoss(EntityPredicate.Builder.entity().of(TFEntities.ALPHA_YETI.get())))
			.and()
			.addCriterion("previous_progression", this.advancementTrigger(lich))
			.requirements()
			.save(consumer, "twilightforest:progress_yeti");

		AdvancementHolder snow_queen = advancementDataMultiRequirements.wrap(Advancement.Builder.advancement().parent(yeti).display(
				TFBlocks.SNOW_QUEEN_TROPHY.get(),
				Component.translatable("advancement.twilightforest.progress_glacier"),
				Component.translatable("advancement.twilightforest.progress_glacier.desc",
					Component.translatable(TFEntities.SNOW_QUEEN.get().getDescriptionId())),
				null, AdvancementType.GOAL, true, true, false))
			.addCriterion("queen", KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of(TFEntities.SNOW_QUEEN.get())))
			.addCriterion("trophy", InventoryChangeTrigger.TriggerInstance.hasItems(TFBlocks.SNOW_QUEEN_TROPHY.get()))
			.addCriterion("was_in_fight", HurtBossTrigger.TriggerInstance.hurtBoss(EntityPredicate.Builder.entity().of(TFEntities.SNOW_QUEEN.get())))
			.and()
			.addCriterion("previous_progression", this.advancementTrigger(yeti))
			.requirements()
			.save(consumer, "twilightforest:progress_glacier");

		AdvancementHolder merge = Advancement.Builder.advancement().parent(lich).display(
				TFBlocks.UBEROUS_SOIL.get(),
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
				TFItems.MAGIC_BEANS.get(),
				Component.translatable("advancement.twilightforest.troll"),
				Component.translatable("advancement.twilightforest.troll.desc",
					Component.translatable(TFEntities.TROLL.get().getDescriptionId())),
				null, AdvancementType.TASK, true, true, false)
			.addCriterion("troll", KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of(TFEntities.TROLL.get()).located(LocationPredicate.Builder.inStructure(structures.getOrThrow(TFStructures.TROLL_CAVE)))))
			.save(consumer, "twilightforest:troll");

		AdvancementHolder beanstalk = Advancement.Builder.advancement().parent(merge).display(
				TFBlocks.HUGE_STALK.get(),
				Component.translatable("advancement.twilightforest.beanstalk"),
				Component.translatable("advancement.twilightforest.beanstalk.desc",
					Component.translatable(TFItems.MAGIC_BEANS.get().getDescriptionId())),
				null, AdvancementType.GOAL, true, true, false)
			.addCriterion("beans", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.MAGIC_BEANS.get()))
			.addCriterion("use_beans", ItemUsedOnLocationTrigger.TriggerInstance.itemUsedOnBlock(LocationPredicate.Builder.location().setBlock(BlockPredicate.Builder.block().of(TFBlocks.UBEROUS_SOIL.get())), ItemPredicate.Builder.item().of(TFItems.MAGIC_BEANS.get())))
			.save(consumer, "twilightforest:beanstalk");

		AdvancementHolder giants = Advancement.Builder.advancement().parent(beanstalk).display(
				TFItems.GIANT_PICKAXE.get(),
				Component.translatable("advancement.twilightforest.giants"),
				Component.translatable("advancement.twilightforest.giants.desc",
					Component.translatable(TFEntities.GIANT_MINER.get().getDescriptionId()),
					Component.translatable(TFItems.GIANT_PICKAXE.get().getDescriptionId())),
				null, AdvancementType.GOAL, true, true, false)
			.addCriterion("giant", KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of(TFEntities.GIANT_MINER.get())))
			.addCriterion("pickaxe", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.MAGIC_BEANS.get()))
			.save(consumer, "twilightforest:giants");

		AdvancementHolder lamp = Advancement.Builder.advancement().parent(giants).display(
				TFItems.LAMP_OF_CINDERS.get(),
				Component.translatable("advancement.twilightforest.progress_troll"),
				Component.translatable("advancement.twilightforest.progress_troll.desc",
					Component.translatable(TFItems.LAMP_OF_CINDERS.get().getDescriptionId())),
				null, AdvancementType.GOAL, true, true, false)
			.addCriterion("lamp", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.LAMP_OF_CINDERS.get()))
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
//						TFBlocks.BROWN_THORNS.get(),
//						Component.translatable("advancement.twilightforest.progress_thorns"),
//						Component.translatable("advancement.twilightforest.progress_thorns.desc"),
//						null, AdvancementType.GOAL, true, true, false)
//				.addCriterion("castle", PlayerTrigger.TriggerInstance.located(LocationPredicate.inBiome(TFBiomes.FINAL_PLATEAU)))
//				.addCriterion("previous_progression", this.advancementTrigger(lamp))
//				.save(consumer, "twilightforest:progress_thorns");
//
//		Advancement.Builder.advancement().parent(thornlands).display(
//						TFBlocks.VIOLET_CASTLE_RUNE_BRICK.get(),
//						Component.translatable("advancement.twilightforest.progress_castle"),
//						Component.translatable("advancement.twilightforest.progress_castle.desc"),
//						null, AdvancementType.GOAL, true, true, false)
//				.addCriterion("castle", PlayerTrigger.TriggerInstance.located(LocationPredicate.inStructure(TFStructures.FINAL_CASTLE)))
//				.addCriterion("previous_progression", this.advancementTrigger(thornlands))
//				.save(consumer, "twilightforest:progress_castle");

		Advancement.Builder.advancement().parent(root).display(
				TFBlocks.QUEST_RAM_TROPHY.get(),
				Component.translatable("advancement.twilightforest.quest_ram"),
				Component.translatable("advancement.twilightforest.quest_ram.desc",
					Component.translatable(TFEntities.QUEST_RAM.get().getDescriptionId())),
				null, AdvancementType.GOAL, true, true, false)
			.addCriterion("quest_ram_complete", SimpleAdvancementTrigger.TriggerInstance.completeQuestRam())
			.rewards(AdvancementRewards.Builder.experience(100))
			.save(consumer, "twilightforest:quest_ram");

		Advancement.Builder.advancement().parent(root).display(
				TFBlocks.CICADA.get(),
				Component.translatable("advancement.twilightforest.kill_cicada"),
				Component.translatable("advancement.twilightforest.kill_cicada.desc"),
				null, AdvancementType.TASK, true, false, true)
			.addCriterion("kill_cicada", KillBugTrigger.TriggerInstance.killBug(TFBlocks.CICADA.get()))
			.save(consumer, "twilightforest:kill_cicada");

		Advancement.Builder.advancement().parent(root).display(
				TFBlocks.UNCRAFTING_TABLE.get(),
				Component.translatable("advancement.twilightforest.uncraft_uncrafting_table"),
				Component.translatable("advancement.twilightforest.uncraft_uncrafting_table.desc"),
				null, AdvancementType.TASK, true, true, true)
			.addCriterion("uncraft_table", UncraftItemTrigger.TriggerInstance.uncraftedItem(TFBlocks.UNCRAFTING_TABLE.get()))
			.save(consumer, "twilightforest:uncraft_uncrafting_table");

		AdvancementHolder focus = Advancement.Builder.advancement().parent(silence).display(
				TFItems.MAGIC_MAP_FOCUS.get(),
				Component.translatable("advancement.twilightforest.magic_map_focus"),
				Component.translatable("advancement.twilightforest.magic_map_focus.desc",
					Component.translatable(TFItems.MAGIC_MAP_FOCUS.get().getDescriptionId()),
					Component.translatable(TFItems.RAVEN_FEATHER.get().getDescriptionId()),
					Component.translatable(Items.GLOWSTONE_DUST.getDescriptionId()),
					Component.translatable(TFItems.TORCHBERRIES.get().getDescriptionId())),
				null, AdvancementType.TASK, true, true, false)
			.addCriterion("focus", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.MAGIC_MAP_FOCUS.get()))
			.save(consumer, "twilightforest:magic_map_focus");

		AdvancementHolder magic_map = Advancement.Builder.advancement().parent(focus).display(
				TFItems.FILLED_MAGIC_MAP.get(),
				Component.translatable("advancement.twilightforest.magic_map"),
				Component.translatable("advancement.twilightforest.magic_map.desc",
					Component.translatable(TFItems.FILLED_MAGIC_MAP.get().getDescriptionId())),
				null, AdvancementType.TASK, true, true, false)
			.addCriterion("magic_map", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.FILLED_MAGIC_MAP.get()))
			.save(consumer, "twilightforest:magic_map");

		AdvancementHolder maze_map = Advancement.Builder.advancement().parent(magic_map).display(
				TFItems.FILLED_MAZE_MAP.get(),
				Component.translatable("advancement.twilightforest.maze_map"),
				Component.translatable("advancement.twilightforest.maze_map.desc",
					Component.translatable(TFItems.FILLED_MAZE_MAP.get().getDescriptionId())),
				null, AdvancementType.GOAL, true, true, false)
			.addCriterion("maze_map", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.FILLED_MAZE_MAP.get()))
			.save(consumer, "twilightforest:maze_map");

		Advancement.Builder.advancement().parent(maze_map).display(
				TFItems.FILLED_ORE_MAP.get(),
				Component.translatable("advancement.twilightforest.ore_map"),
				Component.translatable("advancement.twilightforest.ore_map.desc",
					Component.translatable(TFItems.FILLED_ORE_MAP.get().getDescriptionId())),
				null, AdvancementType.CHALLENGE, true, true, true)
			.addCriterion("ore_map", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.FILLED_ORE_MAP.get()))
			.save(consumer, "twilightforest:ore_map");

		AdvancementHolder hill1 = Advancement.Builder.advancement().parent(root).display(
				Items.IRON_BOOTS,
				Component.translatable("advancement.twilightforest.hill1"),
				Component.translatable("advancement.twilightforest.hill1.desc",
					Component.translatable(TFEntities.REDCAP.get().getDescriptionId())),
				null, AdvancementType.TASK, true, true, false)
			.addCriterion("redcap", KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of(TFEntities.REDCAP.get()).located(LocationPredicate.Builder.inStructure(structures.getOrThrow(TFStructures.HOLLOW_HILL_SMALL)))))
			.save(consumer, "twilightforest:hill1");

		AdvancementHolder hill2 = Advancement.Builder.advancement().parent(hill1).display(
				TFItems.IRONWOOD_PICKAXE.get(),
				Component.translatable("advancement.twilightforest.hill2"),
				Component.translatable("advancement.twilightforest.hill2.desc",
					Component.translatable(TFEntities.REDCAP_SAPPER.get().getDescriptionId())),
				null, AdvancementType.TASK, true, true, false)
			.addCriterion("redcap", KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of(TFEntities.REDCAP_SAPPER.get()).located(LocationPredicate.Builder.inStructure(structures.getOrThrow(TFStructures.HOLLOW_HILL_MEDIUM)))))
			.save(consumer, "twilightforest:hill2");

		Advancement.Builder.advancement().parent(hill2).display(
				Items.GLOWSTONE_DUST,
				Component.translatable("advancement.twilightforest.hill3"),
				Component.translatable("advancement.twilightforest.hill3.desc",
					Component.translatable(TFEntities.WRAITH.get().getDescriptionId())),
				null, AdvancementType.TASK, true, true, false)
			.addCriterion("redcap", KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of(TFEntities.WRAITH.get()).located(LocationPredicate.Builder.inStructure(structures.getOrThrow(TFStructures.HOLLOW_HILL_LARGE)))))
			.save(consumer, "twilightforest:hill3");

		Advancement.Builder.advancement().parent(root).display(
				TFBlocks.HEDGE.get(),
				Component.translatable("advancement.twilightforest.hedge"),
				Component.translatable("advancement.twilightforest.hedge.desc"),
				null, AdvancementType.TASK, true, true, false)
			.addCriterion("hedge_spider", KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of(TFEntities.HEDGE_SPIDER.get()).located(LocationPredicate.Builder.inStructure(structures.getOrThrow(TFStructures.HEDGE_MAZE)))))
			.addCriterion("swarm_spider", KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of(TFEntities.SWARM_SPIDER.get()).located(LocationPredicate.Builder.inStructure(structures.getOrThrow(TFStructures.HEDGE_MAZE)))))
			.requirements(AdvancementRequirements.Strategy.OR)
			.save(consumer, "twilightforest:hedge");

		advancementDataMultiRequirements.wrap(Advancement.Builder.advancement().parent(root).display(
				Items.BOWL,
				Component.translatable("advancement.twilightforest.twilight_dining"),
				Component.translatable("advancement.twilightforest.twilight_dining.desc"),
				null, AdvancementType.CHALLENGE, true, true, false))
			.addCriterion("raw_venison", ConsumeItemTrigger.TriggerInstance.usedItem(TFItems.RAW_VENISON.get()))
			.addCriterion("cooked_venison", ConsumeItemTrigger.TriggerInstance.usedItem(TFItems.COOKED_VENISON.get()))
			.and()
			.addCriterion("raw_meef", ConsumeItemTrigger.TriggerInstance.usedItem(TFItems.RAW_MEEF.get()))
			.addCriterion("cooked_meef", ConsumeItemTrigger.TriggerInstance.usedItem(TFItems.COOKED_MEEF.get()))
			.and()
			.addCriterion("meef_stroganoff", ConsumeItemTrigger.TriggerInstance.usedItem(TFItems.MEEF_STROGANOFF.get()))
			.and()
			.addCriterion("hydra_chop", ConsumeItemTrigger.TriggerInstance.usedItem(TFItems.HYDRA_CHOP.get()))
			.and()
			.addCriterion("maze_wafer", ConsumeItemTrigger.TriggerInstance.usedItem(TFItems.MAZE_WAFER.get()))
			.and()
			.addCriterion("experiment_115", ConsumeItemTrigger.TriggerInstance.usedItem(TFItems.EXPERIMENT_115.get()))
			.and()
			.addCriterion("torchberries", ConsumeItemTrigger.TriggerInstance.usedItem(TFItems.TORCHBERRIES.get()))
			.requirements()
			.rewards(AdvancementRewards.Builder.experience(75))
			.save(consumer, "twilightforest:twilight_dinner");

		Advancement.Builder.advancement().parent(naga).display(
				TFItems.NAGA_CHESTPLATE.get(),
				Component.translatable("advancement.twilightforest.naga_armors"),
				Component.translatable("advancement.twilightforest.naga_armors.desc",
					Component.translatable(TFItems.NAGA_SCALE.get().getDescriptionId())),
				null, AdvancementType.CHALLENGE, true, true, false)
			.addCriterion("armor", InventoryChangeTrigger.TriggerInstance.hasItems(
				TFItems.NAGA_CHESTPLATE.get(), TFItems.NAGA_LEGGINGS.get()))
			.rewards(AdvancementRewards.Builder.experience(25))
			.save(consumer, "twilightforest:naga_armors");

		Advancement.Builder.advancement().parent(lich).display(
				TFItems.ZOMBIE_SCEPTER.get(),
				Component.translatable("advancement.twilightforest.lich_scepters"),
				Component.translatable("advancement.twilightforest.lich_scepters.desc"),
				null, AdvancementType.CHALLENGE, true, true, true)
			.addCriterion("scepters", InventoryChangeTrigger.TriggerInstance.hasItems(
				TFItems.LIFEDRAIN_SCEPTER.get(), TFItems.TWILIGHT_SCEPTER.get(),
				TFItems.ZOMBIE_SCEPTER.get(), TFItems.FORTIFICATION_SCEPTER.get()))
			.rewards(AdvancementRewards.Builder.experience(100))
			.save(consumer, "twilightforest:lich_scepters");

		Advancement.Builder.advancement().parent(lich).display(
				flaskWithHarming(),
				Component.translatable("advancement.twilightforest.full_mettle_alchemist"),
				Component.translatable("advancement.twilightforest.full_mettle_alchemist.desc"),
				null, AdvancementType.CHALLENGE, true, true, true)
			.addCriterion("drink_4_harming", drinkFromFlaskTriggerInstanceFactory.drankPotion(3, MinMaxBounds.Ints.atMost(6), Potions.STRONG_HARMING))
			.rewards(AdvancementRewards.Builder.experience(100))
			.save(consumer, "twilightforest:full_mettle_alchemist");

		Advancement.Builder.advancement().parent(minoshroom).display(
				TFItems.MAZEBREAKER_PICKAXE.get(),
				Component.translatable("advancement.twilightforest.mazebreaker"),
				Component.translatable("advancement.twilightforest.mazebreaker.desc",
					Component.translatable(TFItems.MAZEBREAKER_PICKAXE.get().getDescriptionId())),
				null, AdvancementType.CHALLENGE, true, true, false)
			.addCriterion("pick", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.MAZEBREAKER_PICKAXE.get()))
			.rewards(AdvancementRewards.Builder.experience(50))
			.save(consumer, "twilightforest:mazebreaker");

		Advancement.Builder.advancement().parent(hydra).display(
				TFItems.HYDRA_CHOP.get(),
				Component.translatable("advancement.twilightforest.hydra_chop"),
				Component.translatable("advancement.twilightforest.hydra_chop.desc",
					Component.translatable(TFEntities.HYDRA.get().getDescriptionId())),
				null, AdvancementType.TASK, true, true, false)
			.addCriterion("hydra_chop", SimpleAdvancementTrigger.TriggerInstance.eatHydraChop())
			.save(consumer, "twilightforest:hydra_chop");

		advancementDataMultiRequirements.wrap(Advancement.Builder.advancement().parent(hydra).display(
				TFItems.FIERY_SWORD.get(),
				Component.translatable("advancement.twilightforest.fiery_set"),
				Component.translatable("advancement.twilightforest.fiery_set.desc"),
				null, AdvancementType.CHALLENGE, true, true, false))
			.addCriterion("fiery_pick", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.FIERY_PICKAXE.get()))
			.addCriterion("fiery_sword", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.FIERY_SWORD.get()))
			.and()
			.addCriterion("fiery_helmet", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.FIERY_HELMET.get()))
			.addCriterion("fiery_chestplate", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.FIERY_CHESTPLATE.get()))
			.addCriterion("fiery_leggings", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.FIERY_LEGGINGS.get()))
			.addCriterion("fiery_boots", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.FIERY_BOOTS.get()))
			.requirements()
			.rewards(AdvancementRewards.Builder.experience(75))
			.save(consumer, "twilightforest:fiery_set");

		AdvancementHolder e115 = Advancement.Builder.advancement().parent(knights).display(
				TFItems.EXPERIMENT_115.get(),
				Component.translatable("advancement.twilightforest.experiment_115"),
				Component.translatable("advancement.twilightforest.experiment_115.desc"),
				null, AdvancementType.TASK, true, true, false)
			.addCriterion("eat_experiment_115", ConsumeItemTrigger.TriggerInstance.usedItem(TFItems.EXPERIMENT_115.get()))
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
			.addCriterion("place_complete_e115", ItemUsedOnLocationTrigger.TriggerInstance.itemUsedOnBlock(LocationPredicate.Builder.location().setBlock(BlockPredicate.Builder.block().of(TFBlocks.EXPERIMENT_115.get()).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(Experiment115Block.REGENERATE, true))), ItemPredicate.Builder.item().of(Items.REDSTONE)))
			.save(consumer, "twilightforest:experiment_115_self_replenishing");

		Advancement.Builder.advancement().parent(yeti).display(
						TFItems.ARCTIC_CHESTPLATE.get(),
						Component.translatable("advancement.twilightforest.arctic_dyed"),
						Component.translatable("advancement.twilightforest.arctic_dyed.desc"),
						null, AdvancementType.TASK, true, true, false)
				.addCriterion("helmet", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(TFItems.ARCTIC_HELMET.get()).withSubPredicate(TFItemSubPredicates.COLOR.get(), ItemColorPredicate.anyColor()).build()))
				.addCriterion("chestplate", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(TFItems.ARCTIC_CHESTPLATE.get()).withSubPredicate(TFItemSubPredicates.COLOR.get(), ItemColorPredicate.anyColor()).build()))
				.addCriterion("leggings", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(TFItems.ARCTIC_LEGGINGS.get()).withSubPredicate(TFItemSubPredicates.COLOR.get(), ItemColorPredicate.anyColor()).build()))
				.addCriterion("boots", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(TFItems.ARCTIC_BOOTS.get()).withSubPredicate(TFItemSubPredicates.COLOR.get(), ItemColorPredicate.anyColor()).build()))
				.rewards(AdvancementRewards.Builder.experience(25))
				.save(consumer, "twilightforest:arctic_armor_dyed");

		Advancement.Builder.advancement().parent(yeti).display(
				TFItems.GLASS_SWORD.get(),
				Component.translatable("advancement.twilightforest.glass_sword"),
				Component.translatable("advancement.twilightforest.glass_sword.desc"),
				null, AdvancementType.CHALLENGE, true, true, true)
			.addCriterion("broken_sword", SimpleAdvancementTrigger.TriggerInstance.brokenSword())
			.rewards(AdvancementRewards.Builder.experience(42))
			.save(consumer, "twilightforest:break_glass_sword");

		this.addDendrologistBlock(Advancement.Builder.advancement().parent(root)
				.display(TFBlocks.TWILIGHT_OAK_FENCE.get(),
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
		ItemStack itemstack = new ItemStack(TFItems.GREATER_FLASK.get());
		itemstack.set(TFDataComponents.POTION_FLASK_CONTENTS, new PotionFlaskComponent(
			new PotionContents(Potions.STRONG_HARMING.getDelegate()),
			4,
			0,
			false
		));
		return itemstack;
	}

	//private CompoundTag arcticDye(Item item) {
	//	ItemStack itemstack = new ItemStack(item);
	//	CompoundTag compoundtag = itemstack.getOrCreateTagElement("display");
	//	CompoundTag color = itemstack.getOrCreateTagElement("hasColor");
	//	color.putBoolean("hasColor", true);
	//	compoundtag.put("display", color);
	//	return compoundtag;
	//}

	private Advancement.Builder addTFKillable(Advancement.Builder builder) {
		for (EntityType<?> entity : TF_KILLABLE.get()) {
			builder.addCriterion(EntityType.getKey(entity).getPath(),
				KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of(entity)
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
