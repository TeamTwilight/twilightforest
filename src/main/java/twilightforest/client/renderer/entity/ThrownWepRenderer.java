package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;
import twilightforest.client.state.entity.ThrownWepRenderState;
import twilightforest.entity.projectile.ThrownWep;

public class ThrownWepRenderer extends EntityRenderer<ThrownWep, ThrownWepRenderState> {

	private final ItemModelResolver resolver;

	public ThrownWepRenderer(EntityRendererProvider.Context context) {
		super(context);
		this.resolver = context.getItemModelResolver();
	}

	@Override
	public void submit(ThrownWepRenderState state, PoseStack stack, SubmitNodeCollector collector, CameraRenderState camera) {
		stack.pushPose();
		float spin = state.ageInTicks * 10.0F;
		// size up
		stack.scale(1.25F, 1.25F, 1.25F);
		stack.mulPose(Axis.YP.rotationDegrees(state.yRot + 90.0F));
		stack.mulPose(Axis.ZP.rotationDegrees(spin));
		float f9 = 0.5F;
		float f10 = 0.25F;
		float f12 = 0.0625F;
		float f11 = 0.021875F;
		stack.translate(-f9, -f10, -(f12 + f11));
		stack.translate(0.0F, 0.0F, f12 + f11);

		if (state.item.isEmpty()) {
			state.item.submit(stack, collector, state.lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor);
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
