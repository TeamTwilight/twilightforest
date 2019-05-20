package twilightforest.client.renderer.tileentity;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import twilightforest.TwilightForestMod;
import twilightforest.client.BugModelAnimationHelper;
import twilightforest.client.model.entity.ModelTFCicada;
import twilightforest.tileentity.critters.TileEntityTFCicadaTicking;

import javax.annotation.Nullable;

public class TileEntityTFCicadaRenderer extends TileEntitySpecialRenderer<TileEntityTFCicadaTicking> {

	private final ModelTFCicada cicadaModel = new ModelTFCicada();
	private static final ResourceLocation textureLoc = TwilightForestMod.getModelTexture("cicada-model.png");

	@Override
	public void render(@Nullable TileEntityTFCicadaTicking te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {

		int yaw = te != null ? te.currentYaw : BugModelAnimationHelper.currentYaw;

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
		cicadaModel.render(0.0625f);
		GlStateManager.popMatrix();
		GlStateManager.color(1, 1, 1, 1);
		GlStateManager.popMatrix();
	}
}
