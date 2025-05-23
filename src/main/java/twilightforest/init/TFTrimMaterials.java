package twilightforest.init;

import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.equipment.trim.TrimMaterial;
import twilightforest.TwilightForestMod;

import java.util.Map;

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
		register(context, IRONWOOD, TFItems.IRONWOOD_INGOT, Style.EMPTY.withColor(7037281));
		register(context, STEELEAF, TFItems.STEELEAF_INGOT, Style.EMPTY.withColor(4814643));
		register(context, KNIGHTMETAL, TFItems.KNIGHTMETAL_INGOT, Style.EMPTY.withColor(8424562));
		register(context, FIERY, TFItems.FIERY_INGOT, Style.EMPTY.withColor(16758076));
		register(context, NAGA_SCALE, TFItems.NAGA_SCALE, Style.EMPTY.withColor(2381586));
		register(context, CARMINITE, TFItems.CARMINITE, Style.EMPTY.withColor(10092544));
	}

	private static void register(BootstrapContext<TrimMaterial> context, ResourceKey<TrimMaterial> trimKey, Holder<Item> trimItem, Style color) {
		TrimMaterial material = new TrimMaterial(trimKey.location().getPath(), trimItem, Map.of(), Component.translatable(Util.makeDescriptionId("trim_material", trimKey.location())).withStyle(color));
		context.register(trimKey, material);
	}
}
