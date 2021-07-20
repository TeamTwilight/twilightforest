package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;
import twilightforest.entity.TFPartEntity;

import javax.annotation.Nullable;

public abstract class TFPartRenderer<T extends TFPartEntity<?>, M extends SegmentedModel<T>> extends EntityRenderer<T> {

	protected final M entityModel;

	public TFPartRenderer(EntityRendererManager renderManager, M model) {
		super(renderManager);
		this.entityModel = model;
	}

	@Override
	public void render(T entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int light) {
		int packedLightIn = renderManager.getPackedLight(entityIn.getParent(), partialTicks);
		super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
		matrixStackIn.push();

		float f = MathHelper.interpolateAngle(partialTicks, entityIn.prevRenderYawOffset, entityIn.renderYawOffset);
		float f6 = MathHelper.lerp(partialTicks, entityIn.prevRotationPitch, entityIn.rotationPitch);

		float f7 = this.handleRotationFloat(entityIn, partialTicks);
		this.applyRotations(entityIn, matrixStackIn, f7, f, partialTicks);
		matrixStackIn.scale(-1.0F, -1.0F, 1.0F);
		matrixStackIn.translate(0.0D, -1.501F, 0.0D);
		float f8 = 0.0F;
		float f5 = 0.0F;

		this.entityModel.setLivingAnimations(entityIn, f5, f8, partialTicks);
		this.entityModel.setRotationAngles(entityIn, f5, f8, f7, f, f6);
		Minecraft minecraft = Minecraft.getInstance();
		boolean flag = this.isVisible(entityIn);
		boolean flag1 = !flag && !entityIn.isInvisibleToPlayer(minecraft.player);
		boolean flag2 = minecraft.isEntityGlowing(entityIn);
		RenderType rendertype = this.getRenderType(entityIn, flag, flag1, flag2);
		if (rendertype != null) {
			IVertexBuilder ivertexbuilder = bufferIn.getBuffer(rendertype);
			int i = getPackedOverlay(entityIn, this.getOverlayProgress(entityIn, partialTicks));
			this.entityModel.render(matrixStackIn, ivertexbuilder, packedLightIn, i, 1.0F, 1.0F, 1.0F, flag1 ? 0.15F : 1.0F);
		}

		matrixStackIn.pop();
	}

	protected float getOverlayProgress(T livingEntityIn, float partialTicks) {
		return 0.0F;
	}

	public int getPackedOverlay(T livingEntityIn, float uIn) {
		if (livingEntityIn.getParent() instanceof LivingEntity)
			return OverlayTexture.getPackedUV(OverlayTexture.getU(uIn), OverlayTexture.getV(((LivingEntity) livingEntityIn.getParent()).hurtTime > 0 || ((LivingEntity) livingEntityIn.getParent()).deathTime > 0));
		return OverlayTexture.NO_OVERLAY;
	}

	@Nullable
	protected RenderType getRenderType(T p_230496_1_, boolean p_230496_2_, boolean p_230496_3_, boolean p_230496_4_) {
		ResourceLocation resourcelocation = this.getEntityTexture(p_230496_1_);
		if (p_230496_3_) {
			return RenderType.getItemEntityTranslucentCull(resourcelocation);
		} else if (p_230496_2_) {
			return this.entityModel.getRenderType(resourcelocation);
		} else {
			return p_230496_4_ ? RenderType.getOutline(resourcelocation) : null;
		}
	}

	protected float handleRotationFloat(T livingBase, float partialTicks) {
		return (float)livingBase.ticksExisted + partialTicks;
	}

	protected void applyRotations(T entityLiving, MatrixStack matrixStackIn, float ageInTicks, float rotationYaw, float partialTicks) {
		if (entityLiving.deathTime > 0) {
			float f = ((float)entityLiving.deathTime + partialTicks - 1.0F) / 20.0F * 1.6F;
			f = MathHelper.sqrt(f);
			if (f > 1.0F) {
				f = 1.0F;
			}

			matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(f * this.getDeathMaxRotation(entityLiving)));
		}

	}

	protected float getDeathMaxRotation(T entityLivingBaseIn) {
		return 90.0F;
	}

	protected boolean isVisible(T livingEntityIn) {
		return !livingEntityIn.isInvisible();
	}
}
