package twilightforest.data;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import net.minecraft.core.Direction;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraftforge.client.model.generators.*;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.fmllegacy.RegistryObject;
import twilightforest.TwilightForestMod;
import twilightforest.block.*;
import twilightforest.enums.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Consumer;

import static twilightforest.TwilightForestMod.prefix;

public class BlockstateGenerator extends BlockStateProvider {
	public BlockstateGenerator(DataGenerator gen, ExistingFileHelper exFileHelper) {
		super(gen, TwilightForestMod.ID, exFileHelper);
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
			int rotY;
			switch (d) {
			default: rotY = 0; break;
			case EAST: rotY = 90; break;
			case SOUTH: rotY = 180; break;
			case WEST: rotY = 270; break;
			}

			ironLadder.part().modelFile(ironLadderLeft).rotationY(rotY).addModel()
							.condition(LadderBlock.FACING, d).condition(IronLadderBlock.LEFT, false).end();
			ironLadder.part().modelFile(ironLadderLeftConnected).rotationY(rotY).addModel()
							.condition(LadderBlock.FACING, d).condition(IronLadderBlock.LEFT, true).end();
			ironLadder.part().modelFile(ironLadderRight).rotationY(rotY).addModel()
							.condition(LadderBlock.FACING, d).condition(IronLadderBlock.RIGHT, false).end();
			ironLadder.part().modelFile(ironLadderRightConnected).rotationY(rotY).addModel()
							.condition(LadderBlock.FACING, d).condition(IronLadderBlock.RIGHT, true).end();
		}

		//terrorcotta();
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
		simpleBlock(TFBlocks.THORN_ROSE.get(), models().cross(TFBlocks.THORN_ROSE.getId().getPath(), blockTexture(TFBlocks.THORN_ROSE.get())));
		simpleBlock(TFBlocks.THORN_LEAVES.get(), models().getExistingFile(new ResourceLocation("block/oak_leaves")));
		simpleBlock(TFBlocks.BEANSTALK_LEAVES.get(), models().getExistingFile(new ResourceLocation("block/spruce_leaves")));
		simpleBlock(TFBlocks.HOLLOW_OAK_SAPLING.get(), models().cross(TFBlocks.HOLLOW_OAK_SAPLING.getId().getPath(), blockTexture(TFBlocks.HOLLOW_OAK_SAPLING.get())));
		ModelFile deadrock = models().cubeAll(TFBlocks.DEADROCK.getId().getPath(), blockTexture(TFBlocks.DEADROCK.get()));
		ModelFile deadrockMirrored = models().withExistingParent(TFBlocks.DEADROCK.getId().getPath() + "_mirrored", prefix("block/util/cube_mirrored_all"))
						.texture("all", blockTexture(TFBlocks.DEADROCK.get()));
		simpleBlock(TFBlocks.DEADROCK.get(), ConfiguredModel.builder()
						.modelFile(deadrock).nextModel()
						.rotationY(180).modelFile(deadrock).nextModel()
						.modelFile(deadrockMirrored).nextModel()
						.rotationY(180).modelFile(deadrockMirrored).build()
		);
		ModelFile deadrockCracked = models().cubeAll(TFBlocks.CRACKED_DEADROCK.getId().getPath(), blockTexture(TFBlocks.CRACKED_DEADROCK.get()));
		allRotations(TFBlocks.CRACKED_DEADROCK.get(), deadrockCracked);
		ModelFile deadrockWeathered = models().cubeAll(TFBlocks.WEATHERED_DEADROCK.getId().getPath(), blockTexture(TFBlocks.WEATHERED_DEADROCK.get()));
		allRotations(TFBlocks.WEATHERED_DEADROCK.get(), deadrockWeathered);
		perFaceBlock(TFBlocks.TROLLSTEINN.get(), blockTexture(TFBlocks.TROLLSTEINN.get()), prefix("block/" + TFBlocks.TROLLSTEINN.getId().getPath() + "_light"));
		simpleBlock(TFBlocks.WISPY_CLOUD.get());
		simpleBlock(TFBlocks.FLUFFY_CLOUD.get());
		simpleBlock(TFBlocks.GIANT_COBBLESTONE.get(), models().withExistingParent(TFBlocks.GIANT_COBBLESTONE.getId().getPath(), prefix("block/util/giant_block"))
						.texture("all", blockTexture(Blocks.COBBLESTONE)));
		simpleBlock(TFBlocks.GIANT_LOG.get(), models().withExistingParent(TFBlocks.GIANT_LOG.getId().getPath(), prefix("block/util/giant_column"))
						.texture("side", blockTexture(Blocks.OAK_LOG))
						.texture("end", new ResourceLocation("block/oak_log_top")));
		simpleBlock(TFBlocks.GIANT_LEAVES.get(), models().withExistingParent(TFBlocks.GIANT_LEAVES.getId().getPath(), prefix("block/util/giant_block"))
						.texture("all", blockTexture(Blocks.OAK_LEAVES)));
		simpleBlock(TFBlocks.GIANT_OBSIDIAN.get(), models().withExistingParent(TFBlocks.GIANT_OBSIDIAN.getId().getPath(), prefix("block/util/giant_block"))
						.texture("all", blockTexture(Blocks.OBSIDIAN)));
		simpleBlock(TFBlocks.UBEROUS_SOIL.get(), models().withExistingParent(TFBlocks.UBEROUS_SOIL.getId().getPath(), "block/template_farmland")
						.texture("top", blockTexture(TFBlocks.UBEROUS_SOIL.get()))
						.texture("dirt", blockTexture(TFBlocks.UBEROUS_SOIL.get())));
		axisBlock(TFBlocks.HUGE_STALK.get(), prefix("block/huge_stalk"), prefix("block/huge_stalk_top"));
		perFaceBlock(TFBlocks.HUGE_MUSHGLOOM.get(), prefix("block/huge_gloom_inside"), prefix("block/huge_gloom_cap"));
		perFaceBlock(TFBlocks.HUGE_MUSHGLOOM_STEM.get(), prefix("block/huge_gloom_inside"), prefix("block/huge_mushgloom_stem"));
		simpleBlock(TFBlocks.TROLLVIDR.get(), models().cross(TFBlocks.TROLLVIDR.getId().getPath(), blockTexture(TFBlocks.TROLLVIDR.get())));
		simpleBlock(TFBlocks.UNRIPE_TROLLBER.get(), models().cross(TFBlocks.UNRIPE_TROLLBER.getId().getPath(), blockTexture(TFBlocks.UNRIPE_TROLLBER.get())));
		ModelFile trollber = models().withExistingParent(TFBlocks.TROLLBER.getId().getPath(), prefix("block/util/cross_2_layer"))
						.texture("cross", blockTexture(TFBlocks.TROLLBER.get()))
						.texture("cross2", prefix("block/" + TFBlocks.TROLLBER.getId().getPath() + "_glow"));
		simpleBlock(TFBlocks.TROLLBER.get(), trollber);
		lilyPad(TFBlocks.HUGE_LILY_PAD.get());
		simpleBlock(TFBlocks.HUGE_WATER_LILY.get(), models().cross(TFBlocks.HUGE_WATER_LILY.getId().getPath(), blockTexture(TFBlocks.HUGE_WATER_LILY.get())));
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
		stairsBlock(TFBlocks.ENCASED_CASTLE_BRICK_STAIRS.get(), prefix("block/" + TFBlocks.ENCASED_CASTLE_BRICK_PILLAR.getId().getPath() + "_h"), prefix("block/castleblock_tile"), prefix("block/" + TFBlocks.CASTLE_ROOF_TILE.getId().getPath()));
		stairsBlock(TFBlocks.BOLD_CASTLE_BRICK_STAIRS.get(), prefix("block/" + TFBlocks.BOLD_CASTLE_BRICK_TILE.getId().getPath()));

		ConfiguredModel[] runeBrickModels = new ConfiguredModel[8];
		for (int i = 0; i < runeBrickModels.length; i++) {
			runeBrickModels[i] = new ConfiguredModel(
							models().withExistingParent("castle_rune_brick_" + i, prefix("block/util/cube_all_2_layer"))
							.texture("all", prefix("block/castle_brick"))
							.texture("all2", prefix("block/castleblock_magic_" + i)));
		}

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
		simpleBlockExisting(TFBlocks.IRONWOOD_BLOCK.get());
		simpleBlockExisting(TFBlocks.FIERY_BLOCK.get());
		simpleBlock(TFBlocks.ARCTIC_FUR_BLOCK.get());
		ModelFile steeleafBlock = models().cubeAll(TFBlocks.STEELEAF_BLOCK.getId().getPath(), prefix("block/" + TFBlocks.STEELEAF_BLOCK.getId().getPath()));
		allRotations(TFBlocks.STEELEAF_BLOCK.get(), steeleafBlock);
		ModelFile carminiteBlock = models().withExistingParent(TFBlocks.CARMINITE_BLOCK.getId().getPath(), prefix("block/util/cube_all_2_layer"))
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

		ModelFile bigSpawner = models().withExistingParent("boss_spawner", "block/block").texture("particle", "#all").texture("all", TwilightForestMod.prefix("block/boss_spawner")).element()
				.from(-4, -4, -4)
				.to(20, 20, 20)
				.allFaces((dir, builder) -> builder/*.cullface(dir)*/.uvs(2, 2, 14, 14).texture("#all"))
				.end();

		simpleBlock(TFBlocks.NAGA_BOSS_SPAWNER.get(), bigSpawner);
		simpleBlock(TFBlocks.LICH_BOSS_SPAWNER.get(), bigSpawner);
		simpleBlock(TFBlocks.HYDRA_BOSS_SPAWNER.get(), bigSpawner);
		simpleBlock(TFBlocks.UR_GHAST_BOSS_SPAWNER.get(), bigSpawner);
		simpleBlock(TFBlocks.KNIGHT_PHANTOM_BOSS_SPAWNER.get(), bigSpawner);
		simpleBlock(TFBlocks.SNOW_QUEEN_BOSS_SPAWNER.get(), bigSpawner);
		simpleBlock(TFBlocks.MINOSHROOM_BOSS_SPAWNER.get(), bigSpawner);
		simpleBlock(TFBlocks.ALPHA_YETI_BOSS_SPAWNER.get(), bigSpawner);
		simpleBlock(TFBlocks.FINAL_BOSS_BOSS_SPAWNER.get(), bigSpawner);
		simpleBlockExisting(TFBlocks.FIREFLY_JAR.get());
		simpleBlockExisting(TFBlocks.FIREFLY_SPAWNER.get());
		simpleBlockExisting(TFBlocks.CICADA_JAR.get());
		registerPlantBlocks();
		simpleBlock(TFBlocks.ROOT_BLOCK.get());
		simpleBlock(TFBlocks.LIVEROOT_BLOCK.get());
		simpleBlock(TFBlocks.MANGROVE_ROOT.get());

		ModelFile glowing = models().withExistingParent(TFBlocks.UNCRAFTING_TABLE.getId().getPath() + "_glowing", prefix("block/util/cube_bottom_double_top_and_sides"))
				.texture("top", prefix("block/uncrafting_top"))
				.texture("glow", prefix("block/uncrafting_glow"))
				.texture("sideglow", prefix("block/uncrafting_glow_side"))
				.texture("bottom", new ResourceLocation("block/jungle_planks"))
				.texture("side", prefix("block/uncrafting_side"));

