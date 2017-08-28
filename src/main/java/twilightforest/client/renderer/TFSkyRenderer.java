package twilightforest.client.renderer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.IRenderHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

@SideOnly(Side.CLIENT)
public class TFSkyRenderer extends IRenderHandler {
	private int starGLCallList;
	private net.minecraft.client.renderer.vertex.VertexBuffer starVBO;

	public TFSkyRenderer() {
		generateStars();
	}

	// [VanillaCopy] RenderGlobal.renderSky's overworld branch, without sun/moon/sunrise/sunset, and using our own stars at full brightness
	@SuppressWarnings("unused")
	@Override
	@SideOnly(Side.CLIENT)
	public void render(float partialTicks, WorldClient world, Minecraft mc) {
		RenderGlobal rg = mc.renderGlobal;
		int pass = EntityRenderer.anaglyphField;

		GlStateManager.disableTexture2D();
		Vec3d vec3d = world.getSkyColor(mc.getRenderViewEntity(), partialTicks);
		float f = (float) vec3d.x;
		float f1 = (float) vec3d.y;
		float f2 = (float) vec3d.z;

		if (pass != 2) {
			float f3 = (f * 30.0F + f1 * 59.0F + f2 * 11.0F) / 100.0F;
			float f4 = (f * 30.0F + f1 * 70.0F) / 100.0F;
			float f5 = (f * 30.0F + f2 * 70.0F) / 100.0F;
			f = f3;
			f1 = f4;
			f2 = f5;
		}

		GlStateManager.color(f, f1, f2);
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder vertexbuffer = tessellator.getBuffer();
		GlStateManager.depthMask(false);
		GlStateManager.enableFog();
		GlStateManager.color(f, f1, f2);

		if (OpenGlHelper.useVbo()) {
			rg.skyVBO.bindBuffer();
			GlStateManager.glEnableClientState(32884);
			GlStateManager.glVertexPointer(3, 5126, 12, 0);
			rg.skyVBO.drawArrays(7);
			rg.skyVBO.unbindBuffer();
			GlStateManager.glDisableClientState(32884);
		} else {
			GlStateManager.callList(rg.glSkyList);
		}

		GlStateManager.disableFog();
		GlStateManager.disableAlpha();
		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		RenderHelper.disableStandardItemLighting();
		/* TF - snip out sunrise/sunset since that doesn't happen here
         * float[] afloat = ...
         * if (afloat != null) ...
         */

		GlStateManager.enableTexture2D();
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		GlStateManager.pushMatrix();
		float f16 = 1.0F - world.getRainStrength(partialTicks);
		GlStateManager.color(1.0F, 1.0F, 1.0F, f16);
		GlStateManager.rotate(-90.0F, 0.0F, 1.0F, 0.0F);
		GlStateManager.rotate(world.getCelestialAngle(partialTicks) * 360.0F, 1.0F, 0.0F, 0.0F);
        /* TF - snip out sun/moon
         * float f17 = 30.0F;
         * ...
         * tessellator.draw();
         */
		GlStateManager.disableTexture2D();
		float f15 = 1.0F; // TF - stars are always bright

		if (f15 > 0.0F) {
			GlStateManager.color(f15, f15, f15, f15);

			if (OpenGlHelper.useVbo()) {
				this.starVBO.bindBuffer();
				GlStateManager.glEnableClientState(32884);
				GlStateManager.glVertexPointer(3, 5126, 12, 0);
				this.starVBO.drawArrays(7);
				this.starVBO.unbindBuffer();
				GlStateManager.glDisableClientState(32884);
			} else {
				GlStateManager.callList(this.starGLCallList);
			}
		}

		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.disableBlend();
		GlStateManager.enableAlpha();
		GlStateManager.enableFog();
		GlStateManager.popMatrix();
		GlStateManager.disableTexture2D();
		GlStateManager.color(0.0F, 0.0F, 0.0F);
		double d0 = mc.player.getPositionEyes(partialTicks).y - world.getHorizon();

		if (d0 < 0.0D) {
			GlStateManager.pushMatrix();
			GlStateManager.translate(0.0F, 12.0F, 0.0F);

			if (OpenGlHelper.useVbo()) {
				rg.sky2VBO.bindBuffer();
				GlStateManager.glEnableClientState(32884);
				GlStateManager.glVertexPointer(3, 5126, 12, 0);
				rg.sky2VBO.drawArrays(7);
				rg.sky2VBO.unbindBuffer();
				GlStateManager.glDisableClientState(32884);
			} else {
				GlStateManager.callList(rg.glSkyList2);
			}

			GlStateManager.popMatrix();
			float f18 = 1.0F;
			float f19 = -((float) (d0 + 65.0D));
			float f20 = -1.0F;
			vertexbuffer.begin(7, DefaultVertexFormats.POSITION_COLOR);
			vertexbuffer.pos(-1.0D, (double) f19, 1.0D).color(0, 0, 0, 255).endVertex();
			vertexbuffer.pos(1.0D, (double) f19, 1.0D).color(0, 0, 0, 255).endVertex();
			vertexbuffer.pos(1.0D, -1.0D, 1.0D).color(0, 0, 0, 255).endVertex();
			vertexbuffer.pos(-1.0D, -1.0D, 1.0D).color(0, 0, 0, 255).endVertex();
			vertexbuffer.pos(-1.0D, -1.0D, -1.0D).color(0, 0, 0, 255).endVertex();
			vertexbuffer.pos(1.0D, -1.0D, -1.0D).color(0, 0, 0, 255).endVertex();
			vertexbuffer.pos(1.0D, (double) f19, -1.0D).color(0, 0, 0, 255).endVertex();
			vertexbuffer.pos(-1.0D, (double) f19, -1.0D).color(0, 0, 0, 255).endVertex();
			vertexbuffer.pos(1.0D, -1.0D, -1.0D).color(0, 0, 0, 255).endVertex();
			vertexbuffer.pos(1.0D, -1.0D, 1.0D).color(0, 0, 0, 255).endVertex();
			vertexbuffer.pos(1.0D, (double) f19, 1.0D).color(0, 0, 0, 255).endVertex();
			vertexbuffer.pos(1.0D, (double) f19, -1.0D).color(0, 0, 0, 255).endVertex();
			vertexbuffer.pos(-1.0D, (double) f19, -1.0D).color(0, 0, 0, 255).endVertex();
			vertexbuffer.pos(-1.0D, (double) f19, 1.0D).color(0, 0, 0, 255).endVertex();
			vertexbuffer.pos(-1.0D, -1.0D, 1.0D).color(0, 0, 0, 255).endVertex();
			vertexbuffer.pos(-1.0D, -1.0D, -1.0D).color(0, 0, 0, 255).endVertex();
			vertexbuffer.pos(-1.0D, -1.0D, -1.0D).color(0, 0, 0, 255).endVertex();
			vertexbuffer.pos(-1.0D, -1.0D, 1.0D).color(0, 0, 0, 255).endVertex();
			vertexbuffer.pos(1.0D, -1.0D, 1.0D).color(0, 0, 0, 255).endVertex();
			vertexbuffer.pos(1.0D, -1.0D, -1.0D).color(0, 0, 0, 255).endVertex();
			tessellator.draw();
		}

		if (world.provider.isSkyColored()) {
			GlStateManager.color(f * 0.2F + 0.04F, f1 * 0.2F + 0.04F, f2 * 0.6F + 0.1F);
		} else {
			GlStateManager.color(f, f1, f2);
		}

		GlStateManager.pushMatrix();
		GlStateManager.translate(0.0F, -((float) (d0 - 16.0D)), 0.0F);
		GlStateManager.callList(rg.glSkyList2);
		GlStateManager.popMatrix();
		GlStateManager.enableTexture2D();
		GlStateManager.depthMask(true);
	}

