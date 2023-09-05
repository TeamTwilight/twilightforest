package twilightforest.data;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.MultiPartBlockStateBuilder;
import net.minecraftforge.client.model.generators.loaders.CompositeModelBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import twilightforest.TwilightForestMod;
import twilightforest.block.*;
import twilightforest.client.model.block.doors.CastleDoorBuilder;
import twilightforest.client.model.block.giantblock.GiantBlockBuilder;
import twilightforest.data.helpers.BlockModelBuilders;
import twilightforest.enums.*;
import twilightforest.init.TFBlocks;

import javax.annotation.Nonnull;
import java.util.*;

import static twilightforest.TwilightForestMod.prefix;

//TODO clean this class up for god's sake
//make better helper methods and move them to BlockModelBuilders
//finish datagenning blocks that arent done yet
public class BlockstateGenerator extends BlockModelBuilders {
	public BlockstateGenerator(PackOutput output, ExistingFileHelper exFileHelper) {
		super(output, exFileHelper);
	}

	@Override
	protected void registerStatesAndModels() {
		tintedAndFlipped(TFBlocks.TOWERWOOD.get());
		simpleBlock(TFBlocks.ENCASED_TOWERWOOD.get(), cubeAllTinted(TFBlocks.ENCASED_TOWERWOOD.getId().getPath(), TFBlocks.ENCASED_TOWERWOOD.getId().getPath()));
		simpleBlock(TFBlocks.CRACKED_TOWERWOOD.get(), ConfiguredModel.builder()
				.modelFile(cubeAllTinted(TFBlocks.CRACKED_TOWERWOOD.getId().getPath(), TFBlocks.CRACKED_TOWERWOOD.getId().getPath())).nextModel()
				.modelFile(cubeAllTinted(TFBlocks.CRACKED_TOWERWOOD.getId().getPath() + "_flipped", TFBlocks.CRACKED_TOWERWOOD.getId().getPath(), true)).nextModel()
				.modelFile(cubeAllTinted(TFBlocks.CRACKED_TOWERWOOD.getId().getPath() + "_alt", TFBlocks.CRACKED_TOWERWOOD.getId().getPath() + "_alt")).nextModel()
				.modelFile(cubeAllTinted(TFBlocks.CRACKED_TOWERWOOD.getId().getPath() + "_alt_flipped", TFBlocks.CRACKED_TOWERWOOD.getId().getPath() + "_alt", true)).build()
		);
		tintedAndFlipped(TFBlocks.MOSSY_TOWERWOOD.get());
		tintedAndFlipped(TFBlocks.INFESTED_TOWERWOOD.get());

		builtinEntity(TFBlocks.FIREFLY.get(), "twilightforest:block/stone_twist/twist_blank");
		builtinEntity(TFBlocks.MOONWORM.get(), "twilightforest:block/stone_twist/twist_blank");
		builtinEntity(TFBlocks.CICADA.get(), "twilightforest:block/stone_twist/twist_blank");

		builtinEntity(TFBlocks.RED_THREAD.get(), "twilightforest:block/stone_twist/twist_blank");

		ModelFile portalModel = models().getExistingFile(prefix("block/twilight_portal"));
		ModelFile portalOverlayModel = models().getExistingFile(prefix("block/twilight_portal_barrier"));
		getMultipartBuilder(TFBlocks.TWILIGHT_PORTAL.get())
				.part().modelFile(portalModel).addModel().end()
				.part().modelFile(portalOverlayModel).addModel().condition(TFPortalBlock.DISALLOW_RETURN, true).end();

		getVariantBuilder(TFBlocks.EXPERIMENT_115.get()).forAllStates(state -> {
			int bitesTaken = state.getValue(Experiment115Block.BITES_TAKEN);
			String basePath = String.format("block/experiment115_%d_8", 8 - bitesTaken);
			ModelFile model;
			if (state.getValue(Experiment115Block.REGENERATE)) {
				model = models().withExistingParent(basePath + "_regenerating", prefix(basePath))
						.texture("top_2", "block/experiment115/experiment115_sprinkle");
			} else {
				model = models().getExistingFile(prefix(basePath));
			}
			return ConfiguredModel.builder().modelFile(model).build();
		});

		MultiPartBlockStateBuilder ironLadder = getMultipartBuilder(TFBlocks.IRON_LADDER.get());
		ModelFile ironLadderLeft = models().getExistingFile(prefix("block/iron_ladder_left"));
		ModelFile ironLadderLeftConnected = models().getExistingFile(prefix("block/iron_ladder_left_connection"));
		ModelFile ironLadderRight = models().getExistingFile(prefix("block/iron_ladder_right"));
		ModelFile ironLadderRightConnected = models().getExistingFile(prefix("block/iron_ladder_right_connection"));
		for (Direction d : Direction.Plane.HORIZONTAL) {
			int rotY = switch (d) {
				default -> 0;
				case EAST -> 90;
				case SOUTH -> 180;
				case WEST -> 270;
			};

			ironLadder.part().modelFile(ironLadderLeft).rotationY(rotY).addModel()
					.condition(LadderBlock.FACING, d).condition(IronLadderBlock.LEFT, false).end();
			ironLadder.part().modelFile(ironLadderLeftConnected).rotationY(rotY).addModel()
					.condition(LadderBlock.FACING, d).condition(IronLadderBlock.LEFT, true).end();
			ironLadder.part().modelFile(ironLadderRight).rotationY(rotY).addModel()
					.condition(LadderBlock.FACING, d).condition(IronLadderBlock.RIGHT, false).end();
			ironLadder.part().modelFile(ironLadderRightConnected).rotationY(rotY).addModel()
					.condition(LadderBlock.FACING, d).condition(IronLadderBlock.RIGHT, true).end();
		}

		towerBlocks();

		simpleBlock(TFBlocks.FAKE_GOLD.get(), models().getExistingFile(new ResourceLocation("block/gold_block")));
		simpleBlock(TFBlocks.FAKE_DIAMOND.get(), models().getExistingFile(new ResourceLocation("block/diamond_block")));

		simpleBlock(TFBlocks.NAGA_TROPHY.get(), models().getExistingFile(new ResourceLocation("block/skull")));
		simpleBlock(TFBlocks.LICH_TROPHY.get(), models().getExistingFile(new ResourceLocation("block/skull")));
		simpleBlock(TFBlocks.MINOSHROOM_TROPHY.get(), models().getExistingFile(new ResourceLocation("block/skull")));
		simpleBlock(TFBlocks.HYDRA_TROPHY.get(), models().getExistingFile(new ResourceLocation("block/skull")));
		simpleBlock(TFBlocks.ALPHA_YETI_TROPHY.get(), models().getExistingFile(new ResourceLocation("block/skull")));
		simpleBlock(TFBlocks.SNOW_QUEEN_TROPHY.get(), models().getExistingFile(new ResourceLocation("block/skull")));
		simpleBlock(TFBlocks.KNIGHT_PHANTOM_TROPHY.get(), models().getExistingFile(new ResourceLocation("block/skull")));
		simpleBlock(TFBlocks.UR_GHAST_TROPHY.get(), models().getExistingFile(new ResourceLocation("block/skull")));
		simpleBlock(TFBlocks.QUEST_RAM_TROPHY.get(), models().getExistingFile(new ResourceLocation("block/skull")));
		simpleBlock(TFBlocks.NAGA_WALL_TROPHY.get(), models().getExistingFile(new ResourceLocation("block/skull")));
		simpleBlock(TFBlocks.LICH_WALL_TROPHY.get(), models().getExistingFile(new ResourceLocation("block/skull")));
		simpleBlock(TFBlocks.MINOSHROOM_WALL_TROPHY.get(), models().getExistingFile(new ResourceLocation("block/skull")));
		simpleBlock(TFBlocks.HYDRA_WALL_TROPHY.get(), models().getExistingFile(new ResourceLocation("block/skull")));
		simpleBlock(TFBlocks.ALPHA_YETI_WALL_TROPHY.get(), models().getExistingFile(new ResourceLocation("block/skull")));
		simpleBlock(TFBlocks.SNOW_QUEEN_WALL_TROPHY.get(), models().getExistingFile(new ResourceLocation("block/skull")));
		simpleBlock(TFBlocks.KNIGHT_PHANTOM_WALL_TROPHY.get(), models().getExistingFile(new ResourceLocation("block/skull")));
		simpleBlock(TFBlocks.UR_GHAST_WALL_TROPHY.get(), models().getExistingFile(new ResourceLocation("block/skull")));
		simpleBlock(TFBlocks.QUEST_RAM_WALL_TROPHY.get(), models().getExistingFile(new ResourceLocation("block/skull")));

		simpleBlock(TFBlocks.ZOMBIE_SKULL_CANDLE.get(), models().getExistingFile(new ResourceLocation("block/skull")));
		simpleBlock(TFBlocks.ZOMBIE_WALL_SKULL_CANDLE.get(), models().getExistingFile(new ResourceLocation("block/skull")));
		simpleBlock(TFBlocks.SKELETON_SKULL_CANDLE.get(), models().getExistingFile(new ResourceLocation("block/skull")));
		simpleBlock(TFBlocks.SKELETON_WALL_SKULL_CANDLE.get(), models().getExistingFile(new ResourceLocation("block/skull")));
		simpleBlock(TFBlocks.WITHER_SKELE_SKULL_CANDLE.get(), models().getExistingFile(new ResourceLocation("block/skull")));
		simpleBlock(TFBlocks.WITHER_SKELE_WALL_SKULL_CANDLE.get(), models().getExistingFile(new ResourceLocation("block/skull")));
		simpleBlock(TFBlocks.CREEPER_SKULL_CANDLE.get(), models().getExistingFile(new ResourceLocation("block/skull")));
		simpleBlock(TFBlocks.CREEPER_WALL_SKULL_CANDLE.get(), models().getExistingFile(new ResourceLocation("block/skull")));
		simpleBlock(TFBlocks.PLAYER_SKULL_CANDLE.get(), models().getExistingFile(new ResourceLocation("block/skull")));
		simpleBlock(TFBlocks.PLAYER_WALL_SKULL_CANDLE.get(), models().getExistingFile(new ResourceLocation("block/skull")));

		getVariantBuilder(TFBlocks.SPIRAL_BRICKS.get()).forAllStates(state -> ConfiguredModel.builder().modelFile(models().getExistingFile(TwilightForestMod.prefix("block/spiral_bricks/" + state.getValue(SpiralBrickBlock.AXIS_FACING).getName() + "_spiral_" + state.getValue(SpiralBrickBlock.DIAGONAL).getSerializedName()))).build());

		ModelFile shieldModel = models().cubeTop(TFBlocks.STRONGHOLD_SHIELD.getId().getPath(), prefix("block/shield_outside"), prefix("block/shield_inside"));
		getVariantBuilder(TFBlocks.STRONGHOLD_SHIELD.get())
				.forAllStates(state -> {
					Direction dir = state.getValue(BlockStateProperties.FACING);
					return ConfiguredModel.builder()
							.uvLock(true)
							.modelFile(shieldModel)
							.rotationX(dir == Direction.DOWN ? 180 : dir.getAxis().isHorizontal() ? 90 : 0)
							.rotationY(dir.getAxis().isVertical() ? 0 : (int) dir.toYRot() % 360)
							.build();
				});

		trophyPedestal();
		auroraBlocks();
		slider();
		simpleBlock(TFBlocks.UNDERBRICK.get());
		simpleBlock(TFBlocks.CRACKED_UNDERBRICK.get());
		simpleBlock(TFBlocks.MOSSY_UNDERBRICK.get());
		simpleBlock(TFBlocks.UNDERBRICK_FLOOR.get());
		thorns();
		thornRose();
		simpleBlock(TFBlocks.THORN_LEAVES.get(), models().withExistingParent("thorn_leaves", new ResourceLocation("block/oak_leaves")));
		simpleBlock(TFBlocks.BEANSTALK_LEAVES.get(), models().withExistingParent("beanstalk_leaves", new ResourceLocation("block/azalea_leaves")));
		simpleBlock(TFBlocks.HOLLOW_OAK_SAPLING.get(), models().cross(TFBlocks.HOLLOW_OAK_SAPLING.getId().getPath(), blockTexture(TFBlocks.HOLLOW_OAK_SAPLING.get())).renderType(CUTOUT));
		ModelFile deadrock = models().cubeAll(TFBlocks.DEADROCK.getId().getPath(), blockTexture(TFBlocks.DEADROCK.get()));
		simpleBlock(TFBlocks.DEADROCK.get(), ConfiguredModel.builder()
				.modelFile(deadrock).nextModel()
				.rotationY(180).modelFile(deadrock).nextModel()
				.rotationY(90).modelFile(deadrock).nextModel()
				.rotationY(270).modelFile(deadrock).build()
		);
		ModelFile deadrockCracked = models().cubeAll(TFBlocks.CRACKED_DEADROCK.getId().getPath(), blockTexture(TFBlocks.CRACKED_DEADROCK.get()));
		allRotations(TFBlocks.CRACKED_DEADROCK.get(), deadrockCracked);
		ModelFile deadrockWeathered = models().cubeAll(TFBlocks.WEATHERED_DEADROCK.getId().getPath(), blockTexture(TFBlocks.WEATHERED_DEADROCK.get()));
		allRotations(TFBlocks.WEATHERED_DEADROCK.get(), deadrockWeathered);
		perFaceBlock(TFBlocks.TROLLSTEINN.get(), blockTexture(TFBlocks.TROLLSTEINN.get()), prefix("block/" + TFBlocks.TROLLSTEINN.getId().getPath() + "_light"));
		simpleBlockWithRenderType(TFBlocks.WISPY_CLOUD.get(), TRANSLUCENT);
		simpleBlock(TFBlocks.FLUFFY_CLOUD.get());
		simpleBlock(TFBlocks.GIANT_COBBLESTONE.get(), models().withExistingParent(TFBlocks.GIANT_COBBLESTONE.getId().getPath(), new ResourceLocation("block/block"))
				.texture("particle", blockTexture(Blocks.COBBLESTONE))
				.texture("all", blockTexture(Blocks.COBBLESTONE))
				.customLoader(GiantBlockBuilder::begin).parentBlock(Blocks.COBBLESTONE).end());
		simpleBlock(TFBlocks.GIANT_LOG.get(), models().withExistingParent(TFBlocks.GIANT_LOG.getId().getPath(), new ResourceLocation("block/block"))
				.texture("particle", blockTexture(Blocks.OAK_LOG))
				.texture("north", blockTexture(Blocks.OAK_LOG)).texture("south", blockTexture(Blocks.OAK_LOG))
				.texture("west", blockTexture(Blocks.OAK_LOG)).texture("east", blockTexture(Blocks.OAK_LOG))
				.texture("up", blockTexture(Blocks.OAK_LOG) + "_top").texture("down", blockTexture(Blocks.OAK_LOG) + "_top")
				.customLoader(GiantBlockBuilder::begin).parentBlock(Blocks.OAK_LOG).end());
		simpleBlock(TFBlocks.GIANT_LEAVES.get(), models().withExistingParent(TFBlocks.GIANT_LEAVES.getId().getPath(), new ResourceLocation("block/block"))
				.renderType(CUTOUT_MIPPED)
				.texture("particle", blockTexture(Blocks.OAK_LEAVES))
				.texture("all", blockTexture(Blocks.OAK_LEAVES))
				.customLoader(GiantBlockBuilder::begin).parentBlock(Blocks.OAK_LEAVES).end());
		simpleBlock(TFBlocks.GIANT_OBSIDIAN.get(), models().withExistingParent(TFBlocks.GIANT_OBSIDIAN.getId().getPath(), new ResourceLocation("block/block"))
				.texture("particle", blockTexture(Blocks.OBSIDIAN))
				.texture("all", blockTexture(Blocks.OBSIDIAN))
				.customLoader(GiantBlockBuilder::begin).parentBlock(Blocks.OBSIDIAN).end());
		simpleBlock(TFBlocks.UBEROUS_SOIL.get(), models().withExistingParent(TFBlocks.UBEROUS_SOIL.getId().getPath(), "block/template_farmland").renderType(TRANSLUCENT)
				.texture("top", blockTexture(TFBlocks.UBEROUS_SOIL.get()))
				.texture("dirt", blockTexture(TFBlocks.UBEROUS_SOIL.get())));
		axisBlock(TFBlocks.HUGE_STALK.get(), prefix("block/huge_stalk"), prefix("block/huge_stalk_top"));
		builtinEntity(TFBlocks.BEANSTALK_GROWER.get(), "twilightforest:block/stone_twist/twist_blank");
		perFaceBlock(TFBlocks.HUGE_MUSHGLOOM.get(), prefix("block/huge_gloom_inside"), prefix("block/huge_gloom_cap"));
		perFaceBlock(TFBlocks.HUGE_MUSHGLOOM_STEM.get(), prefix("block/huge_gloom_inside"), prefix("block/huge_mushgloom_stem"));
		simpleBlock(TFBlocks.TROLLVIDR.get(), models().cross(TFBlocks.TROLLVIDR.getId().getPath(), blockTexture(TFBlocks.TROLLVIDR.get())).renderType(CUTOUT));
		simpleBlock(TFBlocks.UNRIPE_TROLLBER.get(), models().cross(TFBlocks.UNRIPE_TROLLBER.getId().getPath(), blockTexture(TFBlocks.UNRIPE_TROLLBER.get())).renderType(CUTOUT));
		ModelFile trollber = this.make2layerCross(TFBlocks.TROLLBER.getId().getPath(), CUTOUT, 1, 10)
				.texture("cross", blockTexture(TFBlocks.TROLLBER.get()))
				.texture("cross2", prefix("block/" + TFBlocks.TROLLBER.getId().getPath() + "_glow"));
		simpleBlock(TFBlocks.TROLLBER.get(), trollber);
		lilyPad(TFBlocks.HUGE_LILY_PAD.get());
		simpleBlock(TFBlocks.HUGE_WATER_LILY.get(), models().cross(TFBlocks.HUGE_WATER_LILY.getId().getPath(), blockTexture(TFBlocks.HUGE_WATER_LILY.get())).renderType(CUTOUT));
		simpleBlock(TFBlocks.CASTLE_BRICK.get());
		simpleBlock(TFBlocks.WORN_CASTLE_BRICK.get());
		simpleBlock(TFBlocks.CRACKED_CASTLE_BRICK.get());
		allRotations(TFBlocks.CASTLE_ROOF_TILE.get(), cubeAll(TFBlocks.CASTLE_ROOF_TILE.get()));
		simpleBlock(TFBlocks.MOSSY_CASTLE_BRICK.get());
		simpleBlock(TFBlocks.THICK_CASTLE_BRICK.get());

		rotationallyCorrectColumn(TFBlocks.ENCASED_CASTLE_BRICK_PILLAR.get());
		rotationallyCorrectColumn(TFBlocks.BOLD_CASTLE_BRICK_PILLAR.get());
		simpleBlock(TFBlocks.ENCASED_CASTLE_BRICK_TILE.get(), models().cubeAll(TFBlocks.ENCASED_CASTLE_BRICK_TILE.getId().getPath(), prefix("block/" + TFBlocks.ENCASED_CASTLE_BRICK_PILLAR.getId().getPath() + "_end")));
		simpleBlock(TFBlocks.BOLD_CASTLE_BRICK_TILE.get());
		stairsBlock(TFBlocks.CASTLE_BRICK_STAIRS.get(), prefix("block/" + TFBlocks.CASTLE_BRICK_STAIRS.getId().getPath()));
		stairsBlock(TFBlocks.WORN_CASTLE_BRICK_STAIRS.get(), prefix("block/" + TFBlocks.WORN_CASTLE_BRICK.getId().getPath()));
		stairsBlock(TFBlocks.CRACKED_CASTLE_BRICK_STAIRS.get(), prefix("block/" + TFBlocks.CRACKED_CASTLE_BRICK_STAIRS.getId().getPath()));
		stairsBlock(TFBlocks.MOSSY_CASTLE_BRICK_STAIRS.get(), prefix("block/" + TFBlocks.MOSSY_CASTLE_BRICK_STAIRS.getId().getPath()));
		bisectedStairsBlock(TFBlocks.ENCASED_CASTLE_BRICK_STAIRS, prefix("block/encased_castle_brick_pillar_h"), prefix("block/castleblock_tile"), prefix("block/" + TFBlocks.CASTLE_ROOF_TILE.getId().getPath()));
		stairsBlock(TFBlocks.BOLD_CASTLE_BRICK_STAIRS.get(), prefix("block/" + TFBlocks.BOLD_CASTLE_BRICK_TILE.getId().getPath()));

		ConfiguredModel[] runeBrickModels = new ConfiguredModel[8];
		for (int i = 0; i < runeBrickModels.length; i++) {
			runeBrickModels[i] = new ConfiguredModel(models().withExistingParent("castle_rune_brick_" + i, "block/block")
					.texture("particle", prefix("block/castle_brick")).customLoader(CompositeModelBuilder::begin)
					.child("brick", models().withExistingParent("castle_rune_bricks", "block/cube_all").texture("all", prefix("block/castle_brick")))
					.child("runes", this.makeEmissiveBlockAll("castle_runes_" + i, CUTOUT, 15).texture("all", prefix("block/castleblock_magic_" + i)))
					.end());
		}

		this.make2LayerCubeAllSidesSame("castle_rune_inventory", CUTOUT, 0, 15, false)
				.texture("all", prefix("block/castle_brick"))
				.texture("all2", prefix("block/castleblock_magic_0"));

		simpleBlock(TFBlocks.YELLOW_CASTLE_RUNE_BRICK.get(), runeBrickModels);
		simpleBlock(TFBlocks.VIOLET_CASTLE_RUNE_BRICK.get(), runeBrickModels);
		simpleBlock(TFBlocks.PINK_CASTLE_RUNE_BRICK.get(), runeBrickModels);
		simpleBlock(TFBlocks.BLUE_CASTLE_RUNE_BRICK.get(), runeBrickModels);

		logBlock(TFBlocks.CINDER_LOG.get());
		simpleBlock(TFBlocks.CINDER_WOOD.get(), models().cubeAll(TFBlocks.CINDER_WOOD.getId().getPath(), prefix("block/" + TFBlocks.CINDER_LOG.getId().getPath())));
		ModelFile furnaceOff = models().getExistingFile(new ResourceLocation("block/furnace"));
		ModelFile furnaceOn = models().getExistingFile(new ResourceLocation("block/furnace_on"));
		horizontalBlock(TFBlocks.CINDER_FURNACE.get(), state -> state.getValue(AbstractFurnaceBlock.LIT) ? furnaceOn : furnaceOff);

		castleDoor(TFBlocks.YELLOW_CASTLE_DOOR.get());
		castleDoor(TFBlocks.VIOLET_CASTLE_DOOR.get());
		castleDoor(TFBlocks.PINK_CASTLE_DOOR.get());
		castleDoor(TFBlocks.BLUE_CASTLE_DOOR.get());

		simpleBlockExisting(TFBlocks.KNIGHTMETAL_BLOCK.get());
		simpleBlock(TFBlocks.IRONWOOD_BLOCK.get());
		simpleBlockExisting(TFBlocks.FIERY_BLOCK.get());
		simpleBlock(TFBlocks.ARCTIC_FUR_BLOCK.get());
		ModelFile steeleafBlock = models().cubeAll(TFBlocks.STEELEAF_BLOCK.getId().getPath(), prefix("block/" + TFBlocks.STEELEAF_BLOCK.getId().getPath()));
		allRotations(TFBlocks.STEELEAF_BLOCK.get(), steeleafBlock);
		ModelFile carminiteBlock = this.make2LayerCubeAllSidesSame(TFBlocks.CARMINITE_BLOCK.getId().getPath(), SOLID, 4, 7, true)
				.texture("all", prefix("block/" + TFBlocks.CARMINITE_BLOCK.getId().getPath()))
				.texture("all2", prefix("block/" + TFBlocks.CARMINITE_BLOCK.getId().getPath() + "_overlay"));
		allRotations(TFBlocks.CARMINITE_BLOCK.get(), carminiteBlock);

		simpleBlock(TFBlocks.TWILIGHT_PORTAL_MINIATURE_STRUCTURE.get(), models().getExistingFile(prefix("block/miniature/portal")));
		simpleBlock(TFBlocks.NAGA_COURTYARD_MINIATURE_STRUCTURE.get(), models().getExistingFile(prefix("block/miniature/naga_courtyard")));
		simpleBlock(TFBlocks.LICH_TOWER_MINIATURE_STRUCTURE.get(), models().getExistingFile(prefix("block/miniature/lich_tower")));
		mazestone();
		simpleBlock(TFBlocks.HEDGE.get(), ConfiguredModel.builder()
				.weight(10).modelFile(models().cubeAll(TFBlocks.HEDGE.getId().getPath(), blockTexture(TFBlocks.HEDGE.get()))).nextModel()
				.weight(1).modelFile(models().cubeAll(TFBlocks.HEDGE.getId().getPath() + "_rose", prefix("block/" + TFBlocks.HEDGE.getId().getPath() + "_rose"))).build());

		/*ModelFile bigSpawner = models().withExistingParent("boss_spawner", "block/block").renderType(CUTOUT).texture("particle", "#all").texture("all", TwilightForestMod.prefix("block/boss_spawner")).element()
				.from(-4, -4, -4)
				.to(20, 20, 20)
				.allFaces((dir, builder) -> builder*//*.cullface(dir)*//*.uvs(2, 2, 14, 14).texture("#all"))
				.end();*/

		ModelFile bigSpawner = models().cubeAll("boss_spawner", TwilightForestMod.prefix("block/boss_spawner")).renderType(CUTOUT);

		simpleBlock(TFBlocks.NAGA_BOSS_SPAWNER.get(), bigSpawner);
		simpleBlock(TFBlocks.LICH_BOSS_SPAWNER.get(), bigSpawner);
		simpleBlock(TFBlocks.HYDRA_BOSS_SPAWNER.get(), bigSpawner);
		simpleBlock(TFBlocks.UR_GHAST_BOSS_SPAWNER.get(), bigSpawner);
		simpleBlock(TFBlocks.KNIGHT_PHANTOM_BOSS_SPAWNER.get(), bigSpawner);
		simpleBlock(TFBlocks.SNOW_QUEEN_BOSS_SPAWNER.get(), bigSpawner);
		simpleBlock(TFBlocks.MINOSHROOM_BOSS_SPAWNER.get(), bigSpawner);
		simpleBlock(TFBlocks.ALPHA_YETI_BOSS_SPAWNER.get(), bigSpawner);
		simpleBlock(TFBlocks.FINAL_BOSS_BOSS_SPAWNER.get(), bigSpawner);
		simpleBlock(TFBlocks.FIREFLY_JAR.get(), this.makeJar(TFBlocks.FIREFLY_JAR.getId().getPath())
				.texture("cork", prefix("block/firefly_jar_cork")));
		simpleBlockExisting(TFBlocks.FIREFLY_SPAWNER.get());
		simpleBlock(TFBlocks.CICADA_JAR.get(), this.makeJar(TFBlocks.CICADA_JAR.getId().getPath())
				.texture("cork", prefix("block/cicada_jar_cork")));
		registerPlantBlocks();
		simpleBlock(TFBlocks.ROOT_BLOCK.get());
		simpleBlock(TFBlocks.LIVEROOT_BLOCK.get());
		simpleBlock(TFBlocks.MANGROVE_ROOT.get());

		ModelFile glowing = this.make2LayerCubeNoBottom(TFBlocks.UNCRAFTING_TABLE.getId().getPath() + "_glowing", TRANSLUCENT, 0, 15, true)
				.texture("top", prefix("block/uncrafting_top"))
				.texture("north", prefix("block/uncrafting_side"))
				.texture("south", prefix("block/uncrafting_side"))
				.texture("west", prefix("block/uncrafting_side"))
				.texture("east", prefix("block/uncrafting_side"))
				.texture("bottom", new ResourceLocation("block/jungle_planks"))
				.texture("top2", prefix("block/uncrafting_glow"))
				.texture("north2", prefix("block/uncrafting_glow_side"))
				.texture("south2", prefix("block/uncrafting_glow_side"))
				.texture("west2", prefix("block/uncrafting_glow_side"))
				.texture("east2", prefix("block/uncrafting_glow_side"));

		ModelFile notglowing = this.makeCubeWithTopLayer(TFBlocks.UNCRAFTING_TABLE.getId().getPath(), TRANSLUCENT, 0, 15)
				.texture("top", prefix("block/uncrafting_top"))
				.texture("north", prefix("block/uncrafting_side"))
				.texture("south", prefix("block/uncrafting_side"))
				.texture("west", prefix("block/uncrafting_side"))
				.texture("east", prefix("block/uncrafting_side"))
				.texture("bottom", new ResourceLocation("block/jungle_planks"))
				.texture("top2", prefix("block/uncrafting_glow"));

		getVariantBuilder(TFBlocks.UNCRAFTING_TABLE.get()).forAllStates(s -> ConfiguredModel.builder().modelFile(s.getValue(UncraftingTableBlock.POWERED) ? glowing : notglowing).build());
		registerSmokersAndJets();
		axisBlock(TFBlocks.TWISTED_STONE.get(), prefix("block/stone_twist/twist_side"), prefix("block/stone_twist/twist_end"));
		axisBlock(TFBlocks.BOLD_STONE_PILLAR.get(), prefix("block/stone_pillar_side"), prefix("block/stone_pillar_end"));
		simpleBlock(TFBlocks.EMPTY_CANOPY_BOOKSHELF.get(), models().cubeColumn("empty_canopy_bookshelf", prefix("block/wood/bookshelf_spawner/bookshelf_empty"), prefix("block/wood/planks_canopy_0")));
		simpleBlock(TFBlocks.CANOPY_BOOKSHELF.get(), ConfiguredModel.builder()
				.weight(3).modelFile(models().cubeColumn("canopy_bookshelf", prefix("block/wood/bookshelf_canopy"), prefix("block/wood/planks_canopy_0"))).nextModel()
				.modelFile(models().cubeColumn("canopy_bookshelf_1", prefix("block/wood/bookshelf_canopy_1"), prefix("block/wood/planks_canopy_0"))).nextModel()
				.modelFile(models().cubeColumn("canopy_bookshelf_2", prefix("block/wood/bookshelf_canopy_2"), prefix("block/wood/planks_canopy_0"))).nextModel()
				.modelFile(models().cubeColumn("canopy_bookshelf_3", prefix("block/wood/bookshelf_canopy_3"), prefix("block/wood/planks_canopy_0")))
				.build());
		//if theres a better way to do this block please do it. I dont want to think about it right now
		getVariantBuilder(TFBlocks.DEATH_TOME_SPAWNER.get()).forAllStatesExcept(s -> {
			switch (s.getValue(TomeSpawnerBlock.BOOK_STAGES)) {
				case 1 -> {
					return ConfiguredModel.builder().modelFile(models().cubeColumn("block/death_tome_spawner_1", prefix("block/wood/bookshelf_spawner/bookshelf_1"), prefix("block/wood/planks_canopy_0"))).build();
				}
				case 2 -> {
					return ConfiguredModel.builder().modelFile(models().cubeColumn("block/death_tome_spawner_2", prefix("block/wood/bookshelf_spawner/bookshelf_2"), prefix("block/wood/planks_canopy_0"))).build();
				}
				case 3 -> {
					return ConfiguredModel.builder().modelFile(models().cubeColumn("block/death_tome_spawner_3", prefix("block/wood/bookshelf_spawner/bookshelf_3"), prefix("block/wood/planks_canopy_0"))).build();
				}
				case 4 -> {
					return ConfiguredModel.builder().modelFile(models().cubeColumn("block/death_tome_spawner_4", prefix("block/wood/bookshelf_spawner/bookshelf_4"), prefix("block/wood/planks_canopy_0"))).build();
				}
				case 5 -> {
					return ConfiguredModel.builder().modelFile(models().cubeColumn("block/death_tome_spawner_5", prefix("block/wood/bookshelf_spawner/bookshelf_5"), prefix("block/wood/planks_canopy_0"))).build();
				}
				case 6 -> {
					return ConfiguredModel.builder().modelFile(models().cubeColumn("block/death_tome_spawner_6", prefix("block/wood/bookshelf_spawner/bookshelf_6"), prefix("block/wood/planks_canopy_0"))).build();
				}
				case 7 -> {
					return ConfiguredModel.builder().modelFile(models().cubeColumn("block/death_tome_spawner_7", prefix("block/wood/bookshelf_spawner/bookshelf_7"), prefix("block/wood/planks_canopy_0"))).build();
				}
				case 8 -> {
					return ConfiguredModel.builder().modelFile(models().cubeColumn("block/death_tome_spawner_8", prefix("block/wood/bookshelf_spawner/bookshelf_8"), prefix("block/wood/planks_canopy_0"))).build();
				}
				case 9 -> {
					return ConfiguredModel.builder().modelFile(models().cubeColumn("block/death_tome_spawner_9", prefix("block/wood/bookshelf_spawner/bookshelf_9"), prefix("block/wood/planks_canopy_0"))).build();
				}
				default -> {
					return ConfiguredModel.builder().modelFile(models().cubeColumn("block/death_tome_spawner_10", prefix("block/wood/bookshelf_canopy"), prefix("block/wood/planks_canopy_0"))).build();
				}
			}
		}, TomeSpawnerBlock.SPAWNER);

		registerWoodBlocks();
		registerNagastone();
		registerForceFields();
		simpleBlock(TFBlocks.POTTED_TWILIGHT_OAK_SAPLING.get(), models().withExistingParent(TFBlocks.POTTED_TWILIGHT_OAK_SAPLING.getId().getPath(), "block/flower_pot_cross").renderType(CUTOUT).texture("plant", blockTexture(TFBlocks.TWILIGHT_OAK_SAPLING.get())));
		simpleBlock(TFBlocks.POTTED_CANOPY_SAPLING.get(), models().withExistingParent(TFBlocks.POTTED_CANOPY_SAPLING.getId().getPath(), "block/flower_pot_cross").renderType(CUTOUT).texture("plant", blockTexture(TFBlocks.CANOPY_SAPLING.get())));
		simpleBlock(TFBlocks.POTTED_MANGROVE_SAPLING.get(), models().withExistingParent(TFBlocks.POTTED_MANGROVE_SAPLING.getId().getPath(), prefix("block/potted_larger_sapling")).renderType(CUTOUT).texture("plant", blockTexture(TFBlocks.MANGROVE_SAPLING.get())));
		simpleBlock(TFBlocks.POTTED_DARKWOOD_SAPLING.get(), models().withExistingParent(TFBlocks.POTTED_DARKWOOD_SAPLING.getId().getPath(), prefix("block/potted_larger_sapling")).renderType(CUTOUT).texture("plant", blockTexture(TFBlocks.DARKWOOD_SAPLING.get())));
		simpleBlock(TFBlocks.POTTED_HOLLOW_OAK_SAPLING.get(), models().withExistingParent(TFBlocks.POTTED_HOLLOW_OAK_SAPLING.getId().getPath(), "block/flower_pot_cross").renderType(CUTOUT).texture("plant", blockTexture(TFBlocks.HOLLOW_OAK_SAPLING.get())));
		simpleBlock(TFBlocks.POTTED_RAINBOW_OAK_SAPLING.get(), models().withExistingParent(TFBlocks.POTTED_RAINBOW_OAK_SAPLING.getId().getPath(), "block/flower_pot_cross").renderType(CUTOUT).texture("plant", blockTexture(TFBlocks.RAINBOW_OAK_SAPLING.get())));
		simpleBlock(TFBlocks.POTTED_TIME_SAPLING.get(), models().withExistingParent(TFBlocks.POTTED_TIME_SAPLING.getId().getPath(), prefix("block/potted_larger_sapling")).renderType(CUTOUT).texture("plant", blockTexture(TFBlocks.TIME_SAPLING.get())));
		simpleBlock(TFBlocks.POTTED_TRANSFORMATION_SAPLING.get(), models().withExistingParent(TFBlocks.POTTED_TRANSFORMATION_SAPLING.getId().getPath(), "block/flower_pot_cross").renderType(CUTOUT).texture("plant", blockTexture(TFBlocks.TRANSFORMATION_SAPLING.get())));
		simpleBlock(TFBlocks.POTTED_MINING_SAPLING.get(), models().withExistingParent(TFBlocks.POTTED_MINING_SAPLING.getId().getPath(), "block/flower_pot_cross").renderType(CUTOUT).texture("plant", blockTexture(TFBlocks.MINING_SAPLING.get())));
		simpleBlock(TFBlocks.POTTED_SORTING_SAPLING.get(), models().withExistingParent(TFBlocks.POTTED_SORTING_SAPLING.getId().getPath(), "block/flower_pot_cross").renderType(CUTOUT).texture("plant", blockTexture(TFBlocks.SORTING_SAPLING.get())));
		simpleBlock(TFBlocks.POTTED_MAYAPPLE.get(), models().getExistingFile(prefix("block/potted_mayapple")));
		simpleBlock(TFBlocks.POTTED_FIDDLEHEAD.get(), models().withExistingParent(TFBlocks.POTTED_FIDDLEHEAD.getId().getPath(), "block/tinted_flower_pot_cross").renderType(CUTOUT).texture("plant", blockTexture(TFBlocks.POTTED_FIDDLEHEAD.get())));
		simpleBlock(TFBlocks.POTTED_MUSHGLOOM.get(), models().withExistingParent(TFBlocks.POTTED_MUSHGLOOM.getId().getPath(), "block/flower_pot_cross").renderType(CUTOUT).texture("plant", blockTexture(TFBlocks.POTTED_MUSHGLOOM.get())));
		simpleBlock(TFBlocks.POTTED_THORN.get(), models().withExistingParent(TFBlocks.POTTED_THORN.getId().getPath(), prefix("block/potted_thorn_template")).texture("thorn_top", prefix("block/brown_thorns_top")).texture("thorn_side", prefix("block/brown_thorns_side")));
		simpleBlock(TFBlocks.POTTED_GREEN_THORN.get(), models().withExistingParent(TFBlocks.POTTED_THORN.getId().getPath(), prefix("block/potted_thorn_template")).texture("thorn_top", prefix("block/green_thorns_top")).texture("thorn_side", prefix("block/green_thorns_side")));
		simpleBlock(TFBlocks.POTTED_DEAD_THORN.get(), models().withExistingParent(TFBlocks.POTTED_THORN.getId().getPath(), prefix("block/potted_thorn_template")).texture("thorn_top", prefix("block/burnt_thorns_top")).texture("thorn_side", prefix("block/burnt_thorns_side")));

		builtinEntity(TFBlocks.TWILIGHT_OAK_SIGN.get(), "twilightforest:block/wood/planks_twilight_oak_0");
		builtinEntity(TFBlocks.TWILIGHT_WALL_SIGN.get(), "twilightforest:block/wood/planks_twilight_oak_0");
		builtinEntity(TFBlocks.CANOPY_SIGN.get(), "twilightforest:block/wood/planks_canopy_0");
		builtinEntity(TFBlocks.CANOPY_WALL_SIGN.get(), "twilightforest:block/wood/planks_canopy_0");
		builtinEntity(TFBlocks.MANGROVE_SIGN.get(), "twilightforest:block/wood/planks_mangrove_0");
		builtinEntity(TFBlocks.MANGROVE_WALL_SIGN.get(), "twilightforest:block/wood/planks_mangrove_0");
		builtinEntity(TFBlocks.DARK_SIGN.get(), "twilightforest:block/wood/planks_darkwood_0");
		builtinEntity(TFBlocks.DARK_WALL_SIGN.get(), "twilightforest:block/wood/planks_darkwood_0");
		builtinEntity(TFBlocks.TIME_SIGN.get(), "twilightforest:block/wood/planks_time_0");
		builtinEntity(TFBlocks.TIME_WALL_SIGN.get(), "twilightforest:block/wood/planks_time_0");
		builtinEntity(TFBlocks.TRANSFORMATION_SIGN.get(), "twilightforest:block/wood/planks_trans_0");
		builtinEntity(TFBlocks.TRANSFORMATION_WALL_SIGN.get(), "twilightforest:block/wood/planks_trans_0");
		builtinEntity(TFBlocks.MINING_SIGN.get(), "twilightforest:block/wood/planks_mine_0");
		builtinEntity(TFBlocks.MINING_WALL_SIGN.get(), "twilightforest:block/wood/planks_mine_0");
		builtinEntity(TFBlocks.SORTING_SIGN.get(), "twilightforest:block/wood/planks_sort_0");
		builtinEntity(TFBlocks.SORTING_WALL_SIGN.get(), "twilightforest:block/wood/planks_sort_0");

		builtinEntity(TFBlocks.TWILIGHT_OAK_CHEST.get(), "twilightforest:block/wood/planks_twilight_oak_0");
		builtinEntity(TFBlocks.CANOPY_CHEST.get(), "twilightforest:block/wood/planks_canopy_0");
		builtinEntity(TFBlocks.MANGROVE_CHEST.get(), "twilightforest:block/wood/planks_mangrove_0");
		builtinEntity(TFBlocks.DARK_CHEST.get(), "twilightforest:block/wood/planks_darkwood_0");
		builtinEntity(TFBlocks.TIME_CHEST.get(), "twilightforest:block/wood/planks_time_0");
		builtinEntity(TFBlocks.TRANSFORMATION_CHEST.get(), "twilightforest:block/wood/planks_trans_0");
		builtinEntity(TFBlocks.MINING_CHEST.get(), "twilightforest:block/wood/planks_mine_0");
		builtinEntity(TFBlocks.SORTING_CHEST.get(), "twilightforest:block/wood/planks_sort_0");

		casketStuff();
		stonePillar();
		candelabra();
	}

