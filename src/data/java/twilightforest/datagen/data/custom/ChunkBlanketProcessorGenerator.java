package twilightforest.datagen.data.custom;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.structure.Structure;
import twilightforest.init.TFBiomes;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFStructures;
import twilightforest.init.custom.ChunkBlanketProcessors;
import twilightforest.world.components.chunkblanketing.CanopyBlanketProcessor;
import twilightforest.world.components.chunkblanketing.ChunkBlanketProcessor;
import twilightforest.world.components.chunkblanketing.GlacierBlanketProcessor;

public class ChunkBlanketProcessorGenerator {

    public static void bootstrap(BootstrapContext<ChunkBlanketProcessor> context) {
        HolderGetter<Biome> biomes = context.lookup(Registries.BIOME);
        HolderGetter<Structure> structures = context.lookup(Registries.STRUCTURE);

        context.register(ChunkBlanketProcessors.DARK_FOREST_CANOPY, new CanopyBlanketProcessor(HolderSet.direct(biomes.getOrThrow(TFBiomes.DARK_FOREST), biomes.getOrThrow(TFBiomes.DARK_FOREST_CENTER)), BlockStateProvider.simple(TFBlocks.HARDENED_DARK_LEAVES.value()), 14, HolderSet.direct(structures.getOrThrow(TFStructures.DARK_TOWER))));
        context.register(ChunkBlanketProcessors.SNOWY_FOREST_GLACIER, new GlacierBlanketProcessor(HolderSet.direct(biomes.getOrThrow(TFBiomes.GLACIER)), BlockStateProvider.simple(Blocks.PACKED_ICE), BlockStateProvider.simple(Blocks.ICE), 32));
    }

}
