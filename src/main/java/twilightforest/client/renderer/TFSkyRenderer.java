package twilightforest.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.multiplayer.ClientLevel;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexBuffer;
import net.minecraft.world.phys.Vec3;
import com.mojang.math.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.ISkyRenderHandler;

import java.util.Random;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.Tesselator;

@OnlyIn(Dist.CLIENT)
public class TFSkyRenderer implements ISkyRenderHandler {

	private VertexBuffer starVBO;
	private final VertexFormat vertexBufferFormat = DefaultVertexFormat.POSITION;

	public TFSkyRenderer() {
		generateStars();
	}

	// [VanillaCopy] RenderGlobal.renderSky's overworld branch, without sun/moon/sunrise/sunset, and using our own stars at full brightness
	@Override
	@OnlyIn(Dist.CLIENT)
	public void render(int ticks, float partialTicks, PoseStack ms, ClientLevel world, Minecraft mc) {
		LevelRenderer rg = mc.levelRenderer;

		RenderSystem.disableTexture();
		Vec3 vec3d = world.getSkyColor(mc.gameRenderer.getMainCamera().getPosition(), partialTicks);
		float f = (float) vec3d.x;
		float f1 = (float) vec3d.y;
		float f2 = (float) vec3d.z;

		FogRenderer.levelFogColor();
		Tesselator tessellator = Tesselator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuilder();
		RenderSystem.depthMask(false);
		RenderSystem.setShaderColor(f, f1, f2, 1.0F);

		ShaderInstance shaderinstance = RenderSystem.getShader();
		rg.skyBuffer.bind();
		rg.skyBuffer.drawWithShader(ms.last().pose(), RenderSystem.getProjectionMatrix(), shaderinstance);
		VertexBuffer.unbind();
		this.vertexBufferFormat.clearBufferState();

		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
//		RenderHelper.disableStandardItemLighting();
		/* TF - snip out sunrise/sunset since that doesn't happen here
         * float[] afloat = ...
         * if (afloat != null) ...
         */

		RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		ms.pushPose();
		ms.mulPose(Vector3f.YP.rotationDegrees(-90.0F));
		ms.mulPose(Vector3f.XP.rotationDegrees(world.getTimeOfDay(partialTicks) * 360.0F));
        /* TF - snip out sun/moon
         * float f17 = 30.0F;
         * ...
         * tessellator.draw();
         */
		float f15 = 1.0F; // TF - stars are always bright

		//if (f15 > 0.0F) { Always true
		RenderSystem.setShaderColor(f15, f15, f15, f15);

		this.starVBO.bind();
		this.starVBO.drawWithShader(ms.last().pose(), RenderSystem.getProjectionMatrix(), shaderinstance);
		VertexBuffer.unbind();
		this.vertexBufferFormat.clearBufferState();
		//}

		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.disableBlend();
		ms.popPose();
		RenderSystem.setShaderColor(0.0F, 0.0F, 0.0F, 1.0F);
		double d0 = mc.player.getEyePosition(partialTicks).y + (world.getSeaLevel() - 10);

		if (d0 < 0.0D) {
			ms.pushPose();
			ms.translate(0.0F, 12.0F, 0.0F);

			rg.darkBuffer.bind();
			rg.darkBuffer.drawWithShader(ms.last().pose(), RenderSystem.getProjectionMatrix(), shaderinstance);
			VertexBuffer.unbind();
			this.vertexBufferFormat.clearBufferState();

			ms.popPose();
//			float f18 = 1.0F;
			float f19 = -((float) (d0 + 65.0D));
//			float f20 = -1.0F;
			bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
			bufferbuilder.vertex(-1.0D, f19, 1.0D).color(0, 0, 0, 255).endVertex();
			bufferbuilder.vertex(1.0D, f19, 1.0D).color(0, 0, 0, 255).endVertex();
			bufferbuilder.vertex(1.0D, -1.0D, 1.0D).color(0, 0, 0, 255).endVertex();
			bufferbuilder.vertex(-1.0D, -1.0D, 1.0D).color(0, 0, 0, 255).endVertex();
			bufferbuilder.vertex(-1.0D, -1.0D, -1.0D).color(0, 0, 0, 255).endVertex();
			bufferbuilder.vertex(1.0D, -1.0D, -1.0D).color(0, 0, 0, 255).endVertex();
			bufferbuilder.vertex(1.0D, f19, -1.0D).color(0, 0, 0, 255).endVertex();
			bufferbuilder.vertex(-1.0D, f19, -1.0D).color(0, 0, 0, 255).endVertex();
			bufferbuilder.vertex(1.0D, -1.0D, -1.0D).color(0, 0, 0, 255).endVertex();
			bufferbuilder.vertex(1.0D, -1.0D, 1.0D).color(0, 0, 0, 255).endVertex();
			bufferbuilder.vertex(1.0D, f19, 1.0D).color(0, 0, 0, 255).endVertex();
			bufferbuilder.vertex(1.0D, f19, -1.0D).color(0, 0, 0, 255).endVertex();
			bufferbuilder.vertex(-1.0D, f19, -1.0D).color(0, 0, 0, 255).endVertex();
			bufferbuilder.vertex(-1.0D, f19, 1.0D).color(0, 0, 0, 255).endVertex();
			bufferbuilder.vertex(-1.0D, -1.0D, 1.0D).color(0, 0, 0, 255).endVertex();
			bufferbuilder.vertex(-1.0D, -1.0D, -1.0D).color(0, 0, 0, 255).endVertex();
			bufferbuilder.vertex(-1.0D, -1.0D, -1.0D).color(0, 0, 0, 255).endVertex();
			bufferbuilder.vertex(-1.0D, -1.0D, 1.0D).color(0, 0, 0, 255).endVertex();
			bufferbuilder.vertex(1.0D, -1.0D, 1.0D).color(0, 0, 0, 255).endVertex();
			bufferbuilder.vertex(1.0D, -1.0D, -1.0D).color(0, 0, 0, 255).endVertex();
			tessellator.end();
		}

		if (world.effects().hasGround()) {
			RenderSystem.setShaderColor(f * 0.2F + 0.04F, f1 * 0.2F + 0.04F, f2 * 0.6F + 0.1F, 1.0F);
		} else {
			RenderSystem.setShaderColor(f, f1, f2, 1.0F);
		}

		RenderSystem.enableTexture();
		RenderSystem.depthMask(true);
	}

	// [VanillaCopy] RenderGlobal.generateStars
	private void generateStars() {
		Tesselator tessellator = Tesselator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuilder();

		if (this.starVBO != null) {
			this.starVBO.close();
		}

		// TF - inlined RenderGlobal field that's only used once here
		this.starVBO = new VertexBuffer(/*DefaultVertexFormat.POSITION*/);
		this.renderStars(bufferbuilder);
		bufferbuilder.end();
		this.starVBO.upload(bufferbuilder);
	}

	// [VanillaCopy] of RenderGlobal.renderStars but with double the number of them
	@SuppressWarnings("unused")
	private void renderStars(BufferBuilder bufferBuilder) {
		Random random = new Random(10842L);
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
					bufferBuilder.vertex(d5 + d25, d6 + d23, d7 + d26).endVertex();
				}
			}
		}
	}
}
