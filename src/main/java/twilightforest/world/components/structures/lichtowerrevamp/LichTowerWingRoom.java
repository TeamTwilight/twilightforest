package twilightforest.world.components.structures.lichtowerrevamp;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.FrontAndTop;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.InclusiveRange;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.RandomizableContainer;
import net.minecraft.world.entity.Display;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.SpawnData;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BrewingStandBlockEntity;
import net.minecraft.world.level.block.entity.LecternBlockEntity;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.common.world.PieceBeardifierModifier;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import twilightforest.TwilightForestMod;
import twilightforest.block.ChiseledCanopyShelfBlock;
import twilightforest.block.LightableBlock;
import twilightforest.block.SkullCandleBlock;
import twilightforest.block.entity.MasonJarBlockEntity;
import twilightforest.block.entity.bookshelf.ChiseledCanopyShelfBlockEntity;
import twilightforest.entity.monster.DeathTome;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFEntities;
import twilightforest.init.TFStructurePieceTypes;
import twilightforest.loot.TFLootTables;
import twilightforest.util.BoundingBoxUtils;
import twilightforest.util.DirectionUtil;
import twilightforest.util.RotationUtil;
import twilightforest.util.WorldUtil;
import twilightforest.util.jigsaw.JigsawPlaceContext;
import twilightforest.util.jigsaw.JigsawRecord;
import twilightforest.util.jigsaw.JigsawUtil;
import twilightforest.world.components.structures.TwilightJigsawPiece;
import twilightforest.world.components.structures.TwilightTemplateStructurePiece;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

public final class LichTowerWingRoom extends TwilightJigsawPiece implements PieceBeardifierModifier {
	private final int roomSize;
	private final boolean generateGround;
	private final int ladderIndex;
	private final String jigsawLadderTarget;
	private final int roofFallback;

	public LichTowerWingRoom(StructurePieceSerializationContext ctx, CompoundTag compoundTag) {
		super(TFStructurePieceTypes.LICH_WING_ROOM.get(), compoundTag, ctx, readSettings(compoundTag));

		LichTowerUtil.addDefaultProcessors(this.placeSettings.addProcessor(LichTowerUtil.ROOM_SPAWNERS));

		this.roomSize = compoundTag.getInt("room_size");
		this.generateGround = compoundTag.getBoolean("gen_ground");
		this.ladderIndex = compoundTag.getInt("ladder_index");
		this.jigsawLadderTarget = this.shouldLadderUpwards() ? this.getSpareJigsaws().get(this.ladderIndex).target() : "";
		this.roofFallback = compoundTag.getInt("roof_index");
	}

	public LichTowerWingRoom(StructureTemplateManager structureManager, int genDepth, JigsawPlaceContext jigsawContext, ResourceLocation roomId, int roomSize, boolean generateGround, boolean canGenerateLadder) {
		super(TFStructurePieceTypes.LICH_WING_ROOM.get(), genDepth, structureManager, roomId, jigsawContext);

		LichTowerUtil.addDefaultProcessors(this.placeSettings.addProcessor(LichTowerUtil.ROOM_SPAWNERS));

		this.roomSize = roomSize;
		this.generateGround = generateGround;

		Set<String> ladderPlacements = canGenerateLadder ? LichTowerUtil.getLadderPlacementsForSize(this.roomSize) : Collections.emptySet();
		this.ladderIndex = canGenerateLadder ? this.pickFirstIndex(this.getSpareJigsaws(), ladderPlacements::contains) : -1;

		this.jigsawLadderTarget = this.shouldLadderUpwards() ? this.getSpareJigsaws().get(this.ladderIndex).target() : "";
		this.roofFallback = canGenerateLadder ? this.pickFirstIndex(this.getSpareJigsaws(), "twilightforest:lich_tower/roof"::equals) : -1;
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
	}

