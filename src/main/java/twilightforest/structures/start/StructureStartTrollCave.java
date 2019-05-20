package twilightforest.structures.start;

import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureComponent;
import twilightforest.TFFeature;
import twilightforest.structures.trollcave.ComponentTFTrollCaveMain;

import java.util.Random;

import static twilightforest.TFFeature.TROLL_CAVE;

public class StructureStartTrollCave extends StructureStartTFFeatureAbstract {
    public StructureStartTrollCave() {
        super();
    }

    public StructureStartTrollCave(World world, TFFeature feature, Random rand, int chunkX, int chunkZ) {
        super(world, feature, rand, chunkX, chunkZ);
    }

    @Override
    protected StructureComponent makeFirstComponent(World world, TFFeature feature, Random rand, int x, int y, int z) {
        return new ComponentTFTrollCaveMain(TROLL_CAVE, world, rand, 0, x, y, z);
    }
}