package twilightforest.data;

import net.minecraft.world.level.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Registry;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.fmllegacy.RegistryObject;
import twilightforest.TwilightForestMod;
import twilightforest.block.HollowLogHorizontal;
import twilightforest.block.TFBlocks;
import twilightforest.item.Experiment115Item;
import twilightforest.item.TFItems;

import static twilightforest.TwilightForestMod.prefix;

public class ItemModelGenerator extends ItemModelProvider {
	public ItemModelGenerator(DataGenerator generator, ExistingFileHelper existingFileHelper) {
		super(generator, TwilightForestMod.ID, existingFileHelper);
	}

	@Override
	protected void registerModels() {
		for (Item i : Registry.ITEM) {
			if (i instanceof SpawnEggItem && i.getRegistryName().getNamespace().equals(TwilightForestMod.ID)) {
				getBuilder(i.getRegistryName().getPath())
								.parent(getExistingFile(new ResourceLocation("item/template_spawn_egg")));
			}
		}
		toBlock(TFBlocks.TOWERWOOD.get());
		toBlock(TFBlocks.ENCASED_TOWERWOOD.get());
		toBlock(TFBlocks.CRACKED_TOWERWOOD.get());
		toBlock(TFBlocks.MOSSY_TOWERWOOD.get());
		toBlock(TFBlocks.INFESTED_TOWERWOOD.get());
		toBlock(TFBlocks.CARMINITE_BUILDER.get());
		toBlock(TFBlocks.ANTIBUILDER.get());
		toBlock(TFBlocks.GHAST_TRAP.get());
		toBlock(TFBlocks.VANISHING_BLOCK.get());
		toBlock(TFBlocks.LOCKED_VANISHING_BLOCK.get());
		toBlock(TFBlocks.REAPPEARING_BLOCK.get());
		toBlock(TFBlocks.CARMINITE_REACTOR.get());
		toBlockModel(TFBlocks.FAKE_GOLD.get(), new ResourceLocation("block/gold_block"));
		toBlockModel(TFBlocks.FAKE_DIAMOND.get(), new ResourceLocation("block/diamond_block"));
		toBlock(TFBlocks.STRONGHOLD_SHIELD.get());
		toBlock(TFBlocks.TROPHY_PEDESTAL.get());
		//toBlock(TFBlocks.TERRORCOTTA_CIRCLE.get());
		//toBlock(TFBlocks.TERRORCOTTA_DIAGONAL.get());
		toBlockModel(TFBlocks.AURORA_BLOCK.get(), prefix("block/aurora_block_0"));
		toBlock(TFBlocks.AURORA_PILLAR.get());
		toBlock(TFBlocks.AURORA_SLAB.get());
		toBlock(TFBlocks.AURORALIZED_GLASS.get());
		toBlock(TFBlocks.UNDERBRICK.get());
		toBlock(TFBlocks.CRACKED_UNDERBRICK.get());
		toBlock(TFBlocks.MOSSY_UNDERBRICK.get());
		toBlock(TFBlocks.UNDERBRICK_FLOOR.get());
		toBlock(TFBlocks.GREEN_THORNS.get());
		toBlock(TFBlocks.BROWN_THORNS.get());
		toBlock(TFBlocks.BURNT_THORNS.get());
		generated(TFBlocks.THORN_ROSE.getId().getPath(), prefix("block/" + TFBlocks.THORN_ROSE.getId().getPath()));
		toBlockModel(TFBlocks.THORN_LEAVES.get(), new ResourceLocation("block/oak_leaves"));
		toBlockModel(TFBlocks.BEANSTALK_LEAVES.get(), new ResourceLocation("block/spruce_leaves"));
		toBlock(TFBlocks.DEADROCK.get());
		toBlock(TFBlocks.CRACKED_DEADROCK.get());
		toBlock(TFBlocks.WEATHERED_DEADROCK.get());
		getBuilder(TFBlocks.TROLLSTEINN.getId().getPath()).parent(getExistingFile(new ResourceLocation("block/cube_all")))
						.texture("all", prefix("block/trollsteinn"));
		toBlock(TFBlocks.WISPY_CLOUD.get());
		toBlock(TFBlocks.FLUFFY_CLOUD.get());
		toBlockModel(TFBlocks.GIANT_COBBLESTONE.get(), new ResourceLocation("block/cobblestone"));
		toBlockModel(TFBlocks.GIANT_LOG.get(), new ResourceLocation("block/oak_log"));
		toBlockModel(TFBlocks.GIANT_LEAVES.get(), new ResourceLocation("block/oak_leaves"));
		toBlockModel(TFBlocks.GIANT_OBSIDIAN.get(), new ResourceLocation("block/obsidian"));
		toBlock(TFBlocks.UBEROUS_SOIL.get());
		toBlock(TFBlocks.HUGE_STALK.get());
		getBuilder(TFBlocks.HUGE_MUSHGLOOM.getId().getPath()).parent(getExistingFile(new ResourceLocation("block/cube_all")))
						.texture("all", prefix("block/huge_gloom_cap"));
		getBuilder(TFBlocks.HUGE_MUSHGLOOM_STEM.getId().getPath()).parent(getExistingFile(new ResourceLocation("block/cube_all")))
				.texture("all", prefix("block/huge_mushgloom_stem"));
		generated(TFBlocks.TROLLVIDR.getId().getPath(), prefix("block/" + TFBlocks.TROLLVIDR.getId().getPath()));
		generated(TFBlocks.UNRIPE_TROLLBER.getId().getPath(), prefix("block/" + TFBlocks.UNRIPE_TROLLBER.getId().getPath()));
		generated(TFBlocks.TROLLBER.getId().getPath(), prefix("block/" + TFBlocks.TROLLBER.getId().getPath()));
		toBlock(TFBlocks.SLIDER.get());
		generated(TFBlocks.HUGE_LILY_PAD.getId().getPath(), prefix("block/" + TFBlocks.HUGE_LILY_PAD.getId().getPath()));
		generated(TFBlocks.HUGE_WATER_LILY.getId().getPath(), prefix("block/" + TFBlocks.HUGE_WATER_LILY.getId().getPath()));
		toBlock(TFBlocks.CASTLE_BRICK.get());
		toBlock(TFBlocks.WORN_CASTLE_BRICK.get());
		toBlock(TFBlocks.CRACKED_CASTLE_BRICK.get());
		toBlock(TFBlocks.CASTLE_ROOF_TILE.get());
		toBlock(TFBlocks.MOSSY_CASTLE_BRICK.get());
		toBlock(TFBlocks.THICK_CASTLE_BRICK.get());
		toBlock(TFBlocks.ENCASED_CASTLE_BRICK_PILLAR.get());
		toBlock(TFBlocks.ENCASED_CASTLE_BRICK_TILE.get());
		toBlock(TFBlocks.BOLD_CASTLE_BRICK_PILLAR.get());
		toBlock(TFBlocks.BOLD_CASTLE_BRICK_TILE.get());
		toBlock(TFBlocks.CASTLE_BRICK_STAIRS.get());
		toBlock(TFBlocks.WORN_CASTLE_BRICK_STAIRS.get());
		toBlock(TFBlocks.CRACKED_CASTLE_BRICK_STAIRS.get());
		toBlock(TFBlocks.MOSSY_CASTLE_BRICK_STAIRS.get());
		toBlock(TFBlocks.ENCASED_CASTLE_BRICK_STAIRS.get());
		toBlock(TFBlocks.BOLD_CASTLE_BRICK_STAIRS.get());
		toBlockModel(TFBlocks.YELLOW_CASTLE_RUNE_BRICK.get(), "castle_rune_brick_0");
		toBlockModel(TFBlocks.VIOLET_CASTLE_RUNE_BRICK.get(), "castle_rune_brick_0");
		toBlockModel(TFBlocks.PINK_CASTLE_RUNE_BRICK.get(), "castle_rune_brick_0");
		toBlockModel(TFBlocks.BLUE_CASTLE_RUNE_BRICK.get(), "castle_rune_brick_0");
		generated(TFBlocks.PINK_FORCE_FIELD.getId().getPath(), prefix("block/forcefield_white"));
		generated(TFBlocks.BLUE_FORCE_FIELD.getId().getPath(), prefix("block/forcefield_white"));
		generated(TFBlocks.GREEN_FORCE_FIELD.getId().getPath(), prefix("block/forcefield_white"));
		generated(TFBlocks.VIOLET_FORCE_FIELD.getId().getPath(), prefix("block/forcefield_white"));
		generated(TFBlocks.ORANGE_FORCE_FIELD.getId().getPath(), prefix("block/forcefield_white"));
		toBlock(TFBlocks.CINDER_LOG.get());
		toBlock(TFBlocks.CINDER_WOOD.get());
		toBlockModel(TFBlocks.CINDER_FURNACE.get(), new ResourceLocation("block/furnace"));
		//toBlock(TFBlocks.YELLOW_CASTLE_DOOR.get());
		//toBlock(TFBlocks.VIOLET_CASTLE_DOOR.get());
		//toBlock(TFBlocks.PINK_CASTLE_DOOR.get());
		//toBlock(TFBlocks.BLUE_CASTLE_DOOR.get());
		ModelFile think115 = generated("item/think115", prefix("items/think115"));
		ModelFile fullBlockSprinkle = getExistingFile(prefix("block/experiment115_8_8_regenerating"));
		generated(TFBlocks.EXPERIMENT_115.getId().getPath(), prefix("items/experiment_115"))
						.override().predicate(Experiment115Item.THINK, 1).model(think115).end()
						.override().predicate(Experiment115Item.FULL, 1).model(fullBlockSprinkle).end();
		toBlockModel(TFBlocks.TWILIGHT_PORTAL_MINIATURE_STRUCTURE.get(), "miniature/portal");
		toBlockModel(TFBlocks.NAGA_COURTYARD_MINIATURE_STRUCTURE.get(), "miniature/naga_courtyard");
		toBlockModel(TFBlocks.LICH_TOWER_MINIATURE_STRUCTURE.get(), "miniature/lich_tower");
		toBlock(TFBlocks.KNIGHTMETAL_BLOCK.get());
		toBlock(TFBlocks.IRONWOOD_BLOCK.get());
		toBlock(TFBlocks.FIERY_BLOCK.get());
		toBlock(TFBlocks.ARCTIC_FUR_BLOCK.get());
		toBlock(TFBlocks.STEELEAF_BLOCK.get());
		toBlock(TFBlocks.CARMINITE_BLOCK.get());
		toBlock(TFBlocks.MAZESTONE.get());
		toBlock(TFBlocks.MAZESTONE_BRICK.get());
		toBlock(TFBlocks.CUT_MAZESTONE.get());
		toBlock(TFBlocks.DECORATIVE_MAZESTONE.get());
		toBlock(TFBlocks.CRACKED_MAZESTONE.get());
		toBlock(TFBlocks.MOSSY_MAZESTONE.get());
		toBlock(TFBlocks.MAZESTONE_MOSAIC.get());
		toBlock(TFBlocks.MAZESTONE_BORDER.get());
		toBlock(TFBlocks.HEDGE.get());
		toBlock(TFBlocks.ROOT_BLOCK.get());
		toBlock(TFBlocks.LIVEROOT_BLOCK.get());
		toBlock(TFBlocks.MANGROVE_ROOT.get());
		toBlock(TFBlocks.UNCRAFTING_TABLE.get());
		toBlockModel(TFBlocks.NAGA_BOSS_SPAWNER.get(), new ResourceLocation("block/spawner"));
		toBlockModel(TFBlocks.LICH_BOSS_SPAWNER.get(), new ResourceLocation("block/spawner"));
		toBlockModel(TFBlocks.HYDRA_BOSS_SPAWNER.get(), new ResourceLocation("block/spawner"));
		toBlockModel(TFBlocks.UR_GHAST_BOSS_SPAWNER.get(), new ResourceLocation("block/spawner"));
		toBlockModel(TFBlocks.KNIGHT_PHANTOM_BOSS_SPAWNER.get(), new ResourceLocation("block/spawner"));
		toBlockModel(TFBlocks.SNOW_QUEEN_BOSS_SPAWNER.get(), new ResourceLocation("block/spawner"));
		toBlockModel(TFBlocks.MINOSHROOM_BOSS_SPAWNER.get(), new ResourceLocation("block/spawner"));
		toBlockModel(TFBlocks.ALPHA_YETI_BOSS_SPAWNER.get(), new ResourceLocation("block/spawner"));
		toBlockModel(TFBlocks.FINAL_BOSS_BOSS_SPAWNER.get(), new ResourceLocation("block/spawner"));
		toBlock(TFBlocks.FIREFLY_JAR.get());
		toBlock(TFBlocks.FIREFLY_SPAWNER.get());
		toBlock(TFBlocks.CICADA_JAR.get());
		generated(TFBlocks.MOSS_PATCH.getId().getPath(), prefix("block/patch/moss"));
		generated(TFBlocks.MAYAPPLE.getId().getPath(), prefix("block/mayapple"));
		generated(TFBlocks.CLOVER_PATCH.getId().getPath(), prefix("block/patch/clover"));
		generated(TFBlocks.FIDDLEHEAD.getId().getPath(), prefix("block/fiddlehead"));
		generated(TFBlocks.MUSHGLOOM.getId().getPath(), prefix("block/mushgloom"), prefix("block/mushgloom_head"));
		generated(TFBlocks.TORCHBERRY_PLANT.getId().getPath(), prefix("block/torchberry_plant"));
		generated(TFBlocks.ROOT_STRAND.getId().getPath(), prefix("block/root_strand"));
		generated(TFBlocks.FALLEN_LEAVES.getId().getPath(), new ResourceLocation("block/spruce_leaves"));
		toBlockModel(TFBlocks.SMOKER.get(), prefix("block/jet"));
		toBlockModel(TFBlocks.FIRE_JET.get(), prefix("block/jet"));
		toBlock(TFBlocks.ENCASED_SMOKER.get());
		toBlock(TFBlocks.ENCASED_FIRE_JET.get());
		toBlock(TFBlocks.NAGASTONE.get());
		toBlock(TFBlocks.NAGASTONE_HEAD.get());
		toBlock(TFBlocks.NAGASTONE_PILLAR.get());
		toBlock(TFBlocks.MOSSY_NAGASTONE_PILLAR.get());
		toBlock(TFBlocks.CRACKED_NAGASTONE_PILLAR.get());
		toBlock(TFBlocks.ETCHED_NAGASTONE.get());
		toBlock(TFBlocks.MOSSY_ETCHED_NAGASTONE.get());
		toBlock(TFBlocks.CRACKED_ETCHED_NAGASTONE.get());
		toBlock(TFBlocks.NAGASTONE_STAIRS_LEFT.get());
		toBlock(TFBlocks.NAGASTONE_STAIRS_RIGHT.get());
		toBlock(TFBlocks.CRACKED_NAGASTONE_STAIRS_LEFT.get());
		toBlock(TFBlocks.CRACKED_NAGASTONE_STAIRS_RIGHT.get());
		toBlock(TFBlocks.MOSSY_NAGASTONE_STAIRS_LEFT.get());
		toBlock(TFBlocks.MOSSY_NAGASTONE_STAIRS_RIGHT.get());
		toBlockModel(TFBlocks.SPIRAL_BRICKS.get(), prefix("block/spiral_bricks/x_spiral_bottom_right"));
		toBlock(TFBlocks.TWISTED_STONE.get());
		toBlockModel(TFBlocks.TWISTED_STONE_PILLAR.get(), prefix("block/pillar/pillar_inventory"));
		toBlock(TFBlocks.BOLD_STONE_PILLAR.get());
		toBlockModel(TFBlocks.DEATH_TOME_SPAWNER.get(), prefix("block/death_tome_spawner_10"));
		toBlock(TFBlocks.EMPTY_CANOPY_BOOKSHELF.get());
		toBlock(TFBlocks.CANOPY_BOOKSHELF.get());
		toBlockModel(TFBlocks.CANDELABRA.get(), "candelabra_4_5_4_plain");
		//toBlock(TFBlocks.lapis_block.get());

		withExistingParent(TFBlocks.OAK_BANISTER.getId().toString(), prefix("item/banister_item")).texture("texture", "minecraft:block/oak_planks");
		withExistingParent(TFBlocks.SPRUCE_BANISTER.getId().toString(), prefix("item/banister_item")).texture("texture", "minecraft:block/spruce_planks");
		withExistingParent(TFBlocks.BIRCH_BANISTER.getId().toString(), prefix("item/banister_item")).texture("texture", "minecraft:block/birch_planks");
		withExistingParent(TFBlocks.JUNGLE_BANISTER.getId().toString(), prefix("item/banister_item")).texture("texture", "minecraft:block/jungle_planks");
		withExistingParent(TFBlocks.ACACIA_BANISTER.getId().toString(), prefix("item/banister_item")).texture("texture", "minecraft:block/acacia_planks");
		withExistingParent(TFBlocks.DARK_OAK_BANISTER.getId().toString(), prefix("item/banister_item")).texture("texture", "minecraft:block/dark_oak_planks");
		withExistingParent(TFBlocks.CRIMSON_BANISTER.getId().toString(), prefix("item/banister_item")).texture("texture", "minecraft:block/crimson_planks");
		withExistingParent(TFBlocks.WARPED_BANISTER.getId().toString(), prefix("item/banister_item")).texture("texture", "minecraft:block/warped_planks");

		toBlock(TFBlocks.TWILIGHT_OAK_LOG.get());
		toBlock(TFBlocks.STRIPPED_TWILIGHT_OAK_LOG.get());
		toBlock(TFBlocks.TWILIGHT_OAK_WOOD.get());
		toBlock(TFBlocks.STRIPPED_TWILIGHT_OAK_WOOD.get());
		toBlock(TFBlocks.TWILIGHT_OAK_LEAVES.get());
		toBlock(TFBlocks.RAINBOW_OAK_LEAVES.get());
		generated(TFBlocks.RAINBOW_OAK_SAPLING.getId().getPath(), prefix("block/" + TFBlocks.RAINBOW_OAK_SAPLING.getId().getPath()));
		generated(TFBlocks.TWILIGHT_OAK_SAPLING.getId().getPath(), prefix("block/" + TFBlocks.TWILIGHT_OAK_SAPLING.getId().getPath()));
		toBlock(TFBlocks.TWILIGHT_OAK_PLANKS.get());
		toBlock(TFBlocks.TWILIGHT_OAK_STAIRS.get());
		toBlock(TFBlocks.TWILIGHT_OAK_SLAB.get());
		woodenButton(TFBlocks.TWILIGHT_OAK_BUTTON.get(), "twilight_oak");
		woodenFence(TFBlocks.TWILIGHT_OAK_FENCE.get(), "twilight_oak");
		toBlock(TFBlocks.TWILIGHT_OAK_GATE.get());
		toBlock(TFBlocks.TWILIGHT_OAK_PLATE.get());
		toBlockModel(TFBlocks.TWILIGHT_OAK_TRAPDOOR.get(), "twilight_oak_trapdoor_bottom");
		generated(TFBlocks.TWILIGHT_OAK_SIGN.getId().getPath(), prefix("items/" + TFBlocks.TWILIGHT_OAK_SIGN.getId().getPath()));
		withExistingParent(TFBlocks.TWILIGHT_OAK_BANISTER.getId().toString(), prefix("item/banister_item")).texture("texture", "block/wood/planks_twilight_oak_0");
		generated(TFBlocks.TWILIGHT_OAK_DOOR.getId().getPath(), prefix("items/" + TFBlocks.TWILIGHT_OAK_DOOR.getId().getPath()));

		toBlock(TFBlocks.CANOPY_LOG.get());
		toBlock(TFBlocks.STRIPPED_CANOPY_LOG.get());
		toBlock(TFBlocks.CANOPY_WOOD.get());
		toBlock(TFBlocks.STRIPPED_CANOPY_WOOD.get());
		toBlock(TFBlocks.CANOPY_LEAVES.get());
		generated(TFBlocks.CANOPY_SAPLING.getId().getPath(), prefix("block/" + TFBlocks.CANOPY_SAPLING.getId().getPath()));
		toBlock(TFBlocks.CANOPY_PLANKS.get());
		toBlock(TFBlocks.CANOPY_STAIRS.get());
		toBlock(TFBlocks.CANOPY_SLAB.get());
		woodenButton(TFBlocks.CANOPY_BUTTON.get(), "canopy");
		woodenFence(TFBlocks.CANOPY_FENCE.get(), "canopy");
		toBlock(TFBlocks.CANOPY_GATE.get());
		toBlock(TFBlocks.CANOPY_PLATE.get());
		toBlockModel(TFBlocks.CANOPY_TRAPDOOR.get(), "canopy_trapdoor_bottom");
		generated(TFBlocks.CANOPY_SIGN.getId().getPath(), prefix("items/" + TFBlocks.CANOPY_SIGN.getId().getPath()));
		withExistingParent(TFBlocks.CANOPY_BANISTER.getId().toString(), prefix("item/banister_item")).texture("texture", "block/wood/planks_canopy_0");
		generated(TFBlocks.CANOPY_DOOR.getId().getPath(), prefix("items/" + TFBlocks.CANOPY_DOOR.getId().getPath()));

		toBlock(TFBlocks.MANGROVE_LOG.get());
		toBlock(TFBlocks.STRIPPED_MANGROVE_LOG.get());
		toBlock(TFBlocks.MANGROVE_WOOD.get());
		toBlock(TFBlocks.STRIPPED_MANGROVE_WOOD.get());
		toBlock(TFBlocks.MANGROVE_LEAVES.get());
		generated(TFBlocks.MANGROVE_SAPLING.getId().getPath(), prefix("block/" + TFBlocks.MANGROVE_SAPLING.getId().getPath()));
		toBlock(TFBlocks.MANGROVE_PLANKS.get());
		toBlock(TFBlocks.MANGROVE_STAIRS.get());
		toBlock(TFBlocks.MANGROVE_SLAB.get());
		woodenButton(TFBlocks.MANGROVE_BUTTON.get(), "mangrove");
		woodenFence(TFBlocks.MANGROVE_FENCE.get(), "mangrove");
		toBlock(TFBlocks.MANGROVE_GATE.get());
		toBlock(TFBlocks.MANGROVE_PLATE.get());
		toBlockModel(TFBlocks.MANGROVE_TRAPDOOR.get(), "mangrove_trapdoor_bottom");
		generated(TFBlocks.MANGROVE_SIGN.getId().getPath(), prefix("items/" + TFBlocks.MANGROVE_SIGN.getId().getPath()));
		withExistingParent(TFBlocks.MANGROVE_BANISTER.getId().toString(), prefix("item/banister_item")).texture("texture", "block/wood/planks_mangrove_0");
		generated(TFBlocks.MANGROVE_DOOR.getId().getPath(), prefix("items/" + TFBlocks.MANGROVE_DOOR.getId().getPath()));

		toBlock(TFBlocks.DARK_LOG.get());
		toBlock(TFBlocks.STRIPPED_DARK_LOG.get());
		toBlock(TFBlocks.DARK_WOOD.get());
		toBlock(TFBlocks.STRIPPED_DARK_WOOD.get());
		toBlock(TFBlocks.DARK_LEAVES.get());
		generated(TFBlocks.DARKWOOD_SAPLING.getId().getPath(), prefix("block/" + TFBlocks.DARKWOOD_SAPLING.getId().getPath()));
		toBlock(TFBlocks.DARK_PLANKS.get());
		toBlock(TFBlocks.DARK_STAIRS.get());
		toBlock(TFBlocks.DARK_SLAB.get());
		woodenButton(TFBlocks.DARK_BUTTON.get(), "darkwood");
		woodenFence(TFBlocks.DARK_FENCE.get(), "darkwood");
		toBlock(TFBlocks.DARK_GATE.get());
		toBlock(TFBlocks.DARK_PLATE.get());
		toBlockModel(TFBlocks.DARK_TRAPDOOR.get(), "dark_trapdoor_bottom");
		generated(TFBlocks.DARKWOOD_SIGN.getId().getPath(), prefix("items/" + TFBlocks.DARKWOOD_SIGN.getId().getPath()));
		withExistingParent(TFBlocks.DARKWOOD_BANISTER.getId().toString(), prefix("item/banister_item")).texture("texture", "block/wood/planks_darkwood_0");
		generated(TFBlocks.DARK_DOOR.getId().getPath(), prefix("items/" + TFBlocks.DARK_DOOR.getId().getPath()));
		generated(TFBlocks.HOLLOW_OAK_SAPLING.getId().getPath(), prefix("block/" + TFBlocks.HOLLOW_OAK_SAPLING.getId().getPath()));
		
		toBlock(TFBlocks.TIME_LOG.get());
		toBlock(TFBlocks.STRIPPED_TIME_LOG.get());
		toBlock(TFBlocks.TIME_WOOD.get());
		toBlock(TFBlocks.STRIPPED_TIME_WOOD.get());
		toBlock(TFBlocks.TIME_LOG_CORE.get());
		toBlock(TFBlocks.TIME_LEAVES.get());
		generated(TFBlocks.TIME_SAPLING.getId().getPath(), prefix("block/" + TFBlocks.TIME_SAPLING.getId().getPath()));
		toBlock(TFBlocks.TIME_PLANKS.get());
		toBlock(TFBlocks.TIME_STAIRS.get());
		toBlock(TFBlocks.TIME_SLAB.get());
		woodenButton(TFBlocks.TIME_BUTTON.get(), "time");
		woodenFence(TFBlocks.TIME_FENCE.get(), "time");
		toBlock(TFBlocks.TIME_GATE.get());
		toBlock(TFBlocks.TIME_PLATE.get());
		toBlockModel(TFBlocks.TIME_TRAPDOOR.get(), "time_trapdoor_bottom");
		generated(TFBlocks.TIME_SIGN.getId().getPath(), prefix("items/" + TFBlocks.TIME_SIGN.getId().getPath()));
		withExistingParent(TFBlocks.TIME_BANISTER.getId().toString(), prefix("item/banister_item")).texture("texture", "block/wood/planks_time_0");
		generated(TFBlocks.TIME_DOOR.getId().getPath(), prefix("items/" + TFBlocks.TIME_DOOR.getId().getPath()));

		toBlock(TFBlocks.TRANSFORMATION_LOG.get());
		toBlock(TFBlocks.STRIPPED_TRANSFORMATION_LOG.get());
		toBlock(TFBlocks.TRANSFORMATION_WOOD.get());
		toBlock(TFBlocks.STRIPPED_TRANSFORMATION_WOOD.get());
		toBlock(TFBlocks.TRANSFORMATION_LOG_CORE.get());
		toBlock(TFBlocks.TRANSFORMATION_LEAVES.get());
		generated(TFBlocks.TRANSFORMATION_SAPLING.getId().getPath(), prefix("block/" + TFBlocks.TRANSFORMATION_SAPLING.getId().getPath()));
		toBlock(TFBlocks.TRANSFORMATION_PLANKS.get());
		toBlock(TFBlocks.TRANSFORMATION_STAIRS.get());
		toBlock(TFBlocks.TRANSFORMATION_SLAB.get());
		woodenButton(TFBlocks.TRANSFORMATION_BUTTON.get(), "trans");
		woodenFence(TFBlocks.TRANSFORMATION_FENCE.get(), "trans");
		toBlock(TFBlocks.TRANSFORMATION_GATE.get());
		toBlock(TFBlocks.TRANSFORMATION_PLATE.get());
		toBlockModel(TFBlocks.TRANSFORMATION_TRAPDOOR.get(), "transformation_trapdoor_bottom");
		generated(TFBlocks.TRANSFORMATION_SIGN.getId().getPath(), prefix("items/" + TFBlocks.TRANSFORMATION_SIGN.getId().getPath()));
		withExistingParent(TFBlocks.TRANSFORMATION_BANISTER.getId().toString(), prefix("item/banister_item")).texture("texture", "block/wood/planks_trans_0");
		generated(TFBlocks.TRANSFORMATION_DOOR.getId().getPath(), prefix("items/" + TFBlocks.TRANSFORMATION_DOOR.getId().getPath()));

		toBlock(TFBlocks.MINING_LOG.get());
		toBlock(TFBlocks.STRIPPED_MINING_LOG.get());
		toBlock(TFBlocks.MINING_WOOD.get());
		toBlock(TFBlocks.STRIPPED_MINING_WOOD.get());
		toBlock(TFBlocks.MINING_LOG_CORE.get());
		toBlock(TFBlocks.MINING_LEAVES.get());
		generated(TFBlocks.MINING_SAPLING.getId().getPath(), prefix("block/" + TFBlocks.MINING_SAPLING.getId().getPath()));
		toBlock(TFBlocks.MINING_PLANKS.get());
		toBlock(TFBlocks.MINING_STAIRS.get());
		toBlock(TFBlocks.MINING_SLAB.get());
		woodenButton(TFBlocks.MINING_BUTTON.get(), "mine");
		woodenFence(TFBlocks.MINING_FENCE.get(), "mine");
		toBlock(TFBlocks.MINING_GATE.get());
		toBlock(TFBlocks.MINING_PLATE.get());
		toBlockModel(TFBlocks.MINING_TRAPDOOR.get(), "mining_trapdoor_bottom");
		generated(TFBlocks.MINING_SIGN.getId().getPath(), prefix("items/" + TFBlocks.MINING_SIGN.getId().getPath()));
		withExistingParent(TFBlocks.MINING_BANISTER.getId().toString(), prefix("item/banister_item")).texture("texture", "block/wood/planks_mine_0");
		generated(TFBlocks.MINING_DOOR.getId().getPath(), prefix("items/" + TFBlocks.MINING_DOOR.getId().getPath()));

		toBlock(TFBlocks.SORTING_LOG.get());
		toBlock(TFBlocks.STRIPPED_SORTING_LOG.get());
		toBlock(TFBlocks.SORTING_WOOD.get());
		toBlock(TFBlocks.STRIPPED_SORTING_WOOD.get());
		toBlock(TFBlocks.SORTING_LOG_CORE.get());
		toBlock(TFBlocks.SORTING_LEAVES.get());
		generated(TFBlocks.SORTING_SAPLING.getId().getPath(), prefix("block/" + TFBlocks.SORTING_SAPLING.getId().getPath()));
		toBlock(TFBlocks.SORTING_PLANKS.get());
		toBlock(TFBlocks.SORTING_STAIRS.get());
		toBlock(TFBlocks.SORTING_SLAB.get());
		woodenButton(TFBlocks.SORTING_BUTTON.get(), "sort");
		woodenFence(TFBlocks.SORTING_FENCE.get(), "sort");
		toBlock(TFBlocks.SORTING_GATE.get());
		toBlock(TFBlocks.SORTING_PLATE.get());
		toBlockModel(TFBlocks.SORTING_TRAPDOOR.get(), "sorting_trapdoor_bottom");
		generated(TFBlocks.SORTING_SIGN.getId().getPath(), prefix("items/" + TFBlocks.SORTING_SIGN.getId().getPath()));
		withExistingParent(TFBlocks.SORTING_BANISTER.getId().toString(), prefix("item/banister_item")).texture("texture", "block/wood/planks_sort_0");
		generated(TFBlocks.SORTING_DOOR.getId().getPath(), prefix("items/" + TFBlocks.SORTING_DOOR.getId().getPath()));

		hollowLog(TFBlocks.HOLLOW_OAK_LOG_HORIZONTAL);
		hollowLog(TFBlocks.HOLLOW_SPRUCE_LOG_HORIZONTAL);
		hollowLog(TFBlocks.HOLLOW_BIRCH_LOG_HORIZONTAL);
		hollowLog(TFBlocks.HOLLOW_JUNGLE_LOG_HORIZONTAL);
		hollowLog(TFBlocks.HOLLOW_ACACIA_LOG_HORIZONTAL);
		hollowLog(TFBlocks.HOLLOW_DARK_OAK_LOG_HORIZONTAL);
		hollowLog(TFBlocks.HOLLOW_CRIMSON_STEM_HORIZONTAL);
		hollowLog(TFBlocks.HOLLOW_WARPED_STEM_HORIZONTAL);

		hollowLog(TFBlocks.HOLLOW_TWILIGHT_OAK_LOG_HORIZONTAL);
		hollowLog(TFBlocks.HOLLOW_CANOPY_LOG_HORIZONTAL);
		hollowLog(TFBlocks.HOLLOW_MANGROVE_LOG_HORIZONTAL);
		hollowLog(TFBlocks.HOLLOW_DARK_LOG_HORIZONTAL);
		hollowLog(TFBlocks.HOLLOW_TIME_LOG_HORIZONTAL);
		hollowLog(TFBlocks.HOLLOW_TRANSFORMATION_LOG_HORIZONTAL);
		hollowLog(TFBlocks.HOLLOW_MINING_LOG_HORIZONTAL);
		hollowLog(TFBlocks.HOLLOW_SORTING_LOG_HORIZONTAL);

		singleTex(TFItems.NAGA_SCALE);
		singleTex(TFItems.NAGA_CHESTPLATE);
		singleTex(TFItems.NAGA_LEGGINGS);
		singleTexTool(TFItems.TWILIGHT_SCEPTER);
		singleTexTool(TFItems.LIFEDRAIN_SCEPTER);
		singleTexTool(TFItems.ZOMBIE_SCEPTER);
		singleTexTool(TFItems.FORTIFICATION_SCEPTER);
		singleTex(TFItems.ORE_METER);
		singleTex(TFItems.FILLED_MAGIC_MAP);
		singleTex(TFItems.FILLED_MAZE_MAP);
		biggerTex(TFItems.FILLED_ORE_MAP, prefix("items/" + TFItems.FILLED_ORE_MAP.getId().getPath()));
		singleTex(TFItems.RAVEN_FEATHER);
		singleTex(TFItems.MAGIC_MAP_FOCUS);
		singleTex(TFItems.MAZE_MAP_FOCUS);
		singleTex(TFItems.LIVEROOT);
		singleTex(TFItems.RAW_IRONWOOD);
		singleTex(TFItems.IRONWOOD_INGOT);
		singleTex(TFItems.IRONWOOD_HELMET);
		singleTex(TFItems.IRONWOOD_CHESTPLATE);
		singleTex(TFItems.IRONWOOD_LEGGINGS);
		singleTex(TFItems.IRONWOOD_BOOTS);
		singleTexTool(TFItems.IRONWOOD_SWORD);
		singleTexTool(TFItems.IRONWOOD_PICKAXE);
		singleTexTool(TFItems.IRONWOOD_AXE);
		singleTexTool(TFItems.IRONWOOD_SHOVEL);
		singleTexTool(TFItems.IRONWOOD_HOE);
		singleTex(TFItems.TORCHBERRIES);
		singleTex(TFItems.RAW_VENISON);
		singleTex(TFItems.COOKED_VENISON);
		singleTex(TFItems.HYDRA_CHOP);
		singleTex(TFItems.FIERY_BLOOD);
		singleTex(TFItems.FIERY_TEARS);
		singleTex(TFItems.FIERY_INGOT);
		singleTex(TFItems.FIERY_HELMET);
		singleTex(TFItems.FIERY_CHESTPLATE);
		singleTex(TFItems.FIERY_LEGGINGS);
		singleTex(TFItems.FIERY_BOOTS);
		singleTexTool(TFItems.FIERY_SWORD);
		singleTexTool(TFItems.FIERY_PICKAXE);
		singleTex(TFItems.STEELEAF_INGOT);
		singleTex(TFItems.STEELEAF_HELMET);
		singleTex(TFItems.STEELEAF_CHESTPLATE);
		singleTex(TFItems.STEELEAF_LEGGINGS);
		singleTex(TFItems.STEELEAF_BOOTS);
		singleTexTool(TFItems.STEELEAF_SWORD);
		singleTexTool(TFItems.STEELEAF_PICKAXE);
		singleTexTool(TFItems.STEELEAF_AXE);
		singleTexTool(TFItems.STEELEAF_SHOVEL);
		singleTexTool(TFItems.STEELEAF_HOE);
		singleTexTool(TFItems.DIAMOND_MINOTAUR_AXE);
		singleTexTool(TFItems.GOLDEN_MINOTAUR_AXE);
		singleTexTool(TFItems.MAZEBREAKER_PICKAXE);
		singleTex(TFItems.TRANSFORMATION_POWDER);
		singleTex(TFItems.RAW_MEEF);
		singleTex(TFItems.COOKED_MEEF);
		singleTex(TFItems.MEEF_STROGANOFF);
		singleTex(TFItems.MAZE_WAFER);
		singleTex(TFItems.MAGIC_MAP);
		singleTex(TFItems.MAZE_MAP);
		biggerTex(TFItems.ORE_MAP, prefix("items/" + TFItems.ORE_MAP.getId().getPath()));
		ModelFile magnetPull1 = generated("ore_magnet_pulling_1", prefix("items/ore_magnet_pulling_1"));
		ModelFile magnetPull2 = generated("ore_magnet_pulling_2", prefix("items/ore_magnet_pulling_2"));
		singleTex(TFItems.ORE_MAGNET)
				.override().predicate(new ResourceLocation("pulling"), 1).predicate(new ResourceLocation("pull"), (float) 0.5).model(magnetPull1).end()
				.override().predicate(new ResourceLocation("pulling"), 1).predicate(new ResourceLocation("pull"), 1).model(magnetPull2).end();
		singleTexTool(TFItems.CRUMBLE_HORN);
		singleTexTool(TFItems.PEACOCK_FEATHER_FAN);
		ModelFile queenAlt = tool("moonworm_queen_alt", prefix("items/moonworm_queen_alt"));
		singleTexTool(TFItems.MOONWORM_QUEEN).override().predicate(prefix("alt"), 1).model(queenAlt).end();
		singleTex(TFItems.CHARM_OF_KEEPING_1);
		singleTex(TFItems.CHARM_OF_KEEPING_2);
		singleTex(TFItems.CHARM_OF_KEEPING_3);
		singleTex(TFItems.CHARM_OF_LIFE_1);
		singleTex(TFItems.CHARM_OF_LIFE_2);
		singleTex(TFItems.TOWER_KEY);
		generated(TFItems.BORER_ESSENCE.getId().getPath(), prefix("items/" + TFItems.BORER_ESSENCE.getId().getPath()), prefix("items/borer_essence_particles"));
		singleTex(TFItems.CARMINITE);
		singleTex(TFItems.ARMOR_SHARD);
		singleTex(TFItems.ARMOR_SHARD_CLUSTER);
		singleTex(TFItems.KNIGHTMETAL_INGOT);
		biggerTex(TFItems.KNIGHTMETAL_HELMET, prefix("items/" + TFItems.KNIGHTMETAL_HELMET.getId().getPath()));
		singleTex(TFItems.KNIGHTMETAL_CHESTPLATE);
		singleTex(TFItems.KNIGHTMETAL_LEGGINGS);
		singleTex(TFItems.KNIGHTMETAL_BOOTS);
		singleTexTool(TFItems.KNIGHTMETAL_SWORD);
		singleTexTool(TFItems.KNIGHTMETAL_PICKAXE);
		singleTexTool(TFItems.KNIGHTMETAL_AXE);
		singleTex(TFItems.KNIGHTMETAL_RING);
		singleTex(TFItems.PHANTOM_HELMET);
		singleTex(TFItems.PHANTOM_CHESTPLATE);
		singleTex(TFItems.LAMP_OF_CINDERS);
		singleTex(TFItems.ALPHA_YETI_FUR);
		biggerTex(TFItems.YETI_HELMET, prefix("items/" + TFItems.YETI_HELMET.getId().getPath()));
		singleTex(TFItems.YETI_CHESTPLATE);
		singleTex(TFItems.YETI_LEGGINGS);
		singleTex(TFItems.YETI_BOOTS);
		singleTex(TFItems.ICE_BOMB);
		singleTex(TFItems.ARCTIC_FUR);
		arcticArmorTex(TFItems.ARCTIC_HELMET);
		arcticArmorTex(TFItems.ARCTIC_CHESTPLATE);
		arcticArmorTex(TFItems.ARCTIC_LEGGINGS);
		arcticArmorTex(TFItems.ARCTIC_BOOTS);
		singleTex(TFItems.MAGIC_BEANS);
		ModelFile triplePulling0 = bowItem("triple_bow_pulling_0", prefix("items/triple_bow_pulling_0"));
		ModelFile triplePulling1 = bowItem("triple_bow_pulling_1", prefix("items/triple_bow_pulling_1"));
		ModelFile triplePulling2 = bowItem("triple_bow_pulling_2", prefix("items/triple_bow_pulling_2"));
		bowTex(TFItems.TRIPLE_BOW, triplePulling0, triplePulling1, triplePulling2);
		ModelFile seekerPulling0 = bowItem("seeker_bow_pulling_0", prefix("items/seeker_bow_pulling_0"));
		ModelFile seekerPulling1 = bowItem("seeker_bow_pulling_1", prefix("items/seeker_bow_pulling_1"));
		ModelFile seekerPulling2 = bowItem("seeker_bow_pulling_2", prefix("items/seeker_bow_pulling_2"));
		bowTex(TFItems.SEEKER_BOW, seekerPulling0, seekerPulling1, seekerPulling2);
		ModelFile icePulling0 = bowItem("ice_bow_pulling_0", prefix("items/ice_bow_solid_pulling_0"), prefix("items/ice_bow_clear_pulling_0"));
		ModelFile icePulling1 = bowItem("ice_bow_pulling_1", prefix("items/ice_bow_solid_pulling_1"), prefix("items/ice_bow_clear_pulling_1"));
		ModelFile icePulling2 = bowItem("ice_bow_pulling_2", prefix("items/ice_bow_solid_pulling_2"), prefix("items/ice_bow_clear_pulling_2"));
		iceBowTex(TFItems.ICE_BOW, icePulling0, icePulling1, icePulling2);
		ModelFile enderPulling0 = bowItem("ender_bow_pulling_0", prefix("items/ender_bow_pulling_0"));
		ModelFile enderPulling1 = bowItem("ender_bow_pulling_1", prefix("items/ender_bow_pulling_1"));
		ModelFile enderPulling2 = bowItem("ender_bow_pulling_2", prefix("items/ender_bow_pulling_2"));
		bowTex(TFItems.ENDER_BOW, enderPulling0, enderPulling1, enderPulling2);
		tool(TFItems.ICE_SWORD.getId().getPath(), prefix("items/ice_sword_solid"), prefix("items/ice_sword_clear"));
		tool(TFItems.GLASS_SWORD.getId().getPath(), prefix("items/glass_sword_solid"), prefix("items/glass_sword_clear"));
		ModelFile chainThrown = biggerTexString("block_and_chain_thrown", prefix("items/block_and_chain_thrown"));
		biggerTex(TFItems.BLOCK_AND_CHAIN, prefix("items/block_and_chain")).override().predicate(prefix("thrown"), 1).model(chainThrown).end();
		ModelFile cubeThrown = biggerTexString("cube_of_annihilation_thrown", prefix("items/cube_of_annihilation_thrown"));
		biggerTex(TFItems.CUBE_OF_ANNIHILATION, prefix("items/cube_of_annihilation")).override().predicate(prefix("thrown"), 1).model(cubeThrown).end();
		singleTex(TFItems.CUBE_TALISMAN);
		//moon dial is a big boi
		ModelFile full = phaseTex("moon_dial_full", prefix("items/moon_dial/full"));
		ModelFile waning_gib = phaseTex("moon_dial_waning_gib", prefix("items/moon_dial/waning_gibbous"));
		ModelFile quarter3 = phaseTex("moon_dial_quarter3", prefix("items/moon_dial/third_quarter"));
		ModelFile waning_cres = phaseTex("moon_dial_waning_cres", prefix("items/moon_dial/waning_cresent"));
		ModelFile unlit = phaseTex("moon_dial_new", prefix("items/moon_dial/new")); //cant use new for the name lmao
		ModelFile waxing_cres = phaseTex("moon_dial_waxing_cres", prefix("items/moon_dial/waxing_cresent"));
		ModelFile quarter1 = phaseTex("moon_dial_quarter1", prefix("items/moon_dial/first_quarter"));
		ModelFile waxing_gib = phaseTex("moon_dial_waxing_gib", prefix("items/moon_dial/waxing_gibbous"));
		phaseTex(TFItems.MOON_DIAL.getId().getPath(), prefix("items/moon_dial/full"))
				.override().predicate(new ResourceLocation("phase"), 0).model(full).end()
				.override().predicate(new ResourceLocation("phase"), 0.125F).model(waning_gib).end()
				.override().predicate(new ResourceLocation("phase"), 0.25F).model(quarter3).end()
				.override().predicate(new ResourceLocation("phase"), 0.375F).model(waning_cres).end()
				.override().predicate(new ResourceLocation("phase"), 0.5F).model(unlit).end()
				.override().predicate(new ResourceLocation("phase"), 0.625F).model(waxing_cres).end()
				.override().predicate(new ResourceLocation("phase"), 0.75F).model(quarter1).end()
				.override().predicate(new ResourceLocation("phase"), 0.875F).model(waxing_gib).end();

		ModelFile fill1 = generated("brittle_flask_0", prefix("items/brittle_potion_flask_1"), prefix("items/brittle_potion_flask_labelled"));
		ModelFile fill2 = generated("brittle_flask_1", prefix("items/brittle_potion_flask_2"), prefix("items/brittle_potion_flask_labelled"));
		ModelFile fill3 = generated("brittle_flask_2", prefix("items/brittle_potion_flask_3"), prefix("items/brittle_potion_flask_labelled"));
		ModelFile fill4 = generated("brittle_flask_3", prefix("items/brittle_potion_flask_4"), prefix("items/brittle_potion_flask_labelled"));
		ModelFile splintered = generated("brittle_flask_splintered", prefix("items/brittle_potion_flask_splintered"));
		ModelFile fill1_splintered = generated("brittle_flask_0_splintered", prefix("items/brittle_potion_flask_1"), prefix("items/brittle_potion_flask_splintered"));
		ModelFile fill2_splintered = generated("brittle_flask_1_splintered", prefix("items/brittle_potion_flask_2"), prefix("items/brittle_potion_flask_splintered"));
		ModelFile fill3_splintered = generated("brittle_flask_2_splintered", prefix("items/brittle_potion_flask_3"), prefix("items/brittle_potion_flask_splintered"));
		ModelFile fill4_splintered = generated("brittle_flask_3_splintered", prefix("items/brittle_potion_flask_4"), prefix("items/brittle_potion_flask_splintered"));
		ModelFile cracked = generated("brittle_flask_cracked", prefix("items/brittle_potion_flask_cracked"));
		ModelFile fill1_cracked = generated("brittle_flask_0_cracked", prefix("items/brittle_potion_flask_1"), prefix("items/brittle_potion_flask_cracked"));
		ModelFile fill2_cracked = generated("brittle_flask_1_cracked", prefix("items/brittle_potion_flask_2"), prefix("items/brittle_potion_flask_cracked"));
		ModelFile fill3_cracked = generated("brittle_flask_2_cracked", prefix("items/brittle_potion_flask_3"), prefix("items/brittle_potion_flask_cracked"));
		ModelFile fill4_cracked = generated("brittle_flask_3_cracked", prefix("items/brittle_potion_flask_4"), prefix("items/brittle_potion_flask_cracked"));
		ModelFile damaged = generated("brittle_flask_damaged", prefix("items/brittle_potion_flask_damaged"));
		ModelFile fill1_damaged = generated("brittle_flask_0_damaged", prefix("items/brittle_potion_flask_1"), prefix("items/brittle_potion_flask_damaged"));
		ModelFile fill2_damaged = generated("brittle_flask_1_damaged", prefix("items/brittle_potion_flask_2"), prefix("items/brittle_potion_flask_damaged"));
		ModelFile fill3_damaged = generated("brittle_flask_2_damaged", prefix("items/brittle_potion_flask_3"), prefix("items/brittle_potion_flask_damaged"));
		ModelFile fill4_damaged = generated("brittle_flask_3_damaged", prefix("items/brittle_potion_flask_4"), prefix("items/brittle_potion_flask_damaged"));
		
		generated(TFItems.BRITTLE_FLASK.getId().getPath(), prefix("block/stone_twist/twist_blank"), prefix("items/brittle_potion_flask"))
				.override().predicate(prefix("potion_level"), 1).model(fill1).end()
				.override().predicate(prefix("potion_level"), 2).model(fill2).end()
				.override().predicate(prefix("potion_level"), 3).model(fill3).end()
				.override().predicate(prefix("potion_level"), 4).model(fill4).end()
				.override().predicate(prefix("potion_level"), 0).predicate(prefix("breakage"), 1).model(splintered).end()
				.override().predicate(prefix("potion_level"), 1).predicate(prefix("breakage"), 1).model(fill1_splintered).end()
				.override().predicate(prefix("potion_level"), 2).predicate(prefix("breakage"), 1).model(fill2_splintered).end()
				.override().predicate(prefix("potion_level"), 3).predicate(prefix("breakage"), 1).model(fill3_splintered).end()
				.override().predicate(prefix("potion_level"), 4).predicate(prefix("breakage"), 1).model(fill4_splintered).end()
				.override().predicate(prefix("potion_level"), 0).predicate(prefix("breakage"), 2).model(cracked).end()
				.override().predicate(prefix("potion_level"), 1).predicate(prefix("breakage"), 2).model(fill1_cracked).end()
				.override().predicate(prefix("potion_level"), 2).predicate(prefix("breakage"), 2).model(fill2_cracked).end()
				.override().predicate(prefix("potion_level"), 3).predicate(prefix("breakage"), 2).model(fill3_cracked).end()
				.override().predicate(prefix("potion_level"), 4).predicate(prefix("breakage"), 2).model(fill4_cracked).end()
				.override().predicate(prefix("potion_level"), 0).predicate(prefix("breakage"), 3).model(damaged).end()
				.override().predicate(prefix("potion_level"), 1).predicate(prefix("breakage"), 3).model(fill1_damaged).end()
				.override().predicate(prefix("potion_level"), 2).predicate(prefix("breakage"), 3).model(fill2_damaged).end()
				.override().predicate(prefix("potion_level"), 3).predicate(prefix("breakage"), 3).model(fill3_damaged).end()
				.override().predicate(prefix("potion_level"), 4).predicate(prefix("breakage"), 3).model(fill4_damaged).end();

		ModelFile gfill1 = generated("greater_flask_0", prefix("items/greater_potion_flask_1"), prefix("items/greater_potion_flask"));
		ModelFile gfill2 = generated("greater_flask_1", prefix("items/greater_potion_flask_2"), prefix("items/greater_potion_flask"));
		ModelFile gfill3 = generated("greater_flask_2", prefix("items/greater_potion_flask_3"), prefix("items/greater_potion_flask"));
		ModelFile gfill4 = generated("greater_flask_3", prefix("items/greater_potion_flask_4"), prefix("items/greater_potion_flask"));

		generated(TFItems.GREATER_FLASK.getId().getPath(), prefix("block/stone_twist/twist_blank"), prefix("items/greater_potion_flask"))
				.override().predicate(prefix("potion_level"), 1).model(gfill1).end()
				.override().predicate(prefix("potion_level"), 2).model(gfill2).end()
				.override().predicate(prefix("potion_level"), 3).model(gfill3).end()
				.override().predicate(prefix("potion_level"), 4).model(gfill4).end();

		singleTex(TFItems.MUSIC_DISC_FINDINGS);
		singleTex(TFItems.MUSIC_DISC_HOME);
		singleTex(TFItems.MUSIC_DISC_MAKER);
		singleTex(TFItems.MUSIC_DISC_MOTION);
		singleTex(TFItems.MUSIC_DISC_RADIANCE);
		singleTex(TFItems.MUSIC_DISC_STEPS);
		singleTex(TFItems.MUSIC_DISC_SUPERSTITIOUS);
		singleTex(TFItems.MUSIC_DISC_THREAD);
		singleTex(TFItems.MUSIC_DISC_WAYFARER);

		generated(TFItems.NAGA_BANNER_PATTERN.getId().getPath(), prefix("items/tf_banner_pattern"));
		generated(TFItems.LICH_BANNER_PATTERN.getId().getPath(), prefix("items/tf_banner_pattern"));
		generated(TFItems.MINOSHROOM_BANNER_PATTERN.getId().getPath(), prefix("items/tf_banner_pattern"));
		generated(TFItems.HYDRA_BANNER_PATTERN.getId().getPath(), prefix("items/tf_banner_pattern"));
		generated(TFItems.KNIGHT_PHANTOM_BANNER_PATTERN.getId().getPath(), prefix("items/tf_banner_pattern"));
		generated(TFItems.UR_GHAST_BANNER_PATTERN.getId().getPath(), prefix("items/tf_banner_pattern"));
		generated(TFItems.ALPHA_YETI_BANNER_PATTERN.getId().getPath(), prefix("items/tf_banner_pattern"));
		generated(TFItems.SNOW_QUEEN_BANNER_PATTERN.getId().getPath(), prefix("items/tf_banner_pattern"));
		generated(TFItems.QUEST_RAM_BANNER_PATTERN.getId().getPath(), prefix("items/tf_banner_pattern"));

		//compat stuff
		ModelFile freshBook = generated("logbook_0", prefix("items/logbook/fresh"));
		ModelFile usedBook = generated("logbook_1", prefix("items/logbook/used"));
		ModelFile smortBook = generated("logbook_2", prefix("items/logbook/knowledgable"));
		ModelFile masterBook = generated("logbook_3", prefix("items/logbook/supreme"));
		generated("logbook", prefix("items/logbook/fresh"))
				.override().predicate(new ResourceLocation("completion"), 0).model(freshBook).end()
				.override().predicate(new ResourceLocation("completion"), 0.333F).model(usedBook).end()
				.override().predicate(new ResourceLocation("completion"), 0.666F).model(smortBook).end()
				.override().predicate(new ResourceLocation("completion"), 1).model(masterBook).end();

		withExistingParent("shader", prefix("item/lunchcase"))
				.texture("missing", prefix("block/fluffy_cloud"))
				.texture("face", prefix("block/lunchbox_face"))
				.texture("side", prefix("block/lunchbox_side"));

		withExistingParent("shader_bag_common", prefix("item/shader"));
		withExistingParent("shader_bag_uncommon", prefix("item/shader"));
		withExistingParent("shader_bag_rare", prefix("item/shader"));
		withExistingParent("shader_bag_epic", prefix("item/shader"));
		withExistingParent("shader_bag_ie_masterwork", prefix("item/shader"));
		withExistingParent("shader_bag_twilight", prefix("item/shader"));

		//these models are used as references in other things, they dont have actual items
		generated("trophy", prefix("items/trophy"));
		generated("trophy_minor", prefix("items/trophy_minor"));
		generated("trophy_quest", prefix("items/trophy_quest"));
		generated("shield", prefix("items/lich_shield_frame"), prefix("items/lich_shield_fill"));
	}

