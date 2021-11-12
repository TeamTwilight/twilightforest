package twilightforest.world.registration;

import net.minecraft.core.Registry;
import net.minecraftforge.fml.common.Mod;
import twilightforest.TwilightForestMod;
import twilightforest.world.components.TFBiomeDistributor;
import twilightforest.world.components.TFBiomeProvider;
import twilightforest.world.components.chunkgenerators.ChunkGeneratorTwilight;

@Mod.EventBusSubscriber(modid = TwilightForestMod.ID)
public class TFDimensions {
	// Find a different way to validate if a world is passible as a "Twilight Forest" instead of hardcoding Dim ID (Instanceof check for example)
	//public static final RegistryKey<World> twilightForest = RegistryKey.getOrCreateKey(Registry.WORLD_KEY, new ResourceLocation(TFConfig.COMMON_CONFIG.DIMENSION.twilightForestID.get()));
	public static long seed;

	public static void init() {
		Registry.register(Registry.BIOME_SOURCE, TwilightForestMod.prefix("smart_distribution"), TFBiomeDistributor.TF_CODEC);
		// TODO legacy
		Registry.register(Registry.BIOME_SOURCE, TwilightForestMod.prefix("grid"), TFBiomeProvider.TF_CODEC);

		Registry.register(Registry.CHUNK_GENERATOR, TwilightForestMod.prefix("structure_locating_wrapper"), ChunkGeneratorTwilight.CODEC);
	}
}
