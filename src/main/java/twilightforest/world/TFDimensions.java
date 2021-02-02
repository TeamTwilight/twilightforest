package twilightforest.world;

import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod;
import twilightforest.TFConfig;
import twilightforest.TwilightForestMod;

@Mod.EventBusSubscriber(modid = TwilightForestMod.ID)
public class TFDimensions {
	public static long seed;
	public static final RegistryKey<DimensionType> twilightForestType = RegistryKey.getOrCreateKey(Registry.DIMENSION_TYPE_KEY, TwilightForestMod.prefix(TwilightForestMod.ID));
	public static final RegistryKey<World> twilightForest = RegistryKey.getOrCreateKey(Registry.WORLD_KEY, new ResourceLocation(TFConfig.COMMON_CONFIG.DIMENSION.twilightForestID.get()));

	public static void init() {
		//Registry.register(Registry.BIOME_PROVIDER_CODEC, TwilightForestMod.prefix("biome_distributor"), TFBiomeDistributor.tfBiomeProviderCodec);
		// TODO legacy
		Registry.register(Registry.BIOME_PROVIDER_CODEC, TwilightForestMod.prefix("grid"), TFBiomeProvider.tfBiomeProviderCodec);

		// For now use minecraft:noise until we need to terraform for features
		Registry.register(Registry.CHUNK_GENERATOR_CODEC, TwilightForestMod.prefix("featured_noise"), ChunkGeneratorTwilightForest.codecTFChunk);
		// TODO Do we even need this? Or can we fold it into the featured_noise because its elasticity to handle skyworld generation
		//Registry.register(Registry.CHUNK_GENERATOR_CODEC, TwilightForestMod.prefix("sky_noise"), ChunkGeneratorTwilightVoid.codecVoidChunk);
	}
}
