package twilightforest.init.custom;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.RegistryFileCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import twilightforest.TFRegistries;
import twilightforest.init.TFStructures;
import twilightforest.util.RectangleLatticeIterator;
import twilightforest.world.components.structures.StructureSpeleothemConfig;

public class StructureSpeleothemConfigs {
    public static final RegistryFileCodec<StructureSpeleothemConfig> CODEC = RegistryFileCodec.create(TFRegistries.Keys.STRUCTURE_SPELEOTHEM_SETTINGS, StructureSpeleothemConfig.CODEC, false);

    public static final ResourceKey<StructureSpeleothemConfig> SMALL_HILL = makeKey(TFStructures.HOLLOW_HILL_SMALL.location());
    public static final ResourceKey<StructureSpeleothemConfig> MEDIUM_HILL = makeKey(TFStructures.HOLLOW_HILL_MEDIUM.location());
    public static final ResourceKey<StructureSpeleothemConfig> LARGE_HILL = makeKey(TFStructures.HOLLOW_HILL_LARGE.location());
    public static final ResourceKey<StructureSpeleothemConfig> HYDRA_LAIR = makeKey(TFStructures.HYDRA_LAIR.location());
    public static final ResourceKey<StructureSpeleothemConfig> YETI_CAVE = makeKey(TFStructures.YETI_CAVE.location());
    public static final ResourceKey<StructureSpeleothemConfig> TROLL_CAVE = makeKey(TFStructures.TROLL_CAVE.location());

    private static ResourceKey<StructureSpeleothemConfig> makeKey(ResourceLocation name) {
        return ResourceKey.create(TFRegistries.Keys.STRUCTURE_SPELEOTHEM_SETTINGS, name);
    }

    public static void bootstrap(BootstapContext<StructureSpeleothemConfig> context) {
        bootstrapRegister(context, SMALL_HILL, RectangleLatticeIterator.TriangularLatticeConfig.DEFAULT);
        bootstrapRegister(context, MEDIUM_HILL, RectangleLatticeIterator.TriangularLatticeConfig.DEFAULT);
        bootstrapRegister(context, LARGE_HILL, RectangleLatticeIterator.TriangularLatticeConfig.DEFAULT);
        bootstrapRegister(context, HYDRA_LAIR, new RectangleLatticeIterator.TriangularLatticeConfig(4.5f));
        bootstrapRegister(context, YETI_CAVE, RectangleLatticeIterator.TriangularLatticeConfig.DEFAULT);
        bootstrapRegister(context, TROLL_CAVE, new RectangleLatticeIterator.TriangularLatticeConfig(4.5f));
    }

    private static void bootstrapRegister(BootstapContext<StructureSpeleothemConfig> context, ResourceKey<StructureSpeleothemConfig> configKey, RectangleLatticeIterator.TriangularLatticeConfig latticeConfig) {
        context.register(configKey, StructureSpeleothemConfig.fromLocation(latticeConfig, configKey.location().getPath()));
    }

    @NotNull
    public static Holder.Reference<StructureSpeleothemConfig> getConfigHolder(HolderLookup.Provider registryAccess, String strRL) {
        return getConfigHolder(registryAccess, makeKey(new ResourceLocation(strRL)));
    }

    @NotNull
    public static Holder.Reference<StructureSpeleothemConfig> getConfigHolder(HolderLookup.Provider registryAccess, ResourceKey<StructureSpeleothemConfig> resourceKey) {
        return registryAccess.lookupOrThrow(TFRegistries.Keys.STRUCTURE_SPELEOTHEM_SETTINGS).get(resourceKey).get();
    }
}
