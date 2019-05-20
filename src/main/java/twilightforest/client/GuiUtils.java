package twilightforest.client;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

public class GuiUtils {

	// [VanillaCopy] of Gui.drawModalRectWithCustomSizedTexture, but accepting a z-level parameter
	public static void drawModalRectWithCustomSizedTexture(int x, int y, float z, float u, float v, int width, int height, float textureWidth, float textureHeight) {
		float f = 1.0F / textureWidth;
		float f1 = 1.0F / textureHeight;
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
		bufferbuilder.pos((double)x, (double)(y + height), (double)z).tex((double)(u * f), (double)((v + (float)height) * f1)).endVertex();
		bufferbuilder.pos((double)(x + width), (double)(y + height), (double)z).tex((double)((u + (float)width) * f), (double)((v + (float)height) * f1)).endVertex();
		bufferbuilder.pos((double)(x + width), (double)y, (double)z).tex((double)((u + (float)width) * f), (double)(v * f1)).endVertex();
		bufferbuilder.pos((double)x, (double)y, (double)z).tex((double)(u * f), (double)(v * f1)).endVertex();
		tessellator.draw();
	}
}
