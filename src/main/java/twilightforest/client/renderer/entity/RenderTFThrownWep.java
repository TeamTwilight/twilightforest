package twilightforest.client.renderer.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import twilightforest.entity.boss.EntityTFThrownWep;

public class RenderTFThrownWep extends Render<EntityTFThrownWep> {

	public RenderTFThrownWep(RenderManager manager) {
		super(manager);
	}

	@Override
	public void doRender(EntityTFThrownWep entity, double x, double y, double z, float yaw, float partialTicks) {

		GlStateManager.pushMatrix();

		float spin = (entity.ticksExisted + partialTicks) * -10F + 90F;


		GlStateManager.translate((float) x, (float) y, (float) z);
		GlStateManager.enableRescaleNormal();

		// size up
		GlStateManager.scale(1.25F, 1.25F, 1.25F);

		this.renderDroppedItem(entity.getItem(), yaw, spin);

		GlStateManager.disableRescaleNormal();
		GlStateManager.popMatrix();
	}

	// todo recheck transformations
	private void renderDroppedItem(ItemStack stack, float rotation, float spin) {
		GlStateManager.pushMatrix();

		float f9 = 0.5F;
		float f10 = 0.25F;

		GlStateManager.rotate(rotation + 270f, 0.0F, 1.0F, 0.0F);
		GlStateManager.rotate(spin, 0.0F, 0.0F, 1.0F);

		float f12 = 0.0625F;
		float f11 = 0.021875F;

		GlStateManager.translate(-f9, -f10, -(f12 + f11));
		GlStateManager.translate(0f, 0f, f12 + f11);

		Minecraft.getMinecraft().getRenderItem().renderItem(stack, ItemCameraTransforms.TransformType.GROUND);

		GlStateManager.popMatrix();
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityTFThrownWep entity) {
		return TextureMap.LOCATION_BLOCKS_TEXTURE;
	}
}
