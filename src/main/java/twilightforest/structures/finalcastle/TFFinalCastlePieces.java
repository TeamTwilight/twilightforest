package twilightforest.structures.finalcastle;

import net.minecraft.world.gen.structure.MapGenStructureIO;

public class TFFinalCastlePieces {
	public static void registerFinalCastlePieces() {
		MapGenStructureIO.registerStructureComponent(ComponentTFFinalCastleMain.class, "TFFCMain");
		MapGenStructureIO.registerStructureComponent(ComponentTFFinalCastleStairTower.class, "TFFCStTo");
		MapGenStructureIO.registerStructureComponent(ComponentTFFinalCastleLargeTower.class, "TFFCLaTo");
		MapGenStructureIO.registerStructureComponent(ComponentTFFinalCastleMural.class, "TFFCMur");
		MapGenStructureIO.registerStructureComponent(ComponentTFFinalCastleFoundation48.class, "TFFCToF48");
		MapGenStructureIO.registerStructureComponent(ComponentTFFinalCastleRoof48Crenellated.class, "TFFCRo48Cr");
		MapGenStructureIO.registerStructureComponent(ComponentTFFinalCastleBossGazebo.class, "TFFCBoGaz");
		MapGenStructureIO.registerStructureComponent(ComponentTFFinalCastleMazeTower13.class, "TFFCSiTo");
		MapGenStructureIO.registerStructureComponent(ComponentTFFinalCastleDungeonSteps.class, "TFFCDunSt");
		MapGenStructureIO.registerStructureComponent(ComponentTFFinalCastleDungeonEntrance.class, "TFFCDunEn");
		MapGenStructureIO.registerStructureComponent(ComponentTFFinalCastleDungeonRoom31.class, "TFFCDunR31");
		MapGenStructureIO.registerStructureComponent(ComponentTFFinalCastleDungeonExit.class, "TFFCDunEx");
		MapGenStructureIO.registerStructureComponent(ComponentTFFinalCastleDungeonForgeRoom.class, "TFFCDunBoR");
		MapGenStructureIO.registerStructureComponent(ComponentTFFinalCastleRoof9Crenellated.class, "TFFCRo9Cr");
		MapGenStructureIO.registerStructureComponent(ComponentTFFinalCastleRoof13Crenellated.class, "TFFCRo13Cr");
		MapGenStructureIO.registerStructureComponent(ComponentTFFinalCastleRoof13Conical.class, "TFFCRo13Con");
		MapGenStructureIO.registerStructureComponent(ComponentTFFinalCastleRoof13Peaked.class, "TFFCRo13Pk");
		MapGenStructureIO.registerStructureComponent(ComponentTFFinalCastleEntranceTower.class, "TFFCEnTo");
		MapGenStructureIO.registerStructureComponent(ComponentTFFinalCastleEntranceSideTower.class, "TFFCEnSiTo");
		MapGenStructureIO.registerStructureComponent(ComponentTFFinalCastleEntranceBottomTower.class, "TFFCEnBoTo");
		MapGenStructureIO.registerStructureComponent(ComponentTFFinalCastleEntranceStairs.class, "TFFCEnSt");
		MapGenStructureIO.registerStructureComponent(ComponentTFFinalCastleBellTower21.class, "TFFCBelTo");
		MapGenStructureIO.registerStructureComponent(ComponentTFFinalCastleBridge.class, "TFFCBri");
		MapGenStructureIO.registerStructureComponent(ComponentTFFinalCastleFoundation13.class, "TFFCToF13");
		MapGenStructureIO.registerStructureComponent(ComponentTFFinalCastleBellFoundation21.class, "TFFCBeF21");
		MapGenStructureIO.registerStructureComponent(ComponentTFFinalCastleFoundation13Thorns.class, "TFFCFTh21");
		MapGenStructureIO.registerStructureComponent(ComponentTFFinalCastleDamagedTower.class, "TFFCDamT");
		MapGenStructureIO.registerStructureComponent(ComponentTFFinalCastleWreckedTower.class, "TFFCWrT");
	}
}
