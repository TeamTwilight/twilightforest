package twilightforest.data;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonElement;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import net.minecraft.data.BiomeProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IDataProvider;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.*;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.Features;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.MultipleRandomFeatureConfig;
import net.minecraft.world.gen.placement.AtSurfaceWithExtraConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.surfacebuilders.ConfiguredSurfaceBuilders;
import twilightforest.entity.TFEntities;
import twilightforest.world.feature.TFBiomeFeatures;
import twilightforest.world.newfeature.TwilightFeatures;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@SuppressWarnings("UnusedReturnValue")
public abstract class BiomeDataHelper extends BiomeProvider {
    public BiomeDataHelper(DataGenerator generator) {
        super(generator);
    }

    public static BiomeGenerationSettings.Builder addWoodRoots(BiomeGenerationSettings.Builder biome) {
        biome.withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, TFBiomeFeatures.WOOD_ROOTS.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).func_242733_d(30).func_242728_a().func_242731_b(20));

        return biome;
    }

    //Canopies, trees, and anything resembling a forest thing
    public static BiomeGenerationSettings.Builder addCanopy(BiomeGenerationSettings.Builder biome) {
        biome.withFeature(GenerationStage.Decoration.VEGETAL_DECORATION, TwilightFeatures.ConfiguredFeatures.CANOPY_TREE.withPlacement(Placement.field_242902_f.configure(new AtSurfaceWithExtraConfig(8, 0.1F, 1)).func_242728_a()));

        return biome;
    }
    public static BiomeGenerationSettings.Builder addCanopyFirefly(BiomeGenerationSettings.Builder biome) {
        biome.withFeature(GenerationStage.Decoration.VEGETAL_DECORATION, TwilightFeatures.ConfiguredFeatures.CANOPY_TREE_FIREFLY.withPlacement(Placement.field_242902_f.configure(new AtSurfaceWithExtraConfig(8, 0.1F, 1)).func_242728_a()));

        return biome;
    }

    public static BiomeGenerationSettings.Builder addCanopyDead(BiomeGenerationSettings.Builder biome) {
        biome.withFeature(GenerationStage.Decoration.VEGETAL_DECORATION, TwilightFeatures.ConfiguredFeatures.CANOPY_TREE_DEAD.withPlacement(Placement.field_242902_f.configure(new AtSurfaceWithExtraConfig(8, 0.1F, 1)).func_242728_a()));

        return biome;
    }

    public static BiomeGenerationSettings.Builder addMushroomCanopy(BiomeGenerationSettings.Builder biome, float mushroomChance) {
        DefaultBiomeFeatures.withNormalMushroomGeneration(biome); // Add small mushrooms
        DefaultBiomeFeatures.withMushroomBiomeVegetation(biome); // Add large mushrooms

        biome.withFeature(
                GenerationStage.Decoration.VEGETAL_DECORATION,
                Feature.RANDOM_SELECTOR.withConfiguration(new MultipleRandomFeatureConfig(ImmutableList.of(
                        TwilightFeatures.ConfiguredFeatures.MUSHROOM_BROWN.withChance(mushroomChance * 0.75f),
                        TwilightFeatures.ConfiguredFeatures.MUSHROOM_RED.withChance(mushroomChance * 0.25f)
                ), TwilightFeatures.ConfiguredFeatures.CANOPY_TREE))
                        .withPlacement(Features.Placements.BAMBOO_PLACEMENT) //TODO?
                        .func_242728_a()
                        .func_242731_b(8)
        );

        return biome;
    }

    protected static BiomeGenerationSettings.Builder modify(BiomeGenerationSettings.Builder builder, Consumer<BiomeGenerationSettings.Builder> consumer) {
        consumer.accept(builder);
        return builder;
    }

    // Defaults

    protected static BiomeAmbience.Builder defaultAmbientBuilder() {
        return (new BiomeAmbience.Builder())
                .setFogColor(0xC0FFD8) // TODO Change based on Biome. Not previously done before
                .setWaterColor(0x3F76E4)
                .setWaterFogColor(0x050533)
                .withSkyColor(0x20224A) //TODO Change based on Biome. Not previously done before
                .setMoodSound(MoodSoundAmbience.DEFAULT_CAVE); // We should probably change it
    }

    protected static BiomeGenerationSettings.Builder defaultGenSettingBuilder() {
        BiomeGenerationSettings.Builder biome = (new BiomeGenerationSettings.Builder())
                .withSurfaceBuilder(ConfiguredSurfaceBuilders.field_244178_j); // GRASS_DIRT_GRAVEL_CONFIG

        // TODO Re-enable, currently disabled so it's just easier to read the jsons
        DefaultBiomeFeatures.withCommonOverworldBlocks(biome);
        DefaultBiomeFeatures.withOverworldOres(biome);
        DefaultBiomeFeatures.withLavaAndWaterLakes(biome);
        DefaultBiomeFeatures.withClayDisks(biome);
        DefaultBiomeFeatures.withForestGrass(biome);
        DefaultBiomeFeatures.withTallGrass(biome);

        DefaultBiomeFeatures.withLavaAndWaterSprings(biome);

        addWoodRoots(biome);

        return biome;
    }

    protected static MobSpawnInfo.Builder defaultMobSpawning() {
        MobSpawnInfo.Builder spawnInfo = new MobSpawnInfo.Builder();

        spawnInfo.withCreatureSpawnProbability(0.1f);

        spawnInfo.withSpawner(EntityClassification.CREATURE, new MobSpawnInfo.Spawners(TFEntities.bighorn_sheep, 12, 4, 4));
        spawnInfo.withSpawner(EntityClassification.CREATURE, new MobSpawnInfo.Spawners(TFEntities.wild_boar, 10, 4, 4));
        spawnInfo.withSpawner(EntityClassification.CREATURE, new MobSpawnInfo.Spawners(EntityType.CHICKEN, 10, 4, 4));
        spawnInfo.withSpawner(EntityClassification.CREATURE, new MobSpawnInfo.Spawners(TFEntities.deer, 15, 4, 5));
        spawnInfo.withSpawner(EntityClassification.CREATURE, new MobSpawnInfo.Spawners(EntityType.WOLF, 5, 4, 4));

        spawnInfo.withSpawner(EntityClassification.CREATURE, new MobSpawnInfo.Spawners(TFEntities.tiny_bird, 15, 4, 8));
        spawnInfo.withSpawner(EntityClassification.CREATURE, new MobSpawnInfo.Spawners(TFEntities.squirrel, 10, 2, 4));
        spawnInfo.withSpawner(EntityClassification.CREATURE, new MobSpawnInfo.Spawners(TFEntities.bunny, 10, 4, 5));
        spawnInfo.withSpawner(EntityClassification.CREATURE, new MobSpawnInfo.Spawners(TFEntities.raven, 10, 1, 2));

        // TODO make Monsters spawn underground only somehow - These are originally underground spawns
        spawnInfo.withSpawner(EntityClassification.MONSTER, new MobSpawnInfo.Spawners(EntityType.SPIDER, 10, 4, 4));
        spawnInfo.withSpawner(EntityClassification.MONSTER, new MobSpawnInfo.Spawners(EntityType.ZOMBIE, 10, 4, 4));
        spawnInfo.withSpawner(EntityClassification.MONSTER, new MobSpawnInfo.Spawners(EntityType.SKELETON, 10, 4, 4));
        spawnInfo.withSpawner(EntityClassification.MONSTER, new MobSpawnInfo.Spawners(EntityType.CREEPER, 1, 4, 4));
        spawnInfo.withSpawner(EntityClassification.MONSTER, new MobSpawnInfo.Spawners(EntityType.SLIME, 10, 4, 4));
        spawnInfo.withSpawner(EntityClassification.MONSTER, new MobSpawnInfo.Spawners(EntityType.ENDERMAN, 1, 1, 4));
        spawnInfo.withSpawner(EntityClassification.MONSTER, new MobSpawnInfo.Spawners(TFEntities.kobold, 10, 4, 8));

        spawnInfo.withSpawner(EntityClassification.AMBIENT, new MobSpawnInfo.Spawners(EntityType.BAT, 10, 8, 8));

        return spawnInfo;
    }

    protected static Biome.Builder biomeWithDefaults() {
        return biomeWithDefaults(defaultAmbientBuilder(), new MobSpawnInfo.Builder(), defaultGenSettingBuilder());
    }

    protected static Biome.Builder biomeWithDefaults(BiomeAmbience.Builder biomeAmbience, MobSpawnInfo.Builder mobSpawnInfo, BiomeGenerationSettings.Builder biomeGenerationSettings) {
        return (new Biome.Builder())
                .precipitation(Biome.RainType.RAIN)
                .category(Biome.Category.FOREST)
                .depth(0.1F)
                .scale(0.2F)
                .temperature(0.5F)
                .downfall(0.5F)
                .setEffects(biomeAmbience.build())
                .withMobSpawnSettings(mobSpawnInfo.copy())
                .withGenerationSettings(biomeGenerationSettings.build())
                .withTemperatureModifier(Biome.TemperatureModifier.NONE);
    }

    // Main methods

    /** Vanillacopy net.minecraft.data.BiomeProvider */
    @Override
    public void act(final DirectoryCache directoryCache) {
        final Path outputPath = this.generator.getOutputFolder();

        for (Map.Entry<ResourceLocation, Biome> biomeKeyPair : generateBiomes().entrySet()) {
            Path filePath = makePath(outputPath, biomeKeyPair.getKey());
            Function<Supplier<Biome>, DataResult<JsonElement>> serializer = JsonOps.INSTANCE.withEncoder(Biome.BIOME_CODEC);

            try {
                Optional<JsonElement> jsonOptional = serializer.apply(biomeKeyPair::getValue).result();

                if (jsonOptional.isPresent()) {
                    IDataProvider.save(GSON, directoryCache, jsonOptional.get(), filePath);

                    System.out.print("\n\"" + biomeKeyPair.getKey() + "\", ");
                } else {
                    LOGGER.error("Couldn't serialize biome {}", filePath);
                }
            } catch (IOException e) {
                LOGGER.error("Couldn't save biome {}", filePath, e);
            }
        }

        System.out.print("\nTF Biome generation finished execution!\n");
    }

    protected static Path makePath(Path path, ResourceLocation resc) {
        return path.resolve("data/" + resc.getNamespace() + "/worldgen/biome/" + resc.getPath() + ".json");
    }

    protected abstract Map<ResourceLocation, Biome> generateBiomes();
}
