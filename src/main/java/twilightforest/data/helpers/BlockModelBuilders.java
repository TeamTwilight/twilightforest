package twilightforest.data.helpers;

import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelOutput;
import net.minecraft.client.data.models.blockstates.*;
import net.minecraft.client.data.models.model.*;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import twilightforest.TwilightForestMod;
import twilightforest.block.CastleDoorBlock;
import twilightforest.client.model.block.connected.ConnectedTextureBuilder;
import twilightforest.client.model.block.forcefield.ForceFieldModel;
import twilightforest.client.model.block.forcefield.ForceFieldModelBuilder;
import twilightforest.data.models.TFBlockModelTemplates;
import twilightforest.data.models.TFTextureMapping;
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

	public void bossSpawner(Block block) {
		TextureMapping texturemapping = TextureMapping.cube(TwilightForestMod.prefix("boss_spawner"));
		this.blockStateOutput.accept(createSimpleBlock(block, ModelTemplates.CUBE_ALL_INNER_FACES.create(block, texturemapping, this.modelOutput)));
	}

	public void basicCtmBlock(Block block) {
		this.blockStateOutput.accept(createSimpleBlock(block, TFBlockModelTemplates.CTM_NO_BASE.extend().customLoader(ConnectedTextureBuilder::new, builder -> builder.connectsTo(block)).build().create(block, TFTextureMapping.ctmBlock(block), this.modelOutput)));
	}

	public void castleDoor(Block block) {
		Function<Boolean, ResourceLocation> door = bool -> TFBlockModelTemplates.CTM.extend().customLoader(ConnectedTextureBuilder::new, builder -> builder.connectsTo(TFBlocks.BLUE_CASTLE_DOOR.get(), TFBlocks.PINK_CASTLE_DOOR.get(), TFBlocks.VIOLET_CASTLE_DOOR.get(), TFBlocks.YELLOW_CASTLE_DOOR.get()).setOverlayEmissivity(15).setOverlayTintIndex(0)).renderType("cutout").build().createWithSuffix(block, bool ? "_vanished" : "", TFTextureMapping.ctmBlock(TwilightForestMod.prefix("block/castle_door" + (bool ? "_vanished" : "")), TwilightForestMod.prefix("castle_door_runes")), this.modelOutput);
		this.blockStateOutput.accept(MultiVariantGenerator.multiVariant(block).with(PropertyDispatch.property(CastleDoorBlock.VANISHED).select(true, Variant.variant().with(VariantProperties.MODEL, door.apply(true))).select(false, Variant.variant().with(VariantProperties.MODEL, door.apply(false)))));
	}

	public void stairsBlock(Block block) {
		var mapping = TextureMapping.cube(block);
		ResourceLocation inner = ModelTemplates.STAIRS_INNER.createWithSuffix(block, "_inner", mapping, this.modelOutput);
		ResourceLocation straight = ModelTemplates.STAIRS_STRAIGHT.create(block, mapping, this.modelOutput);
		ResourceLocation outer = ModelTemplates.STAIRS_OUTER.createWithSuffix(block, "_outer", mapping, this.modelOutput);
		this.blockStateOutput.accept(createStairs(block, inner, straight, outer));
		this.registerSimpleItemModel(block, straight);
	}

	public void simpleBlockWithRenderType(Block block, String type) {
		this.blockWithRenderType(block, type, ModelTemplates.CUBE_ALL, TextureMapping::cube);
	}

	public void blockWithRenderType(Block block, String type, ModelTemplate template, Function<Block, TextureMapping> mapping) {
		this.blockStateOutput.accept(createSimpleBlock(block, template.extend().renderType(type).build().create(block, mapping.apply(block), this.modelOutput)));
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
	}
}
