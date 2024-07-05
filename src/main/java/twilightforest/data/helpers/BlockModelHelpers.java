package twilightforest.data.helpers;

import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.block.state.properties.StairsShape;
import net.neoforged.neoforge.client.model.generators.*;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredHolder;
import twilightforest.TwilightForestMod;
import twilightforest.block.BanisterBlock;

import java.util.function.Consumer;

import static twilightforest.TwilightForestMod.prefix;

public abstract class BlockModelHelpers extends BlockStateProvider {

	protected static final ResourceLocation SOLID = ResourceLocation.withDefaultNamespace("solid");
	protected static final ResourceLocation CUTOUT = ResourceLocation.withDefaultNamespace("cutout");
	protected static final ResourceLocation CUTOUT_MIPPED = ResourceLocation.withDefaultNamespace("cutout_mipped");
	protected static final ResourceLocation TRANSLUCENT = ResourceLocation.withDefaultNamespace("translucent");

	public BlockModelHelpers(PackOutput output, ExistingFileHelper exFileHelper) {
		super(output, TwilightForestMod.ID, exFileHelper);
	}

	protected void builtinEntity(Block b, String particle) {
		simpleBlock(b, models().getBuilder(name(b))
			.parent(new ModelFile.UncheckedModelFile("builtin/entity"))
			.texture("particle", particle));
	}

	protected void simpleBlockExisting(Block b) {
		simpleBlock(b, new ConfiguredModel(models().getExistingFile(prefix(name(b)))));
	}

	public void simpleBlockWithRenderType(Block block, ResourceLocation type) {
		simpleBlock(block, models().cubeAll(name(block), blockTexture(block)).renderType(type));
	}

	/**
	 * Generates a blockstate json pointing to one single generated model with the same name as the block
	 * The single generated model has the given parent and can be customized.
	 */
	protected void singleBlockBoilerPlate(Block b, String parent, Consumer<BlockModelBuilder> modelCustomizer) {
		BlockModelBuilder bModel = models().withExistingParent(name(b), parent);
		modelCustomizer.accept(bModel);
		simpleBlock(b, bModel);
	}

	protected void logWoodSapling(RotatedPillarBlock log, RotatedPillarBlock slog, RotatedPillarBlock wood, RotatedPillarBlock swood, Block sapling) {
		logBlock(log);
		logBlock(slog);
		ResourceLocation sideTex = blockTexture(log);
		axisBlock(wood, sideTex, sideTex);
		ResourceLocation sSideTex = blockTexture(slog);
		axisBlock(swood, sSideTex, sSideTex);

		ResourceLocation saplingTex = prefix("block/" + name(sapling));
		simpleBlock(sapling, models().cross(name(sapling), saplingTex).renderType(CUTOUT));
	}

	protected void plankBlocks(String variant, Block plank, Block slab, StairBlock stair, Block button, Block fence, Block gate, Block plate, DoorBlock door, TrapDoorBlock trapdoor, BanisterBlock banister) {
		this.plankBlocks(variant, plank, slab, stair, button, fence, gate, plate, door, trapdoor, false, false, banister);
	}

