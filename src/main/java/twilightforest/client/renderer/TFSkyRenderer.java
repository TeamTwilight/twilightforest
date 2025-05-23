package twilightforest.client.renderer;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.*;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import org.joml.*;

import java.lang.Math;

public class TFSkyRenderer implements AutoCloseable {

	private static final VertexBuffer starBuffer = VertexBuffer.uploadStatic(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION, TFSkyRenderer::buildStars);

	// [VanillaCopy] LevelRenderer.addSkyPass's overworld branch, without sun/moon/sunrise/sunset, using our own stars at full brightness, and lowering void horizon threshold height from getHorizonHeight (63) to 0
	public static boolean renderSky(ClientLevel level, float partialTicks, Camera camera, Runnable setupFog) {
		LevelRenderer levelRenderer = Minecraft.getInstance().levelRenderer;
		setupFog.run();

		PoseStack posestack = new PoseStack();
		//TF: all unused
//		float f = this.level.getSunAngle(partialTick);
//		float f1 = this.level.getTimeOfDay(partialTick);
//		float f2 = 1.0F - this.level.getRainLevel(partialTick);
//		float f3 = this.level.getStarBrightness(partialTick) * f2;
//		int i = dimensionspecialeffects.getSunriseOrSunsetColor(f1);
//		int j = this.level.getMoonPhase();
		int k = level.getSkyColor(camera.getPosition(), partialTicks);
		float f4 = ARGB.redFloat(k);
		float f5 = ARGB.greenFloat(k);
		float f6 = ARGB.blueFloat(k);
		levelRenderer.skyRenderer.renderSkyDisc(f4, f5, f6);
		//TF: snip out sunrise and sunset coloring
//		if (dimensionspecialeffects.isSunriseOrSunset(f1)) {
//			levelRenderer.skyRenderer.renderSunriseAndSunset(posestack, multibuffersource$buffersource, f, i);
//		}

		//TF: replace sun, moon, and star rendering method with our own star renderer
		//rotation is from SkyRenderer.renderSunMoonAndStars
		posestack.pushPose();
		posestack.mulPose(Axis.YP.rotationDegrees(-90.0F));
		renderStars(setupFog, posestack);
		posestack.popPose();
		//TF: use custom height checks for the void sky as vanilla hardcodes to 63
		if (shouldDarkenSky(level, camera, partialTicks)) {
			levelRenderer.skyRenderer.renderDarkDisc(posestack);
		}

		return true;
	}

	private static boolean shouldDarkenSky(ClientLevel level, Camera camera, float partialTicks) {
		return camera.getEntity().getEyePosition(partialTicks).y - level.getMinY() < 0.0;
	}

	//[VanillaCopy] of SkyRenderer.renderStars, using our own buffer instead. Coloring was also removed as the stars are always fully bright
	private static void renderStars(Runnable setupFog, PoseStack stack) {
		Matrix4fStack matrix = RenderSystem.getModelViewStack();
		matrix.pushMatrix();
		matrix.mul(stack.last().pose());
		RenderSystem.setShaderFog(FogParameters.NO_FOG);
		starBuffer.drawWithRenderType(RenderType.stars());
		setupFog.run();
		matrix.popMatrix();
	}

	// [VanillaCopy] of SkyRenderer.buildStars but with double the number of them
	private static void buildStars(VertexConsumer consumer) {
		RandomSource random = RandomSource.create(10842L);

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
				consumer.addVertex(vector3f.add(new Vector3f(f4, -f4, 0.0F).mul(matrix3f).add(vector3f)));
				consumer.addVertex(vector3f.add(new Vector3f(f4, f4, 0.0F).mul(matrix3f).add(vector3f)));
				consumer.addVertex(vector3f.add(new Vector3f(-f4, f4, 0.0F).mul(matrix3f).add(vector3f)));
				consumer.addVertex(vector3f.add(new Vector3f(-f4, -f4, 0.0F).mul(matrix3f).add(vector3f)));
			}
		}
	}

	@Override
	public void close() {
		starBuffer.close();
	}
}
