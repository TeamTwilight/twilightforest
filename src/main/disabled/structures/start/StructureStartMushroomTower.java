package twilightforest.structures.start;

import net.minecraft.world.World;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import twilightforest.TFFeature;
import twilightforest.structures.mushroomtower.ComponentTFMushroomTowerMain;

import java.util.Random;

import static twilightforest.TFFeature.MUSHROOM_TOWER;

public class StructureStartMushroomTower extends StructureStartTFFeatureAbstract {
//    public StructureStartMushroomTower() {
//        super();
//    }

    public StructureStartMushroomTower(World world, TFFeature feature, Random rand, int chunkX, int chunkZ) {
        super(world, feature, rand, chunkX, chunkZ);
    }

    @Override
    protected StructurePiece makeFirstComponent(TFFeature feature, Random rand, int x, int y, int z) {
        return new ComponentTFMushroomTowerMain(MUSHROOM_TOWER, rand, 0, x, y, z);
    }
}