	@Override
	protected void processJigsaw(StructurePiece parent, StructurePieceAccessor pieceAccessor, RandomSource random, JigsawRecord connection, int jigsawIndex) {
		switch (connection.target()) {
			case "twilightforest:lich_tower/bridge" -> {
				boolean generateGround = this.generateGround && connection.pos().getY() < 5;
				if (this.roomSize < 1) {
					return;
				} else if (this.genDepth > 30 || random.nextInt(this.towerStackIndex() * 2 + 1) == 0) {
					LichTowerWingBridge.putCover(this, pieceAccessor, random, connection.pos(), connection.orientation(), this.structureManager, generateGround, this.genDepth + 1);
				} else if (!generateGround) {
					LichTowerWingBridge.tryRoomAndBridge(this, pieceAccessor, random, connection, this.structureManager, false, this.roomSize - random.nextInt(2), generateGround, this.genDepth + 1, false);
				} else {
					LichTowerWingBridge.putCover(this, pieceAccessor, random, connection.pos(), connection.orientation(), this.structureManager, false, this.genDepth + 1);
				}

				return;
			}
			case "twilightforest:lich_tower/roof" -> {
				if (!this.shouldLadderUpwards()) {
					this.putRoof(pieceAccessor, random, connection);
				}
				return;
			}
			case "twilightforest:lich_tower/beard" -> {
				if (this.generateGround || this.hasLadderBelowRoom()) {
					// Instead of placing a beard structure piece, this piece generates ground with the Beardifier!
					// Or there's a ladder entering the room from underneath
					return;
				}

				FrontAndTop orientationToMatch = getVerticalOrientation(connection, Direction.DOWN, this);

				for (ResourceLocation beardLocation : LichTowerUtil.shuffledBeards(random, this.roomSize)) {
					if (this.tryBeard(pieceAccessor, random, connection, beardLocation, orientationToMatch, false)) {
						return;
					}
				}

				ResourceLocation fallbackBeard = LichTowerUtil.getFallbackBeard(this.roomSize);
				this.tryBeard(pieceAccessor, random, connection, fallbackBeard, orientationToMatch, true);
			}
			case "twilightforest:lich_tower/decor" -> {
				LichTowerRoomDecor.addDecor(this, pieceAccessor, random, connection, this.genDepth + 1, this.structureManager, false);
			}
		}

		if (this.ladderIndex == jigsawIndex && this.jigsawLadderTarget.equals(connection.target())) {
			int ladderOffset = Integer.parseInt(this.jigsawLadderTarget.substring(this.jigsawLadderTarget.length() - 1));
			ResourceLocation roomId = LichTowerUtil.getRoomUpwards(random, this.roomSize, ladderOffset);
			if (roomId != null && (this.templateName.equals(roomId.toString()) || (parent instanceof TwilightTemplateStructurePiece twilightTemplate && twilightTemplate.getTemplateName().equals(roomId.toString())))) {
				// 1 chance at reroll if template is same as current or parent's
				roomId = LichTowerUtil.getRoomUpwards(random, this.roomSize, ladderOffset);
				// Otherwise if a repeat gets rolled -- how lucky!
			}
			BlockPos topPos = connection.pos().offset(0, this.boundingBox.getYSpan() - connection.pos().getY() - 1, 0);
			JigsawPlaceContext placeableJunction = JigsawPlaceContext.pickPlaceableJunction(this.templatePosition(), topPos, connection.orientation(), this.structureManager, roomId, connection.target(), random);

			if (placeableJunction != null) {
				BoundingBox aboveRoomBounds = BoundingBoxUtils.extrusionFrom(placeableJunction.makeBoundingBox(this.structureManager.getOrCreate(roomId)), Direction.UP, 11);

				boolean canGenerateLadder = this.getSourceJigsaw().orientation().front().getAxis().isHorizontal() && random.nextBoolean() && pieceAccessor.findCollisionPiece(aboveRoomBounds) == null;
				StructurePiece room = new LichTowerWingRoom(this.structureManager, this.genDepth + 1, placeableJunction, roomId, this.roomSize, false, canGenerateLadder);

				BoundingBox boundingBox = BoundingBoxUtils.cloneWithAdjustments(room.getBoundingBox(), 1, 0, 1, -1, 0, -1);
				if (pieceAccessor.findCollisionPiece(boundingBox) == null) {
					pieceAccessor.addPiece(room);
					room.addChildren(parent, pieceAccessor, random);

					return;
				}
			}

			Direction front = connection.orientation().front();
			if (front != Direction.UP) {
				TwilightForestMod.LOGGER.error("Jigsaw was facing {} inside of {}", front, this.templateName);
			}

			if (this.roofFallback >= 0) {
				//TwilightForestMod.LOGGER.warn("\nGenerating roof instead of {} over {}, for target {} at {}", roomId, this.templateName, connection.target(), this.templatePosition);
				//TwilightForestMod.LOGGER.warn("Connection priority was {} and index {}. Source priority was {}", connection.priority(), jigsawIndex, this.getSourceJigsaw().priority());
				//TwilightForestMod.LOGGER.warn("\nJigsaws in piece: {}", this.structureManager.getOrCreate(roomId).filterBlocks(BlockPos.ZERO, new StructurePlaceSettings(), Blocks.JIGSAW));

				// If the room above cannot generate, then place the roof instead
				this.putRoof(pieceAccessor, random, this.getSpareJigsaws().get(this.roofFallback));
			}
		}
	}

