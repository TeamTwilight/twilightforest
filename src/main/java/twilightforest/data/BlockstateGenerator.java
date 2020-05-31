package twilightforest.data;

import net.minecraft.block.*;
import net.minecraft.data.DataGenerator;
import net.minecraft.state.properties.AttachFace;
import net.minecraft.state.properties.Half;
import net.minecraft.state.properties.SlabType;
import net.minecraft.state.properties.StairsShape;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.*;
import twilightforest.TwilightForestMod;
import twilightforest.block.*;

import javax.annotation.Nonnull;

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
							.condition(BlockTFLadderBars.FACING, d).condition(BlockTFLadderBars.LEFT, false).end();
			ironLadder.part().modelFile(ironLadderLeftConnected).rotationY(rotY).addModel()
							.condition(BlockTFLadderBars.FACING, d).condition(BlockTFLadderBars.LEFT, true).end();
			ironLadder.part().modelFile(ironLadderRight).rotationY(rotY).addModel()
							.condition(BlockTFLadderBars.FACING, d).condition(BlockTFLadderBars.RIGHT, false).end();
			ironLadder.part().modelFile(ironLadderRightConnected).rotationY(rotY).addModel()
							.condition(BlockTFLadderBars.FACING, d).condition(BlockTFLadderBars.RIGHT, true).end();
		}

		simpleBlock(TFBlocks.fake_gold.get(), models().getExistingFile(new ResourceLocation("block/gold_block")));
		simpleBlock(TFBlocks.fake_diamond.get(), models().getExistingFile(new ResourceLocation("block/diamond_block")));
		simpleBlock(TFBlocks.underbrick.get());
		simpleBlock(TFBlocks.underbrick_cracked.get());
		simpleBlock(TFBlocks.underbrick_mossy.get());
		simpleBlock(TFBlocks.underbrick_floor.get());
		simpleBlock(TFBlocks.wispy_cloud.get());
		simpleBlock(TFBlocks.fluffy_cloud.get());
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

		getVariantBuilder(TFBlocks.castle_rune_brick_yellow.get()).partialState().setModels(runeBrickModels);
		getVariantBuilder(TFBlocks.castle_rune_brick_purple.get()).partialState().setModels(runeBrickModels);
		getVariantBuilder(TFBlocks.castle_rune_brick_pink.get()).partialState().setModels(runeBrickModels);
		getVariantBuilder(TFBlocks.castle_rune_brick_blue.get()).partialState().setModels(runeBrickModels);

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
		simpleBlock(TFBlocks.firefly_jar.get(), models().getExistingFile(prefix("block/firefly_jar")));
		registerWoodBlocks();
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

		logWoodSapling(TFBlocks.transformation_log.get(), TFBlocks.transformation_wood.get(), TFBlocks.transformation_sapling.get());
		plankBlocks("trans", TFBlocks.trans_planks.get(), TFBlocks.trans_slab.get(), TFBlocks.trans_stairs.get(), TFBlocks.trans_button.get(), TFBlocks.trans_fence.get(), TFBlocks.trans_gate.get(), TFBlocks.trans_plate.get(), TFBlocks.trans_door.get(), TFBlocks.trans_trapdoor.get());
		singleBlockBoilerPlate(TFBlocks.transformation_leaves.get(), "block/leaves", m -> m.texture("all", "block/transformation_leaves"));

		logWoodSapling(TFBlocks.mining_log.get(), TFBlocks.mining_wood.get(), TFBlocks.mining_sapling.get());
		plankBlocks("mine", TFBlocks.mine_planks.get(), TFBlocks.mine_slab.get(), TFBlocks.mine_stairs.get(), TFBlocks.mine_button.get(), TFBlocks.mine_fence.get(), TFBlocks.mine_gate.get(), TFBlocks.mine_plate.get(), TFBlocks.mine_door.get(), TFBlocks.mine_trapdoor.get());
		singleBlockBoilerPlate(TFBlocks.mining_leaves.get(), "block/leaves", m -> m.texture("all", "block/mining_leaves"));

		logWoodSapling(TFBlocks.sorting_log.get(), TFBlocks.sorting_wood.get(), TFBlocks.sorting_sapling.get());
		plankBlocks("sort", TFBlocks.sort_planks.get(), TFBlocks.sort_slab.get(), TFBlocks.sort_stairs.get(), TFBlocks.sort_button.get(), TFBlocks.sort_fence.get(), TFBlocks.sort_gate.get(), TFBlocks.sort_plate.get(), TFBlocks.sort_door.get(), TFBlocks.sort_trapdoor.get());
		singleBlockBoilerPlate(TFBlocks.sorting_leaves.get(), "block/leaves", m -> m.texture("all", "block/sorting_leaves"));
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
		getVariantBuilder(b).partialState().setModels(builder.build());
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

	private void logWoodSapling(LogBlock log, Block wood, Block sapling) {
		logBlock(log);
		ResourceLocation sideTex = blockTexture(log);
		getVariantBuilder(wood).partialState()
						.setModels(ConfiguredModel.builder().modelFile(models().cubeAll(wood.getRegistryName().getPath(), sideTex)).build());

		ResourceLocation saplingTex = prefix("block/" + sapling.getRegistryName().getPath());
		getVariantBuilder(sapling).partialState()
						.setModels(ConfiguredModel.builder().modelFile(models().cross(sapling.getRegistryName().getPath(), saplingTex)).build());
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
		getVariantBuilder(plank).partialState().setModels(plankModels);

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
		trapdoorBlock(trapdoor, prefix("block/wood/trapdoor/" + variant + "_trapdoor"), false);
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
							.rotationY((int) state.get(FenceGateBlock.HORIZONTAL_FACING).getHorizontalAngle())
							.uvLock(true).nextModel()

							.weight(10).modelFile(model1)
							.rotationY((int) state.get(FenceGateBlock.HORIZONTAL_FACING).getHorizontalAngle())
							.uvLock(true).nextModel()

							.weight(1).modelFile(model2)
							.rotationY((int) state.get(FenceGateBlock.HORIZONTAL_FACING).getHorizontalAngle())
							.uvLock(true).nextModel()

							.weight(1).modelFile(model3)
							.rotationY((int) state.get(FenceGateBlock.HORIZONTAL_FACING).getHorizontalAngle())
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
		SixWayBlock.FACING_TO_PROPERTY_MAP.entrySet().forEach(e -> {
			Direction dir = e.getKey();
			if (dir.getAxis().isHorizontal()) {
				builder.part()
								.weight(10).modelFile(side0).rotationY((((int) dir.getHorizontalAngle()) + 180) % 360).uvLock(true).nextModel()
								.weight(10).modelFile(side1).rotationY((((int) dir.getHorizontalAngle()) + 180) % 360).uvLock(true).nextModel()
								.weight(1).modelFile(side2).rotationY((((int) dir.getHorizontalAngle()) + 180) % 360).uvLock(true).nextModel()
								.weight(1).modelFile(side3).rotationY((((int) dir.getHorizontalAngle()) + 180) % 360).uvLock(true)
								.addModel()
								.condition(e.getValue(), true);
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
			switch (state.get(AbstractButtonBlock.FACE)) {
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
			if (state.get(AbstractButtonBlock.FACE) == AttachFace.CEILING)  {
				switch (state.get(AbstractButtonBlock.HORIZONTAL_FACING)) {
				case NORTH: rotY = 180; break;
				case SOUTH: rotY = 0; break;
				case WEST: rotY = 90; break;
				case EAST: rotY = 270; break;
				}
			} else {
				switch (state.get(AbstractButtonBlock.HORIZONTAL_FACING)) {
				case NORTH: rotY = 0; break;
				case SOUTH: rotY = 180; break;
				case WEST: rotY = 270; break;
				case EAST: rotY = 90; break;
				}
			}
			boolean uvlock = state.get(AbstractButtonBlock.FACE) == AttachFace.WALL;

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

	@Nonnull
	@Override
	public String getName() {
		return "TwilightForest blockstates and block models";
	}
}
