package twilightforest.datagen.data.custom;

import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import twilightforest.init.custom.StructureSpeleothemConfigs;
import twilightforest.util.iterators.RectangleLatticeIterator;
import twilightforest.world.components.structures.StructureSpeleothemConfig;

public class StructureSpeleothemConfigGenerator {

    public static void bootstrap(BootstrapContext<StructureSpeleothemConfig> context) {
        bootstrapRegister(context, StructureSpeleothemConfigs.SMALL_HILL, RectangleLatticeIterator.TriangularLatticeConfig.DEFAULT);
        bootstrapRegister(context, StructureSpeleothemConfigs.MEDIUM_HILL, RectangleLatticeIterator.TriangularLatticeConfig.DEFAULT);
        bootstrapRegister(context, StructureSpeleothemConfigs.LARGE_HILL, RectangleLatticeIterator.TriangularLatticeConfig.DEFAULT);
        bootstrapRegister(context, StructureSpeleothemConfigs.HYDRA_LAIR, new RectangleLatticeIterator.TriangularLatticeConfig(4.5f));
        bootstrapRegister(context, StructureSpeleothemConfigs.YETI_CAVE, RectangleLatticeIterator.TriangularLatticeConfig.DEFAULT);
        bootstrapRegister(context, StructureSpeleothemConfigs.TROLL_CAVE, new RectangleLatticeIterator.TriangularLatticeConfig(4.5f));
    }

    private static void bootstrapRegister(BootstrapContext<StructureSpeleothemConfig> context, ResourceKey<StructureSpeleothemConfig> configKey, RectangleLatticeIterator.TriangularLatticeConfig latticeConfig) {
        context.register(configKey, StructureSpeleothemConfig.fromLocation(latticeConfig, configKey.identifier().getPath()));
    }

}
