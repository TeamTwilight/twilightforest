package twilightforest.datagen.assets.models;

import net.minecraft.client.color.item.GrassColorSource;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelOutput;
import net.minecraft.client.data.models.blockstates.*;
import net.minecraft.client.data.models.model.*;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.item.properties.select.DisplayContext;
import net.minecraft.core.Direction;
import net.minecraft.data.BlockFamily;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.GrassColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import org.w3c.dom.Text;
import twilightforest.TwilightForestMod;
import twilightforest.block.*;
import twilightforest.client.renderer.special.*;
import twilightforest.util.TFBlockFamilies;
import twilightforest.datagen.helpers.models.BlockModelBuilders;
import twilightforest.init.TFBlocks;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public class BlockModelGenerator extends BlockModelBuilders {
	public BlockModelGenerator(Consumer<BlockStateGenerator> stateOutput, ItemModelOutput itemOutput, BiConsumer<ResourceLocation, ModelInstance> modelOutput) {
		super(stateOutput, itemOutput, modelOutput);
	}

	@Override
	public void run() {
		this.generateWoodBlocks();

		this.spawner(TFBlocks.NAGA_BOSS_SPAWNER.get(), "block/boss_spawner");
		this.spawner(TFBlocks.LICH_BOSS_SPAWNER.get(), "block/boss_spawner");
		this.spawner(TFBlocks.MINOSHROOM_BOSS_SPAWNER.get(), "block/boss_spawner");
		this.spawner(TFBlocks.HYDRA_BOSS_SPAWNER.get(), "block/boss_spawner");
		this.spawner(TFBlocks.KNIGHT_PHANTOM_BOSS_SPAWNER.get(), "block/boss_spawner");
		this.spawner(TFBlocks.UR_GHAST_BOSS_SPAWNER.get(), "block/boss_spawner");
		this.spawner(TFBlocks.ALPHA_YETI_BOSS_SPAWNER.get(), "block/boss_spawner");
		this.spawner(TFBlocks.SNOW_QUEEN_BOSS_SPAWNER.get(), "block/boss_spawner");
		this.spawner(TFBlocks.FINAL_BOSS_BOSS_SPAWNER.get(), "block/boss_spawner");
		this.spawner(TFBlocks.SINISTER_SPAWNER.get(), "block/sinister_spawner");

		this.thorns(TFBlocks.BROWN_THORNS.get());
		this.thorns(TFBlocks.GREEN_THORNS.get());
		this.thorns(TFBlocks.BURNT_THORNS.get());
		this.directionalCrossModel(TFBlocks.THORN_ROSE.get(), PlantType.NOT_TINTED);
		this.generateLeaves(TFBlocks.THORN_LEAVES.get(), TextureMapping.cube(Blocks.SPRUCE_LEAVES), -10380959);
		this.wrapBlockItem(TFBlocks.DEADROCK.get(), this::createTrivialCube);
		this.wrapBlockItem(TFBlocks.CRACKED_DEADROCK.get(), this::createTrivialCube);
		this.wrapBlockItem(TFBlocks.WEATHERED_DEADROCK.get(), this::createTrivialCube);

		this.nagaStone();

		this.blockStateOutput.accept(MultiVariantGenerator.multiVariant(TFBlocks.SPIRAL_BRICKS.get()).with(PropertyDispatch.properties(SpiralBrickBlock.AXIS_FACING, SpiralBrickBlock.DIAGONAL).generate((axis, diagonals) ->
			Variant.variant().with(VariantProperties.MODEL, TwilightForestMod.prefix("block/spiral_bricks/" + axis.getName() + "_spiral_" + diagonals.getSerializedName())))));
		this.itemModelOutput.accept(TFBlocks.SPIRAL_BRICKS.asItem(), ItemModelUtils.plainModel(TwilightForestMod.prefix("block/spiral_bricks/z_spiral_bottom_right")));

		this.wrapBlockItem(TFBlocks.TWISTED_STONE.get(), block -> this.createRotatedPillarWithHorizontalVariant(block, TexturedModel.COLUMN_ALT, TexturedModel.COLUMN_HORIZONTAL_ALT));
		this.wrapBlockItem(TFBlocks.BOLD_STONE_PILLAR.get(), block -> this.createRotatedPillarWithHorizontalVariant(block, TexturedModel.COLUMN_ALT, TexturedModel.COLUMN_HORIZONTAL_ALT));
		this.stonePillar();

		this.terrorcotta();

		this.wrapBlockItem(TFBlocks.MAZESTONE.get(), this::createTrivialCube);
		this.wrapBlockItem(TFBlocks.MAZESTONE_BRICK.get(), this::createTrivialCube);
		this.wrapBlockItem(TFBlocks.CRACKED_MAZESTONE.get(), this::createTrivialCube);
		this.wrapBlockItem(TFBlocks.MOSSY_MAZESTONE.get(), this::createTrivialCube);
		this.wrapBlockItem(TFBlocks.DECORATIVE_MAZESTONE.get(), block -> this.blockStateOutput.accept(createSimpleBlock(block, ModelTemplates.CUBE_COLUMN.create(block, TextureMapping.column(TextureMapping.getBlockTexture(block), TextureMapping.getBlockTexture(TFBlocks.MAZESTONE.get())), this.modelOutput))));
		this.wrapBlockItem(TFBlocks.CUT_MAZESTONE.get(), block -> this.blockStateOutput.accept(createSimpleBlock(block, ModelTemplates.CUBE_COLUMN.create(block, TextureMapping.column(TextureMapping.getBlockTexture(block), TextureMapping.getBlockTexture(TFBlocks.MAZESTONE.get())), this.modelOutput))));
		this.wrapBlockItem(TFBlocks.MAZESTONE_MOSAIC.get(), block -> this.blockStateOutput.accept(createSimpleBlock(block, ModelTemplates.CUBE_COLUMN.create(block, TextureMapping.column(TextureMapping.getBlockTexture(TFBlocks.MAZESTONE_BRICK.get()), TextureMapping.getBlockTexture(block)), this.modelOutput))));
		this.wrapBlockItem(TFBlocks.MAZESTONE_BORDER.get(), block -> this.blockStateOutput.accept(createSimpleBlock(block, ModelTemplates.CUBE_COLUMN.create(block, TextureMapping.column(TextureMapping.getBlockTexture(TFBlocks.MAZESTONE_BRICK.get()), TextureMapping.getBlockTexture(block)), this.modelOutput))));
		this.wrapTintedBlockItem(TFBlocks.SMOKER.get(), new GrassColorSource(), block -> this.blockStateOutput.accept(createSimpleBlock(block, TFModelTemplates.TINTED_CUBE_BOTTOM_TOP.create(block, TextureMapping.cubeTop(block).put(TextureSlot.BOTTOM, TextureMapping.getBlockTexture(Blocks.BLACK_CONCRETE_POWDER)), this.modelOutput))));
		this.wrapTintedBlockItem(TFBlocks.FIRE_JET.get(), new GrassColorSource(), block -> this.blockStateOutput.accept(createSimpleBlock(block, TFModelTemplates.TINTED_CUBE_BOTTOM_TOP.create(block, TextureMapping.cubeTop(block).put(TextureSlot.BOTTOM, TextureMapping.getBlockTexture(Blocks.BLACK_CONCRETE_POWDER)), this.modelOutput))));
		this.wrapBlockItem(TFBlocks.UNDERBRICK.get(), this::createTrivialCube);
		this.wrapBlockItem(TFBlocks.CRACKED_UNDERBRICK.get(), this::createTrivialCube);
		this.wrapBlockItem(TFBlocks.MOSSY_UNDERBRICK.get(), this::createTrivialCube);
		this.wrapBlockItem(TFBlocks.UNDERBRICK_FLOOR.get(), this::createTrivialCube);

		this.wrapBlockItem(TFBlocks.ANTIBUILDER.get(), block -> this.blockWithRenderType(block, "cutout", TFModelTemplates.ANTIBUILDER, TFTextureMapping::threeLayerBlock));
		this.blockWithRenderType(TFBlocks.ANTIBUILT_BLOCK.get(), "cutout", TFModelTemplates.ANTIBUILT_BLOCK, TFTextureMapping::twoLayerBlock);
		this.basicCtmBlock(TFBlocks.ARCTIC_FUR_BLOCK.get());
		//TODO aurora blocks
		this.wrapBlockItem(TFBlocks.BEANSTALK_LEAVES.get(), block -> this.blockWithRenderType(block, "cutout_mipped", ModelTemplates.CUBE_ALL, u -> TextureMapping.cube(Blocks.AZALEA_LEAVES)));
		this.castleDoor(TFBlocks.BLUE_CASTLE_DOOR.get());
		this.forcefield(TFBlocks.BLUE_FORCE_FIELD.get());
		this.wrapBlockItem(TFBlocks.BOLD_CASTLE_BRICK_PILLAR.get(), block -> this.createRotatedPillarWithHorizontalVariant(block, TexturedModel.COLUMN_ALT, TexturedModel.COLUMN_HORIZONTAL_ALT));
		this.generateStairs(TFBlocks.BOLD_CASTLE_BRICK_STAIRS.get(), TextureMapping.cube(TFBlocks.BOLD_CASTLE_BRICK_TILE.get()));
		this.wrapBlockItem(TFBlocks.BOLD_CASTLE_BRICK_TILE.get(), this::createTrivialCube);

		var builtMapping = TextureMapping.cube(TFBlocks.BUILT_BLOCK.get());
		ResourceLocation builtOff = TFModelTemplates.FULLBRIGHT_BLOCK.create(TFBlocks.BUILT_BLOCK.get(), builtMapping, this.modelOutput);
		ResourceLocation builtOn = TFModelTemplates.FULLBRIGHT_BLOCK.createWithSuffix(TFBlocks.BUILT_BLOCK.get(), "_active", builtMapping, this.modelOutput);
		this.blockStateOutput.accept(MultiVariantGenerator.multiVariant(TFBlocks.BUILT_BLOCK.get())
			.with(PropertyDispatch.property(TranslucentBuiltBlock.ACTIVE).generate(active -> Variant.variant().with(VariantProperties.MODEL, active ? builtOn : builtOff))));

		this.generateSpecialModel(TFBlocks.KEEPSAKE_CASKET.get(), Blocks.NETHERITE_BLOCK, block -> ItemModelUtils.specialModel(TwilightForestMod.prefix("item/keepsake_casket"), new KeepsakeCasketSpecialRenderer.Unbaked()));
		this.generateSpecialModel(TFBlocks.SKULL_CHEST.get(), Blocks.BONE_BLOCK, block -> ItemModelUtils.specialModel(TwilightForestMod.prefix("item/skull_chest"), new SkullChestSpecialRenderer.Unbaked()));
		this.generateSpecialModel(TFBlocks.CICADA.get(), Blocks.SLIME_BLOCK, block -> ItemModelUtils.specialModel(TwilightForestMod.prefix("item/cicada"), new CicadaSpecialRenderer.Unbaked()));
		this.generateSpecialModel(TFBlocks.FIREFLY.get(), Blocks.SLIME_BLOCK, block -> ItemModelUtils.specialModel(TwilightForestMod.prefix("item/firefly"), new FireflySpecialRenderer.Unbaked()));
		this.generateSpecialModel(TFBlocks.MOONWORM.get(), Blocks.SLIME_BLOCK, block -> ItemModelUtils.specialModel(TwilightForestMod.prefix("item/moonworm"), new MoonwormSpecialRenderer.Unbaked()));

		this.blockStateOutput.accept(MultiVariantGenerator.multiVariant(TFBlocks.CANDELABRA.get())
			.with(PropertyDispatch.property(CandelabraBlock.ON_WALL)
				.select(true, Variant.variant().with(VariantProperties.MODEL, TwilightForestMod.prefix("block/wall_candelabra")))
				.select(false, Variant.variant().with(VariantProperties.MODEL, TwilightForestMod.prefix("block/candelabra"))))
			.with(PropertyDispatch.property(CandelabraBlock.FACING)
				.select(Direction.NORTH, Variant.variant())
				.select(Direction.EAST, Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
				.select(Direction.WEST, Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
				.select(Direction.SOUTH, Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))));
		this.itemModelOutput.accept(TFBlocks.CANDELABRA.asItem(), ItemModelUtils.composite(ItemModelUtils.plainModel(ModelLocationUtils.getModelLocation(TFBlocks.CANDELABRA.get())), ItemModelUtils.specialModel(TwilightForestMod.prefix("block/candelabra"), new CandelabraSpecialRenderer.Unbaked())));

		this.generateSkullCandle(TFBlocks.ZOMBIE_SKULL_CANDLE.get(), TFBlocks.ZOMBIE_WALL_SKULL_CANDLE.get());
		this.generateSkullCandle(TFBlocks.SKELETON_SKULL_CANDLE.get(), TFBlocks.SKELETON_WALL_SKULL_CANDLE.get());
		this.generateSkullCandle(TFBlocks.CREEPER_SKULL_CANDLE.get(), TFBlocks.CREEPER_WALL_SKULL_CANDLE.get());
		this.generateSkullCandle(TFBlocks.WITHER_SKELE_SKULL_CANDLE.get(), TFBlocks.WITHER_SKELE_WALL_SKULL_CANDLE.get());
		this.generateSkullCandle(TFBlocks.PLAYER_SKULL_CANDLE.get(), TFBlocks.PLAYER_WALL_SKULL_CANDLE.get());
		this.generateSkullCandle(TFBlocks.PIGLIN_SKULL_CANDLE.get(), TFBlocks.PIGLIN_WALL_SKULL_CANDLE.get());

		var major = ItemModelUtils.plainModel(ModelTemplates.FLAT_ITEM.create(TwilightForestMod.prefix("item/major_boss_trophy"), TextureMapping.layer0(TwilightForestMod.prefix("item/trophy")), this.modelOutput));
		var minor = ItemModelUtils.plainModel(ModelTemplates.FLAT_ITEM.create(TwilightForestMod.prefix("item/minor_boss_trophy"), TextureMapping.layer0(TwilightForestMod.prefix("item/trophy_minor")), this.modelOutput));
		var quest = ItemModelUtils.plainModel(ModelTemplates.FLAT_ITEM.create(TwilightForestMod.prefix("item/quest_trophy"), TextureMapping.layer0(TwilightForestMod.prefix("item/trophy_quest")), this.modelOutput));

		this.generateTrophy(TFBlocks.NAGA_TROPHY.get(), TFBlocks.NAGA_WALL_TROPHY.get(), major);
		this.generateTrophy(TFBlocks.LICH_TROPHY.get(), TFBlocks.LICH_WALL_TROPHY.get(), major);
		this.generateTrophy(TFBlocks.MINOSHROOM_TROPHY.get(), TFBlocks.MINOSHROOM_WALL_TROPHY.get(), minor, "smaller_gui_trophy");
		this.generateTrophy(TFBlocks.HYDRA_TROPHY.get(), TFBlocks.HYDRA_WALL_TROPHY.get(), major, "hydra_trophy");
		this.generateTrophy(TFBlocks.KNIGHT_PHANTOM_TROPHY.get(), TFBlocks.KNIGHT_PHANTOM_WALL_TROPHY.get(), minor, "smaller_gui_trophy");
		this.generateTrophy(TFBlocks.UR_GHAST_TROPHY.get(), TFBlocks.UR_GHAST_WALL_TROPHY.get(), major, "ur_ghast_trophy");
		this.generateTrophy(TFBlocks.ALPHA_YETI_TROPHY.get(), TFBlocks.ALPHA_YETI_WALL_TROPHY.get(), minor, "alpha_yeti_trophy");
		this.generateTrophy(TFBlocks.SNOW_QUEEN_TROPHY.get(), TFBlocks.SNOW_QUEEN_WALL_TROPHY.get(), major);
		this.generateTrophy(TFBlocks.QUEST_RAM_TROPHY.get(), TFBlocks.QUEST_RAM_WALL_TROPHY.get(), quest, "smaller_gui_trophy");
	}

	private void generateWoodBlocks() {
		this.wrapBlockItem(TFBlocks.ROOT_BLOCK.get(), this::createTrivialCube);
		this.wrapBlockItem(TFBlocks.LIVEROOT_BLOCK.get(), this::createTrivialCube);

		this.wrapBlockItem(TFBlocks.TWILIGHT_OAK_LOG.get(), block -> this.generateLog(block, TextureMapping.logColumn(TFBlocks.TWILIGHT_OAK_LOG.get())));
		this.wrapBlockItem(TFBlocks.TWILIGHT_OAK_WOOD.get(), block -> this.generateWood(block, TextureMapping.logColumn(TFBlocks.TWILIGHT_OAK_LOG.get())));
		this.wrapBlockItem(TFBlocks.STRIPPED_TWILIGHT_OAK_LOG.get(), block -> this.generateLog(block, TextureMapping.logColumn(TFBlocks.STRIPPED_TWILIGHT_OAK_LOG.get())));
		this.wrapBlockItem(TFBlocks.STRIPPED_TWILIGHT_OAK_WOOD.get(), block -> this.generateLog(block, TextureMapping.logColumn(TFBlocks.STRIPPED_TWILIGHT_OAK_LOG.get())));
		this.generateHollowLog(TFBlocks.TWILIGHT_OAK_LOG.get(), TFBlocks.STRIPPED_TWILIGHT_OAK_LOG.get(), TFBlocks.HOLLOW_TWILIGHT_OAK_LOG_HORIZONTAL.get(), TFBlocks.HOLLOW_TWILIGHT_OAK_LOG_VERTICAL.get(), TFBlocks.HOLLOW_TWILIGHT_OAK_LOG_CLIMBABLE.get());
		this.generateSapling(TFBlocks.TWILIGHT_OAK_SAPLING.get(), TFBlocks.POTTED_TWILIGHT_OAK_SAPLING.get(), BlockModelGenerators.PlantType.NOT_TINTED);
		this.generateLeaves(TFBlocks.TWILIGHT_OAK_LEAVES.get(), TextureMapping.cube(Blocks.OAK_LEAVES), -12012264);
		this.wrapBlockItem(TFBlocks.TWILIGHT_OAK_PLANKS.get(), this::createTrivialCube);
		TextureMapping twilightOak = TextureMapping.cube(TFBlocks.TWILIGHT_OAK_PLANKS.get());
		this.generateStairs(TFBlocks.TWILIGHT_OAK_STAIRS.get(), twilightOak);
		this.generateSlab(TFBlocks.TWILIGHT_OAK_SLAB.get(), TFBlocks.TWILIGHT_OAK_PLANKS.get(), twilightOak);
		this.generateButton(TFBlocks.TWILIGHT_OAK_BUTTON.get(), twilightOak);
		this.generateFence(TFBlocks.TWILIGHT_OAK_FENCE.get(), twilightOak);
		this.generateFenceGate(TFBlocks.TWILIGHT_OAK_GATE.get(), twilightOak);
		this.generatePressurePlate(TFBlocks.TWILIGHT_OAK_PLATE.get(), twilightOak);
		this.generateTrapdoor(TFBlocks.TWILIGHT_OAK_TRAPDOOR.get(), true, "solid");
		this.generateDoor(TFBlocks.TWILIGHT_OAK_DOOR.get(), false, "solid");
		this.generateSign(TFBlocks.TWILIGHT_OAK_SIGN.get(), TFBlocks.TWILIGHT_WALL_SIGN.get(), twilightOak);
		this.generateHangingSign(TFBlocks.TWILIGHT_OAK_HANGING_SIGN.get(), TFBlocks.TWILIGHT_OAK_WALL_HANGING_SIGN.get(), TFBlocks.STRIPPED_TWILIGHT_OAK_LOG.get());
		this.generateBanister(TFBlocks.TWILIGHT_OAK_BANISTER.get(), twilightOak);

		this.wrapBlockItem(TFBlocks.CANOPY_LOG.get(), block -> this.generateLog(block, TextureMapping.logColumn(TFBlocks.CANOPY_LOG.get())));
		this.wrapBlockItem(TFBlocks.CANOPY_WOOD.get(), block -> this.generateWood(block, TextureMapping.logColumn(TFBlocks.CANOPY_LOG.get())));
		this.wrapBlockItem(TFBlocks.STRIPPED_CANOPY_LOG.get(), block -> this.generateLog(block, TextureMapping.logColumn(TFBlocks.STRIPPED_CANOPY_LOG.get())));
		this.wrapBlockItem(TFBlocks.STRIPPED_CANOPY_WOOD.get(), block -> this.generateLog(block, TextureMapping.logColumn(TFBlocks.STRIPPED_CANOPY_LOG.get())));
		this.generateHollowLog(TFBlocks.CANOPY_LOG.get(), TFBlocks.STRIPPED_CANOPY_LOG.get(), TFBlocks.HOLLOW_CANOPY_LOG_HORIZONTAL.get(), TFBlocks.HOLLOW_CANOPY_LOG_VERTICAL.get(), TFBlocks.HOLLOW_CANOPY_LOG_CLIMBABLE.get());
		this.generateSapling(TFBlocks.CANOPY_SAPLING.get(), TFBlocks.POTTED_CANOPY_SAPLING.get(), BlockModelGenerators.PlantType.NOT_TINTED);
		this.generateLeaves(TFBlocks.CANOPY_LEAVES.get(), TextureMapping.cube(Blocks.SPRUCE_LEAVES), -10380959);
		this.wrapBlockItem(TFBlocks.CANOPY_PLANKS.get(), this::createTrivialCube);
		TextureMapping canopy = TextureMapping.cube(TFBlocks.CANOPY_PLANKS.get());
		this.generateStairs(TFBlocks.CANOPY_STAIRS.get(), canopy);
		this.generateSlab(TFBlocks.CANOPY_SLAB.get(), TFBlocks.CANOPY_PLANKS.get(), canopy);
		this.generateButton(TFBlocks.CANOPY_BUTTON.get(), canopy);
		this.generateFence(TFBlocks.CANOPY_FENCE.get(), canopy);
		this.generateFenceGate(TFBlocks.CANOPY_GATE.get(), canopy);
		this.generatePressurePlate(TFBlocks.CANOPY_PLATE.get(), canopy);
		this.generateTrapdoor(TFBlocks.CANOPY_TRAPDOOR.get(), true, "solid");
		this.generateDoor(TFBlocks.CANOPY_DOOR.get(), false, "solid");
		this.generateSign(TFBlocks.CANOPY_SIGN.get(), TFBlocks.CANOPY_WALL_SIGN.get(), canopy);
		this.generateHangingSign(TFBlocks.CANOPY_HANGING_SIGN.get(), TFBlocks.CANOPY_WALL_HANGING_SIGN.get(), TFBlocks.STRIPPED_CANOPY_LOG.get());
		this.generateBanister(TFBlocks.CANOPY_BANISTER.get(), canopy);
		this.wrapBlockItem(TFBlocks.CANOPY_BOOKSHELF.get(), block -> this.blockStateOutput.accept(createSimpleBlock(block, ModelTemplates.CUBE_COLUMN.create(block, TextureMapping.column(TextureMapping.getBlockTexture(block), TextureMapping.getBlockTexture(TFBlocks.CANOPY_PLANKS.get())), this.modelOutput))));
		this.createChiseledBookshelf(TFBlocks.CHISELED_CANOPY_BOOKSHELF.get());
		this.wrapBlockItem(TFBlocks.CANOPY_WINDOW.get(), block -> this.simpleBlockWithRenderType(block, "translucent"));
		this.createPaneBlock(TFBlocks.CANOPY_WINDOW.get(), TFBlocks.CANOPY_WINDOW_PANE.get());

		this.wrapBlockItem(TFBlocks.MANGROVE_LOG.get(), block -> this.generateLog(block, TextureMapping.logColumn(TFBlocks.MANGROVE_LOG.get())));
		this.wrapBlockItem(TFBlocks.MANGROVE_WOOD.get(), block -> this.generateWood(block, TextureMapping.logColumn(TFBlocks.MANGROVE_LOG.get())));
		this.wrapBlockItem(TFBlocks.STRIPPED_MANGROVE_LOG.get(), block -> this.generateLog(block, TextureMapping.logColumn(TFBlocks.STRIPPED_MANGROVE_LOG.get())));
		this.wrapBlockItem(TFBlocks.STRIPPED_MANGROVE_WOOD.get(), block -> this.generateLog(block, TextureMapping.logColumn(TFBlocks.STRIPPED_MANGROVE_LOG.get())));
		this.generateHollowLog(TFBlocks.MANGROVE_LOG.get(), TFBlocks.STRIPPED_MANGROVE_LOG.get(), TFBlocks.HOLLOW_MANGROVE_LOG_HORIZONTAL.get(), TFBlocks.HOLLOW_MANGROVE_LOG_VERTICAL.get(), TFBlocks.HOLLOW_MANGROVE_LOG_CLIMBABLE.get());
		this.generateSapling(TFBlocks.MANGROVE_SAPLING.get(), TFBlocks.POTTED_MANGROVE_SAPLING.get(), BlockModelGenerators.PlantType.NOT_TINTED);
		this.generateLeaves(TFBlocks.MANGROVE_LEAVES.get(), TextureMapping.cube(Blocks.BIRCH_LEAVES), -8345771);
		this.wrapBlockItem(TFBlocks.MANGROVE_ROOT.get(), this::createTrivialCube);
		this.wrapBlockItem(TFBlocks.MANGROVE_PLANKS.get(), this::createTrivialCube);
		TextureMapping mangrove = TextureMapping.cube(TFBlocks.MANGROVE_PLANKS.get());
		this.generateStairs(TFBlocks.MANGROVE_STAIRS.get(), mangrove);
		this.generateSlab(TFBlocks.MANGROVE_SLAB.get(), TFBlocks.MANGROVE_PLANKS.get(), mangrove);
		this.generateButton(TFBlocks.MANGROVE_BUTTON.get(), mangrove);
		this.generateFence(TFBlocks.MANGROVE_FENCE.get(), mangrove);
		this.generateFenceGate(TFBlocks.MANGROVE_GATE.get(), mangrove);
		this.generatePressurePlate(TFBlocks.MANGROVE_PLATE.get(), mangrove);
		this.generateTrapdoor(TFBlocks.MANGROVE_TRAPDOOR.get(), true, "solid");
		this.generateDoor(TFBlocks.MANGROVE_DOOR.get(), false, "solid");
		this.generateSign(TFBlocks.MANGROVE_SIGN.get(), TFBlocks.MANGROVE_WALL_SIGN.get(), mangrove);
		this.generateHangingSign(TFBlocks.MANGROVE_HANGING_SIGN.get(), TFBlocks.MANGROVE_WALL_HANGING_SIGN.get(), TFBlocks.STRIPPED_MANGROVE_LOG.get());
		this.generateBanister(TFBlocks.MANGROVE_BANISTER.get(), mangrove);

		this.wrapBlockItem(TFBlocks.DARK_LOG.get(), block -> this.generateLog(block, TextureMapping.logColumn(TFBlocks.DARK_LOG.get())));
		this.wrapBlockItem(TFBlocks.DARK_WOOD.get(), block -> this.generateWood(block, TextureMapping.logColumn(TFBlocks.DARK_LOG.get())));
		this.wrapBlockItem(TFBlocks.STRIPPED_DARK_LOG.get(), block -> this.generateLog(block, TextureMapping.logColumn(TFBlocks.STRIPPED_DARK_LOG.get())));
		this.wrapBlockItem(TFBlocks.STRIPPED_DARK_WOOD.get(), block -> this.generateLog(block, TextureMapping.logColumn(TFBlocks.STRIPPED_DARK_LOG.get())));
		this.generateHollowLog(TFBlocks.DARK_LOG.get(), TFBlocks.STRIPPED_DARK_LOG.get(), TFBlocks.HOLLOW_DARK_LOG_HORIZONTAL.get(), TFBlocks.HOLLOW_DARK_LOG_VERTICAL.get(), TFBlocks.HOLLOW_DARK_LOG_CLIMBABLE.get());
		this.generateSapling(TFBlocks.DARKWOOD_SAPLING.get(), TFBlocks.POTTED_DARKWOOD_SAPLING.get(), BlockModelGenerators.PlantType.NOT_TINTED);
		this.generateLeaves(TFBlocks.DARK_LEAVES.get(), -12012264);
		this.wrapBlockItem(TFBlocks.DARK_PLANKS.get(), this::createTrivialCube);
		TextureMapping dark = TextureMapping.cube(TFBlocks.DARK_PLANKS.get());
		this.generateStairs(TFBlocks.DARK_STAIRS.get(), dark);
		this.generateSlab(TFBlocks.DARK_SLAB.get(), TFBlocks.DARK_PLANKS.get(), dark);
		this.generateButton(TFBlocks.DARK_BUTTON.get(), dark);
		this.generateFence(TFBlocks.DARK_FENCE.get(), dark);
		this.generateFenceGate(TFBlocks.DARK_GATE.get(), dark);
		this.generatePressurePlate(TFBlocks.DARK_PLATE.get(), dark);
		this.generateTrapdoor(TFBlocks.DARK_TRAPDOOR.get(), true, "solid");
		this.generateDoor(TFBlocks.DARK_DOOR.get(), false, "solid");
		this.generateSign(TFBlocks.DARK_SIGN.get(), TFBlocks.DARK_WALL_SIGN.get(), dark);
		this.generateHangingSign(TFBlocks.DARK_HANGING_SIGN.get(), TFBlocks.DARK_WALL_HANGING_SIGN.get(), TFBlocks.STRIPPED_DARK_LOG.get());
		this.generateBanister(TFBlocks.DARK_BANISTER.get(), dark);

		this.wrapBlockItem(TFBlocks.TIME_LOG.get(), block -> this.generateLog(block, TextureMapping.logColumn(TFBlocks.TIME_LOG.get())));
		this.wrapBlockItem(TFBlocks.TIME_WOOD.get(), block -> this.generateWood(block, TextureMapping.logColumn(TFBlocks.TIME_LOG.get())));
		this.wrapBlockItem(TFBlocks.STRIPPED_TIME_LOG.get(), block -> this.generateLog(block, TextureMapping.logColumn(TFBlocks.STRIPPED_TIME_LOG.get())));
		this.wrapBlockItem(TFBlocks.STRIPPED_TIME_WOOD.get(), block -> this.generateLog(block, TextureMapping.logColumn(TFBlocks.STRIPPED_TIME_LOG.get())));
		this.generateTreeCore(TFBlocks.TIME_LOG.get(), TFBlocks.TIME_LOG_CORE.get());
		this.generateHollowLog(TFBlocks.TIME_LOG.get(), TFBlocks.STRIPPED_TIME_LOG.get(), TFBlocks.HOLLOW_TIME_LOG_HORIZONTAL.get(), TFBlocks.HOLLOW_TIME_LOG_VERTICAL.get(), TFBlocks.HOLLOW_TIME_LOG_CLIMBABLE.get());
		this.generateSapling(TFBlocks.TIME_SAPLING.get(), TFBlocks.POTTED_TIME_SAPLING.get(), BlockModelGenerators.PlantType.NOT_TINTED);
		this.generateLeaves(TFBlocks.TIME_LEAVES.get(), 6986775);
		this.wrapBlockItem(TFBlocks.TIME_PLANKS.get(), this::createTrivialCube);
		TextureMapping time = TextureMapping.cube(TFBlocks.TIME_PLANKS.get());
		this.generateStairs(TFBlocks.TIME_STAIRS.get(), time);
		this.generateSlab(TFBlocks.TIME_SLAB.get(), TFBlocks.TIME_PLANKS.get(), time);
		this.generateButton(TFBlocks.TIME_BUTTON.get(), time);
		this.generateFence(TFBlocks.TIME_FENCE.get(), time);
		this.generateFenceGate(TFBlocks.TIME_GATE.get(), time);
		this.generatePressurePlate(TFBlocks.TIME_PLATE.get(), time);
		this.generateTrapdoor(TFBlocks.TIME_TRAPDOOR.get(), true, "cutout");
		this.generateDoor(TFBlocks.TIME_DOOR.get(), false, "cutout");
		this.generateSign(TFBlocks.TIME_SIGN.get(), TFBlocks.TIME_WALL_SIGN.get(), time);
		this.generateHangingSign(TFBlocks.TIME_HANGING_SIGN.get(), TFBlocks.TIME_WALL_HANGING_SIGN.get(), TFBlocks.STRIPPED_TIME_LOG.get());
		this.generateBanister(TFBlocks.TIME_BANISTER.get(), time);

		this.wrapBlockItem(TFBlocks.TRANSFORMATION_LOG.get(), block -> this.generateLog(block, TextureMapping.logColumn(TFBlocks.TRANSFORMATION_LOG.get())));
		this.wrapBlockItem(TFBlocks.TRANSFORMATION_WOOD.get(), block -> this.generateWood(block, TextureMapping.logColumn(TFBlocks.TRANSFORMATION_LOG.get())));
		this.wrapBlockItem(TFBlocks.STRIPPED_TRANSFORMATION_LOG.get(), block -> this.generateLog(block, TextureMapping.logColumn(TFBlocks.STRIPPED_TRANSFORMATION_LOG.get())));
		this.wrapBlockItem(TFBlocks.STRIPPED_TRANSFORMATION_WOOD.get(), block -> this.generateLog(block, TextureMapping.logColumn(TFBlocks.STRIPPED_TRANSFORMATION_LOG.get())));
		this.generateTreeCore(TFBlocks.TRANSFORMATION_LOG.get(), TFBlocks.TRANSFORMATION_LOG_CORE.get());
		this.generateHollowLog(TFBlocks.TRANSFORMATION_LOG.get(), TFBlocks.STRIPPED_TRANSFORMATION_LOG.get(), TFBlocks.HOLLOW_TRANSFORMATION_LOG_HORIZONTAL.get(), TFBlocks.HOLLOW_TRANSFORMATION_LOG_VERTICAL.get(), TFBlocks.HOLLOW_TRANSFORMATION_LOG_CLIMBABLE.get());
		this.generateSapling(TFBlocks.TRANSFORMATION_SAPLING.get(), TFBlocks.POTTED_TRANSFORMATION_SAPLING.get(), BlockModelGenerators.PlantType.NOT_TINTED);
		this.generateLeaves(TFBlocks.TRANSFORMATION_LEAVES.get(), 7130346);
		this.wrapBlockItem(TFBlocks.TRANSFORMATION_PLANKS.get(), this::createTrivialCube);
		TextureMapping transformation = TextureMapping.cube(TFBlocks.TRANSFORMATION_PLANKS.get());
		this.generateStairs(TFBlocks.TRANSFORMATION_STAIRS.get(), transformation);
		this.generateSlab(TFBlocks.TRANSFORMATION_SLAB.get(), TFBlocks.TRANSFORMATION_PLANKS.get(), transformation);
		this.generateButton(TFBlocks.TRANSFORMATION_BUTTON.get(), transformation);
		this.generateFence(TFBlocks.TRANSFORMATION_FENCE.get(), transformation);
		this.generateFenceGate(TFBlocks.TRANSFORMATION_GATE.get(), transformation);
		this.generatePressurePlate(TFBlocks.TRANSFORMATION_PLATE.get(), transformation);
		this.generateTrapdoor(TFBlocks.TRANSFORMATION_TRAPDOOR.get(), true, "cutout");
		this.generateDoor(TFBlocks.TRANSFORMATION_DOOR.get(), false, "cutout");
		this.generateSign(TFBlocks.TRANSFORMATION_SIGN.get(), TFBlocks.TRANSFORMATION_WALL_SIGN.get(), transformation);
		this.generateHangingSign(TFBlocks.TRANSFORMATION_HANGING_SIGN.get(), TFBlocks.TRANSFORMATION_WALL_HANGING_SIGN.get(), TFBlocks.STRIPPED_TRANSFORMATION_LOG.get());
		this.generateBanister(TFBlocks.TRANSFORMATION_BANISTER.get(), transformation);

		this.wrapBlockItem(TFBlocks.MINING_LOG.get(), block -> this.generateLog(block, TextureMapping.logColumn(TFBlocks.MINING_LOG.get())));
		this.wrapBlockItem(TFBlocks.MINING_WOOD.get(), block -> this.generateWood(block, TextureMapping.logColumn(TFBlocks.MINING_LOG.get())));
		this.wrapBlockItem(TFBlocks.STRIPPED_MINING_LOG.get(), block -> this.generateLog(block, TextureMapping.logColumn(TFBlocks.STRIPPED_MINING_LOG.get())));
		this.wrapBlockItem(TFBlocks.STRIPPED_MINING_WOOD.get(), block -> this.generateLog(block, TextureMapping.logColumn(TFBlocks.STRIPPED_MINING_LOG.get())));
		this.generateTreeCore(TFBlocks.MINING_LOG.get(), TFBlocks.MINING_LOG_CORE.get());
		this.generateHollowLog(TFBlocks.MINING_LOG.get(), TFBlocks.STRIPPED_MINING_LOG.get(), TFBlocks.HOLLOW_MINING_LOG_HORIZONTAL.get(), TFBlocks.HOLLOW_MINING_LOG_VERTICAL.get(), TFBlocks.HOLLOW_MINING_LOG_CLIMBABLE.get());
		this.generateSapling(TFBlocks.MINING_SAPLING.get(), TFBlocks.POTTED_MINING_SAPLING.get(), BlockModelGenerators.PlantType.NOT_TINTED);
		this.generateLeaves(TFBlocks.MINING_LEAVES.get(), 16576836);
		this.wrapBlockItem(TFBlocks.MINING_PLANKS.get(), this::createTrivialCube);
		TextureMapping mining = TextureMapping.cube(TFBlocks.MINING_PLANKS.get());
		this.generateStairs(TFBlocks.MINING_STAIRS.get(), mining);
		this.generateSlab(TFBlocks.MINING_SLAB.get(), TFBlocks.MINING_PLANKS.get(), mining);
		this.generateButton(TFBlocks.MINING_BUTTON.get(), mining);
		this.generateFence(TFBlocks.MINING_FENCE.get(), mining);
		this.generateFenceGate(TFBlocks.MINING_GATE.get(), mining);
		this.generatePressurePlate(TFBlocks.MINING_PLATE.get(), mining);
		this.generateTrapdoor(TFBlocks.MINING_TRAPDOOR.get(), true, "solid");
		this.generateDoor(TFBlocks.MINING_DOOR.get(), false, "solid");
		this.generateSign(TFBlocks.MINING_SIGN.get(), TFBlocks.MINING_WALL_SIGN.get(), mining);
		this.generateHangingSign(TFBlocks.MINING_HANGING_SIGN.get(), TFBlocks.MINING_WALL_HANGING_SIGN.get(), TFBlocks.STRIPPED_MINING_LOG.get());
		this.generateBanister(TFBlocks.MINING_BANISTER.get(), mining);

		this.wrapBlockItem(TFBlocks.SORTING_LOG.get(), block -> this.generateLog(block, TextureMapping.logColumn(TFBlocks.SORTING_LOG.get())));
		this.wrapBlockItem(TFBlocks.SORTING_WOOD.get(), block -> this.generateWood(block, TextureMapping.logColumn(TFBlocks.SORTING_LOG.get())));
		this.wrapBlockItem(TFBlocks.STRIPPED_SORTING_LOG.get(), block -> this.generateLog(block, TextureMapping.logColumn(TFBlocks.STRIPPED_SORTING_LOG.get())));
		this.wrapBlockItem(TFBlocks.STRIPPED_SORTING_WOOD.get(), block -> this.generateLog(block, TextureMapping.logColumn(TFBlocks.STRIPPED_SORTING_LOG.get())));
		this.generateTreeCore(TFBlocks.SORTING_LOG.get(), TFBlocks.SORTING_LOG_CORE.get());
		this.generateHollowLog(TFBlocks.SORTING_LOG.get(), TFBlocks.STRIPPED_SORTING_LOG.get(), TFBlocks.HOLLOW_SORTING_LOG_HORIZONTAL.get(), TFBlocks.HOLLOW_SORTING_LOG_VERTICAL.get(), TFBlocks.HOLLOW_SORTING_LOG_CLIMBABLE.get());
		this.generateSapling(TFBlocks.SORTING_SAPLING.get(), TFBlocks.POTTED_SORTING_SAPLING.get(), BlockModelGenerators.PlantType.NOT_TINTED);
		this.generateLeaves(TFBlocks.SORTING_LEAVES.get(), 3558403);
		this.wrapBlockItem(TFBlocks.SORTING_PLANKS.get(), this::createTrivialCube);
		TextureMapping sorting = TextureMapping.cube(TFBlocks.SORTING_PLANKS.get());
		this.generateStairs(TFBlocks.SORTING_STAIRS.get(), sorting);
		this.generateSlab(TFBlocks.SORTING_SLAB.get(), TFBlocks.SORTING_PLANKS.get(), sorting);
		this.generateButton(TFBlocks.SORTING_BUTTON.get(), sorting);
		this.generateFence(TFBlocks.SORTING_FENCE.get(), sorting);
		this.generateFenceGate(TFBlocks.SORTING_GATE.get(), sorting);
		this.generatePressurePlate(TFBlocks.SORTING_PLATE.get(), sorting);
		this.generateTrapdoor(TFBlocks.SORTING_TRAPDOOR.get(), true, "cutout");
		this.generateDoor(TFBlocks.SORTING_DOOR.get(), true, "cutout");
		this.generateSign(TFBlocks.SORTING_SIGN.get(), TFBlocks.SORTING_WALL_SIGN.get(), sorting);
		this.generateHangingSign(TFBlocks.SORTING_HANGING_SIGN.get(), TFBlocks.SORTING_WALL_HANGING_SIGN.get(), TFBlocks.STRIPPED_SORTING_LOG.get());
		this.generateBanister(TFBlocks.SORTING_BANISTER.get(), sorting);

		this.generateSapling(TFBlocks.HOLLOW_OAK_SAPLING.get(), TFBlocks.POTTED_HOLLOW_OAK_SAPLING.get(), BlockModelGenerators.PlantType.NOT_TINTED);
		this.generateLeaves(TFBlocks.RAINBOW_OAK_LEAVES.get(), TextureMapping.cube(Blocks.OAK_LEAVES), -12012264);
		this.generateSapling(TFBlocks.RAINBOW_OAK_SAPLING.get(), TFBlocks.POTTED_RAINBOW_OAK_SAPLING.get(), BlockModelGenerators.PlantType.NOT_TINTED);

		this.createChest(TFBlocks.TWILIGHT_OAK_CHEST.get(), TFBlocks.TWILIGHT_OAK_PLANKS.get(), TwilightForestMod.prefix("twilight_oak/normal"), false);
		this.createChest(TFBlocks.CANOPY_CHEST.get(), TFBlocks.CANOPY_PLANKS.get(), TwilightForestMod.prefix("canopy/normal"), false);
		this.createChest(TFBlocks.MANGROVE_CHEST.get(), TFBlocks.MANGROVE_PLANKS.get(), TwilightForestMod.prefix("mangrove/normal"), false);
		this.createChest(TFBlocks.DARK_CHEST.get(), TFBlocks.DARK_PLANKS.get(), TwilightForestMod.prefix("darkwood/normal"), false);
		this.createChest(TFBlocks.TIME_CHEST.get(), TFBlocks.TIME_PLANKS.get(), TwilightForestMod.prefix("time/normal"), false);
		this.createChest(TFBlocks.TRANSFORMATION_CHEST.get(), TFBlocks.TRANSFORMATION_PLANKS.get(), TwilightForestMod.prefix("transformation/normal"), false);
		this.createChest(TFBlocks.MINING_CHEST.get(), TFBlocks.MINING_PLANKS.get(), TwilightForestMod.prefix("mining/normal"), false);
		this.createChest(TFBlocks.SORTING_CHEST.get(), TFBlocks.SORTING_PLANKS.get(), TwilightForestMod.prefix("sorting/normal"), false);

		this.createChest(TFBlocks.TWILIGHT_OAK_TRAPPED_CHEST.get(), TFBlocks.TWILIGHT_OAK_PLANKS.get(), TwilightForestMod.prefix("twilight_oak/trapped"), false);
		this.createChest(TFBlocks.CANOPY_TRAPPED_CHEST.get(), TFBlocks.CANOPY_PLANKS.get(), TwilightForestMod.prefix("canopy/trapped"), false);
		this.createChest(TFBlocks.MANGROVE_TRAPPED_CHEST.get(), TFBlocks.MANGROVE_PLANKS.get(), TwilightForestMod.prefix("mangrove/trapped"), false);
		this.createChest(TFBlocks.DARK_TRAPPED_CHEST.get(), TFBlocks.DARK_PLANKS.get(), TwilightForestMod.prefix("darkwood/trapped"), false);
		this.createChest(TFBlocks.TIME_TRAPPED_CHEST.get(), TFBlocks.TIME_PLANKS.get(), TwilightForestMod.prefix("time/trapped"), false);
		this.createChest(TFBlocks.TRANSFORMATION_TRAPPED_CHEST.get(), TFBlocks.TRANSFORMATION_PLANKS.get(), TwilightForestMod.prefix("transformation/trapped"), false);
		this.createChest(TFBlocks.MINING_TRAPPED_CHEST.get(), TFBlocks.MINING_PLANKS.get(), TwilightForestMod.prefix("mining/trapped"), false);
		this.createChest(TFBlocks.SORTING_TRAPPED_CHEST.get(), TFBlocks.SORTING_PLANKS.get(), TwilightForestMod.prefix("sorting/trapped"), false);

		this.generateHollowLog(Blocks.OAK_LOG, Blocks.STRIPPED_OAK_LOG, TFBlocks.HOLLOW_OAK_LOG_HORIZONTAL.get(), TFBlocks.HOLLOW_OAK_LOG_VERTICAL.get(), TFBlocks.HOLLOW_OAK_LOG_CLIMBABLE.get());
		this.generateBanister(TFBlocks.OAK_BANISTER.get(), TextureMapping.cube(Blocks.OAK_PLANKS));
		this.generateHollowLog(Blocks.SPRUCE_LOG, Blocks.STRIPPED_SPRUCE_LOG, TFBlocks.HOLLOW_SPRUCE_LOG_HORIZONTAL.get(), TFBlocks.HOLLOW_SPRUCE_LOG_VERTICAL.get(), TFBlocks.HOLLOW_SPRUCE_LOG_CLIMBABLE.get());
		this.generateBanister(TFBlocks.SPRUCE_BANISTER.get(), TextureMapping.cube(Blocks.SPRUCE_PLANKS));
		this.generateHollowLog(Blocks.BIRCH_LOG, Blocks.STRIPPED_BIRCH_LOG, TFBlocks.HOLLOW_BIRCH_LOG_HORIZONTAL.get(), TFBlocks.HOLLOW_BIRCH_LOG_VERTICAL.get(), TFBlocks.HOLLOW_BIRCH_LOG_CLIMBABLE.get());
		this.generateBanister(TFBlocks.BIRCH_BANISTER.get(), TextureMapping.cube(Blocks.BIRCH_PLANKS));
		this.generateHollowLog(Blocks.JUNGLE_LOG, Blocks.STRIPPED_JUNGLE_LOG, TFBlocks.HOLLOW_JUNGLE_LOG_HORIZONTAL.get(), TFBlocks.HOLLOW_JUNGLE_LOG_VERTICAL.get(), TFBlocks.HOLLOW_JUNGLE_LOG_CLIMBABLE.get());
		this.generateBanister(TFBlocks.JUNGLE_BANISTER.get(), TextureMapping.cube(Blocks.JUNGLE_PLANKS));
		this.generateHollowLog(Blocks.ACACIA_LOG, Blocks.STRIPPED_ACACIA_LOG, TFBlocks.HOLLOW_ACACIA_LOG_HORIZONTAL.get(), TFBlocks.HOLLOW_ACACIA_LOG_VERTICAL.get(), TFBlocks.HOLLOW_ACACIA_LOG_CLIMBABLE.get());
		this.generateBanister(TFBlocks.ACACIA_BANISTER.get(), TextureMapping.cube(Blocks.ACACIA_PLANKS));
		this.generateHollowLog(Blocks.DARK_OAK_LOG, Blocks.STRIPPED_DARK_OAK_LOG, TFBlocks.HOLLOW_DARK_OAK_LOG_HORIZONTAL.get(), TFBlocks.HOLLOW_DARK_OAK_LOG_VERTICAL.get(), TFBlocks.HOLLOW_DARK_OAK_LOG_CLIMBABLE.get());
		this.generateBanister(TFBlocks.DARK_OAK_BANISTER.get(), TextureMapping.cube(Blocks.DARK_OAK_PLANKS));
		this.generateHollowLog(Blocks.CRIMSON_STEM, Blocks.STRIPPED_CRIMSON_STEM, TFBlocks.HOLLOW_CRIMSON_STEM_HORIZONTAL.get(), TFBlocks.HOLLOW_CRIMSON_STEM_VERTICAL.get(), TFBlocks.HOLLOW_CRIMSON_STEM_CLIMBABLE.get());
		this.generateBanister(TFBlocks.CRIMSON_BANISTER.get(), TextureMapping.cube(Blocks.CRIMSON_PLANKS));
		this.generateHollowLog(Blocks.WARPED_STEM, Blocks.STRIPPED_WARPED_STEM, TFBlocks.HOLLOW_WARPED_STEM_HORIZONTAL.get(), TFBlocks.HOLLOW_WARPED_STEM_VERTICAL.get(), TFBlocks.HOLLOW_WARPED_STEM_CLIMBABLE.get());
		this.generateBanister(TFBlocks.WARPED_BANISTER.get(), TextureMapping.cube(Blocks.WARPED_PLANKS));
		this.generateHollowLog(Blocks.MANGROVE_LOG, Blocks.STRIPPED_MANGROVE_LOG, TFBlocks.HOLLOW_VANGROVE_LOG_HORIZONTAL.get(), TFBlocks.HOLLOW_VANGROVE_LOG_VERTICAL.get(), TFBlocks.HOLLOW_VANGROVE_LOG_CLIMBABLE.get());
		this.generateBanister(TFBlocks.VANGROVE_BANISTER.get(), TextureMapping.cube(Blocks.MANGROVE_PLANKS));
		this.generateHollowLog(Blocks.CHERRY_LOG, Blocks.STRIPPED_CHERRY_LOG, TFBlocks.HOLLOW_CHERRY_LOG_HORIZONTAL.get(), TFBlocks.HOLLOW_CHERRY_LOG_VERTICAL.get(), TFBlocks.HOLLOW_CHERRY_LOG_CLIMBABLE.get());
		this.generateBanister(TFBlocks.CHERRY_BANISTER.get(), TextureMapping.cube(Blocks.CHERRY_PLANKS));
		this.generateBanister(TFBlocks.BAMBOO_BANISTER.get(), TextureMapping.cube(Blocks.BAMBOO_PLANKS));
		this.generateHollowLog(Blocks.PALE_OAK_LOG, Blocks.STRIPPED_PALE_OAK_LOG, TFBlocks.HOLLOW_PALE_OAK_LOG_HORIZONTAL.get(), TFBlocks.HOLLOW_PALE_OAK_LOG_VERTICAL.get(), TFBlocks.HOLLOW_PALE_OAK_LOG_CLIMBABLE.get());
		this.generateBanister(TFBlocks.PALE_OAK_BANISTER.get(), TextureMapping.cube(Blocks.PALE_OAK_PLANKS));
	}
}
