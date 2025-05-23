package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.Nullable;
import twilightforest.client.state.PartEntityState;
import twilightforest.entity.TFPart;

public abstract class TFPartRenderer<T extends TFPart<?>, S extends PartEntityState, M extends EntityModel<S>> extends EntityRenderer<T, S> {

	protected final M model;

	public TFPartRenderer(EntityRendererProvider.Context context, M model) {
		super(context);
		this.model = model;
	}

	@Override
	public void render(S state, PoseStack stack, MultiBufferSource buffer, int light) {
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
			VertexConsumer consumer = buffer.getBuffer(rendertype);
			int overlay = this.getOverlayCoords(state);
			int j = ghostly ? 654311423 : -1;
			int k = ARGB.multiply(j, this.getModelTint(state));
			this.model.renderToBuffer(stack, consumer, light, overlay, k);
		}

		stack.popPose();
		super.render(state, stack, buffer, light);
	}

	protected int getModelTint(S state) {
		return -1;
	}

	private int getOverlayCoords(PartEntityState state) {
		return OverlayTexture.pack(OverlayTexture.u(OverlayTexture.NO_WHITE_U), OverlayTexture.v(state.hasRedOverlay));
	}

	@Nullable
	protected RenderType getRenderType(S state, boolean visible, boolean ghostly, boolean glowing) {
		ResourceLocation resourcelocation = this.getTextureLocation(state);
		if (ghostly) {
			return RenderType.itemEntityTranslucentCull(resourcelocation);
		} else if (visible) {
			return this.model.renderType(resourcelocation);
		} else {
			return glowing ? RenderType.outline(resourcelocation) : null;
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
		state.yRot = entity.getYRot();
		state.yRotO = entity.yRotO;
		state.xRot = entity.getXRot(partialTick);
		state.customName = entity.getCustomName();
		state.isUpsideDown = this.isEntityUpsideDown(entity);
		if (state.isUpsideDown) {
			state.xRot *= -1.0F;
			state.yRot *= -1.0F;
		}

		state.isInWater = entity.isInWater() || entity.isInFluidType((fluidType, height) -> entity.canSwimInFluidType(fluidType));
		state.hasRedOverlay = entity.hurtTime > 0 || entity.deathTime > 0;
		state.deathTime = entity.deathTime > 0 ? (float)entity.deathTime + partialTick : 0.0F;
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

	public abstract ResourceLocation getTextureLocation(S state);
}
