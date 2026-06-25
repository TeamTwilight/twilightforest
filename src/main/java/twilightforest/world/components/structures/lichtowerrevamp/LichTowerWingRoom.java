package twilightforest.world.components.structures.lichtowerrevamp;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntArraySet;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.ints.IntSet;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.*;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.util.*;
import net.minecraft.world.RandomizableContainer;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.core.Holder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BrewingStandBlockEntity;
import net.minecraft.world.level.block.entity.LecternBlockEntity;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.*;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.templatesystem.LiquidSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.minecraft.world.level.storage.loot.LootTable;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.common.world.PieceBeardifierModifier;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import twilightforest.TwilightForestMod;
import tamaized.beanification.Autowired;
import twilightforest.block.ChiseledCanopyShelfBlock;
import twilightforest.block.LightableBlock;
import twilightforest.block.SkullCandleBlock;
import twilightforest.block.WroughtIronFenceBlock;
import twilightforest.block.entity.MasonJarBlockEntity;
import twilightforest.block.entity.bookshelf.ChiseledCanopyShelfBlockEntity;
import twilightforest.block.entity.spawner.SinisterSpawnerBlockEntity;
import twilightforest.entity.monster.DeathTome;
import twilightforest.init.*;
import twilightforest.loot.TFLootTables;
import twilightforest.util.BoundingBoxUtils;
import twilightforest.util.DirectionUtil;
import twilightforest.util.RotationUtil;
import twilightforest.util.WorldUtil;
import twilightforest.util.entities.EntityUtil;
import twilightforest.util.jigsaw.JigsawPlaceContext;
import twilightforest.util.jigsaw.JigsawRecord;
import twilightforest.util.jigsaw.JigsawUtil;
import twilightforest.world.components.structures.SpawnIndexProvider;
import twilightforest.world.components.structures.TwilightJigsawPiece;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

public final class LichTowerWingRoom extends TwilightJigsawPiece implements PieceBeardifierModifier, SpawnIndexProvider {
	@Autowired
	private static LichTowerUtil lichTowerUtil;

	private final int roomSize;
	private final boolean generateGround;
	private final int ladderIndex;
	private final String jigsawLadderTarget;
	private final int roofFallback;
	private final int[] allowedCeilingPlacements;

	public LichTowerWingRoom(StructurePieceSerializationContext ctx, CompoundTag compoundTag) {
		super(TFStructurePieceTypes.LICH_WING_ROOM.get(), compoundTag, ctx, readSettings(compoundTag));

		LichTowerUtil.addDefaultProcessors(this.placeSettings.addProcessor(lichTowerUtil.getRoomSpawnerProcessor()));
		this.placeSettings().setLiquidSettings(LiquidSettings.IGNORE_WATERLOGGING);

		this.roomSize = compoundTag.getIntOr("room_size", 0);
		this.generateGround = compoundTag.getBooleanOr("gen_ground", false);
		this.ladderIndex = compoundTag.getIntOr("ladder_index", -1);
		this.jigsawLadderTarget = this.shouldLadderUpwards() ? this.getSpareJigsaws().get(this.ladderIndex).target() : "";
		this.roofFallback = compoundTag.getIntOr("roof_index", -1);
		this.allowedCeilingPlacements = compoundTag.getIntArray("allowed_ceiling_placements").orElse(new int[0]);
	}

	public LichTowerWingRoom(StructureTemplateManager structureManager, int genDepth, JigsawPlaceContext jigsawContext, Identifier roomId, int roomSize, boolean generateGround, boolean canGenerateLadder, RandomSource random) {
		super(TFStructurePieceTypes.LICH_WING_ROOM.get(), genDepth, structureManager, roomId, jigsawContext);

		LichTowerUtil.addDefaultProcessors(this.placeSettings.addProcessor(lichTowerUtil.getRoomSpawnerProcessor()));
		this.placeSettings().setLiquidSettings(LiquidSettings.IGNORE_WATERLOGGING);

		this.roomSize = roomSize;
		this.generateGround = generateGround;

		Set<String> ladderPlacements = canGenerateLadder ? lichTowerUtil.getLadderPlacementsForSize(this.roomSize) : Collections.emptySet();
		this.ladderIndex = canGenerateLadder ? this.pickFirstIndex(this.getSpareJigsaws(), ladderPlacements::contains) : -1;

		this.jigsawLadderTarget = this.shouldLadderUpwards() ? this.getSpareJigsaws().get(this.ladderIndex).target() : "";
		this.roofFallback = canGenerateLadder ? this.pickFirstIndex(this.getSpareJigsaws(), "twilightforest:lich_tower/roof"::equals) : -1;

		this.allowedCeilingPlacements = generateCeilingPlacements(random, this.template, this.boundingBox, this.placeSettings);
	}

	// Generate coord list with elements not orthogonally adjacent to any other element. Assumes room to be square
	private static int[] generateCeilingPlacements(RandomSource random, StructureTemplate template, BoundingBox box, StructurePlaceSettings placeSettings) {
		int width = Math.max(box.getXSpan(), box.getZSpan());

		if (width <= 0) return new int[0];

		ObjectArrayList<StructureTemplate.StructureBlockInfo> blockInfos = template.filterBlocks(BlockPos.ZERO, placeSettings, Blocks.STRUCTURE_BLOCK, false);
		blockInfos.removeIf(info -> {
			CompoundTag nbt = info.nbt();
			if (nbt == null || nbt.isEmpty()) return false;
			String metadata = nbt.getStringOr("metadata", "");
			return !(metadata.startsWith("rope") || metadata.startsWith("chain"));
		});

		if (blockInfos.isEmpty()) return new int[0];

		filterAlignedPieces(random, blockInfos);

		int[] xIndexedZOffsets = new int[width];
		Arrays.fill(xIndexedZOffsets, -1);

		for (int i = 0; i < blockInfos.size(); i++) {
			BlockPos pos = blockInfos.get(i).pos();
			xIndexedZOffsets[pos.getX()] = pos.getZ();
		}

		return xIndexedZOffsets;
	}

	// Removes random elements from the list until all positions have unique X and Z coordinate values
	private static void filterAlignedPieces(RandomSource random, ObjectArrayList<StructureTemplate.StructureBlockInfo> blockInfos) {
		Util.shuffle(blockInfos, random);
		IntSet xDistinct = new IntArraySet();
		IntSet zDistinct = new IntArraySet();

		for (int i = 0; i < blockInfos.size(); i++) {
			StructureTemplate.StructureBlockInfo blockInfo = blockInfos.get(i);
			if (blockInfo.nbt() == null || filterMetadata(random, blockInfo.nbt())) continue;

			BlockPos pos = blockInfo.pos();
			if (xDistinct.contains(pos.getX()) || zDistinct.contains(pos.getZ())) {
				blockInfos.remove(i);
				i--;
			} else {
				xDistinct.add(pos.getX());
				zDistinct.add(pos.getZ());
			}
		}
	}

