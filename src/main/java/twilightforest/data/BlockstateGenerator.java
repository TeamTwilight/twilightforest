package twilightforest.data;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import net.minecraft.block.*;
import net.minecraft.data.DataGenerator;
import net.minecraft.state.properties.*;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.*;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.fml.RegistryObject;
import twilightforest.TwilightForestMod;
import twilightforest.block.*;
import twilightforest.enums.FireJetVariant;
import twilightforest.enums.HugeLilypadPiece;
import twilightforest.enums.TowerDeviceVariant;

import javax.annotation.Nonnull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static twilightforest.TwilightForestMod.prefix;

public class BlockstateGenerator extends BlockStateProvider {
	public BlockstateGenerator(DataGenerator gen, ExistingFileHelper exFileHelper) {
		super(gen, TwilightForestMod.ID, exFileHelper);
	}

	@Override
	protected void registerStatesAndModels() {
		tintedAndFlipped(TFBlocks.tower_wood.get());
		simpleBlock(TFBlocks.tower_wood_encased.get(), cubeAllTinted(TFBlocks.tower_wood_encased.getId().getPath(), TFBlocks.tower_wood_encased.getId().getPath()));
		simpleBlock(TFBlocks.tower_wood_cracked.get(), ConfiguredModel.builder()
						.modelFile(cubeAllTinted(TFBlocks.tower_wood_cracked.getId().getPath(), TFBlocks.tower_wood_cracked.getId().getPath())).nextModel()
						.modelFile(cubeAllTinted(TFBlocks.tower_wood_cracked.getId().getPath() + "_flipped", TFBlocks.tower_wood_cracked.getId().getPath(), true)).nextModel()
						.modelFile(cubeAllTinted(TFBlocks.tower_wood_cracked.getId().getPath() + "_alt", TFBlocks.tower_wood_cracked.getId().getPath() + "_alt")).nextModel()
						.modelFile(cubeAllTinted(TFBlocks.tower_wood_cracked.getId().getPath() + "_alt_flipped", TFBlocks.tower_wood_cracked.getId().getPath() + "_alt", true)).build()
		);
		tintedAndFlipped(TFBlocks.tower_wood_mossy.get());
		tintedAndFlipped(TFBlocks.tower_wood_infested.get());

		builtinEntity(TFBlocks.firefly.get(), "minecraft:block/slime_block");
		builtinEntity(TFBlocks.moonworm.get(), "minecraft:block/slime_block");
		builtinEntity(TFBlocks.cicada.get(), "minecraft:block/slime_block");
		builtinEntity(TFBlocks.keepsake_casket.get(), "minecraft:block/chiseled_stone_bricks");

		ModelFile portalModel = models().getExistingFile(prefix("block/twilight_portal"));
		ModelFile portalOverlayModel = models().getExistingFile(prefix("block/twilight_portal_barrier"));
		getMultipartBuilder(TFBlocks.twilight_portal.get())
						.part().modelFile(portalModel).addModel().end()
						.part().modelFile(portalOverlayModel).addModel().condition(BlockTFPortal.DISALLOW_RETURN, true).end();

		getVariantBuilder(TFBlocks.experiment_115.get()).forAllStates(state -> {
			int bitesTaken = state.get(BlockTFExperiment115.BITES_TAKEN);
			String basePath = String.format("block/experiment115_%d_8", 8 - bitesTaken);
			ModelFile model;
			if (state.get(BlockTFExperiment115.REGENERATE)) {
				model = models().withExistingParent(basePath + "_regenerating", prefix(basePath))
								.texture("top_2", "block/experiment115/experiment115_sprinkle");
			} else {
				model = models().getExistingFile(prefix(basePath));
			}
			return ConfiguredModel.builder().modelFile(model).build();
		});

		MultiPartBlockStateBuilder ironLadder = getMultipartBuilder(TFBlocks.iron_ladder.get());
		ModelFile ironLadderLeft = models().getExistingFile(prefix("block/iron_ladder_left"));
		ModelFile ironLadderLeftConnected = models().getExistingFile(prefix("block/iron_ladder_left_connection"));
		ModelFile ironLadderRight = models().getExistingFile(prefix("block/iron_ladder_right"));
		ModelFile ironLadderRightConnected = models().getExistingFile(prefix("block/iron_ladder_right_connection"));
		for (Direction d : Direction.Plane.HORIZONTAL) {
			int rotY;
			switch (d) {
			default: rotY = 0; break;
			case EAST: rotY = 90; break;
			case SOUTH: rotY = 180; break;
			case WEST: rotY = 270; break;
			}

			ironLadder.part().modelFile(ironLadderLeft).rotationY(rotY).addModel()
							.condition(LadderBlock.FACING, d).condition(BlockTFLadderBars.LEFT, false).end();
			ironLadder.part().modelFile(ironLadderLeftConnected).rotationY(rotY).addModel()
							.condition(LadderBlock.FACING, d).condition(BlockTFLadderBars.LEFT, true).end();
			ironLadder.part().modelFile(ironLadderRight).rotationY(rotY).addModel()
							.condition(LadderBlock.FACING, d).condition(BlockTFLadderBars.RIGHT, false).end();
			ironLadder.part().modelFile(ironLadderRightConnected).rotationY(rotY).addModel()
							.condition(LadderBlock.FACING, d).condition(BlockTFLadderBars.RIGHT, true).end();
		}

		//terrorcotta();
		towerBlocks();

		simpleBlock(TFBlocks.fake_gold.get(), models().getExistingFile(new ResourceLocation("block/gold_block")));
		simpleBlock(TFBlocks.fake_diamond.get(), models().getExistingFile(new ResourceLocation("block/diamond_block")));
		
		simpleBlock(TFBlocks.naga_trophy.get(), models().getExistingFile(new ResourceLocation("block/skull")));
		simpleBlock(TFBlocks.lich_trophy.get(), models().getExistingFile(new ResourceLocation("block/skull")));
		simpleBlock(TFBlocks.minoshroom_trophy.get(), models().getExistingFile(new ResourceLocation("block/skull")));
		simpleBlock(TFBlocks.hydra_trophy.get(), models().getExistingFile(new ResourceLocation("block/skull")));
		simpleBlock(TFBlocks.snow_queen_trophy.get(), models().getExistingFile(new ResourceLocation("block/skull")));
		simpleBlock(TFBlocks.knight_phantom_trophy.get(), models().getExistingFile(new ResourceLocation("block/skull")));
		simpleBlock(TFBlocks.ur_ghast_trophy.get(), models().getExistingFile(new ResourceLocation("block/skull")));
		simpleBlock(TFBlocks.quest_ram_trophy.get(), models().getExistingFile(new ResourceLocation("block/skull")));
		simpleBlock(TFBlocks.naga_wall_trophy.get(), models().getExistingFile(new ResourceLocation("block/skull")));
        simpleBlock(TFBlocks.lich_wall_trophy.get(), models().getExistingFile(new ResourceLocation("block/skull")));
        simpleBlock(TFBlocks.minoshroom_wall_trophy.get(), models().getExistingFile(new ResourceLocation("block/skull")));
        simpleBlock(TFBlocks.hydra_wall_trophy.get(), models().getExistingFile(new ResourceLocation("block/skull")));
        simpleBlock(TFBlocks.snow_queen_wall_trophy.get(), models().getExistingFile(new ResourceLocation("block/skull")));
        simpleBlock(TFBlocks.knight_phantom_wall_trophy.get(), models().getExistingFile(new ResourceLocation("block/skull")));
        simpleBlock(TFBlocks.ur_ghast_wall_trophy.get(), models().getExistingFile(new ResourceLocation("block/skull")));
        simpleBlock(TFBlocks.quest_ram_wall_trophy.get(), models().getExistingFile(new ResourceLocation("block/skull")));
        
		ModelFile shieldModel = models().cubeTop(TFBlocks.stronghold_shield.getId().getPath(), prefix("block/shield_outside"), prefix("block/shield_inside"));
		getVariantBuilder(TFBlocks.stronghold_shield.get())
						.forAllStates(state -> {
							Direction dir = state.get(BlockStateProperties.FACING);
							return ConfiguredModel.builder()
											.uvLock(true)
											.modelFile(shieldModel)
											.rotationX(dir == Direction.DOWN ? 180 : dir.getAxis().isHorizontal() ? 90 : 0)
											.rotationY(dir.getAxis().isVertical() ? 0 : (int) dir.getHorizontalAngle() % 360)
											.build();
						});

		trophyPedestal();
		auroraBlocks();
		simpleBlock(TFBlocks.underbrick.get());
		simpleBlock(TFBlocks.underbrick_cracked.get());
		simpleBlock(TFBlocks.underbrick_mossy.get());
		simpleBlock(TFBlocks.underbrick_floor.get());
		thorns();
		simpleBlock(TFBlocks.thorn_rose.get(), models().cross(TFBlocks.thorn_rose.getId().getPath(), blockTexture(TFBlocks.thorn_rose.get())));
		simpleBlock(TFBlocks.thorn_leaves.get(), models().getExistingFile(new ResourceLocation("block/oak_leaves")));
		simpleBlock(TFBlocks.beanstalk_leaves.get(), models().getExistingFile(new ResourceLocation("block/spruce_leaves")));
		simpleBlock(TFBlocks.hollow_oak_sapling.get(), models().cross(TFBlocks.hollow_oak_sapling.getId().getPath(), blockTexture(TFBlocks.hollow_oak_sapling.get())));
		ModelFile deadrock = models().cubeAll(TFBlocks.deadrock.getId().getPath(), blockTexture(TFBlocks.deadrock.get()));
		ModelFile deadrockMirrored = models().withExistingParent(TFBlocks.deadrock.getId().getPath() + "_mirrored", prefix("block/util/cube_mirrored_all"))
						.texture("all", blockTexture(TFBlocks.deadrock.get()));
		simpleBlock(TFBlocks.deadrock.get(), ConfiguredModel.builder()
						.modelFile(deadrock).nextModel()
						.rotationY(180).modelFile(deadrock).nextModel()
						.modelFile(deadrockMirrored).nextModel()
						.rotationY(180).modelFile(deadrockMirrored).build()
		);
		ModelFile deadrockCracked = models().cubeAll(TFBlocks.deadrock_cracked.getId().getPath(), blockTexture(TFBlocks.deadrock_cracked.get()));
		allRotations(TFBlocks.deadrock_cracked.get(), deadrockCracked);
		ModelFile deadrockWeathered = models().cubeAll(TFBlocks.deadrock_weathered.getId().getPath(), blockTexture(TFBlocks.deadrock_weathered.get()));
		allRotations(TFBlocks.deadrock_weathered.get(), deadrockWeathered);
		perFaceBlock(TFBlocks.trollsteinn.get(), blockTexture(TFBlocks.trollsteinn.get()), prefix("block/" + TFBlocks.trollsteinn.getId().getPath() + "_light"));
		simpleBlock(TFBlocks.wispy_cloud.get());
		simpleBlock(TFBlocks.fluffy_cloud.get());
		simpleBlock(TFBlocks.giant_cobblestone.get(), models().withExistingParent(TFBlocks.giant_cobblestone.getId().getPath(), prefix("block/util/giant_block"))
						.texture("all", blockTexture(Blocks.COBBLESTONE)));
		simpleBlock(TFBlocks.giant_log.get(), models().withExistingParent(TFBlocks.giant_log.getId().getPath(), prefix("block/util/giant_column"))
						.texture("side", blockTexture(Blocks.OAK_LOG))
						.texture("end", new ResourceLocation("block/oak_log_top")));
		simpleBlock(TFBlocks.giant_leaves.get(), models().withExistingParent(TFBlocks.giant_leaves.getId().getPath(), prefix("block/util/giant_block"))
						.texture("all", blockTexture(Blocks.OAK_LEAVES)));
		simpleBlock(TFBlocks.giant_obsidian.get(), models().withExistingParent(TFBlocks.giant_obsidian.getId().getPath(), prefix("block/util/giant_block"))
						.texture("all", blockTexture(Blocks.OBSIDIAN)));
		simpleBlock(TFBlocks.uberous_soil.get(), models().withExistingParent(TFBlocks.uberous_soil.getId().getPath(), prefix("block/util/cube_all_2_layer"))
						.texture("all", blockTexture(TFBlocks.uberous_soil.get()))
						.texture("all2", prefix("block/" + TFBlocks.uberous_soil.getId().getPath() + "_glow")));
		simpleBlock(TFBlocks.huge_stalk.get(), models().cubeColumn(TFBlocks.huge_stalk.getId().getPath(),
						prefix("block/" + TFBlocks.huge_stalk.getId().getPath()),
						prefix("block/" + TFBlocks.huge_stalk.getId().getPath() + "_top")));
		perFaceBlock(TFBlocks.huge_mushgloom.get(), prefix("block/huge_gloom_inside"), prefix("block/huge_gloom_cap"));
		simpleBlock(TFBlocks.huge_mushgloom_stem.get());
		simpleBlock(TFBlocks.trollvidr.get(), models().cross(TFBlocks.trollvidr.getId().getPath(), blockTexture(TFBlocks.trollvidr.get())));
		simpleBlock(TFBlocks.unripe_trollber.get(), models().cross(TFBlocks.unripe_trollber.getId().getPath(), blockTexture(TFBlocks.unripe_trollber.get())));
		ModelFile trollber = models().withExistingParent(TFBlocks.trollber.getId().getPath(), prefix("block/util/cross_2_layer"))
						.texture("cross", blockTexture(TFBlocks.trollber.get()))
						.texture("cross2", prefix("block/" + TFBlocks.trollber.getId().getPath() + "_glow"));
		simpleBlock(TFBlocks.trollber.get(), trollber);
		lilyPad(TFBlocks.huge_lilypad.get());
		simpleBlock(TFBlocks.huge_waterlily.get(), models().cross(TFBlocks.huge_waterlily.getId().getPath(), blockTexture(TFBlocks.huge_waterlily.get())));
		simpleBlock(TFBlocks.castle_brick.get());
		simpleBlock(TFBlocks.castle_brick_worn.get());
		simpleBlock(TFBlocks.castle_brick_cracked.get());
		allRotations(TFBlocks.castle_brick_roof.get(), cubeAll(TFBlocks.castle_brick_roof.get()));
		simpleBlock(TFBlocks.castle_brick_mossy.get());
		simpleBlock(TFBlocks.castle_brick_frame.get());

		rotationallyCorrectColumn(TFBlocks.castle_pillar_encased.get());
		rotationallyCorrectColumn(TFBlocks.castle_pillar_bold.get());
		simpleBlock(TFBlocks.castle_pillar_encased_tile.get(), models().cubeAll(TFBlocks.castle_pillar_encased_tile.getId().getPath(), prefix("block/castle_pillar_encased_end")));
		simpleBlock(TFBlocks.castle_pillar_bold_tile.get());
		stairsBlock(TFBlocks.castle_stairs_brick.get(), prefix("block/" + TFBlocks.castle_stairs_brick.getId().getPath()));
		stairsBlock(TFBlocks.castle_stairs_worn.get(), prefix("block/" + TFBlocks.castle_stairs_worn.getId().getPath()));
		stairsBlock(TFBlocks.castle_stairs_cracked.get(), prefix("block/" + TFBlocks.castle_stairs_cracked.getId().getPath()));
		stairsBlock(TFBlocks.castle_stairs_mossy.get(), prefix("block/" + TFBlocks.castle_stairs_mossy.getId().getPath()));
		stairsBlock(TFBlocks.castle_stairs_encased.get(), prefix("block/castle_pillar_encased_h"), prefix("block/castleblock_tile"), prefix("block/castle_brick_roof"));
		stairsBlock(TFBlocks.castle_stairs_bold.get(), prefix("block/castle_pillar_bold_tile"));

		ConfiguredModel[] runeBrickModels = new ConfiguredModel[8];
		for (int i = 0; i < runeBrickModels.length; i++) {
			runeBrickModels[i] = new ConfiguredModel(
							models().withExistingParent("castle_rune_brick_" + i, prefix("block/util/cube_all_2_layer"))
							.texture("all", prefix("block/castle_brick"))
							.texture("all2", prefix("block/castleblock_magic_" + i)));
		}

		simpleBlock(TFBlocks.castle_rune_brick_yellow.get(), runeBrickModels);
		simpleBlock(TFBlocks.castle_rune_brick_purple.get(), runeBrickModels);
		simpleBlock(TFBlocks.castle_rune_brick_pink.get(), runeBrickModels);
		simpleBlock(TFBlocks.castle_rune_brick_blue.get(), runeBrickModels);

		logBlock(TFBlocks.cinder_log.get());
		simpleBlock(TFBlocks.cinder_wood.get(), models().cubeAll(TFBlocks.cinder_wood.getId().getPath(), prefix("block/" + TFBlocks.cinder_log.getId().getPath())));
		ModelFile furnaceOff = models().getExistingFile(new ResourceLocation("block/furnace"));
		ModelFile furnaceOn = models().getExistingFile(new ResourceLocation("block/furnace_on"));
		horizontalBlock(TFBlocks.cinder_furnace.get(), state -> state.get(AbstractFurnaceBlock.LIT) ? furnaceOn : furnaceOff);

		castleDoor(TFBlocks.castle_door_yellow.get());
		castleDoor(TFBlocks.castle_door_purple.get());
		castleDoor(TFBlocks.castle_door_pink.get());
		castleDoor(TFBlocks.castle_door_blue.get());

		simpleBlockExisting(TFBlocks.knightmetal_block.get());
		simpleBlockExisting(TFBlocks.ironwood_block.get());
		simpleBlockExisting(TFBlocks.fiery_block.get());
		simpleBlock(TFBlocks.arctic_fur_block.get());
		ModelFile steeleafBlock = models().cubeAll(TFBlocks.steeleaf_block.getId().getPath(), prefix("block/" + TFBlocks.steeleaf_block.getId().getPath()));
		allRotations(TFBlocks.steeleaf_block.get(), steeleafBlock);
		ModelFile carminiteBlock = models().withExistingParent(TFBlocks.carminite_block.getId().getPath(), prefix("block/util/cube_all_2_layer"))
						.texture("all", prefix("block/" + TFBlocks.carminite_block.getId().getPath()))
						.texture("all2", prefix("block/" + TFBlocks.carminite_block.getId().getPath() + "_overlay"));
		allRotations(TFBlocks.carminite_block.get(), carminiteBlock);

		simpleBlock(TFBlocks.twilight_portal_miniature_structure.get(), models().getExistingFile(prefix("block/miniature/portal")));
		simpleBlock(TFBlocks.naga_courtyard_miniature_structure.get(), models().getExistingFile(prefix("block/miniature/naga_courtyard")));
		simpleBlock(TFBlocks.lich_tower_miniature_structure.get(), models().getExistingFile(prefix("block/miniature/lich_tower")));
		mazestone();
		simpleBlock(TFBlocks.hedge.get(), ConfiguredModel.builder()
						.weight(10).modelFile(models().cubeAll(TFBlocks.hedge.getId().getPath(), blockTexture(TFBlocks.hedge.get()))).nextModel()
						.weight(1).modelFile(models().cubeAll(TFBlocks.hedge.getId().getPath() + "_rose", prefix("block/" + TFBlocks.hedge.getId().getPath() + "_rose"))).build());
		simpleBlock(TFBlocks.boss_spawner.get(), new ConfiguredModel(models().getExistingFile(new ResourceLocation("block/spawner"))));
		simpleBlockExisting(TFBlocks.firefly_jar.get());
		simpleBlockExisting(TFBlocks.cicada_jar.get());
		registerPlantBlocks();
		simpleBlock(TFBlocks.root.get());
		simpleBlock(TFBlocks.liveroot_block.get());
		simpleBlock(TFBlocks.uncrafting_table.get(), models().withExistingParent(TFBlocks.uncrafting_table.getId().getPath(), prefix("block/util/cube_bottom_double_top"))
						.texture("top", prefix("block/uncrafting_top"))
						.texture("glow", prefix("block/uncrafting_glow"))
						.texture("bottom", new ResourceLocation("block/jungle_planks"))
						.texture("side", prefix("block/uncrafting_side")));
		registerSmokersAndJets();
		axisBlock(TFBlocks.stone_twist.get(), prefix("block/stone_twist/twist_side"), prefix("block/stone_twist/twist_end"));
		ConfiguredModel[] lapisModels = new ConfiguredModel[4];
		//for (int i = 0; i < 4; i++) {
		//	String modelName = TFBlocks.lapis_block.getId().getPath();
		//	if (i != 0) {
		//		modelName += "_" + i;
		//	}
		//	lapisModels[i] = new ConfiguredModel(models().cubeAll(modelName, prefix("block/lapis_shale_" + i)));
		//}
		//simpleBlock(TFBlocks.lapis_block.get(), lapisModels);
		registerWoodBlocks();
		registerNagastone();
		registerForceFields();
		simpleBlock(TFBlocks.potted_twilight_oak_sapling.get(), models().getExistingFile(prefix("block/potted_twilight_oak_sapling")));
		simpleBlock(TFBlocks.potted_canopy_sapling.get(), models().getExistingFile(prefix("block/potted_canopy_sapling")));
		simpleBlock(TFBlocks.potted_mangrove_sapling.get(), models().getExistingFile(prefix("block/potted_mangrove_sapling")));
		simpleBlock(TFBlocks.potted_darkwood_sapling.get(), models().getExistingFile(prefix("block/potted_darkwood_sapling")));
		simpleBlock(TFBlocks.potted_hollow_oak_sapling.get(), models().getExistingFile(prefix("block/potted_hollow_oak_sapling")));
		simpleBlock(TFBlocks.potted_rainboak_sapling.get(), models().getExistingFile(prefix("block/potted_rainboak_sapling")));
		simpleBlock(TFBlocks.potted_time_sapling.get(), models().getExistingFile(prefix("block/potted_time_sapling")));
		simpleBlock(TFBlocks.potted_trans_sapling.get(), models().getExistingFile(prefix("block/potted_trans_sapling")));
		simpleBlock(TFBlocks.potted_mine_sapling.get(), models().getExistingFile(prefix("block/potted_mine_sapling")));
		simpleBlock(TFBlocks.potted_sort_sapling.get(), models().getExistingFile(prefix("block/potted_sort_sapling")));
		simpleBlock(TFBlocks.potted_mayapple.get(), models().getExistingFile(prefix("block/potted_mayapple")));
		simpleBlock(TFBlocks.potted_fiddlehead.get(), models().getExistingFile(prefix("block/potted_fiddlehead")));
		simpleBlock(TFBlocks.potted_mushgloom.get(), models().getExistingFile(prefix("block/potted_mushgloom")));
		simpleBlock(TFBlocks.potted_thorn.get(), models().getExistingFile(prefix("block/potted_thorn")));
		simpleBlock(TFBlocks.potted_green_thorn.get(), models().getExistingFile(prefix("block/potted_green_thorn")));
		simpleBlock(TFBlocks.potted_dead_thorn.get(), models().getExistingFile(prefix("block/potted_dead_thorn")));

		builtinEntity(TFBlocks.twilight_oak_sign.get(), "twilightforest:block/wood/planks_twilight_oak_0");
		builtinEntity(TFBlocks.twilight_wall_sign.get(), "twilightforest:block/wood/planks_twilight_oak_0");
		builtinEntity(TFBlocks.canopy_sign.get(), "twilightforest:block/wood/planks_canopy_0");
		builtinEntity(TFBlocks.canopy_wall_sign.get(), "twilightforest:block/wood/planks_canopy_0");
		builtinEntity(TFBlocks.mangrove_sign.get(), "twilightforest:block/wood/planks_mangrove_0");
		builtinEntity(TFBlocks.mangrove_wall_sign.get(), "twilightforest:block/wood/planks_mangrove_0");
		builtinEntity(TFBlocks.darkwood_sign.get(), "twilightforest:block/wood/planks_darkwood_0");
		builtinEntity(TFBlocks.darkwood_wall_sign.get(), "twilightforest:block/wood/planks_darkwood_0");
		builtinEntity(TFBlocks.time_sign.get(), "twilightforest:block/wood/planks_time_0");
		builtinEntity(TFBlocks.time_wall_sign.get(), "twilightforest:block/wood/planks_time_0");
		builtinEntity(TFBlocks.trans_sign.get(), "twilightforest:block/wood/planks_trans_0");
		builtinEntity(TFBlocks.trans_wall_sign.get(), "twilightforest:block/wood/planks_trans_0");
		builtinEntity(TFBlocks.mine_sign.get(), "twilightforest:block/wood/planks_mine_0");
		builtinEntity(TFBlocks.mine_wall_sign.get(), "twilightforest:block/wood/planks_mine_0");
		builtinEntity(TFBlocks.sort_sign.get(), "twilightforest:block/wood/planks_sort_0");
		builtinEntity(TFBlocks.sort_wall_sign.get(), "twilightforest:block/wood/planks_sort_0");

	}

