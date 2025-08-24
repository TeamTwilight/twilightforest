package twilightforest.world.components.structures;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.*;
import net.neoforged.neoforge.common.util.ConcatenatedListView;
import net.neoforged.neoforge.common.world.PieceBeardifierModifier;
import twilightforest.TwilightForestMod;
import twilightforest.beans.Autowired;
import twilightforest.init.TFStructurePieceTypes;
import twilightforest.util.jigsaw.JigsawPlaceContext;
import twilightforest.util.jigsaw.JigsawRecord;
import twilightforest.world.components.processors.MetaBlockProcessor;
import twilightforest.world.components.structures.util.StructureTemplateDefinitions;
import twilightforest.world.components.structures.util.ProgressionPiece;
import twilightforest.world.components.structures.util.TemplatePoolInstance;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class TwilightJigsawPiece extends TwilightTemplateStructurePiece implements ProgressionPiece, PieceBeardifierModifier {
	@Autowired
	private static StructureTemplateDefinitions structureTemplateDefinitions;

	private static final String NBT_JIGSAW_SOURCE = "source";
	private static final String NBT_JIGSAW_CONNECTIONS = "connections";
	private static final String NBT_TERRAIN_ADAPT = "terrain_adaptation";
	private static final String NBT_TEMPLATE_PROCESSORS = "template_processors";
	private static final String NBT_PLACE_PROJECTION = "place_projection";

	private final JigsawRecord sourceJigsaw;
	private final List<JigsawRecord> spareJigsaws;
	private final TerrainAdjustment terrainAdjustment;
	private final Holder<StructureProcessorList> processors;
	private final StructureTemplatePool.Projection projection;

	public static TwilightJigsawPiece defaultDeserialize(StructurePieceSerializationContext ctx, CompoundTag compoundTag) {
		TwilightJigsawPiece twilightJigsawPiece = new TwilightJigsawPiece(TFStructurePieceTypes.TFJigsawTemplate.value(), compoundTag, ctx, readSettings(compoundTag));
		twilightJigsawPiece.placeSettings().addProcessor(MetaBlockProcessor.INSTANCE);
		return twilightJigsawPiece;
	}

	public static TwilightJigsawPiece defaultForTemplate(int genDepth, StructureTemplateManager structureManager, ResourceLocation templateLocation, JigsawPlaceContext jigsawContext, TemplatePoolInstance templatePoolInstance) {
		TwilightJigsawPiece twilightJigsawPiece = new TwilightJigsawPiece(TFStructurePieceTypes.TFJigsawTemplate.value(), genDepth, structureManager, templateLocation, jigsawContext, templatePoolInstance);
		StructurePlaceSettings structurePlaceSettings = twilightJigsawPiece.placeSettings();
		structurePlaceSettings.addProcessor(MetaBlockProcessor.INSTANCE);
		return twilightJigsawPiece;
	}

	@SuppressWarnings("OptionalIsPresent")
	public TwilightJigsawPiece(StructurePieceType structurePieceType, CompoundTag compoundTag, StructurePieceSerializationContext ctx, StructurePlaceSettings placeSettings) {
		super(structurePieceType, compoundTag, ctx, placeSettings);

		this.sourceJigsaw = readSourceFromNBT(compoundTag);
		this.spareJigsaws = readConnectionsFromNBT(compoundTag);
		this.terrainAdjustment = compoundTag.contains(NBT_TERRAIN_ADAPT) ? TerrainAdjustment.valueOf(compoundTag.getString(NBT_TERRAIN_ADAPT)) : TerrainAdjustment.NONE;
		Optional<Holder<StructureProcessorList>> parsedProcessors = compoundTag.contains(NBT_TEMPLATE_PROCESSORS) ? StructureProcessorType.LIST_CODEC.parse(NbtOps.INSTANCE, compoundTag.getCompound(NBT_TEMPLATE_PROCESSORS)).resultOrPartial(TwilightForestMod.LOGGER::error) : Optional.empty();
		if (parsedProcessors.isPresent()) {
			this.processors = parsedProcessors.get();
		} else {
			this.processors = Holder.direct(new StructureProcessorList(Collections.emptyList()));
		}
		this.projection = compoundTag.contains(NBT_PLACE_PROJECTION) ? StructureTemplatePool.Projection.valueOf(compoundTag.getString(NBT_PLACE_PROJECTION)) : StructureTemplatePool.Projection.RIGID;

		for (StructureProcessor processor : ConcatenatedListView.of(this.processors.value().list(), this.projection.getProcessors())) {
			this.placeSettings.addProcessor(processor);
		}
	}

	public TwilightJigsawPiece(StructurePieceType type, int genDepth, StructureTemplateManager structureManager, ResourceLocation templateLocation, JigsawPlaceContext jigsawContext) {
		super(type, genDepth, structureManager, templateLocation, jigsawContext.placementSettings(), jigsawContext.templatePos());

		this.sourceJigsaw = jigsawContext.seedJigsaw();
		this.spareJigsaws = Collections.unmodifiableList(jigsawContext.spareJigsaws());
		this.terrainAdjustment = TerrainAdjustment.NONE;
		this.processors = Holder.direct(new StructureProcessorList(Collections.emptyList()));
		this.projection = StructureTemplatePool.Projection.RIGID;
	}

	public TwilightJigsawPiece(StructurePieceType type, int genDepth, StructureTemplateManager structureManager, ResourceLocation templateLocation, JigsawPlaceContext jigsawContext, TemplatePoolInstance templatePoolInstance) {
		super(type, genDepth, structureManager, templateLocation, jigsawContext.placementSettings(), jigsawContext.templatePos());

		this.sourceJigsaw = jigsawContext.seedJigsaw();
		this.spareJigsaws = Collections.unmodifiableList(jigsawContext.spareJigsaws());
		this.terrainAdjustment = templatePoolInstance.terrainAdjustment();
		this.processors = templatePoolInstance.processors();
		this.projection = templatePoolInstance.projection();
		for (StructureProcessor processor : ConcatenatedListView.of(this.processors.value().list(), this.projection.getProcessors())) {
			this.placeSettings.addProcessor(processor);
		}
	}

	protected static JigsawRecord readSourceFromNBT(CompoundTag structureTag) {
		return JigsawRecord.fromTag(structureTag.getCompound(NBT_JIGSAW_SOURCE));
	}

	protected static List<JigsawRecord> readConnectionsFromNBT(CompoundTag structureTag) {
		ListTag connections = structureTag.getList(NBT_JIGSAW_CONNECTIONS, Tag.TAG_COMPOUND);

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

		StructureProcessorList value = this.processors.value();
		if (!value.list().isEmpty()) {
			Optional<Tag> processorsList = StructureProcessorType.LIST_CODEC.encodeStart(NbtOps.INSTANCE, this.processors).resultOrPartial(TwilightForestMod.LOGGER::error);
			if (processorsList.isPresent()) {
				structureTag.put(NBT_TEMPLATE_PROCESSORS, processorsList.get());
			}
		}
	}

	@Override
	public void addChildren(StructurePiece parent, StructurePieceAccessor pieceAccessor, RandomSource random) {
		super.addChildren(parent, pieceAccessor, random);

		List<JigsawRecord> jigsaws = this.spareJigsaws;
		for (int i = 0; i < jigsaws.size(); i++) {
			this.processJigsaw(parent, pieceAccessor, random, jigsaws.get(i), i);
		}
	}

	protected void processJigsaw(StructurePiece parent, StructurePieceAccessor pieceAccessor, RandomSource random, JigsawRecord connection, int jigsawIndex) {
		ResourceLocation templatePool = ResourceLocation.parse(connection.pool());
		BlockPos parentJunctionPos = this.templatePosition.offset(connection.pos());
		TwilightJigsawPiece jigsawPiece = structureTemplateDefinitions.initializeTemplateFromPool(templatePool, parentJunctionPos, connection.orientation(), connection.target(), random, this.genDepth + 1, this.structureManager);

		if (jigsawPiece == null)
			return;

		if (pieceAccessor.findCollisionPiece(jigsawPiece.boundingBox) != null)
			return;

		pieceAccessor.addPiece(jigsawPiece);
		jigsawPiece.addChildren(this, pieceAccessor, random);
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
		return 0;
	}
}
