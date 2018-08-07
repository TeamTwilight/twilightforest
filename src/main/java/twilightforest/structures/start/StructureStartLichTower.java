package twilightforest.structures.start;

import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureComponent;
import twilightforest.TFFeature;
import twilightforest.structures.lichtower.ComponentTFTowerMain;

import java.util.Random;

import static twilightforest.TFFeature.LICH_TOWER;

public class StructureStartLichTower extends StructureStartTFAbstract {
    public StructureStartLichTower() {
        super();
    }

    public StructureStartLichTower(World world, TFFeature feature, Random rand, int chunkX, int chunkZ) {
        super(world, feature, rand, chunkX, chunkZ);
    }

    @Override
    protected StructureComponent makeFirstComponent(World world, TFFeature feature, Random rand, int x, int y, int z) {
        return new ComponentTFTowerMain(LICH_TOWER, world, rand, 0, x, y, z);
    }
}