package twilightforest.client;

import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.state.level.LevelRenderState;
import net.minecraft.client.renderer.state.level.SkyRenderState;
import net.minecraft.client.renderer.state.level.WeatherRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.CustomSkyboxRenderer;
import net.neoforged.neoforge.client.CustomWeatherEffectRenderer;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4fc;
import twilightforest.TwilightForestMod;
import twilightforest.client.renderer.TFSkyRenderer;
import twilightforest.client.renderer.TFWeatherRenderer;

public class TwilightForestRenderInfo implements CustomSkyboxRenderer, CustomWeatherEffectRenderer {

	public static final Identifier SKY_RENDERER = TwilightForestMod.prefix("renderer");
	public static final Identifier WEATHER_RENDERER = TwilightForestMod.prefix("weather");

	@Nullable
	private TFSkyRenderer skyRenderer;

	@Override
	public boolean renderSky(LevelRenderState levelRenderState, SkyRenderState skyRenderState, Matrix4fc modelViewMatrix, Runnable setupFog) {
		if (this.skyRenderer == null) {
			this.skyRenderer = new TFSkyRenderer();
		}
		return skyRenderer.renderSky(levelRenderState, skyRenderState, modelViewMatrix, setupFog);
	}

	@Override
	public boolean renderSnowAndRain(LevelRenderState levelRenderState, WeatherRenderState weatherRenderState, Vec3 camPos) {
		ClientLevel level = Minecraft.getInstance().level;
		if (level == null)
			return false;

		return TFWeatherRenderer.renderSnowAndRain(level, (int) (levelRenderState.gameTime & 131071L), Minecraft.getInstance().getDeltaTracker().getGameTimeDeltaPartialTick(false), camPos);
	}

	@Override
	public boolean tickRain(ClientLevel level, long ticks, Camera camera) {
		return TFWeatherRenderer.tickRain(level, ticks, camera.blockPosition());
	}
}
