package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ListModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;
import twilightforest.entity.TFPart;

@SuppressWarnings("unused")
public abstract class TFPartRenderer<T extends TFPart<?>, M extends ListModel<T>> extends EntityRenderer<T> {

	protected final M model;

	public TFPartRenderer(EntityRendererProvider.Context context, M model) {
		super(context);
		this.model = model;
	}

	@Override
	public void render(T entity, float entityYaw, float partialTicks, PoseStack stack, MultiBufferSource buffer, int light) {
		stack.pushPose();

		float yRot = Mth.rotLerp(partialTicks, entity.prevRenderYawOffset, entity.renderYawOffset);
		float xRot = Mth.lerp(partialTicks, entity.xRotO, entity.getXRot());

		float ageInTicks = this.getBob(entity, partialTicks);
		this.setupRotations(entity, stack, ageInTicks, yRot, partialTicks);
		stack.scale(-1.0F, -1.0F, 1.0F);
		stack.translate(0.0D, -1.501F, 0.0D);

		this.model.prepareMobModel(entity, 0.0F, 0.0F, partialTicks);
		this.model.setupAnim(entity, 0.0F, 0.0F, ageInTicks, yRot, xRot);
		boolean visible = this.isBodyVisible(entity);
		boolean ghostly = !visible && !entity.isInvisibleTo(Minecraft.getInstance().player);
		boolean glowing = Minecraft.getInstance().shouldEntityAppearGlowing(entity);
		RenderType rendertype = this.getRenderType(entity, visible, ghostly, glowing);
		if (rendertype != null) {
			VertexConsumer consumer = buffer.getBuffer(rendertype);
			int overlay = this.getOverlayCoords(entity, this.getWhiteOverlayProgress(entity, partialTicks));
			this.model.renderToBuffer(stack, consumer, light, overlay, 1.0F, 1.0F, 1.0F, ghostly ? 0.15F : 1.0F);
		}

		stack.popPose();
		super.render(entity, entityYaw, partialTicks, stack, buffer, light);
	}

	protected float getWhiteOverlayProgress(T entity, float partialTicks) {
		return 0.0F;
	}

	public int getOverlayCoords(T entity, float uIn) {
		if (entity.getParent() instanceof LivingEntity living)
			return OverlayTexture.pack(OverlayTexture.u(uIn), OverlayTexture.v(living.hurtTime > 0 || living.deathTime > 0 || entity.hurtTime > 0 || entity.deathTime > 0));
		return OverlayTexture.pack(OverlayTexture.u(uIn), OverlayTexture.v(entity.hurtTime > 0 || entity.deathTime > 0));
	}

	@Nullable
	protected RenderType getRenderType(T entity, boolean visible, boolean ghostly, boolean glowing) {
		ResourceLocation resourcelocation = this.getTextureLocation(entity);
		if (ghostly) {
			return RenderType.itemEntityTranslucentCull(resourcelocation);
		} else if (visible) {
			return this.model.renderType(resourcelocation);
		} else {
			return glowing ? RenderType.outline(resourcelocation) : null;
		}
	}

	protected float getBob(T entity, float partialTicks) {
		return (float)entity.tickCount + partialTicks;
	}

	protected void setupRotations(T entity, PoseStack stack, float ageInTicks, float rotationYaw, float partialTicks) {
		if (entity.deathTime > 0) {
			float f = ((float)entity.deathTime + partialTicks - 1.0F) / 20.0F * 1.6F;
			f = Mth.sqrt(f);
			if (f > 1.0F) {
				f = 1.0F;
			}

			stack.mulPose(Axis.of(entity.getDirection().step()).rotationDegrees(f * this.getFlipDegrees(entity)));
		} else if (this.isEntityUpsideDown(entity)) {
			stack.translate(0.0F, entity.getBbHeight() + 0.1F, 0.0F);
			stack.mulPose(Axis.ZP.rotationDegrees(180.0F));
		}
	}

	protected float getFlipDegrees(T entity) {
		return 90.0F;
	}

	protected boolean isBodyVisible(T entity) {
		return !entity.isInvisible();
	}

	private boolean isEntityUpsideDown(T entity) {
		if (entity.hasCustomName()) {
			String s = ChatFormatting.stripFormatting(entity.getName().getString());
			return s != null && (s.equalsIgnoreCase("dinnerbone") || s.equalsIgnoreCase("grumm"));
		}

		return false;
	}
}
