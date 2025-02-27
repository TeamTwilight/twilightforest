package twilightforest.init;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import org.lwjgl.glfw.GLFW;
import twilightforest.TwilightForestMod;

public abstract class TFKeyBinds {
	public static class Categories {
		public static final String TRAVELLERS_GEAR = addCategoryPrefix("travellers_gear");
	}

	public static final KeyMapping ZOOM_KEY = new KeyMapping(addPrefix("zoom"), InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_Z, Categories.TRAVELLERS_GEAR);
	public static final KeyMapping RED_THREAD_VISION_KEY = new KeyMapping(addPrefix("red_thread_vision"), InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_H, Categories.TRAVELLERS_GEAR);

	@SubscribeEvent
	public static void registerKeyBindings(RegisterKeyMappingsEvent event) {
		event.register(ZOOM_KEY);
		event.register(RED_THREAD_VISION_KEY);
	}

	private static String addPrefix(String s) {
		return "key." + TwilightForestMod.ID + "." + s;
	}

	private static String addCategoryPrefix(String s) {
		return addPrefix("categories." + s);
	}
}
