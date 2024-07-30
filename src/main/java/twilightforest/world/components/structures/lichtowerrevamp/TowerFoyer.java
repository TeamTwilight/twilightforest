package twilightforest.world.components.structures.lichtowerrevamp;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.neoforged.neoforge.common.world.PieceBeardifierModifier;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFStructurePieceTypes;
import twilightforest.util.jigsaw.JigsawPlaceContext;
import twilightforest.util.jigsaw.JigsawRecord;
import twilightforest.world.components.structures.TwilightJigsawPiece;

public final class TowerFoyer extends TwilightJigsawPiece implements PieceBeardifierModifier {
	public TowerFoyer(StructurePieceSerializationContext ctx, CompoundTag compoundTag) {
		super(TFStructurePieceTypes.TOWER_FOYER.get(), compoundTag, ctx, readSettings(compoundTag));

		TowerUtil.addDefaultProcessors(this.placeSettings);
	}

	public TowerFoyer(StructureTemplateManager structureManager, BlockPos startPosition, Rotation rotation) {
		super(TFStructurePieceTypes.TOWER_FOYER.get(), 0, structureManager, TwilightForestMod.prefix("lich_tower/tower_foyer"), makeSettings(rotation), startPosition.below());

		TowerUtil.addDefaultProcessors(this.placeSettings);
	}

	@Override
	protected void processJigsaw(StructurePiece parent, StructurePieceAccessor pieceAccessor, RandomSource random, JigsawRecord connection, int jigsawIndex) {
		if ("twilightforest:lich_tower/tower_base".equals(connection.target())) {
			JigsawPlaceContext placeableJunction = JigsawPlaceContext.pickPlaceableJunction(this.templatePosition(), connection.pos(), connection.orientation(), this.structureManager, TwilightForestMod.prefix("lich_tower/tower_base"), "twilightforest:lich_tower/tower_base", random);

			if (placeableJunction == null) return;

			StructurePiece towerBase = new CentralTowerBase(this.structureManager, placeableJunction);
			pieceAccessor.addPiece(towerBase);
			towerBase.addChildren(this, pieceAccessor, random);
		} else if ("twilightforest:shelf".equals(connection.target()) && (jigsawIndex % 4) == random.nextInt(5)) {
			JigsawPlaceContext placeableJunction = JigsawPlaceContext.pickPlaceableJunction(this.templatePosition(), connection.pos(), connection.orientation(), this.structureManager, TwilightForestMod.prefix("lich_tower/foyer_decor"), "twilightforest:shelf", random);

			if (placeableJunction == null) return;

			StructurePiece towerBase = new FoyerDecoration(this.genDepth + 1, this.structureManager, placeableJunction);
			pieceAccessor.addPiece(towerBase);
			towerBase.addChildren(this, pieceAccessor, random);
		}
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
}