	private ItemModelBuilder generated(String name, ResourceLocation... layers) {
		ItemModelBuilder builder = withExistingParent(name, "item/generated");
		for (int i = 0; i < layers.length; i++) {
			builder = builder.texture("layer" + i, layers[i]);
		}
		return builder;
	}

	private ItemModelBuilder singleTexTool(RegistryObject<Item> item) {
		return tool(item.getId().getPath(), prefix("items/" + item.getId().getPath()));
	}

	private ItemModelBuilder tool(String name, ResourceLocation... layers) {
		ItemModelBuilder builder = withExistingParent(name, "item/handheld");
		for (int i = 0; i < layers.length; i++) {
			builder = builder.texture("layer" + i, layers[i]);
		}
		return builder;
	}

	private ItemModelBuilder singleTex(RegistryObject<Item> item) {
		return generated(item.getId().getPath(), prefix("items/" + item.getId().getPath()));
	}

	private ItemModelBuilder biggerTex(RegistryObject<Item> item, ResourceLocation... layers) {
		ItemModelBuilder builder = withExistingParent(item.getId().getPath(), "twilightforest:item/util/overlap_gui");
		for (int i = 0; i < layers.length; i++) {
			builder = builder.texture("layer" + i, layers[i]);
		}
		return builder;
	}

