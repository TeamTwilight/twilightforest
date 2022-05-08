package twilightforest.world.components.placements;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import twilightforest.TwilightForestMod;
import twilightforest.world.registration.TwilightFeatures;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Stream;

// Ideally, you should not be mixing this with other decorators unless you know what you're doing
// This litters memory with 64 block positions for each chunk. USE SPARINGLY
public class ChunkBlanketingModifier extends PlacementModifier {

    public static final Codec<ChunkBlanketingModifier> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.floatRange(0.0f, 1.0f).fieldOf("integrity").forGetter(o -> o.integrity),
            Heightmap.Types.CODEC.fieldOf("heightmap").forGetter(o -> o.heightmap),
            ResourceLocation.CODEC.optionalFieldOf("biome_lock").forGetter(o -> o.biomeRLOptional)
    ).apply(instance, ChunkBlanketingModifier::new));

    public final float integrity;
    public final Heightmap.Types heightmap;
    public final Optional<ResourceLocation> biomeRLOptional;

    public ChunkBlanketingModifier(float integrity, Heightmap.Types heightmap, Optional<ResourceLocation> biomeRLOptional) {
        this.integrity = integrity;
        this.heightmap = heightmap;
        this.biomeRLOptional = biomeRLOptional;
    }

    public static ChunkBlanketingModifier addThorns() {
        return new ChunkBlanketingModifier(0.7f, Heightmap.Types.OCEAN_FLOOR_WG, Optional.of(TwilightForestMod.prefix("thornlands")));
    }

    @Override
    public Stream<BlockPos> getPositions(PlacementContext context, Random random, BlockPos placement) {
        int chunkOriginX = placement.getX() & 0xfffffff0;
        int chunkOriginZ = placement.getZ() & 0xfffffff0;

        ArrayList<BlockPos> coordinates = new ArrayList<>();

        for (int zInChunk = 0; zInChunk < 16; zInChunk++) {
            for (int xInChunk = 0; xInChunk < 16; xInChunk++) {
                if (random.nextFloat() > integrity)
                    continue;

                BlockPos pos = new BlockPos(chunkOriginX + xInChunk, context.getHeight(heightmap, chunkOriginX + xInChunk, chunkOriginZ + zInChunk), chunkOriginZ + zInChunk);

                if (biomeRLOptional.isPresent()) {
                    if (biomeRLOptional.get().equals(context.getLevel().getBiome(pos).value().getRegistryName())) {
                        coordinates.add(pos);
                    }
                } else {
                    coordinates.add(pos);
                }
            }
        }

        return coordinates.stream();
    }

    @Override
    public PlacementModifierType<?> type() {
        return TwilightFeatures.CHUNK_BLANKETING.get();
    }
}
