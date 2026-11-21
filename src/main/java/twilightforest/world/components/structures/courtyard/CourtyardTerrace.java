package twilightforest.world.components.structures.courtyard;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.neoforged.neoforge.common.world.PieceBeardifierModifier;
import twilightforest.init.TFStructurePieceTypes;
import twilightforest.world.components.processors.NagastoneVariants;
import twilightforest.world.components.processors.StoneBricksVariants;
import twilightforest.world.components.structures.TwilightTemplateStructurePiece;

public class CourtyardTerrace extends TwilightTemplateStructurePiece implements PieceBeardifierModifier {
	public CourtyardTerrace(StructurePieceSerializationContext ctx, CompoundTag nbt) {
		super(TFStructurePieceTypes.TFNCTe.value(), nbt, ctx, readSettings(nbt).addProcessor(CourtyardTerraceTemplateProcessor.INSTANCE).addProcessor(NagastoneVariants.INSTANCE).addProcessor(StoneBricksVariants.INSTANCE));
	}

	public CourtyardTerrace(int i, int x, int y, int z, Rotation rotation, StructureTemplateManager structureManager, Identifier templateLocation) {
		super(TFStructurePieceTypes.TFNCTe.value(), i, structureManager, templateLocation, makeSettings(rotation).addProcessor(CourtyardTerraceTemplateProcessor.INSTANCE).addProcessor(NagastoneVariants.INSTANCE).addProcessor(StoneBricksVariants.INSTANCE), new BlockPos(x, y, z));
	}

	protected CourtyardTerrace(StructurePieceType structurePieceType, StructurePieceSerializationContext ctx, CompoundTag nbt) {
		super(structurePieceType, nbt, ctx, readSettings(nbt).addProcessor(CourtyardTerraceTemplateProcessor.INSTANCE).addProcessor(NagastoneVariants.INSTANCE).addProcessor(StoneBricksVariants.INSTANCE));
	}

	public CourtyardTerrace(StructurePieceType structurePieceType, int i, int x, int y, int z, Rotation rotation, StructureTemplateManager structureManager, Identifier templateLocation) {
		super(structurePieceType, i, structureManager, templateLocation, makeSettings(rotation).addProcessor(CourtyardTerraceTemplateProcessor.INSTANCE).addProcessor(NagastoneVariants.INSTANCE).addProcessor(StoneBricksVariants.INSTANCE), new BlockPos(x, y, z));
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
		return 3;
	}
}
