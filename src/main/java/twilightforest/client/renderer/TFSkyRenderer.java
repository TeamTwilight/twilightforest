package twilightforest.client.renderer;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexBuffer;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.IRenderHandler;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Random;

@OnlyIn(Dist.CLIENT)
public class TFSkyRenderer implements IRenderHandler {

	private boolean vboEnabled;
	private int starGLCallList;
	private VertexBuffer starVBO;

	public TFSkyRenderer() {
		//vboEnabled = OpenGlHelper.useVbo();
		generateStars();
	}

	// [VanillaCopy] RenderGlobal.renderSky's overworld branch, without sun/moon/sunrise/sunset, and using our own stars at full brightness
	@Override
	@OnlyIn(Dist.CLIENT)
	public void render(int ticks, float partialTicks, ClientWorld world, Minecraft mc) {

		// [VanillaCopy] Excerpt from RenderGlobal.loadRenderers as we don't get a callback
		generateStars();
//		boolean flag = this.vboEnabled;
//		this.vboEnabled = OpenGlHelper.useVbo();
//		if (flag != this.vboEnabled) {
//			generateStars();
//		}

		WorldRenderer rg = mc.worldRenderer;
		//int pass = GameRenderer.anaglyphEnable ? GameRenderer.anaglyphField : 2;

		RenderSystem.disableTexture();
		Vec3d vec3d = world.func_228318_a_(mc.gameRenderer.getActiveRenderInfo().getBlockPos(), partialTicks);
		float f = (float) vec3d.x;
		float f1 = (float) vec3d.y;
		float f2 = (float) vec3d.z;

//		if (pass != 2) {
//			float f3 = (f * 30.0F + f1 * 59.0F + f2 * 11.0F) / 100.0F;
//			float f4 = (f * 30.0F + f1 * 70.0F) / 100.0F;
//			float f5 = (f * 30.0F + f2 * 70.0F) / 100.0F;
//			f = f3;
//			f1 = f4;
//			f2 = f5;
//		}

		RenderSystem.color3f(f, f1, f2);
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		RenderSystem.depthMask(false);
		RenderSystem.enableFog();
		RenderSystem.color3f(f, f1, f2);

		//TODO
//		if (this.vboEnabled) {
//			rg.skyVBO.bindBuffer();
//			RenderSystem.glEnableClientState(32884);
//			RenderSystem.glVertexPointer(3, 5126, 12, 0);
//			rg.skyVBO.drawArrays(7);
//			rg.skyVBO.unbindBuffer();
//			RenderSystem.glDisableClientState(32884);
//		} else {
//			RenderSystem.callList(rg.glSkyList);
//		}

		RenderSystem.disableFog();
		RenderSystem.disableAlphaTest();
		RenderSystem.enableBlend();
		RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		RenderHelper.disableStandardItemLighting();
		/* TF - snip out sunrise/sunset since that doesn't happen here
         * float[] afloat = ...
         * if (afloat != null) ...
         */

		RenderSystem.enableTexture();
		RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		RenderSystem.pushMatrix();
		float f16 = 1.0F - world.getRainStrength(partialTicks);
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, f16);
		RenderSystem.rotatef(-90.0F, 0.0F, 1.0F, 0.0F);
		RenderSystem.rotatef(world.getCelestialAngle(partialTicks) * 360.0F, 1.0F, 0.0F, 0.0F);
        /* TF - snip out sun/moon
         * float f17 = 30.0F;
         * ...
         * tessellator.draw();
         */
		RenderSystem.disableTexture();
		float f15 = 1.0F; // TF - stars are always bright

		if (f15 > 0.0F) {
			RenderSystem.color4f(f15, f15, f15, f15);

			//TODO
//			if (this.vboEnabled) {
//				this.starVBO.bindBuffer();
//				RenderSystem.glEnableClientState(32884);
//				RenderSystem.glVertexPointer(3, 5126, 12, 0);
//				this.starVBO.drawArrays(7);
//				this.starVBO.unbindBuffer();
//				RenderSystem.glDisableClientState(32884);
//			} else {
//				RenderSystem.callList(this.starGLCallList);
//			}
		}

		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.disableBlend();
		RenderSystem.enableAlphaTest();
		RenderSystem.enableFog();
		RenderSystem.popMatrix();
		RenderSystem.disableTexture();
		RenderSystem.color3f(0.0F, 0.0F, 0.0F);
		double d0 = mc.player.getEyePosition(partialTicks).y - world.getSeaLevel();

