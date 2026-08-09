package twilightforest.util;

import net.minecraft.client.GraphicsPreset;
import net.minecraft.client.Minecraft;

public class MinecraftUtil {
	// [VanillaCopy] the copy of removed Minecraft.useFancyGraphics
	public static boolean useFancyGraphics() {
		return Minecraft.getInstance().options.graphicsPreset().get().ordinal() >= GraphicsPreset.FANCY.ordinal();
	}
}
