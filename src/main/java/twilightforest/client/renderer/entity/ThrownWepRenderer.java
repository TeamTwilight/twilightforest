package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import twilightforest.client.state.ThrownWepRenderState;
import twilightforest.entity.projectile.ThrownWep;

public class ThrownWepRenderer extends EntityRenderer<ThrownWep, ThrownWepRenderState> {

	private final ItemModelResolver resolver;

	public ThrownWepRenderer(EntityRendererProvider.Context context) {
		super(context);
		this.resolver = context.getItemModelResolver();
	}

	@Override
	public void render(ThrownWepRenderState state, PoseStack stack, MultiBufferSource buffer, int light) {
		stack.pushPose();
		float spin = state.ageInTicks * 10.0F;
		// size up
		stack.scale(1.25F, 1.25F, 1.25F);
		this.renderDroppedItem(stack, buffer, light, state, spin);
		stack.popPose();
	}

	private void renderDroppedItem(PoseStack stack, MultiBufferSource buffer, int light, ThrownWepRenderState state, float spin) {
		stack.pushPose();
		stack.mulPose(Axis.YP.rotationDegrees(state.yRot + 90.0F));
		stack.mulPose(Axis.ZP.rotationDegrees(spin));
		float f9 = 0.5F;
		float f10 = 0.25F;
		float f12 = 0.0625F;
		float f11 = 0.021875F;

		stack.translate(-f9, -f10, -(f12 + f11));
		stack.translate(0.0F, 0.0F, f12 + f11);

		if (state.item.isEmpty()) {
			state.item.render(stack, buffer, light, OverlayTexture.NO_OVERLAY);
		}
		stack.popPose();
	}

	@Override
	public ThrownWepRenderState createRenderState() {
		return new ThrownWepRenderState();
	}

	@Override
	public void extractRenderState(ThrownWep entity, ThrownWepRenderState state, float partialTick) {
		super.extractRenderState(entity, state, partialTick);
		this.resolver.updateForNonLiving(state.item, entity.getItem(), ItemDisplayContext.GROUND, entity);
		state.yRot = entity.getYRot(partialTick);
	}
}
