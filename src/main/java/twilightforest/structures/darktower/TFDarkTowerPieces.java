package twilightforest.structures.darktower;

import net.minecraft.world.gen.structure.MapGenStructureIO;
import twilightforest.structures.start.StructureStartDarkTower;


public class TFDarkTowerPieces {

	public static void registerPieces() {
		MapGenStructureIO.registerStructure(StructureStartDarkTower.class, "TFDT");

		MapGenStructureIO.registerStructureComponent(ComponentTFDarkTowerBalcony.class, "TFDTBal");
		MapGenStructureIO.registerStructureComponent(ComponentTFDarkTowerBeard.class, "TFDTBea");
		MapGenStructureIO.registerStructureComponent(ComponentTFDarkTowerBossBridge.class, "TFDTBB");
		MapGenStructureIO.registerStructureComponent(ComponentTFDarkTowerBossTrap.class, "TFDTBT");
		MapGenStructureIO.registerStructureComponent(ComponentTFDarkTowerBridge.class, "TFDTBri");
		MapGenStructureIO.registerStructureComponent(ComponentTFDarkTowerEntrance.class, "TFDTEnt");
		MapGenStructureIO.registerStructureComponent(ComponentTFDarkTowerEntranceBridge.class, "TFDTEB");
		MapGenStructureIO.registerStructureComponent(ComponentTFDarkTowerMain.class, "TFDTMai");
		MapGenStructureIO.registerStructureComponent(ComponentTFDarkTowerMainBridge.class, "TFDTMB");
		MapGenStructureIO.registerStructureComponent(ComponentTFDarkTowerRoof.class, "TFDTRooS");
		MapGenStructureIO.registerStructureComponent(ComponentTFDarkTowerRoofAntenna.class, "TFDTRA");
		MapGenStructureIO.registerStructureComponent(ComponentTFDarkTowerRoofCactus.class, "TFDTRC");
		MapGenStructureIO.registerStructureComponent(ComponentTFDarkTowerRoofFourPost.class, "TFDTRFP");
		MapGenStructureIO.registerStructureComponent(ComponentTFDarkTowerRoofRings.class, "TFDTRR");
		MapGenStructureIO.registerStructureComponent(ComponentTFDarkTowerWing.class, "TFDTWin");

	}

}