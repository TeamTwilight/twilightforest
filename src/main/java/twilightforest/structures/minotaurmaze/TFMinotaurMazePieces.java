package twilightforest.structures.minotaurmaze;

import net.minecraft.world.gen.structure.MapGenStructureIO;
import twilightforest.structures.start.StructureStartLabyrinth;


public class TFMinotaurMazePieces {


	public static void registerPieces() {
		MapGenStructureIO.registerStructure(StructureStartLabyrinth.class, "TFLr");

		MapGenStructureIO.registerStructureComponent(ComponentTFMazeCorridor.class, "TFMMC");
		MapGenStructureIO.registerStructureComponent(ComponentTFMazeCorridorIronFence.class, "TFMMCIF");
		MapGenStructureIO.registerStructureComponent(ComponentTFMazeCorridorRoots.class, "TFMMCR");
		MapGenStructureIO.registerStructureComponent(ComponentTFMazeCorridorShrooms.class, "TFMMCS");
		MapGenStructureIO.registerStructureComponent(ComponentTFMazeDeadEnd.class, "TFMMDE");
		MapGenStructureIO.registerStructureComponent(ComponentTFMazeDeadEndChest.class, "TFMMDEC");
		MapGenStructureIO.registerStructureComponent(ComponentTFMazeDeadEndFountain.class, "TFMMDEF");
		MapGenStructureIO.registerStructureComponent(ComponentTFMazeDeadEndFountainLava.class, "TFMMDEFL");
		MapGenStructureIO.registerStructureComponent(ComponentTFMazeDeadEndPainting.class, "TFMMDEP");
		MapGenStructureIO.registerStructureComponent(ComponentTFMazeDeadEndRoots.class, "TFMMDER");
		MapGenStructureIO.registerStructureComponent(ComponentTFMazeDeadEndShrooms.class, "TFMMDES");
		MapGenStructureIO.registerStructureComponent(ComponentTFMazeDeadEndTorches.class, "TFMMDET");
		MapGenStructureIO.registerStructureComponent(ComponentTFMazeDeadEndTrappedChest.class, "TFMMDETC");
		MapGenStructureIO.registerStructureComponent(ComponentTFMazeEntranceShaft.class, "TFMMES");
		MapGenStructureIO.registerStructureComponent(ComponentTFMazeMound.class, "TFMMMound");
		MapGenStructureIO.registerStructureComponent(ComponentTFMazeMushRoom.class, "TFMMMR");
		MapGenStructureIO.registerStructureComponent(ComponentTFMazeRoom.class, "TFMMR");
		MapGenStructureIO.registerStructureComponent(ComponentTFMazeRoomBoss.class, "TFMMRB");
		MapGenStructureIO.registerStructureComponent(ComponentTFMazeRoomCollapse.class, "TFMMRC");
		MapGenStructureIO.registerStructureComponent(ComponentTFMazeRoomExit.class, "TFMMRE");
		MapGenStructureIO.registerStructureComponent(ComponentTFMazeRoomFountain.class, "TFMMRF");
		MapGenStructureIO.registerStructureComponent(ComponentTFMazeRoomSpawnerChests.class, "TFMMRSC");
		MapGenStructureIO.registerStructureComponent(ComponentTFMazeRoomVault.class, "TFMMRV");
		MapGenStructureIO.registerStructureComponent(ComponentTFMazeRuins.class, "TFMMRuins");
		MapGenStructureIO.registerStructureComponent(ComponentTFMazeUpperEntrance.class, "TFMMUE");
		MapGenStructureIO.registerStructureComponent(ComponentTFMinotaurMaze.class, "TFMMaze");
	}

}