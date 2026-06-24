package twilightforest.client;

import com.mojang.blaze3d.pipeline.DepthStencilState;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.CompareOp;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.resources.Identifier;
import twilightforest.client.renderer.TFRenderPipelines;

import java.util.Optional;

public class TFShaders {
	private static final DepthStencilState TRANSLUCENT_DEPTH = new DepthStencilState(CompareOp.GREATER_THAN_OR_EQUAL, false);

	private static final RenderPipeline AURORA_PIPELINE = RenderPipeline.builder(RenderPipelines.DEBUG_FILLED_SNIPPET)
		.withLocation(Identifier.fromNamespaceAndPath("twilightforest", "aurora/aurora"))
		.withVertexFormat(DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.QUADS)
		.withDepthStencilState(Optional.of(TRANSLUCENT_DEPTH))
		.build();

	public static final RenderType AURORA = RenderType.create(
		"twilightforest_aurora",
		RenderSetup.builder(AURORA_PIPELINE).createRenderSetup()
	);

	public static final RenderType RED_THREAD = RenderType.create(
		"twilightforest_red_thread",
		RenderSetup.builder(TFRenderPipelines.RED_THREAD).createRenderSetup()
	);
}
