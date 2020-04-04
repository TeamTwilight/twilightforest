package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import twilightforest.entity.boss.EntityTFThrownWep;

public class RenderTFThrownWep<T extends EntityTFThrownWep> extends EntityRenderer<T> {

	public RenderTFThrownWep(EntityRendererManager manager) {
		super(manager);
	}

	@Override
	public void render(T entity, float yaw, float partialTicks, MatrixStack stack, IRenderTypeBuffer buffer, int light) {
		stack.push();

		float spin = (entity.ticksExisted + partialTicks) * -10F + 90F;

		stack.translate((float) x, (float) y, (float) z);
		RenderSystem.enableRescaleNormal();

		// size up
		stack.scale(1.25F, 1.25F, 1.25F);

		this.renderDroppedItem(stack, buffer, entity.getItem(), yaw, spin);

		RenderSystem.disableRescaleNormal();
		stack.pop();
	}

	// todo recheck transformations
	private void renderDroppedItem(MatrixStack matrix, IRenderTypeBuffer buffer, ItemStack stack, float rotation, float spin) {
		matrix.push();

		float f9 = 0.5F;
		float f10 = 0.25F;

		RenderSystem.rotatef(rotation + 270f, 0.0F, 1.0F, 0.0F);
		RenderSystem.rotatef(spin, 0.0F, 0.0F, 1.0F);

		float f12 = 0.0625F;
		float f11 = 0.021875F;

		matrix.translate(-f9, -f10, -(f12 + f11));
		matrix.translate(0f, 0f, f12 + f11);

		Minecraft.getInstance().getItemRenderer().renderItem(stack, ItemCameraTransforms.TransformType.GROUND, matrix, buffer);

		matrix.pop();
	}

	@Override
	public ResourceLocation getEntityTexture(T entity) {
		return AtlasTexture.LOCATION_BLOCKS_TEXTURE;
	}
}
