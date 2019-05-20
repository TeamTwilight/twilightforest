package twilightforest.structures.mushroomtower;

import net.minecraft.world.gen.structure.MapGenStructureIO;
import twilightforest.structures.start.StructureStartMushroomTower;

public class TFMushroomTowerPieces {

	public static void registerPieces() {
		MapGenStructureIO.registerStructure(StructureStartMushroomTower.class, "TFMT");

		MapGenStructureIO.registerStructureComponent(ComponentTFMushroomTowerMain.class, "TFMTMai");
		MapGenStructureIO.registerStructureComponent(ComponentTFMushroomTowerWing.class, "TFMTWin");
		MapGenStructureIO.registerStructureComponent(ComponentTFMushroomTowerBridge.class, "TFMTBri");
		MapGenStructureIO.registerStructureComponent(ComponentTFMushroomTowerMainBridge.class, "TFMTMB");
		MapGenStructureIO.registerStructureComponent(ComponentTFTowerRoofMushroom.class, "TFMTRoofMush");
	}

}
