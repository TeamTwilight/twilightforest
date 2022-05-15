package twilightforest.data;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.advancements.AdvancementProvider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;
import twilightforest.TFConfig;
import twilightforest.TwilightForestMod;
import twilightforest.advancements.*;
import twilightforest.block.Experiment115Block;
import twilightforest.block.TFBlocks;
import twilightforest.entity.TFEntities;
import twilightforest.item.TFItems;
import twilightforest.util.TFStats;
import twilightforest.world.registration.features.TFConfiguredStructures;

import java.util.function.Consumer;

public class AdvancementGenerator extends AdvancementProvider {

	private static final EntityType<?>[] TF_KILLABLE = new EntityType<?>[]{TFEntities.ADHERENT.get(), TFEntities.ARMORED_GIANT.get(), TFEntities.BIGHORN_SHEEP.get(), TFEntities.BLOCKCHAIN_GOBLIN.get(), TFEntities.DWARF_RABBIT.get(), TFEntities.DEATH_TOME.get(), TFEntities.DEER.get(), TFEntities.FIRE_BEETLE.get(), TFEntities.GIANT_MINER.get(), TFEntities.LOWER_GOBLIN_KNIGHT.get(), TFEntities.UPPER_GOBLIN_KNIGHT.get(), TFEntities.HARBINGER_CUBE.get(), TFEntities.HEDGE_SPIDER.get(), TFEntities.HELMET_CRAB.get(), TFEntities.HOSTILE_WOLF.get(), TFEntities.HYDRA.get(), TFEntities.KING_SPIDER.get(), TFEntities.KNIGHT_PHANTOM.get(), TFEntities.KOBOLD.get(), TFEntities.LICH.get(), TFEntities.LICH_MINION.get(), TFEntities.MAZE_SLIME.get(), TFEntities.CARMINITE_GHASTLING.get(), TFEntities.MINOSHROOM.get(), TFEntities.MINOTAUR.get(), TFEntities.MIST_WOLF.get(), TFEntities.MOSQUITO_SWARM.get(), TFEntities.NAGA.get(), TFEntities.PENGUIN.get(), TFEntities.PINCH_BEETLE.get(), TFEntities.PLATEAU_BOSS.get(), TFEntities.QUEST_RAM.get(), TFEntities.RAVEN.get(), TFEntities.REDCAP.get(), TFEntities.REDCAP_SAPPER.get(), TFEntities.SKELETON_DRUID.get(), TFEntities.SLIME_BEETLE.get(), TFEntities.SNOW_GUARDIAN.get(), TFEntities.SNOW_QUEEN.get(), TFEntities.SQUIRREL.get(), TFEntities.STABLE_ICE_CORE.get(), TFEntities.SWARM_SPIDER.get(), TFEntities.TINY_BIRD.get(), TFEntities.CARMINITE_BROODLING.get(), TFEntities.CARMINITE_GHASTGUARD.get(), TFEntities.CARMINITE_GOLEM.get(), TFEntities.TOWERWOOD_BORER.get(), TFEntities.TROLL.get(), TFEntities.UNSTABLE_ICE_CORE.get(), TFEntities.UR_GHAST.get(), TFEntities.BOAR.get(), TFEntities.WINTER_WOLF.get(), TFEntities.WRAITH.get(), TFEntities.YETI.get(), TFEntities.ALPHA_YETI.get()};
	//man this is a pain
	private static final Block[] DENDROLOGIST_BLOCKS = new Block[]{
			TFBlocks.TWILIGHT_OAK_LOG.get(), TFBlocks.TWILIGHT_OAK_WOOD.get(), TFBlocks.STRIPPED_TWILIGHT_OAK_LOG.get(), TFBlocks.STRIPPED_TWILIGHT_OAK_WOOD.get(), TFBlocks.TWILIGHT_OAK_LEAVES.get(), TFBlocks.TWILIGHT_OAK_SAPLING.get(), TFBlocks.TWILIGHT_OAK_PLANKS.get(), TFBlocks.TWILIGHT_OAK_SLAB.get(), TFBlocks.TWILIGHT_OAK_STAIRS.get(), TFBlocks.TWILIGHT_OAK_BUTTON.get(), TFBlocks.TWILIGHT_OAK_FENCE.get(), TFBlocks.TWILIGHT_OAK_GATE.get(), TFBlocks.TWILIGHT_OAK_PLATE.get(), TFBlocks.TWILIGHT_OAK_DOOR.get(), TFBlocks.TWILIGHT_OAK_TRAPDOOR.get(), TFBlocks.TWILIGHT_OAK_SIGN.get(),
			TFBlocks.CANOPY_LOG.get(), TFBlocks.CANOPY_WOOD.get(), TFBlocks.STRIPPED_CANOPY_LOG.get(), TFBlocks.STRIPPED_CANOPY_WOOD.get(), TFBlocks.CANOPY_LEAVES.get(), TFBlocks.CANOPY_SAPLING.get(), TFBlocks.CANOPY_PLANKS.get(), TFBlocks.CANOPY_SLAB.get(), TFBlocks.CANOPY_STAIRS.get(), TFBlocks.CANOPY_BUTTON.get(), TFBlocks.CANOPY_FENCE.get(), TFBlocks.CANOPY_GATE.get(), TFBlocks.CANOPY_PLATE.get(), TFBlocks.CANOPY_DOOR.get(), TFBlocks.CANOPY_TRAPDOOR.get(), TFBlocks.CANOPY_SIGN.get(),
			TFBlocks.MANGROVE_LOG.get(), TFBlocks.MANGROVE_WOOD.get(), TFBlocks.STRIPPED_MANGROVE_LOG.get(), TFBlocks.STRIPPED_MANGROVE_WOOD.get(), TFBlocks.MANGROVE_LEAVES.get(), TFBlocks.MANGROVE_SAPLING.get(), TFBlocks.MANGROVE_PLANKS.get(), TFBlocks.MANGROVE_SLAB.get(), TFBlocks.MANGROVE_STAIRS.get(), TFBlocks.MANGROVE_BUTTON.get(), TFBlocks.MANGROVE_FENCE.get(), TFBlocks.MANGROVE_GATE.get(), TFBlocks.MANGROVE_PLATE.get(), TFBlocks.MANGROVE_DOOR.get(), TFBlocks.MANGROVE_TRAPDOOR.get(), TFBlocks.MANGROVE_SIGN.get(),
			TFBlocks.DARK_LOG.get(), TFBlocks.DARK_WOOD.get(), TFBlocks.STRIPPED_DARK_LOG.get(), TFBlocks.STRIPPED_DARK_WOOD.get(), TFBlocks.DARK_LEAVES.get(), TFBlocks.DARKWOOD_SAPLING.get(), TFBlocks.DARK_PLANKS.get(), TFBlocks.DARK_SLAB.get(), TFBlocks.DARK_STAIRS.get(), TFBlocks.DARK_BUTTON.get(), TFBlocks.DARK_FENCE.get(), TFBlocks.DARK_GATE.get(), TFBlocks.DARK_PLATE.get(), TFBlocks.DARK_DOOR.get(), TFBlocks.DARK_TRAPDOOR.get(), TFBlocks.DARKWOOD_SIGN.get(),
			TFBlocks.TIME_LOG.get(), TFBlocks.TIME_WOOD.get(), TFBlocks.STRIPPED_TIME_LOG.get(), TFBlocks.STRIPPED_TIME_WOOD.get(), TFBlocks.TIME_LEAVES.get(), TFBlocks.TIME_SAPLING.get(), TFBlocks.TIME_PLANKS.get(), TFBlocks.TIME_SLAB.get(), TFBlocks.TIME_STAIRS.get(), TFBlocks.TIME_BUTTON.get(), TFBlocks.TIME_FENCE.get(), TFBlocks.TIME_GATE.get(), TFBlocks.TIME_PLATE.get(), TFBlocks.TIME_DOOR.get(), TFBlocks.TIME_TRAPDOOR.get(), TFBlocks.TIME_SIGN.get(),
			TFBlocks.TRANSFORMATION_LOG.get(), TFBlocks.TRANSFORMATION_WOOD.get(), TFBlocks.STRIPPED_TRANSFORMATION_LOG.get(), TFBlocks.STRIPPED_TRANSFORMATION_WOOD.get(), TFBlocks.TRANSFORMATION_LEAVES.get(), TFBlocks.TRANSFORMATION_SAPLING.get(), TFBlocks.TRANSFORMATION_PLANKS.get(), TFBlocks.TRANSFORMATION_SLAB.get(), TFBlocks.TRANSFORMATION_STAIRS.get(), TFBlocks.TRANSFORMATION_BUTTON.get(), TFBlocks.TRANSFORMATION_FENCE.get(), TFBlocks.TRANSFORMATION_GATE.get(), TFBlocks.TRANSFORMATION_PLATE.get(), TFBlocks.TRANSFORMATION_DOOR.get(), TFBlocks.TRANSFORMATION_TRAPDOOR.get(), TFBlocks.TRANSFORMATION_SIGN.get(),
			TFBlocks.MINING_LOG.get(), TFBlocks.MINING_WOOD.get(), TFBlocks.STRIPPED_MINING_LOG.get(), TFBlocks.STRIPPED_MINING_WOOD.get(), TFBlocks.MINING_LEAVES.get(), TFBlocks.MINING_SAPLING.get(), TFBlocks.MINING_PLANKS.get(), TFBlocks.MINING_SLAB.get(), TFBlocks.MINING_STAIRS.get(), TFBlocks.MINING_BUTTON.get(), TFBlocks.MINING_FENCE.get(), TFBlocks.MINING_GATE.get(), TFBlocks.MINING_PLATE.get(), TFBlocks.MINING_DOOR.get(), TFBlocks.MINING_TRAPDOOR.get(), TFBlocks.MINING_SIGN.get(),
			TFBlocks.SORTING_LOG.get(), TFBlocks.SORTING_WOOD.get(), TFBlocks.STRIPPED_SORTING_LOG.get(), TFBlocks.STRIPPED_SORTING_WOOD.get(), TFBlocks.SORTING_LEAVES.get(), TFBlocks.SORTING_SAPLING.get(), TFBlocks.SORTING_PLANKS.get(), TFBlocks.SORTING_SLAB.get(), TFBlocks.SORTING_STAIRS.get(), TFBlocks.SORTING_BUTTON.get(), TFBlocks.SORTING_FENCE.get(), TFBlocks.SORTING_GATE.get(), TFBlocks.SORTING_PLATE.get(), TFBlocks.SORTING_DOOR.get(), TFBlocks.SORTING_TRAPDOOR.get(), TFBlocks.SORTING_SIGN.get(),
			TFBlocks.ROOT_BLOCK.get(), TFBlocks.ROOT_STRAND.get(), TFBlocks.LIVEROOT_BLOCK.get(), TFBlocks.HOLLOW_OAK_SAPLING.get(), TFBlocks.RAINBOW_OAK_SAPLING.get(), TFBlocks.RAINBOW_OAK_LEAVES.get(), TFBlocks.TOWERWOOD.get(), TFBlocks.GIANT_LOG.get(), TFBlocks.GIANT_LEAVES.get(), TFBlocks.HUGE_STALK.get(), TFBlocks.BEANSTALK_LEAVES.get(), TFBlocks.THORN_LEAVES.get(), TFBlocks.THORN_ROSE.get(), TFBlocks.HEDGE.get(), TFBlocks.FALLEN_LEAVES.get(), TFBlocks.MANGROVE_ROOT.get(),
			// TFBlocks.OAK_BANISTER.get(), TFBlocks.SPRUCE_BANISTER.get(), TFBlocks.BIRCH_BANISTER.get(), TFBlocks.JUNGLE_BANISTER.get(), TFBlocks.ACACIA_BANISTER.get(), TFBlocks.DARK_OAK_BANISTER.get(), TFBlocks.CRIMSON_BANISTER.get(), TFBlocks.WARPED_BANISTER.get(),
			TFBlocks.TWILIGHT_OAK_BANISTER.get(), TFBlocks.CANOPY_BANISTER.get(), TFBlocks.MANGROVE_BANISTER.get(), TFBlocks.DARKWOOD_BANISTER.get(), TFBlocks.TIME_BANISTER.get(), TFBlocks.TRANSFORMATION_BANISTER.get(), TFBlocks.MINING_BANISTER.get(), TFBlocks.SORTING_BANISTER.get()
	};

