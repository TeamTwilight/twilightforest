package twilightforest.client.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexBuffer;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.ISkyRenderHandler;

import java.util.Random;

@OnlyIn(Dist.CLIENT)
public class TFSkyRenderer implements ISkyRenderHandler {

	private VertexBuffer starVBO;
	private final VertexFormat vertexBufferFormat = DefaultVertexFormats.POSITION;

	public TFSkyRenderer() {
		generateStars();
	}

	// [VanillaCopy] RenderGlobal.renderSky's overworld branch, without sun/moon/sunrise/sunset, and using our own stars at full brightness
	@Override
	@OnlyIn(Dist.CLIENT)
	public void render(int ticks, float partialTicks, MatrixStack ms, ClientWorld world, Minecraft mc) {
		WorldRenderer rg = mc.worldRenderer;

		RenderSystem.disableTexture();
		Vector3d vec3d = world.getSkyColor(mc.gameRenderer.getActiveRenderInfo().getBlockPos(), partialTicks);
		float f = (float) vec3d.x;
		float f1 = (float) vec3d.y;
		float f2 = (float) vec3d.z;

		FogRenderer.applyFog();
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		RenderSystem.color3f(f, f1, f2);
		RenderSystem.depthMask(false);
		RenderSystem.enableFog();
		RenderSystem.color3f(f, f1, f2);

		rg.skyVBO.bindBuffer();
		this.vertexBufferFormat.setupBufferState(0L);
		rg.skyVBO.draw(ms.getLast().getMatrix(), 7);
		VertexBuffer.unbindBuffer();
		this.vertexBufferFormat.clearBufferState();

		RenderSystem.disableFog();
		RenderSystem.disableAlphaTest();
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
//		RenderHelper.disableStandardItemLighting();
		/* TF - snip out sunrise/sunset since that doesn't happen here
         * float[] afloat = ...
         * if (afloat != null) ...
         */

		RenderSystem.enableTexture();
		RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		ms.push();
		float f16 = 1.0F - world.getRainStrength(partialTicks);
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, f16);
		ms.rotate(Vector3f.YP.rotationDegrees(-90.0F));
		ms.rotate(Vector3f.XP.rotationDegrees(world.func_242415_f(partialTicks) * 360.0F));
        /* TF - snip out sun/moon
         * float f17 = 30.0F;
         * ...
         * tessellator.draw();
         */
		RenderSystem.disableTexture();
		float f15 = 1.0F; // TF - stars are always bright

		//if (f15 > 0.0F) { Always true
		RenderSystem.color4f(f15, f15, f15, f15);

		this.starVBO.bindBuffer();
		this.vertexBufferFormat.setupBufferState(0L);
		this.starVBO.draw(ms.getLast().getMatrix(), 7);
		VertexBuffer.unbindBuffer();
		this.vertexBufferFormat.clearBufferState();
		//}

		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.disableBlend();
		RenderSystem.enableAlphaTest();
		RenderSystem.enableFog();
		ms.pop();
		RenderSystem.disableTexture();
		RenderSystem.color3f(0.0F, 0.0F, 0.0F);
		/** world.getWorldInfo().getVoidFogHeight() -> 27, because the sea level for TF is lower TODO: Keep an eye on Forge PR #7528*/
		double d0 = mc.player.getEyePosition(partialTicks).y - 30;

