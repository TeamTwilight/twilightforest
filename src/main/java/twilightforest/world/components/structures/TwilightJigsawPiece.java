package twilightforest.world.components.structures;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
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
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import org.jetbrains.annotations.Nullable;
import twilightforest.util.jigsaw.JigsawPlaceContext;
import twilightforest.util.jigsaw.JigsawRecord;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

public abstract class TwilightJigsawPiece extends TwilightTemplateStructurePiece {
	private final JigsawRecord sourceJigsaw;
	private final List<JigsawRecord> spareJigsaws;

	public TwilightJigsawPiece(StructurePieceType structurePieceType, CompoundTag compoundTag, StructurePieceSerializationContext ctx, StructurePlaceSettings placeSettings) {
		super(structurePieceType, compoundTag, ctx, placeSettings);

		this.sourceJigsaw = readSourceFromNBT(compoundTag);
		this.spareJigsaws = readConnectionsFromNBT(compoundTag);
	}

	public TwilightJigsawPiece(StructurePieceType type, int genDepth, StructureTemplateManager structureManager, ResourceLocation templateLocation, JigsawPlaceContext jigsawContext) {
		super(type, genDepth, structureManager, templateLocation, jigsawContext.placementSettings(), jigsawContext.templatePos());

		this.sourceJigsaw = jigsawContext.seedJigsaw();
		this.spareJigsaws = Collections.unmodifiableList(jigsawContext.spareJigsaws());
	}

	protected static JigsawRecord readSourceFromNBT(CompoundTag structureTag) {
		return JigsawRecord.fromTag(structureTag.getCompound("source"));
	}

	protected static List<JigsawRecord> readConnectionsFromNBT(CompoundTag structureTag) {
		ListTag connections = structureTag.getList("connections", Tag.TAG_COMPOUND);

		List<JigsawRecord> connectionsList = new ArrayList<>();

		for (Tag tagEntry : connections) {
			if (tagEntry instanceof CompoundTag tag) {
				connectionsList.add(JigsawRecord.fromTag(tag));
			}
		}

		return Collections.unmodifiableList(connectionsList);
	}

	@Override
	protected void addAdditionalSaveData(StructurePieceSerializationContext ctx, CompoundTag structureTag) {
		super.addAdditionalSaveData(ctx, structureTag);

		structureTag.put("source", this.sourceJigsaw.toTag());

		ListTag tags = new ListTag();

		for (JigsawRecord record : this.spareJigsaws) {
			tags.add(record.toTag());
		}

		structureTag.put("connections", tags);
	}

	@Override
	public void addChildren(StructurePiece parent, StructurePieceAccessor pieceAccessor, RandomSource random) {
		super.addChildren(parent, pieceAccessor, random);

		List<JigsawRecord> jigsaws = this.spareJigsaws;
		for (int i = 0; i < jigsaws.size(); i++) {
			this.processJigsaw(parent, pieceAccessor, random, jigsaws.get(i), i);
		}
	}

	protected abstract void processJigsaw(StructurePiece parent, StructurePieceAccessor pieceAccessor, RandomSource random, JigsawRecord connection, int jigsawIndex);

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
}
