package twilightforest.init;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import twilightforest.TwilightForestMod;

import java.util.HashSet;
import java.util.Set;

public abstract class TFKeyBinds {
	public static final Set<KeyMapping> KEY_MAPPINGS = new HashSet<>();

	public static final KeyMapping RED_THREAD_VISION_KEY = register("red_thread_vision", InputConstants.UNKNOWN.getValue(), TFKeyBindsCategories.TRAVELLERS_GEAR);
	public static final KeyMapping ITEM_DISPLAY_MAP_CYCLE_KEY = register("item_display_map_cycle", InputConstants.KEY_C, TFKeyBindsCategories.TRAVELLERS_GEAR);
	public static final KeyMapping ZOOM_KEY = register("zoom", InputConstants.KEY_Z, TFKeyBindsCategories.TRAVELLERS_GEAR);
	public static final KeyMapping SWAP_HOTBAR_KEY = register("swap_hotbar", InputConstants.KEY_V, TFKeyBindsCategories.TRAVELLERS_GEAR);

	@SuppressWarnings("SameParameterValue")
	private static KeyMapping register(String name, int key, KeyMapping.Category category) {
		KeyMapping keyMapping = new KeyMapping(addPrefix(name), key, category);
		KEY_MAPPINGS.add(keyMapping);
		return keyMapping;
	}

	private static String addPrefix(String s) {
		return "key." + TwilightForestMod.ID + "." + s;
	}
}
