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

			double time = 13000;
			double d0 = Mth.frac(time / (double) 24000.0F - (double) 0.25F);
			double d1 = (double) 0.5F - Math.cos(d0 * Math.PI) / (double) 2.0F;
			double d2 = (float) (d0 * (double) 2.0F + d1) / 3.0F;
			float daylight = Mth.clamp(Mth.cos(d2 * (float) (Math.PI * 2)) * 2.0F + 0.5F, 0.0F, 1.0F);

			if (client.getBiome(player.blockPosition()).is(TFBiomes.SPOOKY_FOREST)) {
				spookyPercent += 0.005F;
			} else {
				spookyPercent -= 0.005F;
			}
			spookyPercent = Mth.clamp(spookyPercent, 0F, 1F);

			event.setRed(Mth.clampedLerp(spookyPercent, colors[0] * daylight * 0.94F + 0.06F, colors[0]));
			event.setGreen(Mth.clampedLerp(spookyPercent, colors[1] * daylight * 0.94F + 0.06F, colors[1]));
			event.setBlue(Mth.clampedLerp(spookyPercent, colors[2] * daylight * 0.91F + 0.09F, colors[2]));
		}
	}

}
