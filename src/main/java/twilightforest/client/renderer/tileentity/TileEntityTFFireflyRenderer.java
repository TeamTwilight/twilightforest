package twilightforest.client.renderer.tileentity;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import twilightforest.TwilightForestMod;
import twilightforest.client.BugModelAnimationHelper;
import twilightforest.client.model.entity.ModelTFFirefly;
import twilightforest.tileentity.critters.TileEntityTFFireflyTicking;

import javax.annotation.Nullable;

public class TileEntityTFFireflyRenderer extends TileEntitySpecialRenderer<TileEntityTFFireflyTicking> {

	private final ModelTFFirefly fireflyModel = new ModelTFFirefly();
	private static final ResourceLocation textureLoc = TwilightForestMod.getModelTexture("firefly-tiny.png");

	@Override
	public void render(@Nullable TileEntityTFFireflyTicking te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {

		int yaw = te != null ? te.currentYaw : BugModelAnimationHelper.currentYaw;
		float glow = te != null ? te.glowIntensity : BugModelAnimationHelper.glowIntensity;

		GlStateManager.pushMatrix();
		EnumFacing facing = EnumFacing.byIndex(te != null ? te.getBlockMetadata() : 0);

		float rotX = 90.0F;
		float rotZ = 0.0F;
		if (facing == EnumFacing.SOUTH) {
			rotZ = 0F;
		} else if (facing == EnumFacing.NORTH) {
			rotZ = 180F;
		} else if (facing == EnumFacing.EAST) {
			rotZ = -90F;
		} else if (facing == EnumFacing.WEST) {
			rotZ = 90F;
		} else if (facing == EnumFacing.UP) {
			rotX = 0F;
		} else if (facing == EnumFacing.DOWN) {
			rotX = 180F;
		}
		GlStateManager.translate((float) x + 0.5F, (float) y + 0.5F, (float) z + 0.5F);
		GlStateManager.rotate(rotX, 1F, 0F, 0F);
		GlStateManager.rotate(rotZ, 0F, 0F, 1F);
		GlStateManager.rotate(yaw, 0F, 1F, 0F);

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
		GlStateManager.color(1.0F, 1.0F, 1.0F, glow);
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
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