	// [VanillaCopy] RenderGlobal.generateStars
	private void generateStars() {
		// TF - inlined RenderGlobal field that's only used once here
		VertexFormat vertexBufferFormat = new VertexFormat();
		vertexBufferFormat.addElement(new VertexFormatElement(0, VertexFormatElement.EnumType.FLOAT, VertexFormatElement.EnumUsage.POSITION, 3));

		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder vertexbuffer = tessellator.getBuffer();

		if (this.starVBO != null) {
			this.starVBO.deleteGlBuffers();
		}

		if (this.starGLCallList >= 0) {
			GLAllocation.deleteDisplayLists(this.starGLCallList);
			this.starGLCallList = -1;
		}

		if (OpenGlHelper.useVbo()) {
			this.starVBO = new net.minecraft.client.renderer.vertex.VertexBuffer(vertexBufferFormat);
			this.renderStars(vertexbuffer);
			vertexbuffer.finishDrawing();
			vertexbuffer.reset();
			this.starVBO.bufferData(vertexbuffer.getByteBuffer());
		} else {
			this.starGLCallList = GLAllocation.generateDisplayLists(1);
			GlStateManager.pushMatrix();
			GlStateManager.glNewList(this.starGLCallList, 4864);
			this.renderStars(vertexbuffer);
			tessellator.draw();
			GlStateManager.glEndList();
			GlStateManager.popMatrix();
		}
	}

	// [VanillaCopy] of RenderGlobal.renderStars but with double the number of them
	@SuppressWarnings("unused")
	private void renderStars(BufferBuilder worldRendererIn) {
		Random random = new Random(10842L);
		worldRendererIn.begin(7, DefaultVertexFormats.POSITION);

		for (int i = 0; i < 3000; ++i) // TF - 1500 -> 3000
		{
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
					worldRendererIn.pos(d5 + d25, d6 + d23, d7 + d26).endVertex();
				}
			}
		}
	}
}
