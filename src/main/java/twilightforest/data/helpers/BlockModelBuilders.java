package twilightforest.data.helpers;

import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.BlockModelBuilder;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelBuilder;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;
import twilightforest.enums.HollowLogVariants;

import static twilightforest.TwilightForestMod.prefix;

public abstract class BlockModelBuilders extends BlockModelHelpers {

	public BlockModelBuilders(PackOutput output, ExistingFileHelper helper) {
		super(output, helper);
	}

	protected BlockModelBuilder makeTintedBlockAll(String name, ResourceLocation renderType) {
		return this.makeTintedBlock(name, renderType)
			.texture("north", "#all").texture("south", "#all").texture("east", "#all")
			.texture("west", "#all").texture("up", "#all").texture("down", "#all");
	}

	protected BlockModelBuilder makeTintedBlockColumn(String name) {
		return this.makeTintedBlock(name, SOLID)
			.texture("north", "#side").texture("south", "#side").texture("east", "#side")
			.texture("west", "#side").texture("up", "#end").texture("down", "#end");
	}

	protected BlockModelBuilder makeTintedBlockColumnUniqueBottom(String name) {
		return this.makeTintedBlock(name, SOLID)
			.texture("north", "#side").texture("south", "#side").texture("east", "#side")
			.texture("west", "#side").texture("up", "#top").texture("down", "#bottom");
	}

	protected BlockModelBuilder makeTintedBlock(String name, ResourceLocation renderType) {
		return models().withExistingParent(name, "minecraft:block/block").renderType(renderType).texture("particle", "#north")
			.element().from(0.0F, 0.0F, 0.0F).to(16.0F, 16.0F, 16.0F)
			.face(Direction.NORTH).texture("#north").cullface(Direction.NORTH).tintindex(0).end()
			.face(Direction.EAST).texture("#east").cullface(Direction.EAST).tintindex(0).end()
			.face(Direction.SOUTH).texture("#south").cullface(Direction.SOUTH).tintindex(0).end()
			.face(Direction.WEST).texture("#west").cullface(Direction.WEST).tintindex(0).end()
			.face(Direction.UP).texture("#up").cullface(Direction.UP).tintindex(0).end()
			.face(Direction.DOWN).texture("#down").cullface(Direction.DOWN).tintindex(0).end().end();
	}

	protected BlockModelBuilder makeEmissiveBlockAll(String name, ResourceLocation renderType, int emissivity) {
		return this.makeEmissiveBlock(name, renderType, emissivity)
			.texture("north", "#all").texture("south", "#all").texture("east", "#all")
			.texture("west", "#all").texture("up", "#all").texture("down", "#all");
	}

	protected BlockModelBuilder makeEmissiveBlock(String name, ResourceLocation renderType, int emissivity) {
		return models().withExistingParent(name, "minecraft:block/block").renderType(renderType).texture("particle", "#north")
			.element().from(0.0F, 0.0F, 0.0F).to(16.0F, 16.0F, 16.0F)
			.face(Direction.NORTH).texture("#north").cullface(Direction.NORTH).emissivity(emissivity, emissivity).tintindex(0).end()
			.face(Direction.EAST).texture("#east").cullface(Direction.EAST).emissivity(emissivity, emissivity).tintindex(0).end()
			.face(Direction.SOUTH).texture("#south").cullface(Direction.SOUTH).emissivity(emissivity, emissivity).tintindex(0).end()
			.face(Direction.WEST).texture("#west").cullface(Direction.WEST).emissivity(emissivity, emissivity).tintindex(0).end()
			.face(Direction.UP).texture("#up").cullface(Direction.UP).emissivity(emissivity, emissivity).tintindex(0).end()
			.face(Direction.DOWN).texture("#down").cullface(Direction.DOWN).emissivity(emissivity, emissivity).tintindex(0).end().end();
	}

	protected BlockModelBuilder makeTintedFlippedBlockAll(String name) {
		return models().withExistingParent(name, "minecraft:block/block").texture("particle", "#all")
			.element().from(0.0F, 0.0F, 0.0F).to(16.0F, 16.0F, 16.0F)
			.face(Direction.NORTH).uvs(0.0F, 16.0F, 16.0F, 0.0F).texture("#all").cullface(Direction.NORTH).tintindex(0).end()
			.face(Direction.EAST).uvs(0.0F, 16.0F, 16.0F, 0.0F).texture("#all").cullface(Direction.EAST).tintindex(0).end()
			.face(Direction.SOUTH).uvs(0.0F, 16.0F, 16.0F, 0.0F).texture("#all").cullface(Direction.SOUTH).tintindex(0).end()
			.face(Direction.WEST).uvs(0.0F, 16.0F, 16.0F, 0.0F).texture("#all").cullface(Direction.WEST).tintindex(0).end()
			.face(Direction.UP).uvs(0.0F, 16.0F, 16.0F, 0.0F).texture("#all").cullface(Direction.UP).tintindex(0).end()
			.face(Direction.DOWN).uvs(0.0F, 16.0F, 16.0F, 0.0F).texture("#all").cullface(Direction.DOWN).tintindex(0).end().end();
	}

