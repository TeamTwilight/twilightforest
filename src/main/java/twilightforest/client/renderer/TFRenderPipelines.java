package twilightforest.client.renderer;

import com.mojang.blaze3d.PrimitiveTopology;
import com.mojang.blaze3d.pipeline.*;
import com.mojang.blaze3d.platform.BlendFactor;
import com.mojang.blaze3d.platform.CompareOp;
import com.mojang.blaze3d.shaders.UniformType;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.minecraft.client.renderer.RenderPipelines;
import twilightforest.TwilightForestMod;

import java.util.Optional;

public class TFRenderPipelines {

	public static final String AURORA_UNIFORM = "TwilightForestAurora";

	private static final BlendFunction SHADOW = new BlendFunction(BlendFactor.SRC_ALPHA, BlendFactor.ONE_MINUS_SRC_ALPHA);

	public static final RenderPipeline AURORA = RenderPipeline.builder(RenderPipelines.MATRICES_FOG_SNIPPET, RenderPipelines.GLOBALS_SNIPPET)
		.withLocation(TwilightForestMod.prefix("pipeline/aurora"))
		.withVertexShader(TwilightForestMod.prefix("core/aurora/aurora"))
		.withFragmentShader(TwilightForestMod.prefix("core/aurora/aurora"))
		.withColorTargetState(new ColorTargetState(BlendFunction.TRANSLUCENT))
		.withVertexBinding(0, DefaultVertexFormat.POSITION_COLOR)
		.withPrimitiveTopology(PrimitiveTopology.QUADS)
		.withDepthStencilState(new DepthStencilState(CompareOp.LESS_THAN_OR_EQUAL, false))
		.withBindGroupLayout(BindGroupLayout.builder().withUniform(AURORA_UNIFORM, UniformType.UNIFORM_BUFFER).build())
		.build();

	public static final RenderPipeline RED_THREAD = RenderPipeline.builder(RenderPipelines.MATRICES_FOG_SNIPPET)
		.withLocation(TwilightForestMod.prefix("core/red_thread/red_thread"))
		.withVertexShader("core/block")
		.withFragmentShader("core/block")
		.withColorTargetState(new ColorTargetState(BlendFunction.ADDITIVE))
		.withVertexBinding(0, DefaultVertexFormat.BLOCK)
		.withPrimitiveTopology(PrimitiveTopology.QUADS)
		.withCull(true)
		.withDepthStencilState(Optional.of(new DepthStencilState(CompareOp.ALWAYS_PASS, false)))
		.withBindGroupLayout(BindGroupLayout.builder().withSampler("Sampler0").withSampler("Sampler2").build())
		.build();

	public static final RenderPipeline PROTECTION_BOX = RenderPipeline.builder(RenderPipelines.MATRICES_FOG_SNIPPET)
		.withLocation("pipeline/energy_swirl")
		.withVertexShader("core/entity")
		.withFragmentShader("core/entity")
		.withShaderDefine("ALPHA_CUTOUT", 0.1F)
		.withShaderDefine("EMISSIVE")
		.withShaderDefine("NO_OVERLAY")
		.withShaderDefine("NO_CARDINAL_LIGHTING")
		.withShaderDefine("APPLY_TEXTURE_MATRIX")
		.withColorTargetState(new ColorTargetState(BlendFunction.TRANSLUCENT))
		.withCull(false)
		.withVertexBinding(0, DefaultVertexFormat.ENTITY)
		.withPrimitiveTopology(PrimitiveTopology.QUADS)
		.withDepthStencilState(DepthStencilState.DEFAULT)
		.withBindGroupLayout(BindGroupLayout.builder().withSampler("Sampler0").build())
		.build();

	public static final RenderPipeline SHADOW_CLONE = RenderPipeline.builder(RenderPipelines.ENTITY_SNIPPET)
		.withLocation("pipeline/entity_translucent_cull")
		.withShaderDefine("ALPHA_CUTOUT", 0.1F)
		.withCull(false)
		.withColorTargetState(new ColorTargetState(SHADOW))
		.withBindGroupLayout(BindGroupLayout.builder().withSampler("Sampler1").build())
		.build();
}
