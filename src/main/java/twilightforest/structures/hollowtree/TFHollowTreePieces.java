package twilightforest.structures.hollowtree;

import net.minecraft.world.gen.structure.MapGenStructureIO;

public class TFHollowTreePieces {

	public static void registerPieces() {
		MapGenStructureIO.registerStructure(StructureTFHollowTreeStart.class, "TFHTLSt");

		MapGenStructureIO.registerStructureComponent(ComponentTFHollowTreeLargeBranch.class, "TFHTLB");
		MapGenStructureIO.registerStructureComponent(ComponentTFHollowTreeMedBranch.class, "TFHTMB");
		MapGenStructureIO.registerStructureComponent(ComponentTFHollowTreeSmallBranch.class, "TFHTSB");
		MapGenStructureIO.registerStructureComponent(ComponentTFHollowTreeTrunk.class, "TFHTTr");
		MapGenStructureIO.registerStructureComponent(ComponentTFHollowTreeRoot.class, "TFHTRo");
		MapGenStructureIO.registerStructureComponent(ComponentTFHollowTreeLeafDungeon.class, "TFHTLD");

	}
}
