package twilightforest.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.Encoder;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.Lifecycle;
import net.minecraft.core.*;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.HashCache;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import org.slf4j.Logger;
import twilightforest.TwilightForestMod;
import twilightforest.world.components.biomesources.TFBiomeProvider;
import twilightforest.world.components.chunkgenerators.ChunkGeneratorTwilight;
import twilightforest.world.components.chunkgenerators.warp.TerrainPoint;
import twilightforest.world.registration.TFNoiseGenerationSettings;
import twilightforest.world.registration.biomes.BiomeKeys;
import twilightforest.world.registration.biomes.BiomeMaker;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalLong;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public record WorldGenerator(DataGenerator generator) implements DataProvider {

	private static final Logger LOGGER = LogUtils.getLogger();
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

	public void run(HashCache cache) {
		Path path = this.generator.getOutputFolder();
		RegistryAccess registryaccess = BuiltinRegistries.ACCESS;
		DynamicOps<JsonElement> dynamicops = RegistryOps.create(JsonOps.INSTANCE, registryaccess);

		Registry<LevelStem> twilight = this.registerTFSettings(registryaccess);
		//Registry<LevelStem> skylight = this.registerSkylightSettings(registryaccess);
		//TODO void world

		WritableRegistry<Biome> biomeRegistry = new MappedRegistry<>(Registry.BIOME_REGISTRY, Lifecycle.experimental(), null);
		Map<ResourceLocation, Biome> biomes = this.getBiomes();
		biomes.forEach((rl, biome) -> biomeRegistry.register(ResourceKey.create(Registry.BIOME_REGISTRY, rl), biome, Lifecycle.experimental()));

		StreamSupport.stream(RegistryAccess.knownRegistries().spliterator(), false)
				.filter(r -> registryaccess.ownedRegistry(r.key()).isPresent() && !r.key().equals(Registry.BIOME_REGISTRY))
				.forEach((data) -> dumpRegistryCap(cache, path, registryaccess, dynamicops, data));

		LOGGER.info("Dumping real BIOME_REGISTRY");
		dumpRegistry(path, cache, dynamicops, Registry.BIOME_REGISTRY, biomeRegistry, Biome.DIRECT_CODEC);

		LOGGER.info("Dumping real LEVEL_STEM_REGISTRY");
		dumpRegistry(path, cache, dynamicops, Registry.LEVEL_STEM_REGISTRY, twilight, LevelStem.CODEC);
	}

	private static <T> void dumpRegistryCap(HashCache cache, Path path, RegistryAccess access, DynamicOps<JsonElement> ops, RegistryAccess.RegistryData<T> data) {
		LOGGER.info("Dumping: {}", data.key());
		dumpRegistry(path, cache, ops, data.key(), access.ownedRegistryOrThrow(data.key()), data.codec());
	}

	private Registry<LevelStem> registerTFSettings(RegistryAccess access) {
		WritableRegistry<LevelStem> writableregistry = new MappedRegistry<>(Registry.LEVEL_STEM_REGISTRY, Lifecycle.experimental(), null);
		Registry<Biome> biomeRegistry = access.registryOrThrow(Registry.BIOME_REGISTRY);
		Holder<NoiseGeneratorSettings> noiseGenSettings = access.registryOrThrow(Registry.NOISE_GENERATOR_SETTINGS_REGISTRY).getOrCreateHolder(TFNoiseGenerationSettings.TWILIGHT_NOISE_GEN.getKey());

		NoiseBasedChunkGenerator forestChunkGen =
				new NoiseBasedChunkGenerator(
						access.registryOrThrow(Registry.STRUCTURE_SET_REGISTRY),
						access.registryOrThrow(Registry.NOISE_REGISTRY),
						new TFBiomeProvider(0L, biomeRegistry, makeBiomeList(biomeRegistry), -0.49F, 1.0F),
						0L,
						noiseGenSettings
				);

		writableregistry.register(ResourceKey.create(Registry.LEVEL_STEM_REGISTRY, TwilightForestMod.prefix("twilightforest")), new LevelStem(Holder.direct(this.twilightDimType()), new ChunkGeneratorTwilight(forestChunkGen, access.registryOrThrow(Registry.STRUCTURE_SET_REGISTRY), noiseGenSettings, true, true, Optional.of(12), true), true), Lifecycle.experimental());
		return writableregistry;
	}

	private Registry<LevelStem> registerSkylightSettings(RegistryAccess access) {
		WritableRegistry<LevelStem> writableregistry = new MappedRegistry<>(Registry.LEVEL_STEM_REGISTRY, Lifecycle.experimental(), null);
		Registry<Biome> biomeRegistry = new MappedRegistry<>(Registry.BIOME_REGISTRY, Lifecycle.experimental(), null);
		Holder<NoiseGeneratorSettings> noiseGenSettings = access.registryOrThrow(Registry.NOISE_GENERATOR_SETTINGS_REGISTRY).getOrCreateHolder(TFNoiseGenerationSettings.SKYLIGHT_NOISE_GEN.getKey());

		NoiseBasedChunkGenerator forestChunkGen =
				new NoiseBasedChunkGenerator(
						access.registryOrThrow(Registry.STRUCTURE_SET_REGISTRY),
						access.registryOrThrow(Registry.NOISE_REGISTRY),
						new TFBiomeProvider(0L, biomeRegistry, makeBiomeList(biomeRegistry), 0.0F, 1.0F),
						4L, //drull had it set like this so its staying until he changes it
						noiseGenSettings
				);

		writableregistry.register(ResourceKey.create(Registry.LEVEL_STEM_REGISTRY, TwilightForestMod.prefix("skylight_forest")), new LevelStem(Holder.direct(this.twilightDimType()), new ChunkGeneratorTwilight(forestChunkGen, access.registryOrThrow(Registry.STRUCTURE_SET_REGISTRY), noiseGenSettings, true, true, Optional.of(12), true)), Lifecycle.stable());
		return writableregistry;
	}

	private static <E, T extends Registry<E>> void dumpRegistry(Path path, HashCache cache, DynamicOps<JsonElement> ops, ResourceKey<? extends T> key, T registry, Encoder<E> encoder) {
		for (Map.Entry<ResourceKey<E>, E> entry : registry.entrySet()) {
			if (entry.getKey().location().getNamespace().equals(TwilightForestMod.ID)) {
				LOGGER.info("\t\t{}", entry.getKey().location().getPath());
				Path otherPath = createPath(path, key.location(), entry.getKey().location());
				dumpValue(otherPath, cache, ops, encoder, entry.getValue());
			}
		}

	}

	private static <E> void dumpValue(Path path, HashCache cache, DynamicOps<JsonElement> ops, Encoder<E> encoder, E entry) {
		try {
			Optional<JsonElement> optional = encoder.encodeStart(ops, entry).resultOrPartial((p_206405_) -> {
				LOGGER.error("Couldn't serialize element {}: {}", path, p_206405_);
			});
			if (optional.isPresent()) {
				if (optional.get().isJsonObject()) {
					JsonObject object = optional.get().getAsJsonObject();
					if (object.has("generator") && object.get("generator").isJsonObject()) {
						JsonObject generator = object.getAsJsonObject("generator");
						if (generator.has("use_overworld_seed")) {
							generator.remove("use_overworld_seed");
							generator.addProperty("use_overworld_seed", true);
						}
						if (generator.has("wrapped_generator")) {
							JsonObject wrapped_generator = generator.getAsJsonObject("wrapped_generator");
							if (wrapped_generator.has("biome_source"))
								wrapped_generator.getAsJsonObject("biome_source").remove("seed");
						}
					}
				}
				DataProvider.save(GSON, cache, optional.get(), path);
			}
		} catch (IOException ioexception) {
			LOGGER.error("Couldn't save element {}", path, ioexception);
		}

	}

	private static Path createPath(Path path, ResourceLocation registry, ResourceLocation entry) {
		return path.resolve("data").resolve(entry.getNamespace()).resolve(registry.getPath()).resolve(entry.getPath() + ".json");
	}

	public String getName() {
		return "Worldgen";
	}

	private DimensionType twilightDimType() {
		return DimensionType.create(
				OptionalLong.of(13000L), //fixed time TODO Kill the celestial bodies
				true, //skylight
				false, //ceiling
				false, //ultrawarm
				true, //natural
				0.125D, //coordinate scale
				false, //dragon fight
				false, //piglin safe
				true, //bed works
				true, //respawn anchor works
				false, //raids
				-32, // Minimum Y Level
				32 + 256, // Height + Min Y = Max Y
				32 + 256, // Logical Height
				BlockTags.INFINIBURN_OVERWORLD, //infiburn
				TwilightForestMod.prefix("renderer"), // DimensionRenderInfo
				0f // Wish this could be set to -0.05 since it'll make the world truly blacked out if an area is not sky-lit (see: Dark Forests) Sadly this also messes up night vision so it gets 0
		);
	}

	private Map<ResourceLocation, Biome> getBiomes() {
		return BiomeMaker
				.BIOMES
				.entrySet()
				.stream()
				.peek(registryKeyBiomeEntry -> registryKeyBiomeEntry.getValue().setRegistryName(registryKeyBiomeEntry.getKey().location()))
				.collect(Collectors.toMap(entry -> entry.getKey().location(), Map.Entry::getValue));
	}

	private List<Pair<TerrainPoint, Holder<Biome>>> makeBiomeList(Registry<Biome> registry) {
		return List.of(
				pairBiome(registry, 0.025F, 0.05F, BiomeKeys.FOREST),
				pairBiome(registry, 0.1F, 0.2F, BiomeKeys.DENSE_FOREST),
				pairBiome(registry, 0.0625F, 0.05F, BiomeKeys.FIREFLY_FOREST),
				pairBiome(registry, 0.005F, 0.005F, BiomeKeys.CLEARING),
				pairBiome(registry, 0.05F, 0.1F, BiomeKeys.OAK_SAVANNAH),
				pairBiome(registry, -0.8F, 0.0F, BiomeKeys.STREAM),
				pairBiome(registry, -0.9F, 0.1F, BiomeKeys.LAKE),

				pairBiome(registry, 0.025F, 0.05F, BiomeKeys.MUSHROOM_FOREST),
				pairBiome(registry, 0.05F, 0.05F, BiomeKeys.DENSE_MUSHROOM_FOREST),

				pairBiome(registry, 0.025F, 0.05F, BiomeKeys.ENCHANTED_FOREST),
				pairBiome(registry, 0.025F, 0.05F, BiomeKeys.SPOOKY_FOREST),

				pairBiome(registry, -0.125F, 0.15F, BiomeKeys.SWAMP),
				pairBiome(registry, 0.025F, 0.05F, BiomeKeys.FIRE_SWAMP),

				pairBiome(registry, 0.025F, 0.025F, BiomeKeys.DARK_FOREST),
				pairBiome(registry, 0.025F, 0.025F, BiomeKeys.DARK_FOREST_CENTER),

				pairBiome(registry, 0.05F, 0.15F, BiomeKeys.SNOWY_FOREST),
				pairBiome(registry, 0.025F, 0.05F, BiomeKeys.GLACIER),

				pairBiome(registry, 1.75F, 0.05F, BiomeKeys.HIGHLANDS),
				pairBiome(registry, 3.0F, 0.1F, BiomeKeys.THORNLANDS),
				pairBiome(registry, 5.25F, 0.025F, BiomeKeys.FINAL_PLATEAU)
		);
	}

	private Pair<TerrainPoint, Holder<Biome>> pairBiome(Registry<Biome> registry, float depth, float scale, ResourceKey<Biome> key) {
		return Pair.of(new TerrainPoint(depth, scale), Holder.Reference.createStandAlone(registry, key));
	}
}