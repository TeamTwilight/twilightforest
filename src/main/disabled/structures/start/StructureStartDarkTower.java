package twilightforest.structures.start;

import net.minecraft.world.World;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import twilightforest.TFFeature;
import twilightforest.structures.darktower.ComponentTFDarkTowerMain;

import java.util.Random;

import static twilightforest.TFFeature.DARK_TOWER;

public class StructureStartDarkTower extends StructureStartTFFeatureAbstract {
//    public StructureStartDarkTower() {
//        super();
//    }

    public StructureStartDarkTower(World world, TFFeature feature, Random rand, int chunkX, int chunkZ) {
        super(world, feature, rand, chunkX, chunkZ);
    }

    @Override
    protected StructurePiece makeFirstComponent(TFFeature feature, Random rand, int x, int y, int z) {
        return new ComponentTFDarkTowerMain(DARK_TOWER, rand, 0, x, y - 1, z);
    }
}