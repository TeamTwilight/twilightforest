package twilightforest.data;

import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.client.model.generators.loaders.ItemLayerModelBuilder;
import net.neoforged.neoforge.client.model.generators.loaders.SeparateTransformsModelBuilder;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredHolder;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFEntities;
import twilightforest.init.TFItems;
import twilightforest.item.Experiment115Item;
import twilightforest.item.HollowLogItem;

import static twilightforest.TwilightForestMod.prefix;

public class ItemModelGenerator extends ItemModelProvider {
	public ItemModelGenerator(PackOutput output, ExistingFileHelper existingFileHelper) {
		super(output, TwilightForestMod.ID, existingFileHelper);
	}

	@Override
	protected void registerModels() {
		for (DeferredHolder<Item, ?> item : TFEntities.SPAWN_EGGS.getEntries()) {
			if (item.get() instanceof SpawnEggItem) {
				getBuilder(item.getId().getPath()).parent(getExistingFile(new ResourceLocation("item/template_spawn_egg")));
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
		toBlock(TFBlocks.RAINY_CLOUD.get());
		toBlock(TFBlocks.SNOWY_CLOUD.get());

		float giant = 4.0F;

		ItemModelBuilder giant_block = withExistingParent("giant_block_base", new ResourceLocation("block/cube")).transforms()
				.transform(ItemDisplayContext.GROUND).translation( 0.0F, 3.0F, 0.0F).scale(0.25F * giant).end()
				.transform(ItemDisplayContext.FIXED).scale(0.5F * giant * 0.625F).end()
				.transform(ItemDisplayContext.THIRD_PERSON_RIGHT_HAND).rotation(75.0F, 45.0F, 0.0F).translation( 0.0F, 2.5F * giant, 0.0F).scale(0.375F * giant).end()
				.transform(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND).rotation(0.0F, 45.0F, 0.0F).translation( 0.0F, -2.5F * giant, -2.5F * giant).scale(0.40F * giant).end()
				.transform(ItemDisplayContext.FIRST_PERSON_LEFT_HAND).rotation(1.0F, 225.0F, 0.0F).translation( 0.0F, -2.5F * giant, -2.5F * giant).scale(0.40F * giant).end().end()
				.element().allFaces((direction, faceBuilder) -> faceBuilder.uvs(0,0,16,16).texture(direction.getAxis() == Direction.Axis.Y ? "#top" : "#all").tintindex(0).cullface(direction).end().end()).end();

		ItemModelBuilder gui_giant = withExistingParent("giant_block_gui", new ResourceLocation("block/cube")).transforms()
				.transform(ItemDisplayContext.GUI).rotation(30.0F, 45.0F, 0.0F).scale(0.625F).end().end()
				.element().allFaces((direction, faceBuilder) -> faceBuilder.uvs(0,0,4,4).texture(direction.getAxis() == Direction.Axis.Y ? "#top" : "#all").tintindex(0).cullface(direction).end().end()).end();

		toGiantModel(TFBlocks.GIANT_COBBLESTONE.get(), new ResourceLocation("block/cobblestone"), giant_block, gui_giant);
		toGiantModel(TFBlocks.GIANT_LOG.get(), new ResourceLocation("block/oak_log"), new ResourceLocation("block/oak_log_top"), giant_block, gui_giant);
		toGiantModel(TFBlocks.GIANT_LEAVES.get(), new ResourceLocation("block/oak_leaves"), giant_block, gui_giant);
		toGiantModel(TFBlocks.GIANT_OBSIDIAN.get(), new ResourceLocation("block/obsidian"), giant_block, gui_giant);

		ItemModelBuilder giant_tool = withExistingParent("giant_tool_base", new ResourceLocation("item/generated")).transforms()
				.transform(ItemDisplayContext.GROUND).translation( 0.0F, 2.0F, 0.0F).scale(2.5F).end()
				.transform(ItemDisplayContext.HEAD).rotation(0.0F, 180.0F, 0.0F).translation( 0.0F, 13.0F, 7.0F).scale(5.0F).end()
				.transform(ItemDisplayContext.FIXED).rotation(0.0F, 180.0F, 0.0F).scale(5.0F).end()
				.transform(ItemDisplayContext.THIRD_PERSON_RIGHT_HAND).rotation(0.0F, -90.0F, 55.0F).translation( -0.1F, 24.0F, -5.5F).scale(4.25F).end()
				.transform(ItemDisplayContext.THIRD_PERSON_LEFT_HAND).rotation(0.0F, 90.0F, -55.0F).translation( -0.1F, 24.0F, -3.5F).scale(4.25F).end()
				.transform(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND).rotation(0.0F, -90.0F, 25.0F).translation(  1.13F, 3.2F, 1.13F).scale(1.7F).end()
				.transform(ItemDisplayContext.FIRST_PERSON_LEFT_HAND).rotation(0.0F, 90.0F, -25.0F).translation( 1.13F, 3.2F, 1.13F).scale(1.7F).end().end();

		toGiantItemModel(TFItems.GIANT_PICKAXE, new ResourceLocation("item/stone_pickaxe"), giant_tool, 7, 2);
		toGiantItemModel(TFItems.GIANT_SWORD, new ResourceLocation("item/stone_sword"), giant_tool, 3, 5);

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
		ModelFile bundle = generated("red_thread_bundle", prefix("item/red_thread_bundle_0"));
		ModelFile spool = generated("red_thread_spool", prefix("item/red_thread_bundle_1"));
		ModelFile large_spool = generated("red_thread_large_spool", prefix("item/red_thread_bundle_2"));
		singleTex(TFBlocks.RED_THREAD)
				.override().predicate(prefix("size"), 0.25F).model(bundle).end()
				.override().predicate(prefix("size"), 0.5F).model(spool).end()
				.override().predicate(prefix("size"), 1.0F).model(large_spool).end();
		toBlock(TFBlocks.HEDGE.get());
		toBlock(TFBlocks.ROOT_BLOCK.get());
		toBlock(TFBlocks.LIVEROOT_BLOCK.get());
		toBlock(TFBlocks.MANGROVE_ROOT.get());
		toBlock(TFBlocks.UNCRAFTING_TABLE.get());
		toBlockModel(TFBlocks.NAGA_BOSS_SPAWNER.get(), prefix("block/boss_spawner"));
		toBlockModel(TFBlocks.LICH_BOSS_SPAWNER.get(), prefix("block/boss_spawner"));
		toBlockModel(TFBlocks.HYDRA_BOSS_SPAWNER.get(), prefix("block/boss_spawner"));
		toBlockModel(TFBlocks.UR_GHAST_BOSS_SPAWNER.get(), prefix("block/boss_spawner"));
		toBlockModel(TFBlocks.KNIGHT_PHANTOM_BOSS_SPAWNER.get(), prefix("block/boss_spawner"));
		toBlockModel(TFBlocks.SNOW_QUEEN_BOSS_SPAWNER.get(), prefix("block/boss_spawner"));
		toBlockModel(TFBlocks.MINOSHROOM_BOSS_SPAWNER.get(), prefix("block/boss_spawner"));
		toBlockModel(TFBlocks.ALPHA_YETI_BOSS_SPAWNER.get(), prefix("block/boss_spawner"));
		toBlockModel(TFBlocks.FINAL_BOSS_BOSS_SPAWNER.get(), prefix("block/boss_spawner"));
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
		toBlockModel(TFBlocks.SPIRAL_BRICKS.get(), prefix("block/spiral_bricks/z_spiral_bottom_right"));
		toBlock(TFBlocks.TWISTED_STONE.get());
		toBlockModel(TFBlocks.TWISTED_STONE_PILLAR.get(), prefix("block/pillar/pillar_inventory"));
		toBlock(TFBlocks.BOLD_STONE_PILLAR.get());
		toBlockModel(TFBlocks.DEATH_TOME_SPAWNER.get(), prefix("block/death_tome_spawner_10"));
		toBlock(TFBlocks.EMPTY_CANOPY_BOOKSHELF.get());
		toBlock(TFBlocks.CANOPY_BOOKSHELF.get());
		generated(TFBlocks.ROPE.getId().getPath(), prefix("block/" + TFBlocks.ROPE.getId().getPath()));;

		toBlockModel(TFBlocks.TERRORCOTTA_LINES.get(), prefix("block/terrorcotta_lines_0"));
		toBlockModel(TFBlocks.TERRORCOTTA_CURVES.get(), prefix("block/terrorcotta_curves_90"));

		withExistingParent(TFBlocks.OAK_BANISTER.getId().toString(), prefix("item/banister_item")).texture("texture", "minecraft:block/oak_planks");
		withExistingParent(TFBlocks.SPRUCE_BANISTER.getId().toString(), prefix("item/banister_item")).texture("texture", "minecraft:block/spruce_planks");
		withExistingParent(TFBlocks.BIRCH_BANISTER.getId().toString(), prefix("item/banister_item")).texture("texture", "minecraft:block/birch_planks");
		withExistingParent(TFBlocks.JUNGLE_BANISTER.getId().toString(), prefix("item/banister_item")).texture("texture", "minecraft:block/jungle_planks");
		withExistingParent(TFBlocks.ACACIA_BANISTER.getId().toString(), prefix("item/banister_item")).texture("texture", "minecraft:block/acacia_planks");
		withExistingParent(TFBlocks.DARK_OAK_BANISTER.getId().toString(), prefix("item/banister_item")).texture("texture", "minecraft:block/dark_oak_planks");
		withExistingParent(TFBlocks.CRIMSON_BANISTER.getId().toString(), prefix("item/banister_item")).texture("texture", "minecraft:block/crimson_planks");
		withExistingParent(TFBlocks.WARPED_BANISTER.getId().toString(), prefix("item/banister_item")).texture("texture", "minecraft:block/warped_planks");
		withExistingParent(TFBlocks.VANGROVE_BANISTER.getId().toString(), prefix("item/banister_item")).texture("texture", "minecraft:block/mangrove_planks");
		withExistingParent(TFBlocks.BAMBOO_BANISTER.getId().toString(), prefix("item/banister_item")).texture("texture", "minecraft:block/bamboo_planks");
		withExistingParent(TFBlocks.CHERRY_BANISTER.getId().toString(), prefix("item/banister_item")).texture("texture", "minecraft:block/cherry_planks");

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
		singleTex(TFItems.TWILIGHT_OAK_SIGN);
		singleTex(TFItems.TWILIGHT_OAK_HANGING_SIGN);
		withExistingParent(TFBlocks.TWILIGHT_OAK_BANISTER.getId().toString(), prefix("item/banister_item")).texture("texture", "block/wood/planks_twilight_oak_0");
		generated(TFBlocks.TWILIGHT_OAK_DOOR.getId().getPath(), prefix("item/" + TFBlocks.TWILIGHT_OAK_DOOR.getId().getPath()));
		withExistingParent(TFBlocks.TWILIGHT_OAK_CHEST.getId().toString(), "item/chest").texture("particle", prefix("block/wood/planks_twilight_oak_0"));
		withExistingParent(TFBlocks.TWILIGHT_OAK_TRAPPED_CHEST.getId().toString(), "item/chest").texture("particle", prefix("block/wood/planks_twilight_oak_0"));

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
		singleTex(TFItems.CANOPY_SIGN);
		singleTex(TFItems.CANOPY_HANGING_SIGN);
		withExistingParent(TFBlocks.CANOPY_BANISTER.getId().toString(), prefix("item/banister_item")).texture("texture", "block/wood/planks_canopy_0");
		generated(TFBlocks.CANOPY_DOOR.getId().getPath(), prefix("item/" + TFBlocks.CANOPY_DOOR.getId().getPath()));
		withExistingParent(TFBlocks.CANOPY_CHEST.getId().toString(), "item/chest").texture("particle", prefix("block/wood/planks_canopy_0"));
		withExistingParent(TFBlocks.CANOPY_TRAPPED_CHEST.getId().toString(), "item/chest").texture("particle", prefix("block/wood/planks_canopy_0"));

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
		singleTex(TFItems.MANGROVE_SIGN);
		singleTex(TFItems.MANGROVE_HANGING_SIGN);
		withExistingParent(TFBlocks.MANGROVE_BANISTER.getId().toString(), prefix("item/banister_item")).texture("texture", "block/wood/planks_mangrove_0");
		generated(TFBlocks.MANGROVE_DOOR.getId().getPath(), prefix("item/" + TFBlocks.MANGROVE_DOOR.getId().getPath()));
		withExistingParent(TFBlocks.MANGROVE_CHEST.getId().toString(), "item/chest").texture("particle", prefix("block/wood/planks_mangrove_0"));
		withExistingParent(TFBlocks.MANGROVE_TRAPPED_CHEST.getId().toString(), "item/chest").texture("particle", prefix("block/wood/planks_mangrove_0"));

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
		singleTex(TFItems.DARK_SIGN);
		singleTex(TFItems.DARK_HANGING_SIGN);
		withExistingParent(TFBlocks.DARK_BANISTER.getId().toString(), prefix("item/banister_item")).texture("texture", "block/wood/planks_darkwood_0");
		generated(TFBlocks.DARK_DOOR.getId().getPath(), prefix("item/" + TFBlocks.DARK_DOOR.getId().getPath()));
		generated(TFBlocks.HOLLOW_OAK_SAPLING.getId().getPath(), prefix("block/" + TFBlocks.HOLLOW_OAK_SAPLING.getId().getPath()));
		withExistingParent(TFBlocks.DARK_CHEST.getId().toString(), "item/chest").texture("particle", prefix("block/wood/planks_darkwood_0"));
		withExistingParent(TFBlocks.DARK_TRAPPED_CHEST.getId().toString(), "item/chest").texture("particle", prefix("block/wood/planks_darkwood_0"));

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
		singleTex(TFItems.TIME_SIGN);
		singleTex(TFItems.TIME_HANGING_SIGN);
		withExistingParent(TFBlocks.TIME_BANISTER.getId().toString(), prefix("item/banister_item")).texture("texture", "block/wood/planks_time_0");
		generated(TFBlocks.TIME_DOOR.getId().getPath(), prefix("item/" + TFBlocks.TIME_DOOR.getId().getPath()));
		withExistingParent(TFBlocks.TIME_CHEST.getId().toString(), "item/chest").texture("particle", prefix("block/wood/planks_time_0"));
		withExistingParent(TFBlocks.TIME_TRAPPED_CHEST.getId().toString(), "item/chest").texture("particle", prefix("block/wood/planks_time_0"));

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
		singleTex(TFItems.TRANSFORMATION_SIGN);
		singleTex(TFItems.TRANSFORMATION_HANGING_SIGN);
		withExistingParent(TFBlocks.TRANSFORMATION_BANISTER.getId().toString(), prefix("item/banister_item")).texture("texture", "block/wood/planks_trans_0");
		generated(TFBlocks.TRANSFORMATION_DOOR.getId().getPath(), prefix("item/" + TFBlocks.TRANSFORMATION_DOOR.getId().getPath()));
		withExistingParent(TFBlocks.TRANSFORMATION_CHEST.getId().toString(), "item/chest").texture("particle", prefix("block/wood/planks_trans_0"));
		withExistingParent(TFBlocks.TRANSFORMATION_TRAPPED_CHEST.getId().toString(), "item/chest").texture("particle", prefix("block/wood/planks_trans_0"));

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
		singleTex(TFItems.MINING_SIGN);
		singleTex(TFItems.MINING_HANGING_SIGN);
		withExistingParent(TFBlocks.MINING_BANISTER.getId().toString(), prefix("item/banister_item")).texture("texture", "block/wood/planks_mine_0");
		generated(TFBlocks.MINING_DOOR.getId().getPath(), prefix("item/" + TFBlocks.MINING_DOOR.getId().getPath()));
		withExistingParent(TFBlocks.MINING_CHEST.getId().toString(), "item/chest").texture("particle", prefix("block/wood/planks_mine_0"));
		withExistingParent(TFBlocks.MINING_TRAPPED_CHEST.getId().toString(), "item/chest").texture("particle", prefix("block/wood/planks_mine_0"));

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
		singleTex(TFItems.SORTING_SIGN);
		singleTex(TFItems.SORTING_HANGING_SIGN);
		withExistingParent(TFBlocks.SORTING_BANISTER.getId().toString(), prefix("item/banister_item")).texture("texture", "block/wood/planks_sort_0");
		generated(TFBlocks.SORTING_DOOR.getId().getPath(), prefix("item/" + TFBlocks.SORTING_DOOR.getId().getPath()));
		withExistingParent(TFBlocks.SORTING_CHEST.getId().toString(), "item/chest").texture("particle", prefix("block/wood/planks_sort_0"));
		withExistingParent(TFBlocks.SORTING_TRAPPED_CHEST.getId().toString(), "item/chest").texture("particle", prefix("block/wood/planks_sort_0"));

		withExistingParent(TFItems.NAGA_TROPHY.getId().toString(), prefix("item/template_trophy"));
		withExistingParent(TFItems.LICH_TROPHY.getId().toString(), prefix("item/template_trophy"));
		withExistingParent(TFItems.MINOSHROOM_TROPHY.getId().toString(), prefix("item/template_trophy"));
		withExistingParent(TFItems.HYDRA_TROPHY.getId().toString(), prefix("item/template_trophy"));
		withExistingParent(TFItems.KNIGHT_PHANTOM_TROPHY.getId().toString(), prefix("item/template_trophy"));
		//ur-ghast and alpha yeti need special transforms
		withExistingParent(TFItems.SNOW_QUEEN_TROPHY.getId().toString(), prefix("item/template_trophy"));
		withExistingParent(TFItems.QUEST_RAM_TROPHY.getId().toString(), prefix("item/template_trophy"));

		withExistingParent(TFItems.CREEPER_SKULL_CANDLE.getId().toString(), prefix("item/template_skull_candle"));
		withExistingParent(TFItems.PIGLIN_SKULL_CANDLE.getId().toString(), prefix("item/template_skull_candle"));
		withExistingParent(TFItems.PLAYER_SKULL_CANDLE.getId().toString(), prefix("item/template_skull_candle"));
		withExistingParent(TFItems.SKELETON_SKULL_CANDLE.getId().toString(), prefix("item/template_skull_candle"));
		withExistingParent(TFItems.WITHER_SKELETON_SKULL_CANDLE.getId().toString(), prefix("item/template_skull_candle"));
		withExistingParent(TFItems.ZOMBIE_SKULL_CANDLE.getId().toString(), prefix("item/template_skull_candle"));

		hollowLog(TFItems.HOLLOW_OAK_LOG);
		hollowLog(TFItems.HOLLOW_SPRUCE_LOG);
		hollowLog(TFItems.HOLLOW_BIRCH_LOG);
		hollowLog(TFItems.HOLLOW_JUNGLE_LOG);
		hollowLog(TFItems.HOLLOW_ACACIA_LOG);
		hollowLog(TFItems.HOLLOW_DARK_OAK_LOG);
		hollowLog(TFItems.HOLLOW_CRIMSON_STEM);
		hollowLog(TFItems.HOLLOW_WARPED_STEM);
		hollowLog(TFItems.HOLLOW_VANGROVE_LOG);
		hollowLog(TFItems.HOLLOW_CHERRY_LOG);
		hollowLog(TFItems.HOLLOW_TWILIGHT_OAK_LOG);
		hollowLog(TFItems.HOLLOW_CANOPY_LOG);
		hollowLog(TFItems.HOLLOW_MANGROVE_LOG);
		hollowLog(TFItems.HOLLOW_DARK_LOG);
		hollowLog(TFItems.HOLLOW_TIME_LOG);
		hollowLog(TFItems.HOLLOW_TRANSFORMATION_LOG);
		hollowLog(TFItems.HOLLOW_MINING_LOG);
		hollowLog(TFItems.HOLLOW_SORTING_LOG);

		singleTex(TFItems.NAGA_SCALE);
		singleTex(TFItems.NAGA_CHESTPLATE);
		singleTex(TFItems.NAGA_LEGGINGS);
		singleTexTool(TFItems.TWILIGHT_SCEPTER);
		singleTexTool(TFItems.LIFEDRAIN_SCEPTER);
		singleTexTool(TFItems.ZOMBIE_SCEPTER);
		singleTexTool(TFItems.FORTIFICATION_SCEPTER);
		singleTex(TFItems.MAGIC_PAINTING);
		ModelFile active = generated("ore_meter_active", prefix("item/ore_meter_active"));
		singleTex(TFItems.ORE_METER).override().predicate(TwilightForestMod.prefix("active"), 1).model(active).end();
		singleTex(TFItems.FILLED_MAGIC_MAP);
		singleTex(TFItems.FILLED_MAZE_MAP);
		singleTex(TFItems.FILLED_ORE_MAP);
		singleTex(TFItems.RAVEN_FEATHER);
		singleTex(TFItems.MAGIC_MAP_FOCUS);
		singleTex(TFItems.MAZE_MAP_FOCUS);
		singleTex(TFItems.LIVEROOT);
		singleTex(TFItems.RAW_IRONWOOD);
		singleTex(TFItems.IRONWOOD_INGOT);
		trimmedArmor(TFItems.IRONWOOD_HELMET);
		trimmedArmor(TFItems.IRONWOOD_CHESTPLATE);
		trimmedArmor(TFItems.IRONWOOD_LEGGINGS);
		trimmedArmor(TFItems.IRONWOOD_BOOTS);
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
		trimmedFullbrightArmor(TFItems.FIERY_HELMET);
		trimmedFullbrightArmor(TFItems.FIERY_CHESTPLATE);
		trimmedFullbrightArmor(TFItems.FIERY_LEGGINGS);
		trimmedFullbrightArmor(TFItems.FIERY_BOOTS);
		singleTexFullbrightTool(TFItems.FIERY_SWORD);
		singleTexFullbrightTool(TFItems.FIERY_PICKAXE);
		singleTex(TFItems.STEELEAF_INGOT);
		trimmedArmor(TFItems.STEELEAF_HELMET);
		trimmedArmor(TFItems.STEELEAF_CHESTPLATE);
		trimmedArmor(TFItems.STEELEAF_LEGGINGS);
		trimmedArmor(TFItems.STEELEAF_BOOTS);
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
		crumbleHorn(TFItems.CRUMBLE_HORN);
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
		trimmedArmor(TFItems.KNIGHTMETAL_HELMET);
		trimmedArmor(TFItems.KNIGHTMETAL_CHESTPLATE);
		trimmedArmor(TFItems.KNIGHTMETAL_LEGGINGS);
		trimmedArmor(TFItems.KNIGHTMETAL_BOOTS);
		singleTexTool(TFItems.KNIGHTMETAL_SWORD);
		singleTexTool(TFItems.KNIGHTMETAL_PICKAXE);
		singleTexTool(TFItems.KNIGHTMETAL_AXE);
		singleTex(TFItems.KNIGHTMETAL_RING);
		trimmedArmor(TFItems.PHANTOM_HELMET);
		trimmedArmor(TFItems.PHANTOM_CHESTPLATE);
		singleTex(TFItems.LAMP_OF_CINDERS);
		singleTex(TFItems.POCKET_WATCH);
		singleTex(TFItems.ALPHA_YETI_FUR);
		//yeti helmets cant be trimmed
		singleTex(TFItems.YETI_HELMET);
		trimmedArmor(TFItems.YETI_CHESTPLATE);
		trimmedArmor(TFItems.YETI_LEGGINGS);
		trimmedArmor(TFItems.YETI_BOOTS);
		singleTex(TFItems.ICE_BOMB);
		singleTex(TFItems.ARCTIC_FUR);
		trimmedLayeredArmor(TFItems.ARCTIC_HELMET);
		trimmedLayeredArmor(TFItems.ARCTIC_CHESTPLATE);
		trimmedLayeredArmor(TFItems.ARCTIC_LEGGINGS);
		trimmedLayeredArmor(TFItems.ARCTIC_BOOTS);
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
		ModelFile splintered = generated("brittle_flask_splintered", prefix("item/brittle_potion_flask_splintered"));
		ModelFile fill1_splintered = generated("brittle_flask_0_splintered", prefix("item/brittle_potion_flask_1"), prefix("item/brittle_potion_flask_splintered"));
		ModelFile fill2_splintered = generated("brittle_flask_1_splintered", prefix("item/brittle_potion_flask_2"), prefix("item/brittle_potion_flask_splintered"));
		ModelFile fill3_splintered = generated("brittle_flask_2_splintered", prefix("item/brittle_potion_flask_3"), prefix("item/brittle_potion_flask_splintered"));
		ModelFile damaged = generated("brittle_flask_damaged", prefix("item/brittle_potion_flask_damaged"));
		ModelFile fill1_damaged = generated("brittle_flask_0_damaged", prefix("item/brittle_potion_flask_1"), prefix("item/brittle_potion_flask_damaged"));
		ModelFile fill2_damaged = generated("brittle_flask_1_damaged", prefix("item/brittle_potion_flask_2"), prefix("item/brittle_potion_flask_damaged"));
		ModelFile fill3_damaged = generated("brittle_flask_2_damaged", prefix("item/brittle_potion_flask_3"), prefix("item/brittle_potion_flask_damaged"));
		generated(TFItems.BRITTLE_FLASK.getId().getPath(), prefix("block/blank"), prefix("item/brittle_potion_flask"))
				.override().predicate(prefix("potion_level"), 1).model(fill1).end()
				.override().predicate(prefix("potion_level"), 2).model(fill2).end()
				.override().predicate(prefix("potion_level"), 3).model(fill3).end()
				.override().predicate(prefix("potion_level"), 0).predicate(prefix("breakage"), 1).model(splintered).end()
				.override().predicate(prefix("potion_level"), 1).predicate(prefix("breakage"), 1).model(fill1_splintered).end()
				.override().predicate(prefix("potion_level"), 2).predicate(prefix("breakage"), 1).model(fill2_splintered).end()
				.override().predicate(prefix("potion_level"), 3).predicate(prefix("breakage"), 1).model(fill3_splintered).end()
				.override().predicate(prefix("potion_level"), 0).predicate(prefix("breakage"), 2).model(damaged).end()
				.override().predicate(prefix("potion_level"), 1).predicate(prefix("breakage"), 2).model(fill1_damaged).end()
				.override().predicate(prefix("potion_level"), 2).predicate(prefix("breakage"), 2).model(fill2_damaged).end()
				.override().predicate(prefix("potion_level"), 3).predicate(prefix("breakage"), 2).model(fill3_damaged).end();

		ModelFile gfill1 = generated("greater_flask_0", prefix("item/greater_potion_flask_1"), prefix("item/greater_potion_flask"));
		ModelFile gfill2 = generated("greater_flask_1", prefix("item/greater_potion_flask_2"), prefix("item/greater_potion_flask"));
		ModelFile gfill3 = generated("greater_flask_2", prefix("item/greater_potion_flask_3"), prefix("item/greater_potion_flask"));

		generated(TFItems.GREATER_FLASK.getId().getPath(), prefix("block/blank"), prefix("item/greater_potion_flask"))
				.override().predicate(prefix("potion_level"), 1).model(gfill1).end()
				.override().predicate(prefix("potion_level"), 2).model(gfill2).end()
				.override().predicate(prefix("potion_level"), 3).model(gfill3).end();

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
		singleTex(TFItems.DARK_BOAT);
		singleTex(TFItems.TIME_BOAT);
		singleTex(TFItems.TRANSFORMATION_BOAT);
		singleTex(TFItems.MINING_BOAT);
		singleTex(TFItems.SORTING_BOAT);

		singleTex(TFItems.TWILIGHT_OAK_CHEST_BOAT);
		singleTex(TFItems.CANOPY_CHEST_BOAT);
		singleTex(TFItems.MANGROVE_CHEST_BOAT);
		singleTex(TFItems.DARK_CHEST_BOAT);
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

	private ItemModelBuilder fullbright(String name, ResourceLocation... layers) {
		return buildItem(name, "item/generated", 15, layers);
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
		if (emissivity > 0) builder = builder.customLoader(ItemLayerModelBuilder::begin).emissive(emissivity, emissivity, 0).renderType("minecraft:translucent", 0).end();
		return builder;
	}

	private ItemModelBuilder forcefield(String name, ResourceLocation... layers) {
		ItemModelBuilder builder = withExistingParent(name, "item/generated");
		for (int i = 0; i < layers.length; i++) {
			builder = builder.texture("layer" + i, layers[i]);
		}
		builder = builder.customLoader(ItemLayerModelBuilder::begin).emissive(15, 15, 0).renderType("minecraft:translucent", 0).end();
		return builder;
	}

	private ItemModelBuilder singleTexFullbright(DeferredHolder<Item, ? extends Item> item) {
		return fullbright(item.getId().getPath(), prefix("item/" + item.getId().getPath()));
	}

	private ItemModelBuilder singleTexFullbrightTool(DeferredHolder<Item, ? extends Item> item) {
		return fullbrightTool(item.getId().getPath(), prefix("item/" + item.getId().getPath()));
	}

	private ItemModelBuilder singleTexTool(DeferredHolder<Item, ? extends Item> item) {
		return tool(item.getId().getPath(), prefix("item/" + item.getId().getPath()));
	}

	private ItemModelBuilder singleTex(DeferredHolder<?, ?> item) {
		return generated(item.getId().getPath(), prefix("item/" + item.getId().getPath()));
	}

	private ItemModelBuilder bowItem(String name, ResourceLocation... layers) {
		ItemModelBuilder builder = withExistingParent(name, "item/bow");
		for (int i = 0; i < layers.length; i++) {
			builder = builder.texture("layer" + i, layers[i]);
		}
		return builder;
	}

	private void bowTex(DeferredHolder<Item, Item> item, ModelFile pull0, ModelFile pull1, ModelFile pull2) {
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
		getBuilder(BuiltInRegistries.BLOCK.getKey(button).getPath())
						.parent(getExistingFile(mcLoc("block/button_inventory")))
						.texture("texture", "block/wood/planks_" + variant + "_0");
	}

	private void woodenFence(Block fence, String variant) {
		getBuilder(BuiltInRegistries.BLOCK.getKey(fence).getPath())
						.parent(getExistingFile(mcLoc("block/fence_inventory")))
						.texture("texture", "block/wood/planks_" + variant + "_0");
	}

	private void hollowLog(DeferredHolder<Item, HollowLogItem> hollowLog) {
		getBuilder(BuiltInRegistries.ITEM.getKey(hollowLog.get().asItem()).getPath()).parent(new ModelFile.ExistingModelFile(TwilightForestMod.prefix("block/" + hollowLog.getId().getPath() + "_horizontal"), this.existingFileHelper));
	}

	private void toBlock(Block b) {
		toBlockModel(b, BuiltInRegistries.BLOCK.getKey(b).getPath());
	}

	private void woodBlock(Block b, String variant) {
		woodBlockModel(b, BuiltInRegistries.BLOCK.getKey(b).getPath(), variant);
	}

	private void toBlockModel(Block b, String model) {
		toBlockModel(b, prefix("block/" + model));
	}

	private void woodBlockModel(Block b, String model, String variant) {
		toBlockModel(b, prefix("block/wood/" + variant + "/" + model));
	}

	private void toBlockModel(Block b, ResourceLocation model) {
		withExistingParent(BuiltInRegistries.BLOCK.getKey(b).getPath(), model);
	}

	private void toGiantModel(Block b, ResourceLocation model, ItemModelBuilder base, ItemModelBuilder gui) {
		toGiantModel(b, model, model, base, gui);
	}

	private void toGiantModel(Block b, ResourceLocation model, ResourceLocation top, ItemModelBuilder base, ItemModelBuilder gui) {
		String name = BuiltInRegistries.BLOCK.getKey(b).getPath();
		withExistingParent(name, model).customLoader(SeparateTransformsModelBuilder::begin)
				.base(withExistingParent(name + "_base", base.getLocation()).texture("all", model).texture("top", top))
				.perspective(ItemDisplayContext.GUI, withExistingParent(name + "_gui", gui.getLocation()).texture("all", model).texture("top", top)).end();
	}

	private void toGiantItemModel(DeferredHolder<Item, Item> item, ResourceLocation parent, ItemModelBuilder base, int x, int y) {
		String name = item.getId().getPath();

		ItemModelBuilder gui = getBuilder(name + "_gui").texture("all", parent)
				.element().from(0,0,0).to(16, 16, 0).face(Direction.SOUTH).texture("#all").uvs(x, y, x + 8, y + 8).tintindex(0).end().end();

		withExistingParent(name, parent).customLoader(SeparateTransformsModelBuilder::begin)
				.base(withExistingParent(name + "_base", base.getLocation()).texture("layer0", parent))
				.perspective(ItemDisplayContext.GUI, gui.texture("all", parent)).end();
	}

	private void trimmedArmor(DeferredHolder<Item, ArmorItem> armor) {
		ItemModelBuilder base = this.singleTex(armor);
		for (ItemModelGenerators.TrimModelData trim : ItemModelGenerators.GENERATED_TRIM_MODELS) {
			String material = trim.name();
			String name = armor.getId().getPath() + "_" + material + "_trim";
			ModelFile trimModel = this.withExistingParent(name, this.mcLoc("item/generated"))
					.texture("layer0", prefix("item/" + armor.getId().getPath()))
					.texture("layer1", this.mcLoc("trims/items/" + armor.get().getType().getName() + "_trim_" + material));
			base.override().predicate(new ResourceLocation("trim_type"), trim.itemModelIndex()).model(trimModel).end();
		}
	}

	private void trimmedFullbrightArmor(DeferredHolder<Item, ArmorItem> armor) {
		ItemModelBuilder base = this.singleTexFullbright(armor);
		for (ItemModelGenerators.TrimModelData trim : ItemModelGenerators.GENERATED_TRIM_MODELS) {
			String material = trim.name();
			String name = armor.getId().getPath() + "_" + material + "_trim";
			ModelFile trimModel = this.withExistingParent(name, this.mcLoc("item/generated"))
					.texture("layer0", prefix("item/" + armor.getId().getPath()))
					.texture("layer1", this.mcLoc("trims/items/" + armor.get().getType().getName() + "_trim_" + material));
			base.override().predicate(new ResourceLocation("trim_type"), trim.itemModelIndex()).model(trimModel).end();
		}
		base.customLoader(ItemLayerModelBuilder::begin).emissive(15, 15, 0).renderType("minecraft:translucent", 0).end();
	}

	private void trimmedLayeredArmor(DeferredHolder<Item, ArmorItem> armor) {
		ItemModelBuilder base = this.generated(armor.getId().getPath(), prefix("item/" + armor.getId().getPath()), prefix("item/" + armor.getId().getPath() + "_0"));;
		for (ItemModelGenerators.TrimModelData trim : ItemModelGenerators.GENERATED_TRIM_MODELS) {
			String material = trim.name();
			String name = armor.getId().getPath() + "_" + material + "_trim";
			ModelFile trimModel = this.withExistingParent(name, this.mcLoc("item/generated"))
					.texture("layer0", prefix("item/" + armor.getId().getPath()))
					.texture("layer1", prefix("item/" + armor.getId().getPath() + "_0"))
					.texture("layer2", this.mcLoc("trims/items/" + armor.get().getType().getName() + "_trim_" + material));
			base.override().predicate(new ResourceLocation("trim_type"), trim.itemModelIndex()).model(trimModel).end();
		}
	}


	private void crumbleHorn(DeferredHolder<Item, Item> item) {
		String name = item.getId().getPath();
		ItemModelBuilder tooting = withExistingParent("tooting_" + name, this.mcLoc("item/tooting_goat_horn"))
				.texture("layer0", prefix("item/" + name));

		withExistingParent(name, this.mcLoc("item/goat_horn"))
				.texture("layer0", prefix("item/" + name))
				.override().predicate(TwilightForestMod.prefix("tooting"), 1).model(tooting);
	}

	@Override
	public String getName() {
		return "TwilightForest Item and BlockItem models";
	}
}
