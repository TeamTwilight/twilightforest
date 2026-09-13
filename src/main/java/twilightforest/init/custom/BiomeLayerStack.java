package twilightforest.init.custom;

import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.resources.RegistryFileCodec;
import net.minecraft.resources.ResourceKey;
import twilightforest.TFRegistries;
import twilightforest.TwilightForestMod;
import twilightforest.world.components.layer.*;
import twilightforest.world.components.layer.vanillalegacy.BiomeLayerFactory;
import twilightforest.world.components.layer.vanillalegacy.BiomeLayerType;

public class BiomeLayerStack {
	public static final Codec<BiomeLayerFactory> DISPATCH_CODEC = BiomeLayerTypes.CODEC.dispatch("layer_type", BiomeLayerFactory::getType, BiomeLayerType::getCodec);
	public static final Codec<Holder<BiomeLayerFactory>> HOLDER_CODEC = RegistryFileCodec.create(TFRegistries.Keys.BIOME_STACK, BiomeLayerStack.DISPATCH_CODEC, true);

	public static final ResourceKey<BiomeLayerFactory> RANDOM_FOREST_BIOMES = registerKey("random_forest_biomes");
	public static final ResourceKey<BiomeLayerFactory> BIOMES_ALONG_STREAMS = registerKey("biomes_along_streams");

	public static final ResourceKey<BiomeDensitySource> BIOME_GRID = ResourceKey.create(TFRegistries.Keys.BIOME_TERRAIN_DATA, TwilightForestMod.prefix("biome_grid"));

	public static ResourceKey<BiomeLayerFactory> registerKey(String name) {
		return ResourceKey.create(TFRegistries.Keys.BIOME_STACK, TwilightForestMod.prefix(name));
	}

}