	protected void plankBlocks(String variant, Block plank, Block slab, StairBlock stair, Block button, Block fence, Block gate, Block plate, DoorBlock door, TrapDoorBlock trapdoor, boolean cutoutDoors, boolean correctDoors, BanisterBlock banister) {
		String plankTexName = "planks_" + variant;
		String plankDir = "block/wood/planks/" + variant + "/";
		ResourceLocation tex0 = prefix("block/wood/" + plankTexName + "_0");
		ResourceLocation tex1 = prefix("block/wood/" + plankTexName + "_1");
		ResourceLocation tex2 = prefix("block/wood/" + plankTexName + "_2");
		ResourceLocation tex3 = prefix("block/wood/" + plankTexName + "_3");
		ConfiguredModel[] plankModels = ConfiguredModel.builder()
			.weight(10).modelFile(models().cubeAll(plankDir + name(plank), tex0)).nextModel()
			.weight(10).modelFile(models().cubeAll(plankDir + name(plank) + "_1", tex1)).nextModel()
			.weight(1).modelFile(models().cubeAll(plankDir + name(plank) + "_2", tex2)).nextModel()
			.weight(1).modelFile(models().cubeAll(plankDir + name(plank) + "_3", tex3)).build();
		simpleBlock(plank, plankModels);

		String slabDir = "block/wood/slab/" + variant + "/";
		ConfiguredModel[] bottomSlabModels = ConfiguredModel.builder()
			.weight(10).modelFile(models().slab(slabDir + name(slab), tex0, tex0, tex0)).nextModel()
			.weight(10).modelFile(models().slab(slabDir + name(slab) + "_1", tex1, tex1, tex1)).nextModel()
			.weight(1).modelFile(models().slab(slabDir + name(slab) + "_2", tex2, tex2, tex2)).nextModel()
			.weight(1).modelFile(models().slab(slabDir + name(slab) + "_3", tex3, tex3, tex3)).build();
		ConfiguredModel[] topSlabModels = ConfiguredModel.builder()
			.weight(10).uvLock(true).rotationX(180).modelFile(bottomSlabModels[0].model).nextModel()
			.weight(10).uvLock(true).rotationX(180).modelFile(bottomSlabModels[1].model).nextModel()
			.weight(1).uvLock(true).rotationX(180).modelFile(bottomSlabModels[2].model).nextModel()
			.weight(1).uvLock(true).rotationX(180).modelFile(bottomSlabModels[3].model).build();
		getVariantBuilder(slab).partialState().with(SlabBlock.TYPE, SlabType.BOTTOM).setModels(bottomSlabModels);
		getVariantBuilder(slab).partialState().with(SlabBlock.TYPE, SlabType.TOP).setModels(topSlabModels);
		getVariantBuilder(slab).partialState().with(SlabBlock.TYPE, SlabType.DOUBLE).setModels(plankModels);

		woodStairs(stair, plankTexName, variant);
		woodButton(button, plankTexName, variant);
		woodFence(fence, plankTexName, variant);
		woodGate(gate, plankTexName, variant);
		woodPlate(plate, plankTexName, variant);
		String doorDir = "block/wood/door/" + variant + "/";
		String trapdoorDir = "block/wood/trapdoor/" + variant + "/";

		if (correctDoors) {
			correctedDoorBlock(door, doorDir + name(door), prefix("block/wood/door/" + variant + "_lower"), prefix("block/wood/door/" + variant + "_upper"), prefix("block/wood/door/" + variant + "_side"), cutoutDoors ? CUTOUT : SOLID);
		} else {
			doorBlockWithRenderType(door, doorDir + variant, prefix("block/wood/door/" + variant + "_lower"), prefix("block/wood/door/" + variant + "_upper"), cutoutDoors ? CUTOUT : SOLID);
		}
		trapdoorBlockWithRenderType(trapdoor, trapdoorDir + variant, prefix("block/wood/trapdoor/" + variant + "_trapdoor"), true, cutoutDoors ? CUTOUT : SOLID);

		banister(banister, plankTexName, variant);
	}