	protected BlockModelBuilder makeTintedSlab(String name) {
		return models().withExistingParent(name, "minecraft:block/block").texture("particle", "#side")
			.element().from(0.0F, 0.0F, 0.0F).to(16.0F, 8.0F, 16.0F)
			.face(Direction.NORTH).texture("#side").cullface(Direction.NORTH).tintindex(0).end()
			.face(Direction.EAST).texture("#side").cullface(Direction.EAST).tintindex(0).end()
			.face(Direction.SOUTH).texture("#side").cullface(Direction.SOUTH).tintindex(0).end()
			.face(Direction.WEST).texture("#side").cullface(Direction.WEST).tintindex(0).end()
			.face(Direction.UP).texture("#top").tintindex(0).end()
			.face(Direction.DOWN).texture("#bottom").cullface(Direction.DOWN).tintindex(0).end().end();
	}

	protected BlockModelBuilder make4x4x4SmallCube(String name) {
		return models().withExistingParent(name, "minecraft:block/block").renderType(CUTOUT).texture("particle", "#all")
			.element().from(6.0F, 6.0F, 6.0F).to(10.0F, 10.0F, 10.0F)
			.face(Direction.NORTH).texture("#all").cullface(Direction.NORTH).emissivity(15, 15).end()
			.face(Direction.EAST).texture("#all").cullface(Direction.EAST).emissivity(15, 15).end()
			.face(Direction.SOUTH).texture("#all").cullface(Direction.SOUTH).emissivity(15, 15).end()
			.face(Direction.WEST).texture("#all").cullface(Direction.WEST).emissivity(15, 15).end()
			.face(Direction.UP).texture("#all").cullface(Direction.UP).emissivity(15, 15).end()
			.face(Direction.DOWN).texture("#all").cullface(Direction.DOWN).emissivity(15, 15).end().end();
	}

	protected BlockModelBuilder makeCubeWithTopLayer(String name, ResourceLocation renderType, int layer1em, int layer2em) {
		return models().withExistingParent(name, "minecraft:block/block").renderType(renderType).texture("particle", "#bottom")
			.element().from(0.0F, 0.0F, 0.0F).to(16.0F, 16.0F, 16.0F)
			.face(Direction.NORTH).texture("#north").cullface(Direction.NORTH).emissivity(layer1em, layer1em).end()
			.face(Direction.EAST).texture("#east").cullface(Direction.EAST).emissivity(layer1em, layer1em).end()
			.face(Direction.SOUTH).texture("#south").cullface(Direction.SOUTH).emissivity(layer1em, layer1em).end()
			.face(Direction.WEST).texture("#west").cullface(Direction.WEST).emissivity(layer1em, layer1em).end()
			.face(Direction.UP).texture("#top").cullface(Direction.UP).emissivity(layer1em, layer1em).end()
			.face(Direction.DOWN).texture("#bottom").cullface(Direction.DOWN).emissivity(layer1em, layer1em).end().end()
			.element().from(0.0F, 0.0F, 0.0F).to(16.0F, 16.0F, 16.0F)
			.face(Direction.UP).texture("#top2").cullface(Direction.UP).emissivity(layer2em, layer2em).end().end();
	}

	protected BlockModelBuilder make2LayerCubeAllSidesSame(String name, ResourceLocation renderType, int layer1em, int layer2em, boolean shade) {
		return this.make2LayerCube(name, renderType,
				layer1em, layer1em, layer1em, layer1em, layer1em, layer1em,
				layer2em, layer2em, layer2em, layer2em, layer2em, layer2em, shade)
			.texture("north", "#all").texture("south", "#all").texture("east", "#all")
			.texture("west", "#all").texture("top", "#all").texture("bottom", "#all")
			.texture("north2", "#all2").texture("south2", "#all2").texture("east2", "#all2")
			.texture("west2", "#all2").texture("top2", "#all2").texture("bottom2", "#all2");
	}

