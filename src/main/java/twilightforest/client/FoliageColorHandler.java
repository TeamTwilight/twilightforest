package twilightforest.client;

import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import twilightforest.beans.Autowired;
import twilightforest.beans.Component;
import twilightforest.init.TFBiomes;
import twilightforest.world.components.BiomeColorAlgorithms;

import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

@Component
public final class FoliageColorHandler {

	@Autowired
	private BiomeColorAlgorithms biomeColorAlgorithms;

	private final Map<ResourceKey<Biome>, Handler> REGISTRY = new HashMap<>() {{
		put(TFBiomes.SPOOKY_FOREST, (o, x, z) -> biomeColorAlgorithms.spookyFoliage(x, z));
		put(TFBiomes.ENCHANTED_FOREST, (o, x, z) -> biomeColorAlgorithms.enchanted(o, (int) x, (int) z));
		put(TFBiomes.DARK_FOREST_CENTER, (o, x, z) -> biomeColorAlgorithms.darkForestCenterFoliage(x, z));
		put(TFBiomes.DARK_FOREST, (o, x, z) -> biomeColorAlgorithms.darkForest(BiomeColorAlgorithms.Type.Foliage));
		put(TFBiomes.SWAMP, (o, x, z) -> biomeColorAlgorithms.swamp(BiomeColorAlgorithms.Type.Foliage));
	}};

	private final Map<Biome, Handler> HANDLES = new WeakHashMap<>();

	public int get(int o, Biome biome, double x, double z) {
		Handler handler = HANDLES.get(biome);
		if (handler == null) {
			handler = REGISTRY.getOrDefault(
				Minecraft.getInstance().level == null ? null :
					Minecraft.getInstance().level.registryAccess().registryOrThrow(Registries.BIOME).getResourceKey(biome).orElse(null),
				Handler.DEFAULT);
			HANDLES.put(biome, handler);
		}
		return handler.apply(o, x, z);
	}

	@FunctionalInterface
	private interface Handler {

		Handler DEFAULT = (o, x, z) -> o;

		int apply(int o, double x, double z);

	}

}
