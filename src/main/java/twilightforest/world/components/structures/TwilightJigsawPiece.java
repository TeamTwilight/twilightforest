package twilightforest.world.components.structures;

import com.mojang.serialization.DynamicOps;
import net.minecraft.core.BlockPos;
import net.minecraft.core.FrontAndTop;
import net.minecraft.core.Holder;
import net.minecraft.nbt.*;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.structure.*;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.templatesystem.*;
import net.neoforged.neoforge.common.world.PieceBeardifierModifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFStructurePieceTypes;
import twilightforest.util.jigsaw.JigsawPlaceContext;
import twilightforest.util.jigsaw.JigsawRecord;
import twilightforest.world.components.structures.markerhandler.TemplateMarkerHandler;
import twilightforest.world.components.structures.util.*;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Predicate;

public class TwilightJigsawPiece extends TwilightTemplateStructurePiece implements ProgressionPiece, PieceBeardifierModifier {

	private static final Logger LOGGER = LogManager.getLogger(TwilightForestMod.ID + "/TwilightJigsawPiece");

	private static final String NBT_JIGSAW_SOURCE = "source";
	private static final String NBT_JIGSAW_CONNECTIONS = "connections";
	private static final String NBT_TERRAIN_ADAPT = "terrain_adaptation";
	private static final String NBT_TEMPLATE_PROCESSORS = "template_processors";
	private static final String NBT_GROUND_OFFSET = "ground_offset";
	private static final String NBT_IGNORE_WATERLOG = "ignore_waterlog";
	private static final String NBT_MARKER_HANDLERS = "marker_handlers";

	private final JigsawRecord sourceJigsaw;
	private final List<JigsawRecord> spareJigsaws;
	private final TerrainAdjustment terrainAdjustment;
	private final Optional<Holder<StructureProcessorList>> processors;
	private final Optional<Holder<TemplateMarkerHandlerList>> markerHandlers;
	private final int beardifierGroundDelta;

	public static TwilightJigsawPiece defaultDeserialize(StructurePieceSerializationContext ctx, CompoundTag compoundTag) {
		TwilightJigsawPiece twilightJigsawPiece = new TwilightJigsawPiece(TFStructurePieceTypes.TFJigsawTemplate.value(), compoundTag, ctx, readSettings(compoundTag));
		twilightJigsawPiece.placeSettings().addProcessor(JigsawReplacementProcessor.INSTANCE);
		twilightJigsawPiece.placeSettings().addProcessor(BlockIgnoreProcessor.STRUCTURE_BLOCK);
		return twilightJigsawPiece;
	}
	@Nullable
	public static TwilightJigsawPiece initializeTemplateFromPool(Identifier templatePool, BlockPos.MutableBlockPos parentJunctionPos, FrontAndTop parentOrientation, String selectName, RandomSource rand, int genDepth, StructureTemplateManager structureManager) {
		Identifier templateId = StructureTemplateDefinitions.INSTANCE.rollTemplatePool(rand, templatePool);
		JigsawPlaceContext placeContext = JigsawPlaceContext.pickPlaceableJunction(parentJunctionPos, BlockPos.ZERO, parentOrientation, structureManager, templateId, selectName, rand);

		if (templateId == null || placeContext == null)
			return null;

		return TwilightJigsawPiece.defaultForTemplate(genDepth, structureManager, templateId, placeContext);
	}

	public static TwilightJigsawPiece defaultForTemplate(int genDepth, StructureTemplateManager structureManager, Identifier templateLocation, JigsawPlaceContext jigsawContext) {
		TwilightJigsawPiece twilightJigsawPiece = new TwilightJigsawPiece(TFStructurePieceTypes.TFJigsawTemplate.value(), genDepth, structureManager, templateLocation, jigsawContext);
		twilightJigsawPiece.placeSettings().addProcessor(JigsawReplacementProcessor.INSTANCE);
		twilightJigsawPiece.placeSettings().addProcessor(BlockIgnoreProcessor.STRUCTURE_BLOCK);
		return twilightJigsawPiece;
	}

