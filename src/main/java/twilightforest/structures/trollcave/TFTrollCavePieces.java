package twilightforest.structures.trollcave;

import net.minecraft.world.gen.structure.MapGenStructureIO;

public class TFTrollCavePieces {

	public static void registerPieces() {
		MapGenStructureIO.registerStructureComponent(ComponentTFTrollCaveMain.class, "TFTCMai");
		MapGenStructureIO.registerStructureComponent(ComponentTFTrollCaveConnect.class, "TFTCCon");
		MapGenStructureIO.registerStructureComponent(ComponentTFTrollCaveGarden.class, "TFTCGard");
		MapGenStructureIO.registerStructureComponent(ComponentTFTrollCloud.class, "TFTCloud");
		MapGenStructureIO.registerStructureComponent(ComponentTFCloudCastle.class, "TFClCa");
		MapGenStructureIO.registerStructureComponent(ComponentTFCloudTree.class, "TFClTr");
		MapGenStructureIO.registerStructureComponent(ComponentTFTrollVault.class, "TFTCVa");
	}
}