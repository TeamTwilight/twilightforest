package twilightforest.client;

import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderType;
import twilightforest.client.renderer.TFRenderPipelines;

public class TFShaders {

	public static final RenderType AURORA = RenderType.create(
		"twilightforest_aurora",
		RenderSetup.builder(TFRenderPipelines.AURORA_PIPELINE).createRenderSetup()
	);

	public static final RenderType RED_THREAD = RenderType.create(
		"twilightforest_red_thread",
		RenderSetup.builder(TFRenderPipelines.RED_THREAD).createRenderSetup()
	);
}