	public static TwilightJigsawPiece defaultForTemplate(int genDepth, StructureTemplateManager structureManager, Identifier templateLocation, JigsawPlaceContext jigsawContext, TemplatePoolInstance templatePoolInstance, StructureProcessorList randomizedProcessors) {
		TwilightJigsawPiece twilightJigsawPiece = new TwilightJigsawPiece(TFStructurePieceTypes.TFJigsawTemplate.value(), genDepth, structureManager, templateLocation, jigsawContext, templatePoolInstance, randomizedProcessors);
		twilightJigsawPiece.placeSettings().addProcessor(JigsawReplacementProcessor.INSTANCE);
		twilightJigsawPiece.placeSettings().addProcessor(BlockIgnoreProcessor.STRUCTURE_BLOCK);
		return twilightJigsawPiece;
	}

	public TwilightJigsawPiece(StructurePieceType structurePieceType, CompoundTag compoundTag, StructurePieceSerializationContext ctx, StructurePlaceSettings placeSettings) {
		super(structurePieceType, compoundTag, ctx, placeSettings);

		this.sourceJigsaw = readSourceFromNBT(compoundTag);
		this.spareJigsaws = readConnectionsFromNBT(compoundTag);
		this.terrainAdjustment = compoundTag.getString(NBT_TERRAIN_ADAPT).map(TerrainAdjustment::valueOf).orElse(TerrainAdjustment.NONE);
		DynamicOps<Tag> dynamicOps = RegistryOps.create(NbtOps.INSTANCE, ctx.registryAccess());
		this.processors = compoundTag.contains(NBT_TEMPLATE_PROCESSORS) ? StructureProcessorType.LIST_CODEC.parse(dynamicOps, compoundTag.get(NBT_TEMPLATE_PROCESSORS)).resultOrPartial(message -> LOGGER.error("Error deserializing " + NBT_TEMPLATE_PROCESSORS + ": {}", message)) : Optional.empty();
		this.markerHandlers = compoundTag.contains(NBT_MARKER_HANDLERS) ? TemplateMarkerHandlerList.HOLDER_CODEC.parse(dynamicOps, compoundTag.get(NBT_MARKER_HANDLERS)).resultOrPartial(message -> LOGGER.error("Error deserializing " + NBT_MARKER_HANDLERS + ": {}", message)) : Optional.empty();

		this.beardifierGroundDelta = compoundTag.getIntOr(NBT_GROUND_OFFSET, 0);

		Optional<Boolean> ignoreWaterlog = compoundTag.getBoolean(NBT_IGNORE_WATERLOG);
		this.placeSettings.setLiquidSettings(ignoreWaterlog.isPresent() && ignoreWaterlog.get() ? LiquidSettings.IGNORE_WATERLOGGING : LiquidSettings.APPLY_WATERLOGGING);

		this.processors.ifPresent(p -> p.value().list().forEach(this.placeSettings::addProcessor));
	}

	public TwilightJigsawPiece(StructurePieceType type, int genDepth, StructureTemplateManager structureManager, Identifier templateLocation, JigsawPlaceContext jigsawContext) {
		super(type, genDepth, structureManager, templateLocation, jigsawContext.placementSettings(), jigsawContext.templatePos());

		this.sourceJigsaw = jigsawContext.seedJigsaw();
		this.spareJigsaws = Collections.unmodifiableList(jigsawContext.spareJigsaws());
		this.terrainAdjustment = TerrainAdjustment.NONE;
		this.processors = Optional.empty();
		this.markerHandlers = Optional.empty();
		this.beardifierGroundDelta = 0;
	}