	/**
	 * AVERT YOUR GAZE WHILE SCROLLING PAST THIS METHOD, LOOK FOR TOO LONG AND YOU WILL GO MAD
	 * if you find a way to trim this down without sacrificing the modularity of the force-fields, feel free
	 */
	private void registerForceFields() {
		String baseName = "block/force_field/force_field";
		//WEST
		BlockModelBuilder west = this.models().withExistingParent(baseName + "_west", "minecraft:block/block").texture("particle", "#pane").ao(false)
				.element().from(0, 7, 7).to(7, 9, 9).face(Direction.WEST).cullface(Direction.WEST).uvs(7, 7, 9, 9).texture("#pane").emissivity(15, 15).end().shade(false).end();

		BlockModelBuilder no_west = this.models().withExistingParent(baseName + "_no_west", "minecraft:block/block").texture("particle", "#pane").ao(false)
				.element().from(7, 7, 7).to(9, 9, 9).face(Direction.WEST).uvs(7, 7, 9, 9).texture("#pane").emissivity(15, 15).end().shade(false).end();
		//EAST
		BlockModelBuilder east = this.models().withExistingParent(baseName + "_east", "minecraft:block/block").texture("particle", "#pane").ao(false)
				.element().from(9, 7, 7).to(16, 9, 9).face(Direction.EAST).cullface(Direction.EAST).uvs(7, 7, 9, 9).texture("#pane").emissivity(15, 15).end().shade(false).end();

		BlockModelBuilder no_east = this.models().withExistingParent(baseName + "_no_east", "minecraft:block/block").texture("particle", "#pane").ao(false)
				.element().from(7, 7, 7).to(9, 9, 9).face(Direction.EAST).uvs(7, 7, 9, 9).texture("#pane").emissivity(15, 15).end().shade(false).end();
		//DOWN
		BlockModelBuilder down = this.models().withExistingParent(baseName + "_down", "minecraft:block/block").texture("particle", "#pane").ao(false)
				.element().from(7, 0, 7).to(9, 7, 9).face(Direction.DOWN).cullface(Direction.DOWN).uvs(7, 7, 9, 9).texture("#pane").emissivity(15, 15).end().shade(false).end();

		BlockModelBuilder no_down = this.models().withExistingParent(baseName + "_no_down", "minecraft:block/block").texture("particle", "#pane").ao(false)
				.element().from(7, 7, 7).to(9, 9, 9).face(Direction.DOWN).uvs(7, 7, 9, 9).texture("#pane").emissivity(15, 15).end().shade(false).end();
		//UP
		BlockModelBuilder up = this.models().withExistingParent(baseName + "_up", "minecraft:block/block").texture("particle", "#pane").ao(false)
				.element().from(7, 9, 7).to(9, 16, 9).face(Direction.UP).cullface(Direction.UP).uvs(7, 7, 9, 9).texture("#pane").emissivity(15, 15).end().shade(false).end();

		BlockModelBuilder no_up = this.models().withExistingParent(baseName + "_no_up", "minecraft:block/block").texture("particle", "#pane").ao(false)
				.element().from(7, 7, 7).to(9, 9, 9).face(Direction.UP).uvs(7, 7, 9, 9).texture("#pane").emissivity(15, 15).end().shade(false).end();
		//NORTH
		BlockModelBuilder north = this.models().withExistingParent(baseName + "_north", "minecraft:block/block").texture("particle", "#pane").ao(false)
				.element().from(7, 7, 0).to(9, 9, 7).face(Direction.NORTH).cullface(Direction.NORTH).uvs(7, 7, 9, 9).texture("#pane").emissivity(15, 15).end().shade(false).end();

		BlockModelBuilder no_north = this.models().withExistingParent(baseName + "_no_north", "minecraft:block/block").texture("particle", "#pane").ao(false)
				.element().from(7, 7, 7).to(9, 9, 9).face(Direction.NORTH).uvs(7, 7, 9, 9).texture("#pane").emissivity(15, 15).end().shade(false).end();
		//SOUTH
		BlockModelBuilder south = this.models().withExistingParent(baseName + "_south", "minecraft:block/block").texture("particle", "#pane").ao(false)
				.element().from(7, 7, 9).to(9, 9, 16).face(Direction.SOUTH).cullface(Direction.SOUTH).uvs(7, 7, 9, 9).texture("#pane").emissivity(15, 15).end().shade(false).end();

		BlockModelBuilder no_south = this.models().withExistingParent(baseName + "_no_south", "minecraft:block/block").texture("particle", "#pane").ao(false)
				.element().from(7, 7, 7).to(9, 9, 9).face(Direction.SOUTH).uvs(7, 7, 9, 9).texture("#pane").emissivity(15, 15).end().shade(false).end();
		//DOWN WEST
		BlockModelBuilder down_west = this.models().withExistingParent(baseName + "_down_west", "minecraft:block/block").texture("particle", "#pane").ao(false)
				.element().from(0, 0, 7).to(7, 7, 9)
				.face(Direction.DOWN).cullface(Direction.DOWN).uvs(0, 7, 7, 9).end()
				.face(Direction.WEST).cullface(Direction.WEST).uvs(7, 0, 9, 7).end()
				.face(Direction.NORTH).uvs(0, 0, 7, 7).end()
				.face(Direction.SOUTH).uvs(9, 0, 16, 7).end().faces((direction, builder) -> builder.texture("#pane").emissivity(15, 15)).shade(false).end();

		BlockModelBuilder down_no_west = this.models().withExistingParent(baseName + "_down_no_west", "minecraft:block/block").texture("particle", "#pane").ao(false)
				.element().from(7, 0, 7).to(9, 7, 9).face(Direction.WEST).uvs(7, 0, 9, 7).texture("#pane").emissivity(15, 15).end().shade(false).end();

		BlockModelBuilder west_no_down = this.models().withExistingParent(baseName + "_west_no_down", "minecraft:block/block").texture("particle", "#pane").ao(false)
				.element().from(0, 7, 7).to(7, 9, 9).face(Direction.DOWN).uvs(0, 7, 7, 9).texture("#pane").emissivity(15, 15).end().shade(false).end();
		//DOWN EAST
		BlockModelBuilder down_east = this.models().withExistingParent(baseName + "_down_east", "minecraft:block/block").texture("particle", "#pane").ao(false)
				.element().from(9, 0, 7).to(16, 7, 9)
				.face(Direction.DOWN).cullface(Direction.DOWN).uvs(9, 7, 16, 9).end()
				.face(Direction.EAST).cullface(Direction.EAST).uvs(7, 0, 9, 7).end()
				.face(Direction.NORTH).uvs(9, 0, 16, 7).end()
				.face(Direction.SOUTH).uvs(0, 0, 7, 7).end().faces((direction, builder) -> builder.texture("#pane").emissivity(15, 15)).shade(false).end();

		BlockModelBuilder down_no_east = this.models().withExistingParent(baseName + "_down_no_east", "minecraft:block/block").texture("particle", "#pane").ao(false)
				.element().from(7, 0, 7).to(9, 7, 9).face(Direction.EAST).uvs(7, 0, 9, 7).texture("#pane").emissivity(15, 15).end().shade(false).end();

		BlockModelBuilder east_no_down = this.models().withExistingParent(baseName + "_east_no_down", "minecraft:block/block").texture("particle", "#pane").ao(false)
				.element().from(9, 7, 7).to(16, 9, 9).face(Direction.DOWN).uvs(9, 7, 16, 9).texture("#pane").emissivity(15, 15).end().shade(false).end();
		//DOWN NORTH
		BlockModelBuilder down_north = this.models().withExistingParent(baseName + "_down_north", "minecraft:block/block").texture("particle", "#pane").ao(false)
				.element().from(7, 0, 0).to(9, 7, 7)
				.face(Direction.DOWN).cullface(Direction.DOWN).uvs(7, 0, 9, 7).end()
				.face(Direction.NORTH).cullface(Direction.NORTH).uvs(7, 0, 9, 7).end()
				.face(Direction.WEST).uvs(0, 0, 7, 7).end()
				.face(Direction.EAST).uvs(9, 9, 16, 16).end().faces((direction, builder) -> builder.texture("#pane").emissivity(15, 15)).shade(false).end();

		BlockModelBuilder down_no_north = this.models().withExistingParent(baseName + "_down_no_north", "minecraft:block/block").texture("particle", "#pane").ao(false)
				.element().from(7, 0, 7).to(9, 7, 9).face(Direction.NORTH).uvs(7, 0, 9, 7).texture("#pane").emissivity(15, 15).end().shade(false).end();

		BlockModelBuilder north_no_down = this.models().withExistingParent(baseName + "_north_no_down", "minecraft:block/block").texture("particle", "#pane").ao(false)
				.element().from(7, 7, 0).to(9, 9, 7).face(Direction.DOWN).uvs(7, 0, 9, 7).texture("#pane").emissivity(15, 15).end().shade(false).end();
		//DOWN SOUTH
		BlockModelBuilder down_south = this.models().withExistingParent(baseName + "_down_south", "minecraft:block/block").texture("particle", "#pane").ao(false)
				.element().from(7, 0, 9).to(9, 7, 16)
				.face(Direction.DOWN).cullface(Direction.DOWN).uvs(7, 9, 9, 16).end()
				.face(Direction.SOUTH).cullface(Direction.SOUTH).uvs(7, 0, 9, 7).end()
				.face(Direction.WEST).uvs(9, 0, 16, 7).end()
				.face(Direction.EAST).uvs(0, 0, 7, 7).end().faces((direction, builder) -> builder.texture("#pane").emissivity(15, 15)).shade(false).end();

		BlockModelBuilder down_no_south = this.models().withExistingParent(baseName + "_down_no_south", "minecraft:block/block").texture("particle", "#pane").ao(false)
				.element().from(7, 0, 7).to(9, 7, 9).face(Direction.SOUTH).uvs(7, 0, 9, 7).texture("#pane").emissivity(15, 15).end().shade(false).end();

		BlockModelBuilder south_no_down = this.models().withExistingParent(baseName + "_south_no_down", "minecraft:block/block").texture("particle", "#pane").ao(false)
				.element().from(7, 7, 9).to(9, 9, 16).face(Direction.DOWN).uvs(7, 9, 9, 16).texture("#pane").emissivity(15, 15).end().shade(false).end();
		//UP WEST
		BlockModelBuilder up_west = this.models().withExistingParent(baseName + "_up_west", "minecraft:block/block").texture("particle", "#pane").ao(false)
				.element().from(0, 9, 7).to(7, 16, 9)
				.face(Direction.UP).cullface(Direction.UP).uvs(0, 7, 7, 9).end()
				.face(Direction.WEST).cullface(Direction.WEST).uvs(7, 9, 9, 16).end()
				.face(Direction.NORTH).uvs(0, 9, 7, 16).end()
				.face(Direction.SOUTH).uvs(9, 9, 16, 16).end().faces((direction, builder) -> builder.texture("#pane").emissivity(15, 15)).shade(false).end();

		BlockModelBuilder up_no_west = this.models().withExistingParent(baseName + "_up_no_west", "minecraft:block/block").texture("particle", "#pane").ao(false)
				.element().from(7, 9, 7).to(9, 16, 9).face(Direction.WEST).uvs(7, 9, 9, 16).texture("#pane").emissivity(15, 15).end().shade(false).end();

		BlockModelBuilder west_no_up = this.models().withExistingParent(baseName + "_west_no_up", "minecraft:block/block").texture("particle", "#pane").ao(false)
				.element().from(0, 7, 7).to(7, 9, 9).face(Direction.UP).uvs(0, 7, 7, 9).texture("#pane").emissivity(15, 15).end().shade(false).end();
		//UP EAST
		BlockModelBuilder up_east = this.models().withExistingParent(baseName + "_up_east", "minecraft:block/block").texture("particle", "#pane").ao(false)
				.element().from(9, 9, 7).to(16, 16, 9)
				.face(Direction.UP).cullface(Direction.UP).uvs(9, 7, 16, 9).end()
				.face(Direction.EAST).cullface(Direction.EAST).uvs(7, 9, 9, 16).end()
				.face(Direction.NORTH).uvs(9, 9, 16, 16).end()
				.face(Direction.SOUTH).uvs(0, 9, 7, 16).end().faces((direction, builder) -> builder.texture("#pane").emissivity(15, 15)).shade(false).end();

		BlockModelBuilder up_no_east = this.models().withExistingParent(baseName + "_up_no_east", "minecraft:block/block").texture("particle", "#pane").ao(false)
				.element().from(7, 9, 7).to(9, 16, 9).face(Direction.EAST).uvs(7, 9, 9, 16).texture("#pane").emissivity(15, 15).end().shade(false).end();

		BlockModelBuilder east_no_up = this.models().withExistingParent(baseName + "_east_no_up", "minecraft:block/block").texture("particle", "#pane").ao(false)
				.element().from(9, 7, 7).to(16, 9, 9).face(Direction.UP).uvs(9, 7, 16, 9).texture("#pane").emissivity(15, 15).end().shade(false).end();
		//UP NORTH
		BlockModelBuilder up_north = this.models().withExistingParent(baseName + "_up_north", "minecraft:block/block").texture("particle", "#pane").ao(false)
				.element().from(7, 9, 0).to(9, 16, 7)
				.face(Direction.UP).cullface(Direction.UP).uvs(7, 0, 9, 7).end()
				.face(Direction.NORTH).cullface(Direction.NORTH).uvs(7, 9, 9, 16).end()
				.face(Direction.WEST).uvs(0, 9, 7, 16).end()
				.face(Direction.EAST).uvs(9, 9, 16, 16).end().faces((direction, builder) -> builder.texture("#pane").emissivity(15, 15)).shade(false).end();

		BlockModelBuilder up_no_north = this.models().withExistingParent(baseName + "_up_no_north", "minecraft:block/block").texture("particle", "#pane").ao(false)
				.element().from(7, 9, 7).to(9, 16, 9).face(Direction.NORTH).uvs(7, 9, 9, 16).texture("#pane").emissivity(15, 15).end().shade(false).end();

		BlockModelBuilder north_no_up = this.models().withExistingParent(baseName + "_north_no_up", "minecraft:block/block").texture("particle", "#pane").ao(false)
				.element().from(7, 7, 0).to(9, 9, 7).face(Direction.UP).uvs(7, 0, 9, 7).texture("#pane").emissivity(15, 15).end().shade(false).end();
		//UP SOUTH
		BlockModelBuilder up_south = this.models().withExistingParent(baseName + "_up_south", "minecraft:block/block").texture("particle", "#pane").ao(false)
				.element().from(7, 9, 9).to(9, 16, 16)
				.face(Direction.UP).cullface(Direction.UP).uvs(7, 9, 9, 16).end()
				.face(Direction.SOUTH).cullface(Direction.SOUTH).uvs(7, 9, 9, 16).end()
				.face(Direction.WEST).uvs(9, 9, 16, 16).end()
				.face(Direction.EAST).uvs(0, 9, 7, 16).end().faces((direction, builder) -> builder.texture("#pane").emissivity(15, 15)).shade(false).end();

		BlockModelBuilder up_no_south = this.models().withExistingParent(baseName + "_up_no_south", "minecraft:block/block").texture("particle", "#pane").ao(false)
				.element().from(7, 9, 7).to(9, 16, 9).face(Direction.SOUTH).uvs(7, 9, 9, 16).texture("#pane").emissivity(15, 15).end().shade(false).end();

		BlockModelBuilder south_no_up = this.models().withExistingParent(baseName + "_south_no_up", "minecraft:block/block").texture("particle", "#pane").ao(false)
				.element().from(7, 7, 9).to(9, 9, 16).face(Direction.UP).uvs(7, 9, 9, 16).texture("#pane").emissivity(15, 15).end().shade(false).end();
		//NORTH WEST
		BlockModelBuilder north_west = this.models().withExistingParent(baseName + "_north_west", "minecraft:block/block").texture("particle", "#pane").ao(false)
				.element().from(0, 7, 0).to(7, 9, 7)
				.face(Direction.NORTH).cullface(Direction.NORTH).uvs(0, 7, 7, 9).end()
				.face(Direction.WEST).cullface(Direction.WEST).uvs(9, 7, 16, 9).end()
				.face(Direction.DOWN).uvs(0, 9, 7, 16).end()
				.face(Direction.UP).uvs(9, 9, 16, 16).end().faces((direction, builder) -> builder.texture("#pane").emissivity(15, 15)).shade(false).end();

		BlockModelBuilder north_no_west = this.models().withExistingParent(baseName + "_north_no_west", "minecraft:block/block").texture("particle", "#pane").ao(false)
				.element().from(7, 7, 0).to(9, 9, 7).face(Direction.WEST).uvs(9, 7, 16, 9).texture("#pane").emissivity(15, 15).end().shade(false).end();

		BlockModelBuilder west_no_north = this.models().withExistingParent(baseName + "_west_no_north", "minecraft:block/block").texture("particle", "#pane").ao(false)
				.element().from(0, 7, 7).to(7, 9, 9).face(Direction.NORTH).uvs(0, 7, 7, 9).texture("#pane").emissivity(15, 15).end().shade(false).end();
		//NORTH EAST
		BlockModelBuilder north_east = this.models().withExistingParent(baseName + "_north_east", "minecraft:block/block").texture("particle", "#pane").ao(false)
				.element().from(9, 7, 0).to(16, 9, 7)
				.face(Direction.NORTH).cullface(Direction.NORTH).uvs(9, 7, 16, 9).end()
				.face(Direction.EAST).cullface(Direction.EAST).uvs(0, 7, 7, 9).end()
				.face(Direction.DOWN).uvs(9, 9, 16, 16).end()
				.face(Direction.UP).uvs(0, 9, 7, 16).end().faces((direction, builder) -> builder.texture("#pane").emissivity(15, 15)).shade(false).end();

		BlockModelBuilder north_no_east = this.models().withExistingParent(baseName + "_north_no_east", "minecraft:block/block").texture("particle", "#pane").ao(false)
				.element().from(7, 7, 0).to(9, 9, 7).face(Direction.EAST).uvs(0, 7, 7, 9).texture("#pane").emissivity(15, 15).end().shade(false).end();

		BlockModelBuilder east_no_north = this.models().withExistingParent(baseName + "_east_no_north", "minecraft:block/block").texture("particle", "#pane").ao(false)
				.element().from(9, 7, 7).to(16, 9, 9).face(Direction.NORTH).uvs(9, 7, 16, 9).texture("#pane").emissivity(15, 15).end().shade(false).end();
		//SOUTH WEST
		BlockModelBuilder south_west = this.models().withExistingParent(baseName + "_south_west", "minecraft:block/block").texture("particle", "#pane").ao(false)
				.element().from(0, 7, 9).to(7, 9, 16)
				.face(Direction.SOUTH).cullface(Direction.SOUTH).uvs(0, 7, 7, 9).end()
				.face(Direction.WEST).cullface(Direction.WEST).uvs(9, 7, 16, 9).end()
				.face(Direction.DOWN).uvs(0, 9, 7, 16).end()
				.face(Direction.UP).uvs(9, 9, 16, 16).end().faces((direction, builder) -> builder.texture("#pane").emissivity(15, 15)).shade(false).end();

		BlockModelBuilder south_no_west = this.models().withExistingParent(baseName + "_south_no_west", "minecraft:block/block").texture("particle", "#pane").ao(false)
				.element().from(7, 7, 9).to(9, 9, 16).face(Direction.WEST).uvs(9, 7, 16, 9).texture("#pane").emissivity(15, 15).end().shade(false).end();

		BlockModelBuilder west_no_south = this.models().withExistingParent(baseName + "_west_no_south", "minecraft:block/block").texture("particle", "#pane").ao(false)
				.element().from(0, 7, 7).to(7, 9, 9).face(Direction.SOUTH).uvs(0, 7, 7, 9).texture("#pane").emissivity(15, 15).end().shade(false).end();
		//SOUTH EAST
		BlockModelBuilder south_east = this.models().withExistingParent(baseName + "_south_east", "minecraft:block/block").texture("particle", "#pane").ao(false)
				.element().from(9, 7, 9).to(16, 9, 16)
				.face(Direction.SOUTH).cullface(Direction.SOUTH).uvs(0, 7, 7, 9).end()
				.face(Direction.EAST).cullface(Direction.EAST).uvs(9, 7, 16, 9).end()
				.face(Direction.DOWN).uvs(9, 9, 16, 16).end()
				.face(Direction.UP).uvs(0, 9, 7, 16).end().faces((direction, builder) -> builder.texture("#pane").emissivity(15, 15)).shade(false).end();

		BlockModelBuilder south_no_east = this.models().withExistingParent(baseName + "_south_no_east", "minecraft:block/block").texture("particle", "#pane").ao(false)
				.element().from(7, 7, 9).to(9, 9, 16).face(Direction.EAST).uvs(9, 7, 16, 9).texture("#pane").emissivity(15, 15).end().shade(false).end();

		BlockModelBuilder east_no_south = this.models().withExistingParent(baseName + "_east_no_south", "minecraft:block/block").texture("particle", "#pane").ao(false)
				.element().from(9, 7, 7).to(16, 9, 9).face(Direction.SOUTH).uvs(0, 7, 7, 9).texture("#pane").emissivity(15, 15).end().shade(false).end();

		for (RegistryObject<Block> block : ImmutableList.of(TFBlocks.PINK_FORCE_FIELD, TFBlocks.BLUE_FORCE_FIELD, TFBlocks.GREEN_FORCE_FIELD, TFBlocks.VIOLET_FORCE_FIELD, TFBlocks.ORANGE_FORCE_FIELD)) {
			String blockName = "block/force_field/" + block.getId().getPath();
			ResourceLocation textureLocation = prefix("block/" + block.getId().getPath());

			ModelFile west_child = models().withExistingParent(blockName + "_west", west.getLocation()).renderType(TRANSLUCENT).texture("pane", textureLocation);
			ModelFile no_west_child = models().withExistingParent(blockName + "_no_west", no_west.getLocation()).renderType(TRANSLUCENT).texture("pane", textureLocation);
			ModelFile east_child = models().withExistingParent(blockName + "_east", east.getLocation()).renderType(TRANSLUCENT).texture("pane", textureLocation);
			ModelFile no_east_child = models().withExistingParent(blockName + "_no_east", no_east.getLocation()).renderType(TRANSLUCENT).texture("pane", textureLocation);
			ModelFile down_child = models().withExistingParent(blockName + "_down", down.getLocation()).renderType(TRANSLUCENT).texture("pane", textureLocation);
			ModelFile no_down_child = models().withExistingParent(blockName + "_no_down", no_down.getLocation()).renderType(TRANSLUCENT).texture("pane", textureLocation);
			ModelFile up_child = models().withExistingParent(blockName + "_up", up.getLocation()).renderType(TRANSLUCENT).texture("pane", textureLocation);
			ModelFile no_up_child = models().withExistingParent(blockName + "_no_up", no_up.getLocation()).renderType(TRANSLUCENT).texture("pane", textureLocation);
			ModelFile north_child = models().withExistingParent(blockName + "_north", north.getLocation()).renderType(TRANSLUCENT).texture("pane", textureLocation);
			ModelFile no_north_child = models().withExistingParent(blockName + "_no_north", no_north.getLocation()).renderType(TRANSLUCENT).texture("pane", textureLocation);
			ModelFile south_child = models().withExistingParent(blockName + "_south", south.getLocation()).renderType(TRANSLUCENT).texture("pane", textureLocation);
			ModelFile no_south_child = models().withExistingParent(blockName + "_no_south", no_south.getLocation()).renderType(TRANSLUCENT).texture("pane", textureLocation);
			ModelFile down_west_child = models().withExistingParent(blockName + "_down_west", down_west.getLocation()).renderType(TRANSLUCENT).texture("pane", textureLocation);
			ModelFile down_no_west_child = models().withExistingParent(blockName + "_down_no_west", down_no_west.getLocation()).renderType(TRANSLUCENT).texture("pane", textureLocation);
			ModelFile west_no_down_child = models().withExistingParent(blockName + "_west_no_down", west_no_down.getLocation()).renderType(TRANSLUCENT).texture("pane", textureLocation);
			ModelFile down_east_child = models().withExistingParent(blockName + "_down_east", down_east.getLocation()).renderType(TRANSLUCENT).texture("pane", textureLocation);
			ModelFile down_no_east_child = models().withExistingParent(blockName + "_down_no_east", down_no_east.getLocation()).renderType(TRANSLUCENT).texture("pane", textureLocation);
			ModelFile east_no_down_child = models().withExistingParent(blockName + "_east_no_down", east_no_down.getLocation()).renderType(TRANSLUCENT).texture("pane", textureLocation);
			ModelFile down_north_child = models().withExistingParent(blockName + "_down_north", down_north.getLocation()).renderType(TRANSLUCENT).texture("pane", textureLocation);
			ModelFile down_no_north_child = models().withExistingParent(blockName + "_down_no_north", down_no_north.getLocation()).renderType(TRANSLUCENT).texture("pane", textureLocation);
			ModelFile north_no_down_child = models().withExistingParent(blockName + "_north_no_down", north_no_down.getLocation()).renderType(TRANSLUCENT).texture("pane", textureLocation);
			ModelFile down_south_child = models().withExistingParent(blockName + "_down_south", down_south.getLocation()).renderType(TRANSLUCENT).texture("pane", textureLocation);
			ModelFile down_no_south_child = models().withExistingParent(blockName + "_down_no_south", down_no_south.getLocation()).renderType(TRANSLUCENT).texture("pane", textureLocation);
			ModelFile south_no_down_child = models().withExistingParent(blockName + "_south_no_down", south_no_down.getLocation()).renderType(TRANSLUCENT).texture("pane", textureLocation);
			ModelFile up_west_child = models().withExistingParent(blockName + "_up_west", up_west.getLocation()).renderType(TRANSLUCENT).texture("pane", textureLocation);
			ModelFile up_no_west_child = models().withExistingParent(blockName + "_up_no_west", up_no_west.getLocation()).renderType(TRANSLUCENT).texture("pane", textureLocation);
			ModelFile west_no_up_child = models().withExistingParent(blockName + "_west_no_up", west_no_up.getLocation()).renderType(TRANSLUCENT).texture("pane", textureLocation);
			ModelFile up_east_child = models().withExistingParent(blockName + "_up_east", up_east.getLocation()).renderType(TRANSLUCENT).texture("pane", textureLocation);
			ModelFile up_no_east_child = models().withExistingParent(blockName + "_up_no_east", up_no_east.getLocation()).renderType(TRANSLUCENT).texture("pane", textureLocation);
			ModelFile east_no_up_child = models().withExistingParent(blockName + "_east_no_up", east_no_up.getLocation()).renderType(TRANSLUCENT).texture("pane", textureLocation);
			ModelFile up_north_child = models().withExistingParent(blockName + "_up_north", up_north.getLocation()).renderType(TRANSLUCENT).texture("pane", textureLocation);
			ModelFile up_no_north_child = models().withExistingParent(blockName + "_up_no_north", up_no_north.getLocation()).renderType(TRANSLUCENT).texture("pane", textureLocation);
			ModelFile north_no_up_child = models().withExistingParent(blockName + "_north_no_up", north_no_up.getLocation()).renderType(TRANSLUCENT).texture("pane", textureLocation);
			ModelFile up_south_child = models().withExistingParent(blockName + "_up_south", up_south.getLocation()).renderType(TRANSLUCENT).texture("pane", textureLocation);
			ModelFile up_no_south_child = models().withExistingParent(blockName + "_up_no_south", up_no_south.getLocation()).renderType(TRANSLUCENT).texture("pane", textureLocation);
			ModelFile south_no_up_child = models().withExistingParent(blockName + "_south_no_up", south_no_up.getLocation()).renderType(TRANSLUCENT).texture("pane", textureLocation);
			ModelFile north_west_child = models().withExistingParent(blockName + "_north_west", north_west.getLocation()).renderType(TRANSLUCENT).texture("pane", textureLocation);
			ModelFile north_no_west_child = models().withExistingParent(blockName + "_north_no_west", north_no_west.getLocation()).renderType(TRANSLUCENT).texture("pane", textureLocation);
			ModelFile west_no_north_child = models().withExistingParent(blockName + "_west_no_north", west_no_north.getLocation()).renderType(TRANSLUCENT).texture("pane", textureLocation);
			ModelFile north_east_child = models().withExistingParent(blockName + "_north_east", north_east.getLocation()).renderType(TRANSLUCENT).texture("pane", textureLocation);
			ModelFile north_no_east_child = models().withExistingParent(blockName + "_north_no_east", north_no_east.getLocation()).renderType(TRANSLUCENT).texture("pane", textureLocation);
			ModelFile east_no_north_child = models().withExistingParent(blockName + "_east_no_north", east_no_north.getLocation()).renderType(TRANSLUCENT).texture("pane", textureLocation);
			ModelFile south_west_child = models().withExistingParent(blockName + "_south_west", south_west.getLocation()).renderType(TRANSLUCENT).texture("pane", textureLocation);
			ModelFile south_no_west_child = models().withExistingParent(blockName + "_south_no_west", south_no_west.getLocation()).renderType(TRANSLUCENT).texture("pane", textureLocation);
			ModelFile west_no_south_child = models().withExistingParent(blockName + "_west_no_south", west_no_south.getLocation()).renderType(TRANSLUCENT).texture("pane", textureLocation);
			ModelFile south_east_child = models().withExistingParent(blockName + "_south_east", south_east.getLocation()).renderType(TRANSLUCENT).texture("pane", textureLocation);
			ModelFile south_no_east_child = models().withExistingParent(blockName + "_south_no_east", south_no_east.getLocation()).renderType(TRANSLUCENT).texture("pane", textureLocation);
			ModelFile east_no_south_child = models().withExistingParent(blockName + "_east_no_south", east_no_south.getLocation()).renderType(TRANSLUCENT).texture("pane", textureLocation);

			MultiPartBlockStateBuilder builder = getMultipartBuilder(block.get())
					.part().modelFile(west_child).uvLock(true).addModel().condition(PipeBlock.WEST, true).end()
					.part().modelFile(no_west_child).uvLock(true).addModel().condition(PipeBlock.WEST, false).end()
					.part().modelFile(east_child).uvLock(true).addModel().condition(PipeBlock.EAST, true).end()
					.part().modelFile(no_east_child).uvLock(true).addModel().condition(PipeBlock.EAST, false).end()
					.part().modelFile(down_child).uvLock(true).addModel().condition(PipeBlock.DOWN, true).end()
					.part().modelFile(no_down_child).uvLock(true).addModel().condition(PipeBlock.DOWN, false).end()
					.part().modelFile(up_child).uvLock(true).addModel().condition(PipeBlock.UP, true).end()
					.part().modelFile(no_up_child).uvLock(true).addModel().condition(PipeBlock.UP, false).end()
					.part().modelFile(north_child).uvLock(true).addModel().condition(PipeBlock.NORTH, true).end()
					.part().modelFile(no_north_child).uvLock(true).addModel().condition(PipeBlock.NORTH, false).end()
					.part().modelFile(south_child).uvLock(true).addModel().condition(PipeBlock.SOUTH, true).end()
					.part().modelFile(no_south_child).uvLock(true).addModel().condition(PipeBlock.SOUTH, false).end();

			builder = builder.part().modelFile(down_west_child).uvLock(true).addModel()
					.nestedGroup().condition(PipeBlock.DOWN, true).condition(PipeBlock.WEST, true).condition(PipeBlock.NORTH, false).end().useOr()
					.nestedGroup().condition(PipeBlock.DOWN, true).condition(PipeBlock.WEST, true).condition(PipeBlock.SOUTH, false).end().useOr()
					.nestedGroup().condition(PipeBlock.DOWN, true).condition(PipeBlock.WEST, true).condition(BlockStateProperties.AXIS, Direction.Axis.Y, Direction.Axis.X).end().end();

			builder = builder.part().modelFile(down_no_west_child).uvLock(true).addModel()
					.nestedGroup().condition(PipeBlock.DOWN, true).condition(PipeBlock.WEST, false).end().useOr()
					.nestedGroup().condition(PipeBlock.DOWN, true).condition(PipeBlock.WEST, true).condition(PipeBlock.NORTH, true).condition(PipeBlock.SOUTH, true).condition(BlockStateProperties.AXIS, Direction.Axis.Z).end().useOr()
					.nestedGroup().condition(PipeBlock.DOWN, true).condition(PipeBlock.WEST, true).condition(BlockStateProperties.AXIS, Direction.Axis.Z).end().end();

			builder = builder.part().modelFile(west_no_down_child).uvLock(true).addModel()
					.nestedGroup().condition(PipeBlock.DOWN, false).condition(PipeBlock.WEST, true).end().useOr()
					.nestedGroup().condition(PipeBlock.DOWN, true).condition(PipeBlock.WEST, true).condition(PipeBlock.NORTH, true).condition(PipeBlock.SOUTH, true).condition(BlockStateProperties.AXIS, Direction.Axis.Z).end().useOr()
					.nestedGroup().condition(PipeBlock.DOWN, true).condition(PipeBlock.WEST, true).condition(BlockStateProperties.AXIS, Direction.Axis.Z).end().end();

			builder = builder.part().modelFile(down_east_child).uvLock(true).addModel()
					.nestedGroup().condition(PipeBlock.DOWN, true).condition(PipeBlock.EAST, true).condition(PipeBlock.NORTH, false).end().useOr()
					.nestedGroup().condition(PipeBlock.DOWN, true).condition(PipeBlock.EAST, true).condition(PipeBlock.SOUTH, false).end().useOr()
					.nestedGroup().condition(PipeBlock.DOWN, true).condition(PipeBlock.EAST, true).condition(BlockStateProperties.AXIS, Direction.Axis.Y, Direction.Axis.X).end().end();

			builder = builder.part().modelFile(down_no_east_child).uvLock(true).addModel()
					.nestedGroup().condition(PipeBlock.DOWN, true).condition(PipeBlock.EAST, false).end().useOr()
					.nestedGroup().condition(PipeBlock.DOWN, true).condition(PipeBlock.EAST, true).condition(PipeBlock.NORTH, true).condition(PipeBlock.SOUTH, true).condition(BlockStateProperties.AXIS, Direction.Axis.Z).end().useOr()
					.nestedGroup().condition(PipeBlock.DOWN, true).condition(PipeBlock.EAST, true).condition(BlockStateProperties.AXIS, Direction.Axis.Z).end().end();

			builder = builder.part().modelFile(east_no_down_child).uvLock(true).addModel()
					.nestedGroup().condition(PipeBlock.DOWN, false).condition(PipeBlock.EAST, true).end().useOr()
					.nestedGroup().condition(PipeBlock.DOWN, true).condition(PipeBlock.EAST, true).condition(PipeBlock.NORTH, true).condition(PipeBlock.SOUTH, true).condition(BlockStateProperties.AXIS, Direction.Axis.Z).end().useOr()
					.nestedGroup().condition(PipeBlock.DOWN, true).condition(PipeBlock.EAST, true).condition(BlockStateProperties.AXIS, Direction.Axis.Z).end().end();

			builder = builder.part().modelFile(down_north_child).uvLock(true).addModel()
					.nestedGroup().condition(PipeBlock.DOWN, true).condition(PipeBlock.NORTH, true).condition(PipeBlock.WEST, false).end().useOr()
					.nestedGroup().condition(PipeBlock.DOWN, true).condition(PipeBlock.NORTH, true).condition(PipeBlock.EAST, false).end().useOr()
					.nestedGroup().condition(PipeBlock.DOWN, true).condition(PipeBlock.NORTH, true).condition(BlockStateProperties.AXIS, Direction.Axis.Y, Direction.Axis.Z).end().end();

			builder = builder.part().modelFile(down_no_north_child).uvLock(true).addModel()
					.nestedGroup().condition(PipeBlock.DOWN, true).condition(PipeBlock.NORTH, false).end().useOr()
					.nestedGroup().condition(PipeBlock.DOWN, true).condition(PipeBlock.NORTH, true).condition(PipeBlock.WEST, true).condition(PipeBlock.EAST, true).condition(BlockStateProperties.AXIS, Direction.Axis.X).end().useOr()
					.nestedGroup().condition(PipeBlock.DOWN, true).condition(PipeBlock.NORTH, true).condition(BlockStateProperties.AXIS, Direction.Axis.X).end().end();

			builder = builder.part().modelFile(north_no_down_child).uvLock(true).addModel()
					.nestedGroup().condition(PipeBlock.DOWN, false).condition(PipeBlock.NORTH, true).end().useOr()
					.nestedGroup().condition(PipeBlock.DOWN, true).condition(PipeBlock.NORTH, true).condition(PipeBlock.WEST, true).condition(PipeBlock.EAST, true).condition(BlockStateProperties.AXIS, Direction.Axis.X).end().useOr()
					.nestedGroup().condition(PipeBlock.DOWN, true).condition(PipeBlock.NORTH, true).condition(BlockStateProperties.AXIS, Direction.Axis.X).end().end();

			builder = builder.part().modelFile(down_south_child).uvLock(true).addModel()
					.nestedGroup().condition(PipeBlock.DOWN, true).condition(PipeBlock.SOUTH, true).condition(PipeBlock.WEST, false).end().useOr()
					.nestedGroup().condition(PipeBlock.DOWN, true).condition(PipeBlock.SOUTH, true).condition(PipeBlock.EAST, false).end().useOr()
					.nestedGroup().condition(PipeBlock.DOWN, true).condition(PipeBlock.SOUTH, true).condition(BlockStateProperties.AXIS, Direction.Axis.Y, Direction.Axis.Z).end().end();

			builder = builder.part().modelFile(down_no_south_child).uvLock(true).addModel()
					.nestedGroup().condition(PipeBlock.DOWN, true).condition(PipeBlock.SOUTH, false).end().useOr()
					.nestedGroup().condition(PipeBlock.DOWN, true).condition(PipeBlock.SOUTH, true).condition(PipeBlock.WEST, true).condition(PipeBlock.EAST, true).condition(BlockStateProperties.AXIS, Direction.Axis.X).end().useOr()
					.nestedGroup().condition(PipeBlock.DOWN, true).condition(PipeBlock.SOUTH, true).condition(BlockStateProperties.AXIS, Direction.Axis.X).end().end();

			builder = builder.part().modelFile(south_no_down_child).uvLock(true).addModel()
					.nestedGroup().condition(PipeBlock.DOWN, false).condition(PipeBlock.SOUTH, true).end().useOr()
					.nestedGroup().condition(PipeBlock.DOWN, true).condition(PipeBlock.SOUTH, true).condition(PipeBlock.WEST, true).condition(PipeBlock.EAST, true).condition(BlockStateProperties.AXIS, Direction.Axis.X).end().useOr()
					.nestedGroup().condition(PipeBlock.DOWN, true).condition(PipeBlock.SOUTH, true).condition(BlockStateProperties.AXIS, Direction.Axis.X).end().end();

			builder = builder.part().modelFile(up_west_child).uvLock(true).addModel()
					.nestedGroup().condition(PipeBlock.UP, true).condition(PipeBlock.WEST, true).condition(PipeBlock.NORTH, false).end().useOr()
					.nestedGroup().condition(PipeBlock.UP, true).condition(PipeBlock.WEST, true).condition(PipeBlock.SOUTH, false).end().useOr()
					.nestedGroup().condition(PipeBlock.UP, true).condition(PipeBlock.WEST, true).condition(BlockStateProperties.AXIS, Direction.Axis.Y, Direction.Axis.X).end().end();

			builder = builder.part().modelFile(up_no_west_child).uvLock(true).addModel()
					.nestedGroup().condition(PipeBlock.UP, true).condition(PipeBlock.WEST, false).end().useOr()
					.nestedGroup().condition(PipeBlock.UP, true).condition(PipeBlock.WEST, true).condition(PipeBlock.NORTH, true).condition(PipeBlock.SOUTH, true).condition(BlockStateProperties.AXIS, Direction.Axis.Z).end().useOr()
					.nestedGroup().condition(PipeBlock.UP, true).condition(PipeBlock.WEST, true).condition(BlockStateProperties.AXIS, Direction.Axis.Z).end().end();

			builder = builder.part().modelFile(west_no_up_child).uvLock(true).addModel()
					.nestedGroup().condition(PipeBlock.UP, false).condition(PipeBlock.WEST, true).end().useOr()
					.nestedGroup().condition(PipeBlock.UP, true).condition(PipeBlock.WEST, true).condition(PipeBlock.NORTH, true).condition(PipeBlock.SOUTH, true).condition(BlockStateProperties.AXIS, Direction.Axis.Z).end().useOr()
					.nestedGroup().condition(PipeBlock.UP, true).condition(PipeBlock.WEST, true).condition(BlockStateProperties.AXIS, Direction.Axis.Z).end().end();

			builder = builder.part().modelFile(up_east_child).uvLock(true).addModel()
					.nestedGroup().condition(PipeBlock.UP, true).condition(PipeBlock.EAST, true).condition(PipeBlock.NORTH, false).end().useOr()
					.nestedGroup().condition(PipeBlock.UP, true).condition(PipeBlock.EAST, true).condition(PipeBlock.SOUTH, false).end().useOr()
					.nestedGroup().condition(PipeBlock.UP, true).condition(PipeBlock.EAST, true).condition(BlockStateProperties.AXIS, Direction.Axis.Y, Direction.Axis.X).end().end();

			builder = builder.part().modelFile(up_no_east_child).uvLock(true).addModel()
					.nestedGroup().condition(PipeBlock.UP, true).condition(PipeBlock.EAST, false).end().useOr()
					.nestedGroup().condition(PipeBlock.UP, true).condition(PipeBlock.EAST, true).condition(PipeBlock.NORTH, true).condition(PipeBlock.SOUTH, true).condition(BlockStateProperties.AXIS, Direction.Axis.Z).end().useOr()
					.nestedGroup().condition(PipeBlock.UP, true).condition(PipeBlock.EAST, true).condition(BlockStateProperties.AXIS, Direction.Axis.Z).end().end();

			builder = builder.part().modelFile(east_no_up_child).uvLock(true).addModel()
					.nestedGroup().condition(PipeBlock.UP, false).condition(PipeBlock.EAST, true).end().useOr()
					.nestedGroup().condition(PipeBlock.UP, true).condition(PipeBlock.EAST, true).condition(PipeBlock.NORTH, true).condition(PipeBlock.SOUTH, true).condition(BlockStateProperties.AXIS, Direction.Axis.Z).end().useOr()
					.nestedGroup().condition(PipeBlock.UP, true).condition(PipeBlock.EAST, true).condition(BlockStateProperties.AXIS, Direction.Axis.Z).end().end();

			builder = builder.part().modelFile(up_north_child).uvLock(true).addModel()
					.nestedGroup().condition(PipeBlock.UP, true).condition(PipeBlock.NORTH, true).condition(PipeBlock.WEST, false).end().useOr()
					.nestedGroup().condition(PipeBlock.UP, true).condition(PipeBlock.NORTH, true).condition(PipeBlock.EAST, false).end().useOr()
					.nestedGroup().condition(PipeBlock.UP, true).condition(PipeBlock.NORTH, true).condition(BlockStateProperties.AXIS, Direction.Axis.Y, Direction.Axis.Z).end().end();

			builder = builder.part().modelFile(up_no_north_child).uvLock(true).addModel()
					.nestedGroup().condition(PipeBlock.UP, true).condition(PipeBlock.NORTH, false).end().useOr()
					.nestedGroup().condition(PipeBlock.UP, true).condition(PipeBlock.NORTH, true).condition(PipeBlock.WEST, true).condition(PipeBlock.EAST, true).condition(BlockStateProperties.AXIS, Direction.Axis.X).end().useOr()
					.nestedGroup().condition(PipeBlock.UP, true).condition(PipeBlock.NORTH, true).condition(BlockStateProperties.AXIS, Direction.Axis.X).end().end();

			builder = builder.part().modelFile(north_no_up_child).uvLock(true).addModel()
					.nestedGroup().condition(PipeBlock.UP, false).condition(PipeBlock.NORTH, true).end().useOr()
					.nestedGroup().condition(PipeBlock.UP, true).condition(PipeBlock.NORTH, true).condition(PipeBlock.WEST, true).condition(PipeBlock.EAST, true).condition(BlockStateProperties.AXIS, Direction.Axis.X).end().useOr()
					.nestedGroup().condition(PipeBlock.UP, true).condition(PipeBlock.NORTH, true).condition(BlockStateProperties.AXIS, Direction.Axis.X).end().end();

			builder = builder.part().modelFile(up_south_child).uvLock(true).addModel()
					.nestedGroup().condition(PipeBlock.UP, true).condition(PipeBlock.SOUTH, true).condition(PipeBlock.WEST, false).end().useOr()
					.nestedGroup().condition(PipeBlock.UP, true).condition(PipeBlock.SOUTH, true).condition(PipeBlock.EAST, false).end().useOr()
					.nestedGroup().condition(PipeBlock.UP, true).condition(PipeBlock.SOUTH, true).condition(BlockStateProperties.AXIS, Direction.Axis.Y, Direction.Axis.Z).end().end();

			builder = builder.part().modelFile(up_no_south_child).uvLock(true).addModel()
					.nestedGroup().condition(PipeBlock.UP, true).condition(PipeBlock.SOUTH, false).end().useOr()
					.nestedGroup().condition(PipeBlock.UP, true).condition(PipeBlock.SOUTH, true).condition(PipeBlock.WEST, true).condition(PipeBlock.EAST, true).condition(BlockStateProperties.AXIS, Direction.Axis.X).end().useOr()
					.nestedGroup().condition(PipeBlock.UP, true).condition(PipeBlock.SOUTH, true).condition(BlockStateProperties.AXIS, Direction.Axis.X).end().end();

			builder = builder.part().modelFile(south_no_up_child).uvLock(true).addModel()
					.nestedGroup().condition(PipeBlock.UP, false).condition(PipeBlock.SOUTH, true).end().useOr()
					.nestedGroup().condition(PipeBlock.UP, true).condition(PipeBlock.SOUTH, true).condition(PipeBlock.WEST, true).condition(PipeBlock.EAST, true).condition(BlockStateProperties.AXIS, Direction.Axis.X).end().useOr()
					.nestedGroup().condition(PipeBlock.UP, true).condition(PipeBlock.SOUTH, true).condition(BlockStateProperties.AXIS, Direction.Axis.X).end().end();

			builder = builder.part().modelFile(north_west_child).uvLock(true).addModel()
					.nestedGroup().condition(PipeBlock.NORTH, true).condition(PipeBlock.WEST, true).condition(PipeBlock.DOWN, false).end().useOr()
					.nestedGroup().condition(PipeBlock.NORTH, true).condition(PipeBlock.WEST, true).condition(PipeBlock.UP, false).end().useOr()
					.nestedGroup().condition(PipeBlock.NORTH, true).condition(PipeBlock.WEST, true).condition(BlockStateProperties.AXIS, Direction.Axis.X, Direction.Axis.Z).end().end();

			builder = builder.part().modelFile(north_no_west_child).uvLock(true).addModel()
					.nestedGroup().condition(PipeBlock.NORTH, true).condition(PipeBlock.WEST, false).end().useOr()
					.nestedGroup().condition(PipeBlock.NORTH, true).condition(PipeBlock.WEST, true).condition(PipeBlock.DOWN, true).condition(PipeBlock.UP, true).condition(BlockStateProperties.AXIS, Direction.Axis.Y).end().useOr()
					.nestedGroup().condition(PipeBlock.NORTH, true).condition(PipeBlock.WEST, true).condition(BlockStateProperties.AXIS, Direction.Axis.Y).end().end();

			builder = builder.part().modelFile(west_no_north_child).uvLock(true).addModel()
					.nestedGroup().condition(PipeBlock.NORTH, false).condition(PipeBlock.WEST, true).end().useOr()
					.nestedGroup().condition(PipeBlock.NORTH, true).condition(PipeBlock.WEST, true).condition(PipeBlock.DOWN, true).condition(PipeBlock.UP, true).condition(BlockStateProperties.AXIS, Direction.Axis.Y).end().useOr()
					.nestedGroup().condition(PipeBlock.NORTH, true).condition(PipeBlock.WEST, true).condition(BlockStateProperties.AXIS, Direction.Axis.Y).end().end();

			builder = builder.part().modelFile(north_east_child).uvLock(true).addModel()
					.nestedGroup().condition(PipeBlock.NORTH, true).condition(PipeBlock.EAST, true).condition(PipeBlock.DOWN, false).end().useOr()
					.nestedGroup().condition(PipeBlock.NORTH, true).condition(PipeBlock.EAST, true).condition(PipeBlock.UP, false).end().useOr()
					.nestedGroup().condition(PipeBlock.NORTH, true).condition(PipeBlock.EAST, true).condition(BlockStateProperties.AXIS, Direction.Axis.X, Direction.Axis.Z).end().end();

			builder = builder.part().modelFile(north_no_east_child).uvLock(true).addModel()
					.nestedGroup().condition(PipeBlock.NORTH, true).condition(PipeBlock.EAST, false).end().useOr()
					.nestedGroup().condition(PipeBlock.NORTH, true).condition(PipeBlock.EAST, true).condition(PipeBlock.DOWN, true).condition(PipeBlock.UP, true).condition(BlockStateProperties.AXIS, Direction.Axis.Y).end().useOr()
					.nestedGroup().condition(PipeBlock.NORTH, true).condition(PipeBlock.EAST, true).condition(BlockStateProperties.AXIS, Direction.Axis.Y).end().end();

			builder = builder.part().modelFile(east_no_north_child).uvLock(true).addModel()
					.nestedGroup().condition(PipeBlock.NORTH, false).condition(PipeBlock.EAST, true).end().useOr()
					.nestedGroup().condition(PipeBlock.NORTH, true).condition(PipeBlock.EAST, true).condition(PipeBlock.DOWN, true).condition(PipeBlock.UP, true).condition(BlockStateProperties.AXIS, Direction.Axis.Y).end().useOr()
					.nestedGroup().condition(PipeBlock.NORTH, true).condition(PipeBlock.EAST, true).condition(BlockStateProperties.AXIS, Direction.Axis.Y).end().end();

			builder = builder.part().modelFile(south_west_child).uvLock(true).addModel()
					.nestedGroup().condition(PipeBlock.SOUTH, true).condition(PipeBlock.WEST, true).condition(PipeBlock.DOWN, false).end().useOr()
					.nestedGroup().condition(PipeBlock.SOUTH, true).condition(PipeBlock.WEST, true).condition(PipeBlock.UP, false).end().useOr()
					.nestedGroup().condition(PipeBlock.SOUTH, true).condition(PipeBlock.WEST, true).condition(BlockStateProperties.AXIS, Direction.Axis.X, Direction.Axis.Z).end().end();

			builder = builder.part().modelFile(south_no_west_child).uvLock(true).addModel()
					.nestedGroup().condition(PipeBlock.SOUTH, true).condition(PipeBlock.WEST, false).end().useOr()
					.nestedGroup().condition(PipeBlock.SOUTH, true).condition(PipeBlock.WEST, true).condition(PipeBlock.DOWN, true).condition(PipeBlock.UP, true).condition(BlockStateProperties.AXIS, Direction.Axis.Y).end().useOr()
					.nestedGroup().condition(PipeBlock.SOUTH, true).condition(PipeBlock.WEST, true).condition(BlockStateProperties.AXIS, Direction.Axis.Y).end().end();

			builder = builder.part().modelFile(west_no_south_child).uvLock(true).addModel()
					.nestedGroup().condition(PipeBlock.SOUTH, false).condition(PipeBlock.WEST, true).end().useOr()
					.nestedGroup().condition(PipeBlock.SOUTH, true).condition(PipeBlock.WEST, true).condition(PipeBlock.DOWN, true).condition(PipeBlock.UP, true).condition(BlockStateProperties.AXIS, Direction.Axis.Y).end().useOr()
					.nestedGroup().condition(PipeBlock.SOUTH, true).condition(PipeBlock.WEST, true).condition(BlockStateProperties.AXIS, Direction.Axis.Y).end().end();

			builder = builder.part().modelFile(south_east_child).uvLock(true).addModel()
					.nestedGroup().condition(PipeBlock.SOUTH, true).condition(PipeBlock.EAST, true).condition(PipeBlock.DOWN, false).end().useOr()
					.nestedGroup().condition(PipeBlock.SOUTH, true).condition(PipeBlock.EAST, true).condition(PipeBlock.UP, false).end().useOr()
					.nestedGroup().condition(PipeBlock.SOUTH, true).condition(PipeBlock.EAST, true).condition(BlockStateProperties.AXIS, Direction.Axis.X, Direction.Axis.Z).end().end();

			builder = builder.part().modelFile(south_no_east_child).uvLock(true).addModel()
					.nestedGroup().condition(PipeBlock.SOUTH, true).condition(PipeBlock.EAST, false).end().useOr()
					.nestedGroup().condition(PipeBlock.SOUTH, true).condition(PipeBlock.EAST, true).condition(PipeBlock.DOWN, true).condition(PipeBlock.UP, true).condition(BlockStateProperties.AXIS, Direction.Axis.Y).end().useOr()
					.nestedGroup().condition(PipeBlock.SOUTH, true).condition(PipeBlock.EAST, true).condition(BlockStateProperties.AXIS, Direction.Axis.Y).end().end();

			builder.part().modelFile(east_no_south_child).uvLock(true).addModel()
					.nestedGroup().condition(PipeBlock.SOUTH, false).condition(PipeBlock.EAST, true).end().useOr()
					.nestedGroup().condition(PipeBlock.SOUTH, true).condition(PipeBlock.EAST, true).condition(PipeBlock.DOWN, true).condition(PipeBlock.UP, true).condition(BlockStateProperties.AXIS, Direction.Axis.Y).end().useOr()
					.nestedGroup().condition(PipeBlock.SOUTH, true).condition(PipeBlock.EAST, true).condition(BlockStateProperties.AXIS, Direction.Axis.Y).end().end();
		}
	}

