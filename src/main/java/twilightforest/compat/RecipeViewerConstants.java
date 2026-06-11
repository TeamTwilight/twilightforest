package twilightforest.compat;

import net.minecraft.network.chat.Component;

public class RecipeViewerConstants {
	public static Component getDryingTime(int dryingTicks) {
		int dryingMinutes = dryingTicks / 60 / 20;
		int dryingSeconds = (dryingTicks / 20) % 60;
		Component time;

		if (dryingMinutes > 0) {
			if (dryingSeconds > 0) {
				time = Component.translatable("gui.twilightforest.drying_time", dryingMinutes, dryingSeconds);
			} else {
				if (dryingMinutes == 1) {
					time = Component.translatable("gui.twilightforest.drying_minute", dryingMinutes);
				} else {
					time = Component.translatable("gui.twilightforest.drying_minutes", dryingMinutes);
				}
			}
		} else {
			if (dryingSeconds == 1) {
				time = Component.translatable("gui.twilightforest.drying_second", dryingSeconds);
			} else {
				time = Component.translatable("gui.twilightforest.drying_seconds", dryingSeconds);
			}
		}
		return time;
	}
}
