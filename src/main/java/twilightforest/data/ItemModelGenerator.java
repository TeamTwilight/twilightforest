package twilightforest.data;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.ForgeRenderTypes;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.loaders.ItemLayerModelBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import twilightforest.TwilightForestMod;
import twilightforest.block.HollowLogHorizontal;
import twilightforest.init.TFBlocks;
import twilightforest.item.Experiment115Item;
import twilightforest.init.TFItems;

import static twilightforest.TwilightForestMod.prefix;

public class ItemModelGenerator extends ItemModelProvider {
	public ItemModelGenerator(PackOutput output, ExistingFileHelper existingFileHelper) {
		super(output, TwilightForestMod.ID, existingFileHelper);
	}

	@Override
	protected void registerModels() {
		for (Item i : ForgeRegistries.ITEMS) {
			if (i instanceof SpawnEggItem && ForgeRegistries.ITEMS.getKey(i).getNamespace().equals(TwilightForestMod.ID)) {
				getBuilder(ForgeRegistries.ITEMS.getKey(i).getPath())
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
		toBlockModel(TFBlocks.BEANSTALK_LEAVES.get(), new ResourceLocation("block/azalea_leaves"));
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
		toBlock(TFBlocks.PINK_CASTLE_DOOR.get());
		toBlock(TFBlocks.YELLOW_CASTLE_DOOR.get());
		toBlock(TFBlocks.BLUE_CASTLE_DOOR.get());
		toBlock(TFBlocks.VIOLET_CASTLE_DOOR.get());
		toBlockModel(TFBlocks.YELLOW_CASTLE_RUNE_BRICK.get(), "castle_rune_inventory");
		toBlockModel(TFBlocks.VIOLET_CASTLE_RUNE_BRICK.get(), "castle_rune_inventory");
		toBlockModel(TFBlocks.PINK_CASTLE_RUNE_BRICK.get(), "castle_rune_inventory");
		toBlockModel(TFBlocks.BLUE_CASTLE_RUNE_BRICK.get(), "castle_rune_inventory");
		forcefield(TFBlocks.PINK_FORCE_FIELD.getId().getPath(), prefix("block/forcefield_white"));
		forcefield(TFBlocks.BLUE_FORCE_FIELD.getId().getPath(), prefix("block/forcefield_white"));
		forcefield(TFBlocks.GREEN_FORCE_FIELD.getId().getPath(), prefix("block/forcefield_white"));
		forcefield(TFBlocks.VIOLET_FORCE_FIELD.getId().getPath(), prefix("block/forcefield_white"));
		forcefield(TFBlocks.ORANGE_FORCE_FIELD.getId().getPath(), prefix("block/forcefield_white"));
		toBlock(TFBlocks.CINDER_LOG.get());
		toBlock(TFBlocks.CINDER_WOOD.get());
		toBlockModel(TFBlocks.CINDER_FURNACE.get(), new ResourceLocation("block/furnace"));
		ModelFile think115 = generated("item/think115", prefix("item/think115"));
		ModelFile fullBlockSprinkle = getExistingFile(prefix("block/experiment115_8_8_regenerating"));
		generated(TFBlocks.EXPERIMENT_115.getId().getPath(), prefix("item/experiment_115"))
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
		singleTex(TFItems.RED_THREAD);
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
		generated(TFBlocks.FALLEN_LEAVES.getId().getPath(), new ResourceLocation("block/oak_leaves"));
		toBlock(TFBlocks.SMOKER.get());
		toBlock(TFBlocks.FIRE_JET.get());
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

		withExistingParent(TFBlocks.OAK_BANISTER.getId().toString(), prefix("item/banister_item")).texture("texture", "minecraft:block/oak_planks");
		withExistingParent(TFBlocks.SPRUCE_BANISTER.getId().toString(), prefix("item/banister_item")).texture("texture", "minecraft:block/spruce_planks");
		withExistingParent(TFBlocks.BIRCH_BANISTER.getId().toString(), prefix("item/banister_item")).texture("texture", "minecraft:block/birch_planks");
		withExistingParent(TFBlocks.JUNGLE_BANISTER.getId().toString(), prefix("item/banister_item")).texture("texture", "minecraft:block/jungle_planks");
		withExistingParent(TFBlocks.ACACIA_BANISTER.getId().toString(), prefix("item/banister_item")).texture("texture", "minecraft:block/acacia_planks");
		withExistingParent(TFBlocks.DARK_OAK_BANISTER.getId().toString(), prefix("item/banister_item")).texture("texture", "minecraft:block/dark_oak_planks");
		withExistingParent(TFBlocks.CRIMSON_BANISTER.getId().toString(), prefix("item/banister_item")).texture("texture", "minecraft:block/crimson_planks");
		withExistingParent(TFBlocks.WARPED_BANISTER.getId().toString(), prefix("item/banister_item")).texture("texture", "minecraft:block/warped_planks");
		withExistingParent(TFBlocks.VANGROVE_BANISTER.getId().toString(), prefix("item/banister_item")).texture("texture", "minecraft:block/mangrove_planks");

		toBlock(TFBlocks.TWILIGHT_OAK_LOG.get());
		toBlock(TFBlocks.STRIPPED_TWILIGHT_OAK_LOG.get());
		toBlock(TFBlocks.TWILIGHT_OAK_WOOD.get());
		toBlock(TFBlocks.STRIPPED_TWILIGHT_OAK_WOOD.get());
		toBlock(TFBlocks.TWILIGHT_OAK_LEAVES.get());
		toBlock(TFBlocks.RAINBOW_OAK_LEAVES.get());
		generated(TFBlocks.RAINBOW_OAK_SAPLING.getId().getPath(), prefix("block/" + TFBlocks.RAINBOW_OAK_SAPLING.getId().getPath()));
		generated(TFBlocks.TWILIGHT_OAK_SAPLING.getId().getPath(), prefix("block/" + TFBlocks.TWILIGHT_OAK_SAPLING.getId().getPath()));
		woodBlock(TFBlocks.TWILIGHT_OAK_PLANKS.get(), "planks/twilight_oak");
		woodBlock(TFBlocks.TWILIGHT_OAK_STAIRS.get(), "stairs/twilight_oak");
		woodBlock(TFBlocks.TWILIGHT_OAK_SLAB.get(), "slab/twilight_oak");
		woodenButton(TFBlocks.TWILIGHT_OAK_BUTTON.get(), "twilight_oak");
		woodenFence(TFBlocks.TWILIGHT_OAK_FENCE.get(), "twilight_oak");
		woodBlock(TFBlocks.TWILIGHT_OAK_GATE.get(), "fence_gate/twilight_oak");
		woodBlock(TFBlocks.TWILIGHT_OAK_PLATE.get(), "pressure_plate/twilight_oak");
		woodBlockModel(TFBlocks.TWILIGHT_OAK_TRAPDOOR.get(), "twilight_oak_trapdoor_bottom", "trapdoor/twilight_oak");
		generated(TFBlocks.TWILIGHT_OAK_SIGN.getId().getPath(), prefix("item/" + TFBlocks.TWILIGHT_OAK_SIGN.getId().getPath()));
		withExistingParent(TFBlocks.TWILIGHT_OAK_BANISTER.getId().toString(), prefix("item/banister_item")).texture("texture", "block/wood/planks_twilight_oak_0");
		generated(TFBlocks.TWILIGHT_OAK_DOOR.getId().getPath(), prefix("item/" + TFBlocks.TWILIGHT_OAK_DOOR.getId().getPath()));
		withExistingParent(TFBlocks.TWILIGHT_OAK_CHEST.getId().toString(), "item/chest").texture("particle", prefix("block/wood/planks_twilight_oak_0"));

		toBlock(TFBlocks.CANOPY_LOG.get());
		toBlock(TFBlocks.STRIPPED_CANOPY_LOG.get());
		toBlock(TFBlocks.CANOPY_WOOD.get());
		toBlock(TFBlocks.STRIPPED_CANOPY_WOOD.get());
		toBlock(TFBlocks.CANOPY_LEAVES.get());
		generated(TFBlocks.CANOPY_SAPLING.getId().getPath(), prefix("block/" + TFBlocks.CANOPY_SAPLING.getId().getPath()));
		woodBlock(TFBlocks.CANOPY_PLANKS.get(), "planks/canopy");
		woodBlock(TFBlocks.CANOPY_STAIRS.get(), "stairs/canopy");
		woodBlock(TFBlocks.CANOPY_SLAB.get(), "slab/canopy");
		woodenButton(TFBlocks.CANOPY_BUTTON.get(), "canopy");
		woodenFence(TFBlocks.CANOPY_FENCE.get(), "canopy");
		woodBlock(TFBlocks.CANOPY_GATE.get(), "fence_gate/canopy");
		woodBlock(TFBlocks.CANOPY_PLATE.get(), "pressure_plate/canopy");
		woodBlockModel(TFBlocks.CANOPY_TRAPDOOR.get(), "canopy_trapdoor_bottom", "trapdoor/canopy");
		generated(TFBlocks.CANOPY_SIGN.getId().getPath(), prefix("item/" + TFBlocks.CANOPY_SIGN.getId().getPath()));
		withExistingParent(TFBlocks.CANOPY_BANISTER.getId().toString(), prefix("item/banister_item")).texture("texture", "block/wood/planks_canopy_0");
		generated(TFBlocks.CANOPY_DOOR.getId().getPath(), prefix("item/" + TFBlocks.CANOPY_DOOR.getId().getPath()));
		withExistingParent(TFBlocks.CANOPY_CHEST.getId().toString(), "item/chest").texture("particle", prefix("block/wood/planks_canopy_0"));

		toBlock(TFBlocks.MANGROVE_LOG.get());
		toBlock(TFBlocks.STRIPPED_MANGROVE_LOG.get());
		toBlock(TFBlocks.MANGROVE_WOOD.get());
		toBlock(TFBlocks.STRIPPED_MANGROVE_WOOD.get());
		toBlock(TFBlocks.MANGROVE_LEAVES.get());
		generated(TFBlocks.MANGROVE_SAPLING.getId().getPath(), prefix("block/" + TFBlocks.MANGROVE_SAPLING.getId().getPath()));
		woodBlock(TFBlocks.MANGROVE_PLANKS.get(), "planks/mangrove");
		woodBlock(TFBlocks.MANGROVE_STAIRS.get(), "stairs/mangrove");
		woodBlock(TFBlocks.MANGROVE_SLAB.get(), "slab/mangrove");
		woodenButton(TFBlocks.MANGROVE_BUTTON.get(), "mangrove");
		woodenFence(TFBlocks.MANGROVE_FENCE.get(), "mangrove");
		woodBlock(TFBlocks.MANGROVE_GATE.get(), "fence_gate/mangrove");
		woodBlock(TFBlocks.MANGROVE_PLATE.get(), "pressure_plate/mangrove");
		woodBlockModel(TFBlocks.MANGROVE_TRAPDOOR.get(), "mangrove_trapdoor_bottom", "trapdoor/mangrove");
		generated(TFBlocks.MANGROVE_SIGN.getId().getPath(), prefix("item/" + TFBlocks.MANGROVE_SIGN.getId().getPath()));
		withExistingParent(TFBlocks.MANGROVE_BANISTER.getId().toString(), prefix("item/banister_item")).texture("texture", "block/wood/planks_mangrove_0");
		generated(TFBlocks.MANGROVE_DOOR.getId().getPath(), prefix("item/" + TFBlocks.MANGROVE_DOOR.getId().getPath()));
		withExistingParent(TFBlocks.MANGROVE_CHEST.getId().toString(), "item/chest").texture("particle", prefix("block/wood/planks_mangrove_0"));

		toBlock(TFBlocks.DARK_LOG.get());
		toBlock(TFBlocks.STRIPPED_DARK_LOG.get());
		toBlock(TFBlocks.DARK_WOOD.get());
		toBlock(TFBlocks.STRIPPED_DARK_WOOD.get());
		toBlock(TFBlocks.DARK_LEAVES.get());
		generated(TFBlocks.DARKWOOD_SAPLING.getId().getPath(), prefix("block/" + TFBlocks.DARKWOOD_SAPLING.getId().getPath()));
		woodBlock(TFBlocks.DARK_PLANKS.get(), "planks/darkwood");
		woodBlock(TFBlocks.DARK_STAIRS.get(), "stairs/darkwood");
		woodBlock(TFBlocks.DARK_SLAB.get(), "slab/darkwood");
		woodenButton(TFBlocks.DARK_BUTTON.get(), "darkwood");
		woodenFence(TFBlocks.DARK_FENCE.get(), "darkwood");
		woodBlock(TFBlocks.DARK_GATE.get(), "fence_gate/darkwood");
		woodBlock(TFBlocks.DARK_PLATE.get(), "pressure_plate/darkwood");
		woodBlockModel(TFBlocks.DARK_TRAPDOOR.get(), "darkwood_trapdoor_bottom", "trapdoor/darkwood");
		generated(TFBlocks.DARK_SIGN.getId().getPath(), prefix("item/" + TFBlocks.DARK_SIGN.getId().getPath()));
		withExistingParent(TFBlocks.DARK_BANISTER.getId().toString(), prefix("item/banister_item")).texture("texture", "block/wood/planks_darkwood_0");
		generated(TFBlocks.DARK_DOOR.getId().getPath(), prefix("item/" + TFBlocks.DARK_DOOR.getId().getPath()));
		generated(TFBlocks.HOLLOW_OAK_SAPLING.getId().getPath(), prefix("block/" + TFBlocks.HOLLOW_OAK_SAPLING.getId().getPath()));
		withExistingParent(TFBlocks.DARK_CHEST.getId().toString(), "item/chest").texture("particle", prefix("block/wood/planks_darkwood_0"));
		
		toBlock(TFBlocks.TIME_LOG.get());
		toBlock(TFBlocks.STRIPPED_TIME_LOG.get());
		toBlock(TFBlocks.TIME_WOOD.get());
		toBlock(TFBlocks.STRIPPED_TIME_WOOD.get());
		toBlock(TFBlocks.TIME_LOG_CORE.get());
		toBlock(TFBlocks.TIME_LEAVES.get());
		generated(TFBlocks.TIME_SAPLING.getId().getPath(), prefix("block/" + TFBlocks.TIME_SAPLING.getId().getPath()));
		woodBlock(TFBlocks.TIME_PLANKS.get(), "planks/time");
		woodBlock(TFBlocks.TIME_STAIRS.get(), "stairs/time");
		woodBlock(TFBlocks.TIME_SLAB.get(), "slab/time");
		woodenButton(TFBlocks.TIME_BUTTON.get(), "time");
		woodenFence(TFBlocks.TIME_FENCE.get(), "time");
		woodBlock(TFBlocks.TIME_GATE.get(), "fence_gate/time");
		woodBlock(TFBlocks.TIME_PLATE.get(), "pressure_plate/time");
		woodBlockModel(TFBlocks.TIME_TRAPDOOR.get(), "time_trapdoor_bottom", "trapdoor/time");
		generated(TFBlocks.TIME_SIGN.getId().getPath(), prefix("item/" + TFBlocks.TIME_SIGN.getId().getPath()));
		withExistingParent(TFBlocks.TIME_BANISTER.getId().toString(), prefix("item/banister_item")).texture("texture", "block/wood/planks_time_0");
		generated(TFBlocks.TIME_DOOR.getId().getPath(), prefix("item/" + TFBlocks.TIME_DOOR.getId().getPath()));
		withExistingParent(TFBlocks.TIME_CHEST.getId().toString(), "item/chest").texture("particle", prefix("block/wood/planks_time_0"));

		toBlock(TFBlocks.TRANSFORMATION_LOG.get());
		toBlock(TFBlocks.STRIPPED_TRANSFORMATION_LOG.get());
		toBlock(TFBlocks.TRANSFORMATION_WOOD.get());
		toBlock(TFBlocks.STRIPPED_TRANSFORMATION_WOOD.get());
		toBlock(TFBlocks.TRANSFORMATION_LOG_CORE.get());
		toBlock(TFBlocks.TRANSFORMATION_LEAVES.get());
		generated(TFBlocks.TRANSFORMATION_SAPLING.getId().getPath(), prefix("block/" + TFBlocks.TRANSFORMATION_SAPLING.getId().getPath()));
		woodBlock(TFBlocks.TRANSFORMATION_PLANKS.get(), "planks/trans");
		woodBlock(TFBlocks.TRANSFORMATION_STAIRS.get(), "stairs/trans");
		woodBlock(TFBlocks.TRANSFORMATION_SLAB.get(), "slab/trans");
		woodenButton(TFBlocks.TRANSFORMATION_BUTTON.get(), "trans");
		woodenFence(TFBlocks.TRANSFORMATION_FENCE.get(), "trans");
		woodBlock(TFBlocks.TRANSFORMATION_GATE.get(), "fence_gate/trans");
		woodBlock(TFBlocks.TRANSFORMATION_PLATE.get(), "pressure_plate/trans");
		woodBlockModel(TFBlocks.TRANSFORMATION_TRAPDOOR.get(), "trans_trapdoor_bottom", "trapdoor/trans");
		generated(TFBlocks.TRANSFORMATION_SIGN.getId().getPath(), prefix("item/" + TFBlocks.TRANSFORMATION_SIGN.getId().getPath()));
		withExistingParent(TFBlocks.TRANSFORMATION_BANISTER.getId().toString(), prefix("item/banister_item")).texture("texture", "block/wood/planks_trans_0");
		generated(TFBlocks.TRANSFORMATION_DOOR.getId().getPath(), prefix("item/" + TFBlocks.TRANSFORMATION_DOOR.getId().getPath()));
		withExistingParent(TFBlocks.TRANSFORMATION_CHEST.getId().toString(), "item/chest").texture("particle", prefix("block/wood/planks_trans_0"));

		toBlock(TFBlocks.MINING_LOG.get());
		toBlock(TFBlocks.STRIPPED_MINING_LOG.get());
		toBlock(TFBlocks.MINING_WOOD.get());
		toBlock(TFBlocks.STRIPPED_MINING_WOOD.get());
		toBlock(TFBlocks.MINING_LOG_CORE.get());
		toBlock(TFBlocks.MINING_LEAVES.get());
		generated(TFBlocks.MINING_SAPLING.getId().getPath(), prefix("block/" + TFBlocks.MINING_SAPLING.getId().getPath()));
		woodBlock(TFBlocks.MINING_PLANKS.get(), "planks/mine");
		woodBlock(TFBlocks.MINING_STAIRS.get(), "stairs/mine");
		woodBlock(TFBlocks.MINING_SLAB.get(), "slab/mine");
		woodenButton(TFBlocks.MINING_BUTTON.get(), "mine");
		woodenFence(TFBlocks.MINING_FENCE.get(), "mine");
		woodBlock(TFBlocks.MINING_GATE.get(), "fence_gate/mine");
		woodBlock(TFBlocks.MINING_PLATE.get(), "pressure_plate/mine");
		woodBlockModel(TFBlocks.MINING_TRAPDOOR.get(), "mine_trapdoor_bottom", "trapdoor/mine");
		generated(TFBlocks.MINING_SIGN.getId().getPath(), prefix("item/" + TFBlocks.MINING_SIGN.getId().getPath()));
		withExistingParent(TFBlocks.MINING_BANISTER.getId().toString(), prefix("item/banister_item")).texture("texture", "block/wood/planks_mine_0");
		generated(TFBlocks.MINING_DOOR.getId().getPath(), prefix("item/" + TFBlocks.MINING_DOOR.getId().getPath()));
		withExistingParent(TFBlocks.MINING_CHEST.getId().toString(), "item/chest").texture("particle", prefix("block/wood/planks_mine_0"));

		toBlock(TFBlocks.SORTING_LOG.get());
		toBlock(TFBlocks.STRIPPED_SORTING_LOG.get());
		toBlock(TFBlocks.SORTING_WOOD.get());
		toBlock(TFBlocks.STRIPPED_SORTING_WOOD.get());
		toBlock(TFBlocks.SORTING_LOG_CORE.get());
		toBlock(TFBlocks.SORTING_LEAVES.get());
		generated(TFBlocks.SORTING_SAPLING.getId().getPath(), prefix("block/" + TFBlocks.SORTING_SAPLING.getId().getPath()));
		woodBlock(TFBlocks.SORTING_PLANKS.get(), "planks/sort");
		woodBlock(TFBlocks.SORTING_STAIRS.get(), "stairs/sort");
		woodBlock(TFBlocks.SORTING_SLAB.get(), "slab/sort");
		woodenButton(TFBlocks.SORTING_BUTTON.get(), "sort");
		woodenFence(TFBlocks.SORTING_FENCE.get(), "sort");
		woodBlock(TFBlocks.SORTING_GATE.get(), "fence_gate/sort");
		woodBlock(TFBlocks.SORTING_PLATE.get(), "pressure_plate/sort");
		woodBlockModel(TFBlocks.SORTING_TRAPDOOR.get(), "sort_trapdoor_bottom", "trapdoor/sort");
		generated(TFBlocks.SORTING_SIGN.getId().getPath(), prefix("item/" + TFBlocks.SORTING_SIGN.getId().getPath()));
		withExistingParent(TFBlocks.SORTING_BANISTER.getId().toString(), prefix("item/banister_item")).texture("texture", "block/wood/planks_sort_0");
		generated(TFBlocks.SORTING_DOOR.getId().getPath(), prefix("item/" + TFBlocks.SORTING_DOOR.getId().getPath()));
		withExistingParent(TFBlocks.SORTING_CHEST.getId().toString(), "item/chest").texture("particle", prefix("block/wood/planks_sort_0"));

		withExistingParent(TFBlocks.NAGA_TROPHY.getId().toString(), prefix("item/template_trophy"));
		withExistingParent(TFBlocks.LICH_TROPHY.getId().toString(), prefix("item/template_trophy"));
		withExistingParent(TFBlocks.MINOSHROOM_TROPHY.getId().toString(), prefix("item/template_trophy"));
		withExistingParent(TFBlocks.HYDRA_TROPHY.getId().toString(), prefix("item/template_trophy"));
		withExistingParent(TFBlocks.KNIGHT_PHANTOM_TROPHY.getId().toString(), prefix("item/template_trophy"));
		//ur-ghast and alpha yeti need special transforms
		withExistingParent(TFBlocks.SNOW_QUEEN_TROPHY.getId().toString(), prefix("item/template_trophy"));
		withExistingParent(TFBlocks.QUEST_RAM_TROPHY.getId().toString(), prefix("item/template_trophy"));

		withExistingParent(TFBlocks.CREEPER_SKULL_CANDLE.getId().toString(), prefix("item/template_skull_candle"));
		withExistingParent(TFBlocks.PLAYER_SKULL_CANDLE.getId().toString(), prefix("item/template_skull_candle"));
		withExistingParent(TFBlocks.SKELETON_SKULL_CANDLE.getId().toString(), prefix("item/template_skull_candle"));
		withExistingParent(TFBlocks.WITHER_SKELE_SKULL_CANDLE.getId().toString(), prefix("item/template_skull_candle"));
		withExistingParent(TFBlocks.ZOMBIE_SKULL_CANDLE.getId().toString(), prefix("item/template_skull_candle"));

		hollowLog(TFBlocks.HOLLOW_OAK_LOG_HORIZONTAL);
		hollowLog(TFBlocks.HOLLOW_SPRUCE_LOG_HORIZONTAL);
		hollowLog(TFBlocks.HOLLOW_BIRCH_LOG_HORIZONTAL);
		hollowLog(TFBlocks.HOLLOW_JUNGLE_LOG_HORIZONTAL);
		hollowLog(TFBlocks.HOLLOW_ACACIA_LOG_HORIZONTAL);
		hollowLog(TFBlocks.HOLLOW_DARK_OAK_LOG_HORIZONTAL);
		hollowLog(TFBlocks.HOLLOW_CRIMSON_STEM_HORIZONTAL);
		hollowLog(TFBlocks.HOLLOW_WARPED_STEM_HORIZONTAL);
		hollowLog(TFBlocks.HOLLOW_VANGROVE_LOG_HORIZONTAL);
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
		singleTex(TFItems.FILLED_ORE_MAP);
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
		singleTexFullbright(TFItems.TORCHBERRIES);
		singleTex(TFItems.RAW_VENISON);
		singleTex(TFItems.COOKED_VENISON);
		singleTex(TFItems.HYDRA_CHOP);
		singleTex(TFItems.FIERY_BLOOD);
		singleTex(TFItems.FIERY_TEARS);
		singleTexFullbright(TFItems.FIERY_INGOT);
		singleTexFullbright(TFItems.FIERY_HELMET);
		singleTexFullbright(TFItems.FIERY_CHESTPLATE);
		singleTexFullbright(TFItems.FIERY_LEGGINGS);
		singleTexFullbright(TFItems.FIERY_BOOTS);
		singleTexFullbrightTool(TFItems.FIERY_SWORD);
		singleTexFullbrightTool(TFItems.FIERY_PICKAXE);
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
		singleTex(TFItems.ORE_MAP);
		ModelFile magnetPull1 = generated("ore_magnet_pulling_1", prefix("item/ore_magnet_pulling_1"));
		ModelFile magnetPull2 = generated("ore_magnet_pulling_2", prefix("item/ore_magnet_pulling_2"));
		singleTex(TFItems.ORE_MAGNET)
				.override().predicate(new ResourceLocation("pulling"), 1).predicate(new ResourceLocation("pull"), (float) 0.5).model(magnetPull1).end()
				.override().predicate(new ResourceLocation("pulling"), 1).predicate(new ResourceLocation("pull"), 1).model(magnetPull2).end();
		singleTexTool(TFItems.CRUMBLE_HORN);
		singleTexTool(TFItems.PEACOCK_FEATHER_FAN);
		ModelFile queenAlt = fullbrightTool("moonworm_queen_alt", prefix("item/moonworm_queen_alt"));
		singleTexFullbrightTool(TFItems.MOONWORM_QUEEN).override().predicate(prefix("alt"), 1).model(queenAlt).end();
		singleTex(TFItems.CHARM_OF_KEEPING_1);
		singleTex(TFItems.CHARM_OF_KEEPING_2);
		singleTex(TFItems.CHARM_OF_KEEPING_3);
		singleTex(TFItems.CHARM_OF_LIFE_1);
		singleTex(TFItems.CHARM_OF_LIFE_2);
		singleTexFullbright(TFItems.TOWER_KEY);
		generated(TFItems.BORER_ESSENCE.getId().getPath(), prefix("item/" + TFItems.BORER_ESSENCE.getId().getPath()), prefix("item/borer_essence_particles"));
		buildItem(TFItems.CARMINITE.getId().getPath(), "item/generated", 7, prefix("item/" + TFItems.CARMINITE.getId().getPath()));
		singleTex(TFItems.ARMOR_SHARD);
		singleTex(TFItems.ARMOR_SHARD_CLUSTER);
		singleTex(TFItems.KNIGHTMETAL_INGOT);
		singleTex(TFItems.KNIGHTMETAL_HELMET);
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
		singleTex(TFItems.YETI_HELMET);
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
		ModelFile triplePulling0 = bowItem("triple_bow_pulling_0", prefix("item/triple_bow_pulling_0"));
		ModelFile triplePulling1 = bowItem("triple_bow_pulling_1", prefix("item/triple_bow_pulling_1"));
		ModelFile triplePulling2 = bowItem("triple_bow_pulling_2", prefix("item/triple_bow_pulling_2"));
		bowTex(TFItems.TRIPLE_BOW, triplePulling0, triplePulling1, triplePulling2);
		ModelFile seekerPulling0 = bowItem("seeker_bow_pulling_0", prefix("item/seeker_bow_pulling_0"));
		ModelFile seekerPulling1 = bowItem("seeker_bow_pulling_1", prefix("item/seeker_bow_pulling_1"));
		ModelFile seekerPulling2 = bowItem("seeker_bow_pulling_2", prefix("item/seeker_bow_pulling_2"));
		bowTex(TFItems.SEEKER_BOW, seekerPulling0, seekerPulling1, seekerPulling2);
		ModelFile icePulling0 = bowItem("ice_bow_pulling_0", prefix("item/ice_bow_solid_pulling_0"), prefix("item/ice_bow_clear_pulling_0"));
		ModelFile icePulling1 = bowItem("ice_bow_pulling_1", prefix("item/ice_bow_solid_pulling_1"), prefix("item/ice_bow_clear_pulling_1"));
		ModelFile icePulling2 = bowItem("ice_bow_pulling_2", prefix("item/ice_bow_solid_pulling_2"), prefix("item/ice_bow_clear_pulling_2"));
		iceBowTex(icePulling0, icePulling1, icePulling2);
		ModelFile enderPulling0 = bowItem("ender_bow_pulling_0", prefix("item/ender_bow_pulling_0"));
		ModelFile enderPulling1 = bowItem("ender_bow_pulling_1", prefix("item/ender_bow_pulling_1"));
		ModelFile enderPulling2 = bowItem("ender_bow_pulling_2", prefix("item/ender_bow_pulling_2"));
		bowTex(TFItems.ENDER_BOW, enderPulling0, enderPulling1, enderPulling2);
		tool(TFItems.ICE_SWORD.getId().getPath(), prefix("item/ice_sword_solid"), prefix("item/ice_sword_clear"));
		tool(TFItems.GLASS_SWORD.getId().getPath(), prefix("item/glass_sword_solid"), prefix("item/glass_sword_clear"));
		ModelFile chainThrown = tool("block_and_chain_thrown", prefix("item/block_and_chain_thrown"));
		singleTexTool(TFItems.BLOCK_AND_CHAIN).override().predicate(prefix("thrown"), 1).model(chainThrown).end();
		ModelFile cubeThrown = tool("cube_of_annihilation_thrown", prefix("item/cube_of_annihilation_thrown"));
		singleTexTool(TFItems.CUBE_OF_ANNIHILATION).override().predicate(prefix("thrown"), 1).model(cubeThrown).end();
		singleTex(TFItems.CUBE_TALISMAN);
		//moon dial is a big boi
		ModelFile full = phaseTex("moon_dial_full", prefix("item/moon_dial/full"));
		ModelFile waning_gib = phaseTex("moon_dial_waning_gib", prefix("item/moon_dial/waning_gibbous"));
		ModelFile quarter3 = phaseTex("moon_dial_quarter3", prefix("item/moon_dial/third_quarter"));
		ModelFile waning_cres = phaseTex("moon_dial_waning_cres", prefix("item/moon_dial/waning_cresent"));
		ModelFile unlit = phaseTex("moon_dial_new", prefix("item/moon_dial/new")); //cant use new for the name lmao
		ModelFile waxing_cres = phaseTex("moon_dial_waxing_cres", prefix("item/moon_dial/waxing_cresent"));
		ModelFile quarter1 = phaseTex("moon_dial_quarter1", prefix("item/moon_dial/first_quarter"));
		ModelFile waxing_gib = phaseTex("moon_dial_waxing_gib", prefix("item/moon_dial/waxing_gibbous"));
		phaseTex(TFItems.MOON_DIAL.getId().getPath(), prefix("item/moon_dial/full"))
				.override().predicate(new ResourceLocation("phase"), 0).model(full).end()
				.override().predicate(new ResourceLocation("phase"), 0.125F).model(waning_gib).end()
				.override().predicate(new ResourceLocation("phase"), 0.25F).model(quarter3).end()
				.override().predicate(new ResourceLocation("phase"), 0.375F).model(waning_cres).end()
				.override().predicate(new ResourceLocation("phase"), 0.5F).model(unlit).end()
				.override().predicate(new ResourceLocation("phase"), 0.625F).model(waxing_cres).end()
				.override().predicate(new ResourceLocation("phase"), 0.75F).model(quarter1).end()
				.override().predicate(new ResourceLocation("phase"), 0.875F).model(waxing_gib).end();

		ModelFile fill1 = generated("brittle_flask_0", prefix("item/brittle_potion_flask_1"), prefix("item/brittle_potion_flask_labelled"));
		ModelFile fill2 = generated("brittle_flask_1", prefix("item/brittle_potion_flask_2"), prefix("item/brittle_potion_flask_labelled"));
		ModelFile fill3 = generated("brittle_flask_2", prefix("item/brittle_potion_flask_3"), prefix("item/brittle_potion_flask_labelled"));
		ModelFile fill4 = generated("brittle_flask_3", prefix("item/brittle_potion_flask_4"), prefix("item/brittle_potion_flask_labelled"));
		ModelFile splintered = generated("brittle_flask_splintered", prefix("item/brittle_potion_flask_splintered"));
		ModelFile fill1_splintered = generated("brittle_flask_0_splintered", prefix("item/brittle_potion_flask_1"), prefix("item/brittle_potion_flask_splintered"));
		ModelFile fill2_splintered = generated("brittle_flask_1_splintered", prefix("item/brittle_potion_flask_2"), prefix("item/brittle_potion_flask_splintered"));
		ModelFile fill3_splintered = generated("brittle_flask_2_splintered", prefix("item/brittle_potion_flask_3"), prefix("item/brittle_potion_flask_splintered"));
		ModelFile fill4_splintered = generated("brittle_flask_3_splintered", prefix("item/brittle_potion_flask_4"), prefix("item/brittle_potion_flask_splintered"));
		ModelFile cracked = generated("brittle_flask_cracked", prefix("item/brittle_potion_flask_cracked"));
		ModelFile fill1_cracked = generated("brittle_flask_0_cracked", prefix("item/brittle_potion_flask_1"), prefix("item/brittle_potion_flask_cracked"));
		ModelFile fill2_cracked = generated("brittle_flask_1_cracked", prefix("item/brittle_potion_flask_2"), prefix("item/brittle_potion_flask_cracked"));
		ModelFile fill3_cracked = generated("brittle_flask_2_cracked", prefix("item/brittle_potion_flask_3"), prefix("item/brittle_potion_flask_cracked"));
		ModelFile fill4_cracked = generated("brittle_flask_3_cracked", prefix("item/brittle_potion_flask_4"), prefix("item/brittle_potion_flask_cracked"));
		ModelFile damaged = generated("brittle_flask_damaged", prefix("item/brittle_potion_flask_damaged"));
		ModelFile fill1_damaged = generated("brittle_flask_0_damaged", prefix("item/brittle_potion_flask_1"), prefix("item/brittle_potion_flask_damaged"));
		ModelFile fill2_damaged = generated("brittle_flask_1_damaged", prefix("item/brittle_potion_flask_2"), prefix("item/brittle_potion_flask_damaged"));
		ModelFile fill3_damaged = generated("brittle_flask_2_damaged", prefix("item/brittle_potion_flask_3"), prefix("item/brittle_potion_flask_damaged"));
		ModelFile fill4_damaged = generated("brittle_flask_3_damaged", prefix("item/brittle_potion_flask_4"), prefix("item/brittle_potion_flask_damaged"));
		
		generated(TFItems.BRITTLE_FLASK.getId().getPath(), prefix("block/stone_twist/twist_blank"), prefix("item/brittle_potion_flask"))
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

		ModelFile gfill1 = generated("greater_flask_0", prefix("item/greater_potion_flask_1"), prefix("item/greater_potion_flask"));
		ModelFile gfill2 = generated("greater_flask_1", prefix("item/greater_potion_flask_2"), prefix("item/greater_potion_flask"));
		ModelFile gfill3 = generated("greater_flask_2", prefix("item/greater_potion_flask_3"), prefix("item/greater_potion_flask"));
		ModelFile gfill4 = generated("greater_flask_3", prefix("item/greater_potion_flask_4"), prefix("item/greater_potion_flask"));

		generated(TFItems.GREATER_FLASK.getId().getPath(), prefix("block/stone_twist/twist_blank"), prefix("item/greater_potion_flask"))
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

		singleTex(TFItems.TWILIGHT_OAK_BOAT);
		singleTex(TFItems.CANOPY_BOAT);
		singleTex(TFItems.MANGROVE_BOAT);
		singleTex(TFItems.DARKWOOD_BOAT);
		singleTex(TFItems.TIME_BOAT);
		singleTex(TFItems.TRANSFORMATION_BOAT);
		singleTex(TFItems.MINING_BOAT);
		singleTex(TFItems.SORTING_BOAT);

		singleTex(TFItems.TWILIGHT_OAK_CHEST_BOAT);
		singleTex(TFItems.CANOPY_CHEST_BOAT);
		singleTex(TFItems.MANGROVE_CHEST_BOAT);
		singleTex(TFItems.DARKWOOD_CHEST_BOAT);
		singleTex(TFItems.TIME_CHEST_BOAT);
		singleTex(TFItems.TRANSFORMATION_CHEST_BOAT);
		singleTex(TFItems.MINING_CHEST_BOAT);
		singleTex(TFItems.SORTING_CHEST_BOAT);

		generated(TFItems.NAGA_BANNER_PATTERN.getId().getPath(), prefix("item/tf_banner_pattern"));
		generated(TFItems.LICH_BANNER_PATTERN.getId().getPath(), prefix("item/tf_banner_pattern"));
		generated(TFItems.MINOSHROOM_BANNER_PATTERN.getId().getPath(), prefix("item/tf_banner_pattern"));
		generated(TFItems.HYDRA_BANNER_PATTERN.getId().getPath(), prefix("item/tf_banner_pattern"));
		generated(TFItems.KNIGHT_PHANTOM_BANNER_PATTERN.getId().getPath(), prefix("item/tf_banner_pattern"));
		generated(TFItems.UR_GHAST_BANNER_PATTERN.getId().getPath(), prefix("item/tf_banner_pattern"));
		generated(TFItems.ALPHA_YETI_BANNER_PATTERN.getId().getPath(), prefix("item/tf_banner_pattern"));
		generated(TFItems.SNOW_QUEEN_BANNER_PATTERN.getId().getPath(), prefix("item/tf_banner_pattern"));
		generated(TFItems.QUEST_RAM_BANNER_PATTERN.getId().getPath(), prefix("item/tf_banner_pattern"));

		//these models are used as references in other things, they dont have actual items
		generated("trophy", prefix("item/trophy"));
		generated("trophy_minor", prefix("item/trophy_minor"));
		generated("trophy_quest", prefix("item/trophy_quest"));
		generated("shield", prefix("item/lich_shield_frame"), prefix("item/lich_shield_fill"));
	}

	private void fullbright(String name, ResourceLocation... layers) {
		buildItem(name, "item/generated", 15, layers);
	}

	private ItemModelBuilder fullbrightTool(String name, ResourceLocation... layers) {
		return buildItem(name, "item/handheld", 15, layers);
	}

	private ItemModelBuilder generated(String name, ResourceLocation... layers) {
		return buildItem(name, "item/generated", 0, layers);
	}

	private ItemModelBuilder tool(String name, ResourceLocation... layers) {
		return buildItem(name, "item/handheld", 0, layers);
	}

	private ItemModelBuilder buildItem(String name, String parent, int emissivity, ResourceLocation... layers) {
		ItemModelBuilder builder = withExistingParent(name, parent);
		for (int i = 0; i < layers.length; i++) {
			builder = builder.texture("layer" + i, layers[i]);
		}
		if (emissivity > 0) builder = builder.customLoader(ItemLayerModelBuilder::begin).emissive(emissivity, emissivity, 0).renderType("forge_entity_unsorted_translucent", 0).end();
		return builder;
	}

	private ItemModelBuilder forcefield(String name, ResourceLocation... layers) {
		ItemModelBuilder builder = withExistingParent(name, "item/generated");
		for (int i = 0; i < layers.length; i++) {
			builder = builder.texture("layer" + i, layers[i]);
		}
		builder = builder.customLoader(ItemLayerModelBuilder::begin).emissive(15, 15, 0).renderType(new ResourceLocation("translucent"), 0).end();
		return builder;
	}

	private void singleTexFullbright(RegistryObject<Item> item) {
		fullbright(item.getId().getPath(), prefix("item/" + item.getId().getPath()));
	}

	private ItemModelBuilder singleTexFullbrightTool(RegistryObject<Item> item) {
		return fullbrightTool(item.getId().getPath(), prefix("item/" + item.getId().getPath()));
	}

	private ItemModelBuilder singleTexTool(RegistryObject<Item> item) {
		return tool(item.getId().getPath(), prefix("item/" + item.getId().getPath()));
	}

	private ItemModelBuilder singleTex(RegistryObject<Item> item) {
		return generated(item.getId().getPath(), prefix("item/" + item.getId().getPath()));
	}

	private void arcticArmorTex(RegistryObject<Item> item) {
		generated(item.getId().getPath(), prefix("item/" + item.getId().getPath()), prefix("item/" + item.getId().getPath() + "_0"));
	}

	private ItemModelBuilder bowItem(String name, ResourceLocation... layers) {
		ItemModelBuilder builder = withExistingParent(name, "item/bow");
		for (int i = 0; i < layers.length; i++) {
			builder = builder.texture("layer" + i, layers[i]);
		}
		return builder;
	}

	private void bowTex(RegistryObject<Item> item, ModelFile pull0, ModelFile pull1, ModelFile pull2) {
		bowItem(item.getId().getPath(), prefix("item/" + item.getId().getPath()))
				.override().predicate(new ResourceLocation("pulling"), 1).model(pull0).end()
				.override().predicate(new ResourceLocation("pulling"), 1).predicate(new ResourceLocation("pull"), (float) 0.65).model(pull1).end()
				.override().predicate(new ResourceLocation("pulling"), 1).predicate(new ResourceLocation("pull"), (float) 0.9).model(pull2).end();
	}

	private void iceBowTex(ModelFile pull0, ModelFile pull1, ModelFile pull2) {
		bowItem(TFItems.ICE_BOW.getId().getPath(), prefix("item/ice_bow_solid"), prefix("item/ice_bow_clear"))
				.override().predicate(new ResourceLocation("pulling"), 1).model(pull0).end()
				.override().predicate(new ResourceLocation("pulling"), 1).predicate(new ResourceLocation("pull"), (float) 0.65).model(pull1).end()
				.override().predicate(new ResourceLocation("pulling"), 1).predicate(new ResourceLocation("pull"), (float) 0.9).model(pull2).end();
	}

	private ItemModelBuilder phaseTex(String name, ResourceLocation... layers) {
		ItemModelBuilder builder = withExistingParent(name, "twilightforest:item/moon_dial_template");
		for (int i = 0; i < layers.length; i++) {
			builder = builder.texture("layer" + i, layers[i]);
		}
		return builder;
	}

	private void woodenButton(Block button, String variant) {
		getBuilder(ForgeRegistries.BLOCKS.getKey(button).getPath())
						.parent(getExistingFile(mcLoc("block/button_inventory")))
						.texture("texture", "block/wood/planks_" + variant + "_0");
	}

	private void woodenFence(Block fence, String variant) {
		getBuilder(ForgeRegistries.BLOCKS.getKey(fence).getPath())
						.parent(getExistingFile(mcLoc("block/fence_inventory")))
						.texture("texture", "block/wood/planks_" + variant + "_0");
	}

	private void hollowLog(RegistryObject<HollowLogHorizontal> hollowLog) {
		getBuilder(ForgeRegistries.ITEMS.getKey(hollowLog.get().asItem()).getPath()).parent(new ModelFile.ExistingModelFile(TwilightForestMod.prefix("block/" + hollowLog.getId().getPath()), this.existingFileHelper));
	}

	private void toBlock(Block b) {
		toBlockModel(b, ForgeRegistries.BLOCKS.getKey(b).getPath());
	}

	private void woodBlock(Block b, String variant) {
		woodBlockModel(b, ForgeRegistries.BLOCKS.getKey(b).getPath(), variant);
	}

	private void toBlockModel(Block b, String model) {
		toBlockModel(b, prefix("block/" + model));
	}

	private void woodBlockModel(Block b, String model, String variant) {
		toBlockModel(b, prefix("block/wood/" + variant + "/" + model));
	}

	private void toBlockModel(Block b, ResourceLocation model) {
		withExistingParent(ForgeRegistries.BLOCKS.getKey(b).getPath(), model);
	}

	@Override
	public String getName() {
		return "TwilightForest Item and BlockItem models";
	}
}
