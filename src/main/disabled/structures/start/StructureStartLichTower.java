package twilightforest.structures.start;

import net.minecraft.world.World;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import twilightforest.TFFeature;
import twilightforest.structures.lichtower.ComponentTFTowerMain;

import java.util.Random;

import static twilightforest.TFFeature.LICH_TOWER;

public class StructureStartLichTower extends StructureStartTFFeatureAbstract {
//    public StructureStartLichTower() {
//        super();
//    }

    public StructureStartLichTower(World world, TFFeature feature, Random rand, int chunkX, int chunkZ) {
        super(world, feature, rand, chunkX, chunkZ);
    }

    @Override
    protected StructurePiece makeFirstComponent(TFFeature feature, Random rand, int x, int y, int z) {
        return new ComponentTFTowerMain(LICH_TOWER, rand, 0, x, y, z);
    }
}