package twilightforest.client.event;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.neoforged.neoforge.client.event.ViewportEvent;
import twilightforest.init.TFBiomes;
import twilightforest.init.TFDimension;

public class FogHandler {

	private static float spookyPercent = 0.0F;

	protected static void colorFog(ViewportEvent.ComputeFogColor event) {
		if (event.getCamera().entity() instanceof LocalPlayer player && player.level() instanceof ClientLevel client && client.dimension() == TFDimension.DIMENSION_KEY) {
			float[] colors = new float[]{event.getRed(), event.getGreen(), event.getBlue()};
			int spooky = 0x827391; //TODO: If there is a better way to get the base biome fog colour, feel free to do so here.
			float red = ((spooky >> 16) & 0xFF) / 255.0F;
			float green = ((spooky >> 8) & 0xFF) / 255.0F;
			float blue = ((spooky >> 0) & 0xFF) / 255.0F;

			if (client.getBiome(player.blockPosition()).is(TFBiomes.SPOOKY_FOREST)) {
				spookyPercent += 0.005F;
			} else {
				spookyPercent -= 0.005F;
			}
			spookyPercent = Mth.clamp(spookyPercent, 0F, 1F);

			event.setRed(Mth.clampedLerp(spookyPercent, colors[0], red));
			event.setGreen(Mth.clampedLerp(spookyPercent, colors[1], green));
			event.setBlue(Mth.clampedLerp(spookyPercent, colors[2], blue));
		}
	}
}
