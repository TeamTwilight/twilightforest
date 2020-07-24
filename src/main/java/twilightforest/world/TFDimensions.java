package twilightforest.world;

import net.minecraft.util.RegistryKey;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod;
import twilightforest.TwilightForestMod;

@Mod.EventBusSubscriber(modid = TwilightForestMod.ID)
public class TFDimensions {

//	public static final DeferredRegister<BiomeProviderType<?, ?>> BIOME_PROVIDER_TYPES = new DeferredRegister<>(ForgeRegistries.BIOME_PROVIDER_TYPES, TwilightForestMod.ID);
//	public static final DeferredRegister<ChunkGeneratorType<?, ?>> CHUNK_GENERATOR_TYPES = new DeferredRegister<>(ForgeRegistries.CHUNK_GENERATOR_TYPES, TwilightForestMod.ID);
//	public static final DeferredRegister<ModDimension> MOD_DIMENSIONS = new DeferredRegister<>(ForgeRegistries.MOD_DIMENSIONS, TwilightForestMod.ID);

//	public static final RegistryObject<BiomeProviderType<TFBiomeProviderSettings, TFBiomeProvider>> TF_BIOME_PROVIDER = BIOME_PROVIDER_TYPES.register(
//			"tf_biome_provider", () -> new BiomeProviderType<>(TFBiomeProvider::new, TFBiomeProviderSettings::new));

//	public static final RegistryObject<ChunkGeneratorType<TFGenerationSettings, ChunkGeneratorTwilightForest>> TF_CHUNK_GEN = CHUNK_GENERATOR_TYPES.register(
//			"tf_chunk_gen", () -> new ChunkGeneratorType<>(ChunkGeneratorTwilightForest::new, true, TFGenerationSettings::new));
//	public static final RegistryObject<ChunkGeneratorType<TFGenerationSettings, ChunkGeneratorTwilightVoid>> SKYLIGHT_GEN = CHUNK_GENERATOR_TYPES.register(
//			"tf_chunk_gen_void", () -> new ChunkGeneratorType<>(ChunkGeneratorTwilightVoid::new, true, TFGenerationSettings::new));

//	private static final RegistryObject<ModDimension> MOD_DIMENSION = MOD_DIMENSIONS.register("twilight_forest", () -> ModDimension.withFactory(TwilightForestDimension::new));

	public static DimensionType twilight_forest_dimension; //TODO: Add value
	public static final RegistryKey<DimensionType> twilight_forest = RegistryKey.func_240903_a_(Registry.DIMENSION_TYPE_KEY, TwilightForestMod.prefix("twilight_forest"));
	public static final RegistryKey<World> twilight_forest_world = RegistryKey.func_240903_a_(Registry.WORLD_KEY, TwilightForestMod.prefix("twilight_forest"));

	//TODO: Do we even need this anymore? Unless someone uses our exact mod id and registry name, this shouldn't be a problem
//	public static void checkOriginDimension() {
//		ResourceLocation tfDim = new ResourceLocation(TwilightForestMod.ID, "twilight_forest");
//		ForgeConfigSpec.ConfigValue<String> originDim = TFConfig.COMMON_CONFIG.originDimension;
//		ResourceLocation dimRL = new ResourceLocation(originDim.get());
//
//		if (DimensionType.byName(dimRL) == null) {
//			TwilightForestMod.LOGGER.warn("Detected that the configured origin dimension ID ({}) is not registered. Defaulting to the overworld.", originDim.get());
//			originDim.set("minecraft:overworld");
//		} else if (dimRL.equals(tfDim)) {
//			TwilightForestMod.LOGGER.warn("Detected that the configured origin dimension ID ({}) is already used for the Twilight Forest. Defaulting to the overworld.", originDim.get());
//			originDim.set("minecraft:overworld");
//		}
//	}

	public static void init() {
		Registry.register(Registry.BIOME_PROVIDER_CODEC, TwilightForestMod.prefix("twilight_forest"), TFBiomeProvider.tfBiomeProviderCodec);
		Registry.register(Registry.CHUNK_GENERATOR_CODEC, TwilightForestMod.prefix("twilight_forest"), ChunkGeneratorTwilightForest.codecTFChunk);
		Registry.register(Registry.CHUNK_GENERATOR_CODEC, TwilightForestMod.prefix("skylight_forest"), ChunkGeneratorTwilightVoid.codecVoidChunk);
	}
}
