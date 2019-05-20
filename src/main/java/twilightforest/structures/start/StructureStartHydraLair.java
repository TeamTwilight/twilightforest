package twilightforest.structures.start;

import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureComponent;
import twilightforest.TFFeature;
import twilightforest.structures.ComponentTFHydraLair;

import java.util.Random;

import static twilightforest.TFFeature.HYDRA_LAIR;

public class StructureStartHydraLair extends StructureStartTFFeatureAbstract {
    public StructureStartHydraLair() {
        super();
    }

    public StructureStartHydraLair(World world, TFFeature feature, Random rand, int chunkX, int chunkZ) {
        super(world, feature, rand, chunkX, chunkZ);
    }

    @Override
    protected StructureComponent makeFirstComponent(World world, TFFeature feature, Random rand, int x, int y, int z) {
        return new ComponentTFHydraLair(HYDRA_LAIR, world, rand, 0, x, y, z);
    }
}