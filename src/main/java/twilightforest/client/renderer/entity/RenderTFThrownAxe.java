package twilightforest.client.renderer.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;


public class RenderTFThrownAxe extends Render<Entity> {
	private final ItemStack myItem;

	public RenderTFThrownAxe(RenderManager manager, Item knightlyAxe) {
		super(manager);
		this.myItem = new ItemStack(knightlyAxe);
	}

	@Override
	public void doRender(Entity entity, double par2, double par4, double par6, float par8, float par9) {

		GlStateManager.pushMatrix();

		float spin = (entity.ticksExisted + par9) * -10F + 90F;


		GlStateManager.translate((float) par2, (float) par4, (float) par6);
		GlStateManager.enableRescaleNormal();

		// size up
		GlStateManager.scale(1.25F, 1.25F, 1.25F);

		this.renderDroppedItem(par8, spin);

		GlStateManager.disableRescaleNormal();
		GlStateManager.popMatrix();
	}

	// todo recheck transformations
	private void renderDroppedItem(float rotation, float spin) {
		GlStateManager.pushMatrix();

		float f9 = 0.5F;
		float f10 = 0.25F;

		GlStateManager.rotate(rotation + 270f, 0.0F, 1.0F, 0.0F);
		GlStateManager.rotate(spin, 0.0F, 0.0F, 1.0F);

		float f12 = 0.0625F;
		float f11 = 0.021875F;

		GlStateManager.translate(-f9, -f10, -(f12 + f11));
		GlStateManager.translate(0f, 0f, f12 + f11);

		Minecraft.getMinecraft().getRenderItem().renderItem(myItem, ItemCameraTransforms.TransformType.GROUND);

		GlStateManager.popMatrix();
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity entity) {
		return TextureMap.LOCATION_BLOCKS_TEXTURE;
	}
}
