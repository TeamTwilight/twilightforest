package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import twilightforest.entity.projectile.ThrownWep;

public class ThrownWepRenderer extends EntityRenderer<ThrownWep> {

	public ThrownWepRenderer(EntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	public void render(ThrownWep entity, float yaw, float partialTicks, PoseStack stack, MultiBufferSource buffer, int light) {
		stack.pushPose();

		float spin = (entity.tickCount + partialTicks) * 10.0F;

		// size up
		stack.scale(1.25F, 1.25F, 1.25F);

		this.renderDroppedItem(stack, buffer, light, entity.getItem(), yaw, spin);

		stack.popPose();
	}

	private void renderDroppedItem(PoseStack stack, MultiBufferSource buffer, int light, ItemStack item, float rotation, float spin) {
		stack.pushPose();

		float f9 = 0.5F;
		float f10 = 0.25F;

		stack.mulPose(Axis.YP.rotationDegrees(rotation + 90.0F));
		stack.mulPose(Axis.ZP.rotationDegrees(spin));

		float f12 = 0.0625F;
		float f11 = 0.021875F;

		stack.translate(-f9, -f10, -(f12 + f11));
		stack.translate(0.0F, 0.0F, f12 + f11);

		Minecraft.getInstance().getItemRenderer().renderStatic(item, ItemDisplayContext.GROUND, light, OverlayTexture.NO_OVERLAY, stack, buffer, null, 0);

		stack.popPose();
	}

	@Override
	public ResourceLocation getTextureLocation(ThrownWep entity) {
		return InventoryMenu.BLOCK_ATLAS;
	}
}
