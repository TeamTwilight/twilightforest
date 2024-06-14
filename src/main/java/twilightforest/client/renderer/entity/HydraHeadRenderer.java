package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.model.ListModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityAttachment;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.ClientHooks;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import twilightforest.TwilightForestMod;
import twilightforest.entity.boss.Hydra;
import twilightforest.entity.boss.HydraHead;
import twilightforest.entity.boss.HydraHeadContainer;

public class HydraHeadRenderer<T extends HydraHead, M extends ListModel<T>> extends TFPartRenderer<T, M> {

	private static final ResourceLocation TEXTURE = TwilightForestMod.getModelTexture("hydra4.png");


	public HydraHeadRenderer(EntityRendererProvider.Context context, M model) {
		super(context, model);
	}

	@Override
	public void render(T entity, float yaw, float partialTicks, PoseStack stack, MultiBufferSource buffer, int light) {
		// get the HydraHeadContainer that we're taking about
		HydraHeadContainer headCon = getHeadObject(entity);

		if (headCon != null) {
			// see whether we want to render these
			if (entity.isActive()) {
				stack.mulPose(Axis.YP.rotationDegrees(-180));
				super.render(entity, yaw, partialTicks, stack, buffer, light);
			}

		} else {
			super.render(entity, yaw, partialTicks, stack, buffer, light);
		}
	}

	@Override
	protected boolean shouldShowName(T entity) {
		return entity.hasCustomName() && !entity.getCustomName().getString().isEmpty();
	}

	@Override
	protected void renderNameTag(T entity, Component component, PoseStack stack, MultiBufferSource source, int light, float scale) {
		double d0 = this.entityRenderDispatcher.distanceToSqr(entity);
		if (ClientHooks.isNameplateInRenderDistance(entity, d0)) {
			Vec3 vec3 = entity.getAttachments().getNullable(EntityAttachment.NAME_TAG, 0, entity.getViewYRot(scale));
			if (vec3 != null) {
				boolean flag = !entity.isDiscrete();
				stack.pushPose();
				stack.translate(vec3.x, vec3.y + 0.5, vec3.z);
				stack.mulPose(Axis.YP.rotationDegrees(180.0F));
				stack.mulPose(this.entityRenderDispatcher.cameraOrientation());
				stack.scale(-0.05F, -0.05F, 0.05F);
				Matrix4f matrix4f = stack.last().pose();
				float f1 = Minecraft.getInstance().options.getBackgroundOpacity(0.25F);
				int j = (int) (f1 * 255.0F) << 24;
				Font font = this.getFont();
				float f2 = (float) (-font.width(component) / 2);
				font.drawInBatch(component, f2, (float) 0, 553648127, false, matrix4f, source, flag ? Font.DisplayMode.SEE_THROUGH : Font.DisplayMode.NORMAL, j, light);
				if (flag) {
					font.drawInBatch(component, f2, (float) 0, -1, false, matrix4f, source, Font.DisplayMode.NORMAL, 0, light);
				}

				stack.popPose();
			}
		}
	}

	@Nullable
	public static HydraHeadContainer getHeadObject(HydraHead entity) {
		Hydra hydra = entity.getParent();

		if (hydra != null) {
			for (int i = 0; i < Hydra.MAX_HEADS; i++) {
				if (hydra.hc[i].headEntity == entity) {
					return hydra.hc[i];
				}
			}
		}
		return null;
	}

	@Override
	public ResourceLocation getTextureLocation(T entity) {
		return TEXTURE;
	}
}