	public AdvancementGenerator(DataGenerator generatorIn, ExistingFileHelper fileHelperIn) {
		super(generatorIn, fileHelperIn);
	}

	@Override
	protected void registerAdvancements(Consumer<Advancement> consumer, ExistingFileHelper fileHelper) {
		Advancement root = Advancement.Builder.advancement().display(
				TFBlocks.TWILIGHT_PORTAL_MINIATURE_STRUCTURE.get(),
				new TranslatableComponent("itemGroup.twilightforest"),
				new TranslatableComponent("advancement.twilightforest.root.desc"),
				new ResourceLocation(TwilightForestMod.ID, "textures/block/mazestone_large_brick.png"),
				FrameType.TASK,
				true, false, false)
				.requirements(RequirementsStrategy.OR)
				.addCriterion("in_tf",
						LocationTrigger.TriggerInstance.located(
								LocationPredicate.inDimension(
										ResourceKey.create(Registry.DIMENSION_REGISTRY,
												new ResourceLocation(TFConfig.COMMON_CONFIG.DIMENSION.portalDestinationID.get())))))
				.addCriterion("make_portal",
						MakePortalTrigger.Instance.makePortal())
				.save(consumer, "twilightforest:root");

		Advancement silence = this.addTFKillable(Advancement.Builder.advancement().parent(root).display(
				TFItems.RAVEN_FEATHER.get(),
						new TranslatableComponent("advancement.twilightforest.twilight_hunter"),
						new TranslatableComponent("advancement.twilightforest.twilight_hunter.desc"),
				null, FrameType.TASK, true, true, false).requirements(RequirementsStrategy.OR))
				.save(consumer, "twilightforest:twilight_hunter");

		Advancement naga = Advancement.Builder.advancement().parent(root).display(
				TFBlocks.NAGA_COURTYARD_MINIATURE_STRUCTURE.get(),
				new TranslatableComponent("advancement.twilightforest.kill_naga"),
				new TranslatableComponent("advancement.twilightforest.kill_naga.desc",
						new TranslatableComponent(TFEntities.NAGA.get().getDescriptionId()),
						new TranslatableComponent(TFItems.NAGA_SCALE.get().getDescriptionId())),
				null, FrameType.GOAL, true, true, false)
				.addCriterion("naga", KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of(TFEntities.NAGA.get())))
				.addCriterion("trophy", InventoryChangeTrigger.TriggerInstance.hasItems(TFBlocks.NAGA_TROPHY.get()))
				.addCriterion("scale", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.NAGA_SCALE.get()))
				.addCriterion("was_in_fight", HurtBossTrigger.Instance.hurtBoss(EntityPredicate.Builder.entity().of(TFEntities.NAGA.get())))
				.requirements(RequirementsStrategy.OR)
				.rewards(AdvancementRewards.Builder.function(TwilightForestMod.prefix("give_3_shields")))
				.save(consumer, "twilightforest:progress_naga");

		Advancement lich = Advancement.Builder.advancement().parent(naga).display(
						TFBlocks.LICH_TOWER_MINIATURE_STRUCTURE.get(),
						new TranslatableComponent("advancement.twilightforest.kill_lich"),
						new TranslatableComponent("advancement.twilightforest.kill_lich.desc",
								new TranslatableComponent(TFEntities.LICH.get().getDescriptionId())),
						null, FrameType.GOAL, true, true, false)
				.addCriterion("kill_lich", KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of(TFEntities.LICH.get())))
				.addCriterion("trophy", InventoryChangeTrigger.TriggerInstance.hasItems(TFBlocks.LICH_TROPHY.get()))
				.addCriterion("lifedrain_scepter", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.LIFEDRAIN_SCEPTER.get()))
				.addCriterion("twilight_scepter", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.TWILIGHT_SCEPTER.get()))
				.addCriterion("zombie_scepter", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.ZOMBIE_SCEPTER.get()))
				.addCriterion("shield_scepter", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.FORTIFICATION_SCEPTER.get()))
				.addCriterion("was_in_fight", HurtBossTrigger.Instance.hurtBoss(EntityPredicate.Builder.entity().of(TFEntities.LICH.get())))
				.addCriterion("kill_naga", HasAdvancementTrigger.Instance.hasAdvancement(naga.getId()))
				.requirements(new CountRequirementsStrategy(7, 1))
				.rewards(AdvancementRewards.Builder.function(TwilightForestMod.prefix("give_3_shields")))
				.save(consumer, "twilightforest:progress_lich");

		Advancement minoshroom = Advancement.Builder.advancement().parent(lich).display(
						TFItems.MEEF_STROGANOFF.get(),
						new TranslatableComponent("advancement.twilightforest.progress_labyrinth"),
						new TranslatableComponent("advancement.twilightforest.progress_labyrinth.desc"),
						null, FrameType.GOAL, true, true, false)
				.addCriterion("meef", ConsumeItemTrigger.TriggerInstance.usedItem(TFItems.MEEF_STROGANOFF.get()))
				.addCriterion("kill_lich", HasAdvancementTrigger.Instance.hasAdvancement(lich.getId()))
				.requirements(RequirementsStrategy.AND)
				.rewards(AdvancementRewards.Builder.function(TwilightForestMod.prefix("give_3_shields")))
				.save(consumer, "twilightforest:progress_labyrinth");

		Advancement hydra = Advancement.Builder.advancement().parent(minoshroom).display(
						TFBlocks.HYDRA_TROPHY.get(),
						new TranslatableComponent("advancement.twilightforest.kill_hydra"),
						new TranslatableComponent("advancement.twilightforest.kill_hydra.desc",
								new TranslatableComponent(TFEntities.HYDRA.get().getDescriptionId())),
						null, FrameType.GOAL, true, true, false)
				.addCriterion("kill_hydra", KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of(TFEntities.HYDRA.get())))
				.addCriterion("trophy", InventoryChangeTrigger.TriggerInstance.hasItems(TFBlocks.HYDRA_TROPHY.get()))
				.addCriterion("blood", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.FIERY_BLOOD.get()))
				.addCriterion("was_in_fight", HurtBossTrigger.Instance.hurtBoss(EntityPredicate.Builder.entity().of(TFEntities.HYDRA.get())))
				.addCriterion("stroganoff", HasAdvancementTrigger.Instance.hasAdvancement(minoshroom.getId()))
				.requirements(new CountRequirementsStrategy(4, 1))
				.rewards(AdvancementRewards.Builder.function(TwilightForestMod.prefix("give_3_shields")))
				.save(consumer, "twilightforest:progress_hydra");

		Advancement trophy_pedestal = Advancement.Builder.advancement().parent(lich).display(
						TFBlocks.TROPHY_PEDESTAL.get(),
						new TranslatableComponent("advancement.twilightforest.progress_trophy_pedestal"),
						new TranslatableComponent("advancement.twilightforest.progress_trophy_pedestal.desc"),
						null, FrameType.GOAL, true, true, false)
				.addCriterion("trophy_pedestal", new TrophyPedestalTrigger.Instance(EntityPredicate.Composite.ANY))
				.addCriterion("kill_lich", HasAdvancementTrigger.Instance.hasAdvancement(lich.getId()))
				.requirements(RequirementsStrategy.AND)
				.save(consumer, "twilightforest:progress_trophy_pedestal");

		Advancement knights = Advancement.Builder.advancement().parent(trophy_pedestal).display(
						TFBlocks.KNIGHT_PHANTOM_TROPHY.get(),
						new TranslatableComponent("advancement.twilightforest.progress_knights"),
						new TranslatableComponent("advancement.twilightforest.progress_knights.desc"),
						null, FrameType.GOAL, true, true, false)
				.addCriterion("all_knights", KillAllPhantomsTrigger.Instance.killThemAll())
				.addCriterion("trophy", InventoryChangeTrigger.TriggerInstance.hasItems(TFBlocks.KNIGHT_PHANTOM_TROPHY.get()))
				.addCriterion("was_in_fight", HurtBossTrigger.Instance.hurtBoss(EntityPredicate.Builder.entity().of(TFEntities.KNIGHT_PHANTOM.get())))
				.addCriterion("previous_progression", HasAdvancementTrigger.Instance.hasAdvancement(trophy_pedestal.getId()))
				.requirements(new CountRequirementsStrategy(3, 1))
				.rewards(AdvancementRewards.Builder.function(TwilightForestMod.prefix("give_3_shields")))
				.save(consumer, "twilightforest:progress_knights");

		Advancement trap = Advancement.Builder.advancement().parent(knights).display(
				TFBlocks.GHAST_TRAP.get(),
				new TranslatableComponent("advancement.twilightforest.ghast_trap"),
				new TranslatableComponent("advancement.twilightforest.ghast_trap.desc",
						new TranslatableComponent(TFEntities.CARMINITE_GHASTLING.get().getDescriptionId()),
						new TranslatableComponent(TFBlocks.GHAST_TRAP.get().getDescriptionId()),
						new TranslatableComponent(TFEntities.UR_GHAST.get().getDescriptionId())),
				null, FrameType.TASK, true, true, false)
				.addCriterion("activate_ghast_trap", ActivateGhastTrapTrigger.Instance.activateTrap())
				.save(consumer, "twilightforest:ghast_trap");

		Advancement ur_ghast = Advancement.Builder.advancement().parent(trap).display(
						TFBlocks.UR_GHAST_TROPHY.get(),
						new TranslatableComponent("advancement.twilightforest.progress_ur_ghast"),
						new TranslatableComponent("advancement.twilightforest.progress_ur_ghast.desc",
								new TranslatableComponent(TFEntities.UR_GHAST.get().getDescriptionId())),
						null, FrameType.GOAL, true, true, false)
				.addCriterion("ghast", KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of(TFEntities.UR_GHAST.get())))
				.addCriterion("trophy", InventoryChangeTrigger.TriggerInstance.hasItems(TFBlocks.UR_GHAST_TROPHY.get()))
				.addCriterion("tear", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.FIERY_TEARS.get()))
				.addCriterion("was_in_fight", HurtBossTrigger.Instance.hurtBoss(EntityPredicate.Builder.entity().of(TFEntities.UR_GHAST.get())))
				.addCriterion("previous_progression", HasAdvancementTrigger.Instance.hasAdvancement(trap.getId()))
				.requirements(new CountRequirementsStrategy(4, 1))
				.rewards(AdvancementRewards.Builder.function(TwilightForestMod.prefix("give_3_shields")))
				.save(consumer, "twilightforest:progress_ur_ghast");

		Advancement yeti = Advancement.Builder.advancement().parent(lich).display(
						TFItems.ALPHA_YETI_FUR.get(),
						new TranslatableComponent("advancement.twilightforest.progress_yeti"),
						new TranslatableComponent("advancement.twilightforest.progress_yeti.desc",
								new TranslatableComponent(TFEntities.ALPHA_YETI.get().getDescriptionId())),
						null, FrameType.GOAL, true, true, false)
				.addCriterion("yeti", KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of(TFEntities.ALPHA_YETI.get())))
				.addCriterion("trophy", InventoryChangeTrigger.TriggerInstance.hasItems(TFBlocks.ALPHA_YETI_TROPHY.get()))
				.addCriterion("fur", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.ALPHA_YETI_FUR.get()))
				.addCriterion("was_in_fight", HurtBossTrigger.Instance.hurtBoss(EntityPredicate.Builder.entity().of(TFEntities.ALPHA_YETI.get())))
				.addCriterion("previous_progression", HasAdvancementTrigger.Instance.hasAdvancement(lich.getId()))
				.requirements(new CountRequirementsStrategy(4, 1))
				.rewards(AdvancementRewards.Builder.function(TwilightForestMod.prefix("give_3_shields")))
				.save(consumer, "twilightforest:progress_yeti");

		Advancement snow_queen = Advancement.Builder.advancement().parent(yeti).display(
						TFBlocks.SNOW_QUEEN_TROPHY.get(),
						new TranslatableComponent("advancement.twilightforest.progress_glacier"),
						new TranslatableComponent("advancement.twilightforest.progress_glacier.desc",
								new TranslatableComponent(TFEntities.SNOW_QUEEN.get().getDescriptionId())),
						null, FrameType.GOAL, true, true, false)
				.addCriterion("queen", KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of(TFEntities.SNOW_QUEEN.get())))
				.addCriterion("trophy", InventoryChangeTrigger.TriggerInstance.hasItems(TFBlocks.SNOW_QUEEN_TROPHY.get()))
				.addCriterion("was_in_fight", HurtBossTrigger.Instance.hurtBoss(EntityPredicate.Builder.entity().of(TFEntities.SNOW_QUEEN.get())))
				.addCriterion("previous_progression", HasAdvancementTrigger.Instance.hasAdvancement(yeti.getId()))
				.requirements(new CountRequirementsStrategy(3, 1))
				.rewards(AdvancementRewards.Builder.function(TwilightForestMod.prefix("give_3_shields")))
				.save(consumer, "twilightforest:progress_glacier");

		Advancement merge = Advancement.Builder.advancement().parent(lich).display(
						TFBlocks.UBEROUS_SOIL.get(),
						new TranslatableComponent("advancement.twilightforest.progress_merge"),
						new TranslatableComponent("advancement.twilightforest.progress_merge.desc",
								new TranslatableComponent(TFEntities.HYDRA.get().getDescriptionId()),
								new TranslatableComponent(TFEntities.UR_GHAST.get().getDescriptionId()),
								new TranslatableComponent(TFEntities.SNOW_QUEEN.get().getDescriptionId())),
						null, FrameType.GOAL, true, true, false)
				.addCriterion("hydra", HasAdvancementTrigger.Instance.hasAdvancement(hydra.getId()))
				.addCriterion("ur_ghast", HasAdvancementTrigger.Instance.hasAdvancement(ur_ghast.getId()))
				.addCriterion("snow_queen", HasAdvancementTrigger.Instance.hasAdvancement(snow_queen.getId()))
				.save(consumer, "twilightforest:progress_merge");

		Advancement troll = Advancement.Builder.advancement().parent(merge).display(
						TFItems.MAGIC_BEANS.get(),
						new TranslatableComponent("advancement.twilightforest.troll"),
						new TranslatableComponent("advancement.twilightforest.troll.desc",
								new TranslatableComponent(TFEntities.TROLL.get().getDescriptionId())),
						null, FrameType.TASK, true, true, false)
				.addCriterion("troll", KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of(TFEntities.TROLL.get()).located(LocationPredicate.inFeature(TFConfiguredStructures.CONFIGURED_TROLL_CAVE.unwrapKey().orElseThrow()))))
				.save(consumer, "twilightforest:troll");

		Advancement beanstalk = Advancement.Builder.advancement().parent(troll).display(
						TFBlocks.HUGE_STALK.get(),
						new TranslatableComponent("advancement.twilightforest.beanstalk"),
						new TranslatableComponent("advancement.twilightforest.beanstalk.desc",
								new TranslatableComponent(TFItems.MAGIC_BEANS.get().getDescriptionId())),
						null, FrameType.GOAL, true, true, false)
				.addCriterion("beans", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.MAGIC_BEANS.get()))
				.addCriterion("use_beans", ItemUsedOnBlockTrigger.TriggerInstance.itemUsedOnBlock(LocationPredicate.Builder.location().setBlock(BlockPredicate.Builder.block().of(TFBlocks.UBEROUS_SOIL.get()).build()), ItemPredicate.Builder.item().of(TFItems.MAGIC_BEANS.get())))
				.save(consumer, "twilightforest:beanstalk");

		Advancement giants = Advancement.Builder.advancement().parent(beanstalk).display(
						TFItems.GIANT_PICKAXE.get(),
						new TranslatableComponent("advancement.twilightforest.giants"),
						new TranslatableComponent("advancement.twilightforest.giants.desc",
								new TranslatableComponent(TFEntities.GIANT_MINER.get().getDescriptionId()),
								new TranslatableComponent(TFItems.GIANT_PICKAXE.get().getDescriptionId())),
						null, FrameType.GOAL, true, true, false)
				.addCriterion("giant", KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of(TFEntities.GIANT_MINER.get())))
				.addCriterion("pickaxe", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.MAGIC_BEANS.get()))
				.save(consumer, "twilightforest:giants");

		Advancement lamp = Advancement.Builder.advancement().parent(giants).display(
						TFItems.LAMP_OF_CINDERS.get(),
						new TranslatableComponent("advancement.twilightforest.progress_troll"),
						new TranslatableComponent("advancement.twilightforest.progress_troll.desc",
								new TranslatableComponent(TFItems.LAMP_OF_CINDERS.get().getDescriptionId())),
						null, FrameType.GOAL, true, true, false)
				.addCriterion("lamp", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.LAMP_OF_CINDERS.get()))
				.addCriterion("previous_progression", HasAdvancementTrigger.Instance.hasAdvancement(merge.getId()))
				.save(consumer, "twilightforest:progress_troll");

		Advancement thornlands = Advancement.Builder.advancement().parent(lamp).display(
						TFBlocks.BROWN_THORNS.get(),
						new TranslatableComponent("advancement.twilightforest.progress_thorns"),
						new TranslatableComponent("advancement.twilightforest.progress_thorns.desc"),
						null, FrameType.GOAL, true, true, false)
				.addCriterion("castle", LocationTrigger.TriggerInstance.located(LocationPredicate.inFeature(TFConfiguredStructures.CONFIGURED_FINAL_CASTLE.unwrapKey().orElseThrow())))
				.addCriterion("previous_progression", HasAdvancementTrigger.Instance.hasAdvancement(lamp.getId()))
				.save(consumer, "twilightforest:progress_thorns");

		Advancement.Builder.advancement().parent(thornlands).display(
						TFBlocks.VIOLET_CASTLE_RUNE_BRICK.get(),
						new TranslatableComponent("advancement.twilightforest.progress_castle"),
						new TranslatableComponent("advancement.twilightforest.progress_castle.desc"),
						null, FrameType.GOAL, true, true, false)
				.addCriterion("castle", LocationTrigger.TriggerInstance.located(LocationPredicate.inFeature(TFConfiguredStructures.CONFIGURED_FINAL_CASTLE.unwrapKey().orElseThrow())))
				.addCriterion("previous_progression", HasAdvancementTrigger.Instance.hasAdvancement(thornlands.getId()))
				.save(consumer, "twilightforest:progress_castle");

		Advancement.Builder.advancement().parent(root).display(
						TFBlocks.QUEST_RAM_TROPHY.get(),
						new TranslatableComponent("advancement.twilightforest.quest_ram"),
						new TranslatableComponent("advancement.twilightforest.quest_ram.desc",
								new TranslatableComponent(TFEntities.QUEST_RAM.get().getDescriptionId())),
						null, FrameType.GOAL, true, true, false)
				.addCriterion("quest_ram_complete", QuestRamCompletionTrigger.Instance.completeRam())
				.rewards(AdvancementRewards.Builder.function(TwilightForestMod.prefix("give_3_shields")).addExperience(100))
				.save(consumer, "twilightforest:quest_ram");

		Advancement.Builder.advancement().parent(root).display(
						TFBlocks.CICADA.get(),
						new TranslatableComponent("advancement.twilightforest.kill_cicada"),
						new TranslatableComponent("advancement.twilightforest.kill_cicada.desc",
								new TranslatableComponent(TFBlocks.CICADA.get().getDescriptionId())),
						null, FrameType.TASK, true, true, true)
				.addCriterion("kill_cicada", KillBugTrigger.Instance.killBug(TFBlocks.CICADA.get()))
				.save(consumer, "twilightforest:kill_cicada");

		Advancement focus = Advancement.Builder.advancement().parent(silence).display(
						TFItems.MAGIC_MAP_FOCUS.get(),
						new TranslatableComponent("advancement.twilightforest.magic_map_focus"),
						new TranslatableComponent("advancement.twilightforest.magic_map_focus.desc",
								new TranslatableComponent(TFItems.MAGIC_MAP_FOCUS.get().getDescriptionId()),
								new TranslatableComponent(TFItems.RAVEN_FEATHER.get().getDescriptionId()),
								new TranslatableComponent(Items.GLOWSTONE_DUST.getDescriptionId()),
								new TranslatableComponent(TFItems.TORCHBERRIES.get().getDescriptionId())),
						null, FrameType.TASK, true, true, false)
				.addCriterion("focus", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.MAGIC_MAP_FOCUS.get()))
				.save(consumer, "twilightforest:magic_map_focus");

		Advancement magic_map = Advancement.Builder.advancement().parent(focus).display(
						TFItems.FILLED_MAGIC_MAP.get(),
						new TranslatableComponent("advancement.twilightforest.magic_map"),
						new TranslatableComponent("advancement.twilightforest.magic_map.desc",
								new TranslatableComponent(TFItems.FILLED_MAGIC_MAP.get().getDescriptionId())),
						null, FrameType.TASK, true, true, false)
				.addCriterion("magic_map", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.FILLED_MAGIC_MAP.get()))
				.save(consumer, "twilightforest:magic_map");

		Advancement maze_map = Advancement.Builder.advancement().parent(magic_map).display(
						TFItems.FILLED_MAZE_MAP.get(),
						new TranslatableComponent("advancement.twilightforest.maze_map"),
						new TranslatableComponent("advancement.twilightforest.maze_map.desc",
								new TranslatableComponent(TFItems.FILLED_MAZE_MAP.get().getDescriptionId())),
						null, FrameType.GOAL, true, true, false)
				.addCriterion("maze_map", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.FILLED_MAZE_MAP.get()))
				.save(consumer, "twilightforest:maze_map");

		Advancement.Builder.advancement().parent(maze_map).display(
						TFItems.FILLED_ORE_MAP.get(),
						new TranslatableComponent("advancement.twilightforest.ore_map"),
						new TranslatableComponent("advancement.twilightforest.ore_map.desc",
								new TranslatableComponent(TFItems.FILLED_ORE_MAP.get().getDescriptionId())),
						null, FrameType.CHALLENGE, true, true, true)
				.addCriterion("ore_map", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.FILLED_ORE_MAP.get()))
				.save(consumer, "twilightforest:ore_map");

		Advancement hill1 =  Advancement.Builder.advancement().parent(root).display(
						Items.IRON_BOOTS,
						new TranslatableComponent("advancement.twilightforest.hill1"),
						new TranslatableComponent("advancement.twilightforest.hill1.desc",
								new TranslatableComponent(TFEntities.REDCAP.get().getDescriptionId())),
						null, FrameType.TASK, true, true, false)
				.addCriterion("redcap", KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of(TFEntities.REDCAP.get()).located(LocationPredicate.inFeature(TFConfiguredStructures.CONFIGURED_HOLLOW_HILL_SMALL.unwrapKey().orElseThrow()))))
				.save(consumer, "twilightforest:hill1");

		Advancement hill2 =  Advancement.Builder.advancement().parent(hill1).display(
						TFItems.IRONWOOD_PICKAXE.get(),
						new TranslatableComponent("advancement.twilightforest.hill2"),
						new TranslatableComponent("advancement.twilightforest.hill2.desc",
								new TranslatableComponent(TFEntities.REDCAP_SAPPER.get().getDescriptionId())),
						null, FrameType.TASK, true, true, false)
				.addCriterion("redcap", KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of(TFEntities.REDCAP_SAPPER.get()).located(LocationPredicate.inFeature(TFConfiguredStructures.CONFIGURED_HOLLOW_HILL_MEDIUM.unwrapKey().orElseThrow()))))
				.save(consumer, "twilightforest:hill2");

		Advancement.Builder.advancement().parent(hill2).display(
						Items.GLOWSTONE_DUST,
						new TranslatableComponent("advancement.twilightforest.hill3"),
						new TranslatableComponent("advancement.twilightforest.hill3.desc",
								new TranslatableComponent(TFEntities.WRAITH.get().getDescriptionId())),
						null, FrameType.TASK, true, true, false)
				.addCriterion("redcap", KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of(TFEntities.WRAITH.get()).located(LocationPredicate.inFeature(TFConfiguredStructures.CONFIGURED_HOLLOW_HILL_LARGE.unwrapKey().orElseThrow()))))
				.save(consumer, "twilightforest:hill3");

		Advancement.Builder.advancement().parent(root).display(
						TFBlocks.HEDGE.get(),
						new TranslatableComponent("advancement.twilightforest.hedge"),
						new TranslatableComponent("advancement.twilightforest.hedge.desc"),
						null, FrameType.TASK, true, true, false)
				.addCriterion("hedge_spider", KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of(TFEntities.HEDGE_SPIDER.get()).located(LocationPredicate.inFeature(TFConfiguredStructures.CONFIGURED_HEDGE_MAZE.unwrapKey().orElseThrow()))))
				.addCriterion("swarm_spider", KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of(TFEntities.SWARM_SPIDER.get()).located(LocationPredicate.inFeature(TFConfiguredStructures.CONFIGURED_HEDGE_MAZE.unwrapKey().orElseThrow()))))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:hedge");

		Advancement.Builder.advancement().parent(root).display(
						Items.BOWL,
						new TranslatableComponent("advancement.twilightforest.twilight_dining"),
						new TranslatableComponent("advancement.twilightforest.twilight_dining.desc"),
						null, FrameType.CHALLENGE, true, true, false)
				.addCriterion("raw_venison", ConsumeItemTrigger.TriggerInstance.usedItem(TFItems.RAW_VENISON.get()))
				.addCriterion("cooked_venison", ConsumeItemTrigger.TriggerInstance.usedItem(TFItems.COOKED_VENISON.get()))
				.addCriterion("raw_meef", ConsumeItemTrigger.TriggerInstance.usedItem(TFItems.RAW_MEEF.get()))
				.addCriterion("cooked_meef", ConsumeItemTrigger.TriggerInstance.usedItem(TFItems.COOKED_MEEF.get()))
				.addCriterion("meef_stroganoff", ConsumeItemTrigger.TriggerInstance.usedItem(TFItems.MEEF_STROGANOFF.get()))
				.addCriterion("hydra_chop", ConsumeItemTrigger.TriggerInstance.usedItem(TFItems.HYDRA_CHOP.get()))
				.addCriterion("maze_wafer", ConsumeItemTrigger.TriggerInstance.usedItem(TFItems.MAZE_WAFER.get()))
				.addCriterion("experiment_115", ConsumeItemTrigger.TriggerInstance.usedItem(TFItems.EXPERIMENT_115.get()))
				.addCriterion("torchberries", ConsumeItemTrigger.TriggerInstance.usedItem(TFItems.TORCHBERRIES.get()))
				.requirements(new CountRequirementsStrategy(2, 2, 1, 1, 1, 1, 1))
				.rewards(AdvancementRewards.Builder.experience(75))
				.save(consumer, "twilightforest:twilight_dinner");

		Advancement.Builder.advancement().parent(naga).display(
						TFItems.NAGA_CHESTPLATE.get(),
						new TranslatableComponent("advancement.twilightforest.naga_armors"),
						new TranslatableComponent("advancement.twilightforest.naga_armors.desc",
								new TranslatableComponent(TFItems.NAGA_SCALE.get().getDescriptionId())),
						null, FrameType.CHALLENGE, true, true, false)
				.addCriterion("armor", InventoryChangeTrigger.TriggerInstance.hasItems(
						TFItems.NAGA_CHESTPLATE.get(), TFItems.NAGA_LEGGINGS.get()))
				.rewards(AdvancementRewards.Builder.experience(25))
				.save(consumer, "twilightforest:naga_armors");

		Advancement.Builder.advancement().parent(lich).display(
						TFItems.ZOMBIE_SCEPTER.get(),
						new TranslatableComponent("advancement.twilightforest.lich_scepters"),
						new TranslatableComponent("advancement.twilightforest.lich_scepters.desc"),
						null, FrameType.CHALLENGE, true, true, true)
				.addCriterion("scepters", InventoryChangeTrigger.TriggerInstance.hasItems(
						TFItems.LIFEDRAIN_SCEPTER.get(), TFItems.TWILIGHT_SCEPTER.get(),
						TFItems.ZOMBIE_SCEPTER.get(), TFItems.FORTIFICATION_SCEPTER.get()))
				.rewards(AdvancementRewards.Builder.experience(100))
				.save(consumer, "twilightforest:lich_scepters");

		Advancement.Builder.advancement().parent(lich).display(
						flaskWithHarming(),
						new TranslatableComponent("advancement.twilightforest.full_mettle_alchemist"),
						new TranslatableComponent("advancement.twilightforest.full_mettle_alchemist.desc"),
						null, FrameType.CHALLENGE, true, true, true)
				.addCriterion("drink_4_harming", DrinkFromFlaskTrigger.Instance.drankPotion(4, Potions.STRONG_HARMING))
				.rewards(AdvancementRewards.Builder.experience(100))
				.save(consumer, "twilightforest:full_mettle_alchemist");

		Advancement.Builder.advancement().parent(minoshroom).display(
						TFItems.MAZEBREAKER_PICKAXE.get(),
						new TranslatableComponent("advancement.twilightforest.mazebreaker"),
						new TranslatableComponent("advancement.twilightforest.mazebreaker.desc",
								new TranslatableComponent(TFItems.MAZEBREAKER_PICKAXE.get().getDescriptionId())),
						null, FrameType.CHALLENGE, true, true, false)
				.addCriterion("pick", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.MAZEBREAKER_PICKAXE.get()))
				.rewards(AdvancementRewards.Builder.experience(50))
				.save(consumer, "twilightforest:mazebreaker");

		Advancement.Builder.advancement().parent(hydra).display(
						TFItems.HYDRA_CHOP.get(),
						new TranslatableComponent("advancement.twilightforest.hydra_chop"),
						new TranslatableComponent("advancement.twilightforest.hydra_chop.desc",
								new TranslatableComponent(TFEntities.HYDRA.get().getDescriptionId())),
						null, FrameType.TASK, true, true, false)
				.addCriterion("hydra_chop", HydraChopTrigger.Instance.eatChop())
				.save(consumer, "twilightforest:hydra_chop");

		Advancement.Builder.advancement().parent(hydra).display(
						TFItems.FIERY_SWORD.get(),
						new TranslatableComponent("advancement.twilightforest.fiery_set"),
						new TranslatableComponent("advancement.twilightforest.fiery_set.desc"),
						null, FrameType.CHALLENGE, true, true, false)
				.addCriterion("fiery_pick", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.FIERY_PICKAXE.get()))
				.addCriterion("fiery_sword", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.FIERY_SWORD.get()))
				.addCriterion("fiery_helmet", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.FIERY_HELMET.get()))
				.addCriterion("fiery_chestplate", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.FIERY_CHESTPLATE.get()))
				.addCriterion("fiery_leggings", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.FIERY_LEGGINGS.get()))
				.addCriterion("fiery_boots", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.FIERY_BOOTS.get()))
				.requirements(new CountRequirementsStrategy(2, 4))
				.rewards(AdvancementRewards.Builder.experience(75))
				.save(consumer, "twilightforest:fiery_set");

		Advancement e115 = Advancement.Builder.advancement().parent(knights).display(
						TFItems.EXPERIMENT_115.get(),
						new TranslatableComponent("advancement.twilightforest.experiment_115"),
						new TranslatableComponent("advancement.twilightforest.experiment_115.desc"),
						null, FrameType.TASK, true, true, false)
				.addCriterion("eat_experiment_115", ConsumeItemTrigger.TriggerInstance.usedItem(TFItems.EXPERIMENT_115.get()))
				.save(consumer, "twilightforest:experiment_115");

		Advancement.Builder.advancement().parent(e115).display(
						e115Tag("think"),
						new TranslatableComponent("advancement.twilightforest.experiment_115_3"),
						new TranslatableComponent("advancement.twilightforest.experiment_115_3.desc"),
						null, FrameType.CHALLENGE, true, true, true)
				.addCriterion("eat_115_e115", LocationTrigger.TriggerInstance.located(EntityPredicate.Builder.entity().player(PlayerPredicate.Builder.player().addStat(Stats.CUSTOM.get(TFStats.E115_SLICES_EATEN.get()), MinMaxBounds.Ints.atLeast(115)).build()).build()))
				.save(consumer, "twilightforest:experiment_115_115");

		Advancement.Builder.advancement().parent(e115).display(
						e115Tag("full"),
						new TranslatableComponent("advancement.twilightforest.experiment_115_2"),
						new TranslatableComponent("advancement.twilightforest.experiment_115_2.desc"),
						null, FrameType.CHALLENGE, true, true, true)
				.addCriterion("place_complete_e115", ItemUsedOnBlockTrigger.TriggerInstance.itemUsedOnBlock(LocationPredicate.Builder.location().setBlock(BlockPredicate.Builder.block().of(TFBlocks.EXPERIMENT_115.get()).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(Experiment115Block.BITES_TAKEN, 0).hasProperty(Experiment115Block.REGENERATE, false).build()).build()), ItemPredicate.Builder.item().of(Items.REDSTONE)))
				.save(consumer, "twilightforest:experiment_115_self_replenishing");

		Advancement.Builder.advancement().parent(yeti).display(
						TFItems.ARCTIC_CHESTPLATE.get(),
						new TranslatableComponent("advancement.twilightforest.arctic_dyed"),
						new TranslatableComponent("advancement.twilightforest.arctic_dyed.desc"),
						null, FrameType.TASK, true, true, false)
				.addCriterion("helmet", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(TFItems.ARCTIC_HELMET.get()).hasNbt(arcticDye(TFItems.ARCTIC_HELMET.get())).build()))
				.addCriterion("chestplate", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(TFItems.ARCTIC_CHESTPLATE.get()).hasNbt(arcticDye(TFItems.ARCTIC_CHESTPLATE.get())).build()))
				.addCriterion("leggings", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(TFItems.ARCTIC_LEGGINGS.get()).hasNbt(arcticDye(TFItems.ARCTIC_LEGGINGS.get())).build()))
				.addCriterion("boots", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(TFItems.ARCTIC_BOOTS.get()).hasNbt(arcticDye(TFItems.ARCTIC_BOOTS.get())).build()))
				.rewards(AdvancementRewards.Builder.experience(25))
				.save(consumer, "twilightforest:arctic_armor_dyed");

		Advancement.Builder.advancement().parent(yeti).display(
						TFItems.GLASS_SWORD.get(),
						new TranslatableComponent("advancement.twilightforest.glass_sword"),
						new TranslatableComponent("advancement.twilightforest.glass_sword.desc"),
						null, FrameType.CHALLENGE, true, true, true)
				.addCriterion("broken_sword", ItemDurabilityTrigger.TriggerInstance.changedDurability(ItemPredicate.Builder.item().of(TFItems.GLASS_SWORD.get()).build(), MinMaxBounds.Ints.exactly(-1)))
				.rewards(AdvancementRewards.Builder.experience(42).addLootTable(TwilightForestMod.prefix("glass_sword")))
				.save(consumer, "twilightforest:break_glass_sword");

		this.addDendrologistBlock(Advancement.Builder.advancement().parent(root)
				.display(TFBlocks.TWILIGHT_OAK_FENCE.get(),
						new TranslatableComponent("advancement.twilightforest.arborist"),
						new TranslatableComponent("advancement.twilightforest.arborist.desc"),
						null, FrameType.CHALLENGE, true, true, false)
				.requirements(RequirementsStrategy.AND))
				.addCriterion("liveroot", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.LIVEROOT.get()))
				.rewards(AdvancementRewards.Builder.experience(1000))
				.save(consumer, "twilightforest:arborist");

	}

	private ItemStack e115Tag(String nbt) {
		ItemStack itemstack = new ItemStack(TFItems.EXPERIMENT_115.get());
		CompoundTag compoundtag = itemstack.getOrCreateTagElement(nbt);
		compoundtag.putInt(nbt, 1);
		return itemstack;
	}

	private ItemStack flaskWithHarming() {
		ItemStack itemstack = new ItemStack(TFItems.GREATER_FLASK.get());
		CompoundTag compoundtag = itemstack.getOrCreateTag();
		compoundtag.putInt("Uses", 4);
		compoundtag.putString("Potion", Potions.STRONG_HARMING.getRegistryName().toString());
		return itemstack;
	}

	private CompoundTag arcticDye(Item item) {
		ItemStack itemstack = new ItemStack(item);
		CompoundTag compoundtag = itemstack.getOrCreateTagElement("display");
		CompoundTag color = itemstack.getOrCreateTagElement("hasColor");
		color.putBoolean("hasColor", true);
		compoundtag.put("display", color);
		return compoundtag;
	}

	private Advancement.Builder addTFKillable(Advancement.Builder builder) {
		for (EntityType<?> entity : TF_KILLABLE) {
			builder.addCriterion(EntityType.getKey(entity).getPath(),
					KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of(entity)
							.located(
									LocationPredicate.inDimension(
											ResourceKey.create(Registry.DIMENSION_REGISTRY,
													new ResourceLocation(TFConfig.COMMON_CONFIG.DIMENSION.portalDestinationID.get()))))));
		}
		return builder;
	}

	private Advancement.Builder addDendrologistBlock(Advancement.Builder builder) {
		for (Block dendrologistBlock : DENDROLOGIST_BLOCKS) {
			builder.addCriterion(dendrologistBlock.getRegistryName().getPath(), InventoryChangeTrigger.TriggerInstance.hasItems(dendrologistBlock));
		}
		return builder;
	}
}