	//TODO: Absolutely not a 100% reflection of what existed
	private void registerForceFields() {
		ImmutableList<RegistryObject<Block>> forceFields = ImmutableList.of(TFBlocks.force_field_pink, TFBlocks.force_field_blue, TFBlocks.force_field_green, TFBlocks.force_field_purple, TFBlocks.force_field_orange);

		for (RegistryObject<Block> block : forceFields) {
			ModelFile post = models().withExistingParent(block.getId().getPath() + "_post", prefix("block/util/pane/post"))
					.texture("pane", prefix("block/forcefield_white"))
					.texture("edge", "#pane");
			ModelFile side = models().withExistingParent(block.getId().getPath() + "_side", prefix("block/util/pane/side"))
					.texture("pane", TwilightForestMod.prefix("block/forcefield_white"))
					.texture("edge", "#pane");
			ModelFile noside = models().withExistingParent(block.getId().getPath() + "_noside", prefix("block/util/pane/noside"))
					.texture("pane", TwilightForestMod.prefix("block/forcefield_white"));
			ModelFile sidealt = models().withExistingParent(block.getId().getPath() + "_side_alt", prefix("block/util/pane/side_alt"))
					.texture("pane", TwilightForestMod.prefix("block/forcefield_white"))
					.texture("edge", "#pane");
			ModelFile nosidealt = models().withExistingParent(block.getId().getPath() + "_noside_alt", prefix("block/util/pane/noside_alt"))
					.texture("pane", TwilightForestMod.prefix("block/forcefield_white"));

			getMultipartBuilder(block.get())
					.part().modelFile(post).addModel().end()
					.part().modelFile(side).addModel().condition(SixWayBlock.NORTH, true).end()
					.part().modelFile(side).rotationY(90).addModel().condition(SixWayBlock.EAST, true).end()
					.part().modelFile(sidealt).addModel().condition(SixWayBlock.SOUTH, true).end()
					.part().modelFile(sidealt).rotationY(90).addModel().condition(SixWayBlock.WEST, true).end()
					.part().modelFile(noside).addModel().condition(SixWayBlock.NORTH, false).end()
					.part().modelFile(noside).rotationY(90).addModel().condition(SixWayBlock.EAST, false).end()
					.part().modelFile(nosidealt).addModel().condition(SixWayBlock.SOUTH, false).end()
					.part().modelFile(nosidealt).rotationY(90).addModel().condition(SixWayBlock.WEST, false).end();
		}

	}

