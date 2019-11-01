package twilightforest.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.lwjgl.opengl.GLContext;
import twilightforest.TwilightForestMod;
import twilightforest.biomes.TFBiomes;

@Mod.EventBusSubscriber(modid = TwilightForestMod.ID, value = Side.CLIENT)
public class FogHandler {

	private static final float[] spoopColors = new float[3];
	private static float spoopColor = 0F;
	private static float spoopFog = 1F;

	@SubscribeEvent
	public static void fogColors(EntityViewRenderEvent.FogColors event) {
		boolean flag = isSpooky();
		if (flag || spoopColor > 0F) {
			final float[] realColors = {event.getRed(), event.getGreen(), event.getBlue()};
			final float[] lerpColors = {106F / 255F, 60F / 255F, 153F / 255F};
			for (int i = 0; i < 3; i++) {
				final float real = realColors[i];
				final float spoop = lerpColors[i];
				final boolean inverse = real > spoop;
				spoopColors[i] = real == spoop ? spoop : (float) MathHelper.clampedLerp(inverse ? spoop : real, inverse ? real : spoop, spoopColor);
			}
			float shift = (float) (0.01F * event.getRenderPartialTicks());
			if (flag)
				spoopColor += shift;
			else
				spoopColor -= shift;
			spoopColor = MathHelper.clamp(spoopColor, 0F, 1F);
			event.setRed(spoopColors[0]);
			event.setGreen(spoopColors[1]);
			event.setBlue(spoopColors[2]);
		}
	}

	@SubscribeEvent
	public static void fog(EntityViewRenderEvent.RenderFogEvent event) {
		boolean flag = isSpooky();
		if (flag || spoopFog < 1F) {
			float f = 48F;
			f = f >= event.getFarPlaneDistance() ? event.getFarPlaneDistance() : (float) MathHelper.clampedLerp(f, event.getFarPlaneDistance(), spoopFog);
			float shift = (float) (0.001F * event.getRenderPartialTicks());
			if (flag)
				spoopFog -= shift;
			else
				spoopFog += shift;
			spoopFog = MathHelper.clamp(spoopFog, 0F, 1F);

			GlStateManager.setFog(GlStateManager.FogMode.LINEAR);

			if (event.getFogMode() == -1) {
				GlStateManager.setFogStart(0.0F);
				GlStateManager.setFogEnd(f);
			} else {
				GlStateManager.setFogStart(f * 0.75F);
				GlStateManager.setFogEnd(f);
			}

			if (GLContext.getCapabilities().GL_NV_fog_distance) {
				GlStateManager.glFogi(0x855a, 0x855b);
			}
		}
	}

	private static boolean isSpooky() {
		return Minecraft.getMinecraft().world != null && Minecraft.getMinecraft().player != null && Minecraft.getMinecraft().world.getBiome(Minecraft.getMinecraft().player.getPosition()) == TFBiomes.spookyForest;
	}

}
