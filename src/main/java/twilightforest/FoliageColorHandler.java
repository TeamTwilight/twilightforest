package twilightforest;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.FoliageColor;
import net.minecraft.world.level.biome.Biome;
import twilightforest.world.components.BiomeGrassColors;
import twilightforest.world.registration.biomes.BiomeKeys;

import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

public final class FoliageColorHandler {

	private static final Map<Biome, Handler> HANDLES = new WeakHashMap<>();

	public static int get(int o, Biome biome, double x, double z) {
		Handler handler = HANDLES.get(biome);
		if (handler == null) {
			handler = Handler.REGISTRY.getOrDefault(biome.getRegistryName(), Handler.DEFAULT);
			HANDLES.put(biome, handler);
		}
		return handler.apply(o, x, z);
	}

	@FunctionalInterface
	private interface Handler {

		Map<ResourceLocation, Handler> REGISTRY = new HashMap<>() {{
			put(BiomeKeys.SPOOKY_FOREST.location(), (o, x, z) -> {
				double noise = (Biome.BIOME_INFO_NOISE.getValue(x * 0.0225D, z * 0.0225D, false) + 1D) / 2D;
				return BiomeGrassColors.blendColors(0xFF8501, 0xF7FF01, noise > 0.6D ? noise * 0.2D : noise);
			});
			put(BiomeKeys.ENCHANTED_FOREST.location(), (o, x, z) -> (o & 0xFFFF00) + BiomeGrassColors.getEnchantedColor((int) x, (int) z));
			put(BiomeKeys.DARK_FOREST_CENTER.location(), (o, x, z) -> {
				double noise = (Biome.BIOME_INFO_NOISE.getValue(x * 0.0225D, z * 0.0225D, false) + 1D) / 2D;
				return noise < -0.1D ? 0xF9821E : 0xE94E14;
			});
			put(BiomeKeys.DARK_FOREST.location(), (o, x, z) -> ((FoliageColor.get(0.7F, 0.8F) & 0xFEFEFE) + 0x1E0E4E) / 2);
			put(BiomeKeys.SWAMP.location(), (o, x, z) -> ((FoliageColor.get(0.8F, 0.9F) & 0xFEFEFE) + 0x4E0E4E) / 2);
		}};

		Handler DEFAULT = (o, x, z) -> o;

		int apply(int o, double x, double z);


	}

}
