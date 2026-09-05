package twilightforest.datagen.helpers.models;

import com.google.common.collect.ImmutableMap;
import com.mojang.math.Quadrant;
import net.minecraft.client.data.models.ItemModelOutput;
import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.client.data.models.blockstates.*;
import net.minecraft.client.data.models.model.*;
import net.minecraft.client.renderer.block.dispatch.Variant;
import net.minecraft.client.renderer.block.dispatch.VariantMutator;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.item.SelectItemModel;
import net.minecraft.client.renderer.item.properties.select.DisplayContext;
import net.minecraft.client.renderer.special.ChestSpecialRenderer;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.client.model.block.CompositeBlockModel;
import net.neoforged.neoforge.client.model.generators.blockstate.CompositeBlockStateModelBuilder;
import net.neoforged.neoforge.client.model.generators.blockstate.CustomBlockStateModelBuilder;
import org.jetbrains.annotations.NotNull;
import twilightforest.TwilightForestMod;
import twilightforest.block.*;
import twilightforest.client.model.block.aurorablock.UnbakedNoiseVaryingBlockStateModel;
import twilightforest.client.model.block.connected.ConnectedTextureBuilder;
import twilightforest.client.model.block.forcefield.ForceFieldModel;
import twilightforest.client.model.block.forcefield.ForceFieldModelBuilder;
import twilightforest.client.model.block.giantblock.UnbakedGiantBlockStateModel;
import twilightforest.client.renderer.block.JarRenderer;
import twilightforest.client.renderer.special.MasonJarSpecialRenderer;
import twilightforest.client.renderer.special.SkullCandleSpecialRenderer;
import twilightforest.client.renderer.special.TrophySpecialRenderer;
import twilightforest.datagen.assets.models.TFExtendedModelTemplates;
import twilightforest.datagen.assets.models.TFModelTemplates;
import twilightforest.datagen.assets.models.TFTextureMapping;
import twilightforest.datagen.assets.models.TFTextureSlot;
import twilightforest.enums.BossVariant;
import twilightforest.enums.HugeLilypadPiece;
import twilightforest.enums.NagastoneVariant;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFItems;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public abstract class BlockModelBuilders extends WoodBlockBuilders {

	public BlockModelBuilders(Consumer<BlockModelDefinitionGenerator> stateOutput, ItemModelOutput itemOutput, BiConsumer<Identifier, ModelInstance> modelOutput) {
		super(stateOutput, itemOutput, modelOutput);
	}

	public void generateTrophy(TrophyBlock floor, TrophyWallBlock wall, ItemModel.Unbaked backplate) {
		this.generateTrophy(floor, wall, backplate, "template_trophy");
	}

	public void generateTrophy(TrophyBlock floor, TrophyWallBlock wall, ItemModel.Unbaked backplate, String existingTrophy) {
		MultiVariant template = plainVariant(ModelLocationUtils.decorateBlockModelLocation("skull"));
		this.blockStateOutput.accept(createSimpleBlock(floor, template));
		this.blockStateOutput.accept(createSimpleBlock(wall, template));
		Function<ItemDisplayContext, ItemModel.Unbaked> itemTrophy = ctx -> ItemModelUtils.specialModel(ModelLocationUtils.decorateItemModelLocation("twilightforest:" + existingTrophy), new TrophySpecialRenderer.Unbaked(floor.getVariant(), ctx));

		List<SelectItemModel.SwitchCase<ItemDisplayContext>> cases = new ArrayList<>();
		for (ItemDisplayContext context : ItemDisplayContext.values()) {
			if (context == ItemDisplayContext.GUI) {
				cases.add(new SelectItemModel.SwitchCase<>(List.of(context), ItemModelUtils.composite(backplate, itemTrophy.apply(context))));
			} else {
				cases.add(new SelectItemModel.SwitchCase<>(List.of(context), itemTrophy.apply(context)));
			}
		}

		this.itemModelOutput.accept(floor.asItem(), ItemModelUtils.select(new DisplayContext(), cases));
	}

	public void generateSkullCandle(AbstractSkullCandleBlock floor, AbstractSkullCandleBlock wall) {
		MultiVariant template = plainVariant(ModelLocationUtils.decorateBlockModelLocation("skull"));
		this.blockStateOutput.accept(createSimpleBlock(floor, template));
		this.blockStateOutput.accept(createSimpleBlock(wall, template));
		this.itemModelOutput.accept(floor.asItem(), ItemModelUtils.specialModel(TwilightForestMod.prefix("item/template_skull_candle"), new SkullCandleSpecialRenderer.Unbaked(floor.getType())));
	}

	public void spawner(Block block, String texture) {
		TextureMapping texturemapping = TextureMapping.cube(new Material(TwilightForestMod.prefix(texture)));
		this.blockStateOutput.accept(createSimpleBlock(block, plainVariant(ModelTemplates.CUBE_ALL_INNER_FACES.create(block, texturemapping, this.modelOutput))));
		this.generateBlockItem(block);
	}

	public void basicCtmBlock(Block block) {
		this.blockStateOutput.accept(createSimpleBlock(block, plainVariant(TFModelTemplates.CTM_NO_BASE.extend().customLoader(ConnectedTextureBuilder::new, builder -> builder.connectsTo(block)).build().create(block, TFTextureMapping.ctmBlock(block), this.modelOutput))));
		this.generateBlockItem(block);
	}

	public void castleDoor(Block block, int tint) {
		Function<Boolean, Identifier> door = bool -> TFModelTemplates.CTM.extend().customLoader(ConnectedTextureBuilder::new, builder -> builder.connectsTo(TFBlocks.BLUE_CASTLE_DOOR.get(), TFBlocks.PINK_CASTLE_DOOR.get(), TFBlocks.VIOLET_CASTLE_DOOR.get(), TFBlocks.YELLOW_CASTLE_DOOR.get()).setOverlayEmissivity(15).setOverlayTintIndex(0)).build().createWithSuffix(block, bool ? "_vanished" : "", TFTextureMapping.ctmBlock(TwilightForestMod.prefix("block/castle_door" + (bool ? "_vanished" : "")), TwilightForestMod.prefix("block/castle_door_runes")), this.modelOutput);
		this.blockStateOutput.accept(MultiVariantGenerator.dispatch(block).with(PropertyDispatch.initial(CastleDoorBlock.VANISHED).select(true, plainVariant(door.apply(true))).select(false, plainVariant(door.apply(false)))));
		this.registerSimpleTintedItemModel(block, BuiltInRegistries.BLOCK.getKey(block).withPrefix("block/"), ItemModelUtils.constantTint(tint));
	}

	public void giantBlock(Block block, TextureMapping mapping) {
		this.blockStateOutput.accept(createSimpleBlock(block, this.giantBlockVariant(block, mapping, TFModelTemplates.GIANT_BLOCK)));
		this.generateGiantBlockItem(block, mapping);
	}

	private MultiVariant giantBlockVariant(Block block, TextureMapping mapping, ModelTemplate template) {
		Identifier sourceModel = template.create(block, mapping, this.modelOutput);
		return MultiVariant.of(new CustomBlockStateModelBuilder.Simple(new UnbakedGiantBlockStateModel(plainVariant(sourceModel).toUnbaked())));
	}

	public void generateGiantBlockItem(Block giantBlock, TextureMapping mapping) {
		ItemModel.Unbaked base = ItemModelUtils.plainModel(TFModelTemplates.GIANT_BLOCK_BASE.createWithSuffix(giantBlock, "_item", mapping, this.modelOutput));
		ItemModel.Unbaked gui = ItemModelUtils.plainModel(TFModelTemplates.GIANT_BLOCK_GUI.createWithSuffix(giantBlock, "_gui", mapping, this.modelOutput));
		this.itemModelOutput.accept(giantBlock.asItem(), ItemModelUtils.select(new DisplayContext(), base, ItemModelUtils.when(ItemDisplayContext.GUI, gui)));
	}

	public void giantBlock(Block block, TextureMapping mapping, int tint) {
		this.blockStateOutput.accept(createSimpleBlock(block, this.giantBlockVariant(block, mapping, TFModelTemplates.GIANT_BLOCK_TINTED)));
		this.generateGiantBlockItem(block, mapping, tint);
	}

	public void generateGiantBlockItem(Block giantBlock, TextureMapping mapping, int tint) {
		ItemModel.Unbaked base = ItemModelUtils.tintedModel(TFModelTemplates.GIANT_BLOCK_BASE.createWithSuffix(giantBlock, "_item", mapping, this.modelOutput), ItemModelUtils.constantTint(tint));
		ItemModel.Unbaked gui = ItemModelUtils.tintedModel(TFModelTemplates.GIANT_BLOCK_GUI.createWithSuffix(giantBlock, "_gui", mapping, this.modelOutput), ItemModelUtils.constantTint(tint));
		this.itemModelOutput.accept(giantBlock.asItem(), ItemModelUtils.select(new DisplayContext(), base, ItemModelUtils.when(ItemDisplayContext.GUI, gui)));
	}

	public void nagaStone() {
		TextureMapping mapping = TextureMapping.cube(TFBlocks.NAGASTONE.get());

		TextureMapping solidMapping = TextureMapping.cube(TFBlocks.NAGASTONE.get())
			.put(TextureSlot.SIDE, new Material(TwilightForestMod.prefix("block/nagastone_long_side")))
			.put(TextureSlot.BOTTOM, new Material(TwilightForestMod.prefix("block/nagastone_bottom_long")))
			.put(TextureSlot.TOP, new Material(TwilightForestMod.prefix("block/nagastone_turn_top")));

		Identifier solid = TFModelTemplates.CUBE_BOTTOM_TOP.createWithSuffix(TFBlocks.NAGASTONE.get(), "_solid", solidMapping, this.modelOutput);
		// todo 1.21.x cleanup: generate these models as well
		Identifier down = TwilightForestMod.prefix("block/naga_segment/down");
		Identifier up = TwilightForestMod.prefix("block/naga_segment/up");
		Identifier horizontal = TwilightForestMod.prefix("block/naga_segment/horizontal");
		Identifier vertical = TwilightForestMod.prefix("block/naga_segment/vertical");

		this.itemModelOutput.accept(TFBlocks.NAGASTONE.asItem(), ItemModelUtils.plainModel(solid));
		this.blockStateOutput.accept(MultiVariantGenerator.dispatch(TFBlocks.NAGASTONE.get()).with(
			PropertyDispatch.initial(NagastoneBlock.VARIANT)
				.select(NagastoneVariant.NORTH_DOWN, plainVariant(down).with(Y_ROT_270))
				.select(NagastoneVariant.SOUTH_DOWN, plainVariant(down).with(Y_ROT_90))
				.select(NagastoneVariant.WEST_DOWN, plainVariant(down).with(Y_ROT_180))
				.select(NagastoneVariant.EAST_DOWN, plainVariant(down))

				.select(NagastoneVariant.NORTH_UP, plainVariant(up).with(Y_ROT_270))
				.select(NagastoneVariant.SOUTH_UP, plainVariant(up).with(Y_ROT_90))
				.select(NagastoneVariant.WEST_UP, plainVariant(up).with(Y_ROT_180))
				.select(NagastoneVariant.EAST_UP, plainVariant(up))

				.select(NagastoneVariant.AXIS_X, plainVariant(horizontal))
				.select(NagastoneVariant.AXIS_Y, plainVariant(vertical))
				.select(NagastoneVariant.AXIS_Z, plainVariant(horizontal).with(Y_ROT_90))
				.select(NagastoneVariant.SOLID, plainVariant(solid))
		));

		TextureMapping faceMapping = TextureMapping.cube(TFBlocks.NAGASTONE_HEAD.get())
			.put(TextureSlot.UP, new Material(TwilightForestMod.prefix("block/nagastone_top_tip")))
			.put(TextureSlot.DOWN, new Material(TwilightForestMod.prefix("block/nagastone_bottom_tip")))
			.put(TextureSlot.SOUTH, new Material(TwilightForestMod.prefix("block/nagastone_face_left")))
			.put(TextureSlot.NORTH, new Material(TwilightForestMod.prefix("block/nagastone_face_right")))
			.put(TextureSlot.WEST, new Material(TwilightForestMod.prefix("block/nagastone_face_front")))
			.put(TextureSlot.EAST, new Material(TwilightForestMod.prefix("block/nagastone_cross_section")))
			.put(TextureSlot.PARTICLE, new Material(TwilightForestMod.prefix("block/nagastone_face_front")));
		Identifier model = TFModelTemplates.CUBE.create(TFBlocks.NAGASTONE_HEAD.get(), faceMapping, this.modelOutput);

		this.blockStateOutput.accept(MultiVariantGenerator.dispatch(TFBlocks.NAGASTONE_HEAD.get(), plainVariant(model)).with(ROTATION_HORIZONTAL_FACING));
		this.itemModelOutput.accept(TFBlocks.NAGASTONE_HEAD.asItem(), ItemModelUtils.plainModel(model));

		nagastonePillar(TFBlocks.NAGASTONE_PILLAR.get(), "");
		nagastonePillar(TFBlocks.MOSSY_NAGASTONE_PILLAR.get(), "_mossy");
		nagastonePillar(TFBlocks.CRACKED_NAGASTONE_PILLAR.get(), "_weathered");
		etchedNagastone(TFBlocks.ETCHED_NAGASTONE.get(), "");
		etchedNagastone(TFBlocks.MOSSY_ETCHED_NAGASTONE.get(), "_mossy");
		etchedNagastone(TFBlocks.CRACKED_ETCHED_NAGASTONE.get(), "_weathered");

		bisectedStairsBlock(TFBlocks.NAGASTONE_STAIRS_LEFT.get(), ModelLocationUtils.decorateBlockModelLocation("block/etched_nagastone_left"), TwilightForestMod.prefix("block/stone_tiles"), TwilightForestMod.prefix("block/nagastone_bare"));
		bisectedStairsBlock(TFBlocks.NAGASTONE_STAIRS_RIGHT.get(), TwilightForestMod.prefix("block/etched_nagastone_right"), TwilightForestMod.prefix("block/stone_tiles"), TwilightForestMod.prefix("block/nagastone_bare"));
		bisectedStairsBlock(TFBlocks.MOSSY_NAGASTONE_STAIRS_LEFT.get(), TwilightForestMod.prefix("block/etched_nagastone_left_mossy"), TwilightForestMod.prefix("block/stone_tiles_mossy"), TwilightForestMod.prefix("block/nagastone_bare_mossy"));
		bisectedStairsBlock(TFBlocks.MOSSY_NAGASTONE_STAIRS_RIGHT.get(), TwilightForestMod.prefix("block/etched_nagastone_right_mossy"), TwilightForestMod.prefix("block/stone_tiles_mossy"), TwilightForestMod.prefix("block/nagastone_bare_mossy"));
		bisectedStairsBlock(TFBlocks.CRACKED_NAGASTONE_STAIRS_LEFT.get(), TwilightForestMod.prefix("block/etched_nagastone_left_weathered"), TwilightForestMod.prefix("block/stone_tiles_weathered"), TwilightForestMod.prefix("block/nagastone_bare_weathered"));
		bisectedStairsBlock(TFBlocks.CRACKED_NAGASTONE_STAIRS_RIGHT.get(), TwilightForestMod.prefix("block/etched_nagastone_right_weathered"), TwilightForestMod.prefix("block/stone_tiles_weathered"), TwilightForestMod.prefix("block/nagastone_bare_weathered"));
	}

	private void nagastonePillar(Block block, String suffix) {
		TextureMapping mapping = TextureMapping.cube(TFBlocks.NAGASTONE.get())
			.put(TextureSlot.END, new Material(TwilightForestMod.prefix("block/nagastone_pillar_end" + suffix)))
			.put(TextureSlot.SIDE, new Material(TwilightForestMod.prefix("block/nagastone_pillar_side" + suffix)));
		Identifier model = TFModelTemplates.CUBE_COLUMN.create(block, mapping, this.modelOutput);

		TextureMapping altMapping = TextureMapping.cube(TFBlocks.NAGASTONE.get())
			.put(TextureSlot.END, new Material(TwilightForestMod.prefix("block/nagastone_pillar_end" + suffix)))
			.put(TextureSlot.SIDE, new Material(TwilightForestMod.prefix("block/nagastone_pillar_side" + suffix + "_alt")));
		Identifier reversed = TFModelTemplates.CUBE_COLUMN.createWithSuffix(block, "_alt", altMapping, this.modelOutput);

		this.blockStateOutput.accept(MultiVariantGenerator.dispatch(block).with(
			PropertyDispatch.initial(RotatedPillarBlock.AXIS, DirectionalRotatedPillarBlock.REVERSED)
				.select(Direction.Axis.X, true, plainVariant(reversed).with(X_ROT_270).with(Y_ROT_270))
				.select(Direction.Axis.Y, true, plainVariant(reversed))
				.select(Direction.Axis.Z, true, plainVariant(reversed).with(X_ROT_270))

				.select(Direction.Axis.X, false, plainVariant(model).with(X_ROT_270).with(Y_ROT_270))
				.select(Direction.Axis.Y, false, plainVariant(model))
				.select(Direction.Axis.Z, false, plainVariant(model).with(X_ROT_270))
		));
		this.itemModelOutput.accept(block.asItem(), ItemModelUtils.plainModel(model));
	}

	private void etchedNagastone(Block block, String suffix) {
		TextureMapping mapping = TextureMapping.cube(TFBlocks.NAGASTONE.get())
			.put(TextureSlot.END, new Material(TwilightForestMod.prefix("block/stone_tiles" + suffix)))
			.put(TextureSlot.SIDE, new Material(TwilightForestMod.prefix("block/etched_nagastone_up" + suffix)))
			.put(TextureSlot.PARTICLE, new Material(TwilightForestMod.prefix("block/stone_tiles" + suffix)));
		Identifier model = ModelTemplates.CUBE_COLUMN.create(block, mapping, this.modelOutput);

		this.blockStateOutput.accept(MultiVariantGenerator.dispatch(block).with(
			PropertyDispatch.initial(DirectionalBlock.FACING)
				.select(Direction.UP, plainVariant(model))
				.select(Direction.DOWN, plainVariant(model).with(X_ROT_180))
				.select(Direction.SOUTH, plainVariant(model).with(X_ROT_270))
				.select(Direction.NORTH, plainVariant(model).with(X_ROT_90))
				.select(Direction.WEST, plainVariant(model).with(Y_ROT_90).with(X_ROT_270))
				.select(Direction.EAST, plainVariant(model).with(Y_ROT_90).with(X_ROT_90))
		));
		this.itemModelOutput.accept(block.asItem(), ItemModelUtils.plainModel(model));
	}

	protected void bisectedStairsBlock(Block block, Material side, Material end, Material middle) {
		TextureMapping mapping = TextureMapping.cube(block)
			.put(TextureSlot.END, end)
			.put(TextureSlot.SIDE, side)
			.put(TFTextureSlot.MIDDLE, middle)
			.put(TextureSlot.PARTICLE, middle);

		MultiVariant inner = plainVariant(TFModelTemplates.BISECTED_STAIRS_INNER.createWithSuffix(block, "_inner", mapping, this.modelOutput));
		Identifier straight = TFModelTemplates.BISECTED_STAIRS_STRAIGHT.create(block, mapping, this.modelOutput);
		MultiVariant outer = plainVariant(TFModelTemplates.BISECTED_STAIRS_OUTER.createWithSuffix(block, "_outer", mapping, this.modelOutput));
		this.blockStateOutput.accept(createStairs(block, inner, plainVariant(straight), outer));
		this.itemModelOutput.accept(block.asItem(), ItemModelUtils.plainModel(straight));
	}

	protected void bisectedStairsBlock(Block block, Identifier side, Identifier end, Identifier middle) {
		this.bisectedStairsBlock(block, new Material(side), new Material(end), new Material(middle));
	}

	public void stonePillar() {
		Identifier base = TwilightForestMod.prefix("block/pillar/pillar_base");
		Identifier up = TwilightForestMod.prefix("block/pillar/pillar_up");
		Identifier down = TwilightForestMod.prefix("block/pillar/pillar_down");
		Identifier top = TwilightForestMod.prefix("block/pillar/pillar_top");
		Identifier bottom = TwilightForestMod.prefix("block/pillar/pillar_bottom");

		this.itemModelOutput.accept(TFBlocks.TWISTED_STONE_PILLAR.asItem(), ItemModelUtils.plainModel(TwilightForestMod.prefix("block/pillar/pillar_inventory")));

		this.blockStateOutput.accept(
			MultiPartGenerator.multiPart(TFBlocks.TWISTED_STONE_PILLAR.get())
				// X
				.with(
					condition(WallPillarBlock.AXIS, Direction.Axis.X),
					plainVariant(base).with(X_ROT_90).with(Y_ROT_90)
				)
				.with(
					and(condition(WallPillarBlock.AXIS, Direction.Axis.X), condition(PipeBlock.EAST, false)),
					plainVariant(top).with(X_ROT_90).with(Y_ROT_90)
				)
				.with(
					and(condition(WallPillarBlock.AXIS, Direction.Axis.X), condition(PipeBlock.WEST, false)),
					plainVariant(bottom).with(X_ROT_90).with(Y_ROT_90)
				)
				.with(
					and(condition(WallPillarBlock.AXIS, Direction.Axis.Y, Direction.Axis.Z), condition(PipeBlock.EAST, true)),
					plainVariant(up).with(X_ROT_90).with(Y_ROT_90)
				)
				.with(
					and(condition(WallPillarBlock.AXIS, Direction.Axis.Y, Direction.Axis.Z), condition(PipeBlock.WEST, true)),
					plainVariant(down).with(X_ROT_90).with(Y_ROT_90)
				)

				// Y
				.with(
					condition(WallPillarBlock.AXIS, Direction.Axis.Y),
					plainVariant(base)
				)
				.with(
					and(condition(WallPillarBlock.AXIS, Direction.Axis.Y), condition(PipeBlock.UP, false)),
					plainVariant(top)
				)
				.with(
					and(condition(WallPillarBlock.AXIS, Direction.Axis.Y), condition(PipeBlock.DOWN, false)),
					plainVariant(bottom)
				)
				.with(
					and(condition(WallPillarBlock.AXIS, Direction.Axis.X, Direction.Axis.Z), condition(PipeBlock.UP, true)),
					plainVariant(up)
				)
				.with(
					and(condition(WallPillarBlock.AXIS, Direction.Axis.X, Direction.Axis.Z), condition(PipeBlock.DOWN, true)),
					plainVariant(down)
				)

				// Z
				.with(
					condition(WallPillarBlock.AXIS, Direction.Axis.Z),
					plainVariant(base).with(X_ROT_90)
				)
				.with(
					and(condition(WallPillarBlock.AXIS, Direction.Axis.Z), condition(PipeBlock.NORTH, false)),
					plainVariant(top).with(X_ROT_90)
				)
				.with(
					and(condition(WallPillarBlock.AXIS, Direction.Axis.Z), condition(PipeBlock.SOUTH, false)),
					plainVariant(bottom).with(X_ROT_90)
				)
				.with(
					and(condition(WallPillarBlock.AXIS, Direction.Axis.X, Direction.Axis.Y), condition(PipeBlock.NORTH, true)),
					plainVariant(up).with(X_ROT_90)
				)
				.with(
					and(condition(WallPillarBlock.AXIS, Direction.Axis.X, Direction.Axis.Y), condition(PipeBlock.SOUTH, true)),
					plainVariant(down).with(X_ROT_90)
				)
		);
	}

	public void wroughtIronFence() {
		Block block = TFBlocks.WROUGHT_IRON_FENCE.get();
		Identifier post = ModelLocationUtils.getModelLocation(block, "_post");
		Identifier capped = ModelLocationUtils.getModelLocation(block, "_post_capped");
		Identifier full = ModelLocationUtils.getModelLocation(block, "_full");
		Identifier top = ModelLocationUtils.getModelLocation(block, "_top");
		Identifier middle = ModelLocationUtils.getModelLocation(block, "_middle");
		Identifier bottom = ModelLocationUtils.getModelLocation(block, "_bottom");

		this.blockStateOutput.accept(MultiPartGenerator.multiPart(block)
			.with(condition(WroughtIronFenceBlock.POST, WroughtIronFenceBlock.PostState.POST), plainVariant(post))
			.with(condition(WroughtIronFenceBlock.POST, WroughtIronFenceBlock.PostState.CAPPED), plainVariant(capped))

			.with(condition(WroughtIronFenceBlock.NORTH_FENCE, WroughtIronFenceBlock.FenceSide.FULL), plainVariant(full))
			.with(condition(WroughtIronFenceBlock.NORTH_FENCE, WroughtIronFenceBlock.FenceSide.TOP), plainVariant(top))
			.with(condition(WroughtIronFenceBlock.NORTH_FENCE, WroughtIronFenceBlock.FenceSide.MIDDLE), plainVariant(middle))
			.with(condition(WroughtIronFenceBlock.NORTH_FENCE, WroughtIronFenceBlock.FenceSide.BOTTOM), plainVariant(bottom))

			.with(condition(WroughtIronFenceBlock.EAST_FENCE, WroughtIronFenceBlock.FenceSide.FULL), plainVariant(full).with(Y_ROT_90))
			.with(condition(WroughtIronFenceBlock.EAST_FENCE, WroughtIronFenceBlock.FenceSide.TOP), plainVariant(top).with(Y_ROT_90))
			.with(condition(WroughtIronFenceBlock.EAST_FENCE, WroughtIronFenceBlock.FenceSide.MIDDLE), plainVariant(middle).with(Y_ROT_90))
			.with(condition(WroughtIronFenceBlock.EAST_FENCE, WroughtIronFenceBlock.FenceSide.BOTTOM), plainVariant(bottom).with(Y_ROT_90))

			.with(condition(WroughtIronFenceBlock.SOUTH_FENCE, WroughtIronFenceBlock.FenceSide.FULL), plainVariant(full).with(Y_ROT_180))
			.with(condition(WroughtIronFenceBlock.SOUTH_FENCE, WroughtIronFenceBlock.FenceSide.TOP), plainVariant(top).with(Y_ROT_180))
			.with(condition(WroughtIronFenceBlock.SOUTH_FENCE, WroughtIronFenceBlock.FenceSide.MIDDLE), plainVariant(middle).with(Y_ROT_180))
			.with(condition(WroughtIronFenceBlock.SOUTH_FENCE, WroughtIronFenceBlock.FenceSide.BOTTOM), plainVariant(bottom).with(Y_ROT_180))

			.with(condition(WroughtIronFenceBlock.WEST_FENCE, WroughtIronFenceBlock.FenceSide.FULL), plainVariant(full).with(Y_ROT_270))
			.with(condition(WroughtIronFenceBlock.WEST_FENCE, WroughtIronFenceBlock.FenceSide.TOP), plainVariant(top).with(Y_ROT_270))
			.with(condition(WroughtIronFenceBlock.WEST_FENCE, WroughtIronFenceBlock.FenceSide.MIDDLE), plainVariant(middle).with(Y_ROT_270))
			.with(condition(WroughtIronFenceBlock.WEST_FENCE, WroughtIronFenceBlock.FenceSide.BOTTOM), plainVariant(bottom).with(Y_ROT_270))
		);

		this.itemModelOutput.accept(block.asItem(), ItemModelUtils.plainModel(ModelLocationUtils.getModelLocation(block.asItem())));
	}

	public void terrorcotta() {
		this.rotationallySpecialColumn(TFBlocks.TERRORCOTTA_ARCS.get());

		PropertyDispatch.C1<MultiVariant, Direction> directionDispach = PropertyDispatch.initial(GlazedTerracottaBlock.FACING);
		boolean firstCurve = true;
		for (Direction direction : new Direction[]{Direction.SOUTH, Direction.NORTH, Direction.WEST, Direction.EAST}) {
			Identifier location = this.makeTerrorcottaCurvesModel("terrorcotta_curves", direction.get2DDataValue());
			directionDispach = directionDispach.select(direction, plainVariant(location));
			if (firstCurve) {
				this.itemModelOutput.accept(TFBlocks.TERRORCOTTA_CURVES.asItem(), ItemModelUtils.plainModel(location));
				firstCurve = false;
			}
		}
		this.blockStateOutput.accept(MultiVariantGenerator.dispatch(TFBlocks.TERRORCOTTA_CURVES.get()).with(directionDispach));

		Identifier rotated = this.makeTerrorcottaLinesModel("terrorcotta_lines", true);
		Identifier unRotated = this.makeTerrorcottaLinesModel("terrorcotta_lines", false);

		this.blockStateOutput.accept(MultiVariantGenerator.dispatch(TFBlocks.TERRORCOTTA_LINES.get()).with(
			PropertyDispatch.initial(BinaryRotatedBlock.ROTATED)
				.select(true, plainVariant(rotated))
				.select(false, plainVariant(unRotated))
		));

		this.itemModelOutput.accept(TFBlocks.TERRORCOTTA_LINES.asItem(), ItemModelUtils.plainModel(rotated));
	}

	private Identifier makeTerrorcottaCurvesModel(String type, int rotation) {
		TextureMapping mapping = TextureMapping.cube(TFBlocks.TERRORCOTTA_CURVES.get())
			.put(TextureSlot.UP, new Material(TwilightForestMod.prefix("block/" + type + curvesSuffixForFacing(rotation, Direction.UP))))
			.put(TextureSlot.DOWN, new Material(TwilightForestMod.prefix("block/" + type + curvesSuffixForFacing(rotation, Direction.DOWN))))
			.put(TextureSlot.SOUTH, new Material(TwilightForestMod.prefix("block/" + type + curvesSuffixForFacing(rotation, Direction.SOUTH))))
			.put(TextureSlot.NORTH, new Material(TwilightForestMod.prefix("block/" + type + curvesSuffixForFacing(rotation, Direction.NORTH))))
			.put(TextureSlot.WEST, new Material(TwilightForestMod.prefix("block/" + type + curvesSuffixForFacing(rotation, Direction.WEST))))
			.put(TextureSlot.EAST, new Material(TwilightForestMod.prefix("block/" + type + curvesSuffixForFacing(rotation, Direction.EAST))))
			.put(TextureSlot.PARTICLE, new Material(TwilightForestMod.prefix("block/" + type + "_a")));

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

	private Identifier makeTerrorcottaLinesModel(String type, boolean rotated) {
		TextureMapping mapping = TextureMapping.cube(TFBlocks.TERRORCOTTA_CURVES.get())
			.put(TextureSlot.UP, new Material(TwilightForestMod.prefix("block/" + type + linesSuffixForFacing(rotated, Direction.UP))))
			.put(TextureSlot.DOWN, new Material(TwilightForestMod.prefix("block/" + type + linesSuffixForFacing(rotated, Direction.DOWN))))
			.put(TextureSlot.SOUTH, new Material(TwilightForestMod.prefix("block/" + type + linesSuffixForFacing(rotated, Direction.SOUTH))))
			.put(TextureSlot.NORTH, new Material(TwilightForestMod.prefix("block/" + type + linesSuffixForFacing(rotated, Direction.NORTH))))
			.put(TextureSlot.WEST, new Material(TwilightForestMod.prefix("block/" + type + linesSuffixForFacing(rotated, Direction.WEST))))
			.put(TextureSlot.EAST, new Material(TwilightForestMod.prefix("block/" + type + linesSuffixForFacing(rotated, Direction.EAST))))
			.put(TextureSlot.PARTICLE, new Material(TwilightForestMod.prefix("block/" + type + "_a")));

		return TFModelTemplates.CUBE.create(TwilightForestMod.prefix("block/" + type + "_" + (rotated ? 90 : 0)), mapping, this.modelOutput);
	}

	public void makeJars() {
		TextureMapping spawnerMapping = TextureMapping.cube(TFBlocks.MASON_JAR.get())
			.put(TextureSlot.TOP, new Material(TwilightForestMod.prefix("block/jar_top")))
			.put(TextureSlot.BOTTOM, new Material(TwilightForestMod.prefix("block/jar_bottom")))
			.put(TextureSlot.SIDE, new Material(TwilightForestMod.prefix("block/jar_side")))
			.put(TextureSlot.PARTICLE, new Material(TwilightForestMod.prefix("block/jar_side")))
			.put(TFTextureSlot.SOIL, new Material(Identifier.withDefaultNamespace("block/composter_compost")))
			.put(TFTextureSlot.PLANT, new Material(Identifier.withDefaultNamespace("block/poppy")));

		Identifier spawnerLocation = TFExtendedModelTemplates.FIREFLY_PARTICLE_SPAWNER.create(TwilightForestMod.prefix("block/" + TFBlocks.FIREFLY_SPAWNER.getId().getPath()), spawnerMapping, this.modelOutput);
		this.blockStateOutput.accept(createSimpleBlock(TFBlocks.FIREFLY_SPAWNER.get(), plainVariant(spawnerLocation)));
		this.itemModelOutput.accept(TFBlocks.FIREFLY_SPAWNER.get().asItem(), ItemModelUtils.plainModel(spawnerLocation));

		TextureMapping mapping = TextureMapping.cube(TFBlocks.MASON_JAR.get())
			.put(TextureSlot.TOP, new Material(TwilightForestMod.prefix("block/jar_top")))
			.put(TextureSlot.BOTTOM, new Material(TwilightForestMod.prefix("block/jar_bottom")))
			.put(TextureSlot.SIDE, new Material(TwilightForestMod.prefix("block/jar_side")))
			.put(TextureSlot.PARTICLE, new Material(TwilightForestMod.prefix("block/jar_side")));

		Identifier jar = TFExtendedModelTemplates.MASON_JAR.create(TwilightForestMod.prefix("block/" + TFBlocks.MASON_JAR.getId().getPath()), mapping, this.modelOutput);
		this.blockStateOutput.accept(createSimpleBlock(TFBlocks.MASON_JAR.get(), plainVariant(jar)));
		this.blockStateOutput.accept(createSimpleBlock(TFBlocks.CICADA_JAR.get(), plainVariant(jar)));
		this.blockStateOutput.accept(createSimpleBlock(TFBlocks.FIREFLY_JAR.get(), plainVariant(jar)));

		this.itemModelOutput.accept(TFItems.MASON_JAR.get(), ItemModelUtils.composite(ItemModelUtils.plainModel(jar), ItemModelUtils.specialModel(jar, new MasonJarSpecialRenderer.Unbaked(TFBlocks.TWILIGHT_OAK_LOG.asItem()))));
		this.itemModelOutput.accept(TFItems.FIREFLY_JAR.get(), ItemModelUtils.composite(ItemModelUtils.plainModel(jar), ItemModelUtils.specialModel(jar, new MasonJarSpecialRenderer.Unbaked(TFBlocks.TWILIGHT_OAK_LOG.asItem()))));
		this.itemModelOutput.accept(TFItems.CICADA_JAR.get(), ItemModelUtils.composite(ItemModelUtils.plainModel(jar), ItemModelUtils.specialModel(jar, new MasonJarSpecialRenderer.Unbaked(TFBlocks.CANOPY_LOG.asItem()))));

		for (JarRenderer.LidResource lid : JarRenderer.LID_LOCATION_LIST.get()) {
			Identifier item = lid.identifier();
			String name = item.getPath();

			if (lid.lid() == Items.PUMPKIN) {
				TextureMapping lidMapping = TextureMapping.cube(TFBlocks.MASON_JAR.get())
					.put(TextureSlot.SIDE, new Material(Identifier.withDefaultNamespace("block/pumpkin_side")))
					.put(TextureSlot.END, new Material(Identifier.withDefaultNamespace("block/pumpkin_top")));

				TFModelTemplates.JAR_LID.create(TwilightForestMod.prefix("block/lid/" + name), lidMapping, this.modelOutput);
				continue;
			}
			if (lid.customPath() != null) name = lid.customPath();
			TextureMapping lidMapping = TextureMapping.cube(TFBlocks.MASON_JAR.get())
				.put(TextureSlot.SIDE, new Material(Identifier.fromNamespaceAndPath(item.getNamespace(), "block/" + item.getPath())))
				.put(TextureSlot.END, new Material(Identifier.fromNamespaceAndPath(item.getNamespace(), "block/" + item.getPath() + "_top")));

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
		Material sideA = TextureMapping.getBlockTexture(block, "_side_a");
		Material sideB = TextureMapping.getBlockTexture(block, "_side_b");
		Material end = TextureMapping.getBlockTexture(block, "_end");

		Identifier xModel = TFModelTemplates.CUBE_COLUMN_ROTATIONALLY_SPECIAL_X.create(block, TextureMapping.cube(block).put(TextureSlot.END, end).put(TFTextureSlot.SIDE_A, sideA).put(TFTextureSlot.SIDE_B, sideB), this.modelOutput);
		Identifier yModel = TFModelTemplates.CUBE_COLUMN.create(block, TextureMapping.cube(block).put(TextureSlot.END, end).put(TextureSlot.SIDE, sideA), this.modelOutput);
		Identifier zModel = TFModelTemplates.CUBE_COLUMN_ROTATIONALLY_SPECIAL_Z.create(block, TextureMapping.cube(block).put(TextureSlot.END, end).put(TFTextureSlot.SIDE_A, sideA).put(TFTextureSlot.SIDE_B, sideB), this.modelOutput);

		this.blockStateOutput.accept(MultiVariantGenerator.dispatch(block).with(
			PropertyDispatch.initial(RotatedPillarBlock.AXIS)
				.select(Direction.Axis.X, plainVariant(xModel))
				.select(Direction.Axis.Y, plainVariant(yModel))
				.select(Direction.Axis.Z, plainVariant(zModel))
		));
		this.itemModelOutput.accept(block.asItem(), ItemModelUtils.plainModel(yModel));
	}

	public void thorns(Block block, Block potted) {
		TextureMapping mapping = TextureMapping.column(block);
		this.blockStateOutput.accept(createSimpleBlock(potted, plainVariant(TFModelTemplates.POTTED_THORN.create(potted, mapping, this.modelOutput))));

		Identifier main = TFModelTemplates.THORNS_MAIN.createWithSuffix(block, "_main", mapping, this.modelOutput);
		Identifier thorns = TFModelTemplates.THORNS.createWithSuffix(block, "_thorns", mapping, this.modelOutput);
		Identifier top = TFModelTemplates.THORNS_SECTION_TOP.createWithSuffix(block, "_top", mapping, this.modelOutput);
		Identifier bottom = TFModelTemplates.THORNS_SECTION_BOTTOM.createWithSuffix(block, "_bottom", mapping, this.modelOutput);
		Identifier noSection = TFModelTemplates.THORNS_NO_SECTION.createWithSuffix(block, "_no_section", mapping, this.modelOutput);
		Identifier noSectionAlt = TFModelTemplates.THORNS_NO_SECTION_ALT.createWithSuffix(block, "_no_section_alt", mapping, this.modelOutput);

		this.itemModelOutput.accept(block.asItem(), ItemModelUtils.plainModel(main));

		this.blockStateOutput.accept(
			MultiPartGenerator.multiPart(block)
				// MAIN
				.with(
					condition(RotatedPillarBlock.AXIS, Direction.Axis.Y),
					plainVariant(thorns)
				)
				.with(
					condition(RotatedPillarBlock.AXIS, Direction.Axis.Z),
					plainVariant(thorns).with(X_ROT_90)
				)
				.with(
					condition(RotatedPillarBlock.AXIS, Direction.Axis.X),
					plainVariant(thorns).with(X_ROT_90).with(Y_ROT_90)
				)

				// UP
				.with(
					condition(PipeBlock.UP, true),
					plainVariant(top).with(X_ROT_90)
				)
				.with(
					and(condition(PipeBlock.UP, false), condition(RotatedPillarBlock.AXIS, Direction.Axis.Z, Direction.Axis.Y)),
					plainVariant(noSection).with(X_ROT_270)
				)
				.with(
					and(condition(PipeBlock.UP, false), condition(RotatedPillarBlock.AXIS, Direction.Axis.X)),
					plainVariant(noSection).with(X_ROT_270).with(Y_ROT_90)
				)

				// DOWN
				.with(
					condition(PipeBlock.DOWN, true),
					plainVariant(bottom).with(X_ROT_90)
				)
				.with(
					and(condition(PipeBlock.DOWN, false), condition(RotatedPillarBlock.AXIS, Direction.Axis.Z, Direction.Axis.Y)),
					plainVariant(noSection).with(X_ROT_90)
				)
				.with(
					and(condition(PipeBlock.DOWN, false), condition(RotatedPillarBlock.AXIS, Direction.Axis.X)),
					plainVariant(noSection).with(X_ROT_90).with(Y_ROT_90)
				)

				// EAST
				.with(
					condition(PipeBlock.EAST, true),
					plainVariant(top).with(Y_ROT_270)
				)
				.with(
					and(condition(PipeBlock.EAST, false), condition(RotatedPillarBlock.AXIS, Direction.Axis.Y, Direction.Axis.X)),
					plainVariant(noSection).with(Y_ROT_90)
				)
				.with(
					and(condition(PipeBlock.EAST, false), condition(RotatedPillarBlock.AXIS, Direction.Axis.Z)),
					plainVariant(noSectionAlt).with(Y_ROT_90)
				)

				// WEST
				.with(
					condition(PipeBlock.WEST, true),
					plainVariant(bottom).with(Y_ROT_270)
				)
				.with(
					and(condition(PipeBlock.WEST, false), condition(RotatedPillarBlock.AXIS, Direction.Axis.Y, Direction.Axis.X)),
					plainVariant(noSection).with(Y_ROT_270)
				)
				.with(
					and(condition(PipeBlock.WEST, false), condition(RotatedPillarBlock.AXIS, Direction.Axis.Z)),
					plainVariant(noSectionAlt).with(Y_ROT_270)
				)

				// SOUTH
				.with(
					condition(PipeBlock.SOUTH, true),
					plainVariant(top)
				)
				.with(
					and(condition(PipeBlock.SOUTH, false), condition(RotatedPillarBlock.AXIS, Direction.Axis.Y, Direction.Axis.Z)),
					plainVariant(noSection).with(Y_ROT_180)
				)
				.with(
					and(condition(PipeBlock.SOUTH, false), condition(RotatedPillarBlock.AXIS, Direction.Axis.X)),
					plainVariant(noSectionAlt).with(Y_ROT_180)
				)

				// NORTH
				.with(
					condition(PipeBlock.NORTH, true),
					plainVariant(bottom)
				)
				.with(
					and(condition(PipeBlock.NORTH, false), condition(RotatedPillarBlock.AXIS, Direction.Axis.Y, Direction.Axis.Z)),
					plainVariant(noSection)
				)
				.with(
					and(condition(PipeBlock.NORTH, false), condition(RotatedPillarBlock.AXIS, Direction.Axis.X)),
					plainVariant(noSectionAlt)
				)
		);
	}

	public void directionalCrossModel(Block block, PlantType type) {
		Identifier identifier = type.getCross().create(block, TextureMapping.cross(block), this.modelOutput);

		this.itemModelOutput.accept(block.asItem(), ItemModelUtils.plainModel(this.createFlatItemModelWithBlockTexture(block.asItem(), block)));
		this.blockStateOutput.accept(MultiVariantGenerator.dispatch(block).with(
			PropertyDispatch.initial(DirectionalBlock.FACING)
				.select(Direction.UP, plainVariant(identifier))
				.select(Direction.DOWN, plainVariant(identifier).with(X_ROT_180))
				.select(Direction.SOUTH, plainVariant(identifier).with(X_ROT_270))
				.select(Direction.NORTH, plainVariant(identifier).with(X_ROT_90))
				.select(Direction.WEST, plainVariant(identifier).with(Y_ROT_90).with(X_ROT_270))
				.select(Direction.EAST, plainVariant(identifier).with(Y_ROT_90).with(X_ROT_90))
		));
	}

	public void forcefield(Block block, int tint) {
		this.blockStateOutput.accept(createSimpleBlock(block, plainVariant(TFModelTemplates.FORCEFIELD.extend().customLoader(ForceFieldModelBuilder::new, builder -> {
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
		}).build().create(block, TFTextureMapping.forcefield(), this.modelOutput))));
		this.itemModelOutput.accept(block.asItem(), ItemModelUtils.tintedModel(ModelTemplates.FLAT_ITEM.create(ModelLocationUtils.getModelLocation(block.asItem()), TextureMapping.layer0(new Material(TwilightForestMod.prefix("block/forcefield"))), this.modelOutput), ItemModelUtils.constantTint(tint)));
	}

	public void generatePaneBlock(Block glassBlock, Block paneBlock) {
		TextureMapping mapping = new TextureMapping().put(TextureSlot.PANE, TextureMapping.getBlockTexture(glassBlock)).put(TextureSlot.EDGE, TextureMapping.getBlockTexture(glassBlock));
		Identifier post = ModelTemplates.STAINED_GLASS_PANE_POST.create(paneBlock, mapping, this.modelOutput);
		Identifier side = ModelTemplates.STAINED_GLASS_PANE_SIDE.create(paneBlock, mapping, this.modelOutput);
		Identifier sideAlt = ModelTemplates.STAINED_GLASS_PANE_SIDE_ALT.create(paneBlock, mapping, this.modelOutput);
		Identifier noSide = ModelTemplates.STAINED_GLASS_PANE_NOSIDE.create(paneBlock, mapping, this.modelOutput);
		Identifier noSideAlt = ModelTemplates.STAINED_GLASS_PANE_NOSIDE_ALT.create(paneBlock, mapping, this.modelOutput);
		Item item = paneBlock.asItem();
		this.registerSimpleItemModel(item, this.createFlatItemModelWithBlockTexture(item, glassBlock));
		this.blockStateOutput
			.accept(
				MultiPartGenerator.multiPart(paneBlock)
					.with(plainVariant(post))
					.with(condition(BlockStateProperties.NORTH, true), plainVariant(side))
					.with(
						condition(BlockStateProperties.EAST, true),
						plainVariant(side).with(Y_ROT_90)
					)
					.with(condition(BlockStateProperties.SOUTH, true), plainVariant(sideAlt))
					.with(
						condition(BlockStateProperties.WEST, true),
						plainVariant(sideAlt).with(Y_ROT_90)
					)
					.with(condition(BlockStateProperties.NORTH, false), plainVariant(noSide))
					.with(condition(BlockStateProperties.EAST, false), plainVariant(noSideAlt))
					.with(
						condition(BlockStateProperties.SOUTH, false),
						plainVariant(noSideAlt).with(Y_ROT_90)
					)
					.with(
						condition(BlockStateProperties.WEST, false),
						plainVariant(noSide).with(Y_ROT_270)
					)
			);
	}

	public void generateRuneBlock(Block runeBlock, int tint) {
		Variant[] variants = new Variant[8];
		for (int i = 0; i < 8; i++) {
			variants[i] = plainModel(TFModelTemplates.CASTLE_RUNE_TEMPLATE.createWithSuffix(runeBlock, "_" + i, TextureMapping.cube(TFBlocks.CASTLE_BRICK.get()).put(TFTextureSlot.RUNE, new Material(TwilightForestMod.prefix("block/castleblock_magic_" + i))), this.modelOutput));
		}
		this.blockStateOutput.accept(MultiVariantGenerator.dispatch(runeBlock, variants(variants)));
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
			Identifier model = TFModelTemplates.TROPHY_PEDESTAL.createWithSuffix(pedestal, i == 0 ? "" : ("_" + i), TFTextureMapping.trophyPedestal(pedestal, false, variantList[i][0], variantList[i][1], variantList[i][2], variantList[i][3]), this.modelOutput);
			Identifier activeModel = TFModelTemplates.TROPHY_PEDESTAL_ACTIVE.createWithSuffix(pedestal, i == 0 ? "_active" : ("_active_" + i), TFTextureMapping.trophyPedestal(pedestal, true, variantList[i][0], variantList[i][1], variantList[i][2], variantList[i][3]), this.modelOutput);

			for (Quadrant rot : Quadrant.values()) {
				variants.add(plainModel(model).with(VariantMutator.Y_ROT.withValue(rot)));
				activeVariants.add(plainModel(activeModel).with(VariantMutator.Y_ROT.withValue(rot)));
			}
		}
		this.blockStateOutput.accept(MultiVariantGenerator.dispatch(pedestal).with(PropertyDispatch.initial(TrophyPedestalBlock.ACTIVE).select(true, variants(activeVariants.toArray(new Variant[0]))).select(false, variants(variants.toArray(new Variant[0])))));
		this.generateBlockItem(pedestal);
	}

	public void ironLadder() {
		MultiPartGenerator model = MultiPartGenerator.multiPart(TFBlocks.IRON_LADDER.get());
		Identifier left = ModelLocationUtils.getModelLocation(TFBlocks.IRON_LADDER.get(), "_left");
		Identifier leftConnect = ModelLocationUtils.getModelLocation(TFBlocks.IRON_LADDER.get(), "_left_connection");
		Identifier right = ModelLocationUtils.getModelLocation(TFBlocks.IRON_LADDER.get(), "_right");
		Identifier rightConnect = ModelLocationUtils.getModelLocation(TFBlocks.IRON_LADDER.get(), "_right_connection");
		forEachHorizontalDirection((d, mutator) -> {
			model.with(and(
				condition(IronLadderBlock.LEFT, false),
				condition(LadderBlock.FACING, d)), plainVariant(left).with(mutator));
			model.with(and(
				condition(IronLadderBlock.LEFT, true),
				condition(LadderBlock.FACING, d)), plainVariant(leftConnect).with(mutator));
			model.with(and(
				condition(IronLadderBlock.RIGHT, false),
				condition(LadderBlock.FACING, d)), plainVariant(right).with(mutator));
			model.with(and(
				condition(IronLadderBlock.RIGHT, true),
				condition(LadderBlock.FACING, d)), plainVariant(rightConnect).with(mutator));
		});
		this.blockStateOutput.accept(model);
		this.itemModelOutput.accept(TFBlocks.IRON_LADDER.asItem(), ItemModelUtils.plainModel(ModelLocationUtils.getModelLocation(TFBlocks.IRON_LADDER.asItem())));
	}

	public void createMultifaceBlock(Block mushroomBlock, Identifier inside, boolean invertConditions) {
		Identifier outside = ModelTemplates.SINGLE_FACE.create(mushroomBlock, TextureMapping.defaultTexture(mushroomBlock), this.modelOutput);
		this.blockStateOutput.accept(MultiPartGenerator.multiPart(mushroomBlock)
			.with(condition(BlockStateProperties.NORTH, !invertConditions), plainVariant(outside))
			.with(condition(BlockStateProperties.EAST, !invertConditions), plainVariant(outside).with(Y_ROT_90).with(VariantMutator.UV_LOCK.withValue(true)))
			.with(condition(BlockStateProperties.SOUTH, !invertConditions), plainVariant(outside).with(Y_ROT_180).with(VariantMutator.UV_LOCK.withValue(true)))
			.with(condition(BlockStateProperties.WEST, !invertConditions), plainVariant(outside).with(Y_ROT_270).with(VariantMutator.UV_LOCK.withValue(true)))
			.with(condition(BlockStateProperties.UP, !invertConditions), plainVariant(outside).with(X_ROT_270).with(VariantMutator.UV_LOCK.withValue(true)))
			.with(condition(BlockStateProperties.DOWN, !invertConditions), plainVariant(outside).with(X_ROT_90).with(VariantMutator.UV_LOCK.withValue(true)))
			.with(condition(BlockStateProperties.NORTH, invertConditions), plainVariant(inside))
			.with(condition(BlockStateProperties.EAST, invertConditions), plainVariant(inside).with(Y_ROT_90).with(VariantMutator.UV_LOCK.withValue(false)))
			.with(condition(BlockStateProperties.SOUTH, invertConditions), plainVariant(inside).with(Y_ROT_180).with(VariantMutator.UV_LOCK.withValue(false)))
			.with(condition(BlockStateProperties.WEST, invertConditions), plainVariant(inside).with(Y_ROT_270).with(VariantMutator.UV_LOCK.withValue(false)))
			.with(condition(BlockStateProperties.UP, invertConditions), plainVariant(inside).with(X_ROT_270).with(VariantMutator.UV_LOCK.withValue(false)))
			.with(condition(BlockStateProperties.DOWN, invertConditions), plainVariant(inside).with(X_ROT_90).with(VariantMutator.UV_LOCK.withValue(false)))
		);
		this.registerSimpleItemModel(mushroomBlock, TexturedModel.CUBE.createWithSuffix(mushroomBlock, "_inventory", this.modelOutput));
	}

	public void generateHugeLilyPad() {
		Block block = TFBlocks.HUGE_LILY_PAD.get();
		Identifier[] models = new Identifier[4];
		for (int i = 0; i < models.length; i++) {
			Identifier texture = ModelLocationUtils.getModelLocation(block, "_" + i);
			models[i] = TFModelTemplates.create(texture.toString(), TextureSlot.TEXTURE).extend().parent(ModelLocationUtils.getModelLocation(Blocks.LILY_PAD)).build().create(texture, TextureMapping.defaultTexture(new Material(texture)), this.modelOutput);
		}

		Map<Direction, Map<HugeLilypadPiece, Identifier>> stateMap = ImmutableMap.of(
			Direction.NORTH, ImmutableMap.of(HugeLilypadPiece.NW, models[0], HugeLilypadPiece.NE, models[1], HugeLilypadPiece.SE, models[2], HugeLilypadPiece.SW, models[3]),
			Direction.WEST, ImmutableMap.of(HugeLilypadPiece.NW, models[1], HugeLilypadPiece.NE, models[2], HugeLilypadPiece.SE, models[3], HugeLilypadPiece.SW, models[0]),
			Direction.SOUTH, ImmutableMap.of(HugeLilypadPiece.NW, models[2], HugeLilypadPiece.NE, models[3], HugeLilypadPiece.SE, models[0], HugeLilypadPiece.SW, models[1]),
			Direction.EAST, ImmutableMap.of(HugeLilypadPiece.NW, models[3], HugeLilypadPiece.NE, models[0], HugeLilypadPiece.SE, models[1], HugeLilypadPiece.SW, models[2])
		);

		this.blockStateOutput.accept(MultiVariantGenerator.dispatch(block).with(PropertyDispatch.initial(HugeLilyPadBlock.PIECE, HugeLilyPadBlock.FACING).generate((piece, facing) -> createRotatedVariants(plainModel(stateMap.get(facing).get(piece))))));
		this.itemModelOutput.accept(block.asItem(), ItemModelUtils.tintedModel(ModelTemplates.FLAT_ITEM.create(ModelLocationUtils.getModelLocation(block.asItem()), TextureMapping.layer0(block), this.modelOutput), ItemModelUtils.constantTint(-9321636)));
	}

	public void generateAuroraBlocks() {
		Block base = TFBlocks.AURORA_BLOCK.get();
		Identifier[] auroras = new Identifier[16];
		for (int i = 0; i < auroras.length; i++) {
			auroras[i] = TFModelTemplates.TINTED_BLOCK.createWithSuffix(TFBlocks.AURORA_BLOCK.get(), "_" + i, TextureMapping.cube(TextureMapping.getBlockTexture(base, "_" + i)), this.modelOutput);
		}
		this.blockStateOutput.accept(createSimpleBlock(base, MultiVariant.of(new CustomBlockStateModelBuilder.Simple(new UnbakedNoiseVaryingBlockStateModel(Arrays.stream(auroras).map(aurora -> plainVariant(aurora).toUnbaked()).toList())))));
		this.registerSimpleTintedItemModel(base, auroras[0], ItemModelUtils.constantTint(-16711758));

		Block pillar = TFBlocks.AURORA_PILLAR.get();
		this.wrapTintedBlockItem(pillar, ItemModelUtils.constantTint(-9181501), block -> this.blockStateOutput.accept(createAxisAlignedPillarBlock(block, plainVariant(TexturedModel.createDefault(block1 -> new TextureMapping()
				.put(TextureSlot.SIDE, TextureMapping.getBlockTexture(block))
				.put(TextureSlot.TOP, TextureMapping.getBlockTexture(block, "_top"))
				.put(TextureSlot.BOTTOM, TextureMapping.getBlockTexture(block, "_top")), TFModelTemplates.TINTED_CUBE_BOTTOM_TOP)
			.create(block, this.modelOutput)))));

		Block slab = TFBlocks.AURORA_SLAB.get();
		TextureMapping slabMap = new TextureMapping()
			.put(TextureSlot.SIDE, TextureMapping.getBlockTexture(pillar))
			.put(TextureSlot.TOP, TextureMapping.getBlockTexture(pillar, "_top"))
			.put(TextureSlot.BOTTOM, TextureMapping.getBlockTexture(pillar, "_top"));

		Identifier bottom = TFModelTemplates.TINTED_SLAB_BOTTOM.create(slab, slabMap, this.modelOutput);
		Identifier top = TFModelTemplates.TINTED_SLAB_TOP.create(slab, slabMap, this.modelOutput);
		this.wrapTintedBlockItem(slab, ItemModelUtils.constantTint(-9181501), block -> this.blockStateOutput.accept(createSlab(block, plainVariant(bottom), plainVariant(top), plainVariant(ModelLocationUtils.getModelLocation(pillar)))));

		this.wrapTintedBlockItem(TFBlocks.AURORALIZED_GLASS.get(), ItemModelUtils.constantTint(-9181501), block -> this.blockStateOutput.accept(createSimpleBlock(block, plainVariant(TFModelTemplates.CTM_NO_BASE.extend().customLoader(ConnectedTextureBuilder::new, builder -> builder.setOverlayTintIndex(0).connectsTo(block)).build().create(block, TFTextureMapping.ctmBlock(block), this.modelOutput)))));
	}

	public void createTFChest(Block chestBlock, Block particleBlock, Identifier texture) {
		this.createParticleOnlyBlock(chestBlock, particleBlock);
		Item item = chestBlock.asItem();
		this.itemModelOutput.accept(item, ItemModelUtils.specialModel(ModelTemplates.CHEST_INVENTORY.create(item, TextureMapping.particle(particleBlock), this.modelOutput), new ChestSpecialRenderer.Unbaked(texture)));
	}

	//TODO god I hope this works as I want it to
	public void generateBush(Block bush) {
		Identifier small = TFModelTemplates.SMALL_BUSH.createWithSuffix(bush, "_small", TextureMapping.cube(bush), this.modelOutput);
		MultiVariant medium = plainVariant(TFModelTemplates.MEDIUM_BUSH.create(bush, TextureMapping.cube(bush), this.modelOutput));
		MultiVariant large = plainVariant(TFModelTemplates.LARGE_BUSH.createWithSuffix(bush, "_large", TextureMapping.cube(bush), this.modelOutput));
		MultiVariant grown = plainVariant(TFModelTemplates.LARGE_BUSH.createWithSuffix(bush,"_ripe", TextureMapping.cube(TextureMapping.getBlockTexture(bush, "_ripe")), this.modelOutput));

		this.blockStateOutput.accept(MultiVariantGenerator.dispatch(bush).with(PropertyDispatch.initial(TFBushBlock.AGE, TFBushBlock.SNOW_LAYERS).generate((age, snow) -> {
			MultiVariant bushModel = switch (age) {
				case 1 -> medium;
				case 2 -> large;
				case 3 -> grown;
				default -> plainVariant(small);
			};
			if (snow > 0) {
				MultiVariant snowModel = snow < 8 ? plainVariant(ModelLocationUtils.getModelLocation(Blocks.SNOW, "_height" + snow * 2)) : plainVariant(ModelLocationUtils.getModelLocation(Blocks.SNOW_BLOCK));

				CompositeBlockStateModelBuilder builder = new CompositeBlockStateModelBuilder();
				builder.addPartModel(bushModel.toUnbaked());
				builder.addPartModel(snowModel.toUnbaked());

				return MultiVariant.of(builder);
			} else {
				return bushModel;
			}
		})));
		this.itemModelOutput.accept(bush.asItem(), ItemModelUtils.plainModel(small));
	}
}