		if (d0 < 0.0D) {
			ms.push();
			ms.translate(0.0F, 12.0F, 0.0F);

			rg.sky2VBO.bindBuffer();
			this.vertexBufferFormat.setupBufferState(0L);
			rg.sky2VBO.draw(ms.getLast().getMatrix(), 7);
			VertexBuffer.unbindBuffer();
			this.vertexBufferFormat.clearBufferState();

			ms.pop();
//			float f18 = 1.0F;
			float f19 = -((float) (d0 + 65.0D));
//			float f20 = -1.0F;
			bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
			bufferbuilder.pos(-1.0D, f19, 1.0D).color(0, 0, 0, 255).endVertex();
			bufferbuilder.pos(1.0D, f19, 1.0D).color(0, 0, 0, 255).endVertex();
			bufferbuilder.pos(1.0D, -1.0D, 1.0D).color(0, 0, 0, 255).endVertex();
			bufferbuilder.pos(-1.0D, -1.0D, 1.0D).color(0, 0, 0, 255).endVertex();
			bufferbuilder.pos(-1.0D, -1.0D, -1.0D).color(0, 0, 0, 255).endVertex();
			bufferbuilder.pos(1.0D, -1.0D, -1.0D).color(0, 0, 0, 255).endVertex();
			bufferbuilder.pos(1.0D, f19, -1.0D).color(0, 0, 0, 255).endVertex();
			bufferbuilder.pos(-1.0D, f19, -1.0D).color(0, 0, 0, 255).endVertex();
			bufferbuilder.pos(1.0D, -1.0D, -1.0D).color(0, 0, 0, 255).endVertex();
			bufferbuilder.pos(1.0D, -1.0D, 1.0D).color(0, 0, 0, 255).endVertex();
			bufferbuilder.pos(1.0D, f19, 1.0D).color(0, 0, 0, 255).endVertex();
			bufferbuilder.pos(1.0D, f19, -1.0D).color(0, 0, 0, 255).endVertex();
			bufferbuilder.pos(-1.0D, f19, -1.0D).color(0, 0, 0, 255).endVertex();
			bufferbuilder.pos(-1.0D, f19, 1.0D).color(0, 0, 0, 255).endVertex();
			bufferbuilder.pos(-1.0D, -1.0D, 1.0D).color(0, 0, 0, 255).endVertex();
			bufferbuilder.pos(-1.0D, -1.0D, -1.0D).color(0, 0, 0, 255).endVertex();
			bufferbuilder.pos(-1.0D, -1.0D, -1.0D).color(0, 0, 0, 255).endVertex();
			bufferbuilder.pos(-1.0D, -1.0D, 1.0D).color(0, 0, 0, 255).endVertex();
			bufferbuilder.pos(1.0D, -1.0D, 1.0D).color(0, 0, 0, 255).endVertex();
			bufferbuilder.pos(1.0D, -1.0D, -1.0D).color(0, 0, 0, 255).endVertex();
			tessellator.draw();
		}

		if (world.func_239132_a_().func_239216_b_()) {
			RenderSystem.color3f(f * 0.2F + 0.04F, f1 * 0.2F + 0.04F, f2 * 0.6F + 0.1F);
		} else {
			RenderSystem.color3f(f, f1, f2);
		}

		ms.push();
		ms.translate(0.0F, -((float) (d0 - 16.0D)), 0.0F);
		//RenderSystem.callList(rg.glSkyList2);
		ms.pop();
		RenderSystem.enableTexture();
		RenderSystem.depthMask(true);
		RenderSystem.disableFog();
	}

	// [VanillaCopy] RenderGlobal.generateStars
	private void generateStars() {
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();

		if (this.starVBO != null) {
			this.starVBO.close();
		}

		// TF - inlined RenderGlobal field that's only used once here
		this.starVBO = new VertexBuffer(DefaultVertexFormats.POSITION);
		this.renderStars(bufferbuilder);
		bufferbuilder.finishDrawing();
		this.starVBO.upload(bufferbuilder);
	}

	// [VanillaCopy] of RenderGlobal.renderStars but with double the number of them
	@SuppressWarnings("unused")
	private void renderStars(BufferBuilder bufferBuilder) {
		Random random = new Random(10842L);
		bufferBuilder.begin(7, DefaultVertexFormats.POSITION);

		// TF - 1500 -> 3000
		for (int i = 0; i < 3000; ++i) {
			double d0 = random.nextFloat() * 2.0F - 1.0F;
			double d1 = random.nextFloat() * 2.0F - 1.0F;
			double d2 = random.nextFloat() * 2.0F - 1.0F;
			double d3 = 0.15F + random.nextFloat() * 0.1F;
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
					double d18 = ((j & 2) - 1) * d3;
					double d19 = ((j + 1 & 2) - 1) * d3;
					double d20 = 0.0D;
					double d21 = d18 * d16 - d19 * d15;
					double d22 = d19 * d16 + d18 * d15;
					double d23 = d21 * d12 + 0.0D * d13;
					double d24 = 0.0D * d12 - d21 * d13;
					double d25 = d24 * d9 - d22 * d10;
					double d26 = d22 * d9 + d24 * d10;
					bufferBuilder.pos(d5 + d25, d6 + d23, d7 + d26).endVertex();
				}
			}
		}
	}
}