	private int towerStackIndex() {
		boolean hasRoomAbove = this.shouldLadderUpwards();
		boolean hasRoomBelow = this.hasLadderBelowRoom();
		return hasRoomAbove && !hasRoomBelow ? 0 : hasRoomAbove ? 1 : 2;
	}

	private boolean putRoof(StructurePieceAccessor pieceAccessor, RandomSource random, JigsawRecord connection) {
		FrontAndTop orientationToMatch = getVerticalOrientation(connection, Direction.UP, this);
		BoundingBox roofExtension = BoundingBoxUtils.extrusionFrom(this.boundingBox.minX(), this.boundingBox.maxY() + 1, this.boundingBox.minZ(), this.boundingBox.maxX(), this.boundingBox.maxY() + 1, this.boundingBox.maxZ(), orientationToMatch.top().getOpposite(), 1);
		boolean doSideAttachment = connection.orientation().front().getAxis().isHorizontal() && pieceAccessor.findCollisionPiece(roofExtension) != null;

		for (ResourceLocation roofLocation : LichTowerUtil.shuffledRoofs(random, this.roomSize, doSideAttachment)) {
			if (tryRoof(pieceAccessor, random, connection, roofLocation, orientationToMatch, false, this, this.genDepth + 1, this.structureManager)) {
				return true;
			}
		}

		ResourceLocation fallbackRoof = LichTowerUtil.getFallbackRoof(this.roomSize, doSideAttachment);
		tryRoof(pieceAccessor, random, connection, fallbackRoof, orientationToMatch, true, this, this.genDepth + 1, this.structureManager);
		return false;
	}

	@NotNull
	public static FrontAndTop getVerticalOrientation(JigsawRecord connection, Direction vertical, TwilightJigsawPiece towerRoom) {
		JigsawRecord sourceJigsaw = towerRoom.getSourceJigsaw();
		Direction sourceDirection = JigsawUtil.getAbsoluteHorizontal(sourceJigsaw != null ? sourceJigsaw.orientation() : connection.orientation());

		return FrontAndTop.fromFrontAndTop(vertical, sourceDirection.getOpposite());
	}

	public static boolean tryRoof(StructurePieceAccessor pieceAccessor, RandomSource random, JigsawRecord connection, @Nullable ResourceLocation roofLocation, FrontAndTop orientationToMatch, boolean allowClipping, TwilightJigsawPiece parent, int newDepth, StructureTemplateManager structureManager) {
		JigsawPlaceContext placeableJunction = JigsawPlaceContext.pickPlaceableJunction(parent.templatePosition(), connection.pos(), orientationToMatch, structureManager, roofLocation, "twilightforest:lich_tower/roof", random);

		if (placeableJunction != null) {
			LichTowerWingRoof roofPiece = new LichTowerWingRoof(newDepth, structureManager, roofLocation, placeableJunction);

			if (allowClipping || pieceAccessor.findCollisionPiece(roofPiece.generationCollisionBox()) == null) {
				pieceAccessor.addPiece(roofPiece);
				roofPiece.addChildren(parent, pieceAccessor, random);

				return true;
			}
		}
		return false;
	}