	private void registerNagastone() {
		// todo 1.15 cleanup: generate these models as well instead of getExistingFile-ing them
		String baseName = TFBlocks.NAGASTONE.getId().getPath();
		ModelFile solid = models().cubeBottomTop(baseName, prefix("block/nagastone_long_side"), prefix("block/nagastone_bottom_long"), prefix("block/nagastone_turn_top"));
		ModelFile down = models().getExistingFile(prefix("block/naga_segment/down"));
		ModelFile up = models().getExistingFile(prefix("block/naga_segment/up"));
		ModelFile horizontal = models().getExistingFile(prefix("block/naga_segment/horizontal"));
		ModelFile vertical = models().getExistingFile(prefix("block/naga_segment/vertical"));
		getVariantBuilder(TFBlocks.NAGASTONE.get()).forAllStates(s -> switch (s.getValue(NagastoneBlock.VARIANT)) {
			case NORTH_DOWN -> ConfiguredModel.builder().modelFile(down).rotationY(270).build();
			case SOUTH_DOWN -> ConfiguredModel.builder().modelFile(down).rotationY(90).build();
			case WEST_DOWN -> ConfiguredModel.builder().modelFile(down).rotationY(180).build();
			case EAST_DOWN -> ConfiguredModel.builder().modelFile(down).build();
			case NORTH_UP -> ConfiguredModel.builder().modelFile(up).rotationY(270).build();
			case SOUTH_UP -> ConfiguredModel.builder().modelFile(up).rotationY(90).build();
			case EAST_UP -> ConfiguredModel.builder().modelFile(up).build();
			case WEST_UP -> ConfiguredModel.builder().modelFile(up).rotationY(180).build();
			case AXIS_X -> ConfiguredModel.builder().modelFile(horizontal).build();
			case AXIS_Y -> ConfiguredModel.builder().modelFile(vertical).build();
			case AXIS_Z -> ConfiguredModel.builder().modelFile(horizontal).rotationY(90).build();
			case SOLID -> ConfiguredModel.builder().modelFile(solid).build();
		});

		horizontalBlock(TFBlocks.NAGASTONE_HEAD.get(), models().getExistingFile(prefix("block/" + TFBlocks.NAGASTONE_HEAD.getId().getPath())));
		nagastonePillar(TFBlocks.NAGASTONE_PILLAR.get(), "");
		nagastonePillar(TFBlocks.MOSSY_NAGASTONE_PILLAR.get(), "_mossy");
		nagastonePillar(TFBlocks.CRACKED_NAGASTONE_PILLAR.get(), "_weathered");
		etchedNagastone(TFBlocks.ETCHED_NAGASTONE.get(), "");
		etchedNagastone(TFBlocks.MOSSY_ETCHED_NAGASTONE.get(), "_mossy");
		etchedNagastone(TFBlocks.CRACKED_ETCHED_NAGASTONE.get(), "_weathered");

		bisectedStairsBlock(TFBlocks.NAGASTONE_STAIRS_LEFT, prefix("block/etched_nagastone_left"), prefix("block/stone_tiles"), prefix("block/nagastone_bare"));
		bisectedStairsBlock(TFBlocks.NAGASTONE_STAIRS_RIGHT, prefix("block/etched_nagastone_right"), prefix("block/stone_tiles"), prefix("block/nagastone_bare"));
		bisectedStairsBlock(TFBlocks.MOSSY_NAGASTONE_STAIRS_LEFT, prefix("block/etched_nagastone_left_mossy"), prefix("block/stone_tiles_mossy"), prefix("block/nagastone_bare_mossy"));
		bisectedStairsBlock(TFBlocks.MOSSY_NAGASTONE_STAIRS_RIGHT, prefix("block/etched_nagastone_right_mossy"), prefix("block/stone_tiles_mossy"), prefix("block/nagastone_bare_mossy"));
		bisectedStairsBlock(TFBlocks.CRACKED_NAGASTONE_STAIRS_LEFT, prefix("block/etched_nagastone_left_weathered"), prefix("block/stone_tiles_weathered"), prefix("block/nagastone_bare_weathered"));
		bisectedStairsBlock(TFBlocks.CRACKED_NAGASTONE_STAIRS_RIGHT, prefix("block/etched_nagastone_right_weathered"), prefix("block/stone_tiles_weathered"), prefix("block/nagastone_bare_weathered"));
	}

