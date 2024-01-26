package twilightforest.world.components.chunkblanketing;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import twilightforest.init.custom.ChunkBlanketProcessors;

public record GlacierBlanketProcessor(HolderSet<Biome> biomesForApplication, BlockStateProvider glacierBody, BlockStateProvider glacierTop, int height) implements ChunkBlanketProcessor.SimpleProcessor {
    public static final Codec<GlacierBlanketProcessor> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            RegistryCodecs.homogeneousList(Registries.BIOME, true).fieldOf("biome_mask").forGetter(GlacierBlanketProcessor::biomesForApplication),
            BlockStateProvider.CODEC.fieldOf("body_block").forGetter(GlacierBlanketProcessor::glacierBody),
            BlockStateProvider.CODEC.fieldOf("top_block").forGetter(GlacierBlanketProcessor::glacierTop),
            Codec.INT.fieldOf("height").forGetter(GlacierBlanketProcessor::height)
    ).apply(inst, GlacierBlanketProcessor::new));

    @Override
    public void processColumn(RandomSource random, ChunkAccess chunkAccess, BlockPos aboveFloor) {
        int firstAvailableY = aboveFloor.getY();
        int maxY = firstAvailableY + this.height;

        BlockPos maxPosY = aboveFloor.atY(maxY);
        chunkAccess.setBlockState(maxPosY, this.glacierTop.getState(random, maxPosY), false);

        for (int y = maxY - 1; y >= firstAvailableY; y--) {
            BlockPos posSurfaceChunk = aboveFloor.atY(y);
            chunkAccess.setBlockState(posSurfaceChunk, this.glacierBody.getState(random, posSurfaceChunk), false);
        }
    }

    @Override
    public Heightmap.Types heightmap() {
        return Heightmap.Types.WORLD_SURFACE_WG;
    }

    @Override
    public ChunkBlanketType getType() {
        return ChunkBlanketProcessors.GLACIER.value();
    }
}