		if (d0 < 0.0D) {
			RenderSystem.pushMatrix();
			RenderSystem.translatef(0.0F, 12.0F, 0.0F);

			//TODO
//			if (this.vboEnabled) {
//				rg.sky2VBO.bindBuffer();
//				RenderSystem.glEnableClientState(32884);
//				RenderSystem.glVertexPointer(3, 5126, 12, 0);
//				rg.sky2VBO.drawArrays(7);
//				rg.sky2VBO.unbindBuffer();
//				RenderSystem.glDisableClientState(32884);
//			} else {
//				RenderSystem.callList(rg.glSkyList2);
//			}

			RenderSystem.popMatrix();
			float f18 = 1.0F;
			float f19 = -((float) (d0 + 65.0D));
			float f20 = -1.0F;
			bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
			bufferbuilder.vertex(-1.0D, (double) f19, 1.0D).color(0, 0, 0, 255).endVertex();
			bufferbuilder.vertex(1.0D, (double) f19, 1.0D).color(0, 0, 0, 255).endVertex();
			bufferbuilder.vertex(1.0D, -1.0D, 1.0D).color(0, 0, 0, 255).endVertex();
			bufferbuilder.vertex(-1.0D, -1.0D, 1.0D).color(0, 0, 0, 255).endVertex();
			bufferbuilder.vertex(-1.0D, -1.0D, -1.0D).color(0, 0, 0, 255).endVertex();
			bufferbuilder.vertex(1.0D, -1.0D, -1.0D).color(0, 0, 0, 255).endVertex();
			bufferbuilder.vertex(1.0D, (double) f19, -1.0D).color(0, 0, 0, 255).endVertex();
			bufferbuilder.vertex(-1.0D, (double) f19, -1.0D).color(0, 0, 0, 255).endVertex();
			bufferbuilder.vertex(1.0D, -1.0D, -1.0D).color(0, 0, 0, 255).endVertex();
			bufferbuilder.vertex(1.0D, -1.0D, 1.0D).color(0, 0, 0, 255).endVertex();
			bufferbuilder.vertex(1.0D, (double) f19, 1.0D).color(0, 0, 0, 255).endVertex();
			bufferbuilder.vertex(1.0D, (double) f19, -1.0D).color(0, 0, 0, 255).endVertex();
			bufferbuilder.vertex(-1.0D, (double) f19, -1.0D).color(0, 0, 0, 255).endVertex();
			bufferbuilder.vertex(-1.0D, (double) f19, 1.0D).color(0, 0, 0, 255).endVertex();
			bufferbuilder.vertex(-1.0D, -1.0D, 1.0D).color(0, 0, 0, 255).endVertex();
			bufferbuilder.vertex(-1.0D, -1.0D, -1.0D).color(0, 0, 0, 255).endVertex();
			bufferbuilder.vertex(-1.0D, -1.0D, -1.0D).color(0, 0, 0, 255).endVertex();
			bufferbuilder.vertex(-1.0D, -1.0D, 1.0D).color(0, 0, 0, 255).endVertex();
			bufferbuilder.vertex(1.0D, -1.0D, 1.0D).color(0, 0, 0, 255).endVertex();
			bufferbuilder.vertex(1.0D, -1.0D, -1.0D).color(0, 0, 0, 255).endVertex();
			tessellator.draw();
		}

		if (world.dimension.isSkyColored()) {
			RenderSystem.color3f(f * 0.2F + 0.04F, f1 * 0.2F + 0.04F, f2 * 0.6F + 0.1F);
		} else {
			RenderSystem.color3f(f, f1, f2);
		}

		RenderSystem.pushMatrix();
		RenderSystem.translatef(0.0F, -((float) (d0 - 16.0D)), 0.0F);
		//RenderSystem.callList(rg.glSkyList2);
		RenderSystem.popMatrix();
		RenderSystem.enableTexture();
		RenderSystem.depthMask(true);
	}

	// [VanillaCopy] RenderGlobal.generateStars
	private void generateStars() {
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();

		if (this.starVBO != null) {
			this.starVBO.close();
		}

		if (this.starGLCallList >= 0) {
			//GLAllocation.deleteDisplayLists(this.starGLCallList);
			this.starGLCallList = -1;
		}

		if (this.vboEnabled) {
			// TF - inlined RenderGlobal field that's only used once here
			this.starVBO = new VertexBuffer(DefaultVertexFormats.POSITION);
			this.renderStars(bufferbuilder);
			bufferbuilder.finishDrawing();
			bufferbuilder.reset();
			//this.starVBO.bufferData(bufferbuilder.getByteBuffer());

		} else {
			//this.starGLCallList = GLAllocation.generateDisplayLists(1);
			RenderSystem.pushMatrix();
			//RenderSystem.glNewList(this.starGLCallList, 4864);
			this.renderStars(bufferbuilder);
			tessellator.draw();
			//RenderSystem.glEndList();
			RenderSystem.popMatrix();
		}
	}

	// [VanillaCopy] of RenderGlobal.renderStars but with double the number of them
	@SuppressWarnings("unused")
	private void renderStars(BufferBuilder bufferBuilder) {
		Random random = new Random(10842L);
		bufferBuilder.begin(7, DefaultVertexFormats.POSITION);

		// TF - 1500 -> 3000
		for (int i = 0; i < 3000; ++i) {
			double d0 = (double) (random.nextFloat() * 2.0F - 1.0F);
			double d1 = (double) (random.nextFloat() * 2.0F - 1.0F);
			double d2 = (double) (random.nextFloat() * 2.0F - 1.0F);
			double d3 = (double) (0.15F + random.nextFloat() * 0.1F);
			double d4 = d0 * d0 + d1 * d1 + d2 * d2;

			if (d4 < 1.0D && d4 > 0.01D) {
				d4 = 1.0D / Math.sqrt(d4);
				d0 = d0 * d4;
				d1 = d1 * d4;
				d2 = d2 * d4;
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
					double d17 = 0.0D;
					double d18 = (double) ((j & 2) - 1) * d3;
					double d19 = (double) ((j + 1 & 2) - 1) * d3;
					double d20 = 0.0D;
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
	}
}
