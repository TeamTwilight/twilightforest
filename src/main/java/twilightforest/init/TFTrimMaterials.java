package twilightforest.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.equipment.trim.TrimMaterial;
import twilightforest.TwilightForestMod;

public class TFTrimMaterials {

	public static final ResourceKey<TrimMaterial> IRONWOOD = createKey("ironwood");
	public static final ResourceKey<TrimMaterial> STEELEAF = createKey("steeleaf");
	public static final ResourceKey<TrimMaterial> KNIGHTMETAL = createKey("knightmetal");
	public static final ResourceKey<TrimMaterial> FIERY = createKey("fiery");
	public static final ResourceKey<TrimMaterial> NAGA_SCALE = createKey("naga_scale");
	public static final ResourceKey<TrimMaterial> CARMINITE = createKey("carminite");

	public static final Identifier IRONWOOD_PALETTE = createPaletteId("ironwood");
	public static final Identifier STEELEAF_PALETTE = createPaletteId("steeleaf");
	public static final Identifier KNIGHTMETAL_PALETTE = createPaletteId("knightmetal");
	public static final Identifier FIERY_PALETTE = createPaletteId("fiery");
	public static final Identifier NAGA_SCALE_PALETTE = createPaletteId("naga_scale");
	public static final Identifier CARMINITE_PALETTE = createPaletteId("carminite");

	private static ResourceKey<TrimMaterial> createKey(String name) {
		return ResourceKey.create(Registries.TRIM_MATERIAL, TwilightForestMod.prefix(name));
	}

	private static Identifier createPaletteId(String name) {
		return TwilightForestMod.prefix("trim/" + name);
	}
}
