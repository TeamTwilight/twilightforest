package twilightforest.compat.undergarden;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import quek.undergarden.entity.projectile.slingshot.SlingshotProjectile;

public class BugProjectileRenderer extends ThrownItemRenderer<SlingshotProjectile> {

	private final ResourceLocation textureLoc;
	private final Model bugModel;

	public BugProjectileRenderer(EntityRendererProvider.Context ctx, Model bugModel, ResourceLocation texture) {
		super(ctx);
		this.bugModel = bugModel;
		this.textureLoc = texture;
	}

	@Override
	public void render(SlingshotProjectile entity, float yaw, float partialTicks, PoseStack stack, MultiBufferSource buffer, int light) {
		stack.pushPose();
		stack.translate(0.0F, 0.5F, 0.0F);
		stack.scale(-1.0f, -1.0f, -1.0f);

		stack.mulPose(Vector3f.YP.rotationDegrees(Mth.lerp(partialTicks, entity.yRotO, entity.getYRot()) + 180.0F));
		stack.mulPose(Vector3f.XP.rotationDegrees(Mth.lerp(partialTicks, entity.xRotO, entity.getXRot())));
		stack.translate(0.0F, -0.25F, 0.0F);
		VertexConsumer builder = buffer.getBuffer(this.bugModel.renderType(textureLoc));
		this.bugModel.renderToBuffer(stack, builder, entity instanceof MoonwormSlingshotProjectile ? 0xF000F0 : light, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);

		stack.popPose();
	}
}
