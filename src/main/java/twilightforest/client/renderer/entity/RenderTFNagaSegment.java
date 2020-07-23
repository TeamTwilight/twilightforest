package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.entity.ModelTFNaga;
import twilightforest.entity.boss.EntityTFNagaSegment;

public class RenderTFNagaSegment<T extends EntityTFNagaSegment> extends EntityRenderer<T> {
	private static final ResourceLocation part_TextureLoc = TwilightForestMod.getModelTexture("nagasegment.png");

	private final Model segmentModel = new ModelTFNaga<>();

	public RenderTFNagaSegment(EntityRendererManager m) {
		super(m);
	}

	@Override
	public void render(T entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
		if(!entityIn.isInvisible()) {
			matrixStackIn.push();
			IVertexBuilder ivertexbuilder = bufferIn.getBuffer(this.segmentModel.getRenderType(part_TextureLoc));

			float yawDiff = entityIn.rotationYaw - entityIn.prevRotationYaw;
			if (yawDiff > 180) {
				yawDiff -= 360;
			} else if (yawDiff < -180) {
				yawDiff += 360;
			}
			float yaw2 = entityIn.prevRotationYaw + yawDiff * partialTicks;

			matrixStackIn.rotate(Vector3f.YP.rotationDegrees(yaw2));
			matrixStackIn.rotate(Vector3f.XP.rotationDegrees(entityIn.rotationPitch));

			matrixStackIn.scale(-1.0F, -1.0F, 1.0F);
			matrixStackIn.scale(2.0F, 2.0F, 2.0F);
			this.segmentModel.render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
			super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
			matrixStackIn.pop();
		}
	}

	@Override
	public ResourceLocation getEntityTexture(T entity) {
		return part_TextureLoc;
	}
}
