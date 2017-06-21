package twilightforest.structures.icetower;

import net.minecraft.world.gen.structure.MapGenStructureIO;

public class TFIceTowerPieces {

	public static void registerPieces() {
		MapGenStructureIO.registerStructureComponent(ComponentTFIceTowerMain.class, "TFITMai");
		MapGenStructureIO.registerStructureComponent(ComponentTFIceTowerWing.class, "TFITWin");
		MapGenStructureIO.registerStructureComponent(ComponentTFIceTowerRoof.class, "TFITRoof");
		MapGenStructureIO.registerStructureComponent(ComponentTFIceTowerBeard.class, "TFITBea");
		MapGenStructureIO.registerStructureComponent(ComponentTFIceTowerBossWing.class, "TFITBoss");
		MapGenStructureIO.registerStructureComponent(ComponentTFIceTowerEntrance.class, "TFITEnt");
		MapGenStructureIO.registerStructureComponent(ComponentTFIceTowerBridge.class, "TFITBri");
		MapGenStructureIO.registerStructureComponent(ComponentTFIceTowerStairs.class, "TFITSt");
	}
}
