package twilightforest.client.renderer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import twilightforest.TwilightForestMod;
import twilightforest.client.BugModelAnimationHelper;
import twilightforest.client.model.entity.ModelTFMoonworm;
import twilightforest.tileentity.critters.TileEntityTFMoonwormTicking;

import javax.annotation.Nullable;


public class TileEntityTFMoonwormRenderer extends TileEntitySpecialRenderer<TileEntityTFMoonwormTicking> {
	private static final ResourceLocation textureLoc = new ResourceLocation(TwilightForestMod.MODEL_DIR + "moonworm.png");
	private final ModelTFMoonworm moonwormModel = new ModelTFMoonworm();

	@Override
	public void render(@Nullable TileEntityTFMoonwormTicking tileentity, double d, double d1, double d2, float partialTime, int destroyStage, float alpha) {
		int yaw = tileentity != null ? tileentity.currentYaw : BugModelAnimationHelper.currentRotation;
		if (tileentity == null) partialTime = Minecraft.getMinecraft().getRenderPartialTicks();

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
		GlStateManager.translate(d + 0.5F, d1 + 0.5F, d2 + 0.5F);
		GlStateManager.rotate(rotX, 1F, 0F, 0F);
		GlStateManager.rotate(rotZ, 0F, 0F, 1F);
		GlStateManager.rotate(yaw, 0F, 1F, 0F);

		this.bindTexture(textureLoc);

		GlStateManager.scale(1f, -1f, -1f);

		moonwormModel.setLivingAnimations(tileentity, partialTime);

		// render the firefly body
		moonwormModel.render(0.0625f);

		GlStateManager.popMatrix();
	}

}
