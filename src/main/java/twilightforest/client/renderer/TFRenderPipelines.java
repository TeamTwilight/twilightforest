package twilightforest.client.renderer;

import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.ColorTargetState;
import com.mojang.blaze3d.pipeline.DepthStencilState;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.CompareOp;
import com.mojang.blaze3d.platform.DestFactor;
import com.mojang.blaze3d.platform.SourceFactor;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderPipelines;
import twilightforest.TwilightForestMod;

public class TFRenderPipelines {

	private static final BlendFunction SHADOW = new BlendFunction(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);

	public static final RenderPipeline RED_THREAD = RenderPipeline.builder(RenderPipelines.MATRICES_FOG_SNIPPET)
		.withLocation(TwilightForestMod.prefix("pipeline/red_thread"))
		.withVertexShader("core/block")
		.withFragmentShader("core/block")
		.withSampler("Sampler0")
		.withVertexFormat(DefaultVertexFormat.BLOCK, VertexFormat.Mode.QUADS)
		.withCull(true)
		.withDepthStencilState(new DepthStencilState(CompareOp.ALWAYS_PASS, false))
		.build();

	public static final RenderPipeline PROTECTION_BOX = RenderPipeline.builder(RenderPipelines.MATRICES_FOG_SNIPPET)
		.withLocation("pipeline/energy_swirl")
		.withVertexShader("core/entity")
		.withFragmentShader("core/entity")
		.withShaderDefine("ALPHA_CUTOUT", 0.1F)
		.withShaderDefine("EMISSIVE")
		.withShaderDefine("NO_CARDINAL_LIGHTING")
		.withShaderDefine("APPLY_TEXTURE_MATRIX")
		.withSampler("Sampler0")
		.withColorTargetState(new ColorTargetState(BlendFunction.TRANSLUCENT))
		.withCull(false)
		.withVertexFormat(DefaultVertexFormat.ENTITY, VertexFormat.Mode.QUADS)
		.withDepthStencilState(DepthStencilState.DEFAULT)
		.build();

	public static final RenderPipeline SHADOW_CLONE = RenderPipeline.builder(RenderPipelines.ENTITY_SNIPPET)
		.withLocation("pipeline/entity_translucent_cull")
		.withShaderDefine("ALPHA_CUTOUT", 0.1F)
		.withSampler("Sampler1")
		.withCull(false)
		.withColorTargetState(new ColorTargetState(SHADOW))
		.build();
}
