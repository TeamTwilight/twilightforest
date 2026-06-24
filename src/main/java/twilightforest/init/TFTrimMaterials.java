package twilightforest.init;

import net.minecraft.util.Util;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.equipment.trim.MaterialAssetGroup;
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

	public static void bootstrap(BootstrapContext<TrimMaterial> context) {
		register(context, IRONWOOD, TFMaterialAssetGroup.IRONWOOD, Style.EMPTY.withColor(7037281));
		register(context, STEELEAF, TFMaterialAssetGroup.STEELEAF, Style.EMPTY.withColor(4814643));
		register(context, KNIGHTMETAL, TFMaterialAssetGroup.KNIGHTMETAL, Style.EMPTY.withColor(8424562));
		register(context, FIERY, TFMaterialAssetGroup.FIERY, Style.EMPTY.withColor(16758076));
		register(context, NAGA_SCALE, TFMaterialAssetGroup.NAGA_SCALE, Style.EMPTY.withColor(2381586));
		register(context, CARMINITE, TFMaterialAssetGroup.CARMINITE, Style.EMPTY.withColor(10092544));
	}

	private static void register(BootstrapContext<TrimMaterial> context, ResourceKey<TrimMaterial> trimKey, MaterialAssetGroup group, Style color) {
		TrimMaterial material = new TrimMaterial(group, Component.translatable(Util.makeDescriptionId("trim_material", trimKey.identifier())).withStyle(color));
		context.register(trimKey, material);
	}
}
