package twilightforest.data.helpers;

import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelOutput;
import net.minecraft.client.data.models.blockstates.*;
import net.minecraft.client.data.models.model.*;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.PipeBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import twilightforest.TwilightForestMod;
import twilightforest.block.CastleDoorBlock;
import twilightforest.client.model.block.connected.ConnectedTextureBuilder;
import twilightforest.client.model.block.forcefield.ForceFieldModel;
import twilightforest.client.model.block.forcefield.ForceFieldModelBuilder;
import twilightforest.data.models.TFBlockModelTemplates;
import twilightforest.data.models.TFTextureMapping;
import twilightforest.data.models.TFTextureSlot;
import twilightforest.init.TFBlocks;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public abstract class BlockModelBuilders extends BlockModelGenerators {

	public BlockModelBuilders(Consumer<BlockStateGenerator> stateOutput, ItemModelOutput itemOutput, BiConsumer<ResourceLocation, ModelInstance> modelOutput) {
		super(stateOutput, itemOutput, modelOutput);
	}

	@Override
	public abstract void run();

	public void wrapBlockItem(Block block, Consumer<Block> blockRegistry) {
		blockRegistry.accept(block);
		this.createBlockItem(block);
	}

	public void createBlockItem(Block block) {
		this.registerSimpleItemModel(block, BuiltInRegistries.BLOCK.getKey(block).withPrefix("block/"));
	}

	public void bossSpawner(Block block) {
		TextureMapping texturemapping = TextureMapping.cube(TwilightForestMod.prefix("block/boss_spawner"));
		this.blockStateOutput.accept(createSimpleBlock(block, ModelTemplates.CUBE_ALL_INNER_FACES.extend().renderType("cutout").build().create(block, texturemapping, this.modelOutput)));
		this.createBlockItem(block);
	}

	public void basicCtmBlock(Block block) {
		this.blockStateOutput.accept(createSimpleBlock(block, TFBlockModelTemplates.CTM_NO_BASE.extend().customLoader(ConnectedTextureBuilder::new, builder -> builder.connectsTo(block)).build().create(block, TFTextureMapping.ctmBlock(block), this.modelOutput)));
		this.createBlockItem(block);
	}

	public void castleDoor(Block block) {
		Function<Boolean, ResourceLocation> door = bool -> TFBlockModelTemplates.CTM.extend().customLoader(ConnectedTextureBuilder::new, builder -> builder.connectsTo(TFBlocks.BLUE_CASTLE_DOOR.get(), TFBlocks.PINK_CASTLE_DOOR.get(), TFBlocks.VIOLET_CASTLE_DOOR.get(), TFBlocks.YELLOW_CASTLE_DOOR.get()).setOverlayEmissivity(15).setOverlayTintIndex(0)).renderType("cutout").build().createWithSuffix(block, bool ? "_vanished" : "", TFTextureMapping.ctmBlock(TwilightForestMod.prefix("block/castle_door" + (bool ? "_vanished" : "")), TwilightForestMod.prefix("block/castle_door_runes")), this.modelOutput);
		this.blockStateOutput.accept(MultiVariantGenerator.multiVariant(block).with(PropertyDispatch.property(CastleDoorBlock.VANISHED).select(true, Variant.variant().with(VariantProperties.MODEL, door.apply(true))).select(false, Variant.variant().with(VariantProperties.MODEL, door.apply(false)))));
		this.createBlockItem(block);
	}

	public void stairsBlock(Block block) {
		var mapping = TextureMapping.cube(block);
		ResourceLocation inner = ModelTemplates.STAIRS_INNER.createWithSuffix(block, "_inner", mapping, this.modelOutput);
		ResourceLocation straight = ModelTemplates.STAIRS_STRAIGHT.create(block, mapping, this.modelOutput);
		ResourceLocation outer = ModelTemplates.STAIRS_OUTER.createWithSuffix(block, "_outer", mapping, this.modelOutput);
		this.blockStateOutput.accept(createStairs(block, inner, straight, outer));
		this.registerSimpleItemModel(block, straight);
	}

	public void coolerStairsBlock(Block block, ResourceLocation middle) {
		var mapping = TextureMapping.cube(block).put(TFTextureSlot.MIDDLE, middle);
		ResourceLocation inner = TFBlockModelTemplates.STAIRS_INNER.createWithSuffix(block, "_inner", mapping, this.modelOutput);
		ResourceLocation straight = TFBlockModelTemplates.STAIRS_STRAIGHT.create(block, mapping, this.modelOutput);
		ResourceLocation outer = TFBlockModelTemplates.STAIRS_OUTER.createWithSuffix(block, "_outer", mapping, this.modelOutput);
		this.blockStateOutput.accept(createStairs(block, inner, straight, outer));
		this.registerSimpleItemModel(block, straight);
	}

	public void simpleBlockWithRenderType(Block block, String type) {
		this.blockWithRenderType(block, type, ModelTemplates.CUBE_ALL, TextureMapping::cube);
	}

	public void blockWithRenderType(Block block, String type, ModelTemplate template, Function<Block, TextureMapping> mapping) {
		this.blockStateOutput.accept(createSimpleBlock(block, template.extend().renderType(type).build().create(block, mapping.apply(block), this.modelOutput)));
	}

	public void thorns(Block block) {
		TextureMapping mapping = TextureMapping.column(block);
		ResourceLocation main = TFBlockModelTemplates.THORNS_MAIN.createWithSuffix(block, "_main", mapping, this.modelOutput);
		ResourceLocation thorns = TFBlockModelTemplates.THORNS.createWithSuffix(block, "_thorns", mapping, this.modelOutput);
		ResourceLocation top = TFBlockModelTemplates.THORNS_SECTION_TOP.createWithSuffix(block, "_top", mapping, this.modelOutput);
		ResourceLocation bottom = TFBlockModelTemplates.THORNS_SECTION_BOTTOM.createWithSuffix(block, "_bottom", mapping, this.modelOutput);
		ResourceLocation noSection = TFBlockModelTemplates.THORNS_NO_SECTION.createWithSuffix(block, "_no_section", mapping, this.modelOutput);
		ResourceLocation noSectionAlt = TFBlockModelTemplates.THORNS_NO_SECTION_ALT.createWithSuffix(block, "_no_section_alt", mapping, this.modelOutput);

		this.itemModelOutput.accept(block.asItem(), ItemModelUtils.plainModel(main));

		this.blockStateOutput.accept(
			MultiPartGenerator.multiPart(block)
				// MAIN
				.with(
					Condition.condition().term(RotatedPillarBlock.AXIS, Direction.Axis.Y),
					Variant.variant().with(VariantProperties.MODEL, thorns)
				)
				.with(
					Condition.condition().term(RotatedPillarBlock.AXIS, Direction.Axis.Z),
					Variant.variant().with(VariantProperties.MODEL, thorns).with(VariantProperties.X_ROT, VariantProperties.Rotation.R90)
				)
				.with(
					Condition.condition().term(RotatedPillarBlock.AXIS, Direction.Axis.X),
					Variant.variant().with(VariantProperties.MODEL, thorns).with(VariantProperties.X_ROT, VariantProperties.Rotation.R90).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)
				)

				// UP
				.with(
					Condition.condition().term(PipeBlock.UP, true),
					Variant.variant().with(VariantProperties.MODEL, top).with(VariantProperties.X_ROT, VariantProperties.Rotation.R90)
				)
				.with(
					Condition.and(Condition.condition().term(PipeBlock.UP, false), Condition.condition().term(RotatedPillarBlock.AXIS, Direction.Axis.Z, Direction.Axis.Y)),
					Variant.variant().with(VariantProperties.MODEL, noSection).with(VariantProperties.X_ROT, VariantProperties.Rotation.R270)
				)
				.with(
					Condition.and(Condition.condition().term(PipeBlock.UP, false), Condition.condition().term(RotatedPillarBlock.AXIS, Direction.Axis.X)),
					Variant.variant().with(VariantProperties.MODEL, noSection).with(VariantProperties.X_ROT, VariantProperties.Rotation.R270).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)
				)

				// DOWN
				.with(
					Condition.condition().term(PipeBlock.DOWN, true),
					Variant.variant().with(VariantProperties.MODEL, bottom).with(VariantProperties.X_ROT, VariantProperties.Rotation.R90)
				)
				.with(
					Condition.and(Condition.condition().term(PipeBlock.DOWN, false), Condition.condition().term(RotatedPillarBlock.AXIS, Direction.Axis.Z, Direction.Axis.Y)),
					Variant.variant().with(VariantProperties.MODEL, noSection).with(VariantProperties.X_ROT, VariantProperties.Rotation.R90)
				)
				.with(
					Condition.and(Condition.condition().term(PipeBlock.DOWN, false), Condition.condition().term(RotatedPillarBlock.AXIS, Direction.Axis.X)),
					Variant.variant().with(VariantProperties.MODEL, noSection).with(VariantProperties.X_ROT, VariantProperties.Rotation.R90).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)
				)

				// EAST
				.with(
					Condition.condition().term(PipeBlock.EAST, true),
					Variant.variant().with(VariantProperties.MODEL, top).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270)
				)
				.with(
					Condition.and(Condition.condition().term(PipeBlock.EAST, false), Condition.condition().term(RotatedPillarBlock.AXIS, Direction.Axis.Y, Direction.Axis.X)),
					Variant.variant().with(VariantProperties.MODEL, noSection).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)
				)
				.with(
					Condition.and(Condition.condition().term(PipeBlock.EAST, false), Condition.condition().term(RotatedPillarBlock.AXIS, Direction.Axis.Z)),
					Variant.variant().with(VariantProperties.MODEL, noSectionAlt).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)
				)

				// WEST
				.with(
					Condition.condition().term(PipeBlock.WEST, true),
					Variant.variant().with(VariantProperties.MODEL, bottom).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270)
				)
				.with(
					Condition.and(Condition.condition().term(PipeBlock.WEST, false), Condition.condition().term(RotatedPillarBlock.AXIS, Direction.Axis.Y, Direction.Axis.X)),
					Variant.variant().with(VariantProperties.MODEL, noSection).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270)
				)
				.with(
					Condition.and(Condition.condition().term(PipeBlock.WEST, false), Condition.condition().term(RotatedPillarBlock.AXIS, Direction.Axis.Z)),
					Variant.variant().with(VariantProperties.MODEL, noSectionAlt).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270)
				)

				// SOUTH
				.with(
					Condition.condition().term(PipeBlock.SOUTH, true),
					Variant.variant().with(VariantProperties.MODEL, top)
				)
				.with(
					Condition.and(Condition.condition().term(PipeBlock.SOUTH, false), Condition.condition().term(RotatedPillarBlock.AXIS, Direction.Axis.Y, Direction.Axis.Z)),
					Variant.variant().with(VariantProperties.MODEL, noSection).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180)
				)
				.with(
					Condition.and(Condition.condition().term(PipeBlock.SOUTH, false), Condition.condition().term(RotatedPillarBlock.AXIS, Direction.Axis.X)),
					Variant.variant().with(VariantProperties.MODEL, noSectionAlt).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180)
				)

				// NORTH
				.with(
					Condition.condition().term(PipeBlock.NORTH, true),
					Variant.variant().with(VariantProperties.MODEL, bottom)
				)
				.with(
					Condition.and(Condition.condition().term(PipeBlock.NORTH, false), Condition.condition().term(RotatedPillarBlock.AXIS, Direction.Axis.Y, Direction.Axis.Z)),
					Variant.variant().with(VariantProperties.MODEL, noSection)
				)
				.with(
					Condition.and(Condition.condition().term(PipeBlock.NORTH, false), Condition.condition().term(RotatedPillarBlock.AXIS, Direction.Axis.X)),
					Variant.variant().with(VariantProperties.MODEL, noSectionAlt)
				)
		);
	}

	public void forcefield(Block block) {
		this.blockStateOutput.accept(createSimpleBlock(block, TFBlockModelTemplates.FORCEFIELD.extend().customLoader(ForceFieldModelBuilder::new, builder -> {
			//WEST
			builder.forceFieldElement().ifState(ForceFieldModel.ExtraDirection.WEST, true).from(0, 7, 7).to(7, 9, 9).shade(false).face(Direction.WEST).cullface(Direction.WEST).uvs(7, 7, 9, 9).texture("#pane").emissivity(15, 15).end()
				.ifElse().from(7, 7, 7).to(9, 9, 9).shade(false).face(Direction.WEST).uvs(7, 7, 9, 9).texture("#pane").emissivity(15, 15).end().end()

				//EAST
				.forceFieldElement().ifState(ForceFieldModel.ExtraDirection.EAST, true).from(9, 7, 7).to(16, 9, 9).shade(false).face(Direction.EAST).cullface(Direction.EAST).uvs(7, 7, 9, 9).texture("#pane").emissivity(15, 15).end()
				.ifElse().from(7, 7, 7).to(9, 9, 9).shade(false).face(Direction.EAST).uvs(7, 7, 9, 9).texture("#pane").emissivity(15, 15).end().end()

				//DOWN
				.forceFieldElement().ifState(ForceFieldModel.ExtraDirection.DOWN, true).from(7, 0, 7).to(9, 7, 9).shade(false).face(Direction.DOWN).cullface(Direction.DOWN).uvs(7, 7, 9, 9).texture("#pane").emissivity(15, 15).end()
				.ifElse().from(7, 7, 7).to(9, 9, 9).shade(false).face(Direction.DOWN).uvs(7, 7, 9, 9).texture("#pane").emissivity(15, 15).end().end()

				//UP
				.forceFieldElement().ifState(ForceFieldModel.ExtraDirection.UP, true).from(7, 9, 7).to(9, 16, 9).shade(false).face(Direction.UP).cullface(Direction.UP).uvs(7, 7, 9, 9).texture("#pane").emissivity(15, 15).end()
				.ifElse().from(7, 7, 7).to(9, 9, 9).shade(false).face(Direction.UP).uvs(7, 7, 9, 9).texture("#pane").emissivity(15, 15).end().end()

				//NORTH
				.forceFieldElement().ifState(ForceFieldModel.ExtraDirection.NORTH, true).from(7, 7, 0).to(9, 9, 7).shade(false).face(Direction.NORTH).cullface(Direction.NORTH).uvs(7, 7, 9, 9).texture("#pane").emissivity(15, 15).end()
				.ifElse().from(7, 7, 7).to(9, 9, 9).shade(false).face(Direction.NORTH).uvs(7, 7, 9, 9).texture("#pane").emissivity(15, 15).end().end()

				//SOUTH
				.forceFieldElement().ifState(ForceFieldModel.ExtraDirection.SOUTH, true).from(7, 7, 9).to(9, 9, 16).shade(false).face(Direction.SOUTH).cullface(Direction.SOUTH).uvs(7, 7, 9, 9).texture("#pane").emissivity(15, 15).end()
				.ifElse().from(7, 7, 7).to(9, 9, 9).shade(false).face(Direction.SOUTH).uvs(7, 7, 9, 9).texture("#pane").emissivity(15, 15).end().end()

				//DOWN WEST
				.forceFieldElement().ifState(ForceFieldModel.ExtraDirection.DOWN_WEST, true).parents(ForceFieldModel.ExtraDirection.DOWN, ForceFieldModel.ExtraDirection.WEST).from(0, 0, 7).to(7, 7, 9).shade(false)
					.face(Direction.DOWN).cullface(Direction.DOWN).uvs(0, 7, 7, 9).end()
					.face(Direction.WEST).cullface(Direction.WEST).uvs(7, 0, 9, 7).end()
					.face(Direction.NORTH).uvs(0, 0, 7, 7).end()
					.face(Direction.SOUTH).uvs(9, 0, 16, 7).end().faces((direction, face) -> face.texture("#pane").emissivity(15, 15))
				.ifElse().from(7, 0, 7).to(9, 7, 9).parents(ForceFieldModel.ExtraDirection.DOWN).shade(false).face(Direction.WEST).uvs(7, 0, 9, 7).texture("#pane").emissivity(15, 15).end()
				.ifSame().from(0, 7, 7).to(7, 9, 9).parents(ForceFieldModel.ExtraDirection.WEST).shade(false).face(Direction.DOWN).uvs(0, 7, 7, 9).texture("#pane").emissivity(15, 15).end().end()

				//DOWN EAST
				.forceFieldElement().ifState(ForceFieldModel.ExtraDirection.DOWN_EAST, true).parents(ForceFieldModel.ExtraDirection.DOWN, ForceFieldModel.ExtraDirection.EAST).from(9, 0, 7).to(16, 7, 9).shade(false)
					.face(Direction.DOWN).cullface(Direction.DOWN).uvs(9, 7, 16, 9).end()
					.face(Direction.EAST).cullface(Direction.EAST).uvs(7, 0, 9, 7).end()
					.face(Direction.NORTH).uvs(9, 0, 16, 7).end()
					.face(Direction.SOUTH).uvs(0, 0, 7, 7).end().faces((direction, face) -> face.texture("#pane").emissivity(15, 15))
				.ifElse().from(7, 0, 7).to(9, 7, 9).parents(ForceFieldModel.ExtraDirection.DOWN).shade(false).face(Direction.EAST).uvs(7, 0, 9, 7).texture("#pane").emissivity(15, 15).end()
				.ifSame().from(9, 7, 7).to(16, 9, 9).parents(ForceFieldModel.ExtraDirection.EAST).shade(false).face(Direction.DOWN).uvs(9, 7, 16, 9).texture("#pane").emissivity(15, 15).end().end()

				//DOWN NORTH
				.forceFieldElement().ifState(ForceFieldModel.ExtraDirection.DOWN_NORTH, true).from(7, 0, 0).to(9, 7, 7).parents(ForceFieldModel.ExtraDirection.DOWN, ForceFieldModel.ExtraDirection.NORTH).shade(false)
					.face(Direction.DOWN).cullface(Direction.DOWN).uvs(7, 0, 9, 7).end()
					.face(Direction.NORTH).cullface(Direction.NORTH).uvs(7, 0, 9, 7).end()
					.face(Direction.WEST).uvs(0, 0, 7, 7).end()
					.face(Direction.EAST).uvs(9, 9, 16, 16).end().faces((direction, face) -> face.texture("#pane").emissivity(15, 15))
				.ifElse().from(7, 0, 7).to(9, 7, 9).parents(ForceFieldModel.ExtraDirection.DOWN).shade(false).face(Direction.NORTH).uvs(7, 0, 9, 7).texture("#pane").emissivity(15, 15).end()
				.ifSame().from(7, 7, 0).to(9, 9, 7).parents(ForceFieldModel.ExtraDirection.NORTH).shade(false).face(Direction.DOWN).uvs(7, 0, 9, 7).texture("#pane").emissivity(15, 15).end().end()

				//DOWN SOUTH
				.forceFieldElement().ifState(ForceFieldModel.ExtraDirection.DOWN_SOUTH, true).from(7, 0, 9).to(9, 7, 16).parents(ForceFieldModel.ExtraDirection.DOWN, ForceFieldModel.ExtraDirection.SOUTH).shade(false)
					.face(Direction.DOWN).cullface(Direction.DOWN).uvs(7, 9, 9, 16).end()
					.face(Direction.SOUTH).cullface(Direction.SOUTH).uvs(7, 0, 9, 7).end()
					.face(Direction.WEST).uvs(9, 0, 16, 7).end()
					.face(Direction.EAST).uvs(0, 0, 7, 7).end().faces((direction, face) -> face.texture("#pane").emissivity(15, 15))
				.ifElse().from(7, 0, 7).to(9, 7, 9).parents(ForceFieldModel.ExtraDirection.DOWN).shade(false).face(Direction.SOUTH).uvs(7, 0, 9, 7).texture("#pane").emissivity(15, 15).end()
				.ifSame().from(7, 7, 9).to(9, 9, 16).parents(ForceFieldModel.ExtraDirection.SOUTH).shade(false).face(Direction.DOWN).uvs(7, 9, 9, 16).texture("#pane").emissivity(15, 15).end().end()

				//UP WEST
				.forceFieldElement().ifState(ForceFieldModel.ExtraDirection.UP_WEST, true).from(0, 9, 7).to(7, 16, 9).parents(ForceFieldModel.ExtraDirection.UP, ForceFieldModel.ExtraDirection.WEST).shade(false)
					.face(Direction.UP).cullface(Direction.UP).uvs(0, 7, 7, 9).end()
					.face(Direction.WEST).cullface(Direction.WEST).uvs(7, 9, 9, 16).end()
					.face(Direction.NORTH).uvs(0, 9, 7, 16).end()
					.face(Direction.SOUTH).uvs(9, 9, 16, 16).end().faces((direction, face) -> face.texture("#pane").emissivity(15, 15))
				.ifElse().from(7, 9, 7).to(9, 16, 9).parents(ForceFieldModel.ExtraDirection.UP).shade(false).face(Direction.WEST).uvs(7, 9, 9, 16).texture("#pane").emissivity(15, 15).end()
				.ifSame().from(0, 7, 7).to(7, 9, 9).parents(ForceFieldModel.ExtraDirection.WEST).shade(false).face(Direction.UP).uvs(0, 7, 7, 9).texture("#pane").emissivity(15, 15).end().end()

				//UP EAST
				.forceFieldElement().ifState(ForceFieldModel.ExtraDirection.UP_EAST, true).from(9, 9, 7).to(16, 16, 9).parents(ForceFieldModel.ExtraDirection.UP, ForceFieldModel.ExtraDirection.EAST).shade(false)
					.face(Direction.UP).cullface(Direction.UP).uvs(9, 7, 16, 9).end()
					.face(Direction.EAST).cullface(Direction.EAST).uvs(7, 9, 9, 16).end()
					.face(Direction.NORTH).uvs(9, 9, 16, 16).end()
					.face(Direction.SOUTH).uvs(0, 9, 7, 16).end().faces((direction, face) -> face.texture("#pane").emissivity(15, 15))
				.ifElse().from(7, 9, 7).to(9, 16, 9).parents(ForceFieldModel.ExtraDirection.UP).shade(false).face(Direction.EAST).uvs(7, 9, 9, 16).texture("#pane").emissivity(15, 15).end()
				.ifSame().from(9, 7, 7).to(16, 9, 9).parents(ForceFieldModel.ExtraDirection.EAST).shade(false).face(Direction.UP).uvs(9, 7, 16, 9).texture("#pane").emissivity(15, 15).end().end()

				//UP NORTH
				.forceFieldElement().ifState(ForceFieldModel.ExtraDirection.UP_NORTH, true).from(7, 9, 0).to(9, 16, 7).parents(ForceFieldModel.ExtraDirection.UP, ForceFieldModel.ExtraDirection.NORTH).shade(false)
					.face(Direction.UP).cullface(Direction.UP).uvs(7, 0, 9, 7).end()
					.face(Direction.NORTH).cullface(Direction.NORTH).uvs(7, 9, 9, 16).end()
					.face(Direction.WEST).uvs(0, 9, 7, 16).end()
					.face(Direction.EAST).uvs(9, 9, 16, 16).end().faces((direction, face) -> face.texture("#pane").emissivity(15, 15))
				.ifElse().from(7, 9, 7).to(9, 16, 9).parents(ForceFieldModel.ExtraDirection.UP).shade(false).face(Direction.NORTH).uvs(7, 9, 9, 16).texture("#pane").emissivity(15, 15).end()
				.ifSame().from(7, 7, 0).to(9, 9, 7).parents(ForceFieldModel.ExtraDirection.NORTH).shade(false).face(Direction.UP).uvs(7, 0, 9, 7).texture("#pane").emissivity(15, 15).end().end()

				//UP SOUTH
				.forceFieldElement().ifState(ForceFieldModel.ExtraDirection.UP_SOUTH, true).from(7, 9, 9).to(9, 16, 16).parents(ForceFieldModel.ExtraDirection.UP, ForceFieldModel.ExtraDirection.SOUTH).shade(false)
					.face(Direction.UP).cullface(Direction.UP).uvs(7, 9, 9, 16).end()
					.face(Direction.SOUTH).cullface(Direction.SOUTH).uvs(7, 9, 9, 16).end()
					.face(Direction.WEST).uvs(9, 9, 16, 16).end()
					.face(Direction.EAST).uvs(0, 9, 7, 16).end().faces((direction, face) -> face.texture("#pane").emissivity(15, 15))
				.ifElse().from(7, 9, 7).to(9, 16, 9).parents(ForceFieldModel.ExtraDirection.UP).shade(false).face(Direction.SOUTH).uvs(7, 9, 9, 16).texture("#pane").emissivity(15, 15).end()
				.ifSame().from(7, 7, 9).to(9, 9, 16).parents(ForceFieldModel.ExtraDirection.SOUTH).shade(false).face(Direction.UP).uvs(7, 9, 9, 16).texture("#pane").emissivity(15, 15).end().end()

				//NORTH WEST
				.forceFieldElement().ifState(ForceFieldModel.ExtraDirection.NORTH_WEST, true).from(0, 7, 0).to(7, 9, 7).parents(ForceFieldModel.ExtraDirection.NORTH, ForceFieldModel.ExtraDirection.WEST).shade(false)
					.face(Direction.NORTH).cullface(Direction.NORTH).uvs(0, 7, 7, 9).end()
					.face(Direction.WEST).cullface(Direction.WEST).uvs(9, 7, 16, 9).end()
					.face(Direction.DOWN).uvs(0, 9, 7, 16).end()
					.face(Direction.UP).uvs(9, 9, 16, 16).end().faces((direction, face) -> face.texture("#pane").emissivity(15, 15))
				.ifElse().from(7, 7, 0).to(9, 9, 7).parents(ForceFieldModel.ExtraDirection.NORTH).shade(false).face(Direction.WEST).uvs(9, 7, 16, 9).texture("#pane").emissivity(15, 15).end()
				.ifSame().from(0, 7, 7).to(7, 9, 9).parents(ForceFieldModel.ExtraDirection.WEST).shade(false).face(Direction.NORTH).uvs(0, 7, 7, 9).texture("#pane").emissivity(15, 15).end().end()

				//NORTH EAST
				.forceFieldElement().ifState(ForceFieldModel.ExtraDirection.NORTH_EAST, true).from(9, 7, 0).to(16, 9, 7).parents(ForceFieldModel.ExtraDirection.NORTH, ForceFieldModel.ExtraDirection.EAST).shade(false)
					.face(Direction.NORTH).cullface(Direction.NORTH).uvs(9, 7, 16, 9).end()
					.face(Direction.EAST).cullface(Direction.EAST).uvs(0, 7, 7, 9).end()
					.face(Direction.DOWN).uvs(9, 9, 16, 16).end()
					.face(Direction.UP).uvs(0, 9, 7, 16).end().faces((direction, face) -> face.texture("#pane").emissivity(15, 15))
				.ifElse().from(7, 7, 0).to(9, 9, 7).parents(ForceFieldModel.ExtraDirection.NORTH).shade(false).face(Direction.EAST).uvs(0, 7, 7, 9).texture("#pane").emissivity(15, 15).end()
				.ifSame().from(9, 7, 7).to(16, 9, 9).parents(ForceFieldModel.ExtraDirection.EAST).shade(false).face(Direction.NORTH).uvs(9, 7, 16, 9).texture("#pane").emissivity(15, 15).end().end()

				//SOUTH WEST
				.forceFieldElement().ifState(ForceFieldModel.ExtraDirection.SOUTH_WEST, true).from(0, 7, 9).to(7, 9, 16).parents(ForceFieldModel.ExtraDirection.SOUTH, ForceFieldModel.ExtraDirection.WEST).shade(false)
					.face(Direction.SOUTH).cullface(Direction.SOUTH).uvs(0, 7, 7, 9).end()
					.face(Direction.WEST).cullface(Direction.WEST).uvs(9, 7, 16, 9).end()
					.face(Direction.DOWN).uvs(0, 9, 7, 16).end()
					.face(Direction.UP).uvs(9, 9, 16, 16).end().faces((direction, face) -> face.texture("#pane").emissivity(15, 15))
				.ifElse().from(7, 7, 9).to(9, 9, 16).parents(ForceFieldModel.ExtraDirection.SOUTH).shade(false).face(Direction.WEST).uvs(9, 7, 16, 9).texture("#pane").emissivity(15, 15).end()
				.ifSame().from(0, 7, 7).to(7, 9, 9).parents(ForceFieldModel.ExtraDirection.WEST).shade(false).face(Direction.SOUTH).uvs(0, 7, 7, 9).texture("#pane").emissivity(15, 15).end().end()

				//SOUTH EAST
				.forceFieldElement().ifState(ForceFieldModel.ExtraDirection.SOUTH_EAST, true).from(9, 7, 9).to(16, 9, 16).parents(ForceFieldModel.ExtraDirection.SOUTH, ForceFieldModel.ExtraDirection.EAST).shade(false)
					.face(Direction.SOUTH).cullface(Direction.SOUTH).uvs(0, 7, 7, 9).end()
					.face(Direction.EAST).cullface(Direction.EAST).uvs(9, 7, 16, 9).end()
					.face(Direction.DOWN).uvs(9, 9, 16, 16).end()
					.face(Direction.UP).uvs(0, 9, 7, 16).end().faces((direction, face) -> face.texture("#pane").emissivity(15, 15))
				.ifElse().from(7, 7, 9).to(9, 9, 16).parents(ForceFieldModel.ExtraDirection.SOUTH).shade(false).face(Direction.EAST).uvs(9, 7, 16, 9).texture("#pane").emissivity(15, 15).end()
				.ifSame().from(9, 7, 7).to(16, 9, 9).parents(ForceFieldModel.ExtraDirection.EAST).shade(false).face(Direction.SOUTH).uvs(0, 7, 7, 9).texture("#pane").emissivity(15, 15).end().end();
		}).build().create(block, TFTextureMapping.forcefield(block), this.modelOutput)));
		this.registerSimpleFlatItemModel(block);
	}
}
