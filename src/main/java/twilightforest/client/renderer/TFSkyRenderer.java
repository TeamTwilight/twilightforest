package twilightforest.client.renderer;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.joml.Matrix4f;

@OnlyIn(Dist.CLIENT)
public class TFSkyRenderer {

	private static VertexBuffer starBuffer;

	public TFSkyRenderer() {
		this.createStars();
	}


	// [VanillaCopy] LevelRenderer.renderSky's overworld branch, without sun/moon/sunrise/sunset, using our own stars at full brightness, and lowering void horizon threshold height from getHorizonHeight (63) to 0
	public static boolean renderSky(ClientLevel level, float partialTicks, PoseStack stack, Camera camera, Matrix4f projectionMatrix, Runnable setupFog) {
		LevelRenderer levelRenderer = Minecraft.getInstance().levelRenderer;

		setupFog.run();
		Vec3 vec3 = level.getSkyColor(camera.getPosition(), partialTicks);
		float f = (float) vec3.x();
		float f1 = (float) vec3.y();
		float f2 = (float) vec3.z();
		FogRenderer.levelFogColor();
		//BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder(); TF - Unused
		RenderSystem.depthMask(false);
		RenderSystem.setShaderColor(f, f1, f2, 1.0F);
		ShaderInstance shaderinstance = RenderSystem.getShader();
		levelRenderer.skyBuffer.bind();
		levelRenderer.skyBuffer.drawWithShader(stack.last().pose(), projectionMatrix, shaderinstance);
		VertexBuffer.unbind();
		RenderSystem.enableBlend();
		/* TF - snip out sunrise/sunset since that doesn't happen here
		 * float[] afloat = level.effects().getSunriseColor(level.getTimeOfDay(partialTicks), partialTicks);
		 * if (afloat != null) ...
		 */

		RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		stack.pushPose();
		float f11 = 1.0F - level.getRainLevel(partialTicks);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, f11);
		stack.mulPose(Axis.YP.rotationDegrees(-90.0F));
		stack.mulPose(Axis.XP.rotationDegrees(level.getTimeOfDay(partialTicks) * 360.0F));
		/* TF - snip out sun/moon
		 * Matrix4f matrix4f1 = stack.last().pose();
		 * float f12 = 30.0F;
		 * ...
		 * BufferUploader.drawWithShader(bufferbuilder.end());
		 */
		float f10 = 1.0F; // TF - stars are always bright

		//if (f10 > 0.0F) { Always true
		RenderSystem.setShaderColor(f10, f10, f10, f10);
		RenderSystem.setShaderColor(f10, f10, f10, f10);
		FogRenderer.setupNoFog();
		starBuffer.bind();
		starBuffer.drawWithShader(stack.last().pose(), projectionMatrix, GameRenderer.getPositionShader());
		VertexBuffer.unbind();
		setupFog.run();
		//}

		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.disableBlend();
		RenderSystem.defaultBlendFunc();
		stack.popPose();
		RenderSystem.setShaderColor(0.0F, 0.0F, 0.0F, 1.0F);
		double d0 = camera.getEntity().getEyePosition(partialTicks).y(); // - level.getLevelData().getHorizonHeight(level); // TF: Lower Void Horizon Y-Threshold from 63 to 0
		if (d0 < 0.0D) {
			stack.pushPose();
			stack.translate(0.0F, 12.0F, 0.0F);
			levelRenderer.darkBuffer.bind();
			levelRenderer.darkBuffer.drawWithShader(stack.last().pose(), projectionMatrix, shaderinstance);
			VertexBuffer.unbind();
			stack.popPose();
		}

		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.depthMask(true);
		return true;
	}

	// [VanillaCopy] LevelRenderer.createStars
	private void createStars() {
		Tesselator tesselator = Tesselator.getInstance();
		BufferBuilder bufferbuilder = tesselator.getBuilder();
		RenderSystem.setShader(GameRenderer::getPositionShader);
		if (starBuffer != null) {
			starBuffer.close();
		}

		starBuffer = new VertexBuffer(VertexBuffer.Usage.STATIC);
		BufferBuilder.RenderedBuffer renderedBuffer = this.drawStars(bufferbuilder);
		starBuffer.bind();
		starBuffer.upload(renderedBuffer);
		VertexBuffer.unbind();
	}

	// [VanillaCopy] of LevelRenderer.drawStars but with double the number of them
	private BufferBuilder.RenderedBuffer drawStars(BufferBuilder bufferBuilder) {
		RandomSource random = RandomSource.create(10842L);
		bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION);

		// TF - 1500 -> 3000
		for (int i = 0; i < 3000; ++i) {
			double d0 = random.nextFloat() * 2.0F - 1.0F;
			double d1 = random.nextFloat() * 2.0F - 1.0F;
			double d2 = random.nextFloat() * 2.0F - 1.0F;
			double d3 = 0.15F + random.nextFloat() * 0.1F;
			double d4 = d0 * d0 + d1 * d1 + d2 * d2;
			if (d4 < 1.0D && d4 > 0.01D) {
				d4 = 1.0D / Math.sqrt(d4);
				d0 *= d4;
				d1 *= d4;
				d2 *= d4;
				double d5 = d0 * 100.0D;
				double d6 = d1 * 100.0D;
				double d7 = d2 * 100.0D;
				double d8 = Math.atan2(d0, d2);
				double d9 = Math.sin(d8);
				double d10 = Math.cos(d8);
				double d11 = Math.atan2(Math.sqrt(d0 * d0 + d2 * d2), d1);
				double d12 = Math.sin(d11);
				double d13 = Math.cos(d11);
				double d14 = random.nextDouble() * Math.PI * 2.0D;
				double d15 = Math.sin(d14);
				double d16 = Math.cos(d14);

				for (int j = 0; j < 4; ++j) {
					double d18 = ((j & 2) - 1) * d3;
					double d19 = ((j + 1 & 2) - 1) * d3;
					double d21 = d18 * d16 - d19 * d15;
					double d22 = d19 * d16 + d18 * d15;
					double d23 = d21 * d12 + 0.0D * d13;
					double d24 = 0.0D * d12 - d21 * d13;
					double d25 = d24 * d9 - d22 * d10;
					double d26 = d22 * d9 + d24 * d10;
					bufferBuilder.vertex(d5 + d25, d6 + d23, d7 + d26).endVertex();
				}
			}
		}

		return bufferBuilder.end();
	}
}
