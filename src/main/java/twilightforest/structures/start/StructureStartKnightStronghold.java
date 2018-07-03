package twilightforest.structures.start;

import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureComponent;
import twilightforest.TFFeature;
import twilightforest.structures.stronghold.ComponentTFStrongholdEntrance;

import java.util.Random;

import static twilightforest.TFFeature.KNIGHT_STRONGHOLD;

public class StructureStartKnightStronghold extends StructureStartTFAbstract {
    public StructureStartKnightStronghold() {
        super();
    }

    public StructureStartKnightStronghold(World world, TFFeature feature, Random rand, int chunkX, int chunkZ) {
        super(world, feature, rand, chunkX, chunkZ);
    }

    @Override
    protected StructureComponent makeFirstComponent(World world, TFFeature feature, Random rand, int x, int y, int z) {
        return new ComponentTFStrongholdEntrance(KNIGHT_STRONGHOLD, world, rand, 0, x, y, z);
    }
}