	public TwilightJigsawPiece(StructurePieceType type, int genDepth, StructureTemplateManager structureManager, Identifier templateLocation, JigsawPlaceContext jigsawContext, TemplatePoolInstance templatePoolInstance, StructureProcessorList randomizedProcessors) {
		super(type, genDepth, structureManager, templateLocation, jigsawContext.placementSettings(), jigsawContext.templatePos());

		this.sourceJigsaw = jigsawContext.seedJigsaw();
		this.spareJigsaws = Collections.unmodifiableList(jigsawContext.spareJigsaws());
		this.terrainAdjustment = templatePoolInstance.terrainAdjustment();
		this.processors = templatePoolInstance.processors();
		this.markerHandlers = templatePoolInstance.markerHandlers();
		this.beardifierGroundDelta = templatePoolInstance.beardifierGroundDelta().map(TemplatePoolInstance.HeightAdjustment::beardifierGroundDelta).orElse(0);
		if (templatePoolInstance.ignoreWorldWaterlog()) this.placeSettings.setLiquidSettings(LiquidSettings.IGNORE_WATERLOGGING);
		this.processors.ifPresent(p -> p.value().list().forEach(this.placeSettings::addProcessor));
		randomizedProcessors.list().forEach(this.placeSettings::addProcessor);
	}

	protected static JigsawRecord readSourceFromNBT(CompoundTag structureTag) {
		return JigsawRecord.fromTag(structureTag.getCompound(NBT_JIGSAW_SOURCE).orElseThrow());
	}

	protected static List<JigsawRecord> readConnectionsFromNBT(CompoundTag structureTag) {
		ListTag connections = structureTag.getList(NBT_JIGSAW_CONNECTIONS).orElseThrow();

		if (connections.isEmpty())
			return Collections.emptyList();

		List<JigsawRecord> connectionsList = new ArrayList<>();

		for (Tag tagEntry : connections) {
			if (tagEntry instanceof CompoundTag tag) {
				connectionsList.add(JigsawRecord.fromTag(tag));
			}
		}

		return Collections.unmodifiableList(connectionsList);
	}

	@SuppressWarnings("OptionalIsPresent")
	@Override
	protected void addAdditionalSaveData(StructurePieceSerializationContext ctx, CompoundTag structureTag) {
		super.addAdditionalSaveData(ctx, structureTag);

		structureTag.put(NBT_JIGSAW_SOURCE, this.sourceJigsaw.toTag());

		ListTag tags = new ListTag();
		for (JigsawRecord record : this.spareJigsaws) {
			tags.add(record.toTag());
		}
		structureTag.put(NBT_JIGSAW_CONNECTIONS, tags);

		if (this.terrainAdjustment != TerrainAdjustment.NONE) {
			structureTag.putString(NBT_TERRAIN_ADAPT, this.terrainAdjustment.toString());
		}

		RegistryOps<Tag> registryOps = RegistryOps.create(NbtOps.INSTANCE, ctx.registryAccess());
		if (this.processors.isPresent()) {
			Optional<Tag> processorsList = StructureProcessorType.LIST_CODEC.encodeStart(registryOps, this.processors.get()).resultOrPartial(LOGGER::error);
			if (processorsList.isPresent()) {
				structureTag.put(NBT_TEMPLATE_PROCESSORS, processorsList.get());
			}
		}

		if (this.markerHandlers.isPresent()) {
			Optional<Tag> markerHandlersList = TemplateMarkerHandlerList.HOLDER_CODEC.encodeStart(registryOps, this.markerHandlers.get()).resultOrPartial(LOGGER::error);
			if (markerHandlersList.isPresent()) {
				structureTag.put(NBT_MARKER_HANDLERS, markerHandlersList.get());
			}
		}

		structureTag.putInt(NBT_GROUND_OFFSET, this.beardifierGroundDelta);

		if (!this.placeSettings.shouldApplyWaterlogging()) {
			structureTag.putBoolean(NBT_IGNORE_WATERLOG, true);
		}
	}

	@Deprecated(forRemoval = true) // Instead use addChildren(StructurePiece, StructurePieceAccessor, Structure.GenerationContext)
	@Override
	public void addChildren(StructurePiece parent, StructurePieceAccessor pieceAccessor, RandomSource random) {}