	private void registerNagastone() {
		// todo 1.15 cleanup: generate these models as well instead of getExistingFile-ing them
		String baseName = TFBlocks.naga_stone.getId().getPath();
		ModelFile solid = models().cubeBottomTop(baseName, prefix("block/nagastone_long_side"), prefix("block/nagastone_bottom_long"), prefix("block/nagastone_turn_top"));
		ModelFile down = models().getExistingFile(prefix("block/naga_segment/down"));
		ModelFile up = models().getExistingFile(prefix("block/naga_segment/up"));
		ModelFile horizontal = models().getExistingFile(prefix("block/naga_segment/horizontal"));
		ModelFile vertical = models().getExistingFile(prefix("block/naga_segment/vertical"));
		getVariantBuilder(TFBlocks.naga_stone.get()).forAllStates(s -> {
			switch (s.get(BlockTFNagastone.VARIANT)) {
			case NORTH_DOWN:
				return ConfiguredModel.builder().modelFile(down).rotationY(270).build();
			case SOUTH_DOWN:
				return ConfiguredModel.builder().modelFile(down).rotationY(90).build();
			case WEST_DOWN:
				return ConfiguredModel.builder().modelFile(down).rotationY(180).build();
			case EAST_DOWN:
				return ConfiguredModel.builder().modelFile(down).build();
			case NORTH_UP:
				return ConfiguredModel.builder().modelFile(up).rotationY(270).build();
			case SOUTH_UP:
				return ConfiguredModel.builder().modelFile(up).rotationY(90).build();
			case EAST_UP:
				return ConfiguredModel.builder().modelFile(up).build();
			case WEST_UP:
				return ConfiguredModel.builder().modelFile(up).rotationY(180).build();
			case AXIS_X:
				return ConfiguredModel.builder().modelFile(horizontal).build();
			case AXIS_Y:
				return ConfiguredModel.builder().modelFile(vertical).build();
			case AXIS_Z:
				return ConfiguredModel.builder().modelFile(horizontal).rotationY(90).build();
			default:
			case SOLID:
				return ConfiguredModel.builder().modelFile(solid).build();
			}
		});

		horizontalBlock(TFBlocks.naga_stone_head.get(), models().getExistingFile(prefix("block/" + TFBlocks.naga_stone_head.getId().getPath())));
		nagastonePillar(TFBlocks.nagastone_pillar.get(), "");
		nagastonePillar(TFBlocks.nagastone_pillar_mossy.get(), "_mossy");
		nagastonePillar(TFBlocks.nagastone_pillar_weathered.get(), "_weathered");
		etchedNagastone(TFBlocks.etched_nagastone.get(), "");
		etchedNagastone(TFBlocks.etched_nagastone_mossy.get(), "_mossy");
		etchedNagastone(TFBlocks.etched_nagastone_weathered.get(), "_weathered");
		stairsBlock(TFBlocks.nagastone_stairs_left.get(), prefix("block/etched_nagastone_left"), prefix("block/stone_tiles"), prefix("block/nagastone_bare"));
		stairsBlock(TFBlocks.nagastone_stairs_right.get(), prefix("block/etched_nagastone_right"), prefix("block/stone_tiles"), prefix("block/nagastone_bare"));
		stairsBlock(TFBlocks.nagastone_stairs_mossy_left.get(), prefix("block/etched_nagastone_left_mossy"), prefix("block/stone_tiles_mossy"), prefix("block/nagastone_bare_mossy"));
		stairsBlock(TFBlocks.nagastone_stairs_mossy_right.get(), prefix("block/etched_nagastone_right_mossy"), prefix("block/stone_tiles_mossy"), prefix("block/nagastone_bare_mossy"));
		stairsBlock(TFBlocks.nagastone_stairs_weathered_left.get(), prefix("block/etched_nagastone_left_weathered"), prefix("block/stone_tiles_weathered"), prefix("block/nagastone_bare_weathered"));
		stairsBlock(TFBlocks.nagastone_stairs_weathered_right.get(), prefix("block/etched_nagastone_right_weathered"), prefix("block/stone_tiles_weathered"), prefix("block/nagastone_bare_weathered"));
	}

	private void nagastonePillar(Block b, String suffix) {
		ResourceLocation side = prefix("block/nagastone_pillar_side" + suffix);
		ResourceLocation end = prefix("block/nagastone_pillar_end" + suffix);
		ResourceLocation alt = prefix("block/nagastone_pillar_side" + suffix + "_alt");
		ModelFile model = models().cubeColumn(b.getRegistryName().getPath(), side, end);
		ModelFile reversed = models().cubeColumn(b.getRegistryName().getPath() + "_reversed", alt, end);
		getVariantBuilder(b).forAllStates(state -> {
			int rotX = 0, rotY = 0;
			switch (state.get(RotatedPillarBlock.AXIS)) {
			default:
			case X:
				rotX = rotY = 270;
				break;
			case Y:
				break;
			case Z:
				rotX = 270;
				break;
			}
			ModelFile m = state.get(BlockTFDirectionalRotatedPillar.REVERSED) ? reversed : model;
			return ConfiguredModel.builder().rotationX(rotX).rotationY(rotY).modelFile(m).build();
		});
	}

	private void etchedNagastone(Block b, String suffix) {
		ResourceLocation stoneTiles = prefix("block/stone_tiles" + suffix);
		ResourceLocation upTex = prefix("block/etched_nagastone_up" + suffix);
		ResourceLocation downTex = prefix("block/etched_nagastone_down" + suffix);
		ResourceLocation rightTex = prefix("block/etched_nagastone_right" + suffix);
		ResourceLocation leftTex = prefix("block/etched_nagastone_left" + suffix);

		// todo 1.15 cleanup make this more "logical" with rotations, etc. instead of just reproducing what was in the old blockstate json
		ModelFile down = models().cubeColumn(b.getRegistryName().getPath(), downTex, stoneTiles);
		ModelFile up = models().cubeColumn(b.getRegistryName().getPath() + "_up", upTex, stoneTiles);
		ModelFile north = models().cube(b.getRegistryName().getPath() + "_north", upTex, upTex, stoneTiles, stoneTiles, rightTex, leftTex)
						.texture("particle", "#down");
		ModelFile south = models().cube(b.getRegistryName().getPath() + "_south", downTex, downTex, stoneTiles, stoneTiles, leftTex, rightTex)
						.texture("particle", "#down");
		ModelFile west = models().cube(b.getRegistryName().getPath() + "_west", leftTex, rightTex, rightTex, leftTex, stoneTiles, stoneTiles)
						.texture("particle", "#down");
		ModelFile east = models().cube(b.getRegistryName().getPath() + "_east", rightTex, leftTex, leftTex, rightTex, stoneTiles, stoneTiles)
						.texture("particle", "#down");

		getVariantBuilder(b).partialState()
						.with(DirectionalBlock.FACING, Direction.DOWN).setModels(new ConfiguredModel(down));
		getVariantBuilder(b).partialState()
						.with(DirectionalBlock.FACING, Direction.UP).setModels(new ConfiguredModel(up));
		getVariantBuilder(b).partialState()
						.with(DirectionalBlock.FACING, Direction.NORTH).setModels(new ConfiguredModel(north));
		getVariantBuilder(b).partialState()
						.with(DirectionalBlock.FACING, Direction.SOUTH).setModels(new ConfiguredModel(south));
		getVariantBuilder(b).partialState()
						.with(DirectionalBlock.FACING, Direction.WEST).setModels(new ConfiguredModel(west));
		getVariantBuilder(b).partialState()
						.with(DirectionalBlock.FACING, Direction.EAST).setModels(new ConfiguredModel(east));
	}

	private void registerSmokersAndJets() {
		simpleBlock(TFBlocks.smoker.get(), new ConfiguredModel(models().getExistingFile(prefix("block/jet"))));
		simpleBlock(TFBlocks.fire_jet.get(), new ConfiguredModel(models().getExistingFile(prefix("block/jet"))));

		ModelFile smokerOff = models().withExistingParent(TFBlocks.encased_smoker.getId().getPath(), prefix("block/util/cube_bottom_top_3_layer"))
						.texture("top", prefix("block/towerdev_ghasttraplid_off"))
						.texture("side", prefix("block/towerdev_smoker_off"))
						.texture("bottom", blockTexture(TFBlocks.tower_wood_encased.get()))
						.texture("top2", prefix("block/tower_device_level_2/towerdev_ghasttraplid_off_1"))
						.texture("side2", prefix("block/tower_device_level_1/towerdev_smoker_1"))
						.texture("top3", prefix("block/tower_device_level_2/towerdev_ghasttraplid_off_1"))
						.texture("side3", prefix("block/tower_device_level_2/towerdev_smoker_off_1"));

		ModelFile smokerOn = models().withExistingParent(TFBlocks.encased_smoker.getId().getPath() + "_on", prefix("block/util/cube_bottom_top_3_layer"))
						.texture("top", prefix("block/towerdev_ghasttraplid_on"))
						.texture("side", prefix("block/towerdev_firejet_on"))
						.texture("bottom", blockTexture(TFBlocks.tower_wood_encased.get()))
						.texture("top2", prefix("block/tower_device_level_2/towerdev_ghasttraplid_on_1"))
						.texture("side2", prefix("block/tower_device_level_1/towerdev_smoker_1"))
						.texture("top3", prefix("block/tower_device_level_3/towerdev_ghasttraplid_on_2"))
						.texture("side3", prefix("block/tower_device_level_2/towerdev_smoker_on_1"));
		getVariantBuilder(TFBlocks.encased_smoker.get()).partialState()
						.with(BlockTFEncasedSmoker.ACTIVE, false).setModels(new ConfiguredModel(smokerOff));
		getVariantBuilder(TFBlocks.encased_smoker.get()).partialState()
						.with(BlockTFEncasedSmoker.ACTIVE, true).setModels(new ConfiguredModel(smokerOn));

		ModelFile encasedJetOff = models().withExistingParent(TFBlocks.encased_fire_jet.getId().getPath(), prefix("block/util/cube_bottom_top_3_layer"))
						.texture("top", prefix("block/towerdev_ghasttraplid_off"))
						.texture("side", prefix("block/towerdev_firejet_off"))
						.texture("bottom", blockTexture(TFBlocks.tower_wood_encased.get()))
						.texture("top2", prefix("block/tower_device_level_2/towerdev_ghasttraplid_off_1"))
						.texture("side2", prefix("block/tower_device_level_1/towerdev_firejet_1"))
						.texture("top3", prefix("block/tower_device_level_2/towerdev_ghasttraplid_off_1"))
						.texture("side3", prefix("block/tower_device_level_2/towerdev_firejet_off_1"));

		ModelFile encasedJetOn = models().withExistingParent(TFBlocks.encased_fire_jet.getId().getPath() + "_on", prefix("block/util/cube_bottom_top_3_layer"))
						.texture("top", prefix("block/towerdev_ghasttraplid_on"))
						.texture("side", prefix("block/towerdev_firejet_on"))
						.texture("bottom", blockTexture(TFBlocks.tower_wood_encased.get()))
						.texture("top2", prefix("block/tower_device_level_2/towerdev_ghasttraplid_on_1"))
						.texture("side2", prefix("block/tower_device_level_1/towerdev_firejet_1"))
						.texture("top3", prefix("block/tower_device_level_3/towerdev_ghasttraplid_on_2"))
						.texture("side3", prefix("block/tower_device_level_2/towerdev_firejet_on_1"));

		getVariantBuilder(TFBlocks.encased_fire_jet.get()).partialState()
						.with(BlockTFFireJet.STATE, FireJetVariant.IDLE).setModels(new ConfiguredModel(encasedJetOff));
		getVariantBuilder(TFBlocks.encased_fire_jet.get()).partialState()
						.with(BlockTFFireJet.STATE, FireJetVariant.POPPING).setModels(new ConfiguredModel(encasedJetOn));
		getVariantBuilder(TFBlocks.encased_fire_jet.get()).partialState()
						.with(BlockTFFireJet.STATE, FireJetVariant.FLAME).setModels(new ConfiguredModel(encasedJetOn));
	}

