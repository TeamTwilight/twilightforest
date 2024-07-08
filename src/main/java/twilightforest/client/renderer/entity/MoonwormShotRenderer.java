package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.entity.MoonwormModel;
import twilightforest.entity.projectile.MoonwormShot;

public class MoonwormShotRenderer extends EntityRenderer<MoonwormShot> {

	private static final ResourceLocation TEXTURE = TwilightForestMod.getModelTexture("moonworm.png");
	private final MoonwormModel wormModel;

	public MoonwormShotRenderer(EntityRendererProvider.Context context) {
		super(context);
		this.shadowRadius = 0.25F;

		this.wormModel = new MoonwormModel(context.bakeLayer(TFModelLayers.MOONWORM));
	}

	@Override
	public void render(MoonwormShot entity, float yaw, float partialTicks, PoseStack stack, MultiBufferSource buffer, int light) {
		stack.pushPose();
		stack.translate(0.0F, 0.5F, 0.0F);
		stack.scale(-1.0F, -1.0F, -1.0F);

		stack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTicks, entity.yRotO, entity.getYRot()) - 180.0F));
		stack.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(partialTicks, entity.xRotO, entity.getXRot())));

		VertexConsumer consumer = buffer.getBuffer(this.wormModel.renderType(TEXTURE));
		this.wormModel.renderToBuffer(stack, consumer, light, OverlayTexture.NO_OVERLAY);

		stack.popPose();
	}

	@Override
	public ResourceLocation getTextureLocation(MoonwormShot entity) {
		return TEXTURE;
	}
}
