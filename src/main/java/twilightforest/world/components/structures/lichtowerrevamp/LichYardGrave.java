package twilightforest.world.components.structures.lichtowerrevamp;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.neoforged.neoforge.common.world.PieceBeardifierModifier;
import twilightforest.init.TFStructurePieceTypes;
import twilightforest.util.BoundingBoxUtils;
import twilightforest.util.jigsaw.JigsawPlaceContext;
import twilightforest.util.jigsaw.JigsawRecord;
import twilightforest.world.components.processors.MetaBlockProcessor;
import twilightforest.world.components.structures.TwilightJigsawPiece;

public class LichYardGrave extends TwilightJigsawPiece implements PieceBeardifierModifier {
	private final BoundingBox fillUnder;

	public LichYardGrave(StructurePieceSerializationContext ctx, CompoundTag compoundTag) {
		super(TFStructurePieceTypes.LICH_YARD_GRAVE.value(), compoundTag, ctx, readSettings(compoundTag));

		this.placeSettings().addProcessor(MetaBlockProcessor.INSTANCE);

		this.fillUnder = this.makeFillerBox();
	}

	public LichYardGrave(StructureTemplateManager structureManager, JigsawPlaceContext jigsawContext, ResourceLocation templateId) {
		super(TFStructurePieceTypes.LICH_YARD_GRAVE.value(), 0, structureManager, templateId, jigsawContext);

		this.placeSettings().addProcessor(MetaBlockProcessor.INSTANCE);

		this.fillUnder = this.makeFillerBox();
	}

	private BoundingBox makeFillerBox() {
		Direction front = this.getSourceJigsaw().orientation().front();

		BoundingBox adjusted = this.boundingBox.inflatedBy(-1);

		return BoundingBoxUtils.safeRetract(adjusted, front, -1);
	}

	@Override
	public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator chunkGen, RandomSource random, BoundingBox chunkBounds, ChunkPos chunkPos, BlockPos structureCenterPos) {
		BoundingBox applicable = BoundingBoxUtils.getIntersectionOfSBBs(this.fillUnder, chunkBounds);
		if (applicable != null) {
			int yUnder = applicable.minY() - 1;
			BlockPos min = new BlockPos(applicable.minX(), yUnder, applicable.minZ());
			BlockPos max = new BlockPos(applicable.maxX(), yUnder, applicable.maxZ());

			for (BlockPos posUnder : BlockPos.betweenClosed(min, max)) {
				int bottom = level.getHeight(Heightmap.Types.OCEAN_FLOOR_WG, posUnder.getX(), posUnder.getZ());
				for (int y = yUnder ; y >= bottom ; y--) {
					level.setBlock(posUnder.atY(y), Blocks.DIRT.defaultBlockState(), Block.UPDATE_ALL);
				}
			}
		}

		super.postProcess(level, structureManager, chunkGen, random, chunkBounds, chunkPos, structureCenterPos);
	}

	@Override
	public BoundingBox getBeardifierBox() {
		return this.boundingBox;
	}

	@Override
	public TerrainAdjustment getTerrainAdjustment() {
		return TerrainAdjustment.BEARD_BOX;
	}

	@Override
	public int getGroundLevelDelta() {
		return 1;
	}

	@Override
	protected void processJigsaw(StructurePiece parent, StructurePieceAccessor pieceAccessor, RandomSource random, JigsawRecord connection, int jigsawIndex) {
	}
}
