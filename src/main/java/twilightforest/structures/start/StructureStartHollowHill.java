package twilightforest.structures.start;

import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureComponent;
import twilightforest.TFFeature;
import twilightforest.structures.ComponentTFHollowHill;
import twilightforest.structures.start.StructureStartTFAbstract;

import java.util.Random;

/**
 * Created by Drullkus on 6/26/18.
 */
public class StructureStartHollowHill extends StructureStartTFAbstract {
    public StructureStartHollowHill() {
        super();
    }

    public StructureStartHollowHill(World world, TFFeature feature, Random rand, int chunkX, int chunkZ) {
        super(world, feature, rand, chunkX, chunkZ);
    }

    @Override
    protected StructureComponent makeFirstComponent(World world, TFFeature feature, Random rand, int x, int y, int z) {
        return new ComponentTFHollowHill(feature, world, rand, 0, feature.size, x, y, z);
    }
}