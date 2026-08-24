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
import net.minecraft.resources.Identifier;
import twilightforest.TwilightForestMod;

import java.util.Optional;

public class TFRenderPipelines {

	private static final BlendFunction SHADOW = new BlendFunction(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);
	private static final DepthStencilState TRANSLUCENT_DEPTH = new DepthStencilState(CompareOp.GREATER_THAN_OR_EQUAL, false);

	public static final RenderPipeline RED_THREAD = RenderPipeline.builder(RenderPipelines.MATRICES_FOG_SNIPPET)
		.withLocation(TwilightForestMod.prefix("core/red_thread/red_thread"))
		.withSampler("Sampler0")
		.withVertexShader(TwilightForestMod.prefix("core/red_thread/red_thread"))
		.withFragmentShader(TwilightForestMod.prefix("core/red_thread/red_thread"))
		.withVertexFormat(DefaultVertexFormat.BLOCK, VertexFormat.Mode.QUADS)
		.withCull(true)
		.withDepthStencilState(Optional.of(new DepthStencilState(CompareOp.ALWAYS_PASS, false)))
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

	public static final RenderPipeline AURORA_PIPELINE = RenderPipeline.builder(RenderPipelines.DEBUG_FILLED_SNIPPET)
		.withLocation(Identifier.fromNamespaceAndPath("twilightforest", "aurora/aurora"))
		.withVertexFormat(DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.QUADS)
		.withDepthStencilState(Optional.of(TRANSLUCENT_DEPTH))
		.build();
}
