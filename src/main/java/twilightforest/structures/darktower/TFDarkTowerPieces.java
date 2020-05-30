package twilightforest.structures.darktower;

import net.minecraft.world.gen.feature.structure.IStructurePieceType;
import twilightforest.TFFeature;

public class TFDarkTowerPieces {

//	public static final IStructurePieceType TFDT = TFFeature.registerPiece("TFDT", StructureStartDarkTower::new);

	public static final IStructurePieceType TFDTBal = TFFeature.registerPiece("TFDTBal", ComponentTFDarkTowerBalcony::new);
	public static final IStructurePieceType TFDTBea = TFFeature.registerPiece("TFDTBea", ComponentTFDarkTowerBeard::new);
	public static final IStructurePieceType TFDTBB = TFFeature.registerPiece("TFDTBB", ComponentTFDarkTowerBossBridge::new);
	public static final IStructurePieceType TFDTBT = TFFeature.registerPiece("TFDTBT", ComponentTFDarkTowerBossTrap::new);
	public static final IStructurePieceType TFDTBri = TFFeature.registerPiece("TFDTBri", ComponentTFDarkTowerBridge::new);
	public static final IStructurePieceType TFDTEnt = TFFeature.registerPiece("TFDTEnt", ComponentTFDarkTowerEntrance::new);
	public static final IStructurePieceType TFDTEB = TFFeature.registerPiece("TFDTEB", ComponentTFDarkTowerEntranceBridge::new);
	public static final IStructurePieceType TFDTMai = TFFeature.registerPiece("TFDTMai", ComponentTFDarkTowerMain::new);
	public static final IStructurePieceType TFDTMB = TFFeature.registerPiece("TFDTMB", ComponentTFDarkTowerMainBridge::new);
	public static final IStructurePieceType TFDTRooS = TFFeature.registerPiece("TFDTRooS", ComponentTFDarkTowerRoof::new);
	public static final IStructurePieceType TFDTRA = TFFeature.registerPiece("TFDTRA", ComponentTFDarkTowerRoofAntenna::new);
	public static final IStructurePieceType TFDTRC = TFFeature.registerPiece("TFDTRC", ComponentTFDarkTowerRoofCactus::new);
	public static final IStructurePieceType TFDTRFP = TFFeature.registerPiece("TFDTRFP", ComponentTFDarkTowerRoofFourPost::new);
	public static final IStructurePieceType TFDTRR = TFFeature.registerPiece("TFDTRR", ComponentTFDarkTowerRoofRings::new);
	public static final IStructurePieceType TFDTWin = TFFeature.registerPiece("TFDTWin", ComponentTFDarkTowerWing::new);
}