	private static boolean filterMetadata(RandomSource random, CompoundTag nbt) {
		if (nbt.isEmpty() || !nbt.contains("metadata"))
			return true;

		String metadata = nbt.getStringOr("metadata", "").split("%", 1)[0];
		String chance = metadata.startsWith("rope") ? metadata.substring("rope".length()) : metadata.substring("chain".length());

		return chance.isBlank() || StringUtils.isNumeric(chance) && random.nextFloat() > Integer.parseInt(chance) * 0.01f;
	}

	private int pickFirstIndex(List<JigsawRecord> spareJigsaws, Predicate<String> filter) {
		for (int i = 0; i < spareJigsaws.size(); i++) {
			if (filter.test(spareJigsaws.get(i).target())) {
				return i;
			}
		}

		return -1;
	}

	@Override
	protected void addAdditionalSaveData(StructurePieceSerializationContext ctx, CompoundTag structureTag) {
		super.addAdditionalSaveData(ctx, structureTag);

		structureTag.putInt("room_size", this.roomSize);
		structureTag.putBoolean("gen_ground", this.generateGround);
		structureTag.putInt("ladder_index", this.ladderIndex);
		structureTag.putInt("roof_index", this.roofFallback);
		structureTag.putIntArray("allowed_ceiling_placements", this.allowedCeilingPlacements);
	}

	@Override
	protected void processJigsaw(TwilightJigsawPiece parent, StructurePieceAccessor pieceAccessor, Structure.GenerationContext context, JigsawRecord connection, int jigsawIndex) {
		switch (connection.target()) {
			case "twilightforest:lich_tower/bridge" -> {
				if (this.roomSize < 1) {
					return;
				}

				boolean terminate = (this.genDepth > 30 || context.random().nextInt(this.towerStackIndex() * 2 + 1) == 0);
				boolean tooCloseToGround = this.generateGround || (this.boundingBox.getYSpan() > 11 && connection.pos().getY() < 7);

				if (terminate || tooCloseToGround) {
					LichTowerWingBridge.putCover(this, pieceAccessor, context, connection.pos(), connection.orientation(), this.structureManager, true, this.genDepth + 1);
				} else {
					LichTowerWingBridge.tryRoomAndBridge(this, pieceAccessor, context, connection, this.structureManager, false, this.roomSize - context.random().nextInt(2), false, this.genDepth + 1, null);
				}

				return;
			}
			case "twilightforest:lich_tower/roof" -> {
				if (!this.shouldLadderUpwards()) {
					this.putRoof(pieceAccessor, context, connection);
				}
				return;
			}
			case "twilightforest:lich_tower/beard" -> {
				if (this.hasLadderBelowRoom()) {
					// Instead of placing a beard structure piece, this piece generates ground with the Beardifier!
					// Or there's a ladder entering the room from underneath
					return;
				}

				FrontAndTop orientationToMatch = getVerticalOrientation(connection, Direction.DOWN, this);

				if (this.generateGround) {
					Identifier trim = lichTowerUtil.getTrim(context.random(), this.roomSize);
					this.tryBeard(pieceAccessor, context, connection, trim, orientationToMatch, true, true);
				} else {
					for (Identifier beardLocation : lichTowerUtil.shuffledBeards(context.random(), this.roomSize)) {
						if (this.tryBeard(pieceAccessor, context, connection, beardLocation, orientationToMatch, false, false)) {
							return;
						}
					}

					Identifier fallbackBeard = lichTowerUtil.getFallbackBeard(context.random(), this.roomSize);
					this.tryBeard(pieceAccessor, context, connection, fallbackBeard, orientationToMatch, true, false);
				}
			}
			case "twilightforest:lich_tower/decor" -> {
				LichTowerRoomDecor.addDecor(this, pieceAccessor, context, connection, this.genDepth + 1, this.structureManager);
			}
		}

		if (this.ladderIndex == jigsawIndex && this.jigsawLadderTarget.equals(connection.target())) {
			int ladderOffset = Integer.parseInt(this.jigsawLadderTarget.substring(this.jigsawLadderTarget.length() - 1));
			Identifier roomId = lichTowerUtil.getRoomUpwards(context.random(), this.roomSize, ladderOffset);
			if (roomId != null && (this.templateName.equals(roomId.toString()) || (parent.getTemplateName().equals(roomId.toString())))) {
				// 1 chance at reroll if template is same as current or parent's
				roomId = lichTowerUtil.getRoomUpwards(context.random(), this.roomSize, ladderOffset);
				// Otherwise if a repeat gets rolled -- how lucky!
			}
			BlockPos topPos = connection.pos().offset(0, this.boundingBox.getYSpan() - connection.pos().getY() - 1, 0);
			JigsawPlaceContext placeableJunction = JigsawPlaceContext.pickPlaceableJunction(this.templatePosition(), topPos, connection.orientation(), this.structureManager, roomId, connection.target(), context.random());

			if (placeableJunction != null) {
				//BoundingBox aboveRoomBounds = BoundingBoxUtils.extrusionFrom(placeableJunction.makeBoundingBox(this.structureManager.getOrCreate(roomId)), Direction.UP, 11);
				//BoundingBox aboveRoomBounds = placeableJunction.makeBoundingBox(this.structureManager.getOrCreate(roomId));
				//BoundingBox aboveRoomBounds = BoundingBoxUtils.extrusionFrom(this.boundingBox, Direction.UP, Mth.ceil(this.boundingBox.getYSpan() * 1.5f));

				// boolean canGenerateLadder = this.getSourceJigsaw().orientation().front().getAxis().isHorizontal() && random.nextBoolean() && pieceAccessor.findCollisionPiece(aboveRoomBounds) == null;
				boolean canFitRoomAbove = placeableJunction.isWithoutCollision(this.structureManager, pieceAccessor, box -> BoundingBoxUtils.extrusionFrom(box, Direction.UP, Mth.ceil(box.getYSpan() * 1.5f)));
				boolean canGenerateLadder = canFitRoomAbove && this.getSourceJigsaw().orientation().front().getAxis().isHorizontal() && context.random().nextBoolean();
				LichTowerWingRoom room = new LichTowerWingRoom(this.structureManager, this.genDepth + 1, placeableJunction, roomId, this.roomSize, false, canGenerateLadder, context.random());

				BoundingBox boundingBox = BoundingBoxUtils.cloneWithAdjustments(room.getBoundingBox(), 1, 0, 1, -1, 0, -1);
				if (pieceAccessor.findCollisionPiece(boundingBox) == null) {
					pieceAccessor.addPiece(room);
					room.addJigsaws(this, pieceAccessor, context);

					return;
				}
			}

			Direction front = connection.orientation().front();
			if (front != Direction.UP) {
				TwilightForestMod.LOGGER.error("Jigsaw {} was facing {} inside of {}", connection.name(), front, this.templateName);
			}

			if (this.roofFallback >= 0) {
				// if (!FMLLoader.isProduction()) TwilightForestMod.LOGGER.error("Failed to generate room above {}", this.templatePosition.offset(topPos));
				// If the room above cannot generate, then place the roof instead
				this.putRoof(pieceAccessor, context, this.getSpareJigsaws().get(this.roofFallback));
			}
		}
	}

