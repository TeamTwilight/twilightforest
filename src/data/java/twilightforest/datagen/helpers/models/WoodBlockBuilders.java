package twilightforest.datagen.helpers.models;

import com.mojang.datafixers.util.Pair;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelOutput;
import net.minecraft.client.data.models.blockstates.*;
import net.minecraft.client.data.models.model.*;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.neoforged.neoforge.client.model.generators.template.FaceRotation;
import twilightforest.TwilightForestMod;
import twilightforest.block.BanisterBlock;
import twilightforest.block.ClimbableHollowLogBlock;
import twilightforest.block.HorizontalHollowLogBlock;
import twilightforest.block.SpecialMagicLogBlock;
import twilightforest.datagen.assets.models.TFModelTemplates;
import twilightforest.datagen.assets.models.TFTextureMapping;
import twilightforest.datagen.assets.models.TFTextureSlot;
import twilightforest.init.TFBlocks;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public abstract class WoodBlockBuilders extends BlockModelGenerators {

	public WoodBlockBuilders(Consumer<BlockStateGenerator> stateOutput, ItemModelOutput itemOutput, BiConsumer<ResourceLocation, ModelInstance> modelOutput) {
		super(stateOutput, itemOutput, modelOutput);
	}

	@Override
	public abstract void run();

	public void generateSortingLeaves() {
		Block block = TFBlocks.SORTING_LEAVES.get();

		// we create 4 variants of leaves and choose 1 of 4 flowing direction for each face of each variant
		int[][] CHOSEN_VARIANTS = {{0, 2, 2, 3, 0, 0}, {2, 0, 3, 0, 2, 1}, {3, 3, 1, 2, 3, 2}, {1, 1, 0, 1, 1, 3}};
		Variant[] modelFiles = new Variant[CHOSEN_VARIANTS.length];
		for(int i = 0; i < CHOSEN_VARIANTS.length; i++) {
			int finalI = i;
			ResourceLocation model = TFModelTemplates.CUBE_ALL.extend().element(builder -> builder.from(0, 0, 0).to(16, 16, 16).allFaces((direction, faceBuilder) -> {
				FaceRotation rotation = FaceRotation.values()[CHOSEN_VARIANTS[finalI][direction.ordinal()]];
				faceBuilder.cullface(direction).texture(TextureSlot.ALL).rotation(rotation).tintindex(0);
			})).build().createWithSuffix(block, (i > 0 ? "_" + i : ""), TextureMapping.cube(block), this.modelOutput);

			modelFiles[i] = Variant.variant().with(VariantProperties.MODEL, model);
		}

		this.blockStateOutput.accept(MultiVariantGenerator.multiVariant(block, modelFiles));

	}

	public void generateMagicLeaves(Block leaves, int rotation, int tint) {
		FaceRotation faceRotation = rotation % 180 == 0 ? FaceRotation.ZERO : FaceRotation.values()[rotation / 90];
		boolean isRotation180 = rotation == 180;
		float u1 = isRotation180 ? 16 : 0;
		float v1 = isRotation180 ? 16 : 0;
		float u2 = isRotation180 ? 0 : 16;
		float v2 = isRotation180 ? 0 : 16;

		ResourceLocation model = ModelTemplates.CUBE_ALL.extend().element(builder -> builder.from(0, 0, 0).to(16, 16, 16).allFaces(((dir, faceBuilder) -> faceBuilder.cullface(dir).uvs(u1, v1, u2, v2).tintindex(0).rotation(faceRotation).texture(TextureSlot.ALL)))).build().create(leaves, TextureMapping.cube(leaves), this.modelOutput);
		this.wrapTintedBlockItem(leaves, ItemModelUtils.constantTint(tint), block -> this.blockStateOutput.accept(createSimpleBlock(block, model)));
	}

	public void generateSapling(Block block, Block pottedBlock, BlockModelGenerators.PlantType type) {
		ResourceLocation sapling = type.getCross().extend().renderType("cutout").build().create(block, type.getTextureMapping(block), this.modelOutput);
		this.blockStateOutput.accept(createSimpleBlock(block, sapling));
		ResourceLocation potted = type.getCrossPot().extend().renderType("cutout").build().create(pottedBlock, type.getPlantTextureMapping(block), this.modelOutput);
		this.blockStateOutput.accept(createSimpleBlock(pottedBlock, potted));
		this.registerSimpleItemModel(block.asItem(), type.createItemModel(this, block));
	}

	public void generateWood(Block woodBlock, TextureMapping mapping) {
		TextureMapping texturemapping = mapping.copyAndUpdate(TextureSlot.END, mapping.get(TextureSlot.SIDE));
		ResourceLocation model = ModelTemplates.CUBE_COLUMN.create(woodBlock, texturemapping, this.modelOutput);
		this.blockStateOutput.accept(BlockModelGenerators.createAxisAlignedPillarBlock(woodBlock, model));
	}

	public void generateLog(Block logBlock, TextureMapping mapping) {
		ResourceLocation vertical = ModelTemplates.CUBE_COLUMN.create(logBlock, mapping, this.modelOutput);
		ResourceLocation horizontal = ModelTemplates.CUBE_COLUMN_HORIZONTAL.create(logBlock, mapping, this.modelOutput);
		this.blockStateOutput.accept(BlockModelGenerators.createRotatedPillarWithHorizontalVariant(logBlock, vertical, horizontal));
	}

	public void generateButton(Block button, TextureMapping mapping) {
		ResourceLocation unpressed = ModelTemplates.BUTTON.create(button, mapping, this.modelOutput);
		ResourceLocation pressed = ModelTemplates.BUTTON_PRESSED.create(button, mapping, this.modelOutput);
		this.blockStateOutput.accept(BlockModelGenerators.createButton(button, unpressed, pressed));
		ResourceLocation inventory = ModelTemplates.BUTTON_INVENTORY.create(button, mapping, this.modelOutput);
		this.registerSimpleItemModel(button, inventory);
	}

	public void generateFence(Block fence, TextureMapping mapping) {
		ResourceLocation post = ModelTemplates.FENCE_POST.create(fence, mapping, this.modelOutput);
		ResourceLocation side = ModelTemplates.FENCE_SIDE.create(fence, mapping, this.modelOutput);
		this.blockStateOutput.accept(BlockModelGenerators.createFence(fence, post, side));
		ResourceLocation inventory = ModelTemplates.FENCE_INVENTORY.create(fence, mapping, this.modelOutput);
		this.registerSimpleItemModel(fence, inventory);
	}

	public void generateFenceGate(Block fenceGate, TextureMapping mapping) {
		ResourceLocation open = ModelTemplates.FENCE_GATE_OPEN.create(fenceGate, mapping, this.modelOutput);
		ResourceLocation closed = ModelTemplates.FENCE_GATE_CLOSED.create(fenceGate, mapping, this.modelOutput);
		ResourceLocation wallOpen = ModelTemplates.FENCE_GATE_WALL_OPEN.create(fenceGate, mapping, this.modelOutput);
		ResourceLocation wallClosed = ModelTemplates.FENCE_GATE_WALL_CLOSED.create(fenceGate, mapping, this.modelOutput);
		this.blockStateOutput.accept(BlockModelGenerators.createFenceGate(fenceGate, open, closed, wallOpen, wallClosed, true));
		this.registerSimpleItemModel(fenceGate, closed);
	}

	public void generatePressurePlate(Block pressurePlate, TextureMapping mapping) {
		ResourceLocation unpressed = ModelTemplates.PRESSURE_PLATE_UP.create(pressurePlate, mapping, this.modelOutput);
		ResourceLocation pressed = ModelTemplates.PRESSURE_PLATE_DOWN.create(pressurePlate, mapping, this.modelOutput);
		this.blockStateOutput.accept(BlockModelGenerators.createPressurePlate(pressurePlate, unpressed, pressed));
		this.registerSimpleItemModel(pressurePlate, unpressed);
	}

	public void generateSign(Block floor, Block wall, TextureMapping mapping) {
		ResourceLocation model = ModelTemplates.PARTICLE_ONLY.create(floor, mapping, this.modelOutput);
		this.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(floor, model));
		this.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(wall, model));
		this.registerSimpleFlatItemModel(floor.asItem());
	}

	public void generateHangingSign(Block ceiling, Block wall, Block particle) {
		ResourceLocation resourcelocation = ModelTemplates.PARTICLE_ONLY.create(ceiling, new TextureMapping().put(TextureSlot.PARTICLE, TextureMapping.getBlockTexture(particle)), this.modelOutput);
		this.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(ceiling, resourcelocation));
		this.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(wall, resourcelocation));
		this.registerSimpleFlatItemModel(ceiling.asItem());
	}

	public void generateSlab(Block slab, Block full, TextureMapping mapping) {
		ResourceLocation bottom = ModelTemplates.SLAB_BOTTOM.create(slab, mapping, this.modelOutput);
		ResourceLocation top = ModelTemplates.SLAB_TOP.create(slab, mapping, this.modelOutput);
		this.blockStateOutput.accept(createSlab(slab, bottom, top, ModelLocationUtils.getModelLocation(full)));
		this.registerSimpleItemModel(slab, bottom);
	}

	public void generateStairs(Block stairs, TextureMapping mapping) {
		ResourceLocation inner = ModelTemplates.STAIRS_INNER.createWithSuffix(stairs, "_inner", mapping, this.modelOutput);
		ResourceLocation straight = ModelTemplates.STAIRS_STRAIGHT.create(stairs, mapping, this.modelOutput);
		ResourceLocation outer = ModelTemplates.STAIRS_OUTER.createWithSuffix(stairs, "_outer", mapping, this.modelOutput);
		this.blockStateOutput.accept(BlockModelGenerators.createStairs(stairs, inner, straight, outer));
		this.registerSimpleItemModel(stairs, straight);
	}

	public void generateTrapdoor(Block trapdoor, boolean orientable, String renderType) {
		TextureMapping texturemapping = TextureMapping.defaultTexture(trapdoor);
		ResourceLocation top = (orientable ? ModelTemplates.ORIENTABLE_TRAPDOOR_TOP : ModelTemplates.TRAPDOOR_TOP).extend().renderType(renderType).build().create(trapdoor, texturemapping, this.modelOutput);
		ResourceLocation bottom = (orientable ? ModelTemplates.ORIENTABLE_TRAPDOOR_BOTTOM : ModelTemplates.TRAPDOOR_BOTTOM).extend().renderType(renderType).build().create(trapdoor, texturemapping, this.modelOutput);
		ResourceLocation open = (orientable ? ModelTemplates.ORIENTABLE_TRAPDOOR_OPEN : ModelTemplates.TRAPDOOR_OPEN).extend().renderType(renderType).build().create(trapdoor, texturemapping, this.modelOutput);
		this.blockStateOutput.accept(createTrapdoor(trapdoor, top, bottom, open));
		this.registerSimpleItemModel(trapdoor, bottom);
	}

	//holy ternary batman
	public void generateDoor(Block door, boolean useSideTexture, String renderType) {
		TextureMapping texturemapping = useSideTexture ? TFTextureMapping.sideDoor(door) : TextureMapping.door(door);
		ResourceLocation bottomLeft = (useSideTexture ? TFModelTemplates.CORRECTED_DOOR_BOTTOM_LEFT : ModelTemplates.DOOR_BOTTOM_LEFT).extend().renderType(renderType).build().create(door, texturemapping, this.modelOutput);
		ResourceLocation bottomLeftOpen = (useSideTexture ? TFModelTemplates.CORRECTED_DOOR_BOTTOM_LEFT_OPEN : ModelTemplates.DOOR_BOTTOM_LEFT_OPEN).extend().renderType(renderType).build().create(door, texturemapping, this.modelOutput);
		ResourceLocation bottomRight = (useSideTexture ? TFModelTemplates.CORRECTED_DOOR_BOTTOM_RIGHT : ModelTemplates.DOOR_BOTTOM_RIGHT).extend().renderType(renderType).build().create(door, texturemapping, this.modelOutput);
		ResourceLocation bottomRightOpen = (useSideTexture ? TFModelTemplates.CORRECTED_DOOR_BOTTOM_RIGHT_OPEN : ModelTemplates.DOOR_BOTTOM_RIGHT_OPEN).extend().renderType(renderType).build().create(door, texturemapping, this.modelOutput);
		ResourceLocation topLeft = (useSideTexture ? TFModelTemplates.CORRECTED_DOOR_TOP_LEFT : ModelTemplates.DOOR_TOP_LEFT).extend().renderType(renderType).build().create(door, texturemapping, this.modelOutput);
		ResourceLocation topLeftOpen = (useSideTexture ? TFModelTemplates.CORRECTED_DOOR_TOP_LEFT_OPEN : ModelTemplates.DOOR_TOP_LEFT_OPEN).extend().renderType(renderType).build().create(door, texturemapping, this.modelOutput);
		ResourceLocation topRight = (useSideTexture ? TFModelTemplates.CORRECTED_DOOR_TOP_RIGHT : ModelTemplates.DOOR_TOP_RIGHT).extend().renderType(renderType).build().create(door, texturemapping, this.modelOutput);
		ResourceLocation topRightOpen = (useSideTexture ? TFModelTemplates.CORRECTED_DOOR_TOP_RIGHT_OPEN : ModelTemplates.DOOR_TOP_RIGHT_OPEN).extend().renderType(renderType).build().create(door, texturemapping, this.modelOutput);
		this.registerSimpleFlatItemModel(door.asItem());
		this.blockStateOutput.accept(createDoor(door, bottomLeft, bottomLeftOpen, bottomRight, bottomRightOpen, topLeft, topLeftOpen, topRight, topRightOpen));
	}

	public void generateBanister(Block banister, TextureMapping mapping) {
		ResourceLocation connected = TFModelTemplates.BANISTER_CONNECTED.create(banister, mapping, this.modelOutput);
		ResourceLocation connectedExtended = TFModelTemplates.BANISTER_CONNECTED_EXTENDED.create(banister, mapping, this.modelOutput);
		ResourceLocation shorty = TFModelTemplates.BANISTER_SHORT.create(banister, mapping, this.modelOutput);
		ResourceLocation shortExtended = TFModelTemplates.BANISTER_SHORT_EXTENDED.create(banister, mapping, this.modelOutput);
		ResourceLocation tall = TFModelTemplates.BANISTER_TALL.create(banister, mapping, this.modelOutput);
		ResourceLocation tallExtended = TFModelTemplates.BANISTER_TALL_EXTENDED.create(banister, mapping, this.modelOutput);
		this.blockStateOutput.accept(createBanister(banister, connected, connectedExtended, shorty, shortExtended, tall, tallExtended));
		ResourceLocation inventory = TFModelTemplates.BANISTER_INVENTORY.create(banister, mapping, this.modelOutput);
		this.registerSimpleItemModel(banister, inventory);
	}

	public static BlockStateGenerator createBanister(Block banister, ResourceLocation connected, ResourceLocation connectedExtended, ResourceLocation shorty, ResourceLocation shortExtended, ResourceLocation tall, ResourceLocation tallExtended) {
		return MultiVariantGenerator.multiVariant(banister).with(
				PropertyDispatch.properties(BanisterBlock.EXTENDED, BanisterBlock.SHAPE).generate((extended, shape) -> {
					ResourceLocation model = switch (shape) {
						case SHORT -> extended ? shortExtended : shorty;
						case TALL -> extended ? tallExtended : tall;
						case CONNECTED -> extended ? connectedExtended : connected;
					};
					return Variant.variant().with(VariantProperties.MODEL, model);
				})
		).with(createHorizontalFacingDispatchAlt());
	}

	public void generateHollowLog(Block log, Block stripped, Block horizontal, Block vertical, Block climbable) {
		TextureMapping base = TextureMapping.logColumn(log).put(TextureSlot.INSIDE, TextureMapping.getBlockTexture(stripped));
		ResourceLocation horizModel = TFModelTemplates.HORIZONTAL_HOLLOW_LOG.create(horizontal, base, this.modelOutput);
		ResourceLocation mossModel = TFModelTemplates.HORIZONTAL_HOLLOW_LOG_CARPET.createWithSuffix(horizontal, "_moss", base.put(TFTextureSlot.CARPET, TextureMapping.getBlockTexture(TFBlocks.MOSS_PATCH.get())).put(TFTextureSlot.OVERHANG, TwilightForestMod.prefix("block/moss_overhang")), this.modelOutput);
		ResourceLocation grassModel = TFModelTemplates.HORIZONTAL_HOLLOW_LOG_PLANT.createWithSuffix(horizontal, "_grass", base.put(TextureSlot.PLANT, TextureMapping.getBlockTexture(Blocks.SHORT_GRASS)).put(TFTextureSlot.CARPET, TextureMapping.getBlockTexture(TFBlocks.MOSS_PATCH.get())).put(TFTextureSlot.OVERHANG, TwilightForestMod.prefix("block/moss_overhang")), this.modelOutput);
		ResourceLocation snowModel = TFModelTemplates.HORIZONTAL_HOLLOW_LOG_CARPET.createWithSuffix(horizontal, "_snow", base.put(TFTextureSlot.CARPET, TextureMapping.getBlockTexture(Blocks.SNOW)).put(TFTextureSlot.OVERHANG, TwilightForestMod.prefix("block/snow_overhang")), this.modelOutput);
		ResourceLocation vertModel = TFModelTemplates.VERTICAL_HOLLOW_LOG.create(vertical, base, this.modelOutput);
		ResourceLocation ladderModel = TFModelTemplates.CLIMBABLE_HOLLOW_LOG.createWithSuffix(climbable, "_ladder", base.put(TFTextureSlot.CLIMBABLE, TextureMapping.getBlockTexture(Blocks.LADDER)), this.modelOutput);
		ResourceLocation vineModel = TFModelTemplates.CLIMBABLE_HOLLOW_LOG.createWithSuffix(climbable, "_vine", base.put(TFTextureSlot.CLIMBABLE, TextureMapping.getBlockTexture(Blocks.VINE)), this.modelOutput);
		this.blockStateOutput.accept(createHorizontalHollowLog(horizontal, horizModel, mossModel, grassModel, snowModel));
		this.blockStateOutput.accept(createSimpleBlock(vertical, vertModel));
		this.blockStateOutput.accept(createClimableHollowLog(climbable, ladderModel, vineModel));
		this.registerSimpleItemModel(horizontal, horizModel);
	}

	public static BlockStateGenerator createHorizontalHollowLog(Block horizontal, ResourceLocation base, ResourceLocation moss, ResourceLocation grass, ResourceLocation snow) {
		return MultiVariantGenerator.multiVariant(horizontal).with(
				PropertyDispatch.property(HorizontalHollowLogBlock.VARIANT).generate(variant -> {
					ResourceLocation model = switch (variant) {
						case MOSS -> moss;
						case MOSS_AND_GRASS -> grass;
						case SNOW -> snow;
						default -> base;
					};
					return Variant.variant().with(VariantProperties.MODEL, model);
				})
		).with(PropertyDispatch.property(BlockStateProperties.HORIZONTAL_AXIS)
				.select(Direction.Axis.Z, Variant.variant())
				.select(Direction.Axis.X, Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)));
	}

	public static BlockStateGenerator createClimableHollowLog(Block climbable, ResourceLocation ladder, ResourceLocation vine) {
		return MultiVariantGenerator.multiVariant(climbable).with(
				PropertyDispatch.property(ClimbableHollowLogBlock.VARIANT).generate(variant -> {
					ResourceLocation model = switch (variant) {
						case VINE -> vine;
						case LADDER, LADDER_WATERLOGGED -> ladder;
					};
					return Variant.variant().with(VariantProperties.MODEL, model);
				})
		).with(createHorizontalFacingDispatchAlt());
	}

	public void generateTreeCore(Block log, Block core) {
		ResourceLocation off = ModelTemplates.CUBE_COLUMN.create(core, TextureMapping.column(TextureMapping.getBlockTexture(core), TextureMapping.getBlockTexture(log, "_top")), this.modelOutput);
		ResourceLocation on = ModelTemplates.CUBE_COLUMN.createWithSuffix(core, "_on", TextureMapping.column(TextureMapping.getBlockTexture(core, "_on"), TextureMapping.getBlockTexture(log, "_top")), this.modelOutput);
		this.blockStateOutput.accept(MultiVariantGenerator.multiVariant(core).with(PropertyDispatch.property(SpecialMagicLogBlock.ACTIVE).generate(active -> Variant.variant().with(VariantProperties.MODEL, active ? on : off))));
		this.generateBlockItem(core);
	}

	public void generateChiseledBookshelf(Block shelf) {
		ResourceLocation resourcelocation = ModelLocationUtils.getModelLocation(shelf);
		MultiPartGenerator multipartgenerator = MultiPartGenerator.multiPart(shelf);
		List.of(
						Pair.of(Direction.NORTH, VariantProperties.Rotation.R0),
						Pair.of(Direction.EAST, VariantProperties.Rotation.R90),
						Pair.of(Direction.SOUTH, VariantProperties.Rotation.R180),
						Pair.of(Direction.WEST, VariantProperties.Rotation.R270))
				.forEach(
						pair -> {
							Direction direction = pair.getFirst();
							VariantProperties.Rotation rotation = pair.getSecond();
							Condition.TerminalCondition condition = Condition.condition().term(BlockStateProperties.HORIZONTAL_FACING, direction);
							multipartgenerator.with(condition,
									Variant.variant()
											.with(VariantProperties.MODEL, resourcelocation)
											.with(VariantProperties.Y_ROT, rotation)
											.with(VariantProperties.UV_LOCK, true)
							);
							this.addSlotStateAndRotationVariants(shelf, multipartgenerator, condition, rotation);
						}
				);
		this.blockStateOutput.accept(multipartgenerator);
		this.registerSimpleItemModel(shelf, ModelTemplates.CUBE_ORIENTABLE.createWithSuffix(shelf, "_inventory", new TextureMapping()
				.put(TextureSlot.SIDE, TextureMapping.getBlockTexture(shelf, "_side"))
				.put(TextureSlot.FRONT, TextureMapping.getBlockTexture(shelf, "_empty"))
				.put(TextureSlot.TOP, TextureMapping.getBlockTexture(shelf, "_top")), this.modelOutput));
		CHISELED_BOOKSHELF_SLOT_MODEL_CACHE.clear();
	}

	public void addSlotStateAndRotationVariants(Block shelf, MultiPartGenerator generator, Condition.TerminalCondition condition, VariantProperties.Rotation rotation) {
		List.of(
						Pair.of(BlockStateProperties.CHISELED_BOOKSHELF_SLOT_0_OCCUPIED, ModelTemplates.CHISELED_BOOKSHELF_SLOT_TOP_LEFT),
						Pair.of(BlockStateProperties.CHISELED_BOOKSHELF_SLOT_1_OCCUPIED, ModelTemplates.CHISELED_BOOKSHELF_SLOT_TOP_MID),
						Pair.of(BlockStateProperties.CHISELED_BOOKSHELF_SLOT_2_OCCUPIED, ModelTemplates.CHISELED_BOOKSHELF_SLOT_TOP_RIGHT),
						Pair.of(BlockStateProperties.CHISELED_BOOKSHELF_SLOT_3_OCCUPIED, ModelTemplates.CHISELED_BOOKSHELF_SLOT_BOTTOM_LEFT),
						Pair.of(BlockStateProperties.CHISELED_BOOKSHELF_SLOT_4_OCCUPIED, ModelTemplates.CHISELED_BOOKSHELF_SLOT_BOTTOM_MID),
						Pair.of(BlockStateProperties.CHISELED_BOOKSHELF_SLOT_5_OCCUPIED, ModelTemplates.CHISELED_BOOKSHELF_SLOT_BOTTOM_RIGHT)
				)
				.forEach(pair -> {
					BooleanProperty occupied = pair.getFirst();
					ModelTemplate template = pair.getSecond();
					this.addBookSlotModel(shelf, generator, condition, rotation, occupied, template, true);
					this.addBookSlotModel(shelf, generator, condition, rotation, occupied, template, false);
				});
	}

	public void addBookSlotModel(Block shelf, MultiPartGenerator generator, Condition.TerminalCondition condition, VariantProperties.Rotation rotation, BooleanProperty property, ModelTemplate template, boolean occupied) {
		String suffix = occupied ? "_occupied" : "_empty";
		TextureMapping texturemapping = new TextureMapping().put(TextureSlot.TEXTURE, TextureMapping.getBlockTexture(shelf, suffix));
		BlockModelGenerators.BookSlotModelCacheKey cache = new BlockModelGenerators.BookSlotModelCacheKey(template, suffix);
		ResourceLocation resourcelocation = CHISELED_BOOKSHELF_SLOT_MODEL_CACHE.computeIfAbsent(cache, key -> template.createWithSuffix(shelf, suffix, texturemapping, this.modelOutput));
		generator.with(
				Condition.and(condition, Condition.condition().term(property, occupied)),
				Variant.variant().with(VariantProperties.MODEL, resourcelocation).with(VariantProperties.Y_ROT, rotation)
		);
	}

	public void wrapBlockItem(Block block, Consumer<Block> blockRegistry) {
		blockRegistry.accept(block);
		this.generateBlockItem(block);
	}

	public void wrapTintedBlockItem(Block block, ItemTintSource tint, Consumer<Block> blockRegistry) {
		blockRegistry.accept(block);
		this.generateTintedBlockItem(block, tint);
	}

	public void generateBlockItem(Block block) {
		this.registerSimpleItemModel(block, BuiltInRegistries.BLOCK.getKey(block).withPrefix("block/"));
	}

	public void generateTintedBlockItem(Block block, ItemTintSource tint) {
		this.registerSimpleTintedItemModel(block, BuiltInRegistries.BLOCK.getKey(block).withPrefix("block/"), tint);
	}

	public <B extends Block> void generateSpecialModel(B block, Block particleBlock, Function<B, ItemModel.Unbaked> itemModel) {
		this.createParticleOnlyBlock(block, particleBlock);
		this.itemModelOutput.accept(block.asItem(), itemModel.apply(block));
	}
}
