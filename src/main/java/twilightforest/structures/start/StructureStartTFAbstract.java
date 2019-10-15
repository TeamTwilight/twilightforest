package twilightforest.structures.start;

import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureStart;
import net.minecraft.world.gen.structure.template.TemplateManager;
import twilightforest.TFFeature;
import twilightforest.biomes.TFBiomes;
import twilightforest.structures.StructureTFComponentTemplate;
import twilightforest.structures.darktower.ComponentTFDarkTowerMain;
import twilightforest.structures.lichtower.ComponentTFTowerMain;
import twilightforest.world.TFWorld;

import java.util.Random;

public abstract class StructureStartTFAbstract extends StructureStart {

    public StructureStartTFAbstract() {
        super();
    }

    public StructureStartTFAbstract(World world, TFFeature feature, Random rand, int chunkX, int chunkZ) {
        super(chunkX, chunkZ);
        int x = (chunkX << 4) + 8;
        int z = (chunkZ << 4) + 8;
        int y = TFWorld.SEALEVEL + 1; //TODO: maybe a biome-specific altitude for some of them?

        StructureComponent firstComponent = makeFirstComponent(world, feature, rand, x, y, z);
        components.add(firstComponent);
        firstComponent.buildComponent(firstComponent, components, rand);

        updateBoundingBox();

        if (firstComponent instanceof ComponentTFTowerMain || firstComponent instanceof ComponentTFDarkTowerMain)
            moveToAvgGroundLevel(world, x, z);

        setupComponents(world);
    }

    protected abstract StructureComponent makeFirstComponent(World world, TFFeature feature, Random rand, int x, int y, int z);

    /**
     * Move the whole structure up or down
     */
    protected void moveToAvgGroundLevel(World world, int x, int z) {
        // determine the biome at the origin
        Biome biomeAt = world.getBiome(new BlockPos(x, 0, z));

        int offY = (int) ((biomeAt.getBaseHeight() + biomeAt.getHeightVariation()) * 8);

        // dark forest doesn't seem to get the right value. Why is my calculation so bad?
        if (biomeAt == TFBiomes.darkForest) offY += 4;

        if (offY > 0) {
            boundingBox.offset(0, offY, 0);

            for (StructureComponent com : getComponents()) {
                com.getBoundingBox().offset(0, offY, 0);
            }
        }
    }

    protected void setupComponents(World world) {
        TemplateManager templateManager = world.getSaveHandler().getStructureTemplateManager();
        MinecraftServer server = world.getMinecraftServer();

        for (StructureComponent component : components)
            if (component instanceof StructureTFComponentTemplate)
                ((StructureTFComponentTemplate) component).setup(templateManager, server);
    }
}
