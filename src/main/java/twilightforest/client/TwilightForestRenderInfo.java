package twilightforest.client;

import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.state.level.LevelRenderState;
import net.minecraft.client.renderer.state.level.SkyRenderState;
import net.minecraft.client.renderer.state.level.WeatherRenderState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.CustomSkyboxRenderer;
import net.neoforged.neoforge.client.CustomWeatherEffectRenderer;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4fc;
import twilightforest.client.renderer.TFSkyRenderer;
import twilightforest.client.renderer.TFWeatherRenderer;

public class TwilightForestRenderInfo implements CustomSkyboxRenderer, CustomWeatherEffectRenderer {

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

	@Override
	public boolean renderSky(LevelRenderState levelRenderState, SkyRenderState skyRenderState, Matrix4fc modelViewMatrix, Runnable setupFog) {
		if (this.skyRenderer == null) {
			this.skyRenderer = new TFSkyRenderer();
		}
		return skyRenderer.renderSky(levelRenderState, skyRenderState, modelViewMatrix, setupFog);
	}

	@Override
	public boolean renderSnowAndRain(LevelRenderState levelRenderState, WeatherRenderState weatherRenderState, MultiBufferSource bufferSource, Vec3 camPos) {
		return TFWeatherRenderer.renderSnowAndRain(Minecraft.getInstance().level, Minecraft.getInstance().levelRenderer.getTicks(), Minecraft.getInstance().getDeltaTracker().getGameTimeDeltaTicks(), camPos, bufferSource);
	}

	@Override
	public boolean tickRain(ClientLevel level, int ticks, Camera camera) {
		return TFWeatherRenderer.tickRain(level, ticks, camera.blockPosition());
	}
}