	protected BlockModelBuilder make2LayerCube(String name, ResourceLocation renderType,
											   int layer1emN, int layer1emS, int layer1emW, int layer1emE, int layer1emU, int layer1emD,
											   int layer2emN, int layer2emS, int layer2emW, int layer2emE, int layer2emU, int layer2emD, boolean shade) {
		return models().withExistingParent(name, "minecraft:block/block").renderType(renderType).texture("particle", "#bottom")
			.element().from(0.0F, 0.0F, 0.0F).to(16.0F, 16.0F, 16.0F).shade(shade)
			.face(Direction.NORTH).texture("#north").cullface(Direction.NORTH).emissivity(layer1emN, layer1emN).end()
			.face(Direction.EAST).texture("#east").cullface(Direction.EAST).emissivity(layer1emE, layer1emE).end()
			.face(Direction.SOUTH).texture("#south").cullface(Direction.SOUTH).emissivity(layer1emS, layer1emS).end()
			.face(Direction.WEST).texture("#west").cullface(Direction.WEST).emissivity(layer1emW, layer1emW).end()
			.face(Direction.UP).texture("#top").cullface(Direction.UP).emissivity(layer1emU, layer1emU).end()
			.face(Direction.DOWN).texture("#bottom").cullface(Direction.DOWN).emissivity(layer1emD, layer1emD).end().end()
			.element().from(0.0F, 0.0F, 0.0F).to(16.0F, 16.0F, 16.0F)
			.face(Direction.NORTH).texture("#north2").cullface(Direction.NORTH).emissivity(layer2emN, layer2emN).tintindex(0).end()
			.face(Direction.EAST).texture("#east2").cullface(Direction.EAST).emissivity(layer2emE, layer2emE).tintindex(0).end()
			.face(Direction.SOUTH).texture("#south2").cullface(Direction.SOUTH).emissivity(layer2emS, layer2emS).tintindex(0).end()
			.face(Direction.WEST).texture("#west2").cullface(Direction.WEST).emissivity(layer2emW, layer2emW).tintindex(0).end()
			.face(Direction.UP).texture("#top2").cullface(Direction.UP).emissivity(layer2emU, layer2emU).tintindex(0).end()
			.face(Direction.DOWN).texture("#bottom2").cullface(Direction.DOWN).emissivity(layer2emD, layer2emD).tintindex(0).end().end();
	}

	protected BlockModelBuilder make2LayerCubeNoBottom(String name, ResourceLocation renderType, int layer1em, int layer2em, boolean shade) {
		return models().withExistingParent(name, "minecraft:block/block").renderType(renderType).texture("particle", "#bottom")
			.element().from(0.0F, 0.0F, 0.0F).to(16.0F, 16.0F, 16.0F).shade(shade)
			.face(Direction.NORTH).texture("#north").cullface(Direction.NORTH).emissivity(layer1em, layer1em).end()
			.face(Direction.EAST).texture("#east").cullface(Direction.EAST).emissivity(layer1em, layer1em).end()
			.face(Direction.SOUTH).texture("#south").cullface(Direction.SOUTH).emissivity(layer1em, layer1em).end()
			.face(Direction.WEST).texture("#west").cullface(Direction.WEST).emissivity(layer1em, layer1em).end()
			.face(Direction.UP).texture("#top").cullface(Direction.UP).emissivity(layer1em, layer1em).end()
			.face(Direction.DOWN).texture("#bottom").cullface(Direction.DOWN).emissivity(layer1em, layer1em).end().end()
			.element().from(0.0F, 0.0F, 0.0F).to(16.0F, 16.0F, 16.0F).shade(shade)
			.face(Direction.NORTH).texture("#north2").cullface(Direction.NORTH).emissivity(layer2em, layer2em).end()
			.face(Direction.EAST).texture("#east2").cullface(Direction.EAST).emissivity(layer2em, layer2em).end()
			.face(Direction.SOUTH).texture("#south2").cullface(Direction.SOUTH).emissivity(layer2em, layer2em).end()
			.face(Direction.WEST).texture("#west2").cullface(Direction.WEST).emissivity(layer2em, layer2em).end()
			.face(Direction.UP).texture("#top2").cullface(Direction.UP).emissivity(layer2em, layer2em).end().end();
	}

	protected BlockModelBuilder make3LayerCubeAllSidesSame(String name, ResourceLocation renderType, int layer1em, int layer2em, int layer3em) {
		return this.make3LayerCube(name, renderType,
				layer1em, layer1em, layer1em, layer1em, layer1em, layer1em,
				layer2em, layer2em, layer2em, layer2em, layer2em, layer2em,
				layer3em, layer3em, layer3em, layer3em, layer3em, layer3em)
			.texture("north", "#all").texture("south", "#all").texture("east", "#all")
			.texture("west", "#all").texture("top", "#all").texture("bottom", "#all")
			.texture("north2", "#all2").texture("south2", "#all2").texture("east2", "#all2")
			.texture("west2", "#all2").texture("top2", "#all2").texture("bottom2", "#all2")
			.texture("north3", "#all3").texture("south3", "#all3").texture("east3", "#all3")
			.texture("west3", "#all3").texture("top3", "#all3").texture("bottom3", "#all3");
	}