	private boolean tryBeard(StructurePieceAccessor pieceAccessor, RandomSource random, JigsawRecord connection, @Nullable ResourceLocation beardLocation, FrontAndTop orientationToMatch, boolean allowClipping) {
		JigsawPlaceContext placeableJunction = JigsawPlaceContext.pickPlaceableJunction(this.templatePosition(), connection.pos(), orientationToMatch, this.structureManager, beardLocation, "twilightforest:lich_tower/beard", random);

		if (placeableJunction != null) {
			LichTowerWingBeard beardPiece = new LichTowerWingBeard(this.genDepth + 1, this.structureManager, beardLocation, placeableJunction);

			if (allowClipping || pieceAccessor.findCollisionPiece(beardPiece.generationCollisionBox()) == null) {
				pieceAccessor.addPiece(beardPiece);
				beardPiece.addChildren(this, pieceAccessor, random);

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
				level.setBlock(placeAt, ladderBlock, 2);
				level.setBlock(placeAt.above(), ladderBlock, 2);
				BlockState airBlock = Blocks.AIR.defaultBlockState();
				for (BlockPos pos : BlockPos.betweenClosed(placeAt.above(2), placeAt.above(5))) {
					level.setBlock(pos, airBlock, 2);
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
				for (BlockPos placeAt : BlockPos.betweenClosed(startPos, endPos)) {
					level.setBlock(placeAt, ladderBlock, 2);
				}
			}
		}

		// if (!FMLLoader.isProduction()) this.setInvisibleTextEntity(level, Mth.lerpInt(0.5f, this.boundingBox.minX(), this.boundingBox.maxX()), this.boundingBox.minY() + 3, Mth.lerpInt(0.5f, this.boundingBox.minZ(), this.boundingBox.maxZ()), chunkBounds, this.templateName, Display.BillboardConstraints.FIXED);
	}

	private void setInvisibleTextEntity(WorldGenLevel world, int x, int y, int z, BoundingBox sbb, String s, Display.BillboardConstraints billboardConstraint) {
		final BlockPos pos = new BlockPos(x, y, z);

		if (sbb.isInside(pos)) {
			final Display.TextDisplay display = new Display.TextDisplay(EntityType.TEXT_DISPLAY, world.getLevel());
			display.setText(Component.literal(s));
			display.setBillboardConstraints(billboardConstraint);
			display.moveTo(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, 0, 0);

			world.addFreshEntity(display);
		}
	}

	private boolean hasLadderBelowRoom() {
		JigsawRecord sourceJigsaw = this.getSourceJigsaw();
		return sourceJigsaw != null && sourceJigsaw.orientation().front() == Direction.DOWN;
	}

	private boolean shouldLadderUpwards() {
		return this.ladderIndex >= 0;
	}

	private static final int ROPE_SUBSTRING_START = "rope".length();
	@Override
	protected void handleDataMarker(String label, BlockPos pos, WorldGenLevel level, RandomSource random, BoundingBox chunkBounds, ChunkGenerator chunkGen) {
		String[] modifiedLabel = label.split(">");

		String variety = modifiedLabel.length == 2 ? modifiedLabel[1] : label;
		if (modifiedLabel.length == 2 && modifiedLabel[0].startsWith("rope")) {
			String[] ropeChance = modifiedLabel[0].substring(ROPE_SUBSTRING_START).split("%");

			if (ropeChance.length == 0 || (ropeChance.length == 2 && StringUtils.isNumeric(ropeChance[0]) && random.nextFloat() > Integer.parseInt(ropeChance[0]) * 0.01f))
				return;

			String ropeParams = ropeChance[ropeChance.length - 1];

			int ropeLength = this.parseRange(ropeParams, random, 1, 2);

			if (ropeLength > 0) {
				for (BlockPos hangSupportAt : BlockPos.betweenClosed(pos, pos.below(ropeLength - 1))) {
					level.setBlock(hangSupportAt, TFBlocks.ROPE.value().defaultBlockState(), 2);
				}

				pos = pos.below(ropeLength);
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

	private void handleDataParams(BlockPos pos, WorldGenLevel level, RegistryAccess registryAccess, RandomSource random, String[] parameters, Rotation dataRotation) {
		switch (parameters[0]) {
			case "air", "empty" -> {} // No-Op; block already replaced
			case "bookshelf" -> level.setBlock(pos, Blocks.BOOKSHELF.defaultBlockState(), 2);
			case "canopy_shelf", "canopy_bookshelf" -> level.setBlock(pos, TFBlocks.CANOPY_BOOKSHELF.value().defaultBlockState(), 2);
			case "stone_brick_slab" -> level.setBlock(pos, Blocks.STONE_BRICK_SLAB.defaultBlockState(), 2);
			case "firefly_jar" -> level.setBlock(pos, TFBlocks.FIREFLY_JAR.value().defaultBlockState(), 2);
			case "mason_jar" -> this.putMasonJar(pos, level, random, parameters);
			case "canopy_slab" -> level.setBlock(pos, TFBlocks.CANOPY_SLAB.value().defaultBlockState(), 2);
			case "creeper_head" -> this.putHead(pos, level, random, parameters, Blocks.CREEPER_HEAD, dataRotation);
			case "skeleton_skull" -> this.putHead(pos, level, random, parameters, Blocks.SKELETON_SKULL, dataRotation);
			case "wither_skull" -> this.putHead(pos, level, random, parameters, Blocks.WITHER_SKELETON_SKULL, dataRotation);
			case "zombie_head" -> this.putHead(pos, level, random, parameters, Blocks.ZOMBIE_HEAD, dataRotation);
			case "creeper_candle" -> this.putHeadCandles(pos, level, random, parameters, TFBlocks.CREEPER_SKULL_CANDLE.value(), dataRotation);
			case "skeleton_candle" -> this.putHeadCandles(pos, level, random, parameters, TFBlocks.SKELETON_SKULL_CANDLE.value(), dataRotation);
			case "wither_candle" -> this.putHeadCandles(pos, level, random, parameters, TFBlocks.WITHER_SKELE_SKULL_CANDLE.value(), dataRotation);
			case "zombie_candle" -> this.putHeadCandles(pos, level, random, parameters, TFBlocks.ZOMBIE_SKULL_CANDLE.value(), dataRotation);
			case "spawner" -> this.putSpawner(pos, level, random, parameters);
			case "brewing_stand" -> this.putBrewingStand(pos, level, random);
			case "lectern" -> this.putTrappableLectern(pos, level, dataRotation, random.nextBoolean() && WorldUtil.getDifficulty() != Difficulty.PEACEFUL);
			case "chiseled_canopy_shelf" -> this.putTrappableBookshelf(pos, level, registryAccess, random, dataRotation);
			case "chest" -> this.putChest(pos, level, random, parameters, dataRotation, Blocks.CHEST.defaultBlockState());
			case "trapped_chest" -> this.putChest(pos, level, random, parameters, dataRotation, Blocks.TRAPPED_CHEST.defaultBlockState());
			case "candle", "candles" -> this.putCandles(parameters, random, level, pos);
			case "empty_lectern" -> {
				Rotation stateRotation = this.placeSettings.getRotation().getRotated(dataRotation);
				level.setBlock(pos, Blocks.LECTERN.defaultBlockState().rotate(stateRotation), 2);
			}
			case "candled_lectern" -> {
				if (random.nextInt(4) != 0) {
					this.putCandles(parameters, random, level, pos.above());
				} else {
					this.putHeadCandles(pos.above(), level, random, parameters, TFBlocks.SKELETON_SKULL_CANDLE.value(), dataRotation);
				}

				Rotation stateRotation = this.placeSettings.getRotation().getRotated(dataRotation);
				level.setBlock(pos, Blocks.LECTERN.defaultBlockState().rotate(stateRotation), 2);
			}
			default -> {
				Rotation stateRotation = this.placeSettings.getRotation().getRotated(dataRotation);
				BlockState blockState = this.blockFromLabel(parameters[0]).rotate(stateRotation);
				if (!blockState.isAir()) {
					level.setBlock(pos, blockState, 2);
				} else if (!FMLLoader.isProduction()) {
					TwilightForestMod.LOGGER.warn("Variation label {} ({}) obtained {} in {}", parameters[0], parameters, blockState, this.templateName);
				}
			}
		}
	}

	private void putMasonJar(BlockPos pos, WorldGenLevel level, RandomSource random, String[] parameters) {
		BlockState jar = TFBlocks.MASON_JAR.value().defaultBlockState();
		level.setBlock(pos, jar, 2);

		if (parameters.length == 2 && level.getBlockEntity(pos) instanceof MasonJarBlockEntity jarEntity) {
			ResourceLocation lootTableId = switch (parameters[1]) {
				case "hall" -> TFLootTables.USELESS_LOOT.location(); // FIXME
				case "library" -> TFLootTables.TOWER_LIBRARY.location();
				case "potion" -> TFLootTables.TOWER_POTION.location();
				case "room" -> TFLootTables.TOWER_ROOM.location();
				default -> ResourceLocation.parse(parameters[1]);
			};
			jarEntity.fillFromLootTable(ResourceKey.create(Registries.LOOT_TABLE, lootTableId), random.nextLong());
		}

		if (level.getBlockState(pos.above()).is(TFBlocks.CANOPY_BOOKSHELF)) {
			level.setBlock(pos.above(), TFBlocks.CANOPY_SLAB.value().defaultBlockState().setValue(SlabBlock.TYPE, SlabType.TOP), 2);
		}
	}

	private void putBrewingStand(BlockPos pos, WorldGenLevel level, RandomSource random) {
		BlockState brewingStandBlock = Blocks.BREWING_STAND.defaultBlockState();

		IntList filledSlots = new IntArrayList();
		for (int index = 0; index < 3; index++) {
			if (random.nextInt(3) != 0) {
				filledSlots.add(index);
				brewingStandBlock = brewingStandBlock.setValue(BrewingStandBlock.HAS_BOTTLE[index], true);
			}
		}

		level.setBlock(pos, brewingStandBlock, 2);
		if (level.getBlockEntity(pos) instanceof BrewingStandBlockEntity brewingStandBE) {
			for (int index = 0; index < 3; index++) {
				ItemStack potionStack = new ItemStack(random.nextInt(4) == 0 ? Items.SPLASH_POTION : Items.POTION);
				potionStack.set(DataComponents.POTION_CONTENTS, new PotionContents(switch (random.nextInt(8)) {
					case 6 -> Potions.STRONG_HEALING;
					case 4, 5 -> Potions.REGENERATION;
					case 1, 2, 3 -> Potions.HEALING;
					default -> Potions.WATER;
				}));
				brewingStandBE.setItem(index, potionStack);
			}
			brewingStandBE.setItem(4, new ItemStack(Items.BLAZE_POWDER, random.nextIntBetweenInclusive(1, 5)));
			brewingStandBE.fuel = random.nextIntBetweenInclusive(10, 20);
		}
	}

	private void putSpawner(BlockPos pos, WorldGenLevel level, RandomSource random, String[] parameters) {
		level.setBlock(pos, Blocks.SPAWNER.defaultBlockState(), 2);

		if (parameters.length >= 2 && level.getBlockEntity(pos) instanceof SpawnerBlockEntity spawner) {
			String[] monsters = parameters[1].split(",");
			EntityType<?> monster = monsters.length == 0 ? switch (random.nextInt(10)) {
				case 7, 8, 9 -> EntityType.SKELETON;
				case 6 -> EntityType.SPIDER;
				case 5 -> EntityType.CAVE_SPIDER;
				case 4 -> TFEntities.HEDGE_SPIDER.value();
				case 3 -> TFEntities.SWARM_SPIDER.value();
				default -> EntityType.ZOMBIE;
			} : switch (Util.getRandom(monsters, random)) {
				case "skeleton" -> EntityType.SKELETON;
				case "spider" -> EntityType.SPIDER;
				case "cave_spider" -> EntityType.CAVE_SPIDER;
				case "hedge_spider" -> TFEntities.HEDGE_SPIDER.value();
				case "swarm_spider" -> TFEntities.SWARM_SPIDER.value();
				default -> EntityType.ZOMBIE;
			};

			CompoundTag entityToSpawn = new CompoundTag();
			entityToSpawn.putString("id", BuiltInRegistries.ENTITY_TYPE.getKey(monster).toString());
			SpawnData spawnData = new SpawnData(entityToSpawn, Optional.of(new SpawnData.CustomSpawnRules(new InclusiveRange<>(0, 7), new InclusiveRange<>(0, 15))), Optional.empty());
			spawner.getSpawner().setNextSpawnData(null, pos, spawnData);

			if (parameters.length == 3 && StringUtils.isNumeric(parameters[2])) {
				spawner.getSpawner().spawnRange = Mth.clamp(Integer.parseInt(parameters[2]), 1, 16);
			}
		}
	}

	private void putCandles(String[] parameters, RandomSource random, WorldGenLevel level, BlockPos pos) {
		int amount = Math.min(4, parameters.length == 2 ? this.getCandleRanged(parameters[1], random) : random.nextIntBetweenInclusive(1, 3));
		if (amount == 0) return;
		BlockState candles = Blocks.CANDLE.defaultBlockState().setValue(CandleBlock.LIT, true).setValue(CandleBlock.CANDLES, amount);
		level.setBlock(pos, candles, 2);
	}

	private BlockState blockFromLabel(String label) {
		if (label.contains(".")) {
			return BuiltInRegistries.BLOCK.get(ResourceLocation.bySeparator(label, '.')).defaultBlockState();
		} else {
			return BuiltInRegistries.BLOCK.get(ResourceLocation.parse(label)).defaultBlockState();
		}
	}

	private void putChest(BlockPos pos, WorldGenLevel level, RandomSource random, String[] parameters, Rotation dataRotation, BlockState chestState) {
		Rotation stateRotation = this.placeSettings.getRotation().getRotated(dataRotation);
		BlockState chest = chestState.rotate(stateRotation);
		level.setBlock(pos, chest, 2);

		if (parameters.length == 2 && level.getBlockEntity(pos) instanceof RandomizableContainer lootBlock) {
			ResourceLocation lootTableId = switch (parameters[1]) {
				case "hall" -> TFLootTables.USELESS_LOOT.location(); // FIXME
				case "library" -> TFLootTables.TOWER_LIBRARY.location();
				case "potion" -> TFLootTables.TOWER_POTION.location();
				case "room" -> TFLootTables.TOWER_ROOM.location();
				default -> ResourceLocation.parse(parameters[1]);
			};
			lootBlock.setLootTable(ResourceKey.create(Registries.LOOT_TABLE, lootTableId), random.nextLong());
		}

		if (level.getBlockState(pos.above()).is(TFBlocks.CANOPY_BOOKSHELF)) {
			level.setBlock(pos.above(), TFBlocks.CANOPY_SLAB.value().defaultBlockState().setValue(SlabBlock.TYPE, SlabType.TOP), 2);
		}
	}

	private void putTrappableBookshelf(BlockPos pos, WorldGenLevel level, RegistryAccess registryAccess, RandomSource random, Rotation dataRotation) {
		boolean isHostile = random.nextInt(8) == 0 && WorldUtil.getDifficulty() != Difficulty.PEACEFUL;
		Rotation stateRotation = this.placeSettings.getRotation().getRotated(dataRotation);
		BlockState shelf = TFBlocks.CHISELED_CANOPY_BOOKSHELF.value().defaultBlockState().setValue(ChiseledCanopyShelfBlock.SPAWNER, isHostile).rotate(stateRotation);

		IntList filledSlots = new IntArrayList();
		for (int index = 0; index < 6; index++) {
			if (random.nextInt(3) != 0) {
				filledSlots.add(index);
				shelf = shelf.setValue(ChiseledCanopyShelfBlock.SLOT_OCCUPIED_PROPERTIES.get(index), true);
			}
		}

		level.setBlock(pos, shelf, 2);
		if (level.getBlockEntity(pos) instanceof ChiseledCanopyShelfBlockEntity shelfBlockEntity) {
			for (int index : filledSlots) {
				// Spawner shelves never contain enchanted books; Otherwise Chiseled Shelves have a 1/5 chance of generating an enchanted book instead of only a book
				ItemStack book = isHostile || random.nextInt(5) != 0 ? new ItemStack(Items.BOOK) : EnchantmentHelper.enchantItem(random, new ItemStack(Items.BOOK), random.nextIntBetweenInclusive(1, 40), registryAccess, Optional.empty());
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
		level.setBlock(pos, candledHeadState, 2);
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
		level.setBlock(pos, candledHeadState, 2);
	}

	private int getCandleRanged(String amountLabel, RandomSource random) {
		return this.parseRange(amountLabel, random, 1, 3);
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

		level.setBlock(pos, lectern, 2);

		if (putMimic) {
			DeathTome tomeMimic = TFEntities.DEATH_TOME.value().create(level.getLevel());
			if (tomeMimic != null) {
				tomeMimic.setPersistenceRequired();
				tomeMimic.moveTo(pos, lectern.getValue(HorizontalDirectionalBlock.FACING).toYRot(), 0);
				tomeMimic.setOnLectern(true);
				tomeMimic.finalizeSpawn(level, level.getCurrentDifficultyAt(tomeMimic.blockPosition()), MobSpawnType.STRUCTURE, null);
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
		return this.generateGround ? TerrainAdjustment.BEARD_THIN : TerrainAdjustment.NONE;
	}

	@Override
	public int getGroundLevelDelta() {
		return this.roomSize == 0 ? 2 : 1;
	}
}
