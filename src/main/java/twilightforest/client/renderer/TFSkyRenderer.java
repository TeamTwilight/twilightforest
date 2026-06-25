package twilightforest.client.renderer;

import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.GpuTextureView;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.state.level.LevelRenderState;
import net.minecraft.client.renderer.state.level.SkyRenderState;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.context.ContextKey;
import net.neoforged.neoforge.client.event.ExtractLevelRenderStateEvent;
import org.joml.*;
import twilightforest.TwilightForestMod;

import java.lang.Math;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;

public class TFSkyRenderer implements AutoCloseable {

	public static final ContextKey<Boolean> RENDER_DARK_DISC = new ContextKey<>(TwilightForestMod.prefix("render_dark_disc"));
	private final RenderSystem.AutoStorageIndexBuffer starIndices = RenderSystem.getSequentialBuffer(VertexFormat.Mode.QUADS);
	private final GpuBuffer starBuffer;
	private int starIndexCount;

	public TFSkyRenderer() {
		this.starBuffer = buildStars();
	}

	public static void extractLevelRender(ExtractLevelRenderStateEvent event) {
		event.getRenderState().setRenderData(RENDER_DARK_DISC, shouldDarkenSky(event.getLevel(), event.getDeltaTracker().getGameTimeDeltaTicks()));
	}

	// [VanillaCopy] LevelRenderer.addSkyPass's overworld branch, with sun/moon/sunrise-sunset using environment attribute angles, our own stars at full brightness, and lowering void horizon threshold height from getHorizonHeight (63) to 0
	public boolean renderSky(LevelRenderState level, SkyRenderState sky, Matrix4fc modelmatrix, Runnable setupFog) {
		LevelRenderer levelRenderer = Minecraft.getInstance().levelRenderer;

		setupFog.run();

		PoseStack posestack = new PoseStack();
		levelRenderer.skyRenderer.renderSkyDisc(sky.skyColor);

		//TF: render sunrise/sunset color band (dusk glow on horizon)
		if (sky.sunriseAndSunsetColor != 0) {
			levelRenderer.skyRenderer.renderSunriseAndSunset(posestack, sky.sunAngle, sky.sunriseAndSunsetColor);
		}

		//TF: render sun and moon using environment attribute angles from dimension data
		posestack.pushPose();
		levelRenderer.skyRenderer.renderSunMoonAndStars(posestack, sky.sunAngle, sky.moonAngle, sky.starAngle, sky.moonPhase, sky.rainBrightness, 0.0F);
		posestack.popPose();

		//TF: replace star rendering with our own double-star renderer
		posestack.pushPose();
		posestack.mulPose(Axis.YP.rotationDegrees(-90.0F));
		renderStars(posestack);
		posestack.popPose();

		//TF: use custom height checks for the void sky as vanilla hardcodes to 63
		if (Boolean.TRUE.equals(level.getRenderData(RENDER_DARK_DISC))) {
			levelRenderer.skyRenderer.renderDarkDisc();
		}

		return true;
	}

	private static boolean shouldDarkenSky(ClientLevel level, float partialTicks) {
		return Minecraft.getInstance().player.getEyePosition(partialTicks).y - level.getMinY() < 0.0;
	}

	//[VanillaCopy] of SkyRenderer.renderStars, using our own buffer instead. Coloring was also removed as the stars are always fully bright
	private void renderStars(PoseStack stack) {
		Matrix4fStack matrix = RenderSystem.getModelViewStack();
		matrix.pushMatrix();
		matrix.mul(stack.last().pose());
		RenderPipeline renderPipeline = RenderPipelines.STARS;
		GpuTextureView colorTexture = Minecraft.getInstance().getMainRenderTarget().getColorTextureView();
		GpuTextureView depthTexture = Minecraft.getInstance().getMainRenderTarget().getDepthTextureView();
		GpuBuffer indices = this.starIndices.getBuffer(this.starIndexCount);
		GpuBufferSlice dynamicTransforms = RenderSystem.getDynamicUniforms()
			.writeTransform(matrix, new Vector4f(1.0F, 1.0F, 1.0F, 1.0F), new Vector3f(), new Matrix4f());
		try (RenderPass renderPass = RenderSystem.getDevice()
				.createCommandEncoder()
				.createRenderPass(() -> "Stars", colorTexture, OptionalInt.empty(), depthTexture, OptionalDouble.empty())) {
			renderPass.setPipeline(renderPipeline);
			RenderSystem.bindDefaultUniforms(renderPass);
			renderPass.setUniform("DynamicTransforms", dynamicTransforms);
			renderPass.setVertexBuffer(0, this.starBuffer);
			renderPass.setIndexBuffer(indices, this.starIndices.type());
			renderPass.drawIndexed(0, 0, this.starIndexCount, 1);
		}

		matrix.popMatrix();
	}

	// [VanillaCopy] of SkyRenderer.buildStars but with double the number of them
	private GpuBuffer buildStars() {
		RandomSource random = RandomSource.create(10842L);
		GpuBuffer gpuBuffer;

		try (ByteBufferBuilder byteBufferBuilder = ByteBufferBuilder.exactlySized(DefaultVertexFormat.POSITION.getVertexSize() * 3000 * 4)) {
			BufferBuilder bufferBuilder = new BufferBuilder(byteBufferBuilder, VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION);

			// TF - 1500 -> 3000
			for (int i = 0; i < 3000; ++i) {
				float f1 = random.nextFloat() * 2.0F - 1.0F;
				float f2 = random.nextFloat() * 2.0F - 1.0F;
				float f3 = random.nextFloat() * 2.0F - 1.0F;
				float f4 = 0.15F + random.nextFloat() * 0.1F;
				float f5 = Mth.lengthSquared(f1, f2, f3);
				if (!(f5 <= 0.010000001F) && !(f5 >= 1.0F)) {
					Vector3f vector3f = new Vector3f(f1, f2, f3).normalize(100.0F);
					float f6 = (float)(random.nextDouble() * (float) Math.PI * 2.0);
					Matrix3f matrix3f = new Matrix3f().rotateTowards(new Vector3f(vector3f).negate(), new Vector3f(0.0F, 1.0F, 0.0F)).rotateZ(-f6);
					bufferBuilder.addVertex(vector3f.add(new Vector3f(f4, -f4, 0.0F).mul(matrix3f).add(vector3f)));
					bufferBuilder.addVertex(vector3f.add(new Vector3f(f4, f4, 0.0F).mul(matrix3f).add(vector3f)));
					bufferBuilder.addVertex(vector3f.add(new Vector3f(-f4, f4, 0.0F).mul(matrix3f).add(vector3f)));
					bufferBuilder.addVertex(vector3f.add(new Vector3f(-f4, -f4, 0.0F).mul(matrix3f).add(vector3f)));
				}
			}

			try (MeshData mesh = bufferBuilder.buildOrThrow()) {
				this.starIndexCount = mesh.drawState().indexCount();
				gpuBuffer = RenderSystem.getDevice().createBuffer(() -> "TF Stars buffer", 40, mesh.vertexBuffer());
			}
		}

		return gpuBuffer;
	}

	@Override
	public void close() {
		starBuffer.close();
	}
}
