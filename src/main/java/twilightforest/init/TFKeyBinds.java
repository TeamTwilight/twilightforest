package twilightforest.init;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import org.lwjgl.glfw.GLFW;
import twilightforest.TwilightForestMod;

import java.util.HashSet;
import java.util.Set;

public abstract class TFKeyBinds {
	public static final Set<KeyMapping> KEY_MAPPINGS = new HashSet<>();

	public static final KeyMapping RED_THREAD_VISION_KEY = register("red_thread_vision", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_UNKNOWN, TFKeyBindsCategories.TRAVELLERS_GEAR);
	public static final KeyMapping ITEM_DISPLAY_KEY = register("item_display", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_UNKNOWN, TFKeyBindsCategories.TRAVELLERS_GEAR);
	public static final KeyMapping ZOOM_KEY = register("zoom", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_Z, TFKeyBindsCategories.TRAVELLERS_GEAR);
	public static final KeyMapping SWAP_HOTBAR_KEY = register("swap_hotbar", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_V, TFKeyBindsCategories.TRAVELLERS_GEAR);

	@SubscribeEvent
	public static void registerKeyBindings(RegisterKeyMappingsEvent event) {
		KEY_MAPPINGS.forEach(event::register);
	}

	@SuppressWarnings("SameParameterValue")
	private static KeyMapping register(String name, InputConstants.Type type, int key, TFKeyBindsCategories.Category category) {
		KeyMapping keyMapping = new KeyMapping(addPrefix(name), type, key, category.internalName());
		KEY_MAPPINGS.add(keyMapping);
		return keyMapping;
	}

	private static String addPrefix(String s) {
		return "key." + TwilightForestMod.ID + "." + s;
	}
}