	private void registerPlantBlocks() {
		simpleBlockExisting(TFBlocks.moss_patch.get());
		simpleBlockExisting(TFBlocks.mayapple.get());
		ConfiguredModel[] cloverModels = new ConfiguredModel[4];
		for (int i = 0; i < 4; i++) {
			ModelFile model = models().withExistingParent(TFBlocks.clover_patch.getId().getPath() + "_" + i, prefix("block/util/flat_tex"))
							.texture("particle", prefix("block/cloverpatch"))
							.texture("texture", prefix("block/patch/clover_" + i))
							.texture("ctm", prefix("block/patch/clover_" + i + "_ctm"));
			cloverModels[i] = new ConfiguredModel(model);
		}
		simpleBlock(TFBlocks.clover_patch.get(), cloverModels);
		simpleBlock(TFBlocks.fiddlehead.get(), models().withExistingParent(TFBlocks.fiddlehead.getId().getPath(), "block/tinted_cross")
						.texture("cross", blockTexture(TFBlocks.fiddlehead.get())));
		simpleBlock(TFBlocks.mushgloom.get(), models().withExistingParent(TFBlocks.mushgloom.getId().getPath(), prefix("block/util/cross_2_layer"))
						.texture("cross", blockTexture(TFBlocks.mushgloom.get()))
						.texture("cross2", prefix("block/" + TFBlocks.mushgloom.getId().getPath() + "_head")));
		simpleBlock(TFBlocks.torchberry_plant.get(), models().withExistingParent(TFBlocks.torchberry_plant.getId().getPath(), prefix("block/util/cross_2_layer"))
						.texture("cross", blockTexture(TFBlocks.torchberry_plant.get()))
						.texture("cross2", prefix("block/" + TFBlocks.torchberry_plant.getId().getPath() + "_glow")));
		simpleBlockExisting(TFBlocks.root_strand.get());
		simpleBlockExisting(TFBlocks.fallen_leaves.get());
	}

	private void registerWoodBlocks() {
		logWoodSapling(TFBlocks.oak_log.get(), TFBlocks.oak_wood.get(), TFBlocks.oak_sapling.get());
		plankBlocks("twilight_oak", TFBlocks.twilight_oak_planks.get(), TFBlocks.twilight_oak_slab.get(), TFBlocks.twilight_oak_stairs.get(), TFBlocks.twilight_oak_button.get(), TFBlocks.twilight_oak_fence.get(), TFBlocks.twilight_oak_gate.get(), TFBlocks.twilight_oak_plate.get(), TFBlocks.twilight_oak_door.get(), TFBlocks.twilight_oak_trapdoor.get());
		singleBlockBoilerPlate(TFBlocks.oak_leaves.get(), "block/leaves", m -> m.texture("all", "minecraft:block/oak_leaves"));

		ResourceLocation rainboakSaplTex = prefix("block/" + TFBlocks.rainboak_sapling.getId().getPath());
		simpleBlock(TFBlocks.rainboak_sapling.get(), models().cross(TFBlocks.rainboak_sapling.getId().getPath(), rainboakSaplTex));
		singleBlockBoilerPlate(TFBlocks.rainboak_leaves.get(), "block/leaves", m -> m.texture("all", "minecraft:block/oak_leaves"));

		logWoodSapling(TFBlocks.canopy_log.get(), TFBlocks.canopy_wood.get(), TFBlocks.canopy_sapling.get());
		plankBlocks("canopy", TFBlocks.canopy_planks.get(), TFBlocks.canopy_slab.get(), TFBlocks.canopy_stairs.get(), TFBlocks.canopy_button.get(), TFBlocks.canopy_fence.get(), TFBlocks.canopy_gate.get(), TFBlocks.canopy_plate.get(), TFBlocks.canopy_door.get(), TFBlocks.canopy_trapdoor.get());
		singleBlockBoilerPlate(TFBlocks.canopy_leaves.get(), "block/leaves", m -> m.texture("all", "minecraft:block/spruce_leaves"));

		logWoodSapling(TFBlocks.mangrove_log.get(), TFBlocks.mangrove_wood.get(), TFBlocks.mangrove_sapling.get());
		plankBlocks("mangrove", TFBlocks.mangrove_planks.get(), TFBlocks.mangrove_slab.get(), TFBlocks.mangrove_stairs.get(), TFBlocks.mangrove_button.get(), TFBlocks.mangrove_fence.get(), TFBlocks.mangrove_gate.get(), TFBlocks.mangrove_plate.get(), TFBlocks.mangrove_door.get(), TFBlocks.mangrove_trapdoor.get());
		singleBlockBoilerPlate(TFBlocks.mangrove_leaves.get(), "block/leaves", m -> m.texture("all", "minecraft:block/birch_leaves"));

		logWoodSapling(TFBlocks.dark_log.get(), TFBlocks.dark_wood.get(), TFBlocks.darkwood_sapling.get());
		plankBlocks("darkwood", TFBlocks.dark_planks.get(), TFBlocks.dark_slab.get(), TFBlocks.dark_stairs.get(), TFBlocks.dark_button.get(), TFBlocks.dark_fence.get(), TFBlocks.dark_gate.get(), TFBlocks.dark_plate.get(), TFBlocks.dark_door.get(), TFBlocks.dark_trapdoor.get());
		singleBlockBoilerPlate(TFBlocks.dark_leaves.get(), "block/leaves", m -> m.texture("all", "block/darkwood_leaves"));

		logWoodSapling(TFBlocks.time_log.get(), TFBlocks.time_wood.get(), TFBlocks.time_sapling.get());
		plankBlocks("time", TFBlocks.time_planks.get(), TFBlocks.time_slab.get(), TFBlocks.time_stairs.get(), TFBlocks.time_button.get(), TFBlocks.time_fence.get(), TFBlocks.time_gate.get(), TFBlocks.time_plate.get(), TFBlocks.time_door.get(), TFBlocks.time_trapdoor.get());
		singleBlockBoilerPlate(TFBlocks.time_leaves.get(), "block/leaves", m -> m.texture("all", "block/time_leaves"));
		magicLogCore(TFBlocks.time_log_core.get());

		logWoodSapling(TFBlocks.transformation_log.get(), TFBlocks.transformation_wood.get(), TFBlocks.transformation_sapling.get());
		plankBlocks("trans", TFBlocks.trans_planks.get(), TFBlocks.trans_slab.get(), TFBlocks.trans_stairs.get(), TFBlocks.trans_button.get(), TFBlocks.trans_fence.get(), TFBlocks.trans_gate.get(), TFBlocks.trans_plate.get(), TFBlocks.trans_door.get(), TFBlocks.trans_trapdoor.get());
		singleBlockBoilerPlate(TFBlocks.transformation_leaves.get(), "block/leaves", m -> m.texture("all", "block/transformation_leaves"));
		magicLogCore(TFBlocks.transformation_log_core.get());

		logWoodSapling(TFBlocks.mining_log.get(), TFBlocks.mining_wood.get(), TFBlocks.mining_sapling.get());
		plankBlocks("mine", TFBlocks.mine_planks.get(), TFBlocks.mine_slab.get(), TFBlocks.mine_stairs.get(), TFBlocks.mine_button.get(), TFBlocks.mine_fence.get(), TFBlocks.mine_gate.get(), TFBlocks.mine_plate.get(), TFBlocks.mine_door.get(), TFBlocks.mine_trapdoor.get());
		singleBlockBoilerPlate(TFBlocks.mining_leaves.get(), "block/leaves", m -> m.texture("all", "block/mining_leaves"));
		magicLogCore(TFBlocks.mining_log_core.get());

		logWoodSapling(TFBlocks.sorting_log.get(), TFBlocks.sorting_wood.get(), TFBlocks.sorting_sapling.get());
		plankBlocks("sort", TFBlocks.sort_planks.get(), TFBlocks.sort_slab.get(), TFBlocks.sort_stairs.get(), TFBlocks.sort_button.get(), TFBlocks.sort_fence.get(), TFBlocks.sort_gate.get(), TFBlocks.sort_plate.get(), TFBlocks.sort_door.get(), TFBlocks.sort_trapdoor.get());
		singleBlockBoilerPlate(TFBlocks.sorting_leaves.get(), "block/leaves", m -> m.texture("all", "block/sorting_leaves"));
		magicLogCore(TFBlocks.sorting_log_core.get());
	}

	private void magicLogCore(Block b) {
		ResourceLocation topTex = prefix("block/" + b.getRegistryName().getPath().replace("_core", "_top"));
		ModelFile off = models().cubeColumn(b.getRegistryName().getPath(), blockTexture(b), topTex);
		ModelFile on = models().cubeColumn(b.getRegistryName().getPath() + "_on", prefix("block/" + b.getRegistryName().getPath() + "_on"), topTex);
		getVariantBuilder(b).forAllStates(s -> {
			ModelFile f = s.get(BlockTFMagicLogSpecial.ACTIVE) ? on : off;
			Direction.Axis axis = s.get(RotatedPillarBlock.AXIS);
			int rotX = axis == Direction.Axis.X || axis == Direction.Axis.Z ? 90 : 0;
			int rotY = axis == Direction.Axis.X ? 90 : 0;
			return ConfiguredModel.builder()
							.modelFile(f).rotationX(rotX).rotationY(rotY).build();
		});
	}

	private void rotationallyCorrectColumn(Block b) {
		ResourceLocation side = prefix("block/" + b.getRegistryName().getPath() + "_side");
		ResourceLocation end = prefix("block/" + b.getRegistryName().getPath() + "_end");
		ConfiguredModel yModel = new ConfiguredModel(models().cubeColumn(b.getRegistryName().getPath(), side, end));
		ConfiguredModel xModel = ConfiguredModel.builder()
						.modelFile(models().withExistingParent(b.getRegistryName().getPath() + "_x", prefix("block/util/cube_column_rotationally_correct_x"))
										.texture("side", side).texture("end", end))
						.rotationX(90).rotationY(90)
						.buildLast();
		ConfiguredModel zModel = ConfiguredModel.builder()
						.modelFile(models().withExistingParent(b.getRegistryName().getPath() + "_z", prefix("block/util/cube_column_rotationally_correct_z"))
										.texture("side", side).texture("end", end))
						.rotationX(90)
						.buildLast();
		getVariantBuilder(b)
						.partialState().with(RotatedPillarBlock.AXIS, Direction.Axis.Y).setModels(yModel)
						.partialState().with(RotatedPillarBlock.AXIS, Direction.Axis.X).setModels(xModel)
						.partialState().with(RotatedPillarBlock.AXIS, Direction.Axis.Z).setModels(zModel);
	}

	private void castleDoor(Block b) {
		ModelFile overlay = models().getExistingFile(prefix("block/castle_door_overlay"));
		ModelFile main = models().cubeAll(b.getRegistryName().getPath(), prefix("block/castle_door"));
		getMultipartBuilder(b)
						.part().modelFile(overlay).addModel().end()
						.part().modelFile(main).addModel().condition(BlockTFCastleDoor.VANISHED, false).end();
	}

	private void allRotations(Block b, ModelFile model) {
		ConfiguredModel.Builder<?> builder = ConfiguredModel.builder();
		int[] rots = { 0, 90, 180, 270 };
		for (int x : rots) {
			for (int y : rots) {
				builder = builder.modelFile(model).rotationX(x).rotationY(y);
				if (x != rots[rots.length - 1] && y != rots[rots.length - 1]) {
					builder = builder.nextModel();
				}
			}
		}
		simpleBlock(b, builder.build());
	}

	private void builtinEntity(Block b, String particle) {
		simpleBlock(b, models().getBuilder(b.getRegistryName().getPath())
						.parent(new ModelFile.UncheckedModelFile("builtin/entity"))
						.texture("particle", particle));
	}

	private void simpleBlockExisting(Block b) {
		simpleBlock(b, new ConfiguredModel(models().getExistingFile(prefix(b.getRegistryName().getPath()))));
	}

	/**
	 * Generates a blockstate json pointing to one single generated model with the same name as the block
	 * The single generated model has the given parent and can be customized.
	 */
	private void singleBlockBoilerPlate(Block b, String parent, Consumer<BlockModelBuilder> modelCustomizer) {
		BlockModelBuilder bModel = models().withExistingParent(b.getRegistryName().getPath(), parent);
		modelCustomizer.accept(bModel);
		simpleBlock(b, bModel);
	}

	private BlockModelBuilder cubeAllTinted(String name, String all, boolean flipV) {
		String parent = flipV ? "block/util/tinted_cube_all_flipped_v" : "block/util/tinted_cube_all";
		return models().withExistingParent(name, prefix(parent)).texture("all", "block/" + all);
	}

	private BlockModelBuilder cubeAllTinted(String name, String all) {
		return cubeAllTinted(name, all, false);
	}

	private void tintedAndFlipped(Block b) {
		simpleBlock(b, ConfiguredModel.builder()
						.modelFile(cubeAllTinted(b.getRegistryName().getPath(), b.getRegistryName().getPath())).nextModel()
						.modelFile(cubeAllTinted(b.getRegistryName().getPath() + "_flipped", b.getRegistryName().getPath(), true)).build()
		);
	}

	private void logWoodSapling(RotatedPillarBlock log, Block wood, Block sapling) {
		logBlock(log);
		ResourceLocation sideTex = blockTexture(log);
		simpleBlock(wood, models().cubeAll(wood.getRegistryName().getPath(), sideTex));

		ResourceLocation saplingTex = prefix("block/" + sapling.getRegistryName().getPath());
		simpleBlock(sapling, models().cross(sapling.getRegistryName().getPath(), saplingTex));
	}

