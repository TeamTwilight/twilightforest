package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.entity.HydraHeadModel;
import twilightforest.client.state.HydraHeadRenderState;
import twilightforest.entity.boss.Hydra;
import twilightforest.entity.boss.HydraHead;
import twilightforest.entity.boss.HydraHeadContainer;

public class HydraHeadRenderer extends TFPartRenderer<HydraHead, HydraHeadRenderState, HydraHeadModel> {

	private static final ResourceLocation TEXTURE = TwilightForestMod.getModelTexture("hydra4.png");

	public HydraHeadRenderer(EntityRendererProvider.Context context, HydraHeadModel model) {
		super(context, model);
	}

	@Override
	public void render(HydraHeadRenderState state, PoseStack stack, MultiBufferSource buffer, int light) {
		stack.mulPose(Axis.YP.rotationDegrees(-180));
		super.render(state, stack, buffer, light);
	}

	@Override
	protected @Nullable RenderType getRenderType(HydraHeadRenderState state, boolean visible, boolean ghostly, boolean glowing) {
		// see whether we want to render these
		if (!state.active) return null;
		return super.getRenderType(state, visible, ghostly, glowing);
	}

	@Override
	protected boolean shouldShowName(HydraHead entity, double partialTick) {
		return entity.hasCustomName() && !entity.getCustomName().getString().isEmpty();
	}

	@Override
	protected void renderNameTag(HydraHeadRenderState state, Component component, PoseStack stack, MultiBufferSource source, int light) {
		Vec3 vec3 = state.nameTagAttachment;
		if (vec3 != null) {
			boolean flag = !state.isDiscrete;
			stack.pushPose();
			stack.translate(vec3.x, vec3.y + 0.5, vec3.z);
			stack.mulPose(this.entityRenderDispatcher.cameraOrientation());
			stack.scale(0.025F, -0.025F, 0.025F);
			Matrix4f matrix4f = stack.last().pose();
			Font font = this.getFont();
			float f = (float)(-font.width(component)) / 2.0F;
			int j = (int)(Minecraft.getInstance().options.getBackgroundOpacity(0.25F) * 255.0F) << 24;
			font.drawInBatch(component, f, 0, -2130706433, false, matrix4f, source, flag ? Font.DisplayMode.SEE_THROUGH : Font.DisplayMode.NORMAL, j, light);
			if (flag) {
				font.drawInBatch(component, f, 0, -1, false, matrix4f, source, Font.DisplayMode.NORMAL, 0, LightTexture.lightCoordsWithEmission(light, 2));
			}

			stack.popPose();
		}
	}

	@Override
	public HydraHeadRenderState createRenderState() {
		return new HydraHeadRenderState();
	}

	@Override
	public void extractRenderState(HydraHead entity, HydraHeadRenderState state, float partialTick) {
		super.extractRenderState(entity, state, partialTick);
		var container = getHeadObject(entity);
		state.active = container == null || entity.isActive();
		state.mouthAngle = Mth.lerp(partialTick, entity.getMouthOpenLast(), entity.getMouthOpen());
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
	public ResourceLocation getTextureLocation(HydraHeadRenderState state) {
		return TEXTURE;
	}
}
