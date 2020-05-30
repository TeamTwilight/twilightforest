package twilightforest.world;

import io.netty.buffer.Unpooled;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.biome.provider.BiomeProviderType;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.ChunkGeneratorType;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ModDimension;
import net.minecraftforge.event.world.RegisterDimensionsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import twilightforest.TFConfig;
import twilightforest.TwilightForestMod;

import java.util.function.BiFunction;

@Mod.EventBusSubscriber(modid = TwilightForestMod.ID)
public class TFDimensions {

	public static final DeferredRegister<BiomeProviderType<?, ?>> BIOME_PROVIDER_TYPES = new DeferredRegister<>(ForgeRegistries.BIOME_PROVIDER_TYPES, TwilightForestMod.ID);
	public static final DeferredRegister<ChunkGeneratorType<?, ?>> CHUNK_GENERATOR_TYPES = new DeferredRegister<>(ForgeRegistries.CHUNK_GENERATOR_TYPES, TwilightForestMod.ID);
	public static final DeferredRegister<ModDimension> MOD_DIMENSIONS = new DeferredRegister<>(ForgeRegistries.MOD_DIMENSIONS, TwilightForestMod.ID);

	public static final RegistryObject<BiomeProviderType<TFBiomeProviderSettings, TFBiomeProvider>> TF_BIOME_PROVIDER = BIOME_PROVIDER_TYPES.register(
			"tf_biome_provider", () -> new BiomeProviderType<>(TFBiomeProvider::new, TFBiomeProviderSettings::new));

	public static final RegistryObject<ChunkGeneratorType<TFWorld, ChunkGeneratorTwilightForest>> TF_CHUNK_GEN = CHUNK_GENERATOR_TYPES.register(
			"tf_chunk_gen", () -> new ChunkGeneratorType<>(ChunkGeneratorTwilightForest::new, true, TFWorld::new));
	public static final RegistryObject<ChunkGeneratorType<TFWorld, ChunkGeneratorTwilightVoid>> SKYLIGHT_GEN = CHUNK_GENERATOR_TYPES.register(
			"tf_chunk_gen_void", () -> new ChunkGeneratorType<>(ChunkGeneratorTwilightVoid::new, true, TFWorld::new));

	public static final RegistryObject<ModDimension> TWILIGHT_FOREST = MOD_DIMENSIONS.register("twilight_forest", () -> new ModDimension() {
		@Override
		public BiFunction<World, DimensionType, ? extends Dimension> getFactory() {
			return WorldProviderTwilightForest::new;
		}
	});

	public static DimensionType tf_dimType;

	//TODO: Does this actually work? This is a bunch of shambles
	public static void checkOriginDimension() {
		ResourceLocation tfDim = new ResourceLocation(TwilightForestMod.ID, "twilight_forest");
		ForgeConfigSpec.ConfigValue<String> originDim = TFConfig.COMMON_CONFIG.originDimension;
		ResourceLocation dimRL = new ResourceLocation(originDim.get());

		if (DimensionType.byName(dimRL) == null) {
			TwilightForestMod.LOGGER.warn("Detected that the configured origin dimension ID ({}) is not registered. Defaulting to the overworld.", originDim.get());
			originDim.set("minecraft:overworld");
		} else if (dimRL.equals(tfDim)) {
			TwilightForestMod.LOGGER.warn("Detected that the configured origin dimension ID ({}) is already used for the Twilight Forest. Defaulting to the overworld.", originDim.get());
			originDim.set("minecraft:overworld");
		}
	}

	@SubscribeEvent
	public static void registerModDimension(final RegisterDimensionsEvent e) {
		ResourceLocation tf = new ResourceLocation(TwilightForestMod.ID, "twilight_forest");

		if (DimensionType.byName(tf) == null) {
			tf_dimType = DimensionManager.registerDimension(tf, TWILIGHT_FOREST.get(), new PacketBuffer(Unpooled.buffer()), true);
			DimensionManager.keepLoaded(tf_dimType, false);
		} else {
			tf_dimType = DimensionType.byName(tf);
		}
	}
}