	private void plankBlocks(String variant, Block plank, Block slab, StairsBlock stair, Block button, Block fence, Block gate, Block plate, DoorBlock door, TrapDoorBlock trapdoor) {
		String plankTexName = "planks_" + variant;
		ResourceLocation tex0 = prefix("block/wood/" + plankTexName + "_0");
		ResourceLocation tex1 = prefix("block/wood/" + plankTexName + "_1");
		ResourceLocation tex2 = prefix("block/wood/" + plankTexName + "_2");
		ResourceLocation tex3 = prefix("block/wood/" + plankTexName + "_3");
		ConfiguredModel[] plankModels = ConfiguredModel.builder()
						.weight(10).modelFile(models().cubeAll(plank.getRegistryName().getPath(), tex0)).nextModel()
						.weight(10).modelFile(models().cubeAll(plank.getRegistryName().getPath() + "_1", tex1)).nextModel()
						.weight(1).modelFile(models().cubeAll(plank.getRegistryName().getPath() + "_2", tex2)).nextModel()
						.weight(1).modelFile(models().cubeAll(plank.getRegistryName().getPath() + "_3", tex3)).build();
		simpleBlock(plank, plankModels);

		ConfiguredModel[] bottomSlabModels = ConfiguredModel.builder()
						.weight(10).modelFile(models().slab(slab.getRegistryName().getPath(), tex0, tex0, tex0)).nextModel()
						.weight(10).modelFile(models().slab(slab.getRegistryName().getPath() + "_1", tex1, tex1, tex1)).nextModel()
						.weight(1).modelFile(models().slab(slab.getRegistryName().getPath() + "_2", tex2, tex2, tex2)).nextModel()
						.weight(1).modelFile(models().slab(slab.getRegistryName().getPath() + "_3", tex3, tex3, tex3)).build();
		ConfiguredModel[] topSlabModels = ConfiguredModel.builder()
						.weight(10).uvLock(true).rotationX(180).modelFile(bottomSlabModels[0].model).nextModel()
						.weight(10).uvLock(true).rotationX(180).modelFile(bottomSlabModels[1].model).nextModel()
						.weight(1).uvLock(true).rotationX(180).modelFile(bottomSlabModels[2].model).nextModel()
						.weight(1).uvLock(true).rotationX(180).modelFile(bottomSlabModels[3].model).build();
		getVariantBuilder(slab).partialState().with(SlabBlock.TYPE, SlabType.BOTTOM).setModels(bottomSlabModels);
		getVariantBuilder(slab).partialState().with(SlabBlock.TYPE, SlabType.TOP).setModels(topSlabModels);
		getVariantBuilder(slab).partialState().with(SlabBlock.TYPE, SlabType.DOUBLE).setModels(plankModels);

		woodStairs(stair, plankTexName);
		woodButton(button, plankTexName);
		woodFence(fence, plankTexName);
		woodGate(gate, plankTexName);
		woodPlate(plate, plankTexName);
		doorBlock(door, prefix("block/wood/door/" + variant + "_lower"), prefix("block/wood/door/" + variant + "_upper"));
		trapdoorBlock(trapdoor, prefix("block/wood/trapdoor/" + variant + "_trapdoor"), true);
	}

	private void woodGate(Block gate, String texName) {
		ResourceLocation tex0 = prefix("block/wood/" + texName + "_0");
		ResourceLocation tex1 = prefix("block/wood/" + texName + "_1");
		ResourceLocation tex2 = prefix("block/wood/" + texName + "_2");
		ResourceLocation tex3 = prefix("block/wood/" + texName + "_3");

		ModelFile gate0 = models().fenceGate(gate.getRegistryName().getPath(), tex0);
		ModelFile gate1 = models().fenceGate(gate.getRegistryName().getPath() + "_1", tex1);
		ModelFile gate2 = models().fenceGate(gate.getRegistryName().getPath() + "_2", tex2);
		ModelFile gate3 = models().fenceGate(gate.getRegistryName().getPath() + "_3", tex3);
		ModelFile open0 = models().fenceGateOpen(gate.getRegistryName().getPath() + "_open", tex0);
		ModelFile open1 = models().fenceGateOpen(gate.getRegistryName().getPath() + "_open_1", tex1);
		ModelFile open2 = models().fenceGateOpen(gate.getRegistryName().getPath() + "_open_2", tex2);
		ModelFile open3 = models().fenceGateOpen(gate.getRegistryName().getPath() + "_open_3", tex3);
		ModelFile wall0 = models().fenceGateWall(gate.getRegistryName().getPath() + "_wall", tex0);
		ModelFile wall1 = models().fenceGateWall(gate.getRegistryName().getPath() + "_wall_1", tex1);
		ModelFile wall2 = models().fenceGateWall(gate.getRegistryName().getPath() + "_wall_2", tex2);
		ModelFile wall3 = models().fenceGateWall(gate.getRegistryName().getPath() + "_wall_3", tex3);
		ModelFile wallOpen0 = models().fenceGateWallOpen(gate.getRegistryName().getPath() + "_wall_open", tex0);
		ModelFile wallOpen1 = models().fenceGateWallOpen(gate.getRegistryName().getPath() + "_wall_open_1", tex1);
		ModelFile wallOpen2 = models().fenceGateWallOpen(gate.getRegistryName().getPath() + "_wall_open_2", tex2);
		ModelFile wallOpen3 = models().fenceGateWallOpen(gate.getRegistryName().getPath() + "_wall_open_3", tex3);

		// [VanillaCopy] super.fenceGateBlock except with more models
		getVariantBuilder(gate).forAllStatesExcept(state -> {
			ModelFile model0 = gate0;
			ModelFile model1 = gate1;
			ModelFile model2 = gate2;
			ModelFile model3 = gate3;
			if (state.get(FenceGateBlock.IN_WALL)) {
				model0 = wall0;
				model1 = wall1;
				model2 = wall2;
				model3 = wall3;
			}
			if (state.get(FenceGateBlock.OPEN)) {
				model0 = model0 == wall0 ? wallOpen0 : open0;
				model1 = model1 == wall1 ? wallOpen1 : open1;
				model2 = model2 == wall2 ? wallOpen2 : open2;
				model3 = model3 == wall3 ? wallOpen3 : open3;
			}
			return ConfiguredModel.builder()
							.weight(10).modelFile(model0)
							.rotationY((int) state.get(HorizontalBlock.HORIZONTAL_FACING).getHorizontalAngle())
							.uvLock(true).nextModel()

							.weight(10).modelFile(model1)
							.rotationY((int) state.get(HorizontalBlock.HORIZONTAL_FACING).getHorizontalAngle())
							.uvLock(true).nextModel()

							.weight(1).modelFile(model2)
							.rotationY((int) state.get(HorizontalBlock.HORIZONTAL_FACING).getHorizontalAngle())
							.uvLock(true).nextModel()

							.weight(1).modelFile(model3)
							.rotationY((int) state.get(HorizontalBlock.HORIZONTAL_FACING).getHorizontalAngle())
							.uvLock(true)
							.build();
		}, FenceGateBlock.POWERED);
	}

	private void woodFence(Block fence, String texName) {
		ResourceLocation tex0 = prefix("block/wood/" + texName + "_0");
		ResourceLocation tex1 = prefix("block/wood/" + texName + "_1");
		ResourceLocation tex2 = prefix("block/wood/" + texName + "_2");
		ResourceLocation tex3 = prefix("block/wood/" + texName + "_3");

		ModelFile post0 = models().fencePost(fence.getRegistryName().getPath() + "_post", tex0);
		ModelFile post1 = models().fencePost(fence.getRegistryName().getPath() + "_post_1", tex1);
		ModelFile post2 = models().fencePost(fence.getRegistryName().getPath() + "_post_2", tex2);
		ModelFile post3 = models().fencePost(fence.getRegistryName().getPath() + "_post_3", tex3);
		ModelFile side0 = models().fenceSide(fence.getRegistryName().getPath() + "_side", tex0);
		ModelFile side1 = models().fenceSide(fence.getRegistryName().getPath() + "_side_1", tex1);
		ModelFile side2 = models().fenceSide(fence.getRegistryName().getPath() + "_side_2", tex2);
		ModelFile side3 = models().fenceSide(fence.getRegistryName().getPath() + "_side_3", tex3);

		// [VanillaCopy] super.fourWayBlock, but with more models
		MultiPartBlockStateBuilder builder = getMultipartBuilder(fence).part()
						.weight(10).modelFile(post0).nextModel()
						.weight(10).modelFile(post1).nextModel()
						.weight(1).modelFile(post2).nextModel()
						.weight(1).modelFile(post3).addModel().end();
		SixWayBlock.FACING_TO_PROPERTY_MAP.forEach((dir, value) -> {
			if (dir.getAxis().isHorizontal()) {
				builder.part()
						.weight(10).modelFile(side0).rotationY((((int) dir.getHorizontalAngle()) + 180) % 360).uvLock(true).nextModel()
						.weight(10).modelFile(side1).rotationY((((int) dir.getHorizontalAngle()) + 180) % 360).uvLock(true).nextModel()
						.weight(1).modelFile(side2).rotationY((((int) dir.getHorizontalAngle()) + 180) % 360).uvLock(true).nextModel()
						.weight(1).modelFile(side3).rotationY((((int) dir.getHorizontalAngle()) + 180) % 360).uvLock(true)
						.addModel()
						.condition(value, true);
			}
		});
	}

	private void woodPlate(Block plate, String texName) {
		ResourceLocation tex0 = prefix("block/wood/" + texName + "_0");
		ResourceLocation tex1 = prefix("block/wood/" + texName + "_1");
		ResourceLocation tex2 = prefix("block/wood/" + texName + "_2");
		ResourceLocation tex3 = prefix("block/wood/" + texName + "_3");
		ConfiguredModel[] unpressed = ConfiguredModel.builder()
						.weight(10).modelFile(models().withExistingParent(plate.getRegistryName().getPath(), "pressure_plate_up").texture("texture", tex0)).nextModel()
						.weight(10).modelFile(models().withExistingParent(plate.getRegistryName().getPath() + "_1", "pressure_plate_up").texture("texture", tex1)).nextModel()
						.weight(1).modelFile(models().withExistingParent(plate.getRegistryName().getPath() + "_2", "pressure_plate_up").texture("texture", tex2)).nextModel()
						.weight(1).modelFile(models().withExistingParent(plate.getRegistryName().getPath() + "_3", "pressure_plate_up").texture("texture", tex3)).build();
		ConfiguredModel[] pressed = ConfiguredModel.builder()
						.weight(10).modelFile(models().withExistingParent(plate.getRegistryName().getPath() + "_down", "pressure_plate_down").texture("texture", tex0)).nextModel()
						.weight(10).modelFile(models().withExistingParent(plate.getRegistryName().getPath() + "_down_1", "pressure_plate_down").texture("texture", tex1)).nextModel()
						.weight(1).modelFile(models().withExistingParent(plate.getRegistryName().getPath() + "_down_2", "pressure_plate_down").texture("texture", tex2)).nextModel()
						.weight(1).modelFile(models().withExistingParent(plate.getRegistryName().getPath() + "_down_3", "pressure_plate_down").texture("texture", tex3)).build();

		getVariantBuilder(plate).partialState().with(PressurePlateBlock.POWERED, false).setModels(unpressed);
		getVariantBuilder(plate).partialState().with(PressurePlateBlock.POWERED, true).setModels(pressed);
	}

	private void woodButton(Block button, String texName) {
		ResourceLocation tex0 = prefix("block/wood/" + texName + "_0");
		ResourceLocation tex1 = prefix("block/wood/" + texName + "_1");
		ResourceLocation tex2 = prefix("block/wood/" + texName + "_2");
		ResourceLocation tex3 = prefix("block/wood/" + texName + "_3");
		ModelFile unpressed0 = models().withExistingParent(button.getRegistryName().getPath(), "button").texture("texture", tex0);
		ModelFile pressed0 = models().withExistingParent(button.getRegistryName().getPath() + "_pressed", "button_pressed").texture("texture", tex0);
		ModelFile unpressed1 = models().withExistingParent(button.getRegistryName().getPath() + "_1", "button").texture("texture", tex1);
		ModelFile pressed1 = models().withExistingParent(button.getRegistryName().getPath() + "_pressed_1", "button_pressed").texture("texture", tex1);
		ModelFile unpressed2 = models().withExistingParent(button.getRegistryName().getPath() + "_2", "button").texture("texture", tex2);
		ModelFile pressed2 = models().withExistingParent(button.getRegistryName().getPath() + "_pressed_2", "button_pressed").texture("texture", tex2);
		ModelFile unpressed3 = models().withExistingParent(button.getRegistryName().getPath() + "_3", "button").texture("texture", tex3);
		ModelFile pressed3 = models().withExistingParent(button.getRegistryName().getPath() + "_pressed_3", "button_pressed").texture("texture", tex3);

		getVariantBuilder(button).forAllStates(state -> {
			ModelFile model0 = state.get(AbstractButtonBlock.POWERED) ? pressed0 : unpressed0;
			ModelFile model1 = state.get(AbstractButtonBlock.POWERED) ? pressed1 : unpressed1;
			ModelFile model2 = state.get(AbstractButtonBlock.POWERED) ? pressed2 : unpressed2;
			ModelFile model3 = state.get(AbstractButtonBlock.POWERED) ? pressed3 : unpressed3;
			int rotX = 0;
			switch (state.get(HorizontalFaceBlock.FACE)) {
			case WALL:
				rotX = 90;
				break;
			case FLOOR:
				rotX = 0;
				break;
			case CEILING:
				rotX = 180;
				break;
			}
			int rotY = 0;
			if (state.get(HorizontalFaceBlock.FACE) == AttachFace.CEILING)  {
				switch (state.get(HorizontalBlock.HORIZONTAL_FACING)) {
				case NORTH: rotY = 180; break;
				case SOUTH: rotY = 0; break;
				case WEST: rotY = 90; break;
				case EAST: rotY = 270; break;
				}
			} else {
				switch (state.get(HorizontalBlock.HORIZONTAL_FACING)) {
				case NORTH: rotY = 0; break;
				case SOUTH: rotY = 180; break;
				case WEST: rotY = 270; break;
				case EAST: rotY = 90; break;
				}
			}
			boolean uvlock = state.get(HorizontalFaceBlock.FACE) == AttachFace.WALL;

			return ConfiguredModel.builder()
							.weight(10).uvLock(uvlock).rotationX(rotX).rotationY(rotY).modelFile(model0).nextModel()
							.weight(10).uvLock(uvlock).rotationX(rotX).rotationY(rotY).modelFile(model1).nextModel()
							.weight(1).uvLock(uvlock).rotationX(rotX).rotationY(rotY).modelFile(model2).nextModel()
							.weight(1).uvLock(uvlock).rotationX(rotX).rotationY(rotY).modelFile(model3)
							.build();
		});
	}

