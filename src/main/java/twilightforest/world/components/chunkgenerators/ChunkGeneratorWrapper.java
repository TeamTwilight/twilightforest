package twilightforest.world.components.chunkgenerators;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.*;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.*;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public abstract class ChunkGeneratorWrapper extends ChunkGenerator {
    public final ChunkGenerator delegate;

    public ChunkGeneratorWrapper(ChunkGenerator delegate) {
        super(delegate.getBiomeSource(), delegate.getBiomeSource(), delegate.getSettings(), delegate instanceof NoiseBasedChunkGenerator noiseGen ? noiseGen.seed : delegate.strongholdSeed);

        this.delegate = delegate;
    }

    @Override
    public void createBiomes(Registry<Biome> biomes, ChunkAccess chunkAccess) {
        this.delegate.createBiomes(biomes, chunkAccess);
    }

    @Override
    public void applyCarvers(long seed, BiomeManager biomeManager, ChunkAccess chunkAccess, GenerationStep.Carving carving) {
        this.delegate.applyCarvers(seed, biomeManager, chunkAccess, carving);
    }

    // Runtime will not care about public -> protected overrides, only compiletime will
    // I'd rather cut our losses than to attempt delegating this method because reflection would likely end up being
    // worse than accepting the potential problems of not delegating this method
    //@Override
    //public Aquifer createAquifer(ChunkAccess chunkAccess) {
    //    return this.delegate.createAquifer(chunkAccess);
    //}

    @Override
    @Nullable
    public BlockPos findNearestMapFeature(ServerLevel level, StructureFeature<?> structure, BlockPos pos, int searchRadius, boolean skipKnownStructures) {
        return this.delegate.findNearestMapFeature(level, structure, pos, searchRadius, skipKnownStructures);
    }

    @Override
    public void applyBiomeDecoration(WorldGenRegion region, StructureFeatureManager structureManager) {
        this.delegate.applyBiomeDecoration(region, structureManager);
    }

    @Override
    public void buildSurfaceAndBedrock(WorldGenRegion level, ChunkAccess chunkAccess) {
        this.delegate.buildSurfaceAndBedrock(level, chunkAccess);
    }

    @Override
    public void spawnOriginalMobs(WorldGenRegion region) {
        this.delegate.spawnOriginalMobs(region);
    }

    @Override
    public StructureSettings getSettings() {
        return this.delegate.getSettings();
    }

    @Override
    public int getSpawnHeight(LevelHeightAccessor level) {
        return this.delegate.getSpawnHeight(level);
    }

    @Override
    public BiomeSource getBiomeSource() {
        return this.delegate.getBiomeSource();
    }

    @Override
    public int getGenDepth() {
        return this.delegate.getGenDepth();
    }

    @Override
    public WeightedRandomList<MobSpawnSettings.SpawnerData> getMobsAt(Biome biome, StructureFeatureManager structureManager, MobCategory mobCategory, BlockPos pos) {
        return this.delegate.getMobsAt(biome, structureManager, mobCategory, pos);
    }

    @Override
    public void createStructures(RegistryAccess registry, StructureFeatureManager structureManager, ChunkAccess chunkAccess, StructureManager templateManager, long pSeed) {
        this.delegate.createStructures(registry, structureManager, chunkAccess, templateManager, pSeed);
    }

    @Override
    public void createReferences(WorldGenLevel level, StructureFeatureManager structureManager, ChunkAccess chunkAccess) {
        this.delegate.createReferences(level, structureManager, chunkAccess);
    }

    @Override
    public CompletableFuture<ChunkAccess> fillFromNoise(Executor executor, StructureFeatureManager structureManager, ChunkAccess chunkAccess) {
        return this.delegate.fillFromNoise(executor, structureManager, chunkAccess);
    }

    @Override
    public int getSeaLevel() {
        return this.delegate.getSeaLevel();
    }

    @Override
    public int getMinY() {
        return this.delegate.getMinY();
    }

    @Override
    public int getBaseHeight(int x, int z, Heightmap.Types heightMap, LevelHeightAccessor level) {
        return this.delegate.getBaseHeight(x, z, heightMap, level);
    }

    @Override
    public NoiseColumn getBaseColumn(int x, int z, LevelHeightAccessor level) {
        return this.delegate.getBaseColumn(x, z, level);
    }

    @Override
    public int getFirstFreeHeight(int x, int z, Heightmap.Types heightMapType, LevelHeightAccessor level) {
        return this.delegate.getFirstFreeHeight(x, z, heightMapType, level);
    }

    @Override
    public int getFirstOccupiedHeight(int x, int z, Heightmap.Types heightMapType, LevelHeightAccessor level) {
        return this.delegate.getFirstOccupiedHeight(x, z, heightMapType, level);
    }

    @Override
    public boolean hasStronghold(ChunkPos chunkPos) {
        return this.delegate.hasStronghold(chunkPos);
    }

    @Override
    public BaseStoneSource getBaseStoneSource() {
        return this.delegate.getBaseStoneSource();
    }
}