	private ItemModelBuilder biggerTexString(String name, ResourceLocation... layers) {
		ItemModelBuilder builder = withExistingParent(name, "twilightforest:item/util/overlap_gui");
		for (int i = 0; i < layers.length; i++) {
			builder = builder.texture("layer" + i, layers[i]);
		}
		return builder;
	}

	private ItemModelBuilder arcticArmorTex(RegistryObject<Item> item) {
		return generated(item.getId().getPath(), prefix("items/" + item.getId().getPath()), prefix("items/" + item.getId().getPath() + "_0"));
	}

	private ItemModelBuilder bowItem(String name, ResourceLocation... layers) {
		ItemModelBuilder builder = withExistingParent(name, "item/bow");
		for (int i = 0; i < layers.length; i++) {
			builder = builder.texture("layer" + i, layers[i]);
		}
		return builder;
	}

	private ItemModelBuilder bowTex(RegistryObject<Item> item, ModelFile pull0, ModelFile pull1, ModelFile pull2) {
		return bowItem(item.getId().getPath(), prefix("items/" + item.getId().getPath()))
				.override().predicate(new ResourceLocation("pulling"), 1).model(pull0).end()
				.override().predicate(new ResourceLocation("pulling"), 1).predicate(new ResourceLocation("pull"), (float) 0.65).model(pull1).end()
				.override().predicate(new ResourceLocation("pulling"), 1).predicate(new ResourceLocation("pull"), (float) 0.9).model(pull2).end();
	}

