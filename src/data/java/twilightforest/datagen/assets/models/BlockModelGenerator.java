package twilightforest.datagen.assets.models;

import net.minecraft.client.color.item.GrassColorSource;
import net.minecraft.client.data.models.ItemModelOutput;
import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.client.data.models.blockstates.*;
import net.minecraft.client.data.models.model.*;
import net.minecraft.client.renderer.block.dispatch.Variant;
import net.minecraft.client.renderer.block.dispatch.multipart.CombinedCondition;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.util.random.Weighted;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.joml.Vector3f;
import twilightforest.TwilightForestMod;
import twilightforest.block.*;
import twilightforest.client.model.block.connected.ConnectedTextureBuilder;
import twilightforest.client.model.block.patch.PatchBuilder;
import twilightforest.client.renderer.special.*;
import twilightforest.datagen.helpers.models.BlockModelBuilders;
import twilightforest.init.TFBlocks;

import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class BlockModelGenerator extends BlockModelBuilders {
	public BlockModelGenerator(Consumer<BlockModelDefinitionGenerator> stateOutput, ItemModelOutput itemOutput, BiConsumer<Identifier, ModelInstance> modelOutput) {
		super(stateOutput, itemOutput, modelOutput);
	}

	@Override
	public void run() {
		this.generateWoodBlocks();

		this.blockStateOutput.accept(MultiPartGenerator.multiPart(TFBlocks.TWILIGHT_PORTAL.get())
			.with(plainVariant(ModelLocationUtils.getModelLocation(TFBlocks.TWILIGHT_PORTAL.get())))
			.with(condition().term(TFPortalBlock.DISALLOW_RETURN, true), plainVariant(ModelLocationUtils.getModelLocation(TFBlocks.TWILIGHT_PORTAL.get(), "_barrier"))));

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

		this.thorns(TFBlocks.BROWN_THORNS.get(), TFBlocks.POTTED_THORN.get());
		this.thorns(TFBlocks.GREEN_THORNS.get(), TFBlocks.POTTED_GREEN_THORN.get());
		this.thorns(TFBlocks.BURNT_THORNS.get(), TFBlocks.POTTED_DEAD_THORN.get());
		this.directionalCrossModel(TFBlocks.THORN_ROSE.get(), PlantType.NOT_TINTED);
		this.createTintedLeaves(TFBlocks.THORN_LEAVES.get(), TexturedModel.createDefault(block -> TextureMapping.cube(Blocks.SPRUCE_LEAVES), ModelTemplates.LEAVES), -10380959);
		this.wrapBlockItem(TFBlocks.DEADROCK.get(), this::createTrivialCube);
		this.wrapBlockItem(TFBlocks.CRACKED_DEADROCK.get(), this::createTrivialCube);
		this.wrapBlockItem(TFBlocks.WEATHERED_DEADROCK.get(), this::createTrivialCube);

		this.createCrossBlock(TFBlocks.FIDDLEHEAD.get(), PlantType.TINTED);
		this.blockStateOutput.accept(createSimpleBlock(TFBlocks.POTTED_FIDDLEHEAD.get(), plainVariant(ModelTemplates.TINTED_FLOWER_POT_CROSS.create(TFBlocks.POTTED_FIDDLEHEAD.get(), TextureMapping.singleSlot(TextureSlot.PLANT, new Material(TwilightForestMod.prefix("block/potted_fiddlehead"))), this.modelOutput))));
		this.createItemWithGrassTint(TFBlocks.FIDDLEHEAD.get());
		this.blockStateOutput.accept(createSimpleBlock(TFBlocks.MAYAPPLE.get(), plainVariant(ModelLocationUtils.getModelLocation(TFBlocks.MAYAPPLE.get()))));
		this.blockStateOutput.accept(createSimpleBlock(TFBlocks.POTTED_MAYAPPLE.get(), plainVariant(ModelLocationUtils.getModelLocation(TFBlocks.POTTED_MAYAPPLE.get()))));
		this.registerSimpleFlatItemModel(TFBlocks.MAYAPPLE.get());
		this.blockStateOutput.accept(createSimpleBlock(TFBlocks.CLOVER_PATCH.get(), plainVariant(TFModelTemplates.create("block", TextureSlot.TEXTURE, TextureSlot.PARTICLE).extend().customLoader(PatchBuilder::new, _ -> {}).build().create(TFBlocks.CLOVER_PATCH.get(), TextureMapping.defaultTexture(new Material(TwilightForestMod.prefix("block/cloverpatch"))), this.modelOutput))));
		this.itemModelOutput.accept(TFBlocks.CLOVER_PATCH.asItem(), ItemModelUtils.plainModel(ModelTemplates.FLAT_ITEM.create(TFBlocks.CLOVER_PATCH.asItem(), TextureMapping.layer0(new Material(TwilightForestMod.prefix("block/patch/clover"))), this.modelOutput)));
		this.blockStateOutput.accept(createSimpleBlock(TFBlocks.MOSS_PATCH.get(), plainVariant(TFModelTemplates.create("block", TextureSlot.TEXTURE, TextureSlot.PARTICLE).extend().customLoader(PatchBuilder::new, PatchBuilder::shaggify).build().create(TFBlocks.MOSS_PATCH.get(), TextureMapping.defaultTexture(new Material(TwilightForestMod.prefix("block/mosspatch"))), this.modelOutput))));
		this.itemModelOutput.accept(TFBlocks.MOSS_PATCH.asItem(), ItemModelUtils.plainModel(ModelTemplates.FLAT_ITEM.create(TFBlocks.MOSS_PATCH.asItem(), TextureMapping.layer0(new Material(TwilightForestMod.prefix("block/patch/moss"))), this.modelOutput)));
		this.blockStateOutput.accept(MultiVariantGenerator.dispatch(TFBlocks.TORCHBERRY_PLANT.get()).with(createBooleanModelDispatch(TorchberryPlantBlock.HAS_BERRIES,
			plainVariant(ModelTemplates.CROSS_EMISSIVE.createWithSuffix(TFBlocks.TORCHBERRY_PLANT.get(), "_berries", TFTextureMapping.crossEmissiveWithCustomSuffix(TFBlocks.TORCHBERRY_PLANT.get(), "_glow"), this.modelOutput)),
			plainVariant(ModelTemplates.CROSS.create(TFBlocks.TORCHBERRY_PLANT.get(), TextureMapping.cross(TFBlocks.TORCHBERRY_PLANT.get()), this.modelOutput)))));
		this.registerSimpleFlatItemModel(TFBlocks.TORCHBERRY_PLANT.get());
		this.blockStateOutput.accept(createSimpleBlock(TFBlocks.MUSHGLOOM.get(), plainVariant(ModelLocationUtils.getModelLocation(TFBlocks.MUSHGLOOM.get()))));
		this.registerTwoLayerFlatItemModel(TFBlocks.MUSHGLOOM.get(), "_head");
		this.blockStateOutput.accept(createSimpleBlock(TFBlocks.POTTED_MUSHGLOOM.get(), plainVariant(ModelTemplates.FLOWER_POT_CROSS.create(TFBlocks.POTTED_MUSHGLOOM.get(), TextureMapping.singleSlot(TextureSlot.PLANT, new Material(TwilightForestMod.prefix("block/potted_mushgloom"))), this.modelOutput))));
		this.wrapBlockItem(TFBlocks.HEDGE.get(), block -> this.blockStateOutput.accept(MultiVariantGenerator.dispatch(block, new MultiVariant(WeightedList.of(
			new Weighted<>(plainModel(ModelTemplates.CUBE_ALL.create(block, TextureMapping.cube(block), this.modelOutput)), 10),
			new Weighted<>(plainModel(ModelTemplates.CUBE_ALL.createWithSuffix(block, "_rose", TextureMapping.cube(TextureMapping.getBlockTexture(block, "_rose")), this.modelOutput)), 1))))));
		this.blockStateOutput.accept(createSimpleBlock(TFBlocks.ROOT_STRAND.get(), plainVariant(ModelLocationUtils.getModelLocation(TFBlocks.ROOT_STRAND.get()))));
		this.registerSimpleFlatItemModel(TFBlocks.ROOT_STRAND.get());
		this.blockStateOutput.accept(MultiVariantGenerator.dispatch(TFBlocks.FALLEN_LEAVES.get()).with(
			PropertyDispatch.initial(BlockStateProperties.LAYERS).generate(layer -> plainVariant(ModelTemplates.create("block", String.valueOf(layer), TextureSlot.TEXTURE, TextureSlot.PARTICLE)
				.extend().element(builder -> builder.from(0.0F, 0.0F, 0.0F).to(16.0F, layer == 1 ? 0.2F : (layer - 1) * 2, 16.0F)
					.allFacesExcept((direction, face) -> face.tintindex(0).texture(TextureSlot.TEXTURE), Set.of(Direction.DOWN))
					.face(Direction.DOWN, face -> face.cullface(Direction.DOWN).texture(TextureSlot.TEXTURE).tintindex(0))).build().create(TFBlocks.FALLEN_LEAVES.get(), TextureMapping.cube(Blocks.OAK_LEAVES), this.modelOutput)))));
		this.registerSimpleTintedItemModel(TFBlocks.FALLEN_LEAVES.get(), this.createFlatItemModelWithBlockTexture(TFBlocks.FALLEN_LEAVES.asItem(), Blocks.OAK_LEAVES), ItemModelUtils.constantTint(-12012264));

		this.nagaStone();

		this.blockStateOutput.accept(MultiVariantGenerator.dispatch(TFBlocks.SPIRAL_BRICKS.get()).with(PropertyDispatch.initial(SpiralBrickBlock.AXIS_FACING, SpiralBrickBlock.DIAGONAL).generate((axis, diagonals) ->
			plainVariant(TwilightForestMod.prefix("block/spiral_bricks/" + axis.getName() + "_spiral_" + diagonals.getSerializedName())))));
		this.itemModelOutput.accept(TFBlocks.SPIRAL_BRICKS.asItem(), ItemModelUtils.plainModel(TwilightForestMod.prefix("block/spiral_bricks/z_spiral_bottom_right")));

		this.wrapBlockItem(TFBlocks.TWISTED_STONE.get(), this::createTwistedStone);
		this.wrapBlockItem(TFBlocks.BOLD_STONE_PILLAR.get(), this::createBoldStonePillar);
		this.wrapBlockItem(TFBlocks.CORONATION_CARPET.get(), block -> this.blockStateOutput.accept(createSimpleBlock(block, plainVariant(TFModelTemplates.CTM_NO_BASE.extend().customLoader(ConnectedTextureBuilder::new, builder -> builder.connectsTo(block).addConnectionFaces(Direction.UP, Direction.DOWN).createElement(new Vector3f(0, 0, 0), new Vector3f(16, 1, 16))).build().create(block, TFTextureMapping.ctmBlock(block), this.modelOutput)))));
		this.stonePillar();
		this.wroughtIronFence();
		this.terrorcotta();
		this.makeJars();
		MultiVariant floorOminous = this.createFloorFireModels(TFBlocks.OMINOUS_FIRE.get());
		MultiVariant sideOminous = this.createSideFireModels(TFBlocks.OMINOUS_FIRE.get());
		this.blockStateOutput.accept(MultiPartGenerator.multiPart(TFBlocks.OMINOUS_FIRE.get())
			.with(floorOminous)
			.with(sideOminous)
			.with(sideOminous.with(Y_ROT_90))
			.with(sideOminous.with(Y_ROT_180))
			.with(sideOminous.with(Y_ROT_270))
		);
		this.createParticleOnlyBlock(TFBlocks.OMINOUS_CANDLE.get(), Blocks.CANDLE);
		this.createParticleOnlyBlock(TFBlocks.OMINOUS_BROWN_CANDLE.get(), Blocks.BROWN_CANDLE);
		this.createParticleOnlyBlock(TFBlocks.OMINOUS_WHITE_CANDLE.get(), Blocks.WHITE_CANDLE);
		this.createParticleOnlyBlock(TFBlocks.OMINOUS_LIGHT_GRAY_CANDLE.get(), Blocks.LIGHT_GRAY_CANDLE);
		this.createParticleOnlyBlock(TFBlocks.OMINOUS_GRAY_CANDLE.get(), Blocks.GRAY_CANDLE);
		this.createParticleOnlyBlock(TFBlocks.OMINOUS_BLACK_CANDLE.get(), Blocks.BLACK_CANDLE);
		this.createParticleOnlyBlock(TFBlocks.OMINOUS_RED_CANDLE.get(), Blocks.RED_CANDLE);
		this.createParticleOnlyBlock(TFBlocks.OMINOUS_ORANGE_CANDLE.get(), Blocks.ORANGE_CANDLE);
		this.createParticleOnlyBlock(TFBlocks.OMINOUS_YELLOW_CANDLE.get(), Blocks.YELLOW_CANDLE);
		this.createParticleOnlyBlock(TFBlocks.OMINOUS_GREEN_CANDLE.get(), Blocks.GREEN_CANDLE);
		this.createParticleOnlyBlock(TFBlocks.OMINOUS_LIME_CANDLE.get(), Blocks.LIME_CANDLE);
		this.createParticleOnlyBlock(TFBlocks.OMINOUS_BLUE_CANDLE.get(), Blocks.BLUE_CANDLE);
		this.createParticleOnlyBlock(TFBlocks.OMINOUS_CYAN_CANDLE.get(), Blocks.CYAN_CANDLE);
		this.createParticleOnlyBlock(TFBlocks.OMINOUS_LIGHT_BLUE_CANDLE.get(), Blocks.LIGHT_BLUE_CANDLE);
		this.createParticleOnlyBlock(TFBlocks.OMINOUS_PURPLE_CANDLE.get(), Blocks.PURPLE_CANDLE);
		this.createParticleOnlyBlock(TFBlocks.OMINOUS_MAGENTA_CANDLE.get(), Blocks.MAGENTA_CANDLE);
		this.createParticleOnlyBlock(TFBlocks.OMINOUS_PINK_CANDLE.get(), Blocks.PINK_CANDLE);

		this.generateHugeLilyPad();
		this.createCrossBlockWithDefaultItem(TFBlocks.HUGE_WATER_LILY.get(), PlantType.NOT_TINTED);
		this.blockStateOutput.accept(createSimpleBlock(TFBlocks.RED_THREAD.get(), plainVariant(ModelTemplates.PARTICLE_ONLY.create(TFBlocks.RED_THREAD.get(), TextureMapping.particle(new Material(TwilightForestMod.prefix("block/blank"))), this.modelOutput))));
		this.wrapBlockItem(TFBlocks.MAZE_SLIME_BLOCK.get(), block -> this.blockStateOutput.accept(createSimpleBlock(block, plainVariant(ModelTemplates.create(TwilightForestMod.prefix("maze_slime_block").toString(), TextureSlot.TEXTURE, TextureSlot.PARTICLE).extend().parent(Identifier.withDefaultNamespace("block/slime_block")).build().create(block, TextureMapping.cube(block).put(TextureSlot.PARTICLE, TextureMapping.getBlockTexture(block)), this.modelOutput)))));
		this.wrapBlockItem(TFBlocks.MAZESTONE.get(), this::createTrivialCube);
		this.wrapBlockItem(TFBlocks.MAZESTONE_BRICK.get(), this::createTrivialCube);
		this.wrapBlockItem(TFBlocks.CRACKED_MAZESTONE.get(), this::createTrivialCube);
		this.wrapBlockItem(TFBlocks.MOSSY_MAZESTONE.get(), this::createTrivialCube);
		this.wrapBlockItem(TFBlocks.DECORATIVE_MAZESTONE.get(), block -> this.blockStateOutput.accept(createSimpleBlock(block, plainVariant(ModelTemplates.CUBE_COLUMN.create(block, TextureMapping.column(TextureMapping.getBlockTexture(block), TextureMapping.getBlockTexture(TFBlocks.MAZESTONE.get())), this.modelOutput)))));
		this.wrapBlockItem(TFBlocks.CUT_MAZESTONE.get(), block -> this.blockStateOutput.accept(createSimpleBlock(block, plainVariant(ModelTemplates.CUBE_COLUMN.create(block, TextureMapping.column(TextureMapping.getBlockTexture(block), TextureMapping.getBlockTexture(TFBlocks.MAZESTONE.get())), this.modelOutput)))));
		this.wrapBlockItem(TFBlocks.MAZESTONE_MOSAIC.get(), block -> this.blockStateOutput.accept(createSimpleBlock(block, plainVariant(ModelTemplates.CUBE_COLUMN.create(block, TextureMapping.column(TextureMapping.getBlockTexture(TFBlocks.MAZESTONE_BRICK.get()), TextureMapping.getBlockTexture(block)), this.modelOutput)))));
		this.wrapBlockItem(TFBlocks.MAZESTONE_BORDER.get(), block -> this.blockStateOutput.accept(createSimpleBlock(block, plainVariant(ModelTemplates.CUBE_COLUMN.create(block, TextureMapping.column(TextureMapping.getBlockTexture(TFBlocks.MAZESTONE_BRICK.get()), TextureMapping.getBlockTexture(block)), this.modelOutput)))));
		this.wrapTintedBlockItem(TFBlocks.SMOKER.get(), new GrassColorSource(), block -> this.blockStateOutput.accept(createSimpleBlock(block, plainVariant(TFModelTemplates.TINTED_CUBE_BOTTOM_TOP.create(block,
			new TextureMapping()
				.put(TextureSlot.TOP, new Material(TwilightForestMod.prefix("block/firejet_top")))
				.put(TextureSlot.BOTTOM, TextureMapping.getBlockTexture(Blocks.BLACK_CONCRETE_POWDER))
				.put(TextureSlot.SIDE, new Material(TwilightForestMod.prefix("block/firejet_side")))
				.put(TextureSlot.PARTICLE, new Material(TwilightForestMod.prefix("block/firejet_side"))),
			this.modelOutput)))));
		this.wrapTintedBlockItem(TFBlocks.FIRE_JET.get(), new GrassColorSource(), block -> this.blockStateOutput.accept(createSimpleBlock(block, plainVariant(TFModelTemplates.TINTED_CUBE_BOTTOM_TOP.create(block,
			new TextureMapping()
				.put(TextureSlot.TOP, new Material(TwilightForestMod.prefix("block/firejet_top")))
				.put(TextureSlot.BOTTOM, TextureMapping.getBlockTexture(Blocks.BLACK_CONCRETE_POWDER))
				.put(TextureSlot.SIDE, new Material(TwilightForestMod.prefix("block/firejet_side")))
				.put(TextureSlot.PARTICLE, new Material(TwilightForestMod.prefix("block/firejet_side"))),
			this.modelOutput)))));
		this.wrapBlockItem(TFBlocks.UNDERBRICK.get(), this::createTrivialCube);
		this.wrapBlockItem(TFBlocks.CRACKED_UNDERBRICK.get(), this::createTrivialCube);
		this.wrapBlockItem(TFBlocks.MOSSY_UNDERBRICK.get(), this::createTrivialCube);
		this.wrapBlockItem(TFBlocks.UNDERBRICK_FLOOR.get(), this::createTrivialCube);
		this.trophyPedestal();
		this.wrapBlockItem(TFBlocks.STRONGHOLD_SHIELD.get(), block -> this.blockStateOutput.accept(createSimpleBlock(block, plainVariant(ModelTemplates.CUBE_TOP.create(block, new TextureMapping().put(TextureSlot.SIDE, new Material(TwilightForestMod.prefix("block/shield_outside"))).put(TextureSlot.TOP, new Material(TwilightForestMod.prefix("block/shield_inside"))), this.modelOutput))).with(PropertyDispatch.modify(BlockStateProperties.FACING)
			.select(Direction.UP, NOP)
			.select(Direction.DOWN, X_ROT_180)
			.select(Direction.NORTH, X_ROT_90)
			.select(Direction.SOUTH, X_ROT_90.then(Y_ROT_180))
			.select(Direction.WEST, X_ROT_90.then(Y_ROT_270))
			.select(Direction.EAST, X_ROT_90.then(Y_ROT_90)))));

		this.wrapBlockItem(TFBlocks.TOWERWOOD.get(), this::createTrivialCube);
		this.wrapBlockItem(TFBlocks.CRACKED_TOWERWOOD.get(), this::createTrivialCube);
		this.wrapBlockItem(TFBlocks.MOSSY_TOWERWOOD.get(), this::createTrivialCube);
		this.wrapBlockItem(TFBlocks.INFESTED_TOWERWOOD.get(), this::createTrivialCube);
		this.wrapBlockItem(TFBlocks.ENCASED_TOWERWOOD.get(), this::createTrivialCube);

		this.wrapBlockItem(TFBlocks.ENCASED_SMOKER.get(), block -> this.blockStateOutput.accept(MultiVariantGenerator.dispatch(block).with(
			createBooleanModelDispatch(EncasedSmokerBlock.ACTIVE,
				plainVariant(ModelTemplates.CUBE_BOTTOM_TOP.createWithSuffix(block, "_on", TFTextureMapping.threeLayerDeviceOn("smoker"), this.modelOutput)),
				plainVariant(ModelTemplates.CUBE_BOTTOM_TOP.create(block, TFTextureMapping.threeLayerDevice("smoker", ""), this.modelOutput))))));
		MultiVariant jetOn = plainVariant(ModelTemplates.CUBE_BOTTOM_TOP.createWithSuffix(TFBlocks.ENCASED_FIRE_JET.get(), "_on", TFTextureMapping.threeLayerDeviceOn("firejet"), this.modelOutput));
		MultiVariant jetOff = plainVariant(ModelTemplates.CUBE_BOTTOM_TOP.create(TFBlocks.ENCASED_FIRE_JET.get(), TFTextureMapping.threeLayerDevice("firejet", ""), this.modelOutput));
		this.wrapBlockItem(TFBlocks.ENCASED_FIRE_JET.get(), block -> this.blockStateOutput.accept(MultiVariantGenerator.dispatch(block).with(PropertyDispatch.initial(EncasedFireJetBlock.STATE).generate(variant -> variant.isVariantOn() ? jetOn : jetOff))));
		this.wrapBlockItem(TFBlocks.GHAST_TRAP.get(), block -> this.blockStateOutput.accept(MultiVariantGenerator.dispatch(block).with(createBooleanModelDispatch(GhastTrapBlock.ACTIVE,
			plainVariant(ModelTemplates.CUBE_BOTTOM_TOP.createWithSuffix(block, "_on", TFTextureMapping.threeLayerDeviceOn("ghasttrap"), this.modelOutput)),
				plainVariant(ModelTemplates.CUBE_BOTTOM_TOP.create(block, TFTextureMapping.threeLayerDevice("ghasttrap", ""), this.modelOutput))))));
		this.wrapBlockItem(TFBlocks.ANTIBUILDER.get(), block -> this.blockStateOutput.accept(createSimpleBlock(block, plainVariant(ModelTemplates.CUBE_ALL.create(block, TextureMapping.cube(new Material(TwilightForestMod.prefix("block/towerdev_antibuilder"))), this.modelOutput)))));
		this.blockStateOutput.accept(createSimpleBlock(TFBlocks.ANTIBUILT_BLOCK.get(), plainVariant(ModelTemplates.CUBE_ALL.create(TFBlocks.ANTIBUILT_BLOCK.get(), TextureMapping.cube(new Material(TwilightForestMod.prefix("block/towerdev_antibuilt"))), this.modelOutput))));
		this.wrapBlockItem(TFBlocks.CARMINITE_BUILDER.get(), block -> this.blockStateOutput.accept(MultiVariantGenerator.dispatch(block).with(PropertyDispatch.initial(BuilderBlock.STATE).generate(state -> {
			Identifier model = switch (state) {
				case BUILDER_INACTIVE -> ModelTemplates.CUBE_ALL.createWithSuffix(block, "", TextureMapping.cube(new Material(TwilightForestMod.prefix("block/towerdev_builder_off"))), this.modelOutput);
				case BUILDER_ACTIVE -> ModelTemplates.CUBE_ALL.createWithSuffix(block, "_on", TextureMapping.cube(new Material(TwilightForestMod.prefix("block/towerdev_builder_on"))), this.modelOutput);
				case BUILDER_TIMEOUT -> ModelTemplates.CUBE_ALL.createWithSuffix(block, "_timeout", TextureMapping.cube(new Material(TwilightForestMod.prefix("block/towerdev_builder_timeout"))), this.modelOutput);
			};
			return plainVariant(model);
		}))));
		this.blockStateOutput.accept(MultiVariantGenerator.dispatch(TFBlocks.BUILT_BLOCK.get())
			.with(PropertyDispatch.initial(TranslucentBuiltBlock.ACTIVE).generate(active -> plainVariant(active ?
				TFModelTemplates.FULLBRIGHT_BLOCK.createWithSuffix(TFBlocks.BUILT_BLOCK.get(), "_on", TextureMapping.cube(new Material(TwilightForestMod.prefix("block/towerdev_built_on"))), this.modelOutput) :
				TFModelTemplates.FULLBRIGHT_BLOCK.create(TFBlocks.BUILT_BLOCK.get(), TextureMapping.cube(new Material(TwilightForestMod.prefix("block/towerdev_built_off"))), this.modelOutput)))));
		this.wrapBlockItem(TFBlocks.CARMINITE_REACTOR.get(), block -> this.blockStateOutput.accept(MultiVariantGenerator.dispatch(block)
			.with(PropertyDispatch.initial(CarminiteReactorBlock.ACTIVE).generate(active -> plainVariant(active ?
				ModelTemplates.CUBE_ALL.createWithSuffix(block, "_on", TextureMapping.cube(new Material(TwilightForestMod.prefix("block/towerdev_reactor_on"))), this.modelOutput) :
				ModelTemplates.CUBE_ALL.create(block, TextureMapping.cube(new Material(TwilightForestMod.prefix("block/towerdev_reactor_off"))), this.modelOutput))))));
		this.wrapBlockItem(TFBlocks.LOCKED_VANISHING_BLOCK.get(), block -> this.blockStateOutput.accept(MultiVariantGenerator.dispatch(block)
			.with(PropertyDispatch.initial(VanishingBlock.ACTIVE).generate(active -> plainVariant(active ?
				ModelTemplates.CUBE_ALL.createWithSuffix(block, "_on", TextureMapping.cube(new Material(TwilightForestMod.prefix("block/towerdev_lock_off"))), this.modelOutput) :
				ModelTemplates.CUBE_ALL.create(block, TextureMapping.cube(new Material(TwilightForestMod.prefix("block/towerdev_lock_on"))), this.modelOutput))))));
		this.wrapBlockItem(TFBlocks.VANISHING_BLOCK.get(), block -> this.blockStateOutput.accept(MultiVariantGenerator.dispatch(block)
			.with(PropertyDispatch.initial(VanishingBlock.ACTIVE).generate(active -> plainVariant(active ?
				ModelTemplates.CUBE_ALL.createWithSuffix(block, "_on", TextureMapping.cube(new Material(TwilightForestMod.prefix("block/towerdev_vanish_on"))), this.modelOutput) :
				ModelTemplates.CUBE_ALL.create(block, TextureMapping.cube(new Material(TwilightForestMod.prefix("block/towerdev_vanish_off"))), this.modelOutput))))));
		this.blockStateOutput.accept(MultiVariantGenerator.dispatch(TFBlocks.UNBREAKABLE_VANISHING_BLOCK.get())
			.with(PropertyDispatch.initial(VanishingBlock.ACTIVE).generate(active -> plainVariant(active ?
				ModelLocationUtils.getModelLocation(TFBlocks.VANISHING_BLOCK.get(), "_on") :
				ModelLocationUtils.getModelLocation(TFBlocks.VANISHING_BLOCK.get())))));
		this.wrapBlockItem(TFBlocks.REAPPEARING_BLOCK.get(), block -> this.blockStateOutput.accept(MultiVariantGenerator.dispatch(block)
			.with(PropertyDispatch.initial(ReappearingBlock.ACTIVE, ReappearingBlock.VANISHED).generate((active, vanished) -> {
				String variant = (vanished ? "_invis" : "") + (active ? "_on" : "");
				return plainVariant(vanished ?
					(active ?
						ModelTemplates.CUBE_ALL.createWithSuffix(block, variant, TextureMapping.cube(new Material(TwilightForestMod.prefix("block/towerdev_reappearing_trace_on"))), this.modelOutput) :
						ModelTemplates.CUBE_ALL.createWithSuffix(block, variant, TextureMapping.cube(new Material(TwilightForestMod.prefix("block/towerdev_reappearing_trace_off"))), this.modelOutput)
				) : (active ?
						ModelTemplates.CUBE_ALL.createWithSuffix(block, variant, TextureMapping.cube(new Material(TwilightForestMod.prefix("block/towerdev_reappearing_on"))), this.modelOutput) :
						ModelTemplates.CUBE_ALL.createWithSuffix(block, variant, TextureMapping.cube(new Material(TwilightForestMod.prefix("block/towerdev_reappearing_off"))), this.modelOutput)));
			}))));
		this.blockStateOutput.accept(createSimpleBlock(TFBlocks.FAKE_GOLD.get(), plainVariant(ModelLocationUtils.getModelLocation(Blocks.GOLD_BLOCK))));
		this.blockStateOutput.accept(createSimpleBlock(TFBlocks.FAKE_DIAMOND.get(), plainVariant(ModelLocationUtils.getModelLocation(Blocks.DIAMOND_BLOCK))));
		this.blockStateOutput.accept(MultiVariantGenerator.dispatch(TFBlocks.EXPERIMENT_115.get()).with(PropertyDispatch.initial(Experiment115Block.BITES_TAKEN, Experiment115Block.REGENERATE).generate((bites, regen) -> {
			String suffix = String.format("_%d_8", 8 - bites);
			Identifier model;
			if (regen) {
				model = TFModelTemplates.create("twilightforest:experiment_115" + suffix, suffix + "_regenerating", TFTextureSlot.TOP_2).create(TFBlocks.EXPERIMENT_115.get(), new TextureMapping().put(TFTextureSlot.TOP_2, new Material(TwilightForestMod.prefix("block/experiment115/experiment115_sprinkle"))), this.modelOutput);
			} else {
				model = TwilightForestMod.prefix("block/experiment115" + suffix);
			}
			return plainVariant(model);
		})));

		this.generateAuroraBlocks();
		this.wrapBlockItem(TFBlocks.HUGE_STALK.get(), block -> this.createRotatedPillarWithHorizontalVariant(block, TexturedModel.COLUMN_ALT, TexturedModel.COLUMN_HORIZONTAL_ALT));
		this.createParticleOnlyBlock(TFBlocks.BEANSTALK_GROWER.get(), TFBlocks.HUGE_STALK.get());
		this.wrapBlockItem(TFBlocks.BEANSTALK_LEAVES.get(), block -> this.blockStateOutput.accept(createSimpleBlock(block, plainVariant(ModelTemplates.CUBE_ALL.create(block, TextureMapping.cube(new Material(Identifier.withDefaultNamespace("block/azalea_leaves"))), this.modelOutput)))));
		Identifier mushgloomInside = ModelTemplates.SINGLE_FACE.create(TwilightForestMod.prefix("huge_mushgloom_inside"), TextureMapping.cube(new Material(TwilightForestMod.prefix("block/huge_gloom_inside"))), this.modelOutput);
		this.createMultifaceBlock(TFBlocks.HUGE_MUSHGLOOM.get(), mushgloomInside, false, TwilightForestMod.prefix("block/huge_gloom_cap"));
		this.createMultifaceBlock(TFBlocks.HUGE_MUSHGLOOM_STEM.get(), mushgloomInside, false);
		Identifier trollsteinnInside = ModelTemplates.SINGLE_FACE.create(TwilightForestMod.prefix("trollsteinn_inside"), TextureMapping.cube(new Material(TwilightForestMod.prefix("block/trollsteinn_light"))), this.modelOutput);
		this.createMultifaceBlock(TFBlocks.TROLLSTEINN.get(), trollsteinnInside, true);
		this.createCrossBlockWithDefaultItem(TFBlocks.TROLLVIDR.get(), PlantType.NOT_TINTED);
		this.createCrossBlockWithDefaultItem(TFBlocks.UNRIPE_TROLLBER.get(), PlantType.NOT_TINTED);
		this.createCrossBlockWithDefaultItem(TFBlocks.TROLLBER.get(), PlantType.EMISSIVE_NOT_TINTED);
		this.wrapBlockItem(TFBlocks.FLUFFY_CLOUD.get(), this::createTrivialCube);
		this.wrapBlockItem(TFBlocks.WISPY_CLOUD.get(), this::createTrivialCube);
		this.wrapBlockItem(TFBlocks.RAINY_CLOUD.get(), this::createTrivialCube);
		this.wrapBlockItem(TFBlocks.SNOWY_CLOUD.get(), this::createTrivialCube);
		this.wrapBlockItem(TFBlocks.UBEROUS_SOIL.get(), block -> this.createTrivialBlock(block, TexturedModel.createDefault(block1 -> new TextureMapping().put(TextureSlot.DIRT, TextureMapping.getBlockTexture(block)).put(TextureSlot.TOP, TextureMapping.getBlockTexture(block)), ModelTemplates.FARMLAND)));

		this.giantBlock(TFBlocks.GIANT_COBBLESTONE.get(), TFTextureMapping.giantBlock(Blocks.COBBLESTONE));
		this.giantBlock(TFBlocks.GIANT_LOG.get(), TFTextureMapping.giantBlock(TextureMapping.getBlockTexture(Blocks.OAK_LOG), TextureMapping.getBlockTexture(Blocks.OAK_LOG, "_top")));
		this.giantBlock(TFBlocks.GIANT_LEAVES.get(), TFTextureMapping.giantBlock(Blocks.OAK_LEAVES), -12012264);
		this.giantBlock(TFBlocks.GIANT_OBSIDIAN.get(), TFTextureMapping.giantBlock(Blocks.OBSIDIAN));

		this.wrapBlockItem(TFBlocks.CASTLE_BRICK.get(), this::createTrivialCube);
		this.wrapBlockItem(TFBlocks.WORN_CASTLE_BRICK.get(), this::createTrivialCube);
		this.wrapBlockItem(TFBlocks.CRACKED_CASTLE_BRICK.get(), this::createTrivialCube);
		this.wrapBlockItem(TFBlocks.MOSSY_CASTLE_BRICK.get(), this::createTrivialCube);
		this.wrapBlockItem(TFBlocks.THICK_CASTLE_BRICK.get(), this::createTrivialCube);
		this.wrapBlockItem(TFBlocks.CASTLE_ROOF_TILE.get(), this::createTrivialCube);
		this.wrapBlockItem(TFBlocks.ENCASED_CASTLE_BRICK_PILLAR.get(), this::createEncasedCastleBrickPillar);
		this.wrapBlockItem(TFBlocks.ENCASED_CASTLE_BRICK_TILE.get(), block -> this.blockStateOutput.accept(createSimpleBlock(block, plainVariant(ModelTemplates.CUBE_ALL.create(block, TextureMapping.cube(TextureMapping.getBlockTexture(TFBlocks.ENCASED_CASTLE_BRICK_PILLAR.get(), "_end")), this.modelOutput)))));
		this.wrapBlockItem(TFBlocks.BOLD_CASTLE_BRICK_PILLAR.get(), this::createBoldCastleBrickPillar);
		this.wrapBlockItem(TFBlocks.BOLD_CASTLE_BRICK_TILE.get(), this::createTrivialCube);
		this.generateStairs(TFBlocks.CASTLE_BRICK_STAIRS.get(), TextureMapping.cube(TFBlocks.BOLD_CASTLE_BRICK_TILE.get()));
		this.generateStairs(TFBlocks.WORN_CASTLE_BRICK_STAIRS.get(), TextureMapping.cube(TFBlocks.BOLD_CASTLE_BRICK_TILE.get()));
		this.generateStairs(TFBlocks.CRACKED_CASTLE_BRICK_STAIRS.get(), TextureMapping.cube(TFBlocks.BOLD_CASTLE_BRICK_TILE.get()));
		this.generateStairs(TFBlocks.MOSSY_CASTLE_BRICK_STAIRS.get(), TextureMapping.cube(TFBlocks.BOLD_CASTLE_BRICK_TILE.get()));
		this.bisectedStairsBlock(TFBlocks.ENCASED_CASTLE_BRICK_STAIRS.get(), TextureMapping.getBlockTexture(TFBlocks.ENCASED_CASTLE_BRICK_PILLAR.get(), "_h"), TextureMapping.getBlockTexture(TFBlocks.CASTLE_BRICK.get()), TextureMapping.getBlockTexture(TFBlocks.CASTLE_ROOF_TILE.get()));
		this.generateStairs(TFBlocks.BOLD_CASTLE_BRICK_STAIRS.get(), TextureMapping.cube(TFBlocks.BOLD_CASTLE_BRICK_TILE.get()));
		this.generateRuneBlock(TFBlocks.PINK_CASTLE_RUNE_BRICK.get(), 16711935);
		this.generateRuneBlock(TFBlocks.YELLOW_CASTLE_RUNE_BRICK.get(), 16776960);
		this.generateRuneBlock(TFBlocks.BLUE_CASTLE_RUNE_BRICK.get(), 65535);
		this.generateRuneBlock(TFBlocks.VIOLET_CASTLE_RUNE_BRICK.get(), 4915330);

		this.castleDoor(TFBlocks.PINK_CASTLE_DOOR.get(), 16711935);
		this.castleDoor(TFBlocks.YELLOW_CASTLE_DOOR.get(), 16776960);
		this.castleDoor(TFBlocks.BLUE_CASTLE_DOOR.get(), 65535);
		this.castleDoor(TFBlocks.VIOLET_CASTLE_DOOR.get(), 4915330);
		this.forcefield(TFBlocks.PINK_FORCE_FIELD.get());
		this.forcefield(TFBlocks.ORANGE_FORCE_FIELD.get());
		this.forcefield(TFBlocks.GREEN_FORCE_FIELD.get());
		this.forcefield(TFBlocks.BLUE_FORCE_FIELD.get());
		this.forcefield(TFBlocks.VIOLET_FORCE_FIELD.get());

		this.generateSpecialModel(TFBlocks.KEEPSAKE_CASKET.get(), Blocks.NETHERITE_BLOCK, block -> ItemModelUtils.specialModel(TwilightForestMod.prefix("item/keepsake_casket"), new KeepsakeCasketSpecialRenderer.Unbaked()));
		this.generateSpecialModel(TFBlocks.SKULL_CHEST.get(), Blocks.LIGHT_GRAY_CONCRETE_POWDER, block -> ItemModelUtils.specialModel(TwilightForestMod.prefix("item/skull_chest"), new SkullChestSpecialRenderer.Unbaked()));
		this.generateSpecialModel(TFBlocks.CICADA.get(), Blocks.SLIME_BLOCK, block -> ItemModelUtils.specialModel(TwilightForestMod.prefix("item/cicada"), new CicadaSpecialRenderer.Unbaked()));
		this.generateSpecialModel(TFBlocks.FIREFLY.get(), Blocks.SLIME_BLOCK, block -> ItemModelUtils.specialModel(TwilightForestMod.prefix("item/firefly"), new FireflySpecialRenderer.Unbaked()));
		this.generateSpecialModel(TFBlocks.MOONWORM.get(), Blocks.SLIME_BLOCK, block -> ItemModelUtils.specialModel(TwilightForestMod.prefix("item/moonworm"), new MoonwormSpecialRenderer.Unbaked()));

		this.blockStateOutput.accept(MultiVariantGenerator.dispatch(TFBlocks.CANDELABRA.get())
			.with(PropertyDispatch.initial(CandelabraBlock.ON_WALL)
				.select(true, plainVariant(TwilightForestMod.prefix("item/candelabra")))
				.select(false, plainVariant(TwilightForestMod.prefix("item/candelabra"))))
			.with(PropertyDispatch.modify(CandelabraBlock.FACING)
				.select(Direction.NORTH, Y_ROT_180)
				.select(Direction.EAST, Y_ROT_270)
				.select(Direction.WEST, Y_ROT_90)
				.select(Direction.SOUTH, NOP)));
		this.itemModelOutput.accept(TFBlocks.CANDELABRA.asItem(), ItemModelUtils.composite(ItemModelUtils.plainModel(ModelLocationUtils.getModelLocation(TFBlocks.CANDELABRA.get())), ItemModelUtils.specialModel(TwilightForestMod.prefix("item/candelabra"), new CandelabraSpecialRenderer.Unbaked())));

		this.generateSkullCandle(TFBlocks.ZOMBIE_SKULL_CANDLE.get(), TFBlocks.ZOMBIE_WALL_SKULL_CANDLE.get());
		this.generateSkullCandle(TFBlocks.SKELETON_SKULL_CANDLE.get(), TFBlocks.SKELETON_WALL_SKULL_CANDLE.get());
		this.generateSkullCandle(TFBlocks.CREEPER_SKULL_CANDLE.get(), TFBlocks.CREEPER_WALL_SKULL_CANDLE.get());
		this.generateSkullCandle(TFBlocks.WITHER_SKELE_SKULL_CANDLE.get(), TFBlocks.WITHER_SKELE_WALL_SKULL_CANDLE.get());
		this.generateSkullCandle(TFBlocks.PLAYER_SKULL_CANDLE.get(), TFBlocks.PLAYER_WALL_SKULL_CANDLE.get());
		this.generateSkullCandle(TFBlocks.PIGLIN_SKULL_CANDLE.get(), TFBlocks.PIGLIN_WALL_SKULL_CANDLE.get());

		var major = ItemModelUtils.plainModel(ModelTemplates.FLAT_ITEM.create(TwilightForestMod.prefix("item/major_boss_trophy"), TextureMapping.layer0(new Material(TwilightForestMod.prefix("item/trophy"))), this.modelOutput));
		var minor = ItemModelUtils.plainModel(ModelTemplates.FLAT_ITEM.create(TwilightForestMod.prefix("item/minor_boss_trophy"), TextureMapping.layer0(new Material(TwilightForestMod.prefix("item/trophy_minor"))), this.modelOutput));
		var quest = ItemModelUtils.plainModel(ModelTemplates.FLAT_ITEM.create(TwilightForestMod.prefix("item/quest_trophy"), TextureMapping.layer0(new Material(TwilightForestMod.prefix("item/trophy_quest"))), this.modelOutput));

		this.generateTrophy(TFBlocks.NAGA_TROPHY.get(), TFBlocks.NAGA_WALL_TROPHY.get(), major);
		this.generateTrophy(TFBlocks.LICH_TROPHY.get(), TFBlocks.LICH_WALL_TROPHY.get(), major);
		this.generateTrophy(TFBlocks.MINOSHROOM_TROPHY.get(), TFBlocks.MINOSHROOM_WALL_TROPHY.get(), minor, "smaller_gui_trophy");
		this.generateTrophy(TFBlocks.HYDRA_TROPHY.get(), TFBlocks.HYDRA_WALL_TROPHY.get(), major, "hydra_trophy");
		this.generateTrophy(TFBlocks.KNIGHT_PHANTOM_TROPHY.get(), TFBlocks.KNIGHT_PHANTOM_WALL_TROPHY.get(), minor, "smaller_gui_trophy");
		this.generateTrophy(TFBlocks.UR_GHAST_TROPHY.get(), TFBlocks.UR_GHAST_WALL_TROPHY.get(), major, "ur_ghast_trophy");
		this.generateTrophy(TFBlocks.ALPHA_YETI_TROPHY.get(), TFBlocks.ALPHA_YETI_WALL_TROPHY.get(), minor, "alpha_yeti_trophy");
		this.generateTrophy(TFBlocks.SNOW_QUEEN_TROPHY.get(), TFBlocks.SNOW_QUEEN_WALL_TROPHY.get(), major);
		this.generateTrophy(TFBlocks.QUEST_RAM_TROPHY.get(), TFBlocks.QUEST_RAM_WALL_TROPHY.get(), quest, "smaller_gui_trophy");

		this.ironLadder();

		this.blockStateOutput.accept(MultiPartGenerator.multiPart(TFBlocks.ROPE.get())
			.with(condition().term(RopeBlock.X, true), plainVariant(ModelLocationUtils.getModelLocation(TFBlocks.ROPE.get(), "_x")))
			.with(condition().term(RopeBlock.Y, true), plainVariant(ModelLocationUtils.getModelLocation(TFBlocks.ROPE.get(), "_y")))
			.with(condition().term(RopeBlock.Z, true), plainVariant(ModelLocationUtils.getModelLocation(TFBlocks.ROPE.get(), "_z")))
			.with(new CombinedCondition(CombinedCondition.Operation.OR, List.of(
					and(condition().term(RopeBlock.X, true), condition().term(RopeBlock.Y, true)),
					and(condition().term(RopeBlock.Y, true), condition().term(RopeBlock.Z, true)),
					and(condition().term(RopeBlock.Z, true), condition().term(RopeBlock.X, true)))),
				plainVariant(ModelLocationUtils.getModelLocation(TFBlocks.ROPE.get(), "_knot"))));
		this.itemModelOutput.accept(TFBlocks.ROPE.asItem(), ItemModelUtils.plainModel(this.createFlatItemModelWithBlockTexture(TFBlocks.ROPE.asItem(), TFBlocks.ROPE.get())));

		this.wrapBlockItem(TFBlocks.UNCRAFTING_TABLE.get(), block -> this.blockStateOutput.accept(MultiVariantGenerator.dispatch(block)
			.with(createBooleanModelDispatch(UncraftingTableBlock.POWERED,
				plainVariant(ModelTemplates.CUBE_BOTTOM_TOP.createWithSuffix(block, "_activated", TFTextureMapping.uncraftingTableOn(), this.modelOutput)),
				plainVariant(ModelTemplates.CUBE_BOTTOM_TOP.create(block, TFTextureMapping.uncraftingTable(), this.modelOutput))))));
		this.wrapBlockItem(TFBlocks.ARCTIC_FUR_BLOCK.get(), block -> this.blockStateOutput.accept(createSimpleBlock(block, plainVariant(ModelTemplates.CUBE_ALL.create(block, TextureMapping.cube(block), this.modelOutput)))));

		this.wrapBlockItem(TFBlocks.STEELEAF_BLOCK.get(), this::createTrivialCube);
		this.wrapBlockItem(TFBlocks.IRONWOOD_BLOCK.get(), this::createTrivialCube);
		this.wrapBlockItem(TFBlocks.KNIGHTMETAL_BLOCK.get(), block -> this.blockStateOutput.accept(createSimpleBlock(block, plainVariant(ModelLocationUtils.getModelLocation(block)))));
		this.wrapBlockItem(TFBlocks.FIERY_BLOCK.get(), block -> this.blockStateOutput.accept(createSimpleBlock(block, plainVariant(ModelLocationUtils.getModelLocation(block)))));
		this.wrapBlockItem(TFBlocks.CARMINITE_BLOCK.get(), block -> this.blockStateOutput.accept(createSimpleBlock(block, plainVariant(ModelTemplates.CUBE_ALL.create(block, TextureMapping.cube(block), this.modelOutput)))));

		this.generateBush(TFBlocks.RASPBERRY_BUSH.get());
		this.generateBush(TFBlocks.BLACKBERRY_BUSH.get());
		this.generateBush(TFBlocks.BLUEBERRY_BUSH.get());
		this.generateBush(TFBlocks.MALOBERRY_BUSH.get());
		this.generateBush(TFBlocks.BLIGHTBERRY_BUSH.get());
		this.generateBush(TFBlocks.DUSKBERRY_BUSH.get());
		this.generateBush(TFBlocks.SKYBERRY_BUSH.get());
		this.generateBush(TFBlocks.STINGBERRY_BUSH.get());
		this.generateBush(TFBlocks.COPPER_OREBERRY_BUSH.get());
		this.generateBush(TFBlocks.IRON_OREBERRY_BUSH.get());
		this.generateBush(TFBlocks.GOLD_OREBERRY_BUSH.get());
		this.generateBush(TFBlocks.ESSENCE_OREBERRY_BUSH.get());

		this.createParticleOnlyBlock(TFBlocks.BRAZIER.get(), TFBlocks.CANOPY_PLANKS.get());
		this.wrapBlockItem(TFBlocks.SLIDER.get(), block -> this.blockStateOutput.accept(createRotatedPillarWithHorizontalVariant(block, plainVariant(ModelLocationUtils.getModelLocation(block)), plainVariant(ModelLocationUtils.getModelLocation(block, "_horiz")))));
		this.blockStateOutput.accept(createSimpleBlock(TFBlocks.CINDER_FURNACE.get(), plainVariant(ModelLocationUtils.getModelLocation(Blocks.FURNACE))));
		this.registerSimpleItemModel(TFBlocks.CINDER_FURNACE.get(), Identifier.withDefaultNamespace("block/furnace"));
		this.woodProvider(TFBlocks.CINDER_LOG.get()).logWithHorizontal(TFBlocks.CINDER_LOG.get()).wood(TFBlocks.CINDER_WOOD.get());
		this.blockStateOutput.accept(createSimpleBlock(TFBlocks.TWILIGHT_PORTAL_MINIATURE_STRUCTURE.get(), plainVariant(TwilightForestMod.prefix("block/miniature/portal"))));
		this.registerSimpleTintedItemModel(TFBlocks.TWILIGHT_PORTAL_MINIATURE_STRUCTURE.get(), TwilightForestMod.prefix("block/miniature/portal"), new GrassColorSource());
		this.blockStateOutput.accept(createSimpleBlock(TFBlocks.NAGA_COURTYARD_MINIATURE_STRUCTURE.get(), plainVariant(TwilightForestMod.prefix("block/miniature/naga_courtyard"))));
		this.registerSimpleTintedItemModel(TFBlocks.NAGA_COURTYARD_MINIATURE_STRUCTURE.get(), TwilightForestMod.prefix("block/miniature/naga_courtyard"), new GrassColorSource());
		this.blockStateOutput.accept(createSimpleBlock(TFBlocks.LICH_TOWER_MINIATURE_STRUCTURE.get(), plainVariant(TwilightForestMod.prefix("block/miniature/lich_tower"))));
		this.registerSimpleItemModel(TFBlocks.LICH_TOWER_MINIATURE_STRUCTURE.get(), TwilightForestMod.prefix("block/miniature/lich_tower"));
		this.blockStateOutput.accept(createSimpleBlock(TFBlocks.MINOTAUR_LABYRINTH_MINIATURE_STRUCTURE.get(), plainVariant(TwilightForestMod.prefix("block/miniature/labyrinth"))));
		this.registerSimpleItemModel(TFBlocks.MINOTAUR_LABYRINTH_MINIATURE_STRUCTURE.get(), TwilightForestMod.prefix("block/miniature/labyrinth"));
		this.blockStateOutput.accept(createSimpleBlock(TFBlocks.DARK_TOWER_MINIATURE_STRUCTURE.get(), plainVariant(TwilightForestMod.prefix("block/miniature/dark_tower"))));
		this.registerSimpleItemModel(TFBlocks.DARK_TOWER_MINIATURE_STRUCTURE.get(), TwilightForestMod.prefix("block/miniature/dark_tower"));
	}

	private void generateWoodBlocks() {
		this.wrapBlockItem(TFBlocks.ROOT_BLOCK.get(), this::createTrivialCube);
		this.wrapBlockItem(TFBlocks.LIVEROOT_BLOCK.get(), this::createTrivialCube);

		this.woodProvider(TFBlocks.TWILIGHT_OAK_LOG.get()).logWithHorizontal(TFBlocks.TWILIGHT_OAK_LOG.get()).wood(TFBlocks.TWILIGHT_OAK_WOOD.get());
		this.woodProvider(TFBlocks.STRIPPED_TWILIGHT_OAK_LOG.get()).logWithHorizontal(TFBlocks.STRIPPED_TWILIGHT_OAK_LOG.get()).wood(TFBlocks.STRIPPED_TWILIGHT_OAK_WOOD.get());
		this.generateHollowLog(TFBlocks.TWILIGHT_OAK_LOG.get(), TFBlocks.STRIPPED_TWILIGHT_OAK_LOG.get(), TFBlocks.HOLLOW_TWILIGHT_OAK_LOG_HORIZONTAL.get(), TFBlocks.HOLLOW_TWILIGHT_OAK_LOG_VERTICAL.get(), TFBlocks.HOLLOW_TWILIGHT_OAK_LOG_CLIMBABLE.get());
		this.generateSapling(TFBlocks.TWILIGHT_OAK_SAPLING.get(), TFBlocks.POTTED_TWILIGHT_OAK_SAPLING.get(), PlantType.NOT_TINTED);
		this.createTintedLeaves(TFBlocks.TWILIGHT_OAK_LEAVES.get(), TexturedModel.createDefault(block -> TextureMapping.cube(Blocks.OAK_LEAVES), ModelTemplates.LEAVES), -12012264);
		this.generatePlanks(TFBlocks.TWILIGHT_OAK_PLANKS.get(), "twilight_oak");

		TextureMapping twilightOak = TextureMapping.cube(new Material(TwilightForestMod.prefix("block/wood/planks_twilight_oak_0")));
		this.generateStairs(TFBlocks.TWILIGHT_OAK_STAIRS.get(), twilightOak);
		this.generateSlab(TFBlocks.TWILIGHT_OAK_SLAB.get(), TFBlocks.TWILIGHT_OAK_PLANKS.get(), twilightOak);
		this.generateButton(TFBlocks.TWILIGHT_OAK_BUTTON.get(), twilightOak);
		this.generateFence(TFBlocks.TWILIGHT_OAK_FENCE.get(), twilightOak);
		this.generateFenceGate(TFBlocks.TWILIGHT_OAK_GATE.get(), twilightOak);
		this.generatePressurePlate(TFBlocks.TWILIGHT_OAK_PLATE.get(), twilightOak);
		this.generateTrapdoor(TFBlocks.TWILIGHT_OAK_TRAPDOOR.get(), true, "twilight_oak");
		this.generateDoor(TFBlocks.TWILIGHT_OAK_DOOR.get(), false, "twilight_oak");
		this.generateSign(TFBlocks.TWILIGHT_OAK_SIGN.get(), TFBlocks.TWILIGHT_WALL_SIGN.get(), twilightOak);
		this.generateHangingSign(TFBlocks.TWILIGHT_OAK_HANGING_SIGN.get(), TFBlocks.TWILIGHT_OAK_WALL_HANGING_SIGN.get(), TFBlocks.STRIPPED_TWILIGHT_OAK_LOG.get());
		this.generateBanister(TFBlocks.TWILIGHT_OAK_BANISTER.get(), twilightOak);
		this.generateDryingRack(TFBlocks.TWILIGHT_OAK_DRYING_RACK.get(), twilightOak);

		this.woodProvider(TFBlocks.CANOPY_LOG.get()).logWithHorizontal(TFBlocks.CANOPY_LOG.get()).wood(TFBlocks.CANOPY_WOOD.get());
		this.woodProvider(TFBlocks.STRIPPED_CANOPY_LOG.get()).logWithHorizontal(TFBlocks.STRIPPED_CANOPY_LOG.get()).wood(TFBlocks.STRIPPED_CANOPY_WOOD.get());
		this.generateHollowLog(TFBlocks.CANOPY_LOG.get(), TFBlocks.STRIPPED_CANOPY_LOG.get(), TFBlocks.HOLLOW_CANOPY_LOG_HORIZONTAL.get(), TFBlocks.HOLLOW_CANOPY_LOG_VERTICAL.get(), TFBlocks.HOLLOW_CANOPY_LOG_CLIMBABLE.get());
		this.generateSapling(TFBlocks.CANOPY_SAPLING.get(), TFBlocks.POTTED_CANOPY_SAPLING.get(), PlantType.NOT_TINTED);
		this.createTintedLeaves(TFBlocks.CANOPY_LEAVES.get(), TexturedModel.createDefault(block -> TextureMapping.cube(Blocks.SPRUCE_LEAVES), ModelTemplates.LEAVES), -10380959);
		this.generatePlanks(TFBlocks.CANOPY_PLANKS.get(), "canopy");

		final Material canopyMaterial = new Material(TwilightForestMod.prefix("block/wood/planks_canopy_0"));
		TextureMapping canopy = TextureMapping.cube(canopyMaterial);
		this.generateStairs(TFBlocks.CANOPY_STAIRS.get(), canopy);
		this.generateSlab(TFBlocks.CANOPY_SLAB.get(), TFBlocks.CANOPY_PLANKS.get(), canopy);
		this.generateButton(TFBlocks.CANOPY_BUTTON.get(), canopy);
		this.generateFence(TFBlocks.CANOPY_FENCE.get(), canopy);
		this.generateFenceGate(TFBlocks.CANOPY_GATE.get(), canopy);
		this.generatePressurePlate(TFBlocks.CANOPY_PLATE.get(), canopy);
		this.generateTrapdoor(TFBlocks.CANOPY_TRAPDOOR.get(), true, "canopy");
		this.generateDoor(TFBlocks.CANOPY_DOOR.get(), false, "canopy");
		this.generateSign(TFBlocks.CANOPY_SIGN.get(), TFBlocks.CANOPY_WALL_SIGN.get(), canopy);
		this.generateHangingSign(TFBlocks.CANOPY_HANGING_SIGN.get(), TFBlocks.CANOPY_WALL_HANGING_SIGN.get(), TFBlocks.STRIPPED_CANOPY_LOG.get());
		this.generateBanister(TFBlocks.CANOPY_BANISTER.get(), canopy);
		this.generateDryingRack(TFBlocks.CANOPY_DRYING_RACK.get(), canopy);
		this.wrapBlockItem(TFBlocks.CANOPY_BOOKSHELF.get(), block -> this.blockStateOutput.accept(createSimpleBlock(block, variants(
			new Variant(ModelTemplates.CUBE_COLUMN.create(block, TextureMapping.column(new Material(TwilightForestMod.prefix("block/wood/bookshelf_canopy")), canopyMaterial), this.modelOutput)),
			new Variant(ModelTemplates.CUBE_COLUMN.createWithSuffix(block, "_1", TextureMapping.column(new Material(TwilightForestMod.prefix("block/wood/bookshelf_canopy_1")), canopyMaterial), this.modelOutput)),
			new Variant(ModelTemplates.CUBE_COLUMN.createWithSuffix(block, "_2", TextureMapping.column(new Material(TwilightForestMod.prefix("block/wood/bookshelf_canopy_2")), canopyMaterial), this.modelOutput)),
			new Variant(ModelTemplates.CUBE_COLUMN.createWithSuffix(block, "_3", TextureMapping.column(new Material(TwilightForestMod.prefix("block/wood/bookshelf_canopy_3")), canopyMaterial), this.modelOutput))
		))));
		this.generateChiseledBookshelf(TFBlocks.CHISELED_CANOPY_BOOKSHELF.get());
		this.wrapBlockItem(TFBlocks.CANOPY_WINDOW.get(), this::createTrivialCube);
		this.generatePaneBlock(TFBlocks.CANOPY_WINDOW.get(), TFBlocks.CANOPY_WINDOW_PANE.get());

		this.woodProvider(TFBlocks.MANGROVE_LOG.get()).logWithHorizontal(TFBlocks.MANGROVE_LOG.get()).wood(TFBlocks.MANGROVE_WOOD.get());
		this.woodProvider(TFBlocks.STRIPPED_MANGROVE_LOG.get()).logWithHorizontal(TFBlocks.STRIPPED_MANGROVE_LOG.get()).wood(TFBlocks.STRIPPED_MANGROVE_WOOD.get());
		this.generateHollowLog(TFBlocks.MANGROVE_LOG.get(), TFBlocks.STRIPPED_MANGROVE_LOG.get(), TFBlocks.HOLLOW_MANGROVE_LOG_HORIZONTAL.get(), TFBlocks.HOLLOW_MANGROVE_LOG_VERTICAL.get(), TFBlocks.HOLLOW_MANGROVE_LOG_CLIMBABLE.get());
		this.generateSapling(TFBlocks.MANGROVE_SAPLING.get(), TFBlocks.POTTED_MANGROVE_SAPLING.get(), PlantType.NOT_TINTED);
		this.createTintedLeaves(TFBlocks.MANGROVE_LEAVES.get(), TexturedModel.createDefault(block -> TextureMapping.cube(Blocks.BIRCH_LEAVES), ModelTemplates.LEAVES), -8345771);
		this.wrapBlockItem(TFBlocks.MANGROVE_ROOT.get(), this::createTrivialCube);
		this.generatePlanks(TFBlocks.MANGROVE_PLANKS.get(), "mangrove");

		TextureMapping mangrove =  TextureMapping.cube(new Material(TwilightForestMod.prefix("block/wood/planks_mangrove_0")));
		this.generateStairs(TFBlocks.MANGROVE_STAIRS.get(), mangrove);
		this.generateSlab(TFBlocks.MANGROVE_SLAB.get(), TFBlocks.MANGROVE_PLANKS.get(), mangrove);
		this.generateButton(TFBlocks.MANGROVE_BUTTON.get(), mangrove);
		this.generateFence(TFBlocks.MANGROVE_FENCE.get(), mangrove);
		this.generateFenceGate(TFBlocks.MANGROVE_GATE.get(), mangrove);
		this.generatePressurePlate(TFBlocks.MANGROVE_PLATE.get(), mangrove);
		this.generateTrapdoor(TFBlocks.MANGROVE_TRAPDOOR.get(), true, "mangrove");
		this.generateDoor(TFBlocks.MANGROVE_DOOR.get(), false, "mangrove");
		this.generateSign(TFBlocks.MANGROVE_SIGN.get(), TFBlocks.MANGROVE_WALL_SIGN.get(), mangrove);
		this.generateHangingSign(TFBlocks.MANGROVE_HANGING_SIGN.get(), TFBlocks.MANGROVE_WALL_HANGING_SIGN.get(), TFBlocks.STRIPPED_MANGROVE_LOG.get());
		this.generateBanister(TFBlocks.MANGROVE_BANISTER.get(), mangrove);
		this.generateDryingRack(TFBlocks.MANGROVE_DRYING_RACK.get(), mangrove);

		this.woodProvider(TFBlocks.DARK_LOG.get()).logWithHorizontal(TFBlocks.DARK_LOG.get()).wood(TFBlocks.DARK_WOOD.get());
		this.woodProvider(TFBlocks.STRIPPED_DARK_LOG.get()).logWithHorizontal(TFBlocks.STRIPPED_DARK_LOG.get()).wood(TFBlocks.STRIPPED_DARK_WOOD.get());
		this.generateHollowLog(TFBlocks.DARK_LOG.get(), TFBlocks.STRIPPED_DARK_LOG.get(), TFBlocks.HOLLOW_DARK_LOG_HORIZONTAL.get(), TFBlocks.HOLLOW_DARK_LOG_VERTICAL.get(), TFBlocks.HOLLOW_DARK_LOG_CLIMBABLE.get());
		this.generateSapling(TFBlocks.DARKWOOD_SAPLING.get(), TFBlocks.POTTED_DARKWOOD_SAPLING.get(), PlantType.NOT_TINTED);
		this.createTintedLeaves(TFBlocks.DARK_LEAVES.get(), TexturedModel.createDefault(block -> TextureMapping.cube(new Material(TwilightForestMod.prefix("block/darkwood_leaves"))), ModelTemplates.LEAVES), 0x1EBA50);
		this.blockStateOutput.accept(createSimpleBlock(TFBlocks.HARDENED_DARK_LEAVES.get(), plainVariant(ModelTemplates.LEAVES.create(TFBlocks.HARDENED_DARK_LEAVES.get(), TextureMapping.cube(new Material(TwilightForestMod.prefix("block/darkwood_leaves"))), this.modelOutput))));
		this.generatePlanks(TFBlocks.DARK_PLANKS.get(), "darkwood");

		TextureMapping dark =  TextureMapping.cube(new Material(TwilightForestMod.prefix("block/wood/planks_darkwood_0")));
		this.generateStairs(TFBlocks.DARK_STAIRS.get(), dark);
		this.generateSlab(TFBlocks.DARK_SLAB.get(), TFBlocks.DARK_PLANKS.get(), dark);
		this.generateButton(TFBlocks.DARK_BUTTON.get(), dark);
		this.generateFence(TFBlocks.DARK_FENCE.get(), dark);
		this.generateFenceGate(TFBlocks.DARK_GATE.get(), dark);
		this.generatePressurePlate(TFBlocks.DARK_PLATE.get(), dark);
		this.generateTrapdoor(TFBlocks.DARK_TRAPDOOR.get(), true, "darkwood");
		this.generateDoor(TFBlocks.DARK_DOOR.get(), false, "darkwood");
		this.generateSign(TFBlocks.DARK_SIGN.get(), TFBlocks.DARK_WALL_SIGN.get(), dark);
		this.generateHangingSign(TFBlocks.DARK_HANGING_SIGN.get(), TFBlocks.DARK_WALL_HANGING_SIGN.get(), TFBlocks.STRIPPED_DARK_LOG.get());
		this.generateBanister(TFBlocks.DARK_BANISTER.get(), dark);
		this.generateDryingRack(TFBlocks.DARK_DRYING_RACK.get(), dark);

		this.woodProvider(TFBlocks.TIME_LOG.get()).logWithHorizontal(TFBlocks.TIME_LOG.get()).wood(TFBlocks.TIME_WOOD.get());
		this.woodProvider(TFBlocks.STRIPPED_TIME_LOG.get()).logWithHorizontal(TFBlocks.STRIPPED_TIME_LOG.get()).wood(TFBlocks.STRIPPED_TIME_WOOD.get());
		this.generateTreeCore(TFBlocks.TIME_LOG.get(), TFBlocks.TIME_LOG_CORE.get());
		this.generateHollowLog(TFBlocks.TIME_LOG.get(), TFBlocks.STRIPPED_TIME_LOG.get(), TFBlocks.HOLLOW_TIME_LOG_HORIZONTAL.get(), TFBlocks.HOLLOW_TIME_LOG_VERTICAL.get(), TFBlocks.HOLLOW_TIME_LOG_CLIMBABLE.get());
		this.generateSapling(TFBlocks.TIME_SAPLING.get(), TFBlocks.POTTED_TIME_SAPLING.get(), PlantType.NOT_TINTED);
		this.generateMagicLeaves(TFBlocks.TIME_LEAVES.get(), 180, 6986775);
		this.generatePlanks(TFBlocks.TIME_PLANKS.get(), "time");

		TextureMapping time =  TextureMapping.cube(new Material(TwilightForestMod.prefix("block/wood/planks_time_0")));
		this.generateStairs(TFBlocks.TIME_STAIRS.get(), time);
		this.generateSlab(TFBlocks.TIME_SLAB.get(), TFBlocks.TIME_PLANKS.get(), time);
		this.generateButton(TFBlocks.TIME_BUTTON.get(), time);
		this.generateFence(TFBlocks.TIME_FENCE.get(), time);
		this.generateFenceGate(TFBlocks.TIME_GATE.get(), time);
		this.generatePressurePlate(TFBlocks.TIME_PLATE.get(), time);
		this.generateTrapdoor(TFBlocks.TIME_TRAPDOOR.get(), true, "time");
		this.generateDoor(TFBlocks.TIME_DOOR.get(), false, "time");
		this.generateSign(TFBlocks.TIME_SIGN.get(), TFBlocks.TIME_WALL_SIGN.get(), time);
		this.generateHangingSign(TFBlocks.TIME_HANGING_SIGN.get(), TFBlocks.TIME_WALL_HANGING_SIGN.get(), TFBlocks.STRIPPED_TIME_LOG.get());
		this.generateBanister(TFBlocks.TIME_BANISTER.get(), time);
		this.generateDryingRack(TFBlocks.TIME_DRYING_RACK.get(), time);

		this.woodProvider(TFBlocks.TRANSFORMATION_LOG.get()).logWithHorizontal(TFBlocks.TRANSFORMATION_LOG.get()).wood(TFBlocks.TRANSFORMATION_WOOD.get());
		this.woodProvider(TFBlocks.STRIPPED_TRANSFORMATION_LOG.get()).logWithHorizontal(TFBlocks.STRIPPED_TRANSFORMATION_LOG.get()).wood(TFBlocks.STRIPPED_TRANSFORMATION_WOOD.get());
		this.generateTreeCore(TFBlocks.TRANSFORMATION_LOG.get(), TFBlocks.TRANSFORMATION_LOG_CORE.get());
		this.generateHollowLog(TFBlocks.TRANSFORMATION_LOG.get(), TFBlocks.STRIPPED_TRANSFORMATION_LOG.get(), TFBlocks.HOLLOW_TRANSFORMATION_LOG_HORIZONTAL.get(), TFBlocks.HOLLOW_TRANSFORMATION_LOG_VERTICAL.get(), TFBlocks.HOLLOW_TRANSFORMATION_LOG_CLIMBABLE.get());
		this.generateSapling(TFBlocks.TRANSFORMATION_SAPLING.get(), TFBlocks.POTTED_TRANSFORMATION_SAPLING.get(), PlantType.NOT_TINTED);
		this.generateMagicLeaves(TFBlocks.TRANSFORMATION_LEAVES.get(), 270, 7130346);
		this.generatePlanks(TFBlocks.TRANSFORMATION_PLANKS.get(), "trans");

		TextureMapping transformation =  TextureMapping.cube(new Material(TwilightForestMod.prefix("block/wood/planks_trans_0")));
		this.generateStairs(TFBlocks.TRANSFORMATION_STAIRS.get(), transformation);
		this.generateSlab(TFBlocks.TRANSFORMATION_SLAB.get(), TFBlocks.TRANSFORMATION_PLANKS.get(), transformation);
		this.generateButton(TFBlocks.TRANSFORMATION_BUTTON.get(), transformation);
		this.generateFence(TFBlocks.TRANSFORMATION_FENCE.get(), transformation);
		this.generateFenceGate(TFBlocks.TRANSFORMATION_GATE.get(), transformation);
		this.generatePressurePlate(TFBlocks.TRANSFORMATION_PLATE.get(), transformation);
		this.generateTrapdoor(TFBlocks.TRANSFORMATION_TRAPDOOR.get(), true, "trans");
		this.generateDoor(TFBlocks.TRANSFORMATION_DOOR.get(), false, "trans");
		this.generateSign(TFBlocks.TRANSFORMATION_SIGN.get(), TFBlocks.TRANSFORMATION_WALL_SIGN.get(), transformation);
		this.generateHangingSign(TFBlocks.TRANSFORMATION_HANGING_SIGN.get(), TFBlocks.TRANSFORMATION_WALL_HANGING_SIGN.get(), TFBlocks.STRIPPED_TRANSFORMATION_LOG.get());
		this.generateBanister(TFBlocks.TRANSFORMATION_BANISTER.get(), transformation);
		this.generateDryingRack(TFBlocks.TRANSFORMATION_DRYING_RACK.get(), transformation);

		this.woodProvider(TFBlocks.MINING_LOG.get()).logWithHorizontal(TFBlocks.MINING_LOG.get()).wood(TFBlocks.MINING_WOOD.get());
		this.woodProvider(TFBlocks.STRIPPED_MINING_LOG.get()).logWithHorizontal(TFBlocks.STRIPPED_MINING_LOG.get()).wood(TFBlocks.STRIPPED_MINING_WOOD.get());
		this.generateTreeCore(TFBlocks.MINING_LOG.get(), TFBlocks.MINING_LOG_CORE.get());
		this.generateHollowLog(TFBlocks.MINING_LOG.get(), TFBlocks.STRIPPED_MINING_LOG.get(), TFBlocks.HOLLOW_MINING_LOG_HORIZONTAL.get(), TFBlocks.HOLLOW_MINING_LOG_VERTICAL.get(), TFBlocks.HOLLOW_MINING_LOG_CLIMBABLE.get());
		this.generateSapling(TFBlocks.MINING_SAPLING.get(), TFBlocks.POTTED_MINING_SAPLING.get(), PlantType.NOT_TINTED);
		this.generateMagicLeaves(TFBlocks.MINING_LEAVES.get(), 90, 16576836);
		this.generatePlanks(TFBlocks.MINING_PLANKS.get(), "mine");

		TextureMapping mining =  TextureMapping.cube(new Material(TwilightForestMod.prefix("block/wood/planks_mine_0")));
		this.generateStairs(TFBlocks.MINING_STAIRS.get(), mining);
		this.generateSlab(TFBlocks.MINING_SLAB.get(), TFBlocks.MINING_PLANKS.get(), mining);
		this.generateButton(TFBlocks.MINING_BUTTON.get(), mining);
		this.generateFence(TFBlocks.MINING_FENCE.get(), mining);
		this.generateFenceGate(TFBlocks.MINING_GATE.get(), mining);
		this.generatePressurePlate(TFBlocks.MINING_PLATE.get(), mining);
		this.generateTrapdoor(TFBlocks.MINING_TRAPDOOR.get(), true, "mine");
		this.generateDoor(TFBlocks.MINING_DOOR.get(), false, "mine");
		this.generateSign(TFBlocks.MINING_SIGN.get(), TFBlocks.MINING_WALL_SIGN.get(), mining);
		this.generateHangingSign(TFBlocks.MINING_HANGING_SIGN.get(), TFBlocks.MINING_WALL_HANGING_SIGN.get(), TFBlocks.STRIPPED_MINING_LOG.get());
		this.generateBanister(TFBlocks.MINING_BANISTER.get(), mining);
		this.generateDryingRack(TFBlocks.MINING_DRYING_RACK.get(), mining);

		this.woodProvider(TFBlocks.SORTING_LOG.get()).logWithHorizontal(TFBlocks.SORTING_LOG.get()).wood(TFBlocks.SORTING_WOOD.get());
		this.woodProvider(TFBlocks.STRIPPED_SORTING_LOG.get()).logWithHorizontal(TFBlocks.STRIPPED_SORTING_LOG.get()).wood(TFBlocks.STRIPPED_SORTING_WOOD.get());
		this.generateTreeCore(TFBlocks.SORTING_LOG.get(), TFBlocks.SORTING_LOG_CORE.get());
		this.generateHollowLog(TFBlocks.SORTING_LOG.get(), TFBlocks.STRIPPED_SORTING_LOG.get(), TFBlocks.HOLLOW_SORTING_LOG_HORIZONTAL.get(), TFBlocks.HOLLOW_SORTING_LOG_VERTICAL.get(), TFBlocks.HOLLOW_SORTING_LOG_CLIMBABLE.get());
		this.generateSapling(TFBlocks.SORTING_SAPLING.get(), TFBlocks.POTTED_SORTING_SAPLING.get(), PlantType.NOT_TINTED);
		this.wrapTintedBlockItem(TFBlocks.SORTING_LEAVES.get(), ItemModelUtils.constantTint(3558403), block -> this.generateSortingLeaves());
		this.generatePlanks(TFBlocks.SORTING_PLANKS.get(), "sort");

		TextureMapping sorting =  TextureMapping.cube(new Material(TwilightForestMod.prefix("block/wood/planks_sort_0")));
		this.generateStairs(TFBlocks.SORTING_STAIRS.get(), sorting);
		this.generateSlab(TFBlocks.SORTING_SLAB.get(), TFBlocks.SORTING_PLANKS.get(), sorting);
		this.generateButton(TFBlocks.SORTING_BUTTON.get(), sorting);
		this.generateFence(TFBlocks.SORTING_FENCE.get(), sorting);
		this.generateFenceGate(TFBlocks.SORTING_GATE.get(), sorting);
		this.generatePressurePlate(TFBlocks.SORTING_PLATE.get(), sorting);
		this.generateTrapdoor(TFBlocks.SORTING_TRAPDOOR.get(), true, "sort");
		this.generateDoor(TFBlocks.SORTING_DOOR.get(), true, "sort");
		this.generateSign(TFBlocks.SORTING_SIGN.get(), TFBlocks.SORTING_WALL_SIGN.get(), sorting);
		this.generateHangingSign(TFBlocks.SORTING_HANGING_SIGN.get(), TFBlocks.SORTING_WALL_HANGING_SIGN.get(), TFBlocks.STRIPPED_SORTING_LOG.get());
		this.generateBanister(TFBlocks.SORTING_BANISTER.get(), sorting);
		this.generateDryingRack(TFBlocks.SORTING_DRYING_RACK.get(), sorting);

		this.generateSapling(TFBlocks.HOLLOW_OAK_SAPLING.get(), TFBlocks.POTTED_HOLLOW_OAK_SAPLING.get(), PlantType.NOT_TINTED);
		this.createTintedLeaves(TFBlocks.RAINBOW_OAK_LEAVES.get(), TexturedModel.createDefault(block -> TextureMapping.cube(Blocks.OAK_LEAVES), ModelTemplates.LEAVES), -12012264);
		this.generateSapling(TFBlocks.RAINBOW_OAK_SAPLING.get(), TFBlocks.POTTED_RAINBOW_OAK_SAPLING.get(), PlantType.NOT_TINTED);

		this.createTFChest(TFBlocks.TWILIGHT_OAK_CHEST.get(), TwilightForestMod.prefix("twilight_oak/normal"), "twilight_oak");
		this.createTFChest(TFBlocks.CANOPY_CHEST.get(), TwilightForestMod.prefix("canopy/normal"), "canopy");
		this.createTFChest(TFBlocks.MANGROVE_CHEST.get(), TwilightForestMod.prefix("mangrove/normal"), "magrove");
		this.createTFChest(TFBlocks.DARK_CHEST.get(), TwilightForestMod.prefix("darkwood/normal"), "darkwood");
		this.createTFChest(TFBlocks.TIME_CHEST.get(), TwilightForestMod.prefix("time/normal"), "time");
		this.createTFChest(TFBlocks.TRANSFORMATION_CHEST.get(), TwilightForestMod.prefix("transformation/normal"), "trans");
		this.createTFChest(TFBlocks.MINING_CHEST.get(), TwilightForestMod.prefix("mining/normal"), "mine");
		this.createTFChest(TFBlocks.SORTING_CHEST.get(), TwilightForestMod.prefix("sorting/normal"), "sort");

		this.createTFChest(TFBlocks.TWILIGHT_OAK_TRAPPED_CHEST.get(), TwilightForestMod.prefix("twilight_oak/trapped"), "twilight_oak");
		this.createTFChest(TFBlocks.CANOPY_TRAPPED_CHEST.get(), TwilightForestMod.prefix("canopy/trapped"), "canopy");
		this.createTFChest(TFBlocks.MANGROVE_TRAPPED_CHEST.get(), TwilightForestMod.prefix("mangrove/trapped"), "mangrove");
		this.createTFChest(TFBlocks.DARK_TRAPPED_CHEST.get(), TwilightForestMod.prefix("darkwood/trapped"), "darkwood");
		this.createTFChest(TFBlocks.TIME_TRAPPED_CHEST.get(), TwilightForestMod.prefix("time/trapped"), "time");
		this.createTFChest(TFBlocks.TRANSFORMATION_TRAPPED_CHEST.get(), TwilightForestMod.prefix("transformation/trapped"), "trans");
		this.createTFChest(TFBlocks.MINING_TRAPPED_CHEST.get(), TwilightForestMod.prefix("mining/trapped"), "mine");
		this.createTFChest(TFBlocks.SORTING_TRAPPED_CHEST.get(), TwilightForestMod.prefix("sorting/trapped"), "sort");

		this.generateHollowLog(Blocks.OAK_LOG, Blocks.STRIPPED_OAK_LOG, TFBlocks.HOLLOW_OAK_LOG_HORIZONTAL.get(), TFBlocks.HOLLOW_OAK_LOG_VERTICAL.get(), TFBlocks.HOLLOW_OAK_LOG_CLIMBABLE.get());
		this.generateBanister(TFBlocks.OAK_BANISTER.get(), TextureMapping.cube(Blocks.OAK_PLANKS));
		this.generateDryingRack(TFBlocks.OAK_DRYING_RACK.get(), TextureMapping.cube(Blocks.OAK_PLANKS));
		this.generateHollowLog(Blocks.SPRUCE_LOG, Blocks.STRIPPED_SPRUCE_LOG, TFBlocks.HOLLOW_SPRUCE_LOG_HORIZONTAL.get(), TFBlocks.HOLLOW_SPRUCE_LOG_VERTICAL.get(), TFBlocks.HOLLOW_SPRUCE_LOG_CLIMBABLE.get());
		this.generateBanister(TFBlocks.SPRUCE_BANISTER.get(), TextureMapping.cube(Blocks.SPRUCE_PLANKS));
		this.generateDryingRack(TFBlocks.SPRUCE_DRYING_RACK.get(), TextureMapping.cube(Blocks.SPRUCE_PLANKS));
		this.generateHollowLog(Blocks.BIRCH_LOG, Blocks.STRIPPED_BIRCH_LOG, TFBlocks.HOLLOW_BIRCH_LOG_HORIZONTAL.get(), TFBlocks.HOLLOW_BIRCH_LOG_VERTICAL.get(), TFBlocks.HOLLOW_BIRCH_LOG_CLIMBABLE.get());
		this.generateBanister(TFBlocks.BIRCH_BANISTER.get(), TextureMapping.cube(Blocks.BIRCH_PLANKS));
		this.generateDryingRack(TFBlocks.BIRCH_DRYING_RACK.get(), TextureMapping.cube(Blocks.BIRCH_PLANKS));
		this.generateHollowLog(Blocks.JUNGLE_LOG, Blocks.STRIPPED_JUNGLE_LOG, TFBlocks.HOLLOW_JUNGLE_LOG_HORIZONTAL.get(), TFBlocks.HOLLOW_JUNGLE_LOG_VERTICAL.get(), TFBlocks.HOLLOW_JUNGLE_LOG_CLIMBABLE.get());
		this.generateBanister(TFBlocks.JUNGLE_BANISTER.get(), TextureMapping.cube(Blocks.JUNGLE_PLANKS));
		this.generateDryingRack(TFBlocks.JUNGLE_DRYING_RACK.get(), TextureMapping.cube(Blocks.JUNGLE_PLANKS));
		this.generateHollowLog(Blocks.ACACIA_LOG, Blocks.STRIPPED_ACACIA_LOG, TFBlocks.HOLLOW_ACACIA_LOG_HORIZONTAL.get(), TFBlocks.HOLLOW_ACACIA_LOG_VERTICAL.get(), TFBlocks.HOLLOW_ACACIA_LOG_CLIMBABLE.get());
		this.generateBanister(TFBlocks.ACACIA_BANISTER.get(), TextureMapping.cube(Blocks.ACACIA_PLANKS));
		this.generateDryingRack(TFBlocks.ACACIA_DRYING_RACK.get(), TextureMapping.cube(Blocks.ACACIA_PLANKS));
		this.generateHollowLog(Blocks.DARK_OAK_LOG, Blocks.STRIPPED_DARK_OAK_LOG, TFBlocks.HOLLOW_DARK_OAK_LOG_HORIZONTAL.get(), TFBlocks.HOLLOW_DARK_OAK_LOG_VERTICAL.get(), TFBlocks.HOLLOW_DARK_OAK_LOG_CLIMBABLE.get());
		this.generateBanister(TFBlocks.DARK_OAK_BANISTER.get(), TextureMapping.cube(Blocks.DARK_OAK_PLANKS));
		this.generateDryingRack(TFBlocks.DARK_OAK_DRYING_RACK.get(), TextureMapping.cube(Blocks.DARK_OAK_PLANKS));
		this.generateHollowLog(Blocks.CRIMSON_STEM, Blocks.STRIPPED_CRIMSON_STEM, TFBlocks.HOLLOW_CRIMSON_STEM_HORIZONTAL.get(), TFBlocks.HOLLOW_CRIMSON_STEM_VERTICAL.get(), TFBlocks.HOLLOW_CRIMSON_STEM_CLIMBABLE.get());
		this.generateBanister(TFBlocks.CRIMSON_BANISTER.get(), TextureMapping.cube(Blocks.CRIMSON_PLANKS));
		this.generateDryingRack(TFBlocks.CRIMSON_DRYING_RACK.get(), TextureMapping.cube(Blocks.CRIMSON_PLANKS));
		this.generateHollowLog(Blocks.WARPED_STEM, Blocks.STRIPPED_WARPED_STEM, TFBlocks.HOLLOW_WARPED_STEM_HORIZONTAL.get(), TFBlocks.HOLLOW_WARPED_STEM_VERTICAL.get(), TFBlocks.HOLLOW_WARPED_STEM_CLIMBABLE.get());
		this.generateBanister(TFBlocks.WARPED_BANISTER.get(), TextureMapping.cube(Blocks.WARPED_PLANKS));
		this.generateDryingRack(TFBlocks.WARPED_DRYING_RACK.get(), TextureMapping.cube(Blocks.WARPED_PLANKS));
		this.generateHollowLog(Blocks.MANGROVE_LOG, Blocks.STRIPPED_MANGROVE_LOG, TFBlocks.HOLLOW_VANGROVE_LOG_HORIZONTAL.get(), TFBlocks.HOLLOW_VANGROVE_LOG_VERTICAL.get(), TFBlocks.HOLLOW_VANGROVE_LOG_CLIMBABLE.get());
		this.generateBanister(TFBlocks.VANGROVE_BANISTER.get(), TextureMapping.cube(Blocks.MANGROVE_PLANKS));
		this.generateDryingRack(TFBlocks.VANGROVE_DRYING_RACK.get(), TextureMapping.cube(Blocks.MANGROVE_PLANKS));
		this.generateHollowLog(Blocks.CHERRY_LOG, Blocks.STRIPPED_CHERRY_LOG, TFBlocks.HOLLOW_CHERRY_LOG_HORIZONTAL.get(), TFBlocks.HOLLOW_CHERRY_LOG_VERTICAL.get(), TFBlocks.HOLLOW_CHERRY_LOG_CLIMBABLE.get());
		this.generateBanister(TFBlocks.CHERRY_BANISTER.get(), TextureMapping.cube(Blocks.CHERRY_PLANKS));
		this.generateDryingRack(TFBlocks.CHERRY_DRYING_RACK.get(), TextureMapping.cube(Blocks.CHERRY_PLANKS));
		this.generateBanister(TFBlocks.BAMBOO_BANISTER.get(), TextureMapping.cube(Blocks.BAMBOO_PLANKS));
		this.generateDryingRack(TFBlocks.BAMBOO_DRYING_RACK.get(), TextureMapping.cube(Blocks.BAMBOO_PLANKS));
		this.generateHollowLog(Blocks.PALE_OAK_LOG, Blocks.STRIPPED_PALE_OAK_LOG, TFBlocks.HOLLOW_PALE_OAK_LOG_HORIZONTAL.get(), TFBlocks.HOLLOW_PALE_OAK_LOG_VERTICAL.get(), TFBlocks.HOLLOW_PALE_OAK_LOG_CLIMBABLE.get());
		this.generateBanister(TFBlocks.PALE_OAK_BANISTER.get(), TextureMapping.cube(Blocks.PALE_OAK_PLANKS));
		this.generateDryingRack(TFBlocks.PALE_OAK_DRYING_RACK.get(), TextureMapping.cube(Blocks.PALE_OAK_PLANKS));
	}
}
