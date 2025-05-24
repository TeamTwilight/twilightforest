package twilightforest.datagen.helpers.models;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import net.minecraft.client.data.models.ItemModelOutput;
import net.minecraft.client.data.models.blockstates.*;
import net.minecraft.client.data.models.model.*;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.item.properties.select.DisplayContext;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.NotNull;
import twilightforest.TwilightForestMod;
import twilightforest.block.*;
import twilightforest.client.model.block.aurorablock.NoiseVaryingModelBuilder;
import twilightforest.client.model.block.connected.ConnectedTextureBuilder;
import twilightforest.client.model.block.forcefield.ForceFieldModel;
import twilightforest.client.model.block.forcefield.ForceFieldModelBuilder;
import twilightforest.client.renderer.block.JarRenderer;
import twilightforest.client.renderer.special.*;
import twilightforest.datagen.assets.models.TFExtendedModelTemplates;
import twilightforest.datagen.assets.models.TFModelTemplates;
import twilightforest.datagen.assets.models.TFTextureMapping;
import twilightforest.datagen.assets.models.TFTextureSlot;
import twilightforest.enums.BossVariant;
import twilightforest.enums.HugeLilypadPiece;
import twilightforest.enums.NagastoneVariant;
import twilightforest.enums.WebShape;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFItems;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public abstract class BlockModelBuilders extends WoodBlockBuilders {

	public BlockModelBuilders(Consumer<BlockStateGenerator> stateOutput, ItemModelOutput itemOutput, BiConsumer<ResourceLocation, ModelInstance> modelOutput) {
		super(stateOutput, itemOutput, modelOutput);
	}

	public void generateTrophy(TrophyBlock floor, TrophyWallBlock wall, ItemModel.Unbaked backplate) {
		this.generateTrophy(floor, wall, backplate, "template_trophy");
	}

	public void generateTrophy(TrophyBlock floor, TrophyWallBlock wall, ItemModel.Unbaked backplate, String existingTrophy) {
		ResourceLocation template = ModelLocationUtils.decorateBlockModelLocation("skull");
		this.blockStateOutput.accept(createSimpleBlock(floor, template));
		this.blockStateOutput.accept(createSimpleBlock(wall, template));
		var itemTrophy = ItemModelUtils.specialModel(ModelLocationUtils.decorateItemModelLocation("twilightforest:" + existingTrophy), new TrophySpecialRenderer.Unbaked(floor.getVariant()));
		this.itemModelOutput.accept(floor.asItem(), ItemModelUtils.select(new DisplayContext(), itemTrophy,
			ItemModelUtils.when(ItemDisplayContext.GUI, ItemModelUtils.composite(backplate, itemTrophy))));
	}

	public void generateSkullCandle(AbstractSkullCandleBlock floor, AbstractSkullCandleBlock wall) {
		ResourceLocation template = ModelLocationUtils.decorateBlockModelLocation("skull");
		this.blockStateOutput.accept(createSimpleBlock(floor, template));
		this.blockStateOutput.accept(createSimpleBlock(wall, template));
		this.itemModelOutput.accept(floor.asItem(), ItemModelUtils.specialModel(TwilightForestMod.prefix("item/template_skull_candle"), new SkullCandleSpecialRenderer.Unbaked(floor.getType())));
	}

	public void spawner(Block block, String texture) {
		TextureMapping texturemapping = TextureMapping.cube(TwilightForestMod.prefix(texture));
		this.blockStateOutput.accept(createSimpleBlock(block, ModelTemplates.CUBE_ALL_INNER_FACES.extend().renderType("cutout").build().create(block, texturemapping, this.modelOutput)));
		this.generateBlockItem(block);
	}

	public void basicCtmBlock(Block block) {
		this.blockStateOutput.accept(createSimpleBlock(block, TFModelTemplates.CTM_NO_BASE.extend().customLoader(ConnectedTextureBuilder::new, builder -> builder.connectsTo(block)).build().create(block, TFTextureMapping.ctmBlock(block), this.modelOutput)));
		this.generateBlockItem(block);
	}

	public void castleDoor(Block block, int tint) {
		Function<Boolean, ResourceLocation> door = bool -> TFModelTemplates.CTM.extend().customLoader(ConnectedTextureBuilder::new, builder -> builder.connectsTo(TFBlocks.BLUE_CASTLE_DOOR.get(), TFBlocks.PINK_CASTLE_DOOR.get(), TFBlocks.VIOLET_CASTLE_DOOR.get(), TFBlocks.YELLOW_CASTLE_DOOR.get()).setOverlayEmissivity(15).setOverlayTintIndex(0)).renderType("cutout").build().createWithSuffix(block, bool ? "_vanished" : "", TFTextureMapping.ctmBlock(TwilightForestMod.prefix("block/castle_door" + (bool ? "_vanished" : "")), TwilightForestMod.prefix("block/castle_door_runes")), this.modelOutput);
		this.blockStateOutput.accept(MultiVariantGenerator.multiVariant(block).with(PropertyDispatch.property(CastleDoorBlock.VANISHED).select(true, Variant.variant().with(VariantProperties.MODEL, door.apply(true))).select(false, Variant.variant().with(VariantProperties.MODEL, door.apply(false)))));
		this.registerSimpleTintedItemModel(block, BuiltInRegistries.BLOCK.getKey(block).withPrefix("block/"), ItemModelUtils.constantTint(tint));
	}

	public void giantBlock(Block block, TextureMapping mapping) {
		this.blockStateOutput.accept(createSimpleBlock(block, TFModelTemplates.GIANT_BLOCK.create(block, mapping, this.modelOutput)));
		this.generateGiantBlockItem(block, mapping);
	}

	public void generateGiantBlockItem(Block giantBlock, TextureMapping mapping) {
		ItemModel.Unbaked base = ItemModelUtils.plainModel(TFModelTemplates.GIANT_BLOCK_BASE.createWithSuffix(giantBlock, "_item", mapping, this.modelOutput));
		ItemModel.Unbaked gui = ItemModelUtils.plainModel(TFModelTemplates.GIANT_BLOCK_GUI.createWithSuffix(giantBlock, "_gui", mapping, this.modelOutput));
		this.itemModelOutput.accept(giantBlock.asItem(), ItemModelUtils.select(new DisplayContext(), base, ItemModelUtils.when(ItemDisplayContext.GUI, gui)));
	}

	public void giantBlock(Block block, String renderType, TextureMapping mapping, int tint) {
		this.blockStateOutput.accept(createSimpleBlock(block, TFModelTemplates.GIANT_BLOCK.extend().renderType(renderType).build().create(block, mapping, this.modelOutput)));
		this.generateGiantBlockItem(block, mapping, tint);
	}

	public void generateGiantBlockItem(Block giantBlock, TextureMapping mapping, int tint) {
		ItemModel.Unbaked base = ItemModelUtils.tintedModel(TFModelTemplates.GIANT_BLOCK_BASE.createWithSuffix(giantBlock, "_item", mapping, this.modelOutput), ItemModelUtils.constantTint(tint));
		ItemModel.Unbaked gui = ItemModelUtils.tintedModel(TFModelTemplates.GIANT_BLOCK_GUI.createWithSuffix(giantBlock, "_gui", mapping, this.modelOutput), ItemModelUtils.constantTint(tint));
		this.itemModelOutput.accept(giantBlock.asItem(), ItemModelUtils.select(new DisplayContext(), base, ItemModelUtils.when(ItemDisplayContext.GUI, gui)));
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
		this.blockStateOutput.accept(createStairs(block, inner, straight, outer));
		this.itemModelOutput.accept(block.asItem(), ItemModelUtils.plainModel(straight));
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

	public void wroughtIronFence() {
		Block block = TFBlocks.WROUGHT_IRON_FENCE.get();
		ResourceLocation post = ModelLocationUtils.getModelLocation(block, "_post");
		ResourceLocation capped = ModelLocationUtils.getModelLocation(block, "_post_capped");
		ResourceLocation full = ModelLocationUtils.getModelLocation(block, "_full");
		ResourceLocation top = ModelLocationUtils.getModelLocation(block, "_top");
		ResourceLocation middle = ModelLocationUtils.getModelLocation(block, "_middle");
		ResourceLocation bottom = ModelLocationUtils.getModelLocation(block, "_bottom");

		this.blockStateOutput.accept(MultiPartGenerator.multiPart(block)
			.with(Condition.condition().term(WroughtIronFenceBlock.POST, WroughtIronFenceBlock.PostState.POST), Variant.variant().with(VariantProperties.MODEL, post))
			.with(Condition.condition().term(WroughtIronFenceBlock.POST, WroughtIronFenceBlock.PostState.CAPPED), Variant.variant().with(VariantProperties.MODEL, capped))

			.with(Condition.condition().term(WroughtIronFenceBlock.NORTH_FENCE, WroughtIronFenceBlock.FenceSide.FULL), Variant.variant().with(VariantProperties.MODEL, full))
			.with(Condition.condition().term(WroughtIronFenceBlock.NORTH_FENCE, WroughtIronFenceBlock.FenceSide.TOP), Variant.variant().with(VariantProperties.MODEL, top))
			.with(Condition.condition().term(WroughtIronFenceBlock.NORTH_FENCE, WroughtIronFenceBlock.FenceSide.MIDDLE), Variant.variant().with(VariantProperties.MODEL, middle))
			.with(Condition.condition().term(WroughtIronFenceBlock.NORTH_FENCE, WroughtIronFenceBlock.FenceSide.BOTTOM), Variant.variant().with(VariantProperties.MODEL, bottom))

			.with(Condition.condition().term(WroughtIronFenceBlock.EAST_FENCE, WroughtIronFenceBlock.FenceSide.FULL), Variant.variant().with(VariantProperties.MODEL, full).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
			.with(Condition.condition().term(WroughtIronFenceBlock.EAST_FENCE, WroughtIronFenceBlock.FenceSide.TOP), Variant.variant().with(VariantProperties.MODEL, top).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
			.with(Condition.condition().term(WroughtIronFenceBlock.EAST_FENCE, WroughtIronFenceBlock.FenceSide.MIDDLE), Variant.variant().with(VariantProperties.MODEL, middle).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
			.with(Condition.condition().term(WroughtIronFenceBlock.EAST_FENCE, WroughtIronFenceBlock.FenceSide.BOTTOM), Variant.variant().with(VariantProperties.MODEL, bottom).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))

			.with(Condition.condition().term(WroughtIronFenceBlock.SOUTH_FENCE, WroughtIronFenceBlock.FenceSide.FULL), Variant.variant().with(VariantProperties.MODEL, full).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
			.with(Condition.condition().term(WroughtIronFenceBlock.SOUTH_FENCE, WroughtIronFenceBlock.FenceSide.TOP), Variant.variant().with(VariantProperties.MODEL, top).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
			.with(Condition.condition().term(WroughtIronFenceBlock.SOUTH_FENCE, WroughtIronFenceBlock.FenceSide.MIDDLE), Variant.variant().with(VariantProperties.MODEL, middle).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
			.with(Condition.condition().term(WroughtIronFenceBlock.SOUTH_FENCE, WroughtIronFenceBlock.FenceSide.BOTTOM), Variant.variant().with(VariantProperties.MODEL, bottom).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))

			.with(Condition.condition().term(WroughtIronFenceBlock.WEST_FENCE, WroughtIronFenceBlock.FenceSide.FULL), Variant.variant().with(VariantProperties.MODEL, full).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
			.with(Condition.condition().term(WroughtIronFenceBlock.WEST_FENCE, WroughtIronFenceBlock.FenceSide.TOP), Variant.variant().with(VariantProperties.MODEL, top).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
			.with(Condition.condition().term(WroughtIronFenceBlock.WEST_FENCE, WroughtIronFenceBlock.FenceSide.MIDDLE), Variant.variant().with(VariantProperties.MODEL, middle).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
			.with(Condition.condition().term(WroughtIronFenceBlock.WEST_FENCE, WroughtIronFenceBlock.FenceSide.BOTTOM), Variant.variant().with(VariantProperties.MODEL, bottom).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
		);

		this.itemModelOutput.accept(block.asItem(), ItemModelUtils.plainModel(ModelLocationUtils.getModelLocation(block.asItem())));
	}

	public void terrorcotta() {
		this.rotationallySpecialColumn(TFBlocks.TERRORCOTTA_ARCS.get());

		PropertyDispatch.C1<Direction> directionDispach = PropertyDispatch.property(GlazedTerracottaBlock.FACING);
		boolean firstCurve = true;
		for (Direction direction : new Direction[]{Direction.SOUTH, Direction.NORTH, Direction.WEST, Direction.EAST}) {
			ResourceLocation location = this.makeTerrorcottaCurvesModel("terrorcotta_curves", direction.get2DDataValue());
			directionDispach = directionDispach.select(direction, Variant.variant().with(VariantProperties.MODEL, location));
			if (firstCurve) {
				this.itemModelOutput.accept(TFBlocks.TERRORCOTTA_CURVES.asItem(), ItemModelUtils.plainModel(location));
				firstCurve = false;
			}
		}
		this.blockStateOutput.accept(MultiVariantGenerator.multiVariant(TFBlocks.TERRORCOTTA_CURVES.get()).with(directionDispach));

		ResourceLocation rotated = this.makeTerrorcottaLinesModel("terrorcotta_lines", true);
		ResourceLocation unRotated = this.makeTerrorcottaLinesModel("terrorcotta_lines", false);

		this.blockStateOutput.accept(MultiVariantGenerator.multiVariant(TFBlocks.TERRORCOTTA_LINES.get()).with(
			PropertyDispatch.property(BinaryRotatedBlock.ROTATED)
				.select(true, Variant.variant().with(VariantProperties.MODEL, rotated))
				.select(false, Variant.variant().with(VariantProperties.MODEL, unRotated))
		));

		this.itemModelOutput.accept(TFBlocks.TERRORCOTTA_LINES.asItem(), ItemModelUtils.plainModel(rotated));
	}

	private ResourceLocation makeTerrorcottaCurvesModel(String type, int rotation) {
		TextureMapping mapping = TextureMapping.cube(TFBlocks.TERRORCOTTA_CURVES.get())
			.put(TextureSlot.UP, TwilightForestMod.prefix("block/" + type + curvesSuffixForFacing(rotation, Direction.UP)))
			.put(TextureSlot.DOWN, TwilightForestMod.prefix("block/" + type + curvesSuffixForFacing(rotation, Direction.DOWN)))
			.put(TextureSlot.SOUTH, TwilightForestMod.prefix("block/" + type + curvesSuffixForFacing(rotation, Direction.SOUTH)))
			.put(TextureSlot.NORTH, TwilightForestMod.prefix("block/" + type + curvesSuffixForFacing(rotation, Direction.NORTH)))
			.put(TextureSlot.WEST, TwilightForestMod.prefix("block/" + type + curvesSuffixForFacing(rotation, Direction.WEST)))
			.put(TextureSlot.EAST, TwilightForestMod.prefix("block/" + type + curvesSuffixForFacing(rotation, Direction.EAST)))
			.put(TextureSlot.PARTICLE, TwilightForestMod.prefix("block/" + type + "_a"));

		return TFModelTemplates.CUBE.create(TwilightForestMod.prefix("block/" + type + "_" + (rotation * 90)), mapping, this.modelOutput);
	}

	@NotNull
	private static String curvesSuffixForFacing(int blockRotation, Direction blockFace) {
		int rotationForFace = switch (blockFace) {
			case UP -> 2 - blockRotation;
			case DOWN -> 3 + blockRotation;
			case SOUTH -> switch (blockRotation) {
				case 3 -> 0;
				case 2 -> 3;
				case 1 -> 1;
				default -> 2;
			};
			case WEST -> switch (blockRotation) {
				case 3 -> 1;
				case 2 -> 3;
				case 1 -> 0;
				default -> 2;
			};
			case NORTH -> switch (blockRotation) {
				case 3 -> 3;
				case 2 -> 0;
				case 1 -> 2;
				default -> 1;
			};
			case EAST -> switch (blockRotation) {
				case 3 -> 2;
				case 2 -> 0;
				case 1 -> 3;
				default -> 1;
			};
		};

		return switch (Math.floorMod(rotationForFace, 4)) {
			case 3 -> "_d";
			case 2 -> "_c";
			case 1 -> "_b";
			default -> "_a";
		};
	}

	private ResourceLocation makeTerrorcottaLinesModel(String type, boolean rotated) {
		TextureMapping mapping = TextureMapping.cube(TFBlocks.TERRORCOTTA_CURVES.get())
			.put(TextureSlot.UP, TwilightForestMod.prefix("block/" + type + linesSuffixForFacing(rotated, Direction.UP)))
			.put(TextureSlot.DOWN, TwilightForestMod.prefix("block/" + type + linesSuffixForFacing(rotated, Direction.DOWN)))
			.put(TextureSlot.SOUTH, TwilightForestMod.prefix("block/" + type + linesSuffixForFacing(rotated, Direction.SOUTH)))
			.put(TextureSlot.NORTH, TwilightForestMod.prefix("block/" + type + linesSuffixForFacing(rotated, Direction.NORTH)))
			.put(TextureSlot.WEST, TwilightForestMod.prefix("block/" + type + linesSuffixForFacing(rotated, Direction.WEST)))
			.put(TextureSlot.EAST, TwilightForestMod.prefix("block/" + type + linesSuffixForFacing(rotated, Direction.EAST)))
			.put(TextureSlot.PARTICLE, TwilightForestMod.prefix("block/" + type + "_a"));

		return TFModelTemplates.CUBE.create(TwilightForestMod.prefix("block/" + type + "_" + (rotated ? 90 : 0)), mapping, this.modelOutput);
	}

	public void makeJars() {
		TextureMapping spawnerMapping = TextureMapping.cube(TFBlocks.MASON_JAR.get())
			.put(TextureSlot.TOP, TwilightForestMod.prefix("block/jar_top"))
			.put(TextureSlot.BOTTOM, TwilightForestMod.prefix("block/jar_bottom"))
			.put(TextureSlot.SIDE, TwilightForestMod.prefix("block/jar_side"))
			.put(TextureSlot.PARTICLE, TwilightForestMod.prefix("block/jar_side"))
			.put(TFTextureSlot.SOIL, ResourceLocation.withDefaultNamespace("block/composter_compost"))
			.put(TFTextureSlot.PLANT, ResourceLocation.withDefaultNamespace("block/poppy"));

		ResourceLocation spawnerLocation = TFExtendedModelTemplates.FIREFLY_PARTICLE_SPAWNER.create(TwilightForestMod.prefix("block/" + TFBlocks.FIREFLY_SPAWNER.getId().getPath()), spawnerMapping, this.modelOutput);
		this.blockStateOutput.accept(createSimpleBlock(TFBlocks.FIREFLY_SPAWNER.get(), spawnerLocation));
		this.itemModelOutput.accept(TFBlocks.FIREFLY_SPAWNER.get().asItem(), ItemModelUtils.plainModel(spawnerLocation));

		TextureMapping mapping = TextureMapping.cube(TFBlocks.MASON_JAR.get())
			.put(TextureSlot.TOP, TwilightForestMod.prefix("block/jar_top"))
			.put(TextureSlot.BOTTOM, TwilightForestMod.prefix("block/jar_bottom"))
			.put(TextureSlot.SIDE, TwilightForestMod.prefix("block/jar_side"))
			.put(TextureSlot.PARTICLE, TwilightForestMod.prefix("block/jar_side"));

		ResourceLocation location = TFExtendedModelTemplates.MASON_JAR.create(TwilightForestMod.prefix("block/" + TFBlocks.MASON_JAR.getId().getPath()), mapping, this.modelOutput);
		this.blockStateOutput.accept(createSimpleBlock(TFBlocks.MASON_JAR.get(), location));
		this.blockStateOutput.accept(createSimpleBlock(TFBlocks.CICADA_JAR.get(), location));
		this.blockStateOutput.accept(createSimpleBlock(TFBlocks.FIREFLY_JAR.get(), location));
		this.blockStateOutput.accept(createSimpleBlock(TFBlocks.WEBWORM_JAR.get(), location));

		this.itemModelOutput.accept(TFItems.MASON_JAR.get(), ItemModelUtils.composite(ItemModelUtils.plainModel(location), ItemModelUtils.specialModel(location, new MasonJarSpecialRenderer.Unbaked(TFBlocks.TWILIGHT_OAK_LOG.asItem()))));
		this.itemModelOutput.accept(TFItems.FIREFLY_JAR.get(), ItemModelUtils.composite(ItemModelUtils.plainModel(location), ItemModelUtils.specialModel(location, new MasonJarSpecialRenderer.Unbaked(TFBlocks.TWILIGHT_OAK_LOG.asItem()))));
		this.itemModelOutput.accept(TFItems.CICADA_JAR.get(), ItemModelUtils.composite(ItemModelUtils.plainModel(location), ItemModelUtils.specialModel(location, new MasonJarSpecialRenderer.Unbaked(TFBlocks.CANOPY_LOG.asItem()))));
		this.itemModelOutput.accept(TFItems.WEBWORM_JAR.get(), ItemModelUtils.composite(ItemModelUtils.plainModel(location), ItemModelUtils.specialModel(location, new MasonJarSpecialRenderer.Unbaked(TFBlocks.VEILWOOD_LOG.asItem()))));

		for (JarRenderer.LidResource lid : JarRenderer.LID_LOCATION_LIST.get()) {
			ResourceLocation item = lid.resourceLocation();
			String name = item.getPath();

			if (lid.lid() == Items.PUMPKIN) {
				TextureMapping lidMapping = TextureMapping.cube(TFBlocks.MASON_JAR.get())
					.put(TextureSlot.SIDE, ResourceLocation.withDefaultNamespace("block/pumpkin_side"))
					.put(TextureSlot.END, ResourceLocation.withDefaultNamespace("block/pumpkin_top"));

				TFModelTemplates.JAR_LID.create(TwilightForestMod.prefix("block/lid/" + name), lidMapping, this.modelOutput);
				continue;
			}
			if (lid.customPath() != null) name = lid.customPath();
			TextureMapping lidMapping = TextureMapping.cube(TFBlocks.MASON_JAR.get())
				.put(TextureSlot.SIDE, ResourceLocation.fromNamespaceAndPath(item.getNamespace(), "block/" + item.getPath()))
				.put(TextureSlot.END, ResourceLocation.fromNamespaceAndPath(item.getNamespace(), "block/" + item.getPath() + "_top"));

			TFModelTemplates.JAR_LID.create(TwilightForestMod.prefix("block/lid/" + name), lidMapping, this.modelOutput);
		}
	}

	@NotNull
	private static String linesSuffixForFacing(boolean blockRotation, Direction blockFace) {
		Vec3i normal = blockFace.getUnitVec3i();
		int axisDirection = normal.getX() + normal.getY() + normal.getZ();
		// Biblically accurate XOR
		return axisDirection > 0 == ((blockFace.getAxis() == Direction.Axis.Z) != blockRotation) ? "_a" : "_b";
	}

	public void rotationallySpecialColumn(Block block) {
		ResourceLocation sideA = TextureMapping.getBlockTexture(block, "_side_a");
		ResourceLocation sideB = TextureMapping.getBlockTexture(block, "_side_b");
		ResourceLocation end = TextureMapping.getBlockTexture(block, "_end");

		ResourceLocation xModel = TFModelTemplates.CUBE_COLUMN_ROTATIONALLY_SPECIAL_X.create(block, TextureMapping.cube(block).put(TextureSlot.END, end).put(TFTextureSlot.SIDE_A, sideA).put(TFTextureSlot.SIDE_B, sideB), this.modelOutput);
		ResourceLocation yModel = TFModelTemplates.CUBE_COLUMN.create(block, TextureMapping.cube(block).put(TextureSlot.END, end).put(TextureSlot.SIDE, sideA), this.modelOutput);
		ResourceLocation zModel = TFModelTemplates.CUBE_COLUMN_ROTATIONALLY_SPECIAL_Z.create(block, TextureMapping.cube(block).put(TextureSlot.END, end).put(TFTextureSlot.SIDE_A, sideA).put(TFTextureSlot.SIDE_B, sideB), this.modelOutput);

		this.blockStateOutput.accept(MultiVariantGenerator.multiVariant(block).with(
			PropertyDispatch.property(RotatedPillarBlock.AXIS)
				.select(Direction.Axis.X, Variant.variant().with(VariantProperties.MODEL, xModel))
				.select(Direction.Axis.Y, Variant.variant().with(VariantProperties.MODEL, yModel))
				.select(Direction.Axis.Z, Variant.variant().with(VariantProperties.MODEL, zModel))
		));
		this.itemModelOutput.accept(block.asItem(), ItemModelUtils.plainModel(yModel));
	}

	protected void web() {
		TextureMapping flat = TFExtendedModelTemplates.allAndParticle(TwilightForestMod.prefix("block/hanging_web_flat"));
		ResourceLocation flatWeb = TFExtendedModelTemplates.WEB_FLAT.createWithSuffix(TFBlocks.HANGING_WEB.get(), "_flat", flat, this.modelOutput);

		TextureMapping hanging = TFExtendedModelTemplates.allAndParticle(TwilightForestMod.prefix("block/hanging_web"));
		ResourceLocation hangingWeb = TFExtendedModelTemplates.HANGING_WEB.createWithSuffix(TFBlocks.HANGING_WEB.get(), "_hanging", hanging, this.modelOutput);

		TextureMapping altHanging = TFExtendedModelTemplates.allAndParticle(TwilightForestMod.prefix("block/hanging_web_2"));
		ResourceLocation altHangingWeb = TFExtendedModelTemplates.HANGING_WEB.createWithSuffix(TFBlocks.HANGING_WEB.get(), "_hanging_alt", altHanging, this.modelOutput);

		TextureMapping shortHanging = TFExtendedModelTemplates.allAndParticle(TwilightForestMod.prefix("block/hanging_web_short"));
		ResourceLocation shortHangingWeb = TFExtendedModelTemplates.HANGING_WEB.createWithSuffix(TFBlocks.HANGING_WEB.get(), "_short", shortHanging, this.modelOutput);

		TextureMapping altShortHanging = TFExtendedModelTemplates.allAndParticle(TwilightForestMod.prefix("block/hanging_web_short_2"));
		ResourceLocation altShortHangingWeb = TFExtendedModelTemplates.HANGING_WEB.createWithSuffix(TFBlocks.HANGING_WEB.get(), "_short_alt", altShortHanging, this.modelOutput);

		this.blockStateOutput.accept(
			MultiPartGenerator.multiPart(TFBlocks.HANGING_WEB.get())
				// UP
				.with(
					Condition.condition().term(HangingWebBlock.UP, true),
					Variant.variant().with(VariantProperties.MODEL, flatWeb)
						.with(VariantProperties.WEIGHT, 1).with(VariantProperties.X_ROT, VariantProperties.Rotation.R180),
					Variant.variant().with(VariantProperties.MODEL, flatWeb)
						.with(VariantProperties.WEIGHT, 1).with(VariantProperties.X_ROT, VariantProperties.Rotation.R180).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90),
					Variant.variant().with(VariantProperties.MODEL, flatWeb)
						.with(VariantProperties.WEIGHT, 1).with(VariantProperties.X_ROT, VariantProperties.Rotation.R180).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180),
					Variant.variant().with(VariantProperties.MODEL, flatWeb)
						.with(VariantProperties.WEIGHT, 1).with(VariantProperties.X_ROT, VariantProperties.Rotation.R180).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270)
				)

				// DOWN
				.with(
					Condition.condition().term(HangingWebBlock.DOWN, true),
					Variant.variant().with(VariantProperties.MODEL, flatWeb)
						.with(VariantProperties.WEIGHT, 1),
					Variant.variant().with(VariantProperties.MODEL, flatWeb)
						.with(VariantProperties.WEIGHT, 1).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90),
					Variant.variant().with(VariantProperties.MODEL, flatWeb)
						.with(VariantProperties.WEIGHT, 1).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180),
					Variant.variant().with(VariantProperties.MODEL, flatWeb)
						.with(VariantProperties.WEIGHT, 1).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270)
				)

				// SOUTH
				.with(
					Condition.condition().term(HangingWebBlock.SOUTH, WebShape.SHORT),
					Variant.variant().with(VariantProperties.MODEL, shortHangingWeb)
						.with(VariantProperties.WEIGHT, 2).with(VariantProperties.X_ROT, VariantProperties.Rotation.R270).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180),
					Variant.variant().with(VariantProperties.MODEL, altShortHangingWeb)
						.with(VariantProperties.WEIGHT, 2).with(VariantProperties.X_ROT, VariantProperties.Rotation.R270).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180)
				)
				.with(
					Condition.condition().term(HangingWebBlock.SOUTH, WebShape.TALL),
					Variant.variant().with(VariantProperties.MODEL, hangingWeb)
						.with(VariantProperties.WEIGHT, 2).with(VariantProperties.X_ROT, VariantProperties.Rotation.R270).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180),
					Variant.variant().with(VariantProperties.MODEL, altHangingWeb)
						.with(VariantProperties.WEIGHT, 2).with(VariantProperties.X_ROT, VariantProperties.Rotation.R270).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180)
				)

				// NORTH
				.with(
					Condition.condition().term(HangingWebBlock.NORTH, WebShape.SHORT),
					Variant.variant().with(VariantProperties.MODEL, shortHangingWeb)
						.with(VariantProperties.WEIGHT, 2).with(VariantProperties.X_ROT, VariantProperties.Rotation.R270),
					Variant.variant().with(VariantProperties.MODEL, altShortHangingWeb)
						.with(VariantProperties.WEIGHT, 2).with(VariantProperties.X_ROT, VariantProperties.Rotation.R270)
				)
				.with(
					Condition.condition().term(HangingWebBlock.NORTH, WebShape.TALL),
					Variant.variant().with(VariantProperties.MODEL, hangingWeb)
						.with(VariantProperties.WEIGHT, 2).with(VariantProperties.X_ROT, VariantProperties.Rotation.R270),
					Variant.variant().with(VariantProperties.MODEL, altHangingWeb)
						.with(VariantProperties.WEIGHT, 2).with(VariantProperties.X_ROT, VariantProperties.Rotation.R270)
				)

				// WEST
				.with(
					Condition.condition().term(HangingWebBlock.WEST, WebShape.SHORT),
					Variant.variant().with(VariantProperties.MODEL, shortHangingWeb)
						.with(VariantProperties.WEIGHT, 2).with(VariantProperties.X_ROT, VariantProperties.Rotation.R270).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270),
					Variant.variant().with(VariantProperties.MODEL, altShortHangingWeb)
						.with(VariantProperties.WEIGHT, 2).with(VariantProperties.X_ROT, VariantProperties.Rotation.R270).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270)
				)
				.with(
					Condition.condition().term(HangingWebBlock.WEST, WebShape.TALL),
					Variant.variant().with(VariantProperties.MODEL, hangingWeb)
						.with(VariantProperties.WEIGHT, 2).with(VariantProperties.X_ROT, VariantProperties.Rotation.R270).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270),
					Variant.variant().with(VariantProperties.MODEL, altHangingWeb)
						.with(VariantProperties.WEIGHT, 2).with(VariantProperties.X_ROT, VariantProperties.Rotation.R270).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270)
				)

				// EAST
				.with(
					Condition.condition().term(HangingWebBlock.EAST, WebShape.SHORT),
					Variant.variant().with(VariantProperties.MODEL, shortHangingWeb)
						.with(VariantProperties.WEIGHT, 2).with(VariantProperties.X_ROT, VariantProperties.Rotation.R270).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90),
					Variant.variant().with(VariantProperties.MODEL, altShortHangingWeb)
						.with(VariantProperties.WEIGHT, 2).with(VariantProperties.X_ROT, VariantProperties.Rotation.R270).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)
				)
				.with(
					Condition.condition().term(HangingWebBlock.EAST, WebShape.TALL),
					Variant.variant().with(VariantProperties.MODEL, hangingWeb)
						.with(VariantProperties.WEIGHT, 2).with(VariantProperties.X_ROT, VariantProperties.Rotation.R270).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90),
					Variant.variant().with(VariantProperties.MODEL, altHangingWeb)
						.with(VariantProperties.WEIGHT, 2).with(VariantProperties.X_ROT, VariantProperties.Rotation.R270).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)
				)
		);
		this.registerSimpleItemModel(TFBlocks.HANGING_WEB.asItem(), this.createFlatItemModelWithBlockTexture(TFBlocks.HANGING_WEB.asItem(), TFBlocks.HANGING_WEB.get(), "_flat"));

		TextureMapping worm = TextureMapping.cube(TwilightForestMod.prefix("block/hanging_web_flat")).put(TextureSlot.PARTICLE, TextureMapping.getBlockTexture(Blocks.SLIME_BLOCK));
		ResourceLocation wormWeb = TFExtendedModelTemplates.WEB_FLAT.createWithSuffix(TFBlocks.HANGING_WEB.get(), "_worm", worm, this.modelOutput);

		this.blockStateOutput.accept(
			MultiPartGenerator.multiPart(TFBlocks.WEBWORM.get())
				// UP
				.with(
					Condition.condition().term(HangingWebBlock.UP, true),
					Variant.variant().with(VariantProperties.MODEL, wormWeb)
						.with(VariantProperties.WEIGHT, 1).with(VariantProperties.X_ROT, VariantProperties.Rotation.R180),
					Variant.variant().with(VariantProperties.MODEL, wormWeb)
						.with(VariantProperties.WEIGHT, 1).with(VariantProperties.X_ROT, VariantProperties.Rotation.R180).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90),
					Variant.variant().with(VariantProperties.MODEL, wormWeb)
						.with(VariantProperties.WEIGHT, 1).with(VariantProperties.X_ROT, VariantProperties.Rotation.R180).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180),
					Variant.variant().with(VariantProperties.MODEL, wormWeb)
						.with(VariantProperties.WEIGHT, 1).with(VariantProperties.X_ROT, VariantProperties.Rotation.R180).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270)
				)

				// DOWN
				.with(
					Condition.condition().term(HangingWebBlock.DOWN, true),
					Variant.variant().with(VariantProperties.MODEL, wormWeb)
						.with(VariantProperties.WEIGHT, 1),
					Variant.variant().with(VariantProperties.MODEL, wormWeb)
						.with(VariantProperties.WEIGHT, 1).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90),
					Variant.variant().with(VariantProperties.MODEL, wormWeb)
						.with(VariantProperties.WEIGHT, 1).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180),
					Variant.variant().with(VariantProperties.MODEL, wormWeb)
						.with(VariantProperties.WEIGHT, 1).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270)
				)

				// SOUTH
				.with(
					Condition.condition().term(WebwormBlock.SOUTH, true),
					Variant.variant().with(VariantProperties.MODEL, wormWeb).with(VariantProperties.X_ROT, VariantProperties.Rotation.R270).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180)
				)

				// NORTH
				.with(
					Condition.condition().term(WebwormBlock.NORTH, true),
					Variant.variant().with(VariantProperties.MODEL, wormWeb).with(VariantProperties.X_ROT, VariantProperties.Rotation.R270)
				)

				// WEST
				.with(
					Condition.condition().term(WebwormBlock.WEST, true),
					Variant.variant().with(VariantProperties.MODEL, wormWeb).with(VariantProperties.X_ROT, VariantProperties.Rotation.R270).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270)
				)

				// EAST
				.with(
					Condition.condition().term(WebwormBlock.EAST, true),
					Variant.variant().with(VariantProperties.MODEL, wormWeb).with(VariantProperties.X_ROT, VariantProperties.Rotation.R270).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)
				)
		);
		this.itemModelOutput.accept(TFBlocks.WEBWORM.asItem(), ItemModelUtils.specialModel(TwilightForestMod.prefix("item/webworm"), new WebwormSpecialRenderer.Unbaked()));
	}

	public void thorns(Block block, Block potted) {
		TextureMapping mapping = TextureMapping.column(block);
		this.blockStateOutput.accept(createSimpleBlock(potted, TFModelTemplates.POTTED_THORN.create(potted, mapping, this.modelOutput)));

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

	public void forcefield(Block block, int tint) {
		this.blockStateOutput.accept(createSimpleBlock(block, TFModelTemplates.FORCEFIELD.extend().customLoader(ForceFieldModelBuilder::new, builder -> {
			builder.tintAll(0).brightnessOverride(15).disableShade()
			//WEST
			.forceFieldElement().ifState(ForceFieldModel.ExtraDirection.WEST, true).from(0, 7, 7).to(7, 9, 9).face(Direction.WEST).cullface(Direction.WEST).uvs(7, 7, 9, 9).texture("#pane").end()
				.ifElse().from(7, 7, 7).to(9, 9, 9).face(Direction.WEST).uvs(7, 7, 9, 9).texture("#pane").end().end()

				//EAST
				.forceFieldElement().ifState(ForceFieldModel.ExtraDirection.EAST, true).from(9, 7, 7).to(16, 9, 9).face(Direction.EAST).cullface(Direction.EAST).uvs(7, 7, 9, 9).texture("#pane").end()
				.ifElse().from(7, 7, 7).to(9, 9, 9).face(Direction.EAST).uvs(7, 7, 9, 9).texture("#pane").end().end()

				//DOWN
				.forceFieldElement().ifState(ForceFieldModel.ExtraDirection.DOWN, true).from(7, 0, 7).to(9, 7, 9).face(Direction.DOWN).cullface(Direction.DOWN).uvs(7, 7, 9, 9).texture("#pane").end()
				.ifElse().from(7, 7, 7).to(9, 9, 9).face(Direction.DOWN).uvs(7, 7, 9, 9).texture("#pane").end().end()

				//UP
				.forceFieldElement().ifState(ForceFieldModel.ExtraDirection.UP, true).from(7, 9, 7).to(9, 16, 9).face(Direction.UP).cullface(Direction.UP).uvs(7, 7, 9, 9).texture("#pane").end()
				.ifElse().from(7, 7, 7).to(9, 9, 9).face(Direction.UP).uvs(7, 7, 9, 9).texture("#pane").end().end()

				//NORTH
				.forceFieldElement().ifState(ForceFieldModel.ExtraDirection.NORTH, true).from(7, 7, 0).to(9, 9, 7).face(Direction.NORTH).cullface(Direction.NORTH).uvs(7, 7, 9, 9).texture("#pane").end()
				.ifElse().from(7, 7, 7).to(9, 9, 9).face(Direction.NORTH).uvs(7, 7, 9, 9).texture("#pane").end().end()

				//SOUTH
				.forceFieldElement().ifState(ForceFieldModel.ExtraDirection.SOUTH, true).from(7, 7, 9).to(9, 9, 16).face(Direction.SOUTH).cullface(Direction.SOUTH).uvs(7, 7, 9, 9).texture("#pane").end()
				.ifElse().from(7, 7, 7).to(9, 9, 9).face(Direction.SOUTH).uvs(7, 7, 9, 9).texture("#pane").end().end()

				//DOWN WEST
				.forceFieldElement().ifState(ForceFieldModel.ExtraDirection.DOWN_WEST, true).parents(ForceFieldModel.ExtraDirection.DOWN, ForceFieldModel.ExtraDirection.WEST).from(0, 0, 7).to(7, 7, 9)
					.face(Direction.DOWN).cullface(Direction.DOWN).uvs(0, 7, 7, 9).end()
					.face(Direction.WEST).cullface(Direction.WEST).uvs(7, 0, 9, 7).end()
					.face(Direction.NORTH).uvs(0, 0, 7, 7).end()
					.face(Direction.SOUTH).uvs(9, 0, 16, 7).end().faces((direction, face) -> face.texture("#pane"))
				.ifElse().from(7, 0, 7).to(9, 7, 9).parents(ForceFieldModel.ExtraDirection.DOWN).face(Direction.WEST).uvs(7, 0, 9, 7).texture("#pane").end()
				.ifSame().from(0, 7, 7).to(7, 9, 9).parents(ForceFieldModel.ExtraDirection.WEST).face(Direction.DOWN).uvs(0, 7, 7, 9).texture("#pane").end().end()

				//DOWN EAST
				.forceFieldElement().ifState(ForceFieldModel.ExtraDirection.DOWN_EAST, true).parents(ForceFieldModel.ExtraDirection.DOWN, ForceFieldModel.ExtraDirection.EAST).from(9, 0, 7).to(16, 7, 9)
					.face(Direction.DOWN).cullface(Direction.DOWN).uvs(9, 7, 16, 9).end()
					.face(Direction.EAST).cullface(Direction.EAST).uvs(7, 0, 9, 7).end()
					.face(Direction.NORTH).uvs(9, 0, 16, 7).end()
					.face(Direction.SOUTH).uvs(0, 0, 7, 7).end().faces((direction, face) -> face.texture("#pane"))
				.ifElse().from(7, 0, 7).to(9, 7, 9).parents(ForceFieldModel.ExtraDirection.DOWN).face(Direction.EAST).uvs(7, 0, 9, 7).texture("#pane").end()
				.ifSame().from(9, 7, 7).to(16, 9, 9).parents(ForceFieldModel.ExtraDirection.EAST).face(Direction.DOWN).uvs(9, 7, 16, 9).texture("#pane").end().end()

				//DOWN NORTH
				.forceFieldElement().ifState(ForceFieldModel.ExtraDirection.DOWN_NORTH, true).from(7, 0, 0).to(9, 7, 7).parents(ForceFieldModel.ExtraDirection.DOWN, ForceFieldModel.ExtraDirection.NORTH)
					.face(Direction.DOWN).cullface(Direction.DOWN).uvs(7, 0, 9, 7).end()
					.face(Direction.NORTH).cullface(Direction.NORTH).uvs(7, 0, 9, 7).end()
					.face(Direction.WEST).uvs(0, 0, 7, 7).end()
					.face(Direction.EAST).uvs(9, 9, 16, 16).end().faces((direction, face) -> face.texture("#pane"))
				.ifElse().from(7, 0, 7).to(9, 7, 9).parents(ForceFieldModel.ExtraDirection.DOWN).face(Direction.NORTH).uvs(7, 0, 9, 7).texture("#pane").end()
				.ifSame().from(7, 7, 0).to(9, 9, 7).parents(ForceFieldModel.ExtraDirection.NORTH).face(Direction.DOWN).uvs(7, 0, 9, 7).texture("#pane").end().end()

				//DOWN SOUTH
				.forceFieldElement().ifState(ForceFieldModel.ExtraDirection.DOWN_SOUTH, true).from(7, 0, 9).to(9, 7, 16).parents(ForceFieldModel.ExtraDirection.DOWN, ForceFieldModel.ExtraDirection.SOUTH)
					.face(Direction.DOWN).cullface(Direction.DOWN).uvs(7, 9, 9, 16).end()
					.face(Direction.SOUTH).cullface(Direction.SOUTH).uvs(7, 0, 9, 7).end()
					.face(Direction.WEST).uvs(9, 0, 16, 7).end()
					.face(Direction.EAST).uvs(0, 0, 7, 7).end().faces((direction, face) -> face.texture("#pane"))
				.ifElse().from(7, 0, 7).to(9, 7, 9).parents(ForceFieldModel.ExtraDirection.DOWN).face(Direction.SOUTH).uvs(7, 0, 9, 7).texture("#pane").end()
				.ifSame().from(7, 7, 9).to(9, 9, 16).parents(ForceFieldModel.ExtraDirection.SOUTH).face(Direction.DOWN).uvs(7, 9, 9, 16).texture("#pane").end().end()

				//UP WEST
				.forceFieldElement().ifState(ForceFieldModel.ExtraDirection.UP_WEST, true).from(0, 9, 7).to(7, 16, 9).parents(ForceFieldModel.ExtraDirection.UP, ForceFieldModel.ExtraDirection.WEST)
					.face(Direction.UP).cullface(Direction.UP).uvs(0, 7, 7, 9).end()
					.face(Direction.WEST).cullface(Direction.WEST).uvs(7, 9, 9, 16).end()
					.face(Direction.NORTH).uvs(0, 9, 7, 16).end()
					.face(Direction.SOUTH).uvs(9, 9, 16, 16).end().faces((direction, face) -> face.texture("#pane"))
				.ifElse().from(7, 9, 7).to(9, 16, 9).parents(ForceFieldModel.ExtraDirection.UP).face(Direction.WEST).uvs(7, 9, 9, 16).texture("#pane").end()
				.ifSame().from(0, 7, 7).to(7, 9, 9).parents(ForceFieldModel.ExtraDirection.WEST).face(Direction.UP).uvs(0, 7, 7, 9).texture("#pane").end().end()

				//UP EAST
				.forceFieldElement().ifState(ForceFieldModel.ExtraDirection.UP_EAST, true).from(9, 9, 7).to(16, 16, 9).parents(ForceFieldModel.ExtraDirection.UP, ForceFieldModel.ExtraDirection.EAST)
					.face(Direction.UP).cullface(Direction.UP).uvs(9, 7, 16, 9).end()
					.face(Direction.EAST).cullface(Direction.EAST).uvs(7, 9, 9, 16).end()
					.face(Direction.NORTH).uvs(9, 9, 16, 16).end()
					.face(Direction.SOUTH).uvs(0, 9, 7, 16).end().faces((direction, face) -> face.texture("#pane"))
				.ifElse().from(7, 9, 7).to(9, 16, 9).parents(ForceFieldModel.ExtraDirection.UP).face(Direction.EAST).uvs(7, 9, 9, 16).texture("#pane").end()
				.ifSame().from(9, 7, 7).to(16, 9, 9).parents(ForceFieldModel.ExtraDirection.EAST).face(Direction.UP).uvs(9, 7, 16, 9).texture("#pane").end().end()

				//UP NORTH
				.forceFieldElement().ifState(ForceFieldModel.ExtraDirection.UP_NORTH, true).from(7, 9, 0).to(9, 16, 7).parents(ForceFieldModel.ExtraDirection.UP, ForceFieldModel.ExtraDirection.NORTH)
					.face(Direction.UP).cullface(Direction.UP).uvs(7, 0, 9, 7).end()
					.face(Direction.NORTH).cullface(Direction.NORTH).uvs(7, 9, 9, 16).end()
					.face(Direction.WEST).uvs(0, 9, 7, 16).end()
					.face(Direction.EAST).uvs(9, 9, 16, 16).end().faces((direction, face) -> face.texture("#pane"))
				.ifElse().from(7, 9, 7).to(9, 16, 9).parents(ForceFieldModel.ExtraDirection.UP).face(Direction.NORTH).uvs(7, 9, 9, 16).texture("#pane").end()
				.ifSame().from(7, 7, 0).to(9, 9, 7).parents(ForceFieldModel.ExtraDirection.NORTH).face(Direction.UP).uvs(7, 0, 9, 7).texture("#pane").end().end()

				//UP SOUTH
				.forceFieldElement().ifState(ForceFieldModel.ExtraDirection.UP_SOUTH, true).from(7, 9, 9).to(9, 16, 16).parents(ForceFieldModel.ExtraDirection.UP, ForceFieldModel.ExtraDirection.SOUTH)
					.face(Direction.UP).cullface(Direction.UP).uvs(7, 9, 9, 16).end()
					.face(Direction.SOUTH).cullface(Direction.SOUTH).uvs(7, 9, 9, 16).end()
					.face(Direction.WEST).uvs(9, 9, 16, 16).end()
					.face(Direction.EAST).uvs(0, 9, 7, 16).end().faces((direction, face) -> face.texture("#pane"))
				.ifElse().from(7, 9, 7).to(9, 16, 9).parents(ForceFieldModel.ExtraDirection.UP).face(Direction.SOUTH).uvs(7, 9, 9, 16).texture("#pane").end()
				.ifSame().from(7, 7, 9).to(9, 9, 16).parents(ForceFieldModel.ExtraDirection.SOUTH).face(Direction.UP).uvs(7, 9, 9, 16).texture("#pane").end().end()

				//NORTH WEST
				.forceFieldElement().ifState(ForceFieldModel.ExtraDirection.NORTH_WEST, true).from(0, 7, 0).to(7, 9, 7).parents(ForceFieldModel.ExtraDirection.NORTH, ForceFieldModel.ExtraDirection.WEST)
					.face(Direction.NORTH).cullface(Direction.NORTH).uvs(0, 7, 7, 9).end()
					.face(Direction.WEST).cullface(Direction.WEST).uvs(9, 7, 16, 9).end()
					.face(Direction.DOWN).uvs(0, 9, 7, 16).end()
					.face(Direction.UP).uvs(9, 9, 16, 16).end().faces((direction, face) -> face.texture("#pane"))
				.ifElse().from(7, 7, 0).to(9, 9, 7).parents(ForceFieldModel.ExtraDirection.NORTH).face(Direction.WEST).uvs(9, 7, 16, 9).texture("#pane").end()
				.ifSame().from(0, 7, 7).to(7, 9, 9).parents(ForceFieldModel.ExtraDirection.WEST).face(Direction.NORTH).uvs(0, 7, 7, 9).texture("#pane").end().end()

				//NORTH EAST
				.forceFieldElement().ifState(ForceFieldModel.ExtraDirection.NORTH_EAST, true).from(9, 7, 0).to(16, 9, 7).parents(ForceFieldModel.ExtraDirection.NORTH, ForceFieldModel.ExtraDirection.EAST)
					.face(Direction.NORTH).cullface(Direction.NORTH).uvs(9, 7, 16, 9).end()
					.face(Direction.EAST).cullface(Direction.EAST).uvs(0, 7, 7, 9).end()
					.face(Direction.DOWN).uvs(9, 9, 16, 16).end()
					.face(Direction.UP).uvs(0, 9, 7, 16).end().faces((direction, face) -> face.texture("#pane"))
				.ifElse().from(7, 7, 0).to(9, 9, 7).parents(ForceFieldModel.ExtraDirection.NORTH).face(Direction.EAST).uvs(0, 7, 7, 9).texture("#pane").end()
				.ifSame().from(9, 7, 7).to(16, 9, 9).parents(ForceFieldModel.ExtraDirection.EAST).face(Direction.NORTH).uvs(9, 7, 16, 9).texture("#pane").end().end()

				//SOUTH WEST
				.forceFieldElement().ifState(ForceFieldModel.ExtraDirection.SOUTH_WEST, true).from(0, 7, 9).to(7, 9, 16).parents(ForceFieldModel.ExtraDirection.SOUTH, ForceFieldModel.ExtraDirection.WEST)
					.face(Direction.SOUTH).cullface(Direction.SOUTH).uvs(0, 7, 7, 9).end()
					.face(Direction.WEST).cullface(Direction.WEST).uvs(9, 7, 16, 9).end()
					.face(Direction.DOWN).uvs(0, 9, 7, 16).end()
					.face(Direction.UP).uvs(9, 9, 16, 16).end().faces((direction, face) -> face.texture("#pane"))
				.ifElse().from(7, 7, 9).to(9, 9, 16).parents(ForceFieldModel.ExtraDirection.SOUTH).face(Direction.WEST).uvs(9, 7, 16, 9).texture("#pane").end()
				.ifSame().from(0, 7, 7).to(7, 9, 9).parents(ForceFieldModel.ExtraDirection.WEST).face(Direction.SOUTH).uvs(0, 7, 7, 9).texture("#pane").end().end()

				//SOUTH EAST
				.forceFieldElement().ifState(ForceFieldModel.ExtraDirection.SOUTH_EAST, true).from(9, 7, 9).to(16, 9, 16).parents(ForceFieldModel.ExtraDirection.SOUTH, ForceFieldModel.ExtraDirection.EAST)
					.face(Direction.SOUTH).cullface(Direction.SOUTH).uvs(0, 7, 7, 9).end()
					.face(Direction.EAST).cullface(Direction.EAST).uvs(9, 7, 16, 9).end()
					.face(Direction.DOWN).uvs(9, 9, 16, 16).end()
					.face(Direction.UP).uvs(0, 9, 7, 16).end().faces((direction, face) -> face.texture("#pane"))
				.ifElse().from(7, 7, 9).to(9, 9, 16).parents(ForceFieldModel.ExtraDirection.SOUTH).face(Direction.EAST).uvs(9, 7, 16, 9).texture("#pane").end()
				.ifSame().from(9, 7, 7).to(16, 9, 9).parents(ForceFieldModel.ExtraDirection.EAST).face(Direction.SOUTH).uvs(0, 7, 7, 9).texture("#pane").end().end();
		}).build().create(block, TFTextureMapping.forcefield(), this.modelOutput)));
		this.itemModelOutput.accept(block.asItem(), ItemModelUtils.tintedModel(ModelTemplates.FLAT_ITEM.extend().renderType("translucent").build().create(ModelLocationUtils.getModelLocation(block.asItem()), TextureMapping.layer0(TwilightForestMod.prefix("block/forcefield")), this.modelOutput), ItemModelUtils.constantTint(tint)));
	}

	public void generatePaneBlock(Block glassBlock, Block paneBlock) {
		TextureMapping mapping = new TextureMapping().put(TextureSlot.PANE, TextureMapping.getBlockTexture(glassBlock)).put(TextureSlot.EDGE, TextureMapping.getBlockTexture(glassBlock));
		ResourceLocation post = ModelTemplates.STAINED_GLASS_PANE_POST.extend().renderType("translucent").build().create(paneBlock, mapping, this.modelOutput);
		ResourceLocation side = ModelTemplates.STAINED_GLASS_PANE_SIDE.extend().renderType("translucent").build().create(paneBlock, mapping, this.modelOutput);
		ResourceLocation sideAlt = ModelTemplates.STAINED_GLASS_PANE_SIDE_ALT.extend().renderType("translucent").build().create(paneBlock, mapping, this.modelOutput);
		ResourceLocation noSide = ModelTemplates.STAINED_GLASS_PANE_NOSIDE.extend().renderType("translucent").build().create(paneBlock, mapping, this.modelOutput);
		ResourceLocation noSideAlt = ModelTemplates.STAINED_GLASS_PANE_NOSIDE_ALT.extend().renderType("translucent").build().create(paneBlock, mapping, this.modelOutput);
		Item item = paneBlock.asItem();
		this.registerSimpleItemModel(item, this.createFlatItemModelWithBlockTexture(item, glassBlock));
		this.blockStateOutput
			.accept(
				MultiPartGenerator.multiPart(paneBlock)
					.with(Variant.variant().with(VariantProperties.MODEL, post))
					.with(Condition.condition().term(BlockStateProperties.NORTH, true), Variant.variant().with(VariantProperties.MODEL, side))
					.with(
						Condition.condition().term(BlockStateProperties.EAST, true),
						Variant.variant().with(VariantProperties.MODEL, side).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)
					)
					.with(Condition.condition().term(BlockStateProperties.SOUTH, true), Variant.variant().with(VariantProperties.MODEL, sideAlt))
					.with(
						Condition.condition().term(BlockStateProperties.WEST, true),
						Variant.variant().with(VariantProperties.MODEL, sideAlt).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)
					)
					.with(Condition.condition().term(BlockStateProperties.NORTH, false), Variant.variant().with(VariantProperties.MODEL, noSide))
					.with(Condition.condition().term(BlockStateProperties.EAST, false), Variant.variant().with(VariantProperties.MODEL, noSideAlt))
					.with(
						Condition.condition().term(BlockStateProperties.SOUTH, false),
						Variant.variant().with(VariantProperties.MODEL, noSideAlt).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)
					)
					.with(
						Condition.condition().term(BlockStateProperties.WEST, false),
						Variant.variant().with(VariantProperties.MODEL, noSide).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270)
					)
			);
	}

	public void generateRuneBlock(Block runeBlock, int tint) {
		Variant[] variants = new Variant[8];
		for (int i = 0; i < 8; i++) {
			variants[i] = Variant.variant().with(VariantProperties.MODEL, TFModelTemplates.CASTLE_RUNE_TEMPLATE.createWithSuffix(runeBlock, "_" + i, TextureMapping.cube(TFBlocks.CASTLE_BRICK.get()).put(TFTextureSlot.RUNE, TwilightForestMod.prefix("block/castleblock_magic_" + i)), this.modelOutput));
		}
		this.blockStateOutput.accept(MultiVariantGenerator.multiVariant(runeBlock, variants));
		this.itemModelOutput.accept(runeBlock.asItem(), ItemModelUtils.tintedModel(ModelLocationUtils.getModelLocation(runeBlock).withSuffix("_0"), ItemModelUtils.constantTint(tint)));
	}

	public void trophyPedestal() {
		BossVariant[][] variantList = new BossVariant[][]{ //face order: north, south, east, west
			new BossVariant[]{BossVariant.NAGA, BossVariant.LICH, BossVariant.UR_GHAST, BossVariant.HYDRA},
			new BossVariant[]{BossVariant.SNOW_QUEEN, BossVariant.NAGA, BossVariant.HYDRA, BossVariant.LICH},
			new BossVariant[]{BossVariant.UR_GHAST, BossVariant.SNOW_QUEEN, BossVariant.LICH, BossVariant.NAGA},
			new BossVariant[]{BossVariant.HYDRA, BossVariant.UR_GHAST, BossVariant.NAGA, BossVariant.SNOW_QUEEN},
			new BossVariant[]{BossVariant.LICH, BossVariant.HYDRA, BossVariant.SNOW_QUEEN, BossVariant.UR_GHAST}
		};
		Block pedestal = TFBlocks.TROPHY_PEDESTAL.get();
		List<Variant> variants = new ArrayList<>();
		List<Variant> activeVariants = new ArrayList<>();
		for (int i = 0; i < variantList.length; i++) {
			ResourceLocation model = TFModelTemplates.TROPHY_PEDESTAL.createWithSuffix(pedestal, i == 0 ? "" : ("_" + i), TFTextureMapping.trophyPedestal(pedestal, false, variantList[i][0], variantList[i][1], variantList[i][2], variantList[i][3]), this.modelOutput);
			ResourceLocation activeModel = TFModelTemplates.TROPHY_PEDESTAL_ACTIVE.createWithSuffix(pedestal, i == 0 ? "_active" : ("_active_" + i), TFTextureMapping.trophyPedestal(pedestal, true, variantList[i][0], variantList[i][1], variantList[i][2], variantList[i][3]), this.modelOutput);

			for (VariantProperties.Rotation rot : VariantProperties.Rotation.values()) {
				variants.add(Variant.variant().with(VariantProperties.MODEL, model).with(VariantProperties.Y_ROT, rot));
				activeVariants.add(Variant.variant().with(VariantProperties.MODEL, activeModel).with(VariantProperties.Y_ROT, rot));
			}
		}
		this.blockStateOutput.accept(MultiVariantGenerator.multiVariant(pedestal).with(PropertyDispatch.property(TrophyPedestalBlock.ACTIVE).select(true, activeVariants).select(false, variants)));
		this.generateBlockItem(pedestal);
	}

	public void ironLadder() {
		MultiPartGenerator model = MultiPartGenerator.multiPart(TFBlocks.IRON_LADDER.get());
		ResourceLocation left = ModelLocationUtils.getModelLocation(TFBlocks.IRON_LADDER.get(), "_left");
		ResourceLocation leftConnect = ModelLocationUtils.getModelLocation(TFBlocks.IRON_LADDER.get(), "_left_connection");
		ResourceLocation right = ModelLocationUtils.getModelLocation(TFBlocks.IRON_LADDER.get(), "_right");
		ResourceLocation rightConnect = ModelLocationUtils.getModelLocation(TFBlocks.IRON_LADDER.get(), "_right_connection");
		for (Direction d : Direction.Plane.HORIZONTAL) {
			model.with(Condition.and(
				Condition.condition().term(IronLadderBlock.LEFT, false),
				Condition.condition().term(LadderBlock.FACING, d)), Variant.variant().with(VariantProperties.MODEL, left).with(VariantProperties.Y_ROT, getYRotationFromDirection(d)));
			model.with(Condition.and(
				Condition.condition().term(IronLadderBlock.LEFT, true),
				Condition.condition().term(LadderBlock.FACING, d)), Variant.variant().with(VariantProperties.MODEL, leftConnect).with(VariantProperties.Y_ROT, getYRotationFromDirection(d)));
			model.with(Condition.and(
				Condition.condition().term(IronLadderBlock.RIGHT, false),
				Condition.condition().term(LadderBlock.FACING, d)), Variant.variant().with(VariantProperties.MODEL, right).with(VariantProperties.Y_ROT, getYRotationFromDirection(d)));
			model.with(Condition.and(
				Condition.condition().term(IronLadderBlock.RIGHT, true),
				Condition.condition().term(LadderBlock.FACING, d)), Variant.variant().with(VariantProperties.MODEL, rightConnect).with(VariantProperties.Y_ROT, getYRotationFromDirection(d)));
		}
		this.blockStateOutput.accept(model);
		this.itemModelOutput.accept(TFBlocks.IRON_LADDER.asItem(), ItemModelUtils.plainModel(ModelLocationUtils.getModelLocation(TFBlocks.IRON_LADDER.asItem())));
	}

	public void createMultifaceBlock(Block mushroomBlock, ResourceLocation inside, boolean invertConditions) {
		ResourceLocation outside = ModelTemplates.SINGLE_FACE.create(mushroomBlock, TextureMapping.defaultTexture(mushroomBlock), this.modelOutput);
		this.blockStateOutput.accept(MultiPartGenerator.multiPart(mushroomBlock)
			.with(Condition.condition().term(BlockStateProperties.NORTH, !invertConditions), Variant.variant().with(VariantProperties.MODEL, outside))
			.with(Condition.condition().term(BlockStateProperties.EAST, !invertConditions), Variant.variant()
				.with(VariantProperties.MODEL, outside)
				.with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)
				.with(VariantProperties.UV_LOCK, true)
			)
			.with(Condition.condition().term(BlockStateProperties.SOUTH, !invertConditions), Variant.variant()
				.with(VariantProperties.MODEL, outside)
				.with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180)
				.with(VariantProperties.UV_LOCK, true)
			)
			.with(Condition.condition().term(BlockStateProperties.WEST, !invertConditions), Variant.variant()
				.with(VariantProperties.MODEL, outside)
				.with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270)
				.with(VariantProperties.UV_LOCK, true)
			)
			.with(Condition.condition().term(BlockStateProperties.UP, !invertConditions), Variant.variant()
				.with(VariantProperties.MODEL, outside)
				.with(VariantProperties.X_ROT, VariantProperties.Rotation.R270)
				.with(VariantProperties.UV_LOCK, true)
			)
			.with(Condition.condition().term(BlockStateProperties.DOWN, !invertConditions), Variant.variant()
				.with(VariantProperties.MODEL, outside)
				.with(VariantProperties.X_ROT, VariantProperties.Rotation.R90)
				.with(VariantProperties.UV_LOCK, true)
			)
			.with(Condition.condition().term(BlockStateProperties.NORTH, invertConditions), Variant.variant().with(VariantProperties.MODEL, inside))
			.with(Condition.condition().term(BlockStateProperties.EAST, invertConditions), Variant.variant()
				.with(VariantProperties.MODEL, inside)
				.with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)
				.with(VariantProperties.UV_LOCK, false)
			)
			.with(Condition.condition().term(BlockStateProperties.SOUTH, invertConditions), Variant.variant()
				.with(VariantProperties.MODEL, inside)
				.with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180)
				.with(VariantProperties.UV_LOCK, false)
			)
			.with(Condition.condition().term(BlockStateProperties.WEST, invertConditions), Variant.variant()
				.with(VariantProperties.MODEL, inside)
				.with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270)
				.with(VariantProperties.UV_LOCK, false)
			)
			.with(Condition.condition().term(BlockStateProperties.UP, invertConditions), Variant.variant()
				.with(VariantProperties.MODEL, inside)
				.with(VariantProperties.X_ROT, VariantProperties.Rotation.R270)
				.with(VariantProperties.UV_LOCK, false)
			)
			.with(Condition.condition().term(BlockStateProperties.DOWN, invertConditions), Variant.variant()
				.with(VariantProperties.MODEL, inside)
				.with(VariantProperties.X_ROT, VariantProperties.Rotation.R90)
				.with(VariantProperties.UV_LOCK, false)
			)
		);
		this.registerSimpleItemModel(mushroomBlock, TexturedModel.CUBE.createWithSuffix(mushroomBlock, "_inventory", this.modelOutput));
	}

	public void generateHugeLilyPad() {
		Block block = TFBlocks.HUGE_LILY_PAD.get();
		ResourceLocation[] models = new ResourceLocation[4];
		for (int i = 0; i < models.length; i++) {
			ResourceLocation texture = ModelLocationUtils.getModelLocation(block, "_" + i);
			models[i] = TFModelTemplates.create(texture.toString(), TextureSlot.TEXTURE).extend().renderType("cutout").parent(ModelLocationUtils.getModelLocation(Blocks.LILY_PAD)).build().create(texture, TextureMapping.defaultTexture(texture), this.modelOutput);
		}

		Map<Direction, Map<HugeLilypadPiece, ResourceLocation>> stateMap = ImmutableMap.of(
			Direction.NORTH, ImmutableMap.of(HugeLilypadPiece.NW, models[0], HugeLilypadPiece.NE, models[1], HugeLilypadPiece.SE, models[2], HugeLilypadPiece.SW, models[3]),
			Direction.WEST, ImmutableMap.of(HugeLilypadPiece.NW, models[1], HugeLilypadPiece.NE, models[2], HugeLilypadPiece.SE, models[3], HugeLilypadPiece.SW, models[0]),
			Direction.SOUTH, ImmutableMap.of(HugeLilypadPiece.NW, models[2], HugeLilypadPiece.NE, models[3], HugeLilypadPiece.SE, models[0], HugeLilypadPiece.SW, models[1]),
			Direction.EAST, ImmutableMap.of(HugeLilypadPiece.NW, models[3], HugeLilypadPiece.NE, models[0], HugeLilypadPiece.SE, models[1], HugeLilypadPiece.SW, models[2])
		);

		this.blockStateOutput.accept(MultiVariantGenerator.multiVariant(block).with(PropertyDispatch.properties(HugeLilyPadBlock.PIECE, HugeLilyPadBlock.FACING).generate((piece, facing) -> Variant.variant().with(VariantProperties.MODEL, stateMap.get(facing).get(piece)).with(VariantProperties.Y_ROT, getYRotationFromDirection(facing)))));
		this.itemModelOutput.accept(block.asItem(), ItemModelUtils.tintedModel(ModelTemplates.FLAT_ITEM.create(ModelLocationUtils.getModelLocation(block.asItem()), TextureMapping.layer0(block), this.modelOutput), ItemModelUtils.constantTint(-9321636)));
	}

	public void generateAuroraBlocks() {
		Block base = TFBlocks.AURORA_BLOCK.get();
		ResourceLocation[] auroras = new ResourceLocation[16];
		for (int i = 0; i < auroras.length; i++) {
			auroras[i] = TFModelTemplates.TINTED_BLOCK.createWithSuffix(TFBlocks.AURORA_BLOCK.get(), "_" + i, TextureMapping.cube(TextureMapping.getBlockTexture(base, i == 0 ? "" : "_" + i)), this.modelOutput);
		}
		this.wrapTintedBlockItem(base, ItemModelUtils.constantTint(-16711758), block -> this.blockStateOutput.accept(MultiVariantGenerator.multiVariant(block, Variant.variant().with(VariantProperties.MODEL, TFModelTemplates.create("block").extend().customLoader(NoiseVaryingModelBuilder::new, builder -> builder.addAll(auroras)).build().create(block, new TextureMapping(), this.modelOutput)))));

		Block pillar = TFBlocks.AURORA_PILLAR.get();
		this.wrapTintedBlockItem(pillar, ItemModelUtils.constantTint(-9181501), block -> this.blockStateOutput.accept(createAxisAlignedPillarBlock(block, TexturedModel.createDefault(block1 -> new TextureMapping()
				.put(TextureSlot.SIDE, TextureMapping.getBlockTexture(block))
				.put(TextureSlot.TOP, TextureMapping.getBlockTexture(block, "_top"))
				.put(TextureSlot.BOTTOM, TextureMapping.getBlockTexture(block, "_top")), TFModelTemplates.TINTED_CUBE_BOTTOM_TOP)
			.create(block, this.modelOutput))));

		Block slab = TFBlocks.AURORA_SLAB.get();
		TextureMapping slabMap = new TextureMapping()
			.put(TextureSlot.SIDE, TextureMapping.getBlockTexture(pillar))
			.put(TextureSlot.TOP, TextureMapping.getBlockTexture(pillar, "_top"))
			.put(TextureSlot.BOTTOM, TextureMapping.getBlockTexture(pillar, "_top"));

		ResourceLocation bottom = TFModelTemplates.TINTED_SLAB_BOTTOM.create(slab, slabMap, this.modelOutput);
		ResourceLocation top = TFModelTemplates.TINTED_SLAB_TOP.create(slab, slabMap, this.modelOutput);
		this.wrapTintedBlockItem(slab, ItemModelUtils.constantTint(-9181501), block -> this.blockStateOutput.accept(createSlab(block, bottom, top, ModelLocationUtils.getModelLocation(pillar))));

		this.wrapTintedBlockItem(TFBlocks.AURORALIZED_GLASS.get(), ItemModelUtils.constantTint(-9181501), block -> this.blockStateOutput.accept(createSimpleBlock(block, TFModelTemplates.CTM_NO_BASE.extend().renderType("translucent").customLoader(ConnectedTextureBuilder::new, builder -> builder.setOverlayTintIndex(0).connectsTo(block)).build().create(block, TFTextureMapping.ctmBlock(block), this.modelOutput))));
	}

	public void createTFChest(Block chestBlock, Block particleBlock, ResourceLocation texture) {
		this.createParticleOnlyBlock(chestBlock, particleBlock);
		Item item = chestBlock.asItem();
		this.itemModelOutput.accept(item, ItemModelUtils.specialModel(ModelTemplates.CHEST_INVENTORY.create(item, TextureMapping.particle(particleBlock), this.modelOutput), new TFChestSpecialRenderer.Unbaked(texture)));
	}

	@Override
	public void createCrossBlock(Block block, PlantType plantType, TextureMapping textureMapping) {
		ResourceLocation resourcelocation = plantType.getCross().extend().renderType("cutout").build().create(block, textureMapping, this.modelOutput);
		this.blockStateOutput.accept(createSimpleBlock(block, resourcelocation));
	}

	@Override
	public List<ResourceLocation> createFloorFireModels(Block fireBlock) {
		ResourceLocation resourcelocation = ModelTemplates.FIRE_FLOOR.extend().renderType("cutout").build()
			.create(ModelLocationUtils.getModelLocation(fireBlock, "_floor0"), TextureMapping.fire0(fireBlock), this.modelOutput);
		ResourceLocation resourcelocation1 = ModelTemplates.FIRE_FLOOR.extend().renderType("cutout").build()
			.create(ModelLocationUtils.getModelLocation(fireBlock, "_floor1"), TextureMapping.fire1(fireBlock), this.modelOutput);
		return ImmutableList.of(resourcelocation, resourcelocation1);
	}

	@Override
	public List<ResourceLocation> createSideFireModels(Block fireBlock) {
		ResourceLocation resourcelocation = ModelTemplates.FIRE_SIDE.extend().renderType("cutout").build()
			.create(ModelLocationUtils.getModelLocation(fireBlock, "_side0"), TextureMapping.fire0(fireBlock), this.modelOutput);
		ResourceLocation resourcelocation1 = ModelTemplates.FIRE_SIDE.extend().renderType("cutout").build()
			.create(ModelLocationUtils.getModelLocation(fireBlock, "_side1"), TextureMapping.fire1(fireBlock), this.modelOutput);
		ResourceLocation resourcelocation2 = ModelTemplates.FIRE_SIDE_ALT.extend().renderType("cutout").build()
			.create(ModelLocationUtils.getModelLocation(fireBlock, "_side_alt0"), TextureMapping.fire0(fireBlock), this.modelOutput);
		ResourceLocation resourcelocation3 = ModelTemplates.FIRE_SIDE_ALT.extend().renderType("cutout").build()
			.create(ModelLocationUtils.getModelLocation(fireBlock, "_side_alt1"), TextureMapping.fire1(fireBlock), this.modelOutput);
		return ImmutableList.of(resourcelocation, resourcelocation1, resourcelocation2, resourcelocation3);
	}

	public static VariantProperties.Rotation getYRotationFromDirection(Direction direction) {
		return switch (direction) {
			case EAST -> VariantProperties.Rotation.R90;
			case SOUTH -> VariantProperties.Rotation.R180;
			case WEST -> VariantProperties.Rotation.R270;
			default -> VariantProperties.Rotation.R0;
		};
	}
}
