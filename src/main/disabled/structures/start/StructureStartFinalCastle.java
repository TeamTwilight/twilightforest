package twilightforest.structures.start;

import net.minecraft.world.World;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import twilightforest.TFFeature;
import twilightforest.structures.finalcastle.ComponentTFFinalCastleMain;

import java.util.Random;

import static twilightforest.TFFeature.FINAL_CASTLE;

public class StructureStartFinalCastle extends StructureStartTFFeatureAbstract {
//    public StructureStartFinalCastle() {
//        super();
//    }

    public StructureStartFinalCastle(World world, TFFeature feature, Random rand, int chunkX, int chunkZ) {
        super(world, feature, rand, chunkX, chunkZ);
    }

    @Override
    protected StructurePiece makeFirstComponent(TFFeature feature, Random rand, int x, int y, int z) {
        return new ComponentTFFinalCastleMain(FINAL_CASTLE, rand, 0, x, y, z);
    }
}