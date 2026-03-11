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
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class TFSkyRenderer {

	private static VertexBuffer starBuffer;

	// [VanillaCopy] LevelRenderer.renderSky's overworld branch, without sun/moon/sunrise/sunset, using our own stars at full brightness, and lowering void horizon threshold height from getHorizonHeight (63) to 0
	public static boolean renderSky(ClientLevel level, float partialTicks, Matrix4f frustumMatrix, Camera camera, Matrix4f projectionMatrix, Runnable setupFog) {
		LevelRenderer levelRenderer = Minecraft.getInstance().levelRenderer;
		PoseStack stack = new PoseStack();
		stack.mulPose(frustumMatrix);

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
		 * BufferUploader.drawWithShader(bufferbuilder1.buildOrThrow());
		 */
		float f10 = 1.0F; // TF - stars are always bright

		//if (f10 > 0.0F) { Always true
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
		double d0 = camera.getEntity().getEyePosition(partialTicks).y() - level.getMinBuildHeight(); // TF: Lower Void Horizon Y-Threshold from 63 to actual void location (-32)
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
	public static void createStars() {
		if (starBuffer != null) {
			starBuffer.close();
		}

		starBuffer = new VertexBuffer(VertexBuffer.Usage.STATIC);
		starBuffer.bind();
		starBuffer.upload(drawStars(Tesselator.getInstance()));
		VertexBuffer.unbind();
	}

	// [VanillaCopy] of LevelRenderer.drawStars but with double the number of them
	private static MeshData drawStars(Tesselator tesselator) {
		RandomSource random = RandomSource.create(10842L);
		BufferBuilder bufferbuilder = tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION);

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
				Quaternionf quaternionf = new Quaternionf().rotateTo(new Vector3f(0.0F, 0.0F, -1.0F), vector3f).rotateZ(f6);
				bufferbuilder.addVertex(vector3f.add(new Vector3f(f4, -f4, 0.0F).rotate(quaternionf)));
				bufferbuilder.addVertex(vector3f.add(new Vector3f(f4, f4, 0.0F).rotate(quaternionf)));
				bufferbuilder.addVertex(vector3f.add(new Vector3f(-f4, f4, 0.0F).rotate(quaternionf)));
				bufferbuilder.addVertex(vector3f.add(new Vector3f(-f4, -f4, 0.0F).rotate(quaternionf)));
			}
		}

		return bufferbuilder.buildOrThrow();
	}
}
