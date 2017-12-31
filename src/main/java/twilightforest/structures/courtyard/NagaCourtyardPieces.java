package twilightforest.structures.courtyard;

import net.minecraft.world.gen.structure.MapGenStructureIO;

public class NagaCourtyardPieces {
    public static void registerPieces() {
        MapGenStructureIO.registerStructureComponent(ComponentNagaCourtyardMain.class, "TFNCMn");
        MapGenStructureIO.registerStructureComponent(ComponentNagaCourtyardHedgeCap.class, "TFNCCp");
        MapGenStructureIO.registerStructureComponent(ComponentNagaCourtyardHedgeCorner.class, "TFNCCr");
        MapGenStructureIO.registerStructureComponent(ComponentNagaCourtyardHedgeLine.class, "TFNCLn");
        MapGenStructureIO.registerStructureComponent(ComponentNagaCourtyardHedgeTJunction.class, "TFNCT");
        MapGenStructureIO.registerStructureComponent(ComponentNagaCourtyardHedgeIntersection.class, "TFNCIs");
        MapGenStructureIO.registerStructureComponent(ComponentNagaCourtyardHedgePadder.class, "TFNCPd");
        MapGenStructureIO.registerStructureComponent(ComponentNagaCourtyardTerrace.class, "TFNCTr");
        MapGenStructureIO.registerStructureComponent(ComponentNagaCourtyardPath.class, "TFNCPa");
        MapGenStructureIO.registerStructureComponent(ComponentNagaCourtyardWall.class, "TFNCWl");
        MapGenStructureIO.registerStructureComponent(ComponentNagaCourtyardWallPadder.class, "TFNCWP");
        MapGenStructureIO.registerStructureComponent(ComponentNagaCourtyardWallCorner.class, "TFNCWC");
        MapGenStructureIO.registerStructureComponent(ComponentNagaCourtyardWallCornerAlt.class, "TFNCWA");
    }
}