	public void addJigsaws(TwilightJigsawPiece parent, StructurePieceAccessor pieceAccessor, Structure.GenerationContext context) {
		WorldgenRandom random = context.random();
		random.setSeed(random.nextLong() ^ (context.seed() * this.templatePosition.asLong()));

		this.addChildren(parent, pieceAccessor, random);
		List<JigsawRecord> jigsaws = this.spareJigsaws;
		for (int i = 0; i < jigsaws.size(); i++) {
			this.processJigsaw(parent, pieceAccessor, context, jigsaws.get(i), i);
		}
	}

	protected void processJigsaw(TwilightJigsawPiece parent, StructurePieceAccessor pieceAccessor, Structure.GenerationContext context, JigsawRecord connection, int jigsawIndex) {
		Identifier templatePool = Identifier.parse(connection.pool());
		BlockPos parentJunctionPos = this.templatePosition.offset(connection.pos());
		boolean parentProjectsTerrain = this.terrainAdjustment != TerrainAdjustment.NONE;
		TwilightJigsawPiece jigsawPiece = StructureTemplateDefinitions.INSTANCE.initializeTemplateFromPool(templatePool, parentJunctionPos.mutable(), connection.orientation(), connection.target(), context, this.genDepth + 1, parentProjectsTerrain);

		if (jigsawPiece == null)
			return;

		if (pieceAccessor.findCollisionPiece(jigsawPiece.boundingBox) != null)
			return;

		pieceAccessor.addPiece(jigsawPiece);
		jigsawPiece.addJigsaws(this, pieceAccessor, context);
	}

	@Override
	public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator chunkGen, RandomSource random, BoundingBox chunkBounds, ChunkPos chunkPos, BlockPos structureCenterPos) {
		super.postProcess(level, structureManager, chunkGen, random, chunkBounds, chunkPos, structureCenterPos);

		ChunkAccess chunkAt = level.getChunk(chunkPos.getWorldPosition());
		for (StructureTemplate.StructureBlockInfo blockInfo : this.template.filterBlocks(this.templatePosition, this.placeSettings, Blocks.JIGSAW)) {
			BlockPos infoPos = blockInfo.pos();
			if (chunkBounds.isInside(infoPos)) {
				chunkAt.markPosForPostprocessing(infoPos);
			}
		}
	}

	@Override
	protected void handleDataMarker(String label, BlockPos pos, WorldGenLevel level, RandomSource random, BoundingBox chunkBounds, ChunkGenerator chunkGen, Rotation rotation) {
		super.handleDataMarker(label, pos, level, random, chunkBounds, chunkGen, rotation);

		if (this.markerHandlers.isEmpty()) {
			return;
		}

		for (TemplateMarkerHandler handler : this.markerHandlers.get().value().markerHandlers()) {
			if (handler.handleDataMarker(label, pos, level, random, chunkBounds, chunkGen, rotation)) {
				break;
			}
		}
	}

	public JigsawRecord getSourceJigsaw() {
		return this.sourceJigsaw;
	}

	public BlockPos getSourcePosition() {
		return this.templatePosition.offset(this.sourceJigsaw.pos());
	}

	public List<JigsawRecord> getSpareJigsaws() {
		return this.spareJigsaws;
	}

	public List<JigsawRecord> matchSpareJigsaws(Predicate<JigsawRecord> filter) {
		List<JigsawRecord> jigsaws = new ArrayList<>();

		for (JigsawRecord record : this.spareJigsaws)
			if (filter.test(record))
				jigsaws.add(record);

		return jigsaws;
	}

	public int firstMatchIndex(Predicate<JigsawRecord> filter) {
		for (int i = 0; i < this.spareJigsaws.size(); i++)
			if (filter.test(this.spareJigsaws.get(i)))
				return i;

		return -1;
	}

	@Override
	public BoundingBox getBeardifierBox() {
		return this.boundingBox;
	}

	@Override
	public TerrainAdjustment getTerrainAdjustment() {
		return this.terrainAdjustment;
	}

	@Override
	public int getGroundLevelDelta() {
		return this.beardifierGroundDelta;
	}
}
