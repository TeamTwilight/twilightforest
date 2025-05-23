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
import twilightforest.world.components.structures.util.SortablePiece;

public class LichTowerWingBeard extends TwilightJigsawPiece implements PieceBeardifierModifier, SortablePiece {
	private final boolean generateGround;

	public LichTowerWingBeard(StructurePieceSerializationContext ctx, CompoundTag compoundTag) {
		super(TFStructurePieceTypes.LICH_WING_BEARD.get(), compoundTag, ctx, readSettings(compoundTag));

		LichTowerUtil.addDefaultProcessors(this.placeSettings.addProcessor(SoftReplaceProcessor.INSTANCE));

		this.generateGround = compoundTag.getBoolean("gen_ground");
	}

	public LichTowerWingBeard(int genDepth, StructureTemplateManager structureManager, ResourceLocation templateLocation, JigsawPlaceContext jigsawContext, boolean generateGround) {
		super(TFStructurePieceTypes.LICH_WING_BEARD.get(), genDepth, structureManager, templateLocation, jigsawContext);

		LichTowerUtil.addDefaultProcessors(this.placeSettings.addProcessor(SoftReplaceProcessor.INSTANCE));

		this.generateGround = generateGround;
	}

	@Override
	protected void addAdditionalSaveData(StructurePieceSerializationContext ctx, CompoundTag structureTag) {
		super.addAdditionalSaveData(ctx, structureTag);

		structureTag.putBoolean("gen_ground", this.generateGround);
	}

	@Override
	public BoundingBox getBeardifierBox() {
		return this.boundingBox;
	}

	@Override
	public TerrainAdjustment getTerrainAdjustment() {
		return this.generateGround ? TerrainAdjustment.BEARD_BOX : TerrainAdjustment.NONE;
	}

	@Override
	public int getGroundLevelDelta() {
		return 4;
	}

	@Override
	protected void processJigsaw(StructurePiece parent, StructurePieceAccessor pieceAccessor, RandomSource random, JigsawRecord connection, int jigsawIndex) {
	}

	public BoundingBox generationCollisionBox() {
		if (this.boundingBox.getXSpan() < 2 || this.boundingBox.getYSpan() < 2 || this.boundingBox.getZSpan() < 2) {
			return BoundingBoxUtils.safeRetract(this.boundingBox, Direction.UP, 1);
		}

		return BoundingBoxUtils.cloneWithAdjustments(this.boundingBox, 1, 0, 1, -1, -1, -1);
	}

	@Override
	public int getSortKey() {
		return -1;
	}

	// Is this beard a trim piece, on the ground? Used for generating fence
	public boolean isTrim() {
		return this.generateGround;
	}
}