		ModelFile notglowing = models().withExistingParent(TFBlocks.UNCRAFTING_TABLE.getId().getPath(), prefix("block/util/cube_bottom_double_top"))
				.texture("top", prefix("block/uncrafting_top"))
				.texture("glow", prefix("block/uncrafting_glow"))
				.texture("bottom", new ResourceLocation("block/jungle_planks"))
				.texture("side", prefix("block/uncrafting_side"));

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
				case 1 -> { return ConfiguredModel.builder().modelFile(models().cubeColumn("block/death_tome_spawner_1", prefix("block/wood/bookshelf_spawner/bookshelf_1"), prefix("block/wood/planks_canopy_0"))).build(); }
				case 2 -> { return ConfiguredModel.builder().modelFile(models().cubeColumn("block/death_tome_spawner_2", prefix("block/wood/bookshelf_spawner/bookshelf_2"), prefix("block/wood/planks_canopy_0"))).build(); }
				case 3 -> { return ConfiguredModel.builder().modelFile(models().cubeColumn("block/death_tome_spawner_3", prefix("block/wood/bookshelf_spawner/bookshelf_3"), prefix("block/wood/planks_canopy_0"))).build(); }
				case 4 -> { return ConfiguredModel.builder().modelFile(models().cubeColumn("block/death_tome_spawner_4", prefix("block/wood/bookshelf_spawner/bookshelf_4"), prefix("block/wood/planks_canopy_0"))).build(); }
				case 5 -> { return ConfiguredModel.builder().modelFile(models().cubeColumn("block/death_tome_spawner_5", prefix("block/wood/bookshelf_spawner/bookshelf_5"), prefix("block/wood/planks_canopy_0"))).build(); }
				case 6 -> { return ConfiguredModel.builder().modelFile(models().cubeColumn("block/death_tome_spawner_6", prefix("block/wood/bookshelf_spawner/bookshelf_6"), prefix("block/wood/planks_canopy_0"))).build(); }
				case 7 -> { return ConfiguredModel.builder().modelFile(models().cubeColumn("block/death_tome_spawner_7", prefix("block/wood/bookshelf_spawner/bookshelf_7"), prefix("block/wood/planks_canopy_0"))).build(); }
				case 8 -> { return ConfiguredModel.builder().modelFile(models().cubeColumn("block/death_tome_spawner_8", prefix("block/wood/bookshelf_spawner/bookshelf_8"), prefix("block/wood/planks_canopy_0"))).build(); }
				case 9 -> { return ConfiguredModel.builder().modelFile(models().cubeColumn("block/death_tome_spawner_9", prefix("block/wood/bookshelf_spawner/bookshelf_9"), prefix("block/wood/planks_canopy_0"))).build(); }
				default -> { return ConfiguredModel.builder().modelFile(models().cubeColumn("block/death_tome_spawner_10", prefix("block/wood/bookshelf_canopy"), prefix("block/wood/planks_canopy_0"))).build(); }
			}
		}, TomeSpawnerBlock.SPAWNER);
		//ConfiguredModel[] lapisModels = new ConfiguredModel[4];
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
		simpleBlock(TFBlocks.POTTED_TWILIGHT_OAK_SAPLING.get(), models().getExistingFile(prefix("block/potted_twilight_oak_sapling")));
		simpleBlock(TFBlocks.POTTED_CANOPY_SAPLING.get(), models().getExistingFile(prefix("block/potted_canopy_sapling")));
		simpleBlock(TFBlocks.POTTED_MANGROVE_SAPLING.get(), models().getExistingFile(prefix("block/potted_mangrove_sapling")));
		simpleBlock(TFBlocks.POTTED_DARKWOOD_SAPLING.get(), models().getExistingFile(prefix("block/potted_darkwood_sapling")));
		simpleBlock(TFBlocks.POTTED_HOLLOW_OAK_SAPLING.get(), models().getExistingFile(prefix("block/potted_hollow_oak_sapling")));
		simpleBlock(TFBlocks.POTTED_RAINBOW_OAK_SAPLING.get(), models().getExistingFile(prefix("block/potted_rainboak_sapling")));
		simpleBlock(TFBlocks.POTTED_TIME_SAPLING.get(), models().getExistingFile(prefix("block/potted_time_sapling")));
		simpleBlock(TFBlocks.POTTED_TRANSFORMATION_SAPLING.get(), models().getExistingFile(prefix("block/potted_trans_sapling")));
		simpleBlock(TFBlocks.POTTED_MINING_SAPLING.get(), models().getExistingFile(prefix("block/potted_mine_sapling")));
		simpleBlock(TFBlocks.POTTED_SORTING_SAPLING.get(), models().getExistingFile(prefix("block/potted_sort_sapling")));
		simpleBlock(TFBlocks.POTTED_MAYAPPLE.get(), models().getExistingFile(prefix("block/potted_mayapple")));
		simpleBlock(TFBlocks.POTTED_FIDDLEHEAD.get(), models().getExistingFile(prefix("block/potted_fiddlehead")));
		simpleBlock(TFBlocks.POTTED_MUSHGLOOM.get(), models().getExistingFile(prefix("block/potted_mushgloom")));
		simpleBlock(TFBlocks.POTTED_THORN.get(), models().getExistingFile(prefix("block/potted_thorn")));
		simpleBlock(TFBlocks.POTTED_GREEN_THORN.get(), models().getExistingFile(prefix("block/potted_green_thorn")));
		simpleBlock(TFBlocks.POTTED_DEAD_THORN.get(), models().getExistingFile(prefix("block/potted_dead_thorn")));

		builtinEntity(TFBlocks.TWILIGHT_OAK_SIGN.get(), "twilightforest:block/wood/planks_twilight_oak_0");
		builtinEntity(TFBlocks.TWILIGHT_WALL_SIGN.get(), "twilightforest:block/wood/planks_twilight_oak_0");
		builtinEntity(TFBlocks.CANOPY_SIGN.get(), "twilightforest:block/wood/planks_canopy_0");
		builtinEntity(TFBlocks.CANOPY_WALL_SIGN.get(), "twilightforest:block/wood/planks_canopy_0");
		builtinEntity(TFBlocks.MANGROVE_SIGN.get(), "twilightforest:block/wood/planks_mangrove_0");
		builtinEntity(TFBlocks.MANGROVE_WALL_SIGN.get(), "twilightforest:block/wood/planks_mangrove_0");
		builtinEntity(TFBlocks.DARKWOOD_SIGN.get(), "twilightforest:block/wood/planks_darkwood_0");
		builtinEntity(TFBlocks.DARKWOOD_WALL_SIGN.get(), "twilightforest:block/wood/planks_darkwood_0");
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
		builtinEntity(TFBlocks.DARKWOOD_CHEST.get(), "twilightforest:block/wood/planks_darkwood_0");
		builtinEntity(TFBlocks.TIME_CHEST.get(), "twilightforest:block/wood/planks_time_0");
		builtinEntity(TFBlocks.TRANSFORMATION_CHEST.get(), "twilightforest:block/wood/planks_trans_0");
		builtinEntity(TFBlocks.MINING_CHEST.get(), "twilightforest:block/wood/planks_mine_0");
		builtinEntity(TFBlocks.SORTING_CHEST.get(), "twilightforest:block/wood/planks_sort_0");

		casketStuff();
		stonePillar();
		candelabra();
	}

	private void registerForceFields() {
		ImmutableList<RegistryObject<Block>> forceFields = ImmutableList.of(TFBlocks.PINK_FORCE_FIELD, TFBlocks.BLUE_FORCE_FIELD, TFBlocks.GREEN_FORCE_FIELD, TFBlocks.VIOLET_FORCE_FIELD, TFBlocks.ORANGE_FORCE_FIELD);

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
					.part().modelFile(post).uvLock(true).addModel().end()
					.part().modelFile(side).uvLock(true).addModel().condition(PipeBlock.NORTH, true).end()
					.part().modelFile(noside).uvLock(true).addModel().condition(PipeBlock.NORTH, false).end()
					.part().modelFile(side).uvLock(true).rotationY(90).addModel().condition(PipeBlock.EAST, true).end()
					.part().modelFile(nosidealt).uvLock(true).addModel().condition(PipeBlock.EAST, false).end()
					.part().modelFile(sidealt).uvLock(true).addModel().condition(PipeBlock.SOUTH, true).end()
					.part().modelFile(nosidealt).uvLock(true).rotationY(90).addModel().condition(PipeBlock.SOUTH, false).end()
					.part().modelFile(sidealt).uvLock(true).rotationY(90).addModel().condition(PipeBlock.WEST, true).end()
					.part().modelFile(noside).uvLock(true).rotationY(270).addModel().condition(PipeBlock.WEST, false).end();
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
		getVariantBuilder(TFBlocks.NAGASTONE.get()).forAllStates(s -> {
			switch (s.getValue(NagastoneBlock.VARIANT)) {
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

		horizontalBlock(TFBlocks.NAGASTONE_HEAD.get(), models().getExistingFile(prefix("block/" + TFBlocks.NAGASTONE_HEAD.getId().getPath())));
		nagastonePillar(TFBlocks.NAGASTONE_PILLAR.get(), "");
		nagastonePillar(TFBlocks.MOSSY_NAGASTONE_PILLAR.get(), "_mossy");
		nagastonePillar(TFBlocks.CRACKED_NAGASTONE_PILLAR.get(), "_weathered");
		etchedNagastone(TFBlocks.ETCHED_NAGASTONE.get(), "");
		etchedNagastone(TFBlocks.MOSSY_ETCHED_NAGASTONE.get(), "_mossy");
		etchedNagastone(TFBlocks.CRACKED_ETCHED_NAGASTONE.get(), "_weathered");
		stairsBlock(TFBlocks.NAGASTONE_STAIRS_LEFT.get(), prefix("block/etched_nagastone_left"), prefix("block/stone_tiles"), prefix("block/nagastone_bare"));
		stairsBlock(TFBlocks.NAGASTONE_STAIRS_RIGHT.get(), prefix("block/etched_nagastone_right"), prefix("block/stone_tiles"), prefix("block/nagastone_bare"));
		stairsBlock(TFBlocks.MOSSY_NAGASTONE_STAIRS_LEFT.get(), prefix("block/etched_nagastone_left_mossy"), prefix("block/stone_tiles_mossy"), prefix("block/nagastone_bare_mossy"));
		stairsBlock(TFBlocks.MOSSY_NAGASTONE_STAIRS_RIGHT.get(), prefix("block/etched_nagastone_right_mossy"), prefix("block/stone_tiles_mossy"), prefix("block/nagastone_bare_mossy"));
		stairsBlock(TFBlocks.CRACKED_NAGASTONE_STAIRS_LEFT.get(), prefix("block/etched_nagastone_left_weathered"), prefix("block/stone_tiles_weathered"), prefix("block/nagastone_bare_weathered"));
		stairsBlock(TFBlocks.CRACKED_NAGASTONE_STAIRS_RIGHT.get(), prefix("block/etched_nagastone_right_weathered"), prefix("block/stone_tiles_weathered"), prefix("block/nagastone_bare_weathered"));
	}

	private void nagastonePillar(Block b, String suffix) {
		ResourceLocation side = prefix("block/nagastone_pillar_side" + suffix);
		ResourceLocation end = prefix("block/nagastone_pillar_end" + suffix);
		ResourceLocation alt = prefix("block/nagastone_pillar_side" + suffix + "_alt");
		ModelFile model = models().cubeColumn(b.getRegistryName().getPath(), side, end);
		ModelFile reversed = models().cubeColumn(b.getRegistryName().getPath() + "_reversed", alt, end);
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

	private void casketStuff() {
		var builder = getVariantBuilder(TFBlocks.KEEPSAKE_CASKET.get());

		var empty = models().getBuilder(TFBlocks.KEEPSAKE_CASKET.get().getRegistryName().getPath()).parent(new ModelFile.UncheckedModelFile("builtin/entity")).texture("particle", "minecraft:block/netherite_block");
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
		simpleBlock(TFBlocks.SMOKER.get(), new ConfiguredModel(models().getExistingFile(prefix("block/jet"))));
		simpleBlock(TFBlocks.FIRE_JET.get(), new ConfiguredModel(models().getExistingFile(prefix("block/jet"))));

		ModelFile smokerOff = models().withExistingParent(TFBlocks.ENCASED_SMOKER.getId().getPath(), prefix("block/util/cube_bottom_top_3_layer"))
						.texture("top", prefix("block/towerdev_ghasttraplid_off"))
						.texture("side", prefix("block/towerdev_smoker_off"))
						.texture("bottom", blockTexture(TFBlocks.ENCASED_TOWERWOOD.get()))
						.texture("top2", prefix("block/tower_device_level_2/towerdev_ghasttraplid_off_1"))
						.texture("side2", prefix("block/tower_device_level_1/towerdev_smoker_1"))
						.texture("top3", prefix("block/tower_device_level_2/towerdev_ghasttraplid_off_1"))
						.texture("side3", prefix("block/tower_device_level_2/towerdev_smoker_off_1"));

		ModelFile smokerOn = models().withExistingParent(TFBlocks.ENCASED_SMOKER.getId().getPath() + "_on", prefix("block/util/cube_bottom_top_3_layer"))
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

		ModelFile encasedJetOff = models().withExistingParent(TFBlocks.ENCASED_FIRE_JET.getId().getPath(), prefix("block/util/cube_bottom_top_3_layer"))
						.texture("top", prefix("block/towerdev_ghasttraplid_off"))
						.texture("side", prefix("block/towerdev_firejet_off"))
						.texture("bottom", blockTexture(TFBlocks.ENCASED_TOWERWOOD.get()))
						.texture("top2", prefix("block/tower_device_level_2/towerdev_ghasttraplid_off_1"))
						.texture("side2", prefix("block/tower_device_level_1/towerdev_firejet_1"))
						.texture("top3", prefix("block/tower_device_level_2/towerdev_ghasttraplid_off_1"))
						.texture("side3", prefix("block/tower_device_level_2/towerdev_firejet_off_1"));

		ModelFile encasedJetOn = models().withExistingParent(TFBlocks.ENCASED_FIRE_JET.getId().getPath() + "_on", prefix("block/util/cube_bottom_top_3_layer"))
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
						.with(FireJetBlock.STATE, FireJetVariant.POPPING).setModels(new ConfiguredModel(encasedJetOn));
		getVariantBuilder(TFBlocks.ENCASED_FIRE_JET.get()).partialState()
						.with(FireJetBlock.STATE, FireJetVariant.FLAME).setModels(new ConfiguredModel(encasedJetOn));
	}

	private void registerPlantBlocks() {
		simpleBlock(TFBlocks.MOSS_PATCH.get(), new ConfiguredModel(new ModelFile.UncheckedModelFile(TwilightForestMod.prefix("block/moss_patch"))));
		simpleBlockExisting(TFBlocks.MAYAPPLE.get());
		//ConfiguredModel[] cloverModels = new ConfiguredModel[4];
		//for (int i = 0; i < 4; i++) {
		//	ModelFile model = models().withExistingParent(TFBlocks.CLOVER_PATCH.getId().getPath() + "_" + i, prefix("block/util/flat_tex"))
		//					.texture("particle", prefix("block/cloverpatch"))
		//					.texture("texture", prefix("block/patch/clover_" + i))
		//					.texture("ctm", prefix("block/patch/clover_" + i + "_ctm"));
		//	cloverModels[i] = new ConfiguredModel(model);
		//}
		//simpleBlock(TFBlocks.CLOVER_PATCH.get(), cloverModels);
		simpleBlock(TFBlocks.CLOVER_PATCH.get(), new ConfiguredModel(new ModelFile.UncheckedModelFile(TwilightForestMod.prefix("block/clover_patch"))));
		simpleBlock(TFBlocks.FIDDLEHEAD.get(), models().withExistingParent(TFBlocks.FIDDLEHEAD.getId().getPath(), "block/tinted_cross")
						.texture("cross", blockTexture(TFBlocks.FIDDLEHEAD.get())));
		simpleBlock(TFBlocks.MUSHGLOOM.get(), models().withExistingParent(TFBlocks.MUSHGLOOM.getId().getPath(), prefix("block/util/cross_2_layer"))
						.texture("cross", blockTexture(TFBlocks.MUSHGLOOM.get()))
						.texture("cross2", prefix("block/" + TFBlocks.MUSHGLOOM.getId().getPath() + "_head")));

		ModelFile berry = models().withExistingParent(TFBlocks.TORCHBERRY_PLANT.getId().getPath(), prefix("block/util/cross_2_layer"))
				.texture("cross", blockTexture(TFBlocks.TORCHBERRY_PLANT.get()))
				.texture("cross2", prefix("block/" + TFBlocks.TORCHBERRY_PLANT.getId().getPath() + "_glow"));
		ModelFile noBerry = models().withExistingParent(TFBlocks.TORCHBERRY_PLANT.getId().getPath() + "_no_berries", new ResourceLocation("block/cross"))
				.texture("cross", blockTexture(TFBlocks.TORCHBERRY_PLANT.get()));
		getVariantBuilder(TFBlocks.TORCHBERRY_PLANT.get()).forAllStates(s -> ConfiguredModel.builder().modelFile(s.getValue(TorchberryPlantBlock.HAS_BERRIES) ? berry : noBerry).build());

		simpleBlockExisting(TFBlocks.ROOT_STRAND.get());
		simpleBlockExisting(TFBlocks.FALLEN_LEAVES.get());
	}

	private void registerWoodBlocks() {
		logWoodSapling(TFBlocks.TWILIGHT_OAK_LOG.get(), TFBlocks.STRIPPED_TWILIGHT_OAK_LOG.get(), TFBlocks.TWILIGHT_OAK_WOOD.get(), TFBlocks.STRIPPED_TWILIGHT_OAK_WOOD.get(), TFBlocks.TWILIGHT_OAK_SAPLING.get());
		plankBlocks("twilight_oak", TFBlocks.TWILIGHT_OAK_PLANKS.get(), TFBlocks.TWILIGHT_OAK_SLAB.get(), TFBlocks.TWILIGHT_OAK_STAIRS.get(), TFBlocks.TWILIGHT_OAK_BUTTON.get(), TFBlocks.TWILIGHT_OAK_FENCE.get(), TFBlocks.TWILIGHT_OAK_GATE.get(), TFBlocks.TWILIGHT_OAK_PLATE.get(), TFBlocks.TWILIGHT_OAK_DOOR.get(), TFBlocks.TWILIGHT_OAK_TRAPDOOR.get(), TFBlocks.TWILIGHT_OAK_BANISTER.get());
		singleBlockBoilerPlate(TFBlocks.TWILIGHT_OAK_LEAVES.get(), "block/leaves", m -> m.texture("all", "minecraft:block/oak_leaves"));

		ResourceLocation rainboakSaplTex = prefix("block/" + TFBlocks.RAINBOW_OAK_SAPLING.getId().getPath());
		simpleBlock(TFBlocks.RAINBOW_OAK_SAPLING.get(), models().cross(TFBlocks.RAINBOW_OAK_SAPLING.getId().getPath(), rainboakSaplTex));
		singleBlockBoilerPlate(TFBlocks.RAINBOW_OAK_LEAVES.get(), "block/leaves", m -> m.texture("all", "minecraft:block/oak_leaves"));

		logWoodSapling(TFBlocks.CANOPY_LOG.get(), TFBlocks.STRIPPED_CANOPY_LOG.get(), TFBlocks.CANOPY_WOOD.get(), TFBlocks.STRIPPED_CANOPY_WOOD.get(), TFBlocks.CANOPY_SAPLING.get());
		plankBlocks("canopy", TFBlocks.CANOPY_PLANKS.get(), TFBlocks.CANOPY_SLAB.get(), TFBlocks.CANOPY_STAIRS.get(), TFBlocks.CANOPY_BUTTON.get(), TFBlocks.CANOPY_FENCE.get(), TFBlocks.CANOPY_GATE.get(), TFBlocks.CANOPY_PLATE.get(), TFBlocks.CANOPY_DOOR.get(), TFBlocks.CANOPY_TRAPDOOR.get(), TFBlocks.CANOPY_BANISTER.get());
		singleBlockBoilerPlate(TFBlocks.CANOPY_LEAVES.get(), "block/leaves", m -> m.texture("all", "minecraft:block/spruce_leaves"));

		logWoodSapling(TFBlocks.MANGROVE_LOG.get(), TFBlocks.STRIPPED_MANGROVE_LOG.get(), TFBlocks.MANGROVE_WOOD.get(), TFBlocks.STRIPPED_MANGROVE_WOOD.get(), TFBlocks.MANGROVE_SAPLING.get());
		plankBlocks("mangrove", TFBlocks.MANGROVE_PLANKS.get(), TFBlocks.MANGROVE_SLAB.get(), TFBlocks.MANGROVE_STAIRS.get(), TFBlocks.MANGROVE_BUTTON.get(), TFBlocks.MANGROVE_FENCE.get(), TFBlocks.MANGROVE_GATE.get(), TFBlocks.MANGROVE_PLATE.get(), TFBlocks.MANGROVE_DOOR.get(), TFBlocks.MANGROVE_TRAPDOOR.get(), TFBlocks.MANGROVE_BANISTER.get());
		singleBlockBoilerPlate(TFBlocks.MANGROVE_LEAVES.get(), "block/leaves", m -> m.texture("all", "minecraft:block/birch_leaves"));

		logWoodSapling(TFBlocks.DARK_LOG.get(), TFBlocks.STRIPPED_DARK_LOG.get(), TFBlocks.DARK_WOOD.get(), TFBlocks.STRIPPED_DARK_WOOD.get(), TFBlocks.DARKWOOD_SAPLING.get());
		plankBlocks("darkwood", TFBlocks.DARK_PLANKS.get(), TFBlocks.DARK_SLAB.get(), TFBlocks.DARK_STAIRS.get(), TFBlocks.DARK_BUTTON.get(), TFBlocks.DARK_FENCE.get(), TFBlocks.DARK_GATE.get(), TFBlocks.DARK_PLATE.get(), TFBlocks.DARK_DOOR.get(), TFBlocks.DARK_TRAPDOOR.get(), TFBlocks.DARKWOOD_BANISTER.get());
		singleBlockBoilerPlate(TFBlocks.DARK_LEAVES.get(), "block/leaves", m -> m.texture("all", "block/darkwood_leaves"));
		singleBlockBoilerPlate(TFBlocks.HARDENED_DARK_LEAVES.get(), "block/leaves", m -> m.texture("all", "block/darkwood_leaves"));

		logWoodSapling(TFBlocks.TIME_LOG.get(), TFBlocks.STRIPPED_TIME_LOG.get(), TFBlocks.TIME_WOOD.get(), TFBlocks.STRIPPED_TIME_WOOD.get(), TFBlocks.TIME_SAPLING.get());
		plankBlocks("time", TFBlocks.TIME_PLANKS.get(), TFBlocks.TIME_SLAB.get(), TFBlocks.TIME_STAIRS.get(), TFBlocks.TIME_BUTTON.get(), TFBlocks.TIME_FENCE.get(), TFBlocks.TIME_GATE.get(), TFBlocks.TIME_PLATE.get(), TFBlocks.TIME_DOOR.get(), TFBlocks.TIME_TRAPDOOR.get(), TFBlocks.TIME_BANISTER.get());
		singleBlockBoilerPlate(TFBlocks.TIME_LEAVES.get(), "block/leaves", m -> m.texture("all", "block/time_leaves"));
		magicLogCore(TFBlocks.TIME_LOG_CORE.get());

		logWoodSapling(TFBlocks.TRANSFORMATION_LOG.get(), TFBlocks.STRIPPED_TRANSFORMATION_LOG.get(), TFBlocks.TRANSFORMATION_WOOD.get(), TFBlocks.STRIPPED_TRANSFORMATION_WOOD.get(), TFBlocks.TRANSFORMATION_SAPLING.get());
		plankBlocks("trans", TFBlocks.TRANSFORMATION_PLANKS.get(), TFBlocks.TRANSFORMATION_SLAB.get(), TFBlocks.TRANSFORMATION_STAIRS.get(), TFBlocks.TRANSFORMATION_BUTTON.get(), TFBlocks.TRANSFORMATION_FENCE.get(), TFBlocks.TRANSFORMATION_GATE.get(), TFBlocks.TRANSFORMATION_PLATE.get(), TFBlocks.TRANSFORMATION_DOOR.get(), TFBlocks.TRANSFORMATION_TRAPDOOR.get(), TFBlocks.TRANSFORMATION_BANISTER.get());
		singleBlockBoilerPlate(TFBlocks.TRANSFORMATION_LEAVES.get(), "block/leaves", m -> m.texture("all", "block/transformation_leaves"));
		magicLogCore(TFBlocks.TRANSFORMATION_LOG_CORE.get());

		logWoodSapling(TFBlocks.MINING_LOG.get(), TFBlocks.STRIPPED_MINING_LOG.get(), TFBlocks.MINING_WOOD.get(), TFBlocks.STRIPPED_MINING_WOOD.get(), TFBlocks.MINING_SAPLING.get());
		plankBlocks("mine", TFBlocks.MINING_PLANKS.get(), TFBlocks.MINING_SLAB.get(), TFBlocks.MINING_STAIRS.get(), TFBlocks.MINING_BUTTON.get(), TFBlocks.MINING_FENCE.get(), TFBlocks.MINING_GATE.get(), TFBlocks.MINING_PLATE.get(), TFBlocks.MINING_DOOR.get(), TFBlocks.MINING_TRAPDOOR.get(), TFBlocks.MINING_BANISTER.get());
		singleBlockBoilerPlate(TFBlocks.MINING_LEAVES.get(), "block/leaves", m -> m.texture("all", "block/mining_leaves"));
		magicLogCore(TFBlocks.MINING_LOG_CORE.get());

		logWoodSapling(TFBlocks.SORTING_LOG.get(), TFBlocks.STRIPPED_SORTING_LOG.get(), TFBlocks.SORTING_WOOD.get(), TFBlocks.STRIPPED_SORTING_WOOD.get(), TFBlocks.SORTING_SAPLING.get());
		plankBlocks("sort", TFBlocks.SORTING_PLANKS.get(), TFBlocks.SORTING_SLAB.get(), TFBlocks.SORTING_STAIRS.get(), TFBlocks.SORTING_BUTTON.get(), TFBlocks.SORTING_FENCE.get(), TFBlocks.SORTING_GATE.get(), TFBlocks.SORTING_PLATE.get(), TFBlocks.SORTING_DOOR.get(), TFBlocks.SORTING_TRAPDOOR.get(), TFBlocks.SORTING_BANISTER.get());
		singleBlockBoilerPlate(TFBlocks.SORTING_LEAVES.get(), "block/leaves", m -> m.texture("all", "block/sorting_leaves"));
		magicLogCore(TFBlocks.SORTING_LOG_CORE.get());

		banisterVanilla(TFBlocks.OAK_BANISTER.get(), "oak_planks");
		banisterVanilla(TFBlocks.SPRUCE_BANISTER.get(), "spruce_planks");
		banisterVanilla(TFBlocks.BIRCH_BANISTER.get(), "birch_planks");
		banisterVanilla(TFBlocks.JUNGLE_BANISTER.get(), "jungle_planks");
		banisterVanilla(TFBlocks.ACACIA_BANISTER.get(), "acacia_planks");
		banisterVanilla(TFBlocks.DARK_OAK_BANISTER.get(), "dark_oak_planks");
		banisterVanilla(TFBlocks.CRIMSON_BANISTER.get(), "crimson_planks");
		banisterVanilla(TFBlocks.WARPED_BANISTER.get(), "warped_planks");

		final ResourceLocation MOSS = TwilightForestMod.prefix("block/mosspatch");
		final ResourceLocation MOSS_OVERHANG = TwilightForestMod.prefix("block/moss_overhang");
		final ResourceLocation TALL_GRASS = new ResourceLocation("block/grass");
		final ResourceLocation SNOW = new ResourceLocation("block/snow");
		final ResourceLocation SNOW_OVERHANG = TwilightForestMod.prefix("block/snow_overhang");

		final ModelFile EMPTY_LOG = this.buildHorizontalHollowLog(false, false);
		final ModelFile LAYERED_LOG = this.buildHorizontalHollowLog(true, false);
		final ModelFile MOSS_LOG_GRASS = models().getBuilder("hollow_log_moss_grass").parent(this.buildHorizontalHollowLog(true, true)).texture("carpet", MOSS).texture("overhang", MOSS_OVERHANG).texture("plant", TALL_GRASS);
		final ModelFile MOSS_LOG = models().getBuilder("hollow_log_moss").parent(LAYERED_LOG).texture("carpet", MOSS).texture("overhang", MOSS_OVERHANG);
		final ModelFile SNOW_LOG = models().getBuilder("hollow_log_snow").parent(LAYERED_LOG).texture("carpet", SNOW).texture("overhang", SNOW_OVERHANG);
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
		ResourceLocation topTex = prefix("block/" + b.getRegistryName().getPath().replace("_core", "_top"));
		ModelFile off = models().cubeColumn(b.getRegistryName().getPath(), blockTexture(b), topTex);
		ModelFile on = models().cubeColumn(b.getRegistryName().getPath() + "_on", prefix("block/" + b.getRegistryName().getPath() + "_on"), topTex);
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
						.part().modelFile(main).addModel().condition(CastleDoorBlock.VANISHED, false).end();
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

	private void logWoodSapling(RotatedPillarBlock log, RotatedPillarBlock slog, RotatedPillarBlock wood, RotatedPillarBlock swood, Block sapling) {
		logBlock(log);
		logBlock(slog);
		ResourceLocation sideTex = blockTexture(log);
		axisBlock(wood, sideTex, sideTex);
		ResourceLocation sSideTex = blockTexture(slog);
		axisBlock(swood, sSideTex, sSideTex);

		ResourceLocation saplingTex = prefix("block/" + sapling.getRegistryName().getPath());
		simpleBlock(sapling, models().cross(sapling.getRegistryName().getPath(), saplingTex));
	}

	private void plankBlocks(String variant, Block plank, Block slab, StairBlock stair, Block button, Block fence, Block gate, Block plate, DoorBlock door, TrapDoorBlock trapdoor, BanisterBlock banister) {
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
		banister(banister, plankTexName);
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
			if (state.getValue(FenceGateBlock.IN_WALL)) {
				model0 = wall0;
				model1 = wall1;
				model2 = wall2;
				model3 = wall3;
			}
			if (state.getValue(FenceGateBlock.OPEN)) {
				model0 = model0 == wall0 ? wallOpen0 : open0;
				model1 = model1 == wall1 ? wallOpen1 : open1;
				model2 = model2 == wall2 ? wallOpen2 : open2;
				model3 = model3 == wall3 ? wallOpen3 : open3;
			}
			return ConfiguredModel.builder()
							.weight(10).modelFile(model0)
							.rotationY((int) state.getValue(HorizontalDirectionalBlock.FACING).toYRot())
							.uvLock(true).nextModel()

							.weight(10).modelFile(model1)
							.rotationY((int) state.getValue(HorizontalDirectionalBlock.FACING).toYRot())
							.uvLock(true).nextModel()

							.weight(1).modelFile(model2)
							.rotationY((int) state.getValue(HorizontalDirectionalBlock.FACING).toYRot())
							.uvLock(true).nextModel()

							.weight(1).modelFile(model3)
							.rotationY((int) state.getValue(HorizontalDirectionalBlock.FACING).toYRot())
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
		PipeBlock.PROPERTY_BY_DIRECTION.forEach((dir, value) -> {
			if (dir.getAxis().isHorizontal()) {
				builder.part()
						.weight(10).modelFile(side0).rotationY((((int) dir.toYRot()) + 180) % 360).uvLock(true).nextModel()
						.weight(10).modelFile(side1).rotationY((((int) dir.toYRot()) + 180) % 360).uvLock(true).nextModel()
						.weight(1).modelFile(side2).rotationY((((int) dir.toYRot()) + 180) % 360).uvLock(true).nextModel()
						.weight(1).modelFile(side3).rotationY((((int) dir.toYRot()) + 180) % 360).uvLock(true)
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
			ModelFile model0 = state.getValue(ButtonBlock.POWERED) ? pressed0 : unpressed0;
			ModelFile model1 = state.getValue(ButtonBlock.POWERED) ? pressed1 : unpressed1;
			ModelFile model2 = state.getValue(ButtonBlock.POWERED) ? pressed2 : unpressed2;
			ModelFile model3 = state.getValue(ButtonBlock.POWERED) ? pressed3 : unpressed3;
			int rotX = 0;
			switch (state.getValue(FaceAttachedHorizontalDirectionalBlock.FACE)) {
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
			if (state.getValue(FaceAttachedHorizontalDirectionalBlock.FACE) == AttachFace.CEILING)  {
				switch (state.getValue(HorizontalDirectionalBlock.FACING)) {
				case NORTH: rotY = 180; break;
				case SOUTH: rotY = 0; break;
				case WEST: rotY = 90; break;
				case EAST: rotY = 270; break;
				}
			} else {
				switch (state.getValue(HorizontalDirectionalBlock.FACING)) {
				case NORTH: rotY = 0; break;
				case SOUTH: rotY = 180; break;
				case WEST: rotY = 270; break;
				case EAST: rotY = 90; break;
				}
			}
			boolean uvlock = state.getValue(FaceAttachedHorizontalDirectionalBlock.FACE) == AttachFace.WALL;

			return ConfiguredModel.builder()
							.weight(10).uvLock(uvlock).rotationX(rotX).rotationY(rotY).modelFile(model0).nextModel()
							.weight(10).uvLock(uvlock).rotationX(rotX).rotationY(rotY).modelFile(model1).nextModel()
							.weight(1).uvLock(uvlock).rotationX(rotX).rotationY(rotY).modelFile(model2).nextModel()
							.weight(1).uvLock(uvlock).rotationX(rotX).rotationY(rotY).modelFile(model3)
							.build();
		});
	}

	private void woodStairs(StairBlock block, String texName) {
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
							Direction facing = state.getValue(StairBlock.FACING);
							Half half = state.getValue(StairBlock.HALF);
							StairsShape shape = state.getValue(StairBlock.SHAPE);
							int yRot = (int) facing.getClockWise().toYRot(); // Stairs model is rotated 90 degrees clockwise for some reason
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
						}, StairBlock.WATERLOGGED);
	}

	private void banister(BanisterBlock banister, String texName) {
		ResourceLocation tex0 = prefix("block/wood/" + texName + "_0");
		ResourceLocation tex1 = prefix("block/wood/" + texName + "_1");
		ResourceLocation tex2 = prefix("block/wood/" + texName + "_2");
		ResourceLocation tex3 = prefix("block/wood/" + texName + "_3");

		getVariantBuilder(banister).forAllStatesExcept(state -> {
			Direction facing = state.getValue(BanisterBlock.FACING);
			int yRot = (int) facing.toYRot();
			String extended = state.getValue(BanisterBlock.EXTENDED) ? "_extended" : "";
			String variant = state.getValue(BanisterBlock.SHAPE).getSerializedName() + extended;
			String newModelName = banister.getRegistryName().getPath() + "_" + variant;

			ConfiguredModel[] tall = ConfiguredModel.builder()
					.weight(10).modelFile(models().withExistingParent(newModelName, TwilightForestMod.prefix("banister_" + variant)).texture("texture", tex0)).rotationY(yRot).nextModel()
					.weight(10).modelFile(models().withExistingParent(newModelName + "_1", TwilightForestMod.prefix("banister_" + variant)).texture("texture", tex1)).rotationY(yRot).nextModel()
					.weight(1).modelFile(models().withExistingParent(newModelName + "_2", TwilightForestMod.prefix("banister_" + variant)).texture("texture", tex2)).rotationY(yRot).nextModel()
					.weight(1).modelFile(models().withExistingParent(newModelName + "_3", TwilightForestMod.prefix("banister_" + variant)).texture("texture", tex3)).rotationY(yRot).build();

			return tall;
		}, BanisterBlock.WATERLOGGED);
	}

	private void banisterVanilla(BanisterBlock banister, String texName) {
		ResourceLocation tex0 = new ResourceLocation("block/" + texName);

		getVariantBuilder(banister).forAllStatesExcept(state -> {
			Direction facing = state.getValue(BanisterBlock.FACING);
			int yRot = (int) facing.toYRot();
			String extended = state.getValue(BanisterBlock.EXTENDED) ? "_extended" : "";
			String variant = state.getValue(BanisterBlock.SHAPE).getSerializedName() + extended;

			ConfiguredModel[] tall = ConfiguredModel.builder()
					.modelFile(models().withExistingParent(banister.getRegistryName().getPath() + "_" + variant, TwilightForestMod.prefix("banister_" + variant)).texture("texture", tex0)).rotationY(yRot).build();

			return tall;
		}, BanisterBlock.WATERLOGGED);
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

	private void stonePillar() {
		ModelFile main_x = models().withExistingParent("pillar_main_x", prefix("block/pillar/pillar_12_ctm")).texture("side_x", prefix("block/stone_twist/twist_x")).texture("side_z", prefix("block/stone_twist/twist_x"));
		ModelFile bottom_x = models().withExistingParent("pillar_bottom_x", prefix("block/pillar/pillar_bottom")).texture("bottom_x", prefix("block/stone_twist/cap/y_y_bottom")).texture("bottom_z", prefix("block/stone_twist/cap/y_y_bottom")).texture("bottom_cap", prefix("block/stone_twist/cap/end_bottom_x"));
		ModelFile top_x = models().withExistingParent("pillar_top_x", prefix("block/pillar/pillar_top")).texture("top_x", prefix("block/stone_twist/cap/y_y_top")).texture("top_z", prefix("block/stone_twist/cap/y_y_top")).texture("top_cap", prefix("block/stone_twist/cap/end_top_x"));
		ModelFile main_y = models().withExistingParent("pillar_main_y", prefix("block/pillar/pillar_12_ctm")).texture("side_x", prefix("block/stone_twist/twist_y")).texture("side_z", prefix("block/stone_twist/twist_y"));
		ModelFile bottom_y = models().withExistingParent("pillar_bottom_y", prefix("block/pillar/pillar_bottom")).texture("bottom_x", prefix("block/stone_twist/cap/y_y_bottom")).texture("bottom_z", prefix("block/stone_twist/cap/y_y_bottom")).texture("bottom_cap", prefix("block/stone_twist/cap/end_bottom_y"));
		ModelFile top_y = models().withExistingParent("pillar_top_y", prefix("block/pillar/pillar_top")).texture("top_x", prefix("block/stone_twist/cap/y_y_top")).texture("top_z", prefix("block/stone_twist/cap/y_y_top")).texture("top_cap", prefix("block/stone_twist/cap/end_top_y"));
		ModelFile main_z = models().withExistingParent("pillar_main_z", prefix("block/pillar/pillar_12_ctm")).texture("side_x", prefix("block/stone_twist/twist_x")).texture("side_z", prefix("block/stone_twist/twist_y"));
		ModelFile bottom_z = models().withExistingParent("pillar_bottom_z", prefix("block/pillar/pillar_bottom")).texture("bottom_x", prefix("block/stone_twist/cap/y_y_bottom")).texture("bottom_z", prefix("block/stone_twist/cap/y_y_bottom")).texture("bottom_cap", prefix("block/stone_twist/cap/end_bottom_z"));
		ModelFile top_z = models().withExistingParent("pillar_top_z", prefix("block/pillar/pillar_top")).texture("top_x", prefix("block/stone_twist/cap/y_y_top")).texture("top_z", prefix("block/stone_twist/cap/y_y_top")).texture("top_cap", prefix("block/stone_twist/cap/end_top_z"));
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
		getVariantBuilder(TFBlocks.SLIDER.get()).forAllStates(state -> {
			switch (state.getValue(SliderBlock.AXIS)) {
				case X:
					switch(state.getValue(SliderBlock.DELAY)) {
						case 0:
						case 1:
						case 2:
						default:
							return ConfiguredModel.builder().modelFile(horizSlider).rotationX(90).rotationY(90).build();
					}
				case Y:
				default:
					switch(state.getValue(SliderBlock.DELAY)) {
						case 0:
						case 1:
						case 2:
						default:
							return ConfiguredModel.builder().modelFile(slider).build();
					}
				case Z:
					switch(state.getValue(SliderBlock.DELAY)) {
						case 0:
						case 1:
						case 2:
						default:
							return ConfiguredModel.builder().modelFile(horizSlider).rotationX(90).build();
					}

			}
		});
	}

	private void towerBlocks() {
		ResourceLocation cube3 = prefix("block/util/cube_all_3_layer");
		ResourceLocation cubeBt3 = prefix("block/util/cube_bottom_top_3_layer");
		ResourceLocation cube2NoShade = prefix("block/util/cube_all_2_layer_no_shade");
		ResourceLocation fourCube = prefix("block/util/4_cubed");

		ModelFile reappear = models().withExistingParent(TFBlocks.REAPPEARING_BLOCK.getId().getPath(), cube3)
						.texture("all", prefix("block/towerdev_reappearing_off"))
						.texture("all2", prefix("block/tower_device_level_1/towerdev_reappearing_off_1"))
						.texture("all3", prefix("block/tower_device_level_2/towerdev_reappearing_off_2"));
		ModelFile reappearActive = models().withExistingParent(TFBlocks.REAPPEARING_BLOCK.getId().getPath() + "_active", cube3)
						.texture("all", prefix("block/towerdev_reappearing_on"))
						.texture("all2", prefix("block/tower_device_level_1/towerdev_reappearing_on_1"))
						.texture("all3", prefix("block/tower_device_level_2/towerdev_reappearing_on_2"));
		ModelFile reappearVanished = models().withExistingParent(TFBlocks.REAPPEARING_BLOCK.getId().getPath() + "_vanished", fourCube)
						.texture("all", prefix("block/towerdev_reappearing_trace_off"));
		ModelFile reappearVanishedActive = models().withExistingParent(TFBlocks.REAPPEARING_BLOCK.getId().getPath() + "_vanished_active", fourCube)
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

		ModelFile vanish = models().withExistingParent(TFBlocks.VANISHING_BLOCK.getId().getPath(), cube3)
						.texture("all", prefix("block/towerdev_vanish_off"))
						.texture("all2", prefix("block/tower_device_level_1/towerdev_vanish_off_1"))
						.texture("all3", prefix("block/tower_device_level_2/towerdev_vanish_off_2"));
		ModelFile vanishActive = models().withExistingParent(TFBlocks.VANISHING_BLOCK.getId().getPath() + "_active", cube3)
						.texture("all", prefix("block/towerdev_vanish_on"))
						.texture("all2", prefix("block/tower_device_level_1/towerdev_vanish_on_1"))
						.texture("all3", prefix("block/tower_device_level_2/towerdev_vanish_on_2"));
		getVariantBuilder(TFBlocks.VANISHING_BLOCK.get()).partialState()
						.with(VanishingBlock.ACTIVE, false).setModels(new ConfiguredModel(vanish));
		getVariantBuilder(TFBlocks.VANISHING_BLOCK.get()).partialState()
						.with(VanishingBlock.ACTIVE, true).setModels(new ConfiguredModel(vanishActive));

		ModelFile vanishLocked = models().withExistingParent(TFBlocks.LOCKED_VANISHING_BLOCK.getId().getPath(), cube3)
						.texture("all", prefix("block/towerdev_lock_on"))
						.texture("all2", prefix("block/tower_device_level_1/towerdev_lock_on_1"))
						.texture("all3", prefix("block/tower_device_level_2/towerdev_lock_on_2"));
		ModelFile vanishUnlocked = models().withExistingParent(TFBlocks.LOCKED_VANISHING_BLOCK.getId().getPath() + "_unlocked", cube3)
						.texture("all", prefix("block/towerdev_lock_off"))
						.texture("all2", prefix("block/tower_device_level_1/towerdev_lock_off_1"))
						.texture("all3", prefix("block/tower_device_level_2/towerdev_lock_off_2"));
		getVariantBuilder(TFBlocks.LOCKED_VANISHING_BLOCK.get()).partialState()
						.with(LockedVanishingBlock.LOCKED, true).setModels(new ConfiguredModel(vanishLocked));
		getVariantBuilder(TFBlocks.LOCKED_VANISHING_BLOCK.get()).partialState()
						.with(LockedVanishingBlock.LOCKED, false).setModels(new ConfiguredModel(vanishUnlocked));

		ModelFile ghastTrap = models().withExistingParent(TFBlocks.GHAST_TRAP.getId().getPath(), cubeBt3)
						.texture("top", prefix("block/towerdev_ghasttraplid_off"))
						.texture("side", prefix("block/towerdev_ghasttrap_off"))
						.texture("bottom", prefix("block/encased_towerwood"))
						.texture("top2", prefix("block/tower_device_level_2/towerdev_ghasttraplid_off_1"))
						.texture("side2", prefix("block/tower_device_level_1/towerdev_ghasttrap_off_1"))
						.texture("top3", prefix("block/tower_device_level_2/towerdev_ghasttraplid_off_1"))
						.texture("side3", prefix("block/tower_device_level_2/towerdev_ghasttrap_off_2"));
		ModelFile ghastTrapActive = models().withExistingParent(TFBlocks.GHAST_TRAP.getId().getPath() + "_active", cubeBt3)
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

		ModelFile builder = models().withExistingParent(TFBlocks.CARMINITE_BUILDER.getId().getPath(), cube3)
						.texture("all", prefix("block/towerdev_builder_off"))
						.texture("all2", prefix("block/tower_device_level_1/towerdev_builder_off_1"))
						.texture("all3", prefix("block/tower_device_level_2/towerdev_builder_off_2"));
		ModelFile builderActive = models().withExistingParent(TFBlocks.CARMINITE_BUILDER.getId().getPath() + "_active", cube3)
						.texture("all", prefix("block/towerdev_builder_on"))
						.texture("all2", prefix("block/tower_device_level_1/towerdev_builder_on_1"))
						.texture("all3", prefix("block/tower_device_level_2/towerdev_builder_on_2"));
		ModelFile builderTimeout = models().withExistingParent(TFBlocks.CARMINITE_BUILDER.getId().getPath() + "_timeout", cube3)
						.texture("all", prefix("block/towerdev_builder_timeout"))
						.texture("all2", prefix("block/tower_device_level_2/towerdev_builder_timeout_1"))
						.texture("all3", prefix("block/tower_device_level_3/towerdev_builder_timeout_2"));
		getVariantBuilder(TFBlocks.CARMINITE_BUILDER.get()).partialState()
						.with(BuilderBlock.STATE, TowerDeviceVariant.BUILDER_INACTIVE).setModels(new ConfiguredModel(builder));
		getVariantBuilder(TFBlocks.CARMINITE_BUILDER.get()).partialState()
						.with(BuilderBlock.STATE, TowerDeviceVariant.BUILDER_ACTIVE).setModels(new ConfiguredModel(builderActive));
		getVariantBuilder(TFBlocks.CARMINITE_BUILDER.get()).partialState()
						.with(BuilderBlock.STATE, TowerDeviceVariant.BUILDER_TIMEOUT).setModels(new ConfiguredModel(builderTimeout));

		ModelFile built = models().withExistingParent(TFBlocks.BUILT_BLOCK.getId().getPath(), cube2NoShade)
						.texture("all", prefix("block/towerdev_built_off"))
						.texture("all2", prefix("block/tower_device_level_1/towerdev_builder_off_1"));
		ModelFile builtActive = models().withExistingParent(TFBlocks.BUILT_BLOCK.getId().getPath() + "_active", cube2NoShade)
						.texture("all", prefix("block/towerdev_built_on"))
						.texture("all2", prefix("block/tower_device_level_1/towerdev_builder_on_1"));
		getVariantBuilder(TFBlocks.BUILT_BLOCK.get()).partialState()
						.with(TranslucentBuiltBlock.ACTIVE, false).setModels(new ConfiguredModel(built));
		getVariantBuilder(TFBlocks.BUILT_BLOCK.get()).partialState()
						.with(TranslucentBuiltBlock.ACTIVE, true).setModels(new ConfiguredModel(builtActive));

		ModelFile antibuilder = models().withExistingParent(TFBlocks.ANTIBUILDER.getId().getPath(), cube3)
						.texture("all", prefix("block/towerdev_antibuilder"))
						.texture("all2", prefix("block/tower_device_level_1/towerdev_antibuilder_1"))
						.texture("all3", prefix("block/tower_device_level_2/towerdev_antibuilder_2"));
		simpleBlock(TFBlocks.ANTIBUILDER.get(), antibuilder);
		ModelFile antibuilt = models().withExistingParent(TFBlocks.ANTIBUILT_BLOCK.getId().getPath(), cube2NoShade)
						.texture("all", prefix("block/towerdev_antibuilt"))
						.texture("all2", prefix("block/tower_device_level_2/towerdev_antibuilt_1"));
		simpleBlock(TFBlocks.ANTIBUILT_BLOCK.get(), antibuilt);

		ModelFile reactor = models().withExistingParent(TFBlocks.CARMINITE_REACTOR.getId().getPath(), cube3)
						.texture("all", prefix("block/towerdev_reactor_off"))
						.texture("all2", prefix("block/tower_device_level_1/towerdev_reactor_off_1"))
						.texture("all3", prefix("block/tower_device_level_2/towerdev_reactor_off_2"));
		ModelFile reactorActive = models().withExistingParent(TFBlocks.CARMINITE_REACTOR.getId().getPath() + "_active", cube3)
						.texture("all", prefix("block/towerdev_reactor_on"))
						.texture("all2", prefix("block/tower_device_level_1/towerdev_reactor_on_1"))
						.texture("all3", prefix("block/tower_device_level_2/towerdev_reactor_on_2"));
		getVariantBuilder(TFBlocks.CARMINITE_REACTOR.get()).partialState()
						.with(CarminiteReactorBlock.ACTIVE, false).setModels(new ConfiguredModel(reactor));
		getVariantBuilder(TFBlocks.CARMINITE_REACTOR.get()).partialState()
						.with(CarminiteReactorBlock.ACTIVE, true).setModels(new ConfiguredModel(reactorActive));
		simpleBlock(TFBlocks.REACTOR_DEBRIS.get(), models().cubeAll(TFBlocks.REACTOR_DEBRIS.getId().getPath(), new ResourceLocation("block/destroy_stage_9")));
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
		boolean fixer = Direction.SOUTH.getAxis() == Direction.Axis.Z;
		ModelFile green = models().withExistingParent(TFBlocks.GREEN_THORNS.getId().getPath(), prefix("block/thorns_main"))
						.texture("side", prefix("block/green_thorns_side"))
						.texture("end", prefix("block/green_thorns_top"));
		ModelFile greenBottom = models().withExistingParent(TFBlocks.GREEN_THORNS.getId().getPath() + "_bottom", prefix("block/thorns_section_bottom"))
						.texture("side", prefix("block/green_thorns_side"))
						.texture("end", prefix("block/green_thorns_top"));
		ModelFile greenTop = models().withExistingParent(TFBlocks.GREEN_THORNS.getId().getPath() + "_top", prefix("block/thorns_section_top"))
						.texture("side", prefix("block/green_thorns_side"))
						.texture("end", prefix("block/green_thorns_top"));
		getMultipartBuilder(TFBlocks.GREEN_THORNS.get())
						.part().modelFile(green).addModel().condition(RotatedPillarBlock.AXIS, Direction.Axis.Y).end()
						.part().modelFile(green).rotationX(90).addModel().condition(RotatedPillarBlock.AXIS, Direction.Axis.Z).end()
						.part().modelFile(green).rotationX(90).rotationY(90).addModel().condition(RotatedPillarBlock.AXIS, Direction.Axis.X).end()
						.part().modelFile(greenTop).rotationX(90).addModel().condition(PipeBlock.UP, true).end()
						.part().modelFile(greenBottom).rotationX(90).addModel().condition(PipeBlock.DOWN, true).end()
						.part().modelFile(greenTop).rotationY(270).addModel().condition(PipeBlock.EAST, true).end()
						.part().modelFile(greenBottom).rotationY(270).addModel().condition(PipeBlock.WEST, true).end()
						.part().modelFile(fixer ? greenBottom : greenTop).rotationY(fixer ? 180 : 0).addModel().condition(PipeBlock.SOUTH, true).end()
						.part().modelFile(fixer ? greenTop : greenBottom).rotationY(fixer ? 180 : 0).addModel().condition(PipeBlock.NORTH, true).end();

		ModelFile brown = models().withExistingParent(TFBlocks.BROWN_THORNS.getId().getPath(), prefix("block/thorns_main"))
						.texture("side", prefix("block/brown_thorns_side"))
						.texture("end", prefix("block/brown_thorns_top"));
		ModelFile brownBottom = models().withExistingParent(TFBlocks.BROWN_THORNS.getId().getPath() + "_bottom", prefix("block/thorns_section_bottom"))
						.texture("side", prefix("block/brown_thorns_side"))
						.texture("end", prefix("block/brown_thorns_top"));
		ModelFile brownTop = models().withExistingParent(TFBlocks.BROWN_THORNS.getId().getPath() + "_top", prefix("block/thorns_section_top"))
						.texture("side", prefix("block/brown_thorns_side"))
						.texture("end", prefix("block/brown_thorns_top"));
		getMultipartBuilder(TFBlocks.BROWN_THORNS.get())
						.part().modelFile(brown).addModel().condition(RotatedPillarBlock.AXIS, Direction.Axis.Y).end()
						.part().modelFile(brown).rotationX(90).addModel().condition(RotatedPillarBlock.AXIS, Direction.Axis.Z).end()
						.part().modelFile(brown).rotationX(90).rotationY(90).addModel().condition(RotatedPillarBlock.AXIS, Direction.Axis.X).end()
						.part().modelFile(brownTop).rotationX(90).addModel().condition(PipeBlock.UP, true).end()
						.part().modelFile(brownBottom).rotationX(90).addModel().condition(PipeBlock.DOWN, true).end()
						.part().modelFile(brownTop).rotationY(270).addModel().condition(PipeBlock.EAST, true).end()
						.part().modelFile(brownBottom).rotationY(270).addModel().condition(PipeBlock.WEST, true).end()
						.part().modelFile(fixer ? brownBottom : brownTop).rotationY(fixer ? 180 : 0).addModel().condition(PipeBlock.SOUTH, true).end()
						.part().modelFile(fixer ? brownTop : brownBottom).rotationY(fixer ? 180 : 0).addModel().condition(PipeBlock.NORTH, true).end();

		ModelFile burnt = models().withExistingParent(TFBlocks.BURNT_THORNS.getId().getPath(), prefix("block/thorns_main"))
						.texture("side", prefix("block/burnt_thorns_side"))
						.texture("end", prefix("block/burnt_thorns_top"));
		ModelFile burntBottom = models().withExistingParent(TFBlocks.BURNT_THORNS.getId().getPath() + "_bottom", prefix("block/thorns_section_bottom"))
						.texture("side", prefix("block/burnt_thorns_side"))
						.texture("end", prefix("block/burnt_thorns_top"));
		ModelFile burntTop = models().withExistingParent(TFBlocks.BURNT_THORNS.getId().getPath() + "_top", prefix("block/thorns_section_top"))
						.texture("side", prefix("block/burnt_thorns_side"))
						.texture("end", prefix("block/burnt_thorns_top"));
		getMultipartBuilder(TFBlocks.BURNT_THORNS.get())
						.part().modelFile(burnt).addModel().condition(RotatedPillarBlock.AXIS, Direction.Axis.Y).end()
						.part().modelFile(burnt).rotationX(90).addModel().condition(RotatedPillarBlock.AXIS, Direction.Axis.Z).end()
						.part().modelFile(burnt).rotationX(90).rotationY(90).addModel().condition(RotatedPillarBlock.AXIS, Direction.Axis.X).end()
						.part().modelFile(burntTop).rotationX(90).addModel().condition(PipeBlock.UP, true).end()
						.part().modelFile(burntBottom).rotationX(90).addModel().condition(PipeBlock.DOWN, true).end()
						.part().modelFile(burntTop).rotationY(270).addModel().condition(PipeBlock.EAST, true).end()
						.part().modelFile(burntBottom).rotationY(270).addModel().condition(PipeBlock.WEST, true).end()
						.part().modelFile(fixer ? burntBottom : burntTop).rotationY(fixer ? 180 : 0).addModel().condition(PipeBlock.SOUTH, true).end()
						.part().modelFile(fixer ? burntTop : burntBottom).rotationY(fixer ? 180 : 0).addModel().condition(PipeBlock.NORTH, true).end();
	}

	private void auroraBlocks() {
		int variants = 16;
		ModelFile[] models = new ModelFile[variants];
		for (int i = 0; i < variants; i++) {
			models[i] = models().withExistingParent(TFBlocks.AURORA_BLOCK.getId().getPath() + "_" + i, prefix("block/util/tinted_cube_all"))
							.texture("all", prefix("block/" + TFBlocks.AURORA_BLOCK.getId().getPath() + "_" + i));
		}
		for (int i = 0; i < variants; i++) {
			getVariantBuilder(TFBlocks.AURORA_BLOCK.get()).partialState().with(AuroraBrickBlock.VARIANT, i)
							.setModels(ConfiguredModel.builder()
											.weight(3).modelFile(models[i]).nextModel()
											.weight(1).modelFile(models[(i + 1) % variants]).build());
		}

		ModelFile pillarModel = models().withExistingParent(TFBlocks.AURORA_PILLAR.getId().getPath(), prefix("block/util/tinted_cube_column"))
						.texture("end", prefix("block/" + TFBlocks.AURORA_PILLAR.getId().getPath() + "_top"))
						.texture("side", blockTexture(TFBlocks.AURORA_PILLAR.get()));
		axisBlock(TFBlocks.AURORA_PILLAR.get(), pillarModel, pillarModel);

		ModelFile slabModel = models().withExistingParent(TFBlocks.AURORA_SLAB.getId().getPath(), prefix("block/util/tinted_slab"))
						.texture("bottom", prefix("block/" + TFBlocks.AURORA_PILLAR.getId().getPath() + "_top"))
						.texture("top", prefix("block/" + TFBlocks.AURORA_PILLAR.getId().getPath() + "_top"))
						.texture("side", prefix("block/" + TFBlocks.AURORA_SLAB.getId().getPath() + "_side"));
		ModelFile doubleSlabModel = models().withExistingParent(TFBlocks.AURORA_SLAB.getId().getPath() + "_double", prefix("block/util/tinted_cube_column"))
						.texture("end", prefix("block/" + TFBlocks.AURORA_PILLAR.getId().getPath() + "_top"))
						.texture("side", prefix("block/" + TFBlocks.AURORA_SLAB.getId().getPath() + "_side"));

		getVariantBuilder(TFBlocks.AURORA_SLAB.get()).partialState()
						.with(SlabBlock.TYPE, SlabType.BOTTOM).setModels(new ConfiguredModel(slabModel));
		getVariantBuilder(TFBlocks.AURORA_SLAB.get()).partialState()
						.with(SlabBlock.TYPE, SlabType.TOP).setModels(ConfiguredModel.builder().uvLock(true).rotationX(180).modelFile(slabModel).build());
		getVariantBuilder(TFBlocks.AURORA_SLAB.get()).partialState()
						.with(SlabBlock.TYPE, SlabType.DOUBLE).setModels(new ConfiguredModel(doubleSlabModel));

		ModelFile auroraGlass = models().withExistingParent(TFBlocks.AURORALIZED_GLASS.getId().getPath(), prefix("block/util/tinted_cube_all"))
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
		String baseName = b.getRegistryName().getPath();
		ResourceLocation parent = prefix("block/huge_lily_pad");
		ModelFile[] models = new ModelFile[4];
		for (int i = 0; i < models.length; i++) {
			models[i] = models().withExistingParent(baseName + "_" + i, parent)
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

			ModelFile model = m.get(state.getValue(HugeLilyPadBlock.PIECE));
			return ConfiguredModel.builder().rotationY(rotY).modelFile(model).build();
		});
	}

	@SuppressWarnings("SuspiciousNameCombination")
	private void candelabra() {
		// TODO variants
		final ModelFile candelabra = this.buildCandelabra(4, 5, 4);
		final ModelFile candelabraWall = this.buildWallCandelabra(4, 5, 4);
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

		/*ConfiguredModel[] plankModels = ConfiguredModel.builder()
				.weight(10).modelFile(models().cubeAll(plank.getRegistryName().getPath(), tex0)).nextModel()
				.weight(10).modelFile(models().cubeAll(plank.getRegistryName().getPath() + "_1", tex1)).nextModel()
				.weight(1).modelFile(models().cubeAll(plank.getRegistryName().getPath() + "_2", tex2)).nextModel()
				.weight(1).modelFile(models().cubeAll(plank.getRegistryName().getPath() + "_3", tex3)).build();*/

		this.getVariantBuilder(TFBlocks.CANDELABRA.get()).forAllStates(state -> {
			Direction direction = state.getValue(CandelabraBlock.FACING);
			boolean onWall = state.getValue(CandelabraBlock.ON_WALL);
			boolean lit = state.getValue(CandelabraBlock.LIGHTING) != AbstractLightableBlock.Lighting.NONE;

			ConfiguredModel.Builder<?> stateBuilder = ConfiguredModel.builder();

			Iterator<ModelFile> models = onWall ? wallCandelabras.iterator() : candelabras.iterator();

			while (models.hasNext()) {
				ModelFile model = models.next();
				stateBuilder.modelFile(this.models().getBuilder(model.getLocation().toString() + "_plain" + (lit ? "_lit" : "")).parent(model).texture("candle", lit ? "minecraft:block/candle_lit" : "minecraft:block/candle")).rotationY((int) direction.toYRot());

				if (models.hasNext())
					stateBuilder = stateBuilder.nextModel();
			}

			return stateBuilder.build();
		});
	}

	private ModelFile buildCandelabra(final int leftHeight, final int centerHeight, final int rightHeight) {
		return this.models().withExistingParent("candelabra_" + leftHeight + "_" + centerHeight + "_" + rightHeight, "minecraft:block/block").texture("particle", "#candelabra").texture("candelabra", "block/candelabra")
				.element().from(0, 1, 8).to(16, 7, 8).face(Direction.NORTH).uvs(0, 0, 16, 6).texture("#candelabra").end().face(Direction.SOUTH).uvs(16, 0, 0, 6).texture("#candelabra").end().end()
				.element().from(8, 1, 5).to(8, 7, 11).face(Direction.EAST).uvs(0, 6, 6, 12).texture("#candelabra").end().face(Direction.WEST).uvs(6, 6, 0, 12).texture("#candelabra").end().end()
				.element().from(1, 7, 6).to(5, 8, 10).allFaces((direction, builder) -> builder.uvs(0, 12, 4, direction.getAxis().isHorizontal() ? 13 : 16).texture("#candelabra")).end()
				.element().from(6, 7, 6).to(10, 8, 10).allFaces((direction, builder) -> builder.uvs(0, 12, 4, direction.getAxis().isHorizontal() ? 13 : 16).texture("#candelabra")).end()
				.element().from(11, 7, 6).to(15, 8, 10).allFaces((direction, builder) -> builder.uvs(0, 12, 4, direction.getAxis().isHorizontal() ? 13 : 16).texture("#candelabra")).end()
				.element().from(6, 0, 6).to(10, 1, 10).allFaces((direction, builder) -> builder.uvs(0, 12, 4, direction.getAxis().isHorizontal() ? 13 : 16).texture("#candelabra")).end()
				.element().from(2, 8, 7).to(4, 8 + leftHeight, 9).face(Direction.NORTH).end().face(Direction.SOUTH).end().face(Direction.WEST).end().face(Direction.EAST).end().faces((direction, builder) -> builder.uvs(0, 8, 2, 8 + leftHeight).texture("#candle")).face(Direction.UP).uvs(0, 6, 2, 8).texture("#candle").end().end()
				.element().from(7, 8, 7).to(9, 8 + centerHeight, 9).face(Direction.NORTH).end().face(Direction.SOUTH).end().face(Direction.WEST).end().face(Direction.EAST).end().faces((direction, builder) -> builder.uvs(0, 8, 2, 8 + centerHeight).texture("#candle")).face(Direction.UP).uvs(0, 6, 2, 8).texture("#candle").end().end()
				.element().from(12, 8, 7).to(14, 8 + rightHeight, 9).face(Direction.NORTH).end().face(Direction.SOUTH).end().face(Direction.WEST).end().face(Direction.EAST).end().faces((direction, builder) -> builder.uvs(0, 8, 2, 8 + rightHeight).texture("#candle")).face(Direction.UP).uvs(0, 6, 2, 8).texture("#candle").end().end()
				.element().from(2.5f, 8 + leftHeight, 8).to(3.5f, 9 + leftHeight, 8).rotation().angle(45).axis(Direction.Axis.Y).origin(3, 8 + leftHeight, 8).end().face(Direction.NORTH).uvs(0, 5, 1, 6).texture("#candle").end().face(Direction.SOUTH).uvs(0, 5, 1, 6).texture("#candle").end().end()
				.element().from(2.5f, 8 + leftHeight, 8).to(3.5f, 9 + leftHeight, 8).rotation().angle(-45).axis(Direction.Axis.Y).origin(3, 8 + leftHeight, 8).end().face(Direction.NORTH).uvs(0, 5, 1, 6).texture("#candle").end().face(Direction.SOUTH).uvs(0, 5, 1, 6).texture("#candle").end().end()
				.element().from(7.5f, 8 + centerHeight, 8).to(8.5f, 9 + centerHeight, 8).rotation().angle(45).axis(Direction.Axis.Y).origin(8, 8 + centerHeight, 8).end().face(Direction.NORTH).uvs(0, 5, 1, 6).texture("#candle").end().face(Direction.SOUTH).uvs(0, 5, 1, 6).texture("#candle").end().end()
				.element().from(7.5f, 8 + centerHeight, 8).to(8.5f, 9 + centerHeight, 8).rotation().angle(-45).axis(Direction.Axis.Y).origin(8, 8 + centerHeight, 8).end().face(Direction.NORTH).uvs(0, 5, 1, 6).texture("#candle").end().face(Direction.SOUTH).uvs(0, 5, 1, 6).texture("#candle").end().end()
				.element().from(12.5f, 8 + rightHeight, 8).to(13.5f, 9 + rightHeight, 8).rotation().angle(45).axis(Direction.Axis.Y).origin(13, 8 + rightHeight, 8).end().face(Direction.NORTH).uvs(0, 5, 1, 6).texture("#candle").end().face(Direction.SOUTH).uvs(0, 5, 1, 6).texture("#candle").end().end()
				.element().from(12.5f, 8 + rightHeight, 8).to(13.5f, 9 + rightHeight, 8).rotation().angle(-45).axis(Direction.Axis.Y).origin(13, 8 + rightHeight, 8).end().face(Direction.NORTH).uvs(0, 5, 1, 6).texture("#candle").end().face(Direction.SOUTH).uvs(0, 5, 1, 6).texture("#candle").end().end()
				;
	}

	private ModelFile buildWallCandelabra(final int leftHeight, final int centerHeight, final int rightHeight) {
		return this.models().withExistingParent("candelabra_wall_" + leftHeight + "_" + centerHeight + "_" + rightHeight, "minecraft:block/block").texture("particle", "#candelabra").texture("candelabra", "block/candelabra")
				.element().from(0, 1, 12).to(16, 7, 12).face(Direction.NORTH).uvs(0, 0, 16, 6).texture("#candelabra").end().face(Direction.SOUTH).uvs(16, 0, 0, 6).texture("#candelabra").end().end()
				.element().from(8, 1, 9).to(8, 7, 15).face(Direction.EAST).uvs(0, 6, 6, 12).texture("#candelabra").end().face(Direction.WEST).uvs(6, 6, 0, 12).texture("#candelabra").end().end()
				.element().from(1, 7, 10).to(5, 8, 14).allFaces((direction, builder) -> builder.uvs(0, 12, 4, direction.getAxis().isHorizontal() ? 13 : 16).texture("#candelabra")).end()
				.element().from(6, 7, 10).to(10, 8, 14).allFaces((direction, builder) -> builder.uvs(0, 12, 4, direction.getAxis().isHorizontal() ? 13 : 16).texture("#candelabra")).end()
				.element().from(11, 7, 10).to(15, 8, 14).allFaces((direction, builder) -> builder.uvs(0, 12, 4, direction.getAxis().isHorizontal() ? 13 : 16).texture("#candelabra")).end()
				.element().from(6, 2, 15).to(10, 6, 16).allFaces((direction, builder) -> builder.uvs(direction.getAxis() == Direction.Axis.X ? 3 : 0, 12, 4, direction.getAxis() == Direction.Axis.Y ? 13 : 16).texture("#candelabra")).end()
				.element().from(2, 8, 11).to(4, 8 + leftHeight, 13).face(Direction.NORTH).end().face(Direction.SOUTH).end().face(Direction.WEST).end().face(Direction.EAST).end().faces((direction, builder) -> builder.uvs(0, 8, 2, 8 + leftHeight).texture("#candle")).face(Direction.UP).uvs(0, 6, 2, 8).texture("#candle").end().end()
				.element().from(7, 8, 11).to(9, 8 + centerHeight, 13).face(Direction.NORTH).end().face(Direction.SOUTH).end().face(Direction.WEST).end().face(Direction.EAST).end().faces((direction, builder) -> builder.uvs(0, 8, 2, 8 + centerHeight).texture("#candle")).face(Direction.UP).uvs(0, 6, 2, 8).texture("#candle").end().end()
				.element().from(12, 8, 11).to(14, 8 + rightHeight, 13).face(Direction.NORTH).end().face(Direction.SOUTH).end().face(Direction.WEST).end().face(Direction.EAST).end().faces((direction, builder) -> builder.uvs(0, 8, 2, 8 + rightHeight).texture("#candle")).face(Direction.UP).uvs(0, 6, 2, 8).texture("#candle").end().end()
				.element().from(2.5f, 8 + leftHeight, 12).to(3.5f, 9 + leftHeight, 12).rotation().angle(45).axis(Direction.Axis.Y).origin(3, 8 + leftHeight, 12).end().face(Direction.NORTH).uvs(0, 5, 1, 6).texture("#candle").end().face(Direction.SOUTH).uvs(0, 5, 1, 6).texture("#candle").end().end()
				.element().from(2.5f, 8 + leftHeight, 12).to(3.5f, 9 + leftHeight, 12).rotation().angle(-45).axis(Direction.Axis.Y).origin(3, 8 + leftHeight, 12).end().face(Direction.NORTH).uvs(0, 5, 1, 6).texture("#candle").end().face(Direction.SOUTH).uvs(0, 5, 1, 6).texture("#candle").end().end()
				.element().from(7.5f, 8 + centerHeight, 12).to(8.5f, 9 + centerHeight, 12).rotation().angle(45).axis(Direction.Axis.Y).origin(8, 8 + centerHeight, 12).end().face(Direction.NORTH).uvs(0, 5, 1, 6).texture("#candle").end().face(Direction.SOUTH).uvs(0, 5, 1, 6).texture("#candle").end().end()
				.element().from(7.5f, 8 + centerHeight, 12).to(8.5f, 9 + centerHeight, 12).rotation().angle(-45).axis(Direction.Axis.Y).origin(8, 8 + centerHeight, 12).end().face(Direction.NORTH).uvs(0, 5, 1, 6).texture("#candle").end().face(Direction.SOUTH).uvs(0, 5, 1, 6).texture("#candle").end().end()
				.element().from(12.5f, 8 + rightHeight, 12).to(13.5f, 9 + rightHeight, 12).rotation().angle(45).axis(Direction.Axis.Y).origin(13, 8 + rightHeight, 12).end().face(Direction.NORTH).uvs(0, 5, 1, 6).texture("#candle").end().face(Direction.SOUTH).uvs(0, 5, 1, 6).texture("#candle").end().end()
				.element().from(12.5f, 8 + rightHeight, 12).to(13.5f, 9 + rightHeight, 12).rotation().angle(-45).axis(Direction.Axis.Y).origin(13, 8 + rightHeight, 12).end().face(Direction.NORTH).uvs(0, 5, 1, 6).texture("#candle").end().face(Direction.SOUTH).uvs(0, 5, 1, 6).texture("#candle").end().end()
				;
	}

	private void perFaceBlock(Block b, ResourceLocation inside, ResourceLocation outside) {
		ModelFile modelInside = models().withExistingParent(b.getRegistryName().getPath() + "_inside", new ResourceLocation("block/template_single_face"))
						.texture("texture", inside);
		ModelFile modelOutside = models().withExistingParent(b.getRegistryName().getPath() + "_outside", new ResourceLocation("block/template_single_face"))
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
		ResourceLocation top = new ResourceLocation("block/" + originalLog.getRegistryName().getPath() + "_top");
		ResourceLocation side = new ResourceLocation("block/" + originalLog.getRegistryName().getPath());
		ResourceLocation inner = new ResourceLocation("block/" + strippedLog.getRegistryName().getPath());

		this.getVariantBuilder(horizontalHollowLog.get()).forAllStates(state -> ConfiguredModel.builder().modelFile((switch (state.getValue(HollowLogHorizontal.VARIANT)) {
			case MOSS -> models().getBuilder(horizontalHollowLog.getId().getPath() + "_moss").parent(mossLog);
			case MOSS_AND_GRASS -> models().getBuilder(horizontalHollowLog.getId().getPath() + "_moss_grass").parent(grassLog);
			case SNOW -> models().getBuilder(horizontalHollowLog.getId().getPath() + "_snow").parent(snowLog);
			default -> models().getBuilder(horizontalHollowLog.getId().getPath()).parent(emptyLog);
		}).texture("top", top).texture("side", side).texture("inner", inner)).rotationY(state.getValue(HollowLogHorizontal.HORIZONTAL_AXIS) == Direction.Axis.X ? 90 : 0).build());

		this.simpleBlock(verticalHollowLog.get(), models().getBuilder(verticalHollowLog.getId().getPath()).parent(hollowLog).texture("top", top).texture("side", side).texture("inner", inner));

		this.getVariantBuilder(climbableHollowLog.get()).forAllStates(state -> ConfiguredModel.builder().modelFile((switch (state.getValue(HollowLogClimbable.VARIANT)) {
			case VINE -> models().getBuilder(climbableHollowLog.getId().getPath() + "_vine").parent(vineLog);
			case LADDER, LADDER_WATERLOGGED -> models().getBuilder(climbableHollowLog.getId().getPath() + "_ladder").parent(ladderLog);
		}).texture("top", top).texture("side", side).texture("inner", inner)).rotationY((int) state.getValue(HollowLogClimbable.FACING).toYRot()).uvLock(true).build());
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
		}).texture("top", top).texture("side", side).texture("inner", inner)).rotationY(state.getValue(HollowLogHorizontal.HORIZONTAL_AXIS) == Direction.Axis.X ? 90 : 0).build());

		this.simpleBlock(verticalHollowLog.get(), models().getBuilder(verticalHollowLog.getId().getPath()).parent(hollowLog).texture("top", top).texture("side", side).texture("inner", inner));

		this.getVariantBuilder(climbableHollowLog.get()).forAllStates(state -> ConfiguredModel.builder().modelFile((switch (state.getValue(HollowLogClimbable.VARIANT)) {
			case VINE -> models().getBuilder(climbableHollowLog.getId().getPath() + "_vine").parent(vineLog);
			case LADDER, LADDER_WATERLOGGED -> models().getBuilder(climbableHollowLog.getId().getPath() + "_ladder").parent(ladderLog);
		}).texture("top", top).texture("side", side).texture("inner", inner)).rotationY((int) state.getValue(HollowLogClimbable.FACING).toYRot()).uvLock(true).build());
	}

	private BlockModelBuilder buildVerticalLog(@Nullable HollowLogVariants.Climbable variant) {
		BlockModelBuilder model = this.models().withExistingParent(variant == null ? "vertical_hollow_log" : "vertical_hollow_log_" + variant.getSerializedName(), "minecraft:block/block").texture("particle", "#side")
				.element().from(0, 0, 0).to(2, 16, 16).allFaces(((dir, builder) -> builder.cullface(dir).texture(dir == Direction.EAST ? "#inner" : dir.getAxis() == Direction.Axis.Y ? "#top" : "#side"))).face(Direction.EAST).cullface(null).end().end()
				.element().from(14, 0, 0).to(16, 16, 16).allFaces(((dir, builder) -> builder.cullface(dir).texture(dir == Direction.WEST ? "#inner" : dir.getAxis() == Direction.Axis.Y ? "#top" : "#side"))).face(Direction.WEST).cullface(null).end().end()
				.element().from(2, 0, 0).to(14, 16, 2).allFaces(((dir, builder) -> builder.cullface(dir).texture(dir == Direction.SOUTH ? "#inner" : dir.getAxis() == Direction.Axis.Y ? "#top" : "#side"))).face(Direction.SOUTH).cullface(null).end().end()
				.element().from(2, 0, 14).to(14, 16, 16).allFaces(((dir, builder) -> builder.cullface(dir).texture(dir == Direction.NORTH ? "#inner" : dir.getAxis() == Direction.Axis.Y ? "#top" : "#side"))).face(Direction.NORTH).cullface(null).end().end();

		if (variant != null) model.element().from(2, 0, 2.8f).to(14, 16, 2.8f)
				.face(Direction.NORTH).end().face(Direction.SOUTH).end()
				.faces((dir, builder) -> builder.texture("#climbable").tintindex(1)).shade(false).end()
				.texture("climbable", "minecraft:block/" + variant.getSerializedName());

		return model;
	}

	private BlockModelBuilder buildHorizontalHollowLog(boolean carpet, boolean grass) {
		final int height = carpet ? 3 : 2;
		final int heightInv = 16 - height;
		grass &= carpet;

		// Top, Bottom, Left side, Right side
		BlockModelBuilder model = this.models().withExistingParent(carpet ? (grass ? "horizontal_hollow_log_plant" : "horizontal_hollow_log_carpet") : "horizontal_hollow_log", "minecraft:block/block").texture("particle", "#side")
				.element().from(0, 0, 0).to(16, height, 16)
				.allFaces((dir, builder) -> builder
						.uvs(0, dir.getAxis() == Direction.Axis.Y ? 0 : heightInv, 16, 16)
						.cullface(dir == Direction.UP ? null : dir)
						.texture((carpet && dir == Direction.UP) ? "#carpet" : dir.getAxis() == Direction.Axis.Z ? "#top" : dir == Direction.UP ? "#inner" : "#side")
				)
				.face(Direction.EAST).uvs(heightInv, 0, 16, 16).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).end()
				.face(Direction.WEST).uvs(0, 0, height, 16).rotation(ModelBuilder.FaceRotation.COUNTERCLOCKWISE_90).end()
				.face(Direction.DOWN).rotation(ModelBuilder.FaceRotation.UPSIDE_DOWN).end()
				.end()
				.element().from(0, 14, 0).to(16, 16, 16)
				.allFaces((dir, builder) -> builder
						.uvs(0, 0, 16, dir.getAxis() == Direction.Axis.Y ? 16 : 2)
						.cullface(dir == Direction.DOWN ? null : dir)
						.texture(dir.getAxis() == Direction.Axis.Z ? "#top" : dir == Direction.DOWN ? "#inner" : "#side")
				)
				.face(Direction.EAST).uvs(0, 0, 2, 16).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).end()
				.face(Direction.WEST).uvs(14, 0, 16, 16).rotation(ModelBuilder.FaceRotation.COUNTERCLOCKWISE_90).end()
				.face(Direction.DOWN).rotation(ModelBuilder.FaceRotation.UPSIDE_DOWN).end()
				.end()
				.element().from(0, height, 0).to(2, 14, 16)
				.face(Direction.NORTH).uvs(14, 2, 16, heightInv).end()
				.face(Direction.SOUTH).uvs(0, 2, 2, heightInv).end()
				.face(Direction.EAST).uvs(2, 0, heightInv, 16).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).end()
				.face(Direction.WEST).uvs(height, 0, 14, 16).rotation(ModelBuilder.FaceRotation.COUNTERCLOCKWISE_90).end()
				.faces((dir, builder) -> builder.cullface(dir == Direction.EAST ? null : dir).texture(dir.getAxis() == Direction.Axis.Z ? "#top" : dir == Direction.EAST ? "#inner" : "#side")).end()
				.element().from(14, height, 0).to(16, 14, 16)
				.face(Direction.NORTH).uvs(0, 2, 2, heightInv).end()
				.face(Direction.SOUTH).uvs(14, 2, 16, heightInv).end()
				.face(Direction.EAST).uvs(2, 0, heightInv, 16).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).end()
				.face(Direction.WEST).uvs(height, 0, 14, 16).rotation(ModelBuilder.FaceRotation.COUNTERCLOCKWISE_90).end()
				.faces((dir, builder) -> builder.cullface(dir == Direction.WEST ? null : dir).texture(dir.getAxis() == Direction.Axis.Z ? "#top" : dir == Direction.WEST ? "#inner" : "#side")).end()
				;

		if (carpet)
			model.element().from(0, 0, 0).to(16, height, 16).face(Direction.NORTH).end().face(Direction.SOUTH).end().faces((dir, builder) -> builder.uvs(0, 16 - height, 16, 16).texture("#overhang"));
		if (grass) {
			model.element().from(0.8f, height, 8).to(15.2f, 14, 8).rotation().origin(8, 8, 8).axis(Direction.Axis.Y).angle(45).rescale(true).end().shade(false).face(Direction.NORTH).end().face(Direction.SOUTH).end().faces((direction, faceBuilder) -> faceBuilder.uvs(0, height, 16, 14).texture("#plant").tintindex(1));
			model.element().from(8, height, 0.8f).to(8, 14, 15.2f).rotation().origin(8, 8, 8).axis(Direction.Axis.Y).angle(45).rescale(true).end().shade(false).face(Direction.WEST).end().face(Direction.EAST).end().faces((direction, faceBuilder) -> faceBuilder.uvs(0, height, 16, 14).texture("#plant").tintindex(1));
		}

		return model;
	}

	@Nonnull
	@Override
	public String getName() {
		return "TwilightForest blockstates and block models";
	}
}