	protected BlockModelBuilder make3LayerCube(String name, ResourceLocation renderType,
											   int layer1emN, int layer1emS, int layer1emW, int layer1emE, int layer1emU, int layer1emD,
											   int layer2emN, int layer2emS, int layer2emW, int layer2emE, int layer2emU, int layer2emD,
											   int layer3emN, int layer3emS, int layer3emW, int layer3emE, int layer3emU, int layer3emD) {
		return models().withExistingParent(name, "minecraft:block/block").renderType(renderType).texture("particle", "#bottom")
			.element().from(0.0F, 0.0F, 0.0F).to(16.0F, 16.0F, 16.0F)
			.face(Direction.NORTH).texture("#north").cullface(Direction.NORTH).emissivity(layer1emN, layer1emN).end()
			.face(Direction.EAST).texture("#east").cullface(Direction.EAST).emissivity(layer1emE, layer1emE).end()
			.face(Direction.SOUTH).texture("#south").cullface(Direction.SOUTH).emissivity(layer1emS, layer1emS).end()
			.face(Direction.WEST).texture("#west").cullface(Direction.WEST).emissivity(layer1emW, layer1emW).end()
			.face(Direction.UP).texture("#top").cullface(Direction.UP).emissivity(layer1emU, layer1emU).end()
			.face(Direction.DOWN).texture("#bottom").cullface(Direction.DOWN).emissivity(layer1emD, layer1emD).end().end()
			.element().from(0.0F, 0.0F, 0.0F).to(16.0F, 16.0F, 16.0F)
			.face(Direction.NORTH).texture("#north2").cullface(Direction.NORTH).emissivity(layer2emN, layer2emN).end()
			.face(Direction.EAST).texture("#east2").cullface(Direction.EAST).emissivity(layer2emE, layer2emE).end()
			.face(Direction.SOUTH).texture("#south2").cullface(Direction.SOUTH).emissivity(layer2emS, layer2emS).end()
			.face(Direction.WEST).texture("#west2").cullface(Direction.WEST).emissivity(layer2emW, layer2emW).end()
			.face(Direction.UP).texture("#top2").cullface(Direction.UP).emissivity(layer2emU, layer2emU).end()
			.face(Direction.DOWN).texture("#bottom2").cullface(Direction.DOWN).emissivity(layer2emD, layer2emD).end().end()
			.element().from(0.0F, 0.0F, 0.0F).to(16.0F, 16.0F, 16.0F)
			.face(Direction.NORTH).texture("#north3").cullface(Direction.NORTH).emissivity(layer3emN, layer3emN).end()
			.face(Direction.EAST).texture("#east3").cullface(Direction.EAST).emissivity(layer3emE, layer3emE).end()
			.face(Direction.SOUTH).texture("#south3").cullface(Direction.SOUTH).emissivity(layer3emS, layer3emS).end()
			.face(Direction.WEST).texture("#west3").cullface(Direction.WEST).emissivity(layer3emW, layer3emW).end()
			.face(Direction.UP).texture("#top3").cullface(Direction.UP).emissivity(layer3emU, layer3emU).end()
			.face(Direction.DOWN).texture("#bottom3").cullface(Direction.DOWN).emissivity(layer3emD, layer3emD).end().end();
	}

	protected BlockModelBuilder make3LayerCubeIdenticalSides1Bottom(String name, int layer1em, int layer2emTop, int layer2emSides, int layer3emTop, int layer3emSides) {
		return this.make3LayerCubeIdenticalSides1Bottom(name,
			layer1em, layer1em, layer1em, layer1em, layer1em, layer1em,
			layer2emSides, layer2emSides, layer2emSides, layer2emSides, layer2emTop,
			layer3emSides, layer3emSides, layer3emSides, layer3emSides, layer3emTop);
	}

	protected BlockModelBuilder make3LayerCubeIdenticalSides1Bottom(String name,
																	int layer1emN, int layer1emS, int layer1emW, int layer1emE, int layer1emU, int layer1emD,
																	int layer2emN, int layer2emS, int layer2emW, int layer2emE, int layer2emU,
																	int layer3emN, int layer3emS, int layer3emW, int layer3emE, int layer3emU) {
		return models().withExistingParent(name, "minecraft:block/block").renderType(CUTOUT).texture("particle", "#bottom")
			.element().from(0.0F, 0.0F, 0.0F).to(16.0F, 16.0F, 16.0F)
			.face(Direction.NORTH).texture("#side").cullface(Direction.NORTH).emissivity(layer1emN, layer1emN).end()
			.face(Direction.EAST).texture("#side").cullface(Direction.EAST).emissivity(layer1emE, layer1emE).end()
			.face(Direction.SOUTH).texture("#side").cullface(Direction.SOUTH).emissivity(layer1emS, layer1emS).end()
			.face(Direction.WEST).texture("#side").cullface(Direction.WEST).emissivity(layer1emW, layer1emW).end()
			.face(Direction.UP).texture("#top").cullface(Direction.UP).emissivity(layer1emU, layer1emU).end()
			.face(Direction.DOWN).texture("#bottom").cullface(Direction.DOWN).emissivity(layer1emD, layer1emD).end().end()
			.element().from(0.0F, 0.0F, 0.0F).to(16.0F, 16.0F, 16.0F)
			.face(Direction.NORTH).texture("#side2").cullface(Direction.NORTH).emissivity(layer2emN, layer2emN).end()
			.face(Direction.EAST).texture("#side2").cullface(Direction.EAST).emissivity(layer2emE, layer1emE).end()
			.face(Direction.SOUTH).texture("#side2").cullface(Direction.SOUTH).emissivity(layer2emS, layer1emS).end()
			.face(Direction.WEST).texture("#side2").cullface(Direction.WEST).emissivity(layer2emW, layer1emW).end()
			.face(Direction.UP).texture("#top2").cullface(Direction.UP).emissivity(layer2emU, layer1emU).end().end()
			.element().from(0.0F, 0.0F, 0.0F).to(16.0F, 16.0F, 16.0F)
			.face(Direction.NORTH).texture("#side3").cullface(Direction.NORTH).emissivity(layer3emN, layer1emN).end()
			.face(Direction.EAST).texture("#side3").cullface(Direction.EAST).emissivity(layer3emE, layer3emE).end()
			.face(Direction.SOUTH).texture("#side3").cullface(Direction.SOUTH).emissivity(layer3emS, layer3emS).end()
			.face(Direction.WEST).texture("#side3").cullface(Direction.WEST).emissivity(layer3emW, layer3emW).end()
			.face(Direction.UP).texture("#top3").cullface(Direction.UP).emissivity(layer3emU, layer3emU).end().end();
	}

