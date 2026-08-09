package twilightforest.util;

import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.rendertype.OutputTarget;
import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Util;

import java.util.function.Function;

// [VanillaCopy] this class contains removed methods from vanilla RenderType
public class RenderTypeUtil {
	private static final Function<Identifier, RenderType> WEATHER_DEPTH_WRITE = createWeather(true);
	private static final Function<Identifier, RenderType> WEATHER_NO_DEPTH_WRITE = createWeather(false);

	public static RenderType weather(Identifier texture, boolean depthWrite) {
		return (depthWrite ? WEATHER_DEPTH_WRITE : WEATHER_NO_DEPTH_WRITE).apply(texture);
	}

	private static Function<Identifier, RenderType> createWeather(boolean depthWrite) {
		return Util.memoize(
			texture -> RenderType.create(
				"weather",
				RenderSetup.builder(depthWrite ? RenderPipelines.WEATHER_DEPTH_WRITE : RenderPipelines.WEATHER_NO_DEPTH_WRITE)
					.bufferSize(RenderType.TRANSIENT_BUFFER_SIZE)
					.setOutputTarget(OutputTarget.WEATHER_TARGET)
					.withTexture("Sampler0", texture)
					.useLightmap()
					.setOutline(RenderSetup.OutlineProperty.NONE)
					.createRenderSetup()
			)
		);
	}
}
