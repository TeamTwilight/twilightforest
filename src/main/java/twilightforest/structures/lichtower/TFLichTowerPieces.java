package twilightforest.structures.lichtower;

import net.minecraft.world.gen.structure.MapGenStructureIO;

public class TFLichTowerPieces {

	public static void registerPieces() {
		MapGenStructureIO.registerStructureComponent(ComponentTFTowerBeard.class, "TFLTBea");
		MapGenStructureIO.registerStructureComponent(ComponentTFTowerBeardAttached.class, "TFLTBA");
		MapGenStructureIO.registerStructureComponent(ComponentTFTowerBridge.class, "TFLTBri");
		MapGenStructureIO.registerStructureComponent(ComponentTFTowerMain.class, "TFLTMai");
		MapGenStructureIO.registerStructureComponent(ComponentTFTowerOutbuilding.class, "TFLTOut");
		MapGenStructureIO.registerStructureComponent(ComponentTFTowerRoof.class, "TFLTRoo");
		MapGenStructureIO.registerStructureComponent(ComponentTFTowerRoofAttachedSlab.class, "TFLTRAS");
		MapGenStructureIO.registerStructureComponent(ComponentTFTowerRoofFence.class, "TFLTRF");
		MapGenStructureIO.registerStructureComponent(ComponentTFTowerRoofGableForwards.class, "TFLTRGF");
		MapGenStructureIO.registerStructureComponent(ComponentTFTowerRoofPointy.class, "TFLTRP");
		MapGenStructureIO.registerStructureComponent(ComponentTFTowerRoofPointyOverhang.class, "TFLTRPO");
		MapGenStructureIO.registerStructureComponent(ComponentTFTowerRoofSlab.class, "TFLTRS");
		MapGenStructureIO.registerStructureComponent(ComponentTFTowerRoofSlabForwards.class, "TFLTRSF");
		MapGenStructureIO.registerStructureComponent(ComponentTFTowerRoofStairs.class, "TFLTRSt");
		MapGenStructureIO.registerStructureComponent(ComponentTFTowerRoofStairsOverhang.class, "TFLTRStO");
		MapGenStructureIO.registerStructureComponent(ComponentTFTowerWing.class, "TFLTWin");

	}
}