	protected BlockModelBuilder make2layerCross(String name, ResourceLocation renderType, int layer1em, int layer2em) {
		return models().getBuilder(name).ao(false).texture("particle", "#cross").renderType(renderType)
			.element().from(0.8F, 0.0F, 8.0F).to(15.2F, 16.0F, 8.0F)
			.rotation().origin(8.0F, 8.0F, 8.0F).axis(Direction.Axis.Y).angle(45.0F).rescale(true).end()
			.shade(false)
			.face(Direction.NORTH).uvs(0.0F, 0.0F, 16.0F, 16.0F).texture("#cross").emissivity(layer1em, layer1em).end()
			.face(Direction.SOUTH).uvs(0.0F, 0.0F, 16.0F, 16.0F).texture("#cross").emissivity(layer1em, layer1em).end().end()
			.element().from(8.0F, 0.0F, 0.8F).to(8.0F, 16.0F, 15.2F)
			.rotation().origin(8.0F, 8.0F, 8.0F).axis(Direction.Axis.Y).angle(45.0F).rescale(true).end()
			.shade(false)
			.face(Direction.WEST).uvs(0.0F, 0.0F, 16.0F, 16.0F).texture("#cross").emissivity(layer1em, layer1em).end()
			.face(Direction.EAST).uvs(0.0F, 0.0F, 16.0F, 16.0F).texture("#cross").emissivity(layer1em, layer1em).end().end()
			.element().from(0.8F, 0.0F, 8.0F).to(15.2F, 16.0F, 8.0F)
			.rotation().origin(8.0F, 8.0F, 8.0F).axis(Direction.Axis.Y).angle(45.0F).rescale(true).end()
			.shade(false)
			.face(Direction.NORTH).uvs(0.0F, 0.0F, 16.0F, 16.0F).texture("#cross2").emissivity(layer2em, layer2em).end()
			.face(Direction.SOUTH).uvs(0.0F, 0.0F, 16.0F, 16.0F).texture("#cross2").emissivity(layer2em, layer2em).end().end()
			.element().from(8.0F, 0.0F, 0.8F).to(8.0F, 16.0F, 15.2F)
			.rotation().origin(8.0F, 8.0F, 8.0F).axis(Direction.Axis.Y).angle(45.0F).rescale(true).end()
			.shade(false)
			.face(Direction.WEST).uvs(0.0F, 0.0F, 16.0F, 16.0F).texture("#cross2").emissivity(layer2em, layer2em).end()
			.face(Direction.EAST).uvs(0.0F, 0.0F, 16.0F, 16.0F).texture("#cross2").emissivity(layer2em, layer2em).end().end();
	}

	protected BlockModelBuilder buildFallenLeaves(int index) {
		return models().getBuilder("fallen_leaves" + index).renderType(CUTOUT).texture("particle", "#all").texture("all", "minecraft:block/oak_leaves")
			.element().from(0, 0, 0).to(16, index == 1 ? 0.2F : 2.0F * (index - 1), 16)
			.face(Direction.UP).uvs(0, 0, 16, 16).tintindex(0).texture("#all").end()
			.face(Direction.DOWN).uvs(0, 0, 16, 16).tintindex(0).texture("#all").end()
			.face(Direction.NORTH).uvs(0, 0, 16, index == 1 ? 0.2F : 2.0F * (index - 1)).tintindex(0).texture("#all").end()
			.face(Direction.SOUTH).uvs(0, 0, 16, index == 1 ? 0.2F : 2.0F * (index - 1)).tintindex(0).texture("#all").end()
			.face(Direction.EAST).uvs(0, 0, 16, index == 1 ? 0.2F : 2.0F * (index - 1)).tintindex(0).texture("#all").end()
			.face(Direction.WEST).uvs(0, 0, 16, index == 1 ? 0.2F : 2.0F * (index - 1)).tintindex(0).texture("#all").end()
			.end();
	}

