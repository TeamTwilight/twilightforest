package twilightforest.structures.mushroomtower;

import net.minecraft.world.gen.structure.MapGenStructureIO;

public class TFMushroomTowerPieces {

	public static void registerPieces() {
		MapGenStructureIO.registerStructureComponent(ComponentTFMushroomTowerMain.class, "TFMTMai");
		MapGenStructureIO.registerStructureComponent(ComponentTFMushroomTowerWing.class, "TFMTWin");
		MapGenStructureIO.registerStructureComponent(ComponentTFMushroomTowerBridge.class, "TFMTBri");
		MapGenStructureIO.registerStructureComponent(ComponentTFMushroomTowerMainBridge.class, "TFMTMB");
		MapGenStructureIO.registerStructureComponent(ComponentTFTowerRoofMushroom.class, "TFMTRoofMush");
	}

}
