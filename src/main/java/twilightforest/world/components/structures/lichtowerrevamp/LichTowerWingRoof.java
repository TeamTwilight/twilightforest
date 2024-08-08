package twilightforest.world.components.structures.lichtowerrevamp;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
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
import twilightforest.world.components.processors.SoftReplaceProcessor;
import twilightforest.world.components.structures.TwilightJigsawPiece;

public class LichTowerWingRoof extends TwilightJigsawPiece implements PieceBeardifierModifier {
	public LichTowerWingRoof(StructurePieceSerializationContext ctx, CompoundTag compoundTag) {
		super(TFStructurePieceTypes.LICH_WING_ROOF.get(), compoundTag, ctx, readSettings(compoundTag));

		LichTowerUtil.addDefaultProcessors(this.placeSettings.addProcessor(SoftReplaceProcessor.INSTANCE));
	}

	public LichTowerWingRoof(int genDepth, StructureTemplateManager structureManager, ResourceLocation templateLocation, JigsawPlaceContext jigsawContext) {
		super(TFStructurePieceTypes.LICH_WING_ROOF.get(), genDepth, structureManager, templateLocation, jigsawContext);

		LichTowerUtil.addDefaultProcessors(this.placeSettings.addProcessor(SoftReplaceProcessor.INSTANCE));
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
	protected void processJigsaw(StructurePiece parent, StructurePieceAccessor pieceAccessor, RandomSource random, JigsawRecord connection, int jigsawIndex) {
	}

	public BoundingBox generationCollisionBox() {
		if (this.boundingBox.getXSpan() < 2 || this.boundingBox.getYSpan() < 2 || this.boundingBox.getZSpan() < 2) {
			return BoundingBoxUtils.safeRetract(this.boundingBox, Direction.DOWN, 1);
		}

		return BoundingBoxUtils.cloneWithAdjustments(this.boundingBox, 1, 1, 1, -1, 0, -1);
	}
}