	protected ModelFile buildCandelabra() {
		return this.models().withExistingParent("candelabra", "minecraft:block/block").renderType(CUTOUT).texture("particle", "#candelabra").texture("candelabra", "block/candelabra")
			.element().from(0, 1, 8).to(16, 7, 8).face(Direction.NORTH).uvs(0, 0, 16, 6).texture("#candelabra").end().face(Direction.SOUTH).uvs(16, 0, 0, 6).texture("#candelabra").end().end()
			.element().from(8, 1, 5).to(8, 7, 11).face(Direction.EAST).uvs(0, 6, 6, 12).texture("#candelabra").end().face(Direction.WEST).uvs(6, 6, 0, 12).texture("#candelabra").end().end()
			.element().from(1, 7, 6).to(5, 8, 10).allFaces((direction, builder) -> builder.uvs(0, 12, 4, direction.getAxis().isHorizontal() ? 13 : 16).texture("#candelabra")).end()
			.element().from(6, 7, 6).to(10, 8, 10).allFaces((direction, builder) -> builder.uvs(0, 12, 4, direction.getAxis().isHorizontal() ? 13 : 16).texture("#candelabra")).end()
			.element().from(11, 7, 6).to(15, 8, 10).allFaces((direction, builder) -> builder.uvs(0, 12, 4, direction.getAxis().isHorizontal() ? 13 : 16).texture("#candelabra")).end()
			.element().from(6, 0, 6).to(10, 1, 10).allFaces((direction, builder) -> builder.uvs(0, 12, 4, direction.getAxis().isHorizontal() ? 13 : 16).texture("#candelabra")).end()
			;
	}

	protected ModelFile buildWallCandelabra() {
		return this.models().withExistingParent("wall_candelabra", "minecraft:block/block").renderType(CUTOUT).texture("particle", "#candelabra").texture("candelabra", "block/candelabra")
			.element().from(0, 1, 12).to(16, 7, 12).face(Direction.NORTH).uvs(0, 0, 16, 6).texture("#candelabra").end().face(Direction.SOUTH).uvs(16, 0, 0, 6).texture("#candelabra").end().end()
			.element().from(8, 1, 9).to(8, 7, 15).face(Direction.EAST).uvs(0, 6, 6, 12).texture("#candelabra").end().face(Direction.WEST).uvs(6, 6, 0, 12).texture("#candelabra").end().end()
			.element().from(1, 7, 10).to(5, 8, 14).allFaces((direction, builder) -> builder.uvs(0, 12, 4, direction.getAxis().isHorizontal() ? 13 : 16).texture("#candelabra")).end()
			.element().from(6, 7, 10).to(10, 8, 14).allFaces((direction, builder) -> builder.uvs(0, 12, 4, direction.getAxis().isHorizontal() ? 13 : 16).texture("#candelabra")).end()
			.element().from(11, 7, 10).to(15, 8, 14).allFaces((direction, builder) -> builder.uvs(0, 12, 4, direction.getAxis().isHorizontal() ? 13 : 16).texture("#candelabra")).end()
			.element().from(6, 2, 15).to(10, 6, 16).allFaces((direction, builder) -> builder.uvs(direction.getAxis() == Direction.Axis.X ? 3 : 0, 12, 4, direction.getAxis() == Direction.Axis.Y ? 13 : 16).texture("#candelabra")).end()
			;
	}

	protected BlockModelBuilder buildVerticalLog(@Nullable HollowLogVariants.Climbable variant) {
		BlockModelBuilder model = this.models().withExistingParent(variant == null ? "vertical_hollow_log" : "vertical_hollow_log_" + variant.getSerializedName(), "minecraft:block/block").texture("particle", "#side")
			.element().from(0, 0, 0).to(2, 16, 16).allFaces(((dir, builder) -> builder.cullface(dir).texture(dir == Direction.EAST ? "#inner" : dir.getAxis() == Direction.Axis.Y ? "#top" : "#side"))).face(Direction.EAST).cullface(null).end().end()
			.element().from(14, 0, 0).to(16, 16, 16).allFaces(((dir, builder) -> builder.cullface(dir).texture(dir == Direction.WEST ? "#inner" : dir.getAxis() == Direction.Axis.Y ? "#top" : "#side"))).face(Direction.WEST).cullface(null).end().end()
			.element().from(2, 0, 0).to(14, 16, 2).allFaces(((dir, builder) -> builder.cullface(dir).texture(dir == Direction.SOUTH ? "#inner" : dir.getAxis() == Direction.Axis.Y ? "#top" : "#side"))).face(Direction.SOUTH).cullface(null).end().end()
			.element().from(2, 0, 14).to(14, 16, 16).allFaces(((dir, builder) -> builder.cullface(dir).texture(dir == Direction.NORTH ? "#inner" : dir.getAxis() == Direction.Axis.Y ? "#top" : "#side"))).face(Direction.NORTH).cullface(null).end().end();

		if (variant != null) model.element().from(2, 0, 2.8f).to(14, 16, 2.8f)
			.face(Direction.NORTH).end().face(Direction.SOUTH).end()
			.faces((dir, builder) -> builder.texture("#climbable").tintindex(1)).shade(false).end()
			.renderType(CUTOUT).texture("climbable", "minecraft:block/" + variant.getSerializedName());

		return model;
	}

