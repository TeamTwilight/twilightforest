package twilightforest.structures.start;

import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureComponent;
import twilightforest.TFFeature;
import twilightforest.structures.ComponentTFHedgeMaze;

import java.util.Random;

import static twilightforest.TFFeature.HEDGE_MAZE;

public class StructureStartHedgeMaze extends StructureStartTFAbstract {
    public StructureStartHedgeMaze() {
        super();
    }

    public StructureStartHedgeMaze(World world, TFFeature feature, Random rand, int chunkX, int chunkZ) {
        super(world, feature, rand, chunkX, chunkZ);
    }

    @Override
    protected StructureComponent makeFirstComponent(World world, TFFeature feature, Random rand, int x, int y, int z) {
        return new ComponentTFHedgeMaze(HEDGE_MAZE, world, rand, 0, x, y, z);
    }
}