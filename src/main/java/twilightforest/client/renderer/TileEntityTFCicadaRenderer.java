package twilightforest.client.renderer;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import twilightforest.TwilightForestMod;
import twilightforest.client.BugModelAnimationHelper;
import twilightforest.client.model.entity.ModelTFCicada;
import twilightforest.tileentity.critters.TileEntityTFCicadaTicking;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class TileEntityTFCicadaRenderer extends TileEntitySpecialRenderer<TileEntityTFCicadaTicking> {
	private final ModelTFCicada cicadaModel = new ModelTFCicada();
	private static final ResourceLocation textureLoc = new ResourceLocation(TwilightForestMod.MODEL_DIR + "cicada-model.png");

	@Override
	public void render(@Nullable TileEntityTFCicadaTicking tileentity, double d, double d1, double d2, float partialTicks, int destroyStage, float alpha) {
		int yaw = tileentity != null ? tileentity.currentYaw : BugModelAnimationHelper.currentYaw;

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
