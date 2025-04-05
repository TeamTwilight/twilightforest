package twilightforest.datagen.helpers;

import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelOutput;
import net.minecraft.client.data.models.blockstates.*;
import net.minecraft.client.data.models.model.*;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.PipeBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import twilightforest.TwilightForestMod;
import twilightforest.block.CastleDoorBlock;
import twilightforest.block.DirectionalRotatedPillarBlock;
import twilightforest.block.NagastoneBlock;
import twilightforest.block.WallPillarBlock;
import twilightforest.client.model.block.connected.ConnectedTextureBuilder;
import twilightforest.client.model.block.forcefield.ForceFieldModel;
import twilightforest.client.model.block.forcefield.ForceFieldModelBuilder;
import twilightforest.datagen.assets.models.TFModelTemplates;
import twilightforest.datagen.assets.models.TFTextureMapping;
import twilightforest.datagen.assets.models.TFTextureSlot;
import twilightforest.enums.NagastoneVariant;
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

	public void spawner(Block block, String texture) {
		TextureMapping texturemapping = TextureMapping.cube(TwilightForestMod.prefix(texture));
		this.blockStateOutput.accept(createSimpleBlock(block, ModelTemplates.CUBE_ALL_INNER_FACES.extend().renderType("cutout").build().create(block, texturemapping, this.modelOutput)));
		this.createBlockItem(block);
	}

	public void basicCtmBlock(Block block) {
		this.blockStateOutput.accept(createSimpleBlock(block, TFModelTemplates.CTM_NO_BASE.extend().customLoader(ConnectedTextureBuilder::new, builder -> builder.connectsTo(block)).build().create(block, TFTextureMapping.ctmBlock(block), this.modelOutput)));
		this.createBlockItem(block);
	}

	public void castleDoor(Block block) {
		Function<Boolean, ResourceLocation> door = bool -> TFModelTemplates.CTM.extend().customLoader(ConnectedTextureBuilder::new, builder -> builder.connectsTo(TFBlocks.BLUE_CASTLE_DOOR.get(), TFBlocks.PINK_CASTLE_DOOR.get(), TFBlocks.VIOLET_CASTLE_DOOR.get(), TFBlocks.YELLOW_CASTLE_DOOR.get()).setOverlayEmissivity(15).setOverlayTintIndex(0)).renderType("cutout").build().createWithSuffix(block, bool ? "_vanished" : "", TFTextureMapping.ctmBlock(TwilightForestMod.prefix("block/castle_door" + (bool ? "_vanished" : "")), TwilightForestMod.prefix("block/castle_door_runes")), this.modelOutput);
		this.blockStateOutput.accept(MultiVariantGenerator.multiVariant(block).with(PropertyDispatch.property(CastleDoorBlock.VANISHED).select(true, Variant.variant().with(VariantProperties.MODEL, door.apply(true))).select(false, Variant.variant().with(VariantProperties.MODEL, door.apply(false)))));
		this.createBlockItem(block);
	}

	public void stairsBlock(Block block) {
		TextureMapping mapping = TextureMapping.cube(block);
		ResourceLocation inner = ModelTemplates.STAIRS_INNER.createWithSuffix(block, "_inner", mapping, this.modelOutput);
		ResourceLocation straight = ModelTemplates.STAIRS_STRAIGHT.create(block, mapping, this.modelOutput);
		ResourceLocation outer = ModelTemplates.STAIRS_OUTER.createWithSuffix(block, "_outer", mapping, this.modelOutput);
		this.stairsBlock(block, straight, inner, outer);
	}

	public void stairsBlock(Block block, ResourceLocation straight, ResourceLocation inner, ResourceLocation outer) {
		this.blockStateOutput.accept(createStairs(block, inner, straight, outer));
		this.registerSimpleItemModel(block, straight);
	}

	public void coolerStairsBlock(Block block, ResourceLocation middle) {
		TextureMapping mapping = TextureMapping.cube(block).put(TFTextureSlot.MIDDLE, middle);
		ResourceLocation inner = TFModelTemplates.STAIRS_INNER.createWithSuffix(block, "_inner", mapping, this.modelOutput);
		ResourceLocation straight = TFModelTemplates.STAIRS_STRAIGHT.create(block, mapping, this.modelOutput);
		ResourceLocation outer = TFModelTemplates.STAIRS_OUTER.createWithSuffix(block, "_outer", mapping, this.modelOutput);
		this.blockStateOutput.accept(createStairs(block, inner, straight, outer));
		this.registerSimpleItemModel(block, straight);
	}

	public void simpleBlockWithRenderType(Block block, String type) {
		this.blockWithRenderType(block, type, ModelTemplates.CUBE_ALL, TextureMapping::cube);
	}

	public void blockWithRenderType(Block block, String type, ModelTemplate template, Function<Block, TextureMapping> mapping) {
		this.blockStateOutput.accept(createSimpleBlock(block, template.extend().renderType(type).build().create(block, mapping.apply(block), this.modelOutput)));
	}

	public void nagaStone() {
		TextureMapping mapping = TextureMapping.cube(TFBlocks.NAGASTONE.get());

		TextureMapping solidMapping = TextureMapping.cube(TFBlocks.NAGASTONE.get())
			.put(TextureSlot.SIDE, TwilightForestMod.prefix("block/nagastone_long_side"))
			.put(TextureSlot.BOTTOM, TwilightForestMod.prefix("block/nagastone_bottom_long"))
			.put(TextureSlot.TOP, TwilightForestMod.prefix("block/nagastone_turn_top"));

		ResourceLocation solid = TFModelTemplates.CUBE_BOTTOM_TOP.createWithSuffix(TFBlocks.NAGASTONE.get(), "_solid", solidMapping, this.modelOutput);
		// todo 1.21.x cleanup: generate these models as well instead of ModelTemplates.create().extend().parent-ing them
		ResourceLocation down = ModelTemplates.create("twilightforest:naga_segment_down").extend().parent(TwilightForestMod.prefix("block/naga_segment/down")).build().createWithSuffix(TFBlocks.NAGASTONE.get(), "_down", mapping, this.modelOutput);
		ResourceLocation up = ModelTemplates.create("twilightforest:naga_segment_up").extend().parent(TwilightForestMod.prefix("block/naga_segment/up")).build().createWithSuffix(TFBlocks.NAGASTONE.get(), "_up", mapping, this.modelOutput);
		ResourceLocation horizontal = ModelTemplates.create("twilightforest:naga_segment_horizontal").extend().parent(TwilightForestMod.prefix("block/naga_segment/horizontal")).build().createWithSuffix(TFBlocks.NAGASTONE.get(), "_horizontal", mapping, this.modelOutput);
		ResourceLocation vertical = ModelTemplates.create("twilightforest:naga_segment_vertical").extend().parent(TwilightForestMod.prefix("block/naga_segment/vertical")).build().createWithSuffix(TFBlocks.NAGASTONE.get(), "_vertical", mapping, this.modelOutput);

		this.itemModelOutput.accept(TFBlocks.NAGASTONE.asItem(), ItemModelUtils.plainModel(solid));
		this.blockStateOutput.accept(MultiVariantGenerator.multiVariant(TFBlocks.NAGASTONE.get()).with(
			PropertyDispatch.property(NagastoneBlock.VARIANT)
				.select(NagastoneVariant.NORTH_DOWN, Variant.variant().with(VariantProperties.MODEL, down).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
				.select(NagastoneVariant.SOUTH_DOWN, Variant.variant().with(VariantProperties.MODEL, down).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
				.select(NagastoneVariant.WEST_DOWN, Variant.variant().with(VariantProperties.MODEL, down).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
				.select(NagastoneVariant.EAST_DOWN, Variant.variant().with(VariantProperties.MODEL, down))

				.select(NagastoneVariant.NORTH_UP, Variant.variant().with(VariantProperties.MODEL, up).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
				.select(NagastoneVariant.SOUTH_UP, Variant.variant().with(VariantProperties.MODEL, up).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
				.select(NagastoneVariant.WEST_UP, Variant.variant().with(VariantProperties.MODEL, up).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
				.select(NagastoneVariant.EAST_UP, Variant.variant().with(VariantProperties.MODEL, up))

				.select(NagastoneVariant.AXIS_X, Variant.variant().with(VariantProperties.MODEL, horizontal))
				.select(NagastoneVariant.AXIS_Y, Variant.variant().with(VariantProperties.MODEL, vertical))
				.select(NagastoneVariant.AXIS_Z, Variant.variant().with(VariantProperties.MODEL, horizontal).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
				.select(NagastoneVariant.SOLID, Variant.variant().with(VariantProperties.MODEL, solid))
		));

		TextureMapping faceMapping = TextureMapping.cube(TFBlocks.NAGASTONE_HEAD.get())
			.put(TextureSlot.UP, TwilightForestMod.prefix("block/nagastone_top_tip"))
			.put(TextureSlot.DOWN, TwilightForestMod.prefix("block/nagastone_bottom_tip"))
			.put(TextureSlot.SOUTH, TwilightForestMod.prefix("block/nagastone_face_left"))
			.put(TextureSlot.NORTH, TwilightForestMod.prefix("block/nagastone_face_right"))
			.put(TextureSlot.WEST, TwilightForestMod.prefix("block/nagastone_face_front"))
			.put(TextureSlot.EAST, TwilightForestMod.prefix("block/nagastone_cross_section"))
			.put(TextureSlot.PARTICLE, TwilightForestMod.prefix("block/nagastone_face_front"));
		ResourceLocation model = TFModelTemplates.CUBE.create(TFBlocks.NAGASTONE_HEAD.get(), faceMapping, this.modelOutput);

		this.blockStateOutput.accept(MultiVariantGenerator.multiVariant(TFBlocks.NAGASTONE_HEAD.get()).with(
			PropertyDispatch.property(BlockStateProperties.HORIZONTAL_FACING)
				.select(Direction.SOUTH, Variant.variant().with(VariantProperties.MODEL, model).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
				.select(Direction.NORTH, Variant.variant().with(VariantProperties.MODEL, model).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
				.select(Direction.WEST, Variant.variant().with(VariantProperties.MODEL, model).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R0))
				.select(Direction.EAST, Variant.variant().with(VariantProperties.MODEL, model).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
		));
		this.itemModelOutput.accept(TFBlocks.NAGASTONE_HEAD.asItem(), ItemModelUtils.plainModel(model));

		nagastonePillar(TFBlocks.NAGASTONE_PILLAR.get(), "");
		nagastonePillar(TFBlocks.MOSSY_NAGASTONE_PILLAR.get(), "_mossy");
		nagastonePillar(TFBlocks.CRACKED_NAGASTONE_PILLAR.get(), "_weathered");
		etchedNagastone(TFBlocks.ETCHED_NAGASTONE.get(), "");
		etchedNagastone(TFBlocks.MOSSY_ETCHED_NAGASTONE.get(), "_mossy");
		etchedNagastone(TFBlocks.CRACKED_ETCHED_NAGASTONE.get(), "_weathered");

		bisectedStairsBlock(TFBlocks.NAGASTONE_STAIRS_LEFT.get(), TwilightForestMod.prefix("block/etched_nagastone_left"), TwilightForestMod.prefix("block/stone_tiles"), TwilightForestMod.prefix("block/nagastone_bare"));
		bisectedStairsBlock(TFBlocks.NAGASTONE_STAIRS_RIGHT.get(), TwilightForestMod.prefix("block/etched_nagastone_right"), TwilightForestMod.prefix("block/stone_tiles"), TwilightForestMod.prefix("block/nagastone_bare"));
		bisectedStairsBlock(TFBlocks.MOSSY_NAGASTONE_STAIRS_LEFT.get(), TwilightForestMod.prefix("block/etched_nagastone_left_mossy"), TwilightForestMod.prefix("block/stone_tiles_mossy"), TwilightForestMod.prefix("block/nagastone_bare_mossy"));
		bisectedStairsBlock(TFBlocks.MOSSY_NAGASTONE_STAIRS_RIGHT.get(), TwilightForestMod.prefix("block/etched_nagastone_right_mossy"), TwilightForestMod.prefix("block/stone_tiles_mossy"), TwilightForestMod.prefix("block/nagastone_bare_mossy"));
		bisectedStairsBlock(TFBlocks.CRACKED_NAGASTONE_STAIRS_LEFT.get(), TwilightForestMod.prefix("block/etched_nagastone_left_weathered"), TwilightForestMod.prefix("block/stone_tiles_weathered"), TwilightForestMod.prefix("block/nagastone_bare_weathered"));
		bisectedStairsBlock(TFBlocks.CRACKED_NAGASTONE_STAIRS_RIGHT.get(), TwilightForestMod.prefix("block/etched_nagastone_right_weathered"), TwilightForestMod.prefix("block/stone_tiles_weathered"), TwilightForestMod.prefix("block/nagastone_bare_weathered"));
	}

	private void nagastonePillar(Block block, String suffix) {
		TextureMapping mapping = TextureMapping.cube(TFBlocks.NAGASTONE.get())
			.put(TextureSlot.END, TwilightForestMod.prefix("block/nagastone_pillar_end" + suffix))
			.put(TextureSlot.SIDE, TwilightForestMod.prefix("block/nagastone_pillar_side" + suffix));
		ResourceLocation model = TFModelTemplates.CUBE_COLUMN.create(block, mapping, this.modelOutput);

		TextureMapping altMapping = TextureMapping.cube(TFBlocks.NAGASTONE.get())
			.put(TextureSlot.END, TwilightForestMod.prefix("block/nagastone_pillar_end" + suffix))
			.put(TextureSlot.SIDE, TwilightForestMod.prefix("block/nagastone_pillar_side" + suffix + "_alt"));
		ResourceLocation reversed = TFModelTemplates.CUBE_COLUMN.createWithSuffix(block, "_alt", altMapping, this.modelOutput);

		this.blockStateOutput.accept(MultiVariantGenerator.multiVariant(block).with(
			PropertyDispatch.properties(RotatedPillarBlock.AXIS, DirectionalRotatedPillarBlock.REVERSED)
				.select(Direction.Axis.X, true, Variant.variant().with(VariantProperties.MODEL, reversed).with(VariantProperties.X_ROT, VariantProperties.Rotation.R270).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
				.select(Direction.Axis.Y, true, Variant.variant().with(VariantProperties.MODEL, reversed))
				.select(Direction.Axis.Z, true, Variant.variant().with(VariantProperties.MODEL, reversed).with(VariantProperties.X_ROT, VariantProperties.Rotation.R270))

				.select(Direction.Axis.X, false, Variant.variant().with(VariantProperties.MODEL, model).with(VariantProperties.X_ROT, VariantProperties.Rotation.R270).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
				.select(Direction.Axis.Y, false, Variant.variant().with(VariantProperties.MODEL, model))
				.select(Direction.Axis.Z, false, Variant.variant().with(VariantProperties.MODEL, model).with(VariantProperties.X_ROT, VariantProperties.Rotation.R270))
		));
		this.itemModelOutput.accept(block.asItem(), ItemModelUtils.plainModel(model));
	}

	private void etchedNagastone(Block block, String suffix) {
		TextureMapping mapping = TextureMapping.cube(TFBlocks.NAGASTONE.get())
			.put(TextureSlot.END, TwilightForestMod.prefix("block/stone_tiles" + suffix))
			.put(TextureSlot.SIDE, TwilightForestMod.prefix("block/etched_nagastone_up" + suffix))
			.put(TextureSlot.PARTICLE, TwilightForestMod.prefix("block/stone_tiles" + suffix));
		ResourceLocation model = ModelTemplates.CUBE_COLUMN.create(block, mapping, this.modelOutput);

		this.blockStateOutput.accept(MultiVariantGenerator.multiVariant(block).with(
			PropertyDispatch.property(DirectionalBlock.FACING)
				.select(Direction.UP, Variant.variant().with(VariantProperties.MODEL, model))
				.select(Direction.DOWN, Variant.variant().with(VariantProperties.MODEL, model).with(VariantProperties.X_ROT, VariantProperties.Rotation.R180))
				.select(Direction.SOUTH, Variant.variant().with(VariantProperties.MODEL, model).with(VariantProperties.X_ROT, VariantProperties.Rotation.R270))
				.select(Direction.NORTH, Variant.variant().with(VariantProperties.MODEL, model).with(VariantProperties.X_ROT, VariantProperties.Rotation.R90))
				.select(Direction.WEST, Variant.variant().with(VariantProperties.MODEL, model).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90).with(VariantProperties.X_ROT, VariantProperties.Rotation.R270))
				.select(Direction.EAST, Variant.variant().with(VariantProperties.MODEL, model).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90).with(VariantProperties.X_ROT, VariantProperties.Rotation.R90))
		));
		this.itemModelOutput.accept(block.asItem(), ItemModelUtils.plainModel(model));
	}

	protected void bisectedStairsBlock(Block block, ResourceLocation side, ResourceLocation end, ResourceLocation middle) {
		TextureMapping mapping = TextureMapping.cube(block)
			.put(TextureSlot.BOTTOM, end)
			.put(TextureSlot.TOP, end)
			.put(TextureSlot.SIDE, side)
			.put(TFTextureSlot.MIDDLE, middle)
			.put(TextureSlot.PARTICLE, middle);

		ResourceLocation inner = TFModelTemplates.BISECTED_STAIRS_INNER.createWithSuffix(block, "_inner", mapping, this.modelOutput);
		ResourceLocation straight = TFModelTemplates.BISECTED_STAIRS_STRAIGHT.create(block, mapping, this.modelOutput);
		ResourceLocation outer = TFModelTemplates.BISECTED_STAIRS_OUTER.createWithSuffix(block, "_outer", mapping, this.modelOutput);
		this.stairsBlock(block, straight, inner, outer);
	}

	public void stonePillar() {
		ResourceLocation base = TwilightForestMod.prefix("block/pillar/pillar_base");
		ResourceLocation up = TwilightForestMod.prefix("block/pillar/pillar_up");
		ResourceLocation down = TwilightForestMod.prefix("block/pillar/pillar_down");
		ResourceLocation top = TwilightForestMod.prefix("block/pillar/pillar_top");
		ResourceLocation bottom = TwilightForestMod.prefix("block/pillar/pillar_bottom");

		this.itemModelOutput.accept(TFBlocks.TWISTED_STONE_PILLAR.asItem(), ItemModelUtils.plainModel(TwilightForestMod.prefix("block/pillar/pillar_inventory")));

		this.blockStateOutput.accept(
			MultiPartGenerator.multiPart(TFBlocks.TWISTED_STONE_PILLAR.get())
				// X
				.with(
					Condition.condition().term(WallPillarBlock.AXIS, Direction.Axis.X),
					Variant.variant().with(VariantProperties.MODEL, base).with(VariantProperties.X_ROT, VariantProperties.Rotation.R90).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)
				)
				.with(
					Condition.and(Condition.condition().term(WallPillarBlock.AXIS, Direction.Axis.X), Condition.condition().term(PipeBlock.EAST, false)),
					Variant.variant().with(VariantProperties.MODEL, top).with(VariantProperties.X_ROT, VariantProperties.Rotation.R90).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)
				)
				.with(
					Condition.and(Condition.condition().term(WallPillarBlock.AXIS, Direction.Axis.X), Condition.condition().term(PipeBlock.WEST, false)),
					Variant.variant().with(VariantProperties.MODEL, bottom).with(VariantProperties.X_ROT, VariantProperties.Rotation.R90).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)
				)
				.with(
					Condition.and(Condition.condition().term(WallPillarBlock.AXIS, Direction.Axis.Y, Direction.Axis.Z), Condition.condition().term(PipeBlock.EAST, true)),
					Variant.variant().with(VariantProperties.MODEL, up).with(VariantProperties.X_ROT, VariantProperties.Rotation.R90).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)
				)
				.with(
					Condition.and(Condition.condition().term(WallPillarBlock.AXIS, Direction.Axis.Y, Direction.Axis.Z), Condition.condition().term(PipeBlock.WEST, true)),
					Variant.variant().with(VariantProperties.MODEL, down).with(VariantProperties.X_ROT, VariantProperties.Rotation.R90).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)
				)

				// Y
				.with(
					Condition.condition().term(WallPillarBlock.AXIS, Direction.Axis.Y),
					Variant.variant().with(VariantProperties.MODEL, base)
				)
				.with(
					Condition.and(Condition.condition().term(WallPillarBlock.AXIS, Direction.Axis.Y), Condition.condition().term(PipeBlock.UP, false)),
					Variant.variant().with(VariantProperties.MODEL, top)
				)
				.with(
					Condition.and(Condition.condition().term(WallPillarBlock.AXIS, Direction.Axis.Y), Condition.condition().term(PipeBlock.DOWN, false)),
					Variant.variant().with(VariantProperties.MODEL, bottom)
				)
				.with(
					Condition.and(Condition.condition().term(WallPillarBlock.AXIS, Direction.Axis.X, Direction.Axis.Z), Condition.condition().term(PipeBlock.UP, true)),
					Variant.variant().with(VariantProperties.MODEL, up)
				)
				.with(
					Condition.and(Condition.condition().term(WallPillarBlock.AXIS, Direction.Axis.X, Direction.Axis.Z), Condition.condition().term(PipeBlock.DOWN, true)),
					Variant.variant().with(VariantProperties.MODEL, down)
				)

				// Z
				.with(
					Condition.condition().term(WallPillarBlock.AXIS, Direction.Axis.Z),
					Variant.variant().with(VariantProperties.MODEL, base).with(VariantProperties.X_ROT, VariantProperties.Rotation.R90)
				)
				.with(
					Condition.and(Condition.condition().term(WallPillarBlock.AXIS, Direction.Axis.Z), Condition.condition().term(PipeBlock.NORTH, false)),
					Variant.variant().with(VariantProperties.MODEL, top).with(VariantProperties.X_ROT, VariantProperties.Rotation.R90)
				)
				.with(
					Condition.and(Condition.condition().term(WallPillarBlock.AXIS, Direction.Axis.Z), Condition.condition().term(PipeBlock.SOUTH, false)),
					Variant.variant().with(VariantProperties.MODEL, bottom).with(VariantProperties.X_ROT, VariantProperties.Rotation.R90)
				)
				.with(
					Condition.and(Condition.condition().term(WallPillarBlock.AXIS, Direction.Axis.X, Direction.Axis.Y), Condition.condition().term(PipeBlock.NORTH, true)),
					Variant.variant().with(VariantProperties.MODEL, up).with(VariantProperties.X_ROT, VariantProperties.Rotation.R90)
				)
				.with(
					Condition.and(Condition.condition().term(WallPillarBlock.AXIS, Direction.Axis.X, Direction.Axis.Y), Condition.condition().term(PipeBlock.SOUTH, true)),
					Variant.variant().with(VariantProperties.MODEL, down).with(VariantProperties.X_ROT, VariantProperties.Rotation.R90)
				)
		);
	}

	public void thorns(Block block) {
		TextureMapping mapping = TextureMapping.column(block);
		ResourceLocation main = TFModelTemplates.THORNS_MAIN.createWithSuffix(block, "_main", mapping, this.modelOutput);
		ResourceLocation thorns = TFModelTemplates.THORNS.createWithSuffix(block, "_thorns", mapping, this.modelOutput);
		ResourceLocation top = TFModelTemplates.THORNS_SECTION_TOP.createWithSuffix(block, "_top", mapping, this.modelOutput);
		ResourceLocation bottom = TFModelTemplates.THORNS_SECTION_BOTTOM.createWithSuffix(block, "_bottom", mapping, this.modelOutput);
		ResourceLocation noSection = TFModelTemplates.THORNS_NO_SECTION.createWithSuffix(block, "_no_section", mapping, this.modelOutput);
		ResourceLocation noSectionAlt = TFModelTemplates.THORNS_NO_SECTION_ALT.createWithSuffix(block, "_no_section_alt", mapping, this.modelOutput);

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

	public void directionalCrossModel(Block block, PlantType type) {
		ResourceLocation resourcelocation = type.getCross().extend().renderType("cutout").build().create(block, TextureMapping.cross(block), this.modelOutput);

		this.itemModelOutput.accept(block.asItem(), ItemModelUtils.plainModel(this.createFlatItemModelWithBlockTexture(block.asItem(), block)));
		this.blockStateOutput.accept(MultiVariantGenerator.multiVariant(block).with(
			PropertyDispatch.property(DirectionalBlock.FACING)
				.select(Direction.UP, Variant.variant().with(VariantProperties.MODEL, resourcelocation))
				.select(Direction.DOWN, Variant.variant().with(VariantProperties.MODEL, resourcelocation).with(VariantProperties.X_ROT, VariantProperties.Rotation.R180))
				.select(Direction.SOUTH, Variant.variant().with(VariantProperties.MODEL, resourcelocation).with(VariantProperties.X_ROT, VariantProperties.Rotation.R270))
				.select(Direction.NORTH, Variant.variant().with(VariantProperties.MODEL, resourcelocation).with(VariantProperties.X_ROT, VariantProperties.Rotation.R90))
				.select(Direction.WEST, Variant.variant().with(VariantProperties.MODEL, resourcelocation).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90).with(VariantProperties.X_ROT, VariantProperties.Rotation.R270))
				.select(Direction.EAST, Variant.variant().with(VariantProperties.MODEL, resourcelocation).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90).with(VariantProperties.X_ROT, VariantProperties.Rotation.R90))
		));
	}

	public void forcefield(Block block) {
		this.blockStateOutput.accept(createSimpleBlock(block, TFModelTemplates.FORCEFIELD.extend().customLoader(ForceFieldModelBuilder::new, builder -> {
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