	protected BlockModelBuilder buildHorizontalHollowLog(boolean carpet, boolean grass) {
		final int height = carpet ? 3 : 2;
		final int heightInv = 16 - height;
		grass &= carpet;

		// Top, Bottom, Left side, Right side
		BlockModelBuilder model = this.models().withExistingParent(carpet ? (grass ? "horizontal_hollow_log_plant" : "horizontal_hollow_log_carpet") : "horizontal_hollow_log", "minecraft:block/block").renderType(CUTOUT).texture("particle", "#side")
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
			.faces((dir, builder) -> builder.cullface(dir == Direction.WEST ? null : dir).texture(dir.getAxis() == Direction.Axis.Z ? "#top" : dir == Direction.WEST ? "#inner" : "#side")).end();

		if (carpet)
			model.element().from(0, 0, 0).to(16, height, 16).face(Direction.NORTH).end().face(Direction.SOUTH).end().faces((dir, builder) -> builder.uvs(0, 16 - height, 16, 16).texture("#overhang"));
		if (grass) {
			model.element().from(0.8f, height, 8).to(15.2f, 14, 8).rotation().origin(8, 8, 8).axis(Direction.Axis.Y).angle(45).rescale(true).end().shade(false).face(Direction.NORTH).end().face(Direction.SOUTH).end().faces((direction, faceBuilder) -> faceBuilder.uvs(0, height, 16, 14).texture("#plant").tintindex(1));
			model.element().from(8, height, 0.8f).to(8, 14, 15.2f).rotation().origin(8, 8, 8).axis(Direction.Axis.Y).angle(45).rescale(true).end().shade(false).face(Direction.WEST).end().face(Direction.EAST).end().faces((direction, faceBuilder) -> faceBuilder.uvs(0, height, 16, 14).texture("#plant").tintindex(1));
		}

		return model;
	}

