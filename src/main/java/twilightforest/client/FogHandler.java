package twilightforest.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.level.material.FogType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.ViewportEvent;
import net.neoforged.neoforge.event.level.LevelEvent;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFBiomes;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber(modid = TwilightForestMod.ID, value = Dist.CLIENT)
public class FogHandler {

	private static final float[] spoopColors = new float[3];
	private static float spoopColor = 0F;

	@SubscribeEvent
	public static void fogColors(ViewportEvent.ComputeFogColor event) {
		boolean flag = isSpooky(Minecraft.getInstance().level, Minecraft.getInstance().player);
		if (flag || spoopColor > 0F) {
			final float[] realColors = {event.getRed(), event.getGreen(), event.getBlue()};
			final float[] lerpColors = {130F / 255F, 115F / 255F, 145F / 255F};
			for (int i = 0; i < 3; i++) {
				final float real = realColors[i];
				final float spoop = lerpColors[i];
				final boolean inverse = real > spoop;
				spoopColors[i] = real == spoop ? spoop : Mth.clampedLerp(inverse ? spoop : real, inverse ? real : spoop, spoopColor);
			}
			float shift = (float) (0.01F * event.getPartialTick());
			if (flag)
				spoopColor += shift;
			else
				spoopColor -= shift;
			spoopColor = Mth.clamp(spoopColor, 0F, 1F);
			event.setRed(spoopColors[0]);
			event.setGreen(spoopColors[1]);
			event.setBlue(spoopColors[2]);
		}
	}

	private static boolean SKY_CHUNK_LOADED = false;

	private static float SKY_FAR = 0.0F;
	private static float SKY_NEAR = 0.0F;

	private static boolean TERRAIN_CHUNK_LOADED = false;

	private static float TERRAIN_FAR = 0.0F;
	private static float TERRAIN_NEAR = 0.0F;


	@SubscribeEvent
	public static void renderFog(ViewportEvent.RenderFog event) {
		if (event.getType().equals(FogType.NONE) && Minecraft.getInstance().cameraEntity instanceof LocalPlayer player && player.level() instanceof ClientLevel clientLevel && clientLevel.effects() instanceof TwilightForestRenderInfo) {
			if (event.getMode().equals(FogRenderer.FogMode.FOG_SKY)) {
				if (SKY_CHUNK_LOADED) {
					event.setCanceled(true);
					boolean spooky = isSpooky(clientLevel, player);

					float far = spooky ? event.getFarPlaneDistance() * 0.5F : event.getFarPlaneDistance();
					float near = spooky ? 0.0F : event.getNearPlaneDistance();

					SKY_FAR = Mth.lerp(0.003F, SKY_FAR, far);
					SKY_NEAR = Mth.lerp(0.003F * (SKY_NEAR < near ? 0.5F : 2.0F), SKY_NEAR, near);

					event.setFarPlaneDistance(SKY_FAR);
					event.setNearPlaneDistance(SKY_NEAR);
				} else if (clientLevel.isLoaded(player.blockPosition())) { //We do a first-time set up after the chunk the player is in is loaded
					SKY_CHUNK_LOADED = true;
					SKY_FAR = isSpooky(clientLevel, player) ? event.getFarPlaneDistance() * 0.5F : event.getFarPlaneDistance();
					SKY_NEAR = isSpooky(clientLevel, player) ? 0.0F : event.getNearPlaneDistance();
				}
			} else {
				if (TERRAIN_CHUNK_LOADED) {
					event.setCanceled(true);
					boolean spooky = isSpooky(clientLevel, player);

					float far = spooky ? event.getFarPlaneDistance() * 0.5F : event.getFarPlaneDistance();
					float near = spooky ? far * 0.75F : event.getNearPlaneDistance();

					TERRAIN_FAR = Mth.lerp(0.003F, TERRAIN_FAR, far);
					TERRAIN_NEAR = Mth.lerp(0.003F * (TERRAIN_NEAR < near ? 0.5F : 2.0F), TERRAIN_NEAR, near);

					event.setFarPlaneDistance(TERRAIN_FAR);
					event.setNearPlaneDistance(TERRAIN_NEAR);
				} else if (SKY_CHUNK_LOADED || clientLevel.isLoaded(player.blockPosition())) { //SKY is always called first in vanilla, so we only need to check if the SKY flag is true, but just in case
					TERRAIN_CHUNK_LOADED = true;
					TERRAIN_FAR = isSpooky(clientLevel, player) ? event.getFarPlaneDistance() * 0.5F : event.getFarPlaneDistance();
					TERRAIN_NEAR = isSpooky(clientLevel, player) ? TERRAIN_FAR * 0.75F : event.getNearPlaneDistance();
				}
			}
		}
	}

	@SubscribeEvent
	public static void onUnload(LevelEvent.Unload event) { //As supernatural as the fog is, it shouldn't follow the player between worlds
		SKY_CHUNK_LOADED = false;
		TERRAIN_CHUNK_LOADED = false;
	}

	private static boolean isSpooky(@Nullable ClientLevel level, @Nullable LocalPlayer player) {
		return level != null && player != null && level.getBiome(player.blockPosition()).is(TFBiomes.SPOOKY_FOREST);
	}
}
