package twilightforest.client.renderer.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import twilightforest.TwilightForestMod;
import twilightforest.entity.boss.EntityTFHydra;
import twilightforest.entity.boss.EntityTFHydraHead;
import twilightforest.entity.boss.EntityTFHydraPart;
import twilightforest.entity.boss.HydraHeadContainer;

public class RenderTFHydraHead extends RenderLiving<EntityTFHydraHead> {
	private static final ResourceLocation textureLoc = new ResourceLocation(TwilightForestMod.MODEL_DIR + "hydra4.png");

	public RenderTFHydraHead(RenderManager manager, ModelBase modelbase, float shadowSize) {
		super(manager, modelbase, shadowSize);
	}

	@Override
	public void doRender(EntityTFHydraHead entity, double x, double y, double z, float yaw, float partialTicks) {
		// get the HydraHeadContainer that we're taking about
		HydraHeadContainer headCon = getHeadObject(entity);

		if (headCon != null) {
			// see whether we want to render these
			if (headCon.shouldRenderHead()) {
				super.doRender(entity, x, y, z, yaw, partialTicks);
			}

			if (headCon.shouldRenderNeck(0)) {
				renderManager.renderEntityStatic(headCon.necka, partialTicks, false);
			}
			if (headCon.shouldRenderNeck(1)) {
				renderManager.renderEntityStatic(headCon.neckb, partialTicks, false);
			}
			if (headCon.shouldRenderNeck(2)) {
				renderManager.renderEntityStatic(headCon.neckc, partialTicks, false);
			}
			if (headCon.shouldRenderNeck(3)) {
				renderManager.renderEntityStatic(headCon.neckd, partialTicks, false);
			}
			if (headCon.shouldRenderNeck(4)) {
				renderManager.renderEntityStatic(headCon.necke, partialTicks, false);
			}
		} else {
			super.doRender(entity, x, y, z, yaw, partialTicks);

		}
	}

	private HydraHeadContainer getHeadObject(Entity entity) {
		EntityTFHydra hydra = ((EntityTFHydraPart) entity).hydra;

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
	protected ResourceLocation getEntityTexture(EntityTFHydraHead entity) {
		return textureLoc;
	}

}
