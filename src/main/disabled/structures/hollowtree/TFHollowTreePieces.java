package twilightforest.structures.hollowtree;

import net.minecraft.world.gen.feature.structure.IStructurePieceType;
import twilightforest.TFFeature;

public class TFHollowTreePieces {

//	public static final IStructurePieceType TFHTLSt = TFFeature.registerPiece("TFHTLSt", StructureTFHollowTreeStart::new);

	public static final IStructurePieceType TFHTLB = TFFeature.registerPiece("TFHTLB", ComponentTFHollowTreeLargeBranch::new);
	public static final IStructurePieceType TFHTMB = TFFeature.registerPiece("TFHTMB", ComponentTFHollowTreeMedBranch::new);
	public static final IStructurePieceType TFHTSB = TFFeature.registerPiece("TFHTSB", ComponentTFHollowTreeSmallBranch::new);
	public static final IStructurePieceType TFHTTr = TFFeature.registerPiece("TFHTTr", ComponentTFHollowTreeTrunk::new);
	public static final IStructurePieceType TFHTRo = TFFeature.registerPiece("TFHTRo", ComponentTFHollowTreeRoot::new);
	public static final IStructurePieceType TFHTLD = TFFeature.registerPiece("TFHTLD", ComponentTFHollowTreeLeafDungeon::new);
}