	private void woodStairs(StairsBlock block, String texName) {
		ResourceLocation tex0 = prefix("block/wood/" + texName + "_0");
		ResourceLocation tex1 = prefix("block/wood/" + texName + "_1");
		ResourceLocation tex2 = prefix("block/wood/" + texName + "_2");
		ResourceLocation tex3 = prefix("block/wood/" + texName + "_3");
		ModelFile main0 = models().stairs(block.getRegistryName().getPath(), tex0, tex0, tex0);
		ModelFile main1 = models().stairs(block.getRegistryName().getPath() + "_1", tex1, tex1, tex1);
		ModelFile main2 = models().stairs(block.getRegistryName().getPath() + "_2", tex2, tex2, tex2);
		ModelFile main3 = models().stairs(block.getRegistryName().getPath() + "_3", tex3, tex3, tex3);
		ModelFile inner0 = models().stairsInner(block.getRegistryName().getPath() + "_inner", tex0, tex0, tex0);
		ModelFile inner1 = models().stairsInner(block.getRegistryName().getPath() + "_inner_1", tex1, tex1, tex1);
		ModelFile inner2 = models().stairsInner(block.getRegistryName().getPath() + "_inner_2", tex2, tex2, tex2);
		ModelFile inner3 = models().stairsInner(block.getRegistryName().getPath() + "_inner_3", tex3, tex3, tex3);
		ModelFile outer0 = models().stairsOuter(block.getRegistryName().getPath() + "_outer", tex0, tex0, tex0);
		ModelFile outer1 = models().stairsOuter(block.getRegistryName().getPath() + "_outer_1", tex1, tex1, tex1);
		ModelFile outer2 = models().stairsOuter(block.getRegistryName().getPath() + "_outer_2", tex2, tex2, tex2);
		ModelFile outer3 = models().stairsOuter(block.getRegistryName().getPath() + "_outer_3", tex3, tex3, tex3);
		// [VanillaCopy] super.stairsBlock, but multiple files returned each time
		getVariantBuilder(block)
						.forAllStatesExcept(state -> {
							Direction facing = state.get(StairsBlock.FACING);
							Half half = state.get(StairsBlock.HALF);
							StairsShape shape = state.get(StairsBlock.SHAPE);
							int yRot = (int) facing.rotateY().getHorizontalAngle(); // Stairs model is rotated 90 degrees clockwise for some reason
							if (shape == StairsShape.INNER_LEFT || shape == StairsShape.OUTER_LEFT) {
								yRot += 270; // Left facing stairs are rotated 90 degrees clockwise
							}
							if (shape != StairsShape.STRAIGHT && half == Half.TOP) {
								yRot += 90; // Top stairs are rotated 90 degrees clockwise
							}
							yRot %= 360;
							boolean uvlock = yRot != 0 || half == Half.TOP; // Don't set uvlock for states that have no rotation
							return ConfiguredModel.builder()
											.weight(10)
											.modelFile(shape == StairsShape.STRAIGHT ? main0 : shape == StairsShape.INNER_LEFT || shape == StairsShape.INNER_RIGHT ? inner0 : outer0)
											.rotationX(half == Half.BOTTOM ? 0 : 180).rotationY(yRot).uvLock(uvlock)
											.nextModel()

											.weight(10)
											.modelFile(shape == StairsShape.STRAIGHT ? main1 : shape == StairsShape.INNER_LEFT || shape == StairsShape.INNER_RIGHT ? inner1 : outer1)
											.rotationX(half == Half.BOTTOM ? 0 : 180).rotationY(yRot).uvLock(uvlock)
											.nextModel()

											.weight(1)
											.modelFile(shape == StairsShape.STRAIGHT ? main2 : shape == StairsShape.INNER_LEFT || shape == StairsShape.INNER_RIGHT ? inner2 : outer2)
											.rotationX(half == Half.BOTTOM ? 0 : 180).rotationY(yRot).uvLock(uvlock)
											.nextModel()

											.weight(1)
											.modelFile(shape == StairsShape.STRAIGHT ? main3 : shape == StairsShape.INNER_LEFT || shape == StairsShape.INNER_RIGHT ? inner3 : outer3)
											.rotationX(half == Half.BOTTOM ? 0 : 180).rotationY(yRot).uvLock(uvlock)
											.build();
						}, StairsBlock.WATERLOGGED);
	}

	/*private void terrorcotta() {
		ModelFile terrorcottaNorth = models().withExistingParent(TFBlocks.terrorcotta_circle.getId().getPath(), prefix("block/util/terracotta"))
						.texture("down", prefix("block/terrorcotta_b"))
						.texture("up", prefix("block/terrorcotta_a"))
						.texture("top_left", prefix("block/terrorcotta_b"))
						.texture("top_right", prefix("block/terrorcotta_a"))
						.texture("bot_left", prefix("block/terrorcotta_d"))
						.texture("bot_right", prefix("block/terrorcotta_c"));
		ModelFile terrorcottaSouth = models().withExistingParent(TFBlocks.terrorcotta_circle.getId().getPath() + "_south", prefix("block/util/terracotta"))
						.texture("down", prefix("block/terrorcotta_c"))
						.texture("up", prefix("block/terrorcotta_d"))
						.texture("top_left", prefix("block/terrorcotta_b"))
						.texture("top_right", prefix("block/terrorcotta_a"))
						.texture("bot_left", prefix("block/terrorcotta_d"))
						.texture("bot_right", prefix("block/terrorcotta_c"));
		ModelFile terrorcottaWest = models().withExistingParent(TFBlocks.terrorcotta_circle.getId().getPath() + "_west", prefix("block/util/terracotta"))
						.texture("down", prefix("block/terrorcotta_d"))
						.texture("up", prefix("block/terrorcotta_c"))
						.texture("top_left", prefix("block/terrorcotta_b"))
						.texture("top_right", prefix("block/terrorcotta_a"))
						.texture("bot_left", prefix("block/terrorcotta_d"))
						.texture("bot_right", prefix("block/terrorcotta_c"));
		ModelFile terrorcottaEast = models().withExistingParent(TFBlocks.terrorcotta_circle.getId().getPath() + "_east", prefix("block/util/terracotta"))
						.texture("down", prefix("block/terrorcotta_a"))
						.texture("up", prefix("block/terrorcotta_b"))
						.texture("top_left", prefix("block/terrorcotta_b"))
						.texture("top_right", prefix("block/terrorcotta_a"))
						.texture("bot_left", prefix("block/terrorcotta_d"))
						.texture("bot_right", prefix("block/terrorcotta_c"));

		getVariantBuilder(TFBlocks.terrorcotta_circle.get()).partialState()
						.with(HorizontalBlock.HORIZONTAL_FACING, Direction.NORTH).setModels(new ConfiguredModel(terrorcottaNorth));
		getVariantBuilder(TFBlocks.terrorcotta_circle.get()).partialState()
						.with(HorizontalBlock.HORIZONTAL_FACING, Direction.SOUTH).setModels(ConfiguredModel.builder().modelFile(terrorcottaSouth).uvLock(true).rotationY(180).build());
		getVariantBuilder(TFBlocks.terrorcotta_circle.get()).partialState()
						.with(HorizontalBlock.HORIZONTAL_FACING, Direction.WEST).setModels(ConfiguredModel.builder().modelFile(terrorcottaWest).uvLock(true).rotationY(270).build());
		getVariantBuilder(TFBlocks.terrorcotta_circle.get()).partialState()
						.with(HorizontalBlock.HORIZONTAL_FACING, Direction.EAST).setModels(ConfiguredModel.builder().modelFile(terrorcottaEast).uvLock(true).rotationY(90).build());

		ModelFile terrorcottaDiagonal = models().withExistingParent(TFBlocks.terrorcotta_diagonal.getId().getPath(), prefix("block/util/terracotta"))
						.texture("down", prefix("block/terrorcotta_0"))
						.texture("up", prefix("block/terrorcotta_0"))
						.texture("top_left", prefix("block/terrorcotta_0"))
						.texture("top_right", prefix("block/terrorcotta_1"))
						.texture("bot_left", prefix("block/terrorcotta_0"))
						.texture("bot_right", prefix("block/terrorcotta_1"));
		ModelFile terrorcottaDiagonalRotated = models().withExistingParent(TFBlocks.terrorcotta_diagonal.getId().getPath() + "_rotated", prefix("block/util/terracotta"))
						.texture("down", prefix("block/terrorcotta_1"))
						.texture("up", prefix("block/terrorcotta_1"))
						.texture("top_left", prefix("block/terrorcotta_0"))
						.texture("top_right", prefix("block/terrorcotta_1"))
						.texture("bot_left", prefix("block/terrorcotta_0"))
						.texture("bot_right", prefix("block/terrorcotta_1"));
		getVariantBuilder(TFBlocks.terrorcotta_diagonal.get()).partialState()
						.with(BlockTFDiagonal.IS_ROTATED, false).setModels(ConfiguredModel.builder().modelFile(terrorcottaDiagonal).build());
		getVariantBuilder(TFBlocks.terrorcotta_diagonal.get()).partialState()
						.with(BlockTFDiagonal.IS_ROTATED, true).setModels(ConfiguredModel.builder().modelFile(terrorcottaDiagonalRotated).uvLock(true).rotationY(90).build());
	}*/