	private void nagastonePillar(Block b, String suffix) {
		ResourceLocation side = prefix("block/nagastone_pillar_side" + suffix);
		ResourceLocation end = prefix("block/nagastone_pillar_end" + suffix);
		ResourceLocation alt = prefix("block/nagastone_pillar_side" + suffix + "_alt");
		ModelFile model = models().cubeColumn(name(b), side, end);
		ModelFile reversed = models().cubeColumn(name(b) + "_reversed", alt, end);
		getVariantBuilder(b).forAllStates(state -> {
			int rotX = 0, rotY = 0;
			switch (state.getValue(RotatedPillarBlock.AXIS)) {
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
			ModelFile m = state.getValue(DirectionalRotatedPillarBlock.REVERSED) ? reversed : model;
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
		ModelFile down = models().cubeColumn(name(b), downTex, stoneTiles);
		ModelFile up = models().cubeColumn(name(b) + "_up", upTex, stoneTiles);
		ModelFile north = models().cube(name(b) + "_north", upTex, upTex, stoneTiles, stoneTiles, rightTex, leftTex)
				.texture("particle", "#down");
		ModelFile south = models().cube(name(b) + "_south", downTex, downTex, stoneTiles, stoneTiles, leftTex, rightTex)
				.texture("particle", "#down");
		ModelFile west = models().cube(name(b) + "_west", leftTex, rightTex, rightTex, leftTex, stoneTiles, stoneTiles)
				.texture("particle", "#down");
		ModelFile east = models().cube(name(b) + "_east", rightTex, leftTex, leftTex, rightTex, stoneTiles, stoneTiles)
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

	private void casketStuff() {
		var builder = getVariantBuilder(TFBlocks.KEEPSAKE_CASKET.get());

		var empty = models().getBuilder(name(TFBlocks.KEEPSAKE_CASKET.get())).parent(new ModelFile.UncheckedModelFile("builtin/entity")).texture("particle", "minecraft:block/netherite_block");
		var obsidian = models().withExistingParent("casket_obsidian", prefix("block/casket_solid_template")).texture("top", new ResourceLocation("block/obsidian")).texture("side", new ResourceLocation("block/obsidian"));
		var stone = models().withExistingParent("casket_stone", prefix("block/casket_solid_template")).texture("top", new ResourceLocation("block/stone")).texture("side", new ResourceLocation("block/stone"));
		var basalt = models().withExistingParent("casket_basalt", prefix("block/casket_solid_template")).texture("top", new ResourceLocation("block/basalt_top")).texture("side", new ResourceLocation("block/basalt_side"));

		builder.partialState().with(BlockLoggingEnum.MULTILOGGED, BlockLoggingEnum.AIR).setModels(new ConfiguredModel(empty));
		builder.partialState().with(BlockLoggingEnum.MULTILOGGED, BlockLoggingEnum.WATER).setModels(new ConfiguredModel(empty));
		builder.partialState().with(BlockLoggingEnum.MULTILOGGED, BlockLoggingEnum.LAVA).setModels(new ConfiguredModel(empty));

		builder.partialState().with(BlockLoggingEnum.MULTILOGGED, BlockLoggingEnum.OBSIDIAN).setModels(new ConfiguredModel(obsidian));
		builder.partialState().with(BlockLoggingEnum.MULTILOGGED, BlockLoggingEnum.STONE).setModels(new ConfiguredModel(stone));
		builder.partialState().with(BlockLoggingEnum.MULTILOGGED, BlockLoggingEnum.BASALT).setModels(new ConfiguredModel(basalt));
	}

	private void registerSmokersAndJets() {
		simpleBlock(TFBlocks.SMOKER.get(), this.makeTintedBlockColumnUniqueBottom(TFBlocks.SMOKER.getId().getPath())
				.texture("side", prefix("block/firejet_side"))
				.texture("top", prefix("block/firejet_top"))
				.texture("bottom", new ResourceLocation("block/grass_block_top")));
		simpleBlock(TFBlocks.FIRE_JET.get(), this.makeTintedBlockColumnUniqueBottom(TFBlocks.FIRE_JET.getId().getPath())
				.texture("side", prefix("block/firejet_side"))
				.texture("top", prefix("block/firejet_top"))
				.texture("bottom", new ResourceLocation("block/grass_block_top")));

		ModelFile smokerOff = this.make3LayerCubeIdenticalSides1Bottom(TFBlocks.ENCASED_SMOKER.getId().getPath(), 0, 10, 15, 10, 10)
				.texture("top", prefix("block/towerdev_ghasttraplid_off"))
				.texture("side", prefix("block/towerdev_smoker_off"))
				.texture("bottom", blockTexture(TFBlocks.ENCASED_TOWERWOOD.get()))
				.texture("top2", prefix("block/tower_device_level_2/towerdev_ghasttraplid_off_1"))
				.texture("side2", prefix("block/tower_device_level_1/towerdev_smoker_1"))
				.texture("top3", prefix("block/tower_device_level_2/towerdev_ghasttraplid_off_1"))
				.texture("side3", prefix("block/tower_device_level_2/towerdev_smoker_off_1"));

		ModelFile smokerOn = this.make3LayerCubeIdenticalSides1Bottom(TFBlocks.ENCASED_SMOKER.getId().getPath() + "_on", 0, 10, 15, 7, 10)
				.texture("top", prefix("block/towerdev_ghasttraplid_on"))
				.texture("side", prefix("block/towerdev_firejet_on"))
				.texture("bottom", blockTexture(TFBlocks.ENCASED_TOWERWOOD.get()))
				.texture("top2", prefix("block/tower_device_level_2/towerdev_ghasttraplid_on_1"))
				.texture("side2", prefix("block/tower_device_level_1/towerdev_smoker_1"))
				.texture("top3", prefix("block/tower_device_level_3/towerdev_ghasttraplid_on_2"))
				.texture("side3", prefix("block/tower_device_level_2/towerdev_smoker_on_1"));
		getVariantBuilder(TFBlocks.ENCASED_SMOKER.get()).partialState()
				.with(EncasedSmokerBlock.ACTIVE, false).setModels(new ConfiguredModel(smokerOff));
		getVariantBuilder(TFBlocks.ENCASED_SMOKER.get()).partialState()
				.with(EncasedSmokerBlock.ACTIVE, true).setModels(new ConfiguredModel(smokerOn));

		ModelFile encasedJetOff = this.make3LayerCubeIdenticalSides1Bottom(TFBlocks.ENCASED_FIRE_JET.getId().getPath(), 0, 10, 15, 10, 10)
				.texture("top", prefix("block/towerdev_ghasttraplid_off"))
				.texture("side", prefix("block/towerdev_firejet_off"))
				.texture("bottom", blockTexture(TFBlocks.ENCASED_TOWERWOOD.get()))
				.texture("top2", prefix("block/tower_device_level_2/towerdev_ghasttraplid_off_1"))
				.texture("side2", prefix("block/tower_device_level_1/towerdev_firejet_1"))
				.texture("top3", prefix("block/tower_device_level_2/towerdev_ghasttraplid_off_1"))
				.texture("side3", prefix("block/tower_device_level_2/towerdev_firejet_off_1"));

		ModelFile encasedJetOn = this.make3LayerCubeIdenticalSides1Bottom(TFBlocks.ENCASED_FIRE_JET.getId().getPath() + "_on", 0, 10, 15, 7, 10)
				.texture("top", prefix("block/towerdev_ghasttraplid_on"))
				.texture("side", prefix("block/towerdev_firejet_on"))
				.texture("bottom", blockTexture(TFBlocks.ENCASED_TOWERWOOD.get()))
				.texture("top2", prefix("block/tower_device_level_2/towerdev_ghasttraplid_on_1"))
				.texture("side2", prefix("block/tower_device_level_1/towerdev_firejet_1"))
				.texture("top3", prefix("block/tower_device_level_3/towerdev_ghasttraplid_on_2"))
				.texture("side3", prefix("block/tower_device_level_2/towerdev_firejet_on_1"));

		getVariantBuilder(TFBlocks.ENCASED_FIRE_JET.get()).partialState()
				.with(FireJetBlock.STATE, FireJetVariant.IDLE).setModels(new ConfiguredModel(encasedJetOff));
		getVariantBuilder(TFBlocks.ENCASED_FIRE_JET.get()).partialState()
				.with(FireJetBlock.STATE, FireJetVariant.TIMEOUT).setModels(new ConfiguredModel(encasedJetOff));
		getVariantBuilder(TFBlocks.ENCASED_FIRE_JET.get()).partialState()
				.with(FireJetBlock.STATE, FireJetVariant.POPPING).setModels(new ConfiguredModel(encasedJetOn));
		getVariantBuilder(TFBlocks.ENCASED_FIRE_JET.get()).partialState()
				.with(FireJetBlock.STATE, FireJetVariant.FLAME).setModels(new ConfiguredModel(encasedJetOn));
	}

	private void registerPlantBlocks() {
		simpleBlock(TFBlocks.MOSS_PATCH.get(), new ConfiguredModel(new ModelFile.UncheckedModelFile(TwilightForestMod.prefix("block/moss_patch"))));
		simpleBlockExisting(TFBlocks.MAYAPPLE.get());
		simpleBlock(TFBlocks.CLOVER_PATCH.get(), new ConfiguredModel(new ModelFile.UncheckedModelFile(TwilightForestMod.prefix("block/clover_patch"))));
		simpleBlock(TFBlocks.FIDDLEHEAD.get(), models().withExistingParent(TFBlocks.FIDDLEHEAD.getId().getPath(), "block/tinted_cross").renderType(CUTOUT)
				.texture("cross", blockTexture(TFBlocks.FIDDLEHEAD.get())));
		simpleBlock(TFBlocks.MUSHGLOOM.get(), this.make2layerCross(TFBlocks.MUSHGLOOM.getId().getPath(), CUTOUT, 10, 6)
				.texture("cross", blockTexture(TFBlocks.MUSHGLOOM.get()))
				.texture("cross2", prefix("block/" + TFBlocks.MUSHGLOOM.getId().getPath() + "_head")));

		ModelFile berry = this.make2layerCross(TFBlocks.TORCHBERRY_PLANT.getId().getPath(), CUTOUT, 0, 15)
				.texture("cross", blockTexture(TFBlocks.TORCHBERRY_PLANT.get()))
				.texture("cross2", prefix("block/" + TFBlocks.TORCHBERRY_PLANT.getId().getPath() + "_glow"));
		ModelFile noBerry = models().withExistingParent(TFBlocks.TORCHBERRY_PLANT.getId().getPath() + "_no_berries", new ResourceLocation("block/cross")).renderType(CUTOUT)
				.texture("cross", blockTexture(TFBlocks.TORCHBERRY_PLANT.get()));
		getVariantBuilder(TFBlocks.TORCHBERRY_PLANT.get()).forAllStates(s -> ConfiguredModel.builder().modelFile(s.getValue(TorchberryPlantBlock.HAS_BERRIES) ? berry : noBerry).build());

		simpleBlockExisting(TFBlocks.ROOT_STRAND.get());

		getVariantBuilder(TFBlocks.FALLEN_LEAVES.get()).partialState()
				.with(FallenLeavesBlock.LAYERS, 1).setModels(new ConfiguredModel(buildFallenLeaves(1)));
		getVariantBuilder(TFBlocks.FALLEN_LEAVES.get()).partialState()
				.with(FallenLeavesBlock.LAYERS, 2).setModels(new ConfiguredModel(buildFallenLeaves(2)));
		getVariantBuilder(TFBlocks.FALLEN_LEAVES.get()).partialState()
				.with(FallenLeavesBlock.LAYERS, 3).setModels(new ConfiguredModel(buildFallenLeaves(3)));
		getVariantBuilder(TFBlocks.FALLEN_LEAVES.get()).partialState()
				.with(FallenLeavesBlock.LAYERS, 4).setModels(new ConfiguredModel(buildFallenLeaves(4)));
		getVariantBuilder(TFBlocks.FALLEN_LEAVES.get()).partialState()
				.with(FallenLeavesBlock.LAYERS, 5).setModels(new ConfiguredModel(buildFallenLeaves(5)));
		getVariantBuilder(TFBlocks.FALLEN_LEAVES.get()).partialState()
				.with(FallenLeavesBlock.LAYERS, 6).setModels(new ConfiguredModel(buildFallenLeaves(6)));
		getVariantBuilder(TFBlocks.FALLEN_LEAVES.get()).partialState()
				.with(FallenLeavesBlock.LAYERS, 7).setModels(new ConfiguredModel(buildFallenLeaves(7)));
		getVariantBuilder(TFBlocks.FALLEN_LEAVES.get()).partialState()
				.with(FallenLeavesBlock.LAYERS, 8).setModels(new ConfiguredModel(buildFallenLeaves(8)));
	}

	private void registerWoodBlocks() {
		logWoodSapling(TFBlocks.TWILIGHT_OAK_LOG.get(), TFBlocks.STRIPPED_TWILIGHT_OAK_LOG.get(), TFBlocks.TWILIGHT_OAK_WOOD.get(), TFBlocks.STRIPPED_TWILIGHT_OAK_WOOD.get(), TFBlocks.TWILIGHT_OAK_SAPLING.get());
		plankBlocks("twilight_oak", TFBlocks.TWILIGHT_OAK_PLANKS.get(), TFBlocks.TWILIGHT_OAK_SLAB.get(), TFBlocks.TWILIGHT_OAK_STAIRS.get(), TFBlocks.TWILIGHT_OAK_BUTTON.get(), TFBlocks.TWILIGHT_OAK_FENCE.get(), TFBlocks.TWILIGHT_OAK_GATE.get(), TFBlocks.TWILIGHT_OAK_PLATE.get(), TFBlocks.TWILIGHT_OAK_DOOR.get(), TFBlocks.TWILIGHT_OAK_TRAPDOOR.get(), TFBlocks.TWILIGHT_OAK_BANISTER.get());
		singleBlockBoilerPlate(TFBlocks.TWILIGHT_OAK_LEAVES.get(), "block/leaves", m -> m.texture("all", "minecraft:block/oak_leaves"));

		ResourceLocation rainboakSaplTex = prefix("block/" + TFBlocks.RAINBOW_OAK_SAPLING.getId().getPath());
		simpleBlock(TFBlocks.RAINBOW_OAK_SAPLING.get(), models().cross(TFBlocks.RAINBOW_OAK_SAPLING.getId().getPath(), rainboakSaplTex).renderType(CUTOUT));
		singleBlockBoilerPlate(TFBlocks.RAINBOW_OAK_LEAVES.get(), "block/leaves", m -> m.texture("all", "minecraft:block/oak_leaves"));

		logWoodSapling(TFBlocks.CANOPY_LOG.get(), TFBlocks.STRIPPED_CANOPY_LOG.get(), TFBlocks.CANOPY_WOOD.get(), TFBlocks.STRIPPED_CANOPY_WOOD.get(), TFBlocks.CANOPY_SAPLING.get());
		plankBlocks("canopy", TFBlocks.CANOPY_PLANKS.get(), TFBlocks.CANOPY_SLAB.get(), TFBlocks.CANOPY_STAIRS.get(), TFBlocks.CANOPY_BUTTON.get(), TFBlocks.CANOPY_FENCE.get(), TFBlocks.CANOPY_GATE.get(), TFBlocks.CANOPY_PLATE.get(), TFBlocks.CANOPY_DOOR.get(), TFBlocks.CANOPY_TRAPDOOR.get(), TFBlocks.CANOPY_BANISTER.get());
		singleBlockBoilerPlate(TFBlocks.CANOPY_LEAVES.get(), "block/leaves", m -> m.texture("all", "minecraft:block/spruce_leaves"));

		logWoodSapling(TFBlocks.MANGROVE_LOG.get(), TFBlocks.STRIPPED_MANGROVE_LOG.get(), TFBlocks.MANGROVE_WOOD.get(), TFBlocks.STRIPPED_MANGROVE_WOOD.get(), TFBlocks.MANGROVE_SAPLING.get());
		plankBlocks("mangrove", TFBlocks.MANGROVE_PLANKS.get(), TFBlocks.MANGROVE_SLAB.get(), TFBlocks.MANGROVE_STAIRS.get(), TFBlocks.MANGROVE_BUTTON.get(), TFBlocks.MANGROVE_FENCE.get(), TFBlocks.MANGROVE_GATE.get(), TFBlocks.MANGROVE_PLATE.get(), TFBlocks.MANGROVE_DOOR.get(), TFBlocks.MANGROVE_TRAPDOOR.get(), TFBlocks.MANGROVE_BANISTER.get());
		singleBlockBoilerPlate(TFBlocks.MANGROVE_LEAVES.get(), "block/leaves", m -> m.texture("all", "minecraft:block/birch_leaves"));

		logWoodSapling(TFBlocks.DARK_LOG.get(), TFBlocks.STRIPPED_DARK_LOG.get(), TFBlocks.DARK_WOOD.get(), TFBlocks.STRIPPED_DARK_WOOD.get(), TFBlocks.DARKWOOD_SAPLING.get());
		plankBlocks("darkwood", TFBlocks.DARK_PLANKS.get(), TFBlocks.DARK_SLAB.get(), TFBlocks.DARK_STAIRS.get(), TFBlocks.DARK_BUTTON.get(), TFBlocks.DARK_FENCE.get(), TFBlocks.DARK_GATE.get(), TFBlocks.DARK_PLATE.get(), TFBlocks.DARK_DOOR.get(), TFBlocks.DARK_TRAPDOOR.get(), TFBlocks.DARK_BANISTER.get());
		singleBlockBoilerPlate(TFBlocks.DARK_LEAVES.get(), "block/leaves", m -> m.texture("all", "block/darkwood_leaves"));
		singleBlockBoilerPlate(TFBlocks.HARDENED_DARK_LEAVES.get(), "block/leaves", m -> m.texture("all", "block/darkwood_leaves"));

		logWoodSapling(TFBlocks.TIME_LOG.get(), TFBlocks.STRIPPED_TIME_LOG.get(), TFBlocks.TIME_WOOD.get(), TFBlocks.STRIPPED_TIME_WOOD.get(), TFBlocks.TIME_SAPLING.get());
		plankBlocks("time", TFBlocks.TIME_PLANKS.get(), TFBlocks.TIME_SLAB.get(), TFBlocks.TIME_STAIRS.get(), TFBlocks.TIME_BUTTON.get(), TFBlocks.TIME_FENCE.get(), TFBlocks.TIME_GATE.get(), TFBlocks.TIME_PLATE.get(), TFBlocks.TIME_DOOR.get(), TFBlocks.TIME_TRAPDOOR.get(), true, TFBlocks.TIME_BANISTER.get());
		singleBlockBoilerPlate(TFBlocks.TIME_LEAVES.get(), "block/leaves", m -> m.texture("all", "block/time_leaves"));
		magicLogCore(TFBlocks.TIME_LOG_CORE.get());

		logWoodSapling(TFBlocks.TRANSFORMATION_LOG.get(), TFBlocks.STRIPPED_TRANSFORMATION_LOG.get(), TFBlocks.TRANSFORMATION_WOOD.get(), TFBlocks.STRIPPED_TRANSFORMATION_WOOD.get(), TFBlocks.TRANSFORMATION_SAPLING.get());
		plankBlocks("trans", TFBlocks.TRANSFORMATION_PLANKS.get(), TFBlocks.TRANSFORMATION_SLAB.get(), TFBlocks.TRANSFORMATION_STAIRS.get(), TFBlocks.TRANSFORMATION_BUTTON.get(), TFBlocks.TRANSFORMATION_FENCE.get(), TFBlocks.TRANSFORMATION_GATE.get(), TFBlocks.TRANSFORMATION_PLATE.get(), TFBlocks.TRANSFORMATION_DOOR.get(), TFBlocks.TRANSFORMATION_TRAPDOOR.get(), true, TFBlocks.TRANSFORMATION_BANISTER.get());
		singleBlockBoilerPlate(TFBlocks.TRANSFORMATION_LEAVES.get(), "block/leaves", m -> m.texture("all", "block/transformation_leaves"));
		magicLogCore(TFBlocks.TRANSFORMATION_LOG_CORE.get());

		logWoodSapling(TFBlocks.MINING_LOG.get(), TFBlocks.STRIPPED_MINING_LOG.get(), TFBlocks.MINING_WOOD.get(), TFBlocks.STRIPPED_MINING_WOOD.get(), TFBlocks.MINING_SAPLING.get());
		plankBlocks("mine", TFBlocks.MINING_PLANKS.get(), TFBlocks.MINING_SLAB.get(), TFBlocks.MINING_STAIRS.get(), TFBlocks.MINING_BUTTON.get(), TFBlocks.MINING_FENCE.get(), TFBlocks.MINING_GATE.get(), TFBlocks.MINING_PLATE.get(), TFBlocks.MINING_DOOR.get(), TFBlocks.MINING_TRAPDOOR.get(), TFBlocks.MINING_BANISTER.get());
		singleBlockBoilerPlate(TFBlocks.MINING_LEAVES.get(), "block/leaves", m -> m.texture("all", "block/mining_leaves"));
		magicLogCore(TFBlocks.MINING_LOG_CORE.get());

		logWoodSapling(TFBlocks.SORTING_LOG.get(), TFBlocks.STRIPPED_SORTING_LOG.get(), TFBlocks.SORTING_WOOD.get(), TFBlocks.STRIPPED_SORTING_WOOD.get(), TFBlocks.SORTING_SAPLING.get());
		plankBlocks("sort", TFBlocks.SORTING_PLANKS.get(), TFBlocks.SORTING_SLAB.get(), TFBlocks.SORTING_STAIRS.get(), TFBlocks.SORTING_BUTTON.get(), TFBlocks.SORTING_FENCE.get(), TFBlocks.SORTING_GATE.get(), TFBlocks.SORTING_PLATE.get(), TFBlocks.SORTING_DOOR.get(), TFBlocks.SORTING_TRAPDOOR.get(), true, TFBlocks.SORTING_BANISTER.get());
		singleBlockBoilerPlate(TFBlocks.SORTING_LEAVES.get(), "block/leaves", m -> m.texture("all", "block/sorting_leaves"));
		magicLogCore(TFBlocks.SORTING_LOG_CORE.get());

		banisterVanilla(TFBlocks.OAK_BANISTER.get(), "oak_planks", "oak");
		banisterVanilla(TFBlocks.SPRUCE_BANISTER.get(), "spruce_planks", "spruce");
		banisterVanilla(TFBlocks.BIRCH_BANISTER.get(), "birch_planks", "birch");
		banisterVanilla(TFBlocks.JUNGLE_BANISTER.get(), "jungle_planks", "jungle");
		banisterVanilla(TFBlocks.ACACIA_BANISTER.get(), "acacia_planks", "acacia");
		banisterVanilla(TFBlocks.DARK_OAK_BANISTER.get(), "dark_oak_planks", "dark_oak");
		banisterVanilla(TFBlocks.CRIMSON_BANISTER.get(), "crimson_planks", "crimson");
		banisterVanilla(TFBlocks.WARPED_BANISTER.get(), "warped_planks", "warped");
		banisterVanilla(TFBlocks.VANGROVE_BANISTER.get(), "mangrove_planks", "vanilla_mangrove");

		final ResourceLocation MOSS = TwilightForestMod.prefix("block/mosspatch");
		final ResourceLocation MOSS_OVERHANG = TwilightForestMod.prefix("block/moss_overhang");
		final ResourceLocation TALL_GRASS = new ResourceLocation("block/grass");
		final ResourceLocation SNOW = new ResourceLocation("block/snow");
		final ResourceLocation SNOW_OVERHANG = TwilightForestMod.prefix("block/snow_overhang");

		final ModelFile EMPTY_LOG = this.buildHorizontalHollowLog(false, false);
		final ModelFile LAYERED_LOG = this.buildHorizontalHollowLog(true, false);
		final ModelFile MOSS_LOG_GRASS = models().getBuilder("hollow_log_moss_grass").parent(this.buildHorizontalHollowLog(true, true)).renderType(CUTOUT).texture("carpet", MOSS).texture("overhang", MOSS_OVERHANG).texture("plant", TALL_GRASS);
		final ModelFile MOSS_LOG = models().getBuilder("hollow_log_moss").parent(LAYERED_LOG).renderType(CUTOUT).texture("carpet", MOSS).texture("overhang", MOSS_OVERHANG);
		final ModelFile SNOW_LOG = models().getBuilder("hollow_log_snow").parent(LAYERED_LOG).renderType(CUTOUT).texture("carpet", SNOW).texture("overhang", SNOW_OVERHANG);
		final ModelFile HOLLOW_LOG = this.buildVerticalLog(null);
		final ModelFile VINE_LOG = this.buildVerticalLog(HollowLogVariants.Climbable.VINE);
		final ModelFile LADDER_LOG = this.buildVerticalLog(HollowLogVariants.Climbable.LADDER);

		hollowLogs(Blocks.OAK_LOG, Blocks.STRIPPED_OAK_LOG, TFBlocks.HOLLOW_OAK_LOG_HORIZONTAL, TFBlocks.HOLLOW_OAK_LOG_VERTICAL, TFBlocks.HOLLOW_OAK_LOG_CLIMBABLE, EMPTY_LOG, MOSS_LOG, MOSS_LOG_GRASS, SNOW_LOG, HOLLOW_LOG, VINE_LOG, LADDER_LOG);
		hollowLogs(Blocks.SPRUCE_LOG, Blocks.STRIPPED_SPRUCE_LOG, TFBlocks.HOLLOW_SPRUCE_LOG_HORIZONTAL, TFBlocks.HOLLOW_SPRUCE_LOG_VERTICAL, TFBlocks.HOLLOW_SPRUCE_LOG_CLIMBABLE, EMPTY_LOG, MOSS_LOG, MOSS_LOG_GRASS, SNOW_LOG, HOLLOW_LOG, VINE_LOG, LADDER_LOG);
		hollowLogs(Blocks.BIRCH_LOG, Blocks.STRIPPED_BIRCH_LOG, TFBlocks.HOLLOW_BIRCH_LOG_HORIZONTAL, TFBlocks.HOLLOW_BIRCH_LOG_VERTICAL, TFBlocks.HOLLOW_BIRCH_LOG_CLIMBABLE, EMPTY_LOG, MOSS_LOG, MOSS_LOG_GRASS, SNOW_LOG, HOLLOW_LOG, VINE_LOG, LADDER_LOG);
		hollowLogs(Blocks.JUNGLE_LOG, Blocks.STRIPPED_JUNGLE_LOG, TFBlocks.HOLLOW_JUNGLE_LOG_HORIZONTAL, TFBlocks.HOLLOW_JUNGLE_LOG_VERTICAL, TFBlocks.HOLLOW_JUNGLE_LOG_CLIMBABLE, EMPTY_LOG, MOSS_LOG, MOSS_LOG_GRASS, SNOW_LOG, HOLLOW_LOG, VINE_LOG, LADDER_LOG);
		hollowLogs(Blocks.ACACIA_LOG, Blocks.STRIPPED_ACACIA_LOG, TFBlocks.HOLLOW_ACACIA_LOG_HORIZONTAL, TFBlocks.HOLLOW_ACACIA_LOG_VERTICAL, TFBlocks.HOLLOW_ACACIA_LOG_CLIMBABLE, EMPTY_LOG, MOSS_LOG, MOSS_LOG_GRASS, SNOW_LOG, HOLLOW_LOG, VINE_LOG, LADDER_LOG);
		hollowLogs(Blocks.DARK_OAK_LOG, Blocks.STRIPPED_DARK_OAK_LOG, TFBlocks.HOLLOW_DARK_OAK_LOG_HORIZONTAL, TFBlocks.HOLLOW_DARK_OAK_LOG_VERTICAL, TFBlocks.HOLLOW_DARK_OAK_LOG_CLIMBABLE, EMPTY_LOG, MOSS_LOG, MOSS_LOG_GRASS, SNOW_LOG, HOLLOW_LOG, VINE_LOG, LADDER_LOG);
		hollowLogs(Blocks.CRIMSON_STEM, Blocks.STRIPPED_CRIMSON_STEM, TFBlocks.HOLLOW_CRIMSON_STEM_HORIZONTAL, TFBlocks.HOLLOW_CRIMSON_STEM_VERTICAL, TFBlocks.HOLLOW_CRIMSON_STEM_CLIMBABLE, EMPTY_LOG, MOSS_LOG, MOSS_LOG_GRASS, SNOW_LOG, HOLLOW_LOG, VINE_LOG, LADDER_LOG);
		hollowLogs(Blocks.WARPED_STEM, Blocks.STRIPPED_WARPED_STEM, TFBlocks.HOLLOW_WARPED_STEM_HORIZONTAL, TFBlocks.HOLLOW_WARPED_STEM_VERTICAL, TFBlocks.HOLLOW_WARPED_STEM_CLIMBABLE, EMPTY_LOG, MOSS_LOG, MOSS_LOG_GRASS, SNOW_LOG, HOLLOW_LOG, VINE_LOG, LADDER_LOG);
		hollowLogs(Blocks.MANGROVE_LOG, Blocks.STRIPPED_MANGROVE_LOG, TFBlocks.HOLLOW_VANGROVE_LOG_HORIZONTAL, TFBlocks.HOLLOW_VANGROVE_LOG_VERTICAL, TFBlocks.HOLLOW_VANGROVE_LOG_CLIMBABLE, EMPTY_LOG, MOSS_LOG, MOSS_LOG_GRASS, SNOW_LOG, HOLLOW_LOG, VINE_LOG, LADDER_LOG);

		hollowLogs(TFBlocks.TWILIGHT_OAK_LOG, TFBlocks.STRIPPED_TWILIGHT_OAK_LOG, TFBlocks.HOLLOW_TWILIGHT_OAK_LOG_HORIZONTAL, TFBlocks.HOLLOW_TWILIGHT_OAK_LOG_VERTICAL, TFBlocks.HOLLOW_TWILIGHT_OAK_LOG_CLIMBABLE, EMPTY_LOG, MOSS_LOG, MOSS_LOG_GRASS, SNOW_LOG, HOLLOW_LOG, VINE_LOG, LADDER_LOG);
		hollowLogs(TFBlocks.CANOPY_LOG, TFBlocks.STRIPPED_CANOPY_LOG, TFBlocks.HOLLOW_CANOPY_LOG_HORIZONTAL, TFBlocks.HOLLOW_CANOPY_LOG_VERTICAL, TFBlocks.HOLLOW_CANOPY_LOG_CLIMBABLE, EMPTY_LOG, MOSS_LOG, MOSS_LOG_GRASS, SNOW_LOG, HOLLOW_LOG, VINE_LOG, LADDER_LOG);
		hollowLogs(TFBlocks.MANGROVE_LOG, TFBlocks.STRIPPED_MANGROVE_LOG, TFBlocks.HOLLOW_MANGROVE_LOG_HORIZONTAL, TFBlocks.HOLLOW_MANGROVE_LOG_VERTICAL, TFBlocks.HOLLOW_MANGROVE_LOG_CLIMBABLE, EMPTY_LOG, MOSS_LOG, MOSS_LOG_GRASS, SNOW_LOG, HOLLOW_LOG, VINE_LOG, LADDER_LOG);
		hollowLogs(TFBlocks.DARK_LOG, TFBlocks.STRIPPED_DARK_LOG, TFBlocks.HOLLOW_DARK_LOG_HORIZONTAL, TFBlocks.HOLLOW_DARK_LOG_VERTICAL, TFBlocks.HOLLOW_DARK_LOG_CLIMBABLE, EMPTY_LOG, MOSS_LOG, MOSS_LOG_GRASS, SNOW_LOG, HOLLOW_LOG, VINE_LOG, LADDER_LOG);
		hollowLogs(TFBlocks.TIME_LOG, TFBlocks.STRIPPED_TIME_LOG, TFBlocks.HOLLOW_TIME_LOG_HORIZONTAL, TFBlocks.HOLLOW_TIME_LOG_VERTICAL, TFBlocks.HOLLOW_TIME_LOG_CLIMBABLE, EMPTY_LOG, MOSS_LOG, MOSS_LOG_GRASS, SNOW_LOG, HOLLOW_LOG, VINE_LOG, LADDER_LOG);
		hollowLogs(TFBlocks.TRANSFORMATION_LOG, TFBlocks.STRIPPED_TRANSFORMATION_LOG, TFBlocks.HOLLOW_TRANSFORMATION_LOG_HORIZONTAL, TFBlocks.HOLLOW_TRANSFORMATION_LOG_VERTICAL, TFBlocks.HOLLOW_TRANSFORMATION_LOG_CLIMBABLE, EMPTY_LOG, MOSS_LOG, MOSS_LOG_GRASS, SNOW_LOG, HOLLOW_LOG, VINE_LOG, LADDER_LOG);
		hollowLogs(TFBlocks.MINING_LOG, TFBlocks.STRIPPED_MINING_LOG, TFBlocks.HOLLOW_MINING_LOG_HORIZONTAL, TFBlocks.HOLLOW_MINING_LOG_VERTICAL, TFBlocks.HOLLOW_MINING_LOG_CLIMBABLE, EMPTY_LOG, MOSS_LOG, MOSS_LOG_GRASS, SNOW_LOG, HOLLOW_LOG, VINE_LOG, LADDER_LOG);
		hollowLogs(TFBlocks.SORTING_LOG, TFBlocks.STRIPPED_SORTING_LOG, TFBlocks.HOLLOW_SORTING_LOG_HORIZONTAL, TFBlocks.HOLLOW_SORTING_LOG_VERTICAL, TFBlocks.HOLLOW_SORTING_LOG_CLIMBABLE, EMPTY_LOG, MOSS_LOG, MOSS_LOG_GRASS, SNOW_LOG, HOLLOW_LOG, VINE_LOG, LADDER_LOG);
	}

	private void magicLogCore(Block b) {
		ResourceLocation topTex = prefix("block/" + name(b).replace("_core", "_top"));
		ModelFile off = models().cubeColumn(name(b), blockTexture(b), topTex);
		ModelFile on = models().cubeColumn(name(b) + "_on", prefix("block/" + name(b) + "_on"), topTex);
		getVariantBuilder(b).forAllStates(s -> {
			ModelFile f = s.getValue(SpecialMagicLogBlock.ACTIVE) ? on : off;
			Direction.Axis axis = s.getValue(RotatedPillarBlock.AXIS);
			int rotX = axis == Direction.Axis.X || axis == Direction.Axis.Z ? 90 : 0;
			int rotY = axis == Direction.Axis.X ? 90 : 0;
			return ConfiguredModel.builder()
					.modelFile(f).rotationX(rotX).rotationY(rotY).build();
		});
	}

	private void rotationallyCorrectColumn(Block b) {
		ResourceLocation side = prefix("block/" + name(b) + "_side");
		ResourceLocation end = prefix("block/" + name(b) + "_end");
		ConfiguredModel yModel = new ConfiguredModel(models().cubeColumn(name(b), side, end));
		ConfiguredModel xModel = ConfiguredModel.builder()
				.modelFile(models().withExistingParent(name(b) + "_x", prefix("block/util/cube_column_rotationally_correct_x"))
						.texture("side", side).texture("end", end))
				.rotationX(90).rotationY(90)
				.buildLast();
		ConfiguredModel zModel = ConfiguredModel.builder()
				.modelFile(models().withExistingParent(name(b) + "_z", prefix("block/util/cube_column_rotationally_correct_z"))
						.texture("side", side).texture("end", end))
				.rotationX(90)
				.buildLast();
		getVariantBuilder(b)
				.partialState().with(RotatedPillarBlock.AXIS, Direction.Axis.Y).setModels(yModel)
				.partialState().with(RotatedPillarBlock.AXIS, Direction.Axis.X).setModels(xModel)
				.partialState().with(RotatedPillarBlock.AXIS, Direction.Axis.Z).setModels(zModel);
	}

	private void castleDoor(Block b) {
		ModelFile vanished = models().withExistingParent(ForgeRegistries.BLOCKS.getKey(b).getPath() + "_vanished", "block/block")
				.texture("base", TwilightForestMod.prefix("block/castle_door_vanished"))
				.texture("particle", TwilightForestMod.prefix("block/castle_door_vanished"))
				.texture("overlay", TwilightForestMod.prefix("block/castle_door_rune_corners"))
				.texture("overlay_connected", TwilightForestMod.prefix("block/castle_door_rune_ctm"))
				.renderType(CUTOUT)
				.customLoader(CastleDoorBuilder::begin).end();

		ModelFile main = models().withExistingParent(ForgeRegistries.BLOCKS.getKey(b).getPath(), "block/block")
				.texture("base", TwilightForestMod.prefix("block/castle_door"))
				.texture("particle", TwilightForestMod.prefix("block/castle_door"))
				.texture("overlay", TwilightForestMod.prefix("block/castle_door_rune_corners"))
				.texture("overlay_connected", TwilightForestMod.prefix("block/castle_door_rune_ctm"))
				.renderType(CUTOUT)
				.customLoader(CastleDoorBuilder::begin).end();

		getVariantBuilder(b).forAllStates(state -> ConfiguredModel.builder().modelFile(state.getValue(CastleDoorBlock.VANISHED) ? vanished : main).build());
	}

	private void allRotations(Block b, ModelFile model) {
		ConfiguredModel.Builder<?> builder = ConfiguredModel.builder();
		int[] rots = {0, 90, 180, 270};
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

	private void stonePillar() {
		ModelFile main_x = models().withExistingParent("pillar_main_x", prefix("block/pillar/pillar_12_ctm")).renderType(CUTOUT).texture("side_x", prefix("block/stone_twist/twist_x")).texture("side_z", prefix("block/stone_twist/twist_x"));
		ModelFile bottom_x = models().withExistingParent("pillar_bottom_x", prefix("block/pillar/pillar_bottom")).renderType(CUTOUT).texture("bottom_x", prefix("block/stone_twist/cap/y_y_bottom")).texture("bottom_z", prefix("block/stone_twist/cap/y_y_bottom")).texture("bottom_cap", prefix("block/stone_twist/cap/end_bottom_x"));
		ModelFile top_x = models().withExistingParent("pillar_top_x", prefix("block/pillar/pillar_top")).renderType(CUTOUT).texture("top_x", prefix("block/stone_twist/cap/y_y_top")).texture("top_z", prefix("block/stone_twist/cap/y_y_top")).texture("top_cap", prefix("block/stone_twist/cap/end_top_x"));
		ModelFile main_y = models().withExistingParent("pillar_main_y", prefix("block/pillar/pillar_12_ctm")).renderType(CUTOUT).texture("side_x", prefix("block/stone_twist/twist_y")).texture("side_z", prefix("block/stone_twist/twist_y"));
		ModelFile bottom_y = models().withExistingParent("pillar_bottom_y", prefix("block/pillar/pillar_bottom")).renderType(CUTOUT).texture("bottom_x", prefix("block/stone_twist/cap/y_y_bottom")).texture("bottom_z", prefix("block/stone_twist/cap/y_y_bottom")).texture("bottom_cap", prefix("block/stone_twist/cap/end_bottom_y"));
		ModelFile top_y = models().withExistingParent("pillar_top_y", prefix("block/pillar/pillar_top")).renderType(CUTOUT).texture("top_x", prefix("block/stone_twist/cap/y_y_top")).texture("top_z", prefix("block/stone_twist/cap/y_y_top")).texture("top_cap", prefix("block/stone_twist/cap/end_top_y"));
		ModelFile main_z = models().withExistingParent("pillar_main_z", prefix("block/pillar/pillar_12_ctm")).renderType(CUTOUT).texture("side_x", prefix("block/stone_twist/twist_x")).texture("side_z", prefix("block/stone_twist/twist_y"));
		ModelFile bottom_z = models().withExistingParent("pillar_bottom_z", prefix("block/pillar/pillar_bottom")).renderType(CUTOUT).texture("bottom_x", prefix("block/stone_twist/cap/y_y_bottom")).texture("bottom_z", prefix("block/stone_twist/cap/y_y_bottom")).texture("bottom_cap", prefix("block/stone_twist/cap/end_bottom_z"));
		ModelFile top_z = models().withExistingParent("pillar_top_z", prefix("block/pillar/pillar_top")).renderType(CUTOUT).texture("top_x", prefix("block/stone_twist/cap/y_y_top")).texture("top_z", prefix("block/stone_twist/cap/y_y_top")).texture("top_cap", prefix("block/stone_twist/cap/end_top_z"));
		getMultipartBuilder(TFBlocks.TWISTED_STONE_PILLAR.get())
				.part().modelFile(main_x).uvLock(true).rotationX(90).rotationY(90).addModel().condition(WallPillarBlock.AXIS, Direction.Axis.X).end()
				.part().modelFile(top_x).rotationX(90).rotationY(90).addModel().condition(WallPillarBlock.AXIS, Direction.Axis.X).condition(PipeBlock.EAST, false).end()
				.part().modelFile(bottom_x).rotationX(90).rotationY(90).addModel().condition(WallPillarBlock.AXIS, Direction.Axis.X).condition(PipeBlock.WEST, false).end()
				.part().modelFile(main_y).uvLock(true).addModel().condition(WallPillarBlock.AXIS, Direction.Axis.Y).end()
				.part().modelFile(top_y).addModel().condition(WallPillarBlock.AXIS, Direction.Axis.Y).condition(PipeBlock.UP, false).end()
				.part().modelFile(bottom_y).addModel().condition(WallPillarBlock.AXIS, Direction.Axis.Y).condition(PipeBlock.DOWN, false).end()
				.part().modelFile(main_z).uvLock(true).rotationX(90).addModel().condition(WallPillarBlock.AXIS, Direction.Axis.Z).end()
				.part().modelFile(top_z).rotationX(90).addModel().condition(WallPillarBlock.AXIS, Direction.Axis.Z).condition(PipeBlock.NORTH, false).end()
				.part().modelFile(bottom_z).rotationX(90).addModel().condition(WallPillarBlock.AXIS, Direction.Axis.Z).condition(PipeBlock.SOUTH, false).end();
	}

	private void slider() {
		ModelFile slider = models().getExistingFile(TwilightForestMod.prefix("block/slider"));
		ModelFile horizSlider = models().getExistingFile(TwilightForestMod.prefix("block/slider_horiz"));
		getVariantBuilder(TFBlocks.SLIDER.get()).forAllStates(state -> switch (state.getValue(SliderBlock.AXIS)) {
			case X -> ConfiguredModel.builder().modelFile(horizSlider).rotationX(90).rotationY(90).build();
			case Z -> ConfiguredModel.builder().modelFile(horizSlider).rotationX(90).build();
			default -> ConfiguredModel.builder().modelFile(slider).build();
		});
	}

	private void towerBlocks() {
		ModelFile reappear = this.make3LayerCubeAllSidesSame(TFBlocks.REAPPEARING_BLOCK.getId().getPath(), CUTOUT, 0, 15, 10)
				.texture("all", prefix("block/towerdev_reappearing_off"))
				.texture("all2", prefix("block/tower_device_level_1/towerdev_reappearing_off_1"))
				.texture("all3", prefix("block/tower_device_level_2/towerdev_reappearing_off_2"));
		ModelFile reappearActive = this.make3LayerCubeAllSidesSame(TFBlocks.REAPPEARING_BLOCK.getId().getPath() + "_active", CUTOUT, 0, 15, 10)
				.texture("all", prefix("block/towerdev_reappearing_on"))
				.texture("all2", prefix("block/tower_device_level_1/towerdev_reappearing_on_1"))
				.texture("all3", prefix("block/tower_device_level_2/towerdev_reappearing_on_2"));
		ModelFile reappearVanished = this.make4x4x4SmallCube(TFBlocks.REAPPEARING_BLOCK.getId().getPath() + "_vanished")
				.texture("all", prefix("block/towerdev_reappearing_trace_off"));
		ModelFile reappearVanishedActive = this.make4x4x4SmallCube(TFBlocks.REAPPEARING_BLOCK.getId().getPath() + "_vanished_active")
				.texture("all", prefix("block/towerdev_reappearing_trace_on"));
		getVariantBuilder(TFBlocks.REAPPEARING_BLOCK.get()).forAllStates(s -> {
			ModelFile model;
			if (s.getValue(VanishingBlock.VANISHED)) {
				model = s.getValue(VanishingBlock.ACTIVE) ? reappearVanishedActive : reappearVanished;
			} else {
				model = s.getValue(VanishingBlock.ACTIVE) ? reappearActive : reappear;
			}
			return ConfiguredModel.builder().modelFile(model).build();
		});

		ModelFile vanish = this.make3LayerCubeAllSidesSame(TFBlocks.VANISHING_BLOCK.getId().getPath(), CUTOUT, 0, 15, 10)
				.texture("all", prefix("block/towerdev_vanish_off"))
				.texture("all2", prefix("block/tower_device_level_1/towerdev_vanish_off_1"))
				.texture("all3", prefix("block/tower_device_level_2/towerdev_vanish_off_2"));
		ModelFile vanishActive = this.make3LayerCubeAllSidesSame(TFBlocks.VANISHING_BLOCK.getId().getPath() + "_active", CUTOUT, 0, 15, 10)
				.texture("all", prefix("block/towerdev_vanish_on"))
				.texture("all2", prefix("block/tower_device_level_1/towerdev_vanish_on_1"))
				.texture("all3", prefix("block/tower_device_level_2/towerdev_vanish_on_2"));
		getVariantBuilder(TFBlocks.VANISHING_BLOCK.get()).partialState()
				.with(VanishingBlock.ACTIVE, false).setModels(new ConfiguredModel(vanish));
		getVariantBuilder(TFBlocks.VANISHING_BLOCK.get()).partialState()
				.with(VanishingBlock.ACTIVE, true).setModels(new ConfiguredModel(vanishActive));

		ModelFile vanishLocked = this.make3LayerCubeAllSidesSame(TFBlocks.LOCKED_VANISHING_BLOCK.getId().getPath(), CUTOUT, 0, 15, 10)
				.texture("all", prefix("block/towerdev_lock_on"))
				.texture("all2", prefix("block/tower_device_level_1/towerdev_lock_on_1"))
				.texture("all3", prefix("block/tower_device_level_2/towerdev_lock_on_2"));
		ModelFile vanishUnlocked = this.make3LayerCubeAllSidesSame(TFBlocks.LOCKED_VANISHING_BLOCK.getId().getPath() + "_unlocked", CUTOUT, 0, 15, 10)
				.texture("all", prefix("block/towerdev_lock_off"))
				.texture("all2", prefix("block/tower_device_level_1/towerdev_lock_off_1"))
				.texture("all3", prefix("block/tower_device_level_2/towerdev_lock_off_2"));
		getVariantBuilder(TFBlocks.LOCKED_VANISHING_BLOCK.get()).partialState()
				.with(LockedVanishingBlock.LOCKED, true).setModels(new ConfiguredModel(vanishLocked));
		getVariantBuilder(TFBlocks.LOCKED_VANISHING_BLOCK.get()).partialState()
				.with(LockedVanishingBlock.LOCKED, false).setModels(new ConfiguredModel(vanishUnlocked));

		ModelFile ghastTrap = this.make3LayerCubeIdenticalSides1Bottom(TFBlocks.GHAST_TRAP.getId().getPath(), 0, 10, 15, 10, 10)
				.texture("top", prefix("block/towerdev_ghasttraplid_off"))
				.texture("side", prefix("block/towerdev_ghasttrap_off"))
				.texture("bottom", prefix("block/encased_towerwood"))
				.texture("top2", prefix("block/tower_device_level_2/towerdev_ghasttraplid_off_1"))
				.texture("side2", prefix("block/tower_device_level_1/towerdev_ghasttrap_off_1"))
				.texture("top3", prefix("block/tower_device_level_2/towerdev_ghasttraplid_off_1"))
				.texture("side3", prefix("block/tower_device_level_2/towerdev_ghasttrap_off_2"));
		ModelFile ghastTrapActive = this.make3LayerCubeIdenticalSides1Bottom(TFBlocks.GHAST_TRAP.getId().getPath() + "_active", 0, 10, 15, 7, 10)
				.texture("top", prefix("block/towerdev_ghasttraplid_on"))
				.texture("side", prefix("block/towerdev_ghasttrap_on"))
				.texture("bottom", prefix("block/encased_towerwood"))
				.texture("top2", prefix("block/tower_device_level_2/towerdev_ghasttraplid_on_1"))
				.texture("side2", prefix("block/tower_device_level_1/towerdev_ghasttrap_on_1"))
				.texture("top3", prefix("block/tower_device_level_3/towerdev_ghasttraplid_on_2"))
				.texture("side3", prefix("block/tower_device_level_2/towerdev_ghasttrap_on_2"));
		getVariantBuilder(TFBlocks.GHAST_TRAP.get()).partialState()
				.with(GhastTrapBlock.ACTIVE, false).setModels(new ConfiguredModel(ghastTrap));
		getVariantBuilder(TFBlocks.GHAST_TRAP.get()).partialState()
				.with(GhastTrapBlock.ACTIVE, true).setModels(new ConfiguredModel(ghastTrapActive));

		ModelFile builder = this.make3LayerCubeAllSidesSame(TFBlocks.CARMINITE_BUILDER.getId().getPath(), CUTOUT, 0, 15, 10)
				.texture("all", prefix("block/towerdev_builder_off"))
				.texture("all2", prefix("block/tower_device_level_1/towerdev_builder_off_1"))
				.texture("all3", prefix("block/tower_device_level_2/towerdev_builder_off_2"));
		ModelFile builderActive = this.make3LayerCubeAllSidesSame(TFBlocks.CARMINITE_BUILDER.getId().getPath() + "_active", CUTOUT, 0, 15, 10)
				.texture("all", prefix("block/towerdev_builder_on"))
				.texture("all2", prefix("block/tower_device_level_1/towerdev_builder_on_1"))
				.texture("all3", prefix("block/tower_device_level_2/towerdev_builder_on_2"));
		ModelFile builderTimeout = this.make3LayerCubeAllSidesSame(TFBlocks.CARMINITE_BUILDER.getId().getPath() + "_timeout", CUTOUT, 0, 10, 7)
				.texture("all", prefix("block/towerdev_builder_timeout"))
				.texture("all2", prefix("block/tower_device_level_2/towerdev_builder_timeout_1"))
				.texture("all3", prefix("block/tower_device_level_3/towerdev_builder_timeout_2"));
		getVariantBuilder(TFBlocks.CARMINITE_BUILDER.get()).partialState()
				.with(BuilderBlock.STATE, TowerDeviceVariant.BUILDER_INACTIVE).setModels(new ConfiguredModel(builder));
		getVariantBuilder(TFBlocks.CARMINITE_BUILDER.get()).partialState()
				.with(BuilderBlock.STATE, TowerDeviceVariant.BUILDER_ACTIVE).setModels(new ConfiguredModel(builderActive));
		getVariantBuilder(TFBlocks.CARMINITE_BUILDER.get()).partialState()
				.with(BuilderBlock.STATE, TowerDeviceVariant.BUILDER_TIMEOUT).setModels(new ConfiguredModel(builderTimeout));

		ModelFile built = this.make2LayerCubeAllSidesSame(TFBlocks.BUILT_BLOCK.getId().getPath(), CUTOUT, 15, 15, false)
				.texture("all", prefix("block/towerdev_built_off"))
				.texture("all2", prefix("block/tower_device_level_1/towerdev_builder_off_1"));
		ModelFile builtActive = this.make2LayerCubeAllSidesSame(TFBlocks.BUILT_BLOCK.getId().getPath() + "_active", CUTOUT, 15, 15, false)
				.texture("all", prefix("block/towerdev_built_on"))
				.texture("all2", prefix("block/tower_device_level_1/towerdev_builder_on_1"));
		getVariantBuilder(TFBlocks.BUILT_BLOCK.get()).partialState()
				.with(TranslucentBuiltBlock.ACTIVE, false).setModels(new ConfiguredModel(built));
		getVariantBuilder(TFBlocks.BUILT_BLOCK.get()).partialState()
				.with(TranslucentBuiltBlock.ACTIVE, true).setModels(new ConfiguredModel(builtActive));

		ModelFile antibuilder = this.make3LayerCubeAllSidesSame(TFBlocks.ANTIBUILDER.getId().getPath(), CUTOUT, 0, 15, 10)
				.texture("all", prefix("block/towerdev_antibuilder"))
				.texture("all2", prefix("block/tower_device_level_1/towerdev_antibuilder_1"))
				.texture("all3", prefix("block/tower_device_level_2/towerdev_antibuilder_2"));
		simpleBlock(TFBlocks.ANTIBUILDER.get(), antibuilder);
		ModelFile antibuilt = this.make2LayerCubeAllSidesSame(TFBlocks.ANTIBUILT_BLOCK.getId().getPath(), CUTOUT, 0, 10, false)
				.texture("all", prefix("block/towerdev_antibuilt"))
				.texture("all2", prefix("block/tower_device_level_2/towerdev_antibuilt_1"));
		simpleBlock(TFBlocks.ANTIBUILT_BLOCK.get(), antibuilt);

		ModelFile reactor = this.make3LayerCubeAllSidesSame(TFBlocks.CARMINITE_REACTOR.getId().getPath(), CUTOUT, 0, 15, 10)
				.texture("all", prefix("block/towerdev_reactor_off"))
				.texture("all2", prefix("block/tower_device_level_1/towerdev_reactor_off_1"))
				.texture("all3", prefix("block/tower_device_level_2/towerdev_reactor_off_2"));
		ModelFile reactorActive = this.make3LayerCubeAllSidesSame(TFBlocks.CARMINITE_REACTOR.getId().getPath() + "_active", CUTOUT, 0, 15, 10)
				.texture("all", prefix("block/towerdev_reactor_on"))
				.texture("all2", prefix("block/tower_device_level_1/towerdev_reactor_on_1"))
				.texture("all3", prefix("block/tower_device_level_2/towerdev_reactor_on_2"));
		getVariantBuilder(TFBlocks.CARMINITE_REACTOR.get()).partialState()
				.with(CarminiteReactorBlock.ACTIVE, false).setModels(new ConfiguredModel(reactor));
		getVariantBuilder(TFBlocks.CARMINITE_REACTOR.get()).partialState()
				.with(CarminiteReactorBlock.ACTIVE, true).setModels(new ConfiguredModel(reactorActive));
		simpleBlock(TFBlocks.REACTOR_DEBRIS.get(), models().cubeAll(TFBlocks.REACTOR_DEBRIS.getId().getPath(), new ResourceLocation("block/destroy_stage_9")).renderType(CUTOUT));
	}

	private ModelFile pedestalModel(String name, String north, String south, String west, String east, boolean active) {
		BlockModelBuilder ret = this.makePedestal(name, active)
				.texture("end", prefix("block/pedestal/top"))
				.texture("north", prefix("block/pedestal/" + north + "_latent"))
				.texture("south", prefix("block/pedestal/" + south + "_latent"))
				.texture("west", prefix("block/pedestal/" + west + "_latent"))
				.texture("east", prefix("block/pedestal/" + east + "_latent"));
		if (active) {
			ret = ret
					.texture("end2", prefix("block/pedestal/top_glow"))
					.texture("north2", prefix("block/pedestal/" + north + "_glow"))
					.texture("south2", prefix("block/pedestal/" + south + "_glow"))
					.texture("west2", prefix("block/pedestal/" + west + "_glow"))
					.texture("east2", prefix("block/pedestal/" + east + "_glow"))
					.texture("north3", prefix("block/pedestal/" + north))
					.texture("south3", prefix("block/pedestal/" + south))
					.texture("west3", prefix("block/pedestal/" + west))
					.texture("east3", prefix("block/pedestal/" + east));
		}
		return ret;
	}

	private void trophyPedestal() {
		String baseName = TFBlocks.TROPHY_PEDESTAL.getId().getPath();
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
		getVariantBuilder(TFBlocks.TROPHY_PEDESTAL.get()).partialState()
				.with(TrophyPedestalBlock.ACTIVE, false).setModels(latentModels.toArray(new ConfiguredModel[0]));


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
		getVariantBuilder(TFBlocks.TROPHY_PEDESTAL.get()).partialState()
				.with(TrophyPedestalBlock.ACTIVE, true).setModels(activeModels.toArray(new ConfiguredModel[0]));
	}

	private void thorns() {
		for (RegistryObject<Block> block : ImmutableList.of(TFBlocks.GREEN_THORNS, TFBlocks.BROWN_THORNS, TFBlocks.BURNT_THORNS)) {
			String path = block.getId().getPath();
			ResourceLocation sideTexture = prefix("block/" + path + "_side");
			ResourceLocation endTexture = prefix("block/" + path + "_top");

			models().withExistingParent(path, prefix("block/thorns_main")).renderType(CUTOUT)//This is just used for the item model
					.texture("side", sideTexture)
					.texture("end", endTexture);

			ModelFile thorns = models().withExistingParent(path + "_thorns", prefix("block/thorns")).renderType(CUTOUT)
					.texture("side", sideTexture);

			ModelFile top = models().withExistingParent(path + "_top", prefix("block/thorns_section_top")).renderType(CUTOUT)
					.texture("side", sideTexture)
					.texture("end", endTexture);

			ModelFile bottom = models().withExistingParent(path + "_bottom", prefix("block/thorns_section_bottom")).renderType(CUTOUT)
					.texture("side", sideTexture)
					.texture("end", endTexture);

			ModelFile section = models().withExistingParent(path + "_no_section", prefix("block/thorns_no_section")).renderType(CUTOUT)
					.texture("side", sideTexture);

			ModelFile noSectionAlt = models().withExistingParent(path + "_no_section_alt", prefix("block/thorns_no_section_alt")).renderType(CUTOUT)
					.texture("side", sideTexture);

			getMultipartBuilder(block.get())
					.part().modelFile(thorns).addModel().condition(RotatedPillarBlock.AXIS, Direction.Axis.Y).end()
					.part().modelFile(thorns).rotationX(90).addModel().condition(RotatedPillarBlock.AXIS, Direction.Axis.Z).end()
					.part().modelFile(thorns).rotationX(90).rotationY(90).addModel().condition(RotatedPillarBlock.AXIS, Direction.Axis.X).end()

					.part().modelFile(top).rotationX(90).addModel().condition(PipeBlock.UP, true).end()
					.part().modelFile(section).rotationX(270).addModel().condition(PipeBlock.UP, false).condition(RotatedPillarBlock.AXIS, Direction.Axis.Z, Direction.Axis.Y).end()
					.part().modelFile(section).rotationX(270).rotationY(90).addModel().condition(PipeBlock.UP, false).condition(RotatedPillarBlock.AXIS, Direction.Axis.X).end()

					.part().modelFile(bottom).rotationX(90).addModel().condition(PipeBlock.DOWN, true).end()
					.part().modelFile(section).rotationX(90).addModel().condition(PipeBlock.DOWN, false).condition(RotatedPillarBlock.AXIS, Direction.Axis.Z, Direction.Axis.Y).end()
					.part().modelFile(section).rotationX(90).rotationY(90).addModel().condition(PipeBlock.DOWN, false).condition(RotatedPillarBlock.AXIS, Direction.Axis.X).end()

					.part().modelFile(top).rotationY(270).addModel().condition(PipeBlock.EAST, true).end()
					.part().modelFile(section).rotationY(90).addModel().condition(PipeBlock.EAST, false).condition(RotatedPillarBlock.AXIS, Direction.Axis.Y, Direction.Axis.X).end()
					.part().modelFile(noSectionAlt).rotationY(90).addModel().condition(PipeBlock.EAST, false).condition(RotatedPillarBlock.AXIS, Direction.Axis.Z).end()

					.part().modelFile(bottom).rotationY(270).addModel().condition(PipeBlock.WEST, true).end()
					.part().modelFile(section).rotationY(270).addModel().condition(PipeBlock.WEST, false).condition(RotatedPillarBlock.AXIS, Direction.Axis.Y, Direction.Axis.X).end()
					.part().modelFile(noSectionAlt).rotationY(270).addModel().condition(PipeBlock.WEST, false).condition(RotatedPillarBlock.AXIS, Direction.Axis.Z).end()

					.part().modelFile(top).addModel().condition(PipeBlock.SOUTH, true).end()
					.part().modelFile(section).rotationY(180).addModel().condition(PipeBlock.SOUTH, false).condition(RotatedPillarBlock.AXIS, Direction.Axis.Y, Direction.Axis.Z).end()
					.part().modelFile(noSectionAlt).rotationY(180).addModel().condition(PipeBlock.SOUTH, false).condition(RotatedPillarBlock.AXIS, Direction.Axis.X).end()

					.part().modelFile(bottom).addModel().condition(PipeBlock.NORTH, true).end()
					.part().modelFile(section).addModel().condition(PipeBlock.NORTH, false).condition(RotatedPillarBlock.AXIS, Direction.Axis.Y, Direction.Axis.Z).end()
					.part().modelFile(noSectionAlt).addModel().condition(PipeBlock.NORTH, false).condition(RotatedPillarBlock.AXIS, Direction.Axis.X).end();
		}
	}

	private void thornRose() {
		ModelFile rose = models().cross(TFBlocks.THORN_ROSE.getId().getPath(), blockTexture(TFBlocks.THORN_ROSE.get())).renderType(CUTOUT);
		getVariantBuilder(TFBlocks.THORN_ROSE.get()).forAllStates(state -> switch (state.getValue(DirectionalBlock.FACING)) {
			case UP -> ConfiguredModel.builder().modelFile(rose).build();
			case DOWN -> ConfiguredModel.builder().modelFile(rose).rotationX(180).build();
			case SOUTH -> ConfiguredModel.builder().modelFile(rose).rotationX(270).build();
			case NORTH -> ConfiguredModel.builder().modelFile(rose).rotationX(90).build();
			case WEST -> ConfiguredModel.builder().modelFile(rose).rotationY(90).rotationX(270).build();
			case EAST -> ConfiguredModel.builder().modelFile(rose).rotationY(90).rotationX(90).build();
		});
	}

	private void auroraBlocks() {
		int variants = 16;
		ModelFile[] models = new ModelFile[variants];
		for (int i = 0; i < variants; i++) {
			models[i] = this.makeTintedBlockAll(TFBlocks.AURORA_BLOCK.getId().getPath() + "_" + i, SOLID)
					.texture("all", prefix("block/" + TFBlocks.AURORA_BLOCK.getId().getPath() + "_" + i));
		}
		for (int i = 0; i < variants; i++) {
			getVariantBuilder(TFBlocks.AURORA_BLOCK.get()).partialState().with(AuroraBrickBlock.VARIANT, i)
					.setModels(ConfiguredModel.builder()
							.weight(3).modelFile(models[i]).nextModel()
							.weight(1).modelFile(models[(i + 1) % variants]).build());
		}

		ModelFile pillarModel = this.makeTintedBlockColumn(TFBlocks.AURORA_PILLAR.getId().getPath())
				.texture("end", prefix("block/" + TFBlocks.AURORA_PILLAR.getId().getPath() + "_top"))
				.texture("side", blockTexture(TFBlocks.AURORA_PILLAR.get()));
		axisBlock(TFBlocks.AURORA_PILLAR.get(), pillarModel, pillarModel);

		ModelFile slabModel = this.makeTintedSlab(TFBlocks.AURORA_SLAB.getId().getPath())
				.texture("bottom", prefix("block/" + TFBlocks.AURORA_PILLAR.getId().getPath() + "_top"))
				.texture("top", prefix("block/" + TFBlocks.AURORA_PILLAR.getId().getPath() + "_top"))
				.texture("side", prefix("block/" + TFBlocks.AURORA_SLAB.getId().getPath() + "_side"));
		ModelFile doubleSlabModel = this.makeTintedBlockColumn(TFBlocks.AURORA_SLAB.getId().getPath() + "_double")
				.texture("end", prefix("block/" + TFBlocks.AURORA_PILLAR.getId().getPath() + "_top"))
				.texture("side", prefix("block/" + TFBlocks.AURORA_SLAB.getId().getPath() + "_side"));

		getVariantBuilder(TFBlocks.AURORA_SLAB.get()).partialState()
				.with(SlabBlock.TYPE, SlabType.BOTTOM).setModels(new ConfiguredModel(slabModel));
		getVariantBuilder(TFBlocks.AURORA_SLAB.get()).partialState()
				.with(SlabBlock.TYPE, SlabType.TOP).setModels(ConfiguredModel.builder().uvLock(true).rotationX(180).modelFile(slabModel).build());
		getVariantBuilder(TFBlocks.AURORA_SLAB.get()).partialState()
				.with(SlabBlock.TYPE, SlabType.DOUBLE).setModels(new ConfiguredModel(doubleSlabModel));

		ModelFile auroraGlass = this.makeTintedBlockAll(TFBlocks.AURORALIZED_GLASS.getId().getPath(), TRANSLUCENT)
				.texture("all", blockTexture(TFBlocks.AURORALIZED_GLASS.get()));
		simpleBlock(TFBlocks.AURORALIZED_GLASS.get(), auroraGlass);
	}

	private void mazestone() {
		ResourceLocation plainTex = blockTexture(TFBlocks.MAZESTONE.get());

		ModelFile mazeStone = models().cubeAll(TFBlocks.MAZESTONE.getId().getPath(), plainTex);
		simpleBlock(TFBlocks.MAZESTONE.get(), ConfiguredModel.builder()
				.rotationX(90).rotationY(90).modelFile(mazeStone).nextModel()
				.rotationX(270).rotationY(270).modelFile(mazeStone).build());
		simpleBlock(TFBlocks.MAZESTONE_BRICK.get());

		ModelFile chiseled = models().cubeColumn(TFBlocks.CUT_MAZESTONE.getId().getPath(), blockTexture(TFBlocks.CUT_MAZESTONE.get()), plainTex);
		simpleBlock(TFBlocks.CUT_MAZESTONE.get(), chiseled);

		ModelFile decorative = models().cubeColumn(TFBlocks.DECORATIVE_MAZESTONE.getId().getPath(), blockTexture(TFBlocks.DECORATIVE_MAZESTONE.get()), plainTex);
		simpleBlock(TFBlocks.DECORATIVE_MAZESTONE.get(), decorative);

		simpleBlock(TFBlocks.CRACKED_MAZESTONE.get());
		simpleBlock(TFBlocks.MOSSY_MAZESTONE.get());

		ResourceLocation brickTex = blockTexture(TFBlocks.MAZESTONE_BRICK.get());
		ModelFile mosaic = models().cubeColumn(TFBlocks.MAZESTONE_MOSAIC.getId().getPath(), brickTex, blockTexture(TFBlocks.MAZESTONE_MOSAIC.get()));
		simpleBlock(TFBlocks.MAZESTONE_MOSAIC.get(), mosaic);

		ModelFile border = models().cubeColumn(TFBlocks.MAZESTONE_BORDER.getId().getPath(), brickTex, blockTexture(TFBlocks.MAZESTONE_BORDER.get()));
		simpleBlock(TFBlocks.MAZESTONE_BORDER.get(), border);
	}

	private void lilyPad(Block b) {
		String baseName = name(b);
		ResourceLocation parent = prefix("block/huge_lily_pad");
		ModelFile[] models = new ModelFile[4];
		for (int i = 0; i < models.length; i++) {
			models[i] = models().withExistingParent(baseName + "_" + i, parent).renderType(CUTOUT)
					.texture("texture", prefix("block/huge_lily_pad_" + i))
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
			switch (state.getValue(HugeLilyPadBlock.FACING)) {
				case SOUTH -> {
					rotY = 180;
					m = south;
				}
				case WEST -> {
					rotY = 270;
					m = west;
				}
				case EAST -> {
					rotY = 90;
					m = east;
				}
				default -> {
					rotY = 0;
					m = north;
				}
			}

			ModelFile model = m.get(state.getValue(HugeLilyPadBlock.PIECE));
			return ConfiguredModel.builder().rotationY(rotY).modelFile(model).build();
		});
	}

	@SuppressWarnings("SuspiciousNameCombination")
	private void candelabra() {
		// TODO variants
		final List<ModelFile> candelabras = new ArrayList<>();
		final List<ModelFile> wallCandelabras = new ArrayList<>();

		final int minHeight = 4;
		final int maxHeight = 5;
		for (int right = minHeight; right <= maxHeight; right++) {
			for (int center = minHeight; center <= maxHeight; center++) {
				for (int left = minHeight; left <= maxHeight; left++) {
					candelabras.add(this.buildCandelabra(left, center, right));
					wallCandelabras.add(this.buildWallCandelabra(left, center, right));
				}
			}
		}

		this.getVariantBuilder(TFBlocks.CANDELABRA.get()).forAllStates(state -> {
			Direction direction = state.getValue(CandelabraBlock.FACING);
			boolean onWall = state.getValue(CandelabraBlock.ON_WALL);
			boolean lit = state.getValue(CandelabraBlock.LIGHTING) != AbstractLightableBlock.Lighting.NONE;

			ConfiguredModel.Builder<?> stateBuilder = ConfiguredModel.builder();

			Iterator<ModelFile> models = onWall ? wallCandelabras.iterator() : candelabras.iterator();

			while (models.hasNext()) {
				ModelFile model = models.next();
				stateBuilder.modelFile(this.models().getBuilder(model.getLocation().toString() + "_plain" + (lit ? "_lit" : "")).parent(model).renderType(CUTOUT).texture("candle", lit ? "minecraft:block/candle_lit" : "minecraft:block/candle")).rotationY((int) direction.toYRot());

				if (models.hasNext())
					stateBuilder = stateBuilder.nextModel();
			}

			return stateBuilder.build();
		});
	}

	private void perFaceBlock(Block b, ResourceLocation inside, ResourceLocation outside) {
		ModelFile modelInside = models().withExistingParent(name(b) + "_inside", new ResourceLocation("block/template_single_face"))
				.texture("texture", inside);
		ModelFile modelOutside = models().withExistingParent(name(b) + "_outside", new ResourceLocation("block/template_single_face"))
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

	private void hollowLogs(Block originalLog, Block strippedLog, RegistryObject<HollowLogHorizontal> horizontalHollowLog, RegistryObject<HollowLogVertical> verticalHollowLog, RegistryObject<HollowLogClimbable> climbableHollowLog, ModelFile emptyLog, ModelFile mossLog, ModelFile grassLog, ModelFile snowLog, ModelFile hollowLog, ModelFile vineLog, ModelFile ladderLog) {
		ResourceLocation top = new ResourceLocation("block/" + name(originalLog) + "_top");
		ResourceLocation side = new ResourceLocation("block/" + name(originalLog));
		ResourceLocation inner = new ResourceLocation("block/" + name(strippedLog));

		this.getVariantBuilder(horizontalHollowLog.get()).forAllStates(state -> ConfiguredModel.builder().modelFile((switch (state.getValue(HollowLogHorizontal.VARIANT)) {
			case MOSS -> models().getBuilder(horizontalHollowLog.getId().getPath() + "_moss").parent(mossLog);
			case MOSS_AND_GRASS -> models().getBuilder(horizontalHollowLog.getId().getPath() + "_moss_grass").parent(grassLog);
			case SNOW -> models().getBuilder(horizontalHollowLog.getId().getPath() + "_snow").parent(snowLog);
			default -> models().getBuilder(horizontalHollowLog.getId().getPath()).parent(emptyLog);
		}).renderType(CUTOUT).texture("top", top).texture("side", side).texture("inner", inner)).rotationY(state.getValue(HollowLogHorizontal.HORIZONTAL_AXIS) == Direction.Axis.X ? 90 : 0).build());

		this.simpleBlock(verticalHollowLog.get(), models().getBuilder(verticalHollowLog.getId().getPath()).parent(hollowLog).texture("top", top).texture("side", side).texture("inner", inner));

		this.getVariantBuilder(climbableHollowLog.get()).forAllStates(state -> ConfiguredModel.builder().modelFile((switch (state.getValue(HollowLogClimbable.VARIANT)) {
			case VINE -> models().getBuilder(climbableHollowLog.getId().getPath() + "_vine").parent(vineLog);
			case LADDER, LADDER_WATERLOGGED -> models().getBuilder(climbableHollowLog.getId().getPath() + "_ladder").parent(ladderLog);
		}).renderType(CUTOUT).texture("top", top).texture("side", side).texture("inner", inner)).rotationY((int) state.getValue(HollowLogClimbable.FACING).toYRot()).uvLock(true).build());
	}

	private void hollowLogs(RegistryObject<RotatedPillarBlock> originalLog, RegistryObject<RotatedPillarBlock> strippedLog, RegistryObject<HollowLogHorizontal> horizontalHollowLog, RegistryObject<HollowLogVertical> verticalHollowLog, RegistryObject<HollowLogClimbable> climbableHollowLog, ModelFile emptyLog, ModelFile mossLog, ModelFile grassLog, ModelFile snowLog, ModelFile hollowLog, ModelFile vineLog, ModelFile ladderLog) {
		ResourceLocation top = TwilightForestMod.prefix("block/" + originalLog.getId().getPath() + "_top");
		ResourceLocation side = TwilightForestMod.prefix("block/" + originalLog.getId().getPath());
		ResourceLocation inner = TwilightForestMod.prefix("block/" + strippedLog.getId().getPath());

		this.getVariantBuilder(horizontalHollowLog.get()).forAllStates(state -> ConfiguredModel.builder().modelFile((switch (state.getValue(HollowLogHorizontal.VARIANT)) {
			case MOSS -> models().getBuilder(horizontalHollowLog.getId().getPath() + "_moss").parent(mossLog);
			case MOSS_AND_GRASS -> models().getBuilder(horizontalHollowLog.getId().getPath() + "_moss_grass").parent(grassLog);
			case SNOW -> models().getBuilder(horizontalHollowLog.getId().getPath() + "_snow").parent(snowLog);
			default -> models().getBuilder(horizontalHollowLog.getId().getPath()).parent(emptyLog);
		}).renderType(CUTOUT).texture("top", top).texture("side", side).texture("inner", inner)).rotationY(state.getValue(HollowLogHorizontal.HORIZONTAL_AXIS) == Direction.Axis.X ? 90 : 0).build());

		this.simpleBlock(verticalHollowLog.get(), models().getBuilder(verticalHollowLog.getId().getPath()).parent(hollowLog).texture("top", top).texture("side", side).texture("inner", inner));

		this.getVariantBuilder(climbableHollowLog.get()).forAllStates(state -> ConfiguredModel.builder().modelFile((switch (state.getValue(HollowLogClimbable.VARIANT)) {
			case VINE -> models().getBuilder(climbableHollowLog.getId().getPath() + "_vine").parent(vineLog);
			case LADDER, LADDER_WATERLOGGED -> models().getBuilder(climbableHollowLog.getId().getPath() + "_ladder").parent(ladderLog);
		}).renderType(CUTOUT).texture("top", top).texture("side", side).texture("inner", inner)).rotationY((int) state.getValue(HollowLogClimbable.FACING).toYRot()).uvLock(true).build());
	}

	@Nonnull
	@Override
	public String getName() {
		return "TwilightForest blockstates and block models";
	}
}
