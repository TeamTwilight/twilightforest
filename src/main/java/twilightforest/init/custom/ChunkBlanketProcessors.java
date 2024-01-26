package twilightforest.init.custom;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import net.minecraft.core.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ChunkHolder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ThreadedLevelLightEngine;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;
import twilightforest.TFRegistries;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFBiomes;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFStructures;
import twilightforest.world.components.chunkblanketing.CanopyBlanketProcessor;
import twilightforest.world.components.chunkblanketing.ChunkBlanketProcessor;
import twilightforest.world.components.chunkblanketing.ChunkBlanketType;
import twilightforest.world.components.chunkblanketing.GlacierBlanketProcessor;

import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Stream;

public final class ChunkBlanketProcessors {
    public static final DeferredRegister<ChunkBlanketType> CHUNK_BLANKETING_TYPES = DeferredRegister.create(TFRegistries.Keys.CHUNK_BLANKET_TYPE, TwilightForestMod.ID);
    public static final Codec<ChunkBlanketType> TYPE_CODEC = ExtraCodecs.lazyInitializedCodec(TFRegistries.CHUNK_BLANKET_TYPES::byNameCodec);
    public static final Codec<ChunkBlanketProcessor> DISPATCH_CODEC = TYPE_CODEC.dispatch("type", ChunkBlanketProcessor::getType, ChunkBlanketType::getCodec);

    public static final DeferredHolder<ChunkBlanketType, ChunkBlanketType> CANOPY = registerType("canopy", CanopyBlanketProcessor.CODEC);
    public static final DeferredHolder<ChunkBlanketType, ChunkBlanketType> GLACIER = registerType("glacier", GlacierBlanketProcessor.CODEC);

    public static final ResourceKey<ChunkBlanketProcessor> DARK_FOREST_CANOPY = ResourceKey.create(TFRegistries.Keys.CHUNK_BLANKET_PROCESSORS, TwilightForestMod.prefix("dark_forest_canopy"));
    public static final ResourceKey<ChunkBlanketProcessor> SNOWY_FOREST_GLACIER = ResourceKey.create(TFRegistries.Keys.CHUNK_BLANKET_PROCESSORS, TwilightForestMod.prefix("snowy_forest_glacier"));

    public static DeferredHolder<ChunkBlanketType, ChunkBlanketType> registerType(String name, Codec<? extends ChunkBlanketProcessor> codec) {
        Codec<? extends ChunkBlanketProcessor> boxedCodec = codec.fieldOf("config").codec();
        // Ensures the codec is boxed and this same boxed codec is returned every time
        return CHUNK_BLANKETING_TYPES.register(name, () -> () -> boxedCodec);
    }

    public static void bootstrap(BootstapContext<ChunkBlanketProcessor> context) {
        HolderGetter<Biome> biomes = context.lookup(Registries.BIOME);
        HolderGetter<Structure> structures = context.lookup(Registries.STRUCTURE);

        context.register(DARK_FOREST_CANOPY, new CanopyBlanketProcessor(HolderSet.direct(biomes.getOrThrow(TFBiomes.DARK_FOREST), biomes.getOrThrow(TFBiomes.DARK_FOREST_CENTER)), BlockStateProvider.simple(TFBlocks.HARDENED_DARK_LEAVES.value()), 14, HolderSet.direct(structures.getOrThrow(TFStructures.DARK_TOWER))));
        context.register(SNOWY_FOREST_GLACIER, new GlacierBlanketProcessor(HolderSet.direct(biomes.getOrThrow(TFBiomes.GLACIER)), BlockStateProvider.simple(Blocks.PACKED_ICE), BlockStateProvider.simple(Blocks.ICE), 32));
    }

    public static void init() {
        // Ensures a once-execution of the static ChunkStatus initializer below, injecting a custom Chunk Status as a side effect of class-loading.
        // On subsequent executes, expect no side effects to happen.
    }

    static {
        registerInjectChunkStatus(TwilightForestMod.prefix("surface_blanketing"), ChunkStatus.CARVERS, false, ChunkStatus.POST_FEATURES, ChunkStatus.ChunkType.PROTOCHUNK, ChunkBlanketProcessors::chunkBlanketing, ChunkBlanketProcessors::passThrough);
    }