	protected BlockModelBuilder makePedestal(String name, boolean has2layers) {
		BlockModelBuilder model = models().withExistingParent(name, "minecraft:block/block").renderType(TRANSLUCENT).texture("particle", "#end")
			.element().from(2.0F, 3.0F, 2.0F).to(14.0F, 13.0F, 14.0F)
			.face(Direction.NORTH).texture("#north").emissivity(0, 0).end().face(Direction.SOUTH).texture("#south").emissivity(0, 0).end()
			.face(Direction.WEST).texture("#west").emissivity(0, 0).end().face(Direction.EAST).texture("#east").emissivity(0, 0).end().end()
			.element().from(1.0F, 0.0F, 1.0F).to(15.0F, 3.0F, 15.0F)
			.face(Direction.UP).texture("#end").emissivity(0, 0).end().face(Direction.DOWN).texture("#end").emissivity(0, 0).cullface(Direction.DOWN).end()
			.face(Direction.NORTH).texture("#north").emissivity(0, 0).end().face(Direction.SOUTH).texture("#south").emissivity(0, 0).end()
			.face(Direction.WEST).texture("#west").emissivity(0, 0).end().face(Direction.EAST).texture("#east").emissivity(0, 0).end().end()
			.element().from(1.0F, 13.0F, 1.0F).to(15.0F, 16.0F, 15.0F)
			.face(Direction.UP).texture("#end").emissivity(0, 0).cullface(Direction.UP).end().face(Direction.DOWN).texture("#end").emissivity(0, 0).end()
			.face(Direction.NORTH).texture("#north").emissivity(0, 0).end().face(Direction.SOUTH).texture("#south").emissivity(0, 0).end()
			.face(Direction.WEST).texture("#west").emissivity(0, 0).end().face(Direction.EAST).texture("#east").emissivity(0, 0).end().end()
			.element().from(1.0F, 12.0F, 1.0F).to(4.0F, 13.0F, 4.0F)
			.face(Direction.DOWN).texture("#end").emissivity(0, 0).end()
			.face(Direction.NORTH).texture("#north").emissivity(0, 0).end().face(Direction.SOUTH).texture("#south").emissivity(0, 0).end()
			.face(Direction.WEST).texture("#west").emissivity(0, 0).end().face(Direction.EAST).texture("#east").emissivity(0, 0).end().end()
			.element().from(12.0F, 12.0F, 1.0F).to(15.0F, 13.0F, 4.0F)
			.face(Direction.DOWN).texture("#end").emissivity(0, 0).end()
			.face(Direction.NORTH).texture("#north").emissivity(0, 0).end().face(Direction.SOUTH).texture("#south").emissivity(0, 0).end()
			.face(Direction.WEST).texture("#west").emissivity(0, 0).end().face(Direction.EAST).texture("#east").emissivity(0, 0).end().end()
			.element().from(1.0F, 12.0F, 12.0F).to(4.0F, 13.0F, 15.0F)
			.face(Direction.DOWN).texture("#end").emissivity(0, 0).end()
			.face(Direction.NORTH).texture("#north").emissivity(0, 0).end().face(Direction.SOUTH).texture("#south").emissivity(0, 0).end()
			.face(Direction.WEST).texture("#west").emissivity(0, 0).end().face(Direction.EAST).texture("#east").emissivity(0, 0).end().end()
			.element().from(12.0F, 12.0F, 12.0F).to(15.0F, 13.0F, 15.0F)
			.face(Direction.DOWN).texture("#end").emissivity(0, 0).end()
			.face(Direction.NORTH).texture("#north").emissivity(0, 0).end().face(Direction.SOUTH).texture("#south").emissivity(0, 0).end()
			.face(Direction.WEST).texture("#west").emissivity(0, 0).end().face(Direction.EAST).texture("#east").emissivity(0, 0).end().end();

		if (has2layers) {
			model = model.element().from(2.0F, 3.0F, 2.0F).to(14.0F, 13.0F, 14.0F)
				.face(Direction.NORTH).texture("#north2").emissivity(15, 15).end()
				.face(Direction.SOUTH).texture("#south2").emissivity(15, 15).end()
				.face(Direction.WEST).texture("#west2").emissivity(15, 15).end()
				.face(Direction.EAST).texture("#east2").emissivity(15, 15).end().end()
				.element().from(1.0F, 0.0F, 1.0F).to(15.0F, 16.0F, 15.0F)
				.face(Direction.DOWN).texture("#end2").emissivity(12, 12).cullface(Direction.DOWN).end()
				.face(Direction.UP).texture("#end2").emissivity(12, 12).cullface(Direction.UP).end().end()
				.element().from(1.0F, 12.0F, 1.0F).to(4.0F, 13.0F, 4.0F)
				.face(Direction.NORTH).texture("#north2").emissivity(15, 15).end()
				.face(Direction.SOUTH).texture("#south2").emissivity(15, 15).end()
				.face(Direction.WEST).texture("#west2").emissivity(15, 15).end()
				.face(Direction.EAST).texture("#east2").emissivity(15, 15).end().end()
				.element().from(12.0F, 12.0F, 1.0F).to(15.0F, 13.0F, 4.0F)
				.face(Direction.NORTH).texture("#north2").emissivity(15, 15).end()
				.face(Direction.SOUTH).texture("#south2").emissivity(15, 15).end()
				.face(Direction.WEST).texture("#west2").emissivity(15, 15).end()
				.face(Direction.EAST).texture("#east2").emissivity(15, 15).end().end()
				.element().from(1.0F, 12.0F, 12.0F).to(4.0F, 13.0F, 15.0F)
				.face(Direction.NORTH).texture("#north2").emissivity(15, 15).end()
				.face(Direction.SOUTH).texture("#south2").emissivity(15, 15).end()
				.face(Direction.WEST).texture("#west2").emissivity(15, 15).end()
				.face(Direction.EAST).texture("#east2").emissivity(15, 15).end().end()
				.element().from(12.0F, 12.0F, 12.0F).to(15.0F, 13.0F, 15.0F)
				.face(Direction.NORTH).texture("#north2").emissivity(15, 15).end()
				.face(Direction.SOUTH).texture("#south2").emissivity(15, 15).end()
				.face(Direction.WEST).texture("#west2").emissivity(15, 15).end()
				.face(Direction.EAST).texture("#east2").emissivity(15, 15).end().end();

			model = model.element().from(2.0F, 3.0F, 2.0F).to(14.0F, 13.0F, 14.0F)
				.face(Direction.NORTH).texture("#north3").emissivity(10, 10).end()
				.face(Direction.SOUTH).texture("#south3").emissivity(10, 10).end()
				.face(Direction.WEST).texture("#west3").emissivity(10, 10).end()
				.face(Direction.EAST).texture("#east3").emissivity(10, 10).end().end();
		}

		return model;
	}

	protected BlockModelBuilder makeJar(String name) {
		return models().withExistingParent(name, "minecraft:block/block").renderType(CUTOUT)
			.texture("particle", "#side")
			.texture("side", prefix("block/jar_side"))
			.texture("bottom", prefix("block/jar_bottom"))
			.texture("top", prefix("block/jar_top"))
			.element().from(3.0F, 0.0F, 3.0F).to(13.0F, 14.0F, 13.0F)
			.face(Direction.UP).texture("#top").end()
			.face(Direction.DOWN).texture("#bottom").cullface(Direction.DOWN).end()
			.face(Direction.NORTH).texture("#side").end()
			.face(Direction.SOUTH).texture("#side").end()
			.face(Direction.WEST).texture("#side").end()
			.face(Direction.EAST).texture("#side").end().end();
	}

	protected BlockModelBuilder cubeAllTinted(String name, String all, boolean flipV) {
		return (flipV ? this.makeTintedFlippedBlockAll(name) : this.makeTintedBlockAll(name, SOLID)).texture("all", "block/" + all);
	}

	protected BlockModelBuilder cubeAllTinted(String name, String all) {
		return cubeAllTinted(name, all, false);
	}

	protected void tintedAndFlipped(Block b) {
		simpleBlock(b, ConfiguredModel.builder()
			.modelFile(cubeAllTinted(name(b), name(b))).nextModel()
			.modelFile(cubeAllTinted(name(b) + "_flipped", name(b), true)).build()
		);
	}
}
