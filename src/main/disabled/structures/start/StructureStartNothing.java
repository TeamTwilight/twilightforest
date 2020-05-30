package twilightforest.structures.start;

import net.minecraft.world.World;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import twilightforest.TFFeature;
import twilightforest.TwilightForestMod;

import java.util.Random;

public class StructureStartNothing extends StructureStartTFAbstract {
//    public StructureStartNothing() {
//        super();
//    }

    @Override
    protected StructurePiece makeFirstComponent(TFFeature feature, Random rand, int x, int y, int z) {
        //TwilightForestMod.LOGGER.warn("Generating nothing at pos " + x + ", " + y + ", " + z + "!");

        return null;
    }

    public StructureStartNothing(World world, Random rand, int chunkX, int chunkZ) {
        super(world, TFFeature.NOTHING, rand, chunkX, chunkZ);
        TwilightForestMod.LOGGER.warn("Generated nothing at chunk [{}, {}]!", chunkX, chunkZ);
    }
}