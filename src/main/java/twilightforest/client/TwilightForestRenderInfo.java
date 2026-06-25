package twilightforest.client;

import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.SkyRenderer;
import net.minecraft.client.renderer.Lightmap;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.state.level.LevelRenderState;
import net.minecraft.client.renderer.state.level.SkyRenderState;
import net.minecraft.client.renderer.state.level.WeatherRenderState;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.CustomSkyboxRenderer;
import net.neoforged.neoforge.client.CustomWeatherEffectRenderer;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import twilightforest.client.renderer.TFSkyRenderer;
import twilightforest.client.renderer.TFWeatherRenderer;
import twilightforest.init.TFBiomes;

import java.util.Optional;

public class TwilightForestRenderInfo implements CustomSkyboxRenderer, CustomWeatherEffectRenderer {

	public static final TwilightForestRenderInfo INSTANCE = new TwilightForestRenderInfo();

	@Nullable
	private TFSkyRenderer skyRenderer;

	//TODO: Set via EnvironmentAttribute at the DimensionType level
//	@Nullable
//	@Override
//	public float[] getSunriseColor(float daycycle, float partialTicks) { // Fog color
//		return null;
//	}

	//TODO: Set via EnvironmentAttribute at the DimensionType level
//	@Override
//	public Vec3 getBrightnessDependentFogColor(Vec3 biomeFogColor, float daylight) { // For modifying biome fog color with daycycle
//		return biomeFogColor.multiply(daylight * 0.94F + 0.06F, (daylight * 0.94F + 0.06F), (daylight * 0.91F + 0.09F));
//	}

	public boolean isFoggyAt(int x, int y) { // true = nearFog
		Player player = Minecraft.getInstance().player;

		if (player != null) {
			Optional<ResourceKey<Biome>> biome = player.level().getBiome(player.blockPosition()).unwrapKey();
			if (biome.isPresent()) {
				boolean spooky = biome.get() == TFBiomes.SPOOKY_FOREST;

				if (player.position().y > 20 && !spooky) {
					return false; // If player is above the dark forest then no need to make it so spooky. The darkwood leaves cover everything as low as y42.
				}

				return spooky || biome.get() == TFBiomes.DARK_FOREST || biome.get() == TFBiomes.DARK_FOREST_CENTER;
			}
		}

		return false;

		//Make the fog on these biomes much much darker, maybe pitch black even. Do we keep this harsher fog underground too?
	}

	@Override
	public boolean renderSky(LevelRenderState levelRenderState, SkyRenderState skyRenderState, Matrix4fc modelViewMatrix, Runnable setupFog) {
		if (this.skyRenderer == null) {
			this.skyRenderer = new TFSkyRenderer();
		}
		return skyRenderer.renderSky(levelRenderState, skyRenderState, modelViewMatrix, setupFog);
	}

	@Override
	public boolean renderSnowAndRain(LevelRenderState levelRenderState, WeatherRenderState weatherRenderState, MultiBufferSource bufferSource, Vec3 camPos) {
		Minecraft mc = Minecraft.getInstance();
		return TFWeatherRenderer.renderSnowAndRain(mc.level, (int) mc.level.getGameTime(), mc.getDeltaTracker().getGameTimeDeltaPartialTick(false), camPos);
	}

	@Override
	public boolean tickRain(ClientLevel level, int ticks, Camera camera) {
		return TFWeatherRenderer.tickRain(level, ticks, camera.blockPosition());
	}
}