	private void towerBlocks() {
		ResourceLocation cube3 = prefix("block/util/cube_all_3_layer");
		ResourceLocation cubeBt3 = prefix("block/util/cube_bottom_top_3_layer");
		ResourceLocation cube2NoShade = prefix("block/util/cube_all_2_layer_no_shade");
		ResourceLocation fourCube = prefix("block/util/4_cubed");

		ModelFile reappear = models().withExistingParent(TFBlocks.reappearing_block.getId().getPath(), cube3)
						.texture("all", prefix("block/towerdev_reappearing_off"))
						.texture("all2", prefix("block/tower_device_level_1/towerdev_reappearing_off_1"))
						.texture("all3", prefix("block/tower_device_level_2/towerdev_reappearing_off_2"));
		ModelFile reappearActive = models().withExistingParent(TFBlocks.reappearing_block.getId().getPath() + "_active", cube3)
						.texture("all", prefix("block/towerdev_reappearing_on"))
						.texture("all2", prefix("block/tower_device_level_1/towerdev_reappearing_on_1"))
						.texture("all3", prefix("block/tower_device_level_2/towerdev_reappearing_on_2"));
		ModelFile reappearVanished = models().withExistingParent(TFBlocks.reappearing_block.getId().getPath() + "_vanished", fourCube)
						.texture("all", prefix("block/towerdev_reappearing_trace_off"));
		ModelFile reappearVanishedActive = models().withExistingParent(TFBlocks.reappearing_block.getId().getPath() + "_vanished_active", fourCube)
						.texture("all", prefix("block/towerdev_reappearing_trace_on"));
		getVariantBuilder(TFBlocks.reappearing_block.get()).forAllStates(s -> {
			ModelFile model;
			if (s.get(BlockTFVanishingBlock.VANISHED)) {
				model = s.get(BlockTFVanishingBlock.ACTIVE) ? reappearVanishedActive : reappearVanished;
			} else {
				model = s.get(BlockTFVanishingBlock.ACTIVE) ? reappearActive : reappear;
			}
			return ConfiguredModel.builder().modelFile(model).build();
		});

		ModelFile vanish = models().withExistingParent(TFBlocks.vanishing_block.getId().getPath(), cube3)
						.texture("all", prefix("block/towerdev_vanish_off"))
						.texture("all2", prefix("block/tower_device_level_1/towerdev_vanish_off_1"))
						.texture("all3", prefix("block/tower_device_level_2/towerdev_vanish_off_2"));
		ModelFile vanishActive = models().withExistingParent(TFBlocks.vanishing_block.getId().getPath() + "_active", cube3)
						.texture("all", prefix("block/towerdev_vanish_on"))
						.texture("all2", prefix("block/tower_device_level_1/towerdev_vanish_on_1"))
						.texture("all3", prefix("block/tower_device_level_2/towerdev_vanish_on_2"));
		getVariantBuilder(TFBlocks.vanishing_block.get()).partialState()
						.with(BlockTFVanishingBlock.ACTIVE, false).setModels(new ConfiguredModel(vanish));
		getVariantBuilder(TFBlocks.vanishing_block.get()).partialState()
						.with(BlockTFVanishingBlock.ACTIVE, true).setModels(new ConfiguredModel(vanishActive));

		ModelFile vanishLocked = models().withExistingParent(TFBlocks.locked_vanishing_block.getId().getPath(), cube3)
						.texture("all", prefix("block/towerdev_lock_on"))
						.texture("all2", prefix("block/tower_device_level_1/towerdev_lock_on_1"))
						.texture("all3", prefix("block/tower_device_level_2/towerdev_lock_on_2"));
		ModelFile vanishUnlocked = models().withExistingParent(TFBlocks.locked_vanishing_block.getId().getPath() + "_unlocked", cube3)
						.texture("all", prefix("block/towerdev_lock_off"))
						.texture("all2", prefix("block/tower_device_level_1/towerdev_lock_off_1"))
						.texture("all3", prefix("block/tower_device_level_2/towerdev_lock_off_2"));
		getVariantBuilder(TFBlocks.locked_vanishing_block.get()).partialState()
						.with(BlockTFLockedVanishing.LOCKED, true).setModels(new ConfiguredModel(vanishLocked));
		getVariantBuilder(TFBlocks.locked_vanishing_block.get()).partialState()
						.with(BlockTFLockedVanishing.LOCKED, false).setModels(new ConfiguredModel(vanishUnlocked));

		ModelFile ghastTrap = models().withExistingParent(TFBlocks.ghast_trap.getId().getPath(), cubeBt3)
						.texture("top", prefix("block/towerdev_ghasttraplid_off"))
						.texture("side", prefix("block/towerdev_ghasttrap_off"))
						.texture("bottom", prefix("block/tower_wood_encased"))
						.texture("top2", prefix("block/tower_device_level_2/towerdev_ghasttraplid_off_1"))
						.texture("side2", prefix("block/tower_device_level_1/towerdev_ghasttrap_off_1"))
						.texture("top3", prefix("block/tower_device_level_2/towerdev_ghasttraplid_off_1"))
						.texture("side3", prefix("block/tower_device_level_2/towerdev_ghasttrap_off_2"));
		ModelFile ghastTrapActive = models().withExistingParent(TFBlocks.ghast_trap.getId().getPath() + "_active", cubeBt3)
						.texture("top", prefix("block/towerdev_ghasttraplid_on"))
						.texture("side", prefix("block/towerdev_ghasttrap_on"))
						.texture("bottom", prefix("block/tower_wood_encased"))
						.texture("top2", prefix("block/tower_device_level_2/towerdev_ghasttraplid_on_1"))
						.texture("side2", prefix("block/tower_device_level_1/towerdev_ghasttrap_on_1"))
						.texture("top3", prefix("block/tower_device_level_3/towerdev_ghasttraplid_on_2"))
						.texture("side3", prefix("block/tower_device_level_2/towerdev_ghasttrap_on_2"));
		getVariantBuilder(TFBlocks.ghast_trap.get()).partialState()
						.with(BlockTFGhastTrap.ACTIVE, false).setModels(new ConfiguredModel(ghastTrap));
		getVariantBuilder(TFBlocks.ghast_trap.get()).partialState()
						.with(BlockTFGhastTrap.ACTIVE, true).setModels(new ConfiguredModel(ghastTrapActive));

		ModelFile builder = models().withExistingParent(TFBlocks.carminite_builder.getId().getPath(), cube3)
						.texture("all", prefix("block/towerdev_builder_off"))
						.texture("all2", prefix("block/tower_device_level_1/towerdev_builder_off_1"))
						.texture("all3", prefix("block/tower_device_level_2/towerdev_builder_off_2"));
		ModelFile builderActive = models().withExistingParent(TFBlocks.carminite_builder.getId().getPath() + "_active", cube3)
						.texture("all", prefix("block/towerdev_builder_on"))
						.texture("all2", prefix("block/tower_device_level_1/towerdev_builder_on_1"))
						.texture("all3", prefix("block/tower_device_level_2/towerdev_builder_on_2"));
		ModelFile builderTimeout = models().withExistingParent(TFBlocks.carminite_builder.getId().getPath() + "_timeout", cube3)
						.texture("all", prefix("block/towerdev_builder_timeout"))
						.texture("all2", prefix("block/tower_device_level_2/towerdev_builder_timeout_1"))
						.texture("all3", prefix("block/tower_device_level_3/towerdev_builder_timeout_2"));
		getVariantBuilder(TFBlocks.carminite_builder.get()).partialState()
						.with(BlockTFBuilder.STATE, TowerDeviceVariant.BUILDER_INACTIVE).setModels(new ConfiguredModel(builder));
		getVariantBuilder(TFBlocks.carminite_builder.get()).partialState()
						.with(BlockTFBuilder.STATE, TowerDeviceVariant.BUILDER_ACTIVE).setModels(new ConfiguredModel(builderActive));
		getVariantBuilder(TFBlocks.carminite_builder.get()).partialState()
						.with(BlockTFBuilder.STATE, TowerDeviceVariant.BUILDER_TIMEOUT).setModels(new ConfiguredModel(builderTimeout));

		ModelFile built = models().withExistingParent(TFBlocks.built_block.getId().getPath(), cube2NoShade)
						.texture("all", prefix("block/towerdev_built_off"))
						.texture("all2", prefix("block/tower_device_level_1/towerdev_builder_off_1"));
		ModelFile builtActive = models().withExistingParent(TFBlocks.built_block.getId().getPath() + "_active", cube2NoShade)
						.texture("all", prefix("block/towerdev_built_on"))
						.texture("all2", prefix("block/tower_device_level_1/towerdev_builder_on_1"));
		getVariantBuilder(TFBlocks.built_block.get()).partialState()
						.with(BlockTFBuiltTranslucent.ACTIVE, false).setModels(new ConfiguredModel(built));
		getVariantBuilder(TFBlocks.built_block.get()).partialState()
						.with(BlockTFBuiltTranslucent.ACTIVE, true).setModels(new ConfiguredModel(builtActive));

		ModelFile antibuilder = models().withExistingParent(TFBlocks.antibuilder.getId().getPath(), cube3)
						.texture("all", prefix("block/towerdev_antibuilder"))
						.texture("all2", prefix("block/tower_device_level_1/towerdev_antibuilder_1"))
						.texture("all3", prefix("block/tower_device_level_2/towerdev_antibuilder_2"));
		simpleBlock(TFBlocks.antibuilder.get(), antibuilder);
		ModelFile antibuilt = models().withExistingParent(TFBlocks.antibuilt_block.getId().getPath(), cube2NoShade)
						.texture("all", prefix("block/towerdev_antibuilt"))
						.texture("all2", prefix("block/tower_device_level_2/towerdev_antibuilt_1"));
		simpleBlock(TFBlocks.antibuilt_block.get(), antibuilt);

		ModelFile reactor = models().withExistingParent(TFBlocks.carminite_reactor.getId().getPath(), cube3)
						.texture("all", prefix("block/towerdev_reactor_off"))
						.texture("all2", prefix("block/tower_device_level_1/towerdev_reactor_off_1"))
						.texture("all3", prefix("block/tower_device_level_2/towerdev_reactor_off_2"));
		ModelFile reactorActive = models().withExistingParent(TFBlocks.carminite_reactor.getId().getPath() + "_active", cube3)
						.texture("all", prefix("block/towerdev_reactor_on"))
						.texture("all2", prefix("block/tower_device_level_1/towerdev_reactor_on_1"))
						.texture("all3", prefix("block/tower_device_level_2/towerdev_reactor_on_2"));
		getVariantBuilder(TFBlocks.carminite_reactor.get()).partialState()
						.with(BlockTFReactor.ACTIVE, false).setModels(new ConfiguredModel(reactor));
		getVariantBuilder(TFBlocks.carminite_reactor.get()).partialState()
						.with(BlockTFReactor.ACTIVE, true).setModels(new ConfiguredModel(reactorActive));
		simpleBlock(TFBlocks.reactor_debris.get(), models().cubeAll(TFBlocks.reactor_debris.getId().getPath(), new ResourceLocation("block/destroy_stage_9")));
	}
	
	private ModelFile pedestalModel(String name, String north, String south, String west, String east, boolean active) {
		String suffix = active ? "" : "_latent";
		BlockModelBuilder ret = models().withExistingParent(name, prefix(active ? "block/util/pedestal_2_layer" : "block/util/pedestal"))
						.texture("end", prefix("block/pedestal/top"))
						.texture("north", prefix("block/pedestal/" + north + suffix))
						.texture("south", prefix("block/pedestal/" + south + suffix))
						.texture("west", prefix("block/pedestal/" + west + suffix))
						.texture("east", prefix("block/pedestal/" + east + suffix));
		if (active) {
			ret = ret
							.texture("end2", prefix("block/pedestal/top_glow"))
							.texture("north2", prefix("block/pedestal/" + north + "_glow"))
							.texture("south2", prefix("block/pedestal/" + south + "_glow"))
							.texture("west2", prefix("block/pedestal/" + west + "_glow"))
							.texture("east2", prefix("block/pedestal/" + east + "_glow"));
		}
		return ret;
	}

	private void trophyPedestal() {
		String baseName = TFBlocks.trophy_pedestal.getId().getPath();
		ModelFile latent0 = pedestalModel(baseName, "naga", "lich", "hydra", "ur-ghast", false);
		ModelFile latent1 = pedestalModel(baseName + "_1", "snow_queen", "naga", "lich", "hydra", false);
		ModelFile latent2 = pedestalModel(baseName + "_2", "ur-ghast", "snow_queen", "naga", "lich", false);
		ModelFile latent3 = pedestalModel(baseName + "_3", "hydra", "ur-ghast", "snow_queen", "naga", false);
		ModelFile latent4 = pedestalModel(baseName + "_4", "lich", "hydra", "ur-ghast", "snow_queen", false);
		
		List<ConfiguredModel> latentModels = new ArrayList<>();
		for (ModelFile f : Arrays.asList(latent0, latent1, latent2, latent3, latent4)) {
			latentModels.add(new ConfiguredModel(f, 0, 0, false));
			latentModels.add(new ConfiguredModel(f, 0, 90, false));
			latentModels.add(new ConfiguredModel(f, 0, 180, false));
			latentModels.add(new ConfiguredModel(f, 0, 270, false));
		}
		getVariantBuilder(TFBlocks.trophy_pedestal.get()).partialState()
						.with(BlockTFTrophyPedestal.ACTIVE, false).setModels(latentModels.toArray(new ConfiguredModel[0]));


		ModelFile active0 = pedestalModel(baseName + "_active", "naga", "lich", "hydra", "ur-ghast", true);
		ModelFile active1 = pedestalModel(baseName + "_active_1", "snow_queen", "naga", "lich", "hydra", true);
		ModelFile active2 = pedestalModel(baseName + "_active_2", "ur-ghast", "snow_queen", "naga", "lich", true);
		ModelFile active3 = pedestalModel(baseName + "_active_3", "hydra", "ur-ghast", "snow_queen", "naga", true);
		ModelFile active4 = pedestalModel(baseName + "_active_4", "lich", "hydra", "ur-ghast", "snow_queen", true);

		List<ConfiguredModel> activeModels = new ArrayList<>();
		for (ModelFile f : Arrays.asList(active0, active1, active2, active3, active4)) {
			activeModels.add(new ConfiguredModel(f, 0, 0, false));
			activeModels.add(new ConfiguredModel(f, 0, 90, false));
			activeModels.add(new ConfiguredModel(f, 0, 180, false));
			activeModels.add(new ConfiguredModel(f, 0, 270, false));
		}
		getVariantBuilder(TFBlocks.trophy_pedestal.get()).partialState()
						.with(BlockTFTrophyPedestal.ACTIVE, true).setModels(activeModels.toArray(new ConfiguredModel[0]));
	}
	
