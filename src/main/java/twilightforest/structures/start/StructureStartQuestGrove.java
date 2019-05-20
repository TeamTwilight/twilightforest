package twilightforest.structures.start;

import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureComponent;
import twilightforest.TFFeature;
import twilightforest.structures.ComponentTFQuestGrove;

import java.util.Random;

import static twilightforest.TFFeature.QUEST_GROVE;

public class StructureStartQuestGrove extends StructureStartTFFeatureAbstract {
    public StructureStartQuestGrove() {
        super();
    }

    public StructureStartQuestGrove(World world, TFFeature feature, Random rand, int chunkX, int chunkZ) {
        super(world, feature, rand, chunkX, chunkZ);
    }

    @Override
    protected StructureComponent makeFirstComponent(World world, TFFeature feature, Random rand, int x, int y, int z) {
        return new ComponentTFQuestGrove(QUEST_GROVE, world, rand, 0, x, y, z);
    }
}