package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.entity.ModelTFHydraHead;
import twilightforest.entity.boss.EntityTFHydra;
import twilightforest.entity.boss.EntityTFHydraHead;
import twilightforest.entity.boss.HydraHeadContainer;

import javax.annotation.Nullable;

public class RenderTFHydraHead extends TFPartRenderer<EntityTFHydraHead, ModelTFHydraHead> {

	private static final ResourceLocation textureLoc = TwilightForestMod.getModelTexture("hydra4.png");


	public RenderTFHydraHead(EntityRendererManager manager) {
		super(manager, new ModelTFHydraHead());
	}

	@Override
	public void render(EntityTFHydraHead entity, float yaw, float partialTicks, MatrixStack stack, IRenderTypeBuffer buffer, int light) {
		// get the HydraHeadContainer that we're taking about
		HydraHeadContainer headCon = getHeadObject(entity);

		if (headCon != null) {
			// see whether we want to render these
			if (headCon.shouldRenderHead()) {
				stack.rotate(Vector3f.YP.rotationDegrees(-180));
				super.render(entity, yaw, partialTicks, stack, buffer, light);
			}

		} else {
			super.render(entity, yaw, partialTicks, stack, buffer, light);
		}
	}

	@Nullable
	public static HydraHeadContainer getHeadObject(EntityTFHydraHead entity) {
		EntityTFHydra hydra = entity.getParent();

		if (hydra != null) {
			for (int i = 0; i < hydra.numHeads; i++) {
				if (hydra.hc[i].headEntity == entity) {
					return hydra.hc[i];
				}
			}
		}
		return null;
	}

	@Override
	public ResourceLocation getEntityTexture(EntityTFHydraHead entity) {
		return textureLoc;
	}
}