	private void thorns() {
		ModelFile green = models().withExistingParent(TFBlocks.green_thorns.getId().getPath(), prefix("block/thorns_main"))
						.texture("side", prefix("block/green_thorns_side"))
						.texture("end", prefix("block/green_thorns_top"));
		ModelFile greenBottom = models().withExistingParent(TFBlocks.green_thorns.getId().getPath() + "_bottom", prefix("block/thorns_section_bottom"))
						.texture("side", prefix("block/green_thorns_side"))
						.texture("end", prefix("block/green_thorns_top"));
		ModelFile greenTop = models().withExistingParent(TFBlocks.green_thorns.getId().getPath() + "_top", prefix("block/thorns_section_top"))
						.texture("side", prefix("block/green_thorns_side"))
						.texture("end", prefix("block/green_thorns_top"));
		getMultipartBuilder(TFBlocks.green_thorns.get())
						.part().modelFile(green).addModel().condition(RotatedPillarBlock.AXIS, Direction.Axis.Y).end()
						.part().modelFile(green).rotationX(90).addModel().condition(RotatedPillarBlock.AXIS, Direction.Axis.Z).end()
						.part().modelFile(green).rotationX(90).rotationY(90).addModel().condition(RotatedPillarBlock.AXIS, Direction.Axis.X).end()
						.part().modelFile(greenTop).rotationX(90).addModel().condition(SixWayBlock.UP, true).end()
						.part().modelFile(greenBottom).rotationX(90).addModel().condition(SixWayBlock.DOWN, true).end()
						.part().modelFile(greenTop).rotationY(270).addModel().condition(SixWayBlock.EAST, true).end()
						.part().modelFile(greenBottom).rotationY(270).addModel().condition(SixWayBlock.WEST, true).end()
						.part().modelFile(greenTop).addModel().condition(SixWayBlock.SOUTH, true).end()
						.part().modelFile(greenBottom).addModel().condition(SixWayBlock.NORTH, true).end();

		ModelFile brown = models().withExistingParent(TFBlocks.brown_thorns.getId().getPath(), prefix("block/thorns_main"))
						.texture("side", prefix("block/brown_thorns_side"))
						.texture("end", prefix("block/brown_thorns_top"));
		ModelFile brownBottom = models().withExistingParent(TFBlocks.brown_thorns.getId().getPath() + "_bottom", prefix("block/thorns_section_bottom"))
						.texture("side", prefix("block/brown_thorns_side"))
						.texture("end", prefix("block/brown_thorns_top"));
		ModelFile brownTop = models().withExistingParent(TFBlocks.brown_thorns.getId().getPath() + "_top", prefix("block/thorns_section_top"))
						.texture("side", prefix("block/brown_thorns_side"))
						.texture("end", prefix("block/brown_thorns_top"));
		getMultipartBuilder(TFBlocks.brown_thorns.get())
						.part().modelFile(brown).addModel().condition(RotatedPillarBlock.AXIS, Direction.Axis.Y).end()
						.part().modelFile(brown).rotationX(90).addModel().condition(RotatedPillarBlock.AXIS, Direction.Axis.Z).end()
						.part().modelFile(brown).rotationX(90).rotationY(90).addModel().condition(RotatedPillarBlock.AXIS, Direction.Axis.X).end()
						.part().modelFile(brownTop).rotationX(90).addModel().condition(SixWayBlock.UP, true).end()
						.part().modelFile(brownBottom).rotationX(90).addModel().condition(SixWayBlock.DOWN, true).end()
						.part().modelFile(brownTop).rotationY(270).addModel().condition(SixWayBlock.EAST, true).end()
						.part().modelFile(brownBottom).rotationY(270).addModel().condition(SixWayBlock.WEST, true).end()
						.part().modelFile(brownTop).addModel().condition(SixWayBlock.SOUTH, true).end()
						.part().modelFile(brownBottom).addModel().condition(SixWayBlock.NORTH, true).end();

		ModelFile burnt = models().withExistingParent(TFBlocks.burnt_thorns.getId().getPath(), prefix("block/thorns_main"))
						.texture("side", prefix("block/burnt_thorns_side"))
						.texture("end", prefix("block/burnt_thorns_top"));
		ModelFile burntBottom = models().withExistingParent(TFBlocks.burnt_thorns.getId().getPath() + "_bottom", prefix("block/thorns_section_bottom"))
						.texture("side", prefix("block/burnt_thorns_side"))
						.texture("end", prefix("block/burnt_thorns_top"));
		ModelFile burntTop = models().withExistingParent(TFBlocks.burnt_thorns.getId().getPath() + "_top", prefix("block/thorns_section_top"))
						.texture("side", prefix("block/burnt_thorns_side"))
						.texture("end", prefix("block/burnt_thorns_top"));
		getMultipartBuilder(TFBlocks.burnt_thorns.get())
						.part().modelFile(burnt).addModel().condition(RotatedPillarBlock.AXIS, Direction.Axis.Y).end()
						.part().modelFile(burnt).rotationX(90).addModel().condition(RotatedPillarBlock.AXIS, Direction.Axis.Z).end()
						.part().modelFile(burnt).rotationX(90).rotationY(90).addModel().condition(RotatedPillarBlock.AXIS, Direction.Axis.X).end()
						.part().modelFile(burntTop).rotationX(90).addModel().condition(SixWayBlock.UP, true).end()
						.part().modelFile(burntBottom).rotationX(90).addModel().condition(SixWayBlock.DOWN, true).end()
						.part().modelFile(burntTop).rotationY(270).addModel().condition(SixWayBlock.EAST, true).end()
						.part().modelFile(burntBottom).rotationY(270).addModel().condition(SixWayBlock.WEST, true).end()
						.part().modelFile(burntTop).addModel().condition(SixWayBlock.SOUTH, true).end()
						.part().modelFile(burntBottom).addModel().condition(SixWayBlock.NORTH, true).end();
	}

	private void auroraBlocks() {
		int variants = 16;
		ModelFile[] models = new ModelFile[variants];
		for (int i = 0; i < variants; i++) {
			models[i] = models().withExistingParent(TFBlocks.aurora_block.getId().getPath() + "_" + i, prefix("block/util/tinted_cube_all"))
							.texture("all", prefix("block/" + TFBlocks.aurora_block.getId().getPath() + "_" + i));
		}
		for (int i = 0; i < variants; i++) {
			getVariantBuilder(TFBlocks.aurora_block.get()).partialState().with(BlockTFAuroraBrick.VARIANT, i)
							.setModels(ConfiguredModel.builder()
											.weight(3).modelFile(models[i]).nextModel()
											.weight(1).modelFile(models[(i + 1) % variants]).build());
		}

		ModelFile pillarModel = models().withExistingParent(TFBlocks.aurora_pillar.getId().getPath(), prefix("block/util/tinted_cube_column"))
						.texture("end", prefix("block/" + TFBlocks.aurora_pillar.getId().getPath() + "_top"))
						.texture("side", blockTexture(TFBlocks.aurora_pillar.get()));
		axisBlock(TFBlocks.aurora_pillar.get(), pillarModel, pillarModel);

		ModelFile slabModel = models().withExistingParent(TFBlocks.aurora_slab.getId().getPath(), prefix("block/util/tinted_slab"))
						.texture("bottom", prefix("block/" + TFBlocks.aurora_pillar.getId().getPath() + "_top"))
						.texture("top", prefix("block/" + TFBlocks.aurora_pillar.getId().getPath() + "_top"))
						.texture("side", prefix("block/" + TFBlocks.aurora_slab.getId().getPath() + "_side"));
		ModelFile doubleSlabModel = models().withExistingParent(TFBlocks.aurora_slab.getId().getPath() + "_double", prefix("block/util/tinted_cube_column"))
						.texture("end", prefix("block/" + TFBlocks.aurora_pillar.getId().getPath() + "_top"))
						.texture("side", prefix("block/" + TFBlocks.aurora_slab.getId().getPath() + "_side"));

		getVariantBuilder(TFBlocks.aurora_slab.get()).partialState()
						.with(SlabBlock.TYPE, SlabType.BOTTOM).setModels(new ConfiguredModel(slabModel));
		getVariantBuilder(TFBlocks.aurora_slab.get()).partialState()
						.with(SlabBlock.TYPE, SlabType.TOP).setModels(ConfiguredModel.builder().uvLock(true).rotationX(180).modelFile(slabModel).build());
		getVariantBuilder(TFBlocks.aurora_slab.get()).partialState()
						.with(SlabBlock.TYPE, SlabType.DOUBLE).setModels(new ConfiguredModel(doubleSlabModel));

		ModelFile auroraGlass = models().withExistingParent(TFBlocks.auroralized_glass.getId().getPath(), prefix("block/util/tinted_cube_all"))
						.texture("all", blockTexture(TFBlocks.auroralized_glass.get()));
		simpleBlock(TFBlocks.auroralized_glass.get(), auroraGlass);
	}

	private void mazestone() {
		ResourceLocation plainTex = blockTexture(TFBlocks.maze_stone.get());

		ModelFile mazeStone = models().cubeAll(TFBlocks.maze_stone.getId().getPath(), plainTex);
		simpleBlock(TFBlocks.maze_stone.get(), ConfiguredModel.builder()
						.rotationX(90).rotationY(90).modelFile(mazeStone).nextModel()
						.rotationX(270).rotationY(270).modelFile(mazeStone).build());
		simpleBlock(TFBlocks.maze_stone_brick.get());

		ModelFile chiseled = models().cubeColumn(TFBlocks.maze_stone_chiseled.getId().getPath(), blockTexture(TFBlocks.maze_stone_chiseled.get()), plainTex);
		simpleBlock(TFBlocks.maze_stone_chiseled.get(), chiseled);

		ModelFile decorative = models().cubeColumn(TFBlocks.maze_stone_decorative.getId().getPath(), blockTexture(TFBlocks.maze_stone_decorative.get()), plainTex);
		simpleBlock(TFBlocks.maze_stone_decorative.get(), decorative);

		simpleBlock(TFBlocks.maze_stone_cracked.get());
		simpleBlock(TFBlocks.maze_stone_mossy.get());

		ResourceLocation brickTex = blockTexture(TFBlocks.maze_stone_brick.get());
		ModelFile mosaic = models().cubeColumn(TFBlocks.maze_stone_mosaic.getId().getPath(), brickTex, blockTexture(TFBlocks.maze_stone_mosaic.get()));
		simpleBlock(TFBlocks.maze_stone_mosaic.get(), mosaic);

		ModelFile border = models().cubeColumn(TFBlocks.maze_stone_border.getId().getPath(), brickTex, blockTexture(TFBlocks.maze_stone_border.get()));
		simpleBlock(TFBlocks.maze_stone_border.get(), border);
	}

	private void lilyPad(Block b) {
		String baseName = b.getRegistryName().getPath();
		ResourceLocation parent = new ResourceLocation("block/lily_pad");
		ModelFile[] models = new ModelFile[4];
		for (int i = 0; i < models.length; i++) {
			models[i] = models().withExistingParent(baseName + "_" + i, parent)
							.texture("texture", prefix("block/huge_lilypad_" + i))
							.texture("particle", "#texture");
		}

		Map<HugeLilypadPiece, ModelFile> north = ImmutableMap.of(HugeLilypadPiece.NW, models[1],
						HugeLilypadPiece.NE, models[0], HugeLilypadPiece.SE, models[3], HugeLilypadPiece.SW, models[2]);
		Map<HugeLilypadPiece, ModelFile> south = ImmutableMap.of(HugeLilypadPiece.NW, models[3],
						HugeLilypadPiece.NE, models[2], HugeLilypadPiece.SE, models[1], HugeLilypadPiece.SW, models[0]);
		Map<HugeLilypadPiece, ModelFile> west = ImmutableMap.of(HugeLilypadPiece.NW, models[0],
						HugeLilypadPiece.NE, models[3], HugeLilypadPiece.SE, models[2], HugeLilypadPiece.SW, models[1]);
		Map<HugeLilypadPiece, ModelFile> east = ImmutableMap.of(HugeLilypadPiece.NW, models[2],
						HugeLilypadPiece.NE, models[1], HugeLilypadPiece.SE, models[0], HugeLilypadPiece.SW, models[3]);

		getVariantBuilder(b).forAllStates(state -> {
			int rotY;
			Map<HugeLilypadPiece, ModelFile> m;
			switch (state.get(BlockTFHugeLilyPad.FACING)) {
			default:
			case NORTH:
				rotY = 0;
				m = north;
				break;
			case SOUTH:
				rotY = 180;
				m = south;
				break;
			case WEST:
				rotY = 270;
				m = west;
				break;
			case EAST:
				rotY = 90;
				m = east;
				break;
			}

			ModelFile model = m.get(state.get(BlockTFHugeLilyPad.PIECE));
			return ConfiguredModel.builder().rotationY(rotY).modelFile(model).build();
		});
	}

	private void perFaceBlock(Block b, ResourceLocation inside, ResourceLocation outside) {
		ModelFile modelInside = models().withExistingParent(b.getRegistryName().getPath() + "_inside", prefix("block/util/north_face"))
						.texture("texture", inside);
		ModelFile modelOutside = models().withExistingParent(b.getRegistryName().getPath() + "_outside", prefix("block/util/north_face"))
						.texture("texture", outside);
		getMultipartBuilder(b).part().modelFile(modelInside).addModel().condition(HugeMushroomBlock.NORTH, false).end();
		getMultipartBuilder(b).part().modelFile(modelOutside).addModel().condition(HugeMushroomBlock.NORTH, true).end();
		getMultipartBuilder(b).part().modelFile(modelInside).uvLock(true).rotationY(180).addModel().condition(HugeMushroomBlock.SOUTH, false).end();
		getMultipartBuilder(b).part().modelFile(modelOutside).uvLock(true).rotationY(180).addModel().condition(HugeMushroomBlock.SOUTH, true).end();
		getMultipartBuilder(b).part().modelFile(modelInside).uvLock(true).rotationY(270).addModel().condition(HugeMushroomBlock.WEST, false).end();
		getMultipartBuilder(b).part().modelFile(modelOutside).uvLock(true).rotationY(270).addModel().condition(HugeMushroomBlock.WEST, true).end();
		getMultipartBuilder(b).part().modelFile(modelInside).uvLock(true).rotationY(90).addModel().condition(HugeMushroomBlock.EAST, false).end();
		getMultipartBuilder(b).part().modelFile(modelOutside).uvLock(true).rotationY(90).addModel().condition(HugeMushroomBlock.EAST, true).end();
		getMultipartBuilder(b).part().modelFile(modelInside).uvLock(true).rotationX(270).addModel().condition(HugeMushroomBlock.UP, false).end();
		getMultipartBuilder(b).part().modelFile(modelOutside).uvLock(true).rotationX(270).addModel().condition(HugeMushroomBlock.UP, true).end();
		getMultipartBuilder(b).part().modelFile(modelInside).uvLock(true).rotationX(90).addModel().condition(HugeMushroomBlock.DOWN, false).end();
		getMultipartBuilder(b).part().modelFile(modelOutside).uvLock(true).rotationX(90).addModel().condition(HugeMushroomBlock.DOWN, true).end();
	}

	@Nonnull
	@Override
	public String getName() {
		return "TwilightForest blockstates and block models";
	}
}