	private int towerStackIndex() {
		boolean hasRoomAbove = this.shouldLadderUpwards();
		boolean hasRoomBelow = this.hasLadderBelowRoom();
		return hasRoomAbove && !hasRoomBelow ? 0 : hasRoomAbove ? 1 : 2;
	}

	private boolean putRoof(StructurePieceAccessor pieceAccessor, Structure.GenerationContext context, JigsawRecord connection) {
		FrontAndTop orientationToMatch = getVerticalOrientation(connection, Direction.UP, this);
		BoundingBox roofExtension = BoundingBoxUtils.extrusionFrom(this.boundingBox.minX(), this.boundingBox.maxY() + 1, this.boundingBox.minZ(), this.boundingBox.maxX(), this.boundingBox.maxY() + 1, this.boundingBox.maxZ(), orientationToMatch.top().getOpposite(), 1);
		boolean doSideAttachment = connection.orientation().front().getAxis().isHorizontal() && pieceAccessor.findCollisionPiece(roofExtension) != null;

		for (Identifier roofLocation : lichTowerUtil.shuffledRoofs(context.random(), this.roomSize, doSideAttachment)) {
			if (tryRoof(pieceAccessor, context, connection, roofLocation, orientationToMatch, false, this, this.genDepth + 1, this.structureManager)) {
				return true;
			}
		}

		Identifier fallbackRoof = lichTowerUtil.getFallbackRoof(context.random(), this.roomSize, doSideAttachment);
		tryRoof(pieceAccessor, context, connection, fallbackRoof, orientationToMatch, true, this, this.genDepth + 1, this.structureManager);
		return false;
	}

	@NotNull
	public static FrontAndTop getVerticalOrientation(JigsawRecord connection, Direction vertical, TwilightJigsawPiece towerRoom) {
		JigsawRecord sourceJigsaw = towerRoom.getSourceJigsaw();
		Direction sourceDirection = JigsawUtil.getAbsoluteHorizontal(sourceJigsaw != null ? sourceJigsaw.orientation() : connection.orientation());

		return FrontAndTop.fromFrontAndTop(vertical, sourceDirection.getOpposite());
	}

	public static boolean tryRoof(StructurePieceAccessor pieceAccessor, Structure.GenerationContext context, JigsawRecord connection, @Nullable Identifier roofLocation, FrontAndTop orientationToMatch, boolean allowClipping, TwilightJigsawPiece parent, int newDepth, StructureTemplateManager structureManager) {
		JigsawPlaceContext placeableJunction = JigsawPlaceContext.pickPlaceableJunction(parent.templatePosition(), connection.pos(), orientationToMatch, structureManager, roofLocation, "twilightforest:lich_tower/roof", context.random());

		if (placeableJunction != null) {
			LichTowerWingRoof roofPiece = new LichTowerWingRoof(newDepth, structureManager, roofLocation, placeableJunction);

			if (allowClipping || pieceAccessor.findCollisionPiece(roofPiece.generationCollisionBox()) == null) {
				pieceAccessor.addPiece(roofPiece);
				roofPiece.addJigsaws(parent, pieceAccessor, context);

				return true;
			}
		}
		return false;
	}

	private boolean tryBeard(StructurePieceAccessor pieceAccessor, Structure.GenerationContext context, JigsawRecord connection, @Nullable Identifier beardLocation, FrontAndTop orientationToMatch, boolean allowClipping, boolean generateGround) {
		JigsawPlaceContext placeableJunction = JigsawPlaceContext.pickPlaceableJunction(this.templatePosition(), connection.pos(), orientationToMatch, this.structureManager, beardLocation, "twilightforest:lich_tower/beard", context.random());

		if (placeableJunction != null) {
			LichTowerWingBeard beardPiece = new LichTowerWingBeard(this.genDepth + 1, this.structureManager, beardLocation, placeableJunction, generateGround);

			if (allowClipping || pieceAccessor.findCollisionPiece(beardPiece.generationCollisionBox()) == null) {
				pieceAccessor.addPiece(beardPiece);
				beardPiece.addJigsaws(this, pieceAccessor, context);

				return true;
			}
		}
		return false;
	}

