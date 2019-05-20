package twilightforest.structures.start;

import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureComponent;
import twilightforest.TFFeature;
import twilightforest.structures.courtyard.ComponentNagaCourtyardMain;

import java.util.Random;

import static twilightforest.TFFeature.NAGA_COURTYARD;

public class StructureStartCourtyard extends StructureStartTFFeatureAbstract {
    public StructureStartCourtyard() {
        super();
    }

    public StructureStartCourtyard(World world, TFFeature feature, Random rand, int chunkX, int chunkZ) {
        super(world, feature, rand, chunkX, chunkZ);
    }

    @Override
    protected StructureComponent makeFirstComponent(World world, TFFeature feature, Random rand, int x, int y, int z) {
        return new ComponentNagaCourtyardMain(NAGA_COURTYARD, world, rand, 0, x, y, z);
    }
}