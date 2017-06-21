package twilightforest.client.renderer;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.ModelTFFirefly;
import twilightforest.tileentity.TileEntityTFFirefly;

import javax.annotation.Nullable;


public class TileEntityTFFireflyRenderer extends TileEntitySpecialRenderer<TileEntityTFFirefly> {

	private ModelTFFirefly fireflyModel;
	private static final ResourceLocation textureLoc = new ResourceLocation(TwilightForestMod.MODEL_DIR + "firefly-tiny.png");

	public TileEntityTFFireflyRenderer() {
		fireflyModel = new ModelTFFirefly();
	}


	@Override
	public void renderTileEntityAt(@Nullable TileEntityTFFirefly tileentity, double d, double d1, double d2, float partialTicks, int destroyStage) {
		GlStateManager.pushMatrix();
		EnumFacing facing = EnumFacing.getFront(tileentity != null ? tileentity.getBlockMetadata() : 0);

		float rotX = 90.0F;
		float rotZ = 0.0F;
		if (facing == EnumFacing.SOUTH) {
			rotZ = 0F;
		}
		if (facing == EnumFacing.NORTH) {
			rotZ = 180F;
		}
		if (facing == EnumFacing.EAST) {
			rotZ = -90F;
		}
		if (facing == EnumFacing.WEST) {
			rotZ = 90F;
		}
		if (facing == EnumFacing.UP) {
			rotX = 0F;
		}
		if (facing == EnumFacing.DOWN) {
			rotX = 180F;
		}
		GlStateManager.translate((float) d + 0.5F, (float) d1 + 0.5F, (float) d2 + 0.5F);
		GlStateManager.rotate(rotX, 1F, 0F, 0F);
		GlStateManager.rotate(rotZ, 0F, 0F, 1F);
		GlStateManager.rotate(tileentity != null ? tileentity.currentYaw : 0, 0F, 1F, 0F);

		this.bindTexture(textureLoc);
		GlStateManager.pushMatrix();
		GlStateManager.scale(1f, -1f, -1f);

		GlStateManager.colorMask(true, true, true, true);

		// render the firefly body
		GlStateManager.disableBlend();
		fireflyModel.render(0.0625f);

//        
//        GL11.glEnable(3042 /*GL_BLEND*/);
//        GL11.glDisable(3008 /*GL_ALPHA_TEST*/);
//        GL11.glBlendFunc(770, 771);
//        GL11.glColor4f(1.0F, 1.0F, 1.0F, f1);

		// render the firefly glow
		GlStateManager.enableBlend();
		GlStateManager.disableAlpha();
		GlStateManager.disableLighting();
		GlStateManager.color(1.0F, 1.0F, 1.0F, tileentity != null ? tileentity.glowIntensity : 0);
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, 1);
		fireflyModel.glow.render(0.0625f);
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GlStateManager.disableBlend();
		GlStateManager.enableAlpha();
		GlStateManager.enableLighting();

		GlStateManager.popMatrix();
		GlStateManager.color(1, 1, 1, 1);
		GlStateManager.popMatrix();
	}

}