	@Override
	public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator chunkGen, RandomSource random, BoundingBox chunkBounds, ChunkPos chunkPos, BlockPos structureCenterPos) {
		super.postProcess(level, structureManager, chunkGen, random, chunkBounds, chunkPos, structureCenterPos);

		JigsawRecord sourceJigsaw = this.getSourceJigsaw();
		if (this.hasLadderBelowRoom()) {
			BlockPos placeAt = this.templatePosition.offset(sourceJigsaw.pos());
			if (chunkBounds.isInside(placeAt)) {
				BlockState ladderBlock = Blocks.LADDER.defaultBlockState().setValue(LadderBlock.FACING, sourceJigsaw.orientation().top());
				level.setBlock(placeAt.below(), ladderBlock, Block.UPDATE_CLIENTS);
				level.setBlock(placeAt, ladderBlock, Block.UPDATE_CLIENTS);
				level.setBlock(placeAt.above(), ladderBlock, Block.UPDATE_CLIENTS);
				BlockState airBlock = Blocks.AIR.defaultBlockState();
				for (BlockPos pos : BlockPos.betweenClosed(placeAt.above(2), placeAt.above(5))) {
					level.setBlock(pos, airBlock, Block.UPDATE_CLIENTS);
				}
			}
		}

		if (this.shouldLadderUpwards()) {
			JigsawRecord ladderJigsaw = this.getSpareJigsaws().get(this.ladderIndex);
			BlockPos ladderOffset = ladderJigsaw.pos();
			BlockPos startPos = this.templatePosition.offset(ladderOffset);
			if (chunkBounds.isInside(startPos)) {
				BlockPos endPos = startPos.above(this.boundingBox.getYSpan() - ladderOffset.getY() - 1);
				BlockState ladderBlock = Blocks.LADDER.defaultBlockState().setValue(LadderBlock.FACING, ladderJigsaw.orientation().top());
				for (BlockPos placeAt : BlockPos.betweenClosed(startPos, endPos.below())) {
					level.setBlock(placeAt, ladderBlock, Block.UPDATE_CLIENTS);
				}
				level.setBlock(endPos, Blocks.COBBLESTONE.defaultBlockState(), Block.UPDATE_CLIENTS);
			}
		}

		if (this.generateGround) {
			fillCorner(level, new BlockPos(this.boundingBox.minX(), this.boundingBox.minY(), this.boundingBox.minZ()), chunkBounds);
			fillCorner(level, new BlockPos(this.boundingBox.maxX(), this.boundingBox.minY(), this.boundingBox.minZ()), chunkBounds);
			fillCorner(level, new BlockPos(this.boundingBox.maxX(), this.boundingBox.minY(), this.boundingBox.maxZ()), chunkBounds);
			fillCorner(level, new BlockPos(this.boundingBox.minX(), this.boundingBox.minY(), this.boundingBox.maxZ()), chunkBounds);
		}
	}

	private static void fillCorner(WorldGenLevel level, BlockPos pos, BoundingBox chunkBounds) {
		if (chunkBounds.isInside(pos)) {
			level.setBlock(pos, Blocks.STONE_BRICKS.defaultBlockState(), Block.UPDATE_ALL);
			level.setBlock(pos.above(), Blocks.STONE_BRICKS.defaultBlockState(), Block.UPDATE_ALL);
		}
	}

	private boolean hasLadderBelowRoom() {
		return this.getSourceJigsaw().orientation().front() == Direction.DOWN;
	}

	private boolean shouldLadderUpwards() {
		return this.ladderIndex >= 0;
	}

	private boolean canHangBlock(BlockPos pos) {
		int dX = pos.getX() - this.boundingBox.minX();
		int dZ = pos.getZ() - this.boundingBox.minZ();

		return dX >= 0 && dX < this.allowedCeilingPlacements.length && this.allowedCeilingPlacements[dX] == dZ;
	}

	@Override
	protected void handleDataMarker(String label, BlockPos pos, WorldGenLevel level, RandomSource random, BoundingBox chunkBounds, ChunkGenerator chunkGen, Rotation rotation) {
		String[] modifiedLabel = label.split(">");

		String variety = modifiedLabel.length == 2 ? modifiedLabel[1] : label;
		if (modifiedLabel.length == 2) {
			if (modifiedLabel[0].startsWith("rope")) {
				pos = this.danglingBlock(pos, level, random, TFBlocks.ROPE.value().defaultBlockState(), modifiedLabel[0].substring("rope".length()));
				if (pos == null) return;
			} if (modifiedLabel[0].startsWith("chain")) {
				pos = this.danglingBlock(pos, level, random, Blocks.IRON_CHAIN.defaultBlockState(), modifiedLabel[0].substring("chain".length()));
				if (pos == null) return;
			} else if (modifiedLabel[0].equals("pedestal")) {

				level.setBlock(pos, TFBlocks.TWISTED_STONE_PILLAR.value().defaultBlockState(), Block.UPDATE_CLIENTS);

				pos = pos.above();
			} else if (modifiedLabel[0].equals("below")) {
				// Used for the 9x9 winding_ways room
				pos = pos.below();
			}
		}

		String[] directionSplit = variety.split("@");

		if (directionSplit.length == 0) return;

		Rotation dataRotation = directionSplit.length == 1
			? Rotation.CLOCKWISE_180
			: RotationUtil.getRelativeRotation(Direction.NORTH, DirectionUtil.fromStringOrElse(directionSplit[1], Direction.SOUTH));

		String[] permutationSplit = directionSplit[0].split("\\|");

		if (permutationSplit.length == 0) return;

		String chosenLabel = Util.getRandom(permutationSplit, random);
		String[] parameters = chosenLabel.split(":");

		if (parameters.length == 0) return;

		level.removeBlock(pos, false); // Clears block entity data left by Data Marker

		this.handleDataParams(pos, level, WorldUtil.getRegistryAccess(), random, parameters, dataRotation);
	}

	private @Nullable BlockPos danglingBlock(BlockPos pos, WorldGenLevel level, RandomSource random, BlockState binding, String parameters) {
		String[] ropeChance = parameters.split("%");

		if (ropeChance.length == 0 || !this.canHangBlock(pos))
			return null;

		String ropeParams = ropeChance[ropeChance.length - 1];

		int ropeLength = this.parseRange(ropeParams, random, 1, 2);

		if (ropeLength > 0) {
			for (BlockPos hangSupportAt : BlockPos.betweenClosed(pos, pos.below(ropeLength - 1))) {
				level.setBlock(hangSupportAt, binding, Block.UPDATE_CLIENTS);
			}

			pos = pos.below(ropeLength);
		}
		return pos;
	}

	private void handleDataParams(BlockPos pos, WorldGenLevel level, RegistryAccess registryAccess, RandomSource random, String[] parameters, Rotation dataRotation) {
		switch (parameters[0]) {
			case "air", "empty" -> {} // No-Op; block already replaced
			case "bookshelf" -> level.setBlock(pos, Blocks.BOOKSHELF.defaultBlockState(), Block.UPDATE_CLIENTS);
			case "canopy_shelf", "canopy_bookshelf" -> level.setBlock(pos, TFBlocks.CANOPY_BOOKSHELF.value().defaultBlockState(), Block.UPDATE_CLIENTS);
			case "stone_brick_slab" -> level.setBlock(pos, Blocks.STONE_BRICK_SLAB.defaultBlockState(), Block.UPDATE_CLIENTS);
			case "lava" -> level.setBlock(pos, Blocks.LAVA.defaultBlockState(), Block.UPDATE_CLIENTS);
			case "water" -> level.setBlock(pos, Blocks.WATER.defaultBlockState(), Block.UPDATE_CLIENTS);
			case "firefly_jar" -> level.setBlock(pos, TFBlocks.FIREFLY_JAR.value().defaultBlockState(), Block.UPDATE_CLIENTS);
			case "terrorcotta_arcs" -> level.setBlock(pos, TFBlocks.TERRORCOTTA_ARCS.value().defaultBlockState(), Block.UPDATE_CLIENTS);
			case "mason_jar" -> this.putMasonJar(pos, level, random, parameters);
			case "canopy_slab" -> level.setBlock(pos, TFBlocks.CANOPY_SLAB.value().defaultBlockState(), Block.UPDATE_CLIENTS);
			case "canopy_stairs" -> level.setBlock(pos, TFBlocks.CANOPY_STAIRS.value().defaultBlockState(), Block.UPDATE_CLIENTS);
			case "creeper_head" -> this.putHead(pos, level, random, parameters, Blocks.CREEPER_HEAD, dataRotation);
			case "skeleton_skull" -> this.putHead(pos, level, random, parameters, Blocks.SKELETON_SKULL, dataRotation);
			case "wither_skull" -> this.putHead(pos, level, random, parameters, Blocks.WITHER_SKELETON_SKULL, dataRotation);
			case "zombie_head" -> this.putHead(pos, level, random, parameters, Blocks.ZOMBIE_HEAD, dataRotation);
			case "creeper_candle" -> this.putHeadCandles(pos, level, random, parameters, TFBlocks.CREEPER_SKULL_CANDLE.value(), dataRotation);
			case "skeleton_candle" -> this.putHeadCandles(pos, level, random, parameters, TFBlocks.SKELETON_SKULL_CANDLE.value(), dataRotation);
			case "wither_candle" -> this.putHeadCandles(pos, level, random, parameters, TFBlocks.WITHER_SKELE_SKULL_CANDLE.value(), dataRotation);
			case "zombie_candle" -> this.putHeadCandles(pos, level, random, parameters, TFBlocks.ZOMBIE_SKULL_CANDLE.value(), dataRotation);
			case "spawner" -> this.putSpawner(pos, level, random, parameters);
			case "sinister_spawner" -> this.putSinisterSpawner(pos, level, random, parameters);
			case "brewing_stand" -> this.putBrewingStand(pos, level, random);
			case "lectern" -> this.putTrappableLectern(pos, level, dataRotation, random.nextBoolean());
			case "chiseled_canopy_shelf" -> this.putTrappableBookshelf(pos, level, registryAccess, random, dataRotation);
			case "chest" -> this.putChest(pos, level, random, parameters, dataRotation, Blocks.CHEST.defaultBlockState());
			case "trapped_chest" -> this.putChest(pos, level, random, parameters, dataRotation, Blocks.TRAPPED_CHEST.defaultBlockState());
			case "candle", "candles" -> this.putCandles(parameters, random, level, pos, Blocks.CANDLE.defaultBlockState());
			case "white_candle" -> this.putCandles(parameters, random, level, pos, Blocks.WHITE_CANDLE.defaultBlockState());
			case "orange_candle" -> this.putCandles(parameters, random, level, pos, Blocks.ORANGE_CANDLE.defaultBlockState());
			case "magenta_candle" -> this.putCandles(parameters, random, level, pos, Blocks.MAGENTA_CANDLE.defaultBlockState());
			case "light_blue_candle" -> this.putCandles(parameters, random, level, pos, Blocks.LIGHT_BLUE_CANDLE.defaultBlockState());
			case "yellow_candle" -> this.putCandles(parameters, random, level, pos, Blocks.YELLOW_CANDLE.defaultBlockState());
			case "lime_candle" -> this.putCandles(parameters, random, level, pos, Blocks.LIME_CANDLE.defaultBlockState());
			case "pink_candle" -> this.putCandles(parameters, random, level, pos, Blocks.PINK_CANDLE.defaultBlockState());
			case "gray_candle" -> this.putCandles(parameters, random, level, pos, Blocks.GRAY_CANDLE.defaultBlockState());
			case "light_gray_candle" -> this.putCandles(parameters, random, level, pos, Blocks.LIGHT_GRAY_CANDLE.defaultBlockState());
			case "cyan_candle" -> this.putCandles(parameters, random, level, pos, Blocks.CYAN_CANDLE.defaultBlockState());
			case "purple_candle" -> this.putCandles(parameters, random, level, pos, Blocks.PURPLE_CANDLE.defaultBlockState());
			case "blue_candle" -> this.putCandles(parameters, random, level, pos, Blocks.BLUE_CANDLE.defaultBlockState());
			case "brown_candle" -> this.putCandles(parameters, random, level, pos, Blocks.BROWN_CANDLE.defaultBlockState());
			case "green_candle" -> this.putCandles(parameters, random, level, pos, Blocks.GREEN_CANDLE.defaultBlockState());
			case "red_candle" -> this.putCandles(parameters, random, level, pos, Blocks.RED_CANDLE.defaultBlockState());
			case "black_candle" -> this.putCandles(parameters, random, level, pos, Blocks.BLACK_CANDLE.defaultBlockState());
			case "water_cauldron" -> this.putWaterCauldron(parameters, random, level, pos);
			case "zombie_trap" -> this.putZombieTrap(random, level, pos);
			case "wrought_iron_post" -> {
				level.setBlock(pos, TFBlocks.WROUGHT_IRON_FENCE.value().defaultBlockState().setValue(WroughtIronFenceBlock.POST, WroughtIronFenceBlock.PostState.POST), Block.UPDATE_CLIENTS);
				level.getChunk(pos).markPosForPostprocessing(pos);
			}
			case "empty_lectern" -> {
				Rotation stateRotation = this.placeSettings.getRotation().getRotated(dataRotation);
				level.setBlock(pos, Blocks.LECTERN.defaultBlockState().rotate(stateRotation), Block.UPDATE_CLIENTS);
			}
			case "candled_lectern" -> {
				if (random.nextInt(4) != 0) {
					this.putCandles(parameters, random, level, pos.above(), Blocks.CANDLE.defaultBlockState());
				} else {
					this.putHeadCandles(pos.above(), level, random, parameters, TFBlocks.SKELETON_SKULL_CANDLE.value(), dataRotation);
				}

				Rotation stateRotation = this.placeSettings.getRotation().getRotated(dataRotation);
				level.setBlock(pos, Blocks.LECTERN.defaultBlockState().rotate(stateRotation), Block.UPDATE_CLIENTS);
			}
			default -> {
				Rotation stateRotation = this.placeSettings.getRotation().getRotated(dataRotation);
				BlockState blockState = this.blockFromLabel(parameters[0]).rotate(stateRotation);
				if (!blockState.isAir()) {
					level.setBlock(pos, blockState, Block.UPDATE_CLIENTS);
				} else if (!FMLEnvironment.getDist().isDedicatedServer()) {
					TwilightForestMod.LOGGER.warn("Variation label {} ({}) obtained {} in {}", parameters[0], parameters, blockState, this.templateName);
				}
			}
		}
	}

	private void putMasonJar(BlockPos pos, WorldGenLevel level, RandomSource random, String[] parameters) {
		BlockState jar = TFBlocks.MASON_JAR.value().defaultBlockState();
		level.setBlock(pos, jar, Block.UPDATE_CLIENTS);

		if (parameters.length >= 2) {
			if (level.getBlockEntity(pos) instanceof MasonJarBlockEntity jarEntity) {
				String label = parameters[1];
				ResourceKey<LootTable> lootTableId = switch (label) {
					case "jar" -> TFLootTables.TOWER_JARS;
					case "room" -> TFLootTables.TOWER_ROOM;
					case "library" -> TFLootTables.TOWER_LIBRARY;
					case "potion" -> TFLootTables.TOWER_POTION;
					case "enchanting" -> TFLootTables.TOWER_ENCHANTING;
					default -> ResourceKey.create(Registries.LOOT_TABLE, Identifier.bySeparator(label, '.'));
				};
				if (!jarEntity.fillFromLootTable(lootTableId, random.nextLong(), level.getLevel())) {
					Identifier itemId = Identifier.bySeparator(label, '.');
					jarEntity.getItemHandler().setItem(new ItemStack(level.registryAccess().lookup(Registries.ITEM).flatMap(reg -> reg.get(itemId)).map(Holder.Reference::value).orElse(Items.AIR)));
				}
				int itemRotation = this.placeSettings.getRotation().ordinal() * 4 + (parameters.length == 3 ? this.getHeadRotation(parameters[2], random) : 0);
				jarEntity.setItemRotation(Math.floorMod(itemRotation, 16));
			}
		}

		if (level.getBlockState(pos.above()).is(TFBlocks.CANOPY_BOOKSHELF)) {
			level.setBlock(pos.above(), TFBlocks.CANOPY_SLAB.value().defaultBlockState().setValue(SlabBlock.TYPE, SlabType.TOP), Block.UPDATE_CLIENTS);
		}
	}

	private void putBrewingStand(BlockPos pos, WorldGenLevel level, RandomSource random) {
		BlockState brewingStandBlock = Blocks.BREWING_STAND.defaultBlockState()
			.setValue(BrewingStandBlock.HAS_BOTTLE[0], true)
			.setValue(BrewingStandBlock.HAS_BOTTLE[1], true)
			.setValue(BrewingStandBlock.HAS_BOTTLE[2], true);

		level.setBlock(pos, brewingStandBlock, Block.UPDATE_CLIENTS);
		if (level.getBlockEntity(pos) instanceof BrewingStandBlockEntity brewingStandBE) {ItemStack potionStack = new ItemStack(random.nextInt(4) == 0 ? Items.SPLASH_POTION : Items.POTION);
			potionStack.set(DataComponents.POTION_CONTENTS, new PotionContents(switch (random.nextInt(7)) {
				case 6 -> Potions.STRONG_HEALING;
				case 4, 5 -> Potions.REGENERATION;
				case 1, 2, 3 -> Potions.HEALING;
				default -> Potions.WATER;
			}));
			for (int index = 0; index < 3; index++) {
				brewingStandBE.setItem(index, potionStack.copy());
			}
			brewingStandBE.setItem(4, new ItemStack(Items.BLAZE_POWDER, random.nextIntBetweenInclusive(1, 5)));
			brewingStandBE.fuel = random.nextIntBetweenInclusive(10, 20);
		}
	}

	private void putSpawner(BlockPos pos, WorldGenLevel level, RandomSource random, String[] parameters) {
		level.setBlock(pos, Blocks.SPAWNER.defaultBlockState(), Block.UPDATE_CLIENTS);

		if (level.getBlockEntity(pos) instanceof SpawnerBlockEntity spawner) {
			this.configureBaseSpawner(pos, random, parameters, spawner.getSpawner());

			if (parameters.length == 3 && StringUtils.isNumeric(parameters[2])) {
				spawner.getSpawner().spawnRange = Mth.clamp(Integer.parseInt(parameters[2]), 1, 16);
			}
		}
	}

	private void putSinisterSpawner(BlockPos pos, WorldGenLevel level, RandomSource random, String[] parameters) {
		level.setBlock(pos, TFBlocks.SINISTER_SPAWNER.value().defaultBlockState(), Block.UPDATE_CLIENTS);

		if (!(level.getBlockEntity(pos) instanceof SinisterSpawnerBlockEntity spawner))
			return;

		spawner.addParticle(TFParticleType.OMINOUS_FLAME.value(), false);
		spawner.setLootTable(TFLootTables.OMINOUS_SPAWNER_DROPS);

		this.configureBaseSpawner(pos, random, parameters, spawner.getSpawner());

		if (parameters.length >= 3 && StringUtils.isNumeric(parameters[2])) {
			spawner.getSpawner().spawnRange = Mth.clamp(Integer.parseInt(parameters[2]), 1, 16);
		}

		if (parameters.length >= 4 && StringUtils.isNumeric(parameters[3])) {
			spawner.getSpawner().entityScanRange = Mth.clamp(Integer.parseInt(parameters[3]), 1, 32);
		} else {
			spawner.getSpawner().entityScanRange = spawner.getSpawner().spawnRange;
		}
	}

	private void configureBaseSpawner(BlockPos pos, RandomSource random, String[] parameters, BaseSpawner spawner) {
		EntityType<?> monster = this.pickRandomMob(random, parameters);

		CompoundTag entityToSpawn = new CompoundTag();
		entityToSpawn.putString("id", BuiltInRegistries.ENTITY_TYPE.getKey(monster).toString());
		SpawnData spawnData = new SpawnData(entityToSpawn, Optional.of(new SpawnData.CustomSpawnRules(new InclusiveRange<>(0, 7), new InclusiveRange<>(0, 15))), Optional.empty());
		spawner.setNextSpawnData(null, pos, spawnData);
	}

	private EntityType<?> pickRandomMob(RandomSource random, String[] parameters) {
		if (parameters.length >= 2) {
			String[] monsters = parameters[1].split(",");
			return monsters.length == 0 ? this.defaultRandomMob(random) : randomMobFromParams(random, monsters);
		}

		return this.defaultRandomMob(random);
	}

	@NotNull
	private EntityType<?> defaultRandomMob(RandomSource random) {
		return switch (random.nextInt(10)) {
			case 7, 8, 9 -> EntityType.SKELETON;
			case 6 -> EntityType.SPIDER;
			case 5 -> EntityType.CAVE_SPIDER;
			case 4 -> TFEntities.HEDGE_SPIDER.value();
			case 3 -> TFEntities.SWARM_SPIDER.value();
			default -> EntityType.ZOMBIE;
		};
	}

	private static EntityType<?> randomMobFromParams(RandomSource random, String[] monsters) {
		String label = Util.getRandom(monsters, random);
		return switch (label) {
			case "hedge_spider" -> TFEntities.HEDGE_SPIDER.value();
			case "swarm_spider" -> TFEntities.SWARM_SPIDER.value();
			default -> EntityType.byString(label).orElse(EntityType.ZOMBIE);
		};
	}

	private void putCandles(String[] parameters, RandomSource random, WorldGenLevel level, BlockPos pos, BlockState candle) {
		int amount = Math.min(4, parameters.length == 2 ? this.getCandleRanged(parameters[1], random) : random.nextIntBetweenInclusive(1, 3));
		if (amount <= 0) return;
		BlockState candles = candle.setValue(CandleBlock.LIT, true).setValue(CandleBlock.CANDLES, amount);
		level.setBlock(pos, candles, Block.UPDATE_CLIENTS);
	}

	private void putWaterCauldron(String[] parameters, RandomSource random, WorldGenLevel level, BlockPos pos) {
		int amount = Math.min(3, parameters.length == 2 ? this.parseRange(parameters[1], random, 1, 3) : random.nextIntBetweenInclusive(1, 3));

		BlockState cauldron = amount <= 0 ? Blocks.CAULDRON.defaultBlockState() : Blocks.WATER_CAULDRON.defaultBlockState().setValue(BlockStateProperties.LEVEL_CAULDRON, amount);
		level.setBlock(pos, cauldron, Block.UPDATE_CLIENTS);
	}

	private void putZombieTrap(RandomSource random, WorldGenLevel level, BlockPos pos) {
		WroughtIronFenceBlock.PostState postProperty = level.getBlockState(pos.above()).isAir() ? WroughtIronFenceBlock.PostState.CAPPED : WroughtIronFenceBlock.PostState.POST;
		BlockState fenceBlock = TFBlocks.WROUGHT_IRON_FENCE.value().defaultBlockState().setValue(WroughtIronFenceBlock.POST, postProperty);
		level.setBlock(pos, fenceBlock, Block.UPDATE_CLIENTS);
		level.getChunk(pos).markPosForPostprocessing(pos);

		Direction randomDirection = this.getRandomDirectionInsideChunk(level, random, pos);

		if (randomDirection.getAxis() == Direction.Axis.Y) return;

		BlockPos zombiePos = pos.relative(randomDirection, 1);

		var knot = EntityUtil.createEntityIgnoreException(level, EntityType.LEASH_KNOT);
		var trapEntity = EntityUtil.createEntityIgnoreException(level, EntityType.ZOMBIE);
		if (knot == null || trapEntity == null)
			return;

		knot.snapTo(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);

		trapEntity.setPersistenceRequired();
		trapEntity.setLeashedTo(knot, false);
		trapEntity.snapTo(zombiePos.getX() + 0.5, zombiePos.getY() - 1, zombiePos.getZ() + 0.5);
		trapEntity.setData(TFDataAttachments.LEASH_PATHFINDER_OVERRIDE, Unit.INSTANCE);
		level.addFreshEntity(trapEntity);
	}

	private @NotNull Direction getRandomDirectionInsideChunk(WorldGenLevel level, RandomSource random, BlockPos pos) {
		int xInChunk = SectionPos.sectionRelative(pos.getX());
		int zInChunk = SectionPos.sectionRelative(pos.getZ());

		List<Direction> directions = Direction.Plane.HORIZONTAL.shuffledCopy(random);

		if (xInChunk == 0) directions.remove(Direction.WEST);
		if (zInChunk == 0) directions.remove(Direction.NORTH);
		if (xInChunk == 15) directions.remove(Direction.EAST);
		if (zInChunk == 15) directions.remove(Direction.SOUTH);



		if (directions.isEmpty())
			return Direction.UP;

		Direction randomDirection = Util.getRandom(directions, random);
		return randomDirection;
	}

	private BlockState blockFromLabel(String label) {
		if (label.contains(".")) {
			return BuiltInRegistries.BLOCK.get(Identifier.bySeparator(label, '.')).get().value().defaultBlockState();
		} else {
			return BuiltInRegistries.BLOCK.get(Identifier.parse(label)).get().value().defaultBlockState();
		}
	}

	private void putChest(BlockPos pos, WorldGenLevel level, RandomSource random, String[] parameters, Rotation dataRotation, BlockState chestState) {
		Rotation stateRotation = this.placeSettings.getRotation().getRotated(dataRotation);
		BlockState chest = chestState.rotate(stateRotation);
		level.setBlock(pos, chest, Block.UPDATE_CLIENTS);

		if (parameters.length == 2 && level.getBlockEntity(pos) instanceof RandomizableContainer lootBlock) {
			ResourceKey<LootTable> lootTableId = switch (parameters[1]) {
				case "room" -> TFLootTables.TOWER_ROOM;
				case "library" -> TFLootTables.TOWER_LIBRARY;
				case "potion" -> TFLootTables.TOWER_POTION;
				case "enchanting" -> TFLootTables.TOWER_ENCHANTING;
				default -> ResourceKey.create(Registries.LOOT_TABLE, Identifier.bySeparator(parameters[1], '.'));
			};
			lootBlock.setLootTable(lootTableId, random.nextLong());
		}

		if (level.getBlockState(pos.above()).is(TFBlocks.CANOPY_BOOKSHELF)) {
			level.setBlock(pos.above(), TFBlocks.CANOPY_SLAB.value().defaultBlockState().setValue(SlabBlock.TYPE, SlabType.TOP), Block.UPDATE_CLIENTS);
		}
	}

	private void putTrappableBookshelf(BlockPos pos, WorldGenLevel level, RegistryAccess registryAccess, RandomSource random, Rotation dataRotation) {
		boolean isHostile = random.nextInt(12) == 0;
		Rotation stateRotation = this.placeSettings.getRotation().getRotated(dataRotation);
		BlockState shelf = TFBlocks.CHISELED_CANOPY_BOOKSHELF.value().defaultBlockState().setValue(ChiseledCanopyShelfBlock.SPAWNER, isHostile).rotate(stateRotation);

		IntList filledSlots = new IntArrayList();
		for (int index = 0; index < 6; index++) {
			if (random.nextInt(3) != 0) {
				filledSlots.add(index);
				shelf = shelf.setValue(ChiseledCanopyShelfBlock.SLOT_OCCUPIED_PROPERTIES.get(index), true);
			}
		}

		level.setBlock(pos, shelf, Block.UPDATE_CLIENTS);
		if (level.getBlockEntity(pos) instanceof ChiseledCanopyShelfBlockEntity shelfBlockEntity) {
			for (int index : filledSlots) {
				// Spawner shelves never contain enchanted books; Otherwise Chiseled Shelves have a 1/10 chance of generating an enchanted book instead of only a book
				ItemStack book = isHostile || random.nextInt(16) != 0 ? new ItemStack(Items.BOOK) : EnchantmentHelper.enchantItem(random, new ItemStack(Items.BOOK), random.nextIntBetweenInclusive(1, 40), registryAccess, Optional.empty());
				shelfBlockEntity.items.set(index, book);
			}

			if (isHostile) {
				shelfBlockEntity.getSpawner().setEntityId(TFEntities.DEATH_TOME.value(), null, random, pos);
			}
		}
	}

	private void putHead(BlockPos pos, WorldGenLevel level, RandomSource random, String[] parameters, Block headBlock, Rotation dataRotation) {
		int rotation = parameters.length >= 2 ? this.getHeadRotation(parameters[1], random) : random.nextIntBetweenInclusive(0, 15);
		Rotation stateRotation = this.placeSettings.getRotation().getRotated(dataRotation.getRotated(Rotation.CLOCKWISE_180));

		BlockState candledHeadState = headBlock.defaultBlockState().setValue(BlockStateProperties.ROTATION_16, rotation).rotate(stateRotation);
		level.setBlock(pos, candledHeadState, Block.UPDATE_CLIENTS);
	}

	private void putHeadCandles(BlockPos pos, WorldGenLevel level, RandomSource random, String[] parameters, Block candledHeadBlock, Rotation dataRotation) {
		int amount = Math.min(4, parameters.length >= 2 ? this.getCandleRanged(parameters[1], random) : random.nextIntBetweenInclusive(1, 3));
		if (amount <= 0) return;
		int rotation = parameters.length >= 3 ? this.getHeadRotation(parameters[2], random) : random.nextIntBetweenInclusive(0, 15);
		Rotation stateRotation = this.placeSettings.getRotation().getRotated(dataRotation.getRotated(Rotation.CLOCKWISE_180));

		BlockState candledHeadState = candledHeadBlock.defaultBlockState()
			.setValue(SkullCandleBlock.LIGHTING, LightableBlock.Lighting.NORMAL)
			.setValue(BlockStateProperties.CANDLES, amount)
			.setValue(BlockStateProperties.ROTATION_16, rotation)
			.rotate(stateRotation);
		level.setBlock(pos, candledHeadState, Block.UPDATE_CLIENTS);
	}

	private int getCandleRanged(String amountLabel, RandomSource random) {
		return this.parseRange(amountLabel, random, 1, 3); // Don't want 4 because it's too bright for the LT
	}

	@SuppressWarnings("SameParameterValue")
	private int parseRange(String amountLabel, RandomSource random, int defaultMin, int defaultMax) {
		String[] amountParams = amountLabel.split("-");

		if (amountParams.length == 1 && StringUtils.isNumeric(amountParams[0])) {
			return Integer.parseInt(amountParams[0]);
		} else if (amountParams.length == 2 && StringUtils.isNumeric(amountParams[0]) && StringUtils.isNumeric(amountParams[1])) {
			return random.nextIntBetweenInclusive(Integer.parseInt(amountParams[0]), Integer.parseInt(amountParams[1]));
		}

		return random.nextIntBetweenInclusive(defaultMin, defaultMax);
	}

	private int getHeadRotation(String amountLabel, RandomSource random) {
		String[] amountParams = amountLabel.split("\\+");

		if (amountParams.length == 1 && StringUtils.isNumeric(amountParams[0])) {
			return Integer.parseInt(amountParams[0]);
		} else if (amountParams.length == 2 && StringUtils.isNumeric(amountParams[0]) && StringUtils.isNumeric(amountParams[1])) {
			int src = Integer.parseInt(amountParams[0]);
			int extra = Integer.parseInt(amountParams[1]);
			return Math.floorMod(random.nextIntBetweenInclusive(src, src + extra), 16);
		}

		return random.nextIntBetweenInclusive(0, 15);
	}

	private void putTrappableLectern(BlockPos pos, WorldGenLevel level, Rotation dataRotation, boolean putMimic) {
		Rotation stateRotation = this.placeSettings.getRotation().getRotated(dataRotation);
		BlockState lectern = Blocks.LECTERN.defaultBlockState()
			.setValue(LecternBlock.HAS_BOOK, !putMimic)
			.rotate(stateRotation);

		level.setBlock(pos, lectern, Block.UPDATE_CLIENTS);

		if (putMimic) {
			DeathTome tomeMimic = TFEntities.DEATH_TOME.value().create(level.getLevel(), EntitySpawnReason.STRUCTURE);
			if (tomeMimic != null) {
				tomeMimic.setPersistenceRequired();
				tomeMimic.snapTo(pos, lectern.getValue(HorizontalDirectionalBlock.FACING).toYRot(), 0);
				tomeMimic.setOnLectern(true);
				tomeMimic.finalizeSpawn(level, level.getCurrentDifficultyAt(tomeMimic.blockPosition()), EntitySpawnReason.STRUCTURE, null);
				level.addFreshEntityWithPassengers(tomeMimic);
			}
		} else if (level.getBlockEntity(pos) instanceof LecternBlockEntity lecternBlockEntity) {
			lecternBlockEntity.setBook(new ItemStack(Items.WRITABLE_BOOK));
		}
	}

	@Override
	public BoundingBox getBeardifierBox() {
		return this.boundingBox;
	}

	@Override
	public TerrainAdjustment getTerrainAdjustment() {
		return TerrainAdjustment.NONE;
	}

	@Override
	public int getGroundLevelDelta() {
		return 0;
	}

	@Override
	public int getSpawnIndex() {
		return LichTowerPieces.INTERIOR_SPAWNS;
	}
}
