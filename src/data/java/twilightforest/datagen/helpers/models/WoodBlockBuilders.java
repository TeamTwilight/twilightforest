package twilightforest.datagen.helpers.models;

import com.mojang.datafixers.util.Pair;
import com.mojang.math.Quadrant;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelOutput;
import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.client.data.models.blockstates.*;
import net.minecraft.client.data.models.model.*;
import net.minecraft.client.renderer.block.dispatch.Variant;
import net.minecraft.client.renderer.block.dispatch.VariantMutator;
import net.minecraft.client.renderer.block.dispatch.multipart.CombinedCondition;
import net.minecraft.client.renderer.block.dispatch.multipart.Condition;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
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

	public WoodBlockBuilders(Consumer<BlockModelDefinitionGenerator> stateOutput, ItemModelOutput itemOutput, BiConsumer<Identifier, ModelInstance> modelOutput) {
		super(stateOutput, itemOutput, modelOutput);
	}

	@Override
	public abstract void run();

	@Override
	public void registerSimpleItemModel(Block block, Identifier model) {
		if (block.asItem() != Items.AIR) {
			super.registerSimpleItemModel(block, model);
		}
	}

	public void generateSortingLeaves() {
		Block block = TFBlocks.SORTING_LEAVES.get();

		// we create 4 variants of leaves and choose 1 of 4 flowing direction for each face of each variant
		int[][] CHOSEN_VARIANTS = {{0, 2, 2, 3, 0, 0}, {2, 0, 3, 0, 2, 1}, {3, 3, 1, 2, 3, 2}, {1, 1, 0, 1, 1, 3}};
		Variant[] modelFiles = new Variant[CHOSEN_VARIANTS.length];
		for (int i = 0; i < CHOSEN_VARIANTS.length; i++) {
			int finalI = i;
			Identifier model = TFModelTemplates.CUBE_ALL.extend().element(builder -> builder.from(0, 0, 0).to(16, 16, 16).allFaces((direction, faceBuilder) -> {
				Quadrant rotation = Quadrant.values()[CHOSEN_VARIANTS[finalI][direction.ordinal()]];
				faceBuilder.cullface(direction).texture(TextureSlot.ALL).rotation(rotation).tintindex(0);
			})).build().createWithSuffix(block, (i > 0 ? "_" + i : ""), TextureMapping.cube(block), this.modelOutput);

			modelFiles[i] = plainModel(model);
		}

		this.blockStateOutput.accept(MultiVariantGenerator.dispatch(block, variants(modelFiles)));

	}

	public void generateMagicLeaves(Block leaves, int rotation, int tint) {
		Quadrant faceRotation = rotation % 180 == 0 ? Quadrant.R0 : Quadrant.values()[rotation / 90];
		boolean isRotation180 = rotation == 180;
		float u1 = isRotation180 ? 16 : 0;
		float v1 = isRotation180 ? 16 : 0;
		float u2 = isRotation180 ? 0 : 16;
		float v2 = isRotation180 ? 0 : 16;

		Identifier model = ModelTemplates.CUBE_ALL.extend().element(builder -> builder.from(0, 0, 0).to(16, 16, 16).allFaces(((dir, faceBuilder) -> faceBuilder.cullface(dir).uvs(u1, v1, u2, v2).tintindex(0).rotation(faceRotation).texture(TextureSlot.ALL)))).build().create(leaves, TextureMapping.cube(leaves), this.modelOutput);
		this.wrapTintedBlockItem(leaves, ItemModelUtils.constantTint(tint), block -> this.blockStateOutput.accept(createSimpleBlock(block, plainVariant(model))));
	}

	public void generateSapling(Block block, Block pottedBlock, BlockModelGenerators.PlantType type) {
		MultiVariant sapling = plainVariant(type.getCross().create(block, type.getTextureMapping(block), this.modelOutput));
		this.blockStateOutput.accept(createSimpleBlock(block, sapling));
		MultiVariant potted = plainVariant(type.getCrossPot().create(pottedBlock, type.getPlantTextureMapping(block), this.modelOutput));
		this.blockStateOutput.accept(createSimpleBlock(pottedBlock, potted));
		this.registerSimpleItemModel(block.asItem(), type.createItemModel(this, block));
	}

	public void generateButton(Block button, TextureMapping mapping) {
		MultiVariant unpressed = plainVariant(ModelTemplates.BUTTON.create(button, mapping, this.modelOutput));
		MultiVariant pressed = plainVariant(ModelTemplates.BUTTON_PRESSED.create(button, mapping, this.modelOutput));
		this.blockStateOutput.accept(BlockModelGenerators.createButton(button, unpressed, pressed));
		Identifier inventory = ModelTemplates.BUTTON_INVENTORY.create(button, mapping, this.modelOutput);
		this.registerSimpleItemModel(button, inventory);
	}

	public void generateFence(Block fence, TextureMapping mapping) {
		MultiVariant post = plainVariant(ModelTemplates.FENCE_POST.create(fence, mapping, this.modelOutput));
		MultiVariant side = plainVariant(ModelTemplates.FENCE_SIDE.create(fence, mapping, this.modelOutput));
		this.blockStateOutput.accept(BlockModelGenerators.createFence(fence, post, side));
		Identifier inventory = ModelTemplates.FENCE_INVENTORY.create(fence, mapping, this.modelOutput);
		this.registerSimpleItemModel(fence, inventory);
	}

	public void generateFenceGate(Block fenceGate, TextureMapping mapping) {
		MultiVariant open = plainVariant(ModelTemplates.FENCE_GATE_OPEN.create(fenceGate, mapping, this.modelOutput));
		Identifier closed = ModelTemplates.FENCE_GATE_CLOSED.create(fenceGate, mapping, this.modelOutput);
		MultiVariant wallOpen = plainVariant(ModelTemplates.FENCE_GATE_WALL_OPEN.create(fenceGate, mapping, this.modelOutput));
		MultiVariant wallClosed = plainVariant(ModelTemplates.FENCE_GATE_WALL_CLOSED.create(fenceGate, mapping, this.modelOutput));
		this.blockStateOutput.accept(BlockModelGenerators.createFenceGate(fenceGate, open, plainVariant(closed), wallOpen, wallClosed, true));
		this.registerSimpleItemModel(fenceGate, closed);
	}

	public void generatePressurePlate(Block pressurePlate, TextureMapping mapping) {
		Identifier unpressed = ModelTemplates.PRESSURE_PLATE_UP.create(pressurePlate, mapping, this.modelOutput);
		MultiVariant pressed = plainVariant(ModelTemplates.PRESSURE_PLATE_DOWN.create(pressurePlate, mapping, this.modelOutput));
		this.blockStateOutput.accept(BlockModelGenerators.createPressurePlate(pressurePlate, plainVariant(unpressed), pressed));
		this.registerSimpleItemModel(pressurePlate, unpressed);
	}

	public void generateSign(Block floor, Block wall, TextureMapping mapping) {
		MultiVariant model = plainVariant(ModelTemplates.PARTICLE_ONLY.create(floor, mapping, this.modelOutput));
		this.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(floor, model));
		this.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(wall, model));
		this.registerSimpleFlatItemModel(floor.asItem());
	}

	public void generateHangingSign(Block ceiling, Block wall, Block particle) {
		MultiVariant sign = plainVariant(ModelTemplates.PARTICLE_ONLY.create(ceiling, new TextureMapping().put(TextureSlot.PARTICLE, TextureMapping.getBlockTexture(particle)), this.modelOutput));
		this.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(ceiling, sign));
		this.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(wall, sign));
		this.registerSimpleFlatItemModel(ceiling.asItem());
	}

	public void generateSlab(Block slab, Block full, TextureMapping mapping) {
		Identifier bottom = ModelTemplates.SLAB_BOTTOM.create(slab, mapping, this.modelOutput);
		MultiVariant top = plainVariant(ModelTemplates.SLAB_TOP.create(slab, mapping, this.modelOutput));
		this.blockStateOutput.accept(createSlab(slab, plainVariant(bottom), top, plainVariant(ModelLocationUtils.getModelLocation(full))));
		this.registerSimpleItemModel(slab, bottom);
	}

	public void generateStairs(Block stairs, TextureMapping mapping) {
		MultiVariant inner = plainVariant(ModelTemplates.STAIRS_INNER.createWithSuffix(stairs, "", mapping, this.modelOutput));
		Identifier straight = ModelTemplates.STAIRS_STRAIGHT.create(stairs, mapping, this.modelOutput);
		MultiVariant outer = plainVariant(ModelTemplates.STAIRS_OUTER.createWithSuffix(stairs, "", mapping, this.modelOutput));
		this.blockStateOutput.accept(BlockModelGenerators.createStairs(stairs, inner, plainVariant(straight), outer));
		this.registerSimpleItemModel(stairs, straight);
	}

	public void generateTrapdoor(Block trapdoor, boolean orientable) {
		TextureMapping texturemapping = TextureMapping.defaultTexture(trapdoor);
		MultiVariant top = plainVariant((orientable ? ModelTemplates.ORIENTABLE_TRAPDOOR_TOP : ModelTemplates.TRAPDOOR_TOP).create(trapdoor, texturemapping, this.modelOutput));
		Identifier bottom = (orientable ? ModelTemplates.ORIENTABLE_TRAPDOOR_BOTTOM : ModelTemplates.TRAPDOOR_BOTTOM).create(trapdoor, texturemapping, this.modelOutput);
		MultiVariant open = plainVariant((orientable ? ModelTemplates.ORIENTABLE_TRAPDOOR_OPEN : ModelTemplates.TRAPDOOR_OPEN).create(trapdoor, texturemapping, this.modelOutput));
		this.blockStateOutput.accept(createTrapdoor(trapdoor, top, plainVariant(bottom), open));
		this.registerSimpleItemModel(trapdoor, bottom);
	}

	public void generateTrapdoor(Block trapdoor, TextureMapping texturemapping, boolean orientable) {
		MultiVariant top = plainVariant((orientable ? ModelTemplates.ORIENTABLE_TRAPDOOR_TOP : ModelTemplates.TRAPDOOR_TOP).create(trapdoor, texturemapping, this.modelOutput));
		Identifier bottom = (orientable ? ModelTemplates.ORIENTABLE_TRAPDOOR_BOTTOM : ModelTemplates.TRAPDOOR_BOTTOM).create(trapdoor, texturemapping, this.modelOutput);
		MultiVariant open = plainVariant((orientable ? ModelTemplates.ORIENTABLE_TRAPDOOR_OPEN : ModelTemplates.TRAPDOOR_OPEN).create(trapdoor, texturemapping, this.modelOutput));
		this.blockStateOutput.accept(createTrapdoor(trapdoor, top, plainVariant(bottom), open));
		this.registerSimpleItemModel(trapdoor, bottom);
	}

	//holy ternary batman
	public void generateDoor(Block door, boolean useSideTexture) {
		TextureMapping texturemapping = useSideTexture ? TFTextureMapping.sideDoor(door) : TextureMapping.door(door);
		MultiVariant bottomLeft = plainVariant((useSideTexture ? TFModelTemplates.CORRECTED_DOOR_BOTTOM_LEFT : ModelTemplates.DOOR_BOTTOM_LEFT).create(door, texturemapping, this.modelOutput));
		MultiVariant bottomLeftOpen = plainVariant((useSideTexture ? TFModelTemplates.CORRECTED_DOOR_BOTTOM_LEFT_OPEN : ModelTemplates.DOOR_BOTTOM_LEFT_OPEN).create(door, texturemapping, this.modelOutput));
		MultiVariant bottomRight = plainVariant((useSideTexture ? TFModelTemplates.CORRECTED_DOOR_BOTTOM_RIGHT : ModelTemplates.DOOR_BOTTOM_RIGHT).create(door, texturemapping, this.modelOutput));
		MultiVariant bottomRightOpen = plainVariant((useSideTexture ? TFModelTemplates.CORRECTED_DOOR_BOTTOM_RIGHT_OPEN : ModelTemplates.DOOR_BOTTOM_RIGHT_OPEN).create(door, texturemapping, this.modelOutput));
		MultiVariant topLeft = plainVariant((useSideTexture ? TFModelTemplates.CORRECTED_DOOR_TOP_LEFT : ModelTemplates.DOOR_TOP_LEFT).create(door, texturemapping, this.modelOutput));
		MultiVariant topLeftOpen = plainVariant((useSideTexture ? TFModelTemplates.CORRECTED_DOOR_TOP_LEFT_OPEN : ModelTemplates.DOOR_TOP_LEFT_OPEN).create(door, texturemapping, this.modelOutput));
		MultiVariant topRight = plainVariant((useSideTexture ? TFModelTemplates.CORRECTED_DOOR_TOP_RIGHT : ModelTemplates.DOOR_TOP_RIGHT).create(door, texturemapping, this.modelOutput));
		MultiVariant topRightOpen = plainVariant((useSideTexture ? TFModelTemplates.CORRECTED_DOOR_TOP_RIGHT_OPEN : ModelTemplates.DOOR_TOP_RIGHT_OPEN).create(door, texturemapping, this.modelOutput));
		this.registerSimpleFlatItemModel(door.asItem());
		this.blockStateOutput.accept(createDoor(door, bottomLeft, bottomLeftOpen, bottomRight, bottomRightOpen, topLeft, topLeftOpen, topRight, topRightOpen));
	}

	public void generateDoor(Block door, TextureMapping texturemapping, boolean flag) {
		MultiVariant bottomLeft = plainVariant((flag ? TFModelTemplates.CORRECTED_DOOR_BOTTOM_LEFT : ModelTemplates.DOOR_BOTTOM_LEFT).create(door, texturemapping, this.modelOutput));
		MultiVariant bottomLeftOpen = plainVariant((flag ? TFModelTemplates.CORRECTED_DOOR_BOTTOM_LEFT_OPEN : ModelTemplates.DOOR_BOTTOM_LEFT_OPEN).create(door, texturemapping, this.modelOutput));
		MultiVariant bottomRight = plainVariant((flag ? TFModelTemplates.CORRECTED_DOOR_BOTTOM_RIGHT : ModelTemplates.DOOR_BOTTOM_RIGHT).create(door, texturemapping, this.modelOutput));
		MultiVariant bottomRightOpen = plainVariant((flag ? TFModelTemplates.CORRECTED_DOOR_BOTTOM_RIGHT_OPEN : ModelTemplates.DOOR_BOTTOM_RIGHT_OPEN).create(door, texturemapping, this.modelOutput));
		MultiVariant topLeft = plainVariant((flag ? TFModelTemplates.CORRECTED_DOOR_TOP_LEFT : ModelTemplates.DOOR_TOP_LEFT).create(door, texturemapping, this.modelOutput));
		MultiVariant topLeftOpen = plainVariant((flag ? TFModelTemplates.CORRECTED_DOOR_TOP_LEFT_OPEN : ModelTemplates.DOOR_TOP_LEFT_OPEN).create(door, texturemapping, this.modelOutput));
		MultiVariant topRight = plainVariant((flag ? TFModelTemplates.CORRECTED_DOOR_TOP_RIGHT : ModelTemplates.DOOR_TOP_RIGHT).create(door, texturemapping, this.modelOutput));
		MultiVariant topRightOpen = plainVariant((flag ? TFModelTemplates.CORRECTED_DOOR_TOP_RIGHT_OPEN : ModelTemplates.DOOR_TOP_RIGHT_OPEN).create(door, texturemapping, this.modelOutput));
		this.registerSimpleFlatItemModel(door.asItem());
		this.blockStateOutput.accept(createDoor(door, bottomLeft, bottomLeftOpen, bottomRight, bottomRightOpen, topLeft, topLeftOpen, topRight, topRightOpen));
	}

	public void generateBanister(Block banister, TextureMapping mapping) {
		Identifier connected = TFModelTemplates.BANISTER_CONNECTED.create(banister, mapping, this.modelOutput);
		Identifier connectedExtended = TFModelTemplates.BANISTER_CONNECTED_EXTENDED.create(banister, mapping, this.modelOutput);
		Identifier shorty = TFModelTemplates.BANISTER_SHORT.create(banister, mapping, this.modelOutput);
		Identifier shortExtended = TFModelTemplates.BANISTER_SHORT_EXTENDED.create(banister, mapping, this.modelOutput);
		Identifier tall = TFModelTemplates.BANISTER_TALL.create(banister, mapping, this.modelOutput);
		Identifier tallExtended = TFModelTemplates.BANISTER_TALL_EXTENDED.create(banister, mapping, this.modelOutput);
		this.blockStateOutput.accept(createBanister(banister, connected, connectedExtended, shorty, shortExtended, tall, tallExtended));
		Identifier inventory = TFModelTemplates.BANISTER_INVENTORY.create(banister, mapping, this.modelOutput);
		this.registerSimpleItemModel(banister, inventory);
	}

	public static BlockModelDefinitionGenerator createBanister(Block banister, Identifier connected, Identifier connectedExtended, Identifier shorty, Identifier shortExtended, Identifier tall, Identifier tallExtended) {
		return MultiVariantGenerator.dispatch(banister).with(
			PropertyDispatch.initial(BanisterBlock.EXTENDED, BanisterBlock.SHAPE).generate((extended, shape) -> {
				Identifier model = switch (shape) {
					case SHORT -> extended ? shortExtended : shorty;
					case TALL -> extended ? tallExtended : tall;
					case CONNECTED -> extended ? connectedExtended : connected;
				};
				return plainVariant(model);
			})
		).with(ROTATION_HORIZONTAL_FACING_ALT);
	}

	public void generateDryingRack(Block rack, TextureMapping mapping) {
		Identifier rackModel = TFModelTemplates.DRYING_RACK.create(rack, mapping, this.modelOutput);
		this.blockStateOutput.accept(MultiVariantGenerator.dispatch(rack, plainVariant(rackModel)).with(ROTATION_HORIZONTAL_FACING_ALT));
		if (rack.asItem() != net.minecraft.world.item.Items.AIR) {
			this.registerSimpleItemModel(rack, rackModel);
		}
	}

	public void generateHollowLog(Block log, Block stripped, Block horizontal, Block vertical, Block climbable) {
		TextureMapping base = new TextureMapping()
			.put(TextureSlot.SIDE, TextureMapping.getBlockTexture(log))
			.put(TextureSlot.TOP, TextureMapping.getBlockTexture(log, "_top"))
			.put(TFTextureSlot.INNER, TextureMapping.getBlockTexture(stripped));
		Identifier horizModel = TFModelTemplates.HORIZONTAL_HOLLOW_LOG.create(horizontal, base, this.modelOutput);
		Identifier mossModel = TFModelTemplates.HORIZONTAL_HOLLOW_LOG_CARPET.createWithSuffix(horizontal, "_moss", base.put(TFTextureSlot.CARPET, TextureMapping.getBlockTexture(TFBlocks.MOSS_PATCH.get())).put(TFTextureSlot.OVERHANG, new Material(TwilightForestMod.prefix("block/moss_overhang"))), this.modelOutput);
		Identifier grassModel = TFModelTemplates.HORIZONTAL_HOLLOW_LOG_PLANT.createWithSuffix(horizontal, "_grass", base.put(TextureSlot.PLANT, TextureMapping.getBlockTexture(Blocks.SHORT_GRASS)).put(TFTextureSlot.CARPET, TextureMapping.getBlockTexture(TFBlocks.MOSS_PATCH.get())).put(TFTextureSlot.OVERHANG, new Material(TwilightForestMod.prefix("block/moss_overhang"))), this.modelOutput);
		Identifier snowModel = TFModelTemplates.HORIZONTAL_HOLLOW_LOG_CARPET.createWithSuffix(horizontal, "_snow", base.put(TFTextureSlot.CARPET, TextureMapping.getBlockTexture(Blocks.SNOW)).put(TFTextureSlot.OVERHANG, new Material(TwilightForestMod.prefix("block/snow_overhang"))), this.modelOutput);
		Identifier vertModel = TFModelTemplates.VERTICAL_HOLLOW_LOG.create(vertical, base, this.modelOutput);
		Identifier ladderModel = TFModelTemplates.CLIMBABLE_HOLLOW_LOG.createWithSuffix(climbable, "_ladder", base.put(TFTextureSlot.CLIMBABLE, TextureMapping.getBlockTexture(Blocks.LADDER)), this.modelOutput);
		Identifier vineModel = TFModelTemplates.CLIMBABLE_HOLLOW_LOG.createWithSuffix(climbable, "_vine", base.put(TFTextureSlot.CLIMBABLE, TextureMapping.getBlockTexture(Blocks.VINE)), this.modelOutput);
		this.blockStateOutput.accept(createHorizontalHollowLog(horizontal, horizModel, mossModel, grassModel, snowModel));
		this.blockStateOutput.accept(createSimpleBlock(vertical, plainVariant(vertModel)));
		this.blockStateOutput.accept(createClimableHollowLog(climbable, ladderModel, vineModel));
		this.registerSimpleItemModel(horizontal, horizModel);
	}

	public static BlockModelDefinitionGenerator createHorizontalHollowLog(Block horizontal, Identifier base, Identifier moss, Identifier grass, Identifier snow) {
		return MultiVariantGenerator.dispatch(horizontal).with(
			PropertyDispatch.initial(HorizontalHollowLogBlock.VARIANT).generate(variant -> {
				Identifier model = switch (variant) {
					case MOSS -> moss;
					case MOSS_AND_GRASS -> grass;
					case SNOW -> snow;
					default -> base;
				};
				return plainVariant(model);
			})
		).with(PropertyDispatch.modify(BlockStateProperties.HORIZONTAL_AXIS)
			.select(Direction.Axis.Z, NOP)
			.select(Direction.Axis.X, Y_ROT_90));
	}

	public static BlockModelDefinitionGenerator createClimableHollowLog(Block climbable, Identifier ladder, Identifier vine) {
		return MultiVariantGenerator.dispatch(climbable).with(
			PropertyDispatch.initial(ClimbableHollowLogBlock.VARIANT).generate(variant -> {
				Identifier model = switch (variant) {
					case VINE -> vine;
					case LADDER, LADDER_WATERLOGGED -> ladder;
				};
				return plainVariant(model);
			})
		).with(ROTATION_HORIZONTAL_FACING_ALT);
	}

	public void generateTreeCore(Block log, Block core) {
		Identifier off = ModelTemplates.CUBE_COLUMN.create(core, TextureMapping.column(TextureMapping.getBlockTexture(core), TextureMapping.getBlockTexture(log, "_top")), this.modelOutput);
		Identifier on = ModelTemplates.CUBE_COLUMN.createWithSuffix(core, "_on", TextureMapping.column(TextureMapping.getBlockTexture(core, "_on"), TextureMapping.getBlockTexture(log, "_top")), this.modelOutput);
		this.blockStateOutput.accept(MultiVariantGenerator.dispatch(core).with(PropertyDispatch.initial(SpecialMagicLogBlock.ACTIVE).generate(active -> plainVariant(active ? on : off))));
		this.generateBlockItem(core);
	}

	public void generateChiseledBookshelf(Block shelf) {
		MultiVariant variant = plainVariant(ModelLocationUtils.getModelLocation(shelf));
		MultiPartGenerator multipartgenerator = MultiPartGenerator.multiPart(shelf);
		forEachHorizontalDirection((direction, mutator) -> {
				Condition condition = condition(BlockStateProperties.HORIZONTAL_FACING, direction).build();
				multipartgenerator.with(condition, variant.with(mutator).with(UV_LOCK));
				this.addSlotStateAndRotationVariants(shelf, multipartgenerator, condition, mutator);
			}
		);
		this.blockStateOutput.accept(multipartgenerator);
		this.registerSimpleItemModel(shelf, ModelTemplates.CUBE_ORIENTABLE.createWithSuffix(shelf, "_inventory", new TextureMapping()
			.put(TextureSlot.SIDE, new Material(TwilightForestMod.prefix("block/wood/" + BuiltInRegistries.BLOCK.getKey(shelf).getPath() + "_side")))
			.put(TextureSlot.FRONT, new Material(TwilightForestMod.prefix("block/wood/" + BuiltInRegistries.BLOCK.getKey(shelf).getPath() + "_empty")))
			.put(TextureSlot.TOP, new Material(TwilightForestMod.prefix("block/wood/" + BuiltInRegistries.BLOCK.getKey(shelf).getPath() + "_top"))), this.modelOutput));
		CHISELED_BOOKSHELF_SLOT_MODEL_CACHE.clear();
	}

	public void addSlotStateAndRotationVariants(Block shelf, MultiPartGenerator generator, Condition condition, VariantMutator rotation) {
		List.of(
				Pair.of(BlockStateProperties.SLOT_0_OCCUPIED, ModelTemplates.CHISELED_BOOKSHELF_SLOT_TOP_LEFT),
				Pair.of(BlockStateProperties.SLOT_1_OCCUPIED, ModelTemplates.CHISELED_BOOKSHELF_SLOT_TOP_MID),
				Pair.of(BlockStateProperties.SLOT_2_OCCUPIED, ModelTemplates.CHISELED_BOOKSHELF_SLOT_TOP_RIGHT),
				Pair.of(BlockStateProperties.SLOT_3_OCCUPIED, ModelTemplates.CHISELED_BOOKSHELF_SLOT_BOTTOM_LEFT),
				Pair.of(BlockStateProperties.SLOT_4_OCCUPIED, ModelTemplates.CHISELED_BOOKSHELF_SLOT_BOTTOM_MID),
				Pair.of(BlockStateProperties.SLOT_5_OCCUPIED, ModelTemplates.CHISELED_BOOKSHELF_SLOT_BOTTOM_RIGHT)
			)
			.forEach(pair -> {
				BooleanProperty occupied = pair.getFirst();
				ModelTemplate template = pair.getSecond();
				this.addBookSlotModel(shelf, generator, condition, rotation, occupied, template, true);
				this.addBookSlotModel(shelf, generator, condition, rotation, occupied, template, false);
			});
	}

	public void addBookSlotModel(Block shelf, MultiPartGenerator generator, Condition condition, VariantMutator rotation, BooleanProperty property, ModelTemplate template, boolean occupied) {
		String suffix = occupied ? "_occupied" : "_empty";
		TextureMapping texturemapping = new TextureMapping().put(TextureSlot.TEXTURE, new Material(TwilightForestMod.prefix("block/wood/" + BuiltInRegistries.BLOCK.getKey(shelf).getPath() + suffix)));
		BlockModelGenerators.BookSlotModelCacheKey cache = new BlockModelGenerators.BookSlotModelCacheKey(template, suffix);
		MultiVariant variant = plainVariant(CHISELED_BOOKSHELF_SLOT_MODEL_CACHE.computeIfAbsent(cache, key -> template.createWithSuffix(shelf, suffix, texturemapping, this.modelOutput)));
		generator.with(
			new CombinedCondition(CombinedCondition.Operation.AND, List.of(condition, condition().term(property, occupied).build())),
			variant.with(rotation)
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
