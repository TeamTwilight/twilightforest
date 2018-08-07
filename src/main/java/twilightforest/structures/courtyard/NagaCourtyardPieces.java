package twilightforest.structures.courtyard;

import net.minecraft.world.gen.structure.MapGenStructureIO;
import twilightforest.structures.start.StructureStartCourtyard;

public class NagaCourtyardPieces {
    public static void registerPieces() {
        MapGenStructureIO.registerStructure(StructureStartCourtyard.class, "TFNC");

        MapGenStructureIO.registerStructureComponent(ComponentNagaCourtyardMain.class, "TFNCMn");
        MapGenStructureIO.registerStructureComponent(ComponentNagaCourtyardHedgeCap.class, "TFNCCp");
        MapGenStructureIO.registerStructureComponent(ComponentNagaCourtyardHedgeCapPillar.class, "TFNCCpP");
        MapGenStructureIO.registerStructureComponent(ComponentNagaCourtyardHedgeCorner.class, "TFNCCr");
        MapGenStructureIO.registerStructureComponent(ComponentNagaCourtyardHedgeLine.class, "TFNCLn");
        MapGenStructureIO.registerStructureComponent(ComponentNagaCourtyardHedgeTJunction.class, "TFNCT");
        MapGenStructureIO.registerStructureComponent(ComponentNagaCourtyardHedgeIntersection.class, "TFNCIs");
        MapGenStructureIO.registerStructureComponent(ComponentNagaCourtyardHedgePadder.class, "TFNCPd");
        MapGenStructureIO.registerStructureComponent(ComponentNagaCourtyardTerraceBrazier.class, "TFNCTr");
        MapGenStructureIO.registerStructureComponent(ComponentNagaCourtyardTerraceDuct.class, "TFNCDu");
        MapGenStructureIO.registerStructureComponent(ComponentNagaCourtyardTerraceStatue.class, "TFNCSt");
        MapGenStructureIO.registerStructureComponent(ComponentNagaCourtyardPath.class, "TFNCPa");
        MapGenStructureIO.registerStructureComponent(ComponentNagaCourtyardWall.class, "TFNCWl");
        MapGenStructureIO.registerStructureComponent(ComponentNagaCourtyardWallPadder.class, "TFNCWP");
        MapGenStructureIO.registerStructureComponent(ComponentNagaCourtyardWallCorner.class, "TFNCWC");
        MapGenStructureIO.registerStructureComponent(ComponentNagaCourtyardWallCornerAlt.class, "TFNCWA");
    }
}