	private void correctedDoorBlock(DoorBlock block, String baseName, ResourceLocation bottom, ResourceLocation top, ResourceLocation side, ResourceLocation renderType) {
		ModelFile bottomLeft = door(baseName + "_bottom_left", "corrected_door_bottom_left", bottom, top, side).renderType(renderType);
		ModelFile bottomLeftOpen = door(baseName + "_bottom_left_open", "corrected_door_bottom_left_open", bottom, top, side).renderType(renderType);
		ModelFile bottomRight = door(baseName + "_bottom_right", "corrected_door_bottom_right", bottom, top, side).renderType(renderType);
		ModelFile bottomRightOpen = door(baseName + "_bottom_right_open", "corrected_door_bottom_right_open", bottom, top, side).renderType(renderType);
		ModelFile topLeft = door(baseName + "_top_left", "corrected_door_top_left", bottom, top, side).renderType(renderType);
		ModelFile topLeftOpen = door(baseName + "_top_left_open", "corrected_door_top_left_open", bottom, top, side).renderType(renderType);
		ModelFile topRight = door(baseName + "_top_right", "corrected_door_top_right", bottom, top, side).renderType(renderType);
		ModelFile topRightOpen = door(baseName + "_top_right_open", "corrected_door_top_right_open", bottom, top, side).renderType(renderType);
		doorBlock(block, bottomLeft, bottomLeftOpen, bottomRight, bottomRightOpen, topLeft, topLeftOpen, topRight, topRightOpen);
	}

	private BlockModelBuilder door(String name, String model, ResourceLocation bottom, ResourceLocation top, ResourceLocation side) {
		return models().withExistingParent(name, prefix("block/util/" + model))
			.texture("bottom", bottom)
			.texture("top", top)
			.texture("side", side);
	}

