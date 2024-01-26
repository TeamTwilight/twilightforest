package twilightforest.world.components.structures.util;

import com.mojang.datafixers.Products;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import org.jetbrains.annotations.Nullable;
import twilightforest.world.components.biomesources.TFBiomeProvider;
import twilightforest.world.components.structures.TFStructureComponentTemplate;

import java.util.Optional;

// Landmark structure without progression lock; Hollow Hills/Hedge Maze/Naga Courtyard/Quest Grove
public abstract class LandmarkStructure extends Structure implements DecorationClearance {
    protected static <S extends LandmarkStructure> Products.P2<RecordCodecBuilder.Mu<S>, DecorationConfig, StructureSettings> landmarkCodec(RecordCodecBuilder.Instance<S> instance) {
        return instance.group(
                DecorationConfig.FLAT_CODEC.forGetter(s -> s.decorationConfig),
                Structure.settingsCodec(instance)
        );
    }

    protected final DecorationConfig decorationConfig;

    public LandmarkStructure(DecorationConfig decorationConfig, StructureSettings structureSettings) {
        super(structureSettings);
        this.decorationConfig = decorationConfig;
    }

    private static Structure.GenerationStub getStructurePieceGenerationStubFunction(StructurePiece startingPiece, GenerationContext context, int x, int y, int z) {
        return new GenerationStub(new BlockPos(x, y, z), structurePiecesBuilder -> {
            structurePiecesBuilder.addPiece(startingPiece);
            startingPiece.addChildren(startingPiece, structurePiecesBuilder, context.random());

            structurePiecesBuilder.pieces.stream()
                    .filter(TFStructureComponentTemplate.class::isInstance)
                    .map(TFStructureComponentTemplate.class::cast)
                    .forEach(t -> t.LAZY_TEMPLATE_LOADER.run());
        });
    }

    @Override
    protected Optional<GenerationStub> findGenerationPoint(GenerationContext context) {
        ChunkPos chunkPos = context.chunkPos();

        boolean dontCenter = this.dontCenter();
        int x = (chunkPos.x << 4) + (dontCenter ? 0 : 7);
        int z = (chunkPos.z << 4) + (dontCenter ? 0 : 7);
        int y = this.adjustForTerrain(context, x, z);

        return Optional
                .ofNullable(this.getFirstPiece(context, RandomSource.create(context.seed() + chunkPos.x * 25117L + chunkPos.z * 151121L), chunkPos, x, y, z))
                .map(piece -> getStructurePieceGenerationStubFunction(piece, context, x, y, z))
                ;
    }

    @Deprecated
    protected boolean dontCenter() {
        return false;
    }

    @Nullable
    protected abstract StructurePiece getFirstPiece(GenerationContext context, RandomSource random, ChunkPos chunkPos, int x, int y, int z);

    @Override
    public boolean isSurfaceDecorationsAllowed() {
        return this.decorationConfig.surfaceDecorations();
    }

    @Override
    public boolean isUndergroundDecoAllowed() {
        return this.decorationConfig.undergroundDecorations();
    }

    @Override
    public boolean shouldAdjustToTerrain() {
        return this.decorationConfig.adjustElevation();
    }

    @Override
    public int chunkClearanceRadius() {
        return this.decorationConfig.chunkClearanceRadius();
    }

    @Override
    public Optional<GenerationStub> findValidGenerationPoint(GenerationContext context) {
        if (!(context.biomeSource() instanceof TFBiomeProvider twilightBiomeProvider))
            return super.findValidGenerationPoint(context);

        ChunkPos chunkPos = context.chunkPos();
        // set biomeX and biomeZ to center of the biome-grid tile.
        // Otherwise some tightly-fitting biomes like Highlands vs Thornlands may fail the Troll-Clouds structure generation
        int biomeX = (Math.round(chunkPos.x / 16F) << 6) + 2;
        int biomeZ = (Math.round(chunkPos.z / 16F) << 6) + 2;

        Holder<Biome> biomeAt = twilightBiomeProvider.getMainBiome(biomeX, biomeZ);

        return context.validBiome().test(biomeAt) ? this.findGenerationPoint(context) : Optional.empty();
    }
}
