package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.model.ListModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import com.mojang.math.Vector3f;
import twilightforest.entity.TFPart;

import javax.annotation.Nullable;

public abstract class TFPartRenderer<T extends TFPart<?>, M extends ListModel<T>> extends EntityRenderer<T> {

	protected final M entityModel;

	public TFPartRenderer(EntityRendererProvider.Context renderManager, M model) {
		super(renderManager);
		this.entityModel = model;
	}

	@Override
	public void render(T entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int light) {
		int packedLightIn = entityRenderDispatcher.getPackedLightCoords(entityIn.getParent(), partialTicks);
		super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
		matrixStackIn.pushPose();

		float f = Mth.rotLerp(partialTicks, entityIn.prevRenderYawOffset, entityIn.renderYawOffset);
		float f6 = Mth.lerp(partialTicks, entityIn.xRotO, entityIn.getXRot());

		float f7 = this.handleRotationFloat(entityIn, partialTicks);
		this.applyRotations(entityIn, matrixStackIn, f7, f, partialTicks);
		matrixStackIn.scale(-1.0F, -1.0F, 1.0F);
		matrixStackIn.translate(0.0D, -1.501F, 0.0D);
		float f8 = 0.0F;
		float f5 = 0.0F;

		this.entityModel.prepareMobModel(entityIn, f5, f8, partialTicks);
		this.entityModel.setupAnim(entityIn, f5, f8, f7, f, f6);
		Minecraft minecraft = Minecraft.getInstance();
		boolean flag = this.isVisible(entityIn);
		boolean flag1 = !flag && !entityIn.isInvisibleTo(minecraft.player);
		boolean flag2 = minecraft.shouldEntityAppearGlowing(entityIn);
		RenderType rendertype = this.getRenderType(entityIn, flag, flag1, flag2);
		if (rendertype != null) {
			VertexConsumer ivertexbuilder = bufferIn.getBuffer(rendertype);
			int i = getPackedOverlay(entityIn, this.getOverlayProgress(entityIn, partialTicks));
			this.entityModel.renderToBuffer(matrixStackIn, ivertexbuilder, packedLightIn, i, 1.0F, 1.0F, 1.0F, flag1 ? 0.15F : 1.0F);
		}

		matrixStackIn.popPose();
	}

	protected float getOverlayProgress(T livingEntityIn, float partialTicks) {
		return 0.0F;
	}

	public int getPackedOverlay(T livingEntityIn, float uIn) {
		if (livingEntityIn.getParent() instanceof LivingEntity)
			return OverlayTexture.pack(OverlayTexture.u(uIn), OverlayTexture.v(((LivingEntity) livingEntityIn.getParent()).hurtTime > 0 || ((LivingEntity) livingEntityIn.getParent()).deathTime > 0));
		return OverlayTexture.NO_OVERLAY;
	}

	@Nullable
	protected RenderType getRenderType(T p_230496_1_, boolean p_230496_2_, boolean p_230496_3_, boolean p_230496_4_) {
		ResourceLocation resourcelocation = this.getTextureLocation(p_230496_1_);
		if (p_230496_3_) {
			return RenderType.itemEntityTranslucentCull(resourcelocation);
		} else if (p_230496_2_) {
			return this.entityModel.renderType(resourcelocation);
		} else {
			return p_230496_4_ ? RenderType.outline(resourcelocation) : null;
		}
	}

	protected float handleRotationFloat(T livingBase, float partialTicks) {
		return (float)livingBase.tickCount + partialTicks;
	}

	protected void applyRotations(T entityLiving, PoseStack matrixStackIn, float ageInTicks, float rotationYaw, float partialTicks) {
		if (entityLiving.deathTime > 0) {
			float f = ((float)entityLiving.deathTime + partialTicks - 1.0F) / 20.0F * 1.6F;
			f = Mth.sqrt(f);
			if (f > 1.0F) {
				f = 1.0F;
			}

			matrixStackIn.mulPose(Vector3f.ZP.rotationDegrees(f * this.getDeathMaxRotation(entityLiving)));
		}

	}

	protected float getDeathMaxRotation(T entityLivingBaseIn) {
		return 90.0F;
	}

	protected boolean isVisible(T livingEntityIn) {
		return !livingEntityIn.isInvisible();
	}
}
