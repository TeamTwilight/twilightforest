package twilightforest.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.equipment.trim.TrimMaterial;
import twilightforest.TwilightForestMod;

public class TFTrimMaterials {

	public static final ResourceKey<TrimMaterial> IRONWOOD = registerKey("ironwood");
	public static final ResourceKey<TrimMaterial> STEELEAF = registerKey("steeleaf");
	public static final ResourceKey<TrimMaterial> KNIGHTMETAL = registerKey("knightmetal");
	public static final ResourceKey<TrimMaterial> FIERY = registerKey("fiery");
	public static final ResourceKey<TrimMaterial> NAGA_SCALE = registerKey("naga_scale");
	public static final ResourceKey<TrimMaterial> CARMINITE = registerKey("carminite");

	private static ResourceKey<TrimMaterial> registerKey(String name) {
		return ResourceKey.create(Registries.TRIM_MATERIAL, TwilightForestMod.prefix(name));
	}

}
