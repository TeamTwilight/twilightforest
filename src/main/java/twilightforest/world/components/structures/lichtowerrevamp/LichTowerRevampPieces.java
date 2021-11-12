package twilightforest.world.components.structures.lichtowerrevamp;

import net.minecraft.world.level.levelgen.feature.StructurePieceType;
import twilightforest.world.registration.TFFeature;

public final class LichTowerRevampPieces {
    public static final StructurePieceType TOWER_FOYER = TFFeature.registerPiece("TFLT" + "TFoy", TowerFoyer::new);
    public static final StructurePieceType CENTRAL_TOWER = TFFeature.registerPiece("TFLT" + "CTSeg", CentralTowerSegment::new);
    public static final StructurePieceType CENTRAL_TO_SIDE_TOWER = TFFeature.registerPiece("TFLT" + "C2ST", CentralTowerAttachment::new);
    public static final StructurePieceType SIDE_TOWER_ROOM = TFFeature.registerPiece("TFLT" + "STRm", SideTowerRoom::new);
}
