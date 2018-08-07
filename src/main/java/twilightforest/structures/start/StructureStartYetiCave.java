package twilightforest.structures.start;

import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureComponent;
import twilightforest.TFFeature;
import twilightforest.structures.ComponentTFYetiCave;

import java.util.Random;

import static twilightforest.TFFeature.YETI_CAVE;

public class StructureStartYetiCave extends StructureStartTFAbstract {
    public StructureStartYetiCave() {
        super();
    }

    public StructureStartYetiCave(World world, TFFeature feature, Random rand, int chunkX, int chunkZ) {
        super(world, feature, rand, chunkX, chunkZ);
    }

    @Override
    protected StructureComponent makeFirstComponent(World world, TFFeature feature, Random rand, int x, int y, int z) {
        return new ComponentTFYetiCave(YETI_CAVE, world, rand, 0, x, y, z);
    }
}