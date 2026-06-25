package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.Nullable;
import twilightforest.client.state.entity.PartEntityState;
import twilightforest.entity.TFPart;

public abstract class TFPartRenderer<T extends TFPart<?>, S extends PartEntityState, M extends EntityModel<S>> extends EntityRenderer<T, S> {

	protected final M model;

	public TFPartRenderer(EntityRendererProvider.Context context, M model) {
		super(context);
		this.model = model;
	}

	@Override
	public void submit(S state, PoseStack stack, SubmitNodeCollector buffer, CameraRenderState cameraRenderState) {
		stack.pushPose();

		this.setupRotations(state, stack, state.partialTick);
		stack.scale(-1.0F, -1.0F, 1.0F);
		stack.translate(0.0D, -1.501F, 0.0D);
		this.model.setupAnim(state);
		boolean visible = !state.isInvisible;
		boolean ghostly = !visible && !state.isInvisibleToPlayer;
		boolean glowing = state.appearsGlowing;
		RenderType rendertype = this.getRenderType(state, visible, ghostly, glowing);
		if (rendertype != null) {
			int overlay = this.getOverlayCoords(state);
			int j = ghostly ? 654311423 : -1;
			int k = ARGB.multiply(j, this.getModelTint(state));
			buffer.submitModel(this.model, state, stack, rendertype, state.lightCoords, overlay, k, null, state.outlineColor, null);
		}

		stack.popPose();
		super.submit(state, stack, buffer, cameraRenderState);
	}

	protected int getModelTint(S state) {
		return -1;
	}

	private int getOverlayCoords(PartEntityState state) {
		return OverlayTexture.pack(OverlayTexture.u(OverlayTexture.NO_WHITE_U), OverlayTexture.v(state.hasRedOverlay));
	}

	@Nullable
	protected RenderType getRenderType(S state, boolean visible, boolean ghostly, boolean glowing) {
		Identifier Identifier = this.getTextureLocation(state);
		if (ghostly) {
			return RenderTypes.itemTranslucent(Identifier);
		} else if (visible) {
			return this.model.renderType(Identifier);
		} else {
			return glowing ? RenderTypes.outline(Identifier) : null;
		}
	}

	protected void setupRotations(S state, PoseStack stack, float partialTicks) {
		if (state.deathTime > 0) {
			float f = (state.deathTime + partialTicks - 1.0F) / 20.0F * 1.6F;
			f = Mth.sqrt(f);
			if (f > 1.0F) {
				f = 1.0F;
			}

			stack.mulPose(Axis.ZP.rotationDegrees(f * this.getFlipDegrees()));
		} else if (state.isUpsideDown) {
			stack.translate(0.0F, (state.boundingBoxHeight + 0.1F) / partialTicks, 0.0F);
			stack.mulPose(Axis.ZP.rotationDegrees(180.0F));
		}
	}

	protected float getFlipDegrees() {
		return 90.0F;
	}

	@Override
	public void extractRenderState(T entity, S state, float partialTick) {
		super.extractRenderState(entity, state, partialTick);
		state.partRendererId = entity.renderer();
		state.yRot = entity.getYRot();
		state.yRotO = entity.yRotO;
		state.xRot = entity.getXRot(partialTick);
		state.customName = entity.getCustomName();
		state.isUpsideDown = this.isEntityUpsideDown(entity);
		if (state.isUpsideDown) {
			state.xRot *= -1.0F;
			state.yRot *= -1.0F;
		}

		state.isInWater = entity.isInWater()/* || entity.isInFluidType((fluidType, height) -> entity.canSwimInFluidType(fluidType))*/;
		state.hasRedOverlay = entity.hurtTime > 0 || entity.deathTime > 0;
		state.deathTime = entity.deathTime > 0 ? (float) entity.deathTime + partialTick : 0.0F;
		Minecraft minecraft = Minecraft.getInstance();
		state.isInvisibleToPlayer = state.isInvisible && entity.isInvisibleTo(minecraft.player);
		state.appearsGlowing = minecraft.shouldEntityAppearGlowing(entity);
	}

	private boolean isEntityUpsideDown(T entity) {
		if (entity.hasCustomName()) {
			String s = ChatFormatting.stripFormatting(entity.getName().getString());
			return "Dinnerbone".equalsIgnoreCase(s) || "Grumm".equalsIgnoreCase(s);
		}
		return false;
	}

	public abstract Identifier getTextureLocation(S state);
}