	protected void woodGate(Block gate, String texName, String variant) {
		String gateDir = "block/wood/fence_gate/" + variant + "/";

		ResourceLocation tex0 = prefix("block/wood/" + texName + "_0");
		ResourceLocation tex1 = prefix("block/wood/" + texName + "_1");
		ResourceLocation tex2 = prefix("block/wood/" + texName + "_2");
		ResourceLocation tex3 = prefix("block/wood/" + texName + "_3");

		ModelFile gate0 = models().fenceGate(gateDir + name(gate), tex0);
		ModelFile gate1 = models().fenceGate(gateDir + name(gate) + "_1", tex1);
		ModelFile gate2 = models().fenceGate(gateDir + name(gate) + "_2", tex2);
		ModelFile gate3 = models().fenceGate(gateDir + name(gate) + "_3", tex3);
		ModelFile open0 = models().fenceGateOpen(gateDir + name(gate) + "_open", tex0);
		ModelFile open1 = models().fenceGateOpen(gateDir + name(gate) + "_open_1", tex1);
		ModelFile open2 = models().fenceGateOpen(gateDir + name(gate) + "_open_2", tex2);
		ModelFile open3 = models().fenceGateOpen(gateDir + name(gate) + "_open_3", tex3);
		ModelFile wall0 = models().fenceGateWall(gateDir + name(gate) + "_wall", tex0);
		ModelFile wall1 = models().fenceGateWall(gateDir + name(gate) + "_wall_1", tex1);
		ModelFile wall2 = models().fenceGateWall(gateDir + name(gate) + "_wall_2", tex2);
		ModelFile wall3 = models().fenceGateWall(gateDir + name(gate) + "_wall_3", tex3);
		ModelFile wallOpen0 = models().fenceGateWallOpen(gateDir + name(gate) + "_wall_open", tex0);
		ModelFile wallOpen1 = models().fenceGateWallOpen(gateDir + name(gate) + "_wall_open_1", tex1);
		ModelFile wallOpen2 = models().fenceGateWallOpen(gateDir + name(gate) + "_wall_open_2", tex2);
		ModelFile wallOpen3 = models().fenceGateWallOpen(gateDir + name(gate) + "_wall_open_3", tex3);

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

	protected void woodFence(Block fence, String texName, String variant) {
		String fenceDir = "block/wood/fence/" + variant + "/";

		ResourceLocation tex0 = prefix("block/wood/" + texName + "_0");
		ResourceLocation tex1 = prefix("block/wood/" + texName + "_1");
		ResourceLocation tex2 = prefix("block/wood/" + texName + "_2");
		ResourceLocation tex3 = prefix("block/wood/" + texName + "_3");

		ModelFile post0 = models().fencePost(fenceDir + name(fence) + "_post", tex0);
		ModelFile post1 = models().fencePost(fenceDir + name(fence) + "_post_1", tex1);
		ModelFile post2 = models().fencePost(fenceDir + name(fence) + "_post_2", tex2);
		ModelFile post3 = models().fencePost(fenceDir + name(fence) + "_post_3", tex3);
		ModelFile side0 = models().fenceSide(fenceDir + name(fence) + "_side", tex0);
		ModelFile side1 = models().fenceSide(fenceDir + name(fence) + "_side_1", tex1);
		ModelFile side2 = models().fenceSide(fenceDir + name(fence) + "_side_2", tex2);
		ModelFile side3 = models().fenceSide(fenceDir + name(fence) + "_side_3", tex3);

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

	protected void woodPlate(Block plate, String texName, String variant) {
		String plateDir = "block/wood/pressure_plate/" + variant + "/";

		ResourceLocation tex0 = prefix("block/wood/" + texName + "_0");
		ResourceLocation tex1 = prefix("block/wood/" + texName + "_1");
		ResourceLocation tex2 = prefix("block/wood/" + texName + "_2");
		ResourceLocation tex3 = prefix("block/wood/" + texName + "_3");
		ConfiguredModel[] unpressed = ConfiguredModel.builder()
			.weight(10).modelFile(models().withExistingParent(plateDir + name(plate), "pressure_plate_up").texture("texture", tex0)).nextModel()
			.weight(10).modelFile(models().withExistingParent(plateDir + name(plate) + "_1", "pressure_plate_up").texture("texture", tex1)).nextModel()
			.weight(1).modelFile(models().withExistingParent(plateDir + name(plate) + "_2", "pressure_plate_up").texture("texture", tex2)).nextModel()
			.weight(1).modelFile(models().withExistingParent(plateDir + name(plate) + "_3", "pressure_plate_up").texture("texture", tex3)).build();
		ConfiguredModel[] pressed = ConfiguredModel.builder()
			.weight(10).modelFile(models().withExistingParent(plateDir + name(plate) + "_down", "pressure_plate_down").texture("texture", tex0)).nextModel()
			.weight(10).modelFile(models().withExistingParent(plateDir + name(plate) + "_down_1", "pressure_plate_down").texture("texture", tex1)).nextModel()
			.weight(1).modelFile(models().withExistingParent(plateDir + name(plate) + "_down_2", "pressure_plate_down").texture("texture", tex2)).nextModel()
			.weight(1).modelFile(models().withExistingParent(plateDir + name(plate) + "_down_3", "pressure_plate_down").texture("texture", tex3)).build();

		getVariantBuilder(plate).partialState().with(PressurePlateBlock.POWERED, false).setModels(unpressed);
		getVariantBuilder(plate).partialState().with(PressurePlateBlock.POWERED, true).setModels(pressed);
	}

	protected void woodButton(Block button, String texName, String variant) {
		String buttonDir = "block/wood/button/" + variant + "/";

		ResourceLocation tex0 = prefix("block/wood/" + texName + "_0");
		ResourceLocation tex1 = prefix("block/wood/" + texName + "_1");
		ResourceLocation tex2 = prefix("block/wood/" + texName + "_2");
		ResourceLocation tex3 = prefix("block/wood/" + texName + "_3");
		ModelFile unpressed0 = models().withExistingParent(buttonDir + name(button), "button").texture("texture", tex0);
		ModelFile pressed0 = models().withExistingParent(buttonDir + name(button) + "_pressed", "button_pressed").texture("texture", tex0);
		ModelFile unpressed1 = models().withExistingParent(buttonDir + name(button) + "_1", "button").texture("texture", tex1);
		ModelFile pressed1 = models().withExistingParent(buttonDir + name(button) + "_pressed_1", "button_pressed").texture("texture", tex1);
		ModelFile unpressed2 = models().withExistingParent(buttonDir + name(button) + "_2", "button").texture("texture", tex2);
		ModelFile pressed2 = models().withExistingParent(buttonDir + name(button) + "_pressed_2", "button_pressed").texture("texture", tex2);
		ModelFile unpressed3 = models().withExistingParent(buttonDir + name(button) + "_3", "button").texture("texture", tex3);
		ModelFile pressed3 = models().withExistingParent(buttonDir + name(button) + "_pressed_3", "button_pressed").texture("texture", tex3);

		getVariantBuilder(button).forAllStates(state -> {
			ModelFile model0 = state.getValue(ButtonBlock.POWERED) ? pressed0 : unpressed0;
			ModelFile model1 = state.getValue(ButtonBlock.POWERED) ? pressed1 : unpressed1;
			ModelFile model2 = state.getValue(ButtonBlock.POWERED) ? pressed2 : unpressed2;
			ModelFile model3 = state.getValue(ButtonBlock.POWERED) ? pressed3 : unpressed3;
			int rotX = switch (state.getValue(FaceAttachedHorizontalDirectionalBlock.FACE)) {
				case WALL -> 90;
				case FLOOR -> 0;
				case CEILING -> 180;
			};
			int rotY = 0;
			if (state.getValue(FaceAttachedHorizontalDirectionalBlock.FACE) == AttachFace.CEILING) {
				switch (state.getValue(HorizontalDirectionalBlock.FACING)) {
					case NORTH -> rotY = 180;
					case WEST -> rotY = 90;
					case EAST -> rotY = 270;
				}
			} else {
				switch (state.getValue(HorizontalDirectionalBlock.FACING)) {
					case SOUTH -> rotY = 180;
					case WEST -> rotY = 270;
					case EAST -> rotY = 90;
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

	protected void woodStairs(StairBlock block, String texName, String variant) {
		String stairsDir = "block/wood/stairs/" + variant + "/";

		ResourceLocation tex0 = prefix("block/wood/" + texName + "_0");
		ResourceLocation tex1 = prefix("block/wood/" + texName + "_1");
		ResourceLocation tex2 = prefix("block/wood/" + texName + "_2");
		ResourceLocation tex3 = prefix("block/wood/" + texName + "_3");
		ModelFile main0 = models().stairs(stairsDir + name(block), tex0, tex0, tex0);
		ModelFile main1 = models().stairs(stairsDir + name(block) + "_1", tex1, tex1, tex1);
		ModelFile main2 = models().stairs(stairsDir + name(block) + "_2", tex2, tex2, tex2);
		ModelFile main3 = models().stairs(stairsDir + name(block) + "_3", tex3, tex3, tex3);
		ModelFile inner0 = models().stairsInner(stairsDir + name(block) + "_inner", tex0, tex0, tex0);
		ModelFile inner1 = models().stairsInner(stairsDir + name(block) + "_inner_1", tex1, tex1, tex1);
		ModelFile inner2 = models().stairsInner(stairsDir + name(block) + "_inner_2", tex2, tex2, tex2);
		ModelFile inner3 = models().stairsInner(stairsDir + name(block) + "_inner_3", tex3, tex3, tex3);
		ModelFile outer0 = models().stairsOuter(stairsDir + name(block) + "_outer", tex0, tex0, tex0);
		ModelFile outer1 = models().stairsOuter(stairsDir + name(block) + "_outer_1", tex1, tex1, tex1);
		ModelFile outer2 = models().stairsOuter(stairsDir + name(block) + "_outer_2", tex2, tex2, tex2);
		ModelFile outer3 = models().stairsOuter(stairsDir + name(block) + "_outer_3", tex3, tex3, tex3);
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

	protected void banister(BanisterBlock banister, String texName, String woodVariant) {
		String banisterDir = "block/wood/banister/" + woodVariant + "/";

		ResourceLocation tex0 = prefix("block/wood/" + texName + "_0");
		ResourceLocation tex1 = prefix("block/wood/" + texName + "_1");
		ResourceLocation tex2 = prefix("block/wood/" + texName + "_2");
		ResourceLocation tex3 = prefix("block/wood/" + texName + "_3");

		getVariantBuilder(banister).forAllStatesExcept(state -> {
			Direction facing = state.getValue(BanisterBlock.FACING);
			int yRot = (int) facing.toYRot();
			String extended = state.getValue(BanisterBlock.EXTENDED) ? "_extended" : "";
			String variant = state.getValue(BanisterBlock.SHAPE).getSerializedName() + extended;
			String newModelName = banisterDir + name(banister) + "_" + variant;

			return ConfiguredModel.builder()
				.weight(10).modelFile(models().withExistingParent(newModelName, TwilightForestMod.prefix("banister_" + variant)).texture("texture", tex0)).rotationY(yRot).nextModel()
				.weight(10).modelFile(models().withExistingParent(newModelName + "_1", TwilightForestMod.prefix("banister_" + variant)).texture("texture", tex1)).rotationY(yRot).nextModel()
				.weight(1).modelFile(models().withExistingParent(newModelName + "_2", TwilightForestMod.prefix("banister_" + variant)).texture("texture", tex2)).rotationY(yRot).nextModel()
				.weight(1).modelFile(models().withExistingParent(newModelName + "_3", TwilightForestMod.prefix("banister_" + variant)).texture("texture", tex3)).rotationY(yRot).build();
		}, BanisterBlock.WATERLOGGED);
	}

	protected void banisterVanilla(BanisterBlock banister, String texName, String woodVariant) {
		String banisterDir = "block/wood/banister/" + woodVariant + "/";
		ResourceLocation tex0 = ResourceLocation.withDefaultNamespace("block/" + texName);

		getVariantBuilder(banister).forAllStatesExcept(state -> {
			Direction facing = state.getValue(BanisterBlock.FACING);
			int yRot = (int) facing.toYRot();
			String extended = state.getValue(BanisterBlock.EXTENDED) ? "_extended" : "";
			String variant = state.getValue(BanisterBlock.SHAPE).getSerializedName() + extended;

			return ConfiguredModel.builder()
				.modelFile(models().withExistingParent(banisterDir + name(banister) + "_" + variant, TwilightForestMod.prefix("banister_" + variant)).texture("texture", tex0)).rotationY(yRot).build();
		}, BanisterBlock.WATERLOGGED);
	}

	protected void bisectedStairsBlock(DeferredHolder<Block, StairBlock> block, ResourceLocation side, ResourceLocation end, ResourceLocation middle) {
		this.bisectedStairsBlock(block, block.getId().getPath(), side, end, middle);
	}

	protected void bisectedStairsBlock(DeferredHolder<Block, StairBlock> block, String name, ResourceLocation side, ResourceLocation end, ResourceLocation middle) {
		ModelFile stairs = this.models().withExistingParent(name, TwilightForestMod.prefix("block/util/bisected_stairs")).texture("side", side).texture("end", end).texture("middle", middle);
		ModelFile stairsInner = this.models().withExistingParent(name + "_inner", TwilightForestMod.prefix("block/util/bisected_inner_stairs")).texture("side", side).texture("end", end).texture("middle", middle);
		ModelFile stairsOuter = this.models().withExistingParent(name + "_outer", TwilightForestMod.prefix("block/util/bisected_outer_stairs")).texture("side", side).texture("end", end).texture("middle", middle);

		this.stairsBlock(block.get(), stairs, stairsInner, stairsOuter);
	}

	protected ResourceLocation key(Block block) {
		return BuiltInRegistries.BLOCK.getKey(block);
	}

	protected String name(Block block) {
		return key(block).getPath();
	}
}