	private ItemModelBuilder iceBowTex(RegistryObject<Item> item, ModelFile pull0, ModelFile pull1, ModelFile pull2) {
		return bowItem(item.getId().getPath(), prefix("items/ice_bow_solid"), prefix("items/ice_bow_clear"))
				.override().predicate(new ResourceLocation("pulling"), 1).model(pull0).end()
				.override().predicate(new ResourceLocation("pulling"), 1).predicate(new ResourceLocation("pull"), (float) 0.65).model(pull1).end()
				.override().predicate(new ResourceLocation("pulling"), 1).predicate(new ResourceLocation("pull"), (float) 0.9).model(pull2).end();
	}

	private ItemModelBuilder phaseTex(String name, ResourceLocation... layers) {
		ItemModelBuilder builder = withExistingParent(name, "twilightforest:item/util/readable");
		for (int i = 0; i < layers.length; i++) {
			builder = builder.texture("layer" + i, layers[i]);
		}
		return builder;
	}

	private void woodenButton(Block button, String variant) {
		getBuilder(button.getRegistryName().getPath())
						.parent(getExistingFile(mcLoc("block/button_inventory")))
						.texture("texture", "block/wood/planks_" + variant + "_0");
	}

	private void woodenFence(Block fence, String variant) {
		getBuilder(fence.getRegistryName().getPath())
						.parent(getExistingFile(mcLoc("block/fence_inventory")))
						.texture("texture", "block/wood/planks_" + variant + "_0");
	}

	private void hollowLog(RegistryObject<HollowLogHorizontal> hollowLog) {
		getBuilder(hollowLog.get().asItem().getRegistryName().getPath()).parent(new ModelFile.ExistingModelFile(TwilightForestMod.prefix("block/" + hollowLog.getId().getPath()), this.existingFileHelper));
	}

	private void toBlock(Block b) {
		toBlockModel(b, b.getRegistryName().getPath());
	}

	private void toBlockModel(Block b, String model) {
		toBlockModel(b, prefix("block/" + model));
	}

	private void toBlockModel(Block b, ResourceLocation model) {
		withExistingParent(b.getRegistryName().getPath(), model);
	}

	@Override
	public String getName() {
		return "TwilightForest item and itemblock models";
	}
}
