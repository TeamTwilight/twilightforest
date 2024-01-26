package twilightforest.world.components.chunkblanketing;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.XoroshiroRandomSource;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.structure.Structure;
import twilightforest.init.TFDimensionSettings;
import twilightforest.init.custom.ChunkBlanketProcessors;
import twilightforest.util.LegacyLandmarkPlacements;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.function.Function;

public record CanopyBlanketProcessor(HolderSet<Biome> biomesForApplication, BlockStateProvider blockState, int height, HolderSet<Structure> avoidStructures) implements ChunkBlanketProcessor {
    public static final Codec<CanopyBlanketProcessor> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            RegistryCodecs.homogeneousList(Registries.BIOME, true).fieldOf("biome_mask").forGetter(CanopyBlanketProcessor::biomesForApplication),
            BlockStateProvider.CODEC.fieldOf("block").forGetter(CanopyBlanketProcessor::blockState),
            Codec.INT.fieldOf("height").forGetter(CanopyBlanketProcessor::height),
            RegistryCodecs.homogeneousList(Registries.STRUCTURE, true).fieldOf("avoid_structures").forGetter(CanopyBlanketProcessor::avoidStructures)
    ).apply(inst, CanopyBlanketProcessor::new));

    @Override
    public void processChunk(RandomSource random, Function<BlockPos, Holder<Biome>> biomeGetter, ChunkAccess chunkAccess) {
        Collection<Structure> avoidStructures = this.avoidStructures.stream().map(Holder::value).toList();

        addDarkForestCanopy(biomeGetter, chunkAccess, this.height, this.biomesForApplication, this.blockState, avoidStructures);
    }

    /**
     * Adds dark forest canopy.  This version uses the "unzoomed" array of biomes used in land generation to determine how many of the nearby blocks are dark forest
     */
    private static boolean addDarkForestCanopy(Function<BlockPos, Holder<Biome>> biomeGetter, ChunkAccess chunk, int height, HolderSet<Biome> biomeFilter, BlockStateProvider canopyBlock, Collection<Structure> avoidStructures) {
        ChunkPos chunkPos = chunk.getPos();
        BlockPos chunkOrigin = chunkPos.getWorldPosition();
        int[] thicks = new int[5 * 5];
        boolean biomeFound = false;

        for (int dZ = 0; dZ < 5; dZ++) {
            for (int dX = 0; dX < 5; dX++) {
                for (int bx = -1; bx <= 1; bx++) {
                    for (int bz = -1; bz <= 1; bz++) {
                        Holder<Biome> biomeAt = biomeGetter.apply(chunkOrigin.offset((dX + bx) << 2, 0, (dZ + bz) << 2));

                        if (biomeFilter.contains(biomeAt)) {
                            thicks[dX + dZ * 5]++;
                            biomeFound = true;
                        }
                    }
                }
            }
        }

        if (!biomeFound) return false;

        Set<Structure> structuresThroughChunk = chunk.getAllReferences().keySet();
        boolean clearingForStructureNearby = !(structuresThroughChunk.isEmpty() || Collections.disjoint(structuresThroughChunk, avoidStructures));
        // make sure we're not too close to the tower. Skip that method call if the center pos won't be checked at all, substitute with zero pos instead
        BlockPos nearestCenter = clearingForStructureNearby ? LegacyLandmarkPlacements.getNearestCenterXZ(chunkPos.x, chunkPos.z, height).subtract(chunkOrigin) : BlockPos.ZERO;
        int hx = nearestCenter.getX();
        int hz = nearestCenter.getZ();

        RandomSource random = new XoroshiroRandomSource(TFDimensionSettings.seed, Mth.getSeed(chunkOrigin));

        for (int dZ = 0; dZ < 16; dZ++) {
            for (int dX = 0; dX < 16; dX++) {
                int qx = dX >> 2;
                int qz = dZ >> 2;

                final int topOccupiedBlock = chunk.getHeight(Heightmap.Types.WORLD_SURFACE_WG, dX, dZ);
                // We can use the Deltas here as they are offsets from chunk origin
                BlockPos surfacePos = chunkOrigin.offset(dX, topOccupiedBlock, dZ);

                if (chunk.getFluidState(surfacePos).is(FluidTags.WATER)) continue;

                float xweight = (dX % 4) * 0.25F + 0.125F;
                float zweight = (dZ % 4) * 0.25F + 0.125F;

                float thickness = thicks[qx + (qz) * 5] * (1F - xweight) * (1F - zweight)
                        + thicks[qx + 1 + (qz) * 5] * (xweight) * (1F - zweight)
                        + thicks[qx + (qz + 1) * 5] * (1F - xweight) * (zweight)
                        + thicks[qx + 1 + (qz + 1) * 5] * (xweight) * (zweight)
                        - 4;

                if (clearingForStructureNearby) {
                    int rx = dX - hx;
                    int rz = dZ - hz;
                    int dist = (int) Mth.sqrt(rx * rx + rz * rz);

                    if (dist < 24) {
                        thickness -= (24 - dist);
                    }
                }

                if (thickness > 1) {
                    // We can use the Delta here as it is offset from chunk origin
                    final int dY = chunk.getHeight(Heightmap.Types.WORLD_SURFACE_WG, dX, dZ);
                    BlockPos pos = surfacePos.atY(dY);

                    // Skip any blocks over water
                    if (chunk.getBlockState(pos).liquid())
                        continue;

                    // manipulate top and bottom
                    int treeBottom = pos.getY() + height - (int) (thickness * 0.5F);
                    int treeTop = treeBottom + (int) (thickness * 1.5F);

                    for (int y = treeBottom; y < treeTop; y++) {
                        chunk.setBlockState(pos.atY(y), canopyBlock.getState(random, pos), false);
                    }
                }
            }
        }

        return true;
    }

    @Override
    public ChunkBlanketType getType() {
        return ChunkBlanketProcessors.CANOPY.value();
    }
}