    @SuppressWarnings("SameParameterValue")
    @NotNull
    private static ChunkStatus registerInjectChunkStatus(ResourceLocation name, ChunkStatus injectBefore, boolean hasLoadDependencies, EnumSet<Heightmap.Types> oceanFloor, ChunkStatus.ChunkType chunkType, ChunkStatus.SimpleGenerationTask simpleGenerationTask, ChunkStatus.LoadingTask loadingTask) {
        // Given the ASM works, this should execute while the registries are still unfrozen - to allow registration here
        final ChunkStatus forInjection = Registry.register(BuiltInRegistries.CHUNK_STATUS, name, new ChunkStatus(injectBefore.getParent(), injectBefore.getParent().range, hasLoadDependencies, oceanFloor, chunkType, simpleGenerationTask, loadingTask));

        injectBefore.parent = forInjection;

        shiftSequentialStatuses(forInjection);

        return forInjection;
    }

    private static void shiftSequentialStatuses(ChunkStatus forInjection) {
        var name = forInjection.toString();
        var status = ChunkStatus.FULL;
        while (status != status.parent && status != forInjection) {
            status.index++;
            TwilightForestMod.LOGGER.debug(status + " → " + status.index + " (After +1)");
            status = status.parent;
        }
        while (status != status.parent) {
            TwilightForestMod.LOGGER.debug(status + " → " + status.index + " (Unadjusted)");
            status = status.parent;
        }
        TwilightForestMod.LOGGER.info("Successfully processed injection for custom Chunk Status '" + name + "'");
    }

    private static final ResourceLocation WORLDGEN_REGION_RANDOM = new ResourceLocation("worldgen_region_random");

    // ChunkStatus.SimpleGenerationTask function implementation
    private static void chunkBlanketing(ChunkStatus status, ServerLevel serverLevel, ChunkGenerator generator, List<ChunkAccess> chunkAccesses, ChunkAccess chunkAccess) {
        ChunkPos chunkPos = chunkAccess.getPos();
        WorldGenRegion worldGenRegion = new WorldGenRegion(serverLevel, chunkAccesses, status, 0);

        Set<Holder<Biome>> biomesInChunk = new ObjectArraySet<>();

        for (LevelChunkSection levelchunksection : worldGenRegion.getChunk(chunkPos.x, chunkPos.z).getSections()) {
            levelchunksection.getBiomes().getAll(biomesInChunk::add);
        }

        Iterator<ChunkBlanketProcessor> modifierIterator = serverLevel
                .registryAccess()
                .registry(TFRegistries.Keys.CHUNK_BLANKET_PROCESSORS)
                .map(Registry::stream)
                .orElseGet(Stream::empty)
                .filter(modifier -> modifier.biomesForApplication().stream().anyMatch(biomesInChunk::contains))
                .iterator();

        Function<BlockPos, Holder<Biome>> biomeGetter = serverLevel.getBiomeManager()::getBiome;


        while (modifierIterator.hasNext()) {
            // Hopefully, for keeping some level of parity with the WorldGenRegion's RandomSource setup
            RandomSource random = serverLevel.getChunkSource().randomState().getOrCreateRandomFactory(WORLDGEN_REGION_RANDOM).at(chunkPos.getWorldPosition());
            modifierIterator.next().processChunk(random, biomeGetter, chunkAccess);
        }
    }

    // A ChunkStatus.LoadingTask function implementation
    private static CompletableFuture<Either<ChunkAccess, ChunkHolder.ChunkLoadingFailure>> passThrough(ChunkStatus chunkStatus, ServerLevel serverLevel, StructureTemplateManager templateManager, ThreadedLevelLightEngine threadedLevelLightEngine, Function<ChunkAccess, CompletableFuture<Either<ChunkAccess, ChunkHolder.ChunkLoadingFailure>>> completableFutureFunction, ChunkAccess chunkAccess) {
        return CompletableFuture.completedFuture(Either.left(chunkAccess));
    }
}
