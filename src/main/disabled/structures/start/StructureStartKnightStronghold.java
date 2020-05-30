package twilightforest.structures.start;

import net.minecraft.world.World;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import twilightforest.TFFeature;
import twilightforest.structures.stronghold.ComponentTFStrongholdEntrance;

import java.util.Random;

import static twilightforest.TFFeature.KNIGHT_STRONGHOLD;

public class StructureStartKnightStronghold extends StructureStartTFFeatureAbstract {
//    public StructureStartKnightStronghold() {
//        super();
//    }

    public StructureStartKnightStronghold(World world, TFFeature feature, Random rand, int chunkX, int chunkZ) {
        super(world, feature, rand, chunkX, chunkZ);
    }

    @Override
    protected StructurePiece makeFirstComponent(TFFeature feature, Random rand, int x, int y, int z) {
        return new ComponentTFStrongholdEntrance(KNIGHT_STRONGHOLD, rand, 0, x, y, z);
    